package com.jguru.assignment.jpa.service;

import java.util.Date;
import java.util.List;

import com.jguru.assignment.jpa.model.HplPatientMaster;
import com.jguru.assignment.response.model.HplPatientMasterResponse;

public interface PresistenceService {

	HplPatientMaster saveUpdatePatient(Integer patientId, String patientName, Date dob, String gender, String address, String telephoneNo) throws Exception;

	void deletePatient(Integer patientId) throws Exception;

	List<HplPatientMaster> getPatients() throws Exception;

	HplPatientMasterResponse getPatientdetails(List<Object> sortProperties, List<Object> sortTypes,
			List<Object> operator, List<Object> value, List<Object> property, int page, int size) throws Exception;

}
