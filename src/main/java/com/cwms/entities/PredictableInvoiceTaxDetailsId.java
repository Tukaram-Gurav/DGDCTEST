package com.cwms.entities;
import java.io.Serializable;

public class PredictableInvoiceTaxDetailsId implements Serializable
{
		
	private Long SrNo;
	private String companyId;
	private String branchId;
	private String predictableinvoiceNO;
	private String partyId;	
	
	
	
	
	public Long getSrNo() {
		return SrNo;
	}
	public void setSrNo(Long srNo) {
		SrNo = srNo;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
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
	public PredictableInvoiceTaxDetailsId() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PredictableInvoiceTaxDetailsId(Long srNo, String companyId, String branchId, String predictableinvoiceNO,
			String partyId) {
		super();
		SrNo = srNo;
		this.companyId = companyId;
		this.branchId = branchId;
		this.predictableinvoiceNO = predictableinvoiceNO;
		this.partyId = partyId;
	}
	
	

	
	
	
	
	

}
