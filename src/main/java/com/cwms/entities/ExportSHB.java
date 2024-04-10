package com.cwms.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "exportshb")
@IdClass(ExportSHBId.class)
public class ExportSHB {
	@Id
	@Column(name = "Company_Id", length = 6)
	private String companyId;

	@Id
	@Column(name = "Branch_Id", length = 6)
	private String branchId;

	@Id
	@Column(name = "SB_Number", length = 15)
	private String sbNo;

	@Id
	@Column(name = "ER_No", length = 20)
	private String erNo;

	
	@Column(name = "ER_Date")
	private Date erDate;

	@Column(name = "SB_Date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sbDate;

	@Column(name = "IEC_Code", length = 25)
	private String iecCode;

	@Column(name = "Entity_Id", length = 20)
	private String entityId;

	@Column(name = "Exporter_Name", length = 50)
	private String nameOfExporter;

	
	@Column(name = "gross_Weight")
	private Double grossWeight;
	
	
	 @Column(name = "Gross_Weight_CARATS")
	    private BigDecimal grossWeightInCarats;

	@Column(name = "UOMGrossWeight",length = 10)
	private String uomGrossWeight;

	@Column(name = "CountryOfDestination", length = 75)
	private String countryOfDestination;

	@Column(name = "PortOfDestination", length = 75)
	private String portOfDestination;

	@Column(name = "Airway_Bill_No", length = 25)
	private String airwayBillNo;

	@Column(name = "Description_Of_Goods", length = 100)
	private String descriptionOfGoods;

	@Column(name = "Parcel_Status", length = 50)
	private String parcelStatus;

	@Column(name = "DGDC_Status", length = 50)
	private String dgdcStatus;

	

	@Column(name = "CHAName", length = 50)
	private String chaName;

	@Column(name = "ConsoleAgent", length = 25)
	private String consoleAgent;

	@Column(name = "FOBValueInINR")
	private BigDecimal fobValueInINR;

	@Column(name = "NoOfPackages")
	private int noOfPackages;

	@Column(name = "UOMOfPackages")
	private String uomOfPackages;

	@Column(name = "Status", length = 1)
	private String status;

	@Column(name = "Created_By")
	private String createdBy;

	@Column(name = "Created_Date")
	@Temporal(TemporalType.DATE)
	private Date createdDate;

	@Column(name = "Edited_By")
	private String editedBy;

	@Column(name = "Edited_Date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date editedDate;

	@Column(name = "Approved_By")
	private String approvedBy;

	@Column(name = "Approved_Date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approvedDate;

	
	@Column(name = "Console_Representative_Id", length = 10)
	private String consolePartyRepresentativeId;

	

	@Column(name = "Custom_PCTM_No",length = 10)
	private String customPctmNo;

	@Column(name = "Custom_TP_No",length = 10)
	private String customTpNo;

	@Column(name = "Custom_TP_Date")
	private Date customTpDate;

	@Column(name = "Custom_PCTM_Date")
	private Date customPctmDate;
	
	
	@Column(name = "PCTM_No")
	private String pctmNo;

	@Column(name = "TP_No")
	private String tpNo;

	@Column(name = "TP_Date")
	private Date tpDate;

	@Column(name = "PCTM_Date")
	private Date pctmDate;

	@Column(name = "HAWB", length = 15)
	private String hawb;

	@Column(name = "Airline_Name")
	private String airlineName;

	@Column(name = "Flight_No")
	private String flightNo;

	@Column(name = "Airline_Code", length = 10)
	private String airlineCode;

	@Column(name = "Flight_Date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date flightDate;

	@Column(name = "Hold_Status", length = 1)
	private String holdStatus;
	
	@Column(name = "Hold_By")
	private String holdBy;

	@Column(name = "HP_Status", length = 1)
	private String hpStatus;

	@Column(name = "Impose_Penalty_Amt")
	private double imposePenaltyAmount;

	@Column(name = "Impose_Penalty_Remarks")
	private String imposePenaltyRemarks;

	@Column(name = "Reason_for_Override", length = 255)
	private String reasonforOverride;

	@Column(name = "Override_Document", length = 255)
	private String overrideDocument;

	@Column(name = "Cancel_Status", length = 1)
	private String cancelStatus;

	@Column(name = "Cancel_Remarks")
	private String cancelRemarks;

	@Column(name = "GatePass_Vehicle_No", length = 20)
	private String gatePassVehicleNo;

	@Column(name = "PO_Name", length = 30)
	private String pOName;

	@Column(name = "GatePass_Status", length = 1)
	private String gatePassStatus;

	@Column(name = "image_path", length = 252)
	private String imagePath;

	@Column(name = "Redeposite_Remark", length = 252)
	private String redepositeRemark;

	@Column(name = "Back_To_Town_Remark")
	private String backtotownRemark;

	@Column(name = "Back_To_Town_FilePath")
	private String backtotownFilePath;

	@Column(name = "Ep_Copy_Document")
	private String epCopyDocument;

	

	@Column(length = 1)
	private int dgdc_shb_in_scan;
	@Column(length = 1)
	private int dgdc_shb_out_scan;
	@Column(length = 1)
	private int dgdc_cargo_in_scan;
	@Column(length = 1)
	private int dgdc_cargo_out_scan;


	@Column(name = "Out_Date", length = 10)
	private Date outDate;




	@Column(name = "Airline_Date")
	private Date airLineDate;

	
	
	 @Column(name = "Seepz_InDate")
	 private Date seepzInDate;	    
	    
	 @Column(name = "No_Parcel", columnDefinition = "int default 1")
	    private int noOfParcel;
	 
	 @Column(name = "Bill_Calculated", length = 1, columnDefinition = "VARCHAR(1) DEFAULT 'N'")
	    private String billCalculated = "N";
	 
	 
	 
	 
	 public int getNoOfParcel() {
		return noOfParcel;
	}

	public void setNoOfParcel(int noOfParcel) {
		this.noOfParcel = noOfParcel;
	}

	public String getBillCalculated() {
		return billCalculated;
	}

	public void setBillCalculated(String billCalculated) {
		this.billCalculated = billCalculated;
	}

	public Date getSeepzInDate() {
			return seepzInDate;
		}

		public void setSeepzInDate(Date seepzInDate) {
			this.seepzInDate = seepzInDate;
		}


	public ExportSHB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getHoldBy() {
		return holdBy;
	}
	public BigDecimal getGrossWeightInCarats() {
		return grossWeightInCarats;
	}

	public void setGrossWeightInCarats(BigDecimal grossWeightInCarats) {
		this.grossWeightInCarats = grossWeightInCarats;
	}

	public void setHoldBy(String holdBy) {
		this.holdBy = holdBy;
	}







	





	public ExportSHB(String companyId, String branchId, String sbNo, String erNo, Date erDate, Date sbDate,
			String iecCode, String entityId, String nameOfExporter, Double grossWeight, String uomGrossWeight,
			String countryOfDestination, String portOfDestination, String airwayBillNo, String descriptionOfGoods,
			String parcelStatus, String dgdcStatus, String chaName, String consoleAgent, BigDecimal fobValueInINR,
			int noOfPackages, String uomOfPackages, String status, String createdBy, Date createdDate, String editedBy,
			Date editedDate, String approvedBy, Date approvedDate, String consolePartyRepresentativeId,
			String customPctmNo, String customTpNo, Date customTpDate, Date customPctmDate, String pctmNo, String tpNo,
			Date tpDate, Date pctmDate, String hawb, String airlineName, String flightNo, String airlineCode,
			Date flightDate, String holdStatus, String holdBy, String hpStatus, double imposePenaltyAmount,
			String imposePenaltyRemarks, String reasonforOverride, String overrideDocument, String cancelStatus,
			String cancelRemarks, String gatePassVehicleNo, String pOName, String gatePassStatus, String imagePath,
			String redepositeRemark, String backtotownRemark, String backtotownFilePath, String epCopyDocument,
			int dgdc_shb_in_scan, int dgdc_shb_out_scan, int dgdc_cargo_in_scan, int dgdc_cargo_out_scan, Date outDate,
			Date airLineDate) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.sbNo = sbNo;
		this.erNo = erNo;
		this.erDate = erDate;
		this.sbDate = sbDate;
		this.iecCode = iecCode;
		this.entityId = entityId;
		this.nameOfExporter = nameOfExporter;
		this.grossWeight = grossWeight;
		this.uomGrossWeight = uomGrossWeight;
		this.countryOfDestination = countryOfDestination;
		this.portOfDestination = portOfDestination;
		this.airwayBillNo = airwayBillNo;
		this.descriptionOfGoods = descriptionOfGoods;
		this.parcelStatus = parcelStatus;
		this.dgdcStatus = dgdcStatus;
		this.chaName = chaName;
		this.consoleAgent = consoleAgent;
		this.fobValueInINR = fobValueInINR;
		this.noOfPackages = noOfPackages;
		this.uomOfPackages = uomOfPackages;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.editedBy = editedBy;
		this.editedDate = editedDate;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
		this.consolePartyRepresentativeId = consolePartyRepresentativeId;
		this.customPctmNo = customPctmNo;
		this.customTpNo = customTpNo;
		this.customTpDate = customTpDate;
		this.customPctmDate = customPctmDate;
		this.pctmNo = pctmNo;
		this.tpNo = tpNo;
		this.tpDate = tpDate;
		this.pctmDate = pctmDate;
		this.hawb = hawb;
		this.airlineName = airlineName;
		this.flightNo = flightNo;
		this.airlineCode = airlineCode;
		this.flightDate = flightDate;
		this.holdStatus = holdStatus;
		this.holdBy = holdBy;
		this.hpStatus = hpStatus;
		this.imposePenaltyAmount = imposePenaltyAmount;
		this.imposePenaltyRemarks = imposePenaltyRemarks;
		this.reasonforOverride = reasonforOverride;
		this.overrideDocument = overrideDocument;
		this.cancelStatus = cancelStatus;
		this.cancelRemarks = cancelRemarks;
		this.gatePassVehicleNo = gatePassVehicleNo;
		this.pOName = pOName;
		this.gatePassStatus = gatePassStatus;
		this.imagePath = imagePath;
		this.redepositeRemark = redepositeRemark;
		this.backtotownRemark = backtotownRemark;
		this.backtotownFilePath = backtotownFilePath;
		this.epCopyDocument = epCopyDocument;
		this.dgdc_shb_in_scan = dgdc_shb_in_scan;
		this.dgdc_shb_out_scan = dgdc_shb_out_scan;
		this.dgdc_cargo_in_scan = dgdc_cargo_in_scan;
		this.dgdc_cargo_out_scan = dgdc_cargo_out_scan;
		this.outDate = outDate;
		this.airLineDate = airLineDate;
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



	public String getSbNo() {
		return sbNo;
	}



	public void setSbNo(String sbNo) {
		this.sbNo = sbNo;
	}



	public String getErNo() {
		return erNo;
	}



	public void setErNo(String erNo) {
		this.erNo = erNo;
	}



	public Date getErDate() {
		return erDate;
	}



	public void setErDate(Date erDate) {
		this.erDate = erDate;
	}



	public Date getSbDate() {
		return sbDate;
	}



	public void setSbDate(Date sbDate) {
		this.sbDate = sbDate;
	}



	public String getIecCode() {
		return iecCode;
	}



	public void setIecCode(String iecCode) {
		this.iecCode = iecCode;
	}



	public String getEntityId() {
		return entityId;
	}



	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}



	public String getNameOfExporter() {
		return nameOfExporter;
	}



	public void setNameOfExporter(String nameOfExporter) {
		this.nameOfExporter = nameOfExporter;
	}



	public Double getGrossWeight() {
		return grossWeight;
	}



	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}



	public String getUomGrossWeight() {
		return uomGrossWeight;
	}



	public void setUomGrossWeight(String uomGrossWeight) {
		this.uomGrossWeight = uomGrossWeight;
	}



	public String getCountryOfDestination() {
		return countryOfDestination;
	}



	public void setCountryOfDestination(String countryOfDestination) {
		this.countryOfDestination = countryOfDestination;
	}



	public String getPortOfDestination() {
		return portOfDestination;
	}



	public void setPortOfDestination(String portOfDestination) {
		this.portOfDestination = portOfDestination;
	}



	public String getAirwayBillNo() {
		return airwayBillNo;
	}



	public void setAirwayBillNo(String airwayBillNo) {
		this.airwayBillNo = airwayBillNo;
	}



	public String getDescriptionOfGoods() {
		return descriptionOfGoods;
	}



	public void setDescriptionOfGoods(String descriptionOfGoods) {
		this.descriptionOfGoods = descriptionOfGoods;
	}



	public String getParcelStatus() {
		return parcelStatus;
	}



	public void setParcelStatus(String parcelStatus) {
		this.parcelStatus = parcelStatus;
	}



	public String getDgdcStatus() {
		return dgdcStatus;
	}



	public void setDgdcStatus(String dgdcStatus) {
		this.dgdcStatus = dgdcStatus;
	}



	public String getChaName() {
		return chaName;
	}



	public void setChaName(String chaName) {
		this.chaName = chaName;
	}



	public String getConsoleAgent() {
		return consoleAgent;
	}



	public void setConsoleAgent(String consoleAgent) {
		this.consoleAgent = consoleAgent;
	}



	public BigDecimal getFobValueInINR() {
		return fobValueInINR;
	}



	public void setFobValueInINR(BigDecimal fobValueInINR) {
		this.fobValueInINR = fobValueInINR;
	}



	public int getNoOfPackages() {
		return noOfPackages;
	}



	public void setNoOfPackages(int noOfPackages) {
		this.noOfPackages = noOfPackages;
	}



	public String getUomOfPackages() {
		return uomOfPackages;
	}



	public void setUomOfPackages(String uomOfPackages) {
		this.uomOfPackages = uomOfPackages;
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



	public String getConsolePartyRepresentativeId() {
		return consolePartyRepresentativeId;
	}



	public void setConsolePartyRepresentativeId(String consolePartyRepresentativeId) {
		this.consolePartyRepresentativeId = consolePartyRepresentativeId;
	}



	public String getCustomPctmNo() {
		return customPctmNo;
	}



	public void setCustomPctmNo(String customPctmNo) {
		this.customPctmNo = customPctmNo;
	}



	public String getCustomTpNo() {
		return customTpNo;
	}



	public void setCustomTpNo(String customTpNo) {
		this.customTpNo = customTpNo;
	}



	public Date getCustomTpDate() {
		return customTpDate;
	}



	public void setCustomTpDate(Date customTpDate) {
		this.customTpDate = customTpDate;
	}



	public Date getCustomPctmDate() {
		return customPctmDate;
	}



	public void setCustomPctmDate(Date customPctmDate) {
		this.customPctmDate = customPctmDate;
	}



	public String getPctmNo() {
		return pctmNo;
	}



	public void setPctmNo(String pctmNo) {
		this.pctmNo = pctmNo;
	}



	public String getTpNo() {
		return tpNo;
	}



	public void setTpNo(String tpNo) {
		this.tpNo = tpNo;
	}



	public Date getTpDate() {
		return tpDate;
	}



	public void setTpDate(Date tpDate) {
		this.tpDate = tpDate;
	}



	public Date getPctmDate() {
		return pctmDate;
	}



	public void setPctmDate(Date pctmDate) {
		this.pctmDate = pctmDate;
	}



	public String getHawb() {
		return hawb;
	}



	public void setHawb(String hawb) {
		this.hawb = hawb;
	}



	public String getAirlineName() {
		return airlineName;
	}



	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}



	public String getFlightNo() {
		return flightNo;
	}



	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}



	public String getAirlineCode() {
		return airlineCode;
	}



	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}



	public Date getFlightDate() {
		return flightDate;
	}



	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
	}



	public String getHoldStatus() {
		return holdStatus;
	}



	public void setHoldStatus(String holdStatus) {
		this.holdStatus = holdStatus;
	}



	public String getHpStatus() {
		return hpStatus;
	}



	public void setHpStatus(String hpStatus) {
		this.hpStatus = hpStatus;
	}



	public double getImposePenaltyAmount() {
		return imposePenaltyAmount;
	}



	public void setImposePenaltyAmount(double imposePenaltyAmount) {
		this.imposePenaltyAmount = imposePenaltyAmount;
	}



	public String getImposePenaltyRemarks() {
		return imposePenaltyRemarks;
	}



	public void setImposePenaltyRemarks(String imposePenaltyRemarks) {
		this.imposePenaltyRemarks = imposePenaltyRemarks;
	}



	public String getReasonforOverride() {
		return reasonforOverride;
	}



	public void setReasonforOverride(String reasonforOverride) {
		this.reasonforOverride = reasonforOverride;
	}



	public String getOverrideDocument() {
		return overrideDocument;
	}



	public void setOverrideDocument(String overrideDocument) {
		this.overrideDocument = overrideDocument;
	}



	public String getCancelStatus() {
		return cancelStatus;
	}



	public void setCancelStatus(String cancelStatus) {
		this.cancelStatus = cancelStatus;
	}



	public String getCancelRemarks() {
		return cancelRemarks;
	}



	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}



	public String getGatePassVehicleNo() {
		return gatePassVehicleNo;
	}



	public void setGatePassVehicleNo(String gatePassVehicleNo) {
		this.gatePassVehicleNo = gatePassVehicleNo;
	}



	public String getpOName() {
		return pOName;
	}



	public void setpOName(String pOName) {
		this.pOName = pOName;
	}



	public String getGatePassStatus() {
		return gatePassStatus;
	}



	public void setGatePassStatus(String gatePassStatus) {
		this.gatePassStatus = gatePassStatus;
	}



	public String getImagePath() {
		return imagePath;
	}



	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}



	public String getRedepositeRemark() {
		return redepositeRemark;
	}



	public void setRedepositeRemark(String redepositeRemark) {
		this.redepositeRemark = redepositeRemark;
	}



	public String getBacktotownRemark() {
		return backtotownRemark;
	}



	public void setBacktotownRemark(String backtotownRemark) {
		this.backtotownRemark = backtotownRemark;
	}



	public String getBacktotownFilePath() {
		return backtotownFilePath;
	}



	public void setBacktotownFilePath(String backtotownFilePath) {
		this.backtotownFilePath = backtotownFilePath;
	}



	public String getEpCopyDocument() {
		return epCopyDocument;
	}



	public void setEpCopyDocument(String epCopyDocument) {
		this.epCopyDocument = epCopyDocument;
	}



	public int getDgdc_shb_in_scan() {
		return dgdc_shb_in_scan;
	}



	public void setDgdc_shb_in_scan(int dgdc_shb_in_scan) {
		this.dgdc_shb_in_scan = dgdc_shb_in_scan;
	}



	public int getDgdc_shb_out_scan() {
		return dgdc_shb_out_scan;
	}



	public void setDgdc_shb_out_scan(int dgdc_shb_out_scan) {
		this.dgdc_shb_out_scan = dgdc_shb_out_scan;
	}



	public int getDgdc_cargo_in_scan() {
		return dgdc_cargo_in_scan;
	}



	public void setDgdc_cargo_in_scan(int dgdc_cargo_in_scan) {
		this.dgdc_cargo_in_scan = dgdc_cargo_in_scan;
	}



	public int getDgdc_cargo_out_scan() {
		return dgdc_cargo_out_scan;
	}



	public void setDgdc_cargo_out_scan(int dgdc_cargo_out_scan) {
		this.dgdc_cargo_out_scan = dgdc_cargo_out_scan;
	}



	public Date getOutDate() {
		return outDate;
	}



	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}



	public Date getAirLineDate() {
		return airLineDate;
	}



	public void setAirLineDate(Date airLineDate) {
		this.airLineDate = airLineDate;
	}





//Handed over to console

	public ExportSHB(String sbNo, String erNo, Date sbDate, String nameOfExporter, Double grossWeight,
			String parcelStatus, String dgdcStatus, int noOfPackages) {
		super();
		this.sbNo = sbNo;
		this.erNo = erNo;
		this.sbDate = sbDate;
		this.nameOfExporter = nameOfExporter;
		this.grossWeight = grossWeight;
		this.parcelStatus = parcelStatus;
		this.dgdcStatus = dgdcStatus;
		this.noOfPackages = noOfPackages;
	}





//export tp

	public ExportSHB(String erNo, Date erDate, String nameOfExporter, String portOfDestination,
			String descriptionOfGoods, BigDecimal fobValueInINR, int noOfPackages) {
		super();
		this.erNo = erNo;
		this.erDate = erDate;
		this.nameOfExporter = nameOfExporter;
		this.portOfDestination = portOfDestination;
		this.descriptionOfGoods = descriptionOfGoods;
		this.fobValueInINR = fobValueInINR;
		this.noOfPackages = noOfPackages;
	}





//tp report

	public ExportSHB(String tpNo,String consoleAgent) {
		super();
		this.tpNo = tpNo;
		this.consoleAgent = consoleAgent;
	}







public ExportSHB(String sbNo, String erNo, String nameOfExporter, Double grossWeight, String portOfDestination,
		String airwayBillNo, int noOfPackages, String hawb) {
	super();
	this.sbNo = sbNo;
	this.erNo = erNo;
	this.nameOfExporter = nameOfExporter;
	this.grossWeight = grossWeight;
	this.portOfDestination = portOfDestination;
	this.airwayBillNo = airwayBillNo;
	this.noOfPackages = noOfPackages;
	this.hawb = hawb;
}
	
	//pctm report
	
	
public ExportSHB(String companyId, String branchId, String sbNo, String nameOfExporter, String serNo, Date serDate,
		Double grossWeight, BigDecimal fobValueInINR, int noOfPackages, Date outDate) {
	super();
	this.companyId = companyId;
	this.branchId = branchId;
	this.sbNo = sbNo;
	this.nameOfExporter = nameOfExporter;
	this.erNo = serNo;
	this.erDate = serDate;
	this.grossWeight = grossWeight;
	this.fobValueInINR = fobValueInINR;
	this.noOfPackages = noOfPackages;
	this.outDate = outDate;
}

	
	
	
	
	
	
}
