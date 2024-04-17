package com.cwms.controller;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.jdbc.core.JdbcTemplate;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.cwms.entities.CommonGatePass;
import com.cwms.entities.DefaultPartyDetails;
import com.cwms.entities.Export;
import com.cwms.entities.ExportSub;
import com.cwms.entities.ExternalParty;
import com.cwms.entities.Gate_In_Out;
import com.cwms.entities.Import;
import com.cwms.entities.ImportHeavyPackage;
import com.cwms.entities.ImportPC;
import com.cwms.entities.ImportSub;
import com.cwms.entities.Import_History;
import com.cwms.entities.MOPpass;
import com.cwms.entities.Party;
import com.cwms.entities.PredictableInvoiceTaxDetails;
import com.cwms.entities.RepresentParty;
import com.cwms.helper.FileUploadProperties;
import com.cwms.helper.ImageHelper;
import com.cwms.invoice.ServiceIdMappingRepositary;
import com.cwms.repository.CommonGatePassRepo;
import com.cwms.repository.DefaultParyDetailsRepository;
import com.cwms.repository.ExportHeavyPackageRepo;
import com.cwms.repository.ExportRepository;
import com.cwms.repository.ExportSubRepository;
import com.cwms.repository.Gate_In_out_Repo;
import com.cwms.repository.ImportHeavyPackageRepo;
import com.cwms.repository.ImportPCRepositary;
import com.cwms.repository.ImportRepo;
import com.cwms.repository.ImportRepository;
import com.cwms.repository.ImportSubRepository;
import com.cwms.repository.MopPassRepo;
import com.cwms.repository.PartyRepository;
import com.cwms.repository.PredictableInvoiceTaxDetailsRepo;
import com.cwms.repository.ProcessNextIdRepository;
import com.cwms.repository.RepresentPartyRepository;
import com.cwms.service.CFSService;
import com.cwms.service.CFSTariffRangeService;
import com.cwms.service.ExportService;
import com.cwms.service.ExternalParty_Service;
import com.cwms.service.ImportHeavyService;
import com.cwms.service.ImportPCService;
import com.cwms.service.ImportService;
import com.cwms.service.Import_HistoryService;
import com.cwms.service.PartyService;
import com.cwms.service.ProcessNextIdService;
import com.cwms.service.RepsentativeService;
import com.cwms.service.cfsTarrifServiceService;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.lowagie.text.DocumentException;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/importmain")
@CrossOrigin("*")

public class ImportMainController {
	
	@Autowired
	public ImageHelper ImageHelper;
	
	@Autowired
	 private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ImportPCRepositary ImportPCRepositary;
	
	@Autowired
	public ImportHeavyPackageRepo ImportHeavyPackageRepo;
	

	@Autowired
	private ProcessNextIdRepository processNextIdRepository;
	
	@Autowired
	private PredictableInvoiceTaxDetailsRepo PredictableInvoiceTaxDetailsRepo;
	
	
	@Autowired
	private ImportRepo ImportRepo;
	
	@Autowired
	public PartyRepository partyRepository; 
	
	@Autowired
	private RepresentPartyRepository representRepo;
	
	@Autowired
	private PartyRepository partyrepo;

	
	@Autowired
	private ImportRepository importRepository;
	
	@Autowired
	private MopPassRepo mopgatepassrepo;



	@Autowired
	private CommonGatePassRepo commongatepassrepo;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	public DefaultParyDetailsRepository DefaultParyDetailsRepository;

	@Autowired
	private Gate_In_out_Repo gateinoutrepo;

	@Autowired
	public ProcessNextIdService proccessNextIdService;

	@Autowired
	private Import_HistoryService historyService;
	@Autowired
	public FileUploadProperties FileUploadProperties;

	@Autowired
	private RepsentativeService RepsentativeService;

	@Autowired
	private ExternalParty_Service ExternalParty_Service;

	@Autowired
	private PartyService PartyService;

	@Autowired
	public ImportService importService;

	@Autowired
	public ImportPCService ImportPCService;

	@Autowired
	public ImportRepo importRepo;

	@Autowired
	public CFSService CFSService;

	@Autowired
	public CFSTariffRangeService CFSTariffRangeService;


	@Autowired
	private ServiceIdMappingRepositary ServiceIdMappingRepositary;

	@Autowired
	public cfsTarrifServiceService cfsTarrifServiceService;

	@Autowired
	public ExportService ExportService;

	@Autowired
	public ImportHeavyService ImportHeavyService;

	@Autowired
	public ExportHeavyPackageRepo ExportHeavyPackageRepo;

	@Autowired
	private ExportRepository exportrepo;

	@Autowired
	public ImportSubRepository impsubRepo;

	@Autowired
	public ExportSubRepository expsubRepo;
	
	
	
	
	@PutMapping("/updateCustomPctmNumber")
	public ResponseEntity<?> updateCustomPctmNumber(@RequestParam("companyId") String compid,@RequestParam("branchId") String branchId, @RequestBody List<Import> imports,@RequestParam("user") String user,@RequestParam("console") String console,
			@RequestParam(name = "customDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date customDate,@RequestParam("customNumber") String customNumber,@RequestParam("country")String country) 
	{
				
		for(Import import3: imports )
		{
	      importRepository.updateCustomPCTMNumber(compid, branchId,import3.getSirNo(), import3.getMawb(), import3.getHawb(),customNumber,customDate ,user,console,country);
		}
		
		return ResponseEntity.ok("Imports Updated Successfully");	
	
	}
	
	
	
//	Custom TP Update imports
	@GetMapping("/getDistinctPorts")
	public ResponseEntity<?> getDistinctPorts(@RequestParam("companyId")String companyId,@RequestParam("branchId")String branchId,@RequestParam("consoleName")String consoleName)
	{		
		List<Object[]> imports = importRepo.getDistinctContries(companyId, branchId, consoleName);
		return ResponseEntity.ok(imports);
	}
	
//	Custom TP Update imports
	@GetMapping("/getImportsforCustomPctmUpdate")
	public ResponseEntity<?> getImportsforCustomTpUpdate(@RequestParam("companyId")String companyId,@RequestParam("branchId")String branchId,@RequestParam("consoleName")String consoleName,@RequestParam("country")String country)
	{		
		List<Import> imports = importRepo.findImportsForCustomPCTMUpdate(companyId, branchId, consoleName,country);
		return ResponseEntity.ok(imports);
	}
	
	
	
//	Custom TP Update imports
	@GetMapping("/getImportsforCustomTpUpdate")
	public ResponseEntity<?> getImportsforCustomTpUpdate(@RequestParam("companyId")String companyId,@RequestParam("branchId")String branchId,@RequestParam("consoleName")String consoleName)
	{		
		List<Import> imports = importRepo.findImportsForCustomTPUpdate(companyId, branchId, consoleName);
		return ResponseEntity.ok(imports);
	}
	
	
	@PutMapping("/updateCustomTpNumber")
	public ResponseEntity<?> updateImportCustomTPNumber(@RequestParam("companyId") String compid,@RequestParam("branchId") String branchId, @RequestBody List<Import> imports,@RequestParam("user") String user,@RequestParam("console") String console,
			@RequestParam(name = "customDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date customDate,@RequestParam("customNumber") String customNumber) 
	{
				
		for(Import import3: imports )
		{
	      importRepository.updateCustomTPNumber(compid, branchId,import3.getSirNo(), import3.getMawb(), import3.getHawb(),customNumber,customDate ,user,console);
		}
		
		return ResponseEntity.ok("Imports Updated Successfully");	
	
	}

	
	
	
	
	
	
//	Single Party / CHA Updated 

	@PutMapping("/PartyOrCHAupdateSingle")
	public ResponseEntity<?> updateImportPartOrChaSingle(@RequestParam("companyId") String compid,
			@RequestParam("branchId") String branchId, @RequestBody Import import3, @RequestParam("user") String user,
			@RequestParam("otp") String OTP, @RequestParam("cartingAgent") String cartingAgent,
			@RequestParam("reprentativeId") String ReprentativeId) {

		try {
			
			RepresentParty Representative = RepsentativeService.findByRepresentativeId(compid, branchId, cartingAgent,ReprentativeId);
			// Check if the OTP matches
			if (Representative.getOtp().equals(OTP)) {
				// Loop through importList and update each import
            	int updateHandOverToParty = importRepository.updateHandOverToParty(compid, branchId,import3.getSirNo(), import3.getMawb(), import3.getHawb(), "Handed Over to CHA",user,cartingAgent , ReprentativeId);

            	if(updateHandOverToParty == 1) {            		
				Import_History history = new Import_History();
				history.setCompanyId(compid);
				history.setBranchId(branchId);
				history.setSirNo(import3.getSirNo());
				history.setMawb(import3.getMawb());
				history.setHawb(import3.getHawb());
				history.setTransport_Date(new Date());
				history.setOldStatus("Handed over to DGDC SHB");
				history.setNewStatus("Handed Over to CHA");
				history.setUpdatedBy(user);
				history.setHandOverParty(cartingAgent);
				history.setHandOverRepresentative(ReprentativeId);
				historyService.addHistory(history);
            	}	
            	Representative.setOtp("");
            	RepsentativeService.addrepresentative(Representative);
            	return ResponseEntity.ok(import3);
			} else {
				// Return an unauthorized response if the OTP doesn't match
				return null;
			}
		} catch (Exception e) {
			// Handle any unexpected errors here without showing specific error messages.
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
		}
	}
	
	
	
	
	
	@PutMapping("/SingleCartingAgent")
	public Import updateSingleImportCartingAgent(@RequestParam("companyId") String compid,
			@RequestParam("branchId") String branchId, @RequestBody Import existingImport,
			@RequestParam("user") String user, @RequestParam("otp") String OTP, @RequestParam("cartingAgent") String cartingAgent,
			@RequestParam("reprentativeId") String ReprentativeId, @RequestParam("tpNo") String tpdata) {
		RepresentParty Representative = RepsentativeService.findByRepresentativeId(compid, branchId, cartingAgent,ReprentativeId);		
	
		String tpNo = null;

		if ("N".equals(tpdata)) {
			tpNo = proccessNextIdService.generateAndIncrementTPumber();
		} else {
			tpNo = tpdata;
		}

		if (Representative.getOtp().equals(OTP)) {

			int updateCartingAgent = importRepository.updateCartingAgent(compid, branchId, existingImport.getSirNo(), existingImport.getMawb(), existingImport.getHawb(), "Handed over to Console",user,cartingAgent , ReprentativeId, tpNo);

			if(updateCartingAgent == 1) {
			Import_History history = new Import_History();
			history.setCompanyId(compid);
			history.setBranchId(branchId);
			history.setSirNo(existingImport.getSirNo());
			history.setMawb(existingImport.getMawb());
			history.setHawb(existingImport.getHawb());
			history.setTransport_Date(new Date());
			history.setOldStatus("Handed over to DGDC Cargo");
			history.setNewStatus("Handed over to Console");
			history.setUpdatedBy(user);			
			history.setHandOverParty(cartingAgent);
			history.setHandOverRepresentative(ReprentativeId);
			
			
			historyService.addHistory(history);
			
		}

			return existingImport;
		} else {
			return null;
		}
	}
	
	
	@PutMapping("/{compid}/{branchId}/{user}/modifyupdate")
	public ResponseEntity<?> updateImportByIMpTransId(@PathVariable("compid") String compid,
			@PathVariable("branchId") String branchId, @RequestBody Import import2, @PathVariable("user") String User) {
//		import2.setBranchId(branchId);

		Import existingImport = importService.findBytransIdAndSirNo(compid, branchId, import2.getImpTransId(),
				import2.getSirNo());

		
		
		String HawbNo =import2.getHawb();
		
		 
		if (HawbNo == null || HawbNo.isEmpty()) {
		    HawbNo = "0000";
		}
		
		List<Import> duplicates = importService.getByMAWBAndHawbForDuplication(compid,branchId,existingImport.getMawb(), HawbNo);
		duplicates.removeIf(record -> record.getSirNo().equals(import2.getSirNo())); // Remove the current record
		
		
		
		
		if (!duplicates.isEmpty()) {
	        // If duplicates are found, return a conflict status
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Duplicate Mawb and Hawb combination");
	    }
		
		
		
		
		
		List<Import_History> findbyHistorySIRNO = historyService.findbySIRNO(compid, branchId, existingImport.getMawb(),
				existingImport.getHawb(), import2.getSirNo());


		

		
		if (existingImport.getPcStatus().equals("Y")) 
		{
			System.out.println(" Pc Status = Y ");
			
			ImportPC importPC = ImportPCRepositary.findByCompanyIdAndBranchIdAndMawbAndHawbAndSirNo(compid, branchId, existingImport.getMawb(), existingImport.getHawb(), existingImport.getSirNo());
			System.out.println("importPC ");
			System.out.println(importPC);
			
			if(importPC != null)
			{
				
				
				String updateQuery = "UPDATE importpc SET mawb = ?, hawb = ? WHERE sir_No = ?";
				//
//							        // Execute the update query with the provided parameters
		    int rowsUpdated = jdbcTemplate.update(updateQuery, import2.getMawb(), HawbNo, import2.getSirNo());
//								
		    System.out.println("rowsUpdated "+rowsUpdated);
//				jdbcTemplate
				
			}	
		
		}
		
		
		if (existingImport.getHpStatus().equals("Y")) 
		{
			System.out.println(" HW Status = Y ");
			
			List<ImportHeavyPackage> importPC = ImportHeavyPackageRepo.findByCompanyIdAndBranchIdAndImpTransIdAndMawbAndHawbAndSirNo(compid, branchId, existingImport.getImpTransId(), existingImport.getMawb(), existingImport.getHawb(), existingImport.getSirNo());
			System.out.println("importPC ");
			System.out.println(importPC);
			
			if(importPC != null)
			{
				System.out.println("In Pc Update");
				
				String updateQuery = "UPDATE import_heavy_packages SET mawb = ?, hawb = ? WHERE sir_No = ? and imp_trans_id = ?";
				//
//							        // Execute the update query with the provided parameters
		    int rowsUpdated = jdbcTemplate.update(updateQuery, import2.getMawb(), HawbNo, import2.getSirNo(),import2.getImpTransId());
//								
		    System.out.println("rowsUpdated "+rowsUpdated);
//				jdbcTemplate
				
			}
			
			
		
		}
		
		
		
		
		

		for (Import_History history : findbyHistorySIRNO) {
			history.setMawb(import2.getMawb());
			history.setHawb(HawbNo);

		}

		historyService.SaveAllImportsHistory(findbyHistorySIRNO);

		if (existingImport != null) {

			importService.deleteImport(existingImport);
			Import newImport = new Import();

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
			newImport.setHawb(HawbNo);
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
			newImport.setCloseStatus(import2.getCloseStatus());
			newImport.setIec(import2.getIec());
			newImport.setBeNo(import2.getBeNo());
			newImport.setBeRequestId(import2.getBeRequestId());
			newImport.setIgmNo(import2.getIgmNo());
			newImport.setIgmDate(import2.getIgmDate());
			newImport.setPctmNo(import2.getPctmNo());
			newImport.setPackageContentType(import2.getPackageContentType());
			newImport.setUomPackages(import2.getUomPackages());
			newImport.setUomWeight(import2.getUomWeight());
			newImport.setTpNo(existingImport.getTpNo());
			newImport.setTpDate(existingImport.getTpDate());
			newImport.setPctmDate(existingImport.getPctmDate());
			newImport.setCustomPctmNo(import2.getCustomPctmNo());
			newImport.setCustomPctmDate(import2.getCustomPctmDate());
			newImport.setCustomTpDate(import2.getCustomTpDate());
			newImport.setCustomTpNo(import2.getCustomTpNo());	
			newImport.setKpcNo(import2.getKpcNo());			
			newImport.setSeepzInDate(existingImport.getSeepzInDate());
			newImport.setFlightNo(import2.getFlightNo());
			newImport.setFlightDate(import2.getFlightDate());
			newImport.setCountryOrigin(import2.getCountryOrigin());
			newImport.setPortOrigin(import2.getPortOrigin());
			newImport.setNSDL_Status(existingImport.getNSDL_Status());
			newImport.setDGDC_Status(existingImport.getDGDC_Status());
			newImport.setDescriptionOfGoods(import2.getDescriptionOfGoods());
			newImport.setImportAddress(import2.getImportAddress());
			newImport.setChaCde(import2.getChaCde());
			newImport.setAssessableValue(import2.getAssessableValue());
			newImport.setGrossWeight(import2.getGrossWeight());
			newImport.setStatus(import2.getStatus());
			newImport.setCartingAgent(existingImport.getCartingAgent());
			newImport.setPartyRepresentativeId(existingImport.getPartyRepresentativeId());
			newImport.setCancelStatus(import2.getCancelStatus());
			newImport.setCancelRemarks(import2.getCancelRemarks());
			newImport.setHoldBy(existingImport.getHoldBy());
			newImport.setHoldDate(existingImport.getHoldDate());
			newImport.setHoldStatus(import2.getHoldStatus());
			newImport.setReasonforOverride(import2.getReasonforOverride());
			newImport.setNsdlStatusDocs(import2.getNsdlStatusDocs());
			newImport.setHppackageno(import2.getHppackageno());
			newImport.setImposePenaltyAmount(import2.getImposePenaltyAmount());
			newImport.setImposePenaltyRemarks(import2.getImposePenaltyRemarks());
			newImport.setPcStatus(import2.getPcStatus());
			newImport.setScStatus(import2.getScStatus());
			newImport.setHpStatus(import2.getHpStatus());
			newImport.setHpWeight(import2.getHpWeight());
			newImport.setCancelRemarks(import2.getCancelRemarks());
			newImport.setHandedOverPartyId(existingImport.getHandedOverPartyId());
			newImport.setHandedOverRepresentativeId(existingImport.getHandedOverRepresentativeId());
			newImport.setHandedOverToType(existingImport.getHandedOverToType());
			newImport.setNiptStatus(existingImport.getNiptStatus());
			newImport.setQrcodeUrl(existingImport.getQrcodeUrl());
			newImport.setImporternameOnParcel(import2.getImporternameOnParcel());
			newImport.setDoNumber(existingImport.getDoNumber());
			newImport.setDoDate(existingImport.getDoDate());
			newImport.setChaName(import2.getChaName());
			newImport.setAirlineCode(import2.getAirlineCode());
			newImport.setCurrencyRate(import2.getCurrencyRate());
			newImport.setCustomPctmDate(import2.getCustomPctmDate());
			newImport.setCustomPctmNo(import2.getCustomPctmNo());
			newImport.setNoOfParcel(import2.getNoOfParcel());
			newImport.setGrossWeightInCarats(import2.getGrossWeightInCarats());
			newImport.setAssessableValueInDollar(import2.getAssessableValueInDollar());
			newImport.setSnzStatus(import2.getSnzStatus());
			newImport.setSnzExportStatus(existingImport.getSnzExportStatus());			
			
			newImport.setNoc(existingImport.getNoc());
			newImport.setDgdc_cargo_in_scan(existingImport.getDgdc_cargo_in_scan());
			newImport.setDgdc_cargo_out_scan(existingImport.getDgdc_cargo_out_scan());
			newImport.setDgdc_seepz_in_scan(existingImport.getDgdc_seepz_in_scan());
			newImport.setDgdc_seepz_out_scan(existingImport.getDgdc_seepz_out_scan());
			newImport.setOutDate(existingImport.getOutDate());
			newImport.setNiptCustomOfficerName(existingImport.getNiptCustomOfficerName());
			newImport.setNiptCustomsOfficerDesignation(existingImport.getNiptCustomsOfficerDesignation());
			newImport.setNiptDeputedFromDestination(existingImport.getNiptDeputedFromDestination());
			newImport.setNiptDeputedToDestination(existingImport.getNiptDeputedToDestination());
			newImport.setNiptDateOfEscort(existingImport.getNiptDateOfEscort());
			newImport.setNiptApproverName(existingImport.getNiptApproverName());
			newImport.setNiptApproverDesignation(existingImport.getNiptApproverDesignation());
			newImport.setNiptApproverDate(existingImport.getNiptApproverDate());
			newImport.setWrongDepositFilePath(existingImport.getWrongDepositFilePath());
			newImport.setWrongDepositwrongDepositRemarks(existingImport.getWrongDepositwrongDepositRemarks());
			newImport.setWrongDepositStatus(existingImport.getWrongDepositStatus());
			newImport.setDetentionReceiptNo(import2.getDetentionReceiptNo());
			newImport.setMopStatus(existingImport.getMopStatus());

			int sr_no = gateinoutrepo.findbysr2(import2.getCompanyId(), import2.getBranchId(), existingImport.getSirNo(),
					existingImport.getImpTransId());

			if (sr_no != import2.getNop()) {
				List<Gate_In_Out> gateinout1 = gateinoutrepo.findbysr1(existingImport.getCompanyId(),
						existingImport.getBranchId(), existingImport.getSirNo(), existingImport.getImpTransId());
				gateinoutrepo.deleteAll(gateinout1);
				for (int i = 1; i <= import2.getNop(); i++) {
					String srNo = String.format("%04d", i);
					Gate_In_Out gateinout = new Gate_In_Out();
					gateinout.setCompanyId(import2.getCompanyId());
					gateinout.setBranchId(import2.getBranchId());
					gateinout.setNop(import2.getNop());
					gateinout.setErp_doc_ref_no(import2.getSirNo());
					gateinout.setDoc_ref_no(import2.getImpTransId());
					gateinout.setSr_No(import2.getSirNo() + srNo);
					gateinout.setDgdc_cargo_in_scan("N");
					gateinout.setDgdc_cargo_out_scan("N");
					gateinout.setDgdc_seepz_in_scan("N");
					gateinout.setDgdc_seepz_out_scan("N");

					gateinoutrepo.save(gateinout);
				}
			}

			return ResponseEntity.ok(importService.updateImport(newImport));
		}

		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/getLatestCurrencyrate")
	public ResponseEntity<?> getcurrencyRate (@RequestParam("companyId") String companyId,@RequestParam("branchId") String branchId)
	{
		BigDecimal latestCurrencyRate = importRepo.findLatestCurrencyRate(companyId, branchId);
		return ResponseEntity.ok(latestCurrencyRate);		
	}
	
	
	
	@PostMapping("/{compid}/{branchId}/{user}/add")
	public ResponseEntity<?> addImport(@PathVariable("compid") String compid, @PathVariable("branchId") String branchId,
			@RequestBody Import import2, @PathVariable("user") String User) {

		String HawbNo =import2.getHawb();
		
		 
		if (HawbNo == null || HawbNo.isEmpty()) {
		    HawbNo = "0000";
		}
		        boolean duplicate = importService.getByMAWBAndHawbDuplicate(compid, branchId, import2.getMawb(), HawbNo);

		        if (duplicate) {
		            // Return an error response to React
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                    .body("Duplicate Mawb and Hawb combination");
		        }
//		    }

		import2.setCompanyId(compid);
		import2.setBranchId(branchId);

		import2.setDGDC_Status("Handed over to DGDC Cargo");
		import2.setCloseStatus("N");
		import2.setHoldStatus("N");
		import2.setPcStatus("N");
		import2.setScStatus("N");
		import2.setHpStatus("N");
		import2.setNiptStatus("N");
		import2.setForwardedStatus("N");
		import2.setNoc(0);
		import2.setDgdc_cargo_in_scan(0);
		import2.setDgdc_cargo_out_scan(0);
		import2.setDgdc_seepz_in_scan(0);
		import2.setDgdc_seepz_out_scan(0);
		import2.setCancelStatus("N");

		String autoIncrementIMPTransId = proccessNextIdService.autoIncrementIMPTransId();
		import2.setImpTransId(autoIncrementIMPTransId);
		String autoIncrementSIRId = proccessNextIdService.autoIncrementSIRId();
		import2.setSirNo(autoIncrementSIRId);

		if (import2.getHawb() == null || import2.getHawb().isEmpty()) {
			// Handle the case where getHawb() is null or empty

			import2.setHawb("0000");
		}
		import2.setSirDate(new Date());
		import2.setImpTransDate(new Date());
		import2.setCreatedBy(User);
		import2.setCreatedDate(new Date());
		import2.setStatus("A");
		import2.setApprovedBy(User);
		import2.setApprovedDate(new Date());
		import2.setEditedBy(User);
		import2.setEditedDate(new Date());

		for (int i = 1; i <= import2.getNop(); i++) {
			String srNo = String.format("%04d", i);
			Gate_In_Out gateinout = new Gate_In_Out();
			gateinout.setCompanyId(compid);
			gateinout.setBranchId(branchId);
			gateinout.setNop(import2.getNop());
			gateinout.setErp_doc_ref_no(autoIncrementSIRId);
			gateinout.setDoc_ref_no(autoIncrementIMPTransId);
			gateinout.setSr_No(autoIncrementSIRId + srNo);
			gateinout.setDgdc_cargo_in_scan("N");
			gateinout.setDgdc_cargo_out_scan("N");
			gateinout.setDgdc_seepz_in_scan("N");
			gateinout.setDgdc_seepz_out_scan("N");

			gateinoutrepo.save(gateinout);
			System.out.println("gateinout " + gateinout);
		}

		Import_History history = new Import_History();
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

		Import savedImport = importService.addImport(import2);

		// Return the saved import object to React
		return ResponseEntity.ok(savedImport);

	}

	
	
	
	
	
	
	@GetMapping("/ForPartyorCha")
	public ResponseEntity<?> findForPArtyOrCha(@RequestParam("companyId") String compid,
			@RequestParam("branchId") String branchId, @RequestParam("importerId") String importerId) {

		List<Import> imports = importService.findByCompanyIdAndBranchIdAndImporterIdAndDgdcStatus(compid, branchId, importerId);
	return ResponseEntity.ok(imports);
	}
	

	@PutMapping("/PartyOrCHAupdate")
	public ResponseEntity<Object> updateImportPartOrCha(@RequestParam("companyId") String compid,
			@RequestParam("branchId") String branchId, @RequestBody List<Import> importList,
			@RequestParam("user") String user, @RequestParam("otp") String OTP, @RequestParam("carting") String carting,
			@RequestParam("representativeId") String representativeId) {

	    try {
	        // Retrieve data from services or repositories
	        RepresentParty representative = RepsentativeService.findByRepresentativeId(compid, branchId, carting,representativeId);


	        if (representative.getOtp().equals(OTP)) { 
	           

	            List<Import_History> historyEntries = new ArrayList<>();
	            for (Import existingImport : importList) {	            	
	            	
	            	int updateHandOverToParty = importRepository.updateHandOverToParty(compid, branchId,existingImport.getSirNo(), existingImport.getMawb(), existingImport.getHawb(), "Handed Over to CHA",user,carting , representativeId);
	            	
	            	if(updateHandOverToParty == 1) { 
	                Import_History history = new Import_History();
	                history.setCompanyId(compid);
	                history.setBranchId(branchId);
	                history.setSirNo(existingImport.getSirNo());
	                history.setMawb(existingImport.getMawb());
	                history.setHawb(existingImport.getHawb());
	                history.setTransport_Date(new Date());
	                history.setOldStatus("Handed over to DGDC SHB");
	                history.setNewStatus("Handed Over to CHA");
	                history.setUpdatedBy(user);
	            	history.setHandOverParty(carting);
	    			history.setHandOverRepresentative(representativeId);
	                historyEntries.add(history);
	            	}
	            }
	            if(historyEntries != null)
				{
	            historyService.addAllHistory(historyEntries);
				}
	            representative.setOtp("");
	            RepsentativeService.addrepresentative(representative);
	            return ResponseEntity.ok("OK");
	        } else {
	          
	            return null;
	        }
	    } catch (Exception e) {	   	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again later.");
	    }
	}

	
	
	
	
	
	
	
	
	@GetMapping("/Receivedcarting")
	public List<Import> findImportsForReceivedCartingagents(@RequestParam("companyId") String compid,
			@RequestParam("branchId") String branchId, @RequestParam("carting") String carting,
			@RequestParam("representative") String representative) {
		
				return ImportRepo.findImportsForReceiveFromConsole(compid, branchId,carting, representative,"Entry at DGDC SHB Gate");
		    
	}

	@PutMapping("/ReceivedFromCarting")
	public ResponseEntity<?> updateImporReceivedFromtCartingAgent(@RequestParam("companyId") String compid,
			@RequestParam("branchId") String branchId, @RequestBody List<Import> importList,
			@RequestParam("user") String user, @RequestParam("otp") String OTP, @RequestParam("carting") String carting,
			@RequestParam("representativeId") String ReprentativeId) {

		RepresentParty Representative = RepsentativeService.findByRepresentativeId(compid, branchId, carting,	ReprentativeId);

		if (Representative.getOtp().equals(OTP)) {

            List<Import_History> historyEntries = new ArrayList<>();
			for (Import existingImport : importList) {
				
				int updateCartingAgentReceived = importRepository.updateCartingAgentReceived(compid, branchId, existingImport.getSirNo(), existingImport.getMawb(), existingImport.getHawb(), "Handed over to DGDC SHB",user,carting , ReprentativeId);

				if(updateCartingAgentReceived == 1)
				{
				Import_History history = new Import_History();
				history.setCompanyId(compid);
				history.setBranchId(branchId);
				history.setSirNo(existingImport.getSirNo());
				history.setMawb(existingImport.getMawb());
				history.setHawb(existingImport.getHawb());
				history.setTransport_Date(new Date());
				history.setOldStatus("Entry at DGDC SHB Gate");
				history.setNewStatus("Handed over to DGDC SHB");
				history.setUpdatedBy(user);
				history.setHandOverParty(carting);
    			history.setHandOverRepresentative(ReprentativeId);
				
				 historyEntries.add(history);
				}
				
			}
			if(historyEntries != null)
			{
                historyService.addAllHistory(historyEntries);
			}
			    Representative.setOtp("");
	            RepsentativeService.addrepresentative(Representative);
			return ResponseEntity.ok("OK");
		} else {
			return null;
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	@PutMapping("/CartingAgentupdate")
	public List<Import> updateImportCartingAgent( @RequestParam("compid") String compid,
	        @RequestParam("branchId") String branchId,
	        @RequestBody List<Import> importList,
	        @RequestParam("user") String user,
	        @RequestParam("otp") String OTP,
	        @RequestParam("userId") String userId,
	        @RequestParam("tp") String tp,
	        @RequestParam("ReprentativeId") String ReprentativeId){

		RepresentParty Representative = RepsentativeService.findByRepresentativeId(compid, branchId, userId,ReprentativeId);

		String tpNo = null;

		if ("N".equals(tp)) {
			tpNo = proccessNextIdService.generateAndIncrementTPumber();
		} else {
			tpNo = tp;
		}

		if (Representative.getOtp().equals(OTP)) {			
			
			
			List<Import_History> importHistoryList = new ArrayList<>();
			

			for (Import existingImport : importList) {
				
				int updateCartingAgent = importRepository.updateCartingAgent(compid, branchId, existingImport.getSirNo(), existingImport.getMawb(), existingImport.getHawb(), "Handed over to Console",user,userId , ReprentativeId, tpNo);
							
				if(updateCartingAgent == 1) { 
				Import_History history = new Import_History();
				history.setCompanyId(compid);
				history.setBranchId(branchId);
				history.setSirNo(existingImport.getSirNo());
				history.setMawb(existingImport.getMawb());
				history.setHawb(existingImport.getHawb());
				history.setTransport_Date(new Date());
				history.setOldStatus("Handed over to DGDC Cargo");
				history.setNewStatus("Handed over to Console");
				history.setUpdatedBy(user);
							
				history.setHandOverParty(userId);
    			history.setHandOverRepresentative(ReprentativeId);
				importHistoryList.add(history);
				}
				
			}
			
//			importService.updateAll(importList);
			if(importHistoryList != null)
			{
			historyService.SaveAllImportsHistory(importHistoryList);}
			
			Representative.setOtp("");
			RepsentativeService.addrepresentative(Representative);
	
			return importList;
		} else {
			return null;
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/carting")
	public List<Import> findImportsByStatus(@RequestParam("companyId") String companyId,@RequestParam("console") String console,
			@RequestParam("branchId") String branchId)
	{
		return importRepo.findImportsForHandOverToConsole(companyId, branchId,console,"Handed over to DGDC Cargo");
	}
	
	
	
	
	@GetMapping("/getpctmNumbers")
	public ResponseEntity<?> getpctmNumbers(
			@RequestParam(name = "companyId", required = false) String companyId,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "console", required = false) String console,
			@RequestParam(name = "airlineCode", required = false) String airlineCode,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) 
	{
		
		List<String> getdistinctPctmNumbers = importRepository.getDistinctPctmNumbers(companyId, branchId, startDate, endDate,console,airlineCode);
		
		return ResponseEntity.ok(getdistinctPctmNumbers);
	}
	
	
	@GetMapping("/getdistinctAirLinesPCTMgenerated")
	public ResponseEntity<?> getdistinctAirLinesPCTMgenerated(
			@RequestParam(name = "companyId", required = false) String companyId,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "console", required = false) String console,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) 
	{
		List<Object[]> getdistinctAirLines = importRepository.getdistinctAirLinesPCTMgenerated(companyId, branchId, startDate, endDate,console);
		return ResponseEntity.ok(getdistinctAirLines);
	}
	
	@GetMapping("/getdistinctConsolesPCTMgenerated")
	public ResponseEntity<?> getdistinctConsolesPCTMgenerated(
			@RequestParam(name = "companyId", required = false) String companyId,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) 
	{
		List<Object[]> getdistinctConsoles = importRepository.getdistinctConsolesPCTMgenerated(companyId, branchId, startDate, endDate);
		return ResponseEntity.ok(getdistinctConsoles);
	}
	
	
	

	@GetMapping("/getdistinctAirLines")
	public ResponseEntity<?> getdistinctAirLines(
			@RequestParam(name = "companyId", required = false) String companyId,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "console", required = false) String console,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) 
	{
		List<Object[]> getdistinctAirLines = importRepository.getdistinctAirLines(companyId, branchId, startDate, endDate,console);
		return ResponseEntity.ok(getdistinctAirLines);
	}
	
	@GetMapping("/getdistinctConsoles")
	public ResponseEntity<?> getdistinctConsoles(
			@RequestParam(name = "companyId", required = false) String companyId,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) 
	{
		List<Object[]> getdistinctConsoles = importRepository.getdistinctConsoles(companyId, branchId, startDate, endDate);
		return ResponseEntity.ok(getdistinctConsoles);
	}

	
	
	
	
	

	
	
	
	@GetMapping("/SearchDetention")
	public boolean findByDetentionreceiptNo(@RequestParam("companyid") String compid,
			@RequestParam("branchId") String branchId, @RequestParam("detentionNo") String DetentionReceiptNo) {
		return importService.existdetentionNumber(compid, branchId, DetentionReceiptNo);
	}

//	Add Personal Carriage

	@PostMapping("/{compid}/{branchId}/{user}/addPersonal")
	public Import addPersonalImport(@PathVariable("compid") String compid, @PathVariable("branchId") String branchId,
			@RequestBody Import import2, @PathVariable("user") String User) {
		import2.setCompanyId(compid);
		import2.setBranchId(branchId);
		import2.setMawb(import2.getDetentionReceiptNo());
		import2.setHawb(import2.getDetentionReceiptNo());
		import2.setDGDC_Status("Handed over to DGDC SEEPZ");
		import2.setCloseStatus("Y");
		import2.setHoldStatus("N");
		import2.setPcStatus("Y");
		import2.setScStatus("N");
		import2.setHpStatus("N");
		import2.setNiptStatus("N");
		import2.setParcelType("PCP");
		import2.setNoc(0);
		import2.setIgmNo("0000");
		import2.setDgdc_cargo_in_scan(0);
		import2.setDgdc_cargo_out_scan(0);
		import2.setDgdc_seepz_in_scan(0);
		import2.setDgdc_seepz_out_scan(0);
		import2.setCancelStatus("N");
		import2.setForwardedStatus("N");
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
		import2.setForwardedStatus("N");
		DefaultPartyDetails getdatabyParty = DefaultParyDetailsRepository.getdatabyuser_id(compid, branchId,
				import2.getImporterId());

		if (getdatabyParty == null) {
			import2.setChaName("EU0021");
			import2.setConsoleName("EU0009");
			import2.setChaCde("");

		} else {
			import2.setChaName(getdatabyParty.getImpCHA());
			import2.setConsoleName(getdatabyParty.getImpConsole());
			import2.setChaCde("");
		}

		for (int i = 1; i <= import2.getNop(); i++) {
			String srNo = String.format("%04d", i);
			Gate_In_Out gateinout = new Gate_In_Out();
			gateinout.setCompanyId(compid);
			gateinout.setBranchId(branchId);
			gateinout.setNop(import2.getNop());
			gateinout.setErp_doc_ref_no(autoIncrementSIRId);
			gateinout.setDoc_ref_no(autoIncrementIMPTransId);
			gateinout.setSr_No(autoIncrementSIRId + srNo);
			gateinout.setDgdc_cargo_in_scan("N");
			gateinout.setDgdc_cargo_out_scan("N");
			gateinout.setDgdc_seepz_in_scan("N");
			gateinout.setDgdc_seepz_out_scan("N");

			gateinoutrepo.save(gateinout);
//			System.out.println("gateinout " + gateinout);
		}

		Import_History history = new Import_History();
		history.setCompanyId(compid);
		history.setBranchId(branchId);
		history.setSirNo(autoIncrementSIRId);
		history.setMawb(import2.getDetentionReceiptNo());
		history.setHawb(import2.getDetentionReceiptNo());
		history.setTransport_Date(new Date());
		history.setOldStatus("Pending");
		history.setNewStatus("Handed over to DGDC SEEPZ");
		history.setUpdatedBy(User);
		historyService.addHistory(history);
		return importService.addImport(import2);
	}



	@GetMapping("/{compId}/{branchId}/{servicename}/ss")
	public String getASe(@PathVariable("compId") String compid, @PathVariable("branchId") String branchId,
			@PathVariable("servicename") String servicename) {
//			System.out.println(ServiceIdMappingRepositary.findServieIdByKeys(compid, branchId, servicename));
		return ServiceIdMappingRepositary.findServieIdByKeys(compid, branchId, servicename);
	}

//	@GetMapping("/{compid}/{branchId}/{tranId}/{MAWB}/{HAWB}/{sirNo}/getSingle")
//	public Import findByMAWBANDHAWB(@PathVariable("MAWB") String MAWB, @PathVariable("HAWB") String HAWB,
//			@PathVariable("compid") String compid, @PathVariable("branchId") String branchId,
//			@PathVariable("tranId") String transId, @PathVariable("sirNo") String sirNo) {
//
//		return importService.getByMAWBANdHAWB(compid, branchId, transId, MAWB, HAWB, sirNo);
//	}

	
	@GetMapping("/getSingleImportByNew")
	public Import findByMAWBANDHAWB(
			 @RequestParam("MAWB") String MAWB,
			    @RequestParam("HAWB") String HAWB,
			    @RequestParam("compid") String compid,
			    @RequestParam("branchId") String branchId,
			    @RequestParam("transId") String transId,
			    @RequestParam("sirNo") String sirNo
			) {

		return importService.getByMAWBANdHAWB(compid, branchId, transId, MAWB, HAWB, sirNo);
	}
	
	
	
//	@GetMapping("/{cid}/{bid}/{MAWBNo}")
//	public List<Import> getByMawbNo(@PathVariable("MAWBNo") String MAWBNo, @PathVariable("cid") String cid,
//			@PathVariable("bid") String bid) {
//		return importService.getByMAWB(cid, bid, MAWBNo);
//	}
	
	@GetMapping("/getImportsOfMawb")
	public List<Import> getByMawbNo(@RequestParam("mawbno") String MAWBNo, @RequestParam("compId") String cid,
			@RequestParam("branchId") String bid) {
		return importService.getByMAWB(cid, bid, MAWBNo);
	}

	@GetMapping("/{cid}/{bid}/All")
	public List<Import> getAll(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {
		return importService.getAll(cid, bid);
	}

	
	@PutMapping("/{compid}/{branchId}/{user}/update")
	public List<Import> updateImport(@PathVariable("compid") String compid, @PathVariable("branchId") String branchId,
			@RequestBody Import import2, @PathVariable("user") String User) {
		List<Import> ImportList = importService.getByMAWB(compid, branchId, import2.getMawb());

		for (Import imp : ImportList) {
			imp.setCloseStatus("Y");
			imp.setEditedBy(User);
			imp.setEditedDate(new Date());
		}

		return importService.updateAll(ImportList);
	}

	



	@DeleteMapping("/delete")
	public void deleteImport(@RequestParam("MAWb") String MAWB, @RequestParam("HAWB") String HAWB,
			@RequestParam("compid") String compid, @RequestParam("bid") String branchId,
			@RequestParam("transId") String transId, @RequestParam("sirNo") String sirNo) {
		Import byMAWBANdHAWB = importService.getByMAWBANdHAWB(compid, branchId, transId, MAWB, HAWB, sirNo);
		if (byMAWBANdHAWB != null) {
			byMAWBANdHAWB.setStatus("D");
			importService.updateImport(byMAWBANdHAWB);
		}
	}

	
//	Party or Cha Update

//	@PutMapping("/{compid}/{branchId}/{user}/{otp}/{userId}/{ReprentativeId}/PartyOrCHAupdate")
//	public ResponseEntity<Object> updateImportPartOrCha(@PathVariable("compid") String compid,
//			@PathVariable("branchId") String branchId, @RequestBody List<Import> importList,
//			@PathVariable("user") String user, @PathVariable("otp") String OTP, @PathVariable("userId") String userId,
//			@PathVariable("ReprentativeId") String ReprentativeId) {
//
//		try {
//			// Retrieve data from services or repositories
//			RepresentParty Representative = RepsentativeService.findByRepresentativeId(compid, branchId, userId,
//					ReprentativeId);
//			// ExternalParty singleRecord = ExternalParty_Service.getSingleRecord(compid,
//			// branchId, userId);
//
//			// char firstLetter = singleRecord.getUserType().charAt(0);
//
//			// Check if the OTP matches
//			if (Representative.getOtp().equals(OTP)) {
//				// Loop through importList and update each import
//				for (Import existingImport : importList) {
//					
//					existingImport.setDGDC_Status("Handed over to Party/CHA");
//					existingImport.setNSDL_Status("Out Of Charge");
//					existingImport.setOutDate(new Date());
//					existingImport.setHandedOverPartyId(userId);
//					existingImport.setHandedOverRepresentativeId(ReprentativeId);
//					existingImport.setHandedOverToType(userId.charAt(0) == 'E' ? "C" : userId.charAt(0) == 'M' ? "P" :
//					// Add more conditions as needed
//							"d");			
//					
//					
//					
//					
//					Import_History history = new Import_History();
//					history.setCompanyId(compid);
//					history.setBranchId(branchId);
//					history.setSirNo(existingImport.getSirNo());
//					history.setMawb(existingImport.getMawb());
//					history.setHawb(existingImport.getHawb());
//					history.setTransport_Date(new Date());
//					history.setOldStatus("Handed over to DGDC SEEPZ");
//					history.setNewStatus("Handed Over to Party/CHA");
//					history.setUpdatedBy(user);
//					historyService.addHistory(history);
//
//					
//				}
//
//				// Perform your import updates and return a success response
//				List<Import> updatedImports = importService.updateAll(importList);
//				return ResponseEntity.ok(updatedImports);
//			} else {
//				// Return an unauthorized response if the OTP doesn't match
//				return null;
//			}
//		} catch (Exception e) {
//			// Handle any unexpected errors here without showing specific error messages.
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
//		}
//	}
	
	
	
	
	
	

	@PutMapping("/ChangeStatus")
	public Import updateSingleImport(@RequestParam("mawb") String MAWB, @RequestParam("hawb") String HAWB,
			@RequestParam("companyid") String compid, @RequestParam("branchId") String branchId,
			@RequestParam("transId") String transId, @RequestParam("sir") String sirNo,
			@RequestParam("buttonType") String condition, @RequestParam("user") String user,
			@RequestBody Import import2) {
		Import byMAWBANdHAWB = importService.getByMAWBANdHAWB(compid, branchId, transId, MAWB, HAWB, sirNo);
		if (byMAWBANdHAWB != null) {
			switch (condition) {
			case "cancel":

				if ("Y".equals(import2.getCancelStatus())) {
					// Only update the cancel remarks if the cancel status is "Y"
					byMAWBANdHAWB.setCancelRemarks(import2.getCancelRemarks());
				} else {
					// If cancel status is not "Y", perform other operations
					Import_History history = new Import_History();
					history.setCompanyId(compid);
					history.setBranchId(branchId);
					history.setSirNo(byMAWBANdHAWB.getSirNo());
					history.setMawb(byMAWBANdHAWB.getMawb());
					history.setHawb(byMAWBANdHAWB.getHawb());
					history.setTransport_Date(new Date());
					history.setOldStatus(byMAWBANdHAWB.getDGDC_Status());
					history.setNewStatus("Cancelled");
					history.setUpdatedBy(user);
					historyService.addHistory(history);

					byMAWBANdHAWB.setCancelStatus("Y");
					byMAWBANdHAWB.setDGDC_Status("Cancelled");
					byMAWBANdHAWB.setCancelRemarks(import2.getCancelRemarks());
				}

				break;

			case "Uncancel":
				Import_History history2 = new Import_History();
				history2.setCompanyId(compid);
				history2.setBranchId(branchId);
				history2.setSirNo(byMAWBANdHAWB.getSirNo());
				history2.setMawb(byMAWBANdHAWB.getMawb());
				history2.setHawb(byMAWBANdHAWB.getHawb());
				history2.setTransport_Date(new Date());
				history2.setOldStatus(byMAWBANdHAWB.getDGDC_Status());
				history2.setNewStatus("Handed over to DGDC Cargo");
				history2.setUpdatedBy(user);
				historyService.addHistory(history2);

				byMAWBANdHAWB.setCancelStatus("N");
				byMAWBANdHAWB.setDGDC_Status("Handed over to DGDC Cargo");
				byMAWBANdHAWB.setCancelRemarks("");
				break;

			case "hold":
				byMAWBANdHAWB.setHoldStatus("Y");
				byMAWBANdHAWB.setHoldDate(new Date());
				byMAWBANdHAWB.setHoldBy(user);
				break;

			case "unhold":
				byMAWBANdHAWB.setHoldStatus("R");
				byMAWBANdHAWB.setHoldDate(null);
				byMAWBANdHAWB.setHoldBy("");
				break;

			case "personal-carriage":
				byMAWBANdHAWB.setPcStatus("Y");
				break;

			case "unpersonal-carriage":
				byMAWBANdHAWB.setPcStatus("N");
				ImportPC byIDS = ImportPCService.getByIDS(compid, branchId, MAWB, HAWB, sirNo);
				if (byIDS != null) {
					ImportPCService.deleteImportPc(compid, branchId, MAWB, HAWB, sirNo);
				}

				break;

			case "special-carting":
				byMAWBANdHAWB.setScStatus("Y");
				break;

			case "unspecial-carting":
				byMAWBANdHAWB.setScStatus("N");
				break;

			case "heavy":
				byMAWBANdHAWB.setHpStatus("Y");
				byMAWBANdHAWB.setHpWeight(import2.getHpWeight());
				byMAWBANdHAWB.setHppackageno(import2.getHppackageno());
				break;

			case "Unheavy":
				byMAWBANdHAWB.setHpStatus("N");
				byMAWBANdHAWB.setHpWeight(null);
				byMAWBANdHAWB.setHppackageno("");
				break;

			case "impose-Penalty":
				byMAWBANdHAWB.setImposePenaltyAmount(import2.getImposePenaltyAmount());
				byMAWBANdHAWB.setImposePenaltyRemarks(import2.getImposePenaltyRemarks());
				break;

			case "heavy-Report":
				System.out.println("Heavy Report");
				break;

			case "NIPT":
				byMAWBANdHAWB.setNiptCustomOfficerName(import2.getNiptCustomOfficerName());
				byMAWBANdHAWB.setNiptCustomsOfficerDesignation(import2.getNiptCustomsOfficerDesignation());
				byMAWBANdHAWB.setNiptDeputedFromDestination(import2.getNiptDeputedFromDestination());
				byMAWBANdHAWB.setNiptDeputedToDestination(import2.getNiptDeputedToDestination());
				byMAWBANdHAWB.setNiptDateOfEscort(import2.getNiptDateOfEscort());
				byMAWBANdHAWB.setNiptApproverName(import2.getNiptApproverName());
				byMAWBANdHAWB.setNiptApproverDesignation(import2.getNiptApproverDesignation());
				byMAWBANdHAWB.setNiptApproverDate(import2.getNiptApproverDate());
				break;

			default:
				// Handle unknown condition
				break;
			}

			importService.updateImport(byMAWBANdHAWB);
		}

		return null;
	}

	

//	@GetMapping("/search")
//	public List<Import> searchImports(@RequestParam(name = "pcStatus", required = false) String pcStatus,
//			@RequestParam(name = "scStatus", required = false) String scStatus,
//			@RequestParam(name = "searchValue", required = false) String searchValue,
//			@RequestParam(name = "companyid", required = false) String companyid,
//			@RequestParam(name = "branchId", required = false) String branchId,
//			@RequestParam(name = "holdStatus", required = false) String holdStatus,
//			@RequestParam(name = "forwardedStatus", required = false) String forwardedStatus,
//			@RequestParam(name = "hpStatus", required = false) String hpStatus,
//			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
//			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//
////		System.out.println("searchValue "+searchValue);
//
//		if (searchValue != null) {
//			searchValue = searchValue.trim(); // Trim leading and trailing spaces
//		}
//		return importRepo.findByAttributes(companyid, branchId, pcStatus, scStatus, hpStatus, holdStatus,
//				forwardedStatus, dgdcStatus, startDate, endDate, searchValue);
//	}
	
	
	
//	@GetMapping("/search")
//	public List<Object[]> searchImports(@RequestParam(name = "pcStatus", required = false) String pcStatus,
//			@RequestParam(name = "scStatus", required = false) String scStatus,
//			@RequestParam(name = "searchValue", required = false) String searchValue,
//			@RequestParam(name = "companyid", required = false) String companyid,
//			@RequestParam(name = "branchId", required = false) String branchId,
//			@RequestParam(name = "holdStatus", required = false) String holdStatus,
//			@RequestParam(name = "forwardedStatus", required = false) String forwardedStatus,
//			@RequestParam(name = "hpStatus", required = false) String hpStatus,
//			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
//			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//
////		System.out.println("searchValue "+searchValue);
//
//		if (searchValue != null) {
//			searchValue = searchValue.trim(); // Trim leading and trailing spaces
//		}
//		return importRepo.findByAttributes1(companyid, branchId, pcStatus, scStatus, hpStatus, holdStatus,
//				forwardedStatus, dgdcStatus, startDate, endDate, searchValue);
//	}
	
	
	@GetMapping("/search")
	public ResponseEntity<?> searchImports(@RequestParam(name = "pcStatus", required = false) String pcStatus,
			@RequestParam(name = "scStatus", required = false) String scStatus,
			@RequestParam(name = "searchValue", required = false) String searchValue,
			@RequestParam(name = "companyid", required = false) String companyid,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "holdStatus", required = false) String holdStatus,
			@RequestParam(name = "forwardedStatus", required = false) String forwardedStatus,
			@RequestParam(name = "hpStatus", required = false) String hpStatus,
			@RequestParam(name = "niptStatus", required = false) String niptStatus,
			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		if (searchValue != null) {
			searchValue = searchValue.trim(); // Trim leading and trailing spaces
		}
		 List<Import> findByAttributes1 = importRepo.importSearchForAll(companyid, branchId, niptStatus , holdStatus,forwardedStatus, dgdcStatus, startDate, endDate, searchValue);
		 
		 return ResponseEntity.ok(findByAttributes1);
	}
	
	
	
	
	
	
	
	
	

	@PostMapping("/override")
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("sirNo") String sirNo, @RequestParam("reasonforOverride") String reasonforOverride,
			@RequestParam("newnsdlStatus") String nsdlStatus, @RequestParam("mawb") String mawb,
			@RequestParam("hawb") String hawb, @RequestParam("companyid") String companyid,
			@RequestParam("branchId") String branchId, @RequestParam("transId") String transId)
			throws java.io.IOException {
		try {
			Import byMAWBANdHAWB = importService.getByMAWBANdHAWB(companyid, branchId, transId, mawb, hawb, sirNo);

			if (byMAWBANdHAWB != null) {
				// Get the original file name
				String originalFileName = file.getOriginalFilename();

				// Generate a unique file name to avoid duplicates
				String uniqueFileName = generateUniqueFileName(originalFileName);

				// Set the unique file name in the database
				byMAWBANdHAWB.setNsdlStatusDocs(FileUploadProperties.getPath() + uniqueFileName);
				byMAWBANdHAWB.setNSDL_Status(nsdlStatus);
				// Save the file to your local system with the unique name
				Files.copy(file.getInputStream(), Paths.get(FileUploadProperties.getPath() + uniqueFileName));

				// Set other fields in the Import object
				byMAWBANdHAWB.setReasonforOverride(reasonforOverride);
				byMAWBANdHAWB.setNSDL_Status(nsdlStatus);

				// Update the Import object in the database
				Import updateImport = importService.updateImport(byMAWBANdHAWB);
				return ResponseEntity.ok(updateImport);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
		}
	}

	// Helper method to generate a unique file name
	private String generateUniqueFileName(String originalFileName) {
		String uniqueFileName = originalFileName;
		int suffix = 1;

		// Check if the file with the same name already exists
		while (Files.exists(Paths.get(FileUploadProperties.getPath() + uniqueFileName))) {
			int dotIndex = originalFileName.lastIndexOf('.');
			String nameWithoutExtension = dotIndex != -1 ? originalFileName.substring(0, dotIndex) : originalFileName;
			String fileExtension = dotIndex != -1 ? originalFileName.substring(dotIndex) : "";
			uniqueFileName = nameWithoutExtension + "_" + suffix + fileExtension;
			suffix++;
		}

		return uniqueFileName;
	}

	@GetMapping("/getImage")
	public ResponseEntity<?> getImageOrPdf(@RequestParam("MAWb") String MAWB, @RequestParam("HAWB") String HAWB,
			@RequestParam("compid") String compid, @RequestParam("bid") String branchId,
			@RequestParam("transId") String transId, @RequestParam("sirNo") String sirNo) throws IOException {

		Import importObject = importService.getByMAWBANdHAWB(compid, branchId, transId, MAWB, HAWB, sirNo);

		if (importObject != null) {
			String nsdlStatusDocsPath = importObject.getNsdlStatusDocs();
			Path filePath = Paths.get(nsdlStatusDocsPath);

			// Check if the file exists
			if (Files.exists(filePath)) {
				try {
					String fileExtension = getFileExtension(nsdlStatusDocsPath);

					if (isImageFile(fileExtension)) {
						// If it's an image file, return a data URL
						byte[] imageBytes = Files.readAllBytes(filePath);
//						String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//						String dataURL = "data:image/jpeg;base64," + base64Image;

						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.TEXT_PLAIN); // Set the content type to text/plain
						return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
					} else if (isPdfFile(fileExtension)) {
						// If it's a PDF file, return the PDF data as base64
						byte[] pdfBytes = Files.readAllBytes(filePath);
						String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);

						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.APPLICATION_PDF); // Set the content type to application/pdf

						return new ResponseEntity<>(pdfBase64, headers, HttpStatus.OK);
					}
				} catch (IOException | java.io.IOException e) {
					// Handle the IOException appropriately (e.g., log it)
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
			}
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("/getImageWrongDeposit")
	public ResponseEntity<?> getImageOrPdfWrongDeposit(@RequestParam("MAWb") String MAWB,
			@RequestParam("HAWB") String HAWB, @RequestParam("compid") String compid,
			@RequestParam("bid") String branchId, @RequestParam("transId") String transId,
			@RequestParam("sirNo") String sirNo) throws IOException {

		Import importObject = importService.getByMAWBANdHAWB(compid, branchId, transId, MAWB, HAWB, sirNo);

		if (importObject != null) {
			String nsdlStatusDocsPath = importObject.getWrongDepositFilePath();
			Path filePath = Paths.get(nsdlStatusDocsPath);

			// Check if the file exists
			if (Files.exists(filePath)) {
				try {
					String fileExtension = getFileExtension(nsdlStatusDocsPath);

					if (isImageFile(fileExtension)) {
						// If it's an image file, return a data URL
						byte[] imageBytes = Files.readAllBytes(filePath);
						String base64Image = Base64.getEncoder().encodeToString(imageBytes);
						String dataURL = "data:image/jpeg;base64," + base64Image;

						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.TEXT_PLAIN); // Set the content type to text/plain

						return new ResponseEntity<>(dataURL, headers, HttpStatus.OK);
					} else if (isPdfFile(fileExtension)) {
						// If it's a PDF file, return the PDF data as base64
						byte[] pdfBytes = Files.readAllBytes(filePath);
						String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);

						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.APPLICATION_PDF); // Set the content type to application/pdf

						return new ResponseEntity<>(pdfBase64, headers, HttpStatus.OK);
					}
				} catch (IOException | java.io.IOException e) {
					// Handle the IOException appropriately (e.g., log it)
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
			}
		}

		return ResponseEntity.notFound().build();
	}

	private String getFileExtension(String filePath) {
		int dotIndex = filePath.lastIndexOf('.');
		if (dotIndex >= 0 && dotIndex < filePath.length() - 1) {
			return filePath.substring(dotIndex + 1).toLowerCase();
		}
		return "";
	}

	private boolean isImageFile(String fileExtension) {
		return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png")
				|| fileExtension.equals("gif");
	}

	private boolean isPdfFile(String fileExtension) {
		return fileExtension.equals("pdf");
	}

	


//	NIPT
	@GetMapping("/{compid}/{branchId}/{user}/addNIPT")
	public ResponseEntity<?> addNIPTImport(@PathVariable("compid") String compid,
			@PathVariable("branchId") String branchId, @PathVariable("user") String User,
			@RequestParam("url") String url) {

		try {

			RestTemplate restTemplate = new RestTemplate();
			String htmlContent = restTemplate.getForObject(url, String.class);

			// Parse the HTML string using Jsoup
			Document doc = Jsoup.parse(htmlContent);

			// Extract data from specific HTML elements using CSS selectors
			String dcName = doc.select("#lblDCName").text();
			String sezName = doc.select("#lblSEZName").text();
			String entityName = doc.select("#lblEntityName").text();
			String importExportCode = doc.select("#lblImportExportCode").text();
			String entityID = doc.select("#lblEntityID").text();

			// Extract additional fields as needed
			String requestDetails = doc.select("#lblRequestDetails").text();
			String requestID = doc.select("#lblRequestID").text();
			String portCode = doc.select("#lblBOEPortCode").text();
			String portOfOrigin = doc.select("#lblBOEPortOrgn").text();
			String countryOfOrigin = doc.select("#lblBOECntryOrgn").text();
			String importDeptDetails = doc.select("#lblBEThokaNo").text();
			String chaDetails = doc.select("#lblCHADetails").text();
			String assessmentDate = doc.select("#lblAssesmentDate").text();
			String requestStatus = doc.select("#lblRequestStatus").text();
			BigDecimal assessableValue =  new BigDecimal(doc.select("#lblAssessableValue").text());

			// Split the extracted text into chaName and chaCode using the '-' sign
			String[] parts5 = chaDetails.split(" - ");
			String chaName = parts5[0];
			String chaCode = parts5.length > 1 ? parts5[1] : "";

			Element beElement = doc.select("#lblBEThokaNo").first();
			String beText = beElement.text();

			// Split the extracted text into BeNumber and BeDate
			String[] values = beText.split(", ");
			String beNumber = values[0];
			String beDate = values[1];

			// Extract data from the table
			// Extract data from the table
			Element table = doc.select("table#gvConsigneeDetails").first();
			Elements rows = table.select("tr");

			String igmNoDate = "";
			String mawbNoDate = "";
			String hawbNoDate = "";
			String weight = "";
			String packets = "";
			String packageMarksNumbers = "";
			String noOfContainers = "";

			if (rows.size() >= 2) { // Check if there are at least two rows (header row and data row)
				Element dataRow = rows.get(1); // Assuming the data row is the second row (index 1)
				Elements columns = dataRow.select("td");

				if (columns.size() >= 7) { // Check if there are at least seven columns in the data row
					igmNoDate = columns.get(0).text();
					mawbNoDate = columns.get(1).text();
					hawbNoDate = columns.get(2).text();
					weight = columns.get(3).text();
					packets = columns.get(4).text();
					packageMarksNumbers = columns.get(5).text();
					noOfContainers = columns.get(6).text();
				}
			}

			String[] parts = mawbNoDate.split(" ");

			String mawbNo = "";
			String mawbDate = "";
			
			if (parts.length >= 2) {

				Pattern pattern = Pattern.compile("\\b\\d{2}/\\d{2}/\\d{4}\\b"); // Matches date in the format
																					// dd/MM/yyyy
				Matcher matcher = pattern.matcher(mawbNoDate);

				if (matcher.find()) {
					mawbDate = matcher.group(); // Get the found date
					mawbNo = mawbNoDate.substring(0, matcher.start()).replaceAll("\\s", "");
				}

			}
			

//			if (parts.length >= 2) {
//				mawbNo = parts[0]; // "3111795912"
//				mawbDate = parts[1]; // "22/08/2023"
//			}

			String[] parts2 = hawbNoDate.split(" ");

			String HawbNo = "";
			String HawbDate = "";

			if (parts2.length >= 2) {
				HawbNo = parts2[0]; // "3111795912"
				HawbDate = parts2[1]; // "22/08/2023"
			}

			String[] parts3 = igmNoDate.split(" ");

			String igmNo = "";
			String igmDate = "";

			if (parts3.length >= 2) {
				igmNo = parts3[0]; // "3111795912"
				igmDate = parts3[1]; // "22/08/2023"
			}
			// Create a JSON object to store all extracted fields
			String extractedData = "{" + "\"dcName\":\"" + dcName + "\"," + "\"sezName\":\"" + sezName + "\","
					+ "\"entityName\":\"" + entityName + "\"," + "\"importExportCode\":\"" + importExportCode + "\","
					+ "\"entityID\":\"" + entityID + "\"," + "\"requestDetails\":\"" + requestDetails + "\","
					+ "\"requestID\":\"" + requestID + "\"," + "\"portCode\":\"" + portCode + "\","
					+ "\"portOfOrigin\":\"" + portOfOrigin + "\"," + "\"countryOfOrigin\":\"" + countryOfOrigin + "\","
					+ "\"importDeptDetails\":\"" + importDeptDetails + "\"," + "\"chaDetails\":\"" + chaDetails + "\","
					+ "\"assessmentDate\":\"" + assessmentDate + "\"," + "\"requestStatus\":\"" + requestStatus + "\","
					+ "\"assessableValue\":\"" + assessableValue + "\","
//                 + "\"igmNoDate\":\"" + igmNoDate + "\","
//                 + "\"mawbNoDate\":\"" + mawbNoDate + "\","
					+ "\"mawbNo\":\"" + mawbNo + "\"," + "\"mawbDate\":\"" + mawbDate + "\"," + "\"HawbNo\":\"" + HawbNo
					+ "\"," + "\"HawbDate\":\"" + HawbDate + "\","
//                 + "\"hawbNoDate\":\"" + hawbNoDate + "\","
					+ "\"igmNo\":\"" + igmNo + "\"," + "\"igmDate\":\"" + igmDate + "\"," + "\"weight\":\"" + weight
					+ "\"," + "\"packets\":\"" + packets + "\"," + "\"packageMarksNumbers\":\"" + packageMarksNumbers
					+ "\"," + "\"noOfContainers\":\"" + noOfContainers + "\"" + "}";

			Import findByRequestId = importService.findByRequestId(compid, branchId, mawbNo, requestID);

			if (findByRequestId != null) {
				return ResponseEntity.ok("Duplicate Scanning");
			} else {
				Import import2 = new Import();

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				import2.setBeNo(beNumber);
				import2.setBeDate(dateFormat.parse(beDate));
				import2.setCompanyId(compid);
				import2.setBranchId(branchId);
				import2.setChaCde(chaCode);
				import2.setChaName(chaName);
				import2.setCloseStatus("Y");
				import2.setHoldStatus("N");
				import2.setPcStatus("N");
				import2.setScStatus("N");
				import2.setHpStatus("N");
				import2.setNiptStatus("Y");

				import2.setCancelStatus("N");

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
				import2.setForwardedStatus("N");
				import2.setApprovedDate(new Date());
				import2.setEditedBy(User);
				import2.setEditedDate(new Date());
				import2.setMawb(mawbNo);
				import2.setAssessableValue(assessableValue);
				import2.setBeRequestId(requestID);
				import2.setPortOrigin(portOfOrigin);
				import2.setCountryOrigin(countryOfOrigin);
				import2.setNiptStatus("Y");
				import2.setQrcodeUrl(url);
				import2.setImporternameOnParcel(entityName);

//				String extractedString = entityName.replaceAll("\\.\\s*$", "");

//				entityID

//				System.out.println("Entity Id   *************" +entityID);
				Party findByPartyName = PartyService.findByEntityId(compid, branchId, entityID);
				DefaultPartyDetails getdatabyParty = DefaultParyDetailsRepository.getdatabyuser_id(compid, branchId,
						import2.getImporterId());

				if (getdatabyParty == null) {
					import2.setChaName("EU0021");
					import2.setConsoleName("EU0009");
					import2.setChaCde("");

				} else {
					import2.setChaName(getdatabyParty.getImpCHA());
					import2.setConsoleName(getdatabyParty.getImpConsole());
					import2.setChaCde("");
				}

				if (findByPartyName != null) {
					import2.setImporterId(findByPartyName.getPartyId());
					import2.setSezEntityId(findByPartyName.getEntityId());
					import2.setIec(findByPartyName.getIecNo());
				}

				if (HawbNo == null || HawbNo.trim().isEmpty()) {
					import2.setHawb("0000");
				} else {
					import2.setHawb(HawbNo);
				}
				import2.setNSDL_Status(requestStatus);
				import2.setDGDC_Status("Handed over to DGDC SEEPZ");
				import2.setIgmNo(igmNo);
				import2.setChaCde(chaCode);
				
				import2.setForwardedStatus("N");
//				import2.setChaName(getdatabyParty.getImpCHA());
//				import2.setConsoleName(getdatabyParty.getImpConsole());
//				import2.setConsoleName(getdatabyParty.getImpConsole());
				Import FirstNipt = importService.findImportWithCriteria(compid, branchId, new Date());

				if (FirstNipt != null) {
					import2.setNiptCustomOfficerName(FirstNipt.getNiptCustomOfficerName());
					import2.setNiptCustomsOfficerDesignation(FirstNipt.getNiptCustomsOfficerDesignation());
					import2.setNiptDeputedFromDestination(FirstNipt.getNiptDeputedFromDestination());
					import2.setNiptDeputedToDestination(FirstNipt.getNiptDeputedToDestination());
					import2.setNiptDateOfEscort(FirstNipt.getNiptDateOfEscort());
					import2.setNiptApproverName(FirstNipt.getNiptApproverName());
					import2.setNiptApproverDesignation(FirstNipt.getNiptApproverDesignation());
					import2.setNiptApproverDate(FirstNipt.getNiptApproverDate());
				}

//				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date igmDate2 = dateFormat.parse(igmDate);
				import2.setIgmDate(igmDate2);
				String firstCharacter = String.valueOf(packets.charAt(0));

				import2.setNop(Integer.parseInt(firstCharacter));

				BigDecimal grossWeight = null;
				String uomWeight = null;

				// Split the input string by space
				String[] parts4 = weight.split(" ");

				if (parts4.length == 2) {
					try {
						// Attempt to parse the first part as a BigDecimal
						grossWeight = new BigDecimal(parts4[0]);
					} catch (NumberFormatException e) {
						// Handle the exception if parsing fails
					}

					// Assign the second part to uomWeight
					uomWeight = parts4[1];
				}

				import2.setGrossWeight(grossWeight);
				if (uomWeight.equals("KILOGRAMS")) {

					import2.setUomWeight(uomWeight);
				}

				for (int i = 1; i <= Integer.parseInt(firstCharacter); i++) {
					String srNo = String.format("%04d", i);
					Gate_In_Out gateinout = new Gate_In_Out();
					gateinout.setCompanyId(import2.getCompanyId());
					gateinout.setBranchId(import2.getBranchId());
					gateinout.setNop(Integer.parseInt(firstCharacter));
					gateinout.setErp_doc_ref_no(autoIncrementSIRId);
					gateinout.setDoc_ref_no(autoIncrementIMPTransId);
					gateinout.setSr_No(autoIncrementSIRId + srNo);
					gateinout.setDgdc_cargo_in_scan("N");
					gateinout.setDgdc_cargo_out_scan("N");
					gateinout.setDgdc_seepz_in_scan("N");
					gateinout.setDgdc_seepz_out_scan("N");

					gateinoutrepo.save(gateinout);
				}

				Import_History history = new Import_History();
				history.setCompanyId(compid);
				history.setBranchId(branchId);
				history.setSirNo(autoIncrementSIRId);
				history.setMawb(import2.getMawb());
				history.setHawb(import2.getHawb());
				history.setTransport_Date(new Date());
				history.setOldStatus("Pending");
				history.setNewStatus("Handed over to DGDC SEEPZ");
				history.setUpdatedBy(User);
				historyService.addHistory(history);

				importService.addImport(import2);
				return ResponseEntity.ok(autoIncrementSIRId);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("Error extracting data from the provided URL");
		}

	}

	@PostMapping("/wrongDeposit")
	public ResponseEntity<?> handlewrongDeposit(@RequestParam("file") MultipartFile file,
			@RequestParam("sirNo") String sirNo, @RequestParam("reasonwrongDeposit") String reasonwrongDeposit,
			@RequestParam("mawb") String mawb, @RequestParam("hawb") String hawb,
			@RequestParam("companyid") String companyid, @RequestParam("branchId") String branchId,
			@RequestParam("transId") String transId) throws java.io.IOException {
		try {
			Import byMAWBANdHAWB = importService.getByMAWBANdHAWB(companyid, branchId, transId, mawb, hawb, sirNo);

			if (byMAWBANdHAWB != null) {
				// Get the original file name
				String originalFileName = file.getOriginalFilename();

				// Generate a unique file name to avoid duplicates
				String uniqueFileName = generateUniqueFileName(originalFileName);

				// Set the unique file name in the database
				byMAWBANdHAWB.setWrongDepositFilePath((FileUploadProperties.getPath() + uniqueFileName));
				byMAWBANdHAWB.setDgdcStatus("Wrong Deposit");
//				byMAWBANdHAWB.setNSDL_Status(nsdlStatus);
				// Save the file to your local system with the unique name
				Files.copy(file.getInputStream(), Paths.get(FileUploadProperties.getPath() + uniqueFileName));

				// Set other fields in the Import object
				byMAWBANdHAWB.setWrongDepositwrongDepositRemarks(reasonwrongDeposit);
//				byMAWBANdHAWB.setNSDL_Status(nsdlStatus);
				byMAWBANdHAWB.setWrongDepositStatus("Y");
				// Update the Import object in the database
				Import updateImport = importService.updateImport(byMAWBANdHAWB);
				return ResponseEntity.ok(updateImport);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
		}
	}

	@PostMapping("/{compid}/{branchId}/{tranId}/{MAWB}/{HAWB}/{sirNo}/{userId}/updateNIPT")
	public ResponseEntity<?> updateNipt(@PathVariable("MAWB") String MAWB, @PathVariable("HAWB") String HAWB,
			@PathVariable("compid") String compid, @PathVariable("branchId") String branchId,
			@PathVariable("tranId") String transId, @PathVariable("userId") String userId,
			@PathVariable("sirNo") String sirNo) {

		Import importObject = importService.getByMAWBANdHAWB(compid, branchId, transId, MAWB, HAWB, sirNo);

		try {
			RestTemplate restTemplate = new RestTemplate();
			String htmlContent = restTemplate.getForObject(importObject.getQrcodeUrl(), String.class);

			// Parse the HTML string using Jsoup
			Document doc = Jsoup.parse(htmlContent);

			String requestStatus = doc.select("#lblRequestStatus").text();

			// Extract data from the table
			// Extract data from the table
			Element table = doc.select("table#gvConsigneeDetails").first();
			Elements rows = table.select("tr");

			String hawbNoDate = "";

			if (rows.size() >= 2) { // Check if there are at least two rows (header row and data row)
				Element dataRow = rows.get(1); // Assuming the data row is the second row (index 1)
				Elements columns = dataRow.select("td");

				if (columns.size() >= 7) { // Check if there are at least seven columns in the data row

					hawbNoDate = columns.get(2).text();

				}
			}

			String HawbNo = "";
			String[] parts2 = hawbNoDate.split(" ");

			if (parts2.length >= 2) {
				HawbNo = parts2[0];
			}
			if (HawbNo == null || HawbNo.trim().isEmpty()) {
				importObject.setHawb("0000");
			} else {
				importObject.setHawb(HawbNo);
			}
			importObject.setNSDL_Status(requestStatus);
			importObject.setEditedBy(userId);
			importObject.setEditedDate(new Date());
			Import addImport = importService.addImport(importObject);
			return ResponseEntity.ok(addImport);
		} catch (Exception e) {
			e.printStackTrace();
			// Handle other exceptions here if needed
			return ResponseEntity.status(422).body("Error extracting data from the provided URL");
		}

	}

//	@GetMapping("/searchBillinTransaction")
//	public List<CombinedImportExport> searchBillingTransation(
//			@RequestParam(name = "companyid", required = false) String companyid,
//			@RequestParam(name = "branchId", required = false) String branchId,
//			@RequestParam(name = "PartyId", required = false) String PartyId,
//			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//
//		List<Object[]> combinedImportExportData = importRepo.getCombinedImportExportData(companyid, branchId, PartyId,
//				startDate, endDate);
//		String findPartyNameById = PartyService.findPartyNameById(companyid, branchId, PartyId);
//		holidayService.findByDate(companyid, branchId, endDate);
//
//		List<CombinedImportExport> combinedDataList = combinedImportExportData.stream()
//				.map(array -> new CombinedImportExport((String) array[0], findPartyNameById,
//						new Date(((java.util.Date) array[1]).getTime()), (Integer) array[2],
//						array[3] != null ? array[3].toString() : null, array[4] != null ? array[4].toString() : null,
//						array[5] != null ? array[5].toString() : null, (BigDecimal) array[6], (Integer) array[7],
//						array[8] != null ? array[8].toString() : null, array[9] != null ? array[9].toString() : null,
//						array[10] != null ? array[10].toString() : null, (BigDecimal) array[11]))
//				.collect(Collectors.toList());
//
//		return combinedDataList;
//
//	}
//	

	
	public static boolean isSecondSaturday(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);

	    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
	    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

	    // Check if the day is Saturday (Calendar.SATURDAY = 7)
	    if (dayOfWeek == Calendar.SATURDAY) {
	        // Check if the date falls between the 8th and 14th day of the month
	        return dayOfMonth >= 8 && dayOfMonth <= 14;
	    }
	    return false;
	}

//	 Update Bill of Entry Number

	@GetMapping("/{compid}/{branchId}/{user}/updateBillNumber")
	public ResponseEntity<?> updateBillNumber(@PathVariable("compid") String compid,
			@PathVariable("branchId") String branchId, @PathVariable("user") String User,
			@RequestParam("url") String url) {

		try {
//             System.out.println(url);
			RestTemplate restTemplate = new RestTemplate();
			String htmlContent = restTemplate.getForObject(url, String.class);

			// Parse the HTML string using Jsoup
			Document doc = Jsoup.parse(htmlContent);

//				// Extract data from specific HTML elements using CSS selectors
//				String dcName = doc.select("#lblDCName").text();
//				String sezName = doc.select("#lblSEZName").text();
//				String entityName = doc.select("#lblEntityName").text();
//				String importExportCode = doc.select("#lblImportExportCode").text();
			String entityID = doc.select("#lblEntityID").text();
//
//				// Extract additional fields as needed
//				String requestDetails = doc.select("#lblRequestDetails").text();
			String requestID = doc.select("#lblRequestID").text();
//				String portCode = doc.select("#lblBOEPortCode").text();
//				String portOfOrigin = doc.select("#lblBOEPortOrgn").text();
//				String countryOfOrigin = doc.select("#lblBOECntryOrgn").text();
//				String importDeptDetails = doc.select("#lblBEThokaNo").text();
			String chaDetails = doc.select("#lblCHADetails").text();
//				String assessmentDate = doc.select("#lblAssesmentDate").text();
			String requestStatus = doc.select("#lblRequestStatus").text();
			BigDecimal assessableValue =  new BigDecimal(doc.select("#lblAssessableValue").text());

			// Split the extracted text into chaName and chaCode using the '-' sign
//			String[] parts5 = chaDetails.split(" - ");
//			String chaName = parts5[0];
//			String chaCode = parts5.length > 1 ? parts5[1] : "";

			Element beElement = doc.select("#lblBEThokaNo").first();
			String beText = beElement.text();

			// Split the extracted text into BeNumber and BeDate
			String[] values = beText.split(", ");
			String beNumber = values[0];
			String beDate = values[1];

			// Extract data from the table
			// Extract data from the table
			Element table = doc.select("table#gvConsigneeDetails").first();
			Elements rows = table.select("tr");

			String igmNoDate = "";
			String mawbNoDate = "";
			String hawbNoDate = "";
			String weight = "";
			String packets = "";
			String packageMarksNumbers = "";
			String noOfContainers = "";

			if (rows.size() >= 2) { // Check if there are at least two rows (header row and data row)
				Element dataRow = rows.get(1); // Assuming the data row is the second row (index 1)
				Elements columns = dataRow.select("td");

				if (columns.size() >= 7) { // Check if there are at least seven columns in the data row
					igmNoDate = columns.get(0).text();
					mawbNoDate = columns.get(1).text();
					hawbNoDate = columns.get(2).text();
					weight = columns.get(3).text();
					packets = columns.get(4).text();
					packageMarksNumbers = columns.get(5).text();
					noOfContainers = columns.get(6).text();
				}
			}

			String[] parts = mawbNoDate.split(" ");

			String mawbNo = "";
			String mawbDate = "";

			if (parts.length >= 1) {
			    mawbNo = parts[0].replaceAll("\\s", ""); // Initialize with MawbNo removing spaces

			    if (parts.length >= 2) {
			        // Check for a date pattern in the second part
			        Pattern pattern = Pattern.compile("\\b\\d{2}/\\d{2}/\\d{4}\\b");
			        Matcher matcher = pattern.matcher(mawbNoDate);

			        if (matcher.find()) {
			            mawbDate = matcher.group(); // Get the found date
			            mawbNo = mawbNoDate.substring(0, matcher.start()).replaceAll("\\s", "");
			        }
			    }
			}
			
			
//			String[] parts = mawbNoDate.split(" ");
//
//			String mawbNo = "";
//			String mawbDate = "";
//			
//			if (parts.length >= 2) {
//
//				Pattern pattern = Pattern.compile("\\b\\d{2}/\\d{2}/\\d{4}\\b"); // Matches date in the format
//																					// dd/MM/yyyy
//				Matcher matcher = pattern.matcher(mawbNoDate);
//
//				if (matcher.find()) {
//					mawbDate = matcher.group(); // Get the found date
//					mawbNo = mawbNoDate.substring(0, matcher.start()).replaceAll("\\s", "");
//				}
//
//			}
			
			
			
			

			// If a date is found, update mawbNo based on the position of the date
			if (!mawbDate.isEmpty()) {
			    mawbNo = mawbNoDate.substring(0, mawbNoDate.indexOf(mawbDate)).replaceAll("\\s", "");
			}
			
			
			
			String[] parts2 = hawbNoDate.split(" ");

			String HawbNo = "";
			String HawbDate = "";

			if (parts2.length >= 1) {
			    HawbNo = parts2[0]; // If at least HawbNo is present

			    if (parts2.length >= 2) {
			        HawbDate = parts2[1]; // If both HawbNo and HawbDate are present
			    }
			}
			String[] parts3 = igmNoDate.split(" ");

			String igmNo = "";
			String igmDate = "";

			if (parts3.length >= 2) {
				igmNo = parts3[0]; // "3111795912"
				igmDate = parts3[1]; // "22/08/2023"
			}
			
			
			if (HawbNo == null || HawbNo.isEmpty()) {
			    HawbNo = "0000";
			}
			
			if (igmNo == null || igmNo.isEmpty()) {
				igmNo = "0000";
			}
			
			System.out.println("mawb  "+mawbNo);
			
			
			System.out.println("hawb  "+HawbNo);
			
			System.out.println("igm   "+igmNo);
			
			Import import2 = importService.findForBillNumber(compid, branchId, mawbNo, HawbNo, igmNo);

			System.out.println("import2   "+import2);
			Party findByEntityId = PartyService.findByEntityId(compid, branchId, entityID);
			
//			String findByEntityId = PartyService.findByEntityIdPartyId(compid, branchId, entityID);
//				System.out.println("findByEntityId "+findByEntityId);
			DefaultPartyDetails getdatabyParty = DefaultParyDetailsRepository.getdatabyuser_id(compid, branchId,
					findByEntityId.getPartyId());
//				System.out.println("getdatabyParty "+getdatabyParty);
			if (import2 != null) {
				import2.setBeRequestId(requestID);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				import2.setBeNo(beNumber);
				import2.setBeDate(dateFormat.parse(beDate));
				
				import2.setAssessableValue(assessableValue);
				
				import2.setImporterId(findByEntityId.getPartyId());
				import2.setImporternameOnParcel(findByEntityId.getPartyName());
				import2.setSezEntityId(findByEntityId.getEntityId());
				import2.setIec(findByEntityId.getIecNo());
				
//					import2.setChaCde(chaCode);
////					import2.setChaName(getdatabyParty.getImpCHA());
////								
////					import2.setConsoleName(getdatabyParty.getImpConsole());

//					DefaultPartyDetails getdatabyParty = DefaultParyDetailsRepository.getdatabyuser_id(compid, branchId,import2.getImporterId());

				if (getdatabyParty == null) {
					import2.setChaName("EU0021");
//						import2.setConsoleName("EU0009");	
					import2.setChaCde("");

				} else {
					import2.setChaName(getdatabyParty.getImpCHA());
//						import2.setConsoleName(getdatabyParty.getImpConsole());
					import2.setChaCde("");
				}

				import2.setNsdlStatus(requestStatus);
				importService.addImport(import2);
				return ResponseEntity.ok("Bill of Entry of SirNo " + import2.getSirNo() + " Update Successfully");
			} else {
				return ResponseEntity.ok("Data Not Found");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("Error extracting data from the provided URL");
		}

	}

//		 Update Paersonal Carriage

	@GetMapping("/{compid}/{branchId}/{user}/updatePersonalCarraige")
	public ResponseEntity<?> updatePersonalCarraige(@PathVariable("compid") String compid,
			@PathVariable("branchId") String branchId, @PathVariable("user") String User,
			@RequestParam("url") String url) {

		try {
//	             System.out.println("URL  "+url);
			RestTemplate restTemplate = new RestTemplate();
			String htmlContent = restTemplate.getForObject(url, String.class);

			// Parse the HTML string using Jsoup
			Document doc = Jsoup.parse(htmlContent);

//					// Extract data from specific HTML elements using CSS selectors
//					String dcName = doc.select("#lblDCName").text();
//					String sezName = doc.select("#lblSEZName").text();
//					String entityName = doc.select("#lblEntityName").text();
//					String importExportCode = doc.select("#lblImportExportCode").text();
			String entityID = doc.select("#lblEntityID").text();
			//
//					// Extract additional fields as needed
//					String requestDetails = doc.select("#lblRequestDetails").text();
			String requestID = doc.select("#lblRequestID").text();
//					String portCode = doc.select("#lblBOEPortCode").text();
//					String portOfOrigin = doc.select("#lblBOEPortOrgn").text();
//					String countryOfOrigin = doc.select("#lblBOECntryOrgn").text();
//					String importDeptDetails = doc.select("#lblBEThokaNo").text();
			String chaDetails = doc.select("#lblCHADetails").text();
//					String assessmentDate = doc.select("#lblAssesmentDate").text();
			String requestStatus = doc.select("#lblRequestStatus").text();
			BigDecimal assessableValue =  new BigDecimal(doc.select("#lblAssessableValue").text());

			// Split the extracted text into chaName and chaCode using the '-' sign
			String[] parts5 = chaDetails.split(" - ");
			String chaName = parts5[0];
			String chaCode = parts5.length > 1 ? parts5[1] : "";

			Element beElement = doc.select("#lblBEThokaNo").first();
			String beText = beElement.text();

			// Split the extracted text into BeNumber and BeDate
			String[] values = beText.split(", ");
			String beNumber = values[0];
			String beDate = values[1];

			// Extract data from the table
			// Extract data from the table
			Element table = doc.select("table#gvConsigneeDetails").first();
			Elements rows = table.select("tr");

			String igmNoDate = "";
			String mawbNoDate = "";
			String hawbNoDate = "";
			String weight = "";
			String packets = "";
			String packageMarksNumbers = "";
			String noOfContainers = "";

			if (rows.size() >= 2) { // Check if there are at least two rows (header row and data row)
				Element dataRow = rows.get(1); // Assuming the data row is the second row (index 1)
				Elements columns = dataRow.select("td");

				if (columns.size() >= 7) { // Check if there are at least seven columns in the data row
					igmNoDate = columns.get(0).text();
					mawbNoDate = columns.get(1).text();
					hawbNoDate = columns.get(2).text();
					weight = columns.get(3).text();
					packets = columns.get(4).text();
					packageMarksNumbers = columns.get(5).text();
					noOfContainers = columns.get(6).text();
				}
			}

			String[] parts = mawbNoDate.split(" ");

			String mawbNo = "";
			String mawbDate = "";

//					if (parts.length >= 2) {
//					    int firstSpaceIndex = mawbNoDate.indexOf(" ");
//					    int secondSpaceIndex = mawbNoDate.indexOf(" ", firstSpaceIndex + 1);
//
//					    if (secondSpaceIndex != -1) {
//					        mawbNo = mawbNoDate.substring(0, secondSpaceIndex);
//					        mawbDate = mawbNoDate.substring(secondSpaceIndex + 1);
//					    } else {
//					        mawbNo = parts[0];
//					        mawbDate = parts[1];
//					    }
//					}

			if (parts.length >= 2) {

				Pattern pattern = Pattern.compile("\\b\\d{2}/\\d{2}/\\d{4}\\b"); // Matches date in the format
																					// dd/MM/yyyy
				Matcher matcher = pattern.matcher(mawbNoDate);

				if (matcher.find()) {
					mawbDate = matcher.group(); // Get the found date
					mawbNo = mawbNoDate.substring(0, matcher.start()).trim(); // Get text before the date
				}

			}

			String[] parts2 = hawbNoDate.split(" ");

			String HawbNo = "";
			String HawbDate = "";

			if (parts2.length >= 2) {
				HawbNo = parts2[0]; // "3111795912"
				HawbDate = parts2[1]; // "22/08/2023"
			}

//					String[] parts3 = igmNoDate.split(" ");
//
//					String igmNo = "";
//					String igmDate = "";

//					if (parts3.length >= 2) {
//						igmNo = parts3[0]; // "3111795912"
//						igmDate = parts3[1]; // "22/08/2023"
//					}
			// Create a JSON object to store all extracted fields
//					String extractedData = "{" + "\"dcName\":\"" + dcName + "\"," + "\"sezName\":\"" + sezName + "\","
//							+ "\"entityName\":\"" + entityName + "\"," + "\"importExportCode\":\"" + importExportCode + "\","
//							+ "\"entityID\":\"" + entityID + "\"," + "\"requestDetails\":\"" + requestDetails + "\","
//							+ "\"requestID\":\"" + requestID + "\"," + "\"portCode\":\"" + portCode + "\","
//							+ "\"portOfOrigin\":\"" + portOfOrigin + "\"," + "\"countryOfOrigin\":\"" + countryOfOrigin + "\","
//							+ "\"importDeptDetails\":\"" + importDeptDetails + "\"," + "\"chaDetails\":\"" + chaDetails + "\","
//							+ "\"assessmentDate\":\"" + assessmentDate + "\"," + "\"requestStatus\":\"" + requestStatus + "\","
//							+ "\"assessableValue\":\"" + assessableValue + "\","
////		                 + "\"igmNoDate\":\"" + igmNoDate + "\","
////		                 + "\"mawbNoDate\":\"" + mawbNoDate + "\","
//							+ "\"mawbNo\":\"" + mawbNo + "\"," + "\"mawbDate\":\"" + mawbDate + "\"," + "\"HawbNo\":\"" + HawbNo
//							+ "\"," + "\"HawbDate\":\"" + HawbDate + "\","
////		                 + "\"hawbNoDate\":\"" + hawbNoDate + "\","
//							+ "\"igmNo\":\"" + igmNo + "\"," + "\"igmDate\":\"" + igmDate + "\"," + "\"weight\":\"" + weight
//							+ "\"," + "\"packets\":\"" + packets + "\"," + "\"packageMarksNumbers\":\"" + packageMarksNumbers
//							+ "\"," + "\"noOfContainers\":\"" + noOfContainers + "\"" + "}";

//					System.out.println("comapny "+compid +" branch "+branchId + "detention "+mawbNo);

//			System.out.println("detention " + mawbNo);
			
			
			

			Import import2 = importService.findForPersonalCarriage(compid, branchId, mawbNo);
			String findByEntityId = PartyService.findByEntityIdPartyId(compid, branchId, entityID);

//					System.out.println("import2    "+import2);
//					
//					System.out.println("Party Id  "+findByEntityId);

			DefaultPartyDetails getdatabyParty = DefaultParyDetailsRepository.getdatabyuser_id(compid, branchId,
					findByEntityId);

//			System.out.println(import2);
			if (import2 != null) {
				import2.setBeRequestId(requestID);
//						import2.setMawb(mawbNo);					
//						import2.setHawb(mawbNo);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				import2.setBeNo(beNumber);
				import2.setBeDate(dateFormat.parse(beDate));
				import2.setAssessableValue(assessableValue);
				
				if (getdatabyParty == null) {
					import2.setChaName("EU0021");
					import2.setConsoleName("EU0009");	
					import2.setChaCde("");

				} else {
					import2.setChaName(getdatabyParty.getImpCHA());
					import2.setConsoleName(getdatabyParty.getImpConsole());
					import2.setChaCde("");
				}
				
				
				
//				import2.setChaCde(chaCode);
//				import2.setChaName(getdatabyParty.getImpCHA());
//				import2.setConsoleName(getdatabyParty.getImpConsole());
				import2.setNsdlStatus(requestStatus);
				importService.addImport(import2);
				return ResponseEntity.ok("Import Update Successfully");
			} else {
				return ResponseEntity.ok("Data Not Found");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("Error extracting data from the provided URL");
		}

	}

	@GetMapping("/alltp/{cid}/{bid}/{date}")
	public List<String> getalltp(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

		List<String> tp = importRepo.getalltp(cid, bid, date);
		return tp;
	}

	@GetMapping("/searchforparty/{importer}")
	public List<Import> searchImports1(@RequestParam(name = "pcStatus", required = false) String pcStatus,
			@RequestParam(name = "scStatus", required = false) String scStatus,
			@RequestParam(name = "searchValue", required = false) String searchValue,
			@RequestParam(name = "companyid", required = false) String companyid,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "holdStatus", required = false) String holdStatus,
			@RequestParam(name = "hpStatus", required = false) String hpStatus,
			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@PathVariable("importer") String importer) {

//					System.out.println("searchValue "+searchValue);

		System.out.println(importer);
		return importRepo.findByAttributes1(companyid, branchId, pcStatus, scStatus, hpStatus, holdStatus, dgdcStatus,
				startDate, endDate, searchValue, importer);
	}

	@GetMapping("/searchforcartingagent/{importer}")
	public List<Import> searchImports2(@RequestParam(name = "pcStatus", required = false) String pcStatus,
			@RequestParam(name = "scStatus", required = false) String scStatus,
			@RequestParam(name = "searchValue", required = false) String searchValue,
			@RequestParam(name = "companyid", required = false) String companyid,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "holdStatus", required = false) String holdStatus,
			@RequestParam(name = "hpStatus", required = false) String hpStatus,
			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@PathVariable("importer") String importer) {

//					System.out.println("searchValue "+searchValue);

		System.out.println(importer);
		return importRepo.findByAttributes2(companyid, branchId, pcStatus, scStatus, hpStatus, holdStatus, dgdcStatus,
				startDate, endDate, searchValue, importer);
	}

	@GetMapping("/searchforcha/{importer}")
	public List<Import> searchImports3(@RequestParam(name = "pcStatus", required = false) String pcStatus,
			@RequestParam(name = "scStatus", required = false) String scStatus,
			@RequestParam(name = "searchValue", required = false) String searchValue,
			@RequestParam(name = "companyid", required = false) String companyid,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "holdStatus", required = false) String holdStatus,
			@RequestParam(name = "hpStatus", required = false) String hpStatus,
			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@PathVariable("importer") String importer) {

//					System.out.println("searchValue "+searchValue);

		System.out.println(importer);
		return importRepo.findByAttributes3(companyid, branchId, pcStatus, scStatus, hpStatus, holdStatus, dgdcStatus,
				startDate, endDate, searchValue, importer);
	}

	@GetMapping("/searchforconsole/{importer}")
	public List<Import> searchImports4(@RequestParam(name = "pcStatus", required = false) String pcStatus,
			@RequestParam(name = "scStatus", required = false) String scStatus,
			@RequestParam(name = "searchValue", required = false) String searchValue,
			@RequestParam(name = "companyid", required = false) String companyid,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "holdStatus", required = false) String holdStatus,
			@RequestParam(name = "hpStatus", required = false) String hpStatus,
			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@PathVariable("importer") String importer) {

//					System.out.println("searchValue "+searchValue);

		System.out.println(importer);
		return importRepo.findByAttributes4(companyid, branchId, pcStatus, scStatus, hpStatus, holdStatus, dgdcStatus,
				startDate, endDate, searchValue, importer);
	}

//				MOP Get Pass
	@GetMapping("/{compId}/{branchId}/{searchValue}/getDataForMopGetPass")
	public ResponseEntity<?> getDataForMopGetPass(@PathVariable("searchValue") String searchValue,
			@PathVariable("compId") String compId, @PathVariable("branchId") String branchId) {
		Object responseObject = null;

		if (searchValue != null) {
			searchValue = searchValue.trim(); // Trim leading and trailing spaces
		}

		if (searchValue.startsWith("IM")) {
			String value = searchValue.substring(0, 8);
			Import import1 = importRepo.findByCompanyIdAndBranchIdAndSirNoAndDgdcStatusAndStatusNotAndMopStatusNot(
					compId, branchId, value);
			responseObject = mapImportData(import1);
		} else if (searchValue.startsWith("EX")) {
			String value = searchValue.substring(0, 8);
			Export export1 = exportrepo.findByCompanyIdAndBranchIdAndSerNoAndDgdcStatusAndStatusNotAndMopStatusNot(
					compId, branchId, value);
			responseObject = mapExportData(export1);
		} else if (searchValue.startsWith("D-IM")) {
			String value = searchValue.substring(0, 10);
			ImportSub importSub1 = impsubRepo
					.findByCompanyIdAndBranchIdAndSirNoAndAndDgdcStatusAndStatusNotAndMopStatusNot(compId, branchId,
							value);
			responseObject = mapImportSubData(importSub1);
		} else if (searchValue.startsWith("D-EX")) {
			String value = searchValue.substring(0, 10);
			ExportSub exportSub1 = expsubRepo
					.findByCompanyIdAndBranchIdAndSerNoAndAndDgdcStatusAndStatusNotAndMopStatusNot(compId, branchId,
							value);
			responseObject = mapExportSubData(exportSub1);
		}

		return ResponseEntity.ok(responseObject);
	}

	// Mapping function for Import
	private Map<String, Object> mapImportData(Import importData) {

//					PartyService.findByPartyNameByID(importData.getImporterId());
		if (importData != null) {
			Map<String, Object> mappedData = new HashMap<>();
			mappedData.put("sirNo", importData.getSirNo());
			
			 if ("Y".equals(importData.getNiptStatus())) {
				 mappedData.put("hawb", "0000".equals(importData.getHawb()) ? importData.getMawb() : importData.getHawb());
			    } else {
			    	mappedData.put("hawb", "0000".equals(importData.getHawb()) ? importData.getBeRequestId() : importData.getHawb());
			    }
			
//			mappedData.put("hawb", "0000".equals(importData.getHawb()) ? importData.getBeRequestId() : importData.getHawb());
			mappedData.put("importerId", PartyService.findByPartyNameByID(importData.getImporterId()));
			mappedData.put("nop", importData.getNop());
			return mappedData;
		}
		return null;
	}

	// Mapping function for Export
	private Map<String, Object> mapExportData(Export exportData) {
		if (exportData != null) {
			Map<String, Object> mappedData = new HashMap<>();
			mappedData.put("sirNo", exportData.getSerNo());
			mappedData.put("hawb", exportData.getSbNo());
			mappedData.put("importerId", PartyService.findByPartyNameByID(exportData.getNameOfExporter()));
			mappedData.put("nop", exportData.getNoOfPackages());
			return mappedData;
		}
		return null;
	}

	// Mapping function for ImportSub
	private Map<String, Object> mapImportSubData(ImportSub importSubData) {
		if (importSubData != null) {
			Map<String, Object> mappedData = new HashMap<>();
			mappedData.put("sirNo", importSubData.getSirNo());
			mappedData.put("hawb", importSubData.getRequestId());
			mappedData.put("importerId", PartyService.findByPartyNameByID(importSubData.getExporter()));
			mappedData.put("nop", importSubData.getNop());
			return mappedData;
		}
		return null;
	}

	// Mapping function for ExportSub
	private Map<String, Object> mapExportSubData(ExportSub exportSubData) {
		if (exportSubData != null) {
			Map<String, Object> mappedData = new HashMap<>();
			mappedData.put("sirNo", exportSubData.getSerNo());
			mappedData.put("hawb", exportSubData.getRequestId());
			mappedData.put("importerId", PartyService.findByPartyNameByID(exportSubData.getExporter()));
			mappedData.put("nop", exportSubData.getNop());
			return mappedData;
		}
		return null;
	}

	@PostMapping("/{type}/generateMopGetPass")
	public ResponseEntity<?> receiveData(@PathVariable("type") String type,
			@RequestBody Map<String, Object> requestData) throws DocumentException {
		// Process the received data here
		// You can access the values using keys, for example:
		System.out.println("requestData " + requestData);
		List<Object> mopData = (List<Object>) requestData.get("MopData");
		String from = (String) requestData.get("from");
		String to = (String) requestData.get("to");
		String chaName = (String) requestData.get("chaName");
		String rePresentativeName = (String) requestData.get("rePresentativeName");

		int TotalNop = (Integer) requestData.get("nopSum");

		System.out.println("TYPE " + type);
		System.out.println("TotalNop " + TotalNop);
		System.out.println(mopData);

		Context context = new Context();
		context.setVariable("getPassData", mopData);
		context.setVariable("from", from);
		context.setVariable("TotalNop", TotalNop);
		context.setVariable("to", to);
		context.setVariable("chaName", chaName);
		context.setVariable("rePresentativeName", rePresentativeName);
//			        context.setVariable("getPassData",mopData );
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

		context.setVariable("currentDate", dateFormat2.format(new Date()));
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String time = timeFormat.format(new Date());
		context.setVariable("currentTime", time);

		String htmlContent = templateEngine.process("MOP_Get_Pass", context);
//					if ("PDF".equals(type)) {
		// If the type is PDF, generate PDF and return its base URL
		String mopid = proccessNextIdService.autoIncrementMOPGatePassId();
		MOPpass mop = new MOPpass();
		mop.setMopId(mopid);
		mop.setCreatedDate(new Date());
		mop.setToDgdc(to);
		mop.setFromDgdc(from);
		mop.setNameOfCHA(chaName);
		mop.setReciverName(rePresentativeName);
		mop.setTime(time);
		mopgatepassrepo.save(mop);

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

		// Return PDF base URL

		for (Object data : mopData) {
			if (data instanceof Map) {
				Map<?, ?> dataMap = (Map<?, ?>) data;
				// Retrieve and print the value corresponding to the "sirNo" key
				Object sirNoObject = dataMap.get("sirNo");

				if (sirNoObject instanceof String) {
					String sirNoValue = (String) sirNoObject;

					if (sirNoValue.startsWith("IM")) {
						String value = sirNoValue.substring(0, 8);
						System.out.println("requestData " + requestData);
						System.out.println("sirNoValue " + value);
//						            	System.out.println("Im   ");
						Import findBySirNo = importRepo.findBySirNo(sirNoValue);
						findBySirNo.setMopStatus("Y");
						findBySirNo.setMopId(mopid);
						importRepo.save(findBySirNo);
						// Handle the import1 object as needed
					} else if (sirNoValue.startsWith("EX")) {
//						            	System.out.println("EX   ");
						String value = sirNoValue.substring(0, 8);
						Export findBySerNo = exportrepo.findBySerNo(sirNoValue);
						findBySerNo.setMopStatus("Y");
						findBySerNo.setMopId(mopid);
						exportrepo.save(findBySerNo);
						// Handle the export1 object as needed
					} else if (sirNoValue.startsWith("D-IM")) {
//						            	System.out.println("D-IM   ");
						String value = sirNoValue.substring(0, 10);
						ImportSub findBySirNo = impsubRepo.findBySirNo(sirNoValue);
						findBySirNo.setMopStatus("Y");
						findBySirNo.setMopId(mopid);
						impsubRepo.save(findBySirNo);
					} else if (sirNoValue.startsWith("D-EX")) {
//						            	System.out.println("D-EX   ");
						String value = sirNoValue.substring(0, 10);
						ExportSub findBySerNo = expsubRepo.findBySerNo(sirNoValue);
						findBySerNo.setMopStatus("Y");
						findBySerNo.setMopId(mopid);
						expsubRepo.save(findBySerNo);
					}
				}
			} else {
				System.out.println("Not a Map");
			}
		}

		return ResponseEntity.ok(base64Pdf);

	}

	@GetMapping("/checkMOPStatus/{cid}/{bid}/{id}")
	public String checkMOPStatus(@PathVariable("cid") String cid, @PathVariable("bid") String bid,
			@PathVariable("id") String id) {
		if (id.startsWith("IM")) {
			String value = id.substring(0, 8);
			Import findBySirNo = importRepo.findBySirNo(value);
			if (findBySirNo == null) {
				return "not found";
			} else if ("Y".equals(findBySirNo.getMopStatus())) {
				findBySirNo.setMopStatus("R");
				importRepo.save(findBySirNo);
				return findBySirNo.getMopStatus();

			} else if ("R".equals(findBySirNo.getMopStatus())) {

				return "already scan";

			} else {
				return "status not match";
			}
		} else if (id.startsWith("EX")) {
			String value = id.substring(0, 8);
			Export findBySerNo = exportrepo.findBySerNo(value);
			if (findBySerNo == null) {
				return "not found";
			} else if ("Y".equals(findBySerNo.getMopStatus())) {
				findBySerNo.setMopStatus("R");
				exportrepo.save(findBySerNo);
				return findBySerNo.getMopStatus();

			} else if ("R".equals(findBySerNo.getMopStatus())) {

				return "already scan";

			} else {
				return "status not match";
			}
		} else if (id.startsWith("D-EX")) {
			String value = id.substring(0, 10);
			ExportSub findBySerNo = expsubRepo.findBySerNo(value);
			if (findBySerNo == null) {
				return "not found";
			} else if ("Y".equals(findBySerNo.getMopStatus())) {
				findBySerNo.setMopStatus("R");
				expsubRepo.save(findBySerNo);
				return findBySerNo.getMopStatus();

			} else if ("R".equals(findBySerNo.getMopStatus())) {

				return "already scan";

			} else {
				return "status not match";
			}
		} else if (id.startsWith("D-IM")) {
			String value = id.substring(0, 10);
			ImportSub findBySirNo = impsubRepo.findBySirNo(value);
			if (findBySirNo == null) {
				return "not found";
			} else if ("Y".equals(findBySirNo.getMopStatus())) {
				findBySirNo.setMopStatus("R");
				impsubRepo.save(findBySirNo);
				return findBySirNo.getMopStatus();

			} else if ("R".equals(findBySirNo.getMopStatus())) {

				return "already scan";

			} else {
				return "status not match";
			}
		} else {
			return "barcode not found";
		}
	}

	

	
	
	
//	@PostMapping("/common/printgatepass/{compId}/{branchId}/{type}")
//
//	public ResponseEntity<String> generateCommonGatePassPdf(@PathVariable("type") String type,
//
//	        @RequestBody Map<String, Object> requestData, @PathVariable("compId") String compId,
//
//	        @PathVariable("branchId") String branchId) throws Exception {
//
//	    try {
//
//
//
//	        Context context = new Context();
//
//	        List<Object> getPassData = (List<Object>) requestData.get("selectedItems");
//
//	        List<Object> getPassData1 = new ArrayList<Object>();
//
//	        int TotalNop = (Integer) requestData.get("nopSum");
//
//
//
//	        String nextMailId = processNextIdRepository.findNextCommonGatePassId();
//
//
//
//	        int lastNextNumericId = Integer.parseInt(nextMailId.substring(3));
//
//
//
//	        int nextNumericNextID = lastNextNumericId + 1;
//
//
//
//	        String MailId = String.format("COM%05d", nextNumericNextID);
//
//	        // Update the Next_Id directly in the database using the repository
//
//
//
//	        String chaName = (String) requestData.get("paryCHAId");
//
//	        String rePresentativeName = (String) requestData.get("representativeId");
//
//
//
//	        String partyName = "";
//
//
//
//	        if (chaName != null && !chaName.isEmpty() && chaName.charAt(0) == 'M') {
//
//	            Party party1 = partyrepo.findByPartyId(chaName);
//
//	            partyName = party1.getPartyName();
//
//
//
//	        } else if (chaName != null && !chaName.isEmpty() && chaName.charAt(0) == 'E') {
//
//
//
//	            ExternalParty singleRecord = ExternalParty_Service.getSingleRecord(compId, branchId, chaName);
//
//	            partyName = singleRecord.getUserName();
//
//
//
//	        }
//
//
//
//	        String Represent = "";
//
//	        RepresentParty findByFNameAndLName = representRepo.findByFNameAndLName(compId, branchId,
//
//	                rePresentativeName);
//
//	        {
//
//	            Represent = findByFNameAndLName.getFirstName() + " " + findByFNameAndLName.getLastName();
//
//	        }
//
//
//
//	        String status = "N";
//
//
//
//	        for (Object data : getPassData) {
//
//	            if (data instanceof Map) {
//
//	                Map<?, ?> dataMap = (Map<?, ?>) data;
//
//	                Object sirNoObject = dataMap.get("sirNo");
//
//	                if (sirNoObject instanceof String) {
//
//	                    String sirNoValue = (String) sirNoObject;
//
//	                    if (sirNoValue.startsWith("IM")) {
//
//
//
//	                        Import findBySirNo = importRepo.findByCompanyIdAndBranchIdAndSirNo(compId, branchId,
//
//	                                sirNoValue);
//
//	                        if ("Handed over to Party/CHA".equals(findBySirNo.getDGDC_Status())
//
//	                                && !"Y".equals(findBySirNo.getGatePassStatus())) {
//
//	                            findBySirNo.setGatePassStatus("Y");
//
//	                            findBySirNo.setCommonGatePassId(MailId);
//
//	                            importRepo.save(findBySirNo);
//
//	                            getPassData1.add(data);
//
//	                            processNextIdRepository.updateNextCommonGatePassId(MailId);
//
//	                            status = "Y";
//
//	                        }
//
//
//
//	                        // Handle the import1 object as needed
//
//	                    } else if (sirNoValue.startsWith("D-IM")) {
//
//
//
//	                        ImportSub findBySirNo = impsubRepo.findByCompanyIdAndBranchIdAndSirNo(compId, branchId,
//
//	                                sirNoValue);
//
//	                        if ("Handed over to Party/CHA".equals(findBySirNo.getDgdcStatus())
//
//	                                && !"Y".equals(findBySirNo.getGatePassStatus())) {
//
//	                            findBySirNo.setGatePassStatus("Y");
//
//	                            findBySirNo.setCommonGatePassId(MailId);
//
//	                            impsubRepo.save(findBySirNo);
//
//	                            getPassData1.add(data);
//
//	                            processNextIdRepository.updateNextCommonGatePassId(MailId);
//
//	                            status = "Y";
//
//	                        }
//
//	                    } else if (sirNoValue.startsWith("D-EX")) {
//
//
//
//	                        ExportSub findBySerNo = expsubRepo.findByCompanyIdAndBranchIdAndSerNo(compId, branchId,
//
//	                                sirNoValue);
//
//	                        if ("Handed over to Party/CHA".equals(findBySerNo.getDgdcStatus())
//
//	                                && !"Y".equals(findBySerNo.getGatePassStatus())) {
//
//	                            findBySerNo.setGatePassStatus("Y");
//
//	                            findBySerNo.setCommonGatePassId(MailId);
//
//	                            expsubRepo.save(findBySerNo);
//
//	                            getPassData1.add(data);
//
//	                            processNextIdRepository.updateNextCommonGatePassId(MailId);
//
//	                            status = "Y";
//
//	                        }
//
//	                    }
//
//	                }
//
//	            } else {
//
//	                System.out.println("Not a Map");
//
//	            }
//
//
//
//	        }
//
//	        if ("N".equals(status)) {
//
//	            return ResponseEntity.ok().body("not generated");
//
//	        }
//
//
//
//	        // Get the current time in milliseconds
//
//	        long currentTimeMillis = System.currentTimeMillis();
//
//
//
//	        // Create a Date object using the current time
//
//	        Date currentDate = new Date(currentTimeMillis);
//
//
//
//	        // Create a SimpleDateFormat object for formatting the time
//
//	        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//
//
//
//	        // Format the current time
//
//	        String formattedTime = sdf.format(currentDate);
//
//
//
//	        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
//
//	        Date d1 = new Date();
//
//	        context.setVariable("Date", dateFormat2.format(d1));
//
//	        context.setVariable("getPassData", getPassData1);
//
//	        context.setVariable("TotalNop", TotalNop);
//
//	        context.setVariable("formattedTime", formattedTime);
//
//	        context.setVariable("partyName", partyName);
//
//	        context.setVariable("Represent", Represent);
//
//	        context.setVariable("gatepassId", MailId);
//
//	        if ("Y".equals(status)) {
//
//	            CommonGatePass commongatepass = new CommonGatePass();
//
//	            commongatepass.setCommonId(MailId);
//
//	            commongatepass.setNameOfPartyCha(partyName);
//
//	            commongatepass.setReceiverName(Represent);
//
//	            commongatepass.setDeliveryDate(currentDate);
//
//	            commongatepass.setDeliveryTime(formattedTime);
//
//
//
//	            commongatepassrepo.save(commongatepass);
//
//	        }
//
//
//
//	        String htmlContent = templateEngine.process("CommonGatePass", context);
//
//
//
//	        // Create an ITextRenderer instance
//
//	        ITextRenderer renderer = new ITextRenderer();
//
//
//
//	        // Set the PDF page size and margins
//
//	        renderer.setDocumentFromString(htmlContent);
//
//	        renderer.layout();
//
//
//
//	        // Generate PDF content
//
//	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//	        renderer.createPDF(outputStream);
//
//
//
//	        // Get the PDF bytes
//
//	        byte[] pdfBytes = outputStream.toByteArray();
//
//
//
//	        // Encode the PDF content as Base64
//
//	        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
//
//
//
//	        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//
//	                .body(base64Pdf);
//
////	            return null;
//
//	    } catch (Exception e) {
//
//	        // Handle exceptions appropriately
//
//	        return ResponseEntity.badRequest().body("Error generating PDF");
//
//	    }
//
//	}
	
	
	@PostMapping("/common/printgatepass/{compId}/{branchId}/{type}")

	public ResponseEntity<String> generateCommonGatePassPdf(@PathVariable("type") String type,

			@RequestBody Map<String, Object> requestData, @PathVariable("compId") String compId,

			@PathVariable("branchId") String branchId) throws Exception {

		try {

			Context context = new Context();

			List<Object> getPassData = (List<Object>) requestData.get("selectedItems");

			List<Object> getPassData1 = new ArrayList<Object>();

			int TotalNop = (Integer) requestData.get("nopSum");

			String nextMailId = processNextIdRepository.findNextCommonGatePassId();

			int lastNextNumericId = Integer.parseInt(nextMailId.substring(3));

			int nextNumericNextID = lastNextNumericId + 1;

			String MailId = String.format("COM%05d", nextNumericNextID);

			// Update the Next_Id directly in the database using the repository

			String chaName = (String) requestData.get("paryCHAId");

			String rePresentativeName = (String) requestData.get("representativeId");

			String partyName = "";

			if (chaName != null && !chaName.isEmpty() && chaName.charAt(0) == 'M') {

				Party party1 = partyrepo.findByPartyId(chaName);

				partyName = party1.getPartyName();

			} else if (chaName != null && !chaName.isEmpty() && chaName.charAt(0) == 'E') {

				ExternalParty singleRecord = ExternalParty_Service.getSingleRecord(compId, branchId, chaName);

				partyName = singleRecord.getUserName();

			}

			String Represent = "";
			String imagePath = "";

			RepresentParty findByFNameAndLName = representRepo.findByFNameAndLName(compId, branchId,

					rePresentativeName);

			if (findByFNameAndLName != null) {
				Represent = findByFNameAndLName.getFirstName() + " " + findByFNameAndLName.getLastName();

				imagePath = ImageHelper.getRepresentativeImage(findByFNameAndLName);
				
				
			}

			String status = "N";

			for (Object data : getPassData) {

				if (data instanceof Map) {

					Map<?, ?> dataMap = (Map<?, ?>) data;

					Object sirNoObject = dataMap.get("sirNo");

					if (sirNoObject instanceof String) {

						String sirNoValue = (String) sirNoObject;

						if (sirNoValue.startsWith("IM")) {

							Import findBySirNo = importRepo.findByCompanyIdAndBranchIdAndSirNo(compId, branchId,

									sirNoValue);

							if ("Handed over to Party/CHA".equals(findBySirNo.getDGDC_Status())

									&& !"Y".equals(findBySirNo.getGatePassStatus())) {

								findBySirNo.setGatePassStatus("Y");

								findBySirNo.setCommonGatePassId(MailId);

								importRepo.save(findBySirNo);

								getPassData1.add(data);

								processNextIdRepository.updateNextCommonGatePassId(MailId);

								status = "Y";

							}

							// Handle the import1 object as needed

						} else if (sirNoValue.startsWith("D-IM")) {

							ImportSub findBySirNo = impsubRepo.findByCompanyIdAndBranchIdAndSirNo(compId, branchId,

									sirNoValue);

							if ("Handed over to Party/CHA".equals(findBySirNo.getDgdcStatus())

									&& !"Y".equals(findBySirNo.getGatePassStatus())) {

								findBySirNo.setGatePassStatus("Y");

								findBySirNo.setCommonGatePassId(MailId);

								impsubRepo.save(findBySirNo);

								getPassData1.add(data);

								processNextIdRepository.updateNextCommonGatePassId(MailId);

								status = "Y";

							}

						} else if (sirNoValue.startsWith("D-EX")) {

							ExportSub findBySerNo = expsubRepo.findByCompanyIdAndBranchIdAndSerNo(compId, branchId,

									sirNoValue);

							if ("Handed over to Party/CHA".equals(findBySerNo.getDgdcStatus())

									&& !"Y".equals(findBySerNo.getGatePassStatus())) {

								findBySerNo.setGatePassStatus("Y");

								findBySerNo.setCommonGatePassId(MailId);

								expsubRepo.save(findBySerNo);

								getPassData1.add(data);

								processNextIdRepository.updateNextCommonGatePassId(MailId);

								status = "Y";

							}

						}

					}

				} else {

					System.out.println("Not a Map");

				}

			}

			if ("N".equals(status)) {

				return ResponseEntity.ok().body("not generated");

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

			context.setVariable("getPassData", getPassData1);

			context.setVariable("TotalNop", TotalNop);

			context.setVariable("formattedTime", formattedTime);

			context.setVariable("partyName", partyName);

			context.setVariable("Represent", Represent);
			
			context.setVariable("imagePath", imagePath);
			
			context.setVariable("gatepassId", MailId);

			if ("Y".equals(status)) {

				CommonGatePass commongatepass = new CommonGatePass();

				commongatepass.setCommonId(MailId);
				commongatepass.setCompanyId(compId);
				commongatepass.setBranchId(branchId);

				commongatepass.setNameOfPartyCha(chaName);

				commongatepass.setReceiverName(findByFNameAndLName.getRepresentativeId());

				commongatepass.setDeliveryDate(currentDate);

				commongatepass.setDeliveryTime(formattedTime);

				commongatepassrepo.save(commongatepass);

			}
			
			
//			Collections.sort(getPassData1, new Comparator<Map<String, String>>() {
//				@Override
//				public int compare(Map<String, String> map1, Map<String, String> map2) {
//					// Get the values associated with "sirNo" key from the maps
//					String sirNo1 = map1.get("sirNo");
//					String sirNo2 = map2.get("sirNo");
//
//					// Compare the values associated with "sirNo" key
//					return sirNo1.compareTo(sirNo2);
//				}
//			});

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

//	            return null;

		} catch (Exception e) {

			// Handle exceptions appropriately

			return ResponseEntity.badRequest().body("Error generating PDF");

		}

	}
	
	
	
	
	
	
	
	

	
	
	@PostMapping("/commongatepass/search/{compId}/{branchId}")
	public ResponseEntity<?> getdatabyserNo(@PathVariable("compId") String companyId,
			@PathVariable("branchId") String branchId,
			@RequestParam("formattedStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date formattedStartDate)
			throws Exception {
		try {
			// No need to parse the formattedStartDate, it's already a Date object
//System.out.println("formattedStartDate "+formattedStartDate);
			List<ExportSub> expsub = expsubRepo.findByCompanyAndBranchAndSerDateANDDGDC(companyId, branchId,
					formattedStartDate);

			List<ImportSub> impsub = impsubRepo.findByCompanyAndBranchAndDate3(companyId, branchId, formattedStartDate);

			List<Import> imp = importRepository.findByCompanyAndBranchAndDate2(companyId, branchId, formattedStartDate);

			List<Map<String, Object>> combinedList = new ArrayList<>();

			// Map and add data from ExportSub list
			List<Map<String, Object>> mappedExportSubList = mapExportSubList(expsub);
			combinedList.addAll(mappedExportSubList);

			// Map and add data from ImportSub list
			List<Map<String, Object>> mappedImportSubList = mapImportSubList(impsub);
			combinedList.addAll(mappedImportSubList);

			// Map and add data from Import list
			List<Map<String, Object>> mappedImportList = mapImportList(imp);
			combinedList.addAll(mappedImportList);

//			System.out.println("expsub: " + expsub);
//			System.out.println("imp: " + imp);

			// Check if the list is not empty before returning

			return ResponseEntity.ok().body(combinedList); // Return the list of Export objects

		} catch (Exception e) {
			// Handle exceptions appropriately
			return ResponseEntity.badRequest().body("Error combinedList ");
		}
	}
	
	

//		Common Get Pass

		private List<Map<String, Object>> mapImportList(List<Import> importList) {
			List<Map<String, Object>> mappedList = new ArrayList<>();
			for (Import importData : importList) {
				Map<String, Object> mappedData = mapImportData(importData);
				if (mappedData != null) {
					mappedList.add(mappedData);
				}
			}
			return mappedList;
		}

		private List<Map<String, Object>> mapExportSubList(List<ExportSub> exportSubList) {
			List<Map<String, Object>> mappedList = new ArrayList<>();
			for (ExportSub exportSubData : exportSubList) {
				Map<String, Object> mappedData = mapExportSubData(exportSubData);
				if (mappedData != null) {
					mappedList.add(mappedData);
				}
			}
			return mappedList;
		}

		private List<Map<String, Object>> mapImportSubList(List<ImportSub> importSubList) {
			List<Map<String, Object>> mappedList = new ArrayList<>();
			for (ImportSub importSubData : importSubList) {
				Map<String, Object> mappedData = mapImportSubData(importSubData);
				if (mappedData != null) {
					mappedList.add(mappedData);
				}
			}
			return mappedList;
		}
	
		@GetMapping("/searchbymopdate/{date}")
		public List<MOPpass> searchByMopDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date1) {

			List<MOPpass> moppass = mopgatepassrepo.findbydate(date1);

			return moppass;
		}

		@PostMapping("/generateMopGetPass1")
		public ResponseEntity<String> receiveData1(@RequestBody MOPpass pass) {
			try {
				// Process the received data here
				// You can access the values using keys, for example:

				List<Export> export1 = exportrepo.getbymoppassid(pass.getMopId());
				List<ExportSub> exportsub1 = expsubRepo.getbymoppassid(pass.getMopId());
				List<ImportSub> importsub1 = impsubRepo.getbymoppassid(pass.getMopId());
				List<Import> import1 = importRepo.getbymoppassid(pass.getMopId());
				// Add similar blocks for other lists...

				List<Map<String, String>> requestDataList = new ArrayList<>();

				if (export1 != null && !export1.isEmpty()) {
					for (Export exp : export1) {
						Map<String, String> requestData = new HashMap<>();
						Party party1 = partyrepo.findByPartyId(exp.getNameOfExporter());
						String nop1 = Integer.toString(exp.getNoOfPackages());
						requestData.put("sirNo", exp.getSerNo());
						requestData.put("hawb", exp.getSbRequestId());
						requestData.put("nop", nop1);
						requestData.put("importerId", party1.getPartyName());
						requestDataList.add(requestData);
					}
				}
				if (exportsub1 != null && !exportsub1.isEmpty()) {
					for (ExportSub exp : exportsub1) {
						Map<String, String> requestData = new HashMap<>();
						Party party1 = partyrepo.findByPartyId(exp.getExporter());
						String nop1 = Integer.toString(exp.getNop());
						requestData.put("sirNo", exp.getSerNo());
						requestData.put("hawb", exp.getRequestId());
						requestData.put("nop", nop1);
						requestData.put("importerId", party1.getPartyName());
						requestDataList.add(requestData);
					}
				}
				if (import1 != null && !import1.isEmpty()) {
					for (Import exp : import1) {
						Map<String, String> requestData = new HashMap<>();
						Party party1 = partyrepo.findByPartyId(exp.getImporterId());
						String nop1 = Integer.toString(exp.getNop());
						requestData.put("sirNo", exp.getSirNo());
						
						if(exp.getNiptStatus().equals("Y"))
						{
							requestData.put("hawb", "0000".equals(exp.getHawb()) ? exp.getMawb() : exp.getHawb());
							
						}else
						{
						requestData.put("hawb", "0000".equals(exp.getHawb()) ? exp.getBeRequestId() : exp.getHawb());
						}
						
						requestData.put("nop", nop1);
						requestData.put("importerId", party1.getPartyName());
						requestDataList.add(requestData);
					}
				}
				if (importsub1 != null && !importsub1.isEmpty()) {
					for (ImportSub exp : importsub1) {
						Map<String, String> requestData = new HashMap<>();
						Party party1 = partyrepo.findByPartyId(exp.getExporter());
						String nop1 = Integer.toString(exp.getNop());
						requestData.put("sirNo", exp.getSirNo());
						requestData.put("hawb", exp.getRequestId());
						requestData.put("nop", nop1);
						requestData.put("importerId", party1.getPartyName());
						requestDataList.add(requestData);
					}
				}

				int totalNop = requestDataList.size();

				// Set up Thymeleaf context
				Context context = new Context();
				context.setVariable("getPassData", requestDataList);
				context.setVariable("from", pass.getFromDgdc());
				context.setVariable("TotalNop", totalNop);
				context.setVariable("to", pass.getToDgdc());
				context.setVariable("chaName", pass.getNameOfCHA());
				context.setVariable("rePresentativeName", pass.getReciverName());
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
				context.setVariable("currentDate", dateFormat2.format(pass.getCreatedDate()));
				context.setVariable("currentTime", pass.getTime());

				// Process the HTML template with dynamic values
				String htmlContent = templateEngine.process("MOP_Get_Pass", context);

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

				// Return the Base64-encoded PDF
				return ResponseEntity.ok(base64Pdf);

			} catch (Exception e) {
				// Handle exceptions appropriately
				return ResponseEntity.badRequest().body("Error generating PDF");
			}
		}
		
		
//		@GetMapping("/getallcommondata/{date1}")
//		public List<CommonGatePass> getCommonDatabydate(@PathVariable("date1") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date1) {
//	           return commongatepassrepo.getbydate(date1);
//		}

		@GetMapping("/getallcommondata/{date1}/{comapnyId}/{branchId}")
		public List<Object[]> getCommonDatabydate(
				@PathVariable("comapnyId") String companyId,@PathVariable("branchId") String branchId,
				@PathVariable("date1") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date1) {
			return commongatepassrepo.getbydate(date1,companyId,branchId);
		}
		
		
//		@PostMapping("/common/printgatepass1/{compId}/{branchId}")
//
//		public ResponseEntity<String> generateCommonGatePassPdf1(@PathVariable("compId") String compId,
//
//				@PathVariable("branchId") String branchId, @RequestBody CommonGatePass common) throws Exception {
//
//			try {
//
//
//
//				List<ExportSub> exportsub1 = expsubRepo.getbycommonid(common.getCommonId());
//
//				List<Import> import1 = importRepo.getbycommonid(common.getCommonId());
//
//				List<ImportSub> importsub1 = impsubRepo.getbycommonid(common.getCommonId());
//
//
//
//				List<Map<String, String>> requestDataList = new ArrayList<>();
//
//				int totalNop = 0;
//
//				if (exportsub1 != null && !exportsub1.isEmpty()) {
//
//					for (ExportSub exp : exportsub1) {
//
//						totalNop += exp.getNop();
//
//						Map<String, String> requestData = new HashMap<>();
//
//						Party party1 = partyrepo.findByPartyId(exp.getExporter());
//
//						String nop1 = Integer.toString(exp.getNop());
//
//						requestData.put("sirNo", exp.getSerNo());
//
//						requestData.put("hawb", exp.getRequestId());
//
//						requestData.put("nop", nop1);
//
//						requestData.put("importerId", party1.getPartyName());
//
//						requestDataList.add(requestData);
//
//					}
//
//				}
//
//				if (import1 != null && !import1.isEmpty()) {
//
//					for (Import exp : import1) {
//
//						Map<String, String> requestData = new HashMap<>();
//
//						Party party1 = partyrepo.findByPartyId(exp.getImporterId());
//
//						totalNop += exp.getNop();
//
//						String nop1 = Integer.toString(exp.getNop());
//
//						requestData.put("sirNo", exp.getSirNo());
//						
//						
//						
//
////						requestData.put("hawb", "0000".equals(exp.getHawb()) ? exp.getBeRequestId() : exp.getHawb());
//						
//						if(exp.getNiptStatus().equals("Y"))
//						{
//							requestData.put("hawb", "0000".equals(exp.getHawb()) ? exp.getMawb() : exp.getHawb());
//							
//						}else
//						{
//						requestData.put("hawb", "0000".equals(exp.getHawb()) ? exp.getBeRequestId() : exp.getHawb());
//						}
//
//						requestData.put("nop", nop1);
//
//						requestData.put("importerId", party1.getPartyName());
//
//						requestDataList.add(requestData);
//
//					}
//
//				}
//
//				if (importsub1 != null && !importsub1.isEmpty()) {
//
//					for (ImportSub exp : importsub1) {
//
//						Map<String, String> requestData = new HashMap<>();
//
//						Party party1 = partyrepo.findByPartyId(exp.getExporter());
//
//						totalNop += exp.getNop();
//
//						String nop1 = Integer.toString(exp.getNop());
//
//						requestData.put("sirNo", exp.getSirNo());
//
//						requestData.put("hawb", exp.getRequestId());
//
//						requestData.put("nop", nop1);
//
//						requestData.put("importerId", party1.getPartyName());
//
//						requestDataList.add(requestData);
//
//					}
//
//				}
//
//
//
//			
//
//
//
//				Context context = new Context();
//
//
//
//		
//
//
//
//				// Create a SimpleDateFormat object for formatting the time
//
//				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//
//
//
//				
//
//
//
//				SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
//
//				context.setVariable("gatepassId", common.getCommonId());
//
//				context.setVariable("Date", dateFormat2.format(common.getDeliveryDate()));
//
//				context.setVariable("getPassData", requestDataList);
//
//				context.setVariable("TotalNop", totalNop);
//
//				context.setVariable("formattedTime", common.getDeliveryTime());
//
//				context.setVariable("partyName", common.getNameOfPartyCha());
//
//				context.setVariable("Represent", common.getReceiverName());
//
//
//
//				String htmlContent = templateEngine.process("CommonGatePass", context);
//
//
//
//				// Create an ITextRenderer instance
//
//				ITextRenderer renderer = new ITextRenderer();
//
//
//
//				// Set the PDF page size and margins
//
//				renderer.setDocumentFromString(htmlContent);
//
//				renderer.layout();
//
//
//
//				// Generate PDF content
//
//				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//				renderer.createPDF(outputStream);
//
//
//
//				// Get the PDF bytes
//
//				byte[] pdfBytes = outputStream.toByteArray();
//
//
//
//				// Encode the PDF content as Base64
//
//				String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
//
//
//
//				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//
//						.body(base64Pdf);
//
////					return null;
//
//			} catch (Exception e) {
//
//				// Handle exceptions appropriately
//
//				return ResponseEntity.badRequest().body("Error generating PDF");
//
//			}
//
//		}
//		
		
		@PostMapping("/common/printgatepass1/{compId}/{branchId}/{commonId}")

		public ResponseEntity<String> generateCommonGatePassPdf1(@PathVariable("compId") String compId,

				@PathVariable("branchId") String branchId, @PathVariable("commonId") String commonId) throws Exception {

			try {
				
						
				 CommonGatePass common = commongatepassrepo.findByCompanyIdAndBranchIdAndCommonId(compId,branchId,commonId);

				 
				List<ExportSub> exportsub1 = expsubRepo.getbycommonid(common.getCommonId());

				List<Import> import1 = importRepo.getbycommonid(common.getCommonId());

				List<ImportSub> importsub1 = impsubRepo.getbycommonid(common.getCommonId());

				List<Map<String, String>> requestDataList = new ArrayList<>();

				int totalNop = 0;
	
				
				if (exportsub1 != null && !exportsub1.isEmpty()) {

					for (ExportSub exp : exportsub1) {

						totalNop += exp.getNop();

						Map<String, String> requestData = new HashMap<>();

						Party party1 = partyrepo.findByPartyId(exp.getExporter());

						String nop1 = Integer.toString(exp.getNop());

						requestData.put("sirNo", exp.getSerNo());

						requestData.put("hawb", exp.getRequestId());

						requestData.put("nop", nop1);

						requestData.put("importerId", party1.getPartyName());

						requestDataList.add(requestData);

					}

				}

			
				if (import1 != null && !import1.isEmpty()) {

					for (Import exp : import1) {

						Map<String, String> requestData = new HashMap<>();

						Party party1 = partyrepo.findByPartyId(exp.getImporterId());

						totalNop += exp.getNop();

						String nop1 = Integer.toString(exp.getNop());

						requestData.put("sirNo", exp.getSirNo());

//						requestData.put("hawb", "0000".equals(exp.getHawb()) ? exp.getBeRequestId() : exp.getHawb());

						if(exp.getNiptStatus().equals("Y"))
							{
								requestData.put("hawb", "0000".equals(exp.getHawb()) ? exp.getMawb() : exp.getHawb());
								
							}else
							{
							requestData.put("hawb", "0000".equals(exp.getHawb()) ? exp.getBeRequestId() : exp.getHawb());
							}
						
						requestData.put("nop", nop1);

						requestData.put("importerId", party1.getPartyName());

						requestDataList.add(requestData);

					}

				}
		

				if (importsub1 != null && !importsub1.isEmpty()) {

					for (ImportSub exp : importsub1) {

						Map<String, String> requestData = new HashMap<>();

						Party party1 = partyrepo.findByPartyId(exp.getExporter());

						totalNop += exp.getNop();

						String nop1 = Integer.toString(exp.getNop());

						requestData.put("sirNo", exp.getSirNo());

						requestData.put("hawb", exp.getRequestId());

						requestData.put("nop", nop1);

						requestData.put("importerId", party1.getPartyName());

						requestDataList.add(requestData);

					}

				}
				
				
				String partyName = "";
				String chaName = common.getNameOfPartyCha();
				if (chaName != null && !chaName.isEmpty() && chaName.charAt(0) == 'M') {

					Party party1 = partyrepo.findByPartyId(chaName);

					partyName = party1.getPartyName();

				} else if (chaName != null && !chaName.isEmpty() && chaName.charAt(0) == 'E') {

					ExternalParty singleRecord = ExternalParty_Service.getSingleRecord(compId, branchId, chaName);

					partyName = singleRecord.getUserName();

				}

				String Represent = "";
				String imagePath = "";

				RepresentParty findByFNameAndLName = representRepo.findByFNameAndLName(compId, branchId,common.getReceiverName());
			
				if (findByFNameAndLName != null) {
					Represent = findByFNameAndLName.getFirstName() + " " + findByFNameAndLName.getLastName();

					imagePath = ImageHelper.getRepresentativeImage(findByFNameAndLName);
				
				}


				
				
				Context context = new Context();

				// Create a SimpleDateFormat object for formatting the time

				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

				SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

				context.setVariable("gatepassId", common.getCommonId());

				context.setVariable("Date", dateFormat2.format(common.getDeliveryDate()));

				context.setVariable("getPassData", requestDataList);

				context.setVariable("TotalNop", totalNop);

				context.setVariable("formattedTime", common.getDeliveryTime());

				context.setVariable("partyName",partyName );

				context.setVariable("Represent", Represent);
				context.setVariable("imagePath", imagePath);
				
				
//
//				Collections.sort(requestDataList, new Comparator<Map<String, String>>() {
//					@Override
//					public int compare(Map<String, String> map1, Map<String, String> map2) {
//						// Get the values associated with "sirNo" key from the maps
//						String sirNo1 = map1.get("sirNo");
//						String sirNo2 = map2.get("sirNo");
//
//						// Compare the values associated with "sirNo" key
//						return sirNo1.compareTo(sirNo2);
//					}
//				});

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

//						return null;

			} catch (Exception e) {

				// Handle exceptions appropriately

				return ResponseEntity.badRequest().body("Error generating PDF");

			}

		}
		
		
		
		@GetMapping("/getDate")
		public Date getCurrentDate()		
		{	
			 // Get the current date and time
	        Date currentDate = new Date();

	        // Subtract 24 hours in milliseconds (24 hours * 60 minutes * 60 seconds * 1000 milliseconds)
	        long twentyFourHoursAgoMillis = currentDate.getTime() - (24 * 60 * 60 * 1000);

	        // Create a new Date object with the calculated time
	        Date twentyFourHoursAgo = new Date(twentyFourHoursAgoMillis);	        
	        
	        System.out.println("Current Date "+currentDate);
	        System.out.println("Before 24 Hours Date "+twentyFourHoursAgo);
	        
	        return twentyFourHoursAgo;
			
		}
		
		
		private Date parseDateString(String dateString) {
		    try {
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		        return dateFormat.parse(dateString);
		    } catch (ParseException e) {
		        // Handle parsing exception
		        e.printStackTrace();
		        return null; // or throw an exception
		    }
		}
		
		
		
////		Billing Transaction
//		
//		@GetMapping("/searchBillinTransactionAfter")
//		public List<CombinedImportExport> searchBillingTransation22222(
//		        @RequestParam(name = "companyid", required = false) String companyid,
//		        @RequestParam(name = "branchId", required = false) String branchId,
//		        @RequestParam(name = "PartyId", required = false) String PartyId,
//		        @RequestParam(name = "userId", required = false) String userId,
//		        @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//		        @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//
//		    List<CombinedImportExport> result = new ArrayList<>();
//		    
//		    
////		    System.out.println("******* Party Id Received ******"+PartyId);
//
//		    if (PartyId != null && !PartyId.trim().isEmpty()) {
//		        // PartyId is present, perform the operation for a single party
//		        Party findPartyById = PartyService.findPartyById(companyid, branchId, PartyId);
////		        System.out.println("Party Single Party");
////		        System.out.println(findPartyById);
//		        result.addAll(OperationForBilling(companyid,branchId,findPartyById,userId,startDate,endDate));
//		    } else {
//		    	 List<Party> parties = partyRepository.getalldata(companyid,branchId);	
//		        // PartyId is not present, get a list of party IDs and perform the operation for all parties
////		        List<String> partyIds = PartyService.getPartyIdsByCompanyAndBranch(companyid, branchId);
//		        for (Party party : parties) {
////		            Party currentParty = PartyService.findPartyById(companyid, branchId, party);
////		        	System.out.println("Party For Loop");
////		        	System.out.println(party);
//		            result.addAll(OperationForBilling(companyid,branchId,party,userId,startDate,endDate));
//		        }
//		    }
//             Collections.sort(result, Comparator.comparing(CombinedImportExport::getDate));
//		    
//		      return result;
//		}
//		
		
		
//		Billing Transaction
		
		
//		public List<CombinedImportExport> OperationForBilling(
//				String companyid, String branchId, Party Party, String userId, Date startDate, Date endDate) {
//
//			Party findPartyById = Party;
//
//			String findPartyNameById = findPartyById.getPartyName();
//			String PartyId = findPartyById.getPartyId();
//			
//			
//			List<Import> ImportsBetweenStartDateAndEndDate = importRepo.findImportBySirDateStartDateAndEndDate(companyid, branchId, startDate, endDate, PartyId);
//			List<Export> ExportsBetweenStartDateAndEndDate = exportrepo.findImportBySerDateStartDateAndEndDate(companyid, branchId, startDate, endDate, PartyId);
//			List<ImportSub> ImportSubsBetweenStartDateAndEndDate = impsubRepo.findImportBySirDateStartDateAndEndDate(companyid, branchId, startDate, endDate, PartyId);
//			List<ExportSub> ExportSubsBetweenStartDateAndEndDate = expsubRepo.findImportBySerDateStartDateAndEndDate(companyid, branchId, startDate, endDate, PartyId);
//			
//
////			Date invoiceDate = findPartyById.getLastproformaDate();
//
////			if (invoiceDate != null) {
////			    Date lastInvoiceDate = findPartyById.getLastInVoiceDate();
////
////			    if (lastInvoiceDate != null) {
////			        if (invoiceDate.after(lastInvoiceDate)) {
////			            startDate = invoiceDate;
////			        } else {
////			            Calendar calendar = Calendar.getInstance();
////			            calendar.setTime(lastInvoiceDate);
////			            calendar.add(Calendar.DAY_OF_MONTH, 1);
////			            startDate = calendar.getTime();
////			        }
////			    } else {
////			        startDate = invoiceDate;
////			    }
////			} else if (findPartyById.getLastInVoiceDate() != null) {
////			    startDate = findPartyById.getLastInVoiceDate();
////			} else {
////			    startDate = new Date();
////			}
//			
//			
////			System.out.println("StartDate " +startDate);
//
//			List<Object[]> combinedImportExportData = importRepo.getCombinedImportExportData22(companyid, branchId, PartyId,
//					startDate, endDate);
////			System.out.println("*******************Results****************");
//			
////			System.out.println(combinedImportExportData);
//			
//			CfsTarrif finalCfsTarrif;
//
//			String PARTYID;
//
//			CfsTarrif bypartyId = CFSService.getBypartyId(companyid, branchId, PartyId);
//
//			if (bypartyId != null) {
//				finalCfsTarrif = bypartyId;
//				PARTYID = bypartyId.getPartyId();
//			} else {
//				CfsTarrif findByAll = CFSService.getBypartyId(companyid, branchId, "ALL");
//				finalCfsTarrif = findByAll;
//				PARTYID = findByAll.getPartyId();
//			}
//			;
//
//			List<CombinedImportExport> combinedDataList = combinedImportExportData.stream().map(array -> {
//
//				String partyId = (String) array[0];
//
//				BigDecimal importHpWeight = (array[6] != null) ? (BigDecimal) array[6] : BigDecimal.ZERO;
//				int exportNoOfPackages = (array[7] != null) ? Integer.parseInt(array[7].toString()) : 0;
//				BigDecimal exportHpWeight = (array[11] != null) ? (BigDecimal) array[11] : BigDecimal.ZERO;
//				double exportPenaltyLocal = 0.0;
//				double importPenaltyLocal = 0.0;
//
//				int exportSubNoOfPackages = (array[16] != null) ? Integer.parseInt(array[16].toString()) : 0;
//				int importSubNoOfPackages = (array[14] != null) ? Integer.parseInt(array[14].toString()) : 0;
//
//				Date date = new Date(((java.util.Date) array[1]).getTime());
//
//
//				
////				 Calendar calendar = Calendar.getInstance();
////				    calendar.setTime(date);
////				    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//
////				    Date LastDateOfMonth = calendar.getTime();
//				
//				
//				
//
//				int importNoOfPackages = (array[2] != null) ? Integer.parseInt(array[2].toString()) : 0;
//				// Total Packages
//				int totalPackages = importNoOfPackages + exportNoOfPackages;
//
//				int niptPackages = 0;
//
//				double demuragesRate = 0.0;
//				int demuragesNop = 0;
//				Double importRate = 0.0;
//				Double exportRate = 0.0;
//				Double importPcRate = 0.0;
//				Double importHeavyRate = 0.0;
//				Double HolidayRate = 0.0;
//				Double importScRate = 0.0;
//				Double exportPcRate = 0.0;
//				Double exportHeavyRate = 0.0;
////				BigDecimal exportHolidayRate = BigDecimal.ZERO;
//				Double exportScRate = 0.0;
//				Double importSubRate = 0.0;
//				Double exportSubRate = 0.0;				
//				
//				List<Import> findByPartyIdofSirDate = null;
//				List<Export> findByExportsBySerDate = null;
//				List<ImportSub> findBySirDate = null;
//				List<ExportSub> findBySerDate = null;
//
//				int holidayImporters = 0;
//				
//				
//				
//				
//				
//	//Import 
//				if(importNoOfPackages > 0)
//				{
////					findByPartyIdofSirDate = importService.findByPartyIdofSirDate(companyid, branchId, PartyId, date);
//					
//					findByPartyIdofSirDate = ImportsBetweenStartDateAndEndDate.stream()
//					        .filter(importObj -> importObj.getSirDate().equals(date))
//					        .collect(Collectors.toList());
//					
////					holidayImporters = ImportRepo.countDistinctImporterIdsBySIRDATE(companyid,branchId,date);
//					
//				}
//	//Export
//				if(exportNoOfPackages > 0)
//				{
////					findByExportsBySerDate = exportrepo.findByCompanyIdAndBranchIdAndNameOfExporterAndSerDateAndStatusNot(companyid, branchId, PartyId, date, "D");
////					holidayImporters = exportrepo.countDistinctImporterIdsBySERDATE(companyid, branchId,date);
//				
//					findByExportsBySerDate = ExportsBetweenStartDateAndEndDate.stream()
//					        .filter(importObj -> importObj.getSerDate().equals(date))
//					        .collect(Collectors.toList());	
//					
//					
//				}
//	//Sub-Contract Import
//				if(importSubNoOfPackages > 0)
//				{
////					findBySirDate = impsubRepo.findByCompanyIdAndBranchIdAndExporterAndSirDateAndStatusNot(companyid, branchId, PartyId, date, "D");
////					holidayImporters = impsubRepo.countDistinctImporterIdsBySERDATE(companyid,branchId,date);
//
//					findBySirDate = ImportSubsBetweenStartDateAndEndDate.stream()
//					        .filter(importObj -> importObj.getSirDate().equals(date))
//					        .collect(Collectors.toList());	
//				
//				
//				}				
//	//Sub-Contract Export	
//				if(exportSubNoOfPackages > 0)
//				{
////				findBySerDate = expsubRepo.findByCompanyIdAndBranchIdAndExporterAndSerDate(companyid, branchId, PartyId, date);
////				holidayImporters = expsubRepo.countDistinctImporterIdsBySERDATE(companyid,branchId,date);
//				
//				
//					findBySerDate = ExportSubsBetweenStartDateAndEndDate.stream()
//					        .filter(importObj -> importObj.getSerDate().equals(date))
//					        .collect(Collectors.toList());		
//					
//					
//				}
//				
////				Holiday Charges
//				List<Import> filteredListImport = null;
//				List<Export> filteredListExport =null;
//				List<ImportSub> filteredListImportSub =null;
//				List<ExportSub> filteredListExportSub =null;
//				int sumOfImportHolidayNop =0;
//				int sumOfHolidayNopExport = 0;
//				int sumOfNopImportSubHoliday =0;
//				int sumOfNopExportSubHoliday =0;
//				
////				Import
//				if(findByPartyIdofSirDate != null)
//				{
//				 filteredListImport = findByPartyIdofSirDate.stream()
//					    .filter(importObj -> {
//					        String niptStatus = importObj.getNiptStatus();
//					        String pcStatus = importObj.getPcStatus();
//					        Date dateToCheck = "Y".equals(niptStatus) || "Y".equals(pcStatus)
//					                ? importObj.getSirDate()
//					                : (importObj.getTpDate() != null ? importObj.getTpDate() : importObj.getSirDate());
//					                
//					        // Check if dateToCheck is a holiday or second Saturday
//					        boolean isHolidayOrSecondSaturday = holidayService.findByDate(companyid, branchId, dateToCheck) || isSecondSaturday(dateToCheck);
//					        // Check if outDate is not null and if it's a holiday or second Saturday
////					        boolean isOutDateHolidayOrSecondSaturday = importObj.getOutDate() != null &&
////					                (holidayService.findByDate(companyid, branchId, importObj.getOutDate()) || isSecondSaturday(importObj.getOutDate()));
//					        // Return true if either it's a holiday/second Saturday for sirDate or Tpdate,
//					        // or if outDate is not null and it's a holiday/second Saturday for outDate
////					        return isHolidayOrSecondSaturday || isOutDateHolidayOrSecondSaturday;
//					        return isHolidayOrSecondSaturday;
//					    })
//					    .collect(Collectors.toList());
//				 
//				 sumOfImportHolidayNop = filteredListImport.stream()
//	                        .mapToInt(Import::getNop)
//	                        .sum();
//				 
//				}
//	//Export
//				if(findByExportsBySerDate != null)
//				{
//				 filteredListExport = findByExportsBySerDate.stream()
//					    .filter(importObj -> {						    	
//					        boolean isHolidayOrSecondSaturday = holidayService.findByDate(companyid, branchId, importObj.getSerDate()) || isSecondSaturday(importObj.getSerDate());
//					        // Check if outDate is not null and if it's a holiday or second Saturday
////					        boolean isOutDateHolidayOrSecondSaturday = importObj.getOutDate() != null &&
////					                (holidayService.findByDate(companyid, branchId, importObj.getOutDate()) || isSecondSaturday(importObj.getOutDate()));
//					        // Return true if either it's a holiday/second Saturday for sirDate or Tpdate,
//					        // or if outDate is not null and it's a holiday/second Saturday for outDate
////					        return isHolidayOrSecondSaturday || isOutDateHolidayOrSecondSaturday;
//					        return isHolidayOrSecondSaturday;
//					    })
//					    .collect(Collectors.toList());
//				 
//				 
//				 sumOfHolidayNopExport = filteredListExport.stream()
//	                        .mapToInt(Export::getNoOfPackages)
//	                        .sum();
//				}
//				
//	//Import Sub	
//				if(findBySirDate != null)
//				{
//				 filteredListImportSub = findBySirDate.stream()
//					    .filter(importObj -> {						    	
//					        boolean isHolidayOrSecondSaturday = holidayService.findByDate(companyid, branchId, importObj.getSirDate()) || isSecondSaturday(importObj.getSirDate());
//					        // Check if outDate is not null and if it's a holiday or second Saturday
////					        boolean isOutDateHolidayOrSecondSaturday = importObj.getOutDate() != null &&
////					                (holidayService.findByDate(companyid, branchId, importObj.getOutDate()) || isSecondSaturday(importObj.getOutDate()));
//					        // Return true if either it's a holiday/second Saturday for sirDate or Tpdate,
//					        // or if outDate is not null and it's a holiday/second Saturday for outDate
////					        return isHolidayOrSecondSaturday || isOutDateHolidayOrSecondSaturday;
//					        return isHolidayOrSecondSaturday;
//					    })
//					    .collect(Collectors.toList());
//				 
//				 sumOfNopImportSubHoliday = filteredListImportSub.stream()
//	                        .mapToInt(ImportSub::getNop)
//	                        .sum();
//				
//				}
//	//Export-Sub
//				
//				
//				if(findBySerDate !=null)
//				{
//				filteredListExportSub = findBySerDate.stream()
//					    .filter(importObj -> {
//					    	
//					        boolean isHolidayOrSecondSaturday = holidayService.findByDate(companyid, branchId, importObj.getSerDate()) || isSecondSaturday(importObj.getSerDate());
//
//					        // Check if outDate is not null and if it's a holiday or second Saturday
////					        boolean isOutDateHolidayOrSecondSaturday = importObj.getOutDate() != null &&
////					                (holidayService.findByDate(companyid, branchId, importObj.getOutDate()) || isSecondSaturday(importObj.getOutDate()));
//
//					        // Return true if either it's a holiday/second Saturday for sirDate or Tpdate,
//					        // or if outDate is not null and it's a holiday/second Saturday for outDate
////					        return isHolidayOrSecondSaturday || isOutDateHolidayOrSecondSaturday;
//					        return isHolidayOrSecondSaturday;
//					    })
//					    .collect(Collectors.toList());
//				
//				sumOfNopExportSubHoliday = filteredListExportSub.stream()
//	                    .mapToInt(ExportSub::getNop)
//	                    .sum();	
//				
//				
//				}
//				
//				
//				
//				if (sumOfImportHolidayNop > 0 || sumOfHolidayNopExport > 0 || sumOfNopImportSubHoliday > 0 || sumOfNopExportSubHoliday > 0) 
//				{
//					holidayImporters = ImportRepo.sumOfDistinctImporterIds(companyid, branchId, date);
//				}
//				
//				
//				
////				//Import 
////				if(sumOfImportHolidayNop > 0)
////				{							
////					holidayImporters += ImportRepo.countDistinctImporterIdsBySIRDATE(companyid,branchId,date);
////					
////				}
////	//Export
////				if(sumOfHolidayNopExport > 0)
////				{
////					holidayImporters += exportrepo.countDistinctImporterIdsBySERDATE(companyid, branchId,date);
////					
////				}
////	//Sub-Contract Import
////				if(sumOfNopImportSubHoliday > 0)
////				{
////					holidayImporters += impsubRepo.countDistinctImporterIdsBySERDATE(companyid,branchId,date);
////
////				}				
////	//Sub-Contract Export	
////				if(sumOfNopExportSubHoliday > 0)
////				{
////					holidayImporters += expsubRepo.countDistinctImporterIdsBySERDATE(companyid,branchId,date);
////				}
//
//				
//				
//				
//				
//				int TotalHolidayNop = sumOfImportHolidayNop + sumOfHolidayNopExport + sumOfNopImportSubHoliday + sumOfNopExportSubHoliday;
//				
//				
//				
//
//				
//				if(TotalHolidayNop > 0)
//				{	
//					
//					
//					String importHolidayServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,
//							"Holiday");
//					HolidayRate += CFSTariffRangeService.findRateForDervice(companyid, branchId,
//							finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importHolidayServiceId, PARTYID,
//							holidayImporters);
//
//				}	
//				
//				
//				
////				Import
//
//				if (importNoOfPackages > 0) {
//
//			
//					
//					List<Import> importsWithScStatusY = findByPartyIdofSirDate.stream()
//					        .filter(importObj -> "Y".equals(importObj.getScStatus()))
//					        .collect(Collectors.toList());
//					
//					
//					List<Import> importsWithPcStatusY = findByPartyIdofSirDate.stream()
//					        .filter(importObj -> "Y".equals(importObj.getPcStatus()))
//					        .collect(Collectors.toList());
//					
//					
//					
//					
//					
//					
////					System.out.println("Special Array");
////					System.out.println(importsWithScStatusY);
//					
//					int splNop = 0;
//					int PcNop =0;
//					
//					if(importsWithScStatusY != null)
//					{
//						 splNop = importsWithScStatusY.stream()
//	                                .mapToInt(Import::getNop)
//	                                .sum();
//					}
//					
//					
//					if(importsWithPcStatusY != null)
//					{
//					PcNop = importsWithPcStatusY.stream()
//	                        .mapToInt(Import::getNop)
//	                        .sum();
//					}			
//					
//					if(splNop > 0)
//					{
//						
//						Map<String, Integer> sumByTpNoAndDate = importsWithScStatusY.stream()
//						        .collect(
//						                Collectors.groupingBy(
//						                        importObj -> {
//						                            String tpNo = Optional.ofNullable(importObj.getTpNo()).orElse("0000");
//						                            Date tpDate = Optional.ofNullable(importObj.getTpDate()).orElse(importObj.getSirDate());
//						                            return tpNo + "*" + tpDate;
//						                        },
//						                        Collectors.collectingAndThen(
//						                                Collectors.toSet(),
//						                                set -> set.stream().mapToInt(Import::getNop).sum()
//						                        )
//						                )
//						        );
//						
//						
//								
//						
//						
//						
//						
//						 String importScServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,
//									"import SC");
//					
//						MutableValues mutableValues = new MutableValues();
//						
//						  sumByTpNoAndDate.forEach((tpNoAndDate, sum) -> {
//							  						  
//							  String[] parts = tpNoAndDate.split("\\*");
//
//							    String tpNo = parts[0];
//							    Date tpDate = parseDateString(parts[1]);
//							    
//							    int DistinctParties = ImportRepo.countDistinctImporterIds(companyid,branchId,tpNo,tpDate);
//							    							    
//							   
//								double SingleimportScRate = CFSTariffRangeService.findRateForDervice(companyid, branchId,
//										finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importScServiceId, PARTYID, DistinctParties);
//
////								 System.out.println("Single importScRate "+ SingleimportScRate);
//								mutableValues.SPLimportBillAmount += SingleimportScRate;	
//						
//						  });
//						
//						
//						importScRate += mutableValues.SPLimportBillAmount;
//
//						
//					}
//
//					
////					System.out.println("importScRate "+ importScRate);
//					
//					
//					
//					
//					
//
//					
//					if(PcNop > 0)
//					{
//						
//						String importPcServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,
//								"import PC");
//						importPcRate += CFSTariffRangeService.findRateForDervice(companyid, branchId,
//								finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importPcServiceId,
//								PARTYID, PcNop);				
//						
//						
//					}
//					
//					
//					String importserviceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,
//							"import pckgs");
//					String importHPServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,
//							"import HP");
//
//					String importdemurage = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,
//							"export DM");
//					
//					
//					
//					
//					
//					
//					  // Calculate total importPackages
//			        int totalImportPackages = findByPartyIdofSirDate.stream().mapToInt(Import::getNop).sum();
//
//			        // Calculate total penaltyAmount
//			        double penaltyAmount = findByPartyIdofSirDate.stream().mapToDouble(Import::getImposePenaltyAmount).sum();
//
//			        // Calculate total niptPackages with niptStatus 'Y'
//			        int totalNiptPackages = findByPartyIdofSirDate.stream()
//			                .filter(importObj -> "Y".equals(importObj.getNiptStatus()))
//			                .mapToInt(Import::getNop)
//			                .sum();
//					
//			        
//			        double SingleimportRate = cfsTarrifServiceService.findRateService(companyid, branchId,
//							finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importserviceId,
//							PARTYID);
//					importRate += SingleimportRate * totalImportPackages;
//					
//					
//			        niptPackages += totalNiptPackages;
//					
//			        importPenaltyLocal += penaltyAmount;
//					
//					
//					for (Import imp : findByPartyIdofSirDate) {
//
////						if (imp.getNop() > 0) {						
////							
////											
////
//////							if (imp.getNiptStatus() != null && imp.getNiptStatus().equals("Y")) {
//////								niptPackages += imp.getNop();
//////							}
////
//////							importPenaltyLocal += imp.getImposePenaltyAmount();
////
////							
//////							double SingleimportRate = cfsTarrifServiceService.findRateService(companyid, branchId,
//////									finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importserviceId,
//////									PARTYID);
//////							importRate += SingleimportRate * imp.getNop();
////						}
//
//
//						if (imp.getHpStatus() != null && imp.getHpStatus().equals("Y")) {
//
//							
//							List<ImportHeavyPackage> byMAWBImport = ImportHeavyService.getByMAWB(companyid, branchId,
//									imp.getImpTransId(), imp.getMawb(), imp.getHawb(), imp.getSirNo());
//
//							List<BigDecimal> weights = byMAWBImport.stream().map(ImportHeavyPackage::getHpWeight)
//									.collect(Collectors.toList());
//
////							System.out.println("*****Weights  " + weights + "*********");
//
//							double SingleimportHeavyRate = CFSTariffRangeService.findRateForHeavy(companyid, branchId,
//									finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importHPServiceId,
//									PARTYID, weights);
//
//							importHeavyRate += SingleimportHeavyRate;
//						}
//
////						Date toBeSend = (imp.getOutDate() != null) ? imp.getOutDate() : LastDateOfMonth;
//						Date toBeSend = (imp.getOutDate() != null) ? imp.getOutDate() : new Date();
//						
//						Date tpDate = (imp.getTpDate() != null) ? imp.getTpDate() : imp.getSirDate();
//						long timeDifferenceMillis = toBeSend.getTime() - tpDate.getTime();
//						int daysDifference = (int) (timeDifferenceMillis / (1000 * 60 * 60 * 24));
//
//						if (daysDifference > 13) {							
//
//							double importBillAmount = CFSTariffRangeService.findRateForDervice(companyid, branchId,
//									finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importdemurage, PARTYID, daysDifference);
//
//							demuragesNop += imp.getNop();
//							demuragesRate += importBillAmount * imp.getNop();
//						}
//					}
//				}
//
////			Export
//
//				if (exportNoOfPackages > 0) {
//
//					
//					List<Export> exportsWithScStatusY = findByExportsBySerDate.stream()
//					        .filter(ExportObj -> "Y".equals(ExportObj.getScStatus()))
//					        .collect(Collectors.toList());
//					
//					
//					List<Export> exportsWithPcStatusY = findByExportsBySerDate.stream()
//					        .filter(ExportObj -> "Y".equals(ExportObj.getPcStatus()))
//					        .collect(Collectors.toList());
//					
//					
//					
//					
//					
//					
//
//					
//					
//					
//					
////					Map<String, Integer> sumByTpNoAndDate = exportsWithScStatusY.stream()
////					        .collect(
////					                Collectors.groupingBy(
////					                        importObj -> importObj.getTpNo() + "*"+importObj.getTpDate(),
////					                        Collectors.collectingAndThen(
////					                                Collectors.toSet(),
////					                                set -> set.stream().mapToInt(Export::getNoOfPackages).sum()
////					                        )
////					                )
////					        );
//					
//					
//					
//					int splNop = 0;
//					int PcNop =0;
//					
//					if(exportsWithScStatusY != null)
//					{
//						 splNop = exportsWithScStatusY.stream()
//	                                .mapToInt(Export::getNoOfPackages)
//	                                .sum();
//					}
//					
//					
//					if(exportsWithPcStatusY != null)
//					{
//					PcNop = exportsWithPcStatusY.stream()
//	                        .mapToInt(Export::getNoOfPackages)
//	                        .sum();
//					}
//					
//					
//					if(splNop > 0)
//					{
//						
//						Map<String, Integer> sumByTpNoAndDate = exportsWithScStatusY.stream()
//						        .collect(
//						                Collectors.groupingBy(
//						                        importObj -> {
//						                            String tpNo = Optional.ofNullable(importObj.getTpNo()).orElse("0000");
//						                            Date tpDate = Optional.ofNullable(importObj.getTpDate()).orElse(importObj.getSerDate());
//						                            return tpNo + "*" + tpDate;
//						                        },
//						                        Collectors.collectingAndThen(
//						                                Collectors.toSet(),
//						                                set -> set.stream().mapToInt(Export::getNoOfPackages).sum()
//						                        )
//						                )
//						        );
//						
//						
//						
//						
//						
//					String exportScServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId, "export SC");
//					
//					MutableValues mutableValues = new MutableValues();
//					
//					  sumByTpNoAndDate.forEach((tpNoAndDate, sum) -> {
//						  						  
//						  String[] parts = tpNoAndDate.split("\\*");
//
//						    String tpNo = parts[0];
//
//						    Date tpDate = parseDateString(parts[1]);		            
//				            
//				            
//				            
//				            int DistinctParties = exportrepo.countDistinctImporterIds(companyid,branchId,tpNo,tpDate);
//				            
//				            
//							double SingleexportScRate = CFSTariffRangeService.findRateForDervice(companyid, branchId,
//									finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), exportScServiceId,
//									PARTYID, DistinctParties);
////							exportScRate += SingleexportScRate;
//							
//							 mutableValues.SPLimportBillAmount += SingleexportScRate;
////							    mutableValues.SPLimportBillAmount += addInvoiceDetail.getBillAmount();
////							    mutableValues.SPLimportTotalInvoiceAmount += addInvoiceDetail.getTotalInvoiceAmount();
//						   //			           
//				        });		    
//					  
//					  exportScRate += mutableValues.SPLimportBillAmount;
//			}
//
//					
//					
//					
//					
//					
//					
//					if(PcNop > 0)
//					{
//						
//						String exportPcServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,
//								"export PC");
//						exportPcRate += CFSTariffRangeService.findRateForDervice(companyid, branchId,
//								finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), exportPcServiceId,
//								PARTYID, PcNop);
//						
//						
//					}
//					
//			
//					String exportserviceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId, "export pckgs");
//					String importHpServiceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId, "export HP");
//					String exportdemurage = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId, "export DM");
//					
//					
//					
//					
//
//					  // Calculate total importPackages
//			        int totalexportPackages = findByExportsBySerDate.stream().mapToInt(Export::getNoOfPackages).sum();
//
//			        // Calculate total penaltyAmount
//			        double penaltyAmount = findByExportsBySerDate.stream().mapToDouble(Export::getImposePenaltyAmount).sum();
//		        					
//			        
//			    	double SingleexportRate = cfsTarrifServiceService.findRateService(companyid, branchId,finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), exportserviceId,
//							PARTYID);
//			        exportRate += SingleexportRate * totalexportPackages;
//
//					exportPenaltyLocal += penaltyAmount;
//					
//								
//
//					for (Export exp : findByExportsBySerDate) {
//
////						if (exp.getNoOfPackages() > 0) {					
////							
////																	
////
////							exportPenaltyLocal += exp.getImposePenaltyAmount();
////
////							
////							double SingleexportRate = cfsTarrifServiceService.findRateService(companyid, branchId,finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), exportserviceId,
////									PARTYID);
////							exportRate += SingleexportRate * exp.getNoOfPackages();
////						}
//
//
//						if (exp.getHpStatus() != null && exp.getHpStatus().equals("Y")) {
//							
//
//							List<ExportHeavyPackage> byMAWB = ExportHeavyPackageRepo.findalldata(companyid, branchId,
//									exp.getSbRequestId(), exp.getSbNo());
//
//							List<BigDecimal> weights = byMAWB.stream().map(ExportHeavyPackage::getWeight)
//									.collect(Collectors.toList());
//
//							double SingleexportHeavyRate = CFSTariffRangeService.findRateForHeavy(companyid, branchId,
//									finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importHpServiceId,
//									PARTYID, weights);
//							exportHeavyRate += SingleexportHeavyRate;
//
//						}
//
//						Date toBeSend = (exp.getOutDate() != null) ? exp.getOutDate() : new Date();
//						long timeDifferenceMillis = toBeSend.getTime() - exp.getSerDate().getTime();
//						int daysDifference = (int) (timeDifferenceMillis / (1000 * 60 * 60 * 24));
//
//						if (daysDifference > 13) {
//							
//
//							double importBillAmount = CFSTariffRangeService.findRateForDervice(companyid, branchId,
//									finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), exportdemurage, PARTYID,
//									daysDifference);
//
//							demuragesNop += exp.getNoOfPackages();
//							demuragesRate += importBillAmount * exp.getNoOfPackages();
//						}
//					}
//				}
//
////				Import Sub
//
//				if (importSubNoOfPackages > 0) {
//					
//					String importserviceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,	"import pckgs");
//					String importdemurage = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,"export DM");
//					
//					
//					 // Calculate total importSubPackages
//			        int totalimportSubPackages = findBySirDate.stream().mapToInt(ImportSub::getNop).sum();
//
//			        // Calculate total penaltyAmount
//			        double penaltyAmount = findBySirDate.stream().mapToDouble(ImportSub::getImposePenaltyAmount).sum();
//		        		
//			        
//			        importPenaltyLocal += penaltyAmount;
//					double SingleimportRate = cfsTarrifServiceService.findRateService(companyid, branchId,
//							finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importserviceId, PARTYID);
//					importSubRate += SingleimportRate * totalimportSubPackages;
//					
//					
////					System.out.println("Import Sub Rate "+importSubRate);
//					
//					for (ImportSub impsub : findBySirDate) {
//
////						if (impsub.getNop() > 0) {
////							
////							importPenaltyLocal += impsub.getImposePenaltyAmount();
////							double SingleimportRate = cfsTarrifServiceService.findRateService(companyid, branchId,
////									finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importserviceId, PARTYID);
////							importSubRate += SingleimportRate * impsub.getNop();
////
////						}
//
//						Date toBeSend = (impsub.getOutDate() != null) ? impsub.getOutDate() : new Date();
//						long timeDifferenceMillis = toBeSend.getTime() - impsub.getSirDate().getTime();
//						int daysDifference = (int) (timeDifferenceMillis / (1000 * 60 * 60 * 24));
//
//						if (daysDifference > 13) {
//							
//
//							double importBillAmount = CFSTariffRangeService.findRateForDervice(companyid, branchId,
//									finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importdemurage, PARTYID,
//									daysDifference);
//
////						System.out.println("importBillAmount Sub " + importBillAmount);
//
//							demuragesNop += impsub.getNop();
//							demuragesRate += importBillAmount * impsub.getNop();
//
//						}
//					}
//				}
//
//				if (exportSubNoOfPackages > 0) {
//
//					String exportserviceId = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId,	"export pckgs");
//					
//					String importdemurage = ServiceIdMappingRepositary.findServieIdByKeys(companyid, branchId, "export DM");
//
//					
//					 // Calculate total importSubPackages
//			        int totalExportSubPackages = findBySerDate.stream().mapToInt(ExportSub::getNop).sum();
//
//			        // Calculate total penaltyAmount
//			        double penaltyAmount = findBySerDate.stream().mapToDouble(ExportSub::getImposePenaltyAmount).sum();
//		        		
//			        
//			        importPenaltyLocal += penaltyAmount;
//			        
//					double SingleimportRate = cfsTarrifServiceService.findRateService(companyid, branchId,
//							finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), exportserviceId, PARTYID);
//					importSubRate += SingleimportRate * totalExportSubPackages;
//					
//					
//					
//					
//					
//					
//					for (ExportSub expsub : findBySerDate) {
//
////						if (expsub.getNop() > 0) {			
////							
////							exportPenaltyLocal += expsub.getImposePenaltyAmount();							
////							double SingleexportRate = cfsTarrifServiceService.findRateService(companyid, branchId,
////									finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), exportserviceId,PARTYID);
////							exportSubRate += SingleexportRate * expsub.getNop();
////						}
//
//						Date toBeSend = (expsub.getOutDate() != null) ? expsub.getOutDate() : new Date();
//						long timeDifferenceMillis = toBeSend.getTime() - expsub.getSerDate().getTime();
//						int daysDifference = (int) (timeDifferenceMillis / (1000 * 60 * 60 * 24));
//
//						if (daysDifference > 13) {							
//
//							double importBillAmount = CFSTariffRangeService.findRateForDervice(companyid, branchId,
//									finalCfsTarrif.getCfsTariffNo(), finalCfsTarrif.getCfsAmndNo(), importdemurage, PARTYID,
//									daysDifference);
//							demuragesNop += expsub.getNop();
//							demuragesRate += importBillAmount * expsub.getNop();
//						}
//					}
//				}
//				totalPackages += importSubNoOfPackages + exportSubNoOfPackages;
//
//
//				return new CombinedImportExport(partyId, findPartyNameById, HolidayRate, date, importNoOfPackages,
//						totalPackages, importScRate, importPcRate, importHeavyRate, importHpWeight, importPenaltyLocal,
//						exportNoOfPackages, exportScRate, exportPcRate, exportHeavyRate, exportHpWeight, exportPenaltyLocal,
//						importRate, exportRate, importSubNoOfPackages, exportSubNoOfPackages, importSubRate, exportSubRate,
//						demuragesNop, demuragesRate, niptPackages);
//			}).collect(Collectors.toList());
//
//			return combinedDataList;
//
//		}
		
		
		
		@GetMapping("/searchBillinTransactionAfter")
		public ResponseEntity<?> searchBillingTransation22222(
		        @RequestParam(name = "companyid", required = false) String companyid,
		        @RequestParam(name = "branchId", required = false) String branchId,
		        @RequestParam(name = "PartyId", required = false) String PartyId,
		        @RequestParam(name = "userId", required = false) String userId,
		        @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
		        @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		   		
			
			System.out.println("StartDate "+startDate);
			System.out.println("endDate "+endDate);
				List<PredictableInvoiceTaxDetails> invoiceData = PredictableInvoiceTaxDetailsRepo.findInvoiceDataStartDateAndEndDate(companyid, branchId, startDate, endDate, PartyId);
	    
				return ResponseEntity.ok(invoiceData);
		     
		}
		
			
		
		
		public class MutableValues {
		    public double SPLimportBillAmount;
		    public  double SPLimportTaxAmount;
		    public double SPLimportTotalInvoiceAmount;
		}
		
		@GetMapping("/searchbylogintype/{loginid}/{logintype}")
		public List<Object[]> searchByLoginTypeImports(@RequestParam(name = "pcStatus", required = false) String pcStatus,
				@RequestParam(name = "scStatus", required = false) String scStatus,
				@RequestParam(name = "searchValue", required = false) String searchValue,
				@RequestParam(name = "companyid", required = false) String companyid,
				@RequestParam(name = "branchId", required = false) String branchId,
				@RequestParam(name = "holdStatus", required = false) String holdStatus,
				@RequestParam(name = "forwardedStatus", required = false) String forwardedStatus,
				@RequestParam(name = "hpStatus", required = false) String hpStatus,
				@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
				@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
				@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
				@PathVariable("loginid") String loginid,
				@PathVariable("logintype") String logintype) {

//			System.out.println("searchValue "+searchValue);

			if (searchValue != null) {
				searchValue = searchValue.trim(); // Trim leading and trailing spaces
			}
			
			if("Party".equals(logintype)) {
				return importRepo.findByPartyAttributes(companyid, branchId, loginid,pcStatus, scStatus, hpStatus, holdStatus,
						forwardedStatus, dgdcStatus, startDate, endDate, searchValue);
			}
			else if("CHA".equals(logintype)) {
				return importRepo.findByCHAAttributes(companyid, branchId, loginid,pcStatus, scStatus, hpStatus, holdStatus,
						forwardedStatus, dgdcStatus, startDate, endDate, searchValue);
			}
			else if("Console".equals(logintype)) {
				return importRepo.findByConsoleAttributes(companyid, branchId, loginid,pcStatus, scStatus, hpStatus, holdStatus,
						forwardedStatus, dgdcStatus, startDate, endDate, searchValue);
			}
			else {
				return importRepo.findByCartingAttributes(companyid, branchId, loginid,pcStatus, scStatus, hpStatus, holdStatus,
						forwardedStatus, dgdcStatus, startDate, endDate, searchValue);
			}
			
			
			
		}
		
		
		@GetMapping("/getDGDCStatus/{cid}/{bid}/{mawb}/{hawb}/{trans}/{sir}")
		public String findStatus(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("mawb") String mawb,@PathVariable("hawb") String hawb,@PathVariable("trans") String trans,@PathVariable("sir") String sir) {
			
			return importRepo.findStatus(cid, bid, mawb, hawb,trans,sir);
		}
	
}
