package com.cwms.entities;

import java.util.Date;

public class PCGatePass {

	private String id;
	private String officername;
	private Date gatepassdate;
	public PCGatePass() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public PCGatePass(String id, String officername, Date gatepassdate) {
		super();
		this.id = id;
		this.officername = officername;
		this.gatepassdate = gatepassdate;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOfficername() {
		return officername;
	}
	public void setOfficername(String officername) {
		this.officername = officername;
	}

	public Date getGatepassdate() {
		return gatepassdate;
	}

	public void setGatepassdate(Date gatepassdate) {
		this.gatepassdate = gatepassdate;
	}

	
	
	
	
}
