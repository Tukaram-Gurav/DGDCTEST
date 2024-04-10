package com.cwms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cwms.entities.InvoiceTaxDetails;
import com.cwms.entities.ProformaTaxDetails;

public interface ProformaTaxDetailsRepo extends JpaRepository<ProformaTaxDetails, String>
{
	List<ProformaTaxDetails> findByCompanyIdAndBranchIdAndPartyIdAndProformaNo(String companyId,String branchId,String partyId,String invoiceno);
	
	List<ProformaTaxDetails> findByCompanyIdAndBranchIdAndProformaNo(String companyId,String branchId,String invoiceno);
	
	
}
