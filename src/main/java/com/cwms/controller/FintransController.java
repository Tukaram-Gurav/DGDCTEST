package com.cwms.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cwms.entities.FinTrans;
import com.cwms.entities.FinTransDtl;
import com.cwms.service.FinTransDtlServiceIMPL;
import com.cwms.service.FinTransServiceIMPL;
import com.cwms.service.ProcessNextIdService;

@RestController
@CrossOrigin("*")
@RequestMapping("/payment")
public class FintransController {

	@Autowired
	private FinTransDtlServiceIMPL Transdtlservice;

	@Autowired
	private FinTransServiceIMPL Transservice;

	@Autowired
	public ProcessNextIdService proccessNextIdService;

	@PostMapping("/{compid}/{branchId}/{party}/addtransdtl")
	public FinTransDtl adddetail(@PathVariable("compid") String compid, @PathVariable("branchId") String branchId,
			@RequestBody FinTransDtl FinTransDtl, @PathVariable("party") String party) {

//		proccessNextIdService.autoIncrementReceiptNumber();		
		return Transdtlservice.addFinTransDtl(FinTransDtl);

	}

	@PostMapping("/{compid}/{branchId}/{party}/addvance")
	public FinTrans adddetail(@PathVariable("compid") String compid, @PathVariable("branchId") String branchId,
			@RequestBody FinTrans finTrans, @PathVariable("party") String party) {
		
		
		
		System.out.println("Data Received");
		System.out.println(finTrans);
		
		if (finTrans.getTransId() == null || finTrans.getTransId().trim().isEmpty()) {
			String ReceiptNumber = proccessNextIdService.autoIncrementReceiptNumber();
			finTrans.setPartyId(party);
			finTrans.setTransId(ReceiptNumber);
			finTrans.setDocType("AD");
			finTrans.setCompanyId(compid);
			finTrans.setBranchId(branchId);
			finTrans.setAdvAmt(finTrans.getTransactionAmt());
			return Transservice.addFinTransDtl(finTrans);
		}
		else
		{
			
		FinTrans byReceiptId = Transservice.getByReceiptId(compid, branchId, party, finTrans.getTransId());
//		
        byReceiptId.setPartyId(finTrans.getPartyId());
        byReceiptId.setPaymentMode(finTrans.getPaymentMode());
        byReceiptId.setChequeNo(finTrans.getChequeNo());
        byReceiptId.setChequeDate(finTrans.getChequeDate());
        byReceiptId.setBankName(finTrans.getBankName());
        byReceiptId.setTransactionNo(finTrans.getTransactionNo());
        byReceiptId.setTransactionDate(finTrans.getTransactionDate());
        byReceiptId.setTransactionAmt(finTrans.getTransactionAmt());
        byReceiptId.setCurrency(finTrans.getCurrency());
        byReceiptId.setReceiptAmt(finTrans.getReceiptAmt());
        byReceiptId.setNarration(finTrans.getNarration());
        byReceiptId.setClearedAmt(finTrans.getClearedAmt());
        byReceiptId.setAdvTransId(finTrans.getAdvTransId());
        byReceiptId.setAdvTransDate(finTrans.getAdvTransDate());
        byReceiptId.setAdvFlag(finTrans.getAdvFlag());
        byReceiptId.setBalAdvAmt(finTrans.getBalAdvAmt());
        byReceiptId.setAdvAmt(finTrans.getTransactionAmt());
        byReceiptId.setBankReconFlag(finTrans.getBankReconFlag());
        byReceiptId.setBankReconDate(finTrans.getBankReconDate());
        byReceiptId.setBankReconAmt(finTrans.getBankReconAmt());
        byReceiptId.setTdsPercentage(finTrans.getTdsPercentage());
        byReceiptId.setTdsAmt(finTrans.getTdsAmt());
        byReceiptId.setTdsStatus(finTrans.getTdsStatus());
        byReceiptId.setCreatedBy(finTrans.getCreatedBy());
        byReceiptId.setCreatedDate(finTrans.getCreatedDate());
        byReceiptId.setEditedBy(finTrans.getEditedBy());
        byReceiptId.setEditedDate(finTrans.getEditedDate());
        byReceiptId.setApprovedBy(finTrans.getApprovedBy());
        byReceiptId.setApprovedDate(finTrans.getApprovedDate());

        return Transservice.addFinTransDtl(byReceiptId);

			
			
		}

		
	}

//	GetBy Trans Id	
	@GetMapping("/{compid}/{branchId}/{party}/{receiptId}/getTransReceiptId")
	public FinTrans getTransReceiptId(@PathVariable("compid") String compid, @PathVariable("branchId") String branchId,
			@PathVariable("receiptId") String receiptId, @PathVariable("party") String partyId) {

		System.out.println(
				" company " + compid + "branchId " + branchId + "receiptId " + receiptId + " partyId " + partyId);

		FinTrans byReceiptId = Transservice.getByReceiptId(compid, branchId, partyId, receiptId);
		System.out.println(byReceiptId);
		return byReceiptId;
	}

	@GetMapping("/{compid}/{branchId}/{partyId}/gettransByPartyId")
	public FinTrans adddetail(@PathVariable("compid") String compid, @PathVariable("branchId") String branchId,
			@PathVariable("partyId") String partyId) {
		return Transservice.getBypartyIdFinTransDtl(compid, branchId, partyId);
	}

	@GetMapping("/{compid}/{branchId}/{partyId}/getSumOfAdvAndCleared")
	public List<String> getSumOfAdvAndCleared(@PathVariable("compid") String compid,
			@PathVariable("branchId") String branchId, @PathVariable("partyId") String partyId) 
	{
		List<String> calculateSums = Transservice.calculateSums(compid, branchId, partyId);
		return calculateSums;
	}
}
