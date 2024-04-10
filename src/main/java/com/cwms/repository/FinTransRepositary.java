package com.cwms.repository;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.FinTrans;

public interface FinTransRepositary extends JpaRepository<FinTrans, String>
{
	
	FinTrans findByCompanyIdAndBranchIdAndPartyId(String copanyId,String branchId,String partyId);
	
	
	FinTrans findByCompanyIdAndBranchIdAndPartyIdAndTransId(String copanyId,String branchId,String partyId,String transId);
	
	
	 @Query("SELECT SUM(f.advAmt) AS totalAdvAmt, SUM(f.clearedAmt) AS totalClearedAmt " +
	           "FROM FinTrans f " +
	           "WHERE f.companyId = :companyId " +
	           "AND f.branchId = :branchId " +
	           "AND f.partyId = :partyId")
	    List<String> calculateSumAdvClearedAmounts(
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("partyId") String partyId);
	
}
