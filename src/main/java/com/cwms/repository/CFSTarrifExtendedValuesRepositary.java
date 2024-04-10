package com.cwms.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cwms.entities.CFSTarrifExtendedValues;

public interface CFSTarrifExtendedValuesRepositary extends JpaRepository<CFSTarrifExtendedValues, String>
{

	CFSTarrifExtendedValues findByCompanyIdAndBranchIdAndCfsTariffNoAndCfsAmndNoAndServiceIdAndPartyId(String CompanyId,String branchId,String tarrif,String amndNo,String serviceId,String partyId);;
	
}
