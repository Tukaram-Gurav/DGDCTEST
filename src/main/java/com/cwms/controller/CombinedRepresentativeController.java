package com.cwms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cwms.entities.Combined_Representative;
import com.cwms.repository.Combined_RepresentativeRepository;
import com.cwms.service.ProcessNextIdService;

@CrossOrigin("*")
@RestController
@RequestMapping("/combineRepresentative")
public class CombinedRepresentativeController {

	@Autowired
	private Combined_RepresentativeRepository combinerpresentrepo;
	
	@Autowired
	private ProcessNextIdService processIdservice;
	
	@GetMapping("/getall/{cid}/{bid}")
	public List<Object[]> getall(@PathVariable("cid") String cid,@PathVariable("bid") String bid){
		return combinerpresentrepo.findDistinctErpDocRefIdAndPartyId(cid, bid);
	}
	
	@GetMapping("/getbyerp/{cid}/{bid}/{erp}")
	public List<Combined_Representative> getByERPId(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("erp") String erp){
		return combinerpresentrepo.getByERPId(cid, bid, erp);
	}
	
	@PostMapping("/save/{cid}/{bid}/{erp}/{group}/{party}")
	public Combined_Representative saveByERPId(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("erp") String erp,@PathVariable("group") String group,@PathVariable("party") String party){
		
		Combined_Representative  combine = new Combined_Representative();
		combine.setCompanyId(cid);
		combine.setBranchId(bid);
		combine.setErpDocRefId(erp);
		combine.setGroupName(group);
		combine.setPartyId(party);
		combine.setStatus("A");
		
		combinerpresentrepo.save(combine);
		return combine;
	}
	
	@PostMapping("/addData/{cid}/{bid}/{group}")
	public List<Combined_Representative> addData(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("group") String group,@RequestBody List<Combined_Representative> combine){
		
		
		if(combine != null) {
			String id = processIdservice.autoIncrementCombineRepresentativeId();
			for(Combined_Representative combine1 : combine) {
				
				combine1.setCompanyId(cid);
				combine1.setBranchId(bid);
				combine1.setErpDocRefId(id);
				combine1.setGroupName(group);
				combine1.setStatus("A");
				
				combinerpresentrepo.save(combine1);
			}
		}
		
		
		
		return combine;
	}
	
	@DeleteMapping("/delete/{cid}/{bid}/{erpid}/{party}")
	public void deletedata(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("erpid") String erp,@PathVariable("party") String party) {
		Combined_Representative combine = combinerpresentrepo.getByIdandParty(cid, bid, erp, party);
		combinerpresentrepo.delete(combine);
	}
	
	@DeleteMapping("/deleteAll/{cid}/{bid}/{erpid}")
	public void deleteAlldata(@PathVariable("cid") String cid,@PathVariable("bid") String bid,@PathVariable("erpid") String erp) {
		List<Combined_Representative> combine = combinerpresentrepo.getByERPId(cid, bid, erp);
		combinerpresentrepo.deleteAll(combine);
	}
}