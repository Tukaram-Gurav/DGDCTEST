package com.cwms.entities;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "fintrans")
@IdClass(FinTransId.class)
public class FinTrans {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long srno;

    @Id
    @Column(name = "Branch_Id" ,length=6)
    private String branchId;

    @Id
    @Column(name = "Company_Id",length=6)
    private String companyId;

    @Id
    @Column(name = "Doc_Type",length=15)
    private String docType;

    @Id
    @Column(name = "Trans_Id",length=10)
    private String transId;
	
	@Column(name = "Trans_Date")
	private Date transDate;
	
	@Column(name = "Party_Id",length=10)
	private String partyId;

	@Column(name = "Payment_Mode")
	private String paymentMode;

	@Column(name = "Cheque_No")
	private String chequeNo;

	@Column(name = "Cheque_Date")
	private Date chequeDate;

	@Column(name = "Bank_Name")
	private String bankName;

	@Column(name = "Transaction_No")
	private String transactionNo;

	@Column(name = "Transaction_Date")
	private Date transactionDate;

	@Column(name = "Transaction_Amt")
	private double transactionAmt;
	
	@Column(name = "Trans_Bank_Name")
	private String transbankName;

	@Column(name = "Currency")
	private String currency;

	@Column(name = "Receipt_Amt")
	private double receiptAmt;

	@Column(name = "Narration")
	private String narration;

	@Column(name = "Cleared_Amt")
	private double clearedAmt;

	@Column(name = "Adv_Trans_Id")
	private String advTransId;

	@Column(name = "Adv_Trans_Date")
	private Date advTransDate;

	@Column(name = "Adv_Flag")
	private String advFlag;

	@Column(name = "Bal_Adv_Amt")
	private double balAdvAmt;

	@Column(name = "Adv_Amt")
	private double advAmt;

	@Column(name = "Bank_Recon_Flag")
	private String bankReconFlag;

	@Column(name = "Bank_Recon_Date")
	private Date bankReconDate;

	@Column(name = "Bank_Recon_Amt")
	private double bankReconAmt;

	@Column(name = "TDS_Percentage")
	private double tdsPercentage;

	@Column(name = "TDS_Amt")
	private double tdsAmt;

	@Column(name = "TDS_Status")
	private String tdsStatus;

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

	public FinTrans() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	public Long getSrno() {
		return srno;
	}




	public void setSrno(Long srno) {
		this.srno = srno;
	}




	public String getTransbankName() {
		return transbankName;
	}




	public void setTransbankName(String transbankName) {
		this.transbankName = transbankName;
	}




	public FinTrans(String companyId, String branchId, String transId, String docType, Date transDate, String partyId,
			String paymentMode, String chequeNo, Date chequeDate, String bankName, String transactionNo,
			Date transactionDate, double transactionAmt, String transbankName, String currency, double receiptAmt,
			String narration, double clearedAmt, String advTransId, Date advTransDate, String advFlag, double balAdvAmt,
			double advAmt, String bankReconFlag, Date bankReconDate, double bankReconAmt, double tdsPercentage,
			double tdsAmt, String tdsStatus, String createdBy, Date createdDate, String editedBy, Date editedDate,
			String approvedBy, Date approvedDate) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.transId = transId;
		this.docType = docType;
		this.transDate = transDate;
		this.partyId = partyId;
		this.paymentMode = paymentMode;
		this.chequeNo = chequeNo;
		this.chequeDate = chequeDate;
		this.bankName = bankName;
		this.transactionNo = transactionNo;
		this.transactionDate = transactionDate;
		this.transactionAmt = transactionAmt;
		this.transbankName = transbankName;
		this.currency = currency;
		this.receiptAmt = receiptAmt;
		this.narration = narration;
		this.clearedAmt = clearedAmt;
		this.advTransId = advTransId;
		this.advTransDate = advTransDate;
		this.advFlag = advFlag;
		this.balAdvAmt = balAdvAmt;
		this.advAmt = advAmt;
		this.bankReconFlag = bankReconFlag;
		this.bankReconDate = bankReconDate;
		this.bankReconAmt = bankReconAmt;
		this.tdsPercentage = tdsPercentage;
		this.tdsAmt = tdsAmt;
		this.tdsStatus = tdsStatus;
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

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public Date getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getTransactionAmt() {
		return transactionAmt;
	}

	public void setTransactionAmt(double transactionAmt) {
		this.transactionAmt = transactionAmt;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getReceiptAmt() {
		return receiptAmt;
	}

	public void setReceiptAmt(double receiptAmt) {
		this.receiptAmt = receiptAmt;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public double getClearedAmt() {
		return clearedAmt;
	}

	public void setClearedAmt(double clearedAmt) {
		this.clearedAmt = clearedAmt;
	}

	public String getAdvTransId() {
		return advTransId;
	}

	public void setAdvTransId(String advTransId) {
		this.advTransId = advTransId;
	}

	public Date getAdvTransDate() {
		return advTransDate;
	}

	public void setAdvTransDate(Date advTransDate) {
		this.advTransDate = advTransDate;
	}

	public String getAdvFlag() {
		return advFlag;
	}

	public void setAdvFlag(String advFlag) {
		this.advFlag = advFlag;
	}

	public double getBalAdvAmt() {
		return balAdvAmt;
	}

	public void setBalAdvAmt(double balAdvAmt) {
		this.balAdvAmt = balAdvAmt;
	}

	public double getAdvAmt() {
		return advAmt;
	}

	public void setAdvAmt(double advAmt) {
		this.advAmt = advAmt;
	}

	public String getBankReconFlag() {
		return bankReconFlag;
	}

	public void setBankReconFlag(String bankReconFlag) {
		this.bankReconFlag = bankReconFlag;
	}

	public Date getBankReconDate() {
		return bankReconDate;
	}

	public void setBankReconDate(Date bankReconDate) {
		this.bankReconDate = bankReconDate;
	}

	public double getBankReconAmt() {
		return bankReconAmt;
	}

	public void setBankReconAmt(double bankReconAmt) {
		this.bankReconAmt = bankReconAmt;
	}

	public double getTdsPercentage() {
		return tdsPercentage;
	}

	public void setTdsPercentage(double tdsPercentage) {
		this.tdsPercentage = tdsPercentage;
	}

	public double getTdsAmt() {
		return tdsAmt;
	}

	public void setTdsAmt(double tdsAmt) {
		this.tdsAmt = tdsAmt;
	}

	public String getTdsStatus() {
		return tdsStatus;
	}

	public void setTdsStatus(String tdsStatus) {
		this.tdsStatus = tdsStatus;
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

	@Override
	public String toString() {
		return "FinTrans [srno=" + srno + ", branchId=" + branchId + ", companyId=" + companyId + ", docType=" + docType
				+ ", transId=" + transId + ", transDate=" + transDate + ", partyId=" + partyId + ", paymentMode="
				+ paymentMode + ", chequeNo=" + chequeNo + ", chequeDate=" + chequeDate + ", bankName=" + bankName
				+ ", transactionNo=" + transactionNo + ", transactionDate=" + transactionDate + ", transactionAmt="
				+ transactionAmt + ", TransbankName=" + transbankName + ", currency=" + currency + ", receiptAmt="
				+ receiptAmt + ", narration=" + narration + ", clearedAmt=" + clearedAmt + ", advTransId=" + advTransId
				+ ", advTransDate=" + advTransDate + ", advFlag=" + advFlag + ", balAdvAmt=" + balAdvAmt + ", advAmt="
				+ advAmt + ", bankReconFlag=" + bankReconFlag + ", bankReconDate=" + bankReconDate + ", bankReconAmt="
				+ bankReconAmt + ", tdsPercentage=" + tdsPercentage + ", tdsAmt=" + tdsAmt + ", tdsStatus=" + tdsStatus
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", editedBy=" + editedBy
				+ ", editedDate=" + editedDate + ", approvedBy=" + approvedBy + ", approvedDate=" + approvedDate + "]";
	}
	

}
