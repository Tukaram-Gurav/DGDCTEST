package com.cwms.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.cwms.entities.CFSTariffRange;
import com.cwms.entities.CFSTariffService;
import com.cwms.entities.InvoiceDetail;
import com.cwms.entities.InvoiceMain;
import com.cwms.entities.Party;
import com.cwms.entities.ProformaDetail;
import com.cwms.entities.Services;
import com.cwms.repository.InvoiceDetailRepostary;
import com.cwms.repository.InvoiceRepositary;
import com.cwms.repository.PartyRepository;
import com.cwms.repository.ProformaDetailRepostary;
import com.itextpdf.text.Image;

@Service
public class ProformaDetailServiceIMPL {

//	@Autowired
//	private InvoiceRepositary invoiceRepositary;

	@Autowired
	private ProformaDetailRepostary detailRepostary;

	@Autowired
	public cfsTarrifServiceService cfsTarrifServiceService;
	
	@Autowired
	public InvoiceRepositary invoiceRepository;

	@Autowired
	private PartyRepository partyrepo;
	
	@Autowired
	public CFSTariffRangeService CFSTariffRangeService;

	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	public ServicesInterface ServicesInterface;

	public ProformaDetail addInvoiceDetail(String companyId, String branchId, String Invoiceno, String tarrifno,
			String amndno, String serviceId, String partyId, int noOfPackages, String user ,Date invoiceDate) {
		Services servicesById = ServicesInterface.getServicesById(companyId, branchId, serviceId);
		String rangeType = servicesById.getRateCalculation();
		String taxApplicable = servicesById.getTaxApplicable();
		String serviceUnit = servicesById.getServiceUnit();
		double totalPackageDecimal = (double) noOfPackages;
		double rate = 0.0;
		String currencyId = "INR";
		double TaxPercentage = 0.0;
		double billAmount = 0.0;
		double taxamount = 0.0;
		double totalAmount = 0.0;

		if (taxApplicable != null && taxApplicable.equals("Y")) {
			TaxPercentage = Double.parseDouble(servicesById.getTaxPercentage());
		}

//		If rate type is Plain

		if ("Plain".equals(rangeType)) {
			CFSTariffService cfsservice = cfsTarrifServiceService.findByTarrifNoandServiceIdPartyId(companyId, branchId,
					tarrifno, amndno, serviceId, partyId);

			currencyId = cfsservice.getCurrencyId();
			rate = cfsservice.getRate();

			if (TaxPercentage != 0.0) {
				taxamount = rate * (TaxPercentage / 100.0);
				billAmount = totalPackageDecimal * rate;
				totalAmount = billAmount + taxamount;
			} else {
				billAmount = rate * totalPackageDecimal;
				totalAmount = billAmount;
			}

		}
//		If rate type is Range or Slab 
		else {

			List<CFSTariffRange> tariffRanges = CFSTariffRangeService
					.findByCfsTariffNoAndCfsAmndNoAndServiceIdAndPartyId(companyId, branchId, tarrifno, amndno,
							serviceId, partyId);

			if (!tariffRanges.isEmpty()) {
				currencyId = tariffRanges.get(0).getCurrencyId();
			}

			 // Sort the tariffRanges by rangeFrom in ascending order
		    tariffRanges.sort(Comparator.comparingDouble(CFSTariffRange::getRangeFrom));
			for (CFSTariffRange tariffRange : tariffRanges) {
				double rangeFrom = tariffRange.getRangeFrom();
				double rangeTo = tariffRange.getRangeTo();
				double rangeRate = tariffRange.getRangeRate();
//				double ratePlain = 

//				If rate type is Range
				
				if ("Range".equals(rangeType)) {					
					

					if (totalPackageDecimal >= rangeFrom && totalPackageDecimal <= rangeTo) {
						rate = rangeRate;
						
						if (TaxPercentage != 0.0) {
							taxamount = rate * (TaxPercentage / 100.0);
												
//							if("Per Kg".equals(serviceUnit))
//							{
								billAmount = rate;
//							}
//							else
//							{
//								billAmount = totalPackageDecimal * rate;
//							}

							totalAmount = billAmount + taxamount;
							
						} else {
							billAmount = rate;
							totalAmount = billAmount;
						}
						
						
						break;
					}
				

				}

//				If rate type is Slab
				if ("Slab".equals(rangeType)) {					
//									
					if (totalPackageDecimal <= 0) {
						break; // No need to continue if we've accounted for the entire totalPackage
					}

					if (totalPackageDecimal >= (rangeTo - rangeFrom)) {
						// If remainingPackage is greater than or equal to the entire range, calculate
						// rate
						rate += (rangeTo - rangeFrom) * rangeRate;		
						totalPackageDecimal -= (int) (rangeTo - rangeFrom);
					} else {
						rate += totalPackageDecimal * rangeRate;
						totalPackageDecimal = 0.0;
					}
					
					
					if (TaxPercentage != 0.0) {
						taxamount = rate * (TaxPercentage / 100.0);
																		
						billAmount = rate;
											
						totalAmount = billAmount + taxamount;
						
					} else {
						billAmount = rate;
						totalAmount = rate;
					}	
					
				}

			}

		}

		
		
		
		
		
//		System.out.println("Saving    ");
//		System.out.println("billAmount    "+billAmount);
//		System.out.println("totalAmount    "+totalAmount);
		
		
		
		ProformaDetail detail = new ProformaDetail(companyId, branchId, Invoiceno, partyId, serviceId,invoiceDate,
				serviceUnit, serviceUnit, serviceUnit, rate, currencyId, TaxPercentage, taxamount, billAmount,
				totalAmount, "A", user, new Date(), user, new Date(), user, new Date());

		return detailRepostary.save(detail);
	}
	
	
//	Range Service Heavy Weight
	
	
	public ProformaDetail addInvoiceDetailHeavyWeight(String companyId, String branchId, String Invoiceno, String tarrifno,
			String amndno, String serviceId, String partyId, int noOfPackages, String user ,Date invoiceDate,List<BigDecimal> weights) {
		Services servicesById = ServicesInterface.getServicesById(companyId, branchId, serviceId);
		String rangeType = servicesById.getRateCalculation();
		String taxApplicable = servicesById.getTaxApplicable();
		String serviceUnit = servicesById.getServiceUnit();
		double totalPackageDecimal = (double) noOfPackages;
		double rate = 0.0;
		String currencyId = "INR";
		double TaxPercentage = 0.0;
		double billAmount = 0.0;
		double taxamount = 0.0;
		double totalAmount = 0.0;

		if (taxApplicable != null && taxApplicable.equals("Y")) {
			TaxPercentage = Double.parseDouble(servicesById.getTaxPercentage());
		}


//		If rate type is Range or Slab 
		

			List<CFSTariffRange> tariffRanges = CFSTariffRangeService
					.findByCfsTariffNoAndCfsAmndNoAndServiceIdAndPartyId(companyId, branchId, tarrifno, amndno,
							serviceId, partyId);

			if (!tariffRanges.isEmpty()) {
				currencyId = tariffRanges.get(0).getCurrencyId();
			}

			 // Sort the tariffRanges by rangeFrom in ascending order
		    tariffRanges.sort(Comparator.comparingDouble(CFSTariffRange::getRangeFrom));
			for (CFSTariffRange tariffRange : tariffRanges) {
				double rangeFrom = tariffRange.getRangeFrom();
				double rangeTo = tariffRange.getRangeTo();
				double rangeRate = tariffRange.getRangeRate();
				
								
				for (BigDecimal heavy : weights) {
					
					double hpWeightValue = heavy.doubleValue();
									
					
					if (hpWeightValue >= rangeFrom && hpWeightValue <= rangeTo) {
						rate = rangeRate;
						
						if (TaxPercentage != 0.0) {
							taxamount += rate * (TaxPercentage / 100.0);
														
							billAmount += rate;				

							totalAmount += rate + taxamount;
							
						} else {
							billAmount += rate;
							totalAmount += rate;
						}					
						
//						break;
					}				

				}	
							
				
			}

		

			ProformaDetail detail = new ProformaDetail(companyId, branchId, Invoiceno, partyId, serviceId,invoiceDate,
				serviceUnit, serviceUnit, serviceUnit, rate, currencyId, TaxPercentage, taxamount, billAmount,
				totalAmount, "A", user, new Date(), user, new Date(), user, new Date());

		return detailRepostary.save(detail);
	}

	
	
	
	public List<ProformaDetail> getByInvoiceNo(String companyId,String branchId,String invoiceNo)
	{
		return detailRepostary.findByCompanyIdAndBranchIdAndProformaNo(companyId, branchId, invoiceNo);
	}
	
	
		 

}
