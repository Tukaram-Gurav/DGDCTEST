package com.cwms.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.cwms.entities.Airline;
import com.cwms.entities.Branch;
import com.cwms.entities.Company;
import com.cwms.entities.ExternalParty;
import com.cwms.entities.Import;
import com.cwms.entities.ImportSub;
import com.cwms.entities.Import_History;
import com.cwms.entities.Party;
import com.cwms.repository.AirlineRepository;
import com.cwms.repository.BranchRepo;
import com.cwms.repository.CompanyRepo;
import com.cwms.repository.ExternalPartyRepository;
import com.cwms.repository.ImportRepo;
import com.cwms.repository.ImportRepository;
import com.cwms.repository.PartyRepository;
import com.cwms.service.AirlineServiceImpliment;
import com.cwms.service.ImportService;
import com.cwms.service.ImportServiceImpl;
import com.cwms.service.Import_HistoryService;
import com.cwms.service.Importserviceforpctm;
import com.cwms.service.ProcessNextIdService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Image;
import com.lowagie.text.DocumentException;

@RestController
@CrossOrigin("*")
@RequestMapping("/import")
public class ImportController {

	@Autowired
	private ImportRepository imprepo;
	
	
	@Autowired
	AirlineRepository airlineRepository;

	@Autowired
	AirlineServiceImpliment airlineServiceImpliment;
	
	
	@Autowired
	private TemplateEngine templateEngine;

	
	@Autowired
	private ImportServiceImpl importService;
	
	@Autowired
	private Import_HistoryService historyService;
	
	@Autowired
	public ImportService importServices;
	
	@Autowired
	private PartyRepository partyRepository;
	
	@Autowired
	public ProcessNextIdService proccessNextIdService;
	
	@Autowired
	private Importserviceforpctm importservicepctm;
	
	@Autowired
	private CompanyRepo companyRepo;
	
	@Autowired
	private BranchRepo branchRepo;
	
	
	@Autowired
	private ImportRepo importRepo;
	
	@Autowired
	private ExternalPartyRepository externalPartyRepository;
	
	
	
	@GetMapping(value = "/getDoNumberForUpdate/{companyId}/{branchId}")
	public List<Import> getDoNumberForUpdate(@PathVariable("companyId") String companyId,
			@PathVariable("branchId") String branchId) {

		return importRepo.findByCompanyAndBranchNUllDo(companyId, branchId);
	}

	@GetMapping(value = "/getUpdateDoNumber/{companyId}/{branchId}")
	public List<String> getUpdateDoNumber(@PathVariable("companyId") String companyId,
			@PathVariable("branchId") String branchId) {
		List<String> strings = new ArrayList<>();
		List<String> mawbStrings = importRepo.findMawbByCompanyAndBranch(companyId, branchId);


		for (String mawb : mawbStrings) {
			List<Import> importList = importRepo.findByCompanyAndBranchAndMawbListupdated(companyId, branchId, mawb);
			String olddo = importRepo.findByCompanyAndBranchAndMawb(companyId, branchId, mawb);
			String temp = null;

			if (olddo != null && !olddo.trim().isEmpty()) {
				temp = olddo;
			} else
				temp = proccessNextIdService.autoIncrementDoNumber();
			for (Import import1 : importList) {

//				if (import1.getDoDate() != null) {
//					import1.setDoNumber(temp);
//				} else {
					import1.setDoNumber(temp);
					import1.setDoDate(new Date());
//				}

			}
			importRepo.saveAll(importList);

		}

		return strings;
	}

	
	
	
	
	
	@PutMapping("/{compId}/{branchId}/importDataAndUpdatePCTM")
	public List<Import> getImportDataAndUpdatePCTM(@PathVariable("compId")String compId ,@PathVariable("branchId")String branchId  ,@RequestBody List<Import> importList) {

		String updatedCount = proccessNextIdService.generateAndIncrementPCTMNumber();		

		for (Import impo : importList) {

			imprepo.updatePctmNo(compId, branchId, impo.getSirNo(),impo.getMawb(),impo.getHawb(), updatedCount);
		
		}
		return importList;
	}
	
	
	@GetMapping("/getDataforPctmPrint")
	public List<Import> getDataforPctmPrint(@RequestParam(value="companyId",required = false) String companyId,
			@RequestParam(value="branchId",required = false) String branchId,
			@RequestParam(value="pctmNo",required = false) String pctmNo,
			@RequestParam(value="startDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value="endDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value="console",required = false) String console,@RequestParam(value="airlineCode",required = false) String airlineCode) {
	
		return imprepo.findImportAllDataPctmNumber(companyId, branchId, startDate, endDate, airlineCode,console,pctmNo);
	}
	
	
	@GetMapping("/allimportData")
	public List<Import> getAllImportData(@RequestParam(value="companyId",required = false) String companyId,
			@RequestParam(value="branchId",required = false) String branchId,
			@RequestParam(value="startDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value="endDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value="console",required = false) String console,@RequestParam(value="airlineCode",required = false) String airlineCode) {
	
		return imprepo.findImportAllData(companyId, branchId, startDate, endDate, airlineCode,console);
	}
		
	
	
	
	@GetMapping("/printOfImportPctm")
	public ResponseEntity<String> generateGateAllInvoiceDataNewGst(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam("console") String console,
			@RequestParam("airlineCode") String airlineCode, @RequestParam("pctmNo") String pctmNo) {
		try {
			Context context = new Context();			
			
			List<Import> pctmAll = importRepo.findImportPCTMData(companyId, branchId, startDate, endDate, airlineCode, pctmNo,console);
//			System.out.println(pctmAll);
//			
//			System.out.println("Got ");
//			for (Import i:pctmAll)
//			{				
//				System.out.println(i);
//			}
			
			
			 Import firstImport = pctmAll.get(0);

			    String flightNo = firstImport.getFlightNo();
			    String airlineName = firstImport.getAirlineName();

			    context.setVariable("airlineCode", airlineCode);
				context.setVariable("AirlineName", airlineName);
				
				
				LocalDateTime currentDateTime = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");		
				DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				context.setVariable("ReportDateTime", currentDateTime.format(formatter));
				context.setVariable("LocalDate", currentDateTime.format(formatter2));
				
			
			Set<String> distinctPctmNos = pctmAll.stream()
			        .filter(invoice -> pctmNo.equals(invoice.getPctmNo()))
			        .map(Import::getPctmNo)
			        .collect(Collectors.toSet());

			List<String> selectedPctmNos = new ArrayList<>(distinctPctmNos);

			Set<String> distinctIgmNos = pctmAll.stream()
			        .filter(invoice -> selectedPctmNos.contains(invoice.getPctmNo()))
			        .map(Import::getIgmNo)
			        .collect(Collectors.toSet());


			List<String> selectedIgmNos = new ArrayList<>(distinctIgmNos);

			context.setVariable("selectedIgmNos", selectedIgmNos);
				

			Date flightDate = getFlightDate(pctmAll);
 
			int totalDistinctMawbNo=getDistinctMawbFromImport(pctmAll);
			
			int totalDistinctIgmNo=getDistinctIgmNoFromImport(pctmAll);
			

			int totalNoOfPackages = pctmAll.stream()
                    .mapToInt(Import::getNop)
                    .sum();

			
			if (pctmAll != null) {
				context.setVariable("invoiceAll", pctmAll);
				context.setVariable("startDate", startDate);
				context.setVariable("endDate", endDate);
				context.setVariable("flightNo", flightNo);
				context.setVariable("flightNameOnly",airlineName);
				context.setVariable("flightName", airlineCode+" / "+airlineName);
				context.setVariable("flightDate", flightDate);
				context.setVariable("totalNoOfPackages", totalNoOfPackages);
				context.setVariable("totalDistinctMawbNo", totalDistinctMawbNo);
				context.setVariable("totalDistinctIgmNo", totalDistinctIgmNo);
			}

			// Process the HTML template with dynamic values
			String htmlContent = templateEngine.process("import_pctm_Report", context);

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
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/all/{cid}/{bid}")
	public List<Import> getAll1(@PathVariable("cid") String cid,@PathVariable("bid") String bid) {
		return this.imprepo.findByAll(cid,bid);
	}
	
	@GetMapping("/single/{cid}/{bid}/{sir}")
	public Import getSingledata(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("sir") String sir) {
		return this.importRepo.Singledata(cid, bid, sir);
	}

	
	@GetMapping("/tpdate")
	public List<String> getAllbytpdate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,@RequestParam("cid") String cid,@RequestParam("bid") String bid) {
		
		return imprepo.findByTp(date,cid,bid);
	}
	
	@GetMapping("/getalldata")
	public List<Import> getallbyTpnoandTpdate(
	    @RequestParam("cid") String cid,
	    @RequestParam("bid") String bid,
	    @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
	    @RequestParam("tpno") String tpno
	  //  @RequestParam("status") char status
	    ) { // Change the parameter name to "status"
	    return imprepo.findByTpdateTpno(cid, bid, date, tpno); // Use "status" parameter here
	}
	
	@GetMapping("/importData")
	public List<Object[]> getImportData(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,

			@RequestParam("airlineName") String airlineName) {
		
//		 System.out.println("Received companyId: " + companyId);
//		    System.out.println("Received branchId: " + branchId);
//		    System.out.println("Received startDate: " + startDate);
//		    System.out.println("Received endDate: " + endDate);
//		    System.out.println("Received airlineName: " + airlineName);
		List<Object[]> imp = importService.findImportData(companyId, branchId, startDate, endDate, airlineName);

		
//		for (Object[] i : imp) {
//			System.out.println(i);
//		}
		return imp;
	}

	// Dyanamic
	@GetMapping("/airline-names")
	public List<String> getAirlineNames(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//		List<String> airlineNames = importService.findAirlineName(companyId, branchId, startDate, endDate);
//		 
//		Import i= new Import();
//		i.getSirDate();
//		System.out.println( "Sir Date is" +i.getSirDate());
//
//		// Print each airline name individually
//		for (String name : airlineNames) {
//			System.out.println(name);
//		}
//
//		return airlineNames;
		
		return importRepo.findAirlineNames(companyId, branchId, startDate, endDate);
	}
	
	
	
	@GetMapping("/airlineNames/{companyId}/{branchId}/{startDate}/{endDate}")
	public List<String> getallAirlineNames(@PathVariable("companyId") String companyId,
			@PathVariable("branchId") String branchId,
			@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//		List<String> airlineNames = importService.findAirlineName(companyId, branchId, startDate, endDate);
//		 
//		Import i= new Import();
//		i.getSirDate();
//		System.out.println( "Sir Date is" +i.getSirDate());
//
//		// Print each airline name individually
//		for (String name : airlineNames) {
//			System.out.println(name);
//		}
//
//		return airlineNames;
		
		return importRepo.findAirlineNames(companyId, branchId, startDate, endDate);
	}
	
	
	
	

	
	
	
		
	
	
	
	@GetMapping("/getPctmNo")
	public List<String> getPctmNo(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam("flightNo") String flightNo) {

//		 System.out.println("Received companyId: " + companyId);
		return importRepo.findDistinctPctmNos(companyId, branchId, startDate, endDate, flightNo);
	}
	
	// Dyanamic
		@GetMapping("/Allairline-names")
		public List<String> getAllAirlineNames(@RequestParam("companyId") String companyId,
				@RequestParam("branchId") String branchId,
				@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
				@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
			
			
			
			List<String> flightNo = importService.findAllAirlineName(companyId, branchId, startDate, endDate);
//			 System.out.println(airlineName);
			return flightNo;
		}

	
//		@GetMapping("/allimportPCTMData")
//		public List<Import> getAllImportPCTMData(@RequestParam("companyId") String companyId,
//				@RequestParam("branchId") String branchId,
//				@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//				@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
//
//				@RequestParam("flightNo") String flightNo, @RequestParam("pctmNo") String pctmNo) {
//
////			 System.out.println("Received companyId: " + companyId);
//			return importRepo.findImportPCTMData(companyId, branchId, startDate, endDate, flightNo, pctmNo);
//		}
		
		
		
		
		
		
		
		
		public Set<Party> FilterParty(List<Import> list, String cid, String bid) {

			Set<Party> filterParty = new LinkedHashSet<>();
			List<Party> parties = partyRepository.getalldata(cid, bid);

			for (Import packages : list) {
				String partyIdToMatch = packages.getImporterId();
				for (Party party : parties) {
					if (partyIdToMatch.equals(party.getPartyId())) {
						filterParty.add(party);
						break; // Break the inner loop once a match is found
					}
				}
			}

			for (Party party : filterParty) {
				System.out.println(party);
			}

			return filterParty;
		}

		public String getAirlineName(String companyId, String branchId, String flightNo) {
			List<Airline> airlines = airlineServiceImpliment.getListAirlines(companyId, branchId);
			String flightName = null;
			System.out.println(airlines);
			for (Airline a : airlines) {
				if (a.getflightNo() == flightNo || a.getflightNo().equals(flightNo)) {
					System.out.println(a + "ABCD");
					flightName = a.getAirlineName();

				}
			}

			return flightName;
		}

		public Date getFlightDate(List<Import> pctmAll) {

			Date flightDate = null;

			Import fdate = pctmAll.get(0);

			flightDate = fdate.getFlightDate();

			return flightDate;
		}

		public String getConsoleNameFromExternal(List<ExternalParty> extparty, String consoleName) {
			String userNAme = null;
			for (ExternalParty a : extparty) {
				if (a.getExternaluserId().equals(consoleName)) {

//					System.out.println(a.getShortsForm());
					userNAme = a.getShortsForm();

				}
			}

			return userNAme;
		}
		
		
		
		public String getPartyNameFromImporterId(List<Party> Party, String importerId) {
			String userNAme = null;
			for (Party a : Party) {
				if (a.getPartyId().equals(importerId)) {

//					System.out.println(a.getUserName());
					userNAme = a.getPartyName();
				}
			}

			return userNAme;
		}
		
		
		public int getDistinctMawbFromImport(List<Import> pctmAll) {
			
			Set<String> distinctMwab= new HashSet<>();
			
			for (Import a : pctmAll) {
				distinctMwab.add(a.getMawb());
			}

			return distinctMwab.size();
		}
		
	public int getDistinctIgmNoFromImport(List<Import> pctmAll) {
			
			Set<String> distinctIgmNo= new HashSet<>();
			
			for (Import a : pctmAll) {
				distinctIgmNo.add(a.getIgmNo());
			}

			return distinctIgmNo.size();
		}

	private String formatMAWBNumber(String mawb) {
	    if (mawb != null && mawb.length() == 11) {
	        return String.format("%s-%s-%s", mawb.substring(0, 3), mawb.substring(3, 7), mawb.substring(7));
	    } else {
	        // Handle invalid or unexpected input, or just return the input if it doesn't match the expected format.
	        return mawb;
	    }
	}

		
		
		
		
		
	
	
		public String formatPctmNo(String pctmNo) {

			String formatedPCTM = pctmNo.replaceFirst("^0+", "");
			return formatedPCTM;

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
		
		
	
		
		
		
		
		
		
		
		
		
		@PostMapping("/importTpPrint")
		public ResponseEntity<String> generateNewGstPartReport(@RequestParam("cid") String cid,
				@RequestParam("bid") String bid, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
				@RequestParam("tpno") String tpno,@RequestParam("vehicle") String vehicle) {

			try {
				
				
				System.out.println("Vehicle "+vehicle );
				List<Party> partyData = partyRepository.getalldata(cid, bid);

				List<Import> imp = imprepo.findByTpdateTpno(cid, bid, date, tpno);

				List<ExternalParty> extparty = externalPartyRepository.getAllExternalParties(cid, bid);

				if (imp != null) {
					Context context = new Context();

					context.setVariable("imp", imp);
					context.setVariable("extparty", extparty);
					context.setVariable("date", date);
					context.setVariable("vehicle", vehicle.toUpperCase());

					tpno = tpno.replaceFirst("^0+", "");
					context.setVariable("tpno", tpno);

					int TotalNoOfPackage = 0;
				

					for (Import im : imp) {
						TotalNoOfPackage = TotalNoOfPackage + im.getNop();

						im.setPctmNo(formatPctmNo(im.getPctmNo()));
						im.setConsoleName(getConsoleNameFromExternal(extparty, im.getConsoleName()));

					}
//					System.out.println(convertToWords(TotalNoOfPackage));

					context.setVariable("TotalNoOfPackage", TotalNoOfPackage);
					context.setVariable("TotalNoOfPackageInWord", convertToWords(TotalNoOfPackage));

					String imagePath = "/Users/macbook/Mahesh1310/CWMS_JAVA/src/main/resources/static/image/DGDC1.png";
					File imageFile = new File(imagePath);
					if (imageFile.exists()) {
						Image image = Image.getInstance(imagePath);
						image.scaleToFit(400, 300); //
						context.setVariable("dgdclogo", image);
					} else {
						System.out.println("Image not found"); // Handle the case where the image does not exist
					}

					// Process the HTML template with dynamic values
					String htmlContent = templateEngine.process("import_tp_report", context);

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
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				// Handle exceptions appropriately
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}


		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

	
	
	
	
	
	@GetMapping("/{compid}/{branchId}/{tranId}/{MAWB}/{HAWB}/{sirNo}/getSingle")
	public Import findByMAWBANDHAWB(
	        @PathVariable("MAWB") String MAWB,
	        @PathVariable("HAWB") String HAWB,
	        @PathVariable("compid") String compid,
	        @PathVariable("branchId") String branchId,
	        @PathVariable("tranId") String transId,
	        @PathVariable("sirNo") String sirNo) {
	    
	    return importServices.getByMAWBANdHAWB(compid, branchId, transId, MAWB, HAWB, sirNo);
	}
	
	
	@GetMapping("/{cid}/{bid}/{MAWBNo}")
	public List<Import> getByMawbNo(@PathVariable("MAWBNo")String MAWBNo,@PathVariable("cid") String cid, @PathVariable("bid") String bid)
	{
		return importServices.getByMAWB(cid,bid,MAWBNo);
	}
	
	@GetMapping("/{cid}/{bid}/All")
	public List<Import> getAll(@PathVariable("cid") String cid, @PathVariable("bid") String bid)
	{
		return importServices.getAll(cid,bid);
	}
	
	
	@PostMapping("/{compid}/{branchId}/{user}/add")
	public Import addImport(@PathVariable("compid")String compid,@PathVariable("branchId")String branchId,
			@RequestBody Import import2,@PathVariable("user")String User)
	{
		import2.setCompanyId(compid);
		import2.setBranchId(branchId);
		import2.setNSDL_Status("");
		import2.setDGDC_Status("Handed over to DGDC Cargo");
		
		String autoIncrementIMPTransId = proccessNextIdService.autoIncrementIMPTransId();
		import2.setImpTransId(autoIncrementIMPTransId);
		String autoIncrementSIRId = proccessNextIdService.autoIncrementSIRId();
		import2.setSirNo(autoIncrementSIRId);
		import2.setSirDate(new Date());
		import2.setImpTransDate(new Date());
		import2.setCreatedBy(User);
		import2.setCreatedDate(new Date());
		import2.setStatus("A");
		import2.setApprovedBy(User);
		import2.setApprovedDate(new Date());
		import2.setEditedBy(User);
		import2.setEditedDate(new Date());
		
		Import_History history=new Import_History();
		history.setCompanyId(compid);
		history.setBranchId(branchId);
		history.setSirNo(autoIncrementSIRId);
		history.setMawb(import2.getMawb());
		history.setHawb(import2.getHawb());
		history.setTransport_Date(new Date());
		history.setOldStatus("Pending");
		history.setNewStatus("Handed over to DGDC Cargo");
		history.setUpdatedBy(User);
		historyService.addHistory(history);
		return importServices.addImport(import2);
	}
	
	@PutMapping("/{compid}/{branchId}/{user}/update")
	public Import updateImport(@PathVariable("compid")String compid,@PathVariable("branchId")String branchId,
			@RequestBody Import import2,@PathVariable("user")String User)
	{
//		import2.setBranchId(branchId);
		import2.setEditedBy(User);
		import2.setEditedDate(new Date());
//		
		return importServices.updateImport(import2);
	}
	
	@PutMapping("/{compid}/{branchId}/{user}/modifyupdate")
	public Import updateImportByIMpTransId(@PathVariable("compid")String compid,@PathVariable("branchId")String branchId,
			@RequestBody Import import2,@PathVariable("user")String User)
	{
//		import2.setBranchId(branchId);
		
		Import existingImport = importServices.findBytransIdAndSirNo(compid,branchId,import2.getImpTransId(),import2.getSirNo());
		
		if(existingImport != null)
		{
			
			importServices.deleteImport(existingImport);
			Import newImport=new Import();
			
		newImport.setEditedBy(User);
		newImport.setEditedDate(new Date());
		newImport.setAirlineName(import2.getAirlineName());
		newImport.setCreatedBy(import2.getCreatedBy());
		newImport.setCreatedDate(import2.getCreatedDate());
		newImport.setApprovedBy(import2.getApprovedBy());
		newImport.setApprovedDate(import2.getApprovedDate());
		newImport.setCompanyId(import2.getCompanyId());
		newImport.setBranchId(import2.getBranchId());
		newImport.setMawb(import2.getMawb());
		newImport.setHawb(import2.getHawb());
		newImport.setSirNo(import2.getSirNo());
		newImport.setNop(import2.getNop());
		newImport.setImporterId(import2.getImporterId());
		newImport.setConsoleName(import2.getConsoleName());
		newImport.setImportRemarks(import2.getImportRemarks());
		newImport.setBeDate(import2.getBeDate());	
		newImport.setSirDate(import2.getSirDate());
		newImport.setImpTransId(import2.getImpTransId());
		newImport.setImpTransDate(import2.getImpTransDate());
		newImport.setBeDate(import2.getBeDate());
		newImport.setIec(import2.getIec());
		newImport.setBeNo(import2.getBeNo());
		newImport.setBeRequestId(import2.getBeRequestId());
		newImport.setIgmNo(import2.getIgmNo());
		newImport.setIgmDate(import2.getIgmDate());
		newImport.setPctmNo(import2.getPctmNo());
		newImport.setPackageContentType(import2.getPackageContentType());
		newImport.setUomPackages(import2.getUomPackages());
		newImport.setUomWeight(import2.getUomWeight());
		newImport.setTpNo(import2.getTpNo());
		newImport.setTpDate(import2.getTpDate());
		newImport.setFlightNo(import2.getFlightNo());
		newImport.setFlightDate(import2.getFlightDate());
		newImport.setCountryOrigin(import2.getCountryOrigin());
		newImport.setPortOrigin(import2.getPortOrigin());
		newImport.setNSDL_Status(import2.getNSDL_Status());
		newImport.setDGDC_Status(import2.getDGDC_Status());
		newImport.setDescriptionOfGoods(import2.getDescriptionOfGoods());
		newImport.setImportAddress(import2.getImportAddress());
		newImport.setChaCde(import2.getChaCde());
		newImport.setAssessableValue(import2.getAssessableValue());
		newImport.setGrossWeight(import2.getGrossWeight());
		newImport.setStatus(import2.getStatus());
		
		return importServices.updateImport(newImport);
		}
		
		return null;
	}
	
	@DeleteMapping("/{compid}/{branchId}/{tranId}/{MAWB}/{HAWB}/{sirNo}/delete")
	public void deleteImport(@PathVariable("MAWB") String MAWB,
	        @PathVariable("HAWB") String HAWB,
	        @PathVariable("compid") String compid,
	        @PathVariable("branchId") String branchId,
	        @PathVariable("tranId") String transId,
	        @PathVariable("sirNo") String sirNo)
	{
		Import byMAWBANdHAWB = importServices.getByMAWBANdHAWB(compid, branchId, transId, MAWB, HAWB, sirNo);
		if(byMAWBANdHAWB != null)
		{
			byMAWBANdHAWB.setStatus("D");
			importServices.updateImport(byMAWBANdHAWB);
		}
	}
	
	@GetMapping("/importTransaction")
    public ResponseEntity<List<Import>> findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(
			@RequestParam("sirDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate,
            @RequestParam("companyId") String companyId,
            @RequestParam("branchId") String branchId,
            @RequestParam("dgdcStatus") String dgdcStatus) {

	 
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(sirDate);
		
	 System.out.println(formattedDate);
	 
        List<Import> imports = importservicepctm.findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(formattedDate, companyId, branchId, dgdcStatus);
        
        System.out.println(imports);
        if (imports.isEmpty()) {
            return ResponseEntity.notFound().build();
            
        }

        return ResponseEntity.ok(imports);
    }

	@GetMapping(value = "/findCompanyname/{cid}")
	public String findCompanyname(@PathVariable("cid") String param) {
	Company company=	 companyRepo.findByCompany_Id(param);
		
		return company.getCompany_name();
	}

	@GetMapping(value = "/findBranchName/{cid}/{bid}")
	public String findBranchName(@PathVariable("cid") String cid,@PathVariable("bid") String bid) {
	Branch branch =branchRepo.findByBranchIdWithCompanyId(cid, bid);
		return branch.getBranchName();
	}
	
	

	@GetMapping(value = "/getConsole/{companyId}/{branchId}/{doDate}")
    public List<ExternalParty> getConsoleList(
            @PathVariable("companyId") String companyId,
            @PathVariable("branchId") String branchId,
            @PathVariable("doDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date doDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(doDate);

        List<String> cIds = importRepo.findByCompanyAndBranchAndDoDate(companyId, branchId, formattedDate);

        List<ExternalParty> externalParties = new ArrayList<>();
       
        for (String string : cIds) {
           
            ExternalParty external = externalPartyRepository.findBycompbranchexternal(companyId, branchId, string);
            if (external != null) {
                externalParties.add(external);
            }
        }

        return externalParties;
    }

	@GetMapping(value = "/getImportList/{companyId}/{branchId}/{doDate}/{exId}")
	public List<Import> getImportList(@PathVariable("companyId") String companyId,
			@PathVariable("branchId") String branchId,
			@PathVariable("doDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date doDate,
			@PathVariable("exId") String exId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(doDate);
		List<Import> imports = importRepo.findByCompanyAndBranchAndDoDateAndexternalPId(companyId, branchId,
				formattedDate, exId);
		return imports;
	}

	@GetMapping(value = "/getDoNumber/{companyId}/{branchId}/{mawb}")
	public String getDoNumber(@PathVariable("companyId") String companyId, @PathVariable("branchId") String branchId,
			@PathVariable("mawb") String mawb) {
//		System.out.println(companyId + "\t" + branchId + "\t" + mawb);
//		System.out.println(importRepo.findByCompanyAndBranchAndMawb(companyId, branchId, mawb));
		return importRepo.findByCompanyAndBranchAndMawb(companyId, branchId, mawb);
	}

	
	@GetMapping(value = "/getExternalPartys/{companyId}/{branchId}")
	public List<ExternalParty> getExternalPartyUserName(@PathVariable("companyId") String companyId,
			@PathVariable("branchId") String branchId) 
	{

		return externalPartyRepository.getalldataExternalParties(companyId, branchId);
	}
	
	@GetMapping("/findImportAllData")
	public List<Import> findExportSubData(@RequestParam("companyId") String companyId,
			@RequestParam("branchId") String branchId,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,

			@RequestParam("consoleName") String consoleName) {
		return imprepo.findIMMportAllData(companyId, branchId, startDate, endDate, consoleName);
	}

//	@GetMapping("/findImportData")
//	public List<Import> findExportSubData(@RequestParam("companyId") String companyId,
//			@RequestParam("branchId") String branchId,
//			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
//
//	) {
//		return imprepo.findImportData(companyId, branchId, startDate, endDate);
//	}
	@GetMapping("/findPartyName/{companyId}/{branchId}/{partyId}")
	public String findPartyNameByKeys(@PathVariable String companyId, @PathVariable String branchId,
			@PathVariable String partyId) {
		String partyName = partyRepository.findPartyNameByKeys(companyId, branchId, partyId);

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
		String console_name = externalPartyRepository.findUserNameByKeys(companyId, branchId, externaluserId);

		if (console_name != null) {
			return console_name;
		} else {
			// Handle the case where partyName is not found
			return "User Name Not Found";
		}
	}
	
	
	
	//ImprtController

	@PostMapping("/tpexcel")
		public ResponseEntity<byte[]> generateTPEXCEL(@RequestBody List<Import> import1) {
			System.out.println("Hiii "+import1);
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

		        headerRow.createCell(0).setCellValue("SIR NO");
		        headerRow.createCell(1).setCellValue("Parcel Type");
		        headerRow.createCell(2).setCellValue("PCTM No");
		        headerRow.createCell(3).setCellValue("No Of Packages");
		        headerRow.createCell(4).setCellValue("Description Of Goods");
		        headerRow.createCell(5).setCellValue("Gross Weight");
		        headerRow.createCell(6).setCellValue("Value");
		        headerRow.createCell(7).setCellValue("Port Of Origin");

		        // Apply style to header cells
		        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
		            headerRow.getCell(i).setCellStyle(headerStyle);
		        }

		        // Create data rows
		        int rowNum = 1;

		        for (Import importObj : import1) {
		            BigDecimal gross = importObj.getGrossWeight();
		            // Replace with your actual BigDecimal value
		            double convertedValue = gross.doubleValue();
		            Row row = sheet.createRow(rowNum++);
		            row.createCell(0).setCellValue(importObj.getSirNo());
		            row.createCell(1).setCellValue(importObj.getParcelType());
		            row.createCell(2).setCellValue(importObj.getPctmNo());
		            row.createCell(3).setCellValue(importObj.getNop());
		            row.createCell(4).setCellValue(importObj.getDescriptionOfGoods());
		            row.createCell(5).setCellValue(convertedValue);
		            row.createCell(6).setCellValue(importObj.getAssessableValue().doubleValue());
		            row.createCell(7).setCellValue(importObj.getPortOrigin());
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
		        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		        headers.setContentDispositionFormData("attachment", "import_data.xlsx");

		        // Return the Excel file as a byte array in the response body
		        return ResponseEntity.ok()
		                .headers(headers)
		                .body(outputStream.toByteArray());

		    } catch (IOException e) {
		        e.printStackTrace();
		        // Handle the exception appropriately (e.g., log it and return an error response)
		        return ResponseEntity.status(500).build();
		    }
		}
		
		
		
		@PostMapping("/pctmexcel")
		public ResponseEntity<byte[]> generatePCTMEXCEL(@RequestBody List<Import> import1) {
			
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
		        headerRow.createCell(1).setCellValue("MAWB NO");
		        headerRow.createCell(2).setCellValue("HAWB No");
		        headerRow.createCell(3).setCellValue("SIR No");
		        headerRow.createCell(4).setCellValue("SIR Date");
		        headerRow.createCell(5).setCellValue("Parcel Type");
		        headerRow.createCell(6).setCellValue("No Of Packages");
		        headerRow.createCell(7).setCellValue("Importer Name");
		       

		        // Apply style to header cells
		        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
		            headerRow.getCell(i).setCellStyle(headerStyle);
		        }

		        // Create data rows
		        int rowNum = 1;

		        for (Import importObj : import1) {
		      		        	 // Your input Date object
		            Date inputDate = importObj.getSirDate();

		            // Create a SimpleDateFormat object with the desired format
		            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		            // Format the Date object
		            String formattedDate = sdf.format(inputDate);
		         
		            Row row = sheet.createRow(rowNum++);
		            row.createCell(0).setCellValue(rowNum - 1);
		            row.createCell(1).setCellValue(importObj.getMawb());
		            row.createCell(2).setCellValue(importObj.getHawb());
		            row.createCell(3).setCellValue(importObj.getSirNo());
		            row.createCell(4).setCellValue(formattedDate);
		            row.createCell(5).setCellValue(importObj.getParcelType());
		            row.createCell(6).setCellValue(importObj.getNop());
		            row.createCell(7).setCellValue(importObj.getImporterId());
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
		        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		        headers.setContentDispositionFormData("attachment", "import_data.xlsx");

		        // Return the Excel file as a byte array in the response body
		        return ResponseEntity.ok()
		                .headers(headers)
		                .body(outputStream.toByteArray());

		    } catch (IOException e) {
		        e.printStackTrace();
		        // Handle the exception appropriately (e.g., log it and return an error response)
		        return ResponseEntity.status(500).build();
		    }
		}
		
		
		
		@PostMapping("/registerexcel")
		public ResponseEntity<byte[]> generateRegisterEXCEl(@RequestBody Map<String, Object> requestData) {
		    try {
		    	 ObjectMapper objectMapper = new ObjectMapper();
		         List<Import> export1 = objectMapper.convertValue(requestData.get("expdata"), new TypeReference<List<Import>>() {});
		         List<ImportSub> exportsub = objectMapper.convertValue(requestData.get("expsubdata"), new TypeReference<List<ImportSub>>() {});

		  
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
		        headerRow.createCell(1).setCellValue("SIR NO");
		        headerRow.createCell(2).setCellValue("SIR Date");
		        headerRow.createCell(3).setCellValue("Importer");
		        headerRow.createCell(4).setCellValue("No Of Packages");
		        headerRow.createCell(5).setCellValue("DESC");
		        headerRow.createCell(6).setCellValue("DGDC Status");
		        headerRow.createCell(7).setCellValue("BE No");
		        headerRow.createCell(8).setCellValue("BE Date");
		        headerRow.createCell(9).setCellValue("Gross Weight");
		        headerRow.createCell(10).setCellValue("Assessible Value");
		        headerRow.createCell(11).setCellValue("IGM No");
		        headerRow.createCell(12).setCellValue("MAWB No");
		        headerRow.createCell(13).setCellValue("HAWB no");
		        headerRow.createCell(14).setCellValue("Console IS");
		        
		        // Apply style to header cells
		        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
		            headerRow.getCell(i).setCellStyle(headerStyle);
		        }

		        // Create data rows
		        int rowNum = 1;

		        // First loop (export1)
		        for (Import exportObj : export1) {
		            
		            ExternalParty externalparty = externalPartyRepository.getalldatabyid(exportObj.getCompanyId(), exportObj.getBranchId(), exportObj.getConsoleName());
		        	Date inputDate = exportObj.getSirDate();
		        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		            String formattedDate = sdf.format(inputDate);
		            String formattedDate1 = "";
		            if(exportObj.getBeDate() != null) {
		                Date inputDate1 = exportObj.getBeDate();
			        	SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			             formattedDate1 = sdf.format(inputDate1);
		            }
		            
		            BigDecimal gross = exportObj.getGrossWeight();
		            // Replace with your actual BigDecimal value
		            double convertedValue = gross.doubleValue();
		            
		            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(exportObj.getCompanyId(), exportObj.getBranchId(), exportObj.getImporterId());
		            Row row = sheet.createRow(rowNum++);
		            row.createCell(0).setCellValue(rowNum - 1);
		            row.createCell(1).setCellValue(exportObj.getSirNo());
		            row.createCell(2).setCellValue(formattedDate);
		            row.createCell(3).setCellValue(party.getPartyName());
		            row.createCell(4).setCellValue(exportObj.getNop());
		            row.createCell(5).setCellValue(exportObj.getDescriptionOfGoods());
		            row.createCell(6).setCellValue(exportObj.getDgdcStatus());
		            row.createCell(7).setCellValue(exportObj.getBeNo());
		            if(formattedDate1.endsWith("1970")) {
		            	row.createCell(8).setCellValue("");
		            }
		            else {
		            	row.createCell(8).setCellValue(formattedDate1);
		            }
		            row.createCell(9).setCellValue(convertedValue);
		            row.createCell(10).setCellValue(exportObj.getAssessableValue().doubleValue());
		            row.createCell(11).setCellValue(exportObj.getIgmNo());
		            row.createCell(12).setCellValue(exportObj.getMawb());
		            row.createCell(13).setCellValue(exportObj.getHawb());
		            row.createCell(14).setCellValue(externalparty.getUserName());
		        }

		        // Second loop (exportsub)
		        for (ImportSub exportsubObj : exportsub) {
		            Date inputDate = exportsubObj.getSirDate();
//		            BigDecimal gross = exportsubObj.getGwWeight();
//		            double convertedValue = gross.doubleValue();
		            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		            String formattedDate = sdf.format(inputDate);
		            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(exportsubObj.getCompanyId(), exportsubObj.getBranchId(), exportsubObj.getExporter());
		            Row row = sheet.createRow(rowNum++);
		            row.createCell(0).setCellValue(rowNum - 1);
		            row.createCell(1).setCellValue(exportsubObj.getSirNo());
		            row.createCell(2).setCellValue(formattedDate);
		            row.createCell(3).setCellValue(party.getPartyName());
		            row.createCell(4).setCellValue(exportsubObj.getNop());
		            row.createCell(5).setCellValue(exportsubObj.getImportType());
		            row.createCell(6).setCellValue(exportsubObj.getDgdcStatus());
		            row.createCell(7).setCellValue("");
		            row.createCell(8).setCellValue("");
		            row.createCell(9).setCellValue("");
		            row.createCell(10).setCellValue("");
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
		        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		        headers.setContentDispositionFormData("attachment", "export_data.xlsx");

		        // Return the Excel file as a byte array in the response body
		        return ResponseEntity.ok()
		                .headers(headers)
		                .body(outputStream.toByteArray());

		    } catch (IOException e) {
		        e.printStackTrace();
		        // Handle the exception appropriately (e.g., log it and return an error response)
		        return ResponseEntity.status(500).build();
		    }
		}
		
		
//		@PostMapping("/registerexcel1")
//		public ResponseEntity<byte[]> generateRegisterEXCEl1(@RequestBody List<Import> export1) {
//		    try {
//		    	
//		  
//		        Workbook workbook = new XSSFWorkbook();
//		        // Create a sheet
//		        Sheet sheet = workbook.createSheet("Export Data");
//
//		        // Create header row and set font style
//		        Row headerRow = sheet.createRow(0);
//		        CellStyle headerStyle = workbook.createCellStyle();
//		        Font font = workbook.createFont();
//		        font.setBold(true);
//		        headerStyle.setFont(font);
//
//		        headerRow.createCell(0).setCellValue("SR No");
//		        headerRow.createCell(1).setCellValue("SER NO");
//		        headerRow.createCell(2).setCellValue("SB NO");
//		        headerRow.createCell(3).setCellValue("SER Date");
//		        headerRow.createCell(4).setCellValue("Parcel Type");
//		        headerRow.createCell(5).setCellValue("Exporter Names");
//		        headerRow.createCell(6).setCellValue("CHA NO");
//		        headerRow.createCell(7).setCellValue("DESC");
//		        headerRow.createCell(8).setCellValue("No Of Packages");
//		        headerRow.createCell(9).setCellValue("Net Weight");
//		        headerRow.createCell(10).setCellValue("FOB Value IN INR");
//		        headerRow.createCell(11).setCellValue("Airway Bill No");
//		        headerRow.createCell(12).setCellValue("Port Of Destination");
//		        headerRow.createCell(13).setCellValue("Console IS");
//
//		        // Apply style to header cells
//		        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
//		            headerRow.getCell(i).setCellStyle(headerStyle);
//		        }
//
//		        // Create data rows
//		        int rowNum = 1;
//
//	  for (Import exportObj : export1) {
//		            
//		            ExternalParty externalparty = externalPartyRepository.getalldatabyid(exportObj.getCompanyId(), exportObj.getBranchId(), exportObj.getConsoleName());
//		        	Date inputDate = exportObj.getSirDate();
//		        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		            String formattedDate = sdf.format(inputDate);
//		            String formattedDate1 = "";
//		            if(exportObj.getBeDate() != null) {
//		                Date inputDate1 = exportObj.getBeDate();
//			        	SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
//			             formattedDate1 = sdf.format(inputDate1);
//		            }
//		            
//		            BigDecimal gross = exportObj.getGrossWeight();
//		            // Replace with your actual BigDecimal value
//		            double convertedValue = gross.doubleValue();
//		            
//		            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(exportObj.getCompanyId(), exportObj.getBranchId(), exportObj.getImporterId());
//		            Row row = sheet.createRow(rowNum++);
//		            row.createCell(0).setCellValue(rowNum - 1);
//		            row.createCell(1).setCellValue(exportObj.getSirNo());
//		            row.createCell(2).setCellValue(formattedDate);
//		            row.createCell(3).setCellValue(party.getPartyName());
//		            row.createCell(4).setCellValue(exportObj.getNop());
//		            row.createCell(5).setCellValue(exportObj.getDescriptionOfGoods());
//		            row.createCell(6).setCellValue(exportObj.getDgdcStatus());
//		            row.createCell(7).setCellValue(exportObj.getBeNo());
//		            if(formattedDate1.endsWith("1970")) {
//		            	row.createCell(8).setCellValue("");
//		            }
//		            else {
//		            	row.createCell(8).setCellValue(formattedDate1);
//		            }
//		            row.createCell(9).setCellValue(convertedValue);
//		            row.createCell(10).setCellValue(exportObj.getAssessableValue());
//		            row.createCell(11).setCellValue(exportObj.getIgmNo());
//		            row.createCell(12).setCellValue(exportObj.getMawb());
//		            row.createCell(13).setCellValue(exportObj.getHawb());
//		            row.createCell(14).setCellValue(externalparty.getUserName());
//		        }
//		
//
//		       
//
//		        // Adjust column sizes
//		        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
//		            sheet.autoSizeColumn(i);
//		        }
//
//		        // Create a ByteArrayOutputStream to write the workbook to
//		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		        workbook.write(outputStream);
//
//		        // Set headers for the response
//		        HttpHeaders headers = new HttpHeaders();
//		        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//		        headers.setContentDispositionFormData("attachment", "export_data.xlsx");
//
//		        // Return the Excel file as a byte array in the response body
//		        return ResponseEntity.ok()
//		                .headers(headers)
//		                .body(outputStream.toByteArray());
//
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		        // Handle the exception appropriately (e.g., log it and return an error response)
//		        return ResponseEntity.status(500).build();
//		    }
//		}
//		
		
		
		@PostMapping("/registerexcel1")
		public ResponseEntity<byte[]> generateRegisterEXCEl1(@RequestBody List<Import> export1) {
		    try {
		    	
		  
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
		        headerRow.createCell(1).setCellValue("SIR NO");
		        headerRow.createCell(2).setCellValue("SIR Date");
		        headerRow.createCell(3).setCellValue("Importer");
		        headerRow.createCell(4).setCellValue("No Of Packages");
		        headerRow.createCell(5).setCellValue("DESC");
		        headerRow.createCell(6).setCellValue("DGDC Status");
		        headerRow.createCell(7).setCellValue("BE No");
		        headerRow.createCell(8).setCellValue("BE Date");
		        headerRow.createCell(9).setCellValue("Gross Weight");
		        headerRow.createCell(10).setCellValue("Assessible Value");
		        headerRow.createCell(11).setCellValue("IGM No");
		        headerRow.createCell(12).setCellValue("MAWB No");
		        headerRow.createCell(13).setCellValue("HAWB no");
		        headerRow.createCell(14).setCellValue("Console IS");

		        // Apply style to header cells
		        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
		            headerRow.getCell(i).setCellStyle(headerStyle);
		        }

		        // Create data rows
		        int rowNum = 1;

	  for (Import exportObj : export1) {
		            
		            ExternalParty externalparty = externalPartyRepository.getalldatabyid(exportObj.getCompanyId(), exportObj.getBranchId(), exportObj.getConsoleName());
		        	Date inputDate = exportObj.getSirDate();
		        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		            String formattedDate = sdf.format(inputDate);
		            String formattedDate1 = "";
		            if(exportObj.getBeDate() != null) {
		                Date inputDate1 = exportObj.getBeDate();
			        	SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			             formattedDate1 = sdf.format(inputDate1);
		            }
		            
		            BigDecimal gross = exportObj.getGrossWeight();
		            // Replace with your actual BigDecimal value
		            double convertedValue = gross.doubleValue();
		            
		            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(exportObj.getCompanyId(), exportObj.getBranchId(), exportObj.getImporterId());
		            Row row = sheet.createRow(rowNum++);
		            row.createCell(0).setCellValue(rowNum - 1);
		            row.createCell(1).setCellValue(exportObj.getSirNo());
		            row.createCell(2).setCellValue(formattedDate);
		            row.createCell(3).setCellValue(party.getPartyName());
		            row.createCell(4).setCellValue(exportObj.getNop());
		            row.createCell(5).setCellValue(exportObj.getDescriptionOfGoods());
		            row.createCell(6).setCellValue(exportObj.getDgdcStatus());
		            row.createCell(7).setCellValue(exportObj.getBeNo());
		            if(formattedDate1.endsWith("1970")) {
		            	row.createCell(8).setCellValue("");
		            }
		            else {
		            	row.createCell(8).setCellValue(formattedDate1);
		            }
		            row.createCell(9).setCellValue(convertedValue);
		            row.createCell(10).setCellValue(exportObj.getAssessableValue().doubleValue());
		            row.createCell(11).setCellValue(exportObj.getIgmNo());
		            row.createCell(12).setCellValue(exportObj.getMawb());
		            row.createCell(13).setCellValue(exportObj.getHawb());
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
		        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		        headers.setContentDispositionFormData("attachment", "export_data.xlsx");

		        // Return the Excel file as a byte array in the response body
		        return ResponseEntity.ok()
		                .headers(headers)
		                .body(outputStream.toByteArray());

		    } catch (IOException e) {
		        e.printStackTrace();
		        // Handle the exception appropriately (e.g., log it and return an error response)
		        return ResponseEntity.status(500).build();
		    }
		}
	
		
		
		
		
		@PostMapping("/transactionexcel")
		public ResponseEntity<byte[]> generateTransactionEXCEL(@RequestBody List<Import> import1) {
			System.out.println("Hiii "+import1);
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
		        headerRow.createCell(1).setCellValue("SIR No");
		        headerRow.createCell(2).setCellValue("SIR Date");
		        headerRow.createCell(3).setCellValue("Parcel Type");
		        headerRow.createCell(4).setCellValue("Flight No");
		        headerRow.createCell(5).setCellValue("Importer Name");
		        headerRow.createCell(6).setCellValue("Packages");
		        headerRow.createCell(7).setCellValue("MAWB No");
		        headerRow.createCell(8).setCellValue("HAWB No");
		        headerRow.createCell(9).setCellValue("BE Request ID");
		        headerRow.createCell(10).setCellValue("DGDC Status");
		       

		        // Apply style to header cells
		        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
		            headerRow.getCell(i).setCellStyle(headerStyle);
		        }

		        // Create data rows
		        int rowNum = 1;

		        for (Import importObj : import1) {
		            BigDecimal gross = importObj.getGrossWeight();
		            // Replace with your actual BigDecimal value
		            double convertedValue = gross.doubleValue();
		            
		        	 // Your input Date object
		            Date inputDate = importObj.getSirDate();

		            // Create a SimpleDateFormat object with the desired format
		            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		            // Format the Date object
		            String formattedDate = sdf.format(inputDate);
		            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(importObj.getCompanyId(), importObj.getBranchId(), importObj.getImporterId());
		            Row row = sheet.createRow(rowNum++);
		            row.createCell(0).setCellValue(rowNum - 1);
		            row.createCell(1).setCellValue(importObj.getSirNo());
		            row.createCell(2).setCellValue(formattedDate);
		            row.createCell(3).setCellValue(importObj.getParcelType());
		            row.createCell(4).setCellValue(importObj.getFlightNo());
		            row.createCell(5).setCellValue(party.getPartyName());
		            row.createCell(6).setCellValue(importObj.getNop());
		            row.createCell(7).setCellValue(importObj.getMawb());
		            if(importObj.getHawb().startsWith("000")) {
		            	row.createCell(8).setCellValue("");
		            }
		            else {
		            	row.createCell(8).setCellValue(importObj.getHawb());
		            }
		            row.createCell(9).setCellValue(importObj.getBeRequestId());
		            row.createCell(10).setCellValue(importObj.getDGDC_Status());
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
		        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		        headers.setContentDispositionFormData("attachment", "import_data.xlsx");

		        // Return the Excel file as a byte array in the response body
		        return ResponseEntity.ok()
		                .headers(headers)
		                .body(outputStream.toByteArray());

		    } catch (IOException e) {
		        e.printStackTrace();
		        // Handle the exception appropriately (e.g., log it and return an error response)
		        return ResponseEntity.status(500).build();
		    }
		}
		
		@PostMapping("/doexcel")
		public ResponseEntity<byte[]> generateDOEXCEL(@RequestBody List<Import> import1) {
			System.out.println("Hiii "+import1);
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
		        headerRow.createCell(1).setCellValue("DO Number");
		        headerRow.createCell(2).setCellValue("DO Date");
		        headerRow.createCell(3).setCellValue("MAWB");
		        headerRow.createCell(4).setCellValue("HAWB");
		        headerRow.createCell(5).setCellValue("IGM No");
		        headerRow.createCell(6).setCellValue("SIR No");
		        headerRow.createCell(7).setCellValue("SIR Date");
		        headerRow.createCell(8).setCellValue("Parcel Type");
		        headerRow.createCell(9).setCellValue("Importer Name");
		        headerRow.createCell(10).setCellValue("No Of Packages");
		        headerRow.createCell(11).setCellValue("Contents");
		       

		        // Apply style to header cells
		        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
		            headerRow.getCell(i).setCellStyle(headerStyle);
		        }

		        // Create data rows
		        int rowNum = 1;

		        for (Import importObj : import1) {
		            BigDecimal gross = importObj.getGrossWeight();
		            // Replace with your actual BigDecimal value
		            double convertedValue = gross.doubleValue();
		            
		        	 // Your input Date object
		            Date inputDate = importObj.getSirDate();

		            // Create a SimpleDateFormat object with the desired format
		            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		            // Format the Date object
		            String formattedDate = sdf.format(inputDate);
		            String formattedDate1 = "";
		            if(importObj.getDoDate() != null) {
		            	   Date inputDate1 = importObj.getDoDate();

		   	            // Create a SimpleDateFormat object with the desired format
		   	            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

		   	            // Format the Date object
		   	            formattedDate1 = sdf.format(inputDate);
		            }
		            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(importObj.getCompanyId(), importObj.getBranchId(), importObj.getImporterId());
		            Row row = sheet.createRow(rowNum++);
		            row.createCell(0).setCellValue(rowNum - 1);
		            row.createCell(1).setCellValue(importObj.getDoNumber());
		            row.createCell(2).setCellValue(formattedDate1);
		            row.createCell(3).setCellValue(importObj.getMawb());
		            if(importObj.getHawb().startsWith("000")) {
		            	row.createCell(4).setCellValue("");
		            }
		            else {
		            	row.createCell(4).setCellValue(importObj.getHawb());
		            }
		            row.createCell(5).setCellValue(importObj.getIgmNo());
		            row.createCell(6).setCellValue(importObj.getSirNo());
		            row.createCell(7).setCellValue(importObj.getSirDate());
		            row.createCell(8).setCellValue(importObj.getParcelType());
		            row.createCell(9).setCellValue(party.getPartyName());
		            row.createCell(10).setCellValue(importObj.getNop());
		            row.createCell(11).setCellValue(importObj.getPackageContentType());
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
		        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		        headers.setContentDispositionFormData("attachment", "import_data.xlsx");

		        // Return the Excel file as a byte array in the response body
		        return ResponseEntity.ok()
		                .headers(headers)
		                .body(outputStream.toByteArray());

		    } catch (IOException e) {
		        e.printStackTrace();
		        // Handle the exception appropriately (e.g., log it and return an error response)
		        return ResponseEntity.status(500).build();
		    }
		}
		
		
		@PostMapping("/register1excel")
		public ResponseEntity<byte[]> generateREGISTER1EXCEL(@RequestBody List<Import> import1) {
			System.out.println("Hiii "+import1);
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
		        headerRow.createCell(1).setCellValue("SIR No & DO No");
		        headerRow.createCell(2).setCellValue("CONSOLE");
		        headerRow.createCell(3).setCellValue("Importer Name");
		        headerRow.createCell(4).setCellValue("TP No");
		        headerRow.createCell(5).setCellValue("PCTM No");
		        headerRow.createCell(6).setCellValue("IGM No");
		        headerRow.createCell(7).setCellValue("MAWB No");
		        headerRow.createCell(8).setCellValue("HAWB No");
		        headerRow.createCell(9).setCellValue("No Of Packages");
		        headerRow.createCell(10).setCellValue("DESC");
		    
		       

		        // Apply style to header cells
		        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
		            headerRow.getCell(i).setCellStyle(headerStyle);
		        }

		        // Create data rows
		        int rowNum = 1;

		        for (Import importObj : import1) {
		        	 ExternalParty externalparty = externalPartyRepository.getalldatabyid(importObj.getCompanyId(), importObj.getBranchId(), importObj.getConsoleName());
		            BigDecimal gross = importObj.getGrossWeight();
		            // Replace with your actual BigDecimal value
		            double convertedValue = gross.doubleValue();
		            
		        	 // Your input Date object
		            Date inputDate = importObj.getSirDate();

		            // Create a SimpleDateFormat object with the desired format
		            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		            // Format the Date object
		            String formattedDate = sdf.format(inputDate);
		            String formattedDate1 = "";
		            if(importObj.getDoDate() != null) {
		            	   Date inputDate1 = importObj.getDoDate();

		   	            // Create a SimpleDateFormat object with the desired format
		   	            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

		   	            // Format the Date object
		   	            formattedDate1 = sdf.format(inputDate);
		            }
		            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(importObj.getCompanyId(), importObj.getBranchId(), importObj.getImporterId());
		            Row row = sheet.createRow(rowNum++);
		            row.createCell(0).setCellValue(rowNum - 1);
		            row.createCell(1).setCellValue(importObj.getSirNo()+" | "+importObj.getDoNumber());
		            row.createCell(2).setCellValue(externalparty.getUserName());
		            row.createCell(3).setCellValue(party.getPartyName());
		            row.createCell(4).setCellValue(importObj.getTpNo());
		            row.createCell(5).setCellValue(importObj.getPctmNo());
		            row.createCell(6).setCellValue(importObj.getIgmNo());
		            row.createCell(7).setCellValue(importObj.getMawb());
		            if(importObj.getHawb().startsWith("000")) {
		            	row.createCell(8).setCellValue("");
		            }
		            else {
		            	row.createCell(8).setCellValue(importObj.getHawb());
		            }
		            	
		            row.createCell(9).setCellValue(importObj.getNop());
		            row.createCell(10).setCellValue(importObj.getDescriptionOfGoods());
		            
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
		        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		        headers.setContentDispositionFormData("attachment", "import_data.xlsx");

		        // Return the Excel file as a byte array in the response body
		        return ResponseEntity.ok()
		                .headers(headers)
		                .body(outputStream.toByteArray());

		    } catch (IOException e) {
		        e.printStackTrace();
		        // Handle the exception appropriately (e.g., log it and return an error response)
		        return ResponseEntity.status(500).build();
		    }
		}
		
//		Import Register
		
		@GetMapping("/importRegiter")
		public ResponseEntity<?> importRegister(
				@RequestParam(name = "companyid", required = false) String companyid,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "transhipmentPermitNo", required = false) String tpNo,
				@RequestParam(name = "selectedDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date tpDate
				) throws DocumentException 
		{			
			
			
//			System.out.println(" Tp no : " +tpNo   + "Tpdate : "+tpDate + " Comapny : "+companyid + "Branch Id : "+branchId);
			
			List<Import> importData = imprepo.findByTpdateTpno(companyid, branchId, tpDate,tpNo);
			
			List<Party> partyData = partyRepository.getalldata(companyid, branchId);

//			Set<Party> filterParty = FilterParty(pctmAll, companyId, branchId);
			List<ExternalParty> extparty = externalPartyRepository.getAllExternalParties(companyid, branchId);
			
			int totalNoOfPackages = 0;
			for (Import im : importData) 
			{				
				im.setImporterId(getPartyNameFromImporterId(partyData, im.getImporterId()));
				im.setConsoleName(getConsoleNameFromExternal(extparty, im.getConsoleName()));
				im.setMawb(formatMAWBNumber(im.getMawb()));
				im.setTpNo(im.getTpNo().replaceFirst("^0+", ""));	
				im.setPctmNo(im.getPctmNo().replaceFirst("^0+", ""));	
				totalNoOfPackages=totalNoOfPackages+im.getNop();
			}


			tpNo = tpNo.replaceFirst("^0+", "");

			SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
		
			Context context = new Context();
			context.setVariable("importData", importData);
			context.setVariable("tpNo", tpNo);
			context.setVariable("tpDate",  dateFormat2.format(tpDate));
			context.setVariable("totalNoOfPackages", totalNoOfPackages);
			context.setVariable("totalNoOfPackagesWrods", convertToWords(totalNoOfPackages));
			context.setVariable("tpNo", tpNo);
		
			String htmlContent = templateEngine.process("ImportRegister", context);
//						if ("PDF".equals(type)) {
		
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
			context.clearVariables();			
			

			return ResponseEntity.ok(base64Pdf);

		}
		
		
		
		
		
		
		@GetMapping("/findImportData")
		public List<Object[]> findImportRegister(@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
				@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
				@RequestParam(name = "console", required = false) String console) {

			List<Object[]> combinedList = new ArrayList<>();

			if (console == null || console.isEmpty()) {
				List<Object[]> importSubData = imprepo.findImportSubData(companyId, branchId, startDate, endDate);
				combinedList.addAll(importSubData);
			}

			List<Object[]> importData = imprepo.findImportDataRegister(companyId, branchId, startDate, endDate, console);
			combinedList.addAll(importData);
			return combinedList;
		}
		
		
		

		@GetMapping("/importRegisterXLSDownload")
		public ResponseEntity<?> importRegisterXLSDownload(@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "totalNoOfPackages", required = false) String totalNoOfPackages,
				@RequestParam(name = "totalNoSIR", required = false) String totalNoSIR,
				@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
				@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
				@RequestParam(name = "console", required = false) String console)
		{
			try {
				
				List<Object[]> combinedList = new ArrayList<>();

				if (console == null || console.isEmpty()) {
					List<Object[]> importSubData = imprepo.findImportSubData(companyId, branchId, startDate, endDate);
					combinedList.addAll(importSubData);
				}

				List<Object[]> importData = imprepo.findImportDataRegister(companyId, branchId, startDate, endDate, console);
				combinedList.addAll(importData);
				
				
				
				
				// Create a new workbook
							Workbook workbook = new XSSFWorkbook();
							// Create a sheet
							Sheet sheet = workbook.createSheet("Import Register");

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
							headerRow.createCell(2).setCellValue("SER DATE");
							headerRow.createCell(3).setCellValue("IMPORTER");
							headerRow.createCell(4).setCellValue("NO OF PACKAGES");
							headerRow.createCell(5).setCellValue("DESCRIPTION");
							headerRow.createCell(6).setCellValue("DGDC STATUS");
							headerRow.createCell(7).setCellValue("CONSOLE");

							
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
							  
							    row.createCell(2).setCellValue(dateFormat.format((Date) exportObj[1]));
							    row.createCell(3).setCellValue(String.valueOf(exportObj[2]));
							    row.createCell(4).setCellValue(String.valueOf(exportObj[3]));
							    row.createCell(5).setCellValue(String.valueOf(exportObj[4]));
							    row.createCell(6).setCellValue(String.valueOf(exportObj[5]));
							    row.createCell(7).setCellValue(String.valueOf(exportObj[13]));
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
			
			

		
		
		@GetMapping("/importRegisterPrint")
		public ResponseEntity<?> importRegisterPrint(@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "totalNoOfPackages", required = false) String totalNoOfPackages,
				@RequestParam(name = "totalNoSIR", required = false) String totalNoSIR,
				@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
				@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
				@RequestParam(name = "console", required = false) String console) throws Exception {
			try {

				Context context = new Context();
				List<Object[]> combinedList = new ArrayList<>();

				if (console == null || console.isEmpty()) {
					List<Object[]> importSubData = imprepo.findImportSubData(companyId, branchId, startDate, endDate);
					combinedList.addAll(importSubData);
				}

				List<Object[]> importData = imprepo.findImportDataRegister(companyId, branchId, startDate, endDate, console);
				combinedList.addAll(importData);
				
				
							
				
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
				
				
				
				
				
				
				String htmlContent = templateEngine.process("ImportRegisterConsole", context);

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
		
		
		
		
//		Import Transaction

		@GetMapping("/findimportTransactionData")
		public ResponseEntity<?> findimportTransaction(@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "sirDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate,
				@RequestParam(name = "status", required = false) String status) {
			
			List<Object[]> importSubData = null;
			
			String columnName;

			switch (status) {
	        case "Handed over to DGDC Cargo":
	            columnName = "sir_Date";
	            importSubData = imprepo.findImportTransactionData(companyId, branchId, sirDate, columnName);
	            break;
	        case "Handed over to Carting Agent":
	        case "Exit from DGDC Cargo Gate":
	        case "Entry at DGDC SEEPZ Gate":
	            columnName = "tp_Date";
	            importSubData = imprepo.findImportTransactionData(companyId, branchId, sirDate, columnName);
	            break;
	        case "Handed over to Party/CHA":
	        case "Exit from DGDC SEEPZ Gate":
	            columnName = "out_Date";
	            importSubData = imprepo.findImportTransactionData(companyId, branchId, sirDate, columnName);
	            break;
	        case "Handed over to DGDC SEEPZ":
	            importSubData = imprepo.findImportTransactionDataSeepz(companyId, branchId, sirDate);
	            break;
	        default:
	            // Handle unrecognized status
	           
	            break;
	    }
			return  ResponseEntity.ok(importSubData) ;
		}

		@GetMapping("/importTransactionXLSDownload")
		public ResponseEntity<?> importTransactionXLSDownload(
				@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "totalNoOfPackages", required = false) String totalNoOfPackages,
				@RequestParam(name = "totalNoSIR", required = false) String totalNoSIR,
				@RequestParam(name = "sirDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate,
				@RequestParam(name = "status", required = false) String status) throws Exception {
			try {

						
				
				List<Object[]> importSubData = null;
				
				String columnName;

				switch (status) {
		        case "Handed over to DGDC Cargo":
		            columnName = "sir_Date";
		            importSubData = imprepo.findImportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        case "Handed over to Carting Agent":
		        case "Exit from DGDC Cargo Gate":
		        case "Entry at DGDC SEEPZ Gate":
		            columnName = "tp_Date";
		            importSubData = imprepo.findImportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        case "Handed over to Party/CHA":
		        case "Exit from DGDC SEEPZ Gate":
		            columnName = "out_Date";
		            importSubData = imprepo.findImportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        case "Handed over to DGDC SEEPZ":
		            importSubData = imprepo.findImportTransactionDataSeepz(companyId, branchId, sirDate);
		            break;
		        default:
		            // Handle unrecognized status
		           
		            break;
		    }
				
				
				// Create a new workbook
				Workbook workbook = new XSSFWorkbook();
				// Create a sheet
				Sheet sheet = workbook.createSheet("Import Register");

				// Create header row and set font style
				Row headerRow = sheet.createRow(0);
				CellStyle headerStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				font.setBold(true);
				font.setFontHeightInPoints((short) 12);
				headerStyle.setFont(font);
				headerRow.createCell(0).setCellValue("SR NO");
				headerRow.createCell(1).setCellValue("SIR DATE");
				headerRow.createCell(2).setCellValue("SIR NO");
				headerRow.createCell(3).setCellValue("Parcel Type");
				headerRow.createCell(4).setCellValue("Flight No");			
				headerRow.createCell(5).setCellValue("IMPORTER");
				headerRow.createCell(6).setCellValue("NO OF PACKAGES");
				headerRow.createCell(7).setCellValue("MAWB No.");
				headerRow.createCell(8).setCellValue("HAWB No");
				headerRow.createCell(9).setCellValue("BE REQUEST ID");
				headerRow.createCell(10).setCellValue("Current Status");

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

				for (Object[] exportObj : importSubData) {
					Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(rowNum - 1);
					row.createCell(1).setCellValue(dateFormat.format((Date) exportObj[0]));
					row.createCell(2).setCellValue(String.valueOf(exportObj[1]));				
					row.createCell(3).setCellValue(String.valueOf(exportObj[2]));
					row.createCell(4).setCellValue(String.valueOf(exportObj[3]));
					row.createCell(5).setCellValue(String.valueOf(exportObj[4]));
					row.createCell(6).setCellValue(String.valueOf(exportObj[5]));
					row.createCell(7).setCellValue(String.valueOf(exportObj[6]));
					row.createCell(8).setCellValue(String.valueOf(exportObj[7]));
					row.createCell(9).setCellValue(String.valueOf(exportObj[8]));
					row.createCell(10).setCellValue(String.valueOf(exportObj[9]));
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
		
		


		@GetMapping("/importTransactionPrint")
		public ResponseEntity<?> importTransactionPrint(
				@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "totalNoOfPackages", required = false) String totalNoOfPackages,
				@RequestParam(name = "totalNoSIR", required = false) String totalNoSIR,
				@RequestParam(name = "sirDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate,
				@RequestParam(name = "status", required = false) String status) throws Exception {
			try {

				Context context = new Context();	
				
				
				List<Object[]> importSubData = null;
				
				String columnName;

				switch (status) {
		        case "Handed over to DGDC Cargo":
		            columnName = "sir_Date";
		            importSubData = imprepo.findImportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        case "Handed over to Carting Agent":
		        case "Exit from DGDC Cargo Gate":
		        case "Entry at DGDC SEEPZ Gate":
		            columnName = "tp_Date";
		            importSubData = imprepo.findImportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        case "Handed over to Party/CHA":
		        case "Exit from DGDC SEEPZ Gate":
		            columnName = "out_Date";
		            importSubData = imprepo.findImportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        case "Handed over to DGDC SEEPZ":
		            importSubData = imprepo.findImportTransactionDataSeepz(companyId, branchId, sirDate);
		            break;
		        default:
		            // Handle unrecognized status
		           
		            break;
		    }		
				

			
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
				context.setVariable("status", status);
				context.setVariable("startDate", dateFormat2.format(sirDate));
				
				context.setVariable("importData", importSubData);

				context.setVariable("totalNoSIR", totalNoSIR);
				context.setVariable("totalNoOfPackages", totalNoOfPackages);

				String htmlContent = templateEngine.process("ImportTransaction", context);

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

		
		
//		Export Transaction
		
		
		@GetMapping("/findexportTransactionData")
		public ResponseEntity<?> findexportTransaction(@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "sirDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate,
				@RequestParam(name = "status", required = false) String status) {
			
			List<Object[]> importSubData = null;
			
			String columnName;

			switch (status) {
	        case "Entry at DGDC SEEPZ Gate":
	            columnName = "created_Date";
	            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
	            break;
	        case "Handed over to Carting Agent":
	        case "Handed over to DGDC Cargo":
	        case "Exit from DGDC SEEPZ Gate":
	        case "Entry at DGDC Cargo GATE":
	            columnName = "out_Date";
	            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
	            break;
	        case "Handed Over to Airline":
	            columnName = "airline_Date";
	            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
	            break;
	            
	        case "Handed over to DGDC SEEPZ":
	            columnName = "ser_Date";
	            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
	            break;
	        default:
	            // Handle unrecognized status
	           
	            break;
	    }
			return  ResponseEntity.ok(importSubData) ;
		}

		@GetMapping("/exportTransactionXLSDownload")
		public ResponseEntity<?> exportTransactionXLSDownload(
				@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "totalNoOfPackages", required = false) String totalNoOfPackages,
				@RequestParam(name = "totalNoSIR", required = false) String totalNoSIR,
				@RequestParam(name = "sirDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate,
				@RequestParam(name = "status", required = false) String status) throws Exception {
			try {

						
				
				List<Object[]> importSubData = null;
				
				String columnName;

				switch (status) {
		        case "Entry at DGDC SEEPZ Gate":
		            columnName = "created_Date";
		            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        case "Handed over to Carting Agent":
		        case "Handed over to DGDC Cargo":
		        case "Exit from DGDC SEEPZ Gate":
		        case "Entry at DGDC Cargo GATE":
		            columnName = "out_Date";
		            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        case "Handed Over to Airline":
		            columnName = "airline_Date";
		            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		            
		        case "Handed over to DGDC SEEPZ":
		            columnName = "ser_Date";
		            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        default:
		            // Handle unrecognized status
		           
		            break;
		    }
				
				// Create a new workbook
				Workbook workbook = new XSSFWorkbook();
				// Create a sheet
				Sheet sheet = workbook.createSheet("Import Register");

				// Create header row and set font style
				Row headerRow = sheet.createRow(0);
				CellStyle headerStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				font.setBold(true);
				font.setFontHeightInPoints((short) 12);
				headerStyle.setFont(font);
				headerRow.createCell(0).setCellValue("SR NO");
				headerRow.createCell(1).setCellValue("Request Id");
				headerRow.createCell(2).setCellValue("SB No");
				headerRow.createCell(3).setCellValue("SB Date");
				headerRow.createCell(4).setCellValue("SER No");			
				headerRow.createCell(5).setCellValue("SER Date");
				headerRow.createCell(6).setCellValue("Parcel Type");
				headerRow.createCell(7).setCellValue("Exporter Name");
				headerRow.createCell(8).setCellValue("Package Count");
				headerRow.createCell(9).setCellValue("Gross Weight");
				headerRow.createCell(10).setCellValue("Current Status");

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

				for (Object[] exportObj : importSubData) {
					Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(rowNum - 1);
					row.createCell(1).setCellValue(String.valueOf(exportObj[0]));				
					row.createCell(2).setCellValue(String.valueOf(exportObj[1]));
					row.createCell(3).setCellValue(dateFormat.format((Date) exportObj[2]));

					row.createCell(4).setCellValue(String.valueOf(exportObj[3]));
					row.createCell(5).setCellValue(dateFormat.format((Date) exportObj[4]));

					row.createCell(6).setCellValue(String.valueOf(exportObj[5]));
					row.createCell(7).setCellValue(String.valueOf(exportObj[6]));
					row.createCell(8).setCellValue(String.valueOf(exportObj[7]));
					row.createCell(9).setCellValue(String.valueOf(exportObj[8]) + " "+ String.valueOf(exportObj[9]));
					row.createCell(10).setCellValue(String.valueOf(exportObj[10]));
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
		
		


		@GetMapping("/exportTransactionPrint")
		public ResponseEntity<?> exportTransactionPrint(
				@RequestParam(name = "companyId", required = false) String companyId,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "totalNoOfPackages", required = false) String totalNoOfPackages,
				@RequestParam(name = "totalNoSIR", required = false) String totalNoSIR,
				@RequestParam(name = "sirDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate,
				@RequestParam(name = "status", required = false) String status) throws Exception {
			try {

				Context context = new Context();	
				
				
				List<Object[]> importSubData = null;
				
				String columnName;

				switch (status) {
		        case "Entry at DGDC SEEPZ Gate":
		            columnName = "created_Date";
		            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        case "Handed over to Carting Agent":
		        case "Handed over to DGDC Cargo":
		        case "Exit from DGDC SEEPZ Gate":
		        case "Entry at DGDC Cargo GATE":
		            columnName = "out_Date";
		            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        case "Handed Over to Airline":
		            columnName = "airline_Date";
		            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		            
		        case "Handed over to DGDC SEEPZ":
		            columnName = "ser_Date";
		            importSubData = imprepo.findExportTransactionData(companyId, branchId, sirDate, columnName);
		            break;
		        default:
		            // Handle unrecognized status
		           
		            break;
		    }

			
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
				context.setVariable("status", status);
				context.setVariable("startDate", dateFormat2.format(sirDate));
				
				context.setVariable("importData", importSubData);

				context.setVariable("totalNoSIR", totalNoSIR);
				context.setVariable("totalNoOfPackages", totalNoOfPackages);

				String htmlContent = templateEngine.process("ExportTransaction", context);

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
		
	
}
