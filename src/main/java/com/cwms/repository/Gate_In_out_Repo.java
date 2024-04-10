package com.cwms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.Gate_In_Out;

import jakarta.transaction.Transactional;

public interface Gate_In_out_Repo extends JpaRepository<Gate_In_Out, String> {
  
	@Query(value="select * from gate_in_out where company_id=:cid and branch_id=:bid and sr_no=:sr",nativeQuery=true)
	Gate_In_Out findbysr(@Param("cid") String cid,@Param("bid") String bid,@Param("sr") String sr);
	
	@Query(value="select * from gate_in_out where company_id=:cid and branch_id=:bid and erp_doc_ref_no=:sr and doc_ref_no=:doc",nativeQuery=true)
	List<Gate_In_Out> findbysr1(@Param("cid") String cid,@Param("bid") String bid,@Param("sr") String sr,@Param("doc") String doc);

     @Query(value="select count(sr_no) from gate_in_out where company_id=:cid and branch_id=:bid and erp_doc_ref_no=:sr and doc_ref_no=:doc",nativeQuery=true)
    int findbysr2(@Param("cid") String cid,@Param("bid") String bid,@Param("sr") String sr,@Param("doc") String doc);
     
     
     @Transactional
 	@Modifying
 	@Query(value="update gate_in_out set sr_no=:ser where erp_doc_ref_no=:erp",nativeQuery=true)
 	public void updateData(@Param("ser") String nsdl,@Param("erp") String statusdoc);
}
