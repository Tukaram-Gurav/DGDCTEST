package com.cwms.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import com.cwms.entities.Export;
import com.cwms.entities.Import;
import com.cwms.entities.ImportSub;

import jakarta.transaction.Transactional;

public interface ExportRepository extends JpaRepository<Export, String> {
//	Billing 
	
//	Billing Query
	@Query(value = "SELECT NEW com.cwms.entities.Export(i.companyId, i.branchId,i.sbNo,e.partyName,i.serNo,i.serDate,i.grossWeight,i.fobValueInINR,i.noOfPackages,i.outDate) " + "FROM Export i " + "LEFT JOIN Party e ON i.nameOfExporter = e.partyId And i.companyId=e.companyId And i.branchId=e.branchId " +
			"WHERE i.companyId = :companyId " + "AND i.branchId = :branchId "
			+ "AND i.status <> 'D' " + "AND i.nameOfExporter =:importerId " + "AND i.billCalculated = 'N' AND i.outDate > :lastinvoicedate  AND i.dgdcStatus In ('Handed over to Console','Exit from DGDC SEEPZ Gate')")
	List<Export> findExportAllDataBill(@Param("companyId") String companyId, @Param("branchId") String branchId, @Param("importerId") String importerId,@Param("lastinvoicedate")Date lastinvoicedate);

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Query(value="select distinct(e.pctmNo) from Export e where e.companyId=:cid and e.branchId=:bid and e.tpNo=:tp and e.airlineCode=:airline")
	   public String getPctmNo(@Param("cid") String cid,@Param("bid") String bid,@Param("tp") String tp,@Param("airline") String airline);
	
	
	@Query("SELECT DISTINCT i.tpNo FROM Export i " +
	           "WHERE i.companyId = :companyId " +
	           "AND i.branchId = :branchId " +
	           "AND i.tpDate = CURDATE() " + 
	           "AND i.status <> 'D'" )
	List<String> gettpNos(
	        @Param("companyId") String companyId,
	        @Param("branchId") String branchId
	        
	);

	
	
	@Query("SELECT DISTINCT i.pctmNo FROM Export i " +
	           "WHERE i.companyId = :companyId " +
	           "AND i.branchId = :branchId " +
	           "AND i.tpDate = CURDATE() " + 
	           "AND i.tpNo = :tpNo " +
	           "AND i.airlineCode = :airlineCode " + 
	           "AND i.status <> 'D'" )
	List<String> getPCTMNO(
	        @Param("companyId") String companyId,
	        @Param("branchId") String branchId,
	        @Param("tpNo") String tpNo,
	        @Param("airlineCode") String airlineCode     
	);

	
	
	
	
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE Export SET dgdcStatus = 'Handed over to DGDC SEEPZ', pctmNo = '' ,pctmDate = null, tpNo = '' ,tpDate = null, dgdc_seepz_out_scan = 0 , holdStatus = 'Y' ,dgdc_seepz_out_scan = 0 , dgdc_cargo_in_scan = 0 ,editedBy = :user,outDate = null , cartingAgent = '' ,gatePassStatus = 'N',partyRepresentativeId = '' WHERE companyId = :cid and branchId = :bid and serNo = :serNo and sbNo = :sb_No and nameOfExporter = :exporter and sbRequestId =:requestId and status <> 'D'")
	int updateReverseToStock(
	    @Param("cid") String cid,
	    @Param("bid") String bid,
	    @Param("requestId") String requestId,
	    @Param("sb_No") String sb_No,
	    @Param("serNo") String serNo,	    
	    @Param("exporter") String exporter,
	    @Param("user") String user
	);


	@Transactional
	@Modifying
	@Query(value = "UPDATE Gate_In_Out SET dgdc_seepz_out_scan = 'N', dgdc_cargo_in_scan = 'N' WHERE companyId = :cid and branchId = :bid and doc_ref_no = :sb_No and erp_doc_ref_no =:requestId")
	int updateReverseToStockGateInOut(
	    @Param("cid") String cid,
	    @Param("bid") String bid,
	    @Param("requestId") String requestId,
	    @Param("sb_No") String sb_No	    
	);

	
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE Export SET  pctmNo = :pctmNo ,pctmDate = CURdATE(), tpNo = :tpNo ,tpDate = CURdATE(),editedBy = :user,outDate = CURdATE() , cartingAgent = :cartingAgent ,gatePassStatus = 'N',partyRepresentativeId = :representative,dgdcStatus='Handed over to Carting Agent'  WHERE companyId = :cid and branchId = :bid and serNo = :serNo and sbNo = :sb_No and nameOfExporter = :exporter and sbRequestId =:requestId and status <> 'D'")
	int updateExistingTpNo(
	    @Param("cid") String cid,
	    @Param("bid") String bid,
	    @Param("requestId") String requestId,
	    @Param("sb_No") String sb_No,
	    @Param("serNo") String serNo,	    
	    @Param("pctmNo") String pctmNo,
	    @Param("tpNo") String tpNo,
	    @Param("user") String user,
	    @Param("exporter") String exporter,
	    @Param("cartingAgent") String cartingAgent,
	    @Param("representative") String representative
	);
	
		
	
	
	
	@Query(value = "SELECT i.carting_Agent AS cartingAgent, i.representative_id AS partyRepresentativeId FROM Export i WHERE i.company_Id=:cid AND i.branch_Id=:bid AND i.tp_Date=CURDATE() AND i.tp_No=:tpno LIMIT 1", nativeQuery = true)
	public Map<String, String> findCartingAgentAndRepresentative(
	    @Param("cid") String cid,
	    @Param("bid") String bid,
	    @Param("tpno") String tp_no
	);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Query(value = "select i.serNo,i.serDate,i.nameOfExporter,i.noOfPackages,i.descriptionOfGoods,i.fobValueInINR,i.portOfDestination,i.scStatus,i.pcStatus,i.hpStatus,i.entityId,i.cartingAgent from Export i where i.companyId=:cid and i.branchId=:bid and i.tpDate=:date and i.tpNo=:tpno")
 	public List<Object[]> findByTpdateTpno1(
 	    @Param("cid") String cid,
 	    @Param("bid") String bid,
 	    @Param("date") Date date,
 	    @Param("tpno") String tp_no
 	   // @Param("status") char status
 	    );
	
	
	@Query(value ="SELECT e.serNo,e.countryOfDestination,e.portOfDestination,e.noOfPackages,e.sbNo,e.grossWeight,e.airwayBillNo,e.scStatus,e.pcStatus,e.hpStatus,e.pctmNo,e.serDate,e.sbDate,e.airlineName,e.uomGrossWeight FROM Export e " +
            "WHERE e.companyId = :companyId " +
            "AND e.branchId = :branchId " +
            "AND DATE_FORMAT(e.tpDate, '%Y-%m-%d') =:serDate " +
            "AND e.cartingAgent = :cartingAgent " +
            "AND e.tpNo = :tpNo " +
            "ORDER BY e.airwayBillNo ")
     List<Object[]> findExportTPData(@Param("companyId") String companyId,
    		 @Param("branchId") String branchId,
    		  @Param("serDate") String endDate,
    		  @Param("cartingAgent") String cartingAgent,
    		  @Param ("tpNo")String tpNo);
     
     
//     @Query(value ="SELECT distinct e.pctmNo,e.airlineName,sum(e.noOfPackages),COUNT(DISTINCT e.sbNo),COUNT(DISTINCT e.airwayBillNo) FROM Export e " +
//	            "WHERE e.companyId = :companyId " +
//	            "AND e.branchId = :branchId " +
//	            "AND DATE_FORMAT(e.tpDate, '%Y-%m-%d') =:serDate " +
//	            "AND e.airlineName IS NOT NULL " +
//	            "GROUP BY e.airlineName,e.pctmNo")
//	     List<Object[]> findExportPctmSummery(@Param("companyId") String companyId,
//	    		 @Param("branchId") String branchId,
//	    		  @Param("serDate") String endDate); 

     @Query(value ="SELECT distinct e.pctmNo,e.airlineName,sum(e.noOfPackages),COUNT(DISTINCT e.sbNo),COUNT(DISTINCT e.airwayBillNo) FROM Export e " +
	            "WHERE e.companyId = :companyId " +
	            "AND e.branchId = :branchId " +
	            "AND DATE_FORMAT(e.tpDate, '%Y-%m-%d') =:serDate " +
	            "AND e.tpNo = :tpNo " +
	            "AND e.airlineName IS NOT NULL " +
	            "GROUP BY e.airlineName,e.pctmNo")
	     List<Object[]> findExportPctmSummery(@Param("companyId") String companyId,
	    		 @Param("branchId") String branchId,
	    		  @Param("serDate") String endDate,@Param ("tpNo")String tpNo);
     
//     @Query(value ="SELECT distinct e.pctmNo,e.airlineName,sum(e.noOfPackages),COUNT(DISTINCT e.sbNo),COUNT(DISTINCT e.airwayBillNo) FROM Export e " +
//	            "WHERE e.companyId = :companyId " +
//	            "AND e.branchId = :branchId " +
//	            "AND DATE_FORMAT(e.tpDate, '%Y-%m-%d') =:serDate " +
//	            "AND e.airlineName IS NOT NULL " +
//	            "GROUP BY e.airlineName,e.pctmNo")
//	     List<Object[]> findExportPctmSummery(@Param("companyId") String companyId,
//	    		 @Param("branchId") String branchId,
//	    		  @Param("serDate") String endDate);
	
	
	@Query(value = "SELECT DISTINCT e.tpNo FROM Export e WHERE e.companyId =:companyId AND e.branchId =:branchId AND  DATE_FORMAT(e.tpDate, '%Y-%m-%d') =:serDate AND e.cartingAgent=:cartingAgent")
    List<String> findCommonTpNos(@Param("companyId") String companyId,
                                  @Param("branchId") String branchId,
                                 
                                  @Param("serDate") String endDate,
                                  @Param("cartingAgent") String cartingAgent);
	
	@Query(value = "SELECT " +
	        "(SELECT COALESCE(SUM(e.no_of_packages), 0) FROM Export e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND ((e.SER_Date < CURRENT_DATE() AND e.dgdc_status = 'Handed over to DGDC SEEPZ') OR (e.Out_Date = CURRENT_DATE() AND e.dgdc_status IN ('Handed over to Carting Agent', 'Exit from DGDC SEEPZ Gate', 'Entry at DGDC Cargo GATE', 'Handed over to DGDC Cargo')))) as exportOpeningBalance, " +
	        "(SELECT COALESCE(SUM(e.no_of_packages), 0) FROM Export e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SER_Date = CURRENT_DATE()) as exportSERDate, " +
	        "(SELECT COALESCE(SUM(e.no_of_packages), 0) FROM Export e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SER_Date <= CURRENT_DATE()) as exportDGDCStatus, " +
	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ExportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.SER_Date = CURRENT_DATE()) as exportSubSERDate, " +
	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ExportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.DGDC_Status = 'Handed over to DGDC SEEPZ' AND es.SER_Date <= CURRENT_DATE()) as exportSubDGDCStatus, " +
	        "(SELECT COALESCE(SUM(e.no_of_packages), 0) FROM Export e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status IN ('Handed over to Carting Agent','Exit from DGDC SEEPZ Gate', 'Entry at DGDC Cargo GATE', 'Handed over to DGDC Cargo') AND e.Out_Date = CURRENT_DATE()) as exportExitDGDC, " +
	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ExportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.DGDC_Status = 'Exit from DGDC SEEPZ Gate' AND es.Out_Date = CURRENT_DATE()) as exportSubExitDGDC, " +
	        "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND d.Status = 'Deposited' AND d.Parcel_Type = 'Export' AND d.Deposit_Date < CURRENT_DATE()) as exportDetentionOpeningBalance, " +
	        "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND d.Status = 'Deposited' AND d.Parcel_Type = 'Export' AND d.Deposit_Date = CURRENT_DATE()) as exportDetentionDeposite, " +
	        "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND ((d.Status = 'Withdraw' AND d.Parcel_Type = 'Export' AND d.Withdraw_Date = CURRENT_DATE()) OR (d.Status = 'Issued' AND d.Issue_Date = CURRENT_DATE()))) as exportDetentionWithdrawn, " +
	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ExportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND ((es.SER_Date < CURRENT_DATE() AND es.dgdc_status = 'Handed over to DGDC SEEPZ') OR (es.Out_Date = CURRENT_DATE() AND es.dgdc_status = 'Exit from DGDC SEEPZ Gate' ))) as exportSubOpeningBalance"
	        , nativeQuery = true)
	List<Object[]> getCombinedResults(@Param("companyId") String companyId, @Param("branchId") String branchId);


	
//	@Query(value = "SELECT " +
//	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND ((e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SIR_Date < CURRENT_DATE() AND (e.tp_date IS NULL OR e.tp_date <> CURRENT_DATE())) OR (e.Out_Date = CURRENT_DATE() AND e.dgdc_status IN ('Exit from DGDC SEEPZ Gate', 'Handed over to Party/CHA') AND (e.tp_date IS NULL OR e.tp_date <> CURRENT_DATE()) AND e.SIR_Date < CURRENT_DATE()))) as importOpeningBalance, " +
//	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.Tp_Date = CURRENT_DATE() And e.nipt_status <> 'Y' And e.dgdc_status IN ('Exit from DGDC SEEPZ Gate', 'Handed over to Party/CHA','Handed over to DGDC SEEPZ')) as importReceived, " +
//	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SIR_Date = CURRENT_DATE() And e.nipt_status = 'Y') as importNIPTReceived, " +
//	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.SIR_Date = CURRENT_DATE()) as importReceivedSub, " +
//	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SIR_Date <= CURRENT_DATE() And e.nipt_status <> 'Y') as importStock, " +
//	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SIR_Date <= CURRENT_DATE() And e.nipt_status = 'Y') as importNIPTStock, " +
//	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SIR_Date = CURRENT_DATE() And forwarded_status = 'FWD_IN') as importForwardedIn, " +
//	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SIR_Date = CURRENT_DATE() And forwarded_status = 'FWD_OUT') as importForwardedOut, " +
//	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Exit from DGDC SEEPZ Gate' AND e.Out_Date = CURRENT_DATE() and e.nipt_status <> 'Y' ) as importDeliveries, " +
//	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.Out_Date = CURRENT_DATE() And e.DGDC_Status = 'Exit from DGDC SEEPZ Gate' and e.nipt_status = 'Y') as importNiptDeliveries, " +
//	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.DGDC_Status = 'Exit from DGDC SEEPZ Gate' AND es.Out_Date = CURRENT_DATE()) as importDeliveriesSub, " +
//	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.DGDC_Status = 'Handed over to DGDC SEEPZ' AND es.SIR_Date <= CURRENT_DATE()) as importSubStock, " +
//	        "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND d.Status = 'Deposited' AND d.Parcel_Type = 'Import' AND d.Deposit_Date < CURRENT_DATE()) as importDetentionOpeningBalance, " +
//	        "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND d.Status = 'Deposited' AND d.Parcel_Type = 'Import' AND d.Deposit_Date = CURRENT_DATE()) as importDetentionDeposite, " +
//	        "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND ((d.Status = 'Withdraw' AND d.Parcel_Type = 'Import' AND d.Withdraw_Date = CURRENT_DATE()) OR (d.Status = 'Issued' AND d.Issue_Date = CURRENT_DATE()))) as importDetentionWithdrawn, " +
//	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND ((es.SIR_Date < CURRENT_DATE() AND es.dgdc_status = 'Handed over to DGDC SEEPZ') OR (es.Out_Date = CURRENT_DATE() AND es.dgdc_status = 'Exit from DGDC SEEPZ Gate' ))) as importSubOpeningBalance ", 
//	        nativeQuery = true)
//	List<Object[]> getImportData(@Param("companyId") String companyId, @Param("branchId") String branchId);

	
	
	@Query(value = "SELECT " +
	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND ((e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SIR_Date < CURRENT_DATE() AND (e.tp_date IS NULL OR e.tp_date <> CURRENT_DATE())) OR (e.Out_Date = CURRENT_DATE() AND e.dgdc_status IN ('Exit from DGDC SEEPZ Gate', 'Handed over to Party/CHA') AND (e.tp_date IS NULL OR e.tp_date <> CURRENT_DATE()) AND e.SIR_Date < CURRENT_DATE()))) as importOpeningBalance, " +
	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.Tp_Date = CURRENT_DATE() And e.nipt_status <> 'Y' And e.dgdc_status IN ('Exit from DGDC SEEPZ Gate', 'Handed over to Party/CHA','Handed over to DGDC SEEPZ')) as importReceived, " +
	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SIR_Date = CURRENT_DATE() And e.nipt_status = 'Y') as importNIPTReceived, " +
	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.SIR_Date = CURRENT_DATE()) as importReceivedSub, " +
	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SIR_Date <= CURRENT_DATE() And e.nipt_status <> 'Y') as importStock, " +
	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SIR_Date <= CURRENT_DATE() And e.nipt_status = 'Y') as importNIPTStock, " +
	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SIR_Date = CURRENT_DATE() And forwarded_status = 'FWD_IN') as importForwardedIn, " +
	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SIR_Date = CURRENT_DATE() And forwarded_status = 'FWD_OUT') as importForwardedOut, " +
	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Exit from DGDC SEEPZ Gate' AND e.Out_Date = CURRENT_DATE() and e.nipt_status <> 'Y' ) as importDeliveries, " +
	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.Out_Date = CURRENT_DATE() And e.DGDC_Status = 'Exit from DGDC SEEPZ Gate' and e.nipt_status = 'Y') as importNiptDeliveries, " +
	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.DGDC_Status = 'Exit from DGDC SEEPZ Gate' AND es.Out_Date = CURRENT_DATE()) as importDeliveriesSub, " +
	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.DGDC_Status = 'Handed over to DGDC SEEPZ' AND es.SIR_Date <= CURRENT_DATE()) as importSubStock, " +
	        "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND d.Status = 'Deposited' AND d.Parcel_Type = 'Import' AND d.Deposit_Date < CURRENT_DATE()) as importDetentionOpeningBalance, " +
	        "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND d.Status = 'Deposited' AND d.Parcel_Type = 'Import' AND d.Deposit_Date = CURRENT_DATE()) as importDetentionDeposite, " +
	        "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND ((d.Status = 'Withdraw' AND d.Parcel_Type = 'Import' AND d.Withdraw_Date = CURRENT_DATE()) OR (d.Status = 'Issued' AND d.Issue_Date = CURRENT_DATE()))) as importDetentionWithdrawn, " +
	        "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND ((es.SIR_Date < CURRENT_DATE() AND es.dgdc_status = 'Handed over to DGDC SEEPZ') OR (es.Out_Date = CURRENT_DATE() AND es.dgdc_status = 'Exit from DGDC SEEPZ Gate' ))) as importSubOpeningBalance, "+
	        "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SIR_Date = CURRENT_DATE() And e.pc_status = 'Y' And nipt_Status <> 'Y') as importPCReceived ",
	        nativeQuery = true)
	List<Object[]> getImportData(@Param("companyId") String companyId, @Param("branchId") String branchId);

	
	  List<Export> findByCompanyIdAndBranchIdAndNameOfExporterAndOutDateAndStatusNot(String companyId, String branchId,String partyId, Date serDate , String Status);
	
	
	
	@Query("SELECT i FROM Export i " +
		       "WHERE " +
		       "i.companyId = :companyId " +
		       "AND i.branchId = :branchId " +
		       "AND i.nameOfExporter = :PartyId " +
		       "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
		       "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
		       "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
		       "(:startDate <= i.serDate AND :endDate >= i.serDate)) " +
		       "AND i.status != 'D'")
		List<Export> findImportBySerDateStartDateAndEndDate(
		        @Param("companyId") String companyId,
		        @Param("branchId") String branchId,
		        @Param("startDate") Date startDate,
		        @Param("endDate") Date endDate,
		        @Param("PartyId") String PartyId);
	
	
	
	
	 @Query("SELECT COUNT(DISTINCT i.nameOfExporter) FROM Export i " +
	           "WHERE i.companyId = :companyId " +
	           "AND i.branchId = :branchId " +
	           "AND i.tpNo = :tpNo " +
	           "AND i.tpDate = :tpDate "+
	           "AND i.status <> 'D'" 
	           )
	    int countDistinctImporterIds(
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("tpNo") String tpNo,
	            @Param("tpDate") Date tpDate
	    );
	
	@Query("SELECT COUNT(DISTINCT i.nameOfExporter) FROM Export i " +
	           "WHERE i.companyId = :companyId " +
	           "AND i.branchId = :branchId " +
	           "AND i.serDate = :serDate "+
	           "AND i.status <> 'D'" )
	    int countDistinctImporterIdsBySERDATE(
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("serDate") Date serDate
	    );

	@Query(value = "SELECT * FROM Export WHERE status != 'd'", nativeQuery = true)
	List<Export> findAllNotDeleted();
	
	@Query(value="select i from Export i where i.companyId=:cid and i.branchId=:bid and i.sbRequestId=:sbreq and status != 'D'")
	Export findDatabyreq(@Param("cid") String cid, @Param("bid") String bid,@Param("sbreq") String sbreq);
	
	@Query(value="select * from export where company_id=:cid and branch_id=:bid and status != 'D'",nativeQuery=true)
	List<Export> findAllData(@Param("cid") String cid, @Param("bid") String bid);
	
	
	
	@Query(value="select * from export where company_id=:cid and branch_id=:bid and sb_number=:ser",nativeQuery=true)
	Export finexportsbdata(@Param("cid") String cid, @Param("bid") String bid,@Param("ser") String ser);
	
//	List<Export> findByCompanyIdAndBranchIdAndNameOfExporterAndOutDateAndStatusNot(String companyId, String branchId,String partyId, Date serDate , String Status);
	
//	List<Export> findByCompanyIdAndBranchIdAndNameOfExporterAndSerDateAndStatusNot(String companyId, String branchId,String partyId, Date serDate , String Status);
	
	
	@Query(value="select * from export where company_id=:cid and branch_id=:bid and ser_no=:ser",nativeQuery=true)
	Export finexportdata(@Param("cid") String cid, @Param("bid") String bid,@Param("ser") String ser);
	

	@Query(value="select * from export where company_id=:cid and branch_id=:bid and status != 'D'",nativeQuery=true)
	List<Export> findAllData1(@Param("cid") String cid, @Param("bid") String bid);

	@Query(value = "SELECT * FROM export WHERE status != 'D' AND dgdc_status = 'Handed over to DGDC SEEPZ' AND company_id = ?1 AND branch_id = ?2", nativeQuery = true)
    List<Export> findExportsByCompanyAndBranch(String companyId, String branchId);

	@Query(value = "SELECT * FROM Export WHERE company_id=:cid and branch_id=:bid and ser_no=:ser", nativeQuery = true)
	Export findBySer(@Param("cid") String cid, @Param("bid") String bid, @Param("ser") String ser);
	
	@Query(value = "SELECT * FROM export WHERE DATE_FORMAT(created_date, '%Y-%m-%d') = :date AND company_id = :cid AND branch_id = :bid AND ((:dgdcstatus = 'Entry at DGDC SEEPZ Gate') OR (:dgdcstatus = 'Handed over to DGDC SEEPZ' AND dgdc_status != 'Entry at DGDC SEEPZ Gate') OR (:dgdcstatus = 'Handed over to Carting Agent' AND dgdc_status NOT IN ('Entry at DGDC SEEPZ Gate', 'Handed over to DGDC SEEPZ')) OR (:dgdcstatus = 'Exit from DGDC SEEPZ Gate' AND dgdc_status NOT IN ('Entry at DGDC SEEPZ Gate', 'Handed over to DGDC SEEPZ', 'Handed over to Carting Agent')) OR (:dgdcstatus = 'Entry at DGDC Cargo Gate' AND dgdc_status NOT IN ('Entry at DGDC SEEPZ Gate', 'Handed over to DGDC SEEPZ', 'Handed over to Carting Agent', 'Exit from DGDC SEEPZ Gate')) OR (:dgdcstatus = 'Handed over to DGDC Cargo' AND dgdc_status NOT IN ('Entry at DGDC SEEPZ Gate', 'Handed over to DGDC SEEPZ', 'Handed over to Carting Agent', 'Exit from DGDC SEEPZ Gate', 'Entry at DGDC Cargo Gate')) OR (:dgdcstatus = 'Handed Over to Airline' AND dgdc_status NOT IN ('Entry at DGDC SEEPZ Gate', 'Handed over to DGDC SEEPZ', 'Handed over to Carting Agent', 'Exit from DGDC SEEPZ Gate', 'Entry at DGDC Cargo Gate', 'Handed over to DGDC Cargo')))", nativeQuery = true)
	   
	 List<Export> findByCompanyIdAndBranchIdAndSbDateAndDgdcStatus(@Param("date") String date, @Param("cid") String cid, @Param("bid") String bid, @Param("dgdcstatus") String dgdcstatus);
	
	@Query(value = "SELECT DISTINCT carting_agent FROM export WHERE company_id = :companyId AND branch_id = :branchId AND DATE(tp_date) = DATE(:ser_date)", nativeQuery = true)
	List<String> findByCompanyAndBranchAndSerDate(@Param("companyId") String companyId, @Param("branchId") String branchId, @Param("ser_date") String ser_date);

	@Query(value = "SELECT  * FROM export WHERE company_id = :companyId AND branch_id = :branchId AND DATE(tp_date) = DATE(:ser_date) AND carting_agent = :exId", nativeQuery = true)
	List<Export> findByCompanyAndBranchAndserDateAndexternalPId(@Param("companyId") String companyId,
			@Param("branchId") String branchId, @Param("ser_date") String ser_date, @Param("exId") String exId

	);
	
	@Query(value="select * from export where company_id=:cid and branch_id=:bid and sb_request_id=:sbid and sb_number=:sbno",nativeQuery=true)
	public Export findBySBNoandSbreqid(@Param("cid") String cid, @Param("bid") String bid,@Param("sbid") String sbid, @Param("sbno") String sbno);
	
	@Query(value="select * from export where company_id=:cid and branch_id=:bid and sb_request_id=:sbid and sb_number=:sbno",nativeQuery=true)
	public List<Export> findAllBySBNoandSbreqid(@Param("cid") String cid, @Param("bid") String bid,@Param("sbid") String sbid, @Param("sbno") String sbno);
	
	@Transactional
	@Modifying
	
	@Query(value="update export set nsdl_status=:nsdl,reason_for_override=:reason,override_document=:over where company_id=:cid and branch_id=:bid and sb_request_id=:sbid and sb_number =:sbno",nativeQuery=true)
	public void updateOverride(@Param("nsdl") String nsdl,@Param("reason") String reason, @Param("over") String over,@Param("cid") String cid, @Param("bid") String bid,@Param("sbid") String sbid, @Param("sbno") String sbno);

	@Transactional
	@Modifying
	@Query(value="update export set dgdc_status='Handed over to Carting Agent',carting_agent=:carting,representative_id=:representid where company_id=:cid and branch_id=:bid and sb_request_id=:sbid and sb_number =:sbno",nativeQuery=true)
    public void updateCartingRecord(@Param("carting") String carting,@Param("representid") String representid,@Param("cid") String cid, @Param("bid") String bid,@Param("sbid") String sbid, @Param("sbno") String sbno);


	@Query(value="SELECT * FROM export where company_id=:cid and branch_id=:bid and dgdc_status='Entry at DGDC Cargo GATE' and cancel_status='N' and carting_agent=:cartagent and representative_id=:representid and hold_status='N'",nativeQuery=true)
    public List<Export> getalldataforreceivecarting(@Param("cid") String cid, @Param("bid") String bid,@Param("cartagent") String cartagent, @Param("representid") String representid);

    @Query(value="select * from export where company_id=:cid and branch_id=:bid and airline_code=:air and dgdc_status='Handed over to DGDC Cargo' and hold_status='N'",nativeQuery=true)
    public List<Export> getdataByairline(@Param("cid") String cid, @Param("bid") String bid,@Param("air") String air);

//    @Query(value="select * from export where company_id=:cid and branch_id=:bid and dgdc_status='Handed over to DGDC SEEPZ' and (nsdl_status='Allow Export' OR nsdl_status='Let Export') and cancel_status='N' and hold_status='N' ",nativeQuery=true)
//    public List<Export> getalldataforhandover(@Param("cid") String cid, @Param("bid") String bid);

//    @Query(value="select * from export where company_id=:cid and branch_id=:bid and dgdc_status='Handed over to DGDC SEEPZ' and (nsdl_status='Allow Export' OR nsdl_status='Let Export') and cancel_status='N' and hold_status='N' and (airway_bill_no!='' or airway_bill_no!=null) ",nativeQuery=true)
//    public List<Export> getalldataforhandover(@Param("cid") String cid, @Param("bid") String bid);
//    
//    @Query(value="select * from export where company_id=:cid and branch_id=:bid and dgdc_status='Handed over to DGDC SEEPZ' and (nsdl_status='Allow Export' OR nsdl_status='Let Export') and cancel_status='N' and hold_status='N' and (airway_bill_no='' or airway_bill_no=null) ",nativeQuery=true)
//    public List<Export> getalldataforhandover1(@Param("cid") String cid, @Param("bid") String bid);



	@Query(value ="SELECT DISTINCT *  FROM Export " +
            "WHERE company_id = :companyId " +
            "AND branch_id = :branchId " +
            "AND ser_date BETWEEN :startDate AND :endDate " +
            "AND  tp_no  IS NULL " +
            "AND pctm_no  IS NULL ", nativeQuery = true)
     List<Export> findAllExportData( @Param("companyId") String companyId,
    	        @Param("branchId") String branchId,
    	        @Param("startDate") Date startDate,
    	        @Param("endDate") Date endDate );
 
                                  
   @Query(value = "UPDATE Export e " +
            "SET e.pctmNo = :newPCTMNo, e.tpNo = :newTPNo " +
            "WHERE e.companyId = :companyId " +
            "AND e.branchId = :branchId " +
            "AND e.tpNo IS NULL"+
            "AND e.pctmNo IS NULL", nativeQuery = true)
    String updatePCTMAndTPNo(
            @Param("newPCTMNo") String pctmNo,
            @Param("newTPNo") String tpNo,
            @Param("companyId") String companyId,
            @Param("branchId") String branchId  
    );
   
   @Query(value = "SELECT DISTINCT carting_agent " +
            "FROM Export " +
            "WHERE company_id = :companyId " +
            "AND branch_id = :branchId " +
            " AND DATE_FORMAT(ser_date, '%Y-%m-%d') =:serDate " +
            "AND pctm_No IS NOT NULL " +
            "AND tp_No IS NOT NULL " +
            "GROUP BY carting_agent", nativeQuery = true)
    List<String> findAllCartingAgentNames(@Param("companyId")String companyId,
    		@Param("branchId") String branchId, 
    		@Param("serDate")String serDate);
   
   
   
    @Query(value = "SELECT DISTINCT tp_No FROM Export WHERE company_id =:companyId AND branch_id =:branchId AND  DATE_FORMAT(ser_date, '%Y-%m-%d') =:serDate AND carting_agent=:cartingAgent", nativeQuery = true)
List<String> findDistinctTpNos(@Param("companyId") String companyId,
                              @Param("branchId") String branchId,
                             
                              @Param("serDate") String endDate,
                              @Param("cartingAgent") String cartingAgent);
    
    
    
    
//    @Query(value ="SELECT DISTINCT *  FROM Export  " +
//            "WHERE company_id = :companyId " +
//            "AND branch_id = :branchId " +
//            "AND DATE_FORMAT(tp_date, '%Y-%m-%d') =:serDate " +
//            "AND carting_agent = :cartingAgent " +
//            "AND tp_No = :tpNo", nativeQuery = true)
//     List<Export> findImportTPData(@Param("companyId") String companyId,
//    		 @Param("branchId") String branchId,
//    		  @Param("serDate") String endDate,
//    		  @Param("cartingAgent") String cartingAgent,
//    		  @Param ("tpNo")String tpNo);
    
    
    @Query(value ="SELECT DISTINCT *  FROM Export  " +
            "WHERE company_id = :companyId " +
            "AND branch_id = :branchId " +
            "AND DATE_FORMAT(tp_date, '%Y-%m-%d') =:serDate " +
            "AND carting_agent = :cartingAgent " +
            "AND tp_No = :tpNo", nativeQuery = true)
     List<Export> findImportTPData(@Param("companyId") String companyId,
    		 @Param("branchId") String branchId,
    		  @Param("serDate") String endDate,
    		  @Param("cartingAgent") String cartingAgent,
    		  @Param ("tpNo")String tpNo);
    
 

    @Query(value = "select * from Export i where i.company_id=:cid and i.branch_id=:bid and i.tp_date=:date and i.tp_no=:tpno", nativeQuery = true)
	public List<Export> findByTpdateTpno(
	    @Param("cid") String cid,
	    @Param("bid") String bid,
	    @Param("date") Date date,
	    @Param("tpno") String tp_no
	   // @Param("status") char status
	    ); 
	

	   @Query(value="select distinct i.tp_no from Export i where i.tp_date=:date and i.company_id=:cid and i.branch_id=:bid" , nativeQuery=true)
	    public List<String> findByTp(@Param("date") Date date,@Param("cid") String cid,@Param("bid") String bid);

	   @Query(value="select * from Export where company_id=:companyId and branch_id=:branchId",nativeQuery=true)
		public List<Export> findByAll(@Param("companyId") String companyId,@Param("branchId") String branchId);
	   
	   Export findByCompanyIdAndBranchIdAndSerDateAndNameOfExporterAndNoOfPackagesAndPcStatusAndScStatusAndHpStatus(String companyId, String branchId, Date sirDate,String importerId , int ImpoNop, String pcStatus, String scStatus,String hpStatus);

	   @Query(value = "SELECT DISTINCT * FROM Export WHERE company_id = :companyId AND branch_id = :branchId AND cancel_status = 'N' AND ser_date BETWEEN :startDate AND :endDate AND console_Agent = :cartingAgent", nativeQuery = true)
	   List<Export> findExportAllData(@Param("companyId") String companyId, @Param("branchId") String branchId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("cartingAgent") String cartingAgent);

	 
	   
	   
	   @Query(value = "SELECT DISTINCT * " +
	            "FROM Export " +
	            "WHERE company_id = :companyId " +
	            "AND branch_id = :branchId " + 
	            "AND cancel_status='N'" +
	            "AND ser_date BETWEEN :startDate AND :endDate ",
	            nativeQuery = true)
	     List<Export> findExportData(String companyId, String branchId, Date startDate, Date endDate
	                                    );
	 
	   @Query(value="select * from Export b where b.company_id=:cid AND b.branch_id = :bid",nativeQuery=true)
		public Export findByCartingAgnetWithCartingAgent(@Param("cid") String cid,@Param("bid") String bid);
	   
	   
	   @Query(value="select distinct i.ser_date from Export i where i.ser_date=:date and i.company_id=:cid and i.branch_id=:bid" , nativeQuery=true)
	    public List<String> findBySerDate(@Param("date") Date date,@Param("cid") String cid,@Param("bid") String bid);
	   
	   
//	   @Query(value = "SELECT * FROM Export e WHERE e.company_id = :cid AND e.branch_id = :bid AND e.ser_date = :date AND e.airline_code = :airlineCode AND e.dgdc_status = 'Handed Over to Airline' and hold_status='N'", nativeQuery = true)
//	   public List<Export> findBySerDateAndAirlineCode(
//	       @Param("cid") String cid,
//	       @Param("bid") String bid,
//	       @Param("date") Date date,
//	       @Param("airlineCode") String airlineCode
//	   );
	   
	   
	   @Query(value = "SELECT * FROM Export e WHERE e.company_id = :cid AND e.branch_id = :bid AND e.airline_Date = :date AND e.airline_code = :airlineCode AND e.dgdc_status = 'Handed Over to Airline' and hold_status='N'", nativeQuery = true)
	   public List<Export> findBySerDateAndAirlineCode(
	       @Param("cid") String cid,
	       @Param("bid") String bid,
	       @Param("date") Date date,
	       @Param("airlineCode") String airlineCode
	   );
	   
	   @Query(value = "SELECT * FROM Export  WHERE Company_Id = :cid AND Branch_Id = :bid AND SER_No = :serNo AND PC_Status = :pcStatus AND (DGDC_Status = :status1 OR DGDC_Status = :status2) AND Gate_Pass_Status != :status3", nativeQuery = true)
		public List<Export> findByCompanyIdAndBranchIdAndSerNoAndPcStatusAndDgdcStatusAndGatePassStatusNot(
		        @Param("cid") String cid,
		        @Param("bid") String bid,
		        @Param("serNo") String serNo,
		        @Param("pcStatus") String pcStatus,
		        @Param("status1") String status1,
		        @Param("status2") String status2,
		        @Param("status3") String status3
		);

		Export findByCompanyIdAndBranchIdAndSerNo(String companyId, String branchId, String serNo);
		
		//shubham
		   
		@Query(value = "SELECT COALESCE(SUM(no_of_packages), 0) FROM Export WHERE Company_Id = :cid AND Branch_Id = :bid AND DGDC_Status = :string AND SER_Date < :date1", nativeQuery = true)
		Integer findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDateNot(@Param("cid") String cid, @Param("bid") String bid, @Param("string") String string, @Param("date1") Date date1);

//			@Query(value = "SELECT SUM(no_of_packages) FROM Export WHERE Company_Id = :cid AND Branch_Id = :bid AND DGDC_Status = :string AND SER_Date =  :date1", nativeQuery = true)
//			int findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDate1(@Param("cid") String cid, @Param("bid") String bid, @Param("string") String string, @Param("date1") Date date1);

			 @Query(value = "SELECT COALESCE(SUM(no_of_packages), 0) FROM Export WHERE Company_Id = :cid AND Branch_Id = :bid AND SER_Date = :date1", nativeQuery = true)
			    Integer findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDate1(@Param("cid") String cid, @Param("bid") String bid, @Param("date1") Date date1);
			
			 
			 @Query(value = "SELECT COALESCE(SUM(no_of_packages), 0) FROM Export WHERE Company_Id = :cid AND Branch_Id = :bid AND DGDC_Status = :string AND SER_Date = :date1", nativeQuery = true)
			    Integer findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDate3(@Param("cid") String cid, @Param("bid") String bid, @Param("string") String string, @Param("date1") Date date1);

			 
			 
//			 @Query(value = "SELECT SUM(no_of_packages) FROM Export WHERE Company_Id = :cid AND Branch_Id = :bid AND YEAR(SER_Date) = YEAR(CURRENT_DATE()) GROUP BY YEAR(SER_Date), MONTH(SER_Date)", nativeQuery = true)
//			 List<Object[]> findSumByCompanyIdAndBranchIdMonthWise(@Param("cid") String cid, @Param("bid") String bid);

//			 @Query(value = "SELECT MONTH(Export_date) AS month, COALESCE(SUM(no_of_packages), 0) AS totalPackages FROM Export WHERE Company_Id = :cid AND Branch_Id = :bid AND YEAR(Export_date) = YEAR(CURRENT_DATE()) GROUP BY MONTH(Export_date)", nativeQuery = true)
//			 List<Object[]> findSumByCompanyIdAndBranchIdMonthWise(@Param("cid") String cid, @Param("bid") String bid);
		 
//			 @Query(value = "SELECT " +
//			            "SUM(I.nop) AS ImportTotalNop, " +
//			            "SUM(E.No_Of_Packages) AS ExportTotalNopPackages, " +
//			            "DATE_FORMAT(I.SIR_Date, '%Y-%m') AS MonthYear " +
//			            "FROM import I " +
//			            "LEFT JOIN export E ON DATE_FORMAT(I.SIR_Date, '%Y-%m') = DATE_FORMAT(E.SER_Date, '%Y-%m') " +
//			            "WHERE I.Status <> 'D' AND E.Status <> 'D' " +
//			            "AND I.company_Id = :companyId AND I.branch_Id = :branchId " +
//			            "AND E.company_Id = :companyId AND E.branch_Id = :branchId " +
//			            "GROUP BY DATE_FORMAT(I.SIR_Date, '%Y-%m')", nativeQuery = true)
//			    List<Object[]> getImportExportDataByCompanyAndBranch(@Param("companyId") String companyId, @Param("branchId") String branchId);
			 
			    
			    
//			    @Query(value = "SELECT " +
//					 	"    SUM(TotalPackages) AS TotalPackages, " +
//					 	"    SUM(TotalNop) AS TotalNop, " +			            
//			            "    MonthYear " +
//			            "FROM ( " +
//			            "    SELECT " +
//			            "        DATE_FORMAT(i.SIR_Date, '%Y-%m') AS MonthYear, " +
//			            "        SUM(i.nop) AS TotalNop, " +
//			            "        NULL AS TotalPackages " +
//			            "    FROM Import i " +
//			            "    WHERE i.status <> 'D' " +
//			            "        AND i.company_Id = :companyId AND i.branch_Id = :branchId " +
//			            "    GROUP BY DATE_FORMAT(i.SIR_Date, '%Y-%m') " +
//			            "    UNION ALL " +
//			            "    SELECT " +
//			            "        DATE_FORMAT(e.SER_Date, '%Y-%m') AS MonthYear, " +
//			            "        NULL AS TotalNop, " +
//			            "        SUM(e.No_Of_Packages) AS TotalPackages " +
//			            "    FROM Export e " +
//			            "    WHERE e.status <> 'D' AND e.dgdc_status != 'Entry at DGDC SEEPZ Gate' " +
//			            "        AND e.company_Id = :companyId AND e.branch_Id = :branchId " +
//			            "    GROUP BY DATE_FORMAT(e.SER_Date, '%Y-%m') " +
//			            ") AS CombinedData " +
//			            "GROUP BY MonthYear", nativeQuery = true)
//			    List<Object[]> getImportExportDataByCompanyAndBranch(
//			            @Param("companyId") String companyId,
//			            @Param("branchId") String branchId
//			    );
			 
			 
			 @Query(nativeQuery = true, value =
				        "SELECT " +
				        "    COALESCE(SUM(TotalPackages), 0) AS TotalPackages, " +
				        "    COALESCE(SUM(TotalNop), 0) AS TotalNop, " +
				        "    DATE_FORMAT(Date, '%Y-%m') AS MonthYear " +
				        "FROM ( " +
				        "    SELECT i.SIR_Date AS Date, SUM(i.nop) AS TotalNop, NULL AS TotalPackages " +
				        "    FROM Import i " +
				        "    WHERE i.status <> 'D' " +
				        "        AND i.company_Id = :companyId " +
				        "        AND i.branch_Id = :branchId " +
				        "    GROUP BY i.SIR_Date " +
				        "    UNION ALL " +
				        "    SELECT e.SER_Date AS Date, NULL AS TotalNop, SUM(e.No_Of_Packages) AS TotalPackages " +
				        "    FROM Export e " +
				        "    WHERE e.status <> 'D' " +
				        "        AND e.dgdc_status != 'Entry at DGDC SEEPZ Gate' " +
				        "        AND e.company_Id = :companyId " +
				        "        AND e.branch_Id = :branchId " +
				        "    GROUP BY e.SER_Date " +
				        "    UNION ALL " +
				        "    SELECT isub.SIR_Date AS Date, SUM(isub.nop) AS TotalNop, NULL AS TotalPackages " +
				        "    FROM ImportSub isub " +
				        "    WHERE isub.status <> 'D' " +
				        "        AND isub.company_Id = :companyId " +
				        "        AND isub.branch_Id = :branchId " +
				        "    GROUP BY isub.SIR_Date " +
				        "    UNION ALL " +
				        "    SELECT esub.SER_Date AS Date, NULL AS TotalNop, SUM(esub.nop) AS TotalPackages " +
				        "    FROM ExportSub esub " +
				        "    WHERE esub.status <> 'D' " +
				        "        AND esub.company_Id = :companyId " +
				        "        AND esub.branch_Id = :branchId " +
				        "    GROUP BY esub.SER_Date " +
				        ") AS CombinedData " +
				        "GROUP BY DATE_FORMAT(Date, '%Y-%m') " +
				        "ORDER BY MonthYear")
				List<Object[]> getImportExportDataByCompanyAndBranch(@Param("companyId") String companyId, @Param("branchId") String branchId);

			 
			 
			    
			    
			    
			    
			    
			    
			    
//			    @Query(value = "SELECT " +
//			            "SUM(c.total_invoice_amt) AS TotalAmount, " +
//			            "DATE_FORMAT(c.invoice_date, '%Y-%m') AS MonthYear " +
//			            "FROM cfinvsrv c " +
//			            "WHERE c.company_id = :companyId AND c.branch_id = :branchId " +
//			            "GROUP BY DATE_FORMAT(c.invoice_date, '%Y-%m')", nativeQuery = true)
//			    List<Object[]> getTotalInvoiceAmountByMonth(@Param("companyId") String companyId, @Param("branchId") String branchId);
//			  
			   
//				 @Query(value = "SELECT " +
//				            "SUM(c.total_invoice_amt) AS TotalAmount, " +
//				            "DATE_FORMAT(c.invoice_date, '%Y-%m') AS MonthYear " +
//				            "FROM Predictable_cfinvsrv c " +
//				            "WHERE c.company_id = :companyId AND c.branch_id = :branchId " +
//				            "GROUP BY DATE_FORMAT(c.invoice_date, '%Y-%m')", nativeQuery = true)
//				    List<Object[]> getTotalInvoiceAmountByMonth(@Param("companyId") String companyId, @Param("branchId") String branchId);
			    
				@Query(value = "SELECT " +
				        "SUM(c.bill_amt) AS TotalAmount, " +
				        "DATE_FORMAT(c.invoice_date, '%Y-%m') AS MonthYear " +
				        "FROM Predictable_cfinvsrv c " +
				        "WHERE c.company_id = :companyId AND c.branch_id = :branchId " +
				        "GROUP BY DATE_FORMAT(c.invoice_date, '%Y-%m') " +
				        "ORDER BY MAX(c.invoice_date) DESC " +
				        "LIMIT 3 ",nativeQuery = true)
				  List<Object[]> getTotalInvoiceAmountByMonth(@Param("companyId") String companyId, @Param("branchId") String branchId);



			    
			    
			    List<Export> findByCompanyIdAndBranchIdAndNameOfExporterAndSerDateAndStatusNot(String companyId, String branchId,String partyId, Date serDate , String Status);
			    
			    
	@Query(value="select distinct tp_no from export where company_id=:cid and branch_id=:bid and and tp_no != 'NULL' and tp_date=:tpdate",nativeQuery=true)
      public List<String> findtpbytpdata(@Param("cid") String cid, @Param("bid") String bid,@Param("tpdate") @DateTimeFormat(pattern="yyyy-MM-dd") Date tpdate);
	   
	
	  @Query(value="select distinct tp_no from export where company_id=:cid and branch_id=:bid and tp_date=:tdate order by tp_no desc",nativeQuery=true)
	    List<String> getalltp(@Param("cid") String cid, @Param("bid") String bid ,@Param("tdate") Date date);
	  List<Export> findByCompanyIdAndBranchIdAndSerNoAndPcStatus(String cid, String bid, String serNo, String string);
	    
	  @Query(value="select * from export where company_id=:cid and branch_id=:bid and exporter_name=:exporter and status != 'D'",nativeQuery=true)
		List<Export> findAllDataforparty(@Param("cid") String cid, @Param("bid") String bid, @Param("exporter") String exporter);
		
		@Query(value="select * from export where company_id=:cid and branch_id=:bid and carting_agent=:carting and status != 'D'",nativeQuery=true)
		List<Export> findAllDataforcartingagent(@Param("cid") String cid, @Param("bid") String bid, @Param("carting") String carting);
		
		
		@Query(value="select * from export where company_id=:cid and branch_id=:bid and chano=:carting and status != 'D'",nativeQuery=true)
		List<Export> findAllDataforcha(@Param("cid") String cid, @Param("bid") String bid, @Param("carting") String carting);
		
		
		@Query(value="select * from export where company_id=:cid and branch_id=:bid and console_agent=:carting and status != 'D'",nativeQuery=true)
		List<Export> findAllDataforconsole(@Param("cid") String cid, @Param("bid") String bid, @Param("carting") String carting);
		
		@Query("SELECT e FROM Export e WHERE e.companyId = :companyId AND e.branchId = :branchId AND e.serNo = :serNo AND e.dgdcStatus = 'Handed over to DGDC SEEPZ' AND e.status <> 'D' AND (e.mopStatus <> 'Y' OR e.mopStatus IS NULL OR e.mopStatus = '')")
		Export findByCompanyIdAndBranchIdAndSerNoAndDgdcStatusAndStatusNotAndMopStatusNot(@Param("companyId") String companyId, @Param("branchId") String branchId, @Param("serNo") String serNo);
		 Export findBySerNo(String serNo);
		 
		 
		 @Query("SELECT i FROM Export i " +
			        "LEFT JOIN Party p ON i.nameOfExporter = p.partyId " +
			        "WHERE " +
			        "(:pcStatus IS NULL OR :pcStatus = '' OR i.pcStatus = :pcStatus) " +
			        "AND (:scStatus IS NULL OR :scStatus = '' OR i.scStatus = :scStatus) " +
			        "AND (:hpStatus IS NULL OR :hpStatus = '' OR i.hpStatus = :hpStatus) " +
			        "AND (:holdStatus IS NULL OR :holdStatus = '' OR i.holdStatus = :holdStatus) " +
			        "AND (:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
			        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
			        "(:startDate IS NULL AND :endDate >= " +
			        "CASE WHEN i.dgdcStatus = 'Entry at DGDC SEEPZ Gate' THEN i.createdDate ELSE i.serDate END) OR " +
			        "(:startDate <= " +
			        "CASE WHEN i.dgdcStatus = 'Entry at DGDC SEEPZ Gate' THEN i.createdDate ELSE i.serDate END " +
			        "AND :endDate IS NULL) OR " +
			        "(:startDate <= " +
			        "CASE WHEN i.dgdcStatus = 'Entry at DGDC SEEPZ Gate' THEN i.createdDate ELSE i.serDate END " +
			        "AND :endDate >= " +
			        "CASE WHEN i.dgdcStatus = 'Entry at DGDC SEEPZ Gate' THEN i.createdDate ELSE i.serDate END) OR " +
			        "(:startDate <= " +
			        "CASE WHEN i.dgdcStatus = 'Back To Town' AND i.serDate IS NOT NULL THEN i.serDate ELSE i.createdDate END " +
			        "AND :endDate IS NULL) OR " +
			        "(:startDate <= " +
			        "CASE WHEN i.dgdcStatus = 'Back To Town' AND i.serDate IS NOT NULL THEN i.serDate ELSE i.createdDate END " +
			        "AND :endDate >= " +
			        "CASE WHEN i.dgdcStatus = 'Back To Town' AND i.serDate IS NOT NULL THEN i.serDate ELSE i.createdDate END)) " +
			        "AND i.companyId = :companyId " +
			        "AND i.branchId = :branchId " +
			        "AND i.status != 'D' " +
			        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
			        "(i.serNo like CONCAT('%', :searchValue, '%')  OR i.sbRequestId like CONCAT('%', :searchValue, '%')  OR i.sbNo like CONCAT('%', :searchValue, '%')  OR i.nameOfExporter IN " +
			        "(SELECT party.partyId FROM Party party WHERE party.partyName like CONCAT('%', :searchValue, '%') ))) " +
			        "ORDER BY i.serNo DESC")
			List<Export> findByAttributes2(
			    @Param("companyId") String companyId,
			    @Param("branchId") String branchId,
			    @Param("pcStatus") String pcStatus,
			    @Param("scStatus") String scStatus,
			    @Param("hpStatus") String hpStatus,
			    @Param("holdStatus") String holdStatus,
			    @Param("dgdcStatus") String dgdcStatus,
			    @Param("startDate") Date startDate,
			    @Param("endDate") Date endDate,
			    @Param("searchValue") String searchValue);
		 
		 @Query(value="SELECT * FROM export where ser_date='2023-12-07' and hold_status='N'",nativeQuery=true)
		 List<Export> updatedta();
		 
		 
		 
		 @Query(value="select * from export where company_id=:cid and branch_id=:bid and pc_gatepass_date=:date",nativeQuery=true)
		 List<Export> searchdatabypcgatepass(@Param("cid") String cid,@Param("bid") String bid,@Param("date") Date date);

		 @Query(value="select * from export where pc_gatepass_id=:cid",nativeQuery=true)
		 List<Export> getbygatepassid(@Param("cid") String cid);
		 
		 @Query(value="select * from export where mop_id=:cid",nativeQuery=true)
		 List<Export> getbymoppassid(@Param("cid") String cid);
		 
		 
		 
		 @Query(value="select i from ImportSub i where i.companyId=:cid and i.branchId=:bid and i.sirNo=:subid and i.requestId=:reqid")
			public ImportSub findImportSubBysir(@Param("cid") String cid,@Param("bid") String bid,@Param("subid") String subid,@Param("reqid") String reqid);
		 
		 
		 @Query("SELECT i.sbRequestId,i.sbNo,i.serNo,i.serDate,i.nameOfExporter,i.noOfPackages,i.grossWeight,i.nsdlStatus,i.dgdcStatus,i.mopStatus,i.holdStatus,i.pcStatus,i.scStatus,i.hpStatus,i.sbDate,i.airlineCode FROM Export i " +
			        "LEFT JOIN Party p ON i.nameOfExporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +
			        "WHERE " +
			        "(:pcStatus IS NULL OR :pcStatus = '' OR i.pcStatus = :pcStatus) " +
			        "AND (:scStatus IS NULL OR :scStatus = '' OR i.scStatus = :scStatus) " +
			        "AND (:hpStatus IS NULL OR :hpStatus = '' OR i.hpStatus = :hpStatus) " +
			        "AND (:holdStatus IS NULL OR :holdStatus = '' OR i.holdStatus = :holdStatus) " +
			        "AND (:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
			        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
			        "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
			        "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
			        "(:startDate <= i.serDate AND :endDate >= i.serDate) OR " +
			        "(:startDate IS NULL AND :endDate >= i.createdDate) OR " +
			        "(:startDate <= i.createdDate AND :endDate IS NULL) OR " +
			        "(:startDate <= i.createdDate AND :endDate >= i.createdDate)) " +
			        "AND i.companyId = :companyId " +
			        "AND i.branchId = :branchId " +
			        "AND i.status != 'D' " +
			        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
			        "(i.serNo like CONCAT('%', :searchValue, '%')  OR i.sbRequestId like CONCAT('%', :searchValue, '%')  OR i.sbNo like CONCAT('%', :searchValue, '%')  OR i.nameOfExporter IN " +
			        "(SELECT party.partyId FROM Party party WHERE party.partyName like CONCAT('%', :searchValue, '%') ))) " +
			        "ORDER BY i.serNo DESC")
			List<Object[]> findByAttributes4(
			    @Param("companyId") String companyId,
			    @Param("branchId") String branchId,
			    @Param("pcStatus") String pcStatus,
			    @Param("scStatus") String scStatus,
			    @Param("hpStatus") String hpStatus,
			    @Param("holdStatus") String holdStatus,
			    @Param("dgdcStatus") String dgdcStatus,
			    @Param("startDate") Date startDate,
			    @Param("endDate") Date endDate,
			    @Param("searchValue") String searchValue);
		 
		 
			@Query(value="select * from export where company_id=:cid and branch_id=:bid and dgdc_status='Handed over to DGDC SEEPZ' and (nsdl_status='Allow Export' OR nsdl_status='Let Export') and cancel_status='N' and hold_status='N' and (airway_bill_no!='' or airway_bill_no is not null) ",nativeQuery=true)
		    public List<Export> getalldataforhandover(@Param("cid") String cid, @Param("bid") String bid);
		    
		    @Query(value="select * from export where company_id=:cid and branch_id=:bid and dgdc_status='Handed over to DGDC SEEPZ' and (nsdl_status='Allow Export' OR nsdl_status='Let Export') and cancel_status='N' and hold_status='N' and (airway_bill_no='' or airway_bill_no is null) ",nativeQuery=true)
		    public List<Export> getalldataforhandover1(@Param("cid") String cid, @Param("bid") String bid);
		 
		    
		    @Query("SELECT i.sbRequestId,i.sbNo,i.serNo,i.serDate,i.nameOfExporter,i.noOfPackages,i.grossWeight,i.nsdlStatus,i.dgdcStatus,i.mopStatus,i.holdStatus,i.pcStatus,i.scStatus,i.hpStatus,i.sbDate FROM Export i " +
			        "LEFT JOIN Party p ON i.nameOfExporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +
			        "WHERE " +
			        "(:pcStatus IS NULL OR :pcStatus = '' OR i.pcStatus = :pcStatus) " +
			        "AND (:scStatus IS NULL OR :scStatus = '' OR i.scStatus = :scStatus) " +
			        "AND (:hpStatus IS NULL OR :hpStatus = '' OR i.hpStatus = :hpStatus) " +
			        "AND (:holdStatus IS NULL OR :holdStatus = '' OR i.holdStatus = :holdStatus) " +
			        "AND (:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
			        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
			        "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
			        "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
			        "(:startDate <= i.serDate AND :endDate >= i.serDate) OR " +
			        "(:startDate IS NULL AND :endDate >= i.createdDate) OR " +
			        "(:startDate <= i.createdDate AND :endDate IS NULL) OR " +
			        "(:startDate <= i.createdDate AND :endDate >= i.createdDate)) " +
			        "AND i.companyId = :companyId " +
			        "AND i.branchId = :branchId " +
			        "AND i.nameOfExporter = :exporter " +
			        "AND i.status != 'D' " +
			        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
			        "(i.serNo like CONCAT('%', :searchValue, '%')  OR i.sbRequestId like CONCAT('%', :searchValue, '%')  OR i.sbNo like CONCAT('%', :searchValue, '%')  OR i.nameOfExporter IN " +
			        "(SELECT party.partyId FROM Party party WHERE party.partyName like CONCAT('%', :searchValue, '%') ))) " +
			        "ORDER BY i.serNo DESC")
			List<Object[]> findByPartyAttributes(
			    @Param("companyId") String companyId,
			    @Param("branchId") String branchId,
			    @Param("exporter") String exporter,
			    @Param("pcStatus") String pcStatus,
			    @Param("scStatus") String scStatus,
			    @Param("hpStatus") String hpStatus,
			    @Param("holdStatus") String holdStatus,
			    @Param("dgdcStatus") String dgdcStatus,
			    @Param("startDate") Date startDate,
			    @Param("endDate") Date endDate,
			    @Param("searchValue") String searchValue);
			
			
			@Query("SELECT i.sbRequestId,i.sbNo,i.serNo,i.serDate,i.nameOfExporter,i.noOfPackages,i.grossWeight,i.nsdlStatus,i.dgdcStatus,i.mopStatus,i.holdStatus,i.pcStatus,i.scStatus,i.hpStatus,i.sbDate FROM Export i " +
			        "LEFT JOIN Party p ON i.nameOfExporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +
			        "WHERE " +
			        "(:pcStatus IS NULL OR :pcStatus = '' OR i.pcStatus = :pcStatus) " +
			        "AND (:scStatus IS NULL OR :scStatus = '' OR i.scStatus = :scStatus) " +
			        "AND (:hpStatus IS NULL OR :hpStatus = '' OR i.hpStatus = :hpStatus) " +
			        "AND (:holdStatus IS NULL OR :holdStatus = '' OR i.holdStatus = :holdStatus) " +
			        "AND (:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
			        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
			        "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
			        "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
			        "(:startDate <= i.serDate AND :endDate >= i.serDate) OR " +
			        "(:startDate IS NULL AND :endDate >= i.createdDate) OR " +
			        "(:startDate <= i.createdDate AND :endDate IS NULL) OR " +
			        "(:startDate <= i.createdDate AND :endDate >= i.createdDate)) " +
			        "AND i.companyId = :companyId " +
			        "AND i.branchId = :branchId " +
			        "AND i.cartingAgent = :carting " +
			        "AND i.status != 'D' " +
			        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
			        "(i.serNo like CONCAT('%', :searchValue, '%')  OR i.sbRequestId like CONCAT('%', :searchValue, '%')  OR i.sbNo like CONCAT('%', :searchValue, '%')  OR i.nameOfExporter IN " +
			        "(SELECT party.partyId FROM Party party WHERE party.partyName like CONCAT('%', :searchValue, '%') ))) " +
			        "ORDER BY i.serNo DESC")
			List<Object[]> findByCartingAttributes(
			    @Param("companyId") String companyId,
			    @Param("branchId") String branchId,
			    @Param("carting") String carting,
			    @Param("pcStatus") String pcStatus,
			    @Param("scStatus") String scStatus,
			    @Param("hpStatus") String hpStatus,
			    @Param("holdStatus") String holdStatus,
			    @Param("dgdcStatus") String dgdcStatus,
			    @Param("startDate") Date startDate,
			    @Param("endDate") Date endDate,
			    @Param("searchValue") String searchValue);
			
			
			@Query("SELECT i.sbRequestId,i.sbNo,i.serNo,i.serDate,i.nameOfExporter,i.noOfPackages,i.grossWeight,i.nsdlStatus,i.dgdcStatus,i.mopStatus,i.holdStatus,i.pcStatus,i.scStatus,i.hpStatus,i.sbDate FROM Export i " +
			        "LEFT JOIN Party p ON i.nameOfExporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +
			        "WHERE " +
			        "(:pcStatus IS NULL OR :pcStatus = '' OR i.pcStatus = :pcStatus) " +
			        "AND (:scStatus IS NULL OR :scStatus = '' OR i.scStatus = :scStatus) " +
			        "AND (:hpStatus IS NULL OR :hpStatus = '' OR i.hpStatus = :hpStatus) " +
			        "AND (:holdStatus IS NULL OR :holdStatus = '' OR i.holdStatus = :holdStatus) " +
			        "AND (:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
			        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
			        "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
			        "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
			        "(:startDate <= i.serDate AND :endDate >= i.serDate) OR " +
			        "(:startDate IS NULL AND :endDate >= i.createdDate) OR " +
			        "(:startDate <= i.createdDate AND :endDate IS NULL) OR " +
			        "(:startDate <= i.createdDate AND :endDate >= i.createdDate)) " +
			        "AND i.companyId = :companyId " +
			        "AND i.branchId = :branchId " +
			        "AND i.chaNo = :cha " +
			        "AND i.status != 'D' " +
			        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
			        "(i.serNo like CONCAT('%', :searchValue, '%')  OR i.sbRequestId like CONCAT('%', :searchValue, '%')  OR i.sbNo like CONCAT('%', :searchValue, '%')  OR i.nameOfExporter IN " +
			        "(SELECT party.partyId FROM Party party WHERE party.partyName like CONCAT('%', :searchValue, '%') ))) " +
			        "ORDER BY i.serNo DESC")
			List<Object[]> findByCHAAttributes(
			    @Param("companyId") String companyId,
			    @Param("branchId") String branchId,
			    @Param("cha") String cha,
			    @Param("pcStatus") String pcStatus,
			    @Param("scStatus") String scStatus,
			    @Param("hpStatus") String hpStatus,
			    @Param("holdStatus") String holdStatus,
			    @Param("dgdcStatus") String dgdcStatus,
			    @Param("startDate") Date startDate,
			    @Param("endDate") Date endDate,
			    @Param("searchValue") String searchValue);
			
			
			@Query("SELECT i.sbRequestId,i.sbNo,i.serNo,i.serDate,i.nameOfExporter,i.noOfPackages,i.grossWeight,i.nsdlStatus,i.dgdcStatus,i.mopStatus,i.holdStatus,i.pcStatus,i.scStatus,i.hpStatus,i.sbDate FROM Export i " +
			        "LEFT JOIN Party p ON i.nameOfExporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +
			        "WHERE " +
			        "(:pcStatus IS NULL OR :pcStatus = '' OR i.pcStatus = :pcStatus) " +
			        "AND (:scStatus IS NULL OR :scStatus = '' OR i.scStatus = :scStatus) " +
			        "AND (:hpStatus IS NULL OR :hpStatus = '' OR i.hpStatus = :hpStatus) " +
			        "AND (:holdStatus IS NULL OR :holdStatus = '' OR i.holdStatus = :holdStatus) " +
			        "AND (:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
			        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
			        "(:startDate IS NULL AND :endDate >= i.serDate) OR " +
			        "(:startDate <= i.serDate AND :endDate IS NULL) OR " +
			        "(:startDate <= i.serDate AND :endDate >= i.serDate) OR " +
			        "(:startDate IS NULL AND :endDate >= i.createdDate) OR " +
			        "(:startDate <= i.createdDate AND :endDate IS NULL) OR " +
			        "(:startDate <= i.createdDate AND :endDate >= i.createdDate)) " +
			        "AND i.companyId = :companyId " +
			        "AND i.branchId = :branchId " +
			        "AND i.consoleAgent = :console " +
			        "AND i.status != 'D' " +
			        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
			        "(i.serNo like CONCAT('%', :searchValue, '%')  OR i.sbRequestId like CONCAT('%', :searchValue, '%')  OR i.sbNo like CONCAT('%', :searchValue, '%')  OR i.nameOfExporter IN " +
			        "(SELECT party.partyId FROM Party party WHERE party.partyName like CONCAT('%', :searchValue, '%') ))) " +
			        "ORDER BY i.serNo DESC")
			List<Object[]> findByConsoleAttributes(
			    @Param("companyId") String companyId,
			    @Param("branchId") String branchId,
			    @Param("console") String console,
			    @Param("pcStatus") String pcStatus,
			    @Param("scStatus") String scStatus,
			    @Param("hpStatus") String hpStatus,
			    @Param("holdStatus") String holdStatus,
			    @Param("dgdcStatus") String dgdcStatus,
			    @Param("startDate") Date startDate,
			    @Param("endDate") Date endDate,
			    @Param("searchValue") String searchValue);
		    
		    
			
			
			
			
			
			@Query(value = "SELECT i.ser_No,i.sb_number ,i.ser_date, p.party_name,i.chano,i.description_of_goods ,i.no_of_packages, i.gross_weight, i.fobvalue_ininr, i.airway_bill_no, i.port_of_destination,e.short_form,i.uomgross_weight " +
			        "FROM export i " +
			        "LEFT JOIN party p ON i.exporter_name = p.party_id AND i.company_id = p.company_id AND i.branch_id = p.branch_id " +
			        "LEFT JOIN external_party e ON i.console_Agent = e.external_user_id AND i.company_id = e.company_id AND i.branch_id = e.branch_id " +
			        "WHERE i.company_id = :companyId " +
			        "AND i.branch_id = :branchId " +
			        "AND i.cancel_status = 'N' " +
			        "AND i.status <> 'D' " +
			        "AND i.ser_date BETWEEN :startDate AND :endDate " +
			        "AND (:consoleName IS NULL OR :consoleName = '' OR i.console_Agent = :consoleName)",
			        nativeQuery = true)
			List<Object[]> findExportDataRegister(String companyId, String branchId, Date startDate, Date endDate, String consoleName);

			    
			   
			@Query(value = "SELECT i.ser_No, '', i.ser_date, p.party_name,'' ,i.remarks, i.nop, i.gw_Weight , '', '', '', '' ,i.gw_weight_unit " +
			        "FROM exportsub i " +
			        "LEFT JOIN party p ON i.exporter = p.party_id AND i.company_id = p.company_id AND i.branch_id = p.branch_id " +
			        "WHERE i.company_id = :companyId " +
			        "AND i.branch_id = :branchId " +
			        "AND i.status <> 'D' " +
			        "AND i.ser_date BETWEEN :startDate AND :endDate",
			        nativeQuery = true)
			List<Object[]> findExportSubData(String companyId, String branchId, Date startDate, Date endDate);

			
			 @Query(value =
			            "SELECT " +	                   
			                    "    SUM(distinctSbCount) AS totalSBNO, " +	                   
			                    "    SUM(totalNop) AS totalNopSum, " +
			                    "    SUM(totalFobValue) AS totalFabValue " +
			                    "FROM ( " +
			                    "    SELECT " +
			                    "        COUNT(DISTINCT i.sb_number) AS distinctSbCount, " +
			                    "        SUM(COALESCE(i.no_of_packages, 0)) AS totalNop, " +
			                    "        SUM(COALESCE(i.fobvalue_ininr, 0)) AS totalFobValue " +
			                    "    FROM " +
			                    "        Export i " +
			                    "    WHERE " +
			                    "        i.company_id = :companyId " +
			                    "        AND i.branch_id = :branchId " +
			                    "        AND i.cancel_status <> 'Y' " +
			                    "        AND i.status <> 'D' " +
			                    "AND (:consoleName IS NULL OR :consoleName = '' OR i.console_Agent = :consoleName) "+
			                    "        AND i.ser_date BETWEEN :startDate AND :endDate " +
			                    "    UNION ALL " +
			                    "    SELECT " +
			                    "        0 AS distinctSbCount, " +
			                    "        SUM(COALESCE(sub.nop, 0)) AS totalNop, " +
			                    "        0 AS totalFobValue " +
			                    "    FROM " +
			                    "        ExportSub sub " +
			                    "    WHERE " +
			                    "        sub.company_id = :companyId " +
			                    "        AND sub.branch_id = :branchId " +
			                    "        AND sub.status <> 'D' " +
			                    "        AND sub.ser_date BETWEEN :startDate AND :endDate " +
			                    ") AS combinedResults", nativeQuery = true)
			    Object[] getTotalNopAndSbCountAndFobValue(
			            @Param("companyId") String companyId,
			            @Param("branchId") String branchId,
			            @Param("startDate") Date startDate,
			            @Param("endDate") Date endDate,
			            @Param("consoleName") String consoleName
			    );
				
			
			
			
			
			
			
		    
}

