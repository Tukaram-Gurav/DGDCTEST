package com.cwms.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="MopPass")
public class MOPpass {
	@Id
	@Column(name="MOP_Id",length=8)
    private String mopId;
	
	
	@Column(name="From_Dgdc",length=80)
	private String fromDgdc;
	
	@Column(name="To_dgdc",length=80)
	private String toDgdc;
	
	@Column(name="Receiver_Name",length=80)
	private String reciverName;
	
	@Column(name="Name_Of_CHA",length=50)
	private String nameOfCHA;
	
	@Temporal(TemporalType.DATE)
	@Column(name="Created_Date")
	private Date createdDate;
	
	@Column(name="time",length=6)
	private String time;

	public MOPpass() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MOPpass(String mopId, String fromDgdc, String toDgdc, String reciverName, String nameOfCHA, Date createdDate,
			String time) {
		super();
		this.mopId = mopId;
		this.fromDgdc = fromDgdc;
		this.toDgdc = toDgdc;
		this.reciverName = reciverName;
		this.nameOfCHA = nameOfCHA;
		this.createdDate = createdDate;
		this.time = time;
	}

	public String getMopId() {
		return mopId;
	}

	public void setMopId(String mopId) {
		this.mopId = mopId;
	}

	public String getFromDgdc() {
		return fromDgdc;
	}

	public void setFromDgdc(String fromDgdc) {
		this.fromDgdc = fromDgdc;
	}

	public String getToDgdc() {
		return toDgdc;
	}

	public void setToDgdc(String toDgdc) {
		this.toDgdc = toDgdc;
	}

	public String getReciverName() {
		return reciverName;
	}

	public void setReciverName(String reciverName) {
		this.reciverName = reciverName;
	}

	public String getNameOfCHA() {
		return nameOfCHA;
	}

	public void setNameOfCHA(String nameOfCHA) {
		this.nameOfCHA = nameOfCHA;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
	
	
	
}
