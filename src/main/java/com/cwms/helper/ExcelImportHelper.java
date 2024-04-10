package com.cwms.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cwms.entities.Airline;
import com.cwms.entities.Branch;
import com.cwms.entities.CFSTariffRange;
import com.cwms.entities.DefaultPartyDetails;
import com.cwms.entities.Detention;
import com.cwms.entities.Export;
import com.cwms.entities.ExportPC;
import com.cwms.entities.ExportSub;
import com.cwms.entities.ExportSub_History;
import com.cwms.entities.Export_History;
import com.cwms.entities.ExternalParty;
import com.cwms.entities.Gate_In_Out;
import com.cwms.entities.Import;
import com.cwms.entities.ImportPC;
import com.cwms.entities.ImportSub;
import com.cwms.entities.ImportSub_History;
import com.cwms.entities.Import_History;
import com.cwms.entities.InvoiceMain;
import com.cwms.entities.InvoicePackages;
import com.cwms.entities.InvoiceTaxDetails;
import com.cwms.entities.Party;
import com.cwms.entities.PredictableInvoiceMain;
import com.cwms.entities.PredictableInvoicePackages;
import com.cwms.entities.PredictableInvoiceTaxDetails;
import com.cwms.entities.RepresentParty;
import com.cwms.entities.Stock;
import com.cwms.entities.StockDetention;
import com.cwms.entities.User;
import com.cwms.repository.AirlineRepository;
import com.cwms.repository.BranchRepo;
import com.cwms.repository.DefaultParyDetailsRepository;
import com.cwms.repository.DetantionRepository;
import com.cwms.repository.Detention_HistoryRepository;
import com.cwms.repository.ExportHeavyPackageRepo;
import com.cwms.repository.ExportPCRepo;
import com.cwms.repository.ExportRepository;
import com.cwms.repository.ExportSubRepository;
import com.cwms.repository.ExportSub_Historyrepo;
import com.cwms.repository.Export_HistoryRepository;
import com.cwms.repository.ExternalPartyRepository;
import com.cwms.repository.Gate_In_out_Repo;
import com.cwms.repository.ImportPCRepositary;
import com.cwms.repository.ImportRepo;
import com.cwms.repository.ImportSubHistoryRepo;
import com.cwms.repository.ImportSubRepository;
import com.cwms.repository.Import_HistoryRepo;
import com.cwms.repository.InvoicePackagesRepositary;
import com.cwms.repository.InvoiceRepositary;
import com.cwms.repository.PartyRepository;
import com.cwms.repository.PredictableInvoiceMainRepositary;
import com.cwms.repository.PredictableInvoicePackagesRepositary;
import com.cwms.repository.PredictableInvoiceTaxDetailsRepo;
import com.cwms.repository.RepresentPartyRepository;
import com.cwms.repository.StockDetentionRepositary;
import com.cwms.repository.StockRepositary;
import com.cwms.repository.UserCreationRepository;
import com.cwms.repository.UserRepository;
import com.cwms.service.CFSService;
import com.cwms.service.CFSTariffRangeService;
import com.cwms.service.ExternalParty_Service;
import com.cwms.service.InvoiceServiceIMPL;
import com.cwms.service.InvoiceTaxDetailsServiceIMPL;
import com.cwms.service.ProcessNextIdService;

@Component
public class ExcelImportHelper {
	
	
	@Autowired
	public StockDetentionRepositary StockDetentionRepositary;

	@Autowired
	public StockRepositary StockRepositary;

	@Autowired
	 private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public CFSService CFSService;
	@Autowired
	public CFSTariffRangeService CFSTariffRangeService;
	
	@Autowired
	private  PredictableInvoiceMainRepositary PredictableInvoiceMainRepositary;
	
	@Autowired
	private PredictableInvoicePackagesRepositary predictableRepo;
	
	@Autowired
	private InvoicePackagesRepositary InvoicePackagesRepositary;
	@Autowired
	private InvoiceServiceIMPL invoiceServiceIMPL;
	
	@Autowired
	public InvoiceRepositary invoiceRepository;
	
	@Autowired
	private BranchRepo branchRepo;	
	
	@Autowired
	private PredictableInvoiceTaxDetailsRepo PredictableInvoiceTaxDetailsRepo;
	
	@Autowired
	private InvoiceTaxDetailsServiceIMPL InvoiceTaxDetailsServiceIMPL;	
	
	@Autowired
	private ExternalPartyRepository externalPartyRepository;
	
	@Autowired
	public Export_HistoryRepository exporthistoryrepo;
	
	@Autowired
	private ExportHeavyPackageRepo eexportHeavyRepo;
  @Autowired
	private ExportPCRepo exportpcrepo;
	
	@Autowired
	public DefaultParyDetailsRepository defaultrepo;
	
	@Autowired
	public ProcessNextIdService proccessNextIdService;
	
	@Autowired
	private ImportSubRepository impsubrepo;
	
	@Autowired
	private ImportSubHistoryRepo importsubhisrepo;
	
	@Autowired
	private ImportPCRepositary importpcrepo;
	
	@Autowired
	private ExportRepository exportrepo;
	
	@Autowired
	public PartyRepository partyrepo;
	
	@Autowired
	private ExportSub_Historyrepo exportsubhistory;
	
	@Autowired
	private Gate_In_out_Repo gateinoutrepo;
	
	
	@Autowired
	private ImportRepo importrepo;
	
	@Autowired
	public UserCreationRepository userCreationRepository;
	
	@Autowired
	public ExternalParty_Service externalpartyService;
	
	@Autowired
	public ExportSubRepository exportsubrepo;
	
	
	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private AirlineRepository airlinerepo;
	
	@Autowired
	private ExternalPartyRepository externalpartyrepo;
	
	@Autowired
	private RepresentPartyRepository representpartyrepo;
	
	@Autowired
	private Import_HistoryRepo importhistoHistoryRepo;
	
	@Autowired
	public DetantionRepository detentionRepository;

	@Autowired
	private Detention_HistoryRepository detentionHistoryRepository;
	
	
	private Date parseDate2(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.parse(dateString);
    }
	private Date parseDate5(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.parse(dateString);
    }
	
	 public static boolean checkFileFormat(MultipartFile file) {
	        String contentType = file.getContentType();
	        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || contentType.equals("text/csv")) {
	            return true;
	        } else {
	            return false;
	        }
	    }
	 
	

	public List<Party> convertFileToListOfParty(InputStream inputStream, String companyid, String branchId, String user_Id) throws Exception, UncheckedIOException {
	    InputStreamReader reader = new InputStreamReader(inputStream);
	    List<Party> list = new ArrayList<>();
	   
	    try {
	        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
	        
	        System.out.println("CSV Headers: " + csvParser.getHeaderNames());

	        for (CSVRecord record : csvParser) {
	        	Party party = new Party();
	        	
	        	String nextId = proccessNextIdService.autoIncrementNextIdNext();
	        	party.setPartyId(nextId);
	        	party.setCompanyId(companyid);
                party.setBranchId(branchId);
	        	
	        	
	        	String PartyName = record.get("PartyName");
	            String Email = record.get("Email");
	            String MobileNo = record.get("MobileNo");
	            String IECNo = record.get("IECNo");
	            String EntityId = record.get("EntityId");
	            String PanNo = record.get("PanNo");
	            String GstNo = record.get("GstNo");
	            String LoaNumber = record.get("LoaNumber");	
	        	Date currentDate = Calendar.getInstance().getTime();	        	
	        	String dateStr = record.get("LoaExpiryDate"); // Replace with your actual date string
	        	
	        	

	        	
//	        	if (dateStr != null && !dateStr.isEmpty()) {
//	        	    SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEEE, dd MMMM, yyyy", Locale.ENGLISH);
//					Date loaExpiryDate = inputDateFormat.parse(dateStr);
//					party.setLoaExpiryDate(loaExpiryDate); // Set the parsed date as a Date
//	        	} 
	        	
	        	
	        	ExternalParty  externalParty = new ExternalParty(); 		
	    		externalParty.setExternaluserId(nextId);
	    		externalParty.setCompanyId(companyid);
	    		externalParty.setBranchId(branchId);
	    		externalParty.setLoginPassword("Sanket@13");
	    		externalParty.setUserType("Party");
	    		externalParty.setStatus("N");
	    		externalParty.setCreatedBy(user_Id);
	    		externalParty.setApprovedBy(user_Id);
	    		externalParty.setCreatedDate(new Date());
	    		externalParty.setApprovedDate(new Date());		
	    		
	    		
	    		
	    		User externaluser = new User(); 
	    		
	    		externaluser.setCompany_Id(companyid);
	    		externaluser.setBranch_Id(branchId);
	    		externaluser.setUser_Type("Party");
	    		externaluser.setUser_Password("Sanket@13");	    		
	    		externaluser.setLogintype("Party");
	    		externaluser.setLogintypeid(nextId);
	    		externaluser.setCreated_Date(new Date());
	    		externaluser.setStatus("A");
	    		externaluser.setStop_Trans('N');
	    		externaluser.setOTP("1000");
	    				
	    		
	        	
	        	
	        	if (dateStr != null && !dateStr.isEmpty()) {
	        	    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
	        	    Date loaExpiryDate = inputDateFormat.parse(dateStr);
	        	    party.setLoaExpiryDate(loaExpiryDate); // Set the parsed date as a Date
	        	}

	            String CreditLimit = record.get("CreditLimit");
	            
	            if (CreditLimit != null && !CreditLimit.isEmpty()) {
	            	  double creditLimit1 = Double.parseDouble(CreditLimit);	
	            	  party.setCreditLimit(creditLimit1);
	            }
	          
	            
	            String Status = record.get("Status");
	           
	           
	            if (Status != null && !Status.isEmpty()) {
	            	 String status1 = String.valueOf(Status.charAt(0));
	            	party.setStatus(status1);	
	            }
	            
	            
	            if (PartyName != null && !PartyName.isEmpty()) {
//	            	party.setPartyName(PartyName);
//	            	
//	            	externalParty.setUserName(PartyName);
//	            	externaluser.setUser_Name(PartyName);
	            	
	            	  party.setPartyName(PartyName.toUpperCase());
	            	    
	            	    externalParty.setUserName(PartyName.toUpperCase());
	            	    externaluser.setUser_Name(PartyName.toUpperCase());
	            }
	            
	            if (Email != null && !Email.isEmpty()) {
	            	   party.setEmail(Email);
	            	   externalParty.setEmail(Email);
	            	   externaluser.setUser_Id(Email);
	            	  externalParty.setLoginUserName(Email);
	            	  
	            	System.out.println(Email+"Email123");
	            	  
	            }
	            	            
	            if (MobileNo != null && !MobileNo.isEmpty()) {
	            	 party.setMobileNo(MobileNo);	 
	            	 externalParty.setMobile(MobileNo);
	            	 
	            }
	            if (IECNo != null && !IECNo.isEmpty()) {
	            	 party.setIecNo(IECNo);
	            }
	            if (EntityId != null && !EntityId.isEmpty()) {
	            	 party.setEntityId(EntityId);
	            }
	            if (PanNo != null && !PanNo.isEmpty()) {
	            	party.setPanNo(PanNo);
	            }
	           
	            if (GstNo != null || !GstNo.isEmpty()) {
	            	 party.setGstNo(GstNo);
	            }
	            if (LoaNumber != null || !LoaNumber.isEmpty()) {
	            	 party.setLoaNumber(LoaNumber);
	            }
	           
	           
                                  
                party.setCreatedBy(user_Id);               
                party.setCreatedDate(currentDate);
                party.setAddress1("Pune");
                party.setAddress2("Pune");
                party.setAddress3("Pune");
                party.setUnitAdminName(user_Id);
                party.setUnitType(user_Id);
                
	            list.add(party);
	            
	            externalpartyService.addExternalParty(externalParty);
	            userCreationRepository.save(externaluser);
	        }
	    } catch (UncheckedIOException uioe) {
	        uioe.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        reader.close();
	    }
	    return list;
	}
	
	
	
//	public String convertFileToSubExp(InputStream inputStream, String companyid, String branchId, String user_Id)
//            throws IOException, ParseException {
//        InputStreamReader reader = new InputStreamReader(inputStream);
//        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
//
//        for (CSVRecord read : csvParser) {
//            String nexttransid = proccessNextIdService.autoIncrementSubExpTransId();
//            String reqid = read.get("request_id");
//            String approvedby = read.get("approved_by");
//            String approvedDate = read.get("approved_date");
//            String challanDate = read.get("challan_date");
//            String challanNo = read.get("challan_no");
//            String createdBy = read.get("created_by");
//            String createdDate = read.get("created_date");
//            String currency = read.get("currency");
//            String dgdcStatus = read.get("dgdc_status");
//            String editedBY = read.get("edited_by");
//            String editedDate = read.get("edited_date");
//            String exporter = read.get("exporter");
//            String grosswt = read.get("gw_weight");
//            String grosswtunit = read.get("gw_weight_unit");
//            String invoiceDate = read.get("invoice_date");
//            String invoiceNo = read.get("invoice_no");
//            String nop = read.get("nop");
//            
//            String noofpieces = read.get("nopieces");
//            String nsdlStatus = read.get("nsdl_status");
//            String passoutwt = read.get("passout_weight");
//            String passoutwtunit = read.get("passout_weight_unit");
//            String productValue = read.get("product_value");
//            String remarks = read.get("remarks");
//            String serDate = read.get("ser_date");
//            String serNo = read.get("ser_no");
//            String status = read.get("status");
//            String statusdoc = read.get("status_doc");
//            String handoverparchchatype = read.get("handover_party_cha");
//            String handoverpartychaname = read.get("handover_party_name");
//            String handoverrepresentative = read.get("handover_represntative_id");
//            String imposePenalty = read.get("impose_penalty_amt");
//            String imposePenaltyRemark = read.get("impose_penalty_remarks");
//            String receiverWt = read.get("received_wt");
//            String receivedWtUnit = read.get("received_wt_unit");
//            String cargoin = read.get("dgdc_cargo_in_scan");
//            String cargoout = read.get("dgdc_cargo_out_scan");
//            String seepzin = read.get("dgdc_seepz_in_scan");
//            String seepzout = read.get("dgdc_seepz_out_scan");
//            String noc = read.get("noc");
//            String outDate = read.get("out_date");
//            String forwardedStatus = read.get("forwarded_status");
//            String gatePass = read.get("gate_pass_status");
//            String mopStatus = read.get("mop_status");
//            Party party = partyrepo.getdatabypartyname(companyid, branchId, exporter);
//            ExportSub exportsub = new ExportSub();
//            if (!grosswt.isEmpty()) {
//                BigDecimal big = new BigDecimal(grosswt);
//                exportsub.setGwWeight(big);
//            }
//            if (!passoutwt.isEmpty()) {
//                BigDecimal big1 = new BigDecimal(passoutwt);
//                exportsub.setPassoutWeight(big1);
//            }
//            if (!productValue.isEmpty()) {
//                BigDecimal big2 = new BigDecimal(productValue);
//                exportsub.setProductValue(big2);
//            }
//            
//            System.out.println("challanDate "+challanDate);
//
//            exportsub.setCompanyId(companyid);
//            exportsub.setBranchId(branchId);
//            exportsub.setApprovedBy(user_Id);
//            exportsub.setApprovedDate(new Date());
//            exportsub.setChallanDate(parseDate2(challanDate));
//            exportsub.setChallanNo(challanNo);
//            exportsub.setCreatedBy(user_Id);
//            exportsub.setCreatedDate(parseDate(serDate));
//            exportsub.setCurrency(currency);
//            exportsub.setDgdc_cargo_in_scan(0);
//            exportsub.setDgdc_cargo_out_scan(0);
//            exportsub.setDgdc_seepz_in_scan(0);
//            exportsub.setDgdc_seepz_out_scan(0);
//            exportsub.setDgdcStatus(dgdcStatus);
//            exportsub.setEditedBy(editedBY);
//            exportsub.setEditedDate(new Date());
//            exportsub.setExporter(exporter);
//            exportsub.setExpSubId(nexttransid);
//            exportsub.setForwardedStatus("N");
//            exportsub.setGatePassStatus("N");
//
//            exportsub.setGwWeightUnit(grosswtunit);
//            exportsub.setInvoiceDate(parseDate(invoiceDate));
//            exportsub.setInvoiceNo(invoiceNo);
//            exportsub.setNop(Integer.parseInt(nop));
//
//            exportsub.setPassoutWeightUnit(passoutwtunit);
//
//            exportsub.setNsdlStatus(nsdlStatus);
//            exportsub.setRemarks(remarks);
//            exportsub.setRequestId(reqid+approvedby);
//
//            exportsub.setSerDate(parseDate(serDate));
//            exportsub.setSerNo(serNo);
//            exportsub.setStatus('N');
//            exportsub.setNoc(0);
//            exportsub.setMopStatus("N");
//            
//            ExportSub_History exporthistory = new ExportSub_History();
//            exporthistory.setCompanyId(companyid);
//            exporthistory.setBranchId(branchId);
//            exporthistory.setNewStatus("Handed over to DGDC SEEPZ");
//            exporthistory.setOldStatus("Pending");
//            exporthistory.setRequestId(reqid);
//            exporthistory.setTransport_Date(new Date());
//            exporthistory.setSerNo(serNo);
//            exporthistory.setUpdatedBy(user_Id);
//System.out.println("exporthistory "+exporthistory);
//            exportsubhistory.save(exporthistory);
//
//            if (!reqid.isEmpty()) {
//                exportsubrepo.save(exportsub);
//                
//             
//
//                for (int i = 1; i <= exportsub.getNop(); i++) {
//                    String srNo = String.format("%04d", i);
//                    Gate_In_Out gateinout = new Gate_In_Out();
//                    gateinout.setCompanyId(exportsub.getCompanyId());
//                    gateinout.setBranchId(exportsub.getBranchId());
//                    gateinout.setNop(exportsub.getNop());
//                    gateinout.setErp_doc_ref_no(exportsub.getRequestId());
//                    gateinout.setDoc_ref_no(exportsub.getSerNo());
//                    gateinout.setSr_No(exportsub.getSerNo() + srNo);
//                    gateinout.setDgdc_cargo_in_scan("N");
//                    gateinout.setDgdc_cargo_out_scan("N");
//                    gateinout.setDgdc_seepz_in_scan("N");
//                    gateinout.setDgdc_seepz_out_scan("N");
//
//                    gateinoutrepo.save(gateinout);
//                }
//
//            
//            }
//        }
//
//        return "success";
//    }
	
	public String convertFileToSubExp(InputStream inputStream, String companyid, String branchId, String user_Id)
			throws IOException, ParseException {
		InputStreamReader reader = new InputStreamReader(inputStream);
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

		for (CSVRecord read : csvParser) {
		//	String nexttransid = proccessNextIdService.autoIncrementSubExpTransId();
			String reqid = read.get("request_id");
			String approvedby = read.get("approved_by");
			String approvedDate = read.get("approved_date");
			String challanDate = read.get("challan_date");
			String challanNo = read.get("challan_no");
			String createdBy = read.get("created_by");
			String createdDate = read.get("created_date");
			String currency = read.get("currency");
			String dgdcStatus = read.get("dgdc_status");
			String editedBY = read.get("edited_by");
			String editedDate = read.get("edited_date");
			String exporter = read.get("exporter");
			String grosswt = read.get("gw_weight");
			String grosswtunit = read.get("gw_weight_unit");
			String invoiceDate = read.get("invoice_date");
			String invoiceNo = read.get("invoice_no");
			String nop = read.get("nop");

			String noofpieces = read.get("nopieces");
			String nsdlStatus = read.get("nsdl_status");
			String passoutwt = read.get("passout_weight");
			String passoutwtunit = read.get("passout_weight_unit");
			String productValue = read.get("product_value");
			String remarks = read.get("remarks");
			String serDate = read.get("ser_date");
			String serNo = read.get("ser_no");
			String status = read.get("status");
			String statusdoc = read.get("status_doc");
			String handoverparchchatype = read.get("handover_party_cha");
			String handoverpartychaname = read.get("handover_party_name");
			String handoverrepresentative = read.get("handover_represntative_id");
			String imposePenalty = read.get("impose_penalty_amt");
			String imposePenaltyRemark = read.get("impose_penalty_remarks");
			String receiverWt = read.get("received_wt");
			String receivedWtUnit = read.get("received_wt_unit");
			String cargoin = read.get("dgdc_cargo_in_scan");
			String cargoout = read.get("dgdc_cargo_out_scan");
			String seepzin = read.get("dgdc_seepz_in_scan");
			String seepzout = read.get("dgdc_seepz_out_scan");
			String noc = read.get("noc");
			String outDate = read.get("out_date");
			String forwardedStatus = read.get("forwarded_status");
			String gatePass = read.get("gate_pass_status");
			String mopStatus = read.get("mop_status");
			
			ExportSub exportsub = exportsubrepo.findExportSubByseronly(companyid, branchId, serNo.trim());
			if(exportsub != null) {
				if("Handed over to Party/CHA".equals(exportsub.getDgdcStatus()) || "Exit from DGDC SEEPZ Gate".equals(exportsub.getDgdcStatus())) {
					exportsub.setOutDate(parseDate4(outDate));
				}
				exportsubrepo.save(exportsub);
			}

		}

		return "success";
	}

	    // Helper method to parse dates with exception handling
	    private Date parseDate(String dateString) throws ParseException {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM, yyyy");
	        return dateFormat.parse(dateString);
	    }
	    
	    private Date parseDate1(String dateString) throws ParseException {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        return dateFormat.parse(dateString);
	    }

	
	

//	    public String convertFileToSubImp(InputStream inputStream, String companyid, String branchId, String user_Id)
//				throws IOException, ParseException {
//			InputStreamReader reader = new InputStreamReader(inputStream);
//			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
//			System.out.println("csvParser " + csvParser);
//			for (CSVRecord read : csvParser) {
//				String nexttransid = proccessNextIdService.autoIncrementSubImpTransId();
//				String reqid = read.get("request_id");
//				String approvedby = read.get("approved_by");
//				String approvedDate = read.get("approved_date");
//				String challanDate = read.get("challan_date");
//				String challanNo = read.get("challan_no");
//				String createdBy = read.get("created_by");
//				String createdDate = read.get("created_date");
//				String currency = read.get("currency");
//				String dgdcStatus = read.get("dgdc_status");
//				String editedBY = read.get("edited_by");
//				String editedDate = read.get("edited_date");
//				String exporter = read.get("exporter");
//				// String grosswt = read.get("gw_weight");
//				// String grosswtunit = read.get("gw_weight_unit");
//				String invoiceDate = read.get("invoice_date");
//				String invoiceNo = read.get("invoice_no");
//				String nop = read.get("nop");
//				String impsubid = read.get("imp_sub_id");
//				// int nop1 = Integer.parseInt(nop);
//				// String noofpieces = read.get("nopieces");
//				String nsdlStatus = read.get("nsdl_status");
//				String passedinwt = read.get("passed_in_weight");
//				String passwtinwtunit = read.get("passed_in_weight_unit");
//				String productValue = read.get("product_value");
//				String remarks = read.get("remarks");
//				String sirDate = read.get("sir_date");
//				String sirNo = read.get("sir_no");
//				String status = read.get("status");
//				String statusdoc = read.get("status_doc");
//				String handoverparchchatype = read.get("handover_party_cha");
//				String handoverpartychaname = read.get("handover_party_name");
//				String handoverrepresentative = read.get("handover_represntative_id");
//				String imposePenalty = read.get("impose_penalty_amt");
//				String imposePenaltyRemark = read.get("impose_penalty_remarks");
//				String netWt = read.get("net_weight");
//				String netWtUnit = read.get("net_weight_unit");
//				String reentryDate = read.get("reentry_date");
//				String lgdStatus = read.get("lgd_status");
//				String cargoin = read.get("dgdc_cargo_in_scan");
//				String cargoout = read.get("dgdc_cargo_out_scan");
//				String seepzin = read.get("dgdc_seepz_in_scan");
//				String seepzout = read.get("dgdc_seepz_out_scan");
//				String noc = read.get("noc");
//				String outDate = read.get("out_date");
//				String forwardedStatus = read.get("forwarded_status");
//				String gatePass = read.get("gate_pass_status");
//				String mopStatus = read.get("mop_status");
//				String importtype = read.get("import_type");
//				String exporterDate = read.get("exporter_date");
//
//				String exporterName = read.get("exporter_name");
//				Party party = partyrepo.getdatabypartyname(companyid, branchId, exporter);
//				ImportSub exportsub = new ImportSub();
//				System.out.println("Challan date " + challanDate);
//				if (!netWt.isEmpty()) {
//					BigDecimal big = new BigDecimal(netWt.trim());
//					exportsub.setNetWeight(big);
//				}
//
//				if (!productValue.isEmpty()) {
//					BigDecimal big2 = new BigDecimal(productValue.trim());
//					exportsub.setProductValue(big2);
//				}
//
//				DefaultPartyDetails defaultparty = defaultrepo.getdatabyuser_id(companyid, branchId, exporter);
//				System.out.println("defaultparty " + defaultparty);
//
//				exportsub.setCompanyId(companyid);
//				exportsub.setBranchId(branchId);
//				exportsub.setApprovedBy(user_Id);
//				exportsub.setApprovedDate(parseDate(sirDate)); // need changes
//				exportsub.setChallanDate(parseDate(challanDate)); // need changes
//				exportsub.setReentryDate(parseDate(reentryDate));
//				exportsub.setChallanNo(challanNo);
//				exportsub.setExporterName(challanNo);
//				exportsub.setExporterDate(parseDate(challanDate));
//				exportsub.setCreatedBy(user_Id);
//				exportsub.setCreatedDate(parseDate(sirDate)); // need changes
//				exportsub.setCurrency(currency);
//				exportsub.setDgdc_cargo_in_scan(0);
//				exportsub.setDgdc_cargo_out_scan(0);
//				exportsub.setDgdc_seepz_in_scan(0);
//				exportsub.setDgdc_seepz_out_scan(0);
//				exportsub.setDgdcStatus(dgdcStatus);
//				exportsub.setEditedBy(editedBY);
//				exportsub.setEditedDate(new Date());
//				exportsub.setExporter(exporter.trim());
//	            if(!outDate.isEmpty()) {
//	            	if(dgdcStatus.startsWith("Exit from") || dgdcStatus.startsWith("Handed over to Party")) {
//	            		exportsub.setOutDate(parseDate4(outDate));
//	            	}
//	            }
//				if (remarks.startsWith("DTA")) {
//					exportsub.setImportType("JOB");
//				} else if (remarks.startsWith("LGD")) {
//					exportsub.setImportType("LGD");
//				} else if (remarks.startsWith("Zone")) {
//					exportsub.setImportType("Zone to Zone");
//				} else {
//					exportsub.setImportType("JOB");
//				}
//
//				exportsub.setImpSubId(nexttransid);
//				exportsub.setForwardedStatus("N");
//				exportsub.setGatePassStatus("N");
//				exportsub.setExporterName(challanNo);
//				exportsub.setExporterDate(parseDate(challanDate));
//				exportsub.setNetWeightUnit(netWtUnit);
//				exportsub.setInvoiceDate(parseDate(invoiceDate)); // need changes
//				exportsub.setInvoiceNo(invoiceNo);
//				exportsub.setNop(Integer.parseInt(nop));
//
//				if (dgdcStatus.startsWith("Handed over to DGDC")) {
//					exportsub.setNsdlStatus("Pending");
//				} else if (dgdcStatus.startsWith("Exit from") || dgdcStatus.startsWith("Handed over to Party")) {
//					exportsub.setNsdlStatus("Passed In Full");
//				}
//
//				exportsub.setRemarks(remarks);
//				exportsub.setRequestId(reqid + approvedby);
//
//				exportsub.setSirDate(parseDate(sirDate));
//				exportsub.setSirNo(sirNo);
//				exportsub.setStatus("A");
//				if (!reqid.isEmpty()) {
//					impsubrepo.save(exportsub);
//
//					for (int i = 1; i <= exportsub.getNop(); i++) {
//						String srNo = String.format("%04d", i);
//						Gate_In_Out gateinout = new Gate_In_Out();
//						gateinout.setCompanyId(exportsub.getCompanyId());
//						gateinout.setBranchId(exportsub.getBranchId());
//						gateinout.setNop(exportsub.getNop());
//						gateinout.setErp_doc_ref_no(exportsub.getRequestId());
//						gateinout.setDoc_ref_no(exportsub.getSirNo());
//						gateinout.setSr_No(exportsub.getSirNo() + srNo);
//						gateinout.setDgdc_cargo_in_scan("N");
//						gateinout.setDgdc_cargo_out_scan("N");
//						gateinout.setDgdc_seepz_in_scan("N");
//						gateinout.setDgdc_seepz_out_scan("N");
//
//						gateinoutrepo.save(gateinout);
//					}
//
//					ImportSub_History exporthistory = new ImportSub_History();
//					exporthistory.setCompanyId(exportsub.getCompanyId());
//					exporthistory.setBranchId(exportsub.getBranchId());
//					exporthistory.setNewStatus("Handed over to DGDC SEEPZ");
//					exporthistory.setOldStatus("Pending");
//					exporthistory.setRequestId(exportsub.getRequestId());
//					exporthistory.setTransport_Date(parseDate(sirDate));
//					exporthistory.setSirNo(exportsub.getSirNo());
//					exporthistory.setUpdatedBy(user_Id);
//
//					importsubhisrepo.save(exporthistory);
//				}
//
//			}
//
//			return "success";
//		}
	    
	    public String convertFileToSubImp(InputStream inputStream, String companyid, String branchId, String user_Id)
				throws IOException, ParseException {
			InputStreamReader reader = new InputStreamReader(inputStream);
			List<Party> party1 = partyrepo.getalldata(companyid, branchId);
			System.out.println(party1);
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
			System.out.println("csvParser " + csvParser);
			for (CSVRecord read : csvParser) {
				String nexttransid = proccessNextIdService.autoIncrementSubImpTransId();
				String reqid = read.get("request_id");
				String approvedby = read.get("approved_by");
				String approvedDate = read.get("approved_date");
				String challanDate = read.get("challan_date");
				String challanNo = read.get("challan_no");
				String createdBy = read.get("created_by");
				String createdDate = read.get("created_date");
				String currency = read.get("currency");
				String dgdcStatus = read.get("dgdc_status");
				String editedBY = read.get("edited_by");
				String editedDate = read.get("edited_date");
				String exporter = read.get("exporter");
				// String grosswt = read.get("gw_weight");
				// String grosswtunit = read.get("gw_weight_unit");
				String invoiceDate = read.get("invoice_date");
				String invoiceNo = read.get("invoice_no");
				String nop = read.get("nop");
				String impsubid = read.get("imp_sub_id");
				// int nop1 = Integer.parseInt(nop);
				// String noofpieces = read.get("nopieces");
				String nsdlStatus = read.get("nsdl_status");
				String passedinwt = read.get("passed_in_weight");
				String passwtinwtunit = read.get("passed_in_weight_unit");
				String productValue = read.get("product_value");
				String remarks = read.get("remarks");
				String sirDate = read.get("sir_date");
				String sirNo = read.get("sir_no");
				String status = read.get("status");
				String statusdoc = read.get("status_doc");
				String handoverparchchatype = read.get("handover_party_cha");
				String handoverpartychaname = read.get("handover_party_name");
				String handoverrepresentative = read.get("handover_represntative_id");
				String imposePenalty = read.get("impose_penalty_amt");
				String imposePenaltyRemark = read.get("impose_penalty_remarks");
				String netWt = read.get("net_weight");
				String netWtUnit = read.get("net_weight_unit");
				String reentryDate = read.get("reentry_date");
				String lgdStatus = read.get("lgd_status");
				String cargoin = read.get("dgdc_cargo_in_scan");
				String cargoout = read.get("dgdc_cargo_out_scan");
				String seepzin = read.get("dgdc_seepz_in_scan");
				String seepzout = read.get("dgdc_seepz_out_scan");
				String noc = read.get("noc");
				String outDate = read.get("out_date");
				String forwardedStatus = read.get("forwarded_status");
				String gatePass = read.get("gate_pass_status");
				String mopStatus = read.get("mop_status");
				String importtype = read.get("import_type");
				String exporterDate = read.get("exporter_date");

				String exporterName = read.get("exporter_name");
				//Party party = partyrepo.getDataByPartyName(exporter);
				ImportSub exportsub = new ImportSub();
				System.out.println("Challan date " + challanDate);
				if (!netWt.isEmpty()) {
					BigDecimal big = new BigDecimal(netWt.trim());
					exportsub.setNetWeight(big);
				}

				if (!productValue.isEmpty()) {
					BigDecimal big2 = new BigDecimal(productValue.trim());
					exportsub.setProductValue(big2);
				}

			//	DefaultPartyDetails defaultparty = defaultrepo.getdatabyuser_id(companyid, branchId, exporter);
			//	System.out.println("defaultparty " + defaultparty);

				exportsub.setCompanyId(companyid);
				exportsub.setBranchId(branchId);
				exportsub.setApprovedBy(user_Id);
				exportsub.setApprovedDate(parseDate(sirDate)); // need changes
				exportsub.setChallanDate(parseDate(challanDate)); // need changes
				exportsub.setReentryDate(parseDate(reentryDate));
				exportsub.setChallanNo(challanNo);
				exportsub.setExporterName(challanNo);
				exportsub.setExporterDate(parseDate(challanDate));
				exportsub.setCreatedBy(user_Id);
				exportsub.setCreatedDate(parseDate(sirDate)); // need changes
				exportsub.setCurrency(currency);
				exportsub.setDgdc_cargo_in_scan(0);
				exportsub.setDgdc_cargo_out_scan(0);
				exportsub.setDgdc_seepz_in_scan(0);
				exportsub.setDgdc_seepz_out_scan(0);
				exportsub.setDgdcStatus(dgdcStatus);
				exportsub.setEditedBy(editedBY);
				exportsub.setEditedDate(new Date());

				
				
					
						exportsub.setExporter(exporter);
					

				if (!outDate.isEmpty()) {
					if (dgdcStatus.startsWith("Exit from") || dgdcStatus.startsWith("Handed over to Party")) {
						exportsub.setOutDate(parseDate4(outDate));
						
						DefaultPartyDetails defaultparty = defaultrepo.getdatabyuser_id(companyid, branchId,exporter);

						if (defaultparty == null) {
							// Handle the case when defaultparty is null or both expCHA and expConsole are
							// null or empty
							List<RepresentParty> represent = representpartyrepo.getbyuserId(companyid, branchId, exporter);
							exportsub.setHandover_Party_CHA("P");
							exportsub.setHandover_Party_Name(exporter);
							
							if(represent != null && !represent.isEmpty()) {
								for(RepresentParty r : represent) {
									exportsub.setHandover_Represntative_id(r.getRepresentativeId());
									break;
								}
							}
							
						} else {
							 if (defaultparty.getImpCHA() == null || defaultparty.getImpCHA().isEmpty()) {
								List<RepresentParty> represent = representpartyrepo.getbyuserId(companyid, branchId, exporter);
								exportsub.setHandover_Party_CHA("P");
								exportsub.setHandover_Party_Name(exporter);
								
								if(represent != null && !represent.isEmpty()) {
									for(RepresentParty r : represent) {
										exportsub.setHandover_Represntative_id(r.getRepresentativeId());
										break;
									}
								}
							}
							 else if (defaultparty.getImpCHA() != null && !defaultparty.getImpCHA().isEmpty()) {
									List<RepresentParty> represent = representpartyrepo.getbyuserId(companyid, branchId, defaultparty.getImpCHA());
									exportsub.setHandover_Party_CHA("C");
									exportsub.setHandover_Party_Name(defaultparty.getImpCHA());
									
									if(represent != null && !represent.isEmpty()) {
										for(RepresentParty r : represent) {
											exportsub.setHandover_Represntative_id(r.getRepresentativeId());
											break;
										}
									}
								}
						}
					}
				}
				if (remarks.startsWith("DTA")) {
					exportsub.setImportType("JOB");
				} else if (remarks.startsWith("LGD")) {
					exportsub.setImportType("LGD");
				} else if (remarks.startsWith("Zone")) {
					exportsub.setImportType("Zone to Zone");
				} else {
					exportsub.setImportType("JOB");
				}

				exportsub.setImpSubId(nexttransid);
				exportsub.setForwardedStatus("N");
				exportsub.setGatePassStatus("N");
				exportsub.setExporterName(challanNo);
				exportsub.setExporterDate(parseDate(challanDate));
				exportsub.setNetWeightUnit(netWtUnit);
				exportsub.setInvoiceDate(parseDate(invoiceDate)); // need changes
				exportsub.setInvoiceNo(invoiceNo);
				exportsub.setNop(Integer.parseInt(nop));

				if (dgdcStatus.startsWith("Handed over to DGDC")) {
					exportsub.setNsdlStatus("Pending");
				} else if (dgdcStatus.startsWith("Exit from") || dgdcStatus.startsWith("Handed over to Party")) {
					exportsub.setNsdlStatus("Passed In Full");
				}

				exportsub.setRemarks(remarks);
				exportsub.setRequestId(reqid + approvedby);

				exportsub.setSirDate(parseDate(sirDate));
				exportsub.setSirNo(sirNo);
				exportsub.setStatus("A");
				if (!reqid.isEmpty()) {
					impsubrepo.save(exportsub);

					for (int i = 1; i <= exportsub.getNop(); i++) {
						String srNo = String.format("%04d", i);
						Gate_In_Out gateinout = new Gate_In_Out();
						gateinout.setCompanyId(exportsub.getCompanyId());
						gateinout.setBranchId(exportsub.getBranchId());
						gateinout.setNop(exportsub.getNop());
						gateinout.setErp_doc_ref_no(exportsub.getRequestId());
						gateinout.setDoc_ref_no(exportsub.getSirNo());
						gateinout.setSr_No(exportsub.getSirNo() + srNo);
						gateinout.setDgdc_cargo_in_scan("N");
						gateinout.setDgdc_cargo_out_scan("N");
						gateinout.setDgdc_seepz_in_scan("N");
						gateinout.setDgdc_seepz_out_scan("N");

						gateinoutrepo.save(gateinout);
					}

					ImportSub_History exporthistory = new ImportSub_History();
					exporthistory.setCompanyId(exportsub.getCompanyId());
					exporthistory.setBranchId(exportsub.getBranchId());
					exporthistory.setNewStatus("Handed over to DGDC SEEPZ");
					exporthistory.setOldStatus("Pending");
					exporthistory.setRequestId(exportsub.getRequestId());
					exporthistory.setTransport_Date(parseDate(sirDate));
					exporthistory.setSirNo(exportsub.getSirNo());
					exporthistory.setUpdatedBy(user_Id);

					importsubhisrepo.save(exporthistory);
				}

			}

			return "success";
		}
	
//	public String convertFileToExport(InputStream inputStream, String companyid, String branchId, String user_Id) throws IOException {
//		InputStreamReader reader = new InputStreamReader(inputStream);
//		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
//		System.out.println("csvParser "+csvParser);
//		for(CSVRecord read : csvParser) {
//			String sbreqid = read.get("sb_request_id");
//			String sbNo = read.get("sb_number");
//			String airlineName = read.get("airline_name");
//			String airwayBillNo = read.get("airway_bill_no");
//			String approvedBy = read.get("approved_by");
//			String approvedDate=read.get("approved_date");
//			String cancelRemark = read.get("cancel_remarks");
//			String cancelStatus = read.get("cancel_status");
//			String cartingAgent = read.get("carting_agent");
//			String chaname = read.get("chaname");
//			String chano = read.get("chano");
//			String consoleAgent = read.get("console_agent");
//			String countryOfDestination = read.get("country_of_destination");
//			String createdBy = read.get("created_by");
//			String ceatedDate = read.get("created_date");
//			String descOfGoods = read.get("description_of_goods");
//			String dgdcStatus = read.get("dgdc_status");
//			String editedBy = read.get("edited_by");
//			String editedDate = read.get("edited_date");
//			String entityId = read.get("entity_id");
//			String flightDte = read.get("flight_date");
//			String flightNo = read.get("flight_no");
//			String fobValue = read.get("fobvalue_ininr");
//			String gateInDate = read.get("gate_in_date");
//			String gateInId = read.get("gate_in-id");
//			String grossWt = read.get("gross_weight");
//			String holdStatus = read.get("hold_status");
//			String hpStatus = read.get("hp_status");
//			String hpWt = read.get("hp_weight");
//			String hpPackageNo = read.get("hp_package_no");
//			String iecCode = read.get("iec_code");
//			String imposePenltyAmt = read.get("impose_penalty_amt");
//			String imposePenaltyRemark = read.get("impose_penalty_remarks");
//			String mawb = read.get("mawb");
//			String exporterName = read.get("exporter_name"); 
//			String noOfPackages = read.get("no_of_packages");
//			String nsdlStatus = read.get("nsdl_status");
//			String overrideDocument = read.get("override_document");
//			String representativeId = read.get("representative_id");
//			String pcStatus = read.get("pc_status");
//			String pctmDate = read.get("pctm_date");
//			String pctmNo = read.get("pctm_no");
//			String portOfDestination = read.get("port_of_destination");
//			String qrCodeUrl = read.get("qr_code_url");
//			String reasonforOverride = read.get("reason_for_override");
//			String sbDate = read.get("sb_date");
//			String scStatus = read.get("sc_status");
//			String serDate = read.get("ser_date");
//			String serNo = read.get("ser_no");
//			String status = read.get("status");
//			String tpDate = read.get("tp_date");
//			String tpNo = read.get("tp_no");
//			String uomGrossWt = read.get("uomgross_weight");
//			String uomOfPackages = read.get("uomof_packages");
//			String airlineCode = read.get("airline_code");
//			String gatepassVehNo = read.get("gate_pass_vehicle_no");
//			String gatePassStatus = read.get("gate_pass_status");
//			String poName = read.get("po_name");
//			String cargoInScan = read.get("dgdc_cargo_in_scan");
//			String cargoOutScan = read.get("dgdc_cargo_out_scan");
//			String seepzInScan = read.get("dgdc_seepz_in_scan");
//			String seepzOutScan  = read.get("dgdc_seepz_out_scan");
//			String noc = read.get("dgdc_seepz_out_scan");
//			String outDate = read.get("out_date");
//			String imagePath = read.get("image_path");
//			String redepositeRemark = read.get("redeposite_remark");
//			String backToTownImagePath = read.get("back_to_town_file_path");
//			String backtotownRemark = read.get("back_to_town_remark");
//			String mopStatus = read.get("mop_status");
//			String epCopyDocument = read.get("ep_copy_document");			
//			
//			Export export = new Export();
//			export.setCompanyId(companyid);
//			export.setBranchId(branchId);
//			export.setAirlineCode(airlineCode);
//			export.setAirlineName(airlineName);
//			export.setAirwayBillNo(airwayBillNo);
//			export.setApprovedBy(approvedBy);
//			export.setApprovedDate(new Date());      //need changes
//			export.setBacktotownFilePath(backToTownImagePath);
//			export.setBacktotownRemark(backtotownRemark);
//			export.setCancelRemarks(cancelRemark);
//			export.setCancelStatus(cancelStatus);
//			export.setCartingAgent(cartingAgent);
//			export.setChaName(chaname);
//			export.setChaNo(chano);
//			export.setConsoleAgent(consoleAgent);
//			export.setCountryOfDestination(countryOfDestination);
//			export.setCreatedBy(createdBy);
//			export.setCreatedDate(new Date());   //need changes
//   			export.setDescriptionOfGoods(descOfGoods);
//   			export.setDgdc_cargo_in_scan(0);
//   			export.setDgdc_cargo_out_scan(0);
//   			export.setDgdc_seepz_in_scan(0);
//   			export.setDgdc_seepz_out_scan(0);
//   			export.setDgdcStatus(dgdcStatus);
//   			export.setEditedBy(editedBy);
//   			export.setEditedDate(new Date());
//   			export.setEntityId(entityId);
//   			//export.setEpCopyDocument(epCopyDocument);
//   			export.setFlightDate(new Date());     //need changes
//   			export.setFlightNo(flightNo);
//   			if(!fobValue.isEmpty()  && !fobValue.equalsIgnoreCase("NULL")) {
//   				double fob = Double.parseDouble(fobValue);
//   			    export.setFobValueInINR(fob);
//   			}
//   			export.setGateInDate(new Date());   //need changes
//   			export.setGateInId(gateInId);
//   			export.setGatePassStatus(gatePassStatus);
//   			export.setGatePassVehicleNo(gatepassVehNo);
//   			
//   			
//   			if(!grossWt.isEmpty() && !grossWt.equalsIgnoreCase("NULL")) {
//   				double gw = Double.parseDouble(grossWt);
//   				export.setGrossWeight(gw);
//   			}
//   			export.setHoldStatus(holdStatus);
//   			export.setHppackageno(hpPackageNo);
//   			
//   			if(!hpWt.isEmpty() && !hpWt.equalsIgnoreCase("NULL")) {
//   				BigDecimal big = new BigDecimal(hpWt);
//   				export.setHpWeight(big);
//   			}
//   			export.setIecCode(iecCode);
//   			export.setImagePath(imagePath);
//   			
//   			if(!imposePenltyAmt.isEmpty() && !imposePenltyAmt.equalsIgnoreCase("NULL")) {
//   				double penalty = Double.parseDouble(imposePenltyAmt) ;
//   				export.setImposePenaltyAmount(penalty);	
//   			}
//   			export.setImposePenaltyRemarks(imposePenaltyRemark);
//   			export.setMawb(mawb);
//   			export.setMopStatus(mopStatus);
//   			export.setNameOfExporter(exporterName);
//   			export.setNoc(0);
//   			
//   			if(!noOfPackages.isEmpty() && !noOfPackages.equalsIgnoreCase("NULL")) {
//   				int nop = Integer.parseInt(noOfPackages);
//   				export.setNoOfPackages(nop);
//   			}
//   			export.setNsdlStatus(nsdlStatus);
//   			export.setOverrideDocument(overrideDocument);
//   			export.setPartyRepresentativeId(representativeId);
//   			export.setPcStatus(pcStatus);
//   			export.setPortOfDestination(portOfDestination);
//   			export.setQrcodeUrl(qrCodeUrl);
//   			export.setReasonforOverride(reasonforOverride);
//   			export.setRedepositeRemark(redepositeRemark);
//   			if(!sbDate.isEmpty() && !sbDate.equalsIgnoreCase("NULL")) {
//   				//Date date = new Date(sbDate);
//   				export.setSbDate(new Date());
//   			}
//   			export.setSbNo(sbNo);
//   			
//   			if(!serDate.isEmpty() && !serDate.equalsIgnoreCase("NULL")) {
//   				//Date date = new Date(serDate);
//   				export.setSerDate(new Date());
//   			}
//   			export.setSerNo(serNo);
//   			export.setStatus(status);
////   			if(!tpDate.isEmpty() && !tpDate.equalsIgnoreCase("NULL")) {
////   				Date date = new Date(tpDate);
////   				export.setTpDate(new Date());
////   			}
//   			export.setTpNo(tpNo);
//   			export.setUomGrossWeight(uomGrossWt);
//   			export.setUomOfPackages(uomOfPackages);
//   			
//   			if(!sbreqid.isEmpty()) {
//   				export.setSbRequestId(sbreqid);
//   				exportrepo.save(export);
//   				
//   				for(int i=1;i<=export.getNoOfPackages();i++) {
//   	        		String srNo = String.format("%04d", i);
//   	        		Gate_In_Out gateinout = new Gate_In_Out();
//   	        		gateinout.setCompanyId(export.getCompanyId());
//   	        		gateinout.setBranchId(export.getBranchId());
//   	        		gateinout.setNop(export.getNoOfPackages());
//   	        		gateinout.setErp_doc_ref_no(export.getSbRequestId());
//   	        		gateinout.setDoc_ref_no(export.getSbNo());
//   	        		gateinout.setSr_No(export.getSerNo()+srNo);
//   	        		gateinout.setDgdc_cargo_in_scan("N");
//   	        		gateinout.setDgdc_cargo_out_scan("N");
//   	        		gateinout.setDgdc_seepz_in_scan("N");
//   	        		gateinout.setDgdc_seepz_out_scan("N");
//   	        		
//   	        		gateinoutrepo.save(gateinout);
//   	        	}
//   				
//   			}
//		}
//		
//		return "success";
//	}
	    
	    private Date parseDate3(String dateString) throws ParseException {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
	        return dateFormat.parse(dateString);
	    }
	    
	    private Date parseDate4(String dateString) throws ParseException {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	        return dateFormat.parse(dateString);
	    }
	    
	    
	    public String convertFileToExport(InputStream inputStream, String companyid, String branchId, String user_Id)
				throws IOException, ParseException {
			InputStreamReader reader = new InputStreamReader(inputStream);
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
			System.out.println("csvParser " + csvParser);
			for (CSVRecord read : csvParser) {
				String sbreqid = read.get("sb_request_id");
				String sbNo = read.get("sb_number");
				String cid = read.get("company_id");
				String airlineName = read.get("airline_name");
				String airwayBillNo = read.get("airway_bill_no");
				String approvedBy = read.get("approved_by");
				String approvedDate = read.get("approved_date");
				String cancelRemark = read.get("cancel_remarks");
				String cancelStatus = read.get("cancel_status");
				String cartingAgent = read.get("carting_agent");
				String chaname = read.get("chaname");
				String chano = read.get("chano");
				String consoleAgent = read.get("console_agent");
				String countryOfDestination = read.get("country_of_destination");
				String createdBy = read.get("created_by");
				String ceatedDate = read.get("created_date");
				String descOfGoods = read.get("description_of_goods");
				String dgdcStatus = read.get("dgdc_status");
				String editedBy = read.get("edited_by");
				String editedDate = read.get("edited_date");
				String entityId = read.get("entity_id");
				String flightDte = read.get("flight_date");
				String flightNo = read.get("flight_no");
				String fobValue = read.get("fobvalue_ininr");
				String gateInDate = read.get("gate_in_date");
				String gateInId = read.get("gate_in-id");
				String grossWt = read.get("gross_weight");
				String holdStatus = read.get("hold_status");
				String hpStatus = read.get("hp_status");
				String hpWt = read.get("hp_weight");
				String hpPackageNo = read.get("hp_package_no");
				String iecCode = read.get("iec_code");
				String imposePenltyAmt = read.get("impose_penalty_amt");
				String imposePenaltyRemark = read.get("impose_penalty_remarks");
				String mawb = read.get("mawb");
				String exporterName = read.get("exporter_name");
				String noOfPackages = read.get("no_of_packages");
				String nsdlStatus = read.get("nsdl_status");
				String overrideDocument = read.get("override_document");
				String representativeId = read.get("representative_id");
				String pcStatus = read.get("pc_status");
				String pctmDate = read.get("pctm_date");
				String pctmNo = read.get("pctm_no");
				String portOfDestination = read.get("port_of_destination");
				String qrCodeUrl = read.get("qr_code_url");
				String reasonforOverride = read.get("reason_for_override");
				String sbDate = read.get("sb_date");
				String scStatus = read.get("sc_status");
				String serDate = read.get("ser_date");
				String serNo = read.get("ser_no");
				String status = read.get("status");
				String tpDate = read.get("tp_date");
				String tpNo = read.get("tp_no");
				String uomGrossWt = read.get("uomgross_weight");
				String uomOfPackages = read.get("uomof_packages");
				String airlineCode = read.get("airline_code");
				String gatepassVehNo = read.get("gate_pass_vehicle_no");
				String gatePassStatus = read.get("gate_pass_status");
				String poName = read.get("po_name");
				String cargoInScan = read.get("dgdc_cargo_in_scan");
				String cargoOutScan = read.get("dgdc_cargo_out_scan");
				String seepzInScan = read.get("dgdc_seepz_in_scan");
				String seepzOutScan = read.get("dgdc_seepz_out_scan");
				String noc = read.get("dgdc_seepz_out_scan");
				String outDate = read.get("out_date");
				String imagePath = read.get("image_path");
				String redepositeRemark = read.get("redeposite_remark");
				String backToTownImagePath = read.get("back_to_town_file_path");
				String backtotownRemark = read.get("back_to_town_remark");
				String mopStatus = read.get("mop_status");
				String epCopyDocument = read.get("ep_copy_document");

				Export export = new Export();
				export.setCompanyId(companyid);
				export.setBranchId(branchId);

				if (!airwayBillNo.isEmpty() && airwayBillNo != null) {
					String airlinecodee = airwayBillNo.trim() + approvedBy.trim();
					String airlinecde = airlinecodee.substring(0, 3);
					System.out.println(airlinecde);
					Airline airline = airlinerepo.findByAirlineCode(companyid, branchId, airlinecde);
					export.setAirlineCode(airlinecde);
					if (airline != null) {
						export.setAirlineName(airline.getAirlineName());
						export.setFlightNo(airline.getflightNo());
					}
				}

//				if(!chano.isEmpty() && chano != null) {
//					ExternalParty external = externalpartyrepo.getalldatabyid(companyid, branchId, chano);
//					export.setChaName(external.getUserName());
//					export.setChaNo(chano);
//				}
//				else {
//					export.setChaName("SELF");
//					export.setChaNo("EU0021");
//				}
//				
//				if(!chaname.isEmpty() && chaname != null) {
//					export.setConsoleAgent(consoleAgent);
//				}
//				else {
//					export.setConsoleAgent("EU0009");
//				}

				export.setSbNo(cid.trim());
				export.setAirwayBillNo(airwayBillNo.trim() + approvedBy.trim());
				export.setApprovedBy(user_Id);
				export.setApprovedDate(parseDate(serDate.trim())); // need changes
				// export.setBacktotownFilePath(backToTownImagePath);
				// export.setBacktotownRemark(backtotownRemark);
				export.setCancelRemarks(cancelRemark);
				export.setCancelStatus(cancelStatus);
				export.setCartingAgent(cartingAgent);
				export.setPartyRepresentativeId(representativeId);
				export.setCountryOfDestination(countryOfDestination);
				export.setCreatedBy(user_Id);
				export.setCreatedDate(parseDate(serDate.trim())); // need changes
				export.setDescriptionOfGoods(descOfGoods);
				export.setDgdc_cargo_in_scan(0);
				export.setDgdc_cargo_out_scan(0);
				export.setDgdc_seepz_in_scan(0);
				export.setDgdc_seepz_out_scan(0);
				export.setDgdcStatus(dgdcStatus);
				export.setEditedBy(user_Id);
				export.setEditedDate(new Date());

				export.setEntityId(entityId);

				if (!entityId.isEmpty() && entityId != null) {
					Party party = partyrepo.findbyentityid(companyid, branchId, entityId);
					export.setNameOfExporter(party.getPartyId());
					export.setIecCode(party.getIecNo());

					DefaultPartyDetails defaultparty = defaultrepo.getdatabyuser_id(companyid, branchId,
							party.getPartyId());

					if (defaultparty == null) {
						// Handle the case when defaultparty is null or both expCHA and expConsole are
						// null or empty
						export.setChaNo("EU0021");
						export.setChaName("SELF");
						export.setConsoleAgent("EU0009");
					} else if (defaultparty.getExpCHA() == null && defaultparty.getExpConsole() == null) {
						export.setChaNo("EU0021");
						export.setChaName("SELF");
						export.setConsoleAgent("EU0009");
					} else if (defaultparty.getExpCHA() == null) {
						// Handle the case when expCHA is null or empty
						export.setChaNo("EU0021");
						export.setChaName("SELF");
						export.setConsoleAgent(defaultparty.getExpConsole());
					} else if (defaultparty.getExpConsole() == null) {
						// Handle the case when expConsole is null or empty
						ExternalParty external = externalPartyRepository.getalldatabyid(companyid, branchId,
								defaultparty.getExpCHA());
						export.setChaNo(defaultparty.getExpCHA());
						if (external != null) {
							export.setChaName(external.getUserName());
						}
						export.setConsoleAgent("EU0009");
					} else {
						// Handle the case when both expCHA and expConsole have values
						ExternalParty external = externalPartyRepository.getalldatabyid(companyid, branchId,
								defaultparty.getExpCHA());
						export.setChaNo(defaultparty.getExpCHA());
						if (external != null) {
							export.setChaName(external.getUserName());
						}
						export.setConsoleAgent(defaultparty.getExpConsole());
					}
				}
				// export.setEpCopyDocument(epCopyDocument);
				export.setFlightDate(new Date()); // need changes

				if (!fobValue.isEmpty() && !fobValue.equalsIgnoreCase("NULL")) {
					double fob = Double.parseDouble(fobValue.trim());
					export.setFobValueInINR(fob);
				}
				// export.setGateInDate(new Date()); //need changes
				// export.setGateInId(gateInId);
				// export.setGatePassStatus(gatePassStatus);
				// export.setGatePassVehicleNo(gatepassVehNo);

				if (uomGrossWt.isEmpty()) {
					if (!grossWt.isEmpty()) {

						String regex = "([0-9.]+)\\s*\\(\\s*([A-Za-z]+)\\s*\\)";
						Pattern pattern = Pattern.compile(regex);
						Matcher matcher = pattern.matcher(grossWt.trim());

						// Check if the pattern matches
						if (matcher.matches()) {
							// Extract the numeric value as a double
							double weight = Double.parseDouble(matcher.group(1));

							// Extract the unit as a String
							String unit = matcher.group(2);
							export.setGrossWeight(weight);
							export.setUomGrossWeight(unit);

						}
					}
				} else {
					double weight = Double.parseDouble(grossWt);
					export.setGrossWeight(weight);
					export.setUomGrossWeight(uomGrossWt);
				}

				if (!holdStatus.isEmpty() && holdStatus != null) {
					if ("No".equals(holdStatus)) {
						export.setHoldStatus("N");
					}
					if ("Yes".equals(holdStatus)) {
						export.setHoldStatus("Y");
					}
				}

				if (!hpStatus.isEmpty() && hpStatus != null) {
					if ("No".equals(hpStatus)) {
						export.setHpStatus("N");
					}
					if ("Yes".equals(hpStatus)) {
//	   					String[] weightStrings = hpWt.split(",");
//	   					String[] packageStrings = hpPackageNo.split(",");
	//
//	   					List<ExportHeavyPackage> heavyList = new ArrayList<>();
	//
//	   					// Check if the arrays have the same length
//	   					if (weightStrings.length == packageStrings.length) {
//	   					    for (int i = 0; i < weightStrings.length; i++) {
//	   					        ExportHeavyPackage exportHeavy = new ExportHeavyPackage();
	//
//	   					        // Trim to remove any leading or trailing spaces
//	   					        String trimmedWeightString = weightStrings[i].trim();
//	   					        String packageNumber = packageStrings[i].trim();
	//
//	   					        // Remove commas from the weight string
//	   					        trimmedWeightString = trimmedWeightString.replace(",", "");
//	                            
//	   					        // Parse the string to a BigDecimal and set it as the weight
//	   					        try {
//	   					            BigDecimal weight = new BigDecimal(trimmedWeightString);
//	   					            exportHeavy.setWeight(weight);
//	   					        } catch (NumberFormatException e) {
//	   					            // Handle the case where the string cannot be parsed to a BigDecimal
//	   					            System.err.println("Invalid weight format: " + trimmedWeightString);
//	   					            // Optionally, you can continue the loop or take other appropriate action
//	   					            continue;
//	   					        }
	//
//	   					        // Set the package number and add the ExportHeavyPackage object to the list
//	   					        exportHeavy.setPackageNumber(packageNumber);
//	   					        heavyList.add(exportHeavy);
//	   					    }
	//
//	   					    // Save the list of ExportHeavyPackage objects
//	   					    eexportHeavyRepo.saveAll(heavyList);
//	   					} else {
//	   					    System.err.println("Mismatched lengths of weight and package arrays");
//	   					}

						export.setHpStatus("Y");
					}
				}

				if (!scStatus.isEmpty() && scStatus != null) {
					if ("No".equals(scStatus)) {
						export.setScStatus("N");
					}
					if ("Yes".equals(scStatus)) {
						export.setScStatus("Y");
					}
				}

				if (!pcStatus.isEmpty() && pcStatus != null) {
					if ("No".equals(pcStatus)) {
						export.setPcStatus("N");
					}
					if ("Yes".equals(pcStatus)) {
						export.setPcStatus("Y");

						ExportPC exportpc = new ExportPC();
						exportpc.setCompanyId(companyid);
						exportpc.setBranchId(branchId);
						exportpc.setStatus("A");
						exportpc.setApprovedBy(user_Id);
						exportpc.setCreatedBy(user_Id);

						if (!serDate.isEmpty()) {
							// Date date = new Date(serDate);
							exportpc.setApprovedDate(parseDate(serDate.trim()));
							exportpc.setCreatedDate(parseDate(serDate.trim()));
						}
						System.out.println(backtotownRemark + imagePath + cargoInScan);
						exportpc.setAddress(gatepassVehNo);
						exportpc.setApproverDate(parseDate4(backtotownRemark));
						exportpc.setApproverDesignation(backToTownImagePath);
						exportpc.setApproverName(redepositeRemark);
						exportpc.setDateOfEscort(parseDate4(imagePath));
						exportpc.setDeputedCustomsOfficerDesignation(seepzOutScan);
						exportpc.setDeputedCustomsOfficerName(seepzInScan);
						exportpc.setDeputedFromDestination(noc);
						exportpc.setDeputedToDestination(outDate);
						exportpc.setFlightDate(parseDate4(cargoInScan));
						exportpc.setFlightNo(poName);
						exportpc.setNationality(cargoOutScan);
						exportpc.setPassengerName(airlineCode);
						exportpc.setPassportNo(gatePassStatus);
						exportpc.setSbNo(cid);
						exportpc.setSbRequestId(sbreqid + sbNo);
						exportpc.setSerNo(serNo);

						exportpcrepo.save(exportpc);

					}
				}

				export.setHppackageno(hpPackageNo);

				export.setImagePath(imagePath);

				if (!imposePenltyAmt.isEmpty() && !imposePenltyAmt.equalsIgnoreCase("NULL")) {
					double penalty = Double.parseDouble(imposePenltyAmt);
					export.setImposePenaltyAmount(penalty);
				}
				export.setImposePenaltyRemarks(imposePenaltyRemark);
				export.setMawb(mawb);
				export.setMopStatus(mopStatus);

				export.setNoc(0);

				if (!noOfPackages.isEmpty() && !noOfPackages.equalsIgnoreCase("NULL")) {
					int nop = Integer.parseInt(noOfPackages.trim());
					export.setNoOfPackages(nop);
				}
				export.setNsdlStatus(nsdlStatus);
				export.setOverrideDocument(overrideDocument);
				export.setPartyRepresentativeId(representativeId);

				export.setPctmNo(pctmNo);
				export.setTpNo(tpNo);
				if (!pctmDate.isEmpty() && pctmDate != null) {
					export.setPctmDate(parseDate(pctmDate));
					export.setTpDate(parseDate(pctmDate));
					export.setOutDate(parseDate(pctmDate));
				}

				export.setPortOfDestination(portOfDestination);
				export.setQrcodeUrl(qrCodeUrl);
				export.setReasonforOverride(reasonforOverride);
				export.setRedepositeRemark(redepositeRemark);
				if (!sbDate.isEmpty() && !sbDate.equalsIgnoreCase("NULL")) {
					// Date date = new Date(sbDate);
					export.setSbDate(parseDate3(sbDate));
				}

				if (!serDate.isEmpty() && !serDate.equalsIgnoreCase("NULL")) {
					// Date date = new Date(serDate);
					export.setSerDate(parseDate(serDate.trim()));
				}
				export.setSerNo(serNo.trim());
				export.setStatus("A");
//	   			if(!tpDate.isEmpty() && !tpDate.equalsIgnoreCase("NULL")) {
//	   				Date date = new Date(tpDate);
//	   				export.setTpDate(new Date());
//	   			}
				export.setTpNo(tpNo);

				export.setUomOfPackages(uomOfPackages);

				if (!sbreqid.isEmpty()) {
					export.setSbRequestId(sbreqid + sbNo);
					exportrepo.save(export);

					Export_History exporthistory = new Export_History();
					exporthistory.setCompanyId(companyid);
					exporthistory.setBranchId(branchId);
					exporthistory.setNewStatus("Handed over to DGDC SEEPZ");
					exporthistory.setOldStatus("Entry at DGDC SEEPZ Gate");
					exporthistory.setSbNo(cid);
					exporthistory.setSbRequestId(sbreqid + sbNo);
					exporthistory.setserNo(serNo);
					exporthistory.setTransport_Date(parseDate(serDate));
					exporthistory.setUpdatedBy(user_Id);

					exporthistoryrepo.save(exporthistory);

					for (int i = 1; i <= export.getNoOfPackages(); i++) {
						String srNo = String.format("%04d", i);
						Gate_In_Out gateinout = new Gate_In_Out();
						gateinout.setCompanyId(export.getCompanyId());
						gateinout.setBranchId(export.getBranchId());
						gateinout.setNop(export.getNoOfPackages());
						gateinout.setErp_doc_ref_no(export.getSbRequestId());
						gateinout.setDoc_ref_no(export.getSbNo());
						gateinout.setSr_No(export.getSerNo() + srNo);
						gateinout.setDgdc_cargo_in_scan("N");
						gateinout.setDgdc_cargo_out_scan("N");
						gateinout.setDgdc_seepz_in_scan("N");
						gateinout.setDgdc_seepz_out_scan("N");

						gateinoutrepo.save(gateinout);
					}

				}
			}

			return "success";
		}
	    
//	    public String convertFileToExport(InputStream inputStream, String companyid, String branchId, String user_Id)
//				throws IOException, ParseException {
//			InputStreamReader reader = new InputStreamReader(inputStream);
//			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
//			System.out.println("csvParser " + csvParser);
//			for (CSVRecord read : csvParser) {
//				String sbreqid = read.get("sb_request_id");
//				String sbNo = read.get("sb_number");
//				String cid = read.get("company_id");
//				String airlineName = read.get("airline_name");
//				String airwayBillNo = read.get("airway_bill_no");
//				String approvedBy = read.get("approved_by");
//				String approvedDate = read.get("approved_date");
//				String cancelRemark = read.get("cancel_remarks");
//				String cancelStatus = read.get("cancel_status");
//				String cartingAgent = read.get("carting_agent");
//				String chaname = read.get("chaname");
//				String chano = read.get("chano");
//				String consoleAgent = read.get("console_agent");
//				String countryOfDestination = read.get("country_of_destination");
//				String createdBy = read.get("created_by");
//				String ceatedDate = read.get("created_date");
//				String descOfGoods = read.get("description_of_goods");
//				String dgdcStatus = read.get("dgdc_status");
//				String editedBy = read.get("edited_by");
//				String editedDate = read.get("edited_date");
//				String entityId = read.get("entity_id");
//				String flightDte = read.get("flight_date");
//				String flightNo = read.get("flight_no");
//				String fobValue = read.get("fobvalue_ininr");
//				String gateInDate = read.get("gate_in_date");
//				String gateInId = read.get("gate_in-id");
//				String grossWt = read.get("gross_weight");
//				String holdStatus = read.get("hold_status");
//				String hpStatus = read.get("hp_status");
//				String hpWt = read.get("hp_weight");
//				String hpPackageNo = read.get("hp_package_no");
//				String iecCode = read.get("iec_code");
//				String imposePenltyAmt = read.get("impose_penalty_amt");
//				String imposePenaltyRemark = read.get("impose_penalty_remarks");
//				String mawb = read.get("mawb");
//				String exporterName = read.get("exporter_name");
//				String noOfPackages = read.get("no_of_packages");
//				String nsdlStatus = read.get("nsdl_status");
//				String overrideDocument = read.get("override_document");
//				String representativeId = read.get("representative_id");
//				String pcStatus = read.get("pc_status");
//				String pctmDate = read.get("pctm_date");
//				String pctmNo = read.get("pctm_no");
//				String portOfDestination = read.get("port_of_destination");
//				String qrCodeUrl = read.get("qr_code_url");
//				String reasonforOverride = read.get("reason_for_override");
//				String sbDate = read.get("sb_date");
//				String scStatus = read.get("sc_status");
//				String serDate = read.get("ser_date");
//				String serNo = read.get("ser_no");
//				String status = read.get("status");
//				String tpDate = read.get("tp_date");
//				String tpNo = read.get("tp_no");
//				String uomGrossWt = read.get("uomgross_weight");
//				String uomOfPackages = read.get("uomof_packages");
//				String airlineCode = read.get("airline_code");
//				String gatepassVehNo = read.get("gate_pass_vehicle_no");
//				String gatePassStatus = read.get("gate_pass_status");
//				String poName = read.get("po_name");
//				String cargoInScan = read.get("dgdc_cargo_in_scan");
//				String cargoOutScan = read.get("dgdc_cargo_out_scan");
//				String seepzInScan = read.get("dgdc_seepz_in_scan");
//				String seepzOutScan = read.get("dgdc_seepz_out_scan");
//				String noc = read.get("dgdc_seepz_out_scan");
//				String outDate = read.get("out_date");
//				String imagePath = read.get("image_path");
//				String redepositeRemark = read.get("redeposite_remark");
//				String backToTownImagePath = read.get("back_to_town_file_path");
//				String backtotownRemark = read.get("back_to_town_remark");
//				String mopStatus = read.get("mop_status");
//				String epCopyDocument = read.get("ep_copy_document");
//                String newSer = read.get("new_ser");
//			
//                Export export1 = exportrepo.finexportdata(cid, branchId, serNo);
//                export1.setSerNo(newSer);
//                
//				if (!export1.getSerNo().isEmpty()) {
//					
//					
//
//					List<Export_History> exporthistory1 = exporthistoryrepo.findRecordsByCriteria1(branchId, companyid, sbNo, sbreqid);
//					if(exporthistory1 != null) {
//						for(Export_History exporthistory : exporthistory1) {
//							exporthistory.setCompanyId(companyid);
//							exporthistory.setBranchId(branchId);
//							exporthistory.setNewStatus("Handed over to DGDC SEEPZ");
//							exporthistory.setOldStatus("Entry at DGDC SEEPZ Gate");
//							exporthistory.setSbNo(cid);
//							exporthistory.setSbRequestId(sbreqid + sbNo);
//							exporthistory.setserNo(serNo);
//							exporthistory.setTransport_Date(parseDate(serDate));
//							exporthistory.setUpdatedBy(user_Id);
//							exporthistoryrepo.save(exporthistory);
//						}
//					}
//					
////					List<Gate_In_Out> gateinout1 = gateinoutrepo.findbysr1(cid, branchId, export1.getSbRequestId(), export1.getSbNo());
////					
////					for(Gate_In_Out gateinout : gateinout1) {
////						  String sr = gateinout.getSr_No();
////						  String f1 = sr.substring(0, 8);
////						  String f2 = sr.substring(9, 12);
////							
////						
////							gateinout.setSr_No(newSer+f2);
////							
////
////							gateinoutrepo.save(gateinout);
////						
////
////					}
//
//				
//					exportrepo.save(export1);
//					
//				}
//			}
//
//			return "success";
//		}
	
	
	
	
////	public String convertFileToImport(InputStream inputStream, String companyid, String branchId, String user_Id) throws IOException, ParseException {
////		InputStreamReader reader = new InputStreamReader(inputStream);
////		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
////		System.out.println("csvParser "+csvParser);
////		for(CSVRecord read : csvParser) {
////		   String imptransid = read.get("imp_trans_id");
////		   String mawb = read.get("mawb");
////		   String sirNo = read.get("sir_no");
////		   String airlineCode = read.get("airline_code");
////		   String airlineName = read.get("airline_name");
////		   String approveBy = read.get("approved_by");
////		   String approvedDate = read.get("approved_date");
////		   String assessableValue = read.get("assessable_value");
////		    String beDate = read.get("be_date");
////		    String beNo = read.get("be_no");
////		    String beRequestId = read.get("be_request_id");
////		    String cancelRemarks = read.get("cancel_remarks");
////		    String cancelStatus = read.get("cancel_status");
////		    String cartingAgent = read.get("carting_agent");
////		    String chaCde = read.get("cha_cde");
////		    String chaName = read.get("cha_name");
////		    String closeStatus = read.get("close_status");
////		    String consoleName = read.get("console_name");
////		    String countryOrigin = read.get("country_origin");
////		    String createdBy = read.get("created_by");
////		    String createdDate = read.get("created_date");
////		    String descriptionOfGoods = read.get("description_of_goods");
////		    String dgdcStatus = read.get("dgdc_status");
////		    String dgdcCargoInScan = read.get("dgdc_cargo_in_scan");
////		    String dgdcCargoOutScan = read.get("dgdc_cargo_out_scan");
////		    String dgdcSeepzInScan = read.get("dgdc_seepz_in_scan");
////		    String dgdcSeepzOutScan = read.get("dgdc_seepz_out_scan");
////		    String doDate = read.get("do_date");
////		    String doNumber = read.get("do_number");
////		    String editedBy = read.get("edited_by");
////		    String editedDate = read.get("edited_date");
////		    String flightDate = read.get("flight_date");
////		    String flightNo = read.get("flight_no");
////		    String forwardedStatus = read.get("forwarded_status");
////		    String gatePassStatus = read.get("gate_pass_status");
////		    String grossWeight = read.get("gross_weight");
////		    String handedOverPartyId = read.get("handed_over_party_id");
////		    String handedOverRepresentativeId = read.get("handed_over_representative_id");
////		    String handedOverToType = read.get("handed_over_to_type");
////		    String hawb = read.get("hawb");
////		    String holdBy = read.get("hold_by");
////		    String holdDate = read.get("hold_date");
////		    String holdStatus = read.get("hold_status");
////		    String hpStatus = read.get("hp_status");
////		    String hpWeight = read.get("hp_weight");
////		    String hpPackageNo = read.get("hp_package_no");
////		    String iec = read.get("iec");
////		    String igmDate = read.get("igm_date");
////		    String igmNo = read.get("igm_no");
////		    String impTransDate = read.get("imp_trans_date");
////		    String importAddress = read.get("import_address");
////		    String importRemarks = read.get("import_remarks");
////		    String importerId = read.get("importer_id");
////		    String importerNameOnParcel = read.get("importer_name_on_parcel");
////		    String imposePenaltyAmt = read.get("impose_penalty_amt");
////		    String imposePenaltyRemarks = read.get("impose_penalty_remarks");
////		    String niptApproverDate = read.get("nipt_approver_date:");
////		    String niptApproverDesignation = read.get("nipt_approver designation");
////		    String niptApproverName = read.get("nipt_approver_name");
////		    String niptCustomOfficerName = read.get("nipt_custom_officer_name");
////		    String niptCustomsOfficerDesignation = read.get("nipt_customs_officer_designation");
////		    String niptDateOfEscort = read.get("nipt_date_of_escort:");
////		    String niptDeputedFromDestination = read.get("nipt_deputed_from_destination:");
////		    String niptDeputedToDestination = read.get("nipt_deputed_to_destination");
////		    String niptStatus = read.get("nipt_status");
////		    String noc = read.get("noc");
////		    String nop = read.get("nop");
////		    String nsdlStatus = read.get("nsdl_status");
////		    String nsdlOverrideDocs = read.get("nsdl_override_docs");
////		    String outDate = read.get("out_date");
////		    String packageContentType = read.get("package_content_type");
////		    String parcelType = read.get("parcel_type");
////		    String representativeId = read.get("representative_id");
////		    String pcStatus = read.get("pc_status");
////		    String pctmNo = read.get("pctm_no");
////		    String portOrigin = read.get("port_origin");
////		    String qrCodeUrl = read.get("qr_code_url");
////		    String reasonForOverride = read.get("reason_for_override");
////		    String scStatus = read.get("sc_status");
////		    String sezEntityId = read.get("sez_entity_id");
////		    String sirDate = read.get("sir_date");
////		    String status = read.get("status");
////		    String tpDate = read.get("tp_date");
////		    String tpNo = read.get("tp_no");
////		    String uomPackages = read.get("uom_packages");
////		    String uomWeight = read.get("uom_weight");
////		    String wrongDepositFilePath = read.get("wrong_deposit_file_path");
////		    String wrongDepositStatus = read.get("wrong_deposit_status");
////		    String wrongDepositRemarks = read.get("wrong_deposit_remarks");
////		    String detentionReceiptNo = read.get("detention_receipt_no");
////		    String mopStatus = read.get("mop_status");
////		    
////		    
////		    Import import1 = new Import();
////		    String autoIncrementIMPTransId = proccessNextIdService.autoIncrementIMPTransId();
////		    import1.setCompanyId(companyid);
////		    import1.setBranchId(branchId);
////		    import1.setMawb(imptransid+mawb);
////		    if(!hawb.isEmpty()) {
////		    	import1.setHawb(hawb+holdBy);
////		    }
////		    else {
////		    	import1.setHawb("0000");
////		    }
////		    import1.setImpTransId(autoIncrementIMPTransId);
////		    import1.setAirlineCode(airlineCode);
////		    import1.setAirlineName(airlineName);
////		    import1.setNSDL_Status(nsdlStatus);
////		    import1.setChaCde(chaCde);
////		    import1.setCartingAgent(cartingAgent);
////		    import1.setPartyRepresentativeId(representativeId);
////		    import1.setChaName(chaName);
////		    import1.setPctmNo(pctmNo);
////		    import1.setTpNo(tpNo);
////		    if(!sirDate.isEmpty()) {
////		    	import1.setTpDate(parseDate(sirDate));
////		    }
////		   
////		    import1.setIgmNo(igmNo);
////		    if(!igmDate.isEmpty()) {
////		    	import1.setIgmDate(parseDate(igmDate));
////		    }
////		    if(!grossWeight.isEmpty()) {
////		    	import1.setGrossWeight(new BigDecimal(grossWeight));
////		    }
////		    
////		    if(!imposePenaltyRemarks.isEmpty() && imposePenaltyRemarks != null) {
////		    	if(imposePenaltyRemarks.startsWith("RI")) {
////		    		Party party = partyrepo.findbyentityid(companyid,branchId,sezEntityId);
////			    	import1.setHandedOverToType("P");
////			    	import1.setHandedOverPartyId(party.getPartyId());
////			    	import1.setHandedOverRepresentativeId(imposePenaltyRemarks);
////		    	}
////		    }
////		    
////		    if(!imposePenaltyAmt.isEmpty() && imposePenaltyAmt != null) {
////		    	if(imposePenaltyAmt.startsWith("RI")) {
////		    		RepresentParty repesent = representpartyrepo.findByFNameAndLName(companyid, branchId, imposePenaltyAmt);
////			    	import1.setHandedOverToType("C");
////			    	import1.setHandedOverPartyId(repesent.getUserId());
////			    	import1.setHandedOverRepresentativeId(imposePenaltyAmt);
////		    	}
////		    }
////		    
////		    if(!holdStatus.isEmpty() && holdStatus != null) {
////		    	if(holdStatus == "No") {
////		    		import1.setHoldStatus("N");
////		    	}
////		    	if(holdStatus == "Yes") {
////		    		import1.setHoldStatus("Y");
////		    	}
////		    }
////		    
////		    if(!scStatus.isEmpty() && scStatus != null) {
////		    	if(scStatus == "No") {
////		    		import1.setScStatus("N");
////		    	}
////		    	if(scStatus == "Yes") {
////		    		import1.setScStatus("Y");
////		    	}
////		    }
////		    import1.setCloseStatus("Y");
////		    if(!flightNo.isEmpty()) {
////		    	String flight = flightNo.trim();
////		    	 String flight1 = flight.replaceAll("[^a-zA-Z]", "");
////		    	System.out.println("flight "+flight);
////		    	Airline airline = airlinerepo.findByAirlineShortCode(companyid,branchId,flight1);
////		    	 import1.setFlightNo(flightNo.trim());
////				    if(!flightDate.isEmpty()) {
////				    	import1.setFlightDate(parseDate(flightDate));
////				    }
////				    import1.setAirlineCode(airline.getAirlineCode());
////				    import1.setAirlineName(airline.getAirlineName());
////				    import1.setNiptStatus("N");
////		    }
////		    else {
////		    	import1.setNiptStatus("Y");
////		    }
////	
////		    import1.setCountryOrigin(countryOrigin);
////		    import1.setPortOrigin(portOrigin);
////		    import1.setConsoleName(consoleName);
////		    
////		    if(!sezEntityId.isEmpty() && sezEntityId != null) {
////		    	Party party = partyrepo.findbyentityid(companyid,branchId,sezEntityId);
////		    	import1.setImporterId(party.getPartyId());
////			    import1.setImporternameOnParcel(party.getPartyName());
////			    import1.setIec(party.getIecNo());
////			    import1.setSezEntityId(sezEntityId);
////		    }
////	
////		    import1.setImportRemarks(importRemarks);
////		   
////		    import1.setPackageContentType(packageContentType);
////		    
////		    if(!nop.isEmpty()) {
////		    	int nop1 = Integer.parseInt(nop);
////		    	import1.setNop(nop1);
////		    }
////		    import1.setStatus("A");
////		    import1.setUomPackages(uomPackages);
////		    import1.setAssessableValue(assessableValue);
////		    import1.setDescriptionOfGoods(descriptionOfGoods);
////		    import1.setDgdcStatus(dgdcStatus.trim());
////		    if(!grossWeight.isEmpty()) {
////		    	BigDecimal big = new BigDecimal(grossWeight);
////		    	import1.setGrossWeight(big);
////		    }
////		    import1.setUomWeight(uomWeight);
////		    import1.setBeRequestId(beRequestId+cancelRemarks);
////		    import1.setBeNo(beNo);
////		    if(!beDate.isEmpty()) {
////		    	import1.setBeDate(parseDate(beDate));
////		    }
////		//    import1.setImportAddress(importAddress);
////		    
////		    if(!sirNo.isEmpty()) {
////		    	import1.setSirNo(sirNo);
////		    	 if(!sirDate.isEmpty()) {
////		    		 import1.setSirDate(parseDate(sirDate));
////		    	 }
////		    	importrepo.save(import1);
////		    	
////		    	Import_History importhistory = new Import_History();
////		    	importhistory.setBranchId(branchId);
////		    	importhistory.setCompanyId(companyid);
////		    	importhistory.setHawb(import1.getHawb());
////		    	importhistory.setMawb(import1.getMawb());
////		    	importhistory.setNewStatus("Handed over to DGDC SEEPZ");
////		    	importhistory.setOldStatus("Entry at DGDC SEEPZ Gate");
////		    	importhistory.setSirNo(import1.getSirNo());
////		    	importhistory.setUpdatedBy(user_Id);
////		    	importhistory.setTransport_Date(import1.getSirDate());
////		    	importhistoHistoryRepo.save(importhistory);
////		    	
////		    	for(int i=1;i<=import1.getNop();i++) {
////	        		String srNo = String.format("%04d", i);
////	        		Gate_In_Out gateinout = new Gate_In_Out();
////	        		gateinout.setCompanyId(import1.getCompanyId());
////	        		gateinout.setBranchId(import1.getBranchId());
////	        		gateinout.setNop(import1.getNop());
////	        		gateinout.setErp_doc_ref_no(import1.getMawb());
////	        		gateinout.setDoc_ref_no(import1.getHawb());
////	        		gateinout.setSr_No(import1.getSirNo()+srNo);
////	        		gateinout.setDgdc_cargo_in_scan("N");
////	        		gateinout.setDgdc_cargo_out_scan("N");
////	        		gateinout.setDgdc_seepz_in_scan("N");
////	        		gateinout.setDgdc_seepz_out_scan("N");
////	        		
////	        		gateinoutrepo.save(gateinout);
////	        		System.out.println("gateinout "+gateinout);
////	        	}
////		    }
////		}
////		
////		return "success";
//	}
	
	
//	    public String convertFileToImport(InputStream inputStream, String companyid, String branchId, String user_Id)
//				throws IOException, ParseException {
//			InputStreamReader reader = new InputStreamReader(inputStream);
//			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
//			System.out.println("csvParser " + csvParser);
//			for (CSVRecord read : csvParser) {
//				String imptransid = read.get("imp_trans_id");
//				String mawb = read.get("mawb");
//				String sirNo = read.get("sir_no");
//				String airlineCode = read.get("airline_code");
//				String airlineName = read.get("airline_name");
//				String approveBy = read.get("approved_by");
//				String approvedDate = read.get("approved_date");
//				String assessableValue = read.get("assessable_value");
//				String beDate = read.get("be_date");
//				String beNo = read.get("be_no");
//				String beRequestId = read.get("be_request_id");
//				String cancelRemarks = read.get("cancel_remarks");
//				String cancelStatus = read.get("cancel_status");
//				String cartingAgent = read.get("carting_agent");
//				String chaCde = read.get("cha_cde");
//				String chaName = read.get("cha_name");
//				String closeStatus = read.get("close_status");
//				String consoleName = read.get("console_name");
//				String countryOrigin = read.get("country_origin");
//				String createdBy = read.get("created_by");
//				String createdDate = read.get("created_date");
//				String descriptionOfGoods = read.get("description_of_goods");
//				String dgdcStatus = read.get("dgdc_status");
//				String dgdcCargoInScan = read.get("dgdc_cargo_in_scan");
//				String dgdcCargoOutScan = read.get("dgdc_cargo_out_scan");
//				String dgdcSeepzInScan = read.get("dgdc_seepz_in_scan");
//				String dgdcSeepzOutScan = read.get("dgdc_seepz_out_scan");
//				String doDate = read.get("do_date");
//				String doNumber = read.get("do_number");
//				String editedBy = read.get("edited_by");
//				String editedDate = read.get("edited_date");
//				String flightDate = read.get("flight_date");
//				String flightNo = read.get("flight_no");
//				String forwardedStatus = read.get("forwarded_status");
//				String gatePassStatus = read.get("gate_pass_status");
//				String grossWeight = read.get("gross_weight");
//				String handedOverPartyId = read.get("handed_over_party_id");
//				String handedOverRepresentativeId = read.get("handed_over_representative_id");
//				String handedOverToType = read.get("handed_over_to_type");
//				String hawb = read.get("hawb");
//				String holdBy = read.get("hold_by");
//				String holdDate = read.get("hold_date");
//				String holdStatus = read.get("hold_status");
//				String hpStatus = read.get("hp_status");
//				String hpWeight = read.get("hp_weight");
//				String hpPackageNo = read.get("hp_package_no");
//				String iec = read.get("iec");
//				String igmDate = read.get("igm_date");
//				String igmNo = read.get("igm_no");
//				String impTransDate = read.get("imp_trans_date");
//				String importAddress = read.get("import_address");
//				String importRemarks = read.get("import_remarks");
//				String importerId = read.get("importer_id");
//				String importerNameOnParcel = read.get("importer_name_on_parcel");
//				String imposePenaltyAmt = read.get("impose_penalty_amt");
//				String imposePenaltyRemarks = read.get("impose_penalty_remarks");
//				String niptApproverDate = read.get("nipt_approver_date");
//				String niptApproverDesignation = read.get("nipt_approver designation");
//				String niptApproverName = read.get("nipt_approver_name");
//				String niptCustomOfficerName = read.get("nipt_custom_officer_name");
//				String niptCustomsOfficerDesignation = read.get("nipt_customs_officer_designation");
//				String niptDateOfEscort = read.get("nipt_date_of_escort:");
//				String niptDeputedFromDestination = read.get("nipt_deputed_from_destination:");
//				String niptDeputedToDestination = read.get("nipt_deputed_to_destination");
//				String niptStatus = read.get("nipt_status");
//				String noc = read.get("noc");
//				String nop = read.get("nop");
//				String nsdlStatus = read.get("nsdl_status");
//				String nsdlOverrideDocs = read.get("nsdl_override_docs");
//				String outDate = read.get("out_date");
//				String packageContentType = read.get("package_content_type");
//				String parcelType = read.get("parcel_type");
//				String representativeId = read.get("representative_id");
//				String pcStatus = read.get("pc_status");
//				String pctmNo = read.get("pctm_no");
//				String portOrigin = read.get("port_origin");
//				String qrCodeUrl = read.get("qr_code_url");
//				String reasonForOverride = read.get("reason_for_override");
//				String scStatus = read.get("sc_status");
//				String sezEntityId = read.get("sez_entity_id");
//				String sirDate = read.get("sir_date");
//				String status = read.get("status");
//				String tpDate = read.get("tp_date");
//				String tpNo = read.get("tp_no");
//				String uomPackages = read.get("uom_packages");
//				String uomWeight = read.get("uom_weight");
//				String wrongDepositFilePath = read.get("wrong_deposit_file_path");
//				String wrongDepositStatus = read.get("wrong_deposit_status");
//				String wrongDepositRemarks = read.get("wrong_deposit_remarks");
//				String detentionReceiptNo = read.get("detention_receipt_no");
//				String mopStatus = read.get("mop_status");
//
//				Import import1 = new Import();
//				String autoIncrementIMPTransId = proccessNextIdService.autoIncrementIMPTransId();
//				import1.setCompanyId(companyid);
//				import1.setBranchId(branchId);
//				import1.setMawb(imptransid + mawb);
//				if (!hawb.isEmpty()) {
//					import1.setHawb(hawb + holdBy);
//				} else {
//					import1.setHawb("0000");
//				}
//				import1.setImpTransId(autoIncrementIMPTransId);
//
//				import1.setAssessableValue(assessableValue);
//	            if(!outDate.isEmpty()) {
//	            	if("Handed over to Party/CHA".equals(dgdcStatus) || "Exit from DGDC SEEPZ Gate".equals(dgdcStatus) ) {
//	            		import1.setOutDate(parseDate4(outDate));
//	            	}
//	            }
//				import1.setNSDL_Status(nsdlStatus);
//				import1.setChaCde(chaCde);
//				import1.setCartingAgent(cartingAgent);
//				import1.setPartyRepresentativeId(representativeId);
//				// import1.setChaName(chaName);
//				import1.setPctmNo(pctmNo);
//				import1.setTpNo(tpNo);
//				if (!sirDate.isEmpty()) {
//					import1.setTpDate(parseDate(sirDate));
//				}
//
//				import1.setIgmNo(igmNo);
//				if (!igmDate.isEmpty()) {
//					import1.setIgmDate(parseDate(igmDate));
//				}
//				if (!grossWeight.isEmpty()) {
//					import1.setGrossWeight(new BigDecimal(grossWeight));
//				}
//
//				if (!imposePenaltyRemarks.isEmpty() && imposePenaltyRemarks != null) {
//					if (imposePenaltyRemarks.startsWith("RI")) {
//						Party party = partyrepo.findbyentityid(companyid, branchId, sezEntityId);
//						import1.setHandedOverToType("P");
//						import1.setHandedOverPartyId(party.getPartyId());
//						import1.setHandedOverRepresentativeId(imposePenaltyRemarks);
//					}
//				}
//			
//
////			    if(!imposePenaltyAmt.isEmpty() && imposePenaltyAmt != null) {
////			    	if(imposePenaltyAmt.startsWith("RI")) {
////			    		RepresentParty repesent = representpartyrepo.findByFNameAndLName(companyid, branchId, imposePenaltyAmt);
////				    	import1.setHandedOverToType("C");
////				    	import1.setHandedOverPartyId(repesent.getUserId());
////				    	import1.setHandedOverRepresentativeId(imposePenaltyAmt);
////			    	}
////			    }
//
//				if (!holdStatus.isEmpty() && holdStatus != null) {
//					if ("No".equals(holdStatus.trim())) {
//						import1.setHoldStatus("N");
//					}
//					if ("Yes".equals(holdStatus.trim())) {
//						import1.setHoldStatus("Y");
//					}
//				}
//
//				if (!scStatus.isEmpty() && scStatus != null) {
//					if ("No".equals(scStatus.trim())) {
//						import1.setScStatus("N");
//					}
//					if ("Yes".equals(scStatus.trim())) {
//						import1.setScStatus("Y");
//					}
//				}
//				else {
//					import1.setScStatus("N");
//				}
//				
//				if (!hpStatus.isEmpty() && hpStatus != null) {
//					if ("No".equals(hpStatus.trim())) {
//						import1.setHpStatus("N");
//					}
//					if ("Yes".equals(hpStatus.trim())) {
//						import1.setHpStatus("Y");
//					}
//				}
//				else {
//					import1.setHpStatus("N");
//				}
//
//				if (!pcStatus.isEmpty() && pcStatus != null) {
//					if ("No".equals(pcStatus.trim())) {
//						import1.setPcStatus("N");
//					}
//					if ("Yes".equals(pcStatus.trim())) {
//						import1.setPcStatus("Y");
//						ImportPC importpc = new ImportPC();
//						importpc.setCompanyId(companyid);
//						importpc.setBranchId(branchId);
//						importpc.setAddress(dgdcCargoOutScan);
//						importpc.setApprovedBy(user_Id);
//						importpc.setApprovedDate(parseDate(sirDate));
//						importpc.setApproverDesignation(niptDateOfEscort);
//						importpc.setApprovedDate(parseDate4(niptDeputedFromDestination));
//						importpc.setApproverName(niptCustomsOfficerDesignation);
//						importpc.setConfirmation('Y');
//						importpc.setCreatedBy(user_Id);
//						importpc.setCreatedDate(parseDate(sirDate));
//						importpc.setDeputedCoDesignation(editedDate);
//						importpc.setDeputedCoName(editedBy);
//						importpc.setDeputedFromDestination(niptApproverDesignation);
//						importpc.setDeputedToDestination(niptApproverName);
//						importpc.setEscortDate(parseDate4(niptCustomOfficerName));
//						importpc.setFlightDate(parseDate4(doDate));
//						importpc.setFlightNo(dgdcSeepzInScan);
//						importpc.setHawb(hawb.trim() + holdBy.trim());
//						importpc.setMawb(imptransid.trim() + mawb.trim());
//						importpc.setNationality(doNumber);
//						importpc.setPassengerName(dgdcCargoInScan);
//						importpc.setPassportNo(dgdcSeepzInScan);
//						importpc.setSirNo(sirNo);
//						importpc.setStatus("A");
//						importpcrepo.save(importpc);
//
//					}
//				}
//				else {
//					import1.setPcStatus("N");
//				}
//				
//				
//				import1.setCloseStatus("Y");
//				
//					if (!flightNo.isEmpty()) {
//						String flight = flightNo.trim();
//						String flight1 = flight.substring(0,2);
//						System.out.println("flight " + flight);
//						Airline airline = airlinerepo.findByAirlineShortCode(companyid, branchId, flight1.trim());
//						import1.setFlightNo(flightNo.trim());
//						if (!flightDate.isEmpty()) {
//							import1.setFlightDate(parseDate(flightDate));
//						}
//						import1.setAirlineCode(airline.getAirlineCode());
//						import1.setAirlineName(airline.getAirlineName());
//						import1.setNiptStatus("N");
//					} else {
//						if(pcStatus.isEmpty() || "No".equals(pcStatus.trim())) {
//							
//							import1.setNiptStatus("Y");
//							import1.setNiptCustomOfficerName(editedBy);
//							import1.setNiptCustomsOfficerDesignation(niptApproverDate);
//							import1.setNiptApproverDate(parseDate4(niptDeputedFromDestination));
//							import1.setNiptApproverDesignation(niptDateOfEscort);
//							import1.setNiptApproverName(niptCustomsOfficerDesignation);
//							import1.setNiptDateOfEscort(parseDate4(niptCustomOfficerName));
//							import1.setNiptDeputedFromDestination(niptApproverDesignation);
//							import1.setNiptDeputedToDestination(niptApproverName);
//						
//						}
//					
//					
//				}
//
//				import1.setCountryOrigin(countryOrigin);
//				import1.setPortOrigin(portOrigin);
//				// import1.setConsoleName(consoleName);
//
//				if (!sezEntityId.isEmpty() && sezEntityId != null) {
//					Party party = partyrepo.findbyentityid(companyid, branchId, sezEntityId);
//					import1.setImporterId(party.getPartyId());
//					import1.setImporternameOnParcel(party.getPartyName());
//					import1.setIec(party.getIecNo());
//					import1.setSezEntityId(sezEntityId);
//
//					DefaultPartyDetails defaultparty = defaultrepo.getdatabyuser_id(companyid, branchId,
//							party.getPartyId());
//
//					if (defaultparty == null) {
//						// Handle the case when defaultparty is null or both expCHA and expConsole are
//						// null or empty
//						// import1.setChaCde(chaCde);
//						import1.setChaName("SELF");
//						import1.setConsoleName("EU0009");
//					} else if (defaultparty.getExpCHA() == null && defaultparty.getExpConsole() == null) {
//						// import1.setChaNo("EU0021");
//						import1.setChaName("SELF");
//						import1.setConsoleName("EU0009");
//					} else if (defaultparty.getExpCHA() == null) {
//						// Handle the case when expCHA is null or empty
//						// import1.setChaNo("EU0021");
//						import1.setChaName("SELF");
//						import1.setConsoleName(defaultparty.getImpConsole());
//					} else if (defaultparty.getExpConsole() == null) {
//						// Handle the case when expConsole is null or empty
//						ExternalParty external = externalPartyRepository.getalldatabyid(companyid, branchId,
//								defaultparty.getImpCHA());
//						// import1.setChaNo(defaultparty.getExpCHA());
//						if (external != null) {
//							import1.setChaName(external.getUserName());
//						}
//						import1.setConsoleName("EU0009");
//					} else {
//						// Handle the case when both expCHA and expConsole have values
//						ExternalParty external = externalPartyRepository.getalldatabyid(companyid, branchId,
//								defaultparty.getImpCHA());
//						// import1.setChaNo(defaultparty.getExpCHA());
//						if (external != null) {
//							import1.setChaName(external.getUserName());
//						}
//						import1.setConsoleName(defaultparty.getImpConsole());
//					}
//				}
//
//				import1.setImportRemarks(importRemarks);
//
//				import1.setPackageContentType(packageContentType);
//
//				if (!nop.isEmpty()) {
//					int nop1 = Integer.parseInt(nop);
//					import1.setNop(nop1);
//				}
//				import1.setStatus("A");
//				import1.setUomPackages(uomPackages);
//				import1.setAssessableValue(assessableValue);
//				import1.setDescriptionOfGoods(descriptionOfGoods);
//				import1.setDgdcStatus(dgdcStatus.trim());
//				if (!grossWeight.isEmpty()) {
//					BigDecimal big = new BigDecimal(grossWeight);
//					import1.setGrossWeight(big);
//				}
//				import1.setUomWeight(uomWeight);
//				import1.setBeRequestId(beRequestId + cancelRemarks);
//				import1.setBeNo(beNo);
//				if (!beDate.isEmpty()) {
//					import1.setBeDate(parseDate(beDate));
//				}
//				// import1.setImportAddress(importAddress);
//
//				if (!sirNo.isEmpty()) {
//					import1.setSirNo(sirNo);
//					if (!sirDate.isEmpty()) {
//						import1.setSirDate(parseDate(sirDate));
//					}
//					importrepo.save(import1);
//
//					Import_History importhistory = new Import_History();
//					importhistory.setBranchId(branchId);
//					importhistory.setCompanyId(companyid);
//					importhistory.setHawb(import1.getHawb());
//					importhistory.setMawb(import1.getMawb());
//					importhistory.setNewStatus("Handed over to DGDC SEEPZ");
//					importhistory.setOldStatus("Entry at DGDC SEEPZ Gate");
//					importhistory.setSirNo(import1.getSirNo());
//					importhistory.setUpdatedBy(user_Id);
//					importhistory.setTransport_Date(import1.getSirDate());
//					importhistoHistoryRepo.save(importhistory);
//
//					for (int i = 1; i <= import1.getNop(); i++) {
//						String srNo = String.format("%04d", i);
//						Gate_In_Out gateinout = new Gate_In_Out();
//						gateinout.setCompanyId(import1.getCompanyId());
//						gateinout.setBranchId(import1.getBranchId());
//						gateinout.setNop(import1.getNop());
//						gateinout.setErp_doc_ref_no(import1.getMawb());
//						gateinout.setDoc_ref_no(import1.getHawb());
//						gateinout.setSr_No(import1.getSirNo() + srNo);
//						gateinout.setDgdc_cargo_in_scan("N");
//						gateinout.setDgdc_cargo_out_scan("N");
//						gateinout.setDgdc_seepz_in_scan("N");
//						gateinout.setDgdc_seepz_out_scan("N");
//
//						gateinoutrepo.save(gateinout);
//						System.out.println("gateinout " + gateinout);
//					}
//				}
//			}
//
//			return "success";
//		}
	
	
	
	    public String convertFileToImport(InputStream inputStream, String companyid, String branchId, String user_Id)
				throws IOException, ParseException {
			InputStreamReader reader = new InputStreamReader(inputStream);
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
			System.out.println("csvParser " + csvParser);
			for (CSVRecord read : csvParser) {
				String imptransid = read.get("imp_trans_id");
				String mawb = read.get("mawb");
				String sirNo = read.get("sir_no");
				String airlineCode = read.get("airline_code");
				String airlineName = read.get("airline_name");
				String approveBy = read.get("approved_by");
				String approvedDate = read.get("approved_date");				
				BigDecimal assessableValue =  new BigDecimal(read.get("assessable_value"));
				String beDate = read.get("be_date");
				String beNo = read.get("be_no");
				String beRequestId = read.get("be_request_id");
				String cancelRemarks = read.get("cancel_remarks");
				String cancelStatus = read.get("cancel_status");
				String cartingAgent = read.get("carting_agent");
				String chaCde = read.get("cha_cde");
				String chaName = read.get("cha_name");
				String closeStatus = read.get("close_status");
				String consoleName = read.get("console_name");
				String countryOrigin = read.get("country_origin");
				String createdBy = read.get("created_by");
				String createdDate = read.get("created_date");
				String descriptionOfGoods = read.get("description_of_goods");
				String dgdcStatus = read.get("dgdc_status");
				String dgdcCargoInScan = read.get("dgdc_cargo_in_scan");
				String dgdcCargoOutScan = read.get("dgdc_cargo_out_scan");
				String dgdcSeepzInScan = read.get("dgdc_seepz_in_scan");
				String dgdcSeepzOutScan = read.get("dgdc_seepz_out_scan");
				String doDate = read.get("do_date");
				String doNumber = read.get("do_number");
				String editedBy = read.get("edited_by");
				String editedDate = read.get("edited_date");
				String flightDate = read.get("flight_date");
				String flightNo = read.get("flight_no");
				String forwardedStatus = read.get("forwarded_status");
				String gatePassStatus = read.get("gate_pass_status");
				String grossWeight = read.get("gross_weight");
				String handedOverPartyId = read.get("handed_over_party_id");
				String handedOverRepresentativeId = read.get("handed_over_representative_id");
				String handedOverToType = read.get("handed_over_to_type");
				String hawb = read.get("hawb");
				String holdBy = read.get("hold_by");
				String holdDate = read.get("hold_date");
				String holdStatus = read.get("hold_status");
				String hpStatus = read.get("hp_status");
				String hpWeight = read.get("hp_weight");
				String hpPackageNo = read.get("hp_package_no");
				String iec = read.get("iec");
				String igmDate = read.get("igm_date");
				String igmNo = read.get("igm_no");
				String impTransDate = read.get("imp_trans_date");
				String importAddress = read.get("import_address");
				String importRemarks = read.get("import_remarks");
				String importerId = read.get("importer_id");
				String importerNameOnParcel = read.get("importer_name_on_parcel");
				String imposePenaltyAmt = read.get("impose_penalty_amt");
				String imposePenaltyRemarks = read.get("impose_penalty_remarks");
				String niptApproverDate = read.get("nipt_approver_date");
				String niptApproverDesignation = read.get("nipt_approver designation");
				String niptApproverName = read.get("nipt_approver_name");
				String niptCustomOfficerName = read.get("nipt_custom_officer_name");
				String niptCustomsOfficerDesignation = read.get("nipt_customs_officer_designation");
				String niptDateOfEscort = read.get("nipt_date_of_escort:");
				String niptDeputedFromDestination = read.get("nipt_deputed_from_destination:");
				String niptDeputedToDestination = read.get("nipt_deputed_to_destination");
				String niptStatus = read.get("nipt_status");
				String noc = read.get("noc");
				String nop = read.get("nop");
				String nsdlStatus = read.get("nsdl_status");
				String nsdlOverrideDocs = read.get("nsdl_override_docs");
				String outDate = read.get("out_date");
				String packageContentType = read.get("package_content_type");
				String parcelType = read.get("parcel_type");
				String representativeId = read.get("representative_id");
				String pcStatus = read.get("pc_status");
				String pctmNo = read.get("pctm_no");
				String portOrigin = read.get("port_origin");
				String qrCodeUrl = read.get("qr_code_url");
				String reasonForOverride = read.get("reason_for_override");
				String scStatus = read.get("sc_status");
				String sezEntityId = read.get("sez_entity_id");
				String sirDate = read.get("sir_date");
				String status = read.get("status");
				String tpDate = read.get("tp_date");
				String tpNo = read.get("tp_no");
				String uomPackages = read.get("uom_packages");
				String uomWeight = read.get("uom_weight");
				String wrongDepositFilePath = read.get("wrong_deposit_file_path");
				String wrongDepositStatus = read.get("wrong_deposit_status");
				String wrongDepositRemarks = read.get("wrong_deposit_remarks");
				String detentionReceiptNo = read.get("detention_receipt_no");
				String mopStatus = read.get("mop_status");

				Import import1 = new Import();
				String autoIncrementIMPTransId = proccessNextIdService.autoIncrementIMPTransId();
				import1.setCompanyId(companyid);
				import1.setBranchId(branchId);
				import1.setMawb(imptransid + mawb);
				if (!hawb.isEmpty()) {
					import1.setHawb(hawb + holdBy);
				} else {
					import1.setHawb("0000");
				}
				import1.setImpTransId(autoIncrementIMPTransId);

				import1.setAssessableValue(assessableValue);
				
					if ((dgdcStatus.trim()).startsWith("Handed over to Party") || (dgdcStatus.trim()).startsWith("Exit from DGDC SEEPZ")) {
						
						import1.setOutDate(parseDate4(outDate));
						System.out.println(parseDate4(outDate));
				}
				import1.setNSDL_Status(nsdlStatus);
				import1.setChaCde(chaCde);
				import1.setCartingAgent(cartingAgent);
				import1.setPartyRepresentativeId(representativeId);
				// import1.setChaName(chaName);
				import1.setPctmNo(pctmNo);
				import1.setTpNo(tpNo);
				if (!tpDate.isEmpty()) {
					import1.setTpDate(parseDate4(tpDate));
					
				}

				import1.setIgmNo(igmNo);
				if (!igmDate.isEmpty()) {
					import1.setIgmDate(parseDate(igmDate));
				}
				if (!grossWeight.isEmpty()) {
					import1.setGrossWeight(new BigDecimal(grossWeight));
				}

				if (!imposePenaltyRemarks.isEmpty() && imposePenaltyRemarks != null) {
					if (imposePenaltyRemarks.startsWith("RI")) {
						Party party = partyrepo.findbyentityid(companyid, branchId, sezEntityId);
						import1.setHandedOverToType("P");
						import1.setHandedOverPartyId(party.getPartyId());
						import1.setHandedOverRepresentativeId(imposePenaltyRemarks);
					}
				}

//			    if(!imposePenaltyAmt.isEmpty() && imposePenaltyAmt != null) {
//			    	if(imposePenaltyAmt.startsWith("RI")) {
//			    		RepresentParty repesent = representpartyrepo.findByFNameAndLName(companyid, branchId, imposePenaltyAmt);
//				    	import1.setHandedOverToType("C");
//				    	import1.setHandedOverPartyId(repesent.getUserId());
//				    	import1.setHandedOverRepresentativeId(imposePenaltyAmt);
//			    	}
//			    }

				if (!holdStatus.isEmpty() && holdStatus != null) {
					if ("No".equals(holdStatus.trim())) {
						import1.setHoldStatus("N");
					}
					if ("Yes".equals(holdStatus.trim())) {
						import1.setHoldStatus("Y");
					}
				}

				if (!scStatus.isEmpty() && scStatus != null) {
					if ("No".equals(scStatus.trim())) {
						import1.setScStatus("N");
					}
					if ("Yes".equals(scStatus.trim())) {
						import1.setScStatus("Y");
					}
				} else {
					import1.setScStatus("N");
				}

				if (!hpStatus.isEmpty() && hpStatus != null) {
					if ("No".equals(hpStatus.trim())) {
						import1.setHpStatus("N");
					}
					if ("Yes".equals(hpStatus.trim())) {
						import1.setHpStatus("Y");
					}
				} else {
					import1.setHpStatus("N");
				}

				if (!pcStatus.isEmpty() && pcStatus != null) {
					if ("No".equals(pcStatus.trim())) {
						import1.setPcStatus("N");
					}
					if ("Yes".equals(pcStatus.trim())) {
						import1.setPcStatus("Y");
						ImportPC importpc = new ImportPC();
						importpc.setCompanyId(companyid);
						importpc.setBranchId(branchId);
						importpc.setAddress(dgdcCargoOutScan);
						importpc.setApprovedBy(user_Id);
						importpc.setApprovedDate(parseDate(sirDate));
						importpc.setApproverDesignation(niptDateOfEscort);
						importpc.setApprovedDate(parseDate4(niptDeputedFromDestination));
						importpc.setApproverName(niptCustomsOfficerDesignation);
						importpc.setConfirmation('Y');
						importpc.setCreatedBy(user_Id);
						importpc.setCreatedDate(parseDate(sirDate));
						importpc.setDeputedCoDesignation(editedDate);
						importpc.setDeputedCoName(editedBy);
						importpc.setDeputedFromDestination(niptApproverDesignation);
						importpc.setDeputedToDestination(niptApproverName);
						importpc.setEscortDate(parseDate4(niptCustomOfficerName));
						importpc.setFlightDate(parseDate4(doDate));
						importpc.setFlightNo(dgdcSeepzInScan);
						importpc.setHawb(hawb.trim() + holdBy.trim());
						importpc.setMawb(imptransid.trim() + mawb.trim());
						importpc.setNationality(doNumber);
						importpc.setPassengerName(dgdcCargoInScan);
						importpc.setPassportNo(dgdcSeepzInScan);
						importpc.setSirNo(sirNo);
						importpc.setStatus("A");
						importpcrepo.save(importpc);

					}
				} else {
					import1.setPcStatus("N");
				}

				import1.setCloseStatus("Y");

				if (!flightNo.isEmpty()) {
					String flight = flightNo.trim();
					String flight1 = flight.substring(0, 2);
					System.out.println("flight " + flight);
					Airline airline = airlinerepo.findByAirlineShortCode(companyid, branchId, flight1.trim());
					import1.setFlightNo(flightNo.trim());
					if (!flightDate.isEmpty()) {
						import1.setFlightDate(parseDate(flightDate));
					}
					import1.setAirlineCode(airline.getAirlineCode());
					import1.setAirlineName(airline.getAirlineName());
					import1.setNiptStatus("N");
				} else {
					if (pcStatus.isEmpty() || "No".equals(pcStatus.trim())) {

						import1.setNiptStatus("Y");
						import1.setNiptCustomOfficerName(editedDate);
						import1.setNiptCustomsOfficerDesignation(niptApproverDate);
						import1.setNiptApproverDate(parseDate4(niptDeputedFromDestination));
						import1.setNiptApproverDesignation(niptDateOfEscort);
						import1.setNiptApproverName(niptCustomsOfficerDesignation);
						import1.setNiptDateOfEscort(parseDate4(niptCustomOfficerName));
						import1.setNiptDeputedFromDestination(niptApproverDesignation);
						import1.setNiptDeputedToDestination(niptApproverName);

					}

				}

				import1.setCountryOrigin(countryOrigin);
				import1.setPortOrigin(portOrigin);
				// import1.setConsoleName(consoleName);

				if (!sezEntityId.isEmpty() && sezEntityId != null) {
					Party party = partyrepo.findbyentityid(companyid, branchId, sezEntityId);
					import1.setImporterId(party.getPartyId());
					import1.setImporternameOnParcel(party.getPartyName());
					import1.setIec(party.getIecNo());
					import1.setSezEntityId(sezEntityId);
					import1.setChaName(chaName);
					DefaultPartyDetails defaultparty = defaultrepo.getdatabyuser_id(companyid, branchId,
							party.getPartyId());

					if (defaultparty == null) {
						
						import1.setConsoleName("EU0009");
					} else if (defaultparty.getImpConsole() == null) {
						
						import1.setConsoleName("EU0009");
					}   else {
					
						import1.setConsoleName(defaultparty.getImpConsole());
					}
				}

				import1.setImportRemarks(importRemarks);

				import1.setPackageContentType(packageContentType);

				if (!nop.isEmpty()) {
					int nop1 = Integer.parseInt(nop);
					import1.setNop(nop1);
				}
				import1.setStatus("A");
				import1.setUomPackages(uomPackages);
				import1.setAssessableValue(assessableValue);
				import1.setDescriptionOfGoods(descriptionOfGoods);
				import1.setDgdcStatus(dgdcStatus.trim());
				if (!grossWeight.isEmpty()) {
					BigDecimal big = new BigDecimal(grossWeight);
					import1.setGrossWeight(big);
				}
				import1.setUomWeight(uomWeight);
				import1.setBeRequestId(beRequestId + cancelRemarks);
				import1.setBeNo(beNo);
				if (!beDate.isEmpty()) {
					import1.setBeDate(parseDate(beDate));
				}
				// import1.setImportAddress(importAddress);

				if (!sirNo.isEmpty()) {
					import1.setSirNo(sirNo);
					if (!sirDate.isEmpty()) {
						import1.setSirDate(parseDate(sirDate));
					}
					importrepo.save(import1);

					Import_History importhistory = new Import_History();
					importhistory.setBranchId(branchId);
					importhistory.setCompanyId(companyid);
					importhistory.setHawb(import1.getHawb());
					importhistory.setMawb(import1.getMawb());
					importhistory.setNewStatus("Handed over to DGDC SEEPZ");
					importhistory.setOldStatus("Entry at DGDC SEEPZ Gate");
					importhistory.setSirNo(import1.getSirNo());
					importhistory.setUpdatedBy(user_Id);
					importhistory.setTransport_Date(import1.getSirDate());
					importhistoHistoryRepo.save(importhistory);

					for (int i = 1; i <= import1.getNop(); i++) {
						String srNo = String.format("%04d", i);
						Gate_In_Out gateinout = new Gate_In_Out();
						gateinout.setCompanyId(import1.getCompanyId());
						gateinout.setBranchId(import1.getBranchId());
						gateinout.setNop(import1.getNop());
						gateinout.setErp_doc_ref_no(import1.getMawb());
						gateinout.setDoc_ref_no(import1.getHawb());
						gateinout.setSr_No(import1.getSirNo() + srNo);
						gateinout.setDgdc_cargo_in_scan("N");
						gateinout.setDgdc_cargo_out_scan("N");
						gateinout.setDgdc_seepz_in_scan("N");
						gateinout.setDgdc_seepz_out_scan("N");

						gateinoutrepo.save(gateinout);
						System.out.println("gateinout " + gateinout);
					}
				}
			}

			return "success";
		}   
	    
	    
	    
	    
	    
	    
	
	
	
	
//	 public static class PartyError {
//	        private String partyName;
//	        private String parameterWithError;
//
//	        public PartyError(int rowNumber, String partyName, String parameterWithError) {
//	            this.partyName = partyName;
//	            this.parameterWithError = parameterWithError;
//	        }
//
//	        public String getPartyName() {
//	            return partyName;
//	        }
//
//	        public String getParameterWithError() {
//	            return parameterWithError;
//	        }
//	    }
//	 
//	 public static class ResultObject {
//	        private List<Party> parties;
//	        private List<PartyError> errors;
//
//	        public List<Party> getParties() {
//	            return parties;
//	        }
//
//	        public void setParties(List<Party> parties) {
//	            this.parties = parties;
//	        }
//
//	        public List<PartyError> getErrors() {
//	            return errors;
//	        }
//
//	        public void setErrors(List<PartyError> errors) {
//	            this.errors = errors;
//	        }
//	    }
//	
//
//	 public ExcelImportHelper.ResultObject convertFileToListOfParty(InputStream inputStream, String companyId, String branchId, String userId)
//	            throws Exception {
//	    InputStreamReader reader = new InputStreamReader(inputStream);
//	    List<Party> list = new ArrayList<>();
//	    List<PartyError> errorList = new ArrayList<>(); // To store party errors
//	    int rowNumber = 0; // Initialize row number counter
//
//	    try {
//	        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
//
//	        for (CSVRecord record : csvParser) {
//	            rowNumber++; // Increment row number for each record
//	            Party party = new Party();
//	          
//	            String nextId = proccessNextIdService.autoIncrementNextIdNext();
//	           
//	            party.setPartyId(nextId);
//	            party.setCompanyId(companyId);
//	            party.setBranchId(branchId);
//
//	            String partyName = record.get("PartyName");
//	            String email = record.get("Email");
//	            String mobileNo = record.get("MobileNo");
//	            String iecNo = record.get("IECNo");
//	            String entityId = record.get("EntityId");
//	            String panNo = record.get("PanNo");
//	            String gstNo = record.get("GstNo");
//	            String loaNumber = record.get("LoaNumber");
//	            Date currentDate = Calendar.getInstance().getTime();
//	            String dateStr = record.get("LoaExpiryDate");
//	            String creditLimit = record.get("CreditLimit");
//	            
//	            
//	            
//	            
//	            party.setPanNo(panNo);
//                party.setGstNo(gstNo);
//                party.setMobileNo(mobileNo);
//                
//                
//	            // Date format
//	            if (dateStr != null && !dateStr.isEmpty()) {
//	                SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEEE, dd MMMM, yyyy", Locale.ENGLISH);
//	                Date loaExpiryDate = inputDateFormat.parse(dateStr);
//	                party.setLoaExpiryDate(loaExpiryDate);
//	            } else {
//	            	Date loaExpiryDate = parse(dateStr);
//	            	 party.setLoaExpiryDate(loaExpiryDate);
//	            }
//
//	            // Credit limit exception check
//	            if (creditLimit != null && !creditLimit.isEmpty()) {
//	              
//	                    double creditLimit1 = Double.parseDouble(creditLimit);
//	                    party.setCreditLimit(creditLimit1);	               
//	            }                
//	            
//	                          
//	            // Party name  
//	            try {
//	                partyName = record.get("PartyName");
//	                if (partyName != null && !partyName.isEmpty()) {
//	                    party.setPartyName(partyName);
//	                } else {
//	                    errorList.add(new PartyError(rowNumber, "PartyName", partyName)); // Add error with row number and column name
//	                }
//	            } catch (Exception e) {
//	                errorList.add(new PartyError(rowNumber, "PartyName", null)); // Add error with row number and column name, with a null value for partyName
//	            }
//
//     
//	            
//	         // Email
//	            try {
//	                email = record.get("Email");
//	                if (email != null && !email.isEmpty()) {
//	                    party.setEmail(email);
//	                } else {
//	                    errorList.add(new PartyError(rowNumber, "Email", email)); // Add error with row number and column name
//	                }
//	            } catch (Exception e) {
//	                errorList.add(new PartyError(rowNumber, "Email", null)); // Add error with row number and column name, with a null value for email
//	            }
//
//	
//
//	         // IEC Number
//	            
//	            try {
//	                iecNo = record.get("IECNo");
//	                if (iecNo != null && !iecNo.isEmpty()) {
//	                    party.setIecNo(iecNo);
//	                } else {
//	                    errorList.add(new PartyError(rowNumber, "IECNo", iecNo)); // Add error with row number and column name
//	                }
//	            } catch (Exception e) {
//	                errorList.add(new PartyError(rowNumber, "IECNo", null)); // Add error with row number and column name, with a null value for iecNo
//	            }
//
//	            // Entity ID
//	            try {
//	                entityId = record.get("EntityId");
//	                if (entityId != null && !entityId.isEmpty()) {
//	                    party.setEntityId(entityId);
//	                } else {
//	                    errorList.add(new PartyError(rowNumber, "EntityId", entityId)); // Add error with row number and column name
//	                }
//	            } catch (Exception e) {
//	                errorList.add(new PartyError(rowNumber, "EntityId", null)); // Add error with row number and column name, with a null value for entityId
//	            }
//
// 
//	           
//	         // LOA Number
//	            
//	            try {
//	                loaNumber = record.get("LoaNumber");
//	                if (loaNumber != null && !loaNumber.isEmpty()) {
//	                    party.setLoaNumber(loaNumber);
//	                } else {
//	                    errorList.add(new PartyError(rowNumber, "LoaNumber", loaNumber)); // Add error with row number and column name
//	                }
//	            } catch (Exception e) {
//	                errorList.add(new PartyError(rowNumber, "LoaNumber", null)); // Add error with row number and column name, with a null value for loaNumber
//	            }
//
//
//	            
//	            // Status
//	            String status = record.get("Status");
//	            String status1 = status != null && !status.isEmpty() ? String.valueOf(status.charAt(0)) : null;                   
//	            if (status1 != null && !status1.isEmpty()) {
//	                party.setStatus(status1);
//	            }
//	            if (status1 == null || status1.isEmpty()) {
//	                party.setStatus("N");
//	            }
//	            
//	            
//	            // Default set values from backend
//	            
//	            party.setCreatedBy(userId);
//	            party.setCreatedDate(currentDate);
//	            party.setAddress1("Pune");
//	            party.setAddress2("Pune");
//	            party.setAddress3("Pune");
//	            party.setUnitAdminName(userId);
//	            party.setUnitType(userId);
//
//	            
//	                list.add(party);
//	            
//	        }
//	    } catch (UncheckedIOException uioe) {
//	        uioe.printStackTrace();
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    } finally {
//	        reader.close();
//	    }
//
//	    // Create and return a result object containing both the list of parties and the error list
//	    ExcelImportHelper.ResultObject result = new ExcelImportHelper.ResultObject();
//	    result.setParties(list);
//	    result.setErrors(errorList);
//	    return result;
//	}
//
//


	public String convertFileToDetention(InputStream inputStream, String companyid, String branchId, String user_Id)
            throws IOException, ParseException {
        InputStreamReader reader = new InputStreamReader(inputStream);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

        for (CSVRecord read : csvParser) {
           // String nexttransid = proccessNextIdService.autoIncrementDetentionId();
            
            String approvedby = read.get("approved_by");
            String approvedDate = read.get("approved_date");
           
            String createdBy = read.get("created_by");
            String createdDate = read.get("created_date");
           
            String Status = read.get("status");
            String editedBY = read.get("edited_by");
            String editedDate = read.get("edited_date");
            String depositDate = read.get("deposit_date");
            String exporter = read.get("party_id");
           
            String nop = read.get("nop");
            
            
            String detentionId = read.get("detention_id");
            String fileNo = read.get("file_no");
            String siNo = read.get("si_no");
            String dgdcOfficerName = read.get("dgdc_officer_name");
            String remarks = read.get("remarks");
            String dgdcOfficerDesignation = read.get("dgdc_officer_designation");
            String officerDesignation = read.get("officer_designation");
            String officerName = read.get("officer_name");
            String otherParty = read.get("other_party");
            String parcelDetainedBy = read.get("parcel_detained_by");
            String parcelType = read.get("parcel_type");
            String withdrawDate = read.get("withdraw_date");
            String withdrawDgdcOfficerDesignation = read.get("withdraw_dgdc_officer_designation");
            String withdrawOfficerNamedgdc = read.get("withdraw_dgdc_officer_name");
            String withdrwOfficerName = read.get("withdraw_officer_name");
            String withdrawRemark = read.get("withdraw_remarks");
            
            
            
            
            
            
            
            
//            String cargoin = read.get("dgdc_cargo_in_scan");
//            String cargoout = read.get("dgdc_cargo_out_scan");
//            String seepzin = read.get("dgdc_seepz_in_scan");
//            String seepzout = read.get("dgdc_seepz_out_scan");
//            String noc = read.get("noc");
//            String outDate = read.get("out_date");
//            String forwardedStatus = read.get("forwarded_status");
//            String gatePass = read.get("gate_pass_status");
//            String mopStatus = read.get("mop_status");
            Party party = partyrepo.getdatabypartyname(companyid, branchId, exporter);
            
            
            
            Detention detention = new Detention();
            detention.setCompanyId(companyid);
            detention.setBranchId(branchId);
            detention.setDetentionId(detentionId);
            detention.setFileNo(fileNo);
            detention.setSiNo(Long.parseLong(siNo));
            detention.setApprovedBy(approvedby);
            detention.setApprovedDate(parseDate5(approvedDate));
            detention.setCreatedBy(createdBy);
            detention.setCreatedDate(parseDate5(createdDate));
            detention.setDepositDate(parseDate5(depositDate));
            detention.setDgdcOfficerName(dgdcOfficerName);
            detention.setNop(Integer.parseInt(nop));
            detention.setStatus(Status);
            detention.setOfficerName(officerName);
            detention.setOtherParty(otherParty);
            detention.setParcelDetainedBy(parcelDetainedBy.trim());
            detention.setParcelType(parcelType.trim());
            detention.setPartyId(exporter);
            detention.setRemarks(remarks);
            if(!withdrawDate.isEmpty()) {
            
            detention.setWithdrawDate(parseDate5(withdrawDate));
            }
            
            detention.setWithdrawOfficerName(withdrawOfficerNamedgdc);
            detention.setWithdrawDgdcOfficerName(withdrawDgdcOfficerDesignation);
            detention.setWithdrawRemarks(withdrawRemark);
            

            if (!fileNo.isEmpty()) {

            	detentionRepository.save(detention);               
             

                for (int i = 1; i <= detention.getNop(); i++) {
                    String srNo = String.format("%04d", i);
                    Gate_In_Out gateinout = new Gate_In_Out();
                    gateinout.setCompanyId(detention.getCompanyId());
                    gateinout.setBranchId(detention.getBranchId());
                    gateinout.setNop(detention.getNop());
                    gateinout.setErp_doc_ref_no(String.valueOf(detention.getSiNo()));
                    gateinout.setDoc_ref_no(detention.getDetentionId());
                    gateinout.setSr_No(detention.getDetentionId() + srNo);
                    gateinout.setDgdc_cargo_in_scan("N");
                    gateinout.setDgdc_cargo_out_scan("N");
                    gateinout.setDgdc_seepz_in_scan("N");
                    gateinout.setDgdc_seepz_out_scan("N");

                    gateinoutrepo.save(gateinout);
                }

            
            }
        }

        return "success";
    }

	    
	public String convertFileToExport1(InputStream inputStream, String companyid, String branchId, String user_Id)
			throws IOException, ParseException {
		InputStreamReader reader = new InputStreamReader(inputStream);
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
		System.out.println("csvParser " + csvParser);
		for (CSVRecord read : csvParser) {
			String sbreqid = read.get("sb_request_id");
			String sbNo = read.get("sb_number");
			String cid = read.get("company_id");
			String airlineName = read.get("airline_name");
			String airwayBillNo = read.get("airway_bill_no");
			String approvedBy = read.get("approved_by");
			String approvedDate = read.get("approved_date");
			String cancelRemark = read.get("cancel_remarks");
			String cancelStatus = read.get("cancel_status");
			String cartingAgent = read.get("carting_agent");
			String chaname = read.get("chaname");
			String chano = read.get("chano");
			String consoleAgent = read.get("console_agent");
			String countryOfDestination = read.get("country_of_destination");
			String createdBy = read.get("created_by");
			String ceatedDate = read.get("created_date");
			String descOfGoods = read.get("description_of_goods");
			String dgdcStatus = read.get("dgdc_status");
			String editedBy = read.get("edited_by");
			String editedDate = read.get("edited_date");
			String entityId = read.get("entity_id");
			String flightDte = read.get("flight_date");
			String flightNo = read.get("flight_no");
			String fobValue = read.get("fobvalue_ininr");
			String gateInDate = read.get("gate_in_date");
			String gateInId = read.get("gate_in-id");
			String grossWt = read.get("gross_weight");
			String holdStatus = read.get("hold_status");
			String hpStatus = read.get("hp_status");
			String hpWt = read.get("hp_weight");
			String hpPackageNo = read.get("hp_package_no");
			String iecCode = read.get("iec_code");
			String imposePenltyAmt = read.get("impose_penalty_amt");
			String imposePenaltyRemark = read.get("impose_penalty_remarks");
			String mawb = read.get("mawb");
			String exporterName = read.get("exporter_name");
			String noOfPackages = read.get("no_of_packages");
			String nsdlStatus = read.get("nsdl_status");
			String overrideDocument = read.get("override_document");
			String representativeId = read.get("representative_id");
			String pcStatus = read.get("pc_status");
			String pctmDate = read.get("pctm_date");
			String pctmNo = read.get("pctm_no");
			String portOfDestination = read.get("port_of_destination");
			String qrCodeUrl = read.get("qr_code_url");
			String reasonforOverride = read.get("reason_for_override");
			String sbDate = read.get("sb_date");
			String scStatus = read.get("sc_status");
			String serDate = read.get("ser_date");
			String serNo = read.get("ser_no");
			String status = read.get("status");
			String tpDate = read.get("tp_date");
			String tpNo = read.get("tp_no");
			String uomGrossWt = read.get("uomgross_weight");
			String uomOfPackages = read.get("uomof_packages");
			String airlineCode = read.get("airline_code");
			String gatepassVehNo = read.get("gate_pass_vehicle_no");
			String gatePassStatus = read.get("gate_pass_status");
			String poName = read.get("po_name");
			String cargoInScan = read.get("dgdc_cargo_in_scan");
			String cargoOutScan = read.get("dgdc_cargo_out_scan");
			String seepzInScan = read.get("dgdc_seepz_in_scan");
			String seepzOutScan = read.get("dgdc_seepz_out_scan");
			String noc = read.get("dgdc_seepz_out_scan");
			String outDate = read.get("out_date");
			String imagePath = read.get("image_path");
			String redepositeRemark = read.get("redeposite_remark");
			String backToTownImagePath = read.get("back_to_town_file_path");
			String backtotownRemark = read.get("back_to_town_remark");
			String mopStatus = read.get("mop_status");
			String epCopyDocument = read.get("ep_copy_document");
			
			
			Export export = exportrepo.finexportsbdata(companyid, branchId, sbNo);
			
			export.setSerNo(cancelStatus);
			exportrepo.save(export);

			List<Export_History> exphis = exporthistoryrepo.findBySb(companyid,branchId,sbNo);
			for(Export_History exphistory : exphis) {
				exphistory.setserNo(cancelStatus);
				exporthistoryrepo.save(exphistory);
			}
		}

		return "success";
	}

	public String convertFileToInvoiceImport(InputStream inputStream, String companyid, String branchId, String user_Id)
			throws IOException, ParseException {
		InputStreamReader reader = new InputStreamReader(inputStream);
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
		System.out.println(" **************** csvParser ************");
		
		
		double totalBillAmount = 0.0;
		double totalInvoiceAmount = 0.0;
		double totalTaxAmont = 0.0 ;
		int index = 0;
		
		for (CSVRecord csvRecord : csvParser) {			
			
//			System.out.println("Record   In ");
//			System.out.println(csvRecord);
//			index++;
//			System.out.println(index);
//			String branch_id = csvRecord.get("branch_id");
//			System.out.println(branch_id);
//			System.out.println("Party Name "+csvRecord.get(0));
			
			String billNo = csvRecord.get("Bill NO");
			
			PredictableInvoiceMain predictable = PredictableInvoiceMainRepositary.findByCompanyIdAndBranchIdAndPredictableinvoiceNO(companyid, branchId, billNo);
			
			String partyName = predictable.getPartyId();
			
//			System.out.println("Party Name "+partyName);
			Party party = partyrepo.getdatabyid(companyid, branchId, predictable.getPartyId());
			
			String partyId =  predictable.getPartyId();
			
			String gstNo = csvRecord.get("GST NO");
			Date date = convertStringToDateAnotherFormat(csvRecord.get("Transaction Date"));	
			
			Date invoiceDate = convertStringToDate2222(csvRecord.get("Bill Date"));
			
			
			String importNoOfPackagesStr = csvRecord.get("IMPORT PKGS");
			String exportNoOfPackagesStr = csvRecord.get("EXPORT PKGS");
			String totalPackagesStr = csvRecord.get("Total PKGS");
			String rateOfPkgsStr = csvRecord.get("Rate of PKGS");
//			String HolidayRate = csvRecord.get("IIND SAT");
//			String exportScRate = csvRecord.get("SPL CART EXPORT");
//			String exportHeavyRate = csvRecord.get("HW WT EXPORT");
//			String exportPcRate = csvRecord.get("PC EXPORT");
//			String exportPenaltyLocal = csvRecord.get("OC EXPORT");
//			String importScRate = csvRecord.get("SPL CART IMPORT");
//			String importHeavyRate = csvRecord.get("HW WT IMPORT");
//			String importPcRate = csvRecord.get("PC IMPORT");
//			String importPenaltyLocal = csvRecord.get("OC IMPORT");
			
			double holidayRate = (csvRecord.get("IIND SAT") != null && !csvRecord.get("IIND SAT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("IIND SAT"))
			        : 0.0;

			double exportScRate = (csvRecord.get("SPL CART EXPORT") != null && !csvRecord.get("SPL CART EXPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("SPL CART EXPORT"))
			        : 0.0;

			double exportHeavyRate = (csvRecord.get("HW WT EXPORT") != null && !csvRecord.get("HW WT EXPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("HW WT EXPORT"))
			        : 0.0;

			double exportPcRate = (csvRecord.get("PC EXPORT") != null && !csvRecord.get("PC EXPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("PC EXPORT"))
			        : 0.0;

			double exportPenaltyLocal = (csvRecord.get("OC EXPORT") != null && !csvRecord.get("OC EXPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("OC EXPORT"))
			        : 0.0;

			double importScRate = (csvRecord.get("SPL CART IMPORT") != null && !csvRecord.get("SPL CART IMPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("SPL CART IMPORT"))
			        : 0.0;

			double importHeavyRate = (csvRecord.get("HW WT IMPORT") != null && !csvRecord.get("HW WT IMPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("HW WT IMPORT"))
			        : 0.0;

			double importPcRate = (csvRecord.get("PC IMPORT") != null && !csvRecord.get("PC IMPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("PC IMPORT"))
			        : 0.0;

			double importPenaltyLocal = (csvRecord.get("OC IMPORT") != null && !csvRecord.get("OC IMPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("OC IMPORT"))
			        : 0.0;

			
//			String gst = csvRecord.get("GST");
			
//			String total = csvRecord.get("Total");			
//			String grandTotal = csvRecord.get("Grand Total");

			double total = (csvRecord.get("Total") != null && !csvRecord.get("Total").isEmpty())
			        ? Double.parseDouble(csvRecord.get("Total"))
			        : 0.0;

			double grandTotal = (csvRecord.get("Grand Total") != null && !csvRecord.get("Grand Total").isEmpty())
			        ? Double.parseDouble(csvRecord.get("Grand Total"))
			        : 0.0;
			
			
			
			BigDecimal exportHpWeight = BigDecimal.ZERO;
			BigDecimal importHpWeight = BigDecimal.ZERO;
			int importSubNoOfPackages = 0;
			int exportSubNoOfPackages = 0;
			double importSubRate = 0.0;
			double exportSubRate = 0.0;
			int demuragesNop = 0;
			double demuragesRate = 0.0;			
			int InnerniptPackages = 0;
//			int importScPackages = 0;
//			int importPcPackages = 0;
//			int importHwPackages = 0;
//			int TotalHolidayNop = 0;
//			int importOcPackages = 0;
			
			
			
//			int exportScPackages = 0;
//			int exportPcPackages = 0;
//			int exportHwPackages = 0;
//			int exportOcPackages = 0;
			
			
			
			int importScPackages = (importScRate > 0) ? 1 : 0;
//			int importScPackages = 0;
			int importPcPackages = (importPcRate > 0) ? 1 : 0;
			int importHwPackages = (importHeavyRate > 0) ? 1 : 0;
			int TotalHolidayNop = (holidayRate > 0) ? 1 : 0;
			int importOcPackages = (importPenaltyLocal > 0) ? 1 : 0;
			int exportScPackages = (exportScRate > 0) ? 1 : 0;
			int exportPcPackages = (exportPcRate > 0) ? 1 : 0;
			int exportHwPackages = (exportHeavyRate > 0) ? 1 : 0;
			int exportOcPackages = (exportPenaltyLocal > 0) ? 1 : 0;	
			
			
			
			
			
			
			
			
			
			int importNoOfPackages = (importNoOfPackagesStr != null && !importNoOfPackagesStr.isEmpty())
			        ? Integer.parseInt(importNoOfPackagesStr)
			        : 0;
			
			int exportNoOfPackages = (exportNoOfPackagesStr != null && !exportNoOfPackagesStr.isEmpty())
			        ? Integer.parseInt(exportNoOfPackagesStr)
			        : 0;
			
			int totalPackages = (totalPackagesStr != null && !totalPackagesStr.isEmpty())
			        ? Integer.parseInt(totalPackagesStr)
			        : 0;
			
			int rateOfPackages = (rateOfPkgsStr != null && !rateOfPkgsStr.isEmpty())
			        ? Integer.parseInt(rateOfPkgsStr)
			        : 0;		
			
			
			int importRate = importNoOfPackages * rateOfPackages;
			int exportRate = exportNoOfPackages * rateOfPackages;
			
			
			double totaltaxamount = 0.0 ;
			double totalbillamount = total;
			double totalamount = grandTotal;
			
			
			if ("Y".equals(party.getTaxApplicable())) {
				totaltaxamount = totalbillamount * (18 / 100.0);
			}		
			
			
//			System.out.println("Party Id: " + partyId);
//			System.out.println("Party Name: " + partyName);
//			System.out.println("GST NO: " + gstNo);
//			System.out.println("Transaction Date: " + date);
//			System.out.println("Bill Date: " + invoiceDate);
//			System.out.println("Bill NO: " + billNo);
//			System.out.println("IMPORT PKGS: " + importNoOfPackages);
//			System.out.println("EXPORT PKGS: " + exportNoOfPackages);
//			System.out.println("Total PKGS: " + totalPackages);
//			System.out.println("Rate of PKGS: " + rateOfPackages);
//			System.out.println("IIND SAT: " + holidayRate);
//			System.out.println("SPL CART EXPORT: " + exportScRate);
//			System.out.println("HW WT EXPORT: " + exportHeavyRate);
//			System.out.println("PC EXPORT: " + exportPcRate);
//			System.out.println("OC EXPORT: " + exportPenaltyLocal);
//			System.out.println("SPL CART IMPORT: " + importScRate);
//			System.out.println("HW WT IMPORT: " + importHeavyRate);
//			System.out.println("PC IMPORT: " + importPcRate);
//			System.out.println("OC IMPORT: " + importPenaltyLocal);
//			System.out.println("Total: " + total);
////			System.out.println("GST: " + gst);
//			System.out.println("Grand Total: " + grandTotal);

			
			
			System.out.println("**** In Inner Loop *****");
//			String PredictableInvoiceNumber = proccessNextIdService.autoIncrementPredictableInvoiceId();
//			String InvoiceNumber = proccessNextIdService.autoIncrementInvoiceNumber();
			
			PredictableInvoiceTaxDetails detail = new PredictableInvoiceTaxDetails(companyid, branchId,
					billNo, partyName, date, importNoOfPackages, exportNoOfPackages, totalPackages,
					Math.round(holidayRate), Math.round(exportScRate), Math.round(exportPcRate),
					Math.round(exportHeavyRate), exportHpWeight,
					Math.round(exportPenaltyLocal), Math.round(importScRate),
					Math.round(importPcRate), Math.round(importHeavyRate), importHpWeight,
					Math.round(importPenaltyLocal), totaltaxamount, totalbillamount,
					totalamount, "A", "SYSTEM", invoiceDate, importSubNoOfPackages, exportSubNoOfPackages,
					Math.round(importSubRate), Math.round(exportSubRate), demuragesNop, demuragesRate,
					InnerniptPackages , importRate , exportRate,TotalHolidayNop , importScPackages, importPcPackages , importHwPackages, importOcPackages,exportScPackages ,exportPcPackages ,exportHwPackages ,exportOcPackages);

			
			System.out.println("***********Predictable Invoice Tax Detail *************");
			System.out.println(detail);
			PredictableInvoiceTaxDetailsRepo.save(detail);
			
			InvoiceTaxDetails detail2 = new InvoiceTaxDetails(companyid, branchId, billNo, partyName, date,
					importNoOfPackages, exportNoOfPackages, totalPackages, Math.round(holidayRate),
					Math.round(exportScRate), Math.round(exportPcRate), Math.round(exportHeavyRate), exportHpWeight,
					Math.round(exportPenaltyLocal), Math.round(importScRate),
					Math.round(importPcRate), Math.round(importHeavyRate), importHpWeight,
					Math.round(importPenaltyLocal), totaltaxamount, totalbillamount, totalamount, "A",
					"SYSTEM", invoiceDate, importSubNoOfPackages, exportSubNoOfPackages, Math.round(importSubRate),
					Math.round(exportSubRate), demuragesNop, demuragesRate, InnerniptPackages);

			InvoiceTaxDetailsServiceIMPL.saveInvoiceTaxDetails(detail2);
			
			
			System.out.println("***********Invoice Tax Detail *************");
			System.out.println(detail2);
		}

		return "success";
	}
	
	
	
	
//	Main Class Invoice Calculation	
	public String convertFileToInvoiceMainImport(InputStream inputStream, String companyid, String branchId, String user_Id)
			throws IOException, ParseException {
		
//		System.out.println("In Method .........");
		
		InputStreamReader reader = new InputStreamReader(inputStream);
		
//		System.out.println("In Method 2222.........");
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

//		System.out.println("In Method 3333.........");
//		System.out.println("csvParser " + csvParser);
		
		

		    
		    
		   
		    
//		double totalBillAmount = 0.0;
//		double totalInvoiceAmount = 0.0;
//		double totalTaxAmont = 0.0 ;
		
		
		for (CSVRecord csvRecord : csvParser) {
//			String partyName = csvRecord.get("Party Name");
			
			
			 String invoiceNo = csvRecord.get("Invoice No");
			 
			 System.out.println("Invoice Number "+invoiceNo);
			 String partyId = csvRecord.get("Customer Name");
//			
			
//			 System.out.println("predictable "+predictable);
			 
			 Party party = partyrepo.getdatabyid(companyid, branchId, partyId);

		        Date invoiceDate = convertStringToDate333(csvRecord.get("Invoice Date"));

		        int importPkgs = Integer.parseInt(csvRecord.get("IMPORT PKGS"));
		        int exportPkgs = Integer.parseInt(csvRecord.get("EXPORT PKGS"));
			
						
		
//			Date date = parseDate5(csvRecord.get("Transaction Date"));	
//			Date invoiceDate = convertStringToDate(csvRecord.get("Bill Date"));
			String billNo = invoiceNo;
			String importNoOfPackagesStr = csvRecord.get("IMPORT PKGS");
			String exportNoOfPackagesStr = csvRecord.get("EXPORT PKGS");
			
//			String HolidayRate = csvRecord.get("IIND SAT");
//			String exportScRate = csvRecord.get("SPL CART EXPORT");
//			String exportHeavyRate = csvRecord.get("HW WT EXPORT");
//			String exportPcRate = csvRecord.get("PC EXPORT");
//			String exportPenaltyLocal = csvRecord.get("OC EXPORT");
//			String importScRate = csvRecord.get("SPL CART IMPORT");
//			String importHeavyRate = csvRecord.get("HW WT IMPORT");
//			String importPcRate = csvRecord.get("PC IMPORT");
//			String importPenaltyLocal = csvRecord.get("OC IMPORT");
			
			double holidayRate = (csvRecord.get("IIND SAT") != null && !csvRecord.get("IIND SAT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("IIND SAT"))
			        : 0.0;

			double exportScRate = (csvRecord.get("SPL CART EXPORT") != null && !csvRecord.get("SPL CART EXPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("SPL CART EXPORT"))
			        : 0.0;

			double exportHeavyRate = (csvRecord.get("HW WT EXPORT") != null && !csvRecord.get("HW WT EXPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("HW WT EXPORT"))
			        : 0.0;

			double exportPcRate = (csvRecord.get("PC EXPORT") != null && !csvRecord.get("PC EXPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("PC EXPORT"))
			        : 0.0;

			double exportPenaltyLocal = (csvRecord.get("OC EXPORT") != null && !csvRecord.get("OC EXPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("OC EXPORT"))
			        : 0.0;

			double importScRate = (csvRecord.get("SPL CART IMPORT") != null && !csvRecord.get("SPL CART IMPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("SPL CART IMPORT"))
			        : 0.0;

			double importHeavyRate = (csvRecord.get("HW WT IMPORT") != null && !csvRecord.get("HW WT IMPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("HW WT IMPORT"))
			        : 0.0;

			double importPcRate = (csvRecord.get("PC IMPORT") != null && !csvRecord.get("PC IMPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("PC IMPORT"))
			        : 0.0;

			double importPenaltyLocal = (csvRecord.get("OC IMPORT") != null && !csvRecord.get("OC IMPORT").isEmpty())
			        ? Double.parseDouble(csvRecord.get("OC IMPORT"))
			        : 0.0;


//			double total = (csvRecord.get("Total") != null && !csvRecord.get("Total").isEmpty())
//			        ? Double.parseDouble(csvRecord.get("Total"))
//			        : 0.0;

			double grandTotal = (csvRecord.get("TOTAL") != null && !csvRecord.get("TOTAL").isEmpty())
			        ? Double.parseDouble(csvRecord.get("TOTAL"))
			        : 0.0;

		
			
			int importNoOfPackages = (importNoOfPackagesStr != null && !importNoOfPackagesStr.isEmpty())
			        ? Integer.parseInt(importNoOfPackagesStr)
			        : 0;
			
			int exportNoOfPackages = (exportNoOfPackagesStr != null && !exportNoOfPackagesStr.isEmpty())
			        ? Integer.parseInt(exportNoOfPackagesStr)
			        : 0;
			
			int totalPackages = importNoOfPackages+exportNoOfPackages;
			
			int rateOfPackages = 450;
			
			
			
			
			double cgst = convertToDouble(csvRecord.get("CGST"));
			double sgst = convertToDouble(csvRecord.get("SGST"));
			double igst = convertToDouble(csvRecord.get("IGST"));
			
			
			
			int importRate = importNoOfPackages * rateOfPackages;
			int exportRate = exportNoOfPackages * rateOfPackages;
			
			
			double totaltaxamount = 2*(cgst+ sgst + igst) ;
			double totalbillamount = grandTotal - totaltaxamount;
			double totalamount = grandTotal;
			
			
			BigDecimal exportHpWeight = BigDecimal.ZERO;
			BigDecimal importHpWeight = BigDecimal.ZERO;
			int importSubNoOfPackages = 0;
			int exportSubNoOfPackages = 0;
			double importSubRate = 0.0;
			double exportSubRate = 0.0;
			int demuragesNop = 0;
			double demuragesRate = 0.0;			
			int InnerniptPackages = 0;
			
			int importScPackages = (importScRate > 0) ? 1 : 0;
//			int importScPackages = 0;
			int importPcPackages = (importPcRate > 0) ? 1 : 0;
			int importHwPackages = (importHeavyRate > 0) ? 1 : 0;
			int TotalHolidayNop = (holidayRate > 0) ? 1 : 0;
			int importOcPackages = (importPenaltyLocal > 0) ? 1 : 0;
			int exportScPackages = (exportScRate > 0) ? 1 : 0;
			int exportPcPackages = (exportPcRate > 0) ? 1 : 0;
			int exportHwPackages = (exportHeavyRate > 0) ? 1 : 0;
			int exportOcPackages = (exportPenaltyLocal > 0) ? 1 : 0;			
			
		
			Branch findByCompany_Id = branchRepo.findByCompanyIdAndBranchId(companyid, branchId);
			String CompanyGstNo = findByCompany_Id.getGST_No();
			String PartyGstNo = party.getGstNo();
			String companyStateCode = CompanyGstNo.substring(0, 2);
			String partyStateCode = PartyGstNo.substring(0, 2);
			
			
//			Predictable Packages
			
			PredictableInvoicePackages packages = new PredictableInvoicePackages();
			packages.setCompanyId(companyid);
			packages.setBranchId(branchId);
			packages.setPredictableinvoiceNO(invoiceNo);
			packages.setCalculatedDate(invoiceDate);
			packages.setPartyId(party.getPartyId());
			packages.setImportNop(importNoOfPackages);
			packages.setImportSubNop(0);
			packages.setExportNop(exportNoOfPackages);
			packages.setExportSubNop(0);
			packages.setHolidaySubNop(TotalHolidayNop);
			packages.setExportSplCartNop(exportScPackages);
			packages.setExportPcNop(exportPcPackages);
			packages.setExportHpNop(exportHwPackages);
			packages.setExportOcNop(exportOcPackages);
			packages.setImportSplCartNop(importScPackages);
			packages.setImportPcNop(importPcPackages);
			packages.setImportHpNop(importHwPackages);
			packages.setImportOcNop(importOcPackages);
			packages.setImportRate(importRate);
			packages.setImportSubRate(0);
			packages.setExportRate(exportRate);
			packages.setExportSubRate(0);
			packages.setHolidayRate(holidayRate);
			packages.setExportSplCartRate(exportScRate);
			packages.setExportPcRate(exportPcRate);
			packages.setExportHpRate(exportHeavyRate);
			packages.setExportOcRate(exportPenaltyLocal);
			packages.setImportSplCartRate(importScRate);
			packages.setImportPcRate(importPcRate);
			packages.setImportHpRate(importHeavyRate);
			packages.setImportOcRate(importPenaltyLocal);
			packages.setDemuragesNop(0);
			packages.setDemuragesRate(0);
			packages.setNiptPackages(0);

			packages.setTariffNo("CFST000001");
			packages.setTariffAmndNo("000");
			
			
			
			if ("Y".equals(party.getTaxApplicable())) {
//				taxamount = amounts[1] * (18 / 100.0);

				packages.setTaxAmount(totaltaxamount);
				if (companyStateCode.equals(partyStateCode)) {
					packages.setCgst("Y");
					packages.setSgst("Y");
					packages.setIgst("N");
				} else {
					packages.setCgst("N");
					packages.setSgst("N");
					packages.setIgst("Y");
				}

			} else {
				packages.setTaxAmount(totaltaxamount);
				packages.setIgst("N");
				packages.setCgst("N");
				packages.setSgst("N");
			}
			
			packages.setBillAmount(totalbillamount);
			packages.setTotalInvoiceAmount(totalamount);
			
			
//			predictable table ..
			PredictableInvoiceMain main = new PredictableInvoiceMain();
			main.setCompanyId(companyid);
			main.setBranchId(branchId);
			main.setInvoiceDate(invoiceDate);
			main.setApprovedBy("SYSTEM");
			main.setCreatedBy("SYSTEM");
			main.setCreatedDate(invoiceDate);
			main.setApprovedDate(invoiceDate);
			main.setPredictableinvoiceNO(invoiceNo);
			if ("Y".equals(party.getTaxApplicable())) {
//				taxamount = amounts[1] * (18 / 100.0);

				main.setTaxAmount(totaltaxamount);
				if (companyStateCode.equals(partyStateCode)) {
					main.setCgst("Y");
					main.setSgst("Y");
					main.setIgst("N");
				} else {
					main.setCgst("N");
					main.setSgst("N");
					main.setIgst("Y");
				}

			} else {
				main.setTaxAmount(totaltaxamount);
				main.setIgst("N");
				main.setCgst("N");
				main.setSgst("N");
			}
			
			Date firstDateOfMonth = getFirstDateOfMonth(invoiceDate);
			main.setTotalInvoiceAmount(totalamount);
			main.setPeriodFrom(firstDateOfMonth);
			main.setPeriodTo(invoiceDate);
			
			main.setPartyId(party.getPartyId());
			main.setTariffNo("CFST000001");
			main.setTariffAmndNo("000");
			main.setBillAmount(totalbillamount);
			
							
			PredictableInvoiceMainRepositary.save(main);
			
							
			predictableRepo.save(packages);

			
			
			
			
//			Invoice Main 
			InvoiceMain Invoice = new InvoiceMain();
			Invoice.setCompanyId(companyid);
			Invoice.setBranchId(branchId);
			;
			Invoice.setInvoiceNO(invoiceNo);
			Invoice.setInvoiceDate(invoiceDate);
			Invoice.setInvoiceDueDate(addDays(invoiceDate, 3));
			Invoice.setPartyId(party.getPartyId());
			Invoice.setTariffNo("CFST000001");
			Invoice.setTariffAmndNo("000");

			Invoice.setBillAmount(totalbillamount);
			Invoice.setBillNO(invoiceNo);
			Invoice.setPaymentStatus("P");
//			double taxamount = 0.0;

			if ("Y".equals(party.getTaxApplicable())) {
//				taxamount = amounts[1] * (18 / 100.0);

				Invoice.setTaxAmount(totaltaxamount);
				if (companyStateCode.equals(partyStateCode)) {
					Invoice.setCgst("Y");
					Invoice.setSgst("Y");
					Invoice.setIgst("N");
				} else {
					Invoice.setCgst("N");
					Invoice.setSgst("N");
					Invoice.setIgst("Y");
				}

			} else {
				Invoice.setTaxAmount(totaltaxamount);
				Invoice.setIgst("N");
				Invoice.setCgst("N");
				Invoice.setSgst("N");
			}

			Invoice.setTotalInvoiceAmount(totalamount);
			Invoice.setPeriodFrom(firstDateOfMonth);
			Invoice.setPeriodTo(invoiceDate);
			Invoice.setCreatedBy("SYSTEM");
			Invoice.setCreatedDate(invoiceDate);
			Invoice.setEditedBy("SYSTEM");
			Invoice.setEditedDate(invoiceDate);
			Invoice.setApprovedBy("SYSTEM");
			Invoice.setApprovedDate(invoiceDate);
			Invoice.setComments("Invoice Comment");
			Invoice.setStatus("A");
			Invoice.setMailFlag("N");
			Invoice.setReceiptTransactionId("REC101");
			Invoice.setReceiptTransactionDate(invoiceDate);

			
			
			invoiceServiceIMPL.addInvoice(Invoice);
			
			
			
//			InvoicePackages Numbers And Rates
			InvoicePackages packages2 = new InvoicePackages();		
			
			packages2.setCompanyId(companyid);
			packages2.setBranchId(branchId);
			packages2.setInvoiceNO(invoiceNo);
			packages2.setBillNO(invoiceNo);
			packages2.setInvoiceDate(invoiceDate);
			packages2.setPartyId(party.getPartyId());
			packages2.setImportNop(importNoOfPackages);
			packages2.setImportSubNop(0);
			packages2.setExportNop(exportNoOfPackages);
			packages2.setExportSubNop(0);
			packages2.setHolidaySubNop(TotalHolidayNop);
			packages2.setExportSplCartNop(exportScPackages);
			packages2.setExportPcNop(exportPcPackages);
			packages2.setExportHpNop(exportHwPackages);
			packages2.setExportOcNop(exportOcPackages);
			packages2.setImportSplCartNop(importScPackages);
			packages2.setImportPcNop(importPcPackages);
			packages2.setImportHpNop(importHwPackages);
			packages2.setImportOcNop(importOcPackages);
			packages2.setImportRate(importRate);
			packages2.setImportSubRate(0);
			packages2.setExportRate(exportRate);
			packages2.setExportSubRate(0);
			packages2.setHolidayRate(holidayRate);
			packages2.setExportSplCartRate(exportScRate);
			packages2.setExportPcRate(exportPcRate);
			packages2.setExportHpRate(exportHeavyRate);
			packages2.setExportOcRate(exportPenaltyLocal);
			packages2.setImportSplCartRate(importScRate);
			packages2.setImportPcRate(importPcRate);
			packages2.setImportHpRate(importHeavyRate);
			packages2.setImportOcRate(importPenaltyLocal);
			packages2.setDemuragesNop(0);
			packages2.setDemuragesRate(0);
			packages2.setNiptPackages(0);

		
			
			InvoicePackagesRepositary.save(packages2);
			
		
	
			
		}

		return "success";
	}
	
	
	
	
	
	
	 public static Date convertStringToDate(String dateString) {
	        try {
	                // If the first attempt fails, try parsing using the "M/d/yyyy" format
	                SimpleDateFormat altDateFormat = new SimpleDateFormat("M/d/yyyy");
	                return altDateFormat.parse(dateString);
	            } catch (ParseException e2) {
	                // If both attempts fail, print an error message and return null
	                System.err.println("Error: Unable to parse date for input: " + dateString);
	                return null;
	            }
	        
	 }
	 
	 
	 
	 public static Date convertStringToDate333(String dateString) {
	        Date date = null;

	        // Define date formats
	        String[] dateFormats = {"dd-MM-yyyy", "MM/dd/yyyy"};

	        for (String format : dateFormats) {
	            try {
	                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	                date = dateFormat.parse(dateString);

	                if (date != null) {
	                    return date; // Return if successful
	                }

	            } catch (ParseException e) {
	                // Handle parsing exceptions for the current format
	            }
	        }

	        // If all attempts fail, print an error message and return null
	        System.err.println("Error: Unable to parse date for input: " + dateString);
	        return null;
	    }

	 
	 
	 
	 
	 
	 public static Date convertStringToDate2222(String dateString) {
	        try {
	            // Use the "dd/MM/yyyy" format for parsing
	            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
	            return dateFormat.parse(dateString);
	        } catch (ParseException e) {
	            // If parsing fails, print an error message and return null
	            System.err.println("Error: Unable to parse date for input: " + dateString);
	            return null;
	        }
	    }
	
	 public static Date convertStringToDateAnotherFormat(String dateString) {
	        try {
	            // Specify the date format for the second format
	        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	            // Parse the date string into a java.util.Date object
	            return dateFormat.parse(dateString);
	        } catch (ParseException e) {
	            e.printStackTrace(); // Handle the ParseException appropriately
	            return null;
	        }
	    }

	 private double convertToDouble(String value) {
		    return (value != null && !value.isEmpty()) ? Double.parseDouble(value) : 0.0;
		}
	 
	 
	 private static Date getFirstDateOfMonth(Date date) {
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTime(date);
	     calendar.set(Calendar.DAY_OF_MONTH, 1);
	     return calendar.getTime();
	 }
	 
	 private static Date addDays(Date date, int days) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.DAY_OF_MONTH, days);
	        return calendar.getTime();
	    }
	 
////		Main Class Invoice Calculation	
//		public String convertFileToInvoiceMainImportInvoiceNumberUpdate(InputStream inputStream, String companyid, String branchId, String user_Id)
//				throws IOException, ParseException {
//			
////			System.out.println("In Method .........");
//			
//			InputStreamReader reader = new InputStreamReader(inputStream);
//			
////			System.out.println("In Method 2222.........");
//			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
//
//			
//			
//			for (CSVRecord csvRecord : csvParser) {
//				String BillNumber = csvRecord.get("New InvoiceNo");
//				String OldBillNumber = csvRecord.get("Old InvoiceNo");
//				
//				 String invoiceNo = " ";
//				if (BillNumber != null && !BillNumber.isEmpty()) {
//				     invoiceNo = "IN" + BillNumber.substring(2);
//				System.out.println("Invoice Number : "+invoiceNo);
//				System.out.println("Bill Number : "+BillNumber);
//				System.out.println("New Bill Number : "+OldBillNumber);
//				
//				 String updateQuery = "UPDATE cfinvsrv SET invoice_no = ?, bill_no = ? WHERE bill_no = ?";
//
//			        // Execute the update query with the provided parameters
//			        int rowsUpdated = jdbcTemplate.update(updateQuery, OldBillNumber, OldBillNumber, BillNumber);
//				
//			        
//			        String updateQuery2 = "UPDATE cfinvsrvdtl SET invoice_no = ? WHERE invoice_no = ?";
//
//			        // Execute the update query with the provided parameters
//			        int rowsUpdated2 = jdbcTemplate.update(updateQuery2, OldBillNumber, invoiceNo);
//			        
//			        String updateQuery3 = "UPDATE cfssrvanx SET invoice_no = ?WHERE invoice_no = ?";
//
//			        // Execute the update query with the provided parameters
//			        int rowsUpdated3 = jdbcTemplate.update(updateQuery3, OldBillNumber, invoiceNo);
//			        
//			        String updateQuery4 = "UPDATE invoice_packages SET invoice_no = ?, bill_no = ? WHERE bill_no = ?";
//
//			        // Execute the update query with the provided parameters
//			        int rowsUpdated4 = jdbcTemplate.update(updateQuery4, OldBillNumber, OldBillNumber, BillNumber);
//			        
//			        
//			        
//			        
//			        
//			        if (rowsUpdated > 0) {
//			            System.out.println("Update successful. Rows updated: " + rowsUpdated);
//			        } else {
//			            System.out.println("Update failed. No rows were updated.");
//			        }
//				
//				
//				
//				
//				System.out.println(" New InvoiceNo : "+ BillNumber + " Old InvoiceNo : "+OldBillNumber);
//				
//				} 
//				
//			}
//			
//			return  "Success";
//		}
//	 
	 
	 
//		Main Class Invoice Calculation	
		public String convertFileToInvoiceMainImportInvoiceNumberUpdate(InputStream inputStream, String companyid, String branchId, String user_Id)
				throws IOException, ParseException {
			
//			System.out.println("In Method .........");
			
			InputStreamReader reader = new InputStreamReader(inputStream);
			
//			System.out.println("In Method 2222.........");
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			int sr= 1;
			for (CSVRecord csvRecord : csvParser) {
				
				
				Stock stock = new Stock();
				
				
				    String combinedDate = csvRecord.get("Combined_Date");
				    
				    
				    stock.setCompanyId(companyid);
				    stock.setBranchId(branchId);
				    stock.setStockDate(convertStringToDate(combinedDate));
		            stock.setImportOut(Integer.parseInt(csvRecord.get("Import_out")));
		            stock.setImportIn(Integer.parseInt(csvRecord.get("Import_In")));
		            stock.setImportNiptOut(Integer.parseInt(csvRecord.get("NiptImport_out")));
		            stock.setImportNiptIn(Integer.parseInt(csvRecord.get("NiptImport_in")));
		            stock.setImportPcOut(Integer.parseInt(csvRecord.get("PcImport_out")));
		            stock.setImportPcIn(Integer.parseInt(csvRecord.get("PcImport_in")));
		            stock.setExportOut(Integer.parseInt(csvRecord.get("Export_Out")));
		            stock.setExportIn(Integer.parseInt(csvRecord.get("Export_In")));
		            stock.setExportPcOut(Integer.parseInt(csvRecord.get("PcExport_Out")));
		            stock.setExportPcIn(Integer.parseInt(csvRecord.get("PcExport_In")));
		            stock.setImportSubOut(Integer.parseInt(csvRecord.get("SubImport_Out")));
		            stock.setImportSubIn(Integer.parseInt(csvRecord.get("SubImport_In")));
		            stock.setExportSubOut(Integer.parseInt(csvRecord.get("SubExport_Out")));
		            stock.setExportSubIn(Integer.parseInt(csvRecord.get("SubExport_In")));
		            stock.setImportStock(0);
		            stock.setImportNiptStock(0);
		            stock.setImportPcStock(0);
		            stock.setExportPcStock(0);
		            stock.setExportStock(0);
		            stock.setImportSubStock(0);
		            stock.setExportSubStock(0);
		                  
		            
		
		            if (stock.getImportOut() > 0 || stock.getImportIn() > 0 || stock.getImportNiptOut() > 0 ||
		                    stock.getImportNiptIn() > 0 || stock.getImportPcIn() > 0 || stock.getImportPcIn() > 0 ||
		                    stock.getExportOut() > 0 || stock.getExportIn() > 0 || stock.getExportPcIn() > 0 ||
		                    stock.getExportPcIn() > 0 || stock.getImportSubOut() > 0 || stock.getImportSubIn() > 0 ||
		                    stock.getExportSubOut() > 0 || stock.getExportSubIn() > 0) {

		                    // Save the Stock object to the repository
		                    StockRepositary.save(stock);
		                }
				
				
				} 
				
			
			
			return  "Success";
		}
	 
	 
//		Main Class Invoice Calculation	
		public String convertFileToInvoiceMainImportInvoiceNumberUpdateDetention(InputStream inputStream, String companyid, String branchId, String user_Id)
				throws IOException, ParseException {
			
//			System.out.println("In Method .........");
			
			InputStreamReader reader = new InputStreamReader(inputStream);
			
//			System.out.println("In Method 2222.........");
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

			 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
			
			for (CSVRecord csvRecord : csvParser) {
				
				StockDetention stock = new StockDetention();
				
				    String combinedDate = csvRecord.get("Combined_Date");
				    
				    
				    stock.setCompanyId(companyid);
				    stock.setBranchId(branchId);
				    stock.setStockDate(dateFormat.parse(combinedDate));
		            stock.setImportDetentionIn(Integer.parseInt(csvRecord.get("ImportDetention_in")));
		            stock.setImportDetentionOut(Integer.parseInt(csvRecord.get("ImportDetention_out")));
		            stock.setExportDetentionIn(Integer.parseInt(csvRecord.get("ExportDetention_in")));
		            stock.setExportDetentionOut(Integer.parseInt(csvRecord.get("ExportDetention_out")));
		           		            
		            stock.setImportDetentionStock(0);
		            stock.setExportDetentionStock(0);
		            
		                  
		            
		
		            if (stock.getImportDetentionIn() > 0 || stock.getImportDetentionOut() > 0 || stock.getExportDetentionIn() > 0 ||
		                    stock.getExportDetentionOut() > 0 ) {

		                    // Save the Stock object to the repository
		            	StockDetentionRepositary.save(stock);
		                }
				
				
				} 
				
			
			
			return  "Success";
		}

		
//		Main Class Invoice Calculation	
		public String updaterateChart(InputStream inputStream)
				throws IOException, ParseException {
			
//			System.out.println("In Method .........");
			
			InputStreamReader reader = new InputStreamReader(inputStream);
			
//			System.out.println("In Method 2222.........");
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

			 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
			
			 List<CFSTariffRange> tarrifRange = new ArrayList<>();
			 int srl=0;
			for (CSVRecord csvRecord : csvParser) {
				
				srl= srl += 1;	
				CFSTariffRange range = new CFSTariffRange();
				range.setSrlNo(srl);
				range.setCompanyId("C00001");
				range.setBranchId("B00001");
				range.setCfsTariffNo("CFST000001");
				range.setServiceId("S00001");
				range.setCfsAmndNo("000");
				range.setCfsDocRefNo("DOC101");
				range.setPartyId("ALL");
				range.setCurrencyId("INR");
				range.setRangeFrom(Double.parseDouble(csvRecord.get("rangeFrom")));
				range.setRangeRate(Double.parseDouble(csvRecord.get("rate")));
				range.setRangeTo(Double.parseDouble(csvRecord.get("rangeTo")));
				range.setRate(0.0);
				range.setCreatedBy("Tuka");
				range.setCreatedDate(new Date());
				range.setEditedBy("Tuka");
				range.setEditedDate(new Date());
				range.setApprovedBy("Tuka");
				range.setApprovedDate(new Date());
				range.setTaxApplicable("N");
				range.setStatus("A");
				
				
				
				tarrifRange.add(range);
				} 
			CFSTariffRangeService.saveAllTariffRanges(tarrifRange);
			
			
			return  "Success";
		}

		
		
		
		
}