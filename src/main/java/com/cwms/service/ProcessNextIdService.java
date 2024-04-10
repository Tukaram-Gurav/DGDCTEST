package com.cwms.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwms.entities.ProcessNextId;
import com.cwms.repository.ProcessNextIdRepository;

import jakarta.transaction.Transactional;

@Service

public class ProcessNextIdService {
	
	@Autowired(required = true)
	public ProcessNextIdRepository processNextIdRepository;

	public List<ProcessNextId> getAllByProcessId(String processId) {
		return processNextIdRepository.findByProcessId(processId);
	}

	public List<ProcessNextId> getAllByProcessId() {
		return processNextIdRepository.findAll();
	}

	public ProcessNextId saveProcessNextId(ProcessNextId processNextId) {
		return processNextIdRepository.save(processNextId);
	}
	@Transactional
	public synchronized String generateAndIncrementPCTMNumber() {
	    String nextPCTMNumber = processNextIdRepository.findNextPctmNo();
	    // Extract the numeric part
	    int slashIndex = nextPCTMNumber.indexOf("/");
	    if (slashIndex == -1 || slashIndex >= nextPCTMNumber.length() - 1) {
	        throw new IllegalArgumentException("Invalid TP number format: " + nextPCTMNumber);
	    }
	    String numericPart = nextPCTMNumber.substring(0, slashIndex); // Extracts the numeric part before the slash

	    // Convert the numeric part to an integer and increment by 1
	    int nextNumericPart = Integer.parseInt(numericPart) + 1;

	    // Format the incremented numeric part with zero-padding based on the original length
	    String formattedNumericPart = String.format("%0" + numericPart.length() + "d", nextNumericPart);

	    // Extract the fixed part after the slash
	    String fixedPart = nextPCTMNumber.substring(slashIndex + 1);

	    // Combine the formatted numeric part, slash, and the fixed part to create the new TP number
	    String newPCTMNumber = formattedNumericPart + "/" + fixedPart;

	    // Update the Next_PCTM_Number directly in the database using the repository
	    processNextIdRepository.updateNextPctmNo(newPCTMNumber);

	    return newPCTMNumber;
	}

	
	
	@Transactional
	public synchronized String generateAndIncrementTPumber() {
	    String nextTPumber = processNextIdRepository.findNexttpNo();

	    // Extract the numeric part
	    int slashIndex = nextTPumber.indexOf("/");
	    if (slashIndex == -1 || slashIndex >= nextTPumber.length() - 1) {
	        throw new IllegalArgumentException("Invalid TP number format: " + nextTPumber);
	    }
	    String numericPart = nextTPumber.substring(0, slashIndex); // Extracts the numeric part before the slash

	    // Convert the numeric part to an integer and increment by 1
	    int nextNumericPart = Integer.parseInt(numericPart) + 1;

	    // Format the incremented numeric part with zero-padding based on the original length
	    String formattedNumericPart = String.format("%0" + numericPart.length() + "d", nextNumericPart);

	    // Extract the fixed part after the slash
	    String fixedPart = nextTPumber.substring(slashIndex + 1);

	    // Combine the formatted numeric part, slash, and the fixed part to create the new TP number
	    String newTPNumber = formattedNumericPart + "/" + fixedPart;

	    // Update the Next_TP_Number directly in the database using the repository
	    processNextIdRepository.updateNexttpNo(newTPNumber);

	    return newTPNumber;
	}

	
	
	@Transactional
	public synchronized String autoIncrementDoNumber() {

		String doNumber = processNextIdRepository.findDoNumber();

		 // Extract the numeric part
	    int slashIndex = doNumber.indexOf("/");
	    if (slashIndex == -1 || slashIndex >= doNumber.length() - 1) {
	        throw new IllegalArgumentException("Invalid DO number format: " + doNumber);
	    }
	    String numericPart = doNumber.substring(0, slashIndex); // Extracts the numeric part before the slash

	    // Convert the numeric part to an integer and increment by 1
	    int nextNumericPart = Integer.parseInt(numericPart) + 1;

	    // Format the incremented numeric part with zero-padding based on the original length
	    String formattedNumericPart = String.format("%0" + numericPart.length() + "d", nextNumericPart);

	    // Extract the fixed part after the slash
	    String fixedPart = doNumber.substring(slashIndex + 1);

 	    String nextDoNumber = formattedNumericPart + "/" + fixedPart;

		// Update the Next_Id directly in the database using the repository
		processNextIdRepository.updateNextDoNumber(nextDoNumber);

		return doNumber;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Transactional
	public synchronized String autoIncrementNextIdNext() {
//		String NextId = processNextIdRepository.findNextId();
//
//		int lastNextNumericId = Integer.parseInt(NextId.substring(4));
//
//		int nextNumericNextID = lastNextNumericId + 1;
//
//		String NextIdD = String.format("H%05d", nextNumericNextID);
//		processNextIdRepository.updateNextId(NextIdD);
//		return NextIdD;
		
		
		String nextId = processNextIdRepository.findNextId();

        int lastNextNumericId = Integer.parseInt(nextId.substring(1));

        int nextNumericNextID = lastNextNumericId + 1;

        String nextIdD = String.format("M%05d", nextNumericNextID);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNextId(nextIdD);

        return nextIdD;

	}
	
	@Transactional
	public synchronized String autoIncrementNextIdHoliday() {
//		String NextId = processNextIdRepository.findNextId();
//
//		int lastNextNumericId = Integer.parseInt(NextId.substring(4));
//
//		int nextNumericNextID = lastNextNumericId + 1;
//
//		String NextIdD = String.format("H%05d", nextNumericNextID);
//		processNextIdRepository.updateNextId(NextIdD);
//		return NextIdD;
		
		
		String nextholi = processNextIdRepository.findNextIdforHoliday();

        int lastNextNumericIdh = Integer.parseInt(nextholi.substring(1));

        int nextNumericNextIDh = lastNextNumericIdh + 1;

        String nextIdDholi = String.format("H%05d", nextNumericNextIDh);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNextIdforHoliday(nextIdDholi);

        return nextIdDholi;

	}

	@Transactional
	 public synchronized String autoIncrementMailId() {
	  
	  String nextMailId = processNextIdRepository.findNextMailId();

	        int lastNextNumericId = Integer.parseInt(nextMailId.substring(4));

	        int nextNumericNextID = lastNextNumericId + 1;

	        String MailId = String.format("MAIL%06d", nextNumericNextID);
	        // Update the Next_Id directly in the database using the repository
	        processNextIdRepository.updateNextMailId(MailId);

	        return MailId;

	 }

	@Transactional
	public synchronized String autoIncrementNextJarIdNext() {

		
		String nextJarId = processNextIdRepository.findNextJarId();

        int lastNextNumericId = Integer.parseInt(nextJarId.substring(1));

        int nextNumericNextID = lastNextNumericId + 1;

        String nextJD = String.format("J%05d", nextNumericNextID);

        processNextIdRepository.updateNextJarId(nextJD);

        return nextJD;

	}


	
	@Transactional
	public synchronized String autoIncrementReprsentiveiD() {
		
		String nextReId = processNextIdRepository.findNextReId();

        int lastNextNumericId = Integer.parseInt(nextReId.substring(1));

        int nextNumericNextID = lastNextNumericId + 1;

        String ReId = String.format("R%05d", nextNumericNextID);
        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNextReId(ReId);

        return ReId;

	}
	
	
	
	@Transactional
	public synchronized String autoIncrementServiceNextId() {
		
		String serviceId = processNextIdRepository.findNextServiceId();

        int lastNextNumericId = Integer.parseInt(serviceId.substring(1));

        int nextNumericNextID = lastNextNumericId + 1;

        String serviceNextIdD = String.format("S%05d", nextNumericNextID);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNextServiceId(serviceNextIdD);

        return serviceNextIdD;

	}
	
	@Transactional
	public synchronized String autoIncrementCFSTarrifNextId() {
		
		String CFSTarrifNo = processNextIdRepository.findNextCFSTarrifNo();

        int lastNextNumericId = Integer.parseInt(CFSTarrifNo.substring(4));

        int nextNumericNextID = lastNextNumericId + 1;

        String CFSTTArrifNextIdD = String.format("CFST%06d", nextNumericNextID);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNextCFSTarrifNo(CFSTTArrifNextIdD);

        return CFSTTArrifNextIdD;

	}
	
	@Transactional
	public synchronized String autoIncrementSIRId() {
		
		String SIRNo = processNextIdRepository.findNextSIRNo();

        int lastNextNumericId = Integer.parseInt(SIRNo.substring(2));

        int nextNumericNextID = lastNextNumericId + 1;

        String NextSIRNo = String.format("IM%06d", nextNumericNextID);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNextSIRNo(NextSIRNo);

        return NextSIRNo;

	}
	
	
	@Transactional
	public synchronized String autoIncrementIMPTransId() {
		
		String IMPTransId = processNextIdRepository.findNextimpTransId();

        int lastNextNumericId = Integer.parseInt(IMPTransId.substring(4));

        int nextNumericNextID = lastNextNumericId + 1;

        String NextimpTransId = String.format("IMPT%04d", nextNumericNextID);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNextimpTransId(NextimpTransId);

        return NextimpTransId;

	}
	
	
	@Transactional
	public synchronized String autoIncrementSubImpId( ) {
		
		String IMPTransId = processNextIdRepository.findNextsubimpid();

        int lastNextNumericId = Integer.parseInt(IMPTransId.substring(4));

        int nextNumericNextID = lastNextNumericId + 1;

        String NextimpTransId = String.format("D-IM%06d", nextNumericNextID);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNexsubimpid(NextimpTransId);

        return NextimpTransId;

	}
	
	@Transactional
	public String autoIncrementSIRExportId() {
	    synchronized (this) {
	        String SIRNo = processNextIdRepository.findNextSIRExportNo();
	        int lastNextNumericId = Integer.parseInt(SIRNo.substring(2));
	        int nextNumericNextID = lastNextNumericId + 1;
	        String NextSIRNo = String.format("EX%06d", nextNumericNextID);
	        processNextIdRepository.updateNextSIRExportNo(NextSIRNo);
	        return NextSIRNo;
	    }
	}
	
	
	@Transactional
	public synchronized String autoIncrementSubImpTransId( ) {
		
		String IMPTransId = processNextIdRepository.findNextsubimptransid();

        int lastNextNumericId = Integer.parseInt(IMPTransId.substring(3));

        int nextNumericNextID = lastNextNumericId + 1;

        String NextimpTransId = String.format("SIM%05d", nextNumericNextID);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNexsubimptransid(NextimpTransId);

        return NextimpTransId;

	}
	
	@Transactional
	public synchronized String autoIncrementSubExpId( ) {
		
		String IMPTransId = processNextIdRepository.findNextsubexpid();

        int lastNextNumericId = Integer.parseInt(IMPTransId.substring(4));

        int nextNumericNextID = lastNextNumericId + 1;

        String NextimpTransId = String.format("D-EX%06d", nextNumericNextID);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNexsubexpid(NextimpTransId);

        return NextimpTransId;

	}
	
	@Transactional
	public synchronized String autoIncrementSubExpTransId( ) {
		
		String IMPTransId = processNextIdRepository.findNextsubexptransid();

        int lastNextNumericId = Integer.parseInt(IMPTransId.substring(3));

        int nextNumericNextID = lastNextNumericId + 1;

        String NextimpTransId = String.format("SER%05d", nextNumericNextID);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNexsubexptransid(NextimpTransId);

        return NextimpTransId;

	}
	
	
	
	
	
	
	private static final String COMMON_PREFIX = "000";
	private static final String FIXED_PART = "/23-24";
    private static final String SEPARATOR = "/";
    private static final String PREFIX = COMMON_PREFIX + SEPARATOR;
                                                          
	
	public int extractNumericPart(String pctmNumber) throws NumberFormatException {
	    // Use a regular expression to match the expected format "00000000/23-24"
	    Pattern pattern = Pattern.compile("(\\d{8}/\\d{2}-\\d{2})");
	    Matcher matcher = pattern.matcher(pctmNumber);

	    if (!matcher.matches()) {
	        throw new NumberFormatException("Invalid PCTM Number format: " + pctmNumber);
	    }

	    // Extract the numeric part
	    String numericPart = pctmNumber.substring(0, 8); // Assuming "00000000/23-24", this extracts "00000000"

	    return Integer.parseInt(numericPart);
	}
	
	
	
	
	
	
	

	private String formatNumericPart(int numericPart) {
	    return String.format("%08d", numericPart); // Assuming you want an 8-digit zero-padded numeric part
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Transactional
//	public synchronized String generateAndIncrementTPumber() {
//	    String nextTPumber = processNextIdRepository.findNexttpNo();
//
//	    // Extract the fixed part from the constant
//	    String fixedPart = FIXED_PART;
//
//	    // Extract and increment the numeric part
//	    int lastNumericPartTp = extractNumericPart(nextTPumber);
//	    int nextNumericPartTP = lastNumericPartTp + 1;
//
//	    // Format the numeric part with zero-padding
//	    String formattedNumericPart = formatNumericPart(nextNumericPartTP);
//
//	    // Combine the formatted numeric part, slash, and the rest of the string to create the new PCTM number
//	    String newTPNumber = formattedNumericPart + "/" + nextTPumber.substring(9);
//
//	    // Update the Next_PCTM_Number directly in the database using the repository
//	    processNextIdRepository.updateNexttpNo(newTPNumber);
//
//	    return newTPNumber;
//	}

	public int extractNumericPartOfTp(String tpNumber) throws NumberFormatException {
	    // Use a regular expression to match the expected format "00000000/23-24"
	    Pattern pattern = Pattern.compile("(\\d{8}/\\d{2}-\\d{2})");
	    Matcher matcher = pattern.matcher(tpNumber);

	    if (!matcher.matches()) {
	        throw new NumberFormatException("Invalid PCTM Number format: " + tpNumber);
	    }

	    // Extract the numeric part
	    String numericPartTP = tpNumber.substring(0, 8); // Assuming "00000000/23-24", this extracts "00000000"

	    return Integer.parseInt(numericPartTP);
	}

	private String formatNumericPartTP(int numericPartTP) {
	    return String.format("%08d", numericPartTP); // Assuming you want an 8-digit zero-padded numeric part
	}
	
	
	
	@Transactional
	public synchronized String autoIncrementExternalUserId() {
		
		String SIRNo = processNextIdRepository.findNextexternalUserId();

        int lastNextNumericId = Integer.parseInt(SIRNo.substring(2));

        int nextNumericNextID = lastNextNumericId + 1;

        String NextSIRNo = String.format("EU%04d", nextNumericNextID);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNextexternalUserId(NextSIRNo);

        return NextSIRNo;

	}
	
	@Transactional
	public String autoIncrementDetentionId( ) {
		
		String detentionId = processNextIdRepository.findDetentionId();

        int lastNextNumericId = Integer.parseInt(detentionId.substring(1));

        int nextNumericDeten = lastNextNumericId + 1;

        String NextidetentionId = String.format("D%09d", nextNumericDeten);

        // Update the Next_Id directly in the database using the repository
        processNextIdRepository.updateNextDetentionId(NextidetentionId);

        return NextidetentionId;

	}
	
	
	
	 public String getNextPctmNo() {
	        // Get the last value from the database
	        String lastValue = processNextIdRepository.findNextEXPPctmNo();

	        // Get the current date
	        LocalDate currentDate = LocalDate.now();

	        // Determine the year part based on the current date
	        String yearPart = determineYearPart(currentDate);

	        // Increment the last value
	        String nextValue = incrementPctmNo(lastValue, yearPart);

	        // Update the database with the new value
	        processNextIdRepository.updateNextEXPPctmNo(nextValue);

	        return nextValue;
	    }

	 private String determineYearPart(LocalDate currentDate) {
		    LocalDate april1 = LocalDate.of(currentDate.getYear(), Month.APRIL, 1);

		    if (currentDate.isAfter(april1) || currentDate.isEqual(april1)) {
		        // If the current date is on or after April 1, use the current year and the next year
		        return (currentDate.getYear() % 100) + "-" + ((currentDate.getYear() % 100) + 1);
		    } else {
		        // If the current date is before April 1, use the previous year and the current year
		        return ((currentDate.getYear() - 1) % 100) + "-" + (currentDate.getYear() % 100);
		    }
		}


	    private String incrementPctmNo(String lastValue, String yearPart) {
	        if (lastValue == null || lastValue.isEmpty()) {
	            // Handle the case where the last value is not found or empty
	            // You can start with the initial value, e.g., "000000/23-24"
	            return "000000/" + yearPart;
	        }

	        // Split the last value into parts
	        String[] parts = lastValue.split("/");

	        if (parts.length != 2) {
	            // Handle the case where the last value is not in the expected format
	            // You can throw an exception or handle it as needed
	            throw new IllegalArgumentException("Invalid last value format: " + lastValue);
	        }

	        int numericPart = Integer.parseInt(parts[0]);

	        // Increment the numeric part
	        numericPart++;

	        // Format the incremented value
	        String incrementedValue = String.format("%d/%s", numericPart, yearPart);

	        return incrementedValue;
	    }
	    
		
		@Transactional
		public synchronized String autoIncrementGateInId( ) {
			
			String detentionId = processNextIdRepository.findGateInId();

	        int lastNextNumericId = Integer.parseInt(detentionId.substring(2));

	        int nextNumericDeten = lastNextNumericId + 1;

	        String NextidetentionId = String.format("GI%04d", nextNumericDeten);

	        // Update the Next_Id directly in the database using the repository
	        processNextIdRepository.updateGateInId(NextidetentionId);

	        return NextidetentionId;

		}
	
	    
	    public String getNextTPNo() {
	        // Get the last value from the database
	        String lastValue = processNextIdRepository.findNextEXPtpNo();

	        // Get the current date
	        LocalDate currentDate = LocalDate.now();

	        // Determine the year part based on the current date
	        String yearPart = determineYearPart(currentDate);

	        // Increment the last value
	        String nextValue = incrementTPNo(lastValue, yearPart);

	        // Update the database with the new value
	        processNextIdRepository.updateNextEXPtpNo(nextValue);

	        return nextValue;
	    }



	    private String incrementTPNo(String lastValue, String yearPart) {
	        if (lastValue == null || lastValue.isEmpty()) {
	            // Handle the case where the last value is not found or empty
	            // You can start with the initial value, e.g., "000000/23-24"
	            return "000000/" + yearPart;
	        }

	        // Split the last value into parts
	        String[] parts = lastValue.split("/");

	        if (parts.length != 2) {
	            // Handle the case where the last value is not in the expected format
	            // You can throw an exception or handle it as needed
	            throw new IllegalArgumentException("Invalid last value format: " + lastValue);
	        }

	        int numericPart = Integer.parseInt(parts[0]);

	        // Increment the numeric part
	        numericPart++;

	        // Format the incremented value
	        String incrementedValue = String.format("%d/%s", numericPart, yearPart);

	        return incrementedValue;
	    }
	    
	    @Transactional
		public synchronized String autoIncrementInvoiceNumber() {
			
//			String SIRNo = processNextIdRepository.findNextInvoiceNumber();
//
//	        int lastNextNumericId = Integer.parseInt(SIRNo.substring(2));
//
//	        int nextNumericNextID = lastNextNumericId + 1;

//	        String NextInvoiceNumber = String.format("IN%04d", nextNumericNextID);
	        
	        
	        String invoiceNo = processNextIdRepository.findNextInvoiceNumber();

			int lastNextNumericId = Integer.parseInt(invoiceNo);
			lastNextNumericId = lastNextNumericId+1;
			String nextinvoiceNo = String.valueOf(lastNextNumericId);

			// Update the Next_Id directly in the database using the repository
			processNextIdRepository.updateNextextInvoiceNumber(nextinvoiceNo);

			return nextinvoiceNo;

	        // Update the Next_Id directly in the database using the repository
//	        processNextIdRepository.updateNextextInvoiceNumber(nextNumericNextID);

//	        return nextDoNumber;

		}
		
//		 Bill Number
		
		@Transactional
		public synchronized String autoIncrementBillNumber() {
			
			String SIRNo = processNextIdRepository.findNextBillNumber();

	        int lastNextNumericId = Integer.parseInt(SIRNo.substring(2));

	        int nextNumericNextID = lastNextNumericId + 1;

	        String NextBillNumber = String.format("BL%04d", nextNumericNextID);

	        // Update the Next_Id directly in the database using the repository
	        processNextIdRepository.updateNextextBillNumber(NextBillNumber);

	        return NextBillNumber;

		}
		
		
		@Transactional
		public synchronized String autoIncrementRepresentativePartyId() {

			
			String nextRepresentativePartyId = processNextIdRepository.findNextRepresentativePartyId();

	        int lastNextNumericId = Integer.parseInt(nextRepresentativePartyId.substring(2));

	        int nextNumericNextID = lastNextNumericId + 1;

	        String nextID = String.format("RI%04d", nextNumericNextID);

	        processNextIdRepository.updateNextRepresentativePartyId(nextID);

	        return nextID;

		}	
		
		@Transactional
		public String autoIncrementReceiptNumber() {
			
			String receiptNumber = processNextIdRepository.findNextReceiptNumber();

	       int lastNextNumericId = Integer.parseInt(receiptNumber.substring(2));

	       int nextNumericNextID = lastNextNumericId + 1;

	       String NextReceiptNumber = String.format("RE%04d", nextNumericNextID);

	       // Update the Next_Id directly in the database using the repository
	       processNextIdRepository.updateNextextReceiptNumber(NextReceiptNumber);

	       return NextReceiptNumber;

		}
		
		
		@Transactional
		public String autoIncrementProformaNo() {

			
			String nextRepresentativePartyId = processNextIdRepository.findNextProformaId();

	        int lastNextNumericId = Integer.parseInt(nextRepresentativePartyId.substring(2));

	        int nextNumericNextID = lastNextNumericId + 1;

	        String nextID = String.format("PR%04d", nextNumericNextID);

	        processNextIdRepository.updateNextProformaId(nextID);

	        return nextID;

		}
		
		//Personal Gate Pass
		
		
				@Transactional
				 public synchronized String autoIncrementGatePassId() {
				  
				  String nextMailId = processNextIdRepository.findNextPCGatePassId();

				        int lastNextNumericId = Integer.parseInt(nextMailId.substring(2));

				        int nextNumericNextID = lastNextNumericId + 1;

				        String MailId = String.format("GP%06d", nextNumericNextID);
				        // Update the Next_Id directly in the database using the repository
				        processNextIdRepository.updateNextPCGatePassId(MailId);

				        return MailId;

				 }
				
				@Transactional
				 public synchronized String autoIncrementMOPGatePassId() {
				  
				  String nextMailId = processNextIdRepository.findNextMOPGatePassId();

				        int lastNextNumericId = Integer.parseInt(nextMailId.substring(3));

				        int nextNumericNextID = lastNextNumericId + 1;

				        String MailId = String.format("MOP%05d", nextNumericNextID);
				        // Update the Next_Id directly in the database using the repository
				        processNextIdRepository.updateNextMOPGatePassId(MailId);

				        return MailId;

				 }
				
				
				@Transactional
				 public synchronized String autoIncrementCommonGatePassId() {
				  
				  String nextMailId = processNextIdRepository.findNextCommonGatePassId();

				        int lastNextNumericId = Integer.parseInt(nextMailId.substring(3));

				        int nextNumericNextID = lastNextNumericId + 1;

				        String MailId = String.format("COM%05d", nextNumericNextID);
				        // Update the Next_Id directly in the database using the repository
				        processNextIdRepository.updateNextCommonGatePassId(MailId);

				        return MailId;

				 }
				
				@Transactional
				 public synchronized String autoIncrementCombineRepresentativeId() {
				  
				  String nextMailId = processNextIdRepository.findNextCombineReprentativeId();

				        int lastNextNumericId = Integer.parseInt(nextMailId.substring(4));

				        int nextNumericNextID = lastNextNumericId + 1;

				        String MailId = String.format("CR%04d", nextNumericNextID);
				        // Update the Next_Id directly in the database using the repository
				        processNextIdRepository.updateNextCombineReprentativeId(MailId);

				        return MailId;

				 }
		
				@Transactional
				 public synchronized String autoIncrementPredictableInvoiceId() {
				  
				  String nextMailId = processNextIdRepository.findNextPredictableInviceId();

				        int lastNextNumericId = Integer.parseInt(nextMailId.substring(4));

				        int nextNumericNextID = lastNextNumericId + 1;

				        String MailId = String.format("PRIN%06d", nextNumericNextID);
				        // Update the Next_Id directly in the database using the repository
				        processNextIdRepository.updateNextPredictableInviceId(MailId);

				        return MailId;

				 }
				
				
				
}
