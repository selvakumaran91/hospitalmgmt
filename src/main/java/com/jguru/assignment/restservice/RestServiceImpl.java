package com.jguru.assignment.restservice;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jguru.assignment.jpa.model.HplPatientMaster;
import com.jguru.assignment.jpa.service.PresistenceService;

@Service(value = "restService")
public class RestServiceImpl implements RestService {
	
	@Autowired
	private PresistenceService presistenceService;

	@Override
	public HplPatientMaster saveUpdatePatient(Integer patientId, String patientName, Date dob, String gender, String address,
			String telephoneNo) throws Exception {
			return presistenceService.saveUpdatePatient(patientId, patientName, dob, gender, address, telephoneNo);
	}

	@Override
	public void deletePatient(Integer patientId) throws Exception {
		presistenceService.deletePatient(patientId);
		
	}

	@Override
	public List<HplPatientMaster> getPatients() throws Exception {
		return presistenceService.getPatients();
	}

}
