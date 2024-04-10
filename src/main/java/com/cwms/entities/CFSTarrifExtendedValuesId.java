package com.cwms.entities;

import java.io.Serializable;
import java.util.Objects;

public class CFSTarrifExtendedValuesId implements Serializable 
{
	private String companyId;
	private String branchId;
	private String cfsTariffNo;
	private String cfsAmndNo;
	private String serviceId;
	private String partyId;
	private Long SrNo;
	private int srlNo;
	public CFSTarrifExtendedValuesId() {
		super();
		// TODO Auto-generated constructor stub
	}


	public CFSTarrifExtendedValuesId(String companyId, String branchId, String cfsTariffNo, String cfsAmndNo,
			String serviceId, String partyId, Long srNo, int srlNo) {
		super();
		this.companyId = companyId;
		this.branchId = branchId;
		this.cfsTariffNo = cfsTariffNo;
		this.cfsAmndNo = cfsAmndNo;
		this.serviceId = serviceId;
		this.partyId = partyId;
		SrNo = srNo;
		this.srlNo = srlNo;
	}


	
	public int getSrlNo() {
		return srlNo;
	}


	public void setSrlNo(int srlNo) {
		this.srlNo = srlNo;
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
	public String getCfsTariffNo() {
		return cfsTariffNo;
	}
	public void setCfsTariffNo(String cfsTariffNo) {
		this.cfsTariffNo = cfsTariffNo;
	}
	public String getCfsAmndNo() {
		return cfsAmndNo;
	}
	public void setCfsAmndNo(String cfsAmndNo) {
		this.cfsAmndNo = cfsAmndNo;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public Long getSrNo() {
		return SrNo;
	}
	public void setSrNo(Long srNo) {
		SrNo = srNo;
	}


	@Override
	public int hashCode() {
		return Objects.hash(SrNo, branchId, cfsAmndNo, cfsTariffNo, companyId, partyId, serviceId, srlNo);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CFSTarrifExtendedValuesId other = (CFSTarrifExtendedValuesId) obj;
		return Objects.equals(SrNo, other.SrNo) && Objects.equals(branchId, other.branchId)
				&& Objects.equals(cfsAmndNo, other.cfsAmndNo) && Objects.equals(cfsTariffNo, other.cfsTariffNo)
				&& Objects.equals(companyId, other.companyId) && Objects.equals(partyId, other.partyId)
				&& Objects.equals(serviceId, other.serviceId) && srlNo == other.srlNo;
	}
	
	
	

}
