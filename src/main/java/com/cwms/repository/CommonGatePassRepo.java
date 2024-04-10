package com.cwms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.CommonGatePass;

public interface CommonGatePassRepo extends JpaRepository<CommonGatePass, String> {

//	@Query(value="select * from common_gate_pass where delivery_date=:date1",nativeQuery=true)
//	List<CommonGatePass> getbydate(@Param("date1") Date date);
	
	
	
	
	public CommonGatePass findByCompanyIdAndBranchIdAndCommonId(String comapanyId,String branchId,String commonId);
	
	
	 @Query(value =
	            "SELECT cg.commonId AS commonId, " +
	            "CASE " +
	            "   WHEN cg.nameOfPartyCha LIKE 'M%' THEN p.partyName " +
	            "   WHEN cg.nameOfPartyCha LIKE 'E%' THEN ep.userName " +
	            "   ELSE NULL " +
	            "END AS partyName, " +
	            "CONCAT(rp.firstName, ' ', rp.lastName) AS userName, " +
	            "cg.deliveryDate AS deliveryDate, " +
	            "cg.deliveryTime AS deliveryTime " +
	            "FROM CommonGatePass cg " +
	            "LEFT OUTER JOIN Party p ON cg.nameOfPartyCha LIKE 'M%' " +
	            "    AND p.partyId = cg.nameOfPartyCha " +
	            "    AND cg.companyId = p.companyId " +
	            "    AND cg.branchId = p.branchId " +
	            "LEFT OUTER JOIN ExternalParty ep ON cg.nameOfPartyCha LIKE 'E%' " +
	            "    AND ep.externaluserId = cg.nameOfPartyCha " +
	            "    AND cg.companyId = ep.companyId " +
	            "    AND cg.branchId = ep.branchId " +
	            "LEFT OUTER JOIN RepresentParty rp ON cg.receiverName = rp.representativeId " +
	            "WHERE cg.deliveryDate = :deliveryDate " +
	            "    AND cg.companyId = :companyId " +
	            "    AND cg.branchId = :branchId"
	    )
	    List<Object[]> getbydate(
	            @Param("deliveryDate") Date deliveryDate,
	            @Param("companyId") String companyId,
	            @Param("branchId") String branchId
	    );

}
