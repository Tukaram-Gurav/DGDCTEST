package com.cwms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.ExportAutoIncrement;

public interface ExportAutoIncrementRepo extends JpaRepository<ExportAutoIncrement, Integer> {

	   @Query(value="SELECT i.exportId FROM ExportAutoIncrement i WHERE i.sbNo = :sb AND i.sbRequestId = :req")
	    int getId(@Param("sb") String sb, @Param("req") String req);
}
