package com.cwms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.Export;
import com.cwms.entities.ExportSHB;

import jakarta.transaction.Transactional;

public interface ExportShbRepo extends JpaRepository<ExportSHB, String> {
	
	
//	Update Custom TP Number 
	@Transactional
	 @Modifying
	 @Query(value = "UPDATE ExportSHB e " +
	            "SET e.billCalculated = 'Y' " +
	            "WHERE e.companyId = :companyId " +
	            "AND e.branchId = :branchId " +
	            "AND e.erNo = :sirNo " +
	            "AND e.airwayBillNo = :mawb " +	
	            "AND e.hawb = :hawb " +	
	            "AND e.status <>'D'")
	    int updateBillgenerated(	           
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("sirNo") String sirNo,
	            @Param("mawb") String mawb,
	            @Param("hawb") String hawb
	           
	    );
	
	
	@Query(value = "SELECT NEW com.cwms.entities.ExportSHB(i.companyId, i.branchId,i.sbNo,e.partyName,i.erNo,i.erDate,i.grossWeight,i.fobValueInINR,i.noOfPackages,i.outDate) " + "FROM ExportSHB i " + "LEFT JOIN Party e ON i.nameOfExporter = e.partyId And i.companyId=e.companyId And i.branchId=e.branchId " +
			"WHERE i.companyId = :companyId " + "AND i.branchId = :branchId "
			+ "AND i.status <> 'D' " + "AND i.nameOfExporter =:importerId " + "AND i.billCalculated = 'N' AND i.outDate > :lastinvoicedate  AND i.dgdcStatus In ('Handed over to Console','Exit from DGDC SHB Gate')")
	List<ExportSHB> findExportAllDataBill(@Param("companyId") String companyId, @Param("branchId") String branchId, @Param("importerId") String importerId,@Param("lastinvoicedate")Date lastinvoicedate);


	@Query(value="select e from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.sbNo=:sb")
	public ExportSHB getDataBySBNo(@Param("cid") String cid,@Param("bid") String bid,@Param("sb") String sb);
	
	@Query(value="select e from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.erNo=:sb")
	public ExportSHB getDataByErNo(@Param("cid") String cid,@Param("bid") String bid,@Param("sb") String sb);
	
	@Query(value="select e from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.sbNo=:sb and e.erNo=:er")
	public ExportSHB getDataBySBNoAndERNo(@Param("cid") String cid,@Param("bid") String bid,@Param("sb") String sb,@Param("er") String er);
	
	@Query("SELECT i.sbNo,i.erNo,i.erDate,i.nameOfExporter,i.noOfPackages,i.grossWeight,i.parcelStatus,i.dgdcStatus,i.holdStatus,i.hpStatus,i.sbDate,i.airlineCode FROM ExportSHB i " +
	        "LEFT JOIN Party p ON i.nameOfExporter = p.partyId and i.companyId = p.companyId and i.branchId = p.branchId " +
	        "WHERE " +
	        "(:hpStatus IS NULL OR :hpStatus = '' OR i.hpStatus = :hpStatus) " +
	        "AND (:holdStatus IS NULL OR :holdStatus = '' OR i.holdStatus = :holdStatus) " +
	        "AND (:dgdcStatus IS NULL OR :dgdcStatus = '' OR i.dgdcStatus = :dgdcStatus) " +
	        "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
	        "(:startDate IS NULL AND :endDate >= i.erDate) OR " +
	        "(:startDate <= i.erDate AND :endDate IS NULL) OR " +
	        "(:startDate <= i.erDate AND :endDate >= i.erDate)) " +
	        "AND i.companyId = :companyId " +
	        "AND i.branchId = :branchId " +
	        "AND i.status != 'D' " +
	        "AND ((:searchValue IS NULL OR :searchValue = '') OR " +
	        "(i.erNo like CONCAT('%', :searchValue, '%') OR i.sbNo like CONCAT('%', :searchValue, '%')  OR i.nameOfExporter IN " +
	        "(SELECT party.partyId FROM Party party WHERE party.partyName like CONCAT('%', :searchValue, '%') ))) " +
	        "ORDER BY i.erNo DESC")
	List<Object[]> findByAttributes(
	    @Param("companyId") String companyId,
	    @Param("branchId") String branchId,
	    @Param("hpStatus") String hpStatus,
	    @Param("holdStatus") String holdStatus,
	    @Param("dgdcStatus") String dgdcStatus,
	    @Param("startDate") Date startDate,
	    @Param("endDate") Date endDate,
	    @Param("searchValue") String searchValue);
	
	
	@Modifying
	@Query("update ExportSHB e set e.parcelStatus=:nsdl, e.reasonforOverride=:reason, e.overrideDocument=:over where e.companyId=:cid and e.branchId=:bid and e.erNo=:sbid and e.sbNo=:sbno")
	void updateOverride(@Param("nsdl") String nsdl, @Param("reason") String reason, @Param("over") String over, @Param("cid") String cid, @Param("bid") String bid, @Param("sbid") String sbid, @Param("sbno") String sbno);
	
	@Query(value="select e from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.consoleAgent=:console and e.holdStatus != 'Y' and e.dgdcStatus='Handed over to DGDC SHB' and e.parcelStatus IN ('Allow Export','Let Export')")
	public List<ExportSHB> getConsoleData(@Param("cid") String cid,@Param("bid") String bid,@Param("console") String console);
	
	@Query(value="select e from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.consoleAgent=:console and e.holdStatus != 'Y' and e.dgdcStatus='Entry at DGDC Cargo GATE' and e.parcelStatus IN ('Allow Export','Let Export')")
	public List<ExportSHB> getConsoleData1(@Param("cid") String cid,@Param("bid") String bid,@Param("console") String console);
	
	@Query(value="select NEW com.cwms.entities.ExportSHB(e.sbNo,e.erNo,e.sbDate,e.nameOfExporter,e.grossWeight,e.parcelStatus,e.dgdcStatus,e.noOfPackages) from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.consoleAgent=:console and e.holdStatus != 'Y' and e.dgdcStatus='Handed over to DGDC SHB' and e.parcelStatus IN ('Allow Export','Let Export')")
	public List<ExportSHB> getConsoleData2(@Param("cid") String cid,@Param("bid") String bid,@Param("console") String console);
	
	@Query(value="select NEW com.cwms.entities.ExportSHB(e.sbNo,e.erNo,e.sbDate,e.nameOfExporter,e.grossWeight,e.parcelStatus,e.dgdcStatus,e.noOfPackages) from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.consoleAgent=:console and e.holdStatus != 'Y' and e.dgdcStatus='Entry at DGDC Cargo GATE' and e.parcelStatus IN ('Allow Export','Let Export')")
	public List<ExportSHB> getConsoleData3(@Param("cid") String cid,@Param("bid") String bid,@Param("console") String console);
	
	@Query(value="select distinct(e.pctmNo),e.airlineCode from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.tpNo=:tp")
	public List<Object[]> getDistinctPctmData(@Param("cid") String cid,@Param("bid") String bid,@Param("tp") String tp);
	
	@Query(value="select distinct e.tpNo from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.tpDate=:tdate order by e.tpNo desc")
    List<String> getalltp(@Param("cid") String cid, @Param("bid") String bid ,@Param("tdate") Date date);
	
	@Query(value="select e.erNo,e.airwayBillNo,e.sbNo,e.portOfDestination,e.nameOfExporter,e.noOfPackages from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.airlineCode=:airline and e.dgdcStatus='Handed over to DGDC Cargo' and e.holdStatus != 'Y'")
	List<Object[]> getdataByairline(@Param("cid") String cid, @Param("bid") String bid,@Param("airline") String airline);
	
	@Query(value="select e.erNo,e.airwayBillNo,e.sbNo,e.portOfDestination,e.nameOfExporter,e.noOfPackages from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.airlineCode=:airline and e.airLineDate=:airlineDate and e.dgdcStatus='Handed Over to Airline' and e.holdStatus != 'Y'")
	List<Object[]> getdataByairline1(@Param("cid") String cid, @Param("bid") String bid,@Param("airline") String airline,@Param("airlineDate") Date airlineDate);

	
	@Query(value="select distinct(e.pctmNo) from ExportSHB e where e.companyId=:cid and e.branchId=:bid and e.tpNo=:tp and e.airlineCode=:airline")
	   public String getPctmNo(@Param("cid") String cid,@Param("bid") String bid,@Param("tp") String tp,@Param("airline") String airline);

    @Query(value="select distinct NEW com.cwms.entities.ExportSHB( e.tpNo,ex.userName) from ExportSHB e LEFT JOIN ExternalParty ex ON e.companyId = ex.companyId and e.branchId=ex.branchId and e.consoleAgent = ex.externaluserId where e.companyId=:cid and e.branchId=:bid and e.tpDate=:tpdate")  
	public List<ExportSHB> getTpNo(@Param("cid") String cid,@Param("bid") String bid,@Param("tpdate") Date tpdate);
    
    @Query(value="select NEW com.cwms.entities.ExportSHB(e.erNo,e.erDate,p.partyName,e.portOfDestination,e.descriptionOfGoods,e.fobValueInINR,e.noOfPackages) from ExportSHB e LEFT JOIN Party p ON e.companyId=p.companyId and e.branchId=p.branchId and e.nameOfExporter = p.partyId where e.companyId=:cid and e.branchId=:bid and e.tpNo=:customTp and e.tpDate=:tpdate")  
	public List<ExportSHB> getTPData(@Param("cid") String cid,@Param("bid") String bid,@Param("customTp") String custom,@Param("tpdate") Date tpdate);

    @Query(value = "SELECT DISTINCT e.consoleAgent,ex.userName " +
            "FROM ExportSHB e LEFT JOIN ExternalParty ex ON e.companyId=ex.companyId and e.branchId=ex.branchId and e.consoleAgent = ex.externaluserId " +
            "WHERE e.companyId = :companyId " +
            "AND e.branchId = :branchId " +
            " AND DATE_FORMAT(e.erDate, '%Y-%m-%d') =:serDate " +
            "AND e.pctmNo IS NOT NULL " +
            "AND e.tpNo IS NOT NULL " +
            "GROUP BY e.consoleAgent")
    List<Object[]> findAllConsoleAgentNames(@Param("companyId")String companyId,
    		@Param("branchId") String branchId, 
    		@Param("serDate")String serDate);
    
    @Query(value = "SELECT DISTINCT e.tpNo FROM ExportSHB e WHERE e.companyId =:companyId AND e.branchId =:branchId AND  DATE_FORMAT(e.tpDate, '%Y-%m-%d') =:serDate AND e.consoleAgent=:cartingAgent")
    List<String> findCommonTpNos(@Param("companyId") String companyId,
                                  @Param("branchId") String branchId,
                                 
                                  @Param("serDate") String endDate,
                                  @Param("cartingAgent") String cartingAgent);
    
    @Query(value ="SELECT NEW com.cwms.entities.ExportSHB(e.sbNo, e.erNo, e.nameOfExporter, e.grossWeight, e.portOfDestination,e.airwayBillNo, e.noOfPackages, e.hawb) FROM ExportSHB e " +
            "WHERE e.companyId = :companyId " +
            "AND e.branchId = :branchId " +
            "AND DATE_FORMAT(e.tpDate, '%Y-%m-%d') =:erDate " +
            "AND e.consoleAgent = :cartingAgent " +
            "AND e.tpNo = :tpNo " +
            "ORDER BY e.airwayBillNo ")
     List<ExportSHB> findExportTPData(@Param("companyId") String companyId,
    		 @Param("branchId") String branchId,
    		  @Param("erDate") String endDate,
    		  @Param("cartingAgent") String cartingAgent,
    		  @Param ("tpNo")String tpNo);
     
     


     @Query(value ="SELECT distinct e.pctmNo,e.airlineName,sum(e.noOfPackages),COUNT(DISTINCT e.sbNo),COUNT(DISTINCT e.airwayBillNo) FROM ExportSHB e " +
	            "WHERE e.companyId = :companyId " +
	            "AND e.branchId = :branchId " +
	            "AND DATE_FORMAT(e.tpDate, '%Y-%m-%d') =:erDate " +
	            "AND e.tpNo = :tpNo " +
	            "AND e.airlineName IS NOT NULL " +
	            "GROUP BY e.airlineName,e.pctmNo")
	     List<Object[]> findExportPctmSummery(@Param("companyId") String companyId,
	    		 @Param("branchId") String branchId,
	    		  @Param("erDate") String endDate,@Param ("tpNo")String tpNo);

}
