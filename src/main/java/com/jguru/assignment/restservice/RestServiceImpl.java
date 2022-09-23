package com.jguru.assignment.restservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jguru.assignment.jpa.model.HplPatientMaster;
import com.jguru.assignment.jpa.service.PresistenceService;
import com.jguru.assignment.response.model.HplPatientMasterResponse;
import com.jguru.assignment.rqeust.model.HplFilterSort;

@Service(value = "restService")
public class RestServiceImpl implements RestService {
	
	@Autowired
	private PresistenceService presistenceService;

	@Override
	public HplPatientMaster saveUpdatePatient(HplPatientMaster patientMaster) throws Exception {
			return presistenceService.saveUpdatePatient(patientMaster);
	}

	@Override
	public String deletePatient(Integer patientId) throws Exception {
		return presistenceService.deletePatient(patientId);
		
	}

	@Override
	public List<HplPatientMaster> getPatients() throws Exception {
		return presistenceService.getPatients();
	}

	@Override
	public HplPatientMasterResponse getPatientdetails(HplFilterSort filterSort, int page, int size) throws Exception {
		return presistenceService.getPatientdetails(filterSort, page, size);
	}

	public HplPatientMaster findByPatientIdAndIsActive(Integer patientId, String IsActive) {
		return  presistenceService.findByPatientIdAndIsActive(patientId, IsActive);
	}
}
