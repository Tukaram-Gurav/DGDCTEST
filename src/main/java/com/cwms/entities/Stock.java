package com.cwms.entities;

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
@Table(name = "STOCK")
@IdClass(StockIds.class)
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SrNo")
	private Long SrNo;

	@Id
	@Column(name = "Company_Id", length = 6)
	private String companyId;

	@Id
	@Column(name = "Branch_Id", length = 6)
	private String branchId;

	@Id
	@Column(name = "Stock_Date")
	@Temporal(TemporalType.DATE)
	private Date stockDate;

	@Column(length = 10)
	private int importIn;

	@Column(length = 10)
	private int importPcIn;

	@Column(length = 10)
	private int importNiptIn;

	@Column(length = 10)
	private int importSubIn;

	@Column(length = 10)
	private int exportIn;

	@Column(length = 10)
	private int exportPcIn;

	@Column(length = 10)
	private int exportSubIn;

	@Column(length = 10)
	private int importOut;

	@Column(length = 10)
	private int importPcOut;

	@Column(length = 10)
	private int importNiptOut;

	@Column(length = 10)
	private int importSubOut;

	@Column(length = 10)
	private int exportOut;

	@Column(length = 10)
	private int exportPcOut;

	@Column(length = 10)
	private int exportSubOut;

	@Column(length = 10)
	private int importStock;

	@Column(length = 10)
	private int importPcStock;

	@Column(length = 10)
	private int importNiptStock;

	@Column(length = 10)
	private int importSubStock;

	@Column(length = 10)
	private int exportStock;

	@Column(length = 10)
	private int exportPcStock;

	@Column(length = 10)
	private int exportSubStock;

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

	public int getImportIn() {
		return importIn;
	}

	public void setImportIn(int importIn) {
		this.importIn = importIn;
	}

	public int getImportNiptIn() {
		return importNiptIn;
	}

	public void setImportNiptIn(int importNiptIn) {
		this.importNiptIn = importNiptIn;
	}

	public int getImportSubIn() {
		return importSubIn;
	}

	public void setImportSubIn(int importSubIn) {
		this.importSubIn = importSubIn;
	}

	public int getExportIn() {
		return exportIn;
	}

	public void setExportIn(int exportIn) {
		this.exportIn = exportIn;
	}

	public int getExportSubIn() {
		return exportSubIn;
	}

	public void setExportSubIn(int exportSubIn) {
		this.exportSubIn = exportSubIn;
	}

	public int getImportOut() {
		return importOut;
	}

	public void setImportOut(int importOut) {
		this.importOut = importOut;
	}

	public int getImportNiptOut() {
		return importNiptOut;
	}

	public void setImportNiptOut(int importNiptOut) {
		this.importNiptOut = importNiptOut;
	}

	public int getImportSubOut() {
		return importSubOut;
	}

	public void setImportSubOut(int importSubOut) {
		this.importSubOut = importSubOut;
	}

	public int getExportOut() {
		return exportOut;
	}

	public void setExportOut(int exportOut) {
		this.exportOut = exportOut;
	}

	public int getExportSubOut() {
		return exportSubOut;
	}

	public void setExportSubOut(int exportSubOut) {
		this.exportSubOut = exportSubOut;
	}

	public int getImportStock() {
		return importStock;
	}

	public void setImportStock(int importStock) {
		this.importStock = importStock;
	}

	public int getImportNiptStock() {
		return importNiptStock;
	}

	public void setImportNiptStock(int importNiptStock) {
		this.importNiptStock = importNiptStock;
	}

	public int getImportSubStock() {
		return importSubStock;
	}

	public void setImportSubStock(int importSubStock) {
		this.importSubStock = importSubStock;
	}

	public int getExportStock() {
		return exportStock;
	}

	public void setExportStock(int exportStock) {
		this.exportStock = exportStock;
	}

	public int getExportSubStock() {
		return exportSubStock;
	}

	public void setExportSubStock(int exportSubStock) {
		this.exportSubStock = exportSubStock;
	}

	public int getImportPcIn() {
		return importPcIn;
	}

	public void setImportPcIn(int importPcIn) {
		this.importPcIn = importPcIn;
	}

	public int getExportPcIn() {
		return exportPcIn;
	}

	public void setExportPcIn(int exportPcIn) {
		this.exportPcIn = exportPcIn;
	}

	public int getImportPcOut() {
		return importPcOut;
	}

	public void setImportPcOut(int importPcOut) {
		this.importPcOut = importPcOut;
	}

	public int getExportPcOut() {
		return exportPcOut;
	}

	public void setExportPcOut(int exportPcOut) {
		this.exportPcOut = exportPcOut;
	}

	public int getImportPcStock() {
		return importPcStock;
	}

	public void setImportPcStock(int importPcStock) {
		this.importPcStock = importPcStock;
	}

	public int getExportPcStock() {
		return exportPcStock;
	}

	public void setExportPcStock(int exportPcStock) {
		this.exportPcStock = exportPcStock;
	}

	public Date getStockDate() {
		return stockDate;
	}

	public void setStockDate(Date stockDate) {
		this.stockDate = stockDate;
	}

	public Stock() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Stock(String companyId, String branchId, Date stockDate, int importIn, int importPcIn, int importNiptIn,
			int importSubIn, int exportIn, int exportPcIn, int exportSubIn, int importOut, int importPcOut,
			int importNiptOut, int importSubOut, int exportOut, int exportPcOut, int exportSubOut, int importStock,
			int importPcStock, int importNiptStock, int importSubStock, int exportStock, int exportPcStock,
			int exportSubStock) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.stockDate = stockDate;
		this.importIn = importIn;
		this.importPcIn = importPcIn;
		this.importNiptIn = importNiptIn;
		this.importSubIn = importSubIn;
		this.exportIn = exportIn;
		this.exportPcIn = exportPcIn;
		this.exportSubIn = exportSubIn;
		this.importOut = importOut;
		this.importPcOut = importPcOut;
		this.importNiptOut = importNiptOut;
		this.importSubOut = importSubOut;
		this.exportOut = exportOut;
		this.exportPcOut = exportPcOut;
		this.exportSubOut = exportSubOut;
		this.importStock = importStock;
		this.importPcStock = importPcStock;
		this.importNiptStock = importNiptStock;
		this.importSubStock = importSubStock;
		this.exportStock = exportStock;
		this.exportPcStock = exportPcStock;
		this.exportSubStock = exportSubStock;
	}

	public Long getSrNo() {
		return SrNo;
	}

	public void setSrNo(Long srNo) {
		SrNo = srNo;
	}

	
	
}
