package com.cwms.entities;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "CFS_Tarrif_Extended_Limit")
@IdClass(CFSTarrifExtendedValuesId.class)
public class CFSTarrifExtendedValues 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SrNo")
	private Long SrNo;
	
	@Id
	@Column(name = "Srl_No", nullable = false)
	private int srlNo;
	
	@Id
    @Column(name = "Company_Id",length = 6)
    private String companyId;

    @Id
    @Column(name = "Branch_Id",length = 6)
    private String branchId;

    @Id
	@Column(name = "CFS_Tariff_No", length = 10, nullable = false)
	private String cfsTariffNo;

	@Id
	@Column(name = "CFS_Amnd_No", length = 3, nullable = false)
	private String cfsAmndNo;    
	
	@Id
	@Column(name = "Service_Id", length = 10, nullable = false)
	private String serviceId;
	
	@Id
	@Column(name = "Party_Id", length = 6)
	private String partyId;	
	
	@Column(name = "After_Range", length = 6)
	private double afterRange;
	
	@Column(name = "Upto_Range", length = 6)
	private double uptoRange;
	
	@Column(name = "Extended_Range", length = 6)
	private double extendedRange;
	
	@Column(name = "Amount_Addition", length = 6)
	private double amountAddition;
	

	@Column(name = "Created_By", length = 30)
	private String createdBy;


	@Column(name = "Created_Date")
	private Date createdDate;
	
	@Column(name = "Edited_By", length = 30)
	private String editedBy;


	@Column(name = "Edited_Date")
	private Date editedDate;

	@Column(name = "Approved_By", length = 30)
	private String approvedBy;


	@Column(name = "Approved_Date")
	private Date approvedDate;
	
	
	@Column(name = "Status", length = 1, nullable = false)
	private String status;

	
	public CFSTarrifExtendedValues() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	public int getSrlNo() {
		return srlNo;
	}




	public void setSrlNo(int srlNo) {
		this.srlNo = srlNo;
	}




	public double getAfterRange() {
		return afterRange;
	}




	public void setAfterRange(double afterRange) {
		this.afterRange = afterRange;
	}




	public double getUptoRange() {
		return uptoRange;
	}




	public void setUptoRange(double uptoRange) {
		this.uptoRange = uptoRange;
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


	public String getCfsTariffNo() {
		return cfsTariffNo;
	}


	public void setCfsTariffNo(String cfsTariffNo) {
		this.cfsTariffNo = cfsTariffNo;
	}


	public String getCfsAmndNo() {
		return cfsAmndNo;
	}


	public void setCfsAmndNo(String cfsAmndNo) {
		this.cfsAmndNo = cfsAmndNo;
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


	public double getExtendedRange() {
		return extendedRange;
	}


	public void setExtendedRange(double extendedRange) {
		this.extendedRange = extendedRange;
	}


	public double getAmountAddition() {
		return amountAddition;
	}


	public void setAmountAddition(double amountAddition) {
		this.amountAddition = amountAddition;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public String getEditedBy() {
		return editedBy;
	}


	public void setEditedBy(String editedBy) {
		this.editedBy = editedBy;
	}


	public Date getEditedDate() {
		return editedDate;
	}


	public void setEditedDate(Date editedDate) {
		this.editedDate = editedDate;
	}


	public String getApprovedBy() {
		return approvedBy;
	}


	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}


	public Date getApprovedDate() {
		return approvedDate;
	}


	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Long getSrNo() {
		return SrNo;
	}


	public void setSrNo(Long srNo) {
		SrNo = srNo;
	}


	public CFSTarrifExtendedValues(Long srNo, String companyId, String branchId, String cfsTariffNo, String cfsAmndNo,
			String serviceId, String partyId, double extendedRange, double amountAddition, String createdBy,
			Date createdDate, String editedBy, Date editedDate, String approvedBy, Date approvedDate, String status) {
		super();
		SrNo = srNo;
		this.companyId = companyId;
		this.branchId = branchId;
		this.cfsTariffNo = cfsTariffNo;
		this.cfsAmndNo = cfsAmndNo;
		this.serviceId = serviceId;
		this.partyId = partyId;
		this.extendedRange = extendedRange;
		this.amountAddition = amountAddition;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.editedBy = editedBy;
		this.editedDate = editedDate;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
		this.status = status;
	}
	
	
	
	
}
