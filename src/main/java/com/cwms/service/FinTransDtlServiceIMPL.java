package com.cwms.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cwms.entities.FinTransDtl;
import com.cwms.repository.FinTransDtlRepositary;

@Service
public class FinTransDtlServiceIMPL 
{	
	@Autowired
	private FinTransDtlRepositary DtlRepositary;
	
	public FinTransDtl addFinTransDtl(FinTransDtl FinTransDtl)
	{
		return DtlRepositary.save(FinTransDtl);
	}
		
		
		
		
		
		
		

}
