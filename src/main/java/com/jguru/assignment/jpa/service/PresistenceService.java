package com.jguru.assignment.jpa.service;

import java.util.List;

import com.jguru.assignment.jpa.model.HplPatientMaster;
import com.jguru.assignment.response.model.HplPatientMasterResponse;
import com.jguru.assignment.rqeust.model.HplFilterSort;

public interface PresistenceService {

	HplPatientMaster saveUpdatePatient(HplPatientMaster patientMaster) throws Exception;

	String deletePatient(Integer patientId) throws Exception;

	List<HplPatientMaster> getPatients() throws Exception;

	HplPatientMasterResponse getPatientdetails(HplFilterSort filterSort, int page, int size) throws Exception;

	public HplPatientMaster findByPatientIdAndIsActive(Integer patientId, String IsActive);

}
