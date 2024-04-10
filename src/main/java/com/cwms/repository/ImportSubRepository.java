package com.cwms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.ExportSub;
import com.cwms.entities.ImportSub;

import jakarta.transaction.Transactional;

import java.util.*;

@EnableJpaRepositories
public interface ImportSubRepository extends JpaRepository<ImportSub, String> {
	
	
	
	
	@Query(value = "SELECT i.sirDate,i.sirNo,i.importType,i.exporter,i.nop,i.invoiceNo,i.exporterName,i.requestId,i.dgdcStatus FROM ImportSub i WHERE i.companyId =:compnayId AND i.branchId =:branchId AND ((:dgdcstatus = 'Handed over to DGDC SEEPZ' AND DATE_FORMAT(i.sirDate, '%Y-%m-%d') =:sirDate) OR (:dgdcstatus = 'Handed over to Party/CHA' AND i.dgdcStatus != 'Handed over to DGDC SEEPZ' and DATE_FORMAT(i.outDate, '%Y-%m-%d') =:sirDate) OR (:dgdcstatus = 'Exit from DGDC SEEPZ Gate' AND i.dgdcStatus NOT IN ('Handed over to DGDC SEEPZ','Handed over to Party/CHA') AND DATE_FORMAT(i.outDate, '%Y-%m-%d') =:sirDate) )")

	List<Object[]> findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus1(@Param("sirDate") String sirDate,
			@Param("compnayId") String compnayId, @Param("branchId") String branchId,
			@Param("dgdcstatus") String dgdcStatus);
	
	
	
	
	
	
	ImportSub findByCompanyIdAndBranchIdAndSirNoAndRequestIdAndExporterAndImpSubIdAndStatusNot(String companyId, String branchId,String sirNo,String requestId,String exporter ,String impSubId,String status);
	
	
	
	@Query(value="select e.dgdcStatus from ImportSub e where e.companyId=:cid and e.branchId=:bid and e.requestId=:reqid and e.sirNo=:sir")
	String findStatus(@Param("cid") String cid,@Param("bid") String bid,@Param("reqid") String reqid,@Param("sir") String sir);
	
	
	@Query(value = "SELECT * FROM Importsub WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND Out_Date =  :startDate AND (gate_pass_status IS NULL OR gate_pass_status != 'Y' OR gate_pass_status = '')", nativeQuery = true)
    List<ImportSub> findByCompanyAndBranchAndDate3(String companyId, String branchId, Date startDate);
	
	
	List<ImportSub> findByCompanyIdAndBranchIdAndExporterAndOutDateAndStatusNot(String companyId, String branchId,String partyId,Date serDate , String Status);
	
	@Query("SELECT i FROM ImportSub i " +
		       "WHERE " +
		       "i.companyId = :companyId " +
		       "AND i.branchId = :branchId " +
		       "AND i.exporter = :PartyId " +
		       "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
		       "(:startDate IS NULL AND :endDate >= i.sirDate) OR " +
		       "(:startDate <= i.sirDate AND :endDate IS NULL) OR " +
		       "(:startDate <= i.sirDate AND :endDate >= i.sirDate)) " +
		       "AND i.status != 'D'")
		List<ImportSub> findImportBySirDateStartDateAndEndDate(
		        @Param("companyId") String companyId,
		        @Param("branchId") String branchId,
		        @Param("startDate") Date startDate,
		        @Param("endDate") Date endDate,
		        @Param("PartyId") String PartyId);
	
	
	
	@Query("SELECT COUNT(DISTINCT i.exporter) FROM ImportSub i " +
	           "WHERE i.companyId = :companyId " +
	           "AND i.branchId = :branchId " +
	           "AND i.sirDate = :sirDate "+
	           "AND i.status <> 'D'" )
	    int countDistinctImporterIdsBySERDATE(
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("sirDate") Date sirDate
	    );
	
	@Query("SELECT i.sirNo,i.sirDate,i.requestId,i.exporter,i.nop,i.reentryDate,i.netWeight,i.netWeightUnit,i.nsdlStatus,i.dgdcStatus,i.forwardedStatus,i.mopStatus,i.challanDate,i.importType FROM ImportSub i " +
	        "LEFT JOIN Party p ON i.exporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +  // Assuming exporter is related to Party
	        "WHERE " +
	        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
	        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
	        "(:startDate IS NULL AND :endDate >= i.sirDate) OR " +
	        "(:startDate <= i.sirDate AND :endDate IS NULL) OR " +
	        "(:startDate <= i.sirDate AND :endDate >= i.sirDate)) " +
	        "AND i.companyId = :companyId " +
	        "AND i.branchId = :branchId " +
	        "AND i.importType NOT IN('LGD','Zone to Zone') " +
	        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
	        "(i.sirNo like CONCAT('%', :searchValue , '%') OR i.requestId like CONCAT('%', :searchValue , '%') OR p.partyName like CONCAT('%', :searchValue , '%'))) " + 
	        "ORDER BY i.sirNo DESC")
	List<Object[]> findByAttributes6(
	        @Param("companyId") String companyId,
	        @Param("branchId") String branchId,
	        @Param("dgdcStatus") String dgdcStatus,
	        @Param("startDate") Date startDate,
	        @Param("endDate") Date endDate,
	        @Param("searchValue") String searchValue
	);


	@Query("SELECT i.sirNo,i.sirDate,i.requestId,i.exporter,i.nop,i.reentryDate,i.netWeight,i.netWeightUnit,i.nsdlStatus,i.dgdcStatus,i.forwardedStatus,i.mopStatus,i.challanDate,i.importType FROM ImportSub i " +
	        "LEFT JOIN Party p ON i.exporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +  // Assuming exporter is related to Party
	        "WHERE " +
	        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
	        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
	        "(:startDate IS NULL AND :endDate >= i.sirDate) OR " +
	        "(:startDate <= i.sirDate AND :endDate IS NULL) OR " +
	        "(:startDate <= i.sirDate AND :endDate >= i.sirDate)) " +
	        "AND i.companyId = :companyId " +
	        "AND i.branchId = :branchId " +
	        "AND (i.importType='LGD' or i.importType='Zone to Zone') " +
	        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
	        "(i.sirNo like CONCAT('%', :searchValue , '%') OR i.requestId like CONCAT('%', :searchValue , '%') OR p.partyName like CONCAT('%', :searchValue , '%'))) " + 
	        "ORDER BY i.sirNo DESC")
	List<Object[]> findByAttributes7(
	        @Param("companyId") String companyId,
	        @Param("branchId") String branchId,
	        @Param("dgdcStatus") String dgdcStatus,
	        @Param("startDate") Date startDate,
	        @Param("endDate") Date endDate,
	        @Param("searchValue") String searchValue
	);
	
	
	
	

	@Query(value = "select i.* from importsub i where i.company_id=:cid and i.branch_id=:bid", nativeQuery = true)
	public List<ImportSub> getall(@Param("cid") String cid,@Param("bid") String bid);
	
	@Query(value="select i.* from importsub i where i.company_id=:cid and i.branch_id=:bid and i.request_id=:reqid",nativeQuery=true)
	public List<ImportSub> findRequestAllId(@Param("cid") String cid,@Param("bid") String bid,@Param("reqid") String reqid);
	
	@Query(value = "select i.* from importsub i where (i.import_type='LGD' or i.import_type='Zone to Zone') and i.company_id=:cid and i.branch_id=:bid order by sir_no desc", nativeQuery = true)
	public List<ImportSub> getalltocheckLGD(@Param("cid") String cid,@Param("bid") String bid);
	
	@Query(value = "select i.* from importsub i where !(i.import_type='LGD' or i.import_type='Zone to Zone') and i.company_id=:cid and i.branch_id=:bid order by sir_no desc", nativeQuery = true)
	public List<ImportSub> getalltocheckLGD1(@Param("cid") String cid,@Param("bid") String bid);
	
	 @Query(value = "SELECT SUM(nop) FROM importsub WHERE Company_Id = :cid AND Branch_Id = :bid AND DGDC_Status = :string AND SIR_Date = :date1", nativeQuery = true)
	    Integer findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDate2(@Param("cid") String cid, @Param("bid") String bid, @Param("string") String string, @Param("date1") Date date1);
	

//	 List<ImportSub> findByCompanyIdAndBranchIdAndExporterAndOutDateAndStatusNot(String companyId, String branchId,String partyId,Date serDate , String Status);
	 
	   
	    @Query(value = "SELECT * FROM Importsub WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND Out_Date =  :startDate AND handover_represntative_id = :representativeId AND (handover_party_cha = :paryCHAId OR handover_party_name = :paryCHAId) AND (gate_pass_status IS NULL OR gate_pass_status != 'Y' OR gate_pass_status = '')", nativeQuery = true)
	    List<ImportSub> findByCompanyAndBranchAndDate2(String companyId, String branchId, Date startDate, String paryCHAId, String representativeId);
	    
	    
//	    @Query(value = "SELECT * FROM Importsub WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND SIR_Date =  :startDate AND handover_represntative_id = :representativeId AND (handover_party_cha = :paryCHAId OR handover_party_name = :paryCHAId) ", nativeQuery = true)
//	    List<ImportSub> findByCompanyAndBranchAndDate5(String companyId, String branchId, Date startDate, String paryCHAId, String representativeId);
	    
	    @Query(value = "SELECT * FROM Importsub WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND Out_Date =  :startDate AND handover_represntative_id = :representativeId AND (handover_party_cha = :paryCHAId OR handover_party_name = :paryCHAId) ", nativeQuery = true)
	    List<ImportSub> findByCompanyAndBranchAndDate5(String companyId, String branchId, Date startDate, String paryCHAId, String representativeId);
	    
	    @Transactional
	    @Modifying
	    @Query(value = "UPDATE ImportSub SET gate_pass_status = 'Y' WHERE imp_sub_id IN :importSubIds", nativeQuery = true)
	    void setGatePassStatusToY(@Param("importSubIds") List<String> importSubIds);


	
	@Query(value="select i.* from importsub i where i.company_id=:cid and i.branch_id=:bid and i.request_id=:reqid",nativeQuery=true)
	public ImportSub findRequestId(@Param("cid") String cid,@Param("bid") String bid,@Param("reqid") String reqid);
	
	@Query(value="select i.* from importsub i where i.company_id=:cid and i.branch_id=:bid and i.imp_sub_id=:subid and i.request_id=:reqid",nativeQuery=true)
	public ImportSub findImportSub(@Param("cid") String cid,@Param("bid") String bid,@Param("subid") String subid,@Param("reqid") String reqid);
	
	@Query(value="select * from importsub where company_id=:cid and branch_id=:bid and sir_no=:sir",nativeQuery=true)
    ImportSub  Singledata(@Param("cid") String companyId,@Param("bid") String branchId,@Param("sir") String sir);
	
	@Query(value="select i.* from importsub i where i.company_id=:cid and i.branch_id=:bid and i.sir_no=:subid and i.request_id=:reqid",nativeQuery=true)
	public ImportSub findImportSubBysir(@Param("cid") String cid,@Param("bid") String bid,@Param("subid") String subid,@Param("reqid") String reqid);
	
	@Query(value="select i.* from importsub i where i.company_id=:cid and i.branch_id=:bid and i.sir_no=:subid",nativeQuery=true)
	public ImportSub findImportSubBysironly(@Param("cid") String cid,@Param("bid") String bid,@Param("subid") String subid);
	

	@Transactional
	@Modifying
	@Query(value="update importsub set nsdl_status=:nsdl,status_doc=:statusdoc where company_id=:cid and branch_id=:bid and imp_sub_id=:expid and request_id=:reqid",nativeQuery=true)
	public void updateData(@Param("nsdl") String nsdl,@Param("statusdoc") String statusdoc,@Param("cid") String cid,@Param("bid") String bid,@Param("expid") String expid,@Param("reqid") String reqid);
	
	@Transactional
	@Modifying
	@Query(value="update importsub set dgdc_status='Handed over to Party/CHA',handover_party_cha='P',handover_party_name=:hpid,handover_represntative_id=:representid , out_date = CURDATE() where company_id=:cid and branch_id=:bid and imp_sub_id=:eid and request_id=:rid",nativeQuery=true)
	public void updateStatus(@Param("cid") String cid,@Param("bid") String bid,@Param("eid") String eid,@Param("rid") String rid,@Param("hpid") String hpid,@Param("representid") String representid);
	
//	@Transactional
//	@Modifying
//	@Query(value="update importsub set dgdc_status='Handed over to Party/CHA',handover_party_cha='C',handover_party_name=:hpid,handover_represntative_id=:representid  where company_id=:cid and branch_id=:bid and imp_sub_id=:eid and request_id=:rid",nativeQuery=true)
//	public void updateCHAStatus(@Param("cid") String cid,@Param("bid") String bid,@Param("eid") String eid,@Param("rid") String rid,@Param("hpid") String hpid,@Param("representid") String representid);

	
//	Selecting out Date
	@Transactional
	@Modifying
	@Query(value = "UPDATE importsub SET dgdc_status='Handed over to Party/CHA', handover_party_cha='C', handover_party_name=:hpid, handover_represntative_id=:representid, out_date = CURDATE() " +
	       "WHERE company_id=:cid AND branch_id=:bid AND imp_sub_id=:eid AND request_id=:rid", nativeQuery = true)
	public void updateCHAStatus(@Param("cid") String cid, @Param("bid") String bid, @Param("eid") String eid, @Param("rid") String rid, @Param("hpid") String hpid, @Param("representid") String representid);
	
	@Query(value = "SELECT * FROM importsub WHERE DATE_FORMAT(sir_date, '%Y-%m-%d') =:sirDate AND company_id =:compnayId AND branch_id =:branchId AND ((:dgdcstatus = 'Handed over to DGDC SEEPZ') OR (:dgdcstatus = 'Handed over to Party/CHA' AND dgdc_status != 'Handed over to DGDC SEEPZ') OR (:dgdcstatus = 'Exit from DGDC SEEPZ Gate' AND dgdc_status NOT IN ('Handed over to DGDC SEEPZ','Handed over to Party/CHA')) )", nativeQuery = true)

	List<ImportSub> findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(@Param("sirDate") String sirDate,
			@Param("compnayId") String compnayId, @Param("branchId") String branchId,
			@Param("dgdcstatus") String dgdcStatus);

	@Query(value = "SELECT DISTINCT * " +
            "FROM importsub " +
            "WHERE company_id = :companyId " +
            "AND branch_id = :branchId " +
         
            "AND exporter = :exporter", nativeQuery = true)
     List<ImportSub> findImportAllData(String companyId, String branchId,
                                    String exporter);
	

 
 
 
 @Query(value = "SELECT  * " +
            "FROM importsub " +
            "WHERE company_id = :companyId " +
            "AND branch_id = :branchId " + 
      
            "AND sir_date BETWEEN :startDate AND :endDate ",
           
            nativeQuery = true)
     List<ImportSub> findImportSubAllData(String companyId, String branchId, Date startDate, Date endDate);
 
 
 public ImportSub findByCompanyIdAndBranchIdAndSirDateAndExporterAndNop(String companyId, String branchId, Date startDate,String exporter,int nop);
	
//shubham
@Query(value = "SELECT SUM(nop) FROM importsub WHERE Company_Id = :cid AND Branch_Id = :bid AND SIR_Date = :date1", nativeQuery = true)
Integer findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDate2(@Param("cid") String cid, @Param("bid") String bid, @Param("date1") Date date1);


@Query(value = "SELECT SUM(Nop) FROM importsub WHERE Company_Id = :cid AND Branch_Id = :bid AND DGDC_Status = :string AND out_date = :date1", nativeQuery = true)
Integer findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDate3(@Param("cid") String cid, @Param("bid") String bid, @Param("string") String string, @Param("date1") Date date1);

List<ImportSub> findByCompanyIdAndBranchIdAndExporterAndSirDateBetweenAndStatusNot(String companyId, String branchId,String partyId, Date startDate,Date endDate , String Status);
List<ImportSub> findByCompanyIdAndBranchIdAndExporterAndSirDateAndStatusNot(String companyId, String branchId,String partyId,Date serDate , String Status);

//sanket
@Query(value = "select i.* from importsub i where i.company_id=:cid and i.branch_id=:bid and i.exporter=:exporter and i.dgdc_status='Handed over to DGDC SEEPZ' and (i.nsdl_status='Passed In Full' or i.nsdl_status='Passed In Partial') and i.status != 'D' and forwarded_status!='FWD_OUT' and import_type not in('LGD','Zone to Zone')", nativeQuery = true)
	public List<ImportSub> getalldatabyparty(@Param("cid") String cid,@Param("bid") String bid,@Param("exporter") String exporter);
	

@Query(value = "select i.* from importsub i where i.company_id=:cid and i.branch_id=:bid and i.exporter=:exporter and i.dgdc_status='Handed over to DGDC SEEPZ' and (i.nsdl_status='Passed In Full' or i.nsdl_status='Passed In Partial') and i.status != 'D' and forwarded_status!='FWD_OUT' and (import_type='LGD' or import_type='Zone to Zone')", nativeQuery = true)
	public List<ImportSub> getalldatabyparty1(@Param("cid") String cid,@Param("bid") String bid,@Param("exporter") String exporter);
	
@Query(value = "select i.* from importsub i where (i.import_type='LGD' or i.import_type='Zone to Zone') and i.company_id=:cid and i.branch_id=:bid and i.exporter =:exporter order by sir_no desc", nativeQuery = true)
public List<ImportSub> getalltocheckLGD4(@Param("cid") String cid,@Param("bid") String bid,@Param("exporter") String exporter);


@Query(value = "select i.* from importsub i where (i.import_type='LGD' or i.import_type='Zone to Zone') and i.company_id=:cid and i.branch_id=:bid and i.handover_party_cha='C' and i.handover_party_name =:exporter order by sir_no desc", nativeQuery = true)
public List<ImportSub> getalltocheckLGD6(@Param("cid") String cid,@Param("bid") String bid,@Param("exporter") String exporter);

@Query(value = "select i.* from importsub i where !(i.import_type='LGD' or i.import_type='Zone to Zone') and i.company_id=:cid and i.branch_id=:bid and i.exporter =:exporter order by sir_no desc", nativeQuery = true)
public List<ImportSub> getalltocheckLGD3(@Param("cid") String cid,@Param("bid") String bid,@Param("exporter") String exporter);

@Query(value = "select i.* from importsub i where !(i.import_type='LGD' or i.import_type='Zone to Zone') and i.company_id=:cid and i.branch_id=:bid and i.handover_party_cha='C' and i.handover_party_name =:exporter order by sir_no desc", nativeQuery = true)
public List<ImportSub> getalltocheckLGD5(@Param("cid") String cid,@Param("bid") String bid,@Param("exporter") String exporter);


@Query("SELECT e FROM ImportSub e WHERE e.companyId = :companyId AND e.branchId = :branchId AND e.sirNo = :serNo AND e.dgdcStatus = 'Handed over to DGDC SEEPZ' AND e.status <> 'D' AND (e.mopStatus <> 'Y' OR e.mopStatus IS NULL OR e.mopStatus = '')")
ImportSub findByCompanyIdAndBranchIdAndSirNoAndAndDgdcStatusAndStatusNotAndMopStatusNot(@Param("companyId") String companyId, @Param("branchId") String branchId, @Param("serNo") String serNo);


ImportSub findBySirNo(String sirNo);

@Query("SELECT i FROM ImportSub i " +
        "LEFT JOIN Party p ON i.exporter = p.partyId " +  // Assuming exporter is related to Party
        "WHERE " +
        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
        "(:startDate IS NULL AND :endDate >= i.sirDate) OR " +
        "(:startDate <= i.sirDate AND :endDate IS NULL) OR " +
        "(:startDate <= i.sirDate AND :endDate >= i.sirDate)) " +
        "AND i.companyId = :companyId " +
        "AND i.branchId = :branchId " +
        "AND i.importType NOT IN('LGD','Zone to Zone') " +
        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
        "(i.sirNo like CONCAT('%', :searchValue , '%') OR i.requestId like CONCAT('%', :searchValue , '%') OR p.partyName like CONCAT('%', :searchValue , '%'))) " + 
        "ORDER BY i.sirNo DESC")
List<ImportSub> findByAttributes4(
        @Param("companyId") String companyId,
        @Param("branchId") String branchId,
        @Param("dgdcStatus") String dgdcStatus,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("searchValue") String searchValue
);


@Query("SELECT i FROM ImportSub i " +
        "LEFT JOIN Party p ON i.exporter = p.partyId " +  // Assuming exporter is related to Party
        "WHERE " +
        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
        "(:startDate IS NULL AND :endDate >= i.sirDate) OR " +
        "(:startDate <= i.sirDate AND :endDate IS NULL) OR " +
        "(:startDate <= i.sirDate AND :endDate >= i.sirDate)) " +
        "AND i.companyId = :companyId " +
        "AND i.branchId = :branchId " +
        "AND (i.importType='LGD' or i.importType='Zone to Zone') " +
        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
        "(i.sirNo like CONCAT('%', :searchValue , '%') OR i.requestId like CONCAT('%', :searchValue , '%') OR p.partyName like CONCAT('%', :searchValue , '%'))) " + 
        "ORDER BY i.sirNo DESC")
List<ImportSub> findByAttributes5(
        @Param("companyId") String companyId,
        @Param("branchId") String branchId,
        @Param("dgdcStatus") String dgdcStatus,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("searchValue") String searchValue
);

ImportSub findByCompanyIdAndBranchIdAndSirNo(String companyId, String branchId, String sirNo);

@Query(value="select * from importsub where mop_id=:cid",nativeQuery=true)
List<ImportSub> getbymoppassid(@Param("cid") String cid);

@Query(value="select * from importsub where common_gate_pass_id=:cid",nativeQuery=true)
List<ImportSub> getbycommonid(@Param("cid") String cid);

@Query("SELECT i.sirNo,i.sirDate,i.requestId,i.exporter,i.nop,i.reentryDate,i.netWeight,i.netWeightUnit,i.nsdlStatus,i.dgdcStatus,i.forwardedStatus,i.mopStatus,i.challanDate,i.importType FROM ImportSub i " +
        "LEFT JOIN Party p ON i.exporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +  // Assuming exporter is related to Party
        "WHERE " +
        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
        "(:startDate IS NULL AND :endDate >= i.sirDate) OR " +
        "(:startDate <= i.sirDate AND :endDate IS NULL) OR " +
        "(:startDate <= i.sirDate AND :endDate >= i.sirDate)) " +
        "AND i.companyId = :companyId " +
        "AND i.branchId = :branchId " +
        "AND i.exporter = :party " +
        "AND i.importType NOT IN('LGD','Zone to Zone') " +
        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
        "(i.sirNo like CONCAT('%', :searchValue , '%') OR i.requestId like CONCAT('%', :searchValue , '%') OR p.partyName like CONCAT('%', :searchValue , '%'))) " + 
        "ORDER BY i.sirNo DESC")
List<Object[]> findByPartyAttributes(
        @Param("companyId") String companyId,
        @Param("branchId") String branchId,
        @Param("party") String party,
        @Param("dgdcStatus") String dgdcStatus,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("searchValue") String searchValue
);


@Query("SELECT i.sirNo,i.sirDate,i.requestId,i.exporter,i.nop,i.reentryDate,i.netWeight,i.netWeightUnit,i.nsdlStatus,i.dgdcStatus,i.forwardedStatus,i.mopStatus,i.challanDate,i.importType FROM ImportSub i " +
        "LEFT JOIN Party p ON i.exporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +  // Assuming exporter is related to Party
        "WHERE " +
        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
        "(:startDate IS NULL AND :endDate >= i.sirDate) OR " +
        "(:startDate <= i.sirDate AND :endDate IS NULL) OR " +
        "(:startDate <= i.sirDate AND :endDate >= i.sirDate)) " +
        "AND i.companyId = :companyId " +
        "AND i.branchId = :branchId " +
        "AND i.handover_Party_Name = :party " +
        "AND i.importType NOT IN('LGD','Zone to Zone') " +
        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
        "(i.sirNo like CONCAT('%', :searchValue , '%') OR i.requestId like CONCAT('%', :searchValue , '%') OR p.partyName like CONCAT('%', :searchValue , '%'))) " + 
        "ORDER BY i.sirNo DESC")
List<Object[]> findByCHAAttributes(
        @Param("companyId") String companyId,
        @Param("branchId") String branchId,
        @Param("party") String party,
        @Param("dgdcStatus") String dgdcStatus,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("searchValue") String searchValue
);



@Query("SELECT i.sirNo,i.sirDate,i.requestId,i.exporter,i.nop,i.reentryDate,i.netWeight,i.netWeightUnit,i.nsdlStatus,i.dgdcStatus,i.forwardedStatus,i.mopStatus,i.challanDate,i.importType FROM ImportSub i " +
        "LEFT JOIN Party p ON i.exporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +  // Assuming exporter is related to Party
        "WHERE " +
        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
        "(:startDate IS NULL AND :endDate >= i.sirDate) OR " +
        "(:startDate <= i.sirDate AND :endDate IS NULL) OR " +
        "(:startDate <= i.sirDate AND :endDate >= i.sirDate)) " +
        "AND i.companyId = :companyId " +
        "AND i.branchId = :branchId " +
        "AND i.exporter = :party " +
        "AND (i.importType='LGD' or i.importType='Zone to Zone') " +
        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
        "(i.sirNo like CONCAT('%', :searchValue , '%') OR i.requestId like CONCAT('%', :searchValue , '%') OR p.partyName like CONCAT('%', :searchValue , '%'))) " + 
        "ORDER BY i.sirNo DESC")
List<Object[]> findByLGDPartyAttributes(
        @Param("companyId") String companyId,
        @Param("branchId") String branchId,
        @Param("party") String party,
        @Param("dgdcStatus") String dgdcStatus,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("searchValue") String searchValue
);


@Query("SELECT i.sirNo,i.sirDate,i.requestId,i.exporter,i.nop,i.reentryDate,i.netWeight,i.netWeightUnit,i.nsdlStatus,i.dgdcStatus,i.forwardedStatus,i.mopStatus,i.challanDate,i.importType FROM ImportSub i " +
        "LEFT JOIN Party p ON i.exporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +  // Assuming exporter is related to Party
        "WHERE " +
        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
        "(:startDate IS NULL AND :endDate >= i.sirDate) OR " +
        "(:startDate <= i.sirDate AND :endDate IS NULL) OR " +
        "(:startDate <= i.sirDate AND :endDate >= i.sirDate)) " +
        "AND i.companyId = :companyId " +
        "AND i.branchId = :branchId " +
        "AND i.handover_Party_Name = :party " +
        "AND (i.importType='LGD' or i.importType='Zone to Zone') " +
        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
        "(i.sirNo like CONCAT('%', :searchValue , '%') OR i.requestId like CONCAT('%', :searchValue , '%') OR p.partyName like CONCAT('%', :searchValue , '%'))) " + 
        "ORDER BY i.sirNo DESC")
List<Object[]> findByLGDCHAAttributes(
        @Param("companyId") String companyId,
        @Param("branchId") String branchId,
        @Param("party") String party,
        @Param("dgdcStatus") String dgdcStatus,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("searchValue") String searchValue
);



}
