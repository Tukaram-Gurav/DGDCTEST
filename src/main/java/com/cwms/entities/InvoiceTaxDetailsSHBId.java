package com.cwms.entities;

import java.io.Serializable;
import java.util.Objects;

public class InvoiceTaxDetailsSHBId implements Serializable
{
	private Long SrNo;
	private String companyId;
	private String branchId;
	private String invoiceNO;
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
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public InvoiceTaxDetailsSHBId() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InvoiceTaxDetailsSHBId(Long srNo, String companyId, String branchId, String invoiceNO, String partyId) {
		super();
		SrNo = srNo;
		this.companyId = companyId;
		this.branchId = branchId;
		this.invoiceNO = invoiceNO;
		this.partyId = partyId;
	}
	@Override
	public int hashCode() {
		return Objects.hash(SrNo, branchId, companyId, invoiceNO, partyId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceTaxDetailsSHBId other = (InvoiceTaxDetailsSHBId) obj;
		return Objects.equals(SrNo, other.SrNo) && Objects.equals(branchId, other.branchId)
				&& Objects.equals(companyId, other.companyId) && Objects.equals(invoiceNO, other.invoiceNO)
				&& Objects.equals(partyId, other.partyId);
	}
	
	
}
