package com.cwms.entities;

import java.io.Serializable;

public class ProformaTaxDetailsId implements Serializable
{
	private Long SrNo;
	private String companyId;
	private String branchId;
	private String proformaNo;
	private String partyId;
	
	public ProformaTaxDetailsId() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public ProformaTaxDetailsId(Long srNo, String companyId, String branchId, String proformaNo, String partyId) {
		super();
		SrNo = srNo;
		this.companyId = companyId;
		this.branchId = branchId;
		this.proformaNo = proformaNo;
		this.partyId = partyId;
	}




	public String getProformaNo() {
		return proformaNo;
	}




	public void setProformaNo(String proformaNo) {
		this.proformaNo = proformaNo;
	}




	public Long getSrNo() {
		return SrNo;
	}
	public void setSrNo(Long srNo) {
		SrNo = srNo;
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
	
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
		
}
