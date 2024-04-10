package com.cwms.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cwms.entities.CFSTariffRange;
import com.cwms.entities.CFSTariffService;
import com.cwms.entities.CFSTarrifExtendedValues;
import com.cwms.entities.CfsTarrif;
import com.cwms.entities.InvoiceDetail;
import com.cwms.entities.InvoiceDetailSHB;
import com.cwms.entities.Services;
import com.cwms.repository.CFSTarrifExtendedValuesRepositary;
import com.cwms.service.CFSTariffRangeService;
import com.cwms.service.ServicesInterface;
import com.cwms.service.cfsTarrifServiceService;

@Component
public class InvoiceServiceImplSHB 
{
	
	@Autowired
	public CFSTarrifExtendedValuesRepositary extendedRepo;
	
	@Autowired
	public cfsTarrifServiceService cfsTarrifServiceService;
	
	@Autowired
	public CFSTariffRangeService CFSTariffRangeService;

	@Autowired
	public ServicesInterface ServicesInterface;
	
	
	public InvoiceDetailSHB calculateByPrice(String companyId,String branchId,String user,String billingPartyId,String tarrifPartyId,CfsTarrif tarrif,String serviceId,BigDecimal executionUnit)
	{		
		Services servicesById = ServicesInterface.getServicesById(companyId, branchId, serviceId);
		String rangeType = servicesById.getRateCalculation();
		String taxApplicable = servicesById.getTaxApplicable();
		String serviceUnit = servicesById.getServiceUnit();
		String currencyId = "INR";
		 BigDecimal rate = BigDecimal.ZERO;
		   BigDecimal TaxPercentage = BigDecimal.ZERO;
	        BigDecimal billAmount = BigDecimal.ZERO;
	        BigDecimal taxAmount = BigDecimal.ZERO;
	        BigDecimal totalAmount = BigDecimal.ZERO;
		
		if (taxApplicable != null && taxApplicable.equals("Y")) {
			 TaxPercentage = new BigDecimal(servicesById.getTaxPercentage());
		}
		
		if ("Plain".equals(servicesById.getRateCalculation())) {
			CFSTariffService cfsservice = cfsTarrifServiceService.findByTarrifNoandServiceIdPartyId(companyId, branchId,tarrif.getCfsTariffNo(), tarrif.getCfsAmndNo(), serviceId, tarrifPartyId);
			
			rate =  BigDecimal.valueOf(cfsservice.getRate());

			if (!TaxPercentage.equals(BigDecimal.ZERO)) {
	            // Calculate tax amount
	            taxAmount = rate.multiply(TaxPercentage.divide(BigDecimal.valueOf(100.0)));
	            // Calculate bill amount
	            billAmount = executionUnit.multiply(rate);
	            // Calculate total amount
	            totalAmount = billAmount.add(taxAmount);
	        } else {
	            // Calculate bill amount when tax is not applicable
	            billAmount = executionUnit.multiply(rate);
	            totalAmount = billAmount;
	        }

		}
//		If rate type is Range or Slab 
		else {

			List<CFSTariffRange> tariffRanges = CFSTariffRangeService.findByCfsTariffNoAndCfsAmndNoAndServiceIdAndPartyId(companyId, branchId, tarrif.getCfsTariffNo(), tarrif.getCfsAmndNo(),serviceId, tarrifPartyId);

			tariffRanges.sort(Comparator.comparingDouble(CFSTariffRange::getRangeFrom));
			BigDecimal highestRangeTo = BigDecimal.valueOf(Double.MIN_VALUE); 
			BigDecimal highestRate = BigDecimal.valueOf(Double.MIN_VALUE); 
			for (CFSTariffRange tariffRange : tariffRanges) {
			    BigDecimal rangeTo = BigDecimal.valueOf(tariffRange.getRangeTo());
			    BigDecimal Rate = BigDecimal.valueOf(tariffRange.getRangeRate());
			    if (Rate.compareTo(highestRate) > 0) {
			    	highestRate = Rate;
			    }
			    if (rangeTo.compareTo(highestRangeTo) > 0) {
			        highestRangeTo = rangeTo;
			    }
			}
			
			
			for (CFSTariffRange tariffRange : tariffRanges) {
					BigDecimal rangeFrom = BigDecimal.valueOf(tariffRange.getRangeFrom());
		            BigDecimal rangeTo = BigDecimal.valueOf(tariffRange.getRangeTo());
		            BigDecimal rangeRate = BigDecimal.valueOf(tariffRange.getRangeRate());

				if ("Range".equals(rangeType)) {

					if (executionUnit.compareTo(rangeFrom) >= 0 && 	executionUnit.compareTo(rangeTo) <= 0) {		                    
		                    rate = rangeRate;
		                    if (!TaxPercentage.equals(BigDecimal.ZERO)) {
		                        taxAmount = rate.multiply(TaxPercentage.divide(BigDecimal.valueOf(100.0)));
		                        billAmount = rate;
		                        totalAmount = billAmount.add(taxAmount);
		                    } else {
		                        billAmount = rate;
		                        totalAmount = rate;
		                    }
		                    break;
		                  }else if (executionUnit.compareTo(highestRangeTo) > 0) {

		                	  CFSTarrifExtendedValues extendedvalues = extendedRepo.findByCompanyIdAndBranchIdAndCfsTariffNoAndCfsAmndNoAndServiceIdAndPartyId(companyId, branchId,tarrif.getCfsTariffNo(), tarrif.getCfsAmndNo(), serviceId, tarrifPartyId);
		                	  BigDecimal subtract = executionUnit.subtract(highestRangeTo);
		                	  int scale = 5;		                	  
		                	  BigDecimal divide = subtract.divide(BigDecimal.valueOf(extendedvalues.getExtendedRange()), scale, RoundingMode.UP);		                	  
		                	  BigDecimal nextWholeNumber = divide.setScale(0, RoundingMode.UP);
		                	  BigDecimal multiply = nextWholeNumber.multiply(BigDecimal.valueOf(extendedvalues.getAmountAddition()));
		 
		                	  rate = highestRate.add(multiply);
		                	
		                	  if (!TaxPercentage.equals(BigDecimal.ZERO)) {
			                        taxAmount = rate.multiply(TaxPercentage.divide(BigDecimal.valueOf(100.0)));
			                        billAmount = rate;
			                        totalAmount = billAmount.add(taxAmount);
			                    } else {
			                        billAmount = rate;
			                        totalAmount = rate;
			                    }
		                	 
		                	  break;
		                  }

				}

//				If rate type is Slab
				  if ("Slab".equals(rangeType)) {
		                if (executionUnit.compareTo(BigDecimal.ZERO) <= 0) {
		                    break; // No need to continue if we've accounted for the entire totalPackage
		                }
		                if (executionUnit.compareTo(rangeTo.subtract(rangeFrom)) >= 0) {
		                    // If remainingPackage is greater than or equal to the entire range, calculate rate
		                    rate = rate.add(rangeTo.subtract(rangeFrom).multiply(rangeRate));
		                    executionUnit = executionUnit.subtract(rangeTo.subtract(rangeFrom));
		                } else {
		                    rate = rate.add(executionUnit.multiply(rangeRate));
		                    executionUnit = BigDecimal.ZERO;
		                }
		                if (!TaxPercentage.equals(BigDecimal.ZERO)) {
		                    taxAmount = rate.multiply(TaxPercentage.divide(BigDecimal.valueOf(100.0)));
		                    billAmount = rate;
		                    totalAmount = billAmount.add(taxAmount);
		                } else {
		                    billAmount = rate;
		                    totalAmount = rate;
		                }
		            }

			}
		}
		
		
		
		
		InvoiceDetailSHB detail = new InvoiceDetailSHB(companyId, branchId, billingPartyId, serviceId, new Date(),
				executionUnit.toString(), serviceUnit, executionUnit.toString(), rate, currencyId, TaxPercentage, taxAmount, billAmount,
				totalAmount, "A", user, new Date(), user, new Date(), user, new Date());
		
		return detail;
		
		
	}
	
	
	
//	CALCULATE INVOICE AT  7 PM
	
//	public InvoiceDetailSHB calculateByPrice2(String companyId,String branchId,String user,String billingPartyId,String tarrifPartyId,CfsTarrif tarrif,String serviceId,BigDecimal executionUnit)
//	{		
//		Services servicesById = ServicesInterface.getServicesById(companyId, branchId, serviceId);
//		String rangeType = servicesById.getRateCalculation();
//		String taxApplicable = servicesById.getTaxApplicable();
//		String serviceUnit = servicesById.getServiceUnit();
//		String currencyId = "INR";
//		 BigDecimal rate = BigDecimal.ZERO;
//		   BigDecimal TaxPercentage = BigDecimal.ZERO;
//	        BigDecimal billAmount = BigDecimal.ZERO;
//	        BigDecimal taxAmount = BigDecimal.ZERO;
//	        BigDecimal totalAmount = BigDecimal.ZERO;
//		
//		if (taxApplicable != null && taxApplicable.equals("Y")) {
//			 TaxPercentage = new BigDecimal(servicesById.getTaxPercentage());
//		}
//		
//		if ("Plain".equals(servicesById.getRateCalculation())) {
//			CFSTariffService cfsservice = cfsTarrifServiceService.findByTarrifNoandServiceIdPartyId(companyId, branchId,tarrif.getCfsTariffNo(), tarrif.getCfsAmndNo(), serviceId, tarrifPartyId);
//			
//			rate =  BigDecimal.valueOf(cfsservice.getRate());
//
//			if (!TaxPercentage.equals(BigDecimal.ZERO)) {
//	            // Calculate tax amount
//	            taxAmount = rate.multiply(TaxPercentage.divide(BigDecimal.valueOf(100.0)));
//	            // Calculate bill amount
//	            billAmount = executionUnit.multiply(rate);
//	            // Calculate total amount
//	            totalAmount = billAmount.add(taxAmount);
//	        } else {
//	            // Calculate bill amount when tax is not applicable
//	            billAmount = executionUnit.multiply(rate);
//	            totalAmount = billAmount;
//	        }
//
//		}
////		If rate type is Range or Slab 
//		else {
//
//			List<CFSTariffRange> tariffRanges = CFSTariffRangeService.findByCfsTariffNoAndCfsAmndNoAndServiceIdAndPartyId(companyId, branchId, tarrif.getCfsTariffNo(), tarrif.getCfsAmndNo(),serviceId, tarrifPartyId);
//
//			tariffRanges.sort(Comparator.comparingDouble(CFSTariffRange::getRangeFrom));
//			BigDecimal highestRangeTo = BigDecimal.valueOf(Double.MIN_VALUE); 
//			BigDecimal highestRate = BigDecimal.valueOf(Double.MIN_VALUE); 
//			for (CFSTariffRange tariffRange : tariffRanges) {
//			    BigDecimal rangeTo = BigDecimal.valueOf(tariffRange.getRangeTo());
//			    BigDecimal Rate = BigDecimal.valueOf(tariffRange.getRangeRate());
//			    if (Rate.compareTo(highestRate) > 0) {
//			    	highestRate = Rate;
//			    }
//			    if (rangeTo.compareTo(highestRangeTo) > 0) {
//			        highestRangeTo = rangeTo;
//			    }
//			}
//			
//			
//			for (CFSTariffRange tariffRange : tariffRanges) {
//					BigDecimal rangeFrom = BigDecimal.valueOf(tariffRange.getRangeFrom());
//		            BigDecimal rangeTo = BigDecimal.valueOf(tariffRange.getRangeTo());
//		            BigDecimal rangeRate = BigDecimal.valueOf(tariffRange.getRangeRate());
//
//				if ("Range".equals(rangeType)) {
//
//					if (executionUnit.compareTo(rangeFrom) >= 0 && 	executionUnit.compareTo(rangeTo) <= 0) {		                    
//		                    rate = rangeRate;
//		                    if (!TaxPercentage.equals(BigDecimal.ZERO)) {
//		                        taxAmount = rate.multiply(TaxPercentage.divide(BigDecimal.valueOf(100.0)));
//		                        billAmount = rate;
//		                        totalAmount = billAmount.add(taxAmount);
//		                    } else {
//		                        billAmount = rate;
//		                        totalAmount = rate;
//		                    }
//		                    break;
//		                  }else if (executionUnit.compareTo(highestRangeTo) > 0) {
//
//		                	  CFSTarrifExtendedValues extendedvalues = extendedRepo.findByCompanyIdAndBranchIdAndCfsTariffNoAndCfsAmndNoAndServiceIdAndPartyId(companyId, branchId,tarrif.getCfsTariffNo(), tarrif.getCfsAmndNo(), serviceId, tarrifPartyId);
//		                	  BigDecimal subtract = executionUnit.subtract(highestRangeTo);
//		                	  int scale = 5;		                	  
//		                	  BigDecimal divide = subtract.divide(BigDecimal.valueOf(extendedvalues.getExtendedRange()), scale, RoundingMode.UP);		                	  
//		                	  BigDecimal nextWholeNumber = divide.setScale(0, RoundingMode.UP);
//		                	  BigDecimal multiply = nextWholeNumber.multiply(BigDecimal.valueOf(extendedvalues.getAmountAddition()));
//		 
//		                	  rate = highestRate.add(multiply);
//		                	
//		                	  if (!TaxPercentage.equals(BigDecimal.ZERO)) {
//			                        taxAmount = rate.multiply(TaxPercentage.divide(BigDecimal.valueOf(100.0)));
//			                        billAmount = rate;
//			                        totalAmount = billAmount.add(taxAmount);
//			                    } else {
//			                        billAmount = rate;
//			                        totalAmount = rate;
//			                    }
//		                	 
//		                	  break;
//		                  }
//
//				}
//
////				If rate type is Slab
//				  if ("Slab".equals(rangeType)) {
//		                if (executionUnit.compareTo(BigDecimal.ZERO) <= 0) {
//		                    break; // No need to continue if we've accounted for the entire totalPackage
//		                }
//		                if (executionUnit.compareTo(rangeTo.subtract(rangeFrom)) >= 0) {
//		                    // If remainingPackage is greater than or equal to the entire range, calculate rate
//		                    rate = rate.add(rangeTo.subtract(rangeFrom).multiply(rangeRate));
//		                    executionUnit = executionUnit.subtract(rangeTo.subtract(rangeFrom));
//		                } else {
//		                    rate = rate.add(executionUnit.multiply(rangeRate));
//		                    executionUnit = BigDecimal.ZERO;
//		                }
//		                if (!TaxPercentage.equals(BigDecimal.ZERO)) {
//		                    taxAmount = rate.multiply(TaxPercentage.divide(BigDecimal.valueOf(100.0)));
//		                    billAmount = rate;
//		                    totalAmount = billAmount.add(taxAmount);
//		                } else {
//		                    billAmount = rate;
//		                    totalAmount = rate;
//		                }
//		            }
//
//			}
//		}
//		
//		
//		
//		
//		InvoiceDetailSHB detail = new InvoiceDetailSHB(companyId, branchId, billingPartyId, serviceId, new Date(),
//				executionUnit.toString(), serviceUnit, executionUnit.toString(), rate, currencyId, TaxPercentage, taxAmount, billAmount,
//				totalAmount, "A", user, new Date(), user, new Date(), user, new Date());
//		
//		
//		return detail;
//		
//		
//	}
//	
//	
	
	
}
