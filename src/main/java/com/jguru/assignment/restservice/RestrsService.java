package com.jguru.assignment.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jguru.assignment.jpa.model.HplPatientMaster;
import com.jguru.assignment.response.model.HplPatientMasterResponse;
import com.jguru.assignment.rqeust.model.HplFilterSort;



@RestController
@RequestMapping("/v1")
public class RestrsService {
	
	@Autowired
	private RestService restService;

	static Logger log = Logger.getLogger(RestrsService.class);
	
	/**
	 * To create a patient details
	 * @author Selvakumaran Subramanian
	 * @param JosnObject of the details of the patient	
	 * @return Saved patient details 
	 */
	@PostMapping("/patient")
	public ResponseEntity<HplPatientMaster> savePatient(@RequestBody HplPatientMaster patientMaster ) {
		ResponseEntity<HplPatientMaster> result = null;
		try {
			Integer patientId = 0;
				String patientName = patientMaster.getPatientName();
				Date dob = patientMaster.getDob();
				String gender = patientMaster.getGender();
				String address = patientMaster.getAddress();
				String telephoneNo = patientMaster.getTelephoneNo();
				if(patientId !=  0 || patientName == null || patientName.equals("") || dob == null ||
						gender == null || gender.equals("") || address == null || address.equals("") ||
						telephoneNo == null || telephoneNo.equals("")) {
					return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
				}
				patientMaster.setPatientId(patientId);
				HplPatientMaster hplPatientMaster = restService.saveUpdatePatient(patientMaster);
				result =new ResponseEntity<>(hplPatientMaster, HttpStatus.CREATED);
		} catch(Exception e) {
			log.error("Error in save patient");
			e.printStackTrace();
			result = new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
		}
		return result;
	}

	/**
	 * To update a patient details
	 * @author Selvakumaran Subramanian
	 * @param patientId - patient table unique id
	 * @param JosnObject of the details of the patient	
	 * @return Updated patient details 
	 */
	@PutMapping("/patient/{patientId}")
	public ResponseEntity<HplPatientMaster> updatePatient(@PathVariable("patientId") Integer patientId,@RequestBody HplPatientMaster patientMaster ) {
		ResponseEntity<HplPatientMaster> result = null;
		try {
				String patientName = patientMaster.getPatientName();
				Date dob = patientMaster.getDob();
				String gender = patientMaster.getGender();
				String address = patientMaster.getAddress();
				String telephoneNo = patientMaster.getTelephoneNo();
				if(patientId ==  0 || patientName == null || patientName.equals("") || dob == null  ||
						gender == null || gender.equals("") || address == null || address.equals("") ||
						telephoneNo == null || telephoneNo.equals("")) {
					return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
				}
				patientMaster.setPatientId(patientId);
				HplPatientMaster hplPatientMaster = restService.saveUpdatePatient(patientMaster);
				if(hplPatientMaster != null) {
					result = new ResponseEntity<>(hplPatientMaster, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(hplPatientMaster, HttpStatus.NO_CONTENT);
				}
		} catch(Exception e) {
			log.error("Error in update patient");
			e.printStackTrace();
			result = new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
		}
		return result;
	}

	/**
	 * To delete a patient details
	 * @author Selvakumaran Subramanian
	 * @param patientId - patient table unique id
	 * @return 
	 */
	@DeleteMapping("/patient/{patientId}")
	public ResponseEntity<String> deletePatient(@PathVariable("patientId") Integer patientId ) {
		String response;
		try {
			if(patientId ==  0 ) {
				return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
			}
			response = restService.deletePatient(patientId);
		} catch(Exception e) { 
			log.error("Error in delete patient");
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
		}
		return  new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * To Get all patient details
	 * @author Selvakumaran Subramanian
	 * @return List of patient detials
	 */
	@GetMapping("/patient")
	public ResponseEntity<List<HplPatientMaster>> getPatients() {
		List<HplPatientMaster> patientDetails = new ArrayList<>();
		try {
			patientDetails = restService.getPatients();
			
		} catch(Exception e) { 
			log.error("Error in getting all patients detail ");
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
		}
		return new ResponseEntity<>(patientDetails, HttpStatus.OK);
	}
	
	/**
	 * To Get all patient details with filters, sort and pagination
	 * @author SS104707 Selvakumaran Subramanian
	 * @param filterSort - json sort/filter conditions
	 * @param page - current page number
	 * @param size - size of records to display in the current page
	 * @return List of patient detials
	 */
	@PostMapping("/patient/search")
	  public ResponseEntity<HplPatientMasterResponse> getPatientDetails(@RequestBody HplFilterSort filterSort,
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "3") int size
	      ) {
		ResponseEntity<HplPatientMasterResponse> result = null;
		try {
			HplPatientMasterResponse patientDetails = restService.getPatientdetails(filterSort, page, size);
			result = new ResponseEntity<>(patientDetails, HttpStatus.OK);
		} catch(Exception e) { 
			log.error("Error in getting all patients detail with filters and pagination ");
			e.printStackTrace();
			result = new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
		}
		return result;
	  }

}
