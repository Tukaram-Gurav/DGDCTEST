package com.Schedular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import com.cwms.entities.Branch;
import com.cwms.entities.CfsTarrif;
import com.cwms.entities.DemuragePackagesHistory;
import com.cwms.entities.EmailScedulerEntity;
import com.cwms.entities.Export;
import com.cwms.entities.ExportHeavyPackage;
import com.cwms.entities.ExportSub;
import com.cwms.entities.Import;
import com.cwms.entities.ImportHeavyPackage;
import com.cwms.entities.ImportSub;
import com.cwms.entities.InvoiceDetail;
import com.cwms.entities.InvoiceMain;
import com.cwms.entities.InvoicePackages;
import com.cwms.entities.InvoiceTaxDetails;
import com.cwms.entities.Party;
import com.cwms.entities.PredictableInvoiceMain;
import com.cwms.entities.PredictableInvoicePackages;
import com.cwms.entities.PredictableInvoiceTaxDetails;
import com.cwms.entities.Stock;
import com.cwms.entities.StockDetention;
import com.cwms.helper.CombinedImportExport;
import com.cwms.invoice.InvoiceDataStorage;
import com.cwms.invoice.ServiceIdMappingRepositary;
import com.cwms.repository.BranchRepo;
import com.cwms.repository.EmailScedulerRepositary;
import com.cwms.repository.ExportHeavyPackageRepo;
import com.cwms.repository.ExportRepository;
import com.cwms.repository.ExportSubRepository;
import com.cwms.repository.ImportRepo;
import com.cwms.repository.ImportSubRepository;
import com.cwms.repository.InvoiceDetailSHBRepo;
import com.cwms.repository.InvoicePackagesRepositary;
import com.cwms.repository.InvoiceRepositary;
import com.cwms.repository.PartyRepository;
import com.cwms.repository.PredictableInvoiceMainRepositary;
import com.cwms.repository.PredictableInvoicePackagesRepositary;
import com.cwms.repository.PredictableInvoiceTaxDetailsRepo;
import com.cwms.repository.StockDetentionRepositary;
import com.cwms.repository.StockRepositary;
import com.cwms.service.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InvoiceScheduler {	
	
	
	@Autowired
	public StockDetentionRepositary StockDetentionRepositary;

	@Autowired
	public StockRepositary StockRepositary;
	
	@Autowired
	private PartyService PartyService;

	
	@Autowired
	public	EmailService EmailService;
	
	@Autowired
	public PartyRepository partyRepository; 
	
	@Autowired
	public EmailScedulerRepositary emailScedulerRepo;
	
	
	@Autowired
	public ProcessNextIdService proccessNextIdService;

	@Autowired
	public ImportRepo importRepo;
	

	@Autowired
	public InvoiceRepositary invoiceRepository;


	@Autowired
	public ImportSubRepository impsubRepo;
	
	@Autowired
	public ExportSubRepository expsubRepo;
	
	@Autowired
	public CFSService CFSService;

	@Autowired
	private ServiceIdMappingRepositary ServiceIdMappingRepositary;


	@Autowired
	public cfsTarrifServiceService cfsTarrifServiceService;

	@Autowired
	public CFSTariffRangeService CFSTariffRangeService;

	@Autowired
	private InvoiceDetailServiceIMPL InvoiceDetailServiceIMPL;

	@Autowired
	private InvoiceServiceIMPL invoiceServiceIMPL;

	@Autowired
	private InvoiceTaxDetailsServiceIMPL InvoiceTaxDetailsServiceIMPL;

	@Autowired
	private BranchRepo branchRepo;

	@Autowired
	private InvoicePackagesRepositary InvoicePackagesRepositary;

	@Autowired
	public ExportService ExportService;

	@Autowired
	public ImportHeavyService ImportHeavyService;

	@Autowired
	public ExportHeavyPackageRepo ExportHeavyPackageRepo;

	@Autowired
	public ImportService importService;

	@Autowired
	public InvoiceService InvoiceService;
	
	
	
	
//	To calculate a invoices at 7.00 PM
	@Scheduled(cron = "0 24 16 * * *")
	public void calculateInvoice()
	{
		
		System.out.println("In Schedular ***************");
		
		List<Object[]> findDistinctCompanyIdAndBranchId = branchRepo.findDistinctCompanyIdAndBranchId();
		
		
		
		for (Object[] pair : findDistinctCompanyIdAndBranchId) {
		    String companyid = (String) pair[0];
		    String branchId = (String) pair[1];    
		    
		    System.out.println("companyid : "+companyid + "branchId :"+branchId);
		    List<Party> party = partyRepository.getNonBilledAllPartieIds(companyid, branchId);
		    System.out.println(party);
		    for(Party p : party)
		    {   
		    	System.out.println("party :"+p);
		    	InvoiceService.generateInvoice(companyid, branchId, p);		    	
		    }	    
		    
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//@Scheduled(cron = "0 20 12 12 1 *")
public void SendingEmailToParty() throws Exception
{ 

	
	List<Object[]> findDistinctCompanyIdAndBranchId = branchRepo.findDistinctCompanyIdAndBranchId();
	
	
	
	for (Object[] pair : findDistinctCompanyIdAndBranchId) {
	    String companyid = (String) pair[0];
	    String branchId = (String) pair[1];    
	    
	    List<EmailScedulerEntity> EmailScedulerEntity = emailScedulerRepo.findByCompanyIdAndBranchIdAndMailFlagNot(companyid,branchId,"Y");
	    
	    if(EmailScedulerEntity != null  && !EmailScedulerEntity.isEmpty())
		{
	    
	    for(EmailScedulerEntity emails : EmailScedulerEntity)
	    {
	    	
	    	Party PartyId = partyRepository.findByCompanyIdAndBranchIdAndPartyId(companyid, branchId, emails.getPartyId());
	    	
//	    	Bill 
	    	List<InvoiceTaxDetails> findByInvoiceNo = InvoiceTaxDetailsServiceIMPL.findByInvoiceNo(companyid, branchId, emails.getPartyId(), emails.getInvoiceNO());
	    	byte[] functionForBillGeneration = invoiceServiceIMPL.FunctionForBillGeneration(companyid, branchId, emails.getInvoiceNO(), findByInvoiceNo);
	    	
	    	byte[] functionSingleInvice = invoiceServiceIMPL.FunctionSingleInvice(companyid, branchId, emails.getInvoiceNO(), findByInvoiceNo);
	    	
//	    	String body = "<html><body><p>Body of the email</p></body></html>";
	    	
	    	String additionalParagraph = "<p>Dear Sir/Madam,</p>"
	    	        + "<p>Please find below Invoice and Bill Documents for the Date From  <b>"+emails.getPeriodFrom()+" </b> to <b>"+ emails.getPeriodTo() 
	    	        + "</b> for The Party <b>"+PartyId.getPartyName()+"</b>.</p>";
	    	
	    	String body = "<html><body>" + additionalParagraph + "</body></html>";
	    	boolean sendEmail = EmailService.sendEmailWithTwoPdfAttachments("tukaram8805@gmail.com", functionSingleInvice,functionForBillGeneration ,body,"tukaram1030@gmail.com","Invoice.pdf","Bill.pdf");
	    	
//	    	EmailService.sendEmailWithPdfAttachment("tukaram8805@gmail.com", functionForBillGeneration, "tukaram1030@gmail.com","Bill.pdf");
	    	if(sendEmail == true)
	    	{
	    		emails.setMailFlag("Y");
	    		emailScedulerRepo.save(emails);
	    	}	    	
	    }
	    }    
	
	
	 ScheduledTaskRegistrar taskRegistrar = new ScheduledTaskRegistrar();
     taskRegistrar.destroy();
 }
//	}
	
}



private Date parseDateString(String dateString) {
    try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(dateString);
    } catch (ParseException e) {
        // Handle parsing exception
        e.printStackTrace();
        return null; // or throw an exception
    }
}









 private static Date truncateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Set time components to midnight
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

 
//StockRepositary


//	@Scheduled(cron = "0 40 15 9 3 *")
 @Scheduled(cron = "0 55 23 * * *")
	public void insertStock() {

		System.out.println("In @Scheduled");
		Stock stock = new Stock();

		List<Object[]> findDistinctCompanyIdAndBranchId = branchRepo.findDistinctCompanyIdAndBranchId();

		for (Object[] pair : findDistinctCompanyIdAndBranchId) {
			String companyid = (String) pair[0];
			String branchId = (String) pair[1];
	

				Object[] result1 = StockRepositary.getStocksResults(companyid, branchId);
				Object[] result = (Object[]) result1[0];
				stock.setCompanyId(companyid);
				stock.setBranchId(branchId);
				stock.setStockDate(new Date());
				stock.setImportIn(Integer.parseInt(result[0].toString()));
				stock.setImportNiptIn(Integer.parseInt(result[1].toString()));
				stock.setImportPcIn(Integer.parseInt(result[2].toString()));
				stock.setImportSubIn(Integer.parseInt(result[3].toString()));

				stock.setExportIn(Integer.parseInt(result[4].toString()));
				stock.setExportPcIn(Integer.parseInt(result[5].toString()));
				stock.setExportSubIn(Integer.parseInt(result[6].toString()));

				stock.setImportOut(Integer.parseInt(result[7].toString()));
				stock.setImportNiptOut(Integer.parseInt(result[8].toString())); // Assuming this should be a different
																				// index
				stock.setImportPcOut(Integer.parseInt(result[9].toString()));
				stock.setImportSubOut(Integer.parseInt(result[10].toString()));

				stock.setExportOut(Integer.parseInt(result[11].toString()));
				stock.setExportPcOut(Integer.parseInt(result[12].toString()));
				stock.setExportSubOut(Integer.parseInt(result[13].toString()));

				stock.setImportStock(Integer.parseInt(result[14].toString()));
				stock.setImportNiptStock(Integer.parseInt(result[15].toString()));
				stock.setImportPcStock(Integer.parseInt(result[16].toString()));
				stock.setImportSubStock(Integer.parseInt(result[17].toString()));

				stock.setExportStock(Integer.parseInt(result[18].toString()));
				stock.setExportPcStock(Integer.parseInt(result[19].toString()));
				stock.setExportSubStock(Integer.parseInt(result[20].toString()));

				 if (stock.getImportOut() > 0 || stock.getImportIn() > 0 || stock.getImportNiptOut() > 0 ||
		                    stock.getImportNiptIn() > 0 || stock.getImportPcIn() > 0 || stock.getImportPcIn() > 0 ||
		                    stock.getExportOut() > 0 || stock.getExportIn() > 0 || stock.getExportPcIn() > 0 ||
		                    stock.getExportPcIn() > 0 || stock.getImportSubOut() > 0 || stock.getImportSubIn() > 0 ||
		                    stock.getExportSubOut() > 0 || stock.getExportSubIn() > 0) {

		                    // Save the Stock object to the repository
		                    StockRepositary.save(stock);
		     }
		}
	}

    @Scheduled(cron = "0 55 23 * * *")
	public void insertDetentionStock() {		
		StockDetention stock = new StockDetention();
		List<Object[]> findDistinctCompanyIdAndBranchId = branchRepo.findDistinctCompanyIdAndBranchId();

		for (Object[] pair : findDistinctCompanyIdAndBranchId) {
			String companyid = (String) pair[0];
			String branchId = (String) pair[1];
							  
				 Object[] result1 = StockDetentionRepositary.getCombinedResults(companyid, branchId);
				    Object[] result = (Object[]) result1[0];
				
			    int importDetentionIn = Integer.parseInt(result[0].toString());
			    int exportDetentionIn = Integer.parseInt(result[1].toString());
			    int importDetentionOut = Integer.parseInt(result[2].toString());
			    int exportDetentionOut = Integer.parseInt(result[3].toString());

			    // Check if any of the values is greater than 0 before saving
			    if (importDetentionIn > 0 || exportDetentionIn > 0 || importDetentionOut > 0 || exportDetentionOut > 0) {
			        stock.setCompanyId(companyid);
			        stock.setBranchId(branchId);
			        stock.setStockDate(new Date());
			        stock.setImportDetentionIn(importDetentionIn);
			        stock.setExportDetentionIn(exportDetentionIn);
			        stock.setImportDetentionOut(importDetentionOut);
			        stock.setExportDetentionOut(exportDetentionOut);
			        stock.setImportDetentionStock(Integer.parseInt(result[4].toString()));
			        stock.setExportDetentionStock(Integer.parseInt(result[5].toString()));
			        
			        StockDetentionRepositary.save(stock);
			    }
		}
	}
 
 
 


}