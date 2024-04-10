package com.cwms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cwms.entities.Import_History;
import com.cwms.service.Import_HistoryService;

@RestController
@RequestMapping("history")
@CrossOrigin("*")
public class ImportHistoryController 
{
		@Autowired
		private Import_HistoryService historyService;
		
//		@GetMapping("/{cid}/{bid}/{MAWB}/{HAWB}/{SIRNO}")
//		public List<Import_History> getBySirno(@PathVariable("SIRNO")String SIRNO,
//				@PathVariable("cid")String CompId, @PathVariable("bid")String branchId,
//				@PathVariable("MAWB")String MAWB, @PathVariable("HAWB")String HAWB)
//		{
//			return historyService.findbySIRNO(CompId,branchId,MAWB,HAWB,SIRNO);
//		}
		
		
		@GetMapping("/getHistoryOfMaster")
		public List<Import_History> getBySirno(@RequestParam("sirno")String SIRNO,
				@RequestParam("cid")String CompId, @RequestParam("bid")String branchId,
				@RequestParam("mawb")String MAWB, @RequestParam("hawb")String HAWB)
		{
			return historyService.findbySIRNO(CompId,branchId,MAWB,HAWB,SIRNO);
		}
		
}