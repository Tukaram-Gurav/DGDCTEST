package com.cwms.entities;

import java.io.Serializable;
import java.util.Date;

public class StockDetentionIds implements Serializable
{	
	private Long SrNo;
	private String companyId;
	private String branchId;
	private Date stockDate;
	
	public Long getSrNo() {
		return SrNo;
	}
	public void setSrNo(Long srNo) {
		SrNo = srNo;
	}
	public Date getStockDate() {
		return stockDate;
	}
	public void setStockDate(Date stockDate) {
		this.stockDate = stockDate;
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
	public StockDetentionIds() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StockDetentionIds(Long srNo, String companyId, String branchId, Date stockDate) {
		super();
		SrNo = srNo;
		this.companyId = companyId;
		this.branchId = branchId;
		this.stockDate = stockDate;
	}

}
