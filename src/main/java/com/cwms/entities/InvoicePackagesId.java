package com.cwms.entities;

import java.io.Serializable;


public class InvoicePackagesId implements Serializable
{

	private Long SrNo;	
	private String companyId;	
	private String branchId;	
	private String invoiceNO;	
	public String billNO;
	private String partyId;
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
	public String getInvoiceNO() {
		return invoiceNO;
	}
	public void setInvoiceNO(String invoiceNO) {
		this.invoiceNO = invoiceNO;
	}
	public String getBillNO() {
		return billNO;
	}
	public void setBillNO(String billNO) {
		this.billNO = billNO;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public InvoicePackagesId() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InvoicePackagesId(Long srNo, String companyId, String branchId, String invoiceNO, String billNO,
			String partyId) {
		super();
		SrNo = srNo;
		this.companyId = companyId;
		this.branchId = branchId;
		this.invoiceNO = invoiceNO;
		this.billNO = billNO;
		this.partyId = partyId;
	}
	
	
	

	
	
	
	
	
	
	
	
	
}
