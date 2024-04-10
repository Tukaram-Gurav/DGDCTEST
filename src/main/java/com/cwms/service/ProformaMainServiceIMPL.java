package com.cwms.service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.ToDoubleFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.cwms.entities.Branch;
import com.cwms.entities.InvoiceMain;
import com.cwms.entities.InvoicePackages;
import com.cwms.entities.InvoiceTaxDetails;
import com.cwms.entities.Party;
import com.cwms.entities.ProformaMain;
import com.cwms.invoice.ServiceIdMappingRepositary;
import com.cwms.repository.BranchRepo;
import com.cwms.repository.InvoicePackagesRepositary;
import com.cwms.repository.InvoiceRepositary;

@Service
public class ProformaMainServiceIMPL 
{	
	@Autowired
	private com.cwms.repository.ProformaMainRepositary ProformaMainRepositary;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	public cfsTarrifServiceService cfsTarrifServiceService;

	@Autowired
	public CFSTariffRangeService CFSTariffRangeService;
	
	@Autowired
	private PartyService PartyService;
	
	@Autowired
	private BranchRepo branchRepo;
	
	@Autowired
	private com.cwms.repository.ProformaPackagesRepositary ProformaPackagesRepositary;
	
	@Autowired
	private ServiceIdMappingRepositary ServiceIdMappingRepositary;
	
//	@Autowired
//	private InvoiceDetailRepostary detailRepostary;
	
	
	public List<String> getInvoiceNoListByPartyId(String compId,String branchId,String partyid,Date StartDate,Date endDate)
	{
		return ProformaMainRepositary.findInvoiceNumbersByCriteria(compId, branchId, partyid,StartDate,endDate);
	}
	
	public ProformaMain addInvoice(ProformaMain invoiceMain)
	{		
		return ProformaMainRepositary.save(invoiceMain);
	}
	
	public ProformaMain getByInvoiceNo(String compId,String branchId,String partyid,String invoiceNo)
	{
		return ProformaMainRepositary.findByCompanyIdAndBranchIdAndPartyIdAndProformaNo(compId, branchId, partyid, invoiceNo);
	}
	
	public List<ProformaMain> getByPartyId(String compId,String branchId,String partyid)
	{
		return ProformaMainRepositary.findByCompanyIdAndBranchIdAndPartyId(compId, branchId, partyid);
	}
	
	public List<ProformaMain> getByPartyIdBetweenDate(String compId,String branchId,String partyid,Date startDate ,Date endDate)
	{
		return ProformaMainRepositary.findByCompanyIdAndBranchIdAndPartyIdAndProformaDateBetween(compId, branchId, partyid,startDate,endDate);
	}


}