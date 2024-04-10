package com.cwms.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="Export_Auto_Increment_Id")
public class ExportAutoIncrement {

	  @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
	    @SequenceGenerator(name = "sequence_generator", sequenceName = "export_sequence", allocationSize = 1)
	public int exportId;
	public String sbNo;
	public String sbRequestId;
	public ExportAutoIncrement() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ExportAutoIncrement(int exportId, String sbNo, String sbRequestId) {
		super();
		this.exportId = exportId;
		this.sbNo = sbNo;
		this.sbRequestId = sbRequestId;
	}
	public int getExportId() {
		return exportId;
	}
	public void setExportId(int exportId) {
		this.exportId = exportId;
	}
	public String getSbNo() {
		return sbNo;
	}
	public void setSbNo(String sbNo) {
		this.sbNo = sbNo;
	}
	public String getSbRequestId() {
		return sbRequestId;
	}
	public void setSbRequestId(String sbRequestId) {
		this.sbRequestId = sbRequestId;
	}
	
	
	
	
	
	
	
}
