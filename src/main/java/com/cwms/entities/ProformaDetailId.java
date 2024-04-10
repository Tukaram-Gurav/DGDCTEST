package com.cwms.entities;
import java.io.Serializable;

public class ProformaDetailId implements Serializable
{
	 private Long SrlNo;
	private String companyId;
	private String branchId;
	private String proformaNo;
	private String serviceId;
	private String partyId;
	
	public ProformaDetailId() {
		super();
		// TODO Auto-generated constructor stub
	}




	public Long getSrlNo() {
		return SrlNo;
	}




	public void setSrlNo(Long srlNo) {
		SrlNo = srlNo;
	}







	public ProformaDetailId(Long srlNo, String companyId, String branchId, String proformaNo, String serviceId,
			String partyId) {
		super();
		SrlNo = srlNo;
		this.companyId = companyId;
		this.branchId = branchId;
		this.proformaNo = proformaNo;
		this.serviceId = serviceId;
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




	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	
	

}
