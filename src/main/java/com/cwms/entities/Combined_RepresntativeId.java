package com.cwms.entities;

import java.io.Serializable;

public class Combined_RepresntativeId implements Serializable {

	private String companyId;
	private String branchId;
	private String erpDocRefId;
	private String partyId;
	
	public Combined_RepresntativeId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Combined_RepresntativeId(String companyId, String branchId, String erpDocRefId, String partyId) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.erpDocRefId = erpDocRefId;
		this.partyId = partyId;
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

	public String getErpDocRefId() {
		return erpDocRefId;
	}

	public void setErpDocRefId(String erpDocRefId) {
		this.erpDocRefId = erpDocRefId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	
	
	
	
}
