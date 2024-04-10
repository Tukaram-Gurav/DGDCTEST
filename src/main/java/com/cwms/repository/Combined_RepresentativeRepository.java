package com.cwms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.Combined_Representative;

public interface Combined_RepresentativeRepository extends JpaRepository<Combined_Representative, String> {

	@Query(value="select c from Combined_Representative c where c.companyId=:cid and c.branchId=:bid and c.partyId=:party and status='A'")
	Combined_Representative getById(@Param("cid") String cid,@Param("bid") String bid,@Param("party") String party);
	
	@Query(value="select c from Combined_Representative c where c.companyId=:cid and c.branchId=:bid and c.partyId=:party and status='A'")
	List<Combined_Representative> getByIds(@Param("cid") String cid,@Param("bid") String bid,@Param("party") String party);
	
	@Query(value="select c from Combined_Representative c where c.companyId=:cid and c.branchId=:bid and c.erpDocRefId=:erp  and c.partyId=:party and status='A'")
	Combined_Representative getByIdandParty(@Param("cid") String cid,@Param("bid") String bid,@Param("erp") String erp,@Param("party") String party);


	@Query(value="select c from Combined_Representative c where c.companyId=:cid and c.branchId=:bid and c.erpDocRefId=:erp and status='A'")
	List<Combined_Representative> getByERPId(@Param("cid") String cid,@Param("bid") String bid,@Param("erp") String erp);
	
	 @Query(value = "SELECT DISTINCT c.erpDocRefId,c.groupName FROM Combined_Representative c " +
	            "WHERE c.companyId = :cid AND c.branchId = :bid AND c.status = 'A'")
	    List<Object[]> findDistinctErpDocRefIdAndPartyId(
	            @Param("cid") String cid,
	            @Param("bid") String bid
	    );
}


