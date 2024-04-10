package com.cwms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwms.entities.Stock;

public interface StockRepositary extends JpaRepository<Stock, String>
{

	List<Stock> findByCompanyIdAndBranchId(String companyId , String branchId);
	
	@Query(value = "SELECT "	
			+ "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.Tp_Date = CURRENT_DATE() AND e.nipt_status <> 'Y' AND e.pc_status <> 'Y' AND e.nipt_status <> 'Y') as ImportCargoReceived, "
	        + "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SIR_Date = CURRENT_DATE() AND e.nipt_status = 'Y' AND e.pc_status <> 'Y') as importNIPTReceived, "
	        + "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SIR_Date = CURRENT_DATE() AND e.nipt_status <> 'Y' AND e.pc_status = 'Y') as importPcReceived, "
	        + "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.SIR_Date = CURRENT_DATE()) as ImportSubIn, "
	        + "(SELECT COALESCE(SUM(e.no_of_packages), 0) FROM Export e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SER_Date = CURRENT_DATE() AND e.pc_status <> 'Y') as ExportIn, "
	        + "(SELECT COALESCE(SUM(e.no_of_packages), 0) FROM Export e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.SER_Date = CURRENT_DATE() AND e.pc_status = 'Y') as ExportPCIn, "
	        + "(SELECT COALESCE(SUM(es.nop), 0) FROM ExportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.SER_Date = CURRENT_DATE()) as ExportSubIn, "
	        + "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Exit from DGDC SEEPZ Gate' AND e.Out_Date = CURRENT_DATE() AND e.nipt_status <> 'Y' AND e.pc_status <> 'Y') as importOut, "
	        + "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.Out_Date = CURRENT_DATE() AND e.DGDC_Status = 'Exit from DGDC SEEPZ Gate' AND e.nipt_status = 'Y' AND e.pc_status <> 'Y') as importNiptOut, "
	        + "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.Out_Date = CURRENT_DATE() AND e.DGDC_Status = 'Exit from DGDC SEEPZ Gate' AND e.nipt_status <> 'Y' AND e.pc_status = 'Y') as importPcOut, "
	        + "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.DGDC_Status = 'Exit from DGDC SEEPZ Gate' AND es.Out_Date = CURRENT_DATE()) as importSubOut, " 
	        + "(SELECT COALESCE(SUM(e.no_of_packages), 0) FROM Export e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status In ('Handed over to Carting Agent','Exit from DGDC SEEPZ Gate','Handed Over to Airline','Entry at DGDC Cargo GATE','Handed over to DGDC Cargo') AND e.Out_Date = CURRENT_DATE() AND e.pc_status <> 'Y') as ExportOut, "
	        + "(SELECT COALESCE(SUM(e.no_of_packages), 0) FROM Export e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status In ('Handed over to Carting Agent','Exit from DGDC SEEPZ Gate') AND e.Out_Date = CURRENT_DATE() AND e.pc_status = 'Y') as ExportPCOut, "
	        + "(SELECT COALESCE(SUM(es.nop), 0) FROM ExportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.DGDC_Status = 'Exit from DGDC SEEPZ Gate' AND es.Out_Date = CURRENT_DATE()) as ExportSubOut, "     
	        + "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SIR_Date <= CURRENT_DATE() AND e.nipt_status <> 'Y' AND e.pc_status <> 'Y') as importStock, "
	        + "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SIR_Date <= CURRENT_DATE() AND e.nipt_status = 'Y') as importNIPTStock, "
	        + "(SELECT COALESCE(SUM(e.nop), 0) FROM Import e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SIR_Date <= CURRENT_DATE() AND e.nipt_status <> 'Y' AND e.pc_status = 'Y') as importPcStock, "	        
	        + "(SELECT COALESCE(SUM(es.nop), 0) FROM ImportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.DGDC_Status = 'Handed over to DGDC SEEPZ' AND es.SIR_Date <= CURRENT_DATE()) as importSubStock, "  
	        + "(SELECT COALESCE(SUM(e.no_of_packages), 0) FROM Export e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SER_Date <= CURRENT_DATE() AND e.pc_status <> 'Y') as ExportStock, "
	        + "(SELECT COALESCE(SUM(e.no_of_packages), 0) FROM Export e WHERE e.Company_Id = :companyId AND e.Branch_Id = :branchId AND e.DGDC_Status = 'Handed over to DGDC SEEPZ' AND e.SER_Date <= CURRENT_DATE() AND e.pc_status = 'Y') as ExportPCStock, "    
	        + "(SELECT COALESCE(SUM(es.nop), 0) FROM ExportSub es WHERE es.Company_Id = :companyId AND es.Branch_Id = :branchId AND es.DGDC_Status = 'Handed over to DGDC SEEPZ' AND es.SER_Date <= CURRENT_DATE()) as ExportSubStock ", nativeQuery = true)
	Object[] getStocksResults(@Param("companyId") String companyId, @Param("branchId") String branchId);

	
	
	
	
	
}
