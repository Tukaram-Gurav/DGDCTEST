package com.cwms.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class ExportSHBId implements Serializable {

	private String companyId;

	private String branchId;

	private String sbNo;

	private String erNo;

	public ExportSHBId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExportSHBId(String companyId, String branchId, String sbNo, String erNo) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.sbNo = sbNo;
		this.erNo = erNo;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getSbNo() {
		return sbNo;
	}

	public void setSbNo(String sbNo) {
		this.sbNo = sbNo;
	}

	public String getErNo() {
		return erNo;
	}

	public void setErNo(String erNo) {
		this.erNo = erNo;
	}
	
	
	
}
