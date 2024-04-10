package com.cwms.controller;

import java.io.ByteArrayOutputStream;
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

import com.cwms.entities.Branch;
import com.cwms.entities.Company;
import com.cwms.entities.ExportSub;
import com.cwms.entities.ExportSub_History;
import com.cwms.entities.Gate_In_Out;
import com.cwms.entities.Party;
import com.cwms.helper.FileUploadProperties;
import com.cwms.repository.BranchRepo;
import com.cwms.repository.CompanyRepo;
import com.cwms.repository.ExportSubRepository;
import com.cwms.repository.ExportSub_Historyrepo;
import com.cwms.repository.Gate_In_out_Repo;
import com.cwms.repository.PartyRepository;
import com.cwms.repository.ProcessNextIdRepository;
import com.cwms.service.ExportSubService;
import com.cwms.service.ImageService;
import com.cwms.service.ProcessNextIdService;

import jakarta.transaction.Transactional;
import java.util.Base64;
@CrossOrigin("*")
@RestController
@RequestMapping("/exportsub")
public class ExportSubController {
	
	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private ExportSubRepository expsubrepo;
	
	@Autowired(required = true)
	public ProcessNextIdRepository processNextIdRepository;
	
	@Autowired
	private CompanyRepo companyRepo;
	
	@Autowired
	private Gate_In_out_Repo gateinoutrepo;
	
	@Autowired
	private ExportSub_Historyrepo exportsubhistory;
	
	@Autowired
	public FileUploadProperties FileUploadProperties;
	
	@Autowired
	private BranchRepo branchRepo;
	
	@Autowired
	private ExportSubService exportSubService;
	
	
	@Autowired
	private ProcessNextIdService nextservice;
	
	@Autowired
	private PartyRepository partyRepository;
	
	@Autowired
	private ImageService imageService;
	
	
	
	@GetMapping("/findBySerAndReq/{cid}/{bid}/{req}/{ser}")
	public ExportSub fidByReqandSer(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("req") String reqid,@PathVariable("ser") String ser) {
		return expsubrepo.findExportSubByser(cid, bid, ser, reqid);
	}
	
	
	@GetMapping("/searchExport")
	public List<Object[]> searchImports(@RequestParam(name = "searchValue", required = false) String searchValue,
			@RequestParam(name = "companyid", required = false) String companyid,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		

		
		
		return expsubrepo.findByAttributes1(companyid, branchId,dgdcStatus,startDate, endDate,searchValue);
	}
	
	
	@GetMapping("/all/{cid}/{bid}")
	public List<ExportSub> getAlldata(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {
		return expsubrepo.getall(cid, bid);
	}
	
	@GetMapping("/checkdata/{cid}/{bid}")
	public List<ExportSub> getAlldataforcheck(@PathVariable("cid") String cid, @PathVariable("bid") String bid) {
		return expsubrepo.getallFORCHECK(cid, bid);
	}

//	@PostMapping("/insertdata/{id}/{cid}/{bid}")
//	public ExportSub singlesavedata( @RequestBody ExportSub expsub,@PathVariable("id") String id,@PathVariable("cid") String cid,@PathVariable("bid") String bid) throws Exception {
////		ExportSub sub = expsubrepo.findRequestId(cid, bid, expsub.getRequestId());
////		System.out.println("Sub "+sub);
////		if(sub != null) {
////			throw new Exception("RequestId already present");
////		}
//		
//		String nextid = nextservice.autoIncrementSubExpId();
//		String nexttransid = nextservice.autoIncrementSubExpTransId();
//		expsub.setCompanyId(cid);
//		expsub.setBranchId(bid);
//		expsub.setSerNo(nextid);
//		expsub.setExpSubId(nexttransid);
//		expsub.setGatePassStatus("N");
//		expsub.setCreatedBy(id);
//		expsub.setCreatedDate(new Date());
//		expsub.setStatus('N');
//		BigDecimal big = new BigDecimal("0.0");
//		expsub.setReceived_wt(big);
//		expsub.setImposePenaltyAmount(0);
//		expsub.setDgdcStatus("Handed over to DGDC SEEPZ");
//		expsub.setNsdlStatus("Pending");
//	    expsub.setNoc(0);
//	    expsub.setForwardedStatus("N");
//	    expsub.setDgdc_cargo_in_scan(0);
//	    expsub.setDgdc_cargo_out_scan(0);
//	    expsub.setDgdc_seepz_in_scan(0);
//	    expsub.setDgdc_seepz_out_scan(0);
//		expsub.setSerDate(new Date());
//		
//		for(int i=1;i<=expsub.getNop();i++) {
//    		String srNo = String.format("%04d", i);
//    		Gate_In_Out gateinout = new Gate_In_Out();
//    		gateinout.setCompanyId(cid);
//    		gateinout.setBranchId(bid);
//    		gateinout.setNop(expsub.getNop());
//    		gateinout.setErp_doc_ref_no(expsub.getRequestId());
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
//		ExportSub_History exporthistory = new ExportSub_History();
//		exporthistory.setCompanyId(cid);
//		exporthistory.setBranchId(bid);
//		exporthistory.setNewStatus("Handed over to DGDC SEEPZ");
//		exporthistory.setOldStatus("Pending");
//		exporthistory.setRequestId(expsub.getRequestId());
//		exporthistory.setTransport_Date(new Date());
//		exporthistory.setSerNo(nextid);
//		exporthistory.setUpdatedBy(id);
//		
//		exportsubhistory.save(exporthistory);
//		
//		return expsubrepo.save(expsub);
//	}
	
	
	
	@Transactional
	@PostMapping("/insertdata/{id}/{cid}/{bid}")
	public ExportSub singlesavedata( @RequestBody ExportSub expsub,@PathVariable("id") String id,@PathVariable("cid") String cid,@PathVariable("bid") String bid) throws Exception {
//		ExportSub sub = expsubrepo.findRequestId(cid, bid, expsub.getRequestId());
//		System.out.println("Sub "+sub);
//		if(sub != null) {
//			throw new Exception("RequestId already present");
//		}
		
		synchronized (this) {
			String IMPTransId = processNextIdRepository.findNextsubexpid();

	        int lastNextNumericId = Integer.parseInt(IMPTransId.substring(4));

	        int nextNumericNextID = lastNextNumericId + 1;

	        String nextid = String.format("D-EX%06d", nextNumericNextID);

	        // Update the Next_Id directly in the database using the repository
	        processNextIdRepository.updateNexsubexpid(nextid);

	       
			
			String IMPTransId1 = processNextIdRepository.findNextsubexptransid();

	        int lastNextNumericId1 = Integer.parseInt(IMPTransId1.substring(3));

	        int nextNumericNextID1 = lastNextNumericId1 + 1;

	        String nexttransid = String.format("SER%05d", nextNumericNextID1);

	        // Update the Next_Id directly in the database using the repository
	        processNextIdRepository.updateNexsubexptransid(nexttransid);
			
			
			expsub.setCompanyId(cid);
			expsub.setBranchId(bid);
			expsub.setSerNo(nextid);
			expsub.setExpSubId(nexttransid);
			expsub.setGatePassStatus("N");
			expsub.setCreatedBy(id);
			expsub.setCreatedDate(new Date());
			expsub.setStatus('N');
			BigDecimal big = new BigDecimal("0.0");
			expsub.setReceived_wt(big);
			expsub.setImposePenaltyAmount(0);
			expsub.setDgdcStatus("Handed over to DGDC SEEPZ");
			expsub.setNsdlStatus("Pending");
		    expsub.setNoc(0);
		    expsub.setForwardedStatus("N");
		    expsub.setDgdc_cargo_in_scan(0);
		    expsub.setDgdc_cargo_out_scan(0);
		    expsub.setDgdc_seepz_in_scan(0);
		    expsub.setDgdc_seepz_out_scan(0);
			expsub.setSerDate(new Date());
			
			for(int i=1;i<=expsub.getNop();i++) {
	    		String srNo = String.format("%04d", i);
	    		Gate_In_Out gateinout = new Gate_In_Out();
	    		gateinout.setCompanyId(cid);
	    		gateinout.setBranchId(bid);
	    		gateinout.setNop(expsub.getNop());
	    		gateinout.setErp_doc_ref_no(expsub.getRequestId());
	    		gateinout.setDoc_ref_no(nextid);
	    		gateinout.setSr_No(nextid+srNo);
	    		gateinout.setDgdc_cargo_in_scan("N");
	    		gateinout.setDgdc_cargo_out_scan("N");
	    		gateinout.setDgdc_seepz_in_scan("N");
	    		gateinout.setDgdc_seepz_out_scan("N");
	    		
	    		gateinoutrepo.save(gateinout);
	    	}
			
			ExportSub_History exporthistory = new ExportSub_History();
			exporthistory.setCompanyId(cid);
			exporthistory.setBranchId(bid);
			exporthistory.setNewStatus("Handed over to DGDC SEEPZ");
			exporthistory.setOldStatus("Pending");
			exporthistory.setRequestId(expsub.getRequestId());
			exporthistory.setTransport_Date(new Date());
			exporthistory.setSerNo(nextid);
			exporthistory.setUpdatedBy(id);
			
			exportsubhistory.save(exporthistory);
			
			return expsubrepo.save(expsub);
		}
		
		
	}
	
	@GetMapping("/byid/{cid}/{bid}/{expsubid}/{reqid}")
	public ExportSub getdatabyid(@PathVariable("cid") String companyId,@PathVariable("bid") String branchId,@PathVariable("expsubid") String expsubid,@PathVariable("reqid") String reqId) {
		return expsubrepo.findExportSub(companyId, branchId, expsubid, reqId);
	}

//	@Transactional
//	@PostMapping("/updateData/{id}")
//    public ExportSub updateImportSub(@PathVariable("id") String id, @RequestBody ExportSub updatedImportSub) {
//		    updatedImportSub.setEditedBy(id);
//		    updatedImportSub.setEditedDate(new Date());
//            return expsubrepo.save(updatedImportSub);
//        
//    }
	
	
	@Transactional
	@PostMapping("/updateData/{id}")
    public ExportSub updateImportSub(@PathVariable("id") String id, @RequestBody ExportSub updatedImportSub) {
		    updatedImportSub.setEditedBy(id);
		    updatedImportSub.setEditedDate(new Date());
		    
		    int sr_no = gateinoutrepo.findbysr2(updatedImportSub.getCompanyId(), updatedImportSub.getBranchId(), updatedImportSub.getRequestId(),updatedImportSub.getSerNo());
		    
		   if(sr_no != updatedImportSub.getNop()) {
			   List<Gate_In_Out> gateinout1 = gateinoutrepo.findbysr1(updatedImportSub.getCompanyId(), updatedImportSub.getBranchId(), updatedImportSub.getRequestId(),updatedImportSub.getSerNo());
				
			    gateinoutrepo.deleteAll(gateinout1);
				for(int i=1;i<=updatedImportSub.getNop();i++) {
		    		String srNo = String.format("%04d", i);
		    		Gate_In_Out gateinout = new Gate_In_Out();
		    		gateinout.setCompanyId(updatedImportSub.getCompanyId());
		    		gateinout.setBranchId(updatedImportSub.getBranchId());
		    		gateinout.setNop(updatedImportSub.getNop());
		    		gateinout.setErp_doc_ref_no(updatedImportSub.getRequestId());
		    		gateinout.setDoc_ref_no(updatedImportSub.getSerNo());
		    		gateinout.setSr_No(updatedImportSub.getSerNo()+srNo);
		    		gateinout.setDgdc_cargo_in_scan("N");
		    		gateinout.setDgdc_cargo_out_scan("N");
		    		gateinout.setDgdc_seepz_in_scan("N");
		    		gateinout.setDgdc_seepz_out_scan("N");
		    		
		    		gateinoutrepo.save(gateinout);
		    	}
		   }
            return expsubrepo.save(updatedImportSub);
        
    }
	
	@GetMapping("/getdata")
	public List<ExportSub> getdata() {
		return expsubrepo.findAll();
		
	}
	
	
	
	@PostMapping(value="/updatedeliverydata",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ExportSub updateDeliveryUpdate(
	    @RequestParam("file") MultipartFile file,
	    @RequestBody ExportSub updatedImportSub
	) {
	    if (!file.isEmpty()) {
	        try {
	            // Save the file to a folder (you need to specify the folder path)
	            String folderPath = "D:/Log"; // Update with your folder path
	            String fileName = file.getOriginalFilename();
	            String filePath = folderPath + "/" + fileName;
	            file.transferTo(new File(filePath));
	            updatedImportSub.setStatus_document(filePath);

	            // Process requestParams and update ExportSub fields as needed
	            // Example: updatedImportSub.setCompanyId(requestParams.get("companyId"));

	            // Save the updatedExportSub object to your database
	            ExportSub savedExportSub = expsubrepo.save(updatedImportSub);
	            return savedExportSub;
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error saving file and data");
	        }
	    } else {
	        throw new IllegalArgumentException("File is empty");
	    }
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

	    this.expsubrepo.updateData(nsdl, uniqueFilePath, cid, bid, expid, reqid);
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

	
	 @GetMapping("/download/{cid}/{bid}/{expid}/{reqid}")
	    public ResponseEntity<byte[]> downloadImage(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("expid") String expid,@PathVariable("reqid") String reqid) throws IOException {
	        // Retrieve the image path from the database based on imageId
	    	ExportSub expsub = expsubrepo.findExportSub(cid, bid, expid, reqid);
	        String imagePath = expsub.getStatus_document();

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
	    
	    
		@GetMapping("/search")
		public List<ExportSub> searchImports(
		    @RequestParam(name = "companyid", required = false) String companyid,
		    @RequestParam(name = "branchId", required = false) String branchId,
		    @RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
		    @RequestParam(name = "startDate", required = false)
		    @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
		    @RequestParam(name = "endDate", required = false)
		    @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
		) {
			

		    return expsubrepo.findByAttributes(
		    		companyid,branchId,dgdcStatus, startDate, endDate);
		}
		
		
//	 @GetMapping("/exportSubTransaction")
//		    public ResponseEntity<List<ExportSub>> findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(
//					@RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
//		            @RequestParam("companyId") String companyId,
//		            @RequestParam("branchId") String branchId,
//		            @RequestParam("dgdcStatus") String dgdcStatus) {
//
//			 
//			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				String formattedDate = sdf.format(serDate);
//				
//			 System.out.println(formattedDate);
//			 
//		        List<ExportSub> exportsSub = exportSubService.findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(formattedDate, companyId, branchId, dgdcStatus);
//		        
//		        System.out.println(exportsSub);
//		        if (exportsSub.isEmpty()) {
//		            return ResponseEntity.notFound().build();
//		            
//		        }
//
//		        return ResponseEntity.ok(exportsSub);
//		    }

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
        
    
			@GetMapping("/findSubContractData")
		    public List<ExportSub> findExportSubData(
		            @RequestParam("companyId") String companyId,
		            @RequestParam("branchId") String branchId,
		            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
					@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
		           
		            @RequestParam("exporter") String exporter
		    ) {
		        return expsubrepo.findImportAllData(companyId, branchId, startDate, endDate, exporter);
		    }

		  @GetMapping("/findByRequestId")
		    public List<ExportSub> findByRequestId(
		            @RequestParam("companyId") String companyId,
		            @RequestParam("branchId") String branchId,
		            @RequestParam("requestId") String requestId
		    ) {
		        return expsubrepo.findRequestIdData(companyId, branchId, requestId);
		    }
		  
//		  @GetMapping("/findExportSubAllData")
//			public List<ExportSub> findExportSubData(@RequestParam("companyId") String companyId,
//					@RequestParam("branchId") String branchId,
//					@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startDate,
//					@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endDate
//
//			) {
//				return expsubrepo.findExportSubAllData(companyId, branchId, startDate, endDate);
//			}
//		  
		  
		  @GetMapping("/findExportSubAllData")
			public List<ExportSub> findExportSubData(@RequestParam("companyId") String companyId,
					@RequestParam("branchId") String branchId,
					@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
					@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
			) 
		  	{			  
//				System.out.println("companyId "+companyId +" branchId "+branchId + "startDate "+startDate+"endDate "+endDate);
//		    	
//		    	System.out.println("Export Sub Request Received");
			  
				return expsubrepo.findExportSubAllData(companyId, branchId, startDate, endDate);
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
		  
		  @GetMapping("/byser/{cid}/{bid}/{ser}")
		  public ExportSub getdatabyser(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("ser") String ser) {
			   return expsubrepo.findExportSubByseronly(cid, bid, ser);
		  }
		  
		  @GetMapping("/history/{cid}/{bid}/{rid}/{ser}")
		  public List<ExportSub_History> getalldata(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("rid") String rid,@PathVariable("ser") String ser){
			  return exportsubhistory.getalldata(cid, bid, rid, ser);
		  }
		  
		  
		  @GetMapping("/history1/{cid}/{bid}/{rid}")
		  public List<ExportSub_History> getalldata1(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("rid") String rid){
			  return exportsubhistory.getSingledata1(cid, bid, rid);
		  }
		  
		  @GetMapping("/allhistory/{cid}/{bid}/{reqid}")
		  public List<ExportSub> getalldatabyreqid(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("reqid") String reqid){
			  return expsubrepo.findRequestId1(cid,bid,reqid);
		  }
		  
		  
		  @GetMapping("/checkpartydata/{cid}/{bid}/{exporter}")
		  public List<ExportSub> checkpartydata(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("exporter") String exporter){
			  return expsubrepo.getalldatabyparty(cid,bid,exporter);
		  }
		  
		  
		  @GetMapping("/alldatabyparty/{cid}/{bid}/{exporter}")
			public List<ExportSub> getAlldata3(@PathVariable("cid") String cid, @PathVariable("bid") String bid,@PathVariable("exporter") String exporter) {
				return expsubrepo.getall1(cid, bid,exporter);
			}
			
			@GetMapping("/alldatabyCHA/{cid}/{bid}/{exporter}")
			public List<ExportSub> getAlldata4(@PathVariable("cid") String cid, @PathVariable("bid") String bid,@PathVariable("exporter") String exporter) {
				return expsubrepo.getall2(cid, bid,exporter);
			}
			
//			@GetMapping("/searchExport")
//			public List<ExportSub> searchImports(@RequestParam(name = "searchValue", required = false) String searchValue,
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
//				return expsubrepo.findByAttributes6(companyid, branchId,dgdcStatus,startDate, endDate,searchValue);
//			}
			
			
			
			//ExportSubController
			

			  @PostMapping("/transactionexcel")
				public ResponseEntity<byte[]> generateTransactionEXCEL(@RequestBody List<ExportSub> import1) {
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
				        headerRow.createCell(1).setCellValue("SER No");
				        headerRow.createCell(2).setCellValue("SER Date");
				        headerRow.createCell(3).setCellValue("Parcel Type");
				        headerRow.createCell(4).setCellValue("Exporter Name");
				        headerRow.createCell(5).setCellValue("No Of Packages");
				        headerRow.createCell(6).setCellValue("Invoice No");
				        headerRow.createCell(7).setCellValue("Challan No");
				        headerRow.createCell(8).setCellValue("BE Request ID");
				        headerRow.createCell(9).setCellValue("Current Status");
				     
				       

				        // Apply style to header cells
				        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				            headerRow.getCell(i).setCellStyle(headerStyle);
				        }

				        // Create data rows
				        int rowNum = 1;

				        for (ExportSub importObj : import1) {
//				            BigDecimal gross = importObj.getGrossWeight();
//				            // Replace with your actual BigDecimal value
//				            double convertedValue = gross.doubleValue();
				            
				        	 // Your input Date object
				            Date inputDate = importObj.getSerDate();

				            // Create a SimpleDateFormat object with the desired format
				            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

				            // Format the Date object
				            String formattedDate = sdf.format(inputDate);
				            
				        
				            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(importObj.getCompanyId(), importObj.getBranchId(), importObj.getExporter());
				            Row row = sheet.createRow(rowNum++);
				            row.createCell(0).setCellValue(rowNum - 1);
				            row.createCell(1).setCellValue(importObj.getSerNo());
				            row.createCell(2).setCellValue(formattedDate);
				            row.createCell(3).setCellValue(importObj.getRemarks());
				            row.createCell(4).setCellValue(party.getPartyName());
				            row.createCell(5).setCellValue(importObj.getNop());
				            row.createCell(6).setCellValue(importObj.getInvoiceNo());
				            row.createCell(7).setCellValue(importObj.getChallanNo());
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
				
				
//				@GetMapping("/findBySerAndReq/{cid}/{bid}/{req}/{ser}")
//				public ExportSub fidByReqandSer(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("req") String reqid,@PathVariable("ser") String ser) {
//					return expsubrepo.findExportSubByser(cid, bid, ser, reqid);
//				}
				
				
//				@GetMapping("/searchExport")
//				public List<Object[]> searchImports(@RequestParam(name = "searchValue", required = false) String searchValue,
//						@RequestParam(name = "companyid", required = false) String companyid,
//						@RequestParam(name = "branchId", required = false) String branchId,
//						@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
//						@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//						@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//
//					
//			
//					
//					
//					return expsubrepo.findByAttributes1(companyid, branchId,dgdcStatus,startDate, endDate,searchValue);
//				}
				
				
				@GetMapping("/searchbyloginExport")
				public List<Object[]> searchByLogintypeExport(@RequestParam(name = "searchValue", required = false) String searchValue,
						@RequestParam(name = "companyid", required = false) String companyid,
						@RequestParam(name = "branchId", required = false) String branchId,
						@RequestParam(name = "dgdcStatus", required = false) String dgdcStatus,
						@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
						@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
						@RequestParam(name = "loginid", required = false) String loginid,
						@RequestParam(name = "logintype", required = false) String logintype) {

					
			
					if("Party".equals(logintype)) {
						return expsubrepo.findByPartyAttributes(companyid, branchId,loginid,dgdcStatus,startDate, endDate,searchValue);
					}
					
					else {
						return expsubrepo.findByCHAAttributes(companyid, branchId,loginid,dgdcStatus,startDate, endDate,searchValue);
					}
					
				
				}
				
				@GetMapping("/getDGDCStatus/{cid}/{bid}/{req}/{ser}")
				public String findStatus(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("req") String req,@PathVariable("ser") String ser) {
					
					return expsubrepo.findStatus(cid, bid, req, ser);
				}
				
				
				
				
				
				@GetMapping("/exportSubTransaction")
			    public ResponseEntity<List<Object[]>> findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(
						@RequestParam("serDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date serDate,
			            @RequestParam("companyId") String companyId,
			            @RequestParam("branchId") String branchId,
			            @RequestParam("dgdcStatus") String dgdcStatus) {

				 
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String formattedDate = sdf.format(serDate);
					
				 System.out.println(formattedDate);
				 
			        List<Object[]> exportsSub = exportSubService.findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(formattedDate, companyId, branchId, dgdcStatus);
			        
			        System.out.println(exportsSub);
			        if (exportsSub.isEmpty()) {
			            return ResponseEntity.notFound().build();
			            
			        }

			        return ResponseEntity.ok(exportsSub);
			    }


	@PostMapping("/subexpTransactionReport")
					    public ResponseEntity<String> generateExportTPPdf(
					            @RequestParam("companyId") String companyId,
					            @RequestParam("branchId") String branchId,@RequestParam("selecteddate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("dgdcStatus") String dgdcStatus) {
					        try {
					        	
				                Branch branch = branchRepo.findByBranchIdWithCompanyId(companyId, branchId);
				                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				               String tpdate = format.format(date);
				               
				               
				               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				       		String formattedDate = sdf.format(date);
				       		

				       	 
				               List<Object[]> data = exportSubService.findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(formattedDate, companyId, branchId, dgdcStatus);
				               
				               int totalNop = 0;
				           
				              
					            if (!data.isEmpty()) {
					                for (Object[] objArray : data) {
					                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
					                    
					                    String date1 = format1.format(objArray[0]);
					                    objArray[0] = date1;
					                   
					                    String partyname = partyRepository.findPartyNameByKeys(companyId, branchId, String.valueOf(objArray[2]));
			                            totalNop += Integer.parseInt(String.valueOf(objArray[3]));
			                            
					                    if(!partyname.isBlank() && partyname != null) {
					                    	objArray[2] = partyname;
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

					            String htmlContent = templateEngine.process("Sub_Exp_Transaction", context);

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
					        headerRow.createCell(1).setCellValue("SER No");
					        headerRow.createCell(2).setCellValue("SER Date");
					        headerRow.createCell(3).setCellValue("Parcel Type");
					        headerRow.createCell(4).setCellValue("Exporter Name");
					        headerRow.createCell(5).setCellValue("No Of Packages");
					        headerRow.createCell(6).setCellValue("Invoice No");
					        headerRow.createCell(7).setCellValue("Challan No");
					        headerRow.createCell(8).setCellValue("BE Request ID");
					        headerRow.createCell(9).setCellValue("Current Status");
					     
					       

					        // Apply style to header cells
					        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
					            headerRow.getCell(i).setCellStyle(headerStyle);
					        }

					        // Create data rows
					        int rowNum = 1;

					        for (Object[] importObj : import1) {
//					            BigDecimal gross = importObj.getGrossWeight();
//					            // Replace with your actual BigDecimal value
//					            double convertedValue = gross.doubleValue();
					            
					        	   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

						            // Format the Date object
						            String formattedDate = sdf.format(importObj[0]);
					            
					        
					            Party party = partyRepository.findByCompanyIdAndBranchIdAndPartyId(cid, bid, String.valueOf(importObj[2]));
					            Row row = sheet.createRow(rowNum++);
					            row.createCell(0).setCellValue(rowNum - 1);
					            row.createCell(1).setCellValue(String.valueOf(importObj[1]));
					            row.createCell(2).setCellValue(formattedDate);
					            row.createCell(3).setCellValue("");
					            row.createCell(4).setCellValue(party.getPartyName());
					            row.createCell(5).setCellValue(String.valueOf(importObj[3]));
					            row.createCell(6).setCellValue(String.valueOf(importObj[4]));
					            row.createCell(7).setCellValue(String.valueOf(importObj[5]));
					            row.createCell(8).setCellValue(String.valueOf(importObj[6]));
					            row.createCell(9).setCellValue(String.valueOf(importObj[7]));
					        
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
