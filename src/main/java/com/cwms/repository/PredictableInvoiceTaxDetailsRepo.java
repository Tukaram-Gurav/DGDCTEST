package com.cwms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.Import;
import com.cwms.entities.PredictableInvoiceTaxDetails;

public interface PredictableInvoiceTaxDetailsRepo extends JpaRepository<PredictableInvoiceTaxDetails, String>
{

	
	 @Query(value = "SELECT invoice_date, " +
		       "SUM(import_no_of_packages) as total_import_no_of_packages, " +
		       "SUM(import_sub_nop) as total_import_sub_nop, " +
		       "SUM(export_no_of_packages) as total_export_no_of_packages, " +
		       "SUM(export_sub_nop) as total_export_sub_nop, " +
		       "SUM(total_packages) as total_total_packages, " +
		       "SUM(holiday_rate) as total_holiday_rate, " +
		       "SUM(demurages_rate) as total_demurages_rate, " +
		       "SUM(export_sc_rate) as total_export_sc_rate, " +		      
		       "SUM(export_hp_rate) as total_export_hp_rate, " +
		       "SUM(export_pc_rate) as total_export_pc_rate, " +
		       "SUM(export_penalty) as total_export_penalty, " +
		       "SUM(import_sc_rate) as total_import_sc_rate, " +
		       "SUM(import_hp_rate) as total_import_hp_rate, " +
		       "SUM(import_pc_rate) as total_import_pc_rate, " +
		       "SUM(import_penalty) as total_import_penalty, " +
		       "SUM(Bill_Amt) as total_Bill_amount " +
		       "FROM predictable_cfssrvanx " +
		       "WHERE " +
		       "company_id = :companyId " +
		       "AND branch_id = :branchId " +
		       "AND (:PartyId IS NULL OR :PartyId = '' OR party_id = :PartyId) " +
		       "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
		       "(:startDate IS NULL AND :endDate >= invoice_date) OR " +
		       "(:startDate <= invoice_date AND :endDate IS NULL) OR " +
		       "(:startDate <= invoice_date AND :endDate >= invoice_date)) " +
		       "GROUP BY invoice_date " +
		       "ORDER BY invoice_date DESC", nativeQuery = true)
		List<Object[]> findInvoiceDataStartDateAndEndDateWithSum(
		        @Param("companyId") String companyId,
		        @Param("branchId") String branchId,
		        @Param("startDate") Date startDate,
		        @Param("endDate") Date endDate,
		        @Param("PartyId") String PartyId);
	
	
	
	PredictableInvoiceTaxDetails findFirstByCompanyIdAndBranchIdAndPredictableinvoiceNO(String companyId, String branchId,String invoiceNo);
	
	
	List<PredictableInvoiceTaxDetails> findByCompanyIdAndBranchId(String companyId,String branchId);
	
//	List<PredictableInvoiceTaxDetails> findByCompanyIdAndBranchId(String companyId,String branchId);
	
	List<PredictableInvoiceTaxDetails> findByCompanyIdAndBranchIdAndInvoiceDateBetween(
            String companyId, String branchId, Date startDate, Date endDate);
	
	
	 @Query("SELECT i FROM PredictableInvoiceTaxDetails i " +
		       "WHERE " +
		       "i.companyId = :companyId " +
		       "AND i.branchId = :branchId " +
		       "AND (:PartyId IS NULL OR :PartyId = '' OR i.partyId = :PartyId) " +
		       "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
		       "(:startDate IS NULL AND :endDate >= i.invoiceDate) OR " +
		       "(:startDate <= i.invoiceDate AND :endDate IS NULL) OR " +
		       "(:startDate <= i.invoiceDate AND :endDate >= i.invoiceDate)) " +
		       "ORDER BY i.invoiceDate ASC")
		List<PredictableInvoiceTaxDetails> findInvoiceDataStartDateAndEndDate(
		        @Param("companyId") String companyId,
		        @Param("branchId") String branchId,
		        @Param("startDate") Date startDate,
		        @Param("endDate") Date endDate,
		        @Param("PartyId") String PartyId);
	 
	 
	 @Query(value="select p from PredictableInvoiceTaxDetails p where p.invoiceDate between :start and :end and p.companyId=:cid and p.branchId=:bid and p.partyId=:pid")
		List<PredictableInvoiceTaxDetails> getDataByParty(@Param("start") Date start,@Param("end") Date end,@Param("cid") String cid,@Param("bid") String bid,@Param("pid") String pid);
	 
	 @Query(value="select p from PredictableInvoiceTaxDetails p where p.invoiceDate between :start and :end and p.companyId=:cid and p.branchId=:bid")
		List<PredictableInvoiceTaxDetails> getDataByWithoutParty(@Param("start") Date start,@Param("end") Date end,@Param("cid") String cid,@Param("bid") String bid);
	 
	 
	 
	 
	 
}
