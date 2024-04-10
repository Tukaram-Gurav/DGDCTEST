package com.cwms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import java.util.*;
import com.cwms.entities.Import;

import jakarta.transaction.Transactional;


@EnableJpaRepositories
public interface ImportRepository extends JpaRepository<Import, String> {
	
	
//	Update Custom TP Number 
	@Transactional
	 @Modifying
	 @Query(value = "UPDATE Import e " +
	            "SET e.editedBy=:user , e.editedDate=CURRENT_TIMESTAMP,e.customPctmNo = :customPctmNo , e.customPctmDate = :customPctmDate " +
	            "WHERE e.companyId = :companyId " +
	            "AND e.branchId = :branchId " +
	            "AND e.sirNo = :sirNo " +
	            "AND e.mawb = :mawb " +
	            "AND e.consoleName = :console " +	           
	            "AND e.hawb = :hawb " +
	            "AND e.countryOrigin = :country " +	            
	            "AND e.dgdcStatus IN ('Handed over to DGDC Cargo','Handed over to Console') " +
	            "AND e.status <>'D'")
	    int updateCustomPCTMNumber(	           
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("sirNo") String sirNo,
	            @Param("mawb") String mawb,
	            @Param("hawb") String hawb,
	            @Param("customPctmNo") String customPctmNo,
	            @Param("customPctmDate") Date customPctmDate,
	            @Param("user") String user,
	            @Param("console") String console,
	            @Param("country") String country
	    );
	
	
	

//	Update Custom TP Number 
	@Transactional
	 @Modifying
	 @Query(value = "UPDATE Import e " +
	            "SET e.editedBy=:user , e.editedDate=CURRENT_TIMESTAMP,e.customTpNo = :customTpNo , e.customTpDate = :customTpDate " +
	            "WHERE e.companyId = :companyId " +
	            "AND e.branchId = :branchId " +
	            "AND e.sirNo = :sirNo " +
	            "AND e.mawb = :mawb " +
	            "AND e.consoleName = :console " +	           
	            "AND e.hawb = :hawb " +
	            "AND e.dgdcStatus IN ('Handed over to DGDC Cargo','Handed over to Console') " +
	            "AND e.status <>'D'")
	    int updateCustomTPNumber(	           
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("sirNo") String sirNo,
	            @Param("mawb") String mawb,
	            @Param("hawb") String hawb,
	            @Param("customTpNo") String customTpNo,
	            @Param("customTpDate") Date customTpDate,
	            @Param("user") String user,
	            @Param("console") String console      
	    );
	
	
//	Hand Over To Cha
	
	@Transactional
	 @Modifying
	 @Query(value = "UPDATE Import e " +
	            "SET e.editedBy=:user , e.editedDate=CURRENT_TIMESTAMP, e.dgdcStatus=:dgdcStatus , e.handedOverPartyId =:console,e.handedOverRepresentativeId =:representative ,e.handedOverToType = 'C',e.outDate = CURRENT_TIMESTAMP " +
	            "WHERE e.companyId = :companyId " +
	            "AND e.branchId = :branchId " +
	            "AND e.sirNo = :sirNo " +
	            "AND e.mawb = :mawb " +
	            "AND e.chaName = :console " +	           
	            "AND e.hawb = :hawb " +
	            "AND e.dgdcStatus = 'Handed over to DGDC SHB' " +
	            "AND e.status <>'D'")
	    int updateHandOverToParty(	           
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("sirNo") String sirNo,
	            @Param("mawb") String mawb,
	            @Param("hawb") String hawb,
	            @Param("dgdcStatus") String dgdcStatus,
	            @Param("user") String user,
	            @Param("console") String console,
	            @Param("representative") String representative      
	            
	    );
	
		
	
	@Transactional
	 @Modifying
	 @Query(value = "UPDATE Import e " +
	            "SET e.editedBy=:user , e.editedDate=CURRENT_TIMESTAMP, e.dgdcStatus=:dgdcStatus ,e.seepzInDate = CURRENT_TIMESTAMP " +
	            "WHERE e.companyId = :companyId " +
	            "AND e.branchId = :branchId " +
	            "AND e.sirNo = :sirNo " +
	            "AND e.mawb = :mawb " +
	            "AND e.cartingAgent = :console " +
	            "AND e.partyRepresentativeId = :representative " +
	            "AND e.hawb = :hawb " +
	            "AND e.dgdcStatus = 'Entry at DGDC SHB Gate' " +
	            "AND e.status <>'D'")
	    int updateCartingAgentReceived(	           
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("sirNo") String sirNo,
	            @Param("mawb") String mawb,
	            @Param("hawb") String hawb,
	            @Param("dgdcStatus") String dgdcStatus,
	            @Param("user") String user,
	            @Param("console") String console,
	            @Param("representative") String representative      
	            
	    );
	
	
	
	
	 @Transactional
	 @Modifying
	 @Query(value = "UPDATE Import e " +
	            "SET e.cartingAgent = :console, e.tpNo = :tpNo , e.tpDate = CURRENT_TIMESTAMP, e.editedBy=:user , e.editedDate=CURRENT_TIMESTAMP, e.dgdcStatus=:dgdcStatus , e.partyRepresentativeId=:representative " +
	            "WHERE e.companyId = :companyId " +
	            "AND e.branchId = :branchId " +
	            "AND e.sirNo = :sirNo " +
	            "AND e.mawb = :mawb " +
	            "AND e.hawb = :hawb " +
	            "AND e.dgdcStatus = 'Handed over to DGDC Cargo' " +
	            "AND e.status <>'D'")
	    int updateCartingAgent(	           
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("sirNo") String sirNo,
	            @Param("mawb") String mawb,
	            @Param("hawb") String hawb,
	            @Param("dgdcStatus") String dgdcStatus,
	            @Param("user") String user,
	            @Param("console") String console,
	            @Param("representative") String representative,
	            @Param("tpNo") String tpNo
	            
	    );
	
	
	 @Transactional
	 @Modifying
	 @Query(value = "UPDATE Import e " +
	            "SET e.pctmNo = :pctmNo, e.pctmDate = CURRENT_TIMESTAMP " +
	            "WHERE e.companyId = :companyId " +
	            "AND e.branchId = :branchId " +
	            "AND e.sirNo = :sirNo " +
	            "AND e.mawb = :mawb " +
	            "AND e.hawb = :hawb " +
	            "AND e.status <>'D'")
	    int updatePctmNo(	           
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("sirNo") String sirNo,
	            @Param("mawb") String mawb,
	            @Param("hawb") String hawb,
	            @Param("pctmNo") String pctmNo   
	            
	    );
	

	@Query(value = "SELECT NEW com.cwms.entities.Import(i.companyId, i.branchId,i.mawb,i.hawb,i.sirNo,i.sirDate,i.airlineName,e.partyName,i.nop) " + "FROM Import i " + "LEFT JOIN Party e ON i.importerId = e.partyId And i.companyId=e.companyId And i.branchId=e.branchId " +
			"WHERE i.companyId = :companyId " + "AND i.branchId = :branchId "
			+ "AND DATE(i.pctmDate) BETWEEN :startDate AND :endDate " + "AND i.consoleName = :console "
			+ "AND i.status <> 'D' " + "AND pctmNo =:pctmNo " + "AND i.airlineCode = :flightNo")
	List<Import> findImportAllDataPctmNumber(@Param("companyId") String companyId, @Param("branchId") String branchId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("flightNo") String flightNo,
			@Param("console") String console, @Param("pctmNo") String pctmNo);

	@Query(value = "SELECT DISTINCT i.pctmNo " + "FROM Import i " + "WHERE i.companyId = :companyId "
			+ "AND i.branchId = :branchId " + "AND i.consoleName = :console " + "AND i.status <> 'D' "
			+ "AND i.airlineCode = :airlineCode " + "AND DATE(i.pctmDate) BETWEEN :startDate AND :endDate")
	List<String> getDistinctPctmNumbers(@Param("companyId") String companyId, @Param("branchId") String branchId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("console") String console,
			@Param("airlineCode") String airlineCode);

	@Query(value = "SELECT distinct i.consoleName ,e.userName " + "FROM Import i "
			+ "LEFT JOIN ExternalParty e ON i.consoleName = e.externaluserId AND i.companyId = e.companyId AND i.branchId = e.branchId "
			+ "WHERE i.companyId = :companyId " + "AND i.branchId = :branchId " + "AND i.status <> 'D' "
			+ "AND (i.pctmNo IS NOT NULL AND i.pctmNo <> '') " + "AND DATE(i.pctmDate) BETWEEN :startDate AND :endDate")
	List<Object[]> getdistinctConsolesPCTMgenerated(String companyId, String branchId, Date startDate, Date endDate);

	@Query(value = "SELECT distinct i.airlineCode ,i.airlineName " + "FROM Import i "
			+ "WHERE i.companyId = :companyId " + "AND i.branchId = :branchId " + "AND i.consoleName = :Console "
			+ "AND i.status <> 'D' " + "AND (i.pctmNo IS NOT NULL AND i.pctmNo <> '') " 
			+ "AND DATE(i.pctmDate) BETWEEN :startDate AND :endDate")
	List<Object[]> getdistinctAirLinesPCTMgenerated(String companyId, String branchId, Date startDate, Date endDate,
			String Console);
	
	
	
	@Query(value = "SELECT NEW com.cwms.entities.Import(i.companyId, i.branchId,i.mawb,i.hawb,i.sirNo,i.sirDate,i.airlineName,i.importerId,i.nop) " + "FROM Import i " 
	+ "WHERE i.companyId = :companyId " + "AND i.branchId = :branchId "
			+ "AND DATE(i.sirDate) BETWEEN :startDate AND :endDate " + "AND i.consoleName = :console "
			+ "AND i.status <> 'D' " + "AND (i.pctmNo = '' OR i.pctmNo IS NULL) " + "AND i.airlineCode = :flightNo")
	List<Import> findImportAllData(@Param("companyId") String companyId, @Param("branchId") String branchId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("flightNo") String flightNo,
			@Param("console") String console);
	

	@Query(value = "SELECT distinct i.consoleName ,e.userName " + "FROM Import i "
			+ "LEFT JOIN ExternalParty e ON i.consoleName = e.externaluserId AND i.companyId = e.companyId AND i.branchId = e.branchId "
			+ "WHERE i.companyId = :companyId " + "AND i.branchId = :branchId " + "AND i.status <> 'D' "
			+ "AND (i.pctmNo = '' OR i.pctmNo IS NULL) " + "AND DATE(i.sirDate) BETWEEN :startDate AND :endDate")
	List<Object[]> getdistinctConsoles(String companyId, String branchId, Date startDate, Date endDate);

	@Query(value = "SELECT distinct i.airlineCode ,i.airlineName " + "FROM Import i "
			+ "WHERE i.companyId = :companyId " + "AND i.branchId = :branchId " + "AND i.consoleName = :Console "
			+ "AND i.status <> 'D' " + "AND (i.pctmNo = '' OR i.pctmNo IS NULL) "
			+ "AND DATE(i.sirDate) BETWEEN :startDate AND :endDate")
	List<Object[]> getdistinctAirLines(String companyId, String branchId, Date startDate, Date endDate, String Console);

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Query(value = "SELECT i.sir_date, i.sir_no, " + "CASE " + "   WHEN i.nipt_status = 'Y' THEN 'NIPT' "
			+ "   WHEN i.sc_status = 'Y' THEN 'SC' " + "   WHEN i.pc_status = 'Y'  THEN 'PC' "
			+ "   ELSE i.parcel_type " + "END AS parcel_type, "
			+ "i.flight_no, p.party_name, i.nop, i.mawb, i.hawb, i.be_request_id , i.dgdc_status " + "FROM import i "
			+ "LEFT JOIN party p ON i.importer_id = p.party_id AND i.company_id = p.company_id AND i.branch_id = p.branch_id "
			+ "WHERE i.company_id = :companyId " + "AND i.branch_id = :branchId " + "AND i.cancel_status <> 'Y' "
			+ "AND i.status <> 'D' " + "AND ((" + "       (DATE(i.sir_date) = :dateValue AND i.nipt_status = 'Y') "
			+ "       OR " + "       (DATE(i.sir_date) = :dateValue AND i.pc_status = 'Y') " + ") OR ("
			+ "       DATE(i.tp_date) = :dateValue " + "))", nativeQuery = true)
	List<Object[]> findImportTransactionDataSeepz(@Param("companyId") String companyId,
			@Param("branchId") String branchId, @Param("dateValue") Date dateValue);

	@Query(value = "SELECT  i.sir_date, i.sir_no, " + "CASE " + "   WHEN i.pc_status = 'Y' THEN 'PC' "
			+ "   WHEN i.nipt_status = 'Y' THEN 'NIPT' " + "   WHEN i.sc_status = 'Y' THEN 'Sc' "
			+ "   ELSE i.parcel_type " + "END AS parcel_type, "
			+ "i.flight_no, p.party_name, i.nop, i.mawb, i.hawb, i.be_request_id, i.dgdc_status " + "FROM import i "
			+ "LEFT JOIN party p ON i.importer_id = p.party_id AND i.company_id = p.company_id AND i.branch_id = p.branch_id "
			+ "WHERE i.company_id = :companyId " + "AND i.branch_id = :branchId " + "AND i.cancel_status <> 'Y' "
			+ "AND i.status <> 'D' "
			+ "AND (:columnName = 'sir_date' AND DATE(i.sir_date) = :dateValue AND i.nipt_status <> 'Y' AND i.pc_status <> 'Y' OR "
			+ "     :columnName = 'out_date' AND DATE(i.out_date) = :dateValue OR "
			+ "     :columnName = 'tp_date' AND DATE(i.tp_date) = :dateValue)", nativeQuery = true)
	List<Object[]> findImportTransactionData(@Param("companyId") String companyId, @Param("branchId") String branchId,
			@Param("dateValue") Date dateValue, @Param("columnName") String columnName);

//Export Transaction

	@Query(value = "SELECT i.sb_request_id, i.sb_number, i.sb_date, i.ser_no, i.ser_date, " + "CASE "
			+ "   WHEN i.pc_status = 'Y' THEN 'P' " + "   WHEN i.sc_status = 'Y' THEN 'SC' "
			+ "   WHEN i.hp_status = 'HP' THEN 'HP' " + "   ELSE '' " + "END AS parcel_type, "
			+ "p.party_name, i.no_of_packages, i.gross_weight, i.uomgross_weight, i.dgdc_status " + "FROM export i "
			+ "LEFT JOIN party p ON i.exporter_name = p.party_id AND i.company_id = p.company_id AND i.branch_id = p.branch_id "
			+ "WHERE i.company_id = :companyId " + "AND i.branch_id = :branchId " + "AND i.cancel_status <> 'Y' "
			+ "AND i.status <> 'D' " + "AND CASE :columnName " + "       WHEN 'ser_date' THEN i.ser_date "
			+ "       WHEN 'created_date' THEN i.created_date " + "       WHEN 'out_date' THEN i.out_date "
			+ "       WHEN 'airline_date' THEN i.airline_date " + "    END = :dateValue ", nativeQuery = true)
	List<Object[]> findExportTransactionData(@Param("companyId") String companyId, @Param("branchId") String branchId,
			@Param("dateValue") Date dateValue, @Param("columnName") String columnName);

	@Query(value = "SELECT i.sir_no, i.sir_date, p.party_name, i.nop, i.description_of_goods, i.dgdc_status, i.be_no, i.be_date, i.gross_weight, i.assessable_value ,i.igm_no, i.mawb, i.hawb, e.short_form "
			+ "FROM import i "
			+ "LEFT JOIN party p ON i.importer_id = p.party_id AND i.company_id = p.company_id AND i.branch_id = p.branch_id "
			+ "LEFT JOIN external_party e ON i.console_name = e.external_user_id AND i.company_id = e.company_id AND i.branch_id = e.branch_id "
			+ "WHERE i.company_id = :companyId " + "AND i.branch_id = :branchId " + "AND i.cancel_status = 'N' "
			+ "AND i.status <> 'D' " + "AND DATE(i.sir_date) BETWEEN :startDate AND :endDate "
			+ "AND (:consoleName IS NULL OR :consoleName = '' OR i.console_name = :consoleName)", nativeQuery = true)
	List<Object[]> findImportDataRegister(String companyId, String branchId, Date startDate, Date endDate,
			String consoleName);

	@Query(value = "SELECT i.sir_no, i.sir_date, p.party_name, i.nop, '', i.dgdc_status, ' ', ' ', i.net_weight, ' ','',' ', ' ', ' ' "
			+ "FROM importsub i "
			+ "LEFT JOIN party p ON i.exporter = p.party_id AND i.company_id = p.company_id AND i.branch_id = p.branch_id "
			+ "WHERE i.company_id = :companyId " + "AND i.branch_id = :branchId " + "AND i.status <> 'D' "
			+ "AND DATE(i.sir_date) BETWEEN :startDate AND :endDate ", nativeQuery = true)
	List<Object[]> findImportSubData(String companyId, String branchId, Date startDate, Date endDate);

	Import findByCompanyIdAndBranchIdAndSirNoAndImpTransIdAndMawbAndHawbAndImporterIdAndStatusNot(String companyId,
			String branchId, String sirNo, String transId, String mawb, String hawb, String importer, String status);

	@Query(value = "SELECT * FROM Import WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND DATE(Out_Date) =  :startDate AND (gate_pass_status IS NULL OR gate_pass_status != 'Y' OR gate_pass_status = '')", nativeQuery = true)
	List<Import> findByCompanyAndBranchAndDate2(String companyId, String branchId, Date startDate);

	@Query(value = "SELECT SUM(totalNop) AS grandTotalNop " + "FROM ("
			+ "    SELECT COALESCE(SUM(i.nop), 0) AS totalNop " + "    FROM Import i "
			+ "    WHERE DATE(i.sir_date) <= CURRENT_DATE() " + "      AND i.dgdc_status = 'Handed over to DGDC SEEPZ' "
			+ "      AND i.company_id = :companyId " + "      AND i.branch_id = :branchId "
			+ "      AND i.status <> 'D' " + "    UNION ALL " + "    SELECT COALESCE(SUM(isub.nop), 0) AS totalNop "
			+ "    FROM ImportSub isub " + "    WHERE isub.sir_date <= CURRENT_DATE() "
			+ "      AND isub.dgdc_status = 'Handed over to DGDC SEEPZ' " + "      AND isub.company_id = :companyId "
			+ "      AND isub.branch_id = :branchId " + "      AND isub.status <> 'D'"
			+ ") AS subquery", nativeQuery = true)
	int getGrandTotalNop(@Param("companyId") String companyId, @Param("branchId") String branchId);

	@Query(value = "SELECT SUM(totalNoOfPackages) AS grandTotalNoOfPackages " + "FROM ("
			+ "    SELECT COALESCE(SUM(i.no_Of_Packages), 0) AS totalNoOfPackages " + "    FROM Export i "
			+ "    WHERE i.ser_Date <= CURRENT_DATE() " + "      AND i.dgdc_Status = 'Handed over to DGDC SEEPZ' "
			+ "      AND i.company_Id = :companyId " + "      AND i.branch_Id = :branchId "
			+ "      AND i.status <> 'D' " + "    UNION ALL "
			+ "    SELECT COALESCE(SUM(isub.nop), 0) AS totalNoOfPackages " + "    FROM ExportSub isub "
			+ "    WHERE isub.ser_Date <= CURRENT_DATE() " + "      AND isub.dgdc_Status = 'Handed over to DGDC SEEPZ' "
			+ "      AND isub.company_Id = :companyId " + "      AND isub.branch_Id = :branchId "
			+ "      AND isub.status <> 'D'" + ") AS subquery", nativeQuery = true)
	int getGrandTotalNoOfPackages(@Param("companyId") String companyId, @Param("branchId") String branchId);

	@Query(value = "select * from import where company_id=:cid and branch_id=:bid", nativeQuery = true)
	public List<Import> findByAll(@Param("cid") String cid, @Param("bid") String bid);

	@Query(value = "select distinct i.tp_no from import i where DATE(i.tp_date) =:date and i.company_id=:cid and i.branch_id=:bid", nativeQuery = true)
	public List<String> findByTp(@Param("date") Date date, @Param("cid") String cid, @Param("bid") String bid);

	public List<Import> findByTpDate(Date tpDate);

	@Query(value = "SELECT * FROM import i WHERE i.company_id = :cid AND i.branch_id = :bid AND DATE(i.tp_date) = :date AND i.tp_no = :tpno  ORDER BY sir_no ASC", nativeQuery = true)
	public List<Import> findByTpdateTpno(@Param("cid") String cid, @Param("bid") String bid, @Param("date") Date date,
			@Param("tpno") String tp_no);

	@Query(value = "SELECT airline_name FROM import WHERE company_id = ?1 AND branch_id = ?2 AND DATE(sir_date) BETWEEN ?3 AND ?4 AND status = 'A'  GROUP BY airline_name", nativeQuery = true)
	List<String> findAirlineNames(String companyId, String branchId, Date startDate, Date endDate);

	@Query(value = "SELECT DISTINCT airline_name, mawb,  sir_no,sir_date, parcel_type, hawb, nop ,flight_date,flight_no FROM import  WHERE company_id =:companyId AND branch_id =:branchId  AND DATE(sir_date) BETWEEN :startDate AND :endDate AND status = 'A' AND (pctm_no = '' OR pctm_no IS NULL) AND airline_name = :airlineName", nativeQuery = true)
	List<Object[]> findImportData(@Param("companyId") String companyId, @Param("branchId") String branchId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("airlineName") String airlineName);

	@Query(value = "SELECT DISTINCT * " + "FROM import " + "WHERE company_id = :companyId "
			+ "AND branch_id = :branchId " + "AND cancel_status='N'"
			+ "AND DATE(sir_date) BETWEEN :startDate AND :endDate "
			+ "AND console_name = :consoleName", nativeQuery = true)
	List<Import> findIMMportAllData(String companyId, String branchId, Date startDate, Date endDate,
			String consoleName);

	@Query(value = "SELECT DISTINCT * " + "FROM import " + "WHERE company_id = :companyId "
			+ "AND branch_id = :branchId " + "AND cancel_status='N'"
			+ "AND DATE(sir_date) BETWEEN :startDate AND :endDate ", nativeQuery = true)
	List<Import> findImportData(String companyId, String branchId, Date startDate, Date endDate);

	// shubham
	@Query(value = "SELECT SUM(Nop) FROM Import WHERE Company_Id =:cid AND Branch_Id =:bid AND DGDC_Status =:string1 AND DATE(SIR_Date) < :date1", nativeQuery = true)
	int findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDateNot(@Param("cid") String cid, @Param("bid") String bid,
			@Param("string1") String string1, @Param("date1") Date date1);

	@Query(value = "SELECT COALESCE(SUM(Nop), 0) FROM Import WHERE Company_Id =:cid AND Branch_Id =:bid AND Nipt_Status =:string1 AND DATE(SIR_Date) =:date1", nativeQuery = true)
	int findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDateNot1(@Param("cid") String cid, @Param("bid") String bid,
			@Param("string1") String string1, @Param("date1") Date date1);

	@Query(value = "SELECT COALESCE(SUM(Nop), 0) FROM Import WHERE Company_Id =:cid AND Branch_Id =:bid AND Nipt_Status =:string1 AND DATE(tp_date) =:date1", nativeQuery = true)
	int importReceivedbytpDate(@Param("cid") String cid, @Param("bid") String bid, @Param("string1") String string1,
			@Param("date1") Date date1);

	@Query(value = "SELECT SUM(Nop) FROM Import WHERE Company_Id = :cid AND Branch_Id = :bid AND DGDC_Status = :string AND Nipt_Status = :string1 AND DATE(SIR_Date) <  :date1", nativeQuery = true)
	int findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDateNot1(@Param("cid") String cid, @Param("bid") String bid,
			@Param("string") String string, @Param("string1") String string1, @Param("date1") Date date1);

	@Query(value = "SELECT SUM(Nop) FROM Import WHERE Company_Id = :cid AND Branch_Id = :bid AND DGDC_Status = :string AND Nipt_Status = :string1 AND DATE(out_date0 = :date1", nativeQuery = true)
	Integer findSumByCompanyIdAndBranchIdAndDgdcStatusAndSerDate3(@Param("cid") String cid, @Param("bid") String bid,
			@Param("string") String string, @Param("string1") String string1, @Param("date1") Date date1);

	@Query(value = "SELECT * FROM Import WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND DATE(Out_Date) =  :startDate AND  handed_over_party_id =:paryCHAId AND handed_over_representative_id=:representativeId AND (gate_pass_status IS NULL OR gate_pass_status != 'Y' OR gate_pass_status = '')", nativeQuery = true)
	List<Import> findByCompanyAndBranchAndDate1(String companyId, String branchId, Date startDate, String paryCHAId,
			String representativeId);

//	    @Query(value = "SELECT * FROM Import WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND SIR_Date =  :startDate AND  handed_over_party_id =:paryCHAId AND handed_over_representative_id=:representativeId ", nativeQuery = true)
//	    List<Import> findByCompanyAndBranchAndDate4(String companyId, String branchId, Date startDate, String paryCHAId, String representativeId);

	@Query(value = "SELECT * FROM Import WHERE Company_Id = :companyId AND Branch_Id = :branchId AND DGDC_Status = 'Handed over to Party/CHA' AND DATE(Out_Date) =  :startDate AND  handed_over_party_id =:paryCHAId AND handed_over_representative_id=:representativeId ", nativeQuery = true)
	List<Import> findByCompanyAndBranchAndDate4(String companyId, String branchId, Date startDate, String paryCHAId,
			String representativeId);
}
