package com.jguru.assignment.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "hpl_patient_master")
public class HplPatientMaster {
	
	private Integer patientId;
	private String patientName;
	private Date dob;
	private String gender;
	private String address;
	private String telephoneNo;
	private String isActive;
	private Integer createdby;
	private Date createdDate;
	private Integer modifiedBy;
	private Date modifiedDate;
	
	/** Default Constructor */
	public HplPatientMaster() {
		
	}
	
	/** Full Constructor */
	public HplPatientMaster(Integer patientId, String patientName, Date dob, String gender, String address,
			String telephoneNo, String isActive, Integer createdby, Date createdDate, Integer modifiedBy,
			Date modifiedDate) {
		super();
		this.patientId = patientId;
		this.patientName = patientName;
		this.dob = dob;
		this.gender = gender;
		this.address = address;
		this.telephoneNo = telephoneNo;
		this.isActive = isActive;
		this.createdby = createdby;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
	}

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "patient_id", unique =  true, nullable =  false)
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	@Column(name = "patient_name", nullable = false, length = 400)
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	
	@Column(name = "dob", nullable = false)
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	@Column(name = "gender", nullable = false, length = 1)
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Column(name = "address", nullable = false, length = 65535)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "telephone_no", nullable = false, length = 45)
	public String getTelephoneNo() {
		return telephoneNo;
	}
	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}
	
	@Column(name = "is_active", nullable = false, length = 400)
	@JsonIgnore
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	@Column(name = "created_by", nullable = false)
	@JsonIgnore
	public Integer getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}
	
	@Column(name = "created_date", nullable = false)
	@JsonIgnore
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "modified_by", nullable = true)
	@JsonIgnore
	public Integer getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name = "modified_date", nullable = true)
	@JsonIgnore
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	
	

}
