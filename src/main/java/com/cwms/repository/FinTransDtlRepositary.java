package com.cwms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cwms.entities.FinTransDtl;

public interface FinTransDtlRepositary extends JpaRepository<FinTransDtl, String> 
{
	FinTransDtl findByCompanyIdAndBranchIdAndPartyId(String companyId,String branchId,String partyId);
}
