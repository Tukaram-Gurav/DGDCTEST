package com.cwms.entities;

import java.io.Serializable;

public class FinTransDtlId implements Serializable {

    private Long srno;
    private String branchId;
    private String invoiceNo;
    private String transId;

    // Constructors, getters, and setters

    // Default constructor
    public FinTransDtlId() {
    }

    // Constructor with all ID fields
    public FinTransDtlId(Long srno, String branchId, String invoiceNo, String transId) {
        this.srno = srno;
        this.branchId = branchId;
        this.invoiceNo = invoiceNo;
        this.transId = transId;
    }

    // Getters and setters for ID fields
    public Long getSrno() {
        return srno;
    }

    public void setSrno(Long srno) {
        this.srno = srno;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    // Implement equals() and hashCode() methods for proper composite key functionality
    // You should compare each field individually

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FinTransDtlId that = (FinTransDtlId) o;

        if (srno != null ? !srno.equals(that.srno) : that.srno != null) return false;
        if (branchId != null ? !branchId.equals(that.branchId) : that.branchId != null) return false;
        if (invoiceNo != null ? !invoiceNo.equals(that.invoiceNo) : that.invoiceNo != null) return false;
        return transId != null ? transId.equals(that.transId) : that.transId == null;
    }

    @Override
    public int hashCode() {
        int result = srno != null ? srno.hashCode() : 0;
        result = 31 * result + (branchId != null ? branchId.hashCode() : 0);
        result = 31 * result + (invoiceNo != null ? invoiceNo.hashCode() : 0);
        result = 31 * result + (transId != null ? transId.hashCode() : 0);
        return result;
    }
}
