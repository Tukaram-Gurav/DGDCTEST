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
@Table(name="cfssrvanx_SHB")
@IdClass(InvoiceTaxDetailsSHBId.class)
public class InvoiceTaxDetailsSHB {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SrNo")
	private Long SrNo;

	@Id
	@Column(name = "Company_Id", length = 6, nullable = false)
	private String companyId;

	@Id
	@Column(name = "Branch_Id", length = 6, nullable = false)
	private String branchId;

	@Id
	@Column(name = "Invoice_No", length = 20, nullable = false)
	private String invoiceNO;
	
	@Column(name = "Bill_No", length = 20, nullable = false)
	public String billNO;	

	@Id
	@Column(name = "Party_Id", length = 10, nullable = false)
	private String partyId;

	@Column(name = "Invoice_Date", nullable = false)
	private Date invoiceDate;
	
	@Column(name = "TYPE", length = 6)
	private String type;
	
	@Column(name = "SIR_NO", length = 20)
    private String sirNo;
	
	@Column(name = "MAWB", length = 20)
    private String mawb;
	
	@Column(name = "HAWB", length = 20)
    private String hawb;	
	
	@Column(name = "CHA", length = 20)
    private String chaName;
	
	@Column(name = "CONSOLE", length = 20)
    private String consoleName;
	
	@Column(name = "IN_Date")
    private Date inDate;
	
	@Column(name = "OUT_Date")
    private Date outDate;
	
	@Column(name = "NOP",length=3)
    private int nop;
	
	@Column(name = "NO_OF_PACKETES",length=3)
    private int noPackates;
	
	@Column(name = "DOCUMENT_CHARGES")
    private BigDecimal documentCharges;
	
	@Column(name = "RATE_BY_PRICE")
    private BigDecimal rateByPrice;
	
	@Column(name = "PRICE")
    private BigDecimal price;
	
	@Column(name = "RATE_BY_WEIGHT")
    private BigDecimal rateByWeight;
	
	@Column(name = "WEIGHT")
    private BigDecimal weight;
	
	@Column(name = "DEMURAGES_CHARGES")
    private BigDecimal demurageCharges;
	
	@Column(name = "STOCK_DAYS",length=3)
    private long stockDays;	
	
	@Column(name = "Tax_Amt", scale = 2)
	private BigDecimal taxAmount;

	@Column(name = "Bill_Amt", scale = 2)
	private BigDecimal billAmount;

	@Column(name = "Total_Invoice_Amt", scale = 2)
	private BigDecimal totalInvoiceAmount;

	@Column(name = "Status", nullable = false, length = 1)
	private String status;

	@Column(name = "Created_By", nullable = false, length = 30)
	private String createdBy;

	@Column(name = "Created_Date", nullable = false)
	private Date createdDate;	
	
	public String getConsoleName() {
		return consoleName;
	}

	public void setConsoleName(String consoleName) {
		this.consoleName = consoleName;
	}

	public String getChaName() {
		return chaName;
	}

	public void setChaName(String chaName) {
		this.chaName = chaName;
	}	

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

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSirNo() {
		return sirNo;
	}

	public void setSirNo(String sirNo) {
		this.sirNo = sirNo;
	}

	public String getMawb() {
		return mawb;
	}

	public void setMawb(String mawb) {
		this.mawb = mawb;
	}

	public String getHawb() {
		return hawb;
	}

	public void setHawb(String hawb) {
		this.hawb = hawb;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public int getNop() {
		return nop;
	}

	public void setNop(int nop) {
		this.nop = nop;
	}

	public int getNoPackates() {
		return noPackates;
	}

	public void setNoPackates(int noPackates) {
		this.noPackates = noPackates;
	}

	public BigDecimal getDocumentCharges() {
		return documentCharges;
	}

	public void setDocumentCharges(BigDecimal documentCharges) {
		this.documentCharges = documentCharges;
	}

	public BigDecimal getRateByPrice() {
		return rateByPrice;
	}

	public void setRateByPrice(BigDecimal rateByPrice) {
		this.rateByPrice = rateByPrice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getRateByWeight() {
		return rateByWeight;
	}

	public void setRateByWeight(BigDecimal rateByWeight) {
		this.rateByWeight = rateByWeight;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getDemurageCharges() {
		return demurageCharges;
	}

	public void setDemurageCharges(BigDecimal demurageCharges) {
		this.demurageCharges = demurageCharges;
	}

	public long getStockDays() {
		return stockDays;
	}

	public void setStockDays(long stockDays) {
		this.stockDays = stockDays;
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

	public InvoiceTaxDetailsSHB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvoiceTaxDetailsSHB(Long srNo, String companyId, String branchId, String invoiceNO, String billNO,
			String partyId, Date invoiceDate, String type, String sirNo, String mawb, String hawb, Date inDate,
			Date outDate, int nop, int noPackates, BigDecimal documentCharges, BigDecimal rateByPrice, BigDecimal price,
			BigDecimal rateByWeight, BigDecimal weight, BigDecimal demurageCharges, long stockDays,
			BigDecimal taxAmount, BigDecimal billAmount, BigDecimal totalInvoiceAmount, String status, String createdBy,
			Date createdDate) {
		super();
		SrNo = srNo;
		this.companyId = companyId;
		this.branchId = branchId;
		this.invoiceNO = invoiceNO;
		this.billNO = billNO;
		this.partyId = partyId;
		this.invoiceDate = invoiceDate;
		this.type = type;
		this.sirNo = sirNo;
		this.mawb = mawb;
		this.hawb = hawb;
		this.inDate = inDate;
		this.outDate = outDate;
		this.nop = nop;
		this.noPackates = noPackates;
		this.documentCharges = documentCharges;
		this.rateByPrice = rateByPrice;
		this.price = price;
		this.rateByWeight = rateByWeight;
		this.weight = weight;
		this.demurageCharges = demurageCharges;
		this.stockDays = stockDays;
		this.taxAmount = taxAmount;
		this.billAmount = billAmount;
		this.totalInvoiceAmount = totalInvoiceAmount;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}

	
	
	
	
	
}
