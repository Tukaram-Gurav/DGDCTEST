package com.cwms.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "exportshb_history")
@IdClass(ExportSHBHistoryId.class)
public class ExportSHBHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "exporthistory_sequence", allocationSize = 1)
	private Long HistoryId;

	@Id
	@Column(name = "Company_Id", length = 6)
	private String companyId;

	@Id
	@Column(name = "Branch_Id", length = 6)
	private String branchId;

	@Id
	@Column(name = "SB_Number", length = 15)
	private String sbNo;

	@Id
	@Column(name = "ER_No", length = 20)
	private String erNo;
	
	
	@Column(name = "TransacrtionDate")
	private Date transport_Date;

	@Column(name = "updatedBy", length = 20)
	private String updatedBy;

	@Column(name = "oldStatus", length = 30)
	private String oldStatus;
	@Column(name = "newStatus", length = 30)
	private String newStatus;
	@Column(name = "remark", length = 50)
	private String remark;
	public ExportSHBHistory() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ExportSHBHistory(Long historyId, String companyId, String branchId, String sbNo, String erNo,
			Date transport_Date, String updatedBy, String oldStatus, String newStatus, String remark) {
		super();
		HistoryId = historyId;
		this.companyId = companyId;
		this.branchId = branchId;
		this.sbNo = sbNo;
		this.erNo = erNo;
		this.transport_Date = transport_Date;
		this.updatedBy = updatedBy;
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
		this.remark = remark;
	}
	public Long getHistoryId() {
		return HistoryId;
	}
	public void setHistoryId(Long historyId) {
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
	public Date getTransport_Date() {
		return transport_Date;
	}
	public void setTransport_Date(Date transport_Date) {
		this.transport_Date = transport_Date;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getOldStatus() {
		return oldStatus;
	}
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}
	public String getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
