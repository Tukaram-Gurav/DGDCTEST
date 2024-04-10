package com.cwms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.PredictableInvoicePackages;
import com.cwms.entities.PredictableInvoiceTaxDetails;

public interface PredictableInvoicePackagesRepositary extends JpaRepository<PredictableInvoicePackages, String>
{

	
	List<PredictableInvoicePackages> findByCompanyIdAndBranchId(String CompanyId , String branchId);
	
	
	
	List<PredictableInvoicePackages> findByCompanyIdAndBranchIdAndCalculatedDateBetween(
            String companyId, String branchId, Date startDate, Date endDate);
	
	
	@Query(value="select p from PredictableInvoicePackages p where p.calculatedDate between :start and :end and p.companyId=:cid and p.branchId=:bid")
	List<PredictableInvoicePackages> getDataByDate(@Param("start") Date start,@Param("end") Date end,@Param("cid") String cid,@Param("bid") String bid);
	
	
	@Query(value="select p from PredictableInvoicePackages p where p.calculatedDate between :start and :end and p.companyId=:cid and p.branchId=:bid and p.partyId=:party")
	List<PredictableInvoicePackages> getDataByPartyandDate(@Param("start") Date start,@Param("end") Date end,@Param("cid") String cid,@Param("bid") String bid,@Param("party") String party);



}
