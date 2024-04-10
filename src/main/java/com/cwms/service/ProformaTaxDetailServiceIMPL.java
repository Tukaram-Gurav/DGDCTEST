package com.cwms.service;

import java.util.Date;
import java.util.List;
import com.cwms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwms.entities.DemuragePackagesHistory;
import com.cwms.entities.InvoiceTaxDetails;
import com.cwms.entities.ProformaPackagesHistory;
import com.cwms.entities.ProformaTaxDetails;

@Service
public class ProformaTaxDetailServiceIMPL 
{
	@Autowired
	private ProformaTaxDetailsRepo ProformaTaxDetailsRepo;
	
	@Autowired
	private	ProformaPackagesHistoryRepositary demorepositary;
	
	
	public List<ProformaPackagesHistory> getbyDemuragesByPartyIdAndInvoiceNumber(String companyId,String branchId,String partyId,Date startDate,Date endDate,String invoiceno)
	{
		return demorepositary.findDemuragePackagesByCriteria(companyId, branchId, partyId, startDate, endDate, invoiceno);
	}
	
	
	public List<ProformaTaxDetails> findByInvoiceNo(String companyId,String branchId,String partyId,String invoiceno)
	{
		return ProformaTaxDetailsRepo.findByCompanyIdAndBranchIdAndPartyIdAndProformaNo(companyId, branchId, partyId, invoiceno);
	}
	
	public ProformaTaxDetails saveInvoiceTaxDetails(ProformaTaxDetails InvoiceTaxDetails)
	{
		return ProformaTaxDetailsRepo.save(InvoiceTaxDetails);
	}
	
	public ProformaPackagesHistory saveDemuragePackage(ProformaPackagesHistory demorage)
	{
		return demorepositary.save(demorage);
	}
	
	public List<ProformaPackagesHistory> getbyDemuragesByPartyId(String companyId,String branchId,String partyId,String invoiceno)
	{
		return demorepositary.findByCompanyIdAndBranchIdAndPartyIdAndProformaNo(companyId, branchId, partyId, invoiceno);
	}
	
}
