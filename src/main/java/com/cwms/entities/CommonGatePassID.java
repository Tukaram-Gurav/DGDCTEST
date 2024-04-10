package com.cwms.entities;

import java.io.Serializable;

public class CommonGatePassID implements Serializable
{
	private String companyId;
	private String branchId;
	private String commonId;
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
	public String getCommonId() {
		return commonId;
	}
	public void setCommonId(String commonId) {
		this.commonId = commonId;
	}
	public CommonGatePassID() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CommonGatePassID(String companyId, String branchId, String commonId) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.commonId = commonId;
	}
	
	
}
