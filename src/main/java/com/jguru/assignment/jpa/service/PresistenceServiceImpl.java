package com.jguru.assignment.jpa.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jguru.assignment.jpa.dao.HplPatientMasterDao;
import com.jguru.assignment.jpa.model.HplPatientMaster;

@Repository(value = "presistenceService")
@Transactional
public class PresistenceServiceImpl implements PresistenceService {

	@Autowired
	private HplPatientMasterDao hplPatientMasterDao;
	
	@Override
	public HplPatientMaster saveUpdatePatient(Integer patientId, String patientName, Date dob, String gender, String address,
			String telephoneNo) throws Exception {
		HplPatientMaster hplPatientMaster = new HplPatientMaster();
		try {
			if(patientId != 0) {
				hplPatientMaster =hplPatientMasterDao.findByPatientId(patientId);
			}
			Date currentDate = getCurrentDate();
			//DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
			//dobDateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
			hplPatientMaster.setPatientName(patientName);
			hplPatientMaster.setDob(dob);
			hplPatientMaster.setGender(gender);
			hplPatientMaster.setAddress(address);
			hplPatientMaster.setTelephoneNo(telephoneNo);
			hplPatientMaster.setIsActive("Y");
			if(patientId == 0) {
				//TODO : The current user should be session user
				hplPatientMaster.setCreatedby(1);
				hplPatientMaster.setCreatedDate(currentDate);
			} else {
				//TODO : The current user should be session user
				hplPatientMaster.setModifiedBy(1);
				hplPatientMaster.setModifiedDate(currentDate);
			}
			hplPatientMaster = hplPatientMasterDao.saveAndFlush(hplPatientMaster);
		} catch(Exception e) {
			throw new Exception();
		}
		return hplPatientMaster;
	}

	/*
	 * To change the date format to the required format
	 */
	public Date getCurrentDate() throws ParserConfigurationException {
		Date currentDate = null;
		try {
			DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			currentDate = dateFormat.parse(dateFormat.format(new Date()));
		} catch(Exception e) {
			throw new ParserConfigurationException("Error Parsing Date");
		}
		return currentDate;
	}

	@Override
	public void deletePatient(Integer patientId) throws Exception {
		try {
			Date currentDate = getCurrentDate();
			//TODO : The current user should be session user
			Integer currentUser = 1;
			hplPatientMasterDao.deleteParentid(patientId, currentUser, currentDate);
		} catch(Exception e) {
			throw new Exception();
		}
	}

	@Override
	public List<HplPatientMaster> getPatients() throws Exception {
		List<HplPatientMaster> patientsDetail = new ArrayList<>();
		try {
			patientsDetail = hplPatientMasterDao.findByIsActive("Y");
			
		} catch(Exception e) {
			throw new Exception();
		}
		return patientsDetail;
	}
}
