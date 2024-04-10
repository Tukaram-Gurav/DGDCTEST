package com.cwms.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name="Predictable_Invoice_Packages")
@IdClass(PredictabeInvoicePackagesId.class)
public class PredictableInvoicePackages 
{
	 @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SrNo")
    private Long SrNo;
    
    @Id
    private String predictableinvoiceNO;
	
	@Id
	@Column(name = "Company_Id", length = 6)
	private String companyId;

	@Id
	@Column(name = "Branch_Id", length = 6)
	private String branchId;	
		
	@Id
	@Column(name = "Party_Id", length = 6)
	private String partyId;


    @Column(name = "Invoice_Date")
    @Temporal(TemporalType.DATE)
    private Date calculatedDate;
    
    @Column(name = "Import_Nop", length = 6)
   	private int importNop;
       
    @Column(name = "ImportSub_Nop", length = 6)
	private int importSubNop;
    
    @Column(name = "Export_Nop", length = 6)
	private int exportNop;
    
    @Column(name = "ExportSub_Nop", length = 6)
	private int exportSubNop;
    
    
    @Column(name = "Holiday_Nop", length = 6)
	private int holidaySubNop;
    
    @Column(name = "ExportSPLCart_Nop", length = 6)
   	private int exportSplCartNop;
       
    @Column(name = "ExportPc_Nop", length = 6, nullable = false)
   	private int exportPcNop;
       
    @Column(name = "ExportHp_Nop", length = 6, nullable = false)
   	private int exportHpNop;
       
    @Column(name = "ExportOC_Nop", length = 6, nullable = false)
   	private int exportOcNop;
    
    

    @Column(name = "ImportSPLCart_Nop", length = 6)
	private int importSplCartNop;
    
    @Column(name = "ImportPc_Nop", length = 6)
	private int importPcNop;
    
    @Column(name = "ImportHp_Nop", length = 6)
	private int importHpNop;
    
    @Column(name = "ImportOC_Nop", length = 6)
	private int importOcNop;
    
//    Rates
    
    @Column(name = "Import_Rate", length = 6)
   	private double importRate;
    
    @Column(name = "ImportSub_Rate", length = 6)
	private double importSubRate;
    
    @Column(name = "Export_Rate", length = 6)
	private double exportRate;   
      
    @Column(name = "ExportSub_Rate", length = 6)
	private double exportSubRate;
     
    @Column(name = "Holiday_Rate", length = 6)
	private double holidayRate;   
    
    
    @Column(name = "ExportSPLCart_Rate", length = 6)
   	private double exportSplCartRate;
       
    @Column(name = "ExportPc_Rate", length = 6, nullable = false)
   	private double exportPcRate;
       
    @Column(name = "ExportHp_Rate", length = 6, nullable = false)
   	private double exportHpRate;
       
    @Column(name = "ExportOC_Rate", length = 6, nullable = false)
   	private double exportOcRate;
    
    
    @Column(name = "ImportSPLCart_Rate", length = 6)
	private double importSplCartRate;
    
    @Column(name = "ImportPc_Rate", length = 6)
	private double importPcRate;
    
    @Column(name = "ImportHp_Rate", length = 6)
	private double importHpRate;
    
    @Column(name = "ImportOC_Rate", length = 6)
	private double importOcRate;
    
    private int demuragesNop;
	private double demuragesRate;
	
	private int niptPackages;
	
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

	
	    @Column(name = "IGST", length = 1)
	    private String igst;

	    @Column(name = "CGST", length = 1)
	    private String cgst;

	    @Column(name = "SGST", length = 1)
	    private String sgst;
	    
	    
	    
	    
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











	public PredictableInvoicePackages() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	

	




	public String getPredictableinvoiceNO() {
		return predictableinvoiceNO;
	}











	public void setPredictableinvoiceNO(String predictableinvoiceNO) {
		this.predictableinvoiceNO = predictableinvoiceNO;
	}











	public PredictableInvoicePackages( String companyId, String branchId,String predictableinvoiceNO, String partyId,
			Date calculatedDate, int importNop, int importSubNop, int exportNop, int exportSubNop, int holidaySubNop,
			int exportSplCartNop, int exportPcNop, int exportHpNop, int exportOcNop, int importSplCartNop,
			int importPcNop, int importHpNop, int importOcNop, double importRate, double importSubRate,
			double exportRate, double exportSubRate, double holidayRate, double exportSplCartRate, double exportPcRate,
			double exportHpRate, double exportOcRate, double importSplCartRate, double importPcRate,
			double importHpRate, double importOcRate, int demuragesNop, double demuragesRate, int niptPackages,
			String tariffNo, String tariffAmndNo, double taxAmount, double billAmount, double totalInvoiceAmount) {
		super();
		
		this.companyId = companyId;
		this.branchId = branchId;
		this.predictableinvoiceNO = predictableinvoiceNO;
		this.partyId = partyId;
		this.calculatedDate = calculatedDate;
		this.importNop = importNop;
		this.importSubNop = importSubNop;
		this.exportNop = exportNop;
		this.exportSubNop = exportSubNop;
		this.holidaySubNop = holidaySubNop;
		this.exportSplCartNop = exportSplCartNop;
		this.exportPcNop = exportPcNop;
		this.exportHpNop = exportHpNop;
		this.exportOcNop = exportOcNop;
		this.importSplCartNop = importSplCartNop;
		this.importPcNop = importPcNop;
		this.importHpNop = importHpNop;
		this.importOcNop = importOcNop;
		this.importRate = importRate;
		this.importSubRate = importSubRate;
		this.exportRate = exportRate;
		this.exportSubRate = exportSubRate;
		this.holidayRate = holidayRate;
		this.exportSplCartRate = exportSplCartRate;
		this.exportPcRate = exportPcRate;
		this.exportHpRate = exportHpRate;
		this.exportOcRate = exportOcRate;
		this.importSplCartRate = importSplCartRate;
		this.importPcRate = importPcRate;
		this.importHpRate = importHpRate;
		this.importOcRate = importOcRate;
		this.demuragesNop = demuragesNop;
		this.demuragesRate = demuragesRate;
		this.niptPackages = niptPackages;
		this.tariffNo = tariffNo;
		this.tariffAmndNo = tariffAmndNo;
		this.taxAmount = taxAmount;
		this.billAmount = billAmount;
		this.totalInvoiceAmount = totalInvoiceAmount;
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

	public Date getCalculatedDate() {
		return calculatedDate;
	}

	public void setCalculatedDate(Date calculatedDate) {
		this.calculatedDate = calculatedDate;
	}

	public int getImportNop() {
		return importNop;
	}

	public void setImportNop(int importNop) {
		this.importNop = importNop;
	}

	public int getImportSubNop() {
		return importSubNop;
	}

	public void setImportSubNop(int importSubNop) {
		this.importSubNop = importSubNop;
	}

	public int getExportNop() {
		return exportNop;
	}

	public void setExportNop(int exportNop) {
		this.exportNop = exportNop;
	}

	public int getExportSubNop() {
		return exportSubNop;
	}

	public void setExportSubNop(int exportSubNop) {
		this.exportSubNop = exportSubNop;
	}

	public int getHolidaySubNop() {
		return holidaySubNop;
	}

	public void setHolidaySubNop(int holidaySubNop) {
		this.holidaySubNop = holidaySubNop;
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

	public double getImportRate() {
		return importRate;
	}

	public void setImportRate(double importRate) {
		this.importRate = importRate;
	}

	public double getImportSubRate() {
		return importSubRate;
	}

	public void setImportSubRate(double importSubRate) {
		this.importSubRate = importSubRate;
	}

	public double getExportRate() {
		return exportRate;
	}

	public void setExportRate(double exportRate) {
		this.exportRate = exportRate;
	}

	public double getExportSubRate() {
		return exportSubRate;
	}

	public void setExportSubRate(double exportSubRate) {
		this.exportSubRate = exportSubRate;
	}

	public double getHolidayRate() {
		return holidayRate;
	}

	public void setHolidayRate(double holidayRate) {
		this.holidayRate = holidayRate;
	}

	public double getExportSplCartRate() {
		return exportSplCartRate;
	}

	public void setExportSplCartRate(double exportSplCartRate) {
		this.exportSplCartRate = exportSplCartRate;
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

	public double getExportOcRate() {
		return exportOcRate;
	}

	public void setExportOcRate(double exportOcRate) {
		this.exportOcRate = exportOcRate;
	}

	public double getImportSplCartRate() {
		return importSplCartRate;
	}

	public void setImportSplCartRate(double importSplCartRate) {
		this.importSplCartRate = importSplCartRate;
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

	public double getImportOcRate() {
		return importOcRate;
	}

	public void setImportOcRate(double importOcRate) {
		this.importOcRate = importOcRate;
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











//	public int getInvoiceId() {
//		return invoiceId;
//	}
//
//
//
//
//
//
//
//
//
//
//
//	public void setInvoiceId(int invoiceId) {
//		this.invoiceId = invoiceId;
//	}











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
	
	
	

}
