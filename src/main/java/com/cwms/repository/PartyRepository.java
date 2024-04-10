package com.cwms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cwms.entities.Party;
import com.cwms.entities.PartyId;

import jakarta.transaction.Transactional;

@Repository
public interface PartyRepository extends JpaRepository<Party, PartyId> {
	
	
	@Transactional
	 @Modifying
	 @Query(value = "UPDATE Party p " +
	            "SET p.lastInVoiceNo=:invoiceNumber , p.lastInVoiceDate=CURRENT_TIMESTAMP " +
	            "WHERE p.companyId = :companyId " +
	            "AND p.branchId = :branchId " +
	            "AND p.partyId = :partyId " +	            
	            "AND p.status <>'D'")
	    int updateParty(	           
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId,
	            @Param("invoiceNumber") String invoiceNumber,
	            @Param("partyId") String partyId	           
	    );
	
	
	

	
	@Query("SELECT DISTINCT p.partyId, p.partyName " +
		       "FROM Party p " +
		       "LEFT JOIN Import i ON p.partyId = i.importerId AND i.companyId = p.companyId AND i.branchId = p.branchId AND i.billCalculated = 'N' AND DATE(i.outDate) = CURRENT_DATE() AND i.status <> 'D' " +
		       "LEFT JOIN ExportSHB e ON p.partyId = e.nameOfExporter AND e.companyId = :companyId AND e.branchId = p.branchId AND e.billCalculated = 'N' AND DATE(e.outDate) = CURRENT_DATE() AND e.status <> 'D' " +
		       "WHERE (i.importerId IS NOT NULL OR e.nameOfExporter IS NOT NULL) " +
		       "AND p.companyId = :companyId AND p.branchId = :branchId AND p.status <> 'D'")
		List<Object[]> getNonBilledAllParties(@Param("companyId") String companyId, @Param("branchId") String branchId);

		@Query("SELECT DISTINCT NEW com.cwms.entities.Party(p.partyId,p.partyName,p.address1,p.email,p.phoneNo,p.gstNo,p.lastInVoiceDate) " +
			       "FROM Party p " +
			       "LEFT JOIN Import i ON p.partyId = i.importerId AND i.companyId = p.companyId AND i.branchId = p.branchId AND i.billCalculated = 'N' AND DATE(i.outDate) = CURRENT_DATE() AND i.status <> 'D' " +
			       "LEFT JOIN ExportSHB e ON p.partyId = e.nameOfExporter AND e.companyId = :companyId AND e.branchId = p.branchId AND e.billCalculated = 'N' AND DATE(e.outDate) = CURRENT_DATE() AND e.status <> 'D' " +
			       "WHERE (i.importerId IS NOT NULL OR e.nameOfExporter IS NOT NULL) " +
			       "AND p.companyId = :companyId AND p.branchId = :branchId AND p.status <> 'D'")
			List<Party> getNonBilledAllPartieIds(@Param("companyId") String companyId, @Param("branchId") String branchId);



	@Query(value = "SELECT NEW com.cwms.entities.Party(p.companyId, p.branchId,p.partyId,p.partyName,p.iecNo,p.city) " + "FROM Party p " +
			"WHERE p.companyId = :companyId " + "AND p.branchId = :branchId "
			+ "AND p.status <> 'D' " + "AND p.partyId =:partyId")
	Party getByPartyId(@Param("companyId") String companyId, @Param("branchId") String branchId, @Param("partyId") String partyId);
	
	
	
	@Query("SELECT p.partyId, p.partyName FROM Party p WHERE p.companyId = :companyId AND p.branchId = :branchId AND p.status <> 'D'")
	List<Object[]> getAllPartiesByCompanyAndBranch(@Param("companyId") String companyId, @Param("branchId") String branchId);
	
	
	Party findByPartyId(String partyId);
	
	
//	List<Party> findByCompanyIdAndBranchIdAndInvoiceTypeAndStatusNot(String companyId,String branchId,String inviceType, String Status);

	// void deleteById( String partyId);

	void deleteByPartyId(String partyId);
	
//	@Query(value="select * from party where company_id=:cid and branch_id=:bid and entity_id=:entity",nativeQuery=true)
//	Party findbyentityid(@Param("cid") String cid,@Param("bid") String bid,@Param("entity") String entity);
//	
	
	@Query(value="select i from Party i where i.companyId=:cid and i.branchId=:bid and i.entityId=:entity")
	Party findbyentityid(@Param("cid") String cid,@Param("bid") String bid,@Param("entity") String entity);
	
	 List<Party> findByCompanyIdAndBranchIdAndPartyIdNotIn(String companyId, String branchId, List<String> excludedPartyIds);
	
	 @Query(value = "SELECT * FROM party WHERE company_id = :cid AND branch_id = :bid AND status <> 'D' ORDER BY party_Name ASC", nativeQuery = true)
	 public List<Party> getalldata(@Param("cid") String cid, @Param("bid") String bid);
	 
	 @Query(value = "SELECT * FROM party WHERE company_id = :cid AND branch_id = :bid AND status != 'D' and party_status != 'I' ORDER BY party_Name ASC", nativeQuery = true)
	 public List<Party> getalldata3(@Param("cid") String cid, @Param("bid") String bid);
	 
	 
	 
		
		@Query(value="select * from party where company_id=:cid and branch_id=:bid and party_name=:pname and status != 'D'",nativeQuery=true)
		public Party getdatabypartyname(@Param("cid") String cid,@Param("bid") String bid,@Param("pname") String pname );
		
		@Query(value="select * from party where company_id=:cid and branch_id=:bid and party_id=:pid and status != 'D'",nativeQuery=true)
		public Party getdatabyid(@Param("cid") String cid,@Param("bid") String bid,@Param("pid") String pid);
	
	
	Party findByCompanyIdAndBranchIdAndPartyName(String companyId, String branchId,String partyName);
	
	@Query("SELECT p.partyName FROM Party p WHERE p.companyId = :companyId AND p.branchId = :branchId AND p.partyId = :partyId")
    String findPartyNameByKeys(
        @Param("companyId") String companyId,
        @Param("branchId") String branchId,
        @Param("partyId") String partyId
    );
 
	Party findByCompanyIdAndBranchIdAndPartyId(String companyId, String branchId,String partyId);

 @Query("SELECT p.lastInVoiceDate FROM Party p WHERE p.companyId = :companyId AND p.branchId = :branchId AND p.partyId = :partyId")
    Date findInvoiceDateByKeys(
        @Param("companyId") String companyId,
        @Param("branchId") String branchId,
        @Param("partyId") String partyId
    );
 
 @Query("SELECT p.partyId FROM Party p WHERE p.companyId = :companyId AND p.branchId = :branchId AND p.entityId = :EntityId")
 String findPartyNameEntityId(
     @Param("companyId") String companyId,
     @Param("branchId") String branchId,
     @Param("EntityId") String EntityId
 );
 
 
 @Query(value="select * from party where company_id=:cid and branch_id=:bid and status <> 'D' order by party_id desc",nativeQuery=true)
	public List<Party> getalldata1(@Param("cid") String cid,@Param("bid") String bid );
 List<Party> findByCompanyIdAndBranchIdAndInvoiceTypeAndStatusNot(String companyId,String branchId,String inviceType, String Status);
 
 
 @Query(value="SELECT * FROM party WHERE company_id = :cid AND branch_id = :bid AND party_id = :pid AND loa_expiry_date > :date AND status != 'D'", nativeQuery = true)
 public Party getdatabyid1(@Param("cid") String cid, @Param("bid") String bid, @Param("pid") String pid, @Param("date") String date);
 
 @Query("SELECT p.partyName FROM Party p WHERE p.partyId = :partyId")
 String findPartyNameById(     
     @Param("partyId") String partyId
 );
}
