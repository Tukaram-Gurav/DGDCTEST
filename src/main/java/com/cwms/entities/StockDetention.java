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
@Table(name = "STOCK_DETENTION")
@IdClass(StockDetentionIds.class)
public class StockDetention 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SrNo")
	private Long SrNo;

	@Id
	@Column(name = "Company_Id", length = 6)
	private String companyId;

	@Id
	@Column(name = "Stock_Date")
	@Temporal(TemporalType.DATE)
	private Date stockDate;

	
	
	@Id
	@Column(name = "Branch_Id", length = 6)
	private String branchId;

	@Column(length = 10)
	private int importDetentionIn;

	@Column(length = 10)
	private int importDetentionOut;
	
	@Column(length = 10)
	private int importDetentionStock;

	@Column(length = 10)
	private int exportDetentionStock;
	
	@Column(length = 10)
	private int exportDetentionIn;

	@Column(length = 10)
	private int exportDetentionOut;

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

	public int getImportDetentionIn() {
		return importDetentionIn;
	}

	public void setImportDetentionIn(int importDetentionIn) {
		this.importDetentionIn = importDetentionIn;
	}

	public int getImportDetentionOut() {
		return importDetentionOut;
	}

	public void setImportDetentionOut(int importDetentionOut) {
		this.importDetentionOut = importDetentionOut;
	}

	public int getImportDetentionStock() {
		return importDetentionStock;
	}

	public void setImportDetentionStock(int importDetentionStock) {
		this.importDetentionStock = importDetentionStock;
	}

	public int getExportDetentionStock() {
		return exportDetentionStock;
	}

	public void setExportDetentionStock(int exportDetentionStock) {
		this.exportDetentionStock = exportDetentionStock;
	}

	public int getExportDetentionIn() {
		return exportDetentionIn;
	}

	public void setExportDetentionIn(int exportDetentionIn) {
		this.exportDetentionIn = exportDetentionIn;
	}

	public int getExportDetentionOut() {
		return exportDetentionOut;
	}

	public void setExportDetentionOut(int exportDetentionOut) {
		this.exportDetentionOut = exportDetentionOut;
	}

	
	public Date getStockDate() {
		return stockDate;
	}

	public void setStockDate(Date stockDate) {
		this.stockDate = stockDate;
	}

	public StockDetention(Long srNo, String companyId, Date stockDate, String branchId, int importDetentionIn,
			int importDetentionOut, int importDetentionStock, int exportDetentionStock, int exportDetentionIn,
			int exportDetentionOut) {
		super();
		SrNo = srNo;
		this.companyId = companyId;
		this.stockDate = stockDate;
		this.branchId = branchId;
		this.importDetentionIn = importDetentionIn;
		this.importDetentionOut = importDetentionOut;
		this.importDetentionStock = importDetentionStock;
		this.exportDetentionStock = exportDetentionStock;
		this.exportDetentionIn = exportDetentionIn;
		this.exportDetentionOut = exportDetentionOut;
	}

	public StockDetention() {
		super();
		// TODO Auto-generated constructor stub
	}


}
