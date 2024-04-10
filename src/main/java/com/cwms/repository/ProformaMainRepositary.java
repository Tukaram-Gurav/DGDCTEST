package com.cwms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.InvoiceMain;
import com.cwms.entities.ProformaMain;

public interface ProformaMainRepositary extends JpaRepository<ProformaMain, String>
{
	
	@Query("SELECT im.proformaNo FROM ProformaMain im " +
	         "WHERE im.companyId = :companyId " +
	         "AND im.branchId = :branchId " +
	         "AND im.partyId = :partyId " +
	         "AND im.proformaDate BETWEEN :startDate AND :endDate")
	 List<String> findInvoiceNumbersByCriteria(
	         @Param("companyId") String companyId,
	         @Param("branchId") String branchId,
	         @Param("partyId") String partyId,
	         @Param("startDate") Date startDate,
	         @Param("endDate") Date endDate
	 );
	
	
	
	ProformaMain findByCompanyIdAndBranchIdAndPartyIdAndProformaNo(String compId,String branchId,String partyid,String invoiceNo);
	List<ProformaMain> findByCompanyIdAndBranchIdAndPartyId(String compId,String branchId,String partyid); 
	
	
	List<ProformaMain> findByCompanyIdAndBranchIdAndPartyIdAndProformaDateBetween(String compId,String branchId,String partyid,Date startDate , Date EndDate); 
	

	@Query(value = "SELECT * FROM Percfinvsrv WHERE Proforma_Date BETWEEN ?1 AND ?2 "
			+ "AND company_id = ?3 AND branch_id = ?4 AND party_id = ?5 ", nativeQuery = true)
	List<ProformaMain> findInvoicesMainBetweenDatesAndConditions(Date startDate, Date endDate, String cid, String bid,
			String pid);
	
	
	@Query(value ="SELECT  *  FROM Percfinvsrv  " +
            "WHERE company_id = :companyId " +
            "AND branch_id = :branchId " +
            "AND Proforma_Date BETWEEN :startDate AND :endDate " +
            "AND party_id = :partyId "
            , nativeQuery = true)
           
    List<ProformaMain>  findInvoiceData(String companyId, String branchId, Date startDate, Date endDate,
                                    String partyId );    
 
 
 @Query(value ="SELECT  *  FROM Percfinvsrv  " +
            "WHERE company_id = :companyId " +
            "AND branch_id = :branchId " +
            "AND Proforma_Date BETWEEN :startDate AND :endDate ",
             nativeQuery = true)     
    List<ProformaMain>  findAllInvoiceData(String companyId, String branchId, Date startDate, Date endDate );    
 
 
 
 
 @Query(value = "SELECT c.* " +
            "FROM Percfinvsrv c " +
            "WHERE c.company_id = :companyId " +
            "  AND c.branch_id = :branchId " +
            "  AND c.Proforma_Date BETWEEN :startDate AND :endDate " +
            "  AND ( " +
            "    SELECT p.unit_type " +
            "    FROM party p " +
            "    WHERE p.party_Id = c.party_id " +
            "  ) = :unitType ", nativeQuery = true)
    List<ProformaMain> findAllInvoiceDataOfPartyType(String companyId, String branchId, Date startDate, Date endDate, String unitType);

	
}
