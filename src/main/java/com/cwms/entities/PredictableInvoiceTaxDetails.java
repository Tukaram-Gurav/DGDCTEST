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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name="Predictable_cfssrvanx")
@IdClass(PredictableInvoiceTaxDetailsId.class)
public class PredictableInvoiceTaxDetails 
{	

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
	@Column(name = "Predictable_Invoice_No", length = 12, nullable = false)
	private String predictableinvoiceNO;

	@Id
	@Column(name = "Party_Id", length = 6, nullable = false)
	private String partyId;

	@Column(name = "Invoice_Date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date invoiceDate;
	
	@Column(name = "Import_NoOfPackages" )
	private int importNoOfPackages;
	@Column(name = "Export_NoOfPackages" )
	private int exportNoOfPackages;
	@Column(name = "Total_Packages" )
	private int totalPackages;

	@Column(name = "Holiday_Rate")
	private double holidayRate;
	@Column(name = "ExportSc_Rate" )
	private double exportScRate;
	@Column(name = "ExportPc_Rate" )
	private double exportPcRate;
	@Column(name = "ExportHp_Rate" )
	private double exportHpRate;
	@Column(name = "Export_Heavy_Weight" )
	private BigDecimal exportHpWeight;
	@Column(name = "Export_Penalty" )
	private double exportPenalty;
	
	@Column(name = "ImportSc_Rate" )
	private double importScRate;
	@Column(name = "ImportPc_Rate" )
	private double importPcRate;
	@Column(name = "ImportHp_Rate" )
	private double importHpRate;
	@Column(name = "Import_Heavy_Weight" )
	private BigDecimal importHpWeight;
	@Column(name = "Import_Penalty" )
	private double importPenalty;
		
	@Column(name = "Tax_Amt", scale = 2)
	private double taxAmount;

	@Column(name = "Bill_Amt", scale = 2)
	private double billAmount;

	@Column(name = "Total_Invoice_Amt", scale = 2)
	private double totalInvoiceAmount;

	@Column(name = "Status", nullable = false, length = 1)
	private String status;

	@Column(name = "Created_By", nullable = false, length = 30)
	private String createdBy;

	@Column(name = "Created_Date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	 @Column(name = "Import_Rate", length = 6)
	 private double importRate;
	 
	 @Column(name = "Export_Rate", length = 6)
	private double exportRate;  
	
	
	private int importSubNop;
	private int exportSubNop;
	private double importSubRate;
	private double exportSubRate;
	private int demuragesNop;
	private double demuragesRate;
	private int niptPackages;
	
	
	@Column(name = "ImportSPLCart_Nop", length = 6)
	private int importSplCartNop;
    
    @Column(name = "ImportPc_Nop", length = 6)
	private int importPcNop;
    
    @Column(name = "ImportHp_Nop", length = 6)
	private int importHpNop;
    
    @Column(name = "ImportOC_Nop", length = 6)
	private int importOcNop;
	
	
    @Column(name = "ExportSPLCart_Nop", length = 6)
   	private int exportSplCartNop;
       
    @Column(name = "ExportPc_Nop", length = 6)
   	private int exportPcNop;
       
    @Column(name = "ExportHp_Nop", length = 6)
   	private int exportHpNop;
       
    @Column(name = "ExportOC_Nop", length = 6)
   	private int exportOcNop;
    
    
	
	
	
private int holidaySubNop;	
	
	public int getHolidaySubNop() {
		return holidaySubNop;
	}
	public void setHolidaySubNop(int holidaySubNop) {
		this.holidaySubNop = holidaySubNop;
	}
	
//	public Long getSrNo() {
//		return SrNo;
//	}
//	public void setSrNo(Long srNo) {
//		SrNo = srNo;
//	}
	
	
	
	
	public String getCompanyId() {
		return companyId;
	}
	public double getImportRate() {
		return importRate;
	}
	public void setImportRate(double importRate) {
		this.importRate = importRate;
	}
	public double getExportRate() {
		return exportRate;
	}
	public void setExportRate(double exportRate) {
		this.exportRate = exportRate;
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
	public String getPredictableinvoiceNO() {
		return predictableinvoiceNO;
	}
	public void setPredictableinvoiceNO(String predictableinvoiceNO) {
		this.predictableinvoiceNO = predictableinvoiceNO;
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
	public int getImportNoOfPackages() {
		return importNoOfPackages;
	}
	public void setImportNoOfPackages(int importNoOfPackages) {
		this.importNoOfPackages = importNoOfPackages;
	}
	public int getExportNoOfPackages() {
		return exportNoOfPackages;
	}
	public void setExportNoOfPackages(int exportNoOfPackages) {
		this.exportNoOfPackages = exportNoOfPackages;
	}
	public int getTotalPackages() {
		return totalPackages;
	}
	public void setTotalPackages(int totalPackages) {
		this.totalPackages = totalPackages;
	}
	public double getHolidayRate() {
		return holidayRate;
	}
	public void setHolidayRate(double holidayRate) {
		this.holidayRate = holidayRate;
	}
	public double getExportScRate() {
		return exportScRate;
	}
	public void setExportScRate(double exportScRate) {
		this.exportScRate = exportScRate;
	}
	public double getExportPcRate() {
		return exportPcRate;
	}
	public void setExportPcRate(double exportPcRate) {
		this.exportPcRate = exportPcRate;
	}
	public double getExportHpRate() {
		return exportHpRate;
	}
	public void setExportHpRate(double exportHpRate) {
		this.exportHpRate = exportHpRate;
	}
	public BigDecimal getExportHpWeight() {
		return exportHpWeight;
	}
	public void setExportHpWeight(BigDecimal exportHpWeight) {
		this.exportHpWeight = exportHpWeight;
	}
	public double getExportPenalty() {
		return exportPenalty;
	}
	public void setExportPenalty(double exportPenalty) {
		this.exportPenalty = exportPenalty;
	}
	public double getImportScRate() {
		return importScRate;
	}
	public void setImportScRate(double importScRate) {
		this.importScRate = importScRate;
	}
	public double getImportPcRate() {
		return importPcRate;
	}
	public void setImportPcRate(double importPcRate) {
		this.importPcRate = importPcRate;
	}
	public double getImportHpRate() {
		return importHpRate;
	}
	public void setImportHpRate(double importHpRate) {
		this.importHpRate = importHpRate;
	}
	public BigDecimal getImportHpWeight() {
		return importHpWeight;
	}
	public void setImportHpWeight(BigDecimal importHpWeight) {
		this.importHpWeight = importHpWeight;
	}
	public double getImportPenalty() {
		return importPenalty;
	}
	public void setImportPenalty(double importPenalty) {
		this.importPenalty = importPenalty;
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
	public int getImportSubNop() {
		return importSubNop;
	}
	public void setImportSubNop(int importSubNop) {
		this.importSubNop = importSubNop;
	}
	public int getExportSubNop() {
		return exportSubNop;
	}
	public void setExportSubNop(int exportSubNop) {
		this.exportSubNop = exportSubNop;
	}
	public double getImportSubRate() {
		return importSubRate;
	}
	public void setImportSubRate(double importSubRate) {
		this.importSubRate = importSubRate;
	}
	public double getExportSubRate() {
		return exportSubRate;
	}
	public void setExportSubRate(double exportSubRate) {
		this.exportSubRate = exportSubRate;
	}
	public int getDemuragesNop() {
		return demuragesNop;
	}
	public void setDemuragesNop(int demuragesNop) {
		this.demuragesNop = demuragesNop;
	}
	public double getDemuragesRate() {
		return demuragesRate;
	}
	public void setDemuragesRate(double demuragesRate) {
		this.demuragesRate = demuragesRate;
	}
	public int getNiptPackages() {
		return niptPackages;
	}
	public void setNiptPackages(int niptPackages) {
		this.niptPackages = niptPackages;
	}
	public PredictableInvoiceTaxDetails(String companyId, String branchId, String predictableinvoiceNO,
			String partyId, Date invoiceDate, int importNoOfPackages, int exportNoOfPackages, int totalPackages,
			double holidayRate, double exportScRate, double exportPcRate, double exportHpRate,
			BigDecimal exportHpWeight, double exportPenalty, double importScRate, double importPcRate,
			double importHpRate, BigDecimal importHpWeight, double importPenalty, double taxAmount, double billAmount,
			double totalInvoiceAmount, String status, String createdBy, Date createdDate, int importSubNop,
			int exportSubNop, double importSubRate, double exportSubRate, int demuragesNop, double demuragesRate,
			int niptPackages , double importRate , double exportRate,int holidaySubNop ,int importSplCartNop , int importPcNop , int importHpNop , int importOcNop
			,int exportSplCartNop , int exportPcNop , int exportHpNop , int exportOcNop
			) {
		super();
		
		this.companyId = companyId;
		this.branchId = branchId;
		this.predictableinvoiceNO = predictableinvoiceNO;
		this.partyId = partyId;
		this.invoiceDate = invoiceDate;
		this.importNoOfPackages = importNoOfPackages;
		this.exportNoOfPackages = exportNoOfPackages;
		this.totalPackages = totalPackages;
		this.holidayRate = holidayRate;
		this.exportScRate = exportScRate;
		this.exportPcRate = exportPcRate;
		this.exportHpRate = exportHpRate;
		this.exportHpWeight = exportHpWeight;
		this.exportPenalty = exportPenalty;
		this.importScRate = importScRate;
		this.importPcRate = importPcRate;
		this.importHpRate = importHpRate;
		this.importHpWeight = importHpWeight;
		this.importPenalty = importPenalty;
		this.taxAmount = taxAmount;
		this.billAmount = billAmount;
		this.totalInvoiceAmount = totalInvoiceAmount;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.importSubNop = importSubNop;
		this.exportSubNop = exportSubNop;
		this.importSubRate = importSubRate;
		this.exportSubRate = exportSubRate;
		this.demuragesNop = demuragesNop;
		this.demuragesRate = demuragesRate;
		this.niptPackages = niptPackages;
		this.importRate = importRate;
		this.exportRate = exportRate;
		this.holidaySubNop = holidaySubNop;
		this.importSplCartNop = importSplCartNop;
		this.importPcNop = importPcNop;
		this.importHpNop = importHpNop;
		this.importOcNop = importOcNop;
		this.exportSplCartNop = exportSplCartNop;
		this.exportPcNop = exportPcNop;
		this.exportHpNop = exportHpNop;
		this.exportOcNop = exportOcNop;
		
	}
	public PredictableInvoiceTaxDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getSrNo() {
		return SrNo;
	}
	public void setSrNo(Long srNo) {
		SrNo = srNo;
	}
	public int getImportSplCartNop() {
		return importSplCartNop;
	}
	public void setImportSplCartNop(int importSplCartNop) {
		this.importSplCartNop = importSplCartNop;
	}
	public int getImportPcNop() {
		return importPcNop;
	}
	public void setImportPcNop(int importPcNop) {
		this.importPcNop = importPcNop;
	}
	public int getImportHpNop() {
		return importHpNop;
	}
	public void setImportHpNop(int importHpNop) {
		this.importHpNop = importHpNop;
	}
	public int getImportOcNop() {
		return importOcNop;
	}
	public void setImportOcNop(int importOcNop) {
		this.importOcNop = importOcNop;
	}
	public int getExportSplCartNop() {
		return exportSplCartNop;
	}
	public void setExportSplCartNop(int exportSplCartNop) {
		this.exportSplCartNop = exportSplCartNop;
	}
	public int getExportPcNop() {
		return exportPcNop;
	}
	public void setExportPcNop(int exportPcNop) {
		this.exportPcNop = exportPcNop;
	}
	public int getExportHpNop() {
		return exportHpNop;
	}
	public void setExportHpNop(int exportHpNop) {
		this.exportHpNop = exportHpNop;
	}
	public int getExportOcNop() {
		return exportOcNop;
	}
	public void setExportOcNop(int exportOcNop) {
		this.exportOcNop = exportOcNop;
	}

public double getTotal1() {
	
		
		double total = exportRate+importRate+importSubRate+exportSubRate+holidayRate+demuragesRate+exportScRate
+exportHpRate+exportPcRate+exportPenalty+importScRate+importHpRate+importPenalty+importPcRate;
	
	
	return total;
}
public double getTotal() {
	
	
	double total = exportRate+importRate+importSubRate+exportSubRate+holidayRate+demuragesRate+exportScRate
+exportHpRate+exportPcRate+exportPenalty+importScRate+importHpRate+importPenalty+importPcRate;


return total;
}
	
}
