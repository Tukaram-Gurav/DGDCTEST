package com.cwms.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cwms.entities.Combined_Representative;
import com.cwms.entities.RepresentParty;
import com.cwms.repository.Combined_RepresentativeRepository;
import com.cwms.repository.RepresentPartyRepository;
import com.cwms.service.RepsentativeService;
@RestController
@RequestMapping("NewReprentative")
@CrossOrigin("*")
public class RepresentativeController {
	@Autowired
	public com.cwms.helper.ImageHelper ImageHelper;
	
	
	@Autowired
	public RepsentativeService RepsentativeService;
	@Autowired
	public RepresentPartyRepository representPartyRepository;
	
	
	@Autowired
	private Combined_RepresentativeRepository combinerpresentrepo;
	
	@GetMapping("/{CompId}/{BranchId}/{UserType}/Bytype")
	public List<RepresentParty> getAll(@PathVariable("CompId") String CompId, @PathVariable("BranchId") String BranchId
			,@PathVariable("UserType") String UserType)
	{	
		return RepsentativeService.findAlRepositary(CompId, BranchId, UserType);
		
	}
	
	@GetMapping("/{CompId}/{BranchId}/{userId}/{reprentativeId}/Byid")
	public RepresentParty getByrepresentative(@PathVariable("CompId") String CompId, @PathVariable("BranchId") String BranchId
			,@PathVariable("reprentativeId") String reprentativeId,@PathVariable("userId") String userId)
	{
		
		
		System.out.println("*************Representative Id ************** "+reprentativeId);
		
		return RepsentativeService.findByRepresentativeId(CompId, BranchId, userId,reprentativeId);
		
		
	}
	
//	@GetMapping("/{CompId}/{BranchId}/{UserId}/ByUserID")
//	public List<RepresentParty> getAllByUserId(@PathVariable("CompId") String CompId, @PathVariable("BranchId") String BranchId
//			,@PathVariable("UserId") String UserId)
//	{	
//		return RepsentativeService.findAlRepositaryByUserID(CompId, BranchId, UserId);
//		
//	

	@GetMapping("/{CompId}/{BranchId}/{UserId}/ByUserID")
	public List<RepresentParty> getAllByUserId(@PathVariable("CompId") String cid, @PathVariable("BranchId") String bid
			,@PathVariable("UserId") String uid)
	{	
		 // Retrieve the Combined_Representative by company ID, branch ID, and user ID
	    List<Combined_Representative> combine = combinerpresentrepo.getByIds(cid, bid, uid);
	    System.out.println("combine " + combine);

	    if (combine != null && !combine.isEmpty()) {
	       
	    List<RepresentParty> represent = new ArrayList<RepresentParty>();

	       for(Combined_Representative com : combine) {
	    	   List<Combined_Representative> combineList = combinerpresentrepo.getByERPId(cid, bid, com.getErpDocRefId());
		        for (Combined_Representative combine2 : combineList) {
		            // For each related Combined_Representative, get the RepresentParty records
		            List<RepresentParty> representList = representPartyRepository.getbyuserId(cid, bid, combine2.getPartyId());

		            // Check if representList is not empty before adding it to the result list
		            if (representList != null && !representList.isEmpty()) {
		                represent.addAll(representList);
		            }
		        }
	       }

	        return represent;
	    } else {
	        // If Combined_Representative is not found, return RepresentParty records for the given user ID
	        return representPartyRepository.getbyuserId(cid, bid, uid);
	    }
		
	}
	
	@GetMapping("/{CompId}/{BranchId}/{userId}/{reprentativeId}/getImage")
	public ResponseEntity<String> getImage(@PathVariable("CompId") String CompId, @PathVariable("BranchId") String BranchId
			,@PathVariable("reprentativeId") String reprentativeId,@PathVariable("userId") String userId)
	 throws IOException {
	    
	    RepresentParty findByRepresentativeId = RepsentativeService.findByRepresentativeId(CompId, BranchId, userId,reprentativeId);

	    if (findByRepresentativeId != null) {
	        String nsdlStatusDocsPath = findByRepresentativeId.getImagePath();
	        Path filePath = Paths.get(nsdlStatusDocsPath);

	        // Check if the file exists
	        if (Files.exists(filePath)) {
	            try {
	                byte[] imageBytes = Files.readAllBytes(filePath);

	                // Encode the image bytes to base64
	                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

	                // Construct a data URL for the image
	                String dataURL = "data:image/jpeg;base64," + base64Image;
	                
	                System.out.println("In Image");

	                HttpHeaders headers = new HttpHeaders();
	                headers.setContentType(MediaType.TEXT_PLAIN); // Set the content type to text/plain

	                return new ResponseEntity<>(dataURL, headers, HttpStatus.OK);
	            } catch (java.io.IOException e) {
	                // Handle the IOException appropriately (e.g., log it)
	                e.printStackTrace();
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	            }
	        }
	    }

	    return ResponseEntity.notFound().build();
	}


	@GetMapping(value = "/getPartyRepresentative/{CompId}/{BranchId}")
	public List<RepresentParty> getPartyRepresentative(@PathVariable("CompId") String CompId,
			@PathVariable("BranchId") String BranchId) {
		return RepsentativeService.getall(CompId, BranchId);
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

	@GetMapping("/getAllRepresentPartyImg/{rpid}")
	public ResponseEntity<?> getImageOrPdf(@PathVariable("rpid") String rpid) throws IOException {

		RepresentParty RepresentPartyObject = representPartyRepository.getByRepresentativeId(rpid);

		  if (RepresentPartyObject != null) 
		    {
		        String image = ImageHelper.getRepresentativeImagePhoto(RepresentPartyObject);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.TEXT_PLAIN);

				return new ResponseEntity<>(image, headers, HttpStatus.OK);
		    }

		    return ResponseEntity.notFound().build();

	}

	@GetMapping("/getImage1/{compid}/{branchId}/{pid}/{rpid}")
	public ResponseEntity<?> getImageOrPdf(@PathVariable("compid") String compid,
	                                       @PathVariable("branchId") String branchId,
	                                       @PathVariable("pid") String pid,
	                                       @PathVariable("rpid") String rpid) {
	    RepresentParty representPartyObject = representPartyRepository
	            .getByCompanyIdAndBranchIdAndUserIdAndRepresentativeId(compid, branchId, pid, rpid);

	    if (representPartyObject != null) 
	    {
	        String image = ImageHelper.getPhoto(representPartyObject.getImagePath());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_PLAIN);

			return new ResponseEntity<>(image, headers, HttpStatus.OK);
	    }

	    return ResponseEntity.notFound().build();
	}
	
	
	
	@GetMapping("/getRepresentative")
	public ResponseEntity<?> getRepresentative(@RequestParam("companyId") String companyId, @RequestParam("branchId") String branchId,@RequestParam("user") String user) {
	   List<Object[]> representatives = representPartyRepository.getRepresentativesByUser(companyId, branchId, user);
	    return ResponseEntity.ok(representatives);
	}
	
	
	@GetMapping("/getRepresentativeDetails")
	public ResponseEntity<?> getRepresentativeDetails(@RequestParam("companyId") String companyId, @RequestParam("branchId") String branchId,@RequestParam("representative") String representative) {
		RepresentParty representatives = representPartyRepository.getRepresentativesByDetails(companyId, branchId, representative);
	    return ResponseEntity.ok(representatives);
	}
	
	
}
