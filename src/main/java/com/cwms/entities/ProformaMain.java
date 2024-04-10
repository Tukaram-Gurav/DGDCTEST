package com.cwms.entities;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Percfinvsrv")
@IdClass(ProformaMainId.class)
public class ProformaMain {
	@Id
	@Column(name = "Company_Id", length = 6, nullable = false)
	private String companyId;

	@Id
	@Column(name = "Branch_Id", length = 6, nullable = false)
	private String branchId;
	
	@Id
	@Column(name = "Proforma_No", length = 6, nullable = false)
	public String proformaNo;	
	
	@Id
	@Column(name = "Party_Id", length = 6, nullable = false)
	private String partyId;

    @Column(name = "Proforma_Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date proformaDate;

    @Column(name = "Tariff_No", nullable = false, length = 10)
    private String tariffNo;

    @Column(name = "Tariff_Amnd_No", nullable = false, length = 3)
    private String tariffAmndNo;

    
    @Column(name = "Tax_Amt")
    private double taxAmount;

    @Column(name = "Bill_Amt")
    private double billAmount;

    @Column(name = "Total_Invoice_Amt")
    private double totalInvoiceAmount;

    @Column(name = "Receipt_Trans_Id", length = 10)
    private String receiptTransactionId;

    @Column(name = "Receipt_Trans_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiptTransactionDate;

    @Column(name = "Period_From", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodFrom;

    @Column(name = "Period_To", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodTo;

    
    @Column(name = "Comments" , length=500)
    private String comments;

    @Column(name = "Mail_Flag", length = 1)
    private String mailFlag;

    @Column(name = "IGST", length = 1)
    private String igst;

    @Column(name = "CGST", length = 1)
    private String cgst;

    @Column(name = "SGST", length = 1)
    private String sgst;
    
    @Column(name = "Extra_Taxes")
    private double extraTaxes;

    @Column(name = "Status", length = 1)
    private String status;

    @Column(name = "Created_By", length = 30)
    private String createdBy;

    @Column(name = "Created_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "Edited_By", length = 30)
    private String editedBy;

    @Column(name = "Edited_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editedDate;

    @Column(name = "Approved_By", length = 30)
    private String approvedBy;

    @Column(name = "Approved_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedDate;

      
    
	public String getProformaNo() {
		return proformaNo;
	}


	public void setProformaNo(String proformaNo) {
		this.proformaNo = proformaNo;
	}


	public Date getProformaDate() {
		return proformaDate;
	}


	public void setProformaDate(Date proformaDate) {
		this.proformaDate = proformaDate;
	}


	

	
	public ProformaMain() {
		super();
		// TODO Auto-generated constructor stub
	}


	public double getExtraTaxes() {
		return extraTaxes;
	}

	public void setExtraTaxes(double extraTaxes) {
		this.extraTaxes = extraTaxes;
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


	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	

	public String getTariffNo() {
		return tariffNo;
	}

	public void setTariffNo(String tariffNo) {
		this.tariffNo = tariffNo;
	}

	public String getTariffAmndNo() {
		return tariffAmndNo;
	}

	public void setTariffAmndNo(String tariffAmndNo) {
		this.tariffAmndNo = tariffAmndNo;
	}

	
	

	public double getTaxAmount() {
		return taxAmount;
	}






	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}






	public double getBillAmount() {
		return billAmount;
	}






	public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}






	public double getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}






	public void setTotalInvoiceAmount(double totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}






	public String getReceiptTransactionId() {
		return receiptTransactionId;
	}

	public void setReceiptTransactionId(String receiptTransactionId) {
		this.receiptTransactionId = receiptTransactionId;
	}

	public Date getReceiptTransactionDate() {
		return receiptTransactionDate;
	}

	public void setReceiptTransactionDate(Date receiptTransactionDate) {
		this.receiptTransactionDate = receiptTransactionDate;
	}

	public Date getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(Date periodFrom) {
		this.periodFrom = periodFrom;
	}

	public Date getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(Date periodTo) {
		this.periodTo = periodTo;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
	public String getMailFlag() {
		return mailFlag;
	}



	public void setMailFlag(String mailFlag) {
		this.mailFlag = mailFlag;
	}



	public String getIgst() {
		return igst;
	}



	public void setIgst(String igst) {
		this.igst = igst;
	}



	public String getCgst() {
		return cgst;
	}



	public void setCgst(String cgst) {
		this.cgst = cgst;
	}



	public String getSgst() {
		return sgst;
	}



	public void setSgst(String sgst) {
		this.sgst = sgst;
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

}