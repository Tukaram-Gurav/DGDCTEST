package com.cwms.entities;

import java.io.Serializable;

public class ExportSHBHistoryId implements Serializable{
	private String companyId;

	private String branchId;

	private String sbNo;

	private String erNo;
	
	private Long HistoryId;

	public ExportSHBHistoryId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExportSHBHistoryId(String companyId, String branchId, String sbNo, String erNo, Long historyId) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.sbNo = sbNo;
		this.erNo = erNo;
		HistoryId = historyId;
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

	public Long getHistoryId() {
		return HistoryId;
	}

	public void setHistoryId(Long historyId) {
		HistoryId = historyId;
	}
	
	
}
