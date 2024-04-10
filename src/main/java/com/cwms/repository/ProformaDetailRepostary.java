package com.cwms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cwms.entities.InvoiceDetail;
import com.cwms.entities.ProformaDetail;

public interface ProformaDetailRepostary extends JpaRepository<ProformaDetail, String>
{
 List<ProformaDetail> findByCompanyIdAndBranchIdAndProformaNo(String companyId,String branchId,String proformaNo);
}
