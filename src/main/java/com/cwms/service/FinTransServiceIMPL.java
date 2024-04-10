package com.cwms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cwms.entities.FinTrans;
import com.cwms.repository.FinTransRepositary;

@Service
public class FinTransServiceIMPL {
	@Autowired
	private FinTransRepositary repo;
	
	public FinTrans getByReceiptId(String compId, String branchId, String partyId, String receiptId) {
		return repo.findByCompanyIdAndBranchIdAndPartyIdAndTransId(compId, branchId, partyId,receiptId);
	}
	
	
	

	public FinTrans addFinTransDtl(FinTrans finTrans) {
		return repo.save(finTrans);
	}

	public FinTrans getBypartyIdFinTransDtl(String compId, String branchId, String partyId) {
		return repo.findByCompanyIdAndBranchIdAndPartyId(compId, branchId, partyId);
	}
	
	
	
	

	public FinTrans getBypartyIdFinTransByDocType(String compId, String branchId, String partyId) {
		return repo.findByCompanyIdAndBranchIdAndPartyId(compId, branchId, partyId);
	}

	public List<String> calculateSums(String companyId, String branchId, String partyId) {
		return repo.calculateSumAdvClearedAmounts(companyId, branchId, partyId);
	}

}
