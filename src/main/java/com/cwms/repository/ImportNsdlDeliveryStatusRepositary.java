package com.cwms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cwms.entities.ImportNsdlDeliveryStatus;

public interface ImportNsdlDeliveryStatusRepositary extends JpaRepository<ImportNsdlDeliveryStatus, Long>
{
 List<ImportNsdlDeliveryStatus> findByCompanyIdAndBranchId(String companyId,String branchId);
 
 @Query("SELECT d.nsdlDeliveryStaus FROM ImportNsdlDeliveryStatus d WHERE d.companyId = :companyId AND d.branchId = :branchId")
 List<String> findNSDLDeliveryStatusByCompanyIdAndBranchId(String companyId, String branchId);
	
}
