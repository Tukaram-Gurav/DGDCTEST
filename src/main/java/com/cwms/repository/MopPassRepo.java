package com.cwms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.MOPpass;

public interface MopPassRepo extends JpaRepository<MOPpass, String> {

	@Query(value="select * from mop_pass where created_date=:date",nativeQuery=true)
	List<MOPpass> findbydate(@Param("date") Date date);
	
	@Query(value="select * from mop_pass",nativeQuery=true)
	List<MOPpass> getall();
}
