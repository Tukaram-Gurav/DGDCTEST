package com.cwms.controller;


import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.cwms.entities.Branch;
import com.cwms.entities.Company;
import com.cwms.entities.ExportSub;
import com.cwms.entities.Gate_In_Out;
import com.cwms.entities.ImportSub;
import com.cwms.entities.ImportSub_History;
import com.cwms.entities.Party;
import com.cwms.helper.FileUploadProperties;
import com.cwms.repository.BranchRepo;
import com.cwms.repository.CompanyRepo;
import com.cwms.repository.ExportSubRepository;
import com.cwms.repository.Gate_In_out_Repo;
import com.cwms.repository.ImportSubHistoryRepo;
import com.cwms.repository.ImportSubRepository;
import com.cwms.repository.PartyRepository;
import com.cwms.repository.ProcessNextIdRepository;
import com.cwms.service.ImageService;
import com.cwms.service.ImportSubService;
import com.cwms.service.ProcessNextIdService;

import jakarta.transaction.Transactional;
import java.util.Base64;
@CrossOrigin("*")
@RestController
@RequestMapping("/importsub")
public class ImportSubController {
	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private ImportSubRepository impsubrepo;
	
	@Autowired
	private ProcessNextIdService nextservice;
	
	@Autowired
	private Gate_In_out_Repo gateinoutrepo;
	
	@Autowired
	private ExportSubRepository exportrepo;
	
	@Autowired(required = true)
	public ProcessNextIdRepository processNextIdRepository;
	
	@Autowired
	public FileUploadProperties FileUploadProperties;
	
	@Autowired
	private ImportSubHistoryRepo importsubhisrepo;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private PartyRepository partyRepository;
	
	@Autowired
	private CompanyRepo companyRepo;
	
	@Autowired
	private BranchRepo branchRepo;
	
	@Autowired
	public ImportSubService importSubService;
	
	
	
	
	
	
	@GetMapping("/search")
	public List<Object[]> searchImports(@RequestParam(name = "searchValue", required = false) String searchValue,
			@RequestParam(name = "companyid", required = false) String companyid,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		

		
		
		return impsubrepo.findByAttributes6(companyid, branchId,dgdcStatus,startDate, endDate,searchValue);
	}
  
  
  
  @GetMapping("/search1")
	public List<Object[]> searchImports2(@RequestParam(name = "searchValue", required = false) String searchValue,
			@RequestParam(name = "companyid", required = false) String companyid,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		

		
		
		return impsubrepo.findByAttributes7(companyid, branchId,dgdcStatus,startDate, endDate,searchValue);
	}
  
  
  @GetMapping("/findBySeAndReq/{cid}/{bid}/{req}/{sir}")
  public ImportSub getdatabyserandreq(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("req") String req,@PathVariable("sir") String sir) {
	   return impsubrepo.findImportSubBysir(cid, bid, sir,req);
  }
	
	
	
	
	
	

	@GetMapping("/all/{cid}/{bid}")
	public List<ImportSub> getAlldata(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {
		return impsubrepo.getall(cid, bid);
	}
	
	
	@GetMapping("/allwtlgd/{cid}/{bid}")
	public List<ImportSub> getAlldata1(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {
		return impsubrepo.getalltocheckLGD1(cid, bid);
	}
	
	
	@GetMapping("/alllgd/{cid}/{bid}")
	public List<ImportSub> getAlldatatochecklgd(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {
		return impsubrepo.getalltocheckLGD(cid, bid);
	}
	
	
	@GetMapping("/single/{cid}/{bid}/{sir}")
	public ImportSub getSingledata(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("sir") String sir) {
		return this.impsubrepo.Singledata(cid, bid, sir);
	}

//	@PostMapping("/insertdata/{id}/{cid}/{bid}")
//	public ImportSub singlesavedata( @RequestBody ImportSub impsub,@PathVariable("id") String id,@PathVariable("cid") String cid,@PathVariable("bid") String bid) {
//		String nextid = nextservice.autoIncrementSubImpId();
//		String nexttransid = nextservice.autoIncrementSubImpTransId();
//		impsub.setSirNo(nextid);
//		impsub.setCompanyId(cid);
//		impsub.setBranchId(bid);
//		impsub.setLgdStatus("N");
//		impsub.setImpSubId(nexttransid);
//		impsub.setCreatedBy(id);
//		impsub.setCreatedDate(new Date());
//		impsub.setStatus("N");
//		impsub.setGatePassStatus("N");
//		impsub.setDgdcStatus("Handed over to DGDC SEEPZ");
//		impsub.setNsdlStatus("Pending");
//		impsub.setReentryDate(new Date());
//		impsub.setForwardedStatus("N");
//		impsub.setNoc(0);
//		impsub.setDgdc_cargo_in_scan(0);
//		impsub.setDgdc_cargo_out_scan(0);
//		impsub.setDgdc_seepz_in_scan(0);
//		impsub.setDgdc_seepz_out_scan(0);
//		impsub.setSirDate(new Date());
//		BigDecimal big = new BigDecimal("0.0");
//        impsub.setImposePenaltyAmount(0);
//        
//        for(int i=1;i<=impsub.getNop();i++) {
//    		String srNo = String.format("%04d", i);
//    		Gate_In_Out gateinout = new Gate_In_Out();
//    		gateinout.setCompanyId(cid);
//    		gateinout.setBranchId(bid);
//    		gateinout.setNop(impsub.getNop());
//    		gateinout.setErp_doc_ref_no(impsub.getRequestId());
//    		gateinout.setDoc_ref_no(nextid);
//    		gateinout.setSr_No(nextid+srNo);
//    		gateinout.setDgdc_cargo_in_scan("N");
//    		gateinout.setDgdc_cargo_out_scan("N");
//    		gateinout.setDgdc_seepz_in_scan("N");
//    		gateinout.setDgdc_seepz_out_scan("N");
//    		
//    		gateinoutrepo.save(gateinout);
//    	}
//		
//		 ImportSub_History importsubhistory = new ImportSub_History();
//		 importsubhistory.setCompanyId(cid);
//		 importsubhistory.setBranchId(bid);
//		 importsubhistory.setNewStatus("Handed over to DGDC SEEPZ");
//		 importsubhistory.setOldStatus("Pending");
//		 importsubhistory.setRequestId(impsub.getRequestId());
//		 importsubhistory.setSirNo(nextid);
//		 importsubhistory.setTransport_Date(new Date());
//		 importsubhistory.setUpdatedBy(id);
//		 importsubhisrepo.save(importsubhistory);
//        
//		return impsubrepo.save(impsub);
//	}
//	
//	
//	@PostMapping("/insertexportdata/{id}/{cid}/{bid}/{eid}")
//	public ImportSub exportsinglesavedata( @RequestBody ImportSub impsub,@PathVariable("id") String id,@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("eid") String eid) {
//		String nextid = nextservice.autoIncrementSubImpId();
//		String nexttransid = nextservice.autoIncrementSubImpTransId();
//		impsub.setSirNo(nextid);
//		impsub.setCompanyId(cid);
//		impsub.setBranchId(bid);
//		impsub.setImpSubId(nexttransid);
//		impsub.setCreatedBy(id);
//		impsub.setCreatedDate(new Date());
//		impsub.setStatus("N");
//		BigDecimal big = new BigDecimal("0.0");
//        impsub.setImposePenaltyAmount(0);
//        impsub.setNoc(0);
//		impsub.setDgdc_cargo_in_scan(0);
//		impsub.setDgdc_cargo_out_scan(0);
//		impsub.setDgdc_seepz_in_scan(0);
//		impsub.setGatePassStatus("N");
//		impsub.setForwardedStatus("N");
//		impsub.setDgdc_seepz_out_scan(0);
//		impsub.setDgdcStatus("Handed over to DGDC SEEPZ");
//		impsub.setNsdlStatus("Pending");
//		
//		impsub.setSirDate(new Date());
//		
//		
//		for(int i=1;i<=impsub.getNop();i++) {
//    		String srNo = String.format("%04d", i);
//    		Gate_In_Out gateinout = new Gate_In_Out();
//    		gateinout.setCompanyId(cid);
//    		gateinout.setBranchId(bid);
//    		gateinout.setNop(impsub.getNop());
//    		gateinout.setErp_doc_ref_no(impsub.getRequestId());
//    		gateinout.setDoc_ref_no(impsub.getSirNo());
//    		gateinout.setSr_No(nextid+srNo);
//    		gateinout.setDgdc_cargo_in_scan("N");
//    		gateinout.setDgdc_cargo_out_scan("N");
//    		gateinout.setDgdc_seepz_in_scan("N");
//    		gateinout.setDgdc_seepz_out_scan("N");
//    		
//    		gateinoutrepo.save(gateinout);
//    	}
//		
//		
//		//this.exportrepo.updateDELETEStatus(cid, bid, eid, impsub.getRequestId());
//		ImportSub_History importsubhistory = new ImportSub_History();
//		 importsubhistory.setCompanyId(cid);
//		 importsubhistory.setBranchId(bid);
//		 importsubhistory.setNewStatus("Handed over to DGDC SEEPZ");
//		 importsubhistory.setOldStatus("Pending");
//		 importsubhistory.setRequestId(impsub.getRequestId());
//		 importsubhistory.setSirNo(nextid);
//		 importsubhistory.setTransport_Date(new Date());
//		 importsubhistory.setUpdatedBy(id);
//		 importsubhisrepo.save(importsubhistory);
//		return impsubrepo.save(impsub);
//	}

	
	
	@Transactional
	@PostMapping("/insertdata/{id}/{cid}/{bid}")
	public ImportSub singlesavedata(@RequestBody ImportSub impsub, @PathVariable("id") String id,
			@PathVariable("cid") String cid, @PathVariable("bid") String bid) {

		synchronized (this) {
			String IMPTransId = processNextIdRepository.findNextsubimpid();

			int lastNextNumericId = Integer.parseInt(IMPTransId.substring(4));

			int nextNumericNextID = lastNextNumericId + 1;

			String nextid = String.format("D-IM%06d", nextNumericNextID);

			// Update the Next_Id directly in the database using the repository
			processNextIdRepository.updateNexsubimpid(nextid);

			String IMPTransId1 = processNextIdRepository.findNextsubimptransid();

	        int lastNextNumericId1 = Integer.parseInt(IMPTransId1.substring(3));

	        int nextNumericNextID1 = lastNextNumericId1 + 1;

	        String nexttransid = String.format("SIM%05d", nextNumericNextID1);

	        // Update the Next_Id directly in the database using the repository
	        processNextIdRepository.updateNexsubimptransid(nexttransid);

			impsub.setSirNo(nextid);
			impsub.setCompanyId(cid);
			impsub.setBranchId(bid);
			impsub.setLgdStatus("N");
			impsub.setImpSubId(nexttransid);
			impsub.setCreatedBy(id);
			impsub.setCreatedDate(new Date());
			impsub.setStatus("N");
			impsub.setGatePassStatus("N");
			impsub.setDgdcStatus("Handed over to DGDC SEEPZ");
			impsub.setNsdlStatus("Pending");
			impsub.setReentryDate(new Date());
			impsub.setForwardedStatus("N");
			impsub.setNoc(0);
			impsub.setDgdc_cargo_in_scan(0);
			impsub.setDgdc_cargo_out_scan(0);
			impsub.setDgdc_seepz_in_scan(0);
			impsub.setDgdc_seepz_out_scan(0);
			impsub.setSirDate(new Date());
			BigDecimal big = new BigDecimal("0.0");
			impsub.setImposePenaltyAmount(0);

			for (int i = 1; i <= impsub.getNop(); i++) {
				String srNo = String.format("%04d", i);
				Gate_In_Out gateinout = new Gate_In_Out();
				gateinout.setCompanyId(cid);
				gateinout.setBranchId(bid);
				gateinout.setNop(impsub.getNop());
				gateinout.setErp_doc_ref_no(impsub.getRequestId());
				gateinout.setDoc_ref_no(nextid);
				gateinout.setSr_No(nextid + srNo);
				gateinout.setDgdc_cargo_in_scan("N");
				gateinout.setDgdc_cargo_out_scan("N");
				gateinout.setDgdc_seepz_in_scan("N");
				gateinout.setDgdc_seepz_out_scan("N");

				gateinoutrepo.save(gateinout);
			}

			ImportSub_History importsubhistory = new ImportSub_History();
			importsubhistory.setCompanyId(cid);
			importsubhistory.setBranchId(bid);
			importsubhistory.setNewStatus("Handed over to DGDC SEEPZ");
			importsubhistory.setOldStatus("Pending");
			importsubhistory.setRequestId(impsub.getRequestId());
			importsubhistory.setSirNo(nextid);
			importsubhistory.setTransport_Date(new Date());
			importsubhistory.setUpdatedBy(id);
			importsubhisrepo.save(importsubhistory);

			return impsubrepo.save(impsub);
		}
	}

	@Transactional
	@PostMapping("/insertexportdata/{id}/{cid}/{bid}/{eid}")
	public ImportSub exportsinglesavedata(@RequestBody ImportSub impsub, @PathVariable("id") String id,
			@PathVariable("cid") String cid, @PathVariable("bid") String bid, @PathVariable("eid") String eid) {
		synchronized (this) {
			String IMPTransId = processNextIdRepository.findNextsubimpid();

			int lastNextNumericId = Integer.parseInt(IMPTransId.substring(4));

			int nextNumericNextID = lastNextNumericId + 1;

			String nextid = String.format("D-IM%06d", nextNumericNextID);

			// Update the Next_Id directly in the database using the repository
			processNextIdRepository.updateNexsubimpid(nextid);

			String IMPTransId1 = processNextIdRepository.findNextsubimptransid();

	        int lastNextNumericId1 = Integer.parseInt(IMPTransId1.substring(3));

	        int nextNumericNextID1 = lastNextNumericId1 + 1;

	        String nexttransid = String.format("SIM%05d", nextNumericNextID1);

	        // Update the Next_Id directly in the database using the repository
	        processNextIdRepository.updateNexsubimptransid(nexttransid);
			impsub.setSirNo(nextid);
			impsub.setCompanyId(cid);
			impsub.setBranchId(bid);
			impsub.setImpSubId(nexttransid);
			impsub.setCreatedBy(id);
			impsub.setCreatedDate(new Date());
			impsub.setStatus("N");
			BigDecimal big = new BigDecimal("0.0");
			impsub.setImposePenaltyAmount(0);
			impsub.setNoc(0);
			impsub.setDgdc_cargo_in_scan(0);
			impsub.setDgdc_cargo_out_scan(0);
			impsub.setDgdc_seepz_in_scan(0);
			impsub.setGatePassStatus("N");
			impsub.setForwardedStatus("N");
			impsub.setDgdc_seepz_out_scan(0);
			impsub.setDgdcStatus("Handed over to DGDC SEEPZ");
			impsub.setNsdlStatus("Pending");

			impsub.setSirDate(new Date());

			for (int i = 1; i <= impsub.getNop(); i++) {
				String srNo = String.format("%04d", i);
				Gate_In_Out gateinout = new Gate_In_Out();
				gateinout.setCompanyId(cid);
				gateinout.setBranchId(bid);
				gateinout.setNop(impsub.getNop());
				gateinout.setErp_doc_ref_no(impsub.getRequestId());
				gateinout.setDoc_ref_no(impsub.getSirNo());
				gateinout.setSr_No(nextid + srNo);
				gateinout.setDgdc_cargo_in_scan("N");
				gateinout.setDgdc_cargo_out_scan("N");
				gateinout.setDgdc_seepz_in_scan("N");
				gateinout.setDgdc_seepz_out_scan("N");

				gateinoutrepo.save(gateinout);
			}

			// this.exportrepo.updateDELETEStatus(cid, bid, eid, impsub.getRequestId());
			ImportSub_History importsubhistory = new ImportSub_History();
			importsubhistory.setCompanyId(cid);
			importsubhistory.setBranchId(bid);
			importsubhistory.setNewStatus("Handed over to DGDC SEEPZ");
			importsubhistory.setOldStatus("Pending");
			importsubhistory.setRequestId(impsub.getRequestId());
			importsubhistory.setSirNo(nextid);
			importsubhistory.setTransport_Date(new Date());
			importsubhistory.setUpdatedBy(id);
			importsubhisrepo.save(importsubhistory);
			return impsubrepo.save(impsub);
		}
	}
	
	
	
	@GetMapping("/byid/{cid}/{bid}/{impsubid}/{reqid}")
	public ImportSub getdatabyid(@PathVariable("cid") String companyId,@PathVariable("bid") String branchId,@PathVariable("impsubid") String impSubId,@PathVariable("reqid") String reqId) {
		return impsubrepo.findImportSub(companyId, branchId, impSubId, reqId);
	}

//	@Transactional
//	@PostMapping("/updateData/{id}")
//    public ImportSub updateImportSub(@PathVariable("id") String id, @RequestBody ImportSub updatedImportSub) {
//		    updatedImportSub.setEditedBy(id);
//		    updatedImportSub.setEditedDate(new Date());
//            return impsubrepo.save(updatedImportSub);
//        
//    }
	
	
	
	@Transactional
	@PostMapping("/updateData/{id}")
    public ImportSub updateImportSub(@PathVariable("id") String id, @RequestBody ImportSub updatedImportSub) {
		    updatedImportSub.setEditedBy(id);
		    updatedImportSub.setEditedDate(new Date());
		    
		    int sr_no = gateinoutrepo.findbysr2(updatedImportSub.getCompanyId(), updatedImportSub.getBranchId(), updatedImportSub.getRequestId(),updatedImportSub.getSirNo());
		    
		   if(sr_no != updatedImportSub.getNop()) {
			   List<Gate_In_Out> gateinout1 = gateinoutrepo.findbysr1(updatedImportSub.getCompanyId(), updatedImportSub.getBranchId(), updatedImportSub.getRequestId(),updatedImportSub.getSirNo());
				 gateinoutrepo.deleteAll(gateinout1);
				for(int i=1;i<=updatedImportSub.getNop();i++) {
		    		String srNo = String.format("%04d", i);
		    		Gate_In_Out gateinout = new Gate_In_Out();
		    		gateinout.setCompanyId(updatedImportSub.getCompanyId());
		    		gateinout.setBranchId(updatedImportSub.getBranchId());
		    		gateinout.setNop(updatedImportSub.getNop());
		    		gateinout.setErp_doc_ref_no(updatedImportSub.getRequestId());
		    		gateinout.setDoc_ref_no(updatedImportSub.getSirNo());
		    		gateinout.setSr_No(updatedImportSub.getSirNo()+srNo);
		    		gateinout.setDgdc_cargo_in_scan("N");
		    		gateinout.setDgdc_cargo_out_scan("N");
		    		gateinout.setDgdc_seepz_in_scan("N");
		    		gateinout.setDgdc_seepz_out_scan("N");
		    		
		    		gateinoutrepo.save(gateinout);
		    	}
		   }
		    
            return impsubrepo.save(updatedImportSub);
        
    }
	
	@GetMapping("/getdata")
	public List<ImportSub> getdata() {
		return impsubrepo.findAll();
		
	}
	
	@PostMapping("/changedata/{nsdl}/{cid}/{bid}/{expid}/{reqid}")
	public void changeDeliveryUpdate(@PathVariable("nsdl") String nsdl,
	                                 @PathVariable("cid") String cid,
	                                 @PathVariable("bid") String bid,
	                                 @PathVariable("expid") String expid,
	                                 @PathVariable("reqid") String reqid,
	                                 @RequestPart("file") MultipartFile file) throws IllegalStateException, IOException {

	    // Save the file to a folder (you need to specify the folder path)
	    String folderPath = FileUploadProperties.getPath(); // Update with your folder path
	    String fileName = file.getOriginalFilename();
	    String filePath = folderPath + "/" + fileName;

	    // Use the provided logic to generate a unique file name
	    String uniqueFileName = generateUniqueFileName(folderPath, fileName);

	    // Construct the full path for the unique file
	    String uniqueFilePath = folderPath + "/" + uniqueFileName;
	    file.transferTo(new File(uniqueFilePath));

	    this.impsubrepo.updateData(nsdl, uniqueFilePath, cid, bid, expid, reqid);
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

	
	@GetMapping("/getexpdata/{cid}/{bid}/{reqid}")
	public ExportSub getReqId(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("reqid") String reqid) {
		return  exportrepo.findRequestId(cid, bid, reqid);
	}
	
	@GetMapping("/getimpdata/{cid}/{bid}/{reqid}")
	public List<ImportSub> getReqIdforimp(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("reqid") String reqid) {
		return  impsubrepo.findRequestAllId(cid, bid, reqid);
	}
	
	@PostMapping("/penalty")
	public ImportSub imposepenalty(@RequestBody ImportSub impsub) {
	    return this.impsubrepo.save(impsub);
	}
	
	
//	@GetMapping("/importSubTransaction")
//    public ResponseEntity<List<ImportSub>> findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(
//			@RequestParam("sirDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate,
//            @RequestParam("companyId") String companyId,
//            @RequestParam("branchId") String branchId,
//            @RequestParam("dgdcStatus") String dgdcStatus) {
//
//	 
//	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String formattedDate = sdf.format(sirDate);
//		
//	 System.out.println(formattedDate);
//	 
//        List<ImportSub> imports = importSubService.findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(formattedDate, companyId, branchId, dgdcStatus);
//        
//        System.out.println(imports);
//        if (imports.isEmpty()) {
//            return ResponseEntity.notFound().build();
//            
//        }
//
//        return ResponseEntity.ok(imports);
//    }
	
	
	
	@GetMapping("/importSubTransaction")
    public ResponseEntity<List<Object[]>> findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(
			@RequestParam("sirDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date sirDate,
            @RequestParam("companyId") String companyId,
            @RequestParam("branchId") String branchId,
            @RequestParam("dgdcStatus") String dgdcStatus) {

	 
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(sirDate);
		
	 System.out.println(formattedDate);
	 
        List<Object[]> imports = importSubService.findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(formattedDate, companyId, branchId, dgdcStatus);
        
        System.out.println(imports);
        if (imports.isEmpty()) {
            return ResponseEntity.notFound().build();
            
        }
System.out.println("Hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
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
	
	@GetMapping("/findImportSubContractData")
    public List<ImportSub> findSubContractData(
            @RequestParam("companyId") String companyId,
            @RequestParam("branchId") String branchId,
          
            @RequestParam("exporter") String exporter) {
        // Call the repository method to fetch data based on the parameters
        List<ImportSub> importSubList = impsubrepo.findImportAllData(companyId, branchId,exporter);

        // You can add any additional processing here if needed

        return importSubList;
    }
	
	@GetMapping("/download/{cid}/{bid}/{impid}/{reqid}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("impid") String impid,@PathVariable("reqid") String reqid) throws IOException {
        // Retrieve the image path from the database based on imageId
    	ImportSub importsub = impsubrepo.findImportSub(cid, bid, impid, reqid);
        String imagePath = importsub.getStatus_document();

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

	 
//    @GetMapping("/findImportSubAllData/{companyId}/{branchId}/{startDate}/{endDate}")
//	public List<ImportSub> findExportSubData(@PathVariable("companyId") String companyId,
//			@PathVariable("branchId") String branchId,
//			@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
//
//	) {
//		return impsubrepo.findImportSubAllData(companyId, branchId, startDate, endDate);
//	}
    
    
    @GetMapping("/findImportSubAllData")
	public List<ImportSub> findExportSubData(			
			@RequestParam(name = "companyId", required = false) String companyId,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "formattedStartDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "formattedEndDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
	) {
    	
      	
		return impsubrepo.findImportSubAllData(companyId, branchId, startDate, endDate);
	}
    
	  
	  
	 @GetMapping("/bysir/{cid}/{bid}/{sir}")
	  public ImportSub getdatabyser(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("sir") String sir) {
		   return impsubrepo.findImportSubBysironly(cid, bid, sir);
	  }
	  
	  
	  
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
	  
	  @GetMapping("/history/{cid}/{bid}/{rid}/{ser}")
	  public List<ImportSub_History> getalldata(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("rid") String rid,@PathVariable("ser") String ser){
		  return importsubhisrepo.getalldata(cid, bid, rid, ser);
	  }
	  
		@GetMapping("/getexpdata1/{cid}/{bid}/{reqid}")
		public List<ExportSub> getReqId1(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("reqid") String reqid) {
			return  exportrepo.findRequestId1(cid, bid, reqid);
		}
		
		
		  @GetMapping("/checkimportpartydata/{cid}/{bid}/{exporter}")
		  public List<ImportSub> checkpartydata(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("exporter") String exporter){
			  return impsubrepo.getalldatabyparty(cid,bid,exporter);
		  }
		 
		  
		  @GetMapping("/checkimportpartydata1/{cid}/{bid}/{exporter}")
		  public List<ImportSub> checkpartydata1(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("exporter") String exporter){
			  return impsubrepo.getalldatabyparty1(cid,bid,exporter);
		  }
		  
		  
		  @GetMapping("/alldatabyparty/{cid}/{bid}/{exporter}")
			public List<ImportSub> getAlldata3(@PathVariable("cid") String cid, @PathVariable("bid") String bid,@PathVariable("exporter") String exporter) {
				return impsubrepo.getalltocheckLGD3(cid, bid,exporter);
			}
			
			
			@GetMapping("/alldatabycha/{cid}/{bid}/{exporter}")
			public List<ImportSub> getAlldata4(@PathVariable("cid") String cid, @PathVariable("bid") String bid,@PathVariable("exporter") String exporter) {
				return impsubrepo.getalltocheckLGD5(cid, bid,exporter);
			}
			
			@GetMapping("/alllgddatabyparty/{cid}/{bid}/{exporter}")
			public List<ImportSub> getAlldatar(@PathVariable("cid") String cid, @PathVariable("bid") String bid,@PathVariable("exporter") String exporter) {
				return impsubrepo.getalltocheckLGD4(cid, bid,exporter);
			}
			
			
			@GetMapping("/alllgddatabycha/{cid}/{bid}/{exporter}")
			public List<ImportSub> getAlldatar1(@PathVariable("cid") String cid, @PathVariable("bid") String bid,@PathVariable("exporter") String exporter) {
				return impsubrepo.getalltocheckLGD6(cid, bid,exporter);
	  
}
			
//			
//			@GetMapping("/search")
//			public List<ImportSub> searchImports(@RequestParam(name = "searchValue", required = false) String searchValue,
//					@RequestParam(name = "companyid", required = false) String companyid,
//					@RequestParam(name = "branchId", required = false) String branchId,
//					@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
//					@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//					@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//
//				
//		
//				
//				
//				return impsubrepo.findByAttributes4(companyid, branchId,dgdcStatus,startDate, endDate,searchValue);
//			}
		  
		  
		  
//		  @GetMapping("/search1")
//			public List<ImportSub> searchImports2(@RequestParam(name = "searchValue", required = false) String searchValue,
//					@RequestParam(name = "companyid", required = false) String companyid,
//					@RequestParam(name = "branchId", required = false) String branchId,
//					@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
//					@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//					@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//
//				
//		
//				
//				
//				return impsubrepo.findByAttributes5(companyid, branchId,dgdcStatus,startDate, endDate,searchValue);
//			}
		  
		  
//ImportSubController
		  
		  @PostMapping("/transactionexcel")
			public ResponseEntity<byte[]> generateTransactionEXCEL(@RequestBody List<ImportSub> import1) {
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
			        headerRow.createCell(4).setCellValue("Importer Name");
			        headerRow.createCell(5).setCellValue("No Of Packages");
			        headerRow.createCell(6).setCellValue("Invoice No");
			        headerRow.createCell(7).setCellValue("Exporter Name");
			        headerRow.createCell(8).setCellValue("BE Request ID");
			        headerRow.createCell(9).setCellValue("Current Status");
			     
			       

			        // Apply style to header cells
			        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
			            headerRow.getCell(i).setCellStyle(headerStyle);
			        }

			        // Create data rows
			        int rowNum = 1;

			        for (ImportSub importObj : import1) {
//			            BigDecimal gross = importObj.getGrossWeight();
//			            // Replace with your actual BigDecimal value
//			            double convertedValue = gross.doubleValue();
			            
			        	 // Your input Date object
			            Date inputDate = importObj.getSirDate();

			            // Create a SimpleDateFormat object with the desired format
			            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			            // Format the Date object
			            String formattedDate = sdf.format(inputDate);
			            
			        
			            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(importObj.getCompanyId(), importObj.getBranchId(), importObj.getExporter());
			            Row row = sheet.createRow(rowNum++);
			            row.createCell(0).setCellValue(rowNum - 1);
			            row.createCell(1).setCellValue(importObj.getSirNo());
			            row.createCell(2).setCellValue(formattedDate);
			            row.createCell(3).setCellValue(importObj.getImportType());
			            row.createCell(4).setCellValue(party.getPartyName());
			            row.createCell(5).setCellValue(importObj.getNop());
			            row.createCell(6).setCellValue(importObj.getInvoiceNo());
			            row.createCell(7).setCellValue(importObj.getExporterName());
			            row.createCell(8).setCellValue(importObj.getRequestId());
			            row.createCell(9).setCellValue(importObj.getDgdcStatus());
			        
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
			
			@GetMapping("/searchnonlgd")
			public List<Object[]> searchNonLGDImports(@RequestParam(name = "searchValue", required = false) String searchValue,
					@RequestParam(name = "companyid", required = false) String companyid,
					@RequestParam(name = "branchId", required = false) String branchId,
					@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
					@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
					@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
					@RequestParam(name = "loginid", required = false) String loginid,
					@RequestParam(name = "logintype", required = false) String logintype) {

				
				if("Party".equals(logintype)) {
					return impsubrepo.findByPartyAttributes(companyid, branchId, loginid,dgdcStatus, startDate, endDate, searchValue);
				}
				
				else {
					return impsubrepo.findByCHAAttributes(companyid, branchId, loginid,dgdcStatus, startDate, endDate, searchValue);
				}

				
				
			}

			@GetMapping("/searchlgd")
			public List<Object[]> searchLGDImports(@RequestParam(name = "searchValue", required = false) String searchValue,
					@RequestParam(name = "companyid", required = false) String companyid,
					@RequestParam(name = "branchId", required = false) String branchId,
					@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
					@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
					@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
					@RequestParam(name = "loginid", required = false) String loginid,
					@RequestParam(name = "logintype", required = false) String logintype) {

				if("Party".equals(logintype)) {
					return impsubrepo.findByLGDPartyAttributes(companyid, branchId, loginid,dgdcStatus, startDate, endDate, searchValue);
				}
				
				else {
					return impsubrepo.findByLGDCHAAttributes(companyid, branchId, loginid,dgdcStatus, startDate, endDate, searchValue);
				}

				
				
			
			}
			
			
			@GetMapping("/getDGDCStatus/{cid}/{bid}/{req}/{ser}")
			public String findStatus(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("req") String req,@PathVariable("ser") String ser) {
				
				return impsubrepo.findStatus(cid, bid, req, ser);
			}
			
			
			
			
			
			@PostMapping("/subimpTransactionReport")
		    public ResponseEntity<String> generateExportTPPdf(
		            @RequestParam("companyId") String companyId,
		            @RequestParam("branchId") String branchId,@RequestParam("selecteddate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("dgdcStatus") String dgdcStatus) {
		        try {
		        	
	                Branch branch = branchRepo.findByBranchIdWithCompanyId(companyId, branchId);
	                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	               String tpdate = format.format(date);
	               
	               
	               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	       		String formattedDate = sdf.format(date);
	       		

	       	 
	               List<Object[]> data = importSubService.findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(formattedDate, companyId, branchId, dgdcStatus);
	               
	               int totalNop = 0;
	           
	              
		            if (!data.isEmpty()) {
		                for (Object[] objArray : data) {
		                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
		                    
		                    String date1 = format1.format(objArray[0]);
		                    objArray[0] = date1;
		                    System.out.println("objArray[6] "+objArray[6]);
		                    String partyname = partyRepository.findPartyNameByKeys(companyId, branchId, String.valueOf(objArray[3]));
                            totalNop += Integer.parseInt(String.valueOf(objArray[4]));
                            
		                    if(!partyname.isBlank() && partyname != null) {
		                    	objArray[3] = partyname;
		                    }

		                   
		                }
		            }

		            Context context = new Context();
		            context.setVariable("item", data);
		            context.setVariable("gst", branch.getGST_No());
		            context.setVariable("tpdate", tpdate);
		            context.setVariable("dgdcStatus", dgdcStatus);
		            
		            context.setVariable("totalNop", totalNop);
		            context.setVariable("totalRecord", data.size());

		            String htmlContent = templateEngine.process("Sub_Imp_Transaction", context);

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



 @PostMapping("/transactionexcel/{cid}/{bid}")
			public ResponseEntity<byte[]> generateTransactionEXCEL(@RequestBody List<Object[]> import1,@PathVariable("cid") String cid,@PathVariable("bid") String bid) {
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
			        headerRow.createCell(4).setCellValue("Importer Name");
			        headerRow.createCell(5).setCellValue("No Of Packages");
			        headerRow.createCell(6).setCellValue("Invoice No");
			        headerRow.createCell(7).setCellValue("Exporter Name");
			        headerRow.createCell(8).setCellValue("BE Request ID");
			        headerRow.createCell(9).setCellValue("Current Status");
			     
			       

			        // Apply style to header cells
			        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
			            headerRow.getCell(i).setCellStyle(headerStyle);
			        }

			        // Create data rows
			        int rowNum = 1;

			        for (Object[] importObj : import1) {
//			            BigDecimal gross = importObj.getGrossWeight();
//			            // Replace with your actual BigDecimal value
//			            double convertedValue = gross.doubleValue();
			            
			        	 // Your input Date object
			            

			            // Create a SimpleDateFormat object with the desired format
			            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			            // Format the Date object
			            String formattedDate = sdf.format(importObj[0]);
			            
			        
			            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(cid, bid, String.valueOf(importObj[3]));
			            Row row = sheet.createRow(rowNum++);
			            row.createCell(0).setCellValue(rowNum - 1);
			            row.createCell(1).setCellValue(String.valueOf(importObj[1]));
			            row.createCell(2).setCellValue(formattedDate);
			            row.createCell(3).setCellValue(String.valueOf(importObj[2]));
			            row.createCell(4).setCellValue(party.getPartyName());
			            row.createCell(5).setCellValue(String.valueOf(importObj[4]));
			            row.createCell(6).setCellValue(String.valueOf(importObj[5]));
			            if(importObj[6] != null) {
			            	row.createCell(7).setCellValue(String.valueOf(importObj[6]));
			            }
			            
			            row.createCell(8).setCellValue(String.valueOf(importObj[7]));
			            row.createCell(9).setCellValue(String.valueOf(importObj[8]));
			        
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
			
			
			
			
			
}
