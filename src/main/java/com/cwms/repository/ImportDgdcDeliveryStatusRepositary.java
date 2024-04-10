package com.cwms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cwms.entities.ImportDgdcDeliveryStatus;

public interface ImportDgdcDeliveryStatusRepositary extends JpaRepository<ImportDgdcDeliveryStatus, Long> 
{
	@Query("SELECT d.dgdcDeliveryStaus FROM ImportDgdcDeliveryStatus d WHERE d.companyId = :companyId AND d.branchId = :branchId")
    List<String> findDgdcDeliveryStatusByCompanyIdAndBranchId(String companyId, String branchId);
	
	List<ImportDgdcDeliveryStatus> findByCompanyIdAndBranchId(String companyId,String branchId);

}
