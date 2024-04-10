package com.cwms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cwms.helper.InvoiceServiceImplSHB;
import com.cwms.entities.Branch;
import com.cwms.entities.CfsTarrif;
import com.cwms.entities.ExportSHB;
import com.cwms.entities.Import;
import com.cwms.entities.InvoiceDetail;
import com.cwms.entities.InvoiceDetailSHB;
import com.cwms.entities.InvoiceMainSHB;
import com.cwms.entities.InvoiceTaxDetailsSHB;
import com.cwms.entities.Party;
import com.cwms.invoice.InvoiceDataStorage;
import com.cwms.invoice.ServiceIdMappingRepositary;
import com.cwms.repository.BranchRepo;
import com.cwms.repository.ExportShbRepo;
import com.cwms.repository.ImportRepo;
import com.cwms.repository.InvoiceDetailSHBRepo;
import com.cwms.repository.InvoiceMainSGHBRepo;
import com.cwms.repository.InvoiceTaxDetailsSHBRepo;
import com.cwms.repository.PartyRepository;

@Service
public class InvoiceService {
	@Autowired
	private InvoiceServiceImplSHB InvoiceServiceImplSHB;

	@Autowired
	public ImportRepo importRepo;

	@Autowired
	private ExportShbRepo exportShbRepo;

	@Autowired
	private PartyService PartyService;

	@Autowired
	public CFSService CFSService;

	@Autowired
	private ServiceIdMappingRepositary ServiceIdMappingRepositary;

	@Autowired
	public cfsTarrifServiceService cfsTarrifServiceService;

	@Autowired
	public CFSTariffRangeService CFSTariffRangeService;

	@Autowired
	public InvoiceDetailSHBRepo detailRepo;

	@Autowired
	public InvoiceTaxDetailsSHBRepo taxRepo;

	@Autowired
	public InvoiceMainSGHBRepo mainRepo;

	@Autowired
	private BranchRepo branchRepo;
	
	
	@Autowired
	private ProcessNextIdService proceess;
	

//	To show a predictable Invoices
	public List<InvoiceDataStorage> calculatePreInvoiceShow(String companyid, String branchId, Party Party) {

		String importserviceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId, "import pckgs");

		String heavyServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId, "import HP");

		String exportserviceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId, "export pckgs");

		String demurageId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId, "export DM");

		String documentServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,
				"Document Charges");

		String partyId = Party.getPartyId();
		String billingParty = partyId;

		CfsTarrif finalCfsTarrif;

		String tarrifParty;

		CfsTarrif tarrifByPartyId = CFSService.getBypartyId(companyid, branchId, billingParty);

		if (tarrifByPartyId != null) {
			finalCfsTarrif = tarrifByPartyId;
			tarrifParty = tarrifByPartyId.getPartyId();
		} else {
			CfsTarrif findByAll = CFSService.getBypartyId(companyid, branchId, "ALL");
			finalCfsTarrif = findByAll;
			tarrifParty = findByAll.getPartyId();
		}
		;

		Date invoiceDate = Party.getLastInVoiceDate();

		List<Import> Imports = importRepo.findImportAllDataBill(companyid, branchId, billingParty, invoiceDate);

		List<ExportSHB> Exports = exportShbRepo.findExportAllDataBill(companyid, branchId, billingParty, invoiceDate);

		if (Imports.isEmpty() && Exports.isEmpty()) {
			return null;
		}

		else {

			List<InvoiceDataStorage> invoices = new ArrayList<>();

			Imports.forEach(importObj -> {

				InvoiceDetailSHB importRateByPrice = InvoiceServiceImplSHB.calculateByPrice(companyid, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, importserviceId, importObj.getAssessableValue());

				InvoiceDetailSHB importRateByWeight = InvoiceServiceImplSHB.calculateByPrice(companyid, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, heavyServiceId, importObj.getGrossWeightInCarats());

				BigDecimal demurageDays = BigDecimal
						.valueOf((importObj.getOutDate().getTime() - importObj.getSeepzInDate().getTime())
								/ (1000 * 60 * 60 * 24));

				InvoiceDetailSHB importRateByDemurage = InvoiceServiceImplSHB.calculateByPrice(companyid, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, demurageId, demurageDays);

				InvoiceDetailSHB importDocumentCharges = InvoiceServiceImplSHB.calculateByPrice(companyid, branchId,
						"A", billingParty, tarrifParty, finalCfsTarrif, documentServiceId,
						BigDecimal.valueOf(importObj.getNop()));

				BigDecimal loopBillAmount = importRateByPrice.getBillAmount().add(importRateByWeight.getBillAmount())
						.add(importDocumentCharges.getBillAmount()).add(importRateByDemurage.getBillAmount());

				BigDecimal loopTaxAmount = importRateByPrice.getTaxAmount().add(importRateByWeight.getTaxAmount())
						.add(importDocumentCharges.getTaxAmount()).add(importRateByDemurage.getTaxAmount());

				BigDecimal loopTotalInvoiceAmount = importRateByPrice.getTotalInvoiceAmount()
						.add(importRateByWeight.getTotalInvoiceAmount())
						.add(importDocumentCharges.getTotalInvoiceAmount())
						.add(importRateByDemurage.getTotalInvoiceAmount());

				InvoiceDataStorage invoiceStorage = new InvoiceDataStorage();
				invoiceStorage.setCompanyId(companyid);
				invoiceStorage.setBranchId(branchId);
				invoiceStorage.setImporterId(partyId);
				invoiceStorage.setImporterName(Party.getPartyName());
				invoiceStorage.setSirNo(importObj.getSirNo());
				invoiceStorage.setInDate(importObj.getSeepzInDate());
				invoiceStorage.setOutDate(importObj.getOutDate());
				invoiceStorage.setDocumentCharges(importDocumentCharges.getTotalInvoiceAmount());
				invoiceStorage.setRateByPrice(importRateByPrice.getTotalInvoiceAmount());
				invoiceStorage.setPrice(importObj.getAssessableValue());
				invoiceStorage.setRateByWeight(importRateByWeight.getTotalInvoiceAmount());
				invoiceStorage.setWeight(importObj.getGrossWeightInCarats());
				invoiceStorage.setDemurageCharges(importRateByDemurage.getTotalInvoiceAmount());
				invoiceStorage.setStockDays((importObj.getOutDate().getTime() - importObj.getSeepzInDate().getTime())
						/ (1000 * 60 * 60 * 24));
				invoiceStorage.setBillAmount(loopBillAmount);
				invoiceStorage.setTaxAmount(loopTaxAmount);
				invoiceStorage.setTotalInvoiceAmount(loopTotalInvoiceAmount);
				invoiceStorage.setNop(importObj.getNoOfParcel());
				invoiceStorage.setNoPackates(importObj.getNop());
				invoiceStorage.setChaName(importObj.getChaName());
				invoiceStorage.setConsoleName(importObj.getConsoleName());
				invoiceStorage.setMawb(importObj.getMawb());
				invoiceStorage.setHawb(importObj.getHawb());
				invoiceStorage.setType("Imp");

				invoices.add(invoiceStorage);
			});

			Exports.forEach(exportObj -> {
				InvoiceDetailSHB exportRateByPrice = InvoiceServiceImplSHB.calculateByPrice(companyid, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, exportserviceId, exportObj.getFobValueInINR());

				InvoiceDetailSHB exportRateByWeight = InvoiceServiceImplSHB.calculateByPrice(companyid, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, heavyServiceId, exportObj.getGrossWeightInCarats());

				BigDecimal demurageDays = BigDecimal
						.valueOf((exportObj.getOutDate().getTime() - exportObj.getSeepzInDate().getTime())
								/ (1000 * 60 * 60 * 24));

				InvoiceDetailSHB exportRateByDemurage = InvoiceServiceImplSHB.calculateByPrice(companyid, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, demurageId, demurageDays);

				InvoiceDetailSHB exportDocumentCharges = InvoiceServiceImplSHB.calculateByPrice(companyid, branchId,
						"A", billingParty, tarrifParty, finalCfsTarrif, demurageId,
						BigDecimal.valueOf(exportObj.getNoOfParcel()));

				BigDecimal loopBillAmount = exportRateByPrice.getBillAmount().add(exportRateByWeight.getBillAmount())
						.add(exportDocumentCharges.getBillAmount()).add(exportRateByDemurage.getBillAmount());

				BigDecimal loopTaxAmount = exportRateByPrice.getTaxAmount().add(exportRateByWeight.getTaxAmount())
						.add(exportDocumentCharges.getTaxAmount()).add(exportRateByDemurage.getTaxAmount());

				BigDecimal loopTotalInvoiceAmount = exportRateByPrice.getTotalInvoiceAmount()
						.add(exportRateByWeight.getTotalInvoiceAmount())
						.add(exportDocumentCharges.getTotalInvoiceAmount())
						.add(exportRateByDemurage.getTotalInvoiceAmount());

				InvoiceDataStorage invoiceStorage = new InvoiceDataStorage();
				invoiceStorage.setCompanyId(companyid);
				invoiceStorage.setBranchId(branchId);
				invoiceStorage.setImporterId(partyId);
				invoiceStorage.setImporterName(Party.getPartyName());
				invoiceStorage.setSirNo(exportObj.getErNo());
				invoiceStorage.setInDate(exportObj.getSeepzInDate());
				invoiceStorage.setOutDate(exportObj.getOutDate());
				invoiceStorage.setDocumentCharges(exportDocumentCharges.getTotalInvoiceAmount());
				invoiceStorage.setRateByPrice(exportRateByPrice.getTotalInvoiceAmount());
				invoiceStorage.setPrice(exportObj.getFobValueInINR());
				invoiceStorage.setRateByWeight(exportRateByWeight.getTotalInvoiceAmount());
				invoiceStorage.setWeight(exportObj.getGrossWeightInCarats());
				invoiceStorage.setDemurageCharges(exportRateByDemurage.getTotalInvoiceAmount());
				invoiceStorage.setStockDays((exportObj.getOutDate().getTime()
						- exportObj.getSeepzInDate().getTime() / (1000 * 60 * 60 * 24)));
				invoiceStorage.setBillAmount(loopBillAmount);
				invoiceStorage.setTaxAmount(loopTaxAmount);
				invoiceStorage.setTotalInvoiceAmount(loopTotalInvoiceAmount);
				invoiceStorage.setNop(exportObj.getNoOfParcel());
				invoiceStorage.setNoPackates(exportObj.getNoOfPackages());
				invoiceStorage.setChaName(exportObj.getChaName());
				invoiceStorage.setConsoleName(exportObj.getConsoleAgent());
				invoiceStorage.setMawb(exportObj.getAirwayBillNo());
				invoiceStorage.setHawb(exportObj.getHawb());
				invoiceStorage.setType("Exp");

				invoices.add(invoiceStorage);
			});

			return invoices;

		}

	}

//	To calculate a invoices at 7.00 PM

	public void generateInvoice(String companyId, String branchId, Party Party) {

		String importserviceId = ServiceIdMappingRepositary.findServieIdByKeys(companyId, branchId, "import pckgs");

		String heavyServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyId, branchId, "import HP");

		String exportserviceId = ServiceIdMappingRepositary.findServieIdByKeys(companyId, branchId, "export pckgs");

		String demurageId = ServiceIdMappingRepositary.findServieIdByKeys(companyId, branchId, "export DM");

		String documentServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyId, branchId,
				"Document Charges");

		String partyId = Party.getPartyId();
		String billingParty = partyId;

		CfsTarrif finalCfsTarrif;

		String tarrifParty;

		CfsTarrif tarrifByPartyId = CFSService.getBypartyId(companyId, branchId, billingParty);

		if (tarrifByPartyId != null) {
			finalCfsTarrif = tarrifByPartyId;
			tarrifParty = tarrifByPartyId.getPartyId();
		} else {
			CfsTarrif findByAll = CFSService.getBypartyId(companyId, branchId, "ALL");
			finalCfsTarrif = findByAll;
			tarrifParty = findByAll.getPartyId();
		}
		;

		Date invoiceDate = Party.getLastInVoiceDate();

		List<Import> Imports = importRepo.findImportAllDataBill(companyId, branchId, billingParty, invoiceDate);
		System.out.println("IMPORTS");
		for (Import im : Imports) {
			System.out.println(im);
		}

		List<ExportSHB> Exports = exportShbRepo.findExportAllDataBill(companyId, branchId, billingParty, invoiceDate);
		System.out.println("EXPORTS");
		for (ExportSHB ex : Exports) {
			System.out.println(ex);
		}

//     CALCULATE ONLY IF IMPORTS AND EXPORTS ARE NOT EMPTY
		if (!Imports.isEmpty() || !Exports.isEmpty()) {

			String invoiceNo = proceess.autoIncrementInvoiceNumber();
			
			AtomicReference<BigDecimal> billAmount = new AtomicReference<>(BigDecimal.ZERO);
			AtomicReference<BigDecimal> taxAmount = new AtomicReference<>(BigDecimal.ZERO);
			AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(BigDecimal.ZERO);

			List<InvoiceDetailSHB> detailList = new ArrayList<>();
			List<InvoiceTaxDetailsSHB> taxlList = new ArrayList<>();

			Imports.forEach(importObj -> {

				InvoiceTaxDetailsSHB invoiceStorage = new InvoiceTaxDetailsSHB();

				InvoiceDetailSHB importRateByPrice = InvoiceServiceImplSHB.calculateByPrice(companyId, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, importserviceId, importObj.getAssessableValue());

				importRateByPrice.setInvoiceNO(invoiceNo);
				importRateByPrice.setSirNo(importObj.getSirNo());
				detailList.add(importRateByPrice);

				InvoiceDetailSHB importRateByWeight = InvoiceServiceImplSHB.calculateByPrice(companyId, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, heavyServiceId, importObj.getGrossWeightInCarats());

				importRateByWeight.setInvoiceNO(invoiceNo);
				importRateByWeight.setSirNo(importObj.getSirNo());
				detailList.add(importRateByWeight);

				BigDecimal demurageDays = BigDecimal
						.valueOf((importObj.getOutDate().getTime() - importObj.getSeepzInDate().getTime())
								/ (1000 * 60 * 60 * 24));

				InvoiceDetailSHB importRateByDemurage = InvoiceServiceImplSHB.calculateByPrice(companyId, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, demurageId, demurageDays);

				importRateByDemurage.setInvoiceNO(invoiceNo);
				importRateByDemurage.setSirNo(importObj.getSirNo());
				detailList.add(importRateByDemurage);

				InvoiceDetailSHB importDocumentCharges = InvoiceServiceImplSHB.calculateByPrice(companyId, branchId,
						"A", billingParty, tarrifParty, finalCfsTarrif, documentServiceId,
						BigDecimal.valueOf(importObj.getNop()));

				importDocumentCharges.setInvoiceNO(invoiceNo);
				importDocumentCharges.setSirNo(importObj.getSirNo());
				detailList.add(importDocumentCharges);

				BigDecimal loopBillAmount = importRateByPrice.getBillAmount().add(importRateByWeight.getBillAmount())
						.add(importDocumentCharges.getBillAmount()).add(importRateByDemurage.getBillAmount());

				BigDecimal loopTaxAmount = importRateByPrice.getTaxAmount().add(importRateByWeight.getTaxAmount())
						.add(importDocumentCharges.getTaxAmount()).add(importRateByDemurage.getTaxAmount());

				BigDecimal loopTotalInvoiceAmount = importRateByPrice.getTotalInvoiceAmount()
						.add(importRateByWeight.getTotalInvoiceAmount())
						.add(importDocumentCharges.getTotalInvoiceAmount())
						.add(importRateByDemurage.getTotalInvoiceAmount());

				invoiceStorage.setCompanyId(companyId);
				invoiceStorage.setBranchId(branchId);
				invoiceStorage.setInvoiceNO(invoiceNo);
				invoiceStorage.setBillNO(invoiceNo);
				invoiceStorage.setPartyId(partyId);
				invoiceStorage.setInvoiceDate(new Date());	

				invoiceStorage.setSirNo(importObj.getSirNo());
				invoiceStorage.setInDate(importObj.getSeepzInDate());
				invoiceStorage.setOutDate(importObj.getOutDate());
				invoiceStorage.setDocumentCharges(importDocumentCharges.getTotalInvoiceAmount());
				invoiceStorage.setRateByPrice(importRateByPrice.getTotalInvoiceAmount());

				invoiceStorage.setPrice(importObj.getAssessableValue());
				invoiceStorage.setRateByWeight(importRateByWeight.getTotalInvoiceAmount());
				invoiceStorage.setWeight(importObj.getGrossWeightInCarats());
				invoiceStorage.setDemurageCharges(importRateByDemurage.getTotalInvoiceAmount());
				invoiceStorage.setStockDays((importObj.getOutDate().getTime() - importObj.getSeepzInDate().getTime())
						/ (1000 * 60 * 60 * 24));

				invoiceStorage.setBillAmount(loopBillAmount);
				invoiceStorage.setTaxAmount(loopTaxAmount);
				invoiceStorage.setTotalInvoiceAmount(loopTotalInvoiceAmount);
				invoiceStorage.setNop(importObj.getNoOfParcel());
				invoiceStorage.setNoPackates(importObj.getNop());

				invoiceStorage.setChaName(importObj.getChaName());
				invoiceStorage.setConsoleName(importObj.getConsoleName());
				invoiceStorage.setMawb(importObj.getMawb());
				invoiceStorage.setHawb(importObj.getHawb());
				invoiceStorage.setType("Imp");

				invoiceStorage.setStatus("A");
				invoiceStorage.setCreatedBy("SYSTEM");
				invoiceStorage.setCreatedDate(new Date());

				billAmount.updateAndGet(value -> value.add(loopBillAmount));
				taxAmount.updateAndGet(value -> value.add(loopTaxAmount));
				totalAmount.updateAndGet(value -> value.add(loopTotalInvoiceAmount));

				
				taxlList.add(invoiceStorage);
//				importRepo.updateBillgenerated(companyId,branchId,importObj.getSirNo(),importObj.getMawb(),importObj.getHawb());
//				invoices.add(invoiceStorage);
			});

			Exports.forEach(exportObj -> {

				InvoiceTaxDetailsSHB invoiceStorage = new InvoiceTaxDetailsSHB();

				InvoiceDetailSHB exportRateByPrice = InvoiceServiceImplSHB.calculateByPrice(companyId, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, exportserviceId, exportObj.getFobValueInINR());

				exportRateByPrice.setInvoiceNO(invoiceNo);
				exportRateByPrice.setSirNo(exportObj.getErNo());
				detailList.add(exportRateByPrice);

				InvoiceDetailSHB exportRateByWeight = InvoiceServiceImplSHB.calculateByPrice(companyId, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, heavyServiceId, exportObj.getGrossWeightInCarats());

				exportRateByWeight.setInvoiceNO(invoiceNo);
				exportRateByWeight.setSirNo(exportObj.getErNo());
				detailList.add(exportRateByWeight);

				BigDecimal demurageDays = BigDecimal
						.valueOf((exportObj.getOutDate().getTime() - exportObj.getSeepzInDate().getTime())
								/ (1000 * 60 * 60 * 24));

				InvoiceDetailSHB exportRateByDemurage = InvoiceServiceImplSHB.calculateByPrice(companyId, branchId, "A",
						billingParty, tarrifParty, finalCfsTarrif, demurageId, demurageDays);

				exportRateByDemurage.setInvoiceNO(invoiceNo);
				exportRateByDemurage.setSirNo(exportObj.getErNo());
				detailList.add(exportRateByDemurage);

				InvoiceDetailSHB exportDocumentCharges = InvoiceServiceImplSHB.calculateByPrice(companyId, branchId,
						"A", billingParty, tarrifParty, finalCfsTarrif, demurageId,
						BigDecimal.valueOf(exportObj.getNoOfParcel()));

				exportDocumentCharges.setInvoiceNO(invoiceNo);
				exportDocumentCharges.setSirNo(exportObj.getErNo());
				detailList.add(exportDocumentCharges);

				BigDecimal loopBillAmount = exportRateByPrice.getBillAmount().add(exportRateByWeight.getBillAmount())
						.add(exportDocumentCharges.getBillAmount()).add(exportRateByDemurage.getBillAmount());

				BigDecimal loopTaxAmount = exportRateByPrice.getTaxAmount().add(exportRateByWeight.getTaxAmount())
						.add(exportDocumentCharges.getTaxAmount()).add(exportRateByDemurage.getTaxAmount());

				BigDecimal loopTotalInvoiceAmount = exportRateByPrice.getTotalInvoiceAmount()
						.add(exportRateByWeight.getTotalInvoiceAmount())
						.add(exportDocumentCharges.getTotalInvoiceAmount())
						.add(exportRateByDemurage.getTotalInvoiceAmount());

				invoiceStorage.setCompanyId(companyId);
				invoiceStorage.setBranchId(branchId);
				invoiceStorage.setPartyId(partyId);
				invoiceStorage.setInvoiceNO(invoiceNo);
				invoiceStorage.setBillNO(invoiceNo);

				invoiceStorage.setSirNo(exportObj.getErNo());
				invoiceStorage.setInDate(exportObj.getSeepzInDate());
				invoiceStorage.setOutDate(exportObj.getOutDate());
				invoiceStorage.setDocumentCharges(exportDocumentCharges.getTotalInvoiceAmount());
				invoiceStorage.setRateByPrice(exportRateByPrice.getTotalInvoiceAmount());
				invoiceStorage.setPrice(exportObj.getFobValueInINR());
				invoiceStorage.setRateByWeight(exportRateByWeight.getTotalInvoiceAmount());
				invoiceStorage.setWeight(exportObj.getGrossWeightInCarats());
				invoiceStorage.setDemurageCharges(exportRateByDemurage.getTotalInvoiceAmount());
				invoiceStorage.setStockDays((exportObj.getOutDate().getTime()
						- exportObj.getSeepzInDate().getTime() / (1000 * 60 * 60 * 24)));

				invoiceStorage.setBillAmount(loopBillAmount);
				invoiceStorage.setTaxAmount(loopTaxAmount);
				invoiceStorage.setTotalInvoiceAmount(loopTotalInvoiceAmount);
				invoiceStorage.setNop(exportObj.getNoOfParcel());
				invoiceStorage.setNoPackates(exportObj.getNoOfPackages());
				invoiceStorage.setInvoiceDate(new Date());
				invoiceStorage.setChaName(exportObj.getChaName());
				invoiceStorage.setConsoleName(exportObj.getConsoleAgent());
				invoiceStorage.setMawb(exportObj.getAirwayBillNo());
				invoiceStorage.setHawb(exportObj.getHawb());
				invoiceStorage.setType("Exp");
				invoiceStorage.setStatus("A");
				invoiceStorage.setCreatedBy("SYSTEM");
				invoiceStorage.setCreatedDate(new Date());

				billAmount.updateAndGet(value -> value.add(loopBillAmount));
				taxAmount.updateAndGet(value -> value.add(loopTaxAmount));
				totalAmount.updateAndGet(value -> value.add(loopTotalInvoiceAmount));
				
				
				
//				exportShbRepo.updateBillgenerated(companyId,branchId,exportObj.getErNo(),exportObj.getAirwayBillNo(),exportObj.getHawb());
				taxlList.add(invoiceStorage);
			});


			
			
			Branch findByCompany_Id = branchRepo.findByCompanyIdAndBranchId(companyId, branchId);
			String CompanyGstNo = findByCompany_Id.getGST_No();
			String PartyGstNo = Party.getGstNo();
			String companyStateCode = CompanyGstNo.substring(0, 2);
			String partyStateCode = PartyGstNo.substring(0, 2);

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(new Date());

			// Creating a copy of endCalendar to avoid modifying the original instance
			Calendar invoiceDueCalendar = (Calendar) endCalendar.clone();
			invoiceDueCalendar.add(Calendar.DAY_OF_MONTH, 3);

			String sgst = "";
			String cgst = "";
			String igst = "";
			
			
			 BigDecimal sgstAmount = BigDecimal.ZERO;
		     BigDecimal cgstAmount = BigDecimal.ZERO;
		     BigDecimal igstAmount = BigDecimal.ZERO;
		     
		       BigDecimal taxPercentage = new BigDecimal("0.18"); // 18% as a decimal
		        BigDecimal taxAmountFinal = totalAmount.get().multiply(taxPercentage);

			if (companyStateCode.equals(partyStateCode)) {
				BigDecimal divide = taxAmountFinal.divide(BigDecimal.valueOf(2));
				sgstAmount = divide;
				cgstAmount = divide;
				cgst = "Y";
				sgst = "Y";
				igst = "N";
			} else {
				igstAmount = taxAmountFinal;
				cgst = "N";
				sgst = "N";
				igst = "Y";
			}

			InvoiceMainSHB main = new InvoiceMainSHB(companyId,branchId,invoiceNo, invoiceNo ,partyId,
						new Date(), finalCfsTarrif.getCfsTariffNo(),finalCfsTarrif.getCfsAmndNo(), invoiceDueCalendar.getTime(),taxAmount.get().add(taxAmountFinal),
						billAmount.get(),totalAmount.get().add(taxAmountFinal), "RC0001",
						new Date(), new Date(), new Date(),"comments", "N",igst,
						igstAmount, cgst, sgst,cgstAmount, sgstAmount,
						"A", "SYSTEM", new Date(), "SYSTEM", new Date(),
						"SYSTEM", new Date(), BigDecimal.ZERO, "P");

			System.out.println("main");
			System.out.println(main);

			
			
			
			detailRepo.saveAll(detailList);
			taxRepo.saveAll(taxlList);
			mainRepo.save(main);
			PartyService.updateParty(companyId,branchId,partyId,invoiceNo);
		}

	}

	

}
