package com.cwms.entities;
import java.io.Serializable;

public class ProformaMainId implements Serializable
{
	private String companyId;
	private String branchId;
	private String proformaNo;
	private String partyId;
	

	
	public ProformaMainId() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getPartyId() {
		return partyId;
	}



	public void setPartyId(String partyId) {
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



	public String getProformaNo() {
		return proformaNo;
	}



	public void setProformaNo(String proformaNo) {
		this.proformaNo = proformaNo;
	}



	public ProformaMainId(String companyId, String branchId, String proformaNo, String partyId) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.proformaNo = proformaNo;
		this.partyId = partyId;
	}





	
	
	

}
