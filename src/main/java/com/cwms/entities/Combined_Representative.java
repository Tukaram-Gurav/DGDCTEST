package com.cwms.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(Combined_RepresntativeId.class)
@Table(name="Combined_Representatives")
public class Combined_Representative {
    @Id
    @Column(name="Company_Id",length=6)
	private String companyId;
    @Id
    @Column(name="Branch_Id",length=6)
	private String branchId;
    @Id
    @Column(name="Erp_Doc_Ref_Id",length=6)
	private String erpDocRefId;
    @Id
    @Column(name="Party_Id",length=6)
	private String partyId;
    @Column(name="Group_Name",length=50)
	private String groupName;
    @Column(name="Status",length=1)
	private String status;
	public Combined_Representative() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Combined_Representative(String companyId, String branchId, String erpDocRefId, String partyId,
			String groupName, String status) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.erpDocRefId = erpDocRefId;
		this.partyId = partyId;
		this.groupName = groupName;
		this.status = status;
	}
	
	

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
    
}
