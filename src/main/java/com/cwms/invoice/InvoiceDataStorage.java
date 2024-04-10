package com.cwms.invoice;

import java.math.BigDecimal;
import java.util.Date;


public class InvoiceDataStorage 
{
   
    private String companyId;
    private String branchId;
    private String importerId;
    private String importerName;
    private String type;
    private String sirNo;
    private String mawb;
    private String hawb;
    private Date inDate;
    private Date outDate;
    private int nop;
    private int noPackates;
    private BigDecimal documentCharges;
    private BigDecimal rateByPrice;
    private BigDecimal price;
    private BigDecimal rateByWeight;
    private BigDecimal weight;    
    private BigDecimal demurageCharges;
    private long stockDays;
    private BigDecimal billAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalInvoiceAmount;
    private String chaName;
    private String consoleName;
    
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getImporterId() {
		return importerId;
	}
	public void setImporterId(String importerId) {
		this.importerId = importerId;
	}
	public String getImporterName() {
		return importerName;
	}
	public void setImporterName(String importerName) {
		this.importerName = importerName;
	}
	public String getSirNo() {
		return sirNo;
	}
	public void setSirNo(String sirNo) {
		this.sirNo = sirNo;
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
	public BigDecimal getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	public BigDecimal getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}
	public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}
	public InvoiceDataStorage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InvoiceDataStorage(String companyId, String branchId, String importerId, String importerName, String sirNo,
			String mawb, String hawb, Date inDate, Date outDate, int nop, int noPackates, BigDecimal documentCharges,
			BigDecimal rateByPrice, BigDecimal price, BigDecimal rateByWeight, BigDecimal weight,
			BigDecimal demurageCharges, long stockDays, BigDecimal billAmount, BigDecimal taxAmount,
			BigDecimal totalInvoiceAmount, String chaName, String consoleName) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.importerId = importerId;
		this.importerName = importerName;
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
		this.billAmount = billAmount;
		this.taxAmount = taxAmount;
		this.totalInvoiceAmount = totalInvoiceAmount;
		this.chaName = chaName;
		this.consoleName = consoleName;
	}
	
	  
}
