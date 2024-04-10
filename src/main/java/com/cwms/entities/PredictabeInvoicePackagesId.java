package com.cwms.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class PredictabeInvoicePackagesId  implements Serializable
{

	private Long SrNo;
	private String companyId;
	private String branchId;
	private String partyId;
	private String predictableinvoiceNO;
	 
	 
	
	public void setSrNo(Long srNo) {
		SrNo = srNo;
	}
	public void setPredictableinvoiceNO(String predictableinvoiceNO) {
		this.predictableinvoiceNO = predictableinvoiceNO;
	}
	public String getInvoiceId() {
		return predictableinvoiceNO;
	}
	public void setInvoiceId(String predictableinvoiceNO) {
		this.predictableinvoiceNO = predictableinvoiceNO;
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
	
	
	
	  public PredictabeInvoicePackagesId(Long srNo, String companyId, String branchId, String partyId,
			  String predictableinvoiceNO) {
		super();
		SrNo = srNo;
		this.companyId = companyId;
		this.branchId = branchId;
		this.partyId = partyId;
		this.predictableinvoiceNO = predictableinvoiceNO;
	}
	public PredictabeInvoicePackagesId() {
	    }
	  
	  
	// Overrides equals and hashCode
//
//	    @Override
//	    public boolean equals(Object o) {
//	        if (this == o) return true;
//	        if (o == null || getClass() != o.getClass()) return false;
//	        PredictabeInvoicePackagesId that = (PredictabeInvoicePackagesId) o;
//	        return Objects.equals(companyId, that.companyId) &&
//	               Objects.equals(branchId, that.branchId) &&
//	               Objects.equals(partyId, that.partyId) &&
//	               Objects.equals(predictableinvoiceNO, that.predictableinvoiceNO) &&
//	               Objects.equals(calculatedDate, that.calculatedDate);
//	    }
//
//	    @Override
//	    public int hashCode() {
//	        return Objects.hash(companyId, branchId, partyId,predictableinvoiceNO,calculatedDate);
//	    }
	 
	
	 
}
