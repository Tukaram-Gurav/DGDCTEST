package com.cwms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.cwms.entities.StockDetention;

public interface StockDetentionRepositary extends JpaRepository<StockDetention, String>
{

List<StockDetention> findByCompanyIdAndBranchId(String companyId , String branchId);
	
@Query(value = "SELECT "
		+ "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND d.Status = 'Deposited' AND d.Parcel_Type = 'Import' AND d.Deposit_Date = CURRENT_DATE()) as importDetentionDeposite, "
		+ "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND d.Status = 'Deposited' AND d.Parcel_Type = 'Export' AND d.Deposit_Date = CURRENT_DATE()) as exportDetentionDeposite, "		
		+ "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND ((d.Status = 'Withdraw' AND d.Parcel_Type = 'Import' AND d.Withdraw_Date = CURRENT_DATE()) OR (d.Status = 'Issued' AND d.Issue_Date = CURRENT_DATE()))) as importDetentionWithdrawn, "
		+ "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND ((d.Status = 'Withdraw' AND d.Parcel_Type = 'Export' AND d.Withdraw_Date = CURRENT_DATE()) OR (d.Status = 'Issued' AND d.Issue_Date = CURRENT_DATE()))) as exportDetentionWithdrawn,  "
		+ "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND d.Status = 'Deposited' AND d.Parcel_Type = 'Import' AND d.Deposit_Date <= CURRENT_DATE()) as importDetentionStock, "
		+ "(SELECT COALESCE(SUM(d.nop), 0) FROM Detention d WHERE d.Company_Id = :companyId AND d.Branch_Id = :branchId AND d.Status = 'Deposited' AND d.Parcel_Type = 'Export' AND d.Deposit_Date <= CURRENT_DATE()) as exportDetentionStock ", nativeQuery = true)
Object[] getCombinedResults(@Param("companyId") String companyId, @Param("branchId") String branchId);

}
