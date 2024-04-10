package com.cwms.entities;

import java.io.Serializable;
import java.util.Objects;

public class ImportSHBID implements Serializable
{
	 private String companyId;
	 private String branchId;
	 private String impTransId;
	 private String mawb;
	 private String sirNo;
	 
	@Override
	public int hashCode() {
		return Objects.hash(branchId, companyId, impTransId, mawb, sirNo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImportSHBID other = (ImportSHBID) obj;
		return Objects.equals(branchId, other.branchId) && Objects.equals(companyId, other.companyId)
				&& Objects.equals(impTransId, other.impTransId) && Objects.equals(mawb, other.mawb)
				&& Objects.equals(sirNo, other.sirNo);
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
	public String getImpTransId() {
		return impTransId;
	}
	public void setImpTransId(String impTransId) {
		this.impTransId = impTransId;
	}
	public String getMawb() {
		return mawb;
	}
	public void setMawb(String mawb) {
		this.mawb = mawb;
	}
	public String getSirNo() {
		return sirNo;
	}
	public void setSirNo(String sirNo) {
		this.sirNo = sirNo;
	}
	
	 
	
	
}
