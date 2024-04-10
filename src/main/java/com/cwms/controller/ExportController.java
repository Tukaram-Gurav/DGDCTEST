package com.cwms.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.cwms.entities.Branch;
import com.cwms.entities.Company;
import com.cwms.entities.DefaultPartyDetails;
import com.cwms.entities.Export;
import com.cwms.entities.ExportAutoIncrement;
import com.cwms.entities.ExportHeavyPackage;
import com.cwms.entities.ExportSub;
import com.cwms.entities.Export_History;
import com.cwms.entities.ExternalParty;
import com.cwms.entities.Gate_In_Out;
import com.cwms.entities.Import;
import com.cwms.entities.ImportSub;
import com.cwms.entities.InvoiceMain;
import com.cwms.entities.PCGatePass;
import com.cwms.entities.Party;
import com.cwms.entities.ReadURL;
import com.cwms.entities.RepresentParty;
import com.cwms.entities.ScanData;
import com.cwms.entities.ScannedParcels;
import com.cwms.helper.FileUploadProperties;
import com.cwms.repository.BranchRepo;
import com.cwms.repository.CompanyRepo;
import com.cwms.repository.DefaultParyDetailsRepository;
import com.cwms.repository.ExportAutoIncrementRepo;
import com.cwms.repository.ExportHeavyPackageRepo;
import com.cwms.repository.ExportRepository;
import com.cwms.repository.ExportSubRepository;
import com.cwms.repository.Export_HistoryRepository;
import com.cwms.repository.ExternalPartyRepository;
import com.cwms.repository.Gate_In_out_Repo;
import com.cwms.repository.ImportRepository;
import com.cwms.repository.ImportSubRepository;
import com.cwms.repository.InvoiceRepositary;
import com.cwms.repository.PartyRepository;
import com.cwms.repository.ProcessNextIdRepository;
import com.cwms.repository.RepresentPartyRepository;
import com.cwms.repository.ScannedParcelsRepo;
import com.cwms.service.ExportService;
import com.cwms.service.ImageService;
import com.cwms.service.ProcessNextIdService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Image;

import jakarta.transaction.Transactional;

@CrossOrigin("*")
@RequestMapping("/export")
@RestController
public class ExportController {
	
//	@Autowired
//	private ExportAutoIncrementRepo exportautoincrementrepo;
	
	@Autowired
	private CompanyRepo companyRepo;

	@Autowired
	private BranchRepo branchRepo;

	@Autowired
	public DefaultParyDetailsRepository defaultrepo;

	@Autowired(required = true)
	public ProcessNextIdRepository processNextIdRepository;

	@Autowired
	private Gate_In_out_Repo gateinoutrepo;

	@Autowired
	private RepresentPartyRepository representPartyRepository;

	@Autowired
	public InvoiceRepositary invoiceRepository;

	@Autowired
	private ImportRepository importRepository;

	@Autowired
	private ExportSubRepository exportSubRepository;

	@Autowired
	private ImportSubRepository importSubRepository;

	@Autowired
	private ExportRepository exportRepository;

	@Autowired
	private ScannedParcelsRepo scannedparcelsrepo;

	@Autowired
	private ExportService sbTransactionService;

	@Autowired
	private Export_HistoryRepository export_HistoryRepository;

	@Autowired
	public FileUploadProperties FileUploadProperties;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ProcessNextIdService processNextIdService;
	@Autowired
	private ExternalPartyRepository externalPartyRepository;

	@Autowired
	private PartyRepository partyrepo;

	@Autowired
	private RepresentPartyRepository representReo;

	@Autowired
	private ExportHeavyPackageRepo eexportHeavyRepo;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private com.cwms.service.ExternalParty_Service ExternalParty_Service;

	public synchronized String autoid() {

		String id = processNextIdService.autoIncrementSIRExportId();
		return id;
	}
	
	
	
	
//	reverseToStock

	@GetMapping("/reverseToStock")
	public ResponseEntity<?> reverseToStock(@RequestParam("cid") String cid, @RequestParam("bid") String bid,
			@RequestParam("user") String user, @RequestParam("serNo") String serNo,
			@RequestParam("request_id") String request_id, @RequestParam("dgdcStatus") String dgdcStatus,
			@RequestParam("sbNo") String sbNo, @RequestParam("exporter") String exporter) {

	
		List<String> validStatuses = List.of("Handed over to Carting Agent", "Exit from DGDC SEEPZ Gate",
				"Entry at DGDC Cargo GATE", "Handed over to DGDC Cargo");

		if (!validStatuses.contains(dgdcStatus)) {

			System.out.println("dgdc_status is valid: " + dgdcStatus);

			return ResponseEntity.ok("Parcel is not applicable for Reverse to Stock");
		}

		int firedExport = exportRepository.updateReverseToStock(cid, bid, request_id, sbNo, serNo, exporter, user);

//	    int firedExportHistory = exportRepository.updateReverseToStockHistory(cid, bid, request_id, sbNo, serNo);
		int firedExportGateInOut = exportRepository.updateReverseToStockGateInOut(cid, bid, request_id, sbNo);

		Export_History exportHistory = new Export_History();

		exportHistory.setCompanyId(cid);
		exportHistory.setBranchId(bid);
		exportHistory.setserNo(serNo);
		exportHistory.setSbRequestId(request_id);
		exportHistory.setTransport_Date(new Date());
		exportHistory.setRemark("Reveresed");
		exportHistory.setOldStatus(dgdcStatus);
		exportHistory.setNewStatus("Handed over to DGDC SEEPZ");
		exportHistory.setUpdatedBy(user);
		exportHistory.setSbNo(sbNo);

		Export_History save = export_HistoryRepository.save(exportHistory);
		System.out.println("Export History");
		System.out.println(save);
		System.out.println("firedExport " + firedExport);
		System.out.println("firedExportGateInOut " + firedExportGateInOut);

		return ResponseEntity.ok("Parcel Is added to stock");

	}

//	Todays Tp No
	@GetMapping("/addToExistingTpGetTpNo")
	public ResponseEntity<?> addToExistingTp(@RequestParam("cid") String cid, @RequestParam("bid") String bid) {

		List<String> gettpNos = exportRepository.gettpNos(cid, bid);
		
		System.out.println("Getting List");
		System.out.println(gettpNos);
		
		return ResponseEntity.ok(gettpNos);
	}

//	Todays PCTMNO
	@GetMapping("/addToExistingPCTMGet")
	public ResponseEntity<?> addToExistingPCTMGet(@RequestParam("cid") String cid, @RequestParam("bid") String bid,
			@RequestParam("tpNo") String tpNo, @RequestParam("airlineCode") String airlineCode) {

		List<String> getPCTMNO = exportRepository.getPCTMNO(cid, bid,tpNo,airlineCode);
		
		return ResponseEntity.ok(getPCTMNO);
	}
	
	
//	Todays PCTMNO
	@GetMapping("/updateFinalPctmAndTpNoExport")
	public ResponseEntity<?> updateFinalPctmAndTpNoExport(@RequestParam("cid") String cid, @RequestParam("bid") String bid,
			@RequestParam("user") String user, @RequestParam("serNo") String serNo, @RequestParam("exporter") String exporter,
			@RequestParam("request_id") String request_id,@RequestParam("sbNo") String sbNo,@RequestParam("pctmNo") String pctmNo,@RequestParam("tpNo") String tpNo) 
	{

		
		Map<String, String> findCartingAgentAndRepresentative = exportRepository.findCartingAgentAndRepresentative(cid, bid, tpNo);
		String cartingAgent = "";
		String partyRepresentative = "";
		
		
		if (findCartingAgentAndRepresentative != null) {
			cartingAgent = findCartingAgentAndRepresentative.get("cartingAgent");
			partyRepresentative = findCartingAgentAndRepresentative.get("partyRepresentativeId");

		    // Now you can work with these values as needed
		    System.out.println("Carting Agent: " + cartingAgent);
		    System.out.println("Party Representative ID: " + partyRepresentative);
		}		
		
		int getPCTMNO = exportRepository.updateExistingTpNo(cid, bid, request_id, sbNo, serNo, pctmNo, tpNo, user, exporter, cartingAgent, partyRepresentative);
		
		
		if(getPCTMNO == 1)
		{
		Export_History exportHistory = new Export_History();

		exportHistory.setCompanyId(cid);
		exportHistory.setBranchId(bid);
		exportHistory.setserNo(serNo);
		exportHistory.setSbRequestId(request_id);
		exportHistory.setTransport_Date(new Date());
		exportHistory.setRemark("Existing TP");
		exportHistory.setOldStatus("Handed over to DGDC SEEPZ");
		exportHistory.setNewStatus("Handed over to Carting Agent");
		exportHistory.setUpdatedBy(user);
		exportHistory.setSbNo(sbNo);

		export_HistoryRepository.save(exportHistory);
		
		
		}
		
		
		
		
		System.out.println("getPCTMNO : " + getPCTMNO);
		return ResponseEntity.ok(getPCTMNO);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@GetMapping("/byser/{cid}/{bid}/{ser}")
	public Export getdatabyser(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("ser") String ser) {
		return exportRepository.findBySer(cid, bid, ser);
	}

	@GetMapping("/exportData")
	public ResponseEntity<List<Export>> findAllExportData(@RequestParam String companyId, @RequestParam String branchId,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate

	) {

		List<Export> exportData = sbTransactionService.findAllExportData(companyId, branchId, startDate, endDate);

		System.out.println(exportData);

		return ResponseEntity.ok(exportData);
	}

	@GetMapping("/bysbsbreq/{cid}/{bid}/{sbreqid}/{sbno}")
	public Export bysbnoandreq(@PathVariable("cid") String companyId, @PathVariable("bid") String branchId,
			@PathVariable("sbreqid") String sbreqid, @PathVariable("sbno") String sbno) {
		Export export = exportRepository.findBySBNoandSbreqid(companyId, branchId, sbreqid, sbno);
		return export;
	}

	@PostMapping("/updatePCTMAndTPNo")
	public List<Export> updatePCTMAndTPNo(
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam("companyId") String companyId, @RequestParam("branchId") String branchId) {

		List<Export> exportDataToUpdate = exportRepository.findAllExportData(companyId, branchId, startDate, endDate);

		Map<String, String> airlinePctmMap = new HashMap<>();
		String exportTpNo = processNextIdService.getNextTPNo();
		for (Export export : exportDataToUpdate) {
			String airlineName = export.getAirlineName();

			export.setTpNo(exportTpNo);
			export.setTpDate(new Date());

			String pctmNo = airlinePctmMap.get(airlineName);

			if (pctmNo == null) {
				pctmNo = processNextIdService.getNextPctmNo();
				airlinePctmMap.put(airlineName, pctmNo);
			}

			// Set the generated pctmNo for the current Export record
			export.setPctmNo(pctmNo);
			export.setPctmDate(new Date());

			// Generate a new tpNo for each record

		}

		// Save the updated records
		exportRepository.saveAll(exportDataToUpdate);

		return exportDataToUpdate;
	}

	@GetMapping("/carting-agents")
	public List<String> getDistinctCartingAgents(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(serDate);

		List<String> cartingAgents = exportRepository.findAllCartingAgentNames(companyId, branchId, formattedDate);

//		for (String cr : cartingAgents) {
//			System.out.println(cr);
//		}
		return cartingAgents;
	}

//	@GetMapping("/tpNumbers")
//	public List<String> getDistinctTpNumbers(@RequestParam("companyId") String companyId,
//			@RequestParam("branchId") String branchId,
//			@RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
//			@RequestParam("cartingAgent") String cartingAgent) {
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String formattedDate = sdf.format(serDate);
//
//		List<String> tpNumbers = exportRepository.findDistinctTpNos(companyId, branchId, formattedDate, cartingAgent);
//
////		for (String cr : tpNumbers) {
////			System.out.println(cr);
////		}
//		return tpNumbers;
//	}
	
	
	@GetMapping("/tpNumbers")
	public List<String> getDistinctTpNumbers(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
			@RequestParam("cartingAgent") String cartingAgent) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(serDate);

		List<String> tpNumbers = exportRepository.findCommonTpNos(companyId, branchId, formattedDate, cartingAgent);

//		for (String cr : tpNumbers) {
//			System.out.println(cr);
//		}
		return tpNumbers;
	}


	@GetMapping("/exportByTpNo")
	public List<Export> getExportDataByTpNo(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
			@RequestParam("cartingAgent") String cartingAgent, @RequestParam("tpNo") String tpNo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(serDate);

		List<Export> tpDataAll = exportRepository.findImportTPData(companyId, branchId, formattedDate, cartingAgent,
				tpNo);
		// Call the repository method with the given parameters
		for (Export cr : tpDataAll) {
			System.out.println(cr);
		}
		return tpDataAll;
	}

	@GetMapping("/all/{companyId}/{branchId}")
	public List<Export> getAll1(@PathVariable("companyId") String companyId,
			@PathVariable("branchId") String branchId) {
		return this.exportRepository.findByAll(companyId, branchId);
	}

	@GetMapping("/tpNo")
	public List<String> getAllbytpdate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			@RequestParam("cid") String cid, @RequestParam("bid") String bid) {

		return exportRepository.findByTp(date, cid, bid);
	}

//	@GetMapping("/getalldata")
//	public List<Export> getallbyTpnoandTpdate(@RequestParam("cid") String cid, @RequestParam("bid") String bid,
//			@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("tpno") String tpno
//	// @RequestParam("status") char status
//	) { // Change the parameter name to "status"
//		return exportRepository.findByTpdateTpno(cid, bid, date, tpno); // Use "status" parameter here
//	}

	@GetMapping(value = "/getCartingAgent/{companyId}/{branchId}/{sirDate}")
	public List<ExternalParty> getCartingAgent(@PathVariable("companyId") String companyId,
			@PathVariable("branchId") String branchId,
			@PathVariable("sirDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(sirDate);
		List<String> cIds = exportRepository.findByCompanyAndBranchAndSerDate(companyId, branchId, formattedDate);

		List<ExternalParty> externalParties = new ArrayList<>();
		for (String string : cIds) {
			externalParties.add(externalPartyRepository.findBycompbranchexternal(companyId, branchId, string));
		}

		return externalParties;
	}

	@GetMapping(value = "/getExportTpList/{companyId}/{branchId}/{sirDate}/{exId}")
	public List<Export> getExportTpList(@PathVariable("companyId") String companyId,
			@PathVariable("branchId") String branchId,
			@PathVariable("sirDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate,
			@PathVariable("exId") String exId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(sirDate);

		List<Export> exports = exportRepository.findByCompanyAndBranchAndserDateAndexternalPId(companyId, branchId,
				formattedDate, exId);
		return exports;
	}

	@PostMapping(value = "/readurl")
	public Map<String, String> getlink(@RequestBody ReadURL readURL) throws IOException {
		String s = readURL.getLink();
		URL url = new URL(s);
		System.out.println(s);
		int timeoutMillis = 50000; // 5 seconds
		Document document = Jsoup.parse(url, timeoutMillis);
		Map<String, String> hashMap = new LinkedHashMap<>();

		Elements labels = document.select(".Label, .LabelHeader, .SubHeader");

		String currentKey = "";
		String Demo = "";
		String key = "";

		for (Element label : labels) {

			if (label.hasClass("LabelHeader") || label.hasClass("SubHeader")) {
				key = "";
				currentKey = label.text();
				currentKey = currentKey.toLowerCase();

				for (int i = 0; i < currentKey.length(); i++) {
					if ((int) currentKey.charAt(i) >= 97 && (int) currentKey.charAt(i) <= 122) {
						key += currentKey.charAt(i);
					}
				}

				if (label.text().equals("Consignment Details:")) {
					Demo = label.text();
					currentKey = Demo;
					hashMap.put(key, "");
				}

			} else {
				String value = label.text();

				hashMap.put(key, value);
			}
		}

		return hashMap;
	}

	@PostMapping(value = "/readurlSBD") // read url SBD read url Shipping Bill Details
	public Export getlinkToChange(@RequestBody ReadURL readURL) throws IOException {
		String s = readURL.getLink();
		URL url = new URL(s);
		// System.out.println(s);
		int timeoutMillis = 50000; // 5 seconds
		Document document = Jsoup.parse(url, timeoutMillis);
		Map<String, String> hashMap = new LinkedHashMap<>();

		Elements labels = document.select(".Label, .LabelHeader, .SubHeader");

		String currentKey = "";
		String Demo = "";
		String key = "";

		for (Element label : labels) {

			if (label.hasClass("LabelHeader") || label.hasClass("SubHeader")) {
				key = "";
				currentKey = label.text();
				currentKey = currentKey.toLowerCase();

				for (int i = 0; i < currentKey.length(); i++) {
					if ((int) currentKey.charAt(i) >= 97 && (int) currentKey.charAt(i) <= 122) {
						key += currentKey.charAt(i);
					}
				}

				if (label.text().equals("Consignment Details:")) {
					Demo = label.text();
					currentKey = Demo;
					hashMap.put(key, "");
				}

			} else {
				String value = label.text();

				hashMap.put(key, value);
			}
		}
		ScanData ScanData = new ScanData();
		Export sbTransaction = new Export();

		ScanData.setDcoffice(hashMap.get("dcoffice"));
		ScanData.setSezname(hashMap.get("sezname"));
		ScanData.setSezunitdevelopercodeveloper(hashMap.get("sezunitdevelopercodeveloper"));

		ScanData.setImportexportcode(hashMap.get("importexportcode"));
		sbTransaction.setIecCode(hashMap.get("importexportcode"));

		ScanData.setEntityid(hashMap.get("entityid"));
		sbTransaction.setEntityId(hashMap.get("entityid"));

		ScanData.setRequestdetails(hashMap.get("requestdetails"));
		sbTransaction.setDescriptionOfGoods(hashMap.get("requestdetails"));

		ScanData.setRequestid(hashMap.get("requestid"));
		sbTransaction.setSbRequestId(hashMap.get("requestid"));

		ScanData.setPortofloading(hashMap.get("portofloading"));

		ScanData.setPortofdestination(hashMap.get("portofdestination"));
		sbTransaction.setPortOfDestination(hashMap.get("portofdestination"));

		ScanData.setCountryofdestination(hashMap.get("countryofdestination"));
		sbTransaction.setCountryOfDestination(hashMap.get("countryofdestination"));

		ScanData.setSbnodate(hashMap.get("sbnodate"));
		String sbnodate = hashMap.get("sbnodate");
		String[] parts = sbnodate.split(",");
		String sbno = parts[0].trim();
		sbTransaction.setSbNo(sbno);

		String inputDateStr = parts[1].trim();

		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			// Parse the input date string
			Date inputDate = inputDateFormat.parse(inputDateStr);
			sbTransaction.setSbDate(inputDate);
////			System.out.println(
//					"===========================================================================================");
//			System.out.println(sbTransaction.getSbDate());
//			System.out.println(
//					"===========================================================================================");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ScanData.setCustomhouseagentnamecode(hashMap.get("customhouseagentnamecode"));
		sbTransaction.setChaName(hashMap.get("customhouseagentnamecode"));
		sbTransaction.setChaNo(hashMap.get("customhouseagentnamecode"));
		ScanData.setAssessmentdate(hashMap.get("assessmentdate"));

		ScanData.setRequeststatus(hashMap.get("requeststatus"));
		sbTransaction.setNsdlStatus(hashMap.get("requeststatus"));
		ScanData.setConsignmentdetails(hashMap.get("consignmentdetails"));

		ScanData.setRotationnumberdate(hashMap.get("rotationnumberdate"));

		ScanData.setCargodetails(hashMap.get("cargodetails"));

		String cargoDetails = hashMap.get("cargodetails");
		String[] parts1 = cargoDetails.split(",");

		// Create two String variables to store the split cargo details
		String cargoDetail1 = parts1[0].trim();
		String cargoDetail2 = parts1[1].trim();

		Pattern pattern = Pattern.compile("Weight:\\s*(\\d+\\.\\d+)\\s*(\\w+)");
		Matcher matcher = pattern.matcher(cargoDetail1);

		if (matcher.find()) {
			String value1 = matcher.group(1); // This will be "1700.0000"
			String value2 = matcher.group(2); // This will be "GRAMS"
			sbTransaction.setGrossWeight(Double.valueOf(value1));
			sbTransaction.setUomGrossWeight(value2);
		}

		cargoDetail2 = cargoDetail2.replace("Packets: ", "");

		String[] subparts = cargoDetail2.split("\\s+");

		if (subparts.length >= 2) {
			String value1 = subparts[0].trim(); // This will be "1"
			String value2 = subparts[1].trim(); // This will be "PACKAGES"
			sbTransaction.setNoOfPackages(Integer.parseInt(value1));
			sbTransaction.setUomOfPackages(value2);
		}

		ScanData.setNetrealisablevalueinrs(hashMap.get("netrealisablevalueinrs"));
		try {
			double doubleValue = Double.parseDouble(hashMap.get("netrealisablevalueinrs"));
			sbTransaction.setFobValueInINR(doubleValue);

		} catch (NumberFormatException e) {
			System.err.println("Error: Unable to convert the string to a double.");
			e.printStackTrace();
		}

		sbTransaction.setCurrentDate();
		sbTransaction.setCompanyId(readURL.getCompanyId());
		sbTransaction.setBranchId(readURL.getBranchId());

		sbTransaction.setNameOfExporter(ScanData.getSezname());
		sbTransaction.setCreatedBy(readURL.getCreatedBy());
		sbTransaction.setEditedBy(readURL.getEditedBy());
		sbTransaction.setApprovedBy(readURL.getApprovedBy());
		sbTransaction.setStatus("A");
		String id = autoid();
		sbTransaction.setSerNo(id);
		sbTransaction.setDgdcStatus("Handed over to DGDC SEEPZ");

//		System.out.println("----------------------------------------------------------");
//		System.out.println(ScanData);
//		System.out.println(sbTransaction);

		Export_History export_History = new Export_History(sbTransaction.getCompanyId(), sbTransaction.getBranchId(),
				sbTransaction.getSbNo(), sbTransaction.getSbRequestId(), sbTransaction.getSerNo(),
				sbTransaction.getCreatedBy(), "panding . .", sbTransaction.getDgdcStatus(), null);
		export_History.SetHistoryDate();
		export_HistoryRepository.saveAndFlush(export_History);

		return sbTransactionService.createSBTransaction(sbTransaction);
	}

	@GetMapping("/{sbTransId}")
	public Export getSBTransaction(@PathVariable String sbTransId) {

		// Implement the logic to retrieve a single SBTransaction by sbTransId.
		return sbTransactionService.getSBTransaction(sbTransId);
	}

	@PostMapping("/save1")
	public Export createSBTransaction(@RequestBody Export export) throws Exception {

		Export existingSub = exportRepository.findBySBNoandSbreqid(export.getCompanyId(), export.getBranchId(),
				export.getSbRequestId(), export.getSbNo());
		String id = autoid();
		if (existingSub != null) {
			throw new Exception("RequestId already present");
		}
		export.setSerNo(id);
		export.setSerDate(new Date());
		export.setAppovedate();
		if (export.getStatus() == "N") {
			export.setStatus("A");
		} else {
			export.setStatus("A");
		}
		export.setScStatus("N");
		export.setHpStatus("N");
		export.setPcStatus("N");
		export.setHoldStatus("N");
		export.setCancelStatus("N");
		export.setNoc(0);
		export.setDgdc_cargo_in_scan(0);
		export.setDgdc_cargo_out_scan(0);
		export.setDgdc_seepz_in_scan(0);
		export.setDgdc_seepz_out_scan(0);
		export.setDgdcStatus("Handed over to DGDC SEEPZ");
		export.setNsdlStatus("Pending");
		BigDecimal big = new BigDecimal("0.0");
		export.setImposePenaltyAmount(0);
		sbTransactionService.createSBTransaction(export);
		Export_History export_History = new Export_History(export.getCompanyId(), export.getBranchId(),
				export.getSbNo(), export.getSbRequestId(), export.getSerNo(), export.getCreatedBy(), "Pending",
				export.getDgdcStatus(), null);
		export_History.SetHistoryDate();
		export_HistoryRepository.save(export_History);

		for (int i = 1; i <= export.getNoOfPackages(); i++) {
			String srNo = String.format("%04d", i);
			Gate_In_Out gateinout = new Gate_In_Out();
			gateinout.setCompanyId(export.getCompanyId());
			gateinout.setBranchId(export.getBranchId());
			gateinout.setNop(export.getNoOfPackages());
			gateinout.setErp_doc_ref_no(export.getSbRequestId());
			gateinout.setDoc_ref_no(export.getSbNo());
			gateinout.setSr_No(export.getSerNo() + srNo);
			gateinout.setDgdc_cargo_in_scan("N");
			gateinout.setDgdc_cargo_out_scan("N");
			gateinout.setDgdc_seepz_in_scan("N");
			gateinout.setDgdc_seepz_out_scan("N");

			gateinoutrepo.save(gateinout);
		}

		return export;
	}

	
	
//	@PostMapping("/save1")
//	public Export createSBTransaction(@RequestBody Export export) throws Exception {
//
//		Export existingSub = exportRepository.findBySBNoandSbreqid(export.getCompanyId(), export.getBranchId(),
//				export.getSbRequestId(), export.getSbNo());
//
//		if (existingSub != null) {
//			throw new Exception("RequestId already present");
//		}
//		else {
//			ExportAutoIncrement data = new ExportAutoIncrement();
//			data.setSbNo(export.getSbNo());
//			data.setSbRequestId(export.getSbRequestId());
//			exportautoincrementrepo.save(data);
//
//			int nextId = exportautoincrementrepo.getId(export.getSbNo(), export.getSbRequestId());
//			export.setSerNo(generateFormattedString(nextId));
//			export.setSerDate(new Date());
//			export.setAppovedate();
//			if (export.getStatus() == "N") {
//				export.setStatus("A");
//			} else {
//				export.setStatus("A");
//			}
//			export.setScStatus("N");
//			export.setHpStatus("N");
//			export.setPcStatus("N");
//			export.setHoldStatus("N");
//			export.setCancelStatus("N");
//			export.setNoc(0);
//			export.setDgdc_cargo_in_scan(0);
//			export.setDgdc_cargo_out_scan(0);
//			export.setDgdc_seepz_in_scan(0);
//			export.setDgdc_seepz_out_scan(0);
//			export.setDgdcStatus("Handed over to DGDC SEEPZ");
//			export.setNsdlStatus("Pending");
//			BigDecimal big = new BigDecimal("0.0");
//			export.setImposePenaltyAmount(0);
//			sbTransactionService.createSBTransaction(export);
//			Export_History export_History = new Export_History(export.getCompanyId(), export.getBranchId(),
//					export.getSbNo(), export.getSbRequestId(), export.getSerNo(), export.getCreatedBy(), "Pending",
//					export.getDgdcStatus(), null);
//			export_History.SetHistoryDate();
//			export_HistoryRepository.save(export_History);
//
//			for (int i = 1; i <= export.getNoOfPackages(); i++) {
//				String srNo = String.format("%04d", i);
//				Gate_In_Out gateinout = new Gate_In_Out();
//				gateinout.setCompanyId(export.getCompanyId());
//				gateinout.setBranchId(export.getBranchId());
//				gateinout.setNop(export.getNoOfPackages());
//				gateinout.setErp_doc_ref_no(export.getSbRequestId());
//				gateinout.setDoc_ref_no(export.getSbNo());
//				gateinout.setSr_No(export.getSerNo() + srNo);
//				gateinout.setDgdc_cargo_in_scan("N");
//				gateinout.setDgdc_cargo_out_scan("N");
//				gateinout.setDgdc_seepz_in_scan("N");
//				gateinout.setDgdc_seepz_out_scan("N");
//
//				gateinoutrepo.save(gateinout);
//			}
//		}
//
//		return export;
//	}
	
	
	
	
	
	
	
	
	@PostMapping("/submit")
	public Export createSBTransactionSubmit(@RequestBody Export sbTransaction) {
		sbTransaction.setAppovedate();
		sbTransaction.setStatus("A");

//		Export_History export_History = new Export_History(sbTransaction.getCompanyId(), sbTransaction.getBranchId(),
//				sbTransaction.getSbNo(), sbTransaction.getSbRequestId(), sbTransaction.getSerNo(),
//				sbTransaction.getCreatedBy(), "panding . .", sbTransaction.getDgdcStatus(), null);
//		export_History.SetHistoryDate();
//		export_HistoryRepository.saveAndFlush(export_History);

		return sbTransactionService.createSBTransaction(sbTransaction);

	}

	@PostMapping("/delete")
	public Export ToSetStatusD(@RequestBody Export sbTransaction) { // Implement the logic to

		sbTransaction.setStatus("D");
		System.out.println(sbTransaction);
		return sbTransactionService.createSBTransaction(sbTransaction);
	}

	@GetMapping(value = "/listSBTransaction/{cid}/{bid}")
	public List<Export> getListOfSBTransaction(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {

		return exportRepository.findAllData(cid, bid);
	}

	@GetMapping(value = "/listSBTransaction1/{cid}/{bid}")
	public List<Export> getListOfSBTransaction1(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {

		return exportRepository.findAllData1(cid, bid);
	}

	@GetMapping("/filtered/{company_id}/{branch_id}")
	public List<Export> getFilteredExports(@PathVariable("company_id") String companyId,
			@PathVariable("branch_id") String branchId) {
		return sbTransactionService.getExportsByCompanyAndBranch(companyId, branchId);
	}

	@GetMapping("/getExportHistoryList/{company_id}/{branch_id}/{sbno}/{sbRId}/{ser}")
	public List<Export_History> getFilteredExportsHistory(@PathVariable("company_id") String companyId,
			@PathVariable("branch_id") String branchId, @PathVariable("sbno") String sbno,
			@PathVariable("sbRId") String sbRId, @PathVariable("ser") String ser) {
		return export_HistoryRepository.findRecordsByCriteria1(branchId, companyId, sbno, sbRId);
	}

	@DeleteMapping("/{sbTransId}")
	public void deleteSBTransaction(@PathVariable String sbTransId) {
		sbTransactionService.deleteSBTransaction(sbTransId);
	}

	@PostMapping(value = "/updateExportC_A/{cartingAgent}/{respectiveId}")
	public String getUpdateExportC_A(@RequestBody List<Export> items, @PathVariable("cartingAgent") String cartingAgent,
			@PathVariable("respectiveId") String respectiveId) {

		for (Export export : items) {
			export.setPartyRepresentativeId(respectiveId);
			export.setCartingAgent(cartingAgent);

			Export_History export_History = new Export_History(export.getCompanyId(), export.getBranchId(),
					export.getSbNo(), export.getSbRequestId(), export.getSerNo(), export.getCreatedBy(),
					export.getDgdcStatus(), "Handed over To Carting Agent", null);

			export_History.SetHistoryDate();

			export_HistoryRepository.saveAndFlush(export_History);

			export.setDgdcStatus("Hand Over To Carting Agent");

		}
		exportRepository.saveAll(items);

		return "Export Update Successfully.";
	}

	@GetMapping("/getExportsBySbDate/{companyid}/{branchId}/{SbDate}/{dgdcStatus}")
	public List<Export> getExportsByFormattedSbDate(@PathVariable("companyid") String companyId,
			@PathVariable("branchId") String branchId,
			@PathVariable("SbDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date sbdate,
			@PathVariable("dgdcStatus") String status) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(sbdate);
		List<Export> exports = sbTransactionService.findRecordsByFormattedSbDate(formattedDate, companyId, branchId,
				status);
		return exports;
	}

	@GetMapping(value = "/findCompanyname/{cid}")
	public String findCompanyname(@PathVariable("cid") String param) {
		Company company = companyRepo.findByCompany_Id(param);

		return company.getCompany_name();
	}

	@GetMapping(value = "/findBranchName/{cid}/{bid}")
	public String findBranchName(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {
		Branch branch = branchRepo.findByBranchIdWithCompanyId(cid, bid);
		return branch.getBranchName();
	}// exportcontroller

	@GetMapping("/getalldataa")
	public List<Export> getalldata() {
		return exportRepository.findAll();
	}

	@PostMapping(value = "/readgateinurl/{date}")
	public String getlinkforgateIn(@PathVariable("date") String startdate, @RequestBody ReadURL readURL)
			throws IOException {
		String s = readURL.getLink();
		URL url = new URL(s);
		System.out.println(s);
		int timeoutMillis = 50000; // 5 seconds
		Document document = Jsoup.parse(url, timeoutMillis);
		Map<String, String> hashMap = new LinkedHashMap<>();

		Elements labels = document.select(".Label, .LabelHeader, .SubHeader");

		String currentKey = "";
		String Demo = "";
		String key = "";

		for (Element label : labels) {

			if (label.hasClass("LabelHeader") || label.hasClass("SubHeader")) {
				key = "";
				currentKey = label.text();
				currentKey = currentKey.toLowerCase();

				for (int i = 0; i < currentKey.length(); i++) {
					if ((int) currentKey.charAt(i) >= 97 && (int) currentKey.charAt(i) <= 122) {
						key += currentKey.charAt(i);
					}
				}

				if (label.text().equals("Consignment Details:")) {
					Demo = label.text();
					currentKey = Demo;
					hashMap.put(key, "");
				}

			} else {
				String value = label.text();

				hashMap.put(key, value);
			}
		}

		// List<Export> export2 = exportRepository.findAllData(readURL.getCompanyId(),
		// readURL.getBranchId());
		String sbnodate1 = hashMap.get("sbnodate");
		String[] parts2 = sbnodate1.split(",");
		String sbno1 = parts2[0].trim();
		boolean found = false;
		Export exp = exportRepository.findDatabyreq(readURL.getCompanyId(), readURL.getBranchId(),
				hashMap.get("requestid"));
		if (exp != null) {
			found = true;

		}

		if (found) {

			return "found";
		}

		Party party = partyrepo.findbyentityid(readURL.getCompanyId(), readURL.getBranchId(), hashMap.get("entityid"));

		Party party1 = partyrepo.getdatabyid1(party.getCompanyId(), party.getBranchId(), party.getPartyId(), startdate);

//		 if(party1 == null) {
//		       
//		      return "Y";
//		      
//		       
//		 }

		Export export = new Export();
		export.setSerNo("");

		export.setCompanyId(readURL.getCompanyId());
		export.setSbRequestId(hashMap.get("requestid"));
		export.setBranchId(readURL.getBranchId());
		export.setGateInId(processNextIdService.autoIncrementGateInId());
		export.setGateInDate(new Date());
		export.setNoc(0);
		export.setDgdc_cargo_in_scan(0);
		export.setDgdc_cargo_out_scan(0);
		export.setDgdc_seepz_in_scan(0);
		export.setDgdc_seepz_out_scan(0);
		String sbnodate = hashMap.get("sbnodate");
		String[] parts = sbnodate.split(",");
		String sbno = parts[0].trim();
		export.setSbNo(sbno);

		String inputDateStr = parts[1].trim();

		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {

			Date inputDate = inputDateFormat.parse(inputDateStr);
			export.setSbDate(inputDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		String inputString = hashMap.get("customhouseagentnamecode");
		String s1 = "";
		String s2 = "";

		if (inputString.contains(" - ")) {
			String[] parts1 = inputString.split(" - ", 2); // Split into two parts at the first " - "
			s1 = parts1[0];
			s2 = parts1[1];
			export.setChaName(hashMap.get(s1));
			export.setChaNo(hashMap.get(s2));
		} else {
			s1 = inputString;
			export.setChaName(s1);
			export.setChaNo("");
		}

//	    	DefaultPartyDetails defaultparty = defaultrepo.getdatabyuser_id(readURL.getCompanyId(),readURL.getBranchId(),party.getPartyId());
		DefaultPartyDetails defaultparty = defaultrepo.getdatabyuser_id(readURL.getCompanyId(), readURL.getBranchId(),
				party.getPartyId());
		System.out.println("defaultparty " + defaultparty);

		if (defaultparty == null) {
			// Handle the case when defaultparty is null or both expCHA and expConsole are
			// null or empty
			export.setChaNo("EU0021");
			export.setChaName("SELF");
			export.setConsoleAgent("EU0009");
		} else if ((defaultparty.getExpCHA() == null && defaultparty.getExpConsole() == null)
				|| (defaultparty.getExpCHA().isEmpty() && defaultparty.getExpConsole().isEmpty())) {
			export.setChaNo("EU0021");
			export.setChaName("SELF");
			export.setConsoleAgent("EU0009");
		} else if (defaultparty.getExpCHA() == null || defaultparty.getExpCHA().isEmpty()) {
			// Handle the case when expCHA is null or empty
			export.setChaNo("EU0021");
			export.setChaName("SELF");
			export.setConsoleAgent(defaultparty.getExpConsole());
		} else if (defaultparty.getExpConsole() == null || defaultparty.getExpConsole().isEmpty()) {
			// Handle the case when expConsole is null or empty
			ExternalParty external = externalPartyRepository.getalldatabyid(readURL.getCompanyId(),
					readURL.getBranchId(), defaultparty.getExpCHA());
			export.setChaNo(defaultparty.getExpCHA());
			export.setChaName(external.getUserName());
			export.setConsoleAgent("EU0009");
		} else {
			// Handle the case when both expCHA and expConsole have values
			ExternalParty external = externalPartyRepository.getalldatabyid(readURL.getCompanyId(),
					readURL.getBranchId(), defaultparty.getExpCHA());
			export.setChaNo(defaultparty.getExpCHA());
			export.setChaName(external.getUserName());
			export.setConsoleAgent(defaultparty.getExpConsole());
		}

		export.setCountryOfDestination(hashMap.get("countryofdestination"));
		export.setDescriptionOfGoods(hashMap.get("requestdetails"));
		export.setEntityId(hashMap.get("entityid"));
		try {
			double doubleValue = Double.parseDouble(hashMap.get("netrealisablevalueinrs"));
			export.setFobValueInINR(doubleValue);

		} catch (NumberFormatException e) {
			System.err.println("Error: Unable to convert the string to a double.");
			e.printStackTrace();
		}

		String cargoDetails = hashMap.get("cargodetails");
		String[] parts1 = cargoDetails.split(",");

		// Create two String variables to store the split cargo details
		String cargoDetail1 = parts1[0].trim();
		String cargoDetail2 = parts1[1].trim();

		Pattern pattern = Pattern.compile("Weight:\\s*(\\d+\\.\\d+)\\s*(\\w+)");
		Matcher matcher = pattern.matcher(cargoDetail1);

		if (matcher.find()) {
			String value1 = matcher.group(1); // This will be "1700.0000"
			String value2 = matcher.group(2); // This will be "GRAMS"
			export.setGrossWeight(Double.valueOf(value1));
			export.setUomGrossWeight(value2);
		}

		cargoDetail2 = cargoDetail2.replace("Packets: ", "");

		String[] subparts = cargoDetail2.split("\\s+");

		if (subparts.length >= 2) {
			String value1 = subparts[0].trim(); // This will be "1"
			String value2 = subparts[1].trim(); // This will be "PACKAGES"
			export.setNoOfPackages(Integer.parseInt(value1));
			export.setUomOfPackages(value2);
		}

		export.setIecCode(hashMap.get("importexportcode"));
		export.setNameOfExporter(party.getPartyId());
		export.setIecCode(party.getIecNo());
		export.setNsdlStatus(hashMap.get("requeststatus"));
		export.setPortOfDestination(hashMap.get("portofdestination"));
		export.setStatus("A");
		export.setCreatedBy(readURL.getCreatedBy());
		export.setCreatedDate(new Date());
		export.setApprovedBy(readURL.getCreatedBy());
		export.setApprovedDate(new Date());
		export.setDgdcStatus("Entry at DGDC SEEPZ Gate");
		export.setQrcodeUrl(readURL.getLink());
		export.setHoldStatus("N");
		export.setGatePassStatus("N");
		export.setScStatus("N");
		export.setPcStatus("N");
		export.setHpStatus("N");
		export.setCancelStatus("N");
		BigDecimal big = new BigDecimal("0.0");
		export.setImposePenaltyAmount(0);

		this.exportRepository.save(export);

		Export_History export_History = new Export_History(export.getCompanyId(), export.getBranchId(),
				export.getSbNo(), export.getSbRequestId(), export.getSerNo(), export.getCreatedBy(), "Pending",
				export.getDgdcStatus(), null);
		export_History.SetHistoryDate();
		export_HistoryRepository.save(export_History);

		ScannedParcels scanparcels = new ScannedParcels();
		scanparcels.setBranchId(export.getBranchId());
		scanparcels.setCompanyId(export.getCompanyId());
		scanparcels.setDoc_Ref_No(export.getSbNo());
		scanparcels.setNop(export.getNoOfPackages());
		// scanparcels.setPacknum(packnum);
		scanparcels.setGateiout(new Date());
		scanparcels.setStatus("N");
		scanparcels.setParcel_type("");
		scanparcels.setParty(export.getNameOfExporter());
		scanparcels.setSrNo("");
		scanparcels.setTypeOfTransaction("Export-in");
		scanparcels.setScan_parcel_type("seepz");
		scannedparcelsrepo.save(scanparcels);
		if (party1 == null) {
			String op = "The LOA for the " + party.getPartyName() + " has expired.";
			return op;

		} else {
			return "success";
		}
	}

	@GetMapping("/alldata/{cid}/{bid}")
	public List<Export> getalldata(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {
		return exportRepository.findAllData(cid, bid);
	}

	@PostMapping("/existingdata/{id}")
	public ResponseEntity<Map<String, String>> updateExport(@RequestBody ReadURL readURL, @PathVariable("id") String id)
			throws IOException {
		String s = readURL.getLink();
		URL url = new URL(s);
		System.out.println(s);
		int timeoutMillis = 50000; // 5 seconds
		Document document = Jsoup.parse(url, timeoutMillis);
		Map<String, String> hashMap = new LinkedHashMap<>();

		Elements labels = document.select(".Label, .LabelHeader, .SubHeader");

		String currentKey = "";
		String Demo = "";
		String key = "";

		for (Element label : labels) {

			if (label.hasClass("LabelHeader") || label.hasClass("SubHeader")) {
				key = "";
				currentKey = label.text();
				currentKey = currentKey.toLowerCase();

				for (int i = 0; i < currentKey.length(); i++) {
					if ((int) currentKey.charAt(i) >= 97 && (int) currentKey.charAt(i) <= 122) {
						key += currentKey.charAt(i);
					}
				}

				if (label.text().equals("Consignment Details:")) {
					Demo = label.text();
					currentKey = Demo;
					hashMap.put(key, "");
				}

			} else {
				String value = label.text();

				hashMap.put(key, value);
			}
		}

		String sbnodate = hashMap.get("sbnodate");
		String[] parts = sbnodate.split(",");
		String sbno = parts[0].trim();

		Export export = exportRepository.findBySBNoandSbreqid(readURL.getCompanyId(), readURL.getBranchId(),
				hashMap.get("requestid"), sbno);
		Export_History exphis = export_HistoryRepository.findSingledata(export.getCompanyId(), export.getBranchId(),
				export.getSbNo(), export.getSbRequestId(), export.getSerNo());
		export.setDgdcStatus("Handed over to DGDC SEEPZ");

		exportRepository.save(export);

		Export_History export_History = new Export_History(export.getCompanyId(), export.getBranchId(),
				export.getSbNo(), export.getSbRequestId(), export.getSerNo(), id, exphis.getNewStatus(),
				export.getDgdcStatus(), null);
		export_History.SetHistoryDate();
		export_HistoryRepository.save(export_History);

		return new ResponseEntity<>(hashMap, HttpStatus.OK);
	}

	@GetMapping("/holdStatus/{cid}/{bid}/{reqid}/{sbno}")
	public Export updateHoldStatus(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("reqid") String reqid, @PathVariable("sbno") String sbno) {
		Export export = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
		export.setHoldStatus("Y");
		return exportRepository.save(export);
	}

	@GetMapping("/unholdStatus/{cid}/{bid}/{reqid}/{sbno}")
	public Export updateUnHoldStatus(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("reqid") String reqid, @PathVariable("sbno") String sbno) {
		Export export = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
		export.setHoldStatus("N");
		return exportRepository.save(export);
	}

	@GetMapping("/specialStatus/{cid}/{bid}/{reqid}/{sbno}")
	public Export updateSpecialCartingStatus(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("reqid") String reqid, @PathVariable("sbno") String sbno) {
		Export export = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
		export.setScStatus("Y");
		return exportRepository.save(export);
	}

	@GetMapping("/cancelSpecialStatus/{cid}/{bid}/{reqid}/{sbno}")
	public Export updateCancelSpecialCartingStatus(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("reqid") String reqid, @PathVariable("sbno") String sbno) {
		Export export = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
		export.setScStatus("N");
		return exportRepository.save(export);
	}


	@GetMapping("/pcStatus/{cid}/{bid}/{reqid}/{sbno}")
	public Export updatePCStatus(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("reqid") String reqid, @PathVariable("sbno") String sbno) {
		Export export = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
	

		if (!export.getSerNo().startsWith("EX")) {
			String id = autoid();
			export.setSerNo(id);
			export.setSerDate(new Date());
			export.setDgdcStatus("Handed over to DGDC SEEPZ");

			if (export.getNoc() == 0) {
				for (int i = 1; i <= export.getNoOfPackages(); i++) {
					String srNo = String.format("%04d", i);
					Gate_In_Out gateinout = new Gate_In_Out();
					gateinout.setCompanyId(export.getCompanyId());
					gateinout.setBranchId(export.getBranchId());
					gateinout.setNop(export.getNoOfPackages());
					gateinout.setErp_doc_ref_no(export.getSbRequestId());
					gateinout.setDoc_ref_no(export.getSbNo());
					gateinout.setSr_No(id + srNo);
					gateinout.setDgdc_cargo_in_scan("N");
					gateinout.setDgdc_cargo_out_scan("N");
					gateinout.setDgdc_seepz_in_scan("N");
					gateinout.setDgdc_seepz_out_scan("N");

					gateinoutrepo.save(gateinout);
				}

				exportRepository.save(export);
			}
		} else {

			exportRepository.save(export);
		}
		export.setPcStatus("Y");
		return exportRepository.save(export);
	}
	
	
	
	
	
//	@GetMapping("/pcStatus/{cid}/{bid}/{reqid}/{sbno}")
//	public Export updatePCStatus(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
//			@PathVariable("reqid") String reqid, @PathVariable("sbno") String sbno) {
//		Export export = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
//
//		if (!export.getSerNo().startsWith("EX")) {
//			ExportAutoIncrement data = new ExportAutoIncrement();
//			data.setSbNo(export.getSbNo());
//			data.setSbRequestId(export.getSbRequestId());
//			exportautoincrementrepo.save(data);
//
//			int nextId = exportautoincrementrepo.getId(export.getSbNo(), export.getSbRequestId());
//			export.setSerNo(generateFormattedString(nextId));
//			export.setSerDate(new Date());
//			export.setDgdcStatus("Handed over to DGDC SEEPZ");
//
//			if (export.getNoc() == 0) {
//				for (int i = 1; i <= export.getNoOfPackages(); i++) {
//					String srNo = String.format("%04d", i);
//					Gate_In_Out gateinout = new Gate_In_Out();
//					gateinout.setCompanyId(export.getCompanyId());
//					gateinout.setBranchId(export.getBranchId());
//					gateinout.setNop(export.getNoOfPackages());
//					gateinout.setErp_doc_ref_no(export.getSbRequestId());
//					gateinout.setDoc_ref_no(export.getSbNo());
//					gateinout.setSr_No(generateFormattedString(nextId) + srNo);
//					gateinout.setDgdc_cargo_in_scan("N");
//					gateinout.setDgdc_cargo_out_scan("N");
//					gateinout.setDgdc_seepz_in_scan("N");
//					gateinout.setDgdc_seepz_out_scan("N");
//
//					gateinoutrepo.save(gateinout);
//				}
//
//				exportRepository.save(export);
//			}
//		} else {
//
//			exportRepository.save(export);
//		}
//		export.setPcStatus("Y");
//		return exportRepository.save(export);
//	}
	
	
	
	
	
	
	
	

	@GetMapping("/cancelPCStatus/{cid}/{bid}/{reqid}/{sbno}")
	public Export updateCancelPCStatus(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("reqid") String reqid, @PathVariable("sbno") String sbno) {
		Export export = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
		export.setPcStatus("N");
		return exportRepository.save(export);
	}

	@PostMapping("/penalty")
	public Export savePenalty(@RequestBody Export export) {
		return exportRepository.save(export);
	}

//	@PostMapping("/getNSDLStatus")
//	public Export updateNSDLStatus(@RequestBody Export export) throws IOException {
//		String s = export.getQrcodeUrl();
//		URL url = new URL(s);
//		System.out.println(s);
//		int timeoutMillis = 50000; // 5 seconds
//		Document document = Jsoup.parse(url, timeoutMillis);
//		Map<String, String> hashMap = new LinkedHashMap<>();
//
//		Elements labels = document.select(".Label, .LabelHeader, .SubHeader");
//
//		String currentKey = "";
//		String Demo = "";
//		String key = "";
//
//		for (Element label : labels) {
//
//			if (label.hasClass("LabelHeader") || label.hasClass("SubHeader")) {
//				key = "";
//				currentKey = label.text();
//				currentKey = currentKey.toLowerCase();
//
//				for (int i = 0; i < currentKey.length(); i++) {
//					if ((int) currentKey.charAt(i) >= 97 && (int) currentKey.charAt(i) <= 122) {
//						key += currentKey.charAt(i);
//					}
//				}
//
//				if (label.text().equals("Consignment Details:")) {
//					Demo = label.text();
//					currentKey = Demo;
//					hashMap.put(key, "");
//				}
//
//			} else {
//				String value = label.text();
//
//				hashMap.put(key, value);
//			}
//		}
//
//		export.setNsdlStatus(hashMap.get("requeststatus"));
//		return exportRepository.save(export);
//	}
	
	
	@PostMapping("/getNSDLStatus/{cid}/{bid}/{req}/{sb}")
	public Export updateNSDLStatus(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("req") String req,@PathVariable("sb") String sb) throws IOException {
		Export export = exportRepository.findBySBNoandSbreqid(cid, bid, req, sb);
		
		String s = export.getQrcodeUrl();
		URL url = new URL(s);
		System.out.println(s);
		int timeoutMillis = 50000; // 5 seconds
		Document document = Jsoup.parse(url, timeoutMillis);
		Map<String, String> hashMap = new LinkedHashMap<>();

		Elements labels = document.select(".Label, .LabelHeader, .SubHeader");

		String currentKey = "";
		String Demo = "";
		String key = "";

		for (Element label : labels) {

			if (label.hasClass("LabelHeader") || label.hasClass("SubHeader")) {
				key = "";
				currentKey = label.text();
				currentKey = currentKey.toLowerCase();

				for (int i = 0; i < currentKey.length(); i++) {
					if ((int) currentKey.charAt(i) >= 97 && (int) currentKey.charAt(i) <= 122) {
						key += currentKey.charAt(i);
					}
				}

				if (label.text().equals("Consignment Details:")) {
					Demo = label.text();
					currentKey = Demo;
					hashMap.put(key, "");
				}

			} else {
				String value = label.text();

				hashMap.put(key, value);
			}
		}

		export.setNsdlStatus(hashMap.get("requeststatus"));
		return exportRepository.save(export);
	}
	
	
	

	@PostMapping("/override/{nsdl}/{reason}/{cid}/{bid}/{sbid}/{sbno}")
	public void changeDeliveryUpdate(@PathVariable("nsdl") String nsdl, @PathVariable("reason") String reason,
			@PathVariable("cid") String cid, @PathVariable("bid") String bid, @PathVariable("sbid") String sbid,
			@PathVariable("sbno") String sbno, @RequestParam("file") MultipartFile file)
			throws IllegalStateException, IOException {

		// Save the file to a folder (you need to specify the folder path)
		String folderPath = FileUploadProperties.getPath(); // Update with your folder path
		String fileName = file.getOriginalFilename();
		String filePath = folderPath + "/" + fileName;

		// Use the provided logic to generate a unique file name
		String uniqueFileName = generateUniqueFileName(folderPath, fileName);

		// Construct the full path for the unique file
		String uniqueFilePath = folderPath + "/" + uniqueFileName;
		file.transferTo(new File(uniqueFilePath));

		List<Export> export = exportRepository.findAllBySBNoandSbreqid(cid, bid, sbid, sbno);

		// Update the repository with the unique file path
		this.exportRepository.updateOverride(nsdl, reason, uniqueFilePath, cid, bid, sbid, sbno);
	}

	private String generateUniqueFileName(String folderPath, String originalFileName) {
		int suffix = 1;
		String uniqueFileName = originalFileName;

		while (Files.exists(Paths.get(folderPath + "/" + uniqueFileName))) {
			int dotIndex = originalFileName.lastIndexOf('.');
			String nameWithoutExtension = dotIndex != -1 ? originalFileName.substring(0, dotIndex) : originalFileName;
			String fileExtension = dotIndex != -1 ? originalFileName.substring(dotIndex) : "";
			uniqueFileName = nameWithoutExtension + "_" + suffix + fileExtension;
			suffix++;
		}

		return uniqueFileName;
	}

	@GetMapping("/allheavydata")
	public List<ExportHeavyPackage> alldata() {
		return eexportHeavyRepo.findAll();
	}

	@PostMapping("/saveheavydata")
	public ResponseEntity<String> saveHeavydata(@RequestBody ExportHeavyPackage exportheavy) {
		// BigDecimal big = new BigDecimal(wt);

		Export existexport = exportRepository.findBySBNoandSbreqid(exportheavy.getCompanyId(),
				exportheavy.getBranchId(), exportheavy.getSbRequestId(), exportheavy.getSbNo());
		existexport.setHpStatus("Y");
		exportRepository.save(existexport);
		this.eexportHeavyRepo.save(exportheavy);
		return new ResponseEntity<>("Data inserted successfully", HttpStatus.CREATED);

	}

	@GetMapping("/allheavydata/{cid}/{bid}/{reqid}/{sbno}")
	public List<ExportHeavyPackage> getallheavydata(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("reqid") String reqid, @PathVariable("sbno") String sbno) {
		return this.eexportHeavyRepo.findalldata(cid, bid, reqid, sbno);
	}

	@PostMapping("/deletedata")
	public ResponseEntity<String> deleteByPackNo(@RequestBody ExportHeavyPackage exportHeavy) {
		ExportHeavyPackage existingHeavy = eexportHeavyRepo.finddata(exportHeavy.getCompanyId(),
				exportHeavy.getBranchId(), exportHeavy.getSbRequestId(), exportHeavy.getSbNo(),
				exportHeavy.getPackageNumber());

		if (existingHeavy == null) {
			// If the ExportHeavyPackage object doesn't exist, return a 404 Not Found
			// response
			return new ResponseEntity<>("ExportHeavyPackage not found", HttpStatus.NOT_FOUND);
		}

		// Attempt to delete the ExportHeavyPackage object
		try {
			eexportHeavyRepo.delete(existingHeavy);
		} catch (Exception e) {
			// Handle any exceptions that occur during deletion
			return new ResponseEntity<>("Error deleting data", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Check if there are any remaining heavy packages with the same SB data
		List<ExportHeavyPackage> remainingHeavyPackages = eexportHeavyRepo.findalldata(exportHeavy.getCompanyId(),
				exportHeavy.getBranchId(), exportHeavy.getSbRequestId(), exportHeavy.getSbNo());

		if (remainingHeavyPackages.isEmpty()) {
			// If no remaining heavy packages, update the Export object status
			Export export = exportRepository.findBySBNoandSbreqid(exportHeavy.getCompanyId(), exportHeavy.getBranchId(),
					exportHeavy.getSbRequestId(), exportHeavy.getSbNo());
			export.setHpStatus("N");
			exportRepository.save(export);
		}

		return new ResponseEntity<>("Data deleted successfully", HttpStatus.OK);
	}

//	@PostMapping("/cancelparcel")
//	public void cancelparcel(@RequestBody Export export) {
//		Export_History exphis = export_HistoryRepository.findSingledata(export.getCompanyId(), export.getBranchId(),
//				export.getSbNo(), export.getSbRequestId(), export.getSerNo());
//		export.setCancelStatus("Y");
//		export.setDgdcStatus("Cancelled");
//
//		exportRepository.save(export);
//
//		Export_History export_History = new Export_History(export.getCompanyId(), export.getBranchId(),
//				export.getSbNo(), export.getSbRequestId(), export.getSerNo(), export.getCreatedBy(),
//				exphis.getNewStatus(), export.getDgdcStatus(), null);
//		export_History.SetHistoryDate();
//		export_HistoryRepository.save(export_History);
//
//	}
	
	
	@PostMapping("/cancelparcel")
	public void cancelparcel(@RequestBody Export export) {
		String oldstatus = export.getDgdcStatus();
		export.setCancelStatus("Y");
		export.setDgdcStatus("Cancelled");

		exportRepository.save(export);

		Export_History export_History = new Export_History(export.getCompanyId(), export.getBranchId(),
				export.getSbNo(), export.getSbRequestId(), export.getSerNo(), export.getCreatedBy(),
				oldstatus, export.getDgdcStatus(), null);
		export_History.SetHistoryDate();
		export_HistoryRepository.save(export_History);

	}

//	@PostMapping("/removecancelparcel")
//	public void removecancelparcel(@RequestBody Export export) {
//		Export_History exphis = export_HistoryRepository.findSingledata(export.getCompanyId(), export.getBranchId(),
//				export.getSbNo(), export.getSbRequestId(), export.getSerNo());
//		export.setCancelStatus("N");
//		export.setDgdcStatus(exphis.getOldStatus());
//		exportRepository.save(export);
//		Export_History export_History = new Export_History(export.getCompanyId(), export.getBranchId(),
//				export.getSbNo(), export.getSbRequestId(), export.getSerNo(), export.getCreatedBy(),
//				exphis.getNewStatus(), export.getDgdcStatus(), null);
//		export_History.SetHistoryDate();
//		export_HistoryRepository.save(export_History);
//	}

	
	@PostMapping("/removecancelparcel")
	public void removecancelparcel(@RequestBody Export export) {
		String oldstatus = export.getDgdcStatus();
		export.setCancelStatus("N");
		export.setDgdcStatus("Handed over to DGDC SEEPZ");
		exportRepository.save(export);
		Export_History export_History = new Export_History(export.getCompanyId(), export.getBranchId(),
				export.getSbNo(), export.getSbRequestId(), export.getSerNo(), export.getCreatedBy(),
				"Cancelled", export.getDgdcStatus(), null);
		export_History.SetHistoryDate();
		export_HistoryRepository.save(export_History);
	}
	
	
	@PostMapping("/updatecancelparcel")
	public void updatecancelparcel(@RequestBody Export export) {

		export.setCancelStatus("Y");
		export.setDgdcStatus("Cancelled");

		exportRepository.save(export);

	}

	@GetMapping("/receivecarting/{cid}/{bid}/{carting}/{represent}")
	public List<Export> getalldataforreceivecartingagent(@PathVariable("cid") String cid,
			@PathVariable("bid") String bid, @PathVariable("carting") String carting,
			@PathVariable("represent") String represent) {
		return exportRepository.getalldataforreceivecarting(cid, bid, carting, represent);
	}

	@GetMapping("/byairline/{cid}/{bid}/{air}")
	public List<Export> getalldatabyairline(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("air") String air) {
		return exportRepository.getdataByairline(cid, bid, air);
	}

	@PostMapping("/handoverairline/{id}")
	public void updatedataforhandovertoairline(@RequestBody List<Export> export, @PathVariable("id") String id) {
		List<Export_History> exportHistoryList = new ArrayList<>();
		for (Export exp : export) {
			exp.setDgdcStatus("Handed Over to Airline");
			exp.setAirLineDate(new Date());
			exportRepository.save(exp);

			List<ScannedParcels> scan = scannedparcelsrepo.getbydocrefid(exp.getCompanyId(), exp.getBranchId(),
					exp.getSbNo());
			for (ScannedParcels scan1 : scan) {
				scan1.setStatus("Y");
				scannedparcelsrepo.save(scan1);
			}

			Export_History export_History = new Export_History();
			export_History.setCompanyId(exp.getCompanyId());
			export_History.setBranchId(exp.getBranchId());
			export_History.setNewStatus("Handed Over to Airline");
			export_History.setOldStatus("Handed over to DGDC Cargo");
			export_History.setSbNo(exp.getSbNo());
			export_History.setSbRequestId(exp.getSbRequestId());
			export_History.setserNo(exp.getSerNo());
			export_History.setTransport_Date(new Date());
			export_History.setUpdatedBy(id);
			export_History.SetHistoryDate();
			exportHistoryList.add(export_History);
		}
		this.export_HistoryRepository.saveAll(exportHistoryList);
	}

	@GetMapping("/getdataforhandover/{cid}/{bid}")
	public List<Export> getdataforHandover(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {
		return exportRepository.getalldataforhandover(cid, bid);
	}

	@GetMapping("/getdataforhandover1/{cid}/{bid}")
	public List<Export> getdataforHandover1(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {
		return exportRepository.getalldataforhandover1(cid, bid);
	}

	@GetMapping("/getdataforedit/{cid}/{bid}/{reqid}/{sbno}")
	public Export findbysbidandsbreq(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("reqid") String reqid, @PathVariable("sbno") String sbno) {
		return exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
	}

//	@PostMapping("/editexport")
//	public Export saveedit(@RequestBody Export export) {
//		return exportRepository.save(export);
//	}

//	@PostMapping("/editexport")
//	public Export saveedit(@RequestBody Export export) {
//
//		int sr_no = gateinoutrepo.findbysr2(export.getCompanyId(), export.getBranchId(), export.getSbRequestId(),
//				export.getSbNo());
//		if (sr_no != export.getNoOfPackages()) {
//			List<Gate_In_Out> gateinout1 = gateinoutrepo.findbysr1(export.getCompanyId(), export.getBranchId(),
//					export.getSbRequestId(), export.getSbNo());
//			gateinoutrepo.deleteAll(gateinout1);
//			for (int i = 1; i <= export.getNoOfPackages(); i++) {
//				String srNo = String.format("%04d", i);
//				Gate_In_Out gateinout = new Gate_In_Out();
//				gateinout.setCompanyId(export.getCompanyId());
//				gateinout.setBranchId(export.getBranchId());
//				gateinout.setNop(export.getNoOfPackages());
//				gateinout.setErp_doc_ref_no(export.getSbRequestId());
//				gateinout.setDoc_ref_no(export.getSbNo());
//				gateinout.setSr_No(export.getSerNo() + srNo);
//				gateinout.setDgdc_cargo_in_scan("N");
//				gateinout.setDgdc_cargo_out_scan("N");
//				gateinout.setDgdc_seepz_in_scan("N");
//				gateinout.setDgdc_seepz_out_scan("N");
//
//				gateinoutrepo.save(gateinout);
//			}
//		}
//
//		return exportRepository.save(export);
//	}
	
	
	
	@PostMapping("/editexport")
	public Export saveedit(@RequestBody Export export) {
		
		if(!"Handed over to DGDC SEEPZ".equals(export.getDgdcStatus()) && !"Entry at DGDC SEEPZ Gate".equals(export.getDgdcStatus())){
			String airliString = export.getAirwayBillNo().trim().substring(0,3);
			String newPctm = exportRepository.getPctmNo(export.getCompanyId(), export.getBranchId(), export.getTpNo(), airliString);
			if(!newPctm.isEmpty() && newPctm != null) {
				export.setPctmNo(newPctm);
			}
			else {
				String  pctmno = processNextIdService.getNextPctmNo();
				export.setPctmNo(pctmno);
			}
		}

		int sr_no = gateinoutrepo.findbysr2(export.getCompanyId(), export.getBranchId(), export.getSbRequestId(),
				export.getSbNo());
		if (sr_no != export.getNoOfPackages()) {
			List<Gate_In_Out> gateinout1 = gateinoutrepo.findbysr1(export.getCompanyId(), export.getBranchId(),
					export.getSbRequestId(), export.getSbNo());
			gateinoutrepo.deleteAll(gateinout1);
			for (int i = 1; i <= export.getNoOfPackages(); i++) {
				String srNo = String.format("%04d", i);
				Gate_In_Out gateinout = new Gate_In_Out();
				gateinout.setCompanyId(export.getCompanyId());
				gateinout.setBranchId(export.getBranchId());
				gateinout.setNop(export.getNoOfPackages());
				gateinout.setErp_doc_ref_no(export.getSbRequestId());
				gateinout.setDoc_ref_no(export.getSbNo());
				gateinout.setSr_No(export.getSerNo() + srNo);
				gateinout.setDgdc_cargo_in_scan("N");
				gateinout.setDgdc_cargo_out_scan("N");
				gateinout.setDgdc_seepz_in_scan("N");
				gateinout.setDgdc_seepz_out_scan("N");

				gateinoutrepo.save(gateinout);
			}
		}

		return exportRepository.save(export);
	}
	
	

	@GetMapping("/generateotp/{cid}/{bid}/{rid}/{mobile}")
	public String generateCartingOTP(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("rid") String rid, @PathVariable("mobile") String mobile) {
		String otp = generateOTP();
		RepresentParty represent = representReo.checkOTP(cid, bid, rid, mobile);
		represent.setOtp(otp);
		this.representReo.save(represent);

		try {
			String apiKey = "apikey=" + URLEncoder.encode("N2E2ZjU4NmU1OTY5Njg2YjczNjI3OTMxNjg3MjQ4NjM=", "UTF-8");
			String message = "Dear Sir/Madam, Please validate your identity in DGDC E-Custodian with OTP " + otp + " .";
			String sender = "sender=" + URLEncoder.encode("DGDCSZ", "UTF-8");
			String numbers = "numbers=" + URLEncoder.encode("91" + mobile, "UTF-8");

			// Send data
			String data = "https://api.textlocal.in/send/?" + apiKey + "&" + numbers + "&message="
					+ URLEncoder.encode(message, "UTF-8") + "&" + sender;
			URL url = new URL(data);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuilder sResult = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				sResult.append(line).append(" ");
			}
			rd.close();

			return sResult.toString();
		} catch (Exception e) {
			System.out.println("Error SMS " + e);
			return "Error " + e;
		}
	}

	private String generateOTP() {
		Random random = new Random();
		int otp = random.nextInt(900000) + 100000; // Generates a random number between 1000 and 9999
		return String.valueOf(otp);
	}

	@GetMapping("/findExportAllData")
	public List<Export> findExportSubData(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam("cartingAgent") String cartingAgent) {
		return exportRepository.findExportAllData(companyId, branchId, startDate, endDate, cartingAgent);
	}

	@GetMapping("/findExportData")
	public List<Export> findExportSubData(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate

	) {
		return exportRepository.findExportData(companyId, branchId, startDate, endDate);
	}

	@GetMapping("/findPartyName/{companyId}/{branchId}/{partyId}")
	public String findPartyNameByKeys(@PathVariable String companyId, @PathVariable String branchId,
			@PathVariable String partyId) {
		String partyName = partyrepo.findPartyNameByKeys(companyId, branchId, partyId);

		if (partyName != null) {
			return partyName;
		} else {
			// Handle the case where partyName is not found
			return "Party Name Not Found";
		}
	}

	@GetMapping("/findConsoleName/{companyId}/{branchId}/{externaluserId}")
	public String findConsoleName(@PathVariable String companyId, @PathVariable String branchId,
			@PathVariable String externaluserId) {
		String user_name = externalPartyRepository.findUserNameByKeys(companyId, branchId, externaluserId);

		if (user_name != null) {
			return user_name;
		} else {
			// Handle the case where partyName is not found
			return "User Name Not Found";
		}
	}

	@PostMapping("/getdataforairline")
	public ResponseEntity<Export> getdataforairline(@RequestBody ReadURL readURL) throws IOException {
		String s = readURL.getLink();
		URL url = new URL(s);
		System.out.println(s);
		int timeoutMillis = 50000; // 5 seconds
		Document document = Jsoup.parse(url, timeoutMillis);
		Map<String, String> hashMap = new LinkedHashMap<>();

		Elements labels = document.select(".Label, .LabelHeader, .SubHeader");

		String currentKey = "";
		String Demo = "";
		String key = "";

		for (Element label : labels) {

			if (label.hasClass("LabelHeader") || label.hasClass("SubHeader")) {
				key = "";
				currentKey = label.text();
				currentKey = currentKey.toLowerCase();

				for (int i = 0; i < currentKey.length(); i++) {
					if ((int) currentKey.charAt(i) >= 97 && (int) currentKey.charAt(i) <= 122) {
						key += currentKey.charAt(i);
					}
				}

				if (label.text().equals("Consignment Details:")) {
					Demo = label.text();
					currentKey = Demo;
					hashMap.put(key, "");
				}

			} else {
				String value = label.text();

				hashMap.put(key, value);
			}
		}
		String sbnodate = hashMap.get("sbnodate");
		String[] parts = sbnodate.split(",");
		String sbno = parts[0].trim();

		Export export = exportRepository.findBySBNoandSbreqid(readURL.getCompanyId(), readURL.getBranchId(),
				hashMap.get("requestid"), sbno);
		export.setNsdlStatus(hashMap.get("requeststatus"));
		exportRepository.save(export);
		if (export != null) {
			return ResponseEntity.ok(export); // Return the export data if found.
		} else {
			// Export data not found, return a 401 Unauthorized response.
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@GetMapping("/download/{cid}/{bid}/{reqid}/{sbno}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("sbno") String sbno, @PathVariable("reqid") String reqid) throws IOException {
		// Retrieve the image path from the database based on imageId
		Export exp = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
		String imagePath = exp.getOverrideDocument();

		if (imagePath != null) {

			MediaType mediaType = MediaType.IMAGE_JPEG; // Default to JPEG

			if (imagePath.endsWith(".pdf")) {
				mediaType = MediaType.APPLICATION_PDF;
			} else if (imagePath.endsWith(".png")) {
				mediaType = MediaType.IMAGE_PNG;
			} else if (imagePath.endsWith(".jpg") || imagePath.endsWith(".jpeg")) {
				mediaType = MediaType.IMAGE_JPEG;
			}
			// Load the image file as a byte array
			byte[] imageBytes = imageService.loadImage(imagePath);

			// Determine the content type based on the image file type (e.g., image/jpeg)
			HttpHeaders headers = new HttpHeaders();
			// Adjust as needed
			headers.setContentType(mediaType);

			return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


//	@Transactional
//	@PostMapping("/saveairway/{id}")
//	public void saveAirwaydata(@RequestBody Export exp, @PathVariable("id") String id) {
////		System.out.println(exp);
//		synchronized (this) {
//			Export export1 = exportRepository.findBySBNoandSbreqid(exp.getCompanyId(), exp.getBranchId(),
//					exp.getSbRequestId(), exp.getSbNo());
//			if (!export1.getSerNo().startsWith("EX")) {
//
//				String SIRNo = processNextIdRepository.findNextSIRExportNo();
//				int lastNextNumericId = Integer.parseInt(SIRNo.substring(2));
//				int nextNumericNextID = lastNextNumericId + 1;
//				String NextSIRNo = String.format("EX%06d", nextNumericNextID);
//				processNextIdRepository.updateNextSIRExportNo(NextSIRNo);
//
//				exp.setSerNo(NextSIRNo);
//				exp.setSerDate(new Date());
//				exp.setDgdcStatus("Handed over to DGDC SEEPZ");
//
//				exportRepository.save(exp);
//				Export_History export_History = new Export_History();
//				export_History.setCompanyId(exp.getCompanyId());
//				export_History.setBranchId(exp.getBranchId());
//				export_History.setNewStatus("Handed over to DGDC SEEPZ");
//				export_History.setOldStatus("Entry at DGDC SEEPZ Gate");
//				export_History.setSbNo(exp.getSbNo());
//				export_History.setSbRequestId(exp.getSbRequestId());
//				export_History.setserNo(exp.getSerNo());
//				export_History.setTransport_Date(new Date());
//				export_History.setUpdatedBy(id);
//				export_History.SetHistoryDate();
//				export_HistoryRepository.save(export_History);
//
//				if (exp.getNoc() == 0) {
//					for (int i = 1; i <= exp.getNoOfPackages(); i++) {
//						String srNo = String.format("%04d", i);
//						Gate_In_Out gateinout = new Gate_In_Out();
//						gateinout.setCompanyId(exp.getCompanyId());
//						gateinout.setBranchId(exp.getBranchId());
//						gateinout.setNop(exp.getNoOfPackages());
//						gateinout.setErp_doc_ref_no(exp.getSbRequestId());
//						gateinout.setDoc_ref_no(exp.getSbNo());
//						gateinout.setSr_No(exp.getSerNo() + srNo);
//						gateinout.setDgdc_cargo_in_scan("N");
//						gateinout.setDgdc_cargo_out_scan("N");
//						gateinout.setDgdc_seepz_in_scan("N");
//						gateinout.setDgdc_seepz_out_scan("N");
//
//						gateinoutrepo.save(gateinout);
//					}
//				}
//			} else {
//				exportRepository.save(exp);
//			}
//
//		}
//	}
//	
	
	
	@Transactional
	@PostMapping("/saveairway/{id}")
	public void saveAirwaydata(@RequestBody Export exp, @PathVariable("id") String id) {
	   System.out.println("exp.getAirlineCode() "+exp.getAirlineCode());
	    synchronized (this) {
			Export export1 = exportRepository.findBySBNoandSbreqid(exp.getCompanyId(), exp.getBranchId(),
					exp.getSbRequestId(), exp.getSbNo());
			if (!export1.getSerNo().startsWith("EX")) {

				String SIRNo = processNextIdRepository.findNextSIRExportNo();
				int lastNextNumericId = Integer.parseInt(SIRNo.substring(2));
				int nextNumericNextID = lastNextNumericId + 1;
				String NextSIRNo = String.format("EX%06d", nextNumericNextID);
				processNextIdRepository.updateNextSIRExportNo(NextSIRNo);

				exp.setSerNo(NextSIRNo);
				exp.setSerDate(new Date());
				exp.setDgdcStatus("Handed over to DGDC SEEPZ");
				
				

				exportRepository.save(exp);
				Export_History export_History = new Export_History();
				export_History.setCompanyId(exp.getCompanyId());
				export_History.setBranchId(exp.getBranchId());
				export_History.setNewStatus("Handed over to DGDC SEEPZ");
				export_History.setOldStatus("Entry at DGDC SEEPZ Gate");
				export_History.setSbNo(exp.getSbNo());
				export_History.setSbRequestId(exp.getSbRequestId());
				export_History.setserNo(exp.getSerNo());
				export_History.setTransport_Date(new Date());
				export_History.setUpdatedBy(id);
				export_History.SetHistoryDate();
				export_HistoryRepository.save(export_History);

				if (exp.getNoc() == 0) {
					for (int i = 1; i <= exp.getNoOfPackages(); i++) {
						String srNo = String.format("%04d", i);
						Gate_In_Out gateinout = new Gate_In_Out();
						gateinout.setCompanyId(exp.getCompanyId());
						gateinout.setBranchId(exp.getBranchId());
						gateinout.setNop(exp.getNoOfPackages());
						gateinout.setErp_doc_ref_no(exp.getSbRequestId());
						gateinout.setDoc_ref_no(exp.getSbNo());
						gateinout.setSr_No(exp.getSerNo() + srNo);
						gateinout.setDgdc_cargo_in_scan("N");
						gateinout.setDgdc_cargo_out_scan("N");
						gateinout.setDgdc_seepz_in_scan("N");
						gateinout.setDgdc_seepz_out_scan("N");

						gateinoutrepo.save(gateinout);
					}
				}
			} else {
				if(!"Handed over to DGDC SEEPZ".equals(export1.getDgdcStatus()) && !"Entry at DGDC SEEPZ Gate".equals(export1.getDgdcStatus())){
					String newPctm = exportRepository.getPctmNo(exp.getCompanyId(), exp.getBranchId(), exp.getTpNo(), exp.getAirlineCode());
//					System.out.println("newPctm "+newPctm);
					if(!newPctm.isEmpty() && newPctm != null) {
						exp.setPctmNo(newPctm);
					}
					else {
						String  pctmno = processNextIdService.getNextPctmNo();
						exp.setPctmNo(pctmno);
					}
				}
				exportRepository.save(exp);
			}

		}
	}
	
	
	
	
	
	
	
	
	
	
	public String generateFormattedString(int input) {
	    String id = String.format("%06d", input); // Pad the input with leading zeros to make it exactly 6 characters long
	    return "EX" + id;
	}
	
	
	
	
	
//	@Transactional
//	@PostMapping("/saveairway/{id}")
//	public void saveAirwaydata(@RequestBody Export exp, @PathVariable("id") String id) {
//
//		synchronized (this) {
//			Export export1 = exportRepository.findBySBNoandSbreqid(exp.getCompanyId(), exp.getBranchId(),
//					exp.getSbRequestId(), exp.getSbNo());
//			if (!export1.getSerNo().startsWith("EX")) {
//
//				ExportAutoIncrement data = new ExportAutoIncrement();
//				data.setSbNo(export1.getSbNo());
//				data.setSbRequestId(export1.getSbRequestId());
//				exportautoincrementrepo.save(data);
//
//				int nextId = exportautoincrementrepo.getId(export1.getSbNo(), export1.getSbRequestId());
//
//				exp.setSerNo(generateFormattedString(nextId));
//				exp.setSerDate(new Date());
//				exp.setDgdcStatus("Handed over to DGDC SEEPZ");
//
//				exportRepository.save(exp);
//				Export_History export_History = new Export_History();
//				export_History.setCompanyId(exp.getCompanyId());
//				export_History.setBranchId(exp.getBranchId());
//				export_History.setNewStatus("Handed over to DGDC SEEPZ");
//				export_History.setOldStatus("Entry at DGDC SEEPZ Gate");
//				export_History.setSbNo(exp.getSbNo());
//				export_History.setSbRequestId(exp.getSbRequestId());
//				export_History.setserNo(exp.getSerNo());
//				export_History.setTransport_Date(new Date());
//				export_History.setUpdatedBy(id);
//				export_History.SetHistoryDate();
//				export_HistoryRepository.save(export_History);
//
//				if (exp.getNoc() == 0) {
//					for (int i = 1; i <= exp.getNoOfPackages(); i++) {
//						String srNo = String.format("%04d", i);
//						Gate_In_Out gateinout = new Gate_In_Out();
//						gateinout.setCompanyId(exp.getCompanyId());
//						gateinout.setBranchId(exp.getBranchId());
//						gateinout.setNop(exp.getNoOfPackages());
//						gateinout.setErp_doc_ref_no(exp.getSbRequestId());
//						gateinout.setDoc_ref_no(exp.getSbNo());
//						gateinout.setSr_No(exp.getSerNo() + srNo);
//						gateinout.setDgdc_cargo_in_scan("N");
//						gateinout.setDgdc_cargo_out_scan("N");
//						gateinout.setDgdc_seepz_in_scan("N");
//						gateinout.setDgdc_seepz_out_scan("N");
//
//						gateinoutrepo.save(gateinout);
//					}
//				}
//			} else {
//				exportRepository.save(exp);
//			}
//
//		}
//	}	
	
	
	
	@GetMapping("/serDate")
	public List<String> getAllbySerdate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			@RequestParam("cid") String cid, @RequestParam("bid") String bid) {

		return exportRepository.findByTp(date, cid, bid);
	}

	@GetMapping("/exportDataBySerDateAndAirlineCode")
	public List<Export> getExportsBySerDateAndAirlineCode(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
			@RequestParam("airlineCode") String airlineCode) {
		return exportRepository.findBySerDateAndAirlineCode(companyId, branchId, serDate, airlineCode);
	}

	@PostMapping("/redeposite/{id}")
	public void updatedataforRedeposite(@RequestBody List<Export> export, @PathVariable("id") String id) {
		List<Export_History> exportHistoryList = new ArrayList<>();
		for (Export exp : export) {
			exp.setDgdcStatus("Handed over to DGDC Cargo");
			exportRepository.save(exp);

			Export_History export_History = new Export_History();
			export_History.setCompanyId(exp.getCompanyId());
			export_History.setBranchId(exp.getBranchId());
			export_History.setNewStatus("Handed over to DGDC Cargo");
			export_History.setOldStatus("Handed Over to Airline");
			export_History.setSbNo(exp.getSbNo());
			export_History.setSbRequestId(exp.getSbRequestId());
			export_History.setserNo(exp.getSerNo());
			export_History.setTransport_Date(new Date());
			export_History.setUpdatedBy(id);
			export_History.SetHistoryDate();

			exportHistoryList.add(export_History);
		}
		this.export_HistoryRepository.saveAll(exportHistoryList);
	}

	@GetMapping("/getdatabyserNoandDGDCStatus/{cid}/{bid}/{serNo}")
	public List<Export> getdatabyserNoandDGDCStatus(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("serNo") String serNo) {
		return exportRepository.findByCompanyIdAndBranchIdAndSerNoAndPcStatusAndDgdcStatusAndGatePassStatusNot(cid, bid,
				serNo, "Y", "Handed over to DGDC SEEPZ", "Handed over to Carting Agent", "Y");
	}

	@PostMapping("/printgatepass/{compId}/{branchId}")
	public ResponseEntity<String> generateGatePassPdf(@PathVariable("compId") String companyId,
			@PathVariable("branchId") String branchId, @RequestParam("serNoArray") List<String> serNoArray)
			throws Exception {
		try {
			// Create a Thymeleaf context

			Context context = new Context();
			List<Export> exportList = new ArrayList<>();
			List<String> names = new ArrayList<>();

			int totalNoPackages = 0;

			for (String serNo : serNoArray) {
				Export exp = exportRepository.findByCompanyIdAndBranchIdAndSerNo(companyId, branchId, serNo);

				if (exp != null) {

					Party cname = partyrepo.findByPartyId(exp.getNameOfExporter());

					exp.setCompanyId(cname.getPartyName());

					names.add(cname.getPartyName());
					System.out.println(cname.getPartyName());

					context.setVariable("officerName", exp.getpOName());
					context.setVariable("vehNo", exp.getGatePassVehicleNo());
					exportList.add(exp); // Add the found object to the list
					totalNoPackages = totalNoPackages + exp.getNoOfPackages();
				} else {

				}
			}
//			System.out.println(totalNoPackages);

			String imagePath = "C:/DGDC/Java Code/CWMS_JAVA/src/main/resources/static/image/DGDC1.png";

			File imageFile = new File(imagePath);
			if (imageFile.exists()) {
				Image image = Image.getInstance(imagePath);
				image.scaleToFit(400, 300); // Adjust the dimensions as needed
// 				image.setAlignment(Element.ALIGN_CENTER);
// 				document.add(image);
				context.setVariable("dgdclogo", image);
			} else {
				System.out.println("img not here");// Handle the case where the image does not exist
			}

			SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
			Date d1 = new Date();
			context.setVariable("Date", dateFormat2.format(d1));
			context.setVariable("companyId", companyId);
			context.setVariable("exportList", exportList);
			context.setVariable("totalNoPackages", totalNoPackages);
			context.setVariable("names", names);

			// Process the HTML template with dynamic values
			String htmlContent = templateEngine.process("GatePass", context);

			// Create an ITextRenderer instance
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

//			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//					.body(base64Pdf);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(base64Pdf);

		} catch (Exception e) {
			// Handle exceptions appropriately
			return ResponseEntity.badRequest().body("Error generating PDF");
		}
	}

	// code for Generating GatePass
	@PostMapping("/generateGatePass/{compId}/{branchId}")
	public ResponseEntity<String> generateGatePass(@PathVariable("compId") String companyId,
			@PathVariable("branchId") String branchId, @RequestParam("serNoArray") List<String> serNoArray,
			@RequestParam("vehNo") String vehNo, @RequestParam("OfficerName") String officerName) throws Exception {
		try {
			String gpid = processNextIdService.autoIncrementGatePassId();
			Date date = new Date();
			for (String serNo : serNoArray) {
				Export exp = exportRepository.findByCompanyIdAndBranchIdAndSerNo(companyId, branchId, serNo);

				if (exp != null) {
					exp.setGatePassVehicleNo(vehNo);
					exp.setPcGatePassId(gpid);
					exp.setPcGatePassDate(date);
					exp.setpOName(officerName); // Assuming 'pOName' was a typo and should be 'POName'
					exportRepository.save(exp);
				} else {
					// Handle the case where Export with the given serNo is not found
				}
			}

			System.out.println(serNoArray);
			System.out.println(serNoArray.get(0));
//				System.out.println(serNoArray.get(1));

			return ResponseEntity.ok().body("Gate Pass generated Successfully");
		} catch (Exception e) {
			// Handle exceptions appropriately
			return ResponseEntity.badRequest().body("Error generating Gate Pass");
		}
	}



	@GetMapping("/provisional/{cid}/{bid}/{sbreqid}/{sbno}/{id}")
	public String provisionalSER(@PathVariable("cid") String companyId, @PathVariable("bid") String branchId,
			@PathVariable("sbreqid") String sbreqid, @PathVariable("sbno") String sbno, @PathVariable("id") String id) {
		Export export = exportRepository.findBySBNoandSbreqid(companyId, branchId, sbreqid, sbno);
		if (!export.getSerNo().startsWith("EX")) {
			String id1 = autoid();

			export.setSerNo(id1);
			export.setSerDate(new Date());
			export.setDgdcStatus("Handed over to DGDC SEEPZ");

			if (export.getNoc() == 0) {
				for (int i = 1; i <= export.getNoOfPackages(); i++) {
					String srNo = String.format("%04d", i);
					Gate_In_Out gateinout = new Gate_In_Out();
					gateinout.setCompanyId(companyId);
					gateinout.setBranchId(branchId);
					gateinout.setNop(export.getNoOfPackages());
					gateinout.setErp_doc_ref_no(export.getSbRequestId());
					gateinout.setDoc_ref_no(export.getSbNo());
					gateinout.setSr_No(id1 + srNo);
					gateinout.setDgdc_cargo_in_scan("N");
					gateinout.setDgdc_cargo_out_scan("N");
					gateinout.setDgdc_seepz_in_scan("N");
					gateinout.setDgdc_seepz_out_scan("N");

					gateinoutrepo.save(gateinout);
				}
			}

			exportRepository.save(export);

			Export_History export_History = new Export_History();
			export_History.setCompanyId(export.getCompanyId());
			export_History.setBranchId(export.getBranchId());
			export_History.setNewStatus("Handed over to DGDC SEEPZ");
			export_History.setOldStatus("Entry at DGDC SEEPZ Gate");
			export_History.setSbNo(export.getSbNo());
			export_History.setSbRequestId(export.getSbRequestId());
			export_History.setserNo(export.getSerNo());
			export_History.setTransport_Date(new Date());
			export_History.setUpdatedBy(id);
			export_History.SetHistoryDate();
			export_HistoryRepository.save(export_History);
		}

		return "success";
	}

	
	
//	@GetMapping("/provisional/{cid}/{bid}/{sbreqid}/{sbno}/{id}")
//	public String provisionalSER(@PathVariable("cid") String companyId, @PathVariable("bid") String branchId,
//			@PathVariable("sbreqid") String sbreqid, @PathVariable("sbno") String sbno, @PathVariable("id") String id) {
//		Export export = exportRepository.findBySBNoandSbreqid(companyId, branchId, sbreqid, sbno);
//		if (!export.getSerNo().startsWith("EX")) {
//			ExportAutoIncrement data = new ExportAutoIncrement();
//			data.setSbNo(export.getSbNo());
//			data.setSbRequestId(export.getSbRequestId());
//			exportautoincrementrepo.save(data);
//
//			int nextId = exportautoincrementrepo.getId(export.getSbNo(), export.getSbRequestId());
//
//			export.setSerNo(generateFormattedString(nextId));
//			export.setSerDate(new Date());
//			export.setDgdcStatus("Handed over to DGDC SEEPZ");
//
//			if (export.getNoc() == 0) {
//				for (int i = 1; i <= export.getNoOfPackages(); i++) {
//					String srNo = String.format("%04d", i);
//					Gate_In_Out gateinout = new Gate_In_Out();
//					gateinout.setCompanyId(companyId);
//					gateinout.setBranchId(branchId);
//					gateinout.setNop(export.getNoOfPackages());
//					gateinout.setErp_doc_ref_no(export.getSbRequestId());
//					gateinout.setDoc_ref_no(export.getSbNo());
//					gateinout.setSr_No(generateFormattedString(nextId) + srNo);
//					gateinout.setDgdc_cargo_in_scan("N");
//					gateinout.setDgdc_cargo_out_scan("N");
//					gateinout.setDgdc_seepz_in_scan("N");
//					gateinout.setDgdc_seepz_out_scan("N");
//
//					gateinoutrepo.save(gateinout);
//				}
//			}
//
//			exportRepository.save(export);
//
//			Export_History export_History = new Export_History();
//			export_History.setCompanyId(export.getCompanyId());
//			export_History.setBranchId(export.getBranchId());
//			export_History.setNewStatus("Handed over to DGDC SEEPZ");
//			export_History.setOldStatus("Entry at DGDC SEEPZ Gate");
//			export_History.setSbNo(export.getSbNo());
//			export_History.setSbRequestId(export.getSbRequestId());
//			export_History.setserNo(export.getSerNo());
//			export_History.setTransport_Date(new Date());
//			export_History.setUpdatedBy(id);
//			export_History.SetHistoryDate();
//			export_HistoryRepository.save(export_History);
//		}
//
//		return "success";
//	}
//	
	
	
	
	
	// invoiceData Print
	@PostMapping("/Print1/{companyid}/{branchId}")
	public ResponseEntity<?> generateInvoicePdf(@PathVariable("companyid") String companyId,
			@PathVariable("branchId") String branchId,
			@RequestParam("formattedStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date StartDate,
			@RequestParam("formattedEndDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date EndDate

	) throws Exception {
		try {
			// Create a Thymeleaf context

			Context context = new Context();
			List<String> names = new ArrayList<>();

			double totalInvoiceAmount = 0;
			double totalAmountPaid = 0;

			List<InvoiceMain> InvoiceList = invoiceRepository.findAllInvoiceData(companyId, branchId, StartDate,
					EndDate);

			System.out.println("InvoiceList" + InvoiceList);
			for (InvoiceMain InvoiceObject : InvoiceList) {

				System.out.println("InvoiceObject" + InvoiceObject);
				System.out.println("InvoiceObject.getPartyId()" + InvoiceObject.getPartyId());

				Party partyName = partyrepo.findByPartyId(InvoiceObject.getPartyId());

				System.out.println("partyName" + partyName.getPartyName());

				names.add(partyName.getPartyName());

				totalInvoiceAmount = totalInvoiceAmount + InvoiceObject.getTotalInvoiceAmount();
				totalAmountPaid = totalAmountPaid + InvoiceObject.getClearedAmt();
			}

			double total = totalInvoiceAmount - totalAmountPaid;

			System.out.println(names + "names11");

			System.out.println(total + "total1");
			System.out.println(totalInvoiceAmount + "totalInvoiceAmount11");
			System.out.println(totalAmountPaid + " totalAmountPaid11");

			String imagePath = "G:/Updated DGDC/Project-13-10-23/java/CWMS_JAVA/CWMS_JAVA/src/main/resources/static/image/DGDC1.png";

			File imageFile = new File(imagePath);
			if (imageFile.exists()) {
				Image image = Image.getInstance(imagePath);
				image.scaleToFit(400, 300); // Adjust the dimensions as needed
//				image.setAlignment(Element.ALIGN_CENTER);
//				document.add(image);
				context.setVariable("dgdclogo", image);
			} else {
				System.out.println("img not here");// Handle the case where the image does not exist
			}

			context.setVariable("StartDate", StartDate);
			context.setVariable("EndDate", EndDate);
			context.setVariable("companyId", companyId);
			context.setVariable("InvoiceList", InvoiceList);
			context.setVariable("totalInvoiceAmount", totalInvoiceAmount);
			context.setVariable("totalAmountPaid", totalAmountPaid);
			context.setVariable("total", total);
			context.setVariable("names", names);

			System.out.println(StartDate + "StartDate");
			System.out.println(EndDate + "EndDate");
			System.out.println(companyId + "companyId");

			// Process the HTML template with dynamic values
			String htmlContent = templateEngine.process("PartyBillPaymentsReport", context);

			// Create an ITextRenderer instance
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

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(base64Pdf);

		} catch (Exception e) {
			// Log the exception for debugging
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Error generating PDF");
		}
	}

//		code for getting Search Data of Export Sub , Import and Import Sub
	@PostMapping("/commongatepass/search/{compId}/{branchId}")
	public ResponseEntity<?> getdatabyserNo(@PathVariable("compId") String companyId,
			@PathVariable("branchId") String branchId, @RequestParam("representativeId") String representativeId,
			@RequestParam("paryCHAId") String paryCHAId,
			@RequestParam("formattedStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date formattedStartDate)
			throws Exception {
		try {
			// No need to parse the formattedStartDate, it's already a Date object

			List<ExportSub> expsub = exportSubRepository.findByCompanyAndBranchAndSerDate(companyId, branchId,
					formattedStartDate, paryCHAId, representativeId);

			List<ImportSub> impsub = importSubRepository.findByCompanyAndBranchAndDate2(companyId, branchId,
					formattedStartDate, paryCHAId, representativeId);

			List<Import> imp = importRepository.findByCompanyAndBranchAndDate1(companyId, branchId, formattedStartDate,
					paryCHAId, representativeId);

			System.out.println("Out Date " + formattedStartDate);

//	         System.out.println(imp);

			// Combine all lists into a single list
			List<Object> combinedList = new ArrayList<>();
			combinedList.addAll(expsub);
			combinedList.addAll(impsub);
			combinedList.addAll(imp);

			System.out.println("expsub: " + expsub);
			System.out.println("imp: " + imp);

			// Check if the list is not empty before returning
			if (!combinedList.isEmpty()) {
				return ResponseEntity.ok().body(combinedList); // Return the list of Export objects
			} else {
				// Handle the case where no records were found
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(combinedList);
			}
		} catch (Exception e) {
			// Handle exceptions appropriately
			return ResponseEntity.badRequest().body("Error combinedList ");
		}
	}

//	 code for print common gatepass
	@PostMapping("/common/printgatepass/{compId}/{branchId}")
	public ResponseEntity<String> generateCommonGatePassPdf(@PathVariable("compId") String companyId,
			@PathVariable("branchId") String branchId, @RequestParam("printRecordArray") List<String> searchData,
			@RequestParam("removedRecordArray") List<String> removedRecordArray,
			@RequestParam("representativeId") String representativeId, @RequestParam("paryCHAId") String paryCHAId,
			@RequestParam("typeName") String typeName,
			@RequestParam("formattedStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date formattedStartDate

	) throws Exception {
		try {
			// Create a Thymeleaf context

			Context context = new Context();
			List<String> names1 = new ArrayList<>();
			List<String> names2 = new ArrayList<>();
			List<String> names3 = new ArrayList<>();
			List<ExportSub> FilteredExportSubList = new ArrayList<>();
			List<ImportSub> FilteredImportSubList = new ArrayList<>();
			List<Import> FilteredImportList = new ArrayList<>();

			System.out.println("Out Date " + formattedStartDate);

			List<ExportSub> expsub = exportSubRepository.findByCompanyAndBranchAndSerDate3(companyId, branchId,
					formattedStartDate, paryCHAId, representativeId);

			List<ImportSub> impsub = importSubRepository.findByCompanyAndBranchAndDate5(companyId, branchId,
					formattedStartDate, paryCHAId, representativeId);

			List<Import> imp = importRepository.findByCompanyAndBranchAndDate4(companyId, branchId, formattedStartDate,
					paryCHAId, representativeId);

//		       Party party1=partyrepo.findByPartyId(paryCHAId);
//		       String partyName= party1.getPartyName();

			String partyName = "";

			Party party1 = partyrepo.findByPartyId(paryCHAId);

			if (party1 != null) {
				partyName = party1.getPartyName();
			} else {
				ExternalParty singleRecord = ExternalParty_Service.getSingleRecord(companyId, branchId, paryCHAId);
				partyName = singleRecord.getUserName();

			}

			RepresentParty representParty = representPartyRepository.findByFNameAndLName(companyId, branchId,
					representativeId);

			String RName = representParty.getFirstName() + " " + representParty.getLastName();

			// Combine all lists into a single list
			List<Object> combinedList = new ArrayList<>();

			for (ExportSub list1 : expsub) {

				// Check if either invoiceNo or hawb is not in the removedRecordArray
				if (!removedRecordArray.contains(list1.getRequestId()) && searchData.contains(list1.getSerNo())) {

					Party party = partyrepo.findByPartyId(list1.getExporter());
					names1.add(party.getPartyName());

//			    	ExportSub exsub=exportSubRepository.findExportSub1( companyId, branchId,);
					FilteredExportSubList.add(list1);
				}

			}
//	       update gate_pass_Status as "Y" to db 
			List<String> exportSubIds = FilteredExportSubList.stream().map(ExportSub::getExpSubId)
					.collect(Collectors.toList());
			exportSubRepository.setGatePassStatusToY(exportSubIds);

			for (ImportSub list2 : impsub) {

				if (!removedRecordArray.contains(list2.getRequestId()) && searchData.contains(list2.getSirNo())) {
					FilteredImportSubList.add(list2);

					Party party = partyrepo.findByPartyId(list2.getExporter());
					names2.add(party.getPartyName());
				}

			}

			List<String> importSubIds = FilteredImportSubList.stream().map(ImportSub::getImpSubId)
					.collect(Collectors.toList());

			importSubRepository.setGatePassStatusToY(importSubIds);

			for (Import list3 : imp) {

				// Check if either invoiceNo or hawb is not in the removedRecordArray
				if (!removedRecordArray.contains(list3.getHawb()) && searchData.contains(list3.getSirNo())) {
					FilteredImportList.add(list3);
					Party party = partyrepo.findByPartyId(list3.getImporterId());
					names3.add(party.getPartyName());
				}

			}

			for (Import importItem : FilteredImportList) {
				// Replace setGatePassStatus with your actual method to set the gate_pass_status
				importItem.setGatePassStatus("Y");
			}

			// Save the changes to the database if needed
			importRepository.saveAll(FilteredImportList);

			String imagePath = "G:/Updated DGDC/Project-13-10-23/java/CWMS_JAVA/CWMS_JAVA/src/main/resources/static/image/DGDC1.png";

			File imageFile = new File(imagePath);
			if (imageFile.exists()) {
				Image image = Image.getInstance(imagePath);
				image.scaleToFit(400, 300); // Adjust the dimensions as needed
//				image.setAlignment(Element.ALIGN_CENTER);
//				document.add(image);
				context.setVariable("dgdclogo", image);
			} else {
				System.out.println("img not here");// Handle the case where the image does not exist
			}

			// Get the current time in milliseconds
			long currentTimeMillis = System.currentTimeMillis();

			// Create a Date object using the current time
			Date currentDate = new Date(currentTimeMillis);

			// Create a SimpleDateFormat object for formatting the time
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

			// Format the current time
			String formattedTime = sdf.format(currentDate);

			SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
			Date d1 = new Date();
			context.setVariable("Date", dateFormat2.format(d1));
			context.setVariable("companyId", companyId);
			context.setVariable("combinedList", combinedList);
			context.setVariable("formattedTime", formattedTime);
			context.setVariable("FilteredExportSubList", FilteredExportSubList);
			context.setVariable("FilteredImportSubList", FilteredImportSubList);
			context.setVariable("FilteredImportList", FilteredImportList);
			context.setVariable("names1", names1);
			context.setVariable("names2", names2);
			context.setVariable("names3", names3);
			context.setVariable("partyName", partyName);
			context.setVariable("RName", RName);
			context.setVariable("typeName", typeName);
			// Process the HTML template with dynamic values
			String htmlContent = templateEngine.process("CommonGatePass", context);

			// Create an ITextRenderer instance
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

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(base64Pdf);

		} catch (Exception e) {
			// Handle exceptions appropriately
			return ResponseEntity.badRequest().body("Error generating PDF");
		}
	}
//	 
//	 @GetMapping("/findbytpno/{cid}/{bid}")
//	 public List<String> finduniquetpno(@PathVariable("cid") String companyId,@PathVariable("bid") String branchId) throws ParseException {
//		 
//		// Get the current LocalDate
//	        LocalDate localDate = LocalDate.now();
//
//		   
//		 List<String> tpdata = exportRepository.findtpbytpdata(companyId,branchId,date);
//		 return tpdata;
//	 }

	@GetMapping("/alltp/{cid}/{bid}/{date}")
	public List<String> getalltp(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

		List<String> tp = exportRepository.getalltp(cid, bid, date);
		return tp;
	}

	private String generateUniqueFileName(String originalFileName) {
		String uniqueFileName = originalFileName;
		int suffix = 1;

		while (Files.exists(Paths.get(FileUploadProperties.getPath() + uniqueFileName))) {
			int dotIndex = originalFileName.lastIndexOf('.');
			String nameWithoutExtension = dotIndex != -1 ? originalFileName.substring(0, dotIndex) : originalFileName;
			String fileExtension = dotIndex != -1 ? originalFileName.substring(dotIndex) : "";
			uniqueFileName = nameWithoutExtension + "_" + suffix + fileExtension;
			suffix++;
		}

		return uniqueFileName;
	}

	@PostMapping("/redeposite/{id}/{cid}/{bid}")
	public ResponseEntity<String> updatedataforRedeposite(@PathVariable("cid") String cid,
			@PathVariable("bid") String bid, @PathVariable("id") String id, @RequestParam("file") MultipartFile file,
			@RequestParam("remarks") String remarks,
			@RequestParam("RemovedRecordArray") List<String> RemovedRecordArray) {

		List<Export_History> exportHistoryList = new ArrayList<>();

//			System.out.println(file + "file");
//			System.out.println(remarks + "remarks");
//			System.out.println(RemovedRecordArray + "RemovedRecordArray");

		List<Export> export = exportRepository.findAll();

		for (Export exp : export) {

			String pc = exp.getSerNo();

			if (RemovedRecordArray.contains(pc)) {
				exp.setDgdcStatus("Handed over to DGDC Cargo");
				exp.setAirLineDate(null);
				exp.setRedepositeRemark(remarks);
				exportRepository.save(exp);

				Export_History export_History = new Export_History(exp.getCompanyId(), exp.getBranchId(), exp.getSbNo(),
						exp.getSbRequestId(), exp.getSerNo(), id, "Handed Over to Airline", exp.getDgdcStatus(), null);
				export_History.SetHistoryDate();
				exportHistoryList.add(export_History);

				// Get the original file name
				String originalFileName = file.getOriginalFilename();

				// Generate a unique file name to avoid duplicates
				String uniqueFileName = generateUniqueFileName(originalFileName);

				exp.setImagePath(FileUploadProperties.getPath() + uniqueFileName);

				// Save the file to your local system with the unique name
				try {
					Files.copy(file.getInputStream(), Paths.get(FileUploadProperties.getPath() + uniqueFileName));
				} catch (IOException e) {
					e.printStackTrace(); // Consider proper error handling here
					return ResponseEntity.badRequest().body("Failed to save the file.");
				}

			}
		}
		export_HistoryRepository.saveAll(exportHistoryList);

		return ResponseEntity.ok("Data and file saved successfully.");
	}

	// Get Data By SER Number
	@GetMapping("/getdatabyserNo/{cid}/{bid}/{serNo}")
	public List<Export> getdatabyserNo(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("serNo") String serNo) {
		return exportRepository.findByCompanyIdAndBranchIdAndSerNoAndPcStatus(cid, bid, serNo, "Y");
	}

	@GetMapping("/alldatabyparty/{cid}/{bid}/{party}")
	public List<Export> alldatabyexportername(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("party") String party) {
		return this.exportRepository.findAllDataforparty(cid, bid, party);
	}

	@GetMapping("/alldatabycartingagent/{cid}/{bid}/{party}")
	public List<Export> alldatabycartingagent(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("party") String party) {
		return this.exportRepository.findAllDataforcartingagent(cid, bid, party);
	}

	@GetMapping("/alldatabycha/{cid}/{bid}/{party}")
	public List<Export> alldatabyCHA(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("party") String party) {
		return this.exportRepository.findAllDataforcha(cid, bid, party);
	}

	@GetMapping("/alldatabyconsole/{cid}/{bid}/{party}")
	public List<Export> alldatabyConsole(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("party") String party) {
		return this.exportRepository.findAllDataforconsole(cid, bid, party);
	}

	@PostMapping("/backtotown/{cid}/{bid}/{id}/{status}/{sbno}/{reqid}/{remark}")
	public Export backtotown(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("status") String status, @PathVariable("id") String id, @PathVariable("sbno") String sbno,
			@PathVariable("reqid") String reqid, @PathVariable("remark") String remark,
			@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {

		String folderPath = FileUploadProperties.getPath(); // Update with your folder path
		String fileName = file.getOriginalFilename();
		String filePath = folderPath + "/" + fileName;

		// Use the provided logic to generate a unique file name
		String uniqueFileName = generateUniqueFileName(folderPath, fileName);

		// Construct the full path for the unique file
		String uniqueFilePath = folderPath + "/" + uniqueFileName;
		file.transferTo(new File(uniqueFilePath));

		Export export = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
		export.setBacktotownRemark(remark);
		export.setDgdcStatus("Back To Town");
		export.setBacktotownFilePath(uniqueFilePath);
		exportRepository.save(export);

		Export_History export_History = new Export_History();
		export_History.setCompanyId(export.getCompanyId());
		export_History.setBranchId(export.getBranchId());
		export_History.setNewStatus("Back To Town");
		export_History.setOldStatus(status);
		export_History.setSbNo(export.getSbNo());
		export_History.setSbRequestId(export.getSbRequestId());

		export_History.setTransport_Date(new Date());
		export_History.setUpdatedBy(id);
		export_History.SetHistoryDate();
		export_HistoryRepository.save(export_History);

		return export;
	}

	@GetMapping("/download1/{cid}/{bid}/{reqid}/{sbno}")
	public ResponseEntity<byte[]> downloadImage1(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("sbno") String sbno, @PathVariable("reqid") String reqid) throws IOException {
		// Retrieve the image path from the database based on imageId
		Export exp = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
		String imagePath = exp.getBacktotownFilePath();

		if (imagePath != null) {

			MediaType mediaType = MediaType.IMAGE_JPEG; // Default to JPEG

			if (imagePath.endsWith(".pdf")) {
				mediaType = MediaType.APPLICATION_PDF;
			} else if (imagePath.endsWith(".png")) {
				mediaType = MediaType.IMAGE_PNG;
			} else if (imagePath.endsWith(".jpg") || imagePath.endsWith(".jpeg")) {
				mediaType = MediaType.IMAGE_JPEG;
			}
			// Load the image file as a byte array
			byte[] imageBytes = imageService.loadImage(imagePath);

			// Determine the content type based on the image file type (e.g., image/jpeg)
			HttpHeaders headers = new HttpHeaders();
			// Adjust as needed
			headers.setContentType(mediaType);

			return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

//	@GetMapping("/search")
//	public List<Export> searchImports(@RequestParam(name = "pcStatus", required = false) String pcStatus,
//			@RequestParam(name = "scStatus", required = false) String scStatus,
//			@RequestParam(name = "searchValue", required = false) String searchValue,
//			@RequestParam(name = "companyid", required = false) String companyid,
//			@RequestParam(name = "branchId", required = false) String branchId,
//			@RequestParam(name = "holdStatus", required = false) String holdStatus,
//			@RequestParam(name = "hpStatus", required = false) String hpStatus,
//			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
//			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//
//		System.out.println("searchValue " + companyid + branchId + pcStatus + scStatus + hpStatus + holdStatus
//				+ dgdcStatus + startDate + endDate + searchValue);
//
//		return exportRepository.findByAttributes2(companyid, branchId, pcStatus, scStatus, hpStatus, holdStatus,
//				dgdcStatus, startDate, endDate, searchValue);
//	}
	
	
	
	@GetMapping("/search")
	public List<Object[]> searchImports(@RequestParam(name = "pcStatus", required = false) String pcStatus,
			@RequestParam(name = "scStatus", required = false) String scStatus,
			@RequestParam(name = "searchValue", required = false) String searchValue,
			@RequestParam(name = "companyid", required = false) String companyid,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "holdStatus", required = false) String holdStatus,
			@RequestParam(name = "hpStatus", required = false) String hpStatus,
			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		
		return exportRepository.findByAttributes4(companyid, branchId, pcStatus, scStatus, hpStatus, holdStatus,
				dgdcStatus, startDate, endDate, searchValue);
	}
	
	
	

	@PostMapping("/uploadepcopy/{cid}/{bid}/{sbno}/{reqid}")
	public Export uploadepcopy(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("sbno") String sbno, @PathVariable("reqid") String reqid,
			@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {

		String folderPath = FileUploadProperties.getPath(); // Update with your folder path
		String fileName = file.getOriginalFilename();
		String filePath = folderPath + "/" + fileName;

		// Use the provided logic to generate a unique file name
		String uniqueFileName = generateUniqueFileName(folderPath, fileName);

		// Construct the full path for the unique file
		String uniqueFilePath = folderPath + "/" + uniqueFileName;
		file.transferTo(new File(uniqueFilePath));

		Export export = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);

		export.setEpCopyDocument(uniqueFilePath);
		exportRepository.save(export);

		return export;
	}

	@GetMapping("/download2/{cid}/{bid}/{reqid}/{sbno}")
	public ResponseEntity<byte[]> downloadImage2(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("sbno") String sbno, @PathVariable("reqid") String reqid) throws IOException {
		// Retrieve the image path from the database based on imageId
		Export exp = exportRepository.findBySBNoandSbreqid(cid, bid, reqid, sbno);
		String imagePath = exp.getEpCopyDocument();

		if (imagePath != null) {

			MediaType mediaType = MediaType.IMAGE_JPEG; // Default to JPEG

			if (imagePath.endsWith(".pdf")) {
				mediaType = MediaType.APPLICATION_PDF;
			} else if (imagePath.endsWith(".png")) {
				mediaType = MediaType.IMAGE_PNG;
			} else if (imagePath.endsWith(".jpg") || imagePath.endsWith(".jpeg")) {
				mediaType = MediaType.IMAGE_JPEG;
			}
			// Load the image file as a byte array
			byte[] imageBytes = imageService.loadImage(imagePath);

			// Determine the content type based on the image file type (e.g., image/jpeg)
			HttpHeaders headers = new HttpHeaders();
			// Adjust as needed
			headers.setContentType(mediaType);

			return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/updategateinout")
	public void updatedata() {
		List<Export> export1 = exportRepository.updatedta();

		for (Export exp : export1) {
			String ser1 = exp.getSerNo() + "0001";
			this.gateinoutrepo.updateData(ser1, exp.getSbRequestId());
		}
	}

//ExportController

	@PostMapping("/tpexcel")
	public ResponseEntity<byte[]> generateTPEXCEL(@RequestBody List<Export> export1) {

		try {
			// Create a new workbook
			Workbook workbook = new XSSFWorkbook();
			// Create a sheet
			Sheet sheet = workbook.createSheet("Import Data");

			// Create header row and set font style
			Row headerRow = sheet.createRow(0);
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(true);
			headerStyle.setFont(font);
			headerRow.createCell(0).setCellValue("SR No");
			headerRow.createCell(1).setCellValue("SER NO");
			headerRow.createCell(2).setCellValue("SER Date");
			headerRow.createCell(3).setCellValue("Parcel Type");
			headerRow.createCell(4).setCellValue("Name Of The Exporter");
			headerRow.createCell(5).setCellValue("No Of Packages");
			headerRow.createCell(6).setCellValue("DESC");
			headerRow.createCell(7).setCellValue("Value in RS");
			headerRow.createCell(8).setCellValue("Port Of Destination");

			// Apply style to header cells
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				headerRow.getCell(i).setCellStyle(headerStyle);
			}

			// Create data rows
			int rowNum = 1;

			for (Export exportObj : export1) {
				// Your input Date object
				Date inputDate = exportObj.getSerDate();

				// Create a SimpleDateFormat object with the desired format
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

				// Format the Date object
				String formattedDate = sdf.format(inputDate);
				Party party = partyrepo.findbyentityid(exportObj.getCompanyId(), exportObj.getBranchId(),
						exportObj.getEntityId());
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowNum - 1);
				row.createCell(1).setCellValue(exportObj.getSerNo());
				row.createCell(2).setCellValue(formattedDate);
				row.createCell(3).setCellValue("");
				row.createCell(4).setCellValue(party.getPartyName());
				row.createCell(5).setCellValue(exportObj.getNoOfPackages());
				row.createCell(6).setCellValue(exportObj.getDescriptionOfGoods());
				row.createCell(7).setCellValue(exportObj.getFobValueInINR());
				row.createCell(8).setCellValue(exportObj.getPortOfDestination());
				// Add more fields if necessary
			}

			// Adjust column sizes
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				sheet.autoSizeColumn(i);
			}

			// Create a ByteArrayOutputStream to write the workbook to
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);

			// Set headers for the response
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
			headers.setContentDispositionFormData("attachment", "export_data.xlsx");

			// Return the Excel file as a byte array in the response body
			return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception appropriately (e.g., log it and return an error
			// response)
			return ResponseEntity.status(500).build();
		}
	}

//	@PostMapping("/pctmexcel")
//	public ResponseEntity<byte[]> generatePCTMEXCEL(@RequestBody List<Export> export1) {
//
//		try {
//			// Create a new workbook
//			Workbook workbook = new XSSFWorkbook();
//			// Create a sheet
//			Sheet sheet = workbook.createSheet("Import Data");
//
//			// Create header row and set font style
//			Row headerRow = sheet.createRow(0);
//			CellStyle headerStyle = workbook.createCellStyle();
//			Font font = workbook.createFont();
//			font.setBold(true);
//			headerStyle.setFont(font);
//			headerRow.createCell(0).setCellValue("SR No");
//			headerRow.createCell(1).setCellValue("SER NO");
//			headerRow.createCell(2).setCellValue("Parcel Type");
//			headerRow.createCell(3).setCellValue("Destination");
//			headerRow.createCell(4).setCellValue("No Of Packages");
//			headerRow.createCell(5).setCellValue("Shipping Bill No");
//			headerRow.createCell(6).setCellValue("Net Weight");
//			headerRow.createCell(7).setCellValue("Airway Bill No");
//
//			// Apply style to header cells
//			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
//				headerRow.getCell(i).setCellStyle(headerStyle);
//			}
//
//			// Create data rows
//			int rowNum = 1;
//
//			for (Export exportObj : export1) {
//				// Your input Date object
//				Date inputDate = exportObj.getSerDate();
//
//				// Create a SimpleDateFormat object with the desired format
//				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//				// Format the Date object
//				String formattedDate = sdf.format(inputDate);
//				Party party = partyrepo.findbyentityid(exportObj.getCompanyId(), exportObj.getBranchId(),
//						exportObj.getEntityId());
//				Row row = sheet.createRow(rowNum++);
//				row.createCell(0).setCellValue(rowNum - 1);
//				row.createCell(1).setCellValue(exportObj.getSerNo());
//				row.createCell(2).setCellValue("");
//				row.createCell(3)
//						.setCellValue(exportObj.getCountryOfDestination() + " | " + exportObj.getPortOfDestination());
//				row.createCell(4).setCellValue(exportObj.getNoOfPackages());
//				row.createCell(5).setCellValue(exportObj.getSbNo());
//				row.createCell(6).setCellValue(exportObj.getGrossWeight());
//				row.createCell(7).setCellValue(exportObj.getAirwayBillNo());
//
//				// Add more fields if necessary
//			}
//
//			// Adjust column sizes
//			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
//				sheet.autoSizeColumn(i);
//			}
//
//			// Create a ByteArrayOutputStream to write the workbook to
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			workbook.write(outputStream);
//
//			// Set headers for the response
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(
//					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//			headers.setContentDispositionFormData("attachment", "export_data.xlsx");
//
//			// Return the Excel file as a byte array in the response body
//			return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			// Handle the exception appropriately (e.g., log it and return an error
//			// response)
//			return ResponseEntity.status(500).build();
//		}
//	}

	
	
	
	@PostMapping("/pctmexcel")
	public ResponseEntity<byte[]> generatePCTMEXCEL(@RequestBody List<Object[]> export1) {

		try {
			// Create a new workbook
			Workbook workbook = new XSSFWorkbook();
			// Create a sheet
			Sheet sheet = workbook.createSheet("Import Data");

			// Create header row and set font style
			Row headerRow = sheet.createRow(0);
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(true);
			headerStyle.setFont(font);
			headerRow.createCell(0).setCellValue("SR No");
			headerRow.createCell(1).setCellValue("SER NO");
			headerRow.createCell(2).setCellValue("Parcel Type");
			headerRow.createCell(3).setCellValue("Destination");
			headerRow.createCell(4).setCellValue("No Of Packages");
			headerRow.createCell(5).setCellValue("Shipping Bill No");
			headerRow.createCell(6).setCellValue("Net Weight");
			headerRow.createCell(7).setCellValue("Airway Bill No");

			// Apply style to header cells
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				headerRow.getCell(i).setCellStyle(headerStyle);
			}

			// Create data rows
			int rowNum = 1;

			for (Object[] exportObj : export1) {
			    Row row = sheet.createRow(rowNum++);
			    row.createCell(0).setCellValue(rowNum - 1);
			    row.createCell(1).setCellValue(String.valueOf(exportObj[0]));
			    row.createCell(2).setCellValue("");
			    row.createCell(3).setCellValue(String.valueOf(exportObj[1]) + " | " + String.valueOf(exportObj[2]));
			    row.createCell(4).setCellValue(String.valueOf(exportObj[3]));
			    row.createCell(5).setCellValue(String.valueOf(exportObj[4]));
			    row.createCell(6).setCellValue(String.valueOf(exportObj[5]));
			    row.createCell(7).setCellValue(String.valueOf(exportObj[6]));
			    // Add more fields if necessary
			}


			// Adjust column sizes
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				sheet.autoSizeColumn(i);
			}

			// Create a ByteArrayOutputStream to write the workbook to
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);

			// Set headers for the response
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
			headers.setContentDispositionFormData("attachment", "export_data.xlsx");

			// Return the Excel file as a byte array in the response body
			return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception appropriately (e.g., log it and return an error
			// response)
			return ResponseEntity.status(500).build();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@PostMapping("/registerexcel")
//	public ResponseEntity<byte[]> generateRegisterEXCEl(@RequestBody Map<String, Object> requestData) {
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			List<Export> export1 = objectMapper.convertValue(requestData.get("expdata"),
//					new TypeReference<List<Export>>() {
//					});
//			List<ExportSub> exportsub = objectMapper.convertValue(requestData.get("expsubdata"),
//					new TypeReference<List<ExportSub>>() {
//					});
//
//			Workbook workbook = new XSSFWorkbook();
//			// Create a sheet
//			Sheet sheet = workbook.createSheet("Export Data");
//
//			// Create header row and set font style
//			Row headerRow = sheet.createRow(0);
//			CellStyle headerStyle = workbook.createCellStyle();
//			Font font = workbook.createFont();
//			font.setBold(true);
//			headerStyle.setFont(font);
//
//			headerRow.createCell(0).setCellValue("SR No");
//			headerRow.createCell(1).setCellValue("SER NO");
//			headerRow.createCell(2).setCellValue("SB NO");
//			headerRow.createCell(3).setCellValue("SER Date");
//			headerRow.createCell(4).setCellValue("Parcel Type");
//			headerRow.createCell(5).setCellValue("Exporter Names");
//			headerRow.createCell(6).setCellValue("CHA NO");
//			headerRow.createCell(7).setCellValue("DESC");
//			headerRow.createCell(8).setCellValue("No Of Packages");
//			headerRow.createCell(9).setCellValue("Net Weight");
//			headerRow.createCell(10).setCellValue("FOB Value IN INR");
//			headerRow.createCell(11).setCellValue("Airway Bill No");
//			headerRow.createCell(12).setCellValue("Port Of Destination");
//			headerRow.createCell(13).setCellValue("Console IS");
//
//			// Apply style to header cells
//			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
//				headerRow.getCell(i).setCellStyle(headerStyle);
//			}
//
//			// Create data rows
//			int rowNum = 1;
//
//			// First loop (export1)
//			for (Export exportObj : export1) {
//				Date inputDate = exportObj.getSerDate();
//				ExternalParty externalparty = externalPartyRepository.getalldatabyid(exportObj.getCompanyId(),
//						exportObj.getBranchId(), exportObj.getConsoleAgent());
//				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//				String formattedDate = sdf.format(inputDate);
//				Party party = partyrepo.findbyentityid(exportObj.getCompanyId(), exportObj.getBranchId(),
//						exportObj.getEntityId());
//				Row row = sheet.createRow(rowNum++);
//				row.createCell(0).setCellValue(rowNum - 1);
//				row.createCell(1).setCellValue(exportObj.getSerNo());
//				row.createCell(2).setCellValue(exportObj.getSbNo());
//				row.createCell(3).setCellValue(formattedDate);
//				row.createCell(4).setCellValue("");
//				row.createCell(5).setCellValue(party.getPartyName());
//				row.createCell(6).setCellValue(exportObj.getChaNo());
//				row.createCell(7).setCellValue(exportObj.getDescriptionOfGoods());
//				row.createCell(8).setCellValue(exportObj.getNoOfPackages());
//				row.createCell(9).setCellValue(exportObj.getGrossWeight());
//				row.createCell(10).setCellValue(exportObj.getFobValueInINR());
//				row.createCell(11).setCellValue(exportObj.getAirwayBillNo());
//				row.createCell(12).setCellValue(exportObj.getPortOfDestination());
//				row.createCell(13).setCellValue(externalparty.getUserName());
//			}
//
//			// Second loop (exportsub)
//			for (ExportSub exportsubObj : exportsub) {
//				Date inputDate = exportsubObj.getSerDate();
//				BigDecimal gross = exportsubObj.getGwWeight();
//				double convertedValue = gross.doubleValue();
//				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//				String formattedDate = sdf.format(inputDate);
//				Party party = partyrepo.findByCompanyIdAndBranchIdAndPartyId(exportsubObj.getCompanyId(),
//						exportsubObj.getBranchId(), exportsubObj.getExporter());
//				Row row = sheet.createRow(rowNum++);
//				row.createCell(0).setCellValue(rowNum - 1);
//				row.createCell(1).setCellValue(exportsubObj.getSerNo());
//				row.createCell(2).setCellValue("");
//				row.createCell(3).setCellValue(formattedDate);
//				row.createCell(4).setCellValue("");
//				row.createCell(5).setCellValue(party.getPartyName());
//				row.createCell(6).setCellValue("");
//				row.createCell(7).setCellValue(exportsubObj.getRemarks());
//				row.createCell(8).setCellValue(exportsubObj.getNop());
//				row.createCell(9).setCellValue(convertedValue);
//				row.createCell(10).setCellValue("");
//				row.createCell(11).setCellValue("");
//				row.createCell(12).setCellValue("");
//				row.createCell(13).setCellValue("");
//			}
//
//			// Adjust column sizes
//			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
//				sheet.autoSizeColumn(i);
//			}
//
//			// Create a ByteArrayOutputStream to write the workbook to
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			workbook.write(outputStream);
//
//			// Set headers for the response
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(
//					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//			headers.setContentDispositionFormData("attachment", "export_data.xlsx");
//
//			// Return the Excel file as a byte array in the response body
//			return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			// Handle the exception appropriately (e.g., log it and return an error
//			// response)
//			return ResponseEntity.status(500).build();
//		}
//	}
	
	
	
	@PostMapping("/registerexcel")
	public ResponseEntity<byte[]> generateRegisterEXCEl(@RequestBody Map<String, Object> requestData) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			List<Export> export1 = objectMapper.convertValue(requestData.get("expdata"),
					new TypeReference<List<Export>>() {
					});
			List<ExportSub> exportsub = objectMapper.convertValue(requestData.get("expsubdata"),
					new TypeReference<List<ExportSub>>() {
					});

			Workbook workbook = new XSSFWorkbook();
			// Create a sheet
			Sheet sheet = workbook.createSheet("Export Data");

			// Create header row and set font style
			Row headerRow = sheet.createRow(0);
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(true);
			headerStyle.setFont(font);

			headerRow.createCell(0).setCellValue("SR No");
			headerRow.createCell(1).setCellValue("SER NO");
			headerRow.createCell(2).setCellValue("SB NO");
			headerRow.createCell(3).setCellValue("In Date");
			headerRow.createCell(4).setCellValue("SER Date");
			headerRow.createCell(5).setCellValue("Parcel Type");
			headerRow.createCell(6).setCellValue("Exporter Names");
			headerRow.createCell(7).setCellValue("CHA NO");
			headerRow.createCell(8).setCellValue("DESC");
			headerRow.createCell(9).setCellValue("No Of Packages");
			headerRow.createCell(10).setCellValue("Net Weight");
			headerRow.createCell(11).setCellValue("FOB Value IN INR");
			headerRow.createCell(12).setCellValue("Airway Bill No");
			headerRow.createCell(13).setCellValue("Port Of Destination");
			headerRow.createCell(14).setCellValue("Console IS");

			// Apply style to header cells
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				headerRow.getCell(i).setCellStyle(headerStyle);
			}

			// Create data rows
			int rowNum = 1;

			// First loop (export1)
			for (Export exportObj : export1) {
				Date inputDate = exportObj.getSerDate();
				ExternalParty externalparty = externalPartyRepository.getalldatabyid(exportObj.getCompanyId(),
						exportObj.getBranchId(), exportObj.getConsoleAgent());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String formattedDate = sdf.format(inputDate);
				
				Date approvedDate = exportObj.getApprovedDate();
		        String formattedDate2 = (approvedDate != null) ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(approvedDate) : null;
				
				
				Party party = partyrepo.findbyentityid(exportObj.getCompanyId(), exportObj.getBranchId(),
						exportObj.getEntityId());
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowNum - 1);
				row.createCell(1).setCellValue(exportObj.getSerNo());
				row.createCell(2).setCellValue(exportObj.getSbNo());
				row.createCell(3).setCellValue(formattedDate2);
				row.createCell(4).setCellValue(formattedDate);
				row.createCell(5).setCellValue("");
				row.createCell(6).setCellValue(party.getPartyName());
				row.createCell(7).setCellValue(exportObj.getChaNo());
				row.createCell(8).setCellValue(exportObj.getDescriptionOfGoods());
				row.createCell(9).setCellValue(exportObj.getNoOfPackages());
				row.createCell(10).setCellValue(exportObj.getGrossWeight());
				row.createCell(11).setCellValue(exportObj.getFobValueInINR());
				row.createCell(12).setCellValue(exportObj.getAirwayBillNo());
				row.createCell(13).setCellValue(exportObj.getPortOfDestination());
				row.createCell(14).setCellValue(externalparty.getUserName());
			}

			// Second loop (exportsub)
			for (ExportSub exportsubObj : exportsub) {
				Date inputDate = exportsubObj.getSerDate();
				BigDecimal gross = exportsubObj.getGwWeight();
				double convertedValue = gross.doubleValue();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//				 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//			        String formattedDate2 = dateFormat.format(exportsubObj.getApprovedDate());
				
				Date approvedDate = exportsubObj.getCreatedDate();
		        String formattedDate2 = (approvedDate != null) ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(approvedDate) : null;
				
				String formattedDate = sdf.format(inputDate);
				Party party = partyrepo.findByCompanyIdAndBranchIdAndPartyId(exportsubObj.getCompanyId(),
						exportsubObj.getBranchId(), exportsubObj.getExporter());
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowNum - 1);
				row.createCell(1).setCellValue(exportsubObj.getSerNo());
				row.createCell(2).setCellValue("");
				row.createCell(3).setCellValue(formattedDate2);
				row.createCell(4).setCellValue(formattedDate);
				row.createCell(5).setCellValue("");
				row.createCell(6).setCellValue(party.getPartyName());
				row.createCell(7).setCellValue("");
				row.createCell(8).setCellValue(exportsubObj.getRemarks());
				row.createCell(9).setCellValue(exportsubObj.getNop());
				row.createCell(10).setCellValue(convertedValue);
				row.createCell(11).setCellValue("");
				row.createCell(12).setCellValue("");
				row.createCell(13).setCellValue("");
				row.createCell(14).setCellValue("");
			}

			// Adjust column sizes
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				sheet.autoSizeColumn(i);
			}

			// Create a ByteArrayOutputStream to write the workbook to
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);

			// Set headers for the response
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
			headers.setContentDispositionFormData("attachment", "export_data.xlsx");

			// Return the Excel file as a byte array in the response body
			return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception appropriately (e.g., log it and return an error
			// response)
			return ResponseEntity.status(500).build();
		}
	}

	

//	@PostMapping("/registerexcel1")
//	public ResponseEntity<byte[]> generateRegisterEXCEl1(@RequestBody List<Export> export1) {
//		try {
//
//			Workbook workbook = new XSSFWorkbook();
//			// Create a sheet
//			Sheet sheet = workbook.createSheet("Export Data");
//
//			// Create header row and set font style
//			Row headerRow = sheet.createRow(0);
//			CellStyle headerStyle = workbook.createCellStyle();
//			Font font = workbook.createFont();
//			font.setBold(true);
//			headerStyle.setFont(font);
//
//			headerRow.createCell(0).setCellValue("SR No");
//			headerRow.createCell(1).setCellValue("SER NO");
//			headerRow.createCell(2).setCellValue("SB NO");
//			headerRow.createCell(3).setCellValue("SER Date");
//			headerRow.createCell(4).setCellValue("Parcel Type");
//			headerRow.createCell(5).setCellValue("Exporter Names");
//			headerRow.createCell(6).setCellValue("CHA NO");
//			headerRow.createCell(7).setCellValue("DESC");
//			headerRow.createCell(8).setCellValue("No Of Packages");
//			headerRow.createCell(9).setCellValue("Net Weight");
//			headerRow.createCell(10).setCellValue("FOB Value IN INR");
//			headerRow.createCell(11).setCellValue("Airway Bill No");
//			headerRow.createCell(12).setCellValue("Port Of Destination");
//			headerRow.createCell(13).setCellValue("Console IS");
//
//			// Apply style to header cells
//			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
//				headerRow.getCell(i).setCellStyle(headerStyle);
//			}
//
//			// Create data rows
//			int rowNum = 1;
//
//			// First loop (export1)
//			for (Export exportObj : export1) {
//				Date inputDate = exportObj.getSerDate();
//				ExternalParty externalparty = externalPartyRepository.getalldatabyid(exportObj.getCompanyId(),
//						exportObj.getBranchId(), exportObj.getConsoleAgent());
//				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//				String formattedDate = sdf.format(inputDate);
//				Party party = partyrepo.findbyentityid(exportObj.getCompanyId(), exportObj.getBranchId(),
//						exportObj.getEntityId());
//				Row row = sheet.createRow(rowNum++);
//				row.createCell(0).setCellValue(rowNum - 1);
//				row.createCell(1).setCellValue(exportObj.getSerNo());
//				row.createCell(2).setCellValue(exportObj.getSbNo());
//				row.createCell(3).setCellValue(formattedDate);
//				row.createCell(4).setCellValue("");
//				row.createCell(5).setCellValue(party.getPartyName());
//				row.createCell(6).setCellValue(exportObj.getChaNo());
//				row.createCell(7).setCellValue(exportObj.getDescriptionOfGoods());
//				row.createCell(8).setCellValue(exportObj.getNoOfPackages());
//				row.createCell(9).setCellValue(exportObj.getGrossWeight());
//				row.createCell(10).setCellValue(exportObj.getFobValueInINR());
//				row.createCell(11).setCellValue(exportObj.getAirwayBillNo());
//				row.createCell(12).setCellValue(exportObj.getPortOfDestination());
//				row.createCell(13).setCellValue(externalparty.getUserName());
//			}
//
//			// Adjust column sizes
//			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
//				sheet.autoSizeColumn(i);
//			}
//
//			// Create a ByteArrayOutputStream to write the workbook to
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			workbook.write(outputStream);
//
//			// Set headers for the response
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(
//					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//			headers.setContentDispositionFormData("attachment", "export_data.xlsx");
//
//			// Return the Excel file as a byte array in the response body
//			return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			// Handle the exception appropriately (e.g., log it and return an error
//			// response)
//			return ResponseEntity.status(500).build();
//		}
//	}
	
	
	
	@PostMapping("/registerexcel1")
	public ResponseEntity<byte[]> generateRegisterEXCEl1(@RequestBody List<Export> export1) {
		try {
			
			
			
			System.out.println("Excel Called Export Register");

			Workbook workbook = new XSSFWorkbook();
			// Create a sheet
			Sheet sheet = workbook.createSheet("Export Data");

			// Create header row and set font style
			Row headerRow = sheet.createRow(0);
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(true);
			headerStyle.setFont(font);

			headerRow.createCell(0).setCellValue("SR No");
			headerRow.createCell(1).setCellValue("SER NO");
			headerRow.createCell(2).setCellValue("SB NO");
			headerRow.createCell(3).setCellValue("In Date");
			headerRow.createCell(4).setCellValue("SER Date");
			headerRow.createCell(5).setCellValue("Parcel Type");
			headerRow.createCell(6).setCellValue("Exporter Names");
			headerRow.createCell(7).setCellValue("CHA NO");
			headerRow.createCell(8).setCellValue("DESC");
			headerRow.createCell(9).setCellValue("No Of Packages");
			headerRow.createCell(10).setCellValue("Net Weight");
			headerRow.createCell(11).setCellValue("FOB Value IN INR");
			headerRow.createCell(12).setCellValue("Airway Bill No");
			headerRow.createCell(13).setCellValue("Port Of Destination");
			headerRow.createCell(14).setCellValue("Console IS");

			// Apply style to header cells
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				headerRow.getCell(i).setCellStyle(headerStyle);
			}

			// Create data rows
			int rowNum = 1;

			// First loop (export1)
			for (Export exportObj : export1) {
				Date inputDate = exportObj.getSerDate();
				ExternalParty externalparty = externalPartyRepository.getalldatabyid(exportObj.getCompanyId(),
						exportObj.getBranchId(), exportObj.getConsoleAgent());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String formattedDate = sdf.format(inputDate);
				
				Date approvedDate = exportObj.getApprovedDate();
		        String formattedDate2 = (approvedDate != null) ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(approvedDate) : null;
				
				
				
				Party party = partyrepo.findbyentityid(exportObj.getCompanyId(), exportObj.getBranchId(),
						exportObj.getEntityId());
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowNum - 1);
				row.createCell(1).setCellValue(exportObj.getSerNo());
				row.createCell(2).setCellValue(exportObj.getSbNo());
				row.createCell(3).setCellValue(formattedDate2);
				row.createCell(4).setCellValue(formattedDate);
				row.createCell(5).setCellValue("");
				row.createCell(6).setCellValue(party.getPartyName());
				row.createCell(7).setCellValue(exportObj.getChaNo());
				row.createCell(8).setCellValue(exportObj.getDescriptionOfGoods());
				row.createCell(9).setCellValue(exportObj.getNoOfPackages());
				row.createCell(10).setCellValue(exportObj.getGrossWeight());
				row.createCell(11).setCellValue(exportObj.getFobValueInINR());
				row.createCell(12).setCellValue(exportObj.getAirwayBillNo());
				row.createCell(13).setCellValue(exportObj.getPortOfDestination());
				row.createCell(14).setCellValue(externalparty.getUserName());
			}

			// Adjust column sizes
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				sheet.autoSizeColumn(i);
			}

			// Create a ByteArrayOutputStream to write the workbook to
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);

			// Set headers for the response
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
			headers.setContentDispositionFormData("attachment", "export_data.xlsx");

			// Return the Excel file as a byte array in the response body
			return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception appropriately (e.g., log it and return an error
			// response)
			return ResponseEntity.status(500).build();
		}
	}
	

	@PostMapping("/transactionexcel")
	public ResponseEntity<byte[]> generateTransactionEXCEL(@RequestBody List<Export> export1) {

		try {
			// Create a new workbook
			Workbook workbook = new XSSFWorkbook();
			// Create a sheet
			Sheet sheet = workbook.createSheet("Import Data");

			// Create header row and set font style
			Row headerRow = sheet.createRow(0);
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(true);
			headerStyle.setFont(font);
			headerRow.createCell(0).setCellValue("SR No");
			headerRow.createCell(1).setCellValue("Request ID");
			headerRow.createCell(2).setCellValue("SB No");
			headerRow.createCell(3).setCellValue("SER No");
			headerRow.createCell(4).setCellValue("SER Date");
			headerRow.createCell(5).setCellValue("Exporter Name");
			headerRow.createCell(6).setCellValue("PKGS");
			headerRow.createCell(7).setCellValue("Gross Wt");
			headerRow.createCell(8).setCellValue("NSDL Status");
			headerRow.createCell(9).setCellValue("DGDC Status");

			// Apply style to header cells
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				headerRow.getCell(i).setCellStyle(headerStyle);
			}

			// Create data rows
			int rowNum = 1;

			for (Export exportObj : export1) {
				// Your input Date object
				String formattedDate = "";

				if (exportObj.getSerDate() != null) {
					Date inputDate = exportObj.getSerDate();

					// Create a SimpleDateFormat object with the desired format
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

					// Format the Date object
					formattedDate = sdf.format(inputDate);
				}
				Party party = partyrepo.findbyentityid(exportObj.getCompanyId(), exportObj.getBranchId(),
						exportObj.getEntityId());
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowNum - 1);
				row.createCell(1).setCellValue(exportObj.getSbRequestId());
				row.createCell(2).setCellValue(exportObj.getSbNo());
				row.createCell(3).setCellValue(exportObj.getSerNo());
				row.createCell(4).setCellValue(formattedDate);
				row.createCell(5).setCellValue(party.getPartyName());
				row.createCell(6).setCellValue(exportObj.getNoOfPackages());
				row.createCell(7).setCellValue(exportObj.getGrossWeight());
				row.createCell(8).setCellValue(exportObj.getNsdlStatus());
				row.createCell(9).setCellValue(exportObj.getDgdcStatus());
				// Add more fields if necessary
			}

			// Adjust column sizes
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				sheet.autoSizeColumn(i);
			}

			// Create a ByteArrayOutputStream to write the workbook to
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);

			// Set headers for the response
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
			headers.setContentDispositionFormData("attachment", "export_data.xlsx");

			// Return the Excel file as a byte array in the response body
			return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception appropriately (e.g., log it and return an error
			// response)
			return ResponseEntity.status(500).build();
		}
	}

	@GetMapping("/searchbypcdte/{cid}/{bid}/{date}")
	public List<PCGatePass> searchpcdata(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		List<Export> export1 = exportRepository.searchdatabypcgatepass(cid, bid, date);
		List<PCGatePass> gatepasses = new ArrayList<>();

		Map<String, String> idMap = new HashMap<>();
		Map<String, String> officernameMap = new HashMap<>();
		Map<Date, Date> dateMap = new HashMap<>();

		for (Export exp : export1) {
			String id = exp.getPcGatePassId();
			String officername = exp.getpOName();
			Date gatepassDate = exp.getPcGatePassDate();

			if (id != null && !idMap.containsKey(id)) {
				idMap.put(id, id);
				officernameMap.put(officername, officername);
				dateMap.put(gatepassDate, gatepassDate);

				PCGatePass gatepass = new PCGatePass();
				gatepass.setId(id);
				gatepass.setOfficername(officername);
				gatepass.setGatepassdate(gatepassDate);
				gatepasses.add(gatepass);
			}
		}

		return gatepasses;
	}

	@PostMapping("/printgatepass1/{cid}/{bid}")
	public ResponseEntity<String> generateGatePassPdf1(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@RequestBody PCGatePass gatepass) {
		try {
			// Create Thymeleaf context
			Context context = new Context();
			List<Export> exportList = exportRepository.getbygatepassid(gatepass.getId());
			List<String> names = new ArrayList<>();
			int totalNoPackages = 0;

			for (Export exp : exportList) {
				if (exp != null) {
					Party cname = partyrepo.findByPartyId(exp.getNameOfExporter());
					exp.setCompanyId(cname.getPartyName());
					names.add(cname.getPartyName());

					context.setVariable("officerName", exp.getpOName());
					context.setVariable("vehNo", exp.getGatePassVehicleNo());
					totalNoPackages += exp.getNoOfPackages();
				}
			}

			// Load DGDC logo
			String imagePath = "C:/DGDC/Java Code/CWMS_JAVA/src/main/resources/static/image/DGDC1.png";
			File imageFile = new File(imagePath);
			if (imageFile.exists()) {
				Image image = Image.getInstance(imagePath);
				image.scaleToFit(400, 300);
				context.setVariable("dgdclogo", image);
			} else {
				System.out.println("Image not found");
			}

			// Formatting using SimpleDateFormat
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String formattedDate = dateFormat.format(gatepass.getGatepassdate());
			context.setVariable("Date", formattedDate);
			context.setVariable("companyId", cid);
			context.setVariable("exportList", exportList);
			context.setVariable("totalNoPackages", totalNoPackages);
			context.setVariable("names", names);

			// Process the HTML template with dynamic values
			String htmlContent = templateEngine.process("GatePass", context);

			// Create an ITextRenderer instance
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

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(base64Pdf);

		} catch (Exception e) {
			// Handle exceptions appropriately
			return ResponseEntity.badRequest().body("Error generating PDF");
		}
	}
	
	
	
	@GetMapping("/searchbylogintype")
	@Transactional
	public List<Object[]> search(@RequestParam(name = "pcStatus", required = false) String pcStatus,
			@RequestParam(name = "scStatus", required = false) String scStatus,
			@RequestParam(name = "searchValue", required = false) String searchValue,
			@RequestParam(name = "companyid", required = false) String companyid,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "holdStatus", required = false) String holdStatus,
			@RequestParam(name = "hpStatus", required = false) String hpStatus,
			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(name = "loginid", required = false) String loginid,
			@RequestParam(name = "logintype", required = false) String logintype) {

		System.out.println(loginid + "" + logintype);
		if("Party".equals(logintype)) {
			return exportRepository.findByPartyAttributes(companyid, branchId, loginid,pcStatus, scStatus, hpStatus, holdStatus,
					dgdcStatus, startDate, endDate, searchValue);
		}
		else if("CHA".equals(logintype)) {
			return exportRepository.findByCHAAttributes(companyid, branchId, loginid,pcStatus, scStatus, hpStatus, holdStatus,
					dgdcStatus, startDate, endDate, searchValue);
		}
		else if("Console".equals(logintype)) {
			return exportRepository.findByConsoleAttributes(companyid, branchId, loginid,pcStatus, scStatus, hpStatus, holdStatus,
					dgdcStatus, startDate, endDate, searchValue);
		}
		else {
			return exportRepository.findByCartingAttributes(companyid, branchId, loginid,pcStatus, scStatus, hpStatus, holdStatus,
					dgdcStatus, startDate, endDate, searchValue);
		}
		
	}
	
	
//	@GetMapping("/getExportTpData")
//	public Map<String, Object> getExportDataByTpNos(@RequestParam("companyId") String companyId,
//			@RequestParam("branchId") String branchId,
//			@RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
//			@RequestParam("cartingAgent") String cartingAgent, @RequestParam("tpNo") String tpNo) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String formattedDate = sdf.format(serDate);
//
//		List<Object[]> tpDataAll = exportRepository.findExportTPData(companyId, branchId, formattedDate, cartingAgent,
//				tpNo);
//		
//		List<Object[]> tpDataAll1 = exportRepository.findExportPctmSummery(companyId, branchId, formattedDate);
//		
//		Map<String, Object> result = new HashMap<String, Object>();
//		if(!tpDataAll.isEmpty()) {
//			result.put("tpData", tpDataAll);
//		}
//		if(!tpDataAll1.isEmpty()) {
//			result.put("tpSummary", tpDataAll1);
//		}
//		
//		
//		
//		
//		
//		return result;
//	}
	
	
	@GetMapping("/getExportTpData")
	public Map<String, Object> getExportDataByTpNos(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
			@RequestParam("cartingAgent") String cartingAgent, @RequestParam("tpNo") String tpNo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(serDate);

		List<Object[]> tpDataAll = exportRepository.findExportTPData(companyId, branchId, formattedDate, cartingAgent,
				tpNo);

		List<Object[]> tpDataAll1 = exportRepository.findExportPctmSummery(companyId, branchId, formattedDate,tpNo);

		Map<String, Object> result = new HashMap<String, Object>();
		if (!tpDataAll.isEmpty()) {
			result.put("tpData", tpDataAll);
		}
		if (!tpDataAll1.isEmpty()) {
			result.put("tpSummary", tpDataAll1);
		}

		return result;
	}
	
	
//	 @PostMapping("/exportPctmReport")
//	    public ResponseEntity<String> generateExportPctmPdf(
//	            @RequestParam("companyId") String companyId,
//	            @RequestParam("branchId") String branchId,
//	            @RequestParam("tpNo") String tpNo,
//	            @RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
//	            @RequestBody Map<String, Object> data1) {
//	        try {
//	        	ObjectMapper objectMapper = new ObjectMapper();
//	        	List<Object[]> data = objectMapper.convertValue(data1.get("tpData"),
//	    				new TypeReference<List<Object[]>>() {
//	    				});
//	        	
//	        	
//	        	List<Object[]> summery = objectMapper.convertValue(data1.get("tpSummery"),
//	    				new TypeReference<List<Object[]>>() {
//	    				});
//	            // Use TreeMap to automatically sort the keys
//	            Map<Object, List<Object[]>> groupedData = new TreeMap<>();
//                Branch branch = branchRepo.findByBranchIdWithCompanyId(companyId, branchId);
//                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                String tpdate = format.format(serDate);
//              
//	            if (!data.isEmpty()) {
//	                for (Object[] objArray : data) {
//	                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
//	                    
//	                    String date = format1.format(objArray[11]);
//	                    objArray[11] = date;
//
//	                    String date1 = format1.format(objArray[12]);
//	                    objArray[12] = date1;
//
//	                    Object data10 = objArray[10];
//	                    if (!groupedData.containsKey(data10)) {
//	                        groupedData.put(data10, new ArrayList<>());
//	                    }
//	                    groupedData.get(data10).add(objArray);
//	                }
//	            }
//
//	            Context context = new Context();
//	            context.setVariable("item", groupedData);
//	            context.setVariable("tp", tpNo);
//	            context.setVariable("gst", branch.getGST_No());
//	            context.setVariable("tpDate", tpdate);
//	            context.setVariable("summery", summery);
//	            
//
//	            String htmlContent = templateEngine.process("ExportPctm", context);
//
//	            ITextRenderer renderer = new ITextRenderer();
//	            renderer.setDocumentFromString(htmlContent);
//	            renderer.layout();
//
//	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//	            renderer.createPDF(outputStream);
//	            byte[] pdfBytes = outputStream.toByteArray();
//
//	            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
//
//	            return ResponseEntity.ok()
//	                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//	                    .body(base64Pdf);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return ResponseEntity.badRequest().body("Error generating PDF");
//	        }
//	    }
	
	 @PostMapping("/exportPctmReport")
	    public ResponseEntity<String> generateExportPctmPdf(
	            @RequestParam("companyId") String companyId,
	            @RequestParam("branchId") String branchId,
	            @RequestParam("tpNo") String tpNo,
	            @RequestParam(value="cartingAgent",required = false) String cartingAgent,
	            @RequestParam(value="custodian",required = false) String custodian,
	            @RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
	            @RequestBody Map<String, Object> data1) {
	        try {
	        	ObjectMapper objectMapper = new ObjectMapper();
	        	List<Object[]> data = objectMapper.convertValue(data1.get("tpData"),
	    				new TypeReference<List<Object[]>>() {
	    				});
	        	
	        	String CatingAgent = externalPartyRepository.findUserNameByKeys(companyId, branchId ,cartingAgent );
	        	 
	        	 
	        	 
	        	List<Object[]> summery = objectMapper.convertValue(data1.get("tpSummery"),
	    				new TypeReference<List<Object[]>>() {
	    				});
	            // Use TreeMap to automatically sort the keys
	            Map<Object, List<Object[]>> groupedData = new TreeMap<>();
             Branch branch = branchRepo.findByBranchIdWithCompanyId(companyId, branchId);
             SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
             String tpdate = format.format(serDate);
           
	            if (!data.isEmpty()) {
	                for (Object[] objArray : data) {
	                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
	                    
	                    String date = format1.format(objArray[11]);
	                    objArray[11] = date;

	                    String date1 = format1.format(objArray[12]);
	                    objArray[12] = date1;

	                    Object data10 = objArray[10];
	                    if (!groupedData.containsKey(data10)) {
	                        groupedData.put(data10, new ArrayList<>());
	                    }
	                    groupedData.get(data10).add(objArray);
	                }
	            }

	            Context context = new Context();
	            context.setVariable("item", groupedData);
	            context.setVariable("tp", tpNo);
	            context.setVariable("gst", branch.getGST_No());
	            context.setVariable("tpDate", tpdate);
	            context.setVariable("summery", summery);
	            

	            
	            LocalDateTime currentDateTime = LocalDateTime.now();
		         // Define the desired date format
		         DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		         // Format the current date
		         String formattedDate = currentDateTime.format(dateFormatter);

		         // Define the desired time format
		         DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		         // Format the current time
		         String formattedTime = currentDateTime.format(timeFormatter);

		         // Set the formatted date and time as variables in the Thymeleaf context
		         context.setVariable("currentDate", formattedDate);
		         context.setVariable("currentTime", formattedTime);
	            
	            
	            
	            
	            
	            context.setVariable("custodian", custodian);
	            context.setVariable("cartingAgent", CatingAgent);
	            
	            
	            String htmlContent = templateEngine.process("ExportPctm", context);

	            ITextRenderer renderer = new ITextRenderer();
	            renderer.setDocumentFromString(htmlContent);
	            renderer.layout();

	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            renderer.createPDF(outputStream);
	            byte[] pdfBytes = outputStream.toByteArray();

	            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
	                    .body(base64Pdf);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.badRequest().body("Error generating PDF");
	        }
	    }
	
	
	
	
	
	
	 
	 
	 @PostMapping("/exportAckReport")
	    public ResponseEntity<String> generateExportAckPdf(
	            @RequestParam("companyId") String companyId,
	            @RequestParam("branchId") String branchId,
	            @RequestParam("tpNo") String tpNo,
	            @RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
	            @RequestBody Map<String, Object> data1) {
	        try {
	        	ObjectMapper objectMapper = new ObjectMapper();
	        	List<Object[]> data = objectMapper.convertValue(data1.get("tpData"),
	    				new TypeReference<List<Object[]>>() {
	    				});
	        	
	        	
	        	List<Object[]> summery = objectMapper.convertValue(data1.get("tpSummery"),
	    				new TypeReference<List<Object[]>>() {
	    				});
	            // Use TreeMap to automatically sort the keys
	            Map<Object, List<Object[]>> groupedData = new TreeMap<>();
             Branch branch = branchRepo.findByBranchIdWithCompanyId(companyId, branchId);
             SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
             String tpdate = format.format(serDate);
           
	            if (!data.isEmpty()) {
	                for (Object[] objArray : data) {
	                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
	                    
	                    String date = format1.format(objArray[11]);
	                    objArray[11] = date;

	                    String date1 = format1.format(objArray[12]);
	                    objArray[12] = date1;

	                    Object data10 = objArray[10];
	                    if (!groupedData.containsKey(data10)) {
	                        groupedData.put(data10, new ArrayList<>());
	                    }
	                    groupedData.get(data10).add(objArray);
	                }
	            }

	            Context context = new Context();
	            context.setVariable("item", groupedData);
	            context.setVariable("tp", tpNo);
	            context.setVariable("gst", branch.getGST_No());
	            context.setVariable("tpDate", tpdate);
	            context.setVariable("summery", summery);
	            

	            String htmlContent = templateEngine.process("ExportAcknowledgement", context);

	            ITextRenderer renderer = new ITextRenderer();
	            renderer.setDocumentFromString(htmlContent);
	            renderer.layout();

	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            renderer.createPDF(outputStream);
	            byte[] pdfBytes = outputStream.toByteArray();

	            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
	                    .body(base64Pdf);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.badRequest().body("Error generating PDF");
	        }
	    }
	 @PostMapping("/holdReport/{cid}/{bid}")
		public ResponseEntity<String> generateCommonGatePassPdf(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@RequestBody List<Object[]> data) throws Exception {
			try {
				System.out.println("data data " + data);

				if(!data.isEmpty()) {
					for (Object[] objArray : data) {
						// Modify each object array as needed
						// For example, if you want to set the first element of each array to a new
						// value:
						String id = String.valueOf(objArray[4]);
						Party party = partyrepo.getdatabyid(cid, bid, id);
						if(party != null) {
							objArray[4] = party.getPartyName();
						}
						
						SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
						
						String date = format1.format(objArray[3]);
						objArray[3] = date;
						
					}
				}

				Context context = new Context();

				context.setVariable("item", data);

				String htmlContent = templateEngine.process("Hold-Report", context);

				// Create an ITextRenderer instance
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

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.body(base64Pdf);
//		            return null;
			} catch (Exception e) {
				// Handle exceptions appropriately
				return ResponseEntity.badRequest().body("Error generating PDF");
			}
		}
	
	 
	 @GetMapping("/findExportRegister")
		public ResponseEntity<?> findExportRegister(@RequestParam(name = "companyId", required = false) String companyId,
		@RequestParam(name = "branchId", required = false) String branchId,
		@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
		@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
		@RequestParam(name = "console", required = false) String console,
		@RequestParam(name = "totalNoSIR", required = false) String totalNoSIR,
		@RequestParam(name = "totalFOBValue", required = false) String totalFOBValue)
	{	
			
			List<Object[]> combinedList = new ArrayList<>();

			if (console == null || console.isEmpty()) {
				List<Object[]> importData = exportRepository.findExportSubData(companyId, branchId, startDate, endDate);

				combinedList.addAll(importData);
			}

			List<Object[]> importSubData = exportRepository.findExportDataRegister(companyId, branchId, startDate, endDate,console);

			combinedList.addAll(importSubData);
			
			Object[] Values = exportRepository.getTotalNopAndSbCountAndFobValue(companyId, branchId, startDate, endDate,console);
			
			
			System.out.println(Values);
			 Map<String, Object> responseMap = new HashMap<>();
		        responseMap.put("combinedList", combinedList);
		        responseMap.put("Values", Values);

		        return ResponseEntity.ok(responseMap);		
			
						
//			return exportRepository.findExportData(companyId, branchId, startDate, endDate);
		}
	 
	 

		@GetMapping("/exportRegisterXLSDownload")
		public ResponseEntity<?> exportRegisterXLSDownload(@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "totalNoOfPackages", required = false) String totalNoOfPackages,
				@RequestParam(name = "totalNoSIR", required = false) String totalNoSIR,
				@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
				@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
				@RequestParam(name = "console", required = false) String console,
				@RequestParam(name = "totalFOBValue", required = false) String totalFOBValue)
		{	
			try {
				
				List<Object[]> combinedList = new ArrayList<>();

				if (console == null || console.isEmpty()) {
					List<Object[]> importData = exportRepository.findExportSubData(companyId, branchId, startDate, endDate);

					combinedList.addAll(importData);
				}

				List<Object[]> importSubData = exportRepository.findExportDataRegister(companyId, branchId, startDate, endDate,console);

				combinedList.addAll(importSubData);
				
			
				
				
				// Create a new workbook
							Workbook workbook = new XSSFWorkbook();
							// Create a sheet
							Sheet sheet = workbook.createSheet("Export Register");

							// Create header row and set font style
							Row headerRow = sheet.createRow(0);
							CellStyle headerStyle = workbook.createCellStyle();
							Font font = workbook.createFont();
							headerStyle.setAlignment(HorizontalAlignment.CENTER); 
							font.setBold(true);
							font.setFontHeightInPoints((short) 12);
							headerStyle.setFont(font);
							headerRow.createCell(0).setCellValue("SR NO");
							headerRow.createCell(1).setCellValue("SER NO");
							headerRow.createCell(2).setCellValue("SB NO");
							headerRow.createCell(3).setCellValue("SER DATE");
							headerRow.createCell(4).setCellValue("EXPORTERS NAME");
							headerRow.createCell(5).setCellValue("CHA NO");
							headerRow.createCell(6).setCellValue("DESC");
							headerRow.createCell(7).setCellValue("No of Pkg");
							headerRow.createCell(8).setCellValue("Neta Weight");
							headerRow.createCell(9).setCellValue("FOB value in INR");
							headerRow.createCell(10).setCellValue("AIRWAY BILL NO");
							headerRow.createCell(11).setCellValue("PORT OF DESTINATION");
							headerRow.createCell(12).setCellValue("Console");
							
							
							// Create data row style without bold font
							CellStyle dataStyle = workbook.createCellStyle();
							dataStyle.setAlignment(HorizontalAlignment.CENTER); // Center align the data
							// Apply style to header cells
							for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
								headerRow.getCell(i).setCellStyle(headerStyle);
							}

							// Create data rows
							int rowNum = 1;
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

							for (Object[] exportObj : combinedList) {
							    Row row = sheet.createRow(rowNum++);
							    row.createCell(0).setCellValue(rowNum - 1);
							    row.createCell(1).setCellValue(String.valueOf(exportObj[0]));
							    row.createCell(2).setCellValue(String.valueOf(exportObj[1]));
							    row.createCell(3).setCellValue(dateFormat.format((Date) exportObj[2]));
							    row.createCell(4).setCellValue(String.valueOf(exportObj[3]));
							    row.createCell(5).setCellValue(String.valueOf(exportObj[4]));
							    row.createCell(6).setCellValue(String.valueOf(exportObj[5]));
							    row.createCell(7).setCellValue(String.valueOf(exportObj[6]));
							    row.createCell(8).setCellValue(String.valueOf(exportObj[7]) + " " + String.valueOf(exportObj[12]));						    
							    
							    row.createCell(9).setCellValue(String.valueOf(exportObj[8]));
							    row.createCell(10).setCellValue(String.valueOf(exportObj[9]));
							    row.createCell(11).setCellValue(String.valueOf(exportObj[10]));
							    row.createCell(12).setCellValue(String.valueOf(exportObj[11]));
							    						    
							    // Add more fields if necessary
							    
							    for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
							        row.getCell(i).setCellStyle(dataStyle);
							    }
							}


							// Adjust column sizes
							for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
								sheet.autoSizeColumn(i);
							}

							// Create a ByteArrayOutputStream to write the workbook to
							ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
							workbook.write(outputStream);

							// Set headers for the response
							HttpHeaders headers = new HttpHeaders();
							headers.setContentType(
									MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
							headers.setContentDispositionFormData("attachment", "export_data.xlsx");

							// Return the Excel file as a byte array in the response body
							return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
		} catch (Exception e) {
			// Handle exceptions appropriately
			return ResponseEntity.badRequest().body("Error generating PDF");
		}
			
		}		
			
			

		
		
		@GetMapping("/exportRegisterPrint")
		public ResponseEntity<?> importRegisterPrint(@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "totalNoOfPackages", required = false) String totalNoOfPackages,
				@RequestParam(name = "totalNoSIR", required = false) String totalNoSIR,				
				@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
				@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
				@RequestParam(name = "console", required = false) String console ,
				@RequestParam(name = "totalFOBValue", required = false) String totalFOBValue) throws Exception {
			try {

				Context context = new Context();
				List<Object[]> combinedList = new ArrayList<>();

				if (console == null || console.isEmpty()) {
					List<Object[]> importData = exportRepository.findExportSubData(companyId, branchId, startDate, endDate);

					combinedList.addAll(importData);
				}

				List<Object[]> importSubData = exportRepository.findExportDataRegister(companyId, branchId, startDate, endDate,console);

				combinedList.addAll(importSubData);
				
				
				
							
				
				LocalDateTime currentDateTime = LocalDateTime.now();
				// Define the desired date and time format
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
				// Format the current date and time
				String formattedDateTime = currentDateTime.format(formatter);
				
				
				
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
				context.setVariable("currentDate", formattedDateTime);
				context.setVariable("startDate", dateFormat2.format(startDate));
				context.setVariable("enddate", dateFormat2.format(endDate));
				context.setVariable("importData", combinedList);
				
				
				
				
				context.setVariable("totalNoSIR", totalNoSIR);
				context.setVariable("totalNoOfPackages", totalNoOfPackages);
				context.setVariable("totalFOBValue", totalFOBValue);
				
				
				
				
				
				String htmlContent = templateEngine.process("ExportRegister", context);

				// Create an ITextRenderer instance
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

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.body(base64Pdf);
//	            return null;
			} catch (Exception e) {
				// Handle exceptions appropriately
				return ResponseEntity.badRequest().body("Error generating PDF");
			}
		}
	 
		@GetMapping("/getalldata")
		public List<Object[]> getallbyTpnoandTpdate(@RequestParam("cid") String cid, @RequestParam("bid") String bid,
				@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("tpno") String tpno
		// @RequestParam("status") char status
		) { // Change the parameter name to "status"
			return exportRepository.findByTpdateTpno1(cid, bid, date, tpno); // Use "status" parameter here
		}

		
//		 @PostMapping("/exportTPReport")
//		    public ResponseEntity<String> generateExportTPPdf(
//		            @RequestParam("companyId") String companyId,
//		            @RequestParam("branchId") String branchId,@RequestParam("tpdate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("tpno") String tpno,
//		           @RequestParam(name="vehicle",required = false) String vehicle,
//		            @RequestBody List<Object[]> data) {
//		        try {
//		        	
//	                Branch branch = branchRepo.findByBranchIdWithCompanyId(companyId, branchId);
//	                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//	               String tpdate = format.format(date);
//	               
//	               int totalNop = 0;
//	               int totalFob = 0;
//	              
//		            if (!data.isEmpty()) {
//		                for (Object[] objArray : data) {
//		                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
//		                    
//		                    String date1 = format1.format(objArray[1]);
//		                    objArray[1] = date1;
//		                    
//		                    String partyname = partyrepo.findPartyNameByKeys(companyId, branchId, String.valueOf(objArray[2]));
//                            totalNop += Integer.parseInt(String.valueOf(objArray[3]));
//                            totalFob += Integer.parseInt(String.valueOf(objArray[5]));
//		                    if(!partyname.isBlank() && partyname != null) {
//		                    	objArray[2] = partyname;
//		                    }
//
//		                   
//		                }
//		            }
//
//		            Context context = new Context();
//		            context.setVariable("item", data);
//		            context.setVariable("gst", branch.getGST_No());
//		            context.setVariable("tpdate", tpdate);
//		            context.setVariable("tpno", tpno);
//		            context.setVariable("vehicle", vehicle);
//		            context.setVariable("totalNop", totalNop);
//		            context.setVariable("totalFob", totalFob);
//
//		            String htmlContent = templateEngine.process("ExportTP", context);
//
//		            ITextRenderer renderer = new ITextRenderer();
//		            renderer.setDocumentFromString(htmlContent);
//		            renderer.layout();
//
//		            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		            renderer.createPDF(outputStream);
//		            byte[] pdfBytes = outputStream.toByteArray();
//
//		            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
//
//		            return ResponseEntity.ok()
//		                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//		                    .body(base64Pdf);
//		        } catch (Exception e) {
//		            e.printStackTrace();
//		            return ResponseEntity.badRequest().body("Error generating PDF");
//		        }
//		    }
		
		
		

		 @PostMapping("/exportTPReport")
		    public ResponseEntity<String> generateExportTPPdf(
		            @RequestParam("companyId") String companyId,
		            @RequestParam("branchId") String branchId,@RequestParam("tpdate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("tpno") String tpno,
		           @RequestParam(name="vehicle",required = false) String vehicle,
		           @RequestParam(name="custodian",required = false) String custodian,
		            @RequestBody List<Object[]> data) {
		        try {
		        	
		        	
	                Branch branch = branchRepo.findByBranchIdWithCompanyId(companyId, branchId);
	                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	               String tpdate = format.format(date);
	               String CatingAgent = "";
	               
	               if (!data.isEmpty()) {
	               Object[] firstObject = data.get(0); 
	               if (firstObject.length > 10) {
	                   Object valueAt11thPosition = firstObject[11];
	                   
//	                   System.out.println("Carting Agent : "+ valueAt11thPosition.toString());
	                   
	                   CatingAgent = externalPartyRepository.findUserNameByKeys(companyId, branchId ,valueAt11thPosition.toString() );
	                   
//	                   System.out.println("Carting Agent After : "+ CatingAgent );
	               }
	               }
	               
	               int totalNop = 0;
	               int totalFob = 0;
	              
		            if (!data.isEmpty()) {
		                for (Object[] objArray : data) {
		                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
		                    
		                    String date1 = format1.format(objArray[1]);
		                    objArray[1] = date1;
		                    
		                    String partyname = partyrepo.findPartyNameByKeys(companyId, branchId, String.valueOf(objArray[2]));
                           totalNop += Integer.parseInt(String.valueOf(objArray[3]));
                           totalFob += Integer.parseInt(String.valueOf(objArray[5]));
		                    if(!partyname.isBlank() && partyname != null) {
		                    	objArray[2] = partyname;
		                    }

		                   
		                }
		            }

		            Context context = new Context();
		            context.setVariable("item", data);
		            context.setVariable("gst", branch.getGST_No());
		            context.setVariable("tpdate", tpdate);
		            context.setVariable("tpno", tpno);
		            context.setVariable("vehicle", vehicle);
		            context.setVariable("totalNop", totalNop);
		            context.setVariable("totalFob", totalFob);
		            context.setVariable("cartingAgent", CatingAgent);
		            context.setVariable("custodian", custodian);
		            
		            
		            
		            
		            
		            
		            LocalDateTime currentDateTime = LocalDateTime.now();
		         // Define the desired date format
		         DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		         // Format the current date
		         String formattedDate = currentDateTime.format(dateFormatter);

		         // Define the desired time format
		         DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		         // Format the current time
		         String formattedTime = currentDateTime.format(timeFormatter);

		         // Set the formatted date and time as variables in the Thymeleaf context
		         context.setVariable("currentDate", formattedDate);
		         context.setVariable("currentTime", formattedTime);
		            
		            
		            
		            
		            context.setVariable("TotalNoOfPackageInWord", convertToWords(totalNop));

		            String htmlContent = templateEngine.process("ExportTP", context);

		            ITextRenderer renderer = new ITextRenderer();
		            renderer.setDocumentFromString(htmlContent);
		            renderer.layout();

		            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		            renderer.createPDF(outputStream);
		            byte[] pdfBytes = outputStream.toByteArray();

		            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

		            return ResponseEntity.ok()
		                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		                    .body(base64Pdf);
		        } catch (Exception e) {
		            e.printStackTrace();
		            return ResponseEntity.badRequest().body("Error generating PDF");
		        }
		    }
		
		
		 private static final String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
					"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen",
					"Nineteen" };
			private static final String[] tens = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty",
					"Ninety" };

			public String convertToWords(int number) {
				if (number < 20) {
					return units[number];
				}
				if (number < 100) {
					return tens[number / 10] + " " + units[number % 10];
				}
				if (number < 1000) {
					return units[number / 100] + " Hundred " + convertToWords(number % 100);
				}
				if (number < 1000000) {
					return convertToWords(number / 1000) + " Thousand " + convertToWords(number % 1000);
				}
				if (number < 1000000000) {
					return convertToWords(number / 1000000) + " Million " + convertToWords(number % 1000000);
				}
				return "Number out of range";
			}
		 
		 @PostMapping("/tpexcel/{cid}/{bid}")
			public ResponseEntity<byte[]> generateTPEXCEL(@RequestBody List<Object[]> export1,@PathVariable("cid") String cid,@PathVariable("bid") String bid) {

				try {
					// Create a new workbook
					Workbook workbook = new XSSFWorkbook();
					// Create a sheet
					Sheet sheet = workbook.createSheet("Import Data");

					// Create header row and set font style
					Row headerRow = sheet.createRow(0);
					CellStyle headerStyle = workbook.createCellStyle();
					Font font = workbook.createFont();
					font.setBold(true);
					headerStyle.setFont(font);
					headerRow.createCell(0).setCellValue("SR No");
					headerRow.createCell(1).setCellValue("SER NO");
					headerRow.createCell(2).setCellValue("SER Date");
					headerRow.createCell(3).setCellValue("Parcel Type");
					headerRow.createCell(4).setCellValue("Name Of The Exporter");
					headerRow.createCell(5).setCellValue("No Of Packages");
					headerRow.createCell(6).setCellValue("DESC");
					headerRow.createCell(7).setCellValue("Value in RS");
					headerRow.createCell(8).setCellValue("Port Of Destination");

					// Apply style to header cells
					for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
						headerRow.getCell(i).setCellStyle(headerStyle);
					}

					// Create data rows
					int rowNum = 1;

					for (Object[] exportObj : export1) {
						// Your input Date object
						Long timestamp = (Long) exportObj[1];
						Date date = new Date(timestamp);

						// Create a SimpleDateFormat object with the desired format
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

						// Format the Date object
						String formattedDate = sdf.format(date);
						Party party = partyrepo.findbyentityid(cid, bid,
								String.valueOf(exportObj[10]));
						Row row = sheet.createRow(rowNum++);
						row.createCell(0).setCellValue(rowNum - 1);
						row.createCell(1).setCellValue(String.valueOf(exportObj[0]));
						row.createCell(2).setCellValue(formattedDate);
						row.createCell(3).setCellValue("Y".equals(String.valueOf(exportObj[7])) ? "SC" : "Y".equals(String.valueOf(exportObj[8])) ? "P" : "Y".equals(String.valueOf(exportObj[9])) ? "HW" : "");
						row.createCell(4).setCellValue(party.getPartyName());
						row.createCell(5).setCellValue(String.valueOf(exportObj[3]));
						row.createCell(6).setCellValue(String.valueOf(exportObj[4]));
						row.createCell(7).setCellValue(String.valueOf(exportObj[5]));
						row.createCell(8).setCellValue(String.valueOf(exportObj[6]));
						// Add more fields if necessary
					}

					// Adjust column sizes
					for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
						sheet.autoSizeColumn(i);
					}

					// Create a ByteArrayOutputStream to write the workbook to
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					workbook.write(outputStream);

					// Set headers for the response
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(
							MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
					headers.setContentDispositionFormData("attachment", "export_data.xlsx");

					// Return the Excel file as a byte array in the response body
					return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());

				} catch (IOException e) {
					e.printStackTrace();
					// Handle the exception appropriately (e.g., log it and return an error
					// response)
					return ResponseEntity.status(500).build();
				}
			}
	
}