package com.cwms.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cwms.entities.PredictableInvoiceMain;
import com.cwms.entities.PredictableInvoiceTaxDetails;

public interface PredictableInvoiceMainRepositary extends JpaRepository<PredictableInvoiceMain, String>
{
	
	List<PredictableInvoiceMain> findByCompanyIdAndBranchIdAndInvoiceDateBetween( String companyId, String branchId, Date startDate, Date endDate);

	List<PredictableInvoiceMain> findByCompanyIdAndBranchIdAndPartyIdAndInvoiceDateBetween( String companyId, String branchId, String partyId,Date startDate, Date endDate);
		
	PredictableInvoiceMain findByCompanyIdAndBranchIdAndPredictableinvoiceNO(String companyId, String branchId,String invoiceNo);
}
