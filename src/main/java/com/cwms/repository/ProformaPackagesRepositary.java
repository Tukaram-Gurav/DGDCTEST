package com.cwms.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cwms.entities.InvoicePackages;
import com.cwms.entities.ProformaPackages;

public interface ProformaPackagesRepositary extends JpaRepository<ProformaPackages, String>
{

	ProformaPackages findByCompanyIdAndBranchIdAndPartyIdAndProformaNo(String companyId,String branchId,String partyId,String invoiceno);
	@Query(value = "SELECT * FROM Proforma_Packages WHERE Proforma_Date BETWEEN ?1 AND ?2 "
			+ "AND company_id = ?3 AND branch_id = ?4 ", nativeQuery = true)
	List<ProformaPackages> findInvoicesBetweenDatesAndConditionsAll(Date startDate, Date endDate, String cid, String bid);
	

	@Query(value = "SELECT * FROM Proforma_Packages WHERE Proforma_Date BETWEEN ?1 AND ?2 "
			+ "AND company_id = ?3 AND branch_id = ?4 AND party_id = ?5 ", nativeQuery = true)
	List<ProformaPackages> findInvoicesBetweenDatesAndConditions(Date startDate, Date endDate, String cid, String bid,
			String pid);
}
