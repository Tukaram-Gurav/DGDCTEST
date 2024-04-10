package com.cwms.entities;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "fintransdtl")
@IdClass(FinTransDtlId.class)
public class FinTransDtl {

	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long srno;
	
	@Column(name = "Company_Id",length=6)
	private String companyId;
	
	@Id
	@Column(name = "Branch_Id",length=6)
	private String branchId;
	
	@Id
	@Column(name = "Invoice_No",length=10)
	private String invoiceNo;
		
	@Id
	@Column(name = "Trans_Id",length=10)
	private String transId;
	
    @Column(name = "Trans_Date")
    private Date transDate;

    @Column(name = "Sr_No")
    private int srNo;

    @Column(name = "Party_Id")
    private String partyId;
   
    @Column(name = "Invoice_Date")
    private Date invoiceDate;

    @Column(name = "Invoice_Amt")
    private BigDecimal invoiceAmt;

    @Column(name = "Receipt_Amt")
    private BigDecimal receiptAmt;

    @Column(name = "Created_By")
    private String createdBy;

    @Column(name = "Created_Date")
    private Date createdDate;

    @Column(name = "Edited_By")
    private String editedBy;

    @Column(name = "Edited_Date")
    private Date editedDate;

    @Column(name = "Approved_By")
    private String approvedBy;

    @Column(name = "Approved_Date")
    private Date approvedDate;

	public FinTransDtl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FinTransDtl(String companyId, String branchId, String invoiceNo, String transId, Date transDate, int srNo,
			String partyId, Date invoiceDate, BigDecimal invoiceAmt, BigDecimal receiptAmt, String createdBy,
			Date createdDate, String editedBy, Date editedDate, String approvedBy, Date approvedDate) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.invoiceNo = invoiceNo;
		this.transId = transId;
		this.transDate = transDate;
		this.srNo = srNo;
		this.partyId = partyId;
		this.invoiceDate = invoiceDate;
		this.invoiceAmt = invoiceAmt;
		this.receiptAmt = receiptAmt;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.editedBy = editedBy;
		this.editedDate = editedDate;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
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

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public BigDecimal getInvoiceAmt() {
		return invoiceAmt;
	}

	public void setInvoiceAmt(BigDecimal invoiceAmt) {
		this.invoiceAmt = invoiceAmt;
	}

	public BigDecimal getReceiptAmt() {
		return receiptAmt;
	}

	public void setReceiptAmt(BigDecimal receiptAmt) {
		this.receiptAmt = receiptAmt;
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

    
    
    
    
}
