package com.jguru.assignment.jpa.service;

import java.util.Date;
import java.util.List;

import com.jguru.assignment.jpa.model.HplPatientMaster;

public interface PresistenceService {

	HplPatientMaster saveUpdatePatient(Integer patientId, String patientName, Date dob, String gender, String address, String telephoneNo) throws Exception;

	void deletePatient(Integer patientId) throws Exception;

	List<HplPatientMaster> getPatients() throws Exception;

}
