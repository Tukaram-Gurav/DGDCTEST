package com.cwms.entities;

import java.io.Serializable;

public class PredictableInvoiceMainId implements Serializable
{
	
	private String companyId;
	private String branchId;
	private String predictableinvoiceNO;
	private String partyId;
	
	
	public PredictableInvoiceMainId(String companyId, String branchId, String predictableinvoiceNO, String partyId) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.predictableinvoiceNO = predictableinvoiceNO;
		this.partyId = partyId;
	}
	public PredictableInvoiceMainId() {
		super();
		// TODO Auto-generated constructor stub
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
	
	
	
	

}
