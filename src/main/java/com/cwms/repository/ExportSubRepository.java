package com.cwms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.ExportSub;
import com.cwms.entities.Import;
import com.cwms.entities.ImportSub;

import jakarta.transaction.Transactional;

@EnableJpaRepositories
public interface ExportSubRepository extends JpaRepository<ExportSub, String> {
	
	@Query(value = "SELECT e.serDate,e.serNo,e.exporter,e.nop,e.invoiceNo,e.challanNo,e.requestId,e.dgdcStatus FROM ExportSub e WHERE e.companyId =:compnayId AND e.branchId =:branchId AND ((:dgdcstatus = 'Handed over to DGDC SEEPZ' AND DATE_FORMAT(e.serDate, '%Y-%m-%d') =:serDate) OR (:dgdcstatus = 'Handed over to Party/CHA' AND e.dgdcStatus != 'Handed over to DGDC SEEPZ' AND DATE_FORMAT(e.outDate, '%Y-%m-%d') =:serDate) OR (:dgdcstatus = 'Exit from DGDC SEEPZ Gate' AND e.dgdcStatus NOT IN ('Handed over to DGDC SEEPZ','Handed over to Party/CHA') AND DATE_FORMAT(e.outDate, '%Y-%m-%d') =:serDate)) ORDER BY serNo ASC")

	List<Object[]> findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus1(@Param("serDate") String serDate,
			@Param("compnayId") String compnayId, @Param("branchId") String branchId,
			@Param("dgdcstatus") String dgdcStatus);
	
	
	
	
	@Query(value="select e.dgdcStatus from ExportSub e where e.companyId=:cid and e.branchId=:bid and e.requestId=:reqid and e.serNo=:ser")
	String findStatus(@Param("cid") String cid,@Param("bid") String bid,@Param("reqid") String reqid,@Param("ser") String ser);
	
	@Query(value = "SELECT * FROM Importsub WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND Out_Date =  :startDate AND (gate_pass_status IS NULL OR gate_pass_status != 'Y' OR gate_pass_status = '')", nativeQuery = true)
    List<ImportSub> findByCompanyAndBranchAndDate3(String companyId, String branchId, Date startDate);
	
	
	@Query(value = "SELECT * FROM Exportsub WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND Out_Date = :startDate AND (gate_pass_status IS NULL OR gate_pass_status != 'Y' OR gate_pass_status = '')", nativeQuery = true)
	 List<ExportSub> findByCompanyAndBranchAndSerDateANDDGDC(String companyId, String branchId, Date startDate);
	
	
	
	@Query("SELECT i FROM ExportSub i " +
		       "WHERE " +
		       "i.companyId = :companyId " +
		       "AND i.branchId = :branchId " +
		       "AND i.exporter = :PartyId " +
		       "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
		       "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
		       "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
		       "(:startDate <= i.serDate AND :endDate >= i.serDate)) " +
		       "AND i.status != 'D'")
		List<ExportSub> findImportBySerDateStartDateAndEndDate(
		        @Param("companyId") String companyId,
		        @Param("branchId") String branchId,
		        @Param("startDate") Date startDate,
		        @Param("endDate") Date endDate,
		        @Param("PartyId") String PartyId);
	
	
	 @Query("SELECT COUNT(DISTINCT i.exporter) FROM ExportSub i " +
	           "WHERE i.companyId = :companyId " +
	           "AND i.branchId = :branchId " +
	           "AND i.serDate = :serDate "+
	           "AND i.status <> 'D'" )
	    int countDistinctImporterIdsBySERDATE(
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("serDate") Date serDate
	    );
	
//	@Query("SELECT i.requestId,i.serNo,i.serDate,i.exporter,i.nop,i.passoutWeight,i.passoutWeightUnit,i.dgdcStatus,i.nsdlStatus,i.forwardedStatus,i.mopStatus,i.challanDate FROM ExportSub i " +
//	        "LEFT JOIN Party p ON i.exporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +  // Assuming exporter is related to Party
//	        "WHERE " +
//	        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
//	        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
//	        "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
//	        "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
//	        "(:startDate <= i.serDate AND :endDate >= i.serDate)) " +
//	        "AND i.companyId = :companyId " +
//	        "AND i.branchId = :branchId " +
//	        "AND i.status != 'D' " +
//	        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
//	        "(i.serNo like CONCAT('%', :searchValue, '%') OR i.requestId like CONCAT('%', :searchValue, '%') OR p.partyName like CONCAT('%', :searchValue, '%'))) " + 
//	        "ORDER BY i.serNo DESC")
//	List<Object[]> findByAttributes1(
//	        @Param("companyId") String companyId,
//	        @Param("branchId") String branchId,
//	        @Param("dgdcStatus") String dgdcStatus,
//	        @Param("startDate") Date startDate,
//	        @Param("endDate") Date endDate,
//	        @Param("searchValue") String searchValue
//	);
	
	
	
	
	
	
	
	
	
	
	
	@Query(value = "select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid and i.status<>'D' order by i.ser_no desc", nativeQuery = true)
	public List<ExportSub> getall(@Param("cid") String cid,@Param("bid") String bid);
	
	@Query(value = "select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid", nativeQuery = true)
	public List<ExportSub> getallFORCHECK(@Param("cid") String cid,@Param("bid") String bid);
	

	List<ExportSub> findByCompanyIdAndBranchIdAndExporterAndOutDate(String companyId, String branchId,String partyId, Date serDate);
	
	@Query(value="select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid and i.ser_no=:subid",nativeQuery=true)
	public ExportSub findExportSubByseronly(@Param("cid") String cid,@Param("bid") String bid,@Param("subid") String subid);
	
	@Query(value="select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid and i.request_id=:reqid",nativeQuery=true)
	public  List<ExportSub>findRequestId1(@Param("cid") String cid,@Param("bid") String bid,@Param("reqid") String reqid);
	
	
	@Query(value="select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid and i.ser_no=:subid and i.request_id=:reqid",nativeQuery=true)
	public ExportSub findExportSubByser(@Param("cid") String cid,@Param("bid") String bid,@Param("subid") String subid,@Param("reqid") String reqid);
	
	
	@Query(value="select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid and i.request_id=:reqid",nativeQuery=true)
	public ExportSub findRequestId(@Param("cid") String cid,@Param("bid") String bid,@Param("reqid") String reqid);
	
	@Query(value="select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid and i.exp_sub_id=:subid and i.request_id=:reqid",nativeQuery=true)
	public ExportSub findExportSub(@Param("cid") String cid,@Param("bid") String bid,@Param("subid") String subid,@Param("reqid") String reqid);
	
	@Transactional
	@Modifying
	@Query(value="update exportsub set nsdl_status=:nsdl,status_doc=:statusdoc where company_id=:cid and branch_id=:bid and exp_sub_id=:expid and request_id=:reqid",nativeQuery=true)
	public void updateData(@Param("nsdl") String nsdl,@Param("statusdoc") String statusdoc,@Param("cid") String cid,@Param("bid") String bid,@Param("expid") String expid,@Param("reqid") String reqid);
	
	@Transactional
	@Modifying
	@Query(value="update exportsub set dgdc_status='Handed over to Party/CHA',handover_party_cha='P',handover_party_name=:hpid,handover_represntative_id=:representid, out_date = CURDATE()  where company_id=:cid and branch_id=:bid and exp_sub_id=:eid and request_id=:rid",nativeQuery=true)
	public void updateStatus(@Param("cid") String cid,@Param("bid") String bid,@Param("eid") String eid,@Param("rid") String rid,@Param("hpid") String hpid,@Param("representid") String representid);
	
//	@Transactional
//	@Modifying
//	@Query(value="update exportsub set dgdc_status='Handed over to Party/CHA',handover_party_cha='C',handover_party_name=:hpid,handover_represntative_id=:representid  where company_id=:cid and branch_id=:bid and exp_sub_id=:eid and request_id=:rid",nativeQuery=true)
//	public void updateCHAStatus(@Param("cid") String cid,@Param("bid") String bid,@Param("eid") String eid,@Param("rid") String rid,@Param("hpid") String hpid,@Param("representid") String representid);

	
//	Updating outdate
	
	@Transactional
	@Modifying
	@Query(value="update exportsub set dgdc_status='Handed over to Party/CHA',handover_party_cha='C',handover_party_name=:hpid,handover_represntative_id=:representid , out_date = CURDATE()  where company_id=:cid and branch_id=:bid and exp_sub_id=:eid and request_id=:rid",nativeQuery=true)
	public void updateCHAStatus(@Param("cid") String cid,@Param("bid") String bid,@Param("eid") String eid,@Param("rid") String rid,@Param("hpid") String hpid,@Param("representid") String representid);
	
	
	@Transactional
	@Modifying
	@Query(value="update exportsub set status='D'  where company_id=:cid and branch_id=:bid and exp_sub_id=:eid and request_id=:rid",nativeQuery=true)
	public void updateDELETEStatus(@Param("cid") String cid,@Param("bid") String bid,@Param("eid") String eid,@Param("rid") String rid);
	 
	 @Query(value = "SELECT * FROM Exportsub WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND Out_Date = :startDate AND handover_represntative_id = :representativeId AND (handover_party_cha = :paryCHAId OR handover_party_name = :paryCHAId) AND (gate_pass_status IS NULL OR gate_pass_status != 'Y' OR gate_pass_status = '')", nativeQuery = true)
	 List<ExportSub> findByCompanyAndBranchAndSerDate(String companyId, String branchId, Date startDate, String paryCHAId, String representativeId);
 
	 
//	 @Query(value = "SELECT * FROM Exportsub WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND SER_Date = :startDate AND handover_represntative_id = :representativeId AND (handover_party_cha = :paryCHAId OR handover_party_name = :paryCHAId)", nativeQuery = true)
//	 List<ExportSub> findByCompanyAndBranchAndSerDate3(String companyId, String branchId, Date startDate, String paryCHAId, String representativeId);
 
	 
	 @Query(value = "SELECT * FROM Exportsub WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND Out_Date = :startDate AND handover_represntative_id = :representativeId AND (handover_party_cha = :paryCHAId OR handover_party_name = :paryCHAId)", nativeQuery = true)
	 List<ExportSub> findByCompanyAndBranchAndSerDate3(String companyId, String branchId, Date startDate, String paryCHAId, String representativeId);
	 
	 
	 @Query(value="select i.* from exportsub where (:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdc_status = :dgdcStatus) and ((:startDate IS NULL AND :endDate IS NULL) OR (:startDate IS NULL AND :endDate >= i.ser_date) OR (:startDate <= i.ser_date AND :endDate IS NULL) OR (:startDate <= i.ser_date AND :endDate >= i.ser_date)) AND i.company_id = :companyId AND i.branch_id = :branchId ORDER BY i.ser_date ASC",nativeQuery=true)
		List<ExportSub> findByAttributes(
		    @Param("companyId") String companyId,
		    @Param("branchId") String branchId,
		    @Param("dgdcStatus") String dgdcStatus,
		    @Param("startDate") Date startDate,
		    @Param("endDate") Date endDate);
	 
	 @Query(value = "SELECT * FROM exportsub WHERE DATE_FORMAT(ser_date, '%Y-%m-%d') =:serDate AND company_id =:compnayId AND branch_id =:branchId AND ((:dgdcstatus = 'Handed over to DGDC SEEPZ') OR (:dgdcstatus = 'Handed over to Party/CHA' AND dgdc_status != 'Handed over to DGDC SEEPZ') OR (:dgdcstatus = 'Exit from DGDC SEEPZ Gate' AND dgdc_status NOT IN ('Handed over to DGDC SEEPZ','Handed over to Party/CHA'))) ORDER BY ser_No ASC", nativeQuery = true)

		List<ExportSub> findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(@Param("serDate") String serDate,
				@Param("compnayId") String compnayId, @Param("branchId") String branchId,
				@Param("dgdcstatus") String dgdcStatus);
	 
	 @Query(value = "SELECT DISTINCT * " +
	            "FROM exportsub " +
	            "WHERE company_id = :companyId " +
	            "AND branch_id = :branchId " +
	            "AND ser_date BETWEEN :startDate AND :endDate " +
	           
	            "AND exporter = :exporter", nativeQuery = true)
	     List<ExportSub> findImportAllData(String companyId, String branchId, Date startDate, Date endDate,
	                                    String exporter);
	 
	 
	 @Query(value = "SELECT DISTINCT * " +
	            "FROM exportsub " +
	            "WHERE company_id = :companyId " +
	            "AND branch_id = :branchId " +    
	            "AND request_id = :requestId", nativeQuery = true)
	     List<ExportSub> findRequestIdData(String companyId, String branchId,
	                                    String requestId);
	 
	 @Query(value = "SELECT  * " +
	            "FROM exportsub " +
	            "WHERE company_id = :companyId " +
	            "AND branch_id = :branchId " + 
	      
	            "AND ser_date BETWEEN :startDate AND :endDate ",
	           
	            nativeQuery = true)
	     List<ExportSub> findExportSubAllData(String companyId, String branchId, Date startDate, Date endDate);
	 
	 public ExportSub findByCompanyIdAndBranchIdAndSerDateAndExporterAndNop(String companyId, String branchId, Date startDate,String exporter,int nop);
	 
	//shubham 
		 @Query(value = "SELECT SUM(nop) FROM exportsub WHERE Company_Id = :cid AND Branch_Id = :bid  AND SER_Date = :date1", nativeQuery = true)
		    Integer findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDate2(@Param("cid") String cid, @Param("bid") String bid,  @Param("date1") Date date1);
		
		 @Query(value = "SELECT SUM(nop) FROM exportsub WHERE Company_Id = :cid AND Branch_Id = :bid AND DGDC_Status = :string AND SER_Date = :date1", nativeQuery = true)
		    Integer findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDate4(@Param("cid") String cid, @Param("bid") String bid, @Param("string") String string, @Param("date1") Date date1);
		
		 @Transactional
		 @Modifying
		 @Query(value = "UPDATE ExportSub SET gate_pass_status = 'Y' WHERE exp_sub_id IN :exportSubIds", nativeQuery = true)
		 void setGatePassStatusToY(@Param("exportSubIds") List<String> exportSubIds);
		 
		 List<ExportSub> findByCompanyIdAndBranchIdAndExporterAndSerDateBetweenAndStatusNot(String companyId, String branchId,String partyId, Date startDate,Date endDate , String Status);
//		 List<ExportSub> findByCompanyIdAndBranchIdAndExporterAndSerDateBetweenAndStatusNot(String companyId, String branchId,String partyId, Date startDate,Date endDate , String Status);
		 List<ExportSub> findByCompanyIdAndBranchIdAndExporterAndSerDate(String companyId, String branchId,String partyId, Date serDate);
		 
		 
		//sanket
		 
		 @Query(value="select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid and i.exporter=:exp and i.dgdc_status='Handed over to DGDC SEEPZ' and i.nsdl_status='Passed Out' and forwarded_status!='FWD_OUT' and i.status != 'D'",nativeQuery=true)
			public List<ExportSub> findExportSubByparty(@Param("cid") String cid,@Param("bid") String bid,@Param("exp") String exporter);
			
		 @Query(value = "select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid and i.exporter=:exporter and i.dgdc_status='Handed over to DGDC SEEPZ' and i.nsdl_status='Passed Out' and i.status != 'D' and forwarded_status!='FWD_OUT'", nativeQuery = true)
			public List<ExportSub> getalldatabyparty(@Param("cid") String cid,@Param("bid") String bid,@Param("exporter") String exporter);
			
		 
		 @Query(value = "select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid and i.exporter =:exporter and i.status<>'D' order by i.ser_no desc", nativeQuery = true)
			public List<ExportSub> getall1(@Param("cid") String cid,@Param("bid") String bid,@Param("exporter") String exporter);
			
			@Query(value = "select i.* from exportsub i where i.company_id=:cid and i.branch_id=:bid and i.handover_party_cha='C' and i.handover_party_name =:exporter and i.status<>'D' order by i.ser_no desc", nativeQuery = true)
			public List<ExportSub> getall2(@Param("cid") String cid,@Param("bid") String bid,@Param("exporter") String exporter);
			
			@Query("SELECT e FROM ExportSub e WHERE e.companyId = :companyId AND e.branchId = :branchId AND e.serNo = :serNo AND e.dgdcStatus = 'Handed over to DGDC SEEPZ' AND e.status <> 'D' AND (e.mopStatus <> 'Y' OR e.mopStatus IS NULL OR e.mopStatus = '')")
			ExportSub findByCompanyIdAndBranchIdAndSerNoAndAndDgdcStatusAndStatusNotAndMopStatusNot(@Param("companyId") String companyId, @Param("branchId") String branchId, @Param("serNo") String serNo);

			 
			 ExportSub findBySerNo(String serNo);
			 
			 @Query("SELECT i FROM ExportSub i " +
				        "LEFT JOIN Party p ON i.exporter = p.partyId " +  // Assuming exporter is related to Party
				        "WHERE " +
				        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
				        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
				        "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
				        "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
				        "(:startDate <= i.serDate AND :endDate >= i.serDate)) " +
				        "AND i.companyId = :companyId " +
				        "AND i.branchId = :branchId " +
				        "AND i.status != 'D' " +
				        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
				        "(i.serNo like CONCAT('%', :searchValue, '%') OR i.requestId like CONCAT('%', :searchValue, '%') OR p.partyName like CONCAT('%', :searchValue, '%'))) " + 
				        "ORDER BY i.serNo DESC")
				List<ExportSub> findByAttributes6(
				        @Param("companyId") String companyId,
				        @Param("branchId") String branchId,
				        @Param("dgdcStatus") String dgdcStatus,
				        @Param("startDate") Date startDate,
				        @Param("endDate") Date endDate,
				        @Param("searchValue") String searchValue
				);
			 
			 ExportSub findByCompanyIdAndBranchIdAndSerNo(String companyId, String branchId, String sirNo);
			 
			 
			 @Query(value="select * from exportsub where mop_id=:cid",nativeQuery=true)
			 List<ExportSub> getbymoppassid(@Param("cid") String cid);
			 
			 
			 @Query(value="select * from exportsub where common_gate_pass_id=:cid",nativeQuery=true)
			 List<ExportSub> getbycommonid(@Param("cid") String cid);
			 
			 
			 @Query("SELECT i.requestId,i.serNo,i.serDate,i.exporter,i.nop,i.passoutWeight,i.passoutWeightUnit,i.dgdcStatus,i.nsdlStatus,i.forwardedStatus,i.mopStatus,i.challanDate FROM ExportSub i " +
				        "LEFT JOIN Party p ON i.exporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +  // Assuming exporter is related to Party
				        "WHERE " +
				        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
				        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
				        "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
				        "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
				        "(:startDate <= i.serDate AND :endDate >= i.serDate)) " +
				        "AND i.companyId = :companyId " +
				        "AND i.branchId = :branchId " +
				        "AND i.status != 'D' " +
				        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
				        "(i.serNo like CONCAT('%', :searchValue, '%') OR i.requestId like CONCAT('%', :searchValue, '%') OR p.partyName like CONCAT('%', :searchValue, '%'))) " + 
				        "ORDER BY i.serNo DESC")
				List<Object[]> findByAttributes1(
				        @Param("companyId") String companyId,
				        @Param("branchId") String branchId,
				        @Param("dgdcStatus") String dgdcStatus,
				        @Param("startDate") Date startDate,
				        @Param("endDate") Date endDate,
				        @Param("searchValue") String searchValue
				);
				
				
				
				@Query("SELECT i.requestId,i.serNo,i.serDate,i.exporter,i.nop,i.passoutWeight,i.passoutWeightUnit,i.dgdcStatus,i.nsdlStatus,i.forwardedStatus,i.mopStatus,i.challanDate FROM ExportSub i " +
				        "LEFT JOIN Party p ON i.exporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +  // Assuming exporter is related to Party
				        "WHERE " +
				        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
				        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
				        "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
				        "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
				        "(:startDate <= i.serDate AND :endDate >= i.serDate)) " +
				        "AND i.companyId = :companyId " +
				        "AND i.branchId = :branchId " +
				        "AND i.exporter = :party " +
				        "AND i.status != 'D' " +
				        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
				        "(i.serNo like CONCAT('%', :searchValue, '%') OR i.requestId like CONCAT('%', :searchValue, '%') OR p.partyName like CONCAT('%', :searchValue, '%'))) " + 
				        "ORDER BY i.serNo DESC")
				List<Object[]> findByPartyAttributes(
				        @Param("companyId") String companyId,
				        @Param("branchId") String branchId,
				        @Param("party") String party,
				        @Param("dgdcStatus") String dgdcStatus,
				        @Param("startDate") Date startDate,
				        @Param("endDate") Date endDate,
				        @Param("searchValue") String searchValue
				);
				
				
				@Query("SELECT i.requestId,i.serNo,i.serDate,i.exporter,i.nop,i.passoutWeight,i.passoutWeightUnit,i.dgdcStatus,i.nsdlStatus,i.forwardedStatus,i.mopStatus,i.challanDate FROM ExportSub i " +
				        "LEFT JOIN Party p ON i.exporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +  // Assuming exporter is related to Party
				        "WHERE " +
				        "(:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
				        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
				        "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
				        "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
				        "(:startDate <= i.serDate AND :endDate >= i.serDate)) " +
				        "AND i.companyId = :companyId " +
				        "AND i.branchId = :branchId " +
				        "AND i.handover_Party_Name = :party " +
				        "AND i.status != 'D' " +
				        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
				        "(i.serNo like CONCAT('%', :searchValue, '%') OR i.requestId like CONCAT('%', :searchValue, '%') OR p.partyName like CONCAT('%', :searchValue, '%'))) " + 
				        "ORDER BY i.serNo DESC")
				List<Object[]> findByCHAAttributes(
				        @Param("companyId") String companyId,
				        @Param("branchId") String branchId,
				        @Param("party") String party,
				        @Param("dgdcStatus") String dgdcStatus,
				        @Param("startDate") Date startDate,
				        @Param("endDate") Date endDate,
				        @Param("searchValue") String searchValue
				);
			 
}
