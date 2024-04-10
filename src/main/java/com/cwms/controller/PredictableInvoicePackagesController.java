package com.cwms.controller;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.cwms.entities.Party;
import com.cwms.entities.PredictableInvoiceMain;
import com.cwms.entities.PredictableInvoicePackages;
import com.cwms.entities.PredictableInvoiceTaxDetails;
import com.cwms.repository.PartyRepository;
import com.cwms.repository.PredictableInvoiceMainRepositary;
import com.cwms.repository.PredictableInvoicePackagesRepositary;
import com.cwms.repository.PredictableInvoiceTaxDetailsRepo;

@RestController
@CrossOrigin("*")
@RequestMapping("/predictable")
public class PredictableInvoicePackagesController {

//	@Autowired
//	private PredictableInvoicePackagesRepositary predictablerepo;

	@Autowired
	private PredictableInvoiceTaxDetailsRepo predictabletaxrepo;
	
//	@Autowired
//	private PredictableInvoiceMainRepositary predictableinvoicemain;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	
	@Autowired
	public PartyRepository partyRepository;

	@GetMapping(value = "/list/{cid}/{bid}/{sdate}/{edate}/{pid}")
	public List<PredictableInvoiceTaxDetails> getlistInvoicePackages(@PathVariable("cid") String cid,
			@PathVariable("bid") String bid, @PathVariable("sdate") String sdate, @PathVariable("edate") String edate,
			@PathVariable(required = false, name = "pid") String pid) throws ParseException {

		List<PredictableInvoiceTaxDetails> invoicePackagesList = new ArrayList<>();

		invoicePackagesList = getData(cid, bid, sdate, edate, pid);
		System.out.println("invoicePackagesList " + invoicePackagesList);

		return invoicePackagesList;
	}
	
	
	
	

	@GetMapping(value = "/dailyReport")
	public ResponseEntity<?> getDailyReport(@RequestParam(name = "companyid", required = false) String CompanyId,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "PartyId", required = false) String PartyId,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws ParseException {
		
		System.out.println("Company Id  : "+ CompanyId + " Branch Id : "+branchId + " Party Id : "+PartyId + " Start Date : "+startDate + " End date : "+endDate);
			
		
		List<Object[]> invoiceDailyData = predictabletaxrepo.findInvoiceDataStartDateAndEndDateWithSum(CompanyId, branchId, startDate, endDate, PartyId);
		
		System.out.println("Invoice Data Found");

		System.out.println(invoiceDailyData);
	
		
		return ResponseEntity.ok(invoiceDailyData);
	}
	
	
//	public List<PredictableInvoicePackages> getData(String cid, String bid, String sdate, String edate, String pid)
//			throws ParseException {
//
//		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)");
//
//		Date startDate = null;
//		Date endDate = null;
//		startDate = dateFormat.parse(sdate);
//		endDate = dateFormat.parse(edate);
//		System.out.println(cid + "" + bid + "" + startDate + "" + endDate + "" + pid);
//		List<PredictableInvoicePackages> invoicePackages = new ArrayList<>();
//		List<PredictableInvoicePackages> total = new ArrayList<>();
//		if (pid == null || pid == "Null" || pid.equals("null") || pid.equals("Select Party")) {
//			invoicePackages = predictablerepo.getDataByDate(startDate, endDate,cid, bid);
//
//		} else {
//			invoicePackages = predictablerepo.getDataByPartyandDate(startDate, endDate, cid,bid, pid);
//		}
//
//		// System.out.println(invoicePackages);
//		Map<String, List<PredictableInvoicePackages>> groupedByPartyId = invoicePackages.stream()
//				.collect(Collectors.groupingBy(PredictableInvoicePackages::getPartyId));
//
//		for (Map.Entry<String, List<PredictableInvoicePackages>> entry : groupedByPartyId.entrySet()) {
//			String partyId = entry.getKey();
//			List<PredictableInvoicePackages> packagesForParty = entry.getValue();
//			System.out.println("packagesForParty " + packagesForParty);
//			PredictableInvoicePackages addPackages = new PredictableInvoicePackages();
//			for (PredictableInvoicePackages packages : packagesForParty) {
//				addPackages.setPartyId(packages.getPartyId());
//				addPackages.setCompanyId(packages.getCompanyId());
//				addPackages.setBranchId(packages.getBranchId());
//			//	addPackages.setSrNo(packages.getSrNo());
//				addPackages.setPredictableinvoiceNO(packages.getPredictableinvoiceNO());
//	
//
//				addPackages.setExportNop(addPackages.getExportNop() + packages.getExportNop());
//				addPackages.setExportRate(addPackages.getExportRate() + packages.getExportRate());
//				addPackages.setExportSubNop(addPackages.getExportSubNop() + packages.getExportSubNop());
//				addPackages.setExportSubRate(addPackages.getExportSubRate() + packages.getExportSubRate());
//
//				addPackages.setImportNop(addPackages.getImportNop() + packages.getImportNop());
//				addPackages.setImportRate(addPackages.getImportRate() + packages.getImportRate());
//				addPackages.setImportSubNop(addPackages.getImportSubNop() + packages.getImportSubNop());
//				addPackages.setImportSubRate(addPackages.getImportSubRate() + packages.getImportSubRate());
//
//				addPackages.setHolidayRate(addPackages.getHolidayRate() + packages.getHolidayRate());
//				addPackages.setHolidaySubNop(addPackages.getHolidaySubNop() + packages.getHolidaySubNop());
//
//				addPackages.setExportSplCartNop(addPackages.getExportSplCartNop() + packages.getExportSplCartNop());
//				addPackages.setExportSplCartRate(addPackages.getExportSplCartRate() + packages.getExportSplCartRate());
//				addPackages.setExportHpNop(addPackages.getExportHpNop() + packages.getExportHpNop());
//				addPackages.setExportHpRate(addPackages.getExportHpRate() + packages.getExportHpRate());
//				addPackages.setExportPcNop(addPackages.getExportPcNop() + packages.getExportPcNop());
//				addPackages.setExportPcRate(addPackages.getExportPcRate() + packages.getExportPcRate());
//				addPackages.setExportOcNop(addPackages.getExportOcNop() + packages.getExportOcNop());
//				addPackages.setExportOcRate(addPackages.getExportOcRate() + packages.getExportOcRate());
//
//				addPackages.setImportSplCartNop(addPackages.getImportSplCartNop() + packages.getImportSplCartNop());
//				addPackages.setImportSplCartRate(addPackages.getImportSplCartRate() + packages.getImportSplCartRate());
//				addPackages.setImportHpNop(addPackages.getImportHpNop() + packages.getImportHpNop());
//				addPackages.setImportHpRate(addPackages.getImportHpRate() + packages.getImportHpRate());
//				addPackages.setImportPcNop(addPackages.getImportPcNop() + packages.getImportPcNop());
//				addPackages.setImportPcRate(addPackages.getImportPcRate() + packages.getImportPcRate());
//				addPackages.setImportOcNop(addPackages.getImportOcNop() + packages.getImportOcNop());
//				addPackages.setImportOcRate(addPackages.getImportOcRate() + packages.getImportOcRate());
//				addPackages.setDemuragesNop(addPackages.getDemuragesNop() + packages.getDemuragesNop());
//				addPackages.setDemuragesRate(addPackages.getDemuragesRate() + packages.getDemuragesRate());
//
//			}
//			total.add(addPackages);
//		}
//
//		System.out.println("total " + total);
//
//		Map<String, Double> statusMap = new HashMap<>();
//		int temp = 1;
//		for (PredictableInvoicePackages invoicePackages2 : total) {
////			System.out.println("Party ID: " + invoicePackages2.getPartyId());
//
//			List<PredictableInvoiceMain> main = predictableinvoicemain.findByCompanyIdAndBranchIdAndPartyIdAndInvoiceDateBetween(cid, bid, invoicePackages2.getPartyId(),startDate, endDate);
//			String status = null;
//			double tax = 0;
//			for (PredictableInvoiceMain inMain : main) {
//
//				status = inMain.getIgst();
//				tax = inMain.getTaxAmount();
//			}
//
//			if (status == "Y" || status.equals("Y")) {
//				invoicePackages2.setCompanyId("1");
//
//			} else {
//				invoicePackages2.setCompanyId("0");
//
//			}
//			invoicePackages2.setBranchId(String.valueOf(tax));
//		}
//		return total;
//
//	}
	
	
	
	
//	public List<PredictableInvoiceTaxDetails> getData(String cid, String bid, String sdate, String edate, String pid)
//			throws ParseException {
//
//		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)");
//
//		Date startDate = null;
//		Date endDate = null;
//		startDate = dateFormat.parse(sdate);
//		endDate = dateFormat.parse(edate);
//		SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat outputDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//		String formattedStartDate = outputDateFormat.format(startDate);
//		String formattedEndDate = outputDateFormat.format(endDate);
//		System.out.println("formattedStartDate "+formattedStartDate+" "+"formattedEndDate "+formattedEndDate);
//		Date start = outputDateFormat1.parse(formattedStartDate);
//		Date end = outputDateFormat1.parse(formattedEndDate);
//		System.out.println(cid + "" + bid + "" + startDate + "" + endDate + "" + pid);
//		List<PredictableInvoiceTaxDetails> invoicePackages = new ArrayList<>();
//		List<PredictableInvoiceTaxDetails> total = new ArrayList<>();
//		System.out.println("Pid "+pid+" "+startDate+endDate);
//		if (pid == null || pid == "Null" || pid.equals("null") || pid.equals("Select Party")) {
//			invoicePackages = predictabletaxrepo.getDataByWithoutParty(start, end,cid, bid);
//
//		} else {
//			invoicePackages = predictabletaxrepo.getDataByParty(start, end, cid,bid, pid);
//		}
//		System.out.println("invoicePackages "+invoicePackages);
//
//		// System.out.println(invoicePackages);
//		Map<String, List<PredictableInvoiceTaxDetails>> groupedByPartyId = invoicePackages.stream()
//				.collect(Collectors.groupingBy(PredictableInvoiceTaxDetails::getPartyId));
//
//		for (Map.Entry<String, List<PredictableInvoiceTaxDetails>> entry : groupedByPartyId.entrySet()) {
//			String partyId = entry.getKey();
//			List<PredictableInvoiceTaxDetails> packagesForParty = entry.getValue();
//			System.out.println("packagesForParty " + packagesForParty);
//			PredictableInvoiceTaxDetails addPackages = new PredictableInvoiceTaxDetails();
//			for (PredictableInvoiceTaxDetails packages : packagesForParty) {
//				addPackages.setPartyId(packages.getPartyId());
//				addPackages.setCompanyId(packages.getCompanyId());
//				addPackages.setBranchId(packages.getBranchId());
//			//	addPackages.setSrNo(packages.getSrNo());
//				addPackages.setPredictableinvoiceNO(packages.getPredictableinvoiceNO());
//	
//
//				addPackages.setExportNoOfPackages(addPackages.getExportNoOfPackages() + packages.getExportNoOfPackages());
//				addPackages.setExportRate(addPackages.getExportRate() + packages.getExportRate());
//				addPackages.setExportSubNop(addPackages.getExportSubNop() + packages.getExportSubNop());
//				addPackages.setExportSubRate(addPackages.getExportSubRate() + packages.getExportSubRate());
//
//				addPackages.setImportNoOfPackages(addPackages.getImportNoOfPackages() + packages.getImportNoOfPackages());
//				addPackages.setImportRate(addPackages.getImportRate() + packages.getImportRate());
//				addPackages.setImportSubNop(addPackages.getImportSubNop() + packages.getImportSubNop());
//				addPackages.setImportSubRate(addPackages.getImportSubRate() + packages.getImportSubRate());
//
//				addPackages.setHolidayRate(addPackages.getHolidayRate() + packages.getHolidayRate());
//				addPackages.setHolidaySubNop(addPackages.getHolidaySubNop() + packages.getHolidaySubNop());
//
//				addPackages.setExportSplCartNop(addPackages.getExportSplCartNop() + packages.getExportSplCartNop());
//				addPackages.setExportScRate(addPackages.getExportScRate() + packages.getExportScRate());
//				addPackages.setExportHpNop(addPackages.getExportHpNop() + packages.getExportHpNop());
//				addPackages.setExportHpRate(addPackages.getExportHpRate() + packages.getExportHpRate());
//				addPackages.setExportPcNop(addPackages.getExportPcNop() + packages.getExportPcNop());
//				addPackages.setExportPcRate(addPackages.getExportPcRate() + packages.getExportPcRate());
//				addPackages.setExportOcNop(addPackages.getExportOcNop() + packages.getExportOcNop());
//				addPackages.setExportPenalty(addPackages.getExportPenalty() + packages.getExportPenalty());
//
//				addPackages.setImportSplCartNop(addPackages.getImportSplCartNop() + packages.getImportSplCartNop());
//				addPackages.setImportScRate(addPackages.getImportScRate() + packages.getImportScRate());
//				addPackages.setImportHpNop(addPackages.getImportHpNop() + packages.getImportHpNop());
//				addPackages.setImportHpRate(addPackages.getImportHpRate() + packages.getImportHpRate());
//				addPackages.setImportPcNop(addPackages.getImportPcNop() + packages.getImportPcNop());
//				addPackages.setImportPcRate(addPackages.getImportPcRate() + packages.getImportPcRate());
//				addPackages.setImportOcNop(addPackages.getImportOcNop() + packages.getImportOcNop());
//				addPackages.setImportPenalty(addPackages.getImportPenalty() + packages.getImportPenalty());
//				addPackages.setDemuragesNop(addPackages.getDemuragesNop() + packages.getDemuragesNop());
//				addPackages.setDemuragesRate(addPackages.getDemuragesRate() + packages.getDemuragesRate());
//
//			}
//			total.add(addPackages);
//		}
//
//		System.out.println("total " + total);
//
//		Map<String, Double> statusMap = new HashMap<>();
//		int temp = 1;
//		for (PredictableInvoiceTaxDetails invoicePackages2 : total) {
//			System.out.println("Party ID: " + invoicePackages2.getPartyId());
//
//			List<PredictableInvoiceMain> main = predictableinvoicemain.findByCompanyIdAndBranchIdAndPartyIdAndInvoiceDateBetween(cid, bid, invoicePackages2.getPartyId(),startDate, endDate);
//			
//			System.out.println("main "+main);
//			
//			String status = null;
//			double tax = 0;
//		if(main != null && !main.isEmpty()) {
//			for (PredictableInvoiceMain inMain : main) {
//
//				status = inMain.getIgst();
//				tax = inMain.getTaxAmount();
//			}
//
//			if (status == "Y" || status.equals("Y")) {
//				invoicePackages2.setCompanyId("1");
//
//			} else {
//				invoicePackages2.setCompanyId("0");
//
//			}
//			invoicePackages2.setBranchId(String.valueOf(tax));
//		}
//		}
//		return total;
//
//	}
//	
	
	
	
	public List<PredictableInvoiceTaxDetails> getData(String cid, String bid, String sdate, String edate, String pid)
			throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)");

		Date startDate = null;
		Date endDate = null;
		startDate = dateFormat.parse(sdate);
		endDate = dateFormat.parse(edate);
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outputDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedStartDate = outputDateFormat.format(startDate);
		String formattedEndDate = outputDateFormat.format(endDate);
		System.out.println("formattedStartDate "+formattedStartDate+" "+"formattedEndDate "+formattedEndDate);
		Date start = outputDateFormat1.parse(formattedStartDate);
		Date end = outputDateFormat1.parse(formattedEndDate);
		System.out.println(cid + "" + bid + "" + startDate + "" + endDate + "" + pid);
		List<PredictableInvoiceTaxDetails> invoicePackages = new ArrayList<>();
		List<PredictableInvoiceTaxDetails> total = new ArrayList<>();
		System.out.println("Pid "+pid+" "+startDate+endDate);
		if (pid == null || pid == "Null" || pid.equals("null") || pid.equals("Select Party")) {
			invoicePackages = predictabletaxrepo.getDataByWithoutParty(start, end,cid, bid);

		} else {
			invoicePackages = predictabletaxrepo.getDataByParty(start, end, cid,bid, pid);
		}
		System.out.println("invoicePackages "+invoicePackages);

		// System.out.println(invoicePackages);
		Map<String, List<PredictableInvoiceTaxDetails>> groupedByPartyId = invoicePackages.stream()
				.collect(Collectors.groupingBy(PredictableInvoiceTaxDetails::getPartyId));

		for (Map.Entry<String, List<PredictableInvoiceTaxDetails>> entry : groupedByPartyId.entrySet()) {
			String partyId = entry.getKey();
			List<PredictableInvoiceTaxDetails> packagesForParty = entry.getValue();
			System.out.println("packagesForParty " + packagesForParty);
			PredictableInvoiceTaxDetails addPackages = new PredictableInvoiceTaxDetails();
			for (PredictableInvoiceTaxDetails packages : packagesForParty) {
				addPackages.setPartyId(packages.getPartyId());
				addPackages.setCompanyId(packages.getCompanyId());
				addPackages.setBranchId(packages.getBranchId());
			//	addPackages.setSrNo(packages.getSrNo());
				addPackages.setPredictableinvoiceNO(packages.getPredictableinvoiceNO());
	

				addPackages.setExportNoOfPackages(addPackages.getExportNoOfPackages() + packages.getExportNoOfPackages());
				addPackages.setExportRate(addPackages.getExportRate() + packages.getExportRate());
				addPackages.setExportSubNop(addPackages.getExportSubNop() + packages.getExportSubNop());
				addPackages.setExportSubRate(addPackages.getExportSubRate() + packages.getExportSubRate());

				addPackages.setImportNoOfPackages(addPackages.getImportNoOfPackages() + packages.getImportNoOfPackages());
				addPackages.setImportRate(addPackages.getImportRate() + packages.getImportRate());
				addPackages.setImportSubNop(addPackages.getImportSubNop() + packages.getImportSubNop());
				addPackages.setImportSubRate(addPackages.getImportSubRate() + packages.getImportSubRate());

				addPackages.setHolidayRate(addPackages.getHolidayRate() + packages.getHolidayRate());
				addPackages.setHolidaySubNop(addPackages.getHolidaySubNop() + packages.getHolidaySubNop());

				addPackages.setExportSplCartNop(addPackages.getExportSplCartNop() + packages.getExportSplCartNop());
				addPackages.setExportScRate(addPackages.getExportScRate() + packages.getExportScRate());
				addPackages.setExportHpNop(addPackages.getExportHpNop() + packages.getExportHpNop());
				addPackages.setExportHpRate(addPackages.getExportHpRate() + packages.getExportHpRate());
				addPackages.setExportPcNop(addPackages.getExportPcNop() + packages.getExportPcNop());
				addPackages.setExportPcRate(addPackages.getExportPcRate() + packages.getExportPcRate());
				addPackages.setExportOcNop(addPackages.getExportOcNop() + packages.getExportOcNop());
				addPackages.setExportPenalty(addPackages.getExportPenalty() + packages.getExportPenalty());

				addPackages.setImportSplCartNop(addPackages.getImportSplCartNop() + packages.getImportSplCartNop());
				addPackages.setImportScRate(addPackages.getImportScRate() + packages.getImportScRate());
				addPackages.setImportHpNop(addPackages.getImportHpNop() + packages.getImportHpNop());
				addPackages.setImportHpRate(addPackages.getImportHpRate() + packages.getImportHpRate());
				addPackages.setImportPcNop(addPackages.getImportPcNop() + packages.getImportPcNop());
				addPackages.setImportPcRate(addPackages.getImportPcRate() + packages.getImportPcRate());
				addPackages.setImportOcNop(addPackages.getImportOcNop() + packages.getImportOcNop());
				addPackages.setImportPenalty(addPackages.getImportPenalty() + packages.getImportPenalty());
				addPackages.setDemuragesNop(addPackages.getDemuragesNop() + packages.getDemuragesNop());
				addPackages.setDemuragesRate(addPackages.getDemuragesRate() + packages.getDemuragesRate());

			}
			total.add(addPackages);
		}

		System.out.println("total " + total);

		Map<String, Double> statusMap = new HashMap<>();
		int temp = 1;
		for (PredictableInvoiceTaxDetails invoicePackages2 : total) {
			System.out.println("Party ID: " + invoicePackages2.getPartyId());
			
			Party party = partyRepository.getdatabyid(cid, bid, invoicePackages2.getPartyId());

		//	List<PredictableInvoiceMain> main = predictableinvoicemain.findByCompanyIdAndBranchIdAndPartyIdAndInvoiceDateBetween(cid, bid, invoicePackages2.getPartyId(),startDate, endDate);
			
			
			String status = null;
			double tax = 0;
			double tax1 = 0;
			
			if(party!=null) {
				if("Y".equals(party.getTaxApplicable())) {
					
					tax = invoicePackages2.getTotal1();
					 tax1 = (tax*18)/100;
					if(party.getGstNo().startsWith("27")) {
						status = "Y";
						
					}
					else {
						status = "N";
						
					}
				}
			}


				
			

			if (status != null && (status.equals("Y") || "Y".equals(status))) {
			    invoicePackages2.setCompanyId("1");
			} else {
			    invoicePackages2.setCompanyId("0");
			}

			if(tax1 == 0.0) {
				invoicePackages2.setBranchId(String.valueOf(0));
			}
			else {
				invoicePackages2.setBranchId(String.valueOf(tax1));
			}
		}
		
		return total;

	}

	public String FunctionPrintPdf(List<PredictableInvoiceTaxDetails> list, String sdate, String edate, String cid, String bid)
			throws Exception {
		System.out.println("Hiii2 "+list);
		PredictableInvoiceTaxDetails invoicePackages = null;
		int totalExportNop = 0;
		int totalExportRate = 0;
		int totalExportSubNop = 0;
		int totalExportSubRate = 0;
		int totalImportNop = 0;
		int totalImportRate = 0;
		int totalImportSubNop = 0;
		int totalImportSubRate = 0;
		int totalHolidayRate = 0;
		int totalDemuragesRate = 0;
		int totalExportSplCartRate = 0;
		int totalExportHpRate = 0;
		int totalExportPcRate = 0;
		int totalExportOcRate = 0;
		int totalImportSplCartRate = 0;
		int totalImportHpRate = 0;
		int totalImportPcRate = 0;
		int totalImportOcRate = 0;
		int totalHolidaySubNop = 0;
		int totalIgst = 0;
		int totalCgst = 0;
		int totalSgst = 0;

		for (PredictableInvoiceTaxDetails invoice : list) {
			System.out.println("invoice "+invoice);
			totalExportNop += invoice.getExportNoOfPackages();
			totalExportRate += (int) invoice.getExportRate();
			totalExportSubNop += invoice.getExportSubNop();
			totalExportSubRate += (int) invoice.getExportSubRate();
			totalImportNop += invoice.getImportNoOfPackages();
			totalImportRate += (int) invoice.getImportRate();
			totalImportSubNop += invoice.getImportSubNop();
			totalImportSubRate += (int) invoice.getImportSubRate();
			totalHolidayRate += (int) invoice.getHolidayRate();
			totalDemuragesRate += (int) invoice.getDemuragesRate();
			totalExportSplCartRate += (int) invoice.getExportScRate();
			totalExportHpRate += (int) invoice.getExportHpRate();
			totalExportPcRate += (int) invoice.getExportPcRate();
			totalExportOcRate += (int) invoice.getExportPenalty();
			totalImportSplCartRate += (int) invoice.getImportScRate();
			totalImportHpRate += (int) invoice.getImportHpRate();
			totalImportPcRate += (int) invoice.getImportPcRate();
			totalImportOcRate += (int) invoice.getImportPenalty();
			totalHolidaySubNop += invoice.getHolidaySubNop();
			System.out.println("invoice.getCompanyId() "+invoice.getCompanyId());
		
			if (invoice.getCompanyId().equals("0")) {
				System.out.println("Hiii3 "+invoice.getBranchId());
			    // Assuming totalIgst, totalCgst, and totalSgst are variables to accumulate the values
			    if(!invoice.getBranchId().startsWith("B")) {
			    	totalIgst += Double.parseDouble(invoice.getBranchId());
			 
			    }
			    else {
			       	totalIgst += 0;
			    }
			    System.out.println("Hiii4");
			} else {
				
			    if(invoice.getBranchId().startsWith("B")) {
			    	
				    
				    totalCgst += 0;
				    totalSgst += 0;
			    }
			    else {
			    	double branchIdValue = Double.parseDouble(invoice.getBranchId());
				    
				    totalCgst += branchIdValue / 2;
				    totalSgst += branchIdValue / 2;
			    }
			   
			}


		}
	    
		List<Party> parties = filterPartyByPartyNameDesc(list, cid, bid);
		
		
		
		list.sort(Comparator.comparing(pkg -> {
		    for (Party party : parties) {
		        if (party.getPartyId().equals(pkg.getPartyId())) {
		            return party.getPartyName();
		        }
		    }
		    return null; // Handle the case when party not found
		}));
		
		
		System.out.println("parties "+parties);
		
		try {

			Context context = new Context();
			context.setVariable("name", "mahesh");
			context.setVariable("startDate", returnformatedate(sdate));
			context.setVariable("endDate", returnformatedate(edate));
			context.setVariable("invoiceDetails", list);
			context.setVariable("totalExportNop", totalExportNop);
			context.setVariable("totalExportRate", totalExportRate);
			context.setVariable("totalExportSubNop", totalExportSubNop);
			context.setVariable("totalExportSubRate", totalExportSubRate);
			context.setVariable("totalImportNop", totalImportNop);
			context.setVariable("totalImportRate", totalImportRate);
			context.setVariable("totalImportSubNop", totalImportSubNop);
			context.setVariable("totalImportSubRate", totalImportSubRate);
			context.setVariable("totalHolidayRate", totalHolidayRate);
			context.setVariable("totalDemuragesRate", totalDemuragesRate);
			context.setVariable("totalExportSplCartRate", totalExportSplCartRate);
			context.setVariable("totalExportHpRate", totalExportHpRate);
			context.setVariable("totalExportPcRate", totalExportPcRate);
			context.setVariable("totalExportOcRate", totalExportOcRate);
			context.setVariable("totalImportSplCartRate", totalImportSplCartRate);
			context.setVariable("totalImportHpRate", totalImportHpRate);
			context.setVariable("totalImportPcRate", totalImportPcRate);
			context.setVariable("totalImportOcRate", totalImportOcRate);
			context.setVariable("totalHolidaySubNop", totalHolidaySubNop);
			context.setVariable("totalIgst", totalIgst);
			context.setVariable("totalCgst", totalCgst);
			context.setVariable("totalSgst", totalSgst);

			if (parties != null && !parties.isEmpty()) {
				// Render the Thymeleaf template with the parties list
				context.setVariable("parties", parties);
			} else {
			}
System.out.println("context "+context);
			Thread.sleep(1000);
			System.out.println("list "+list);
			// Process the HTML template with dynamic values
			String htmlContent = templateEngine.process("PartyBillSummaryReport", context);

			ITextRenderer renderer = new ITextRenderer();

			// Set the PDF page size and margins
			renderer.setDocumentFromString(htmlContent);
			renderer.layout();

			// Generate PDF content
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			renderer.createPDF(outputStream);

			// Get the PDF bytes
			byte[] pdfBytes = outputStream.toByteArray();

			// Encode the PDF content as Base64
			String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
System.out.println("base64Pdf "+base64Pdf);
			return base64Pdf;
		} catch (Exception e) {
			// Handle exceptions appropriately
			return null;
		}
	}
	
	
	
	@GetMapping("/generatePrint/{cid}/{bid}/{sdate}/{edate}/{pid}")
	public ResponseEntity<String> generatePrint(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("sdate") String sdate, @PathVariable("edate") String edate,
			@PathVariable(required = false, name = "pid") String pid) throws Exception {
		try {
              System.out.println("Hiiii ");
			List<PredictableInvoiceTaxDetails> invoicePackagesList = new ArrayList<>();
        
			invoicePackagesList = getData(cid, bid, sdate, edate, pid);
			System.out.println("invoicePackagesList "+invoicePackagesList);
//			InvoicePackages Total = gettotal(invoicePackagesList);
		//	List<Party> list = FilterParty1(invoicePackagesList, cid, bid);
			String base64Pdf = FunctionPrintPdf(invoicePackagesList, sdate, edate, cid, bid);
            System.out.println(base64Pdf);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(base64Pdf);
		} catch (Exception e) {
			// Handle exceptions appropriately
			return ResponseEntity.badRequest().body("Error generating PDF");
		}

	}
	
	
	
	public List<Party> FilterParty1(List<PredictableInvoiceTaxDetails> list, String cid, String bid) {

		List<Party> filterParty = new ArrayList<>();
		List<Party> parties = partyRepository.getalldata(cid, bid);

		for (PredictableInvoiceTaxDetails packages : list) {
			String partyIdToMatch = packages.getPartyId();
			for (Party party : parties) {
				if (partyIdToMatch.equals(party.getPartyId())) {
					filterParty.add(party);
					break; // Break the inner loop once a match is found
				}
			}
		}

	
		return filterParty;
	}
	
	
	
//	public String FunctionPrintPdf(List<PredictableInvoicePackages> list, String sdate, String edate, String cid, String bid)
//			throws Exception {
//		System.out.println("Hiii2 "+list);
//		PredictableInvoicePackages invoicePackages = null;
//		int totalExportNop = 0;
//		int totalExportRate = 0;
//		int totalExportSubNop = 0;
//		int totalExportSubRate = 0;
//		int totalImportNop = 0;
//		int totalImportRate = 0;
//		int totalImportSubNop = 0;
//		int totalImportSubRate = 0;
//		int totalHolidayRate = 0;
//		int totalDemuragesRate = 0;
//		int totalExportSplCartRate = 0;
//		int totalExportHpRate = 0;
//		int totalExportPcRate = 0;
//		int totalExportOcRate = 0;
//		int totalImportSplCartRate = 0;
//		int totalImportHpRate = 0;
//		int totalImportPcRate = 0;
//		int totalImportOcRate = 0;
//		int totalHolidaySubNop = 0;
//		int totalIgst = 0;
//		int totalCgst = 0;
//		int totalSgst = 0;
//
//		for (PredictableInvoicePackages invoice : list) {
//			System.out.println("invoice "+invoice);
//			totalExportNop += invoice.getExportNop();
//			totalExportRate += (int) invoice.getExportRate();
//			totalExportSubNop += invoice.getExportSubNop();
//			totalExportSubRate += (int) invoice.getExportSubRate();
//			totalImportNop += invoice.getImportNop();
//			totalImportRate += (int) invoice.getImportRate();
//			totalImportSubNop += invoice.getImportSubNop();
//			totalImportSubRate += (int) invoice.getImportSubRate();
//			totalHolidayRate += (int) invoice.getHolidayRate();
//			totalDemuragesRate += (int) invoice.getDemuragesRate();
//			totalExportSplCartRate += (int) invoice.getExportSplCartRate();
//			totalExportHpRate += (int) invoice.getExportHpRate();
//			totalExportPcRate += (int) invoice.getExportPcRate();
//			totalExportOcRate += (int) invoice.getExportOcRate();
//			totalImportSplCartRate += (int) invoice.getImportSplCartRate();
//			totalImportHpRate += (int) invoice.getImportHpRate();
//			totalImportPcRate += (int) invoice.getImportPcRate();
//			totalImportOcRate += (int) invoice.getImportOcRate();
//			totalHolidaySubNop += invoice.getHolidaySubNop();
//			System.out.println("invoice.getCompanyId() "+invoice.getCompanyId());
//			int flag = Integer.parseInt(invoice.getCompanyId());
//			if (flag != 0) {
//				System.out.println("Hiii3");
//			    // Assuming totalIgst, totalCgst, and totalSgst are variables to accumulate the values
//			    totalIgst += Integer.parseInt(invoice.getBranchId());
//			} else {
//				System.out.println("Hiii4");
//			    // Assuming getBranchId returns an integer value
//			    double branchIdValue = Double.parseDouble(invoice.getBranchId());
//			    System.out.println("branchIdValue "+branchIdValue);
//			    // Assuming totalCgst and totalSgst are variables to accumulate the values
//			    totalCgst += branchIdValue / 2;
//			    totalSgst += branchIdValue / 2;
//			    System.out.println("Hiii5");
//			}
//
//
//		}
//	    
//		List<Party> parties = FilterParty1(list, cid, bid);
//		
//		
//		System.out.println("parties "+parties);
//		
//		try {
//
//			Context context = new Context();
//			context.setVariable("name", "mahesh");
//			context.setVariable("startDate", returnformatedate(sdate));
//			context.setVariable("endDate", returnformatedate(edate));
//			context.setVariable("invoiceDetails", list);
//			context.setVariable("totalExportNop", totalExportNop);
//			context.setVariable("totalExportRate", totalExportRate);
//			context.setVariable("totalExportSubNop", totalExportSubNop);
//			context.setVariable("totalExportSubRate", totalExportSubRate);
//			context.setVariable("totalImportNop", totalImportNop);
//			context.setVariable("totalImportRate", totalImportRate);
//			context.setVariable("totalImportSubNop", totalImportSubNop);
//			context.setVariable("totalImportSubRate", totalImportSubRate);
//			context.setVariable("totalHolidayRate", totalHolidayRate);
//			context.setVariable("totalDemuragesRate", totalDemuragesRate);
//			context.setVariable("totalExportSplCartRate", totalExportSplCartRate);
//			context.setVariable("totalExportHpRate", totalExportHpRate);
//			context.setVariable("totalExportPcRate", totalExportPcRate);
//			context.setVariable("totalExportOcRate", totalExportOcRate);
//			context.setVariable("totalImportSplCartRate", totalImportSplCartRate);
//			context.setVariable("totalImportHpRate", totalImportHpRate);
//			context.setVariable("totalImportPcRate", totalImportPcRate);
//			context.setVariable("totalImportOcRate", totalImportOcRate);
//			context.setVariable("totalHolidaySubNop", totalHolidaySubNop);
//			context.setVariable("totalIgst", totalIgst);
//			context.setVariable("totalCgst", totalCgst);
//			context.setVariable("totalSgst", totalSgst);
//
//			if (parties != null && !parties.isEmpty()) {
//				// Render the Thymeleaf template with the parties list
//				context.setVariable("parties", parties);
//			} else {
//			}
//
//			Thread.sleep(1000);
//			System.out.println("list "+list);
//			// Process the HTML template with dynamic values
//			String htmlContent = templateEngine.process("PartyBillSummaryReport", context);
//
//			ITextRenderer renderer = new ITextRenderer();
//
//			// Set the PDF page size and margins
//			renderer.setDocumentFromString(htmlContent);
//			renderer.layout();
//
//			// Generate PDF content
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			renderer.createPDF(outputStream);
//
//			// Get the PDF bytes
//			byte[] pdfBytes = outputStream.toByteArray();
//
//			// Encode the PDF content as Base64
//			String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
//System.out.println("base64Pdf "+base64Pdf);
//			return base64Pdf;
//		} catch (Exception e) {
//			// Handle exceptions appropriately
//			return null;
//		}
//	}
	
	
//	public String FunctionPrintPdf(List<PredictableInvoiceTaxDetails> list, String sdate, String edate, String cid, String bid)
//			throws Exception {
//		System.out.println("Hiii2 "+list);
//		PredictableInvoiceTaxDetails invoicePackages = null;
//		int totalExportNop = 0;
//		int totalExportRate = 0;
//		int totalExportSubNop = 0;
//		int totalExportSubRate = 0;
//		int totalImportNop = 0;
//		int totalImportRate = 0;
//		int totalImportSubNop = 0;
//		int totalImportSubRate = 0;
//		int totalHolidayRate = 0;
//		int totalDemuragesRate = 0;
//		int totalExportSplCartRate = 0;
//		int totalExportHpRate = 0;
//		int totalExportPcRate = 0;
//		int totalExportOcRate = 0;
//		int totalImportSplCartRate = 0;
//		int totalImportHpRate = 0;
//		int totalImportPcRate = 0;
//		int totalImportOcRate = 0;
//		int totalHolidaySubNop = 0;
//		int totalIgst = 0;
//		int totalCgst = 0;
//		int totalSgst = 0;
//
//		for (PredictableInvoiceTaxDetails invoice : list) {
//			System.out.println("invoice "+invoice);
//			totalExportNop += invoice.getExportNoOfPackages();
//			totalExportRate += (int) invoice.getExportRate();
//			totalExportSubNop += invoice.getExportSubNop();
//			totalExportSubRate += (int) invoice.getExportSubRate();
//			totalImportNop += invoice.getImportNoOfPackages();
//			totalImportRate += (int) invoice.getImportRate();
//			totalImportSubNop += invoice.getImportSubNop();
//			totalImportSubRate += (int) invoice.getImportSubRate();
//			totalHolidayRate += (int) invoice.getHolidayRate();
//			totalDemuragesRate += (int) invoice.getDemuragesRate();
//			totalExportSplCartRate += (int) invoice.getExportScRate();
//			totalExportHpRate += (int) invoice.getExportHpRate();
//			totalExportPcRate += (int) invoice.getExportPcRate();
//			totalExportOcRate += (int) invoice.getExportPenalty();
//			totalImportSplCartRate += (int) invoice.getImportScRate();
//			totalImportHpRate += (int) invoice.getImportHpRate();
//			totalImportPcRate += (int) invoice.getImportPcRate();
//			totalImportOcRate += (int) invoice.getImportPenalty();
//			totalHolidaySubNop += invoice.getHolidaySubNop();
//			System.out.println("invoice.getCompanyId() "+invoice.getCompanyId());
//		
//			if (!invoice.getCompanyId().equals("0")) {
//				System.out.println("Hiii3");
//			    // Assuming totalIgst, totalCgst, and totalSgst are variables to accumulate the values
//			    if(invoice.getBranchId().startsWith("B")) {
//			    	totalIgst += 0;
//			    }
//			    else {
//			    	totalIgst += Integer.parseInt(invoice.getBranchId());
//			    }
//			} else {
//				
//			    if(invoice.getBranchId().startsWith("B")) {
//			    	
//				    
//				    totalCgst += 0;
//				    totalSgst += 0;
//			    }
//			    else {
//			    	double branchIdValue = Double.parseDouble(invoice.getBranchId());
//				    
//				    totalCgst += branchIdValue / 2;
//				    totalSgst += branchIdValue / 2;
//			    }
//			   
//			}
//
//
//		}
//	    
//		List<Party> parties = filterPartyByPartyNameDesc(list, cid, bid);
//		
//		
//		
//		list.sort(Comparator.comparing(pkg -> {
//		    for (Party party : parties) {
//		        if (party.getPartyId().equals(pkg.getPartyId())) {
//		            return party.getPartyName();
//		        }
//		    }
//		    return null; // Handle the case when party not found
//		}));
//		
//		
//		System.out.println("parties "+parties);
//		
//		try {
//
//			Context context = new Context();
//			context.setVariable("name", "mahesh");
//			context.setVariable("startDate", returnformatedate(sdate));
//			context.setVariable("endDate", returnformatedate(edate));
//			context.setVariable("invoiceDetails", list);
//			context.setVariable("totalExportNop", totalExportNop);
//			context.setVariable("totalExportRate", totalExportRate);
//			context.setVariable("totalExportSubNop", totalExportSubNop);
//			context.setVariable("totalExportSubRate", totalExportSubRate);
//			context.setVariable("totalImportNop", totalImportNop);
//			context.setVariable("totalImportRate", totalImportRate);
//			context.setVariable("totalImportSubNop", totalImportSubNop);
//			context.setVariable("totalImportSubRate", totalImportSubRate);
//			context.setVariable("totalHolidayRate", totalHolidayRate);
//			context.setVariable("totalDemuragesRate", totalDemuragesRate);
//			context.setVariable("totalExportSplCartRate", totalExportSplCartRate);
//			context.setVariable("totalExportHpRate", totalExportHpRate);
//			context.setVariable("totalExportPcRate", totalExportPcRate);
//			context.setVariable("totalExportOcRate", totalExportOcRate);
//			context.setVariable("totalImportSplCartRate", totalImportSplCartRate);
//			context.setVariable("totalImportHpRate", totalImportHpRate);
//			context.setVariable("totalImportPcRate", totalImportPcRate);
//			context.setVariable("totalImportOcRate", totalImportOcRate);
//			context.setVariable("totalHolidaySubNop", totalHolidaySubNop);
//			context.setVariable("totalIgst", totalIgst);
//			context.setVariable("totalCgst", totalCgst);
//			context.setVariable("totalSgst", totalSgst);
//
//			if (parties != null && !parties.isEmpty()) {
//				// Render the Thymeleaf template with the parties list
//				context.setVariable("parties", parties);
//			} else {
//			}
//System.out.println("context "+context);
//			Thread.sleep(1000);
//			System.out.println("list "+list);
//			// Process the HTML template with dynamic values
//			String htmlContent = templateEngine.process("PartyBillSummaryReport", context);
//
//			ITextRenderer renderer = new ITextRenderer();
//
//			// Set the PDF page size and margins
//			renderer.setDocumentFromString(htmlContent);
//			renderer.layout();
//
//			// Generate PDF content
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			renderer.createPDF(outputStream);
//
//			// Get the PDF bytes
//			byte[] pdfBytes = outputStream.toByteArray();
//
//			// Encode the PDF content as Base64
//			String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
//System.out.println("base64Pdf "+base64Pdf);
//			return base64Pdf;
//		} catch (Exception e) {
//			// Handle exceptions appropriately
//			return null;
//		}
//	}
//	
	public List<Party> filterPartyByPartyNameDesc(List<PredictableInvoiceTaxDetails> list, String cid, String bid) {
	    List<Party> parties = partyRepository.getalldata(cid, bid);
	    
	    // Filter parties based on PredictableInvoicePackages
	    List<Party> filteredParties = parties.stream()
	            .filter(party -> list.stream().anyMatch(pkg -> pkg.getPartyId().equals(party.getPartyId())))
	            .collect(Collectors.toList());

	    // Sort the filtered parties by partyName in descending order
	    filteredParties.sort(Comparator.comparing(Party::getPartyName));

	    return filteredParties;
	}
	
	
	
	
	
	
	public String returnformatedate(String originalDateString) {

		SimpleDateFormat originalDateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");

		// Create a SimpleDateFormat to format the date in the new format
		SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String newDateString = null;
		try {
			// Parse the original date string
			Date date = originalDateFormat.parse(originalDateString);

			// Format the date as a new string in the desired format
			newDateString = newDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newDateString;
	}

	public PredictableInvoicePackages gettotal(List<PredictableInvoicePackages> list) {

		PredictableInvoicePackages invoicePackages = null;
		int totalExportNop = 0;
		int totalExportRate = 0;
		int totalExportSubNop = 0;
		int totalExportSubRate = 0;
		int totalImportNop = 0;
		int totalImportRate = 0;
		int totalImportSubNop = 0;
		int totalImportSubRate = 0;
		int totalHolidayRate = 0;
		int totalDemuragesRate = 0;
		int totalExportSplCartRate = 0;
		int totalExportHpRate = 0;
		int totalExportPcRate = 0;
		int totalExportOcRate = 0;
		int totalImportSplCartRate = 0;
		int totalImportHpRate = 0;
		int totalImportPcRate = 0;
		int totalImportOcRate = 0;
		int totalHolidaySubNop = 0;
		int totalIgst = 0;
		int totalCgst = 0;
		int totalSgst = 0;

		for (PredictableInvoicePackages invoice : list) {

			totalExportNop = invoice.getExportNop();
			totalExportRate = (int) invoice.getExportRate();
			totalExportSubNop = invoice.getExportSubNop();
			totalExportSubRate = (int) invoice.getExportSubRate();
			totalImportNop = invoice.getImportNop();
			totalImportRate = (int) invoice.getImportRate();
			totalImportSubNop = invoice.getImportSubNop();
			totalImportSubRate = (int) invoice.getImportSubRate();
			totalHolidayRate = (int) invoice.getHolidayRate();
			totalDemuragesRate = (int) invoice.getDemuragesRate();
			totalExportSplCartRate = (int) invoice.getExportSplCartRate();
			totalExportHpRate = (int) invoice.getExportHpRate();
			totalExportPcRate = (int) invoice.getExportPcRate();
			totalExportOcRate = (int) invoice.getExportOcRate();
			totalImportSplCartRate = (int) invoice.getImportSplCartRate();
			totalImportHpRate = (int) invoice.getImportHpRate();
			totalImportPcRate = (int) invoice.getImportPcRate();
			totalImportOcRate = (int) invoice.getImportOcRate();
			totalHolidaySubNop = invoice.getHolidaySubNop();
			if (invoice.getImportNop() == 1) {
				totalIgst += invoice.getHolidaySubNop();
			} else {
				totalCgst = invoice.getHolidaySubNop() / 2;
				totalSgst = invoice.getHolidaySubNop() / 2;
			}
		}
		invoicePackages.setExportNop(totalExportNop);
		invoicePackages.setExportRate(totalExportRate);
		invoicePackages.setExportSubNop(totalExportSubNop);
		invoicePackages.setExportSubRate(totalExportSubRate);
		invoicePackages.setImportNop(totalImportNop);
		invoicePackages.setImportRate(totalImportRate);
		invoicePackages.setImportSubNop(totalImportSubNop);
		invoicePackages.setImportSubRate(totalImportSubRate);
		invoicePackages.setHolidayRate(totalHolidayRate);
		invoicePackages.setDemuragesRate(totalDemuragesRate);
		invoicePackages.setExportSplCartRate(totalExportSplCartRate);
		invoicePackages.setExportHpRate(totalExportHpRate);
		invoicePackages.setExportPcRate(totalExportPcRate);
		invoicePackages.setExportOcRate(totalExportOcRate);
		invoicePackages.setImportSplCartRate(totalImportSplCartRate);
		invoicePackages.setImportHpRate(totalImportHpRate);
		invoicePackages.setImportPcRate(totalImportPcRate);
		invoicePackages.setImportOcRate(totalImportOcRate);
		invoicePackages.setHolidaySubNop(totalHolidaySubNop);

		invoicePackages.setDemuragesNop(totalIgst);

		invoicePackages.setCompanyId(String.valueOf(totalCgst));
		invoicePackages.setBranchId(String.valueOf(totalSgst));

		return invoicePackages;
	}
	

//	public List<InvoicePackages> getData(String cid, String bid, String sdate, String edate, String pid)
//			throws ParseException {
//
//		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)");
//
//		Date startDate = null;
//		Date endDate = null;
//		startDate = dateFormat.parse(sdate);
//		endDate = dateFormat.parse(edate);
//		System.out.println(cid + "" + bid + "" + startDate + "" + endDate + "" + pid);
//		List<InvoicePackages> invoicePackages = new ArrayList<>();
//		List<InvoicePackages> total = new ArrayList<>();
//		if (pid == null || pid == "Null" || pid.equals("null") || pid.equals("Select Party")) {
//			invoicePackages = InvoicePackagesRepositary.findInvoicesBetweenDatesAndConditionsAll(startDate, endDate,
//					cid, bid);
//
//		} else {
//			invoicePackages = InvoicePackagesRepositary.findInvoicesBetweenDatesAndConditions(startDate, endDate, cid,
//					bid, pid);
//		}
//
//		// System.out.println(invoicePackages);
//		Map<String, List<InvoicePackages>> groupedByPartyId = invoicePackages.stream()
//				.collect(Collectors.groupingBy(InvoicePackages::getPartyId));
//
//		for (Map.Entry<String, List<InvoicePackages>> entry : groupedByPartyId.entrySet()) {
//			String partyId = entry.getKey();
//			List<InvoicePackages> packagesForParty = entry.getValue();
//			System.out.println("packagesForParty " + packagesForParty);
//			InvoicePackages addPackages = new InvoicePackages();
//			for (InvoicePackages packages : packagesForParty) {
//				addPackages.setPartyId(packages.getPartyId());
//				addPackages.setCompanyId(packages.getCompanyId());
//				addPackages.setBranchId(packages.getBranchId());
//				addPackages.setSrNo(packages.getSrNo());
//				addPackages.setInvoiceNO(packages.getInvoiceNO());
//				addPackages.setBillNO(packages.getBillNO());
//				addPackages.setInvoiceDate(packages.getInvoiceDate());
//
//				addPackages.setExportNop(addPackages.getExportNop() + packages.getExportNop());
//				addPackages.setExportRate(addPackages.getExportRate() + packages.getExportRate());
//				addPackages.setExportSubNop(addPackages.getExportSubNop() + packages.getExportSubNop());
//				addPackages.setExportSubRate(addPackages.getExportSubRate() + packages.getExportSubRate());
//
//				addPackages.setImportNop(addPackages.getImportNop() + packages.getImportNop());
//				addPackages.setImportRate(addPackages.getImportRate() + packages.getImportRate());
//				addPackages.setImportSubNop(addPackages.getImportSubNop() + packages.getImportSubNop());
//				addPackages.setImportSubRate(addPackages.getImportSubRate() + packages.getImportSubRate());
//
//				addPackages.setHolidayRate(addPackages.getHolidayRate() + packages.getHolidayRate());
//				addPackages.setHolidaySubNop(addPackages.getHolidaySubNop() + packages.getHolidaySubNop());
//
//				addPackages.setExportSplCartNop(addPackages.getExportSplCartNop() + packages.getExportSplCartNop());
//				addPackages.setExportSplCartRate(addPackages.getExportSplCartRate() + packages.getExportSplCartRate());
//				addPackages.setExportHpNop(addPackages.getExportHpNop() + packages.getExportHpNop());
//				addPackages.setExportHpRate(addPackages.getExportHpRate() + packages.getExportHpRate());
//				addPackages.setExportPcNop(addPackages.getExportPcNop() + packages.getExportPcNop());
//				addPackages.setExportPcRate(addPackages.getExportPcRate() + packages.getExportPcRate());
//				addPackages.setExportOcNop(addPackages.getExportOcNop() + packages.getExportOcNop());
//				addPackages.setExportOcRate(addPackages.getExportOcRate() + packages.getExportOcRate());
//
//				addPackages.setImportSplCartNop(addPackages.getImportSplCartNop() + packages.getImportSplCartNop());
//				addPackages.setImportSplCartRate(addPackages.getImportSplCartRate() + packages.getImportSplCartRate());
//				addPackages.setImportHpNop(addPackages.getImportHpNop() + packages.getImportHpNop());
//				addPackages.setImportHpRate(addPackages.getImportHpRate() + packages.getImportHpRate());
//				addPackages.setImportPcNop(addPackages.getImportPcNop() + packages.getImportPcNop());
//				addPackages.setImportPcRate(addPackages.getImportPcRate() + packages.getImportPcRate());
//				addPackages.setImportOcNop(addPackages.getImportOcNop() + packages.getImportOcNop());
//				addPackages.setImportOcRate(addPackages.getImportOcRate() + packages.getImportOcRate());
//				addPackages.setDemuragesNop(addPackages.getDemuragesNop() + packages.getDemuragesNop());
//				addPackages.setDemuragesRate(addPackages.getDemuragesRate() + packages.getDemuragesRate());
//				// System.out.println(packages.getExportRate()); // You can customize how you
//				// want to print the
//				// InvoicePackages
//			}
//			total.add(addPackages);
//		}
//
//		System.out.println("total " + total);
//
//		Map<String, Double> statusMap = new HashMap<>();
//		int temp = 1;
//		for (InvoicePackages invoicePackages2 : total) {
////			System.out.println("Party ID: " + invoicePackages2.getPartyId());
//
//			List<InvoiceMain> main = invoiceRepositary.findInvoicesMainBetweenDatesAndConditions(startDate, endDate,
//					cid, bid, invoicePackages2.getPartyId());
//			String status = null;
//			double tax = 0;
//			for (InvoiceMain inMain : main) {
//
//				status = inMain.getIgst();
//				tax = inMain.getTaxAmount();
//			}
//
//			if (status == "Y" || status.equals("Y")) {
//				invoicePackages2.setCompanyId("1");
//
//			} else {
//				invoicePackages2.setCompanyId("0");
//
//			}
//			invoicePackages2.setBranchId(String.valueOf(tax));
//		}
//		return total;
//
//	}
	

	

	
}
