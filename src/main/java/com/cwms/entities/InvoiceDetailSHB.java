package com.cwms.entities;

import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;


@Entity
@Table(name = "cfinvsrvdtl_SHB")
@IdClass(InvoiceDetailSHBId.class)
public class InvoiceDetailSHB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SrlNo")
	private Long SrlNo;

	@Id
	@Column(name = "Company_Id", length = 6, nullable = false)
	private String companyId;

	@Id
	@Column(name = "Branch_Id", length = 6, nullable = false)
	private String branchId;

	@Id
	@Column(name = "Invoice_No", length = 6, nullable = false)
	private String invoiceNO;

	@Id
	@Column(name = "Party_Id", length = 10, nullable = false)
	private String partyId;

	@Id
	@Column(name = "Service_Id", length = 6, nullable = false)
	private String serviceId;
	
	@Column(name = "SIR_NO", length = 20)
    private String sirNo;
	
	@Column(name = "Invoice_Date", nullable = false)
	private Date invoiceDate;

	@Column(name = "Execution_Unit", nullable = false, length = 40)
	private String executionUnit;

	
	@Column(name = "service_unit1", length = 40)
	private String serviceUnit1;

	@Column(name = "Execution_Unit1", length = 40)
	private String executionUnit1;

	@Column(name = "Rate")
	private BigDecimal rate;

	@Column(name = "Currency_Id", nullable = false, length = 6)
	private String currencyId;

	@Column(name = "Tax_Perc")
	private BigDecimal taxPercentage;

	@Column(name = "Tax_Amt")
	private BigDecimal taxAmount;

	@Column(name = "Bill_Amt")
	private BigDecimal billAmount;

	@Column(name = "Total_Invoice_Amt")
	private BigDecimal totalInvoiceAmount;

	@Column(name = "Status", nullable = false, length = 1)
	private String status;

	@Column(name = "Created_By", nullable = false, length = 30)
	private String createdBy;

	@Column(name = "Created_Date", nullable = false)
	private Date createdDate;

	@Column(name = "Edited_By", length = 30)
	private String editedBy;

	@Column(name = "Edited_Date", nullable = false)
	private Date editedDate;

	@Column(name = "Approved_By", length = 30)
	private String approvedBy;

	@Column(name = "Approved_Date")
	private Date approvedDate;

	public Long getSrlNo() {
		return SrlNo;
	}

	public void setSrlNo(Long srlNo) {
		SrlNo = srlNo;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSirNo() {
		return sirNo;
	}

	public void setSirNo(String sirNo) {
		this.sirNo = sirNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getExecutionUnit() {
		return executionUnit;
	}

	public void setExecutionUnit(String executionUnit) {
		this.executionUnit = executionUnit;
	}

	public String getServiceUnit1() {
		return serviceUnit1;
	}

	public void setServiceUnit1(String serviceUnit1) {
		this.serviceUnit1 = serviceUnit1;
	}

	public String getExecutionUnit1() {
		return executionUnit1;
	}

	public void setExecutionUnit1(String executionUnit1) {
		this.executionUnit1 = executionUnit1;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(BigDecimal taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public BigDecimal getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}

	public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public InvoiceDetailSHB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvoiceDetailSHB(Long srlNo, String companyId, String branchId, String invoiceNO, String partyId,
			String serviceId, String sirNo, Date invoiceDate, String executionUnit, String serviceUnit1,
			String executionUnit1, BigDecimal rate, String currencyId, BigDecimal taxPercentage, BigDecimal taxAmount,
			BigDecimal billAmount, BigDecimal totalInvoiceAmount, String status, String createdBy, Date createdDate,
			String editedBy, Date editedDate, String approvedBy, Date approvedDate) {
		super();
		SrlNo = srlNo;
		this.companyId = companyId;
		this.branchId = branchId;
		this.invoiceNO = invoiceNO;
		this.partyId = partyId;
		this.serviceId = serviceId;
		this.sirNo = sirNo;
		this.invoiceDate = invoiceDate;
		this.executionUnit = executionUnit;
		this.serviceUnit1 = serviceUnit1;
		this.executionUnit1 = executionUnit1;
		this.rate = rate;
		this.currencyId = currencyId;
		this.taxPercentage = taxPercentage;
		this.taxAmount = taxAmount;
		this.billAmount = billAmount;
		this.totalInvoiceAmount = totalInvoiceAmount;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.editedBy = editedBy;
		this.editedDate = editedDate;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
	}

	public InvoiceDetailSHB(String companyId, String branchId, String partyId, String serviceId, Date invoiceDate,
			String executionUnit, String serviceUnit1, String executionUnit1, BigDecimal rate, String currencyId,
			BigDecimal taxPercentage, BigDecimal taxAmount, BigDecimal billAmount, BigDecimal totalInvoiceAmount,
			String status, String createdBy, Date createdDate, String editedBy, Date editedDate, String approvedBy,
			Date approvedDate) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.partyId = partyId;
		this.serviceId = serviceId;
		this.invoiceDate = invoiceDate;
		this.executionUnit = executionUnit;
		this.serviceUnit1 = serviceUnit1;
		this.executionUnit1 = executionUnit1;
		this.rate = rate;
		this.currencyId = currencyId;
		this.taxPercentage = taxPercentage;
		this.taxAmount = taxAmount;
		this.billAmount = billAmount;
		this.totalInvoiceAmount = totalInvoiceAmount;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.editedBy = editedBy;
		this.editedDate = editedDate;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
	}

	
	
	

}
