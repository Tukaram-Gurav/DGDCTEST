package com.cwms.entities;

import java.io.Serializable;

public class FinTransId implements Serializable {

    private Long srno;
    private String branchId;
    private String companyId;
    private String docType;
    private String transId;

    // Constructors, getters, and setters
    // Make sure to implement equals() and hashCode() methods

    // Default constructor
    public FinTransId() {
    }

    // Constructor with all ID fields
    public FinTransId(Long srno, String branchId, String companyId, String docType, String transId) {
        this.srno = srno;
        this.branchId = branchId;
        this.companyId = companyId;
        this.docType = docType;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
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

        FinTransId that = (FinTransId) o;

        if (srno != null ? !srno.equals(that.srno) : that.srno != null) return false;
        if (branchId != null ? !branchId.equals(that.branchId) : that.branchId != null) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (docType != null ? !docType.equals(that.docType) : that.docType != null) return false;
        return transId != null ? transId.equals(that.transId) : that.transId == null;
    }

    @Override
    public int hashCode() {
        int result = srno != null ? srno.hashCode() : 0;
        result = 31 * result + (branchId != null ? branchId.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (docType != null ? docType.hashCode() : 0);
        result = 31 * result + (transId != null ? transId.hashCode() : 0);
        return result;
    }
}
