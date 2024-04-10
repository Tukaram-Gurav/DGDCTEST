package com.cwms.entities;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="CommonGatePass")
@IdClass(CommonGatePassID.class)
public class CommonGatePass {

	@Id
	@Column(name="Common_Id",length=8)
	private String commonId;
	
	@Id
	private String companyId;

	@Id
	private String branchId;
	
	@Column(name="Name_Of_Party_Cha",length=150)
	private String nameOfPartyCha;
	
	@Column(name="Receiver_name",length = 150)
	private String receiverName;
	
	@Temporal(TemporalType.DATE)
	@Column(name="Delivery_Date")
	private Date deliveryDate;
	
	@Column(name="Delivery_Time",length = 10)
	private String deliveryTime;

	public CommonGatePass() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommonGatePass(String commonId, String nameOfPartyCha, String receiverName, Date deliveryDate,
			String deliveryTime) {
		super();
		this.commonId = commonId;
		this.nameOfPartyCha = nameOfPartyCha;
		this.receiverName = receiverName;
		this.deliveryDate = deliveryDate;
		this.deliveryTime = deliveryTime;
	}

	public String getCommonId() {
		return commonId;
	}

	public void setCommonId(String commonId) {
		this.commonId = commonId;
	}

	public String getNameOfPartyCha() {
		return nameOfPartyCha;
	}

	public void setNameOfPartyCha(String nameOfPartyCha) {
		this.nameOfPartyCha = nameOfPartyCha;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public CommonGatePass(String commonId, String companyId, String branchId, String nameOfPartyCha,
			String receiverName, Date deliveryDate, String deliveryTime) {
		super();
		this.commonId = commonId;
		this.companyId = companyId;
		this.branchId = branchId;
		this.nameOfPartyCha = nameOfPartyCha;
		this.receiverName = receiverName;
		this.deliveryDate = deliveryDate;
		this.deliveryTime = deliveryTime;
	}


	
	
}
