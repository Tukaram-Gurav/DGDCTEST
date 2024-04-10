package com.cwms.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.DemuragePackagesHistory;
import com.cwms.entities.ProformaPackagesHistory;

public interface ProformaPackagesHistoryRepositary extends JpaRepository<ProformaPackagesHistory, Long>
{
	List<ProformaPackagesHistory> findByCompanyIdAndBranchIdAndPartyIdAndProformaNo(String companyId,String branchId,String partyId,String invoiceno);	
	
	@Query("SELECT dph FROM ProformaPackagesHistory dph " +
	        "WHERE dph.companyId = :companyId " +
	        "AND dph.branchId = :branchId " +
	        "AND dph.partyId = :partyId " +
	        "AND dph.proformaNoDate >= :startDate " +
	        "AND dph.proformaNoDate <= :endDate " +
	        "AND (:proformaNo IS NULL OR :proformaNo = '' OR dph.proformaNo = :inviceNo) " +
	        "ORDER BY dph.proformaNoDate DESC")
	List<ProformaPackagesHistory> findDemuragePackagesByCriteria(
	        @Param("companyId") String companyId,
	        @Param("branchId") String branchId,
	        @Param("partyId") String partyId,
	        @Param("startDate") Date startDate,
	        @Param("endDate") Date endDate,
	        @Param("inviceNo") String inviceNo
	);
	
	
}
