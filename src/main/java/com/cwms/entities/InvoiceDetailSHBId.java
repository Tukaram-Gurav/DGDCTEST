package com.cwms.entities;

import java.io.Serializable;
import java.util.Objects;

public class InvoiceDetailSHBId implements Serializable
{
	 private Long SrlNo;
		private String companyId;
		private String branchId;
		private String invoiceNO;
		private String serviceId;
		private String partyId;
		public Long getSrlNo() {
			return SrlNo;
		}
		public void setSrlNo(Long srlNo) {
			SrlNo = srlNo;
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
		public String getInvoiceNO() {
			return invoiceNO;
		}
		public void setInvoiceNO(String invoiceNO) {
			this.invoiceNO = invoiceNO;
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
		public InvoiceDetailSHBId() {
			super();
			// TODO Auto-generated constructor stub
		}
		public InvoiceDetailSHBId(Long srlNo, String companyId, String branchId, String invoiceNO, String serviceId,
				String partyId) {
			super();
			SrlNo = srlNo;
			this.companyId = companyId;
			this.branchId = branchId;
			this.invoiceNO = invoiceNO;
			this.serviceId = serviceId;
			this.partyId = partyId;
		}
		@Override
		public int hashCode() {
			return Objects.hash(SrlNo, branchId, companyId, invoiceNO, partyId, serviceId);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			InvoiceDetailSHBId other = (InvoiceDetailSHBId) obj;
			return Objects.equals(SrlNo, other.SrlNo) && Objects.equals(branchId, other.branchId)
					&& Objects.equals(companyId, other.companyId) && Objects.equals(invoiceNO, other.invoiceNO)
					&& Objects.equals(partyId, other.partyId) && Objects.equals(serviceId, other.serviceId);
		}
		
		
		
}
