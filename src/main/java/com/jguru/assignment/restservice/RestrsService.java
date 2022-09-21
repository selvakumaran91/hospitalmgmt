package com.jguru.assignment.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.jguru.assignment.rqeust.model.HplFilter;
import com.jguru.assignment.rqeust.model.HplFilterSort;
import com.jguru.assignment.rqeust.model.HplSort;



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
	public Response savePatient(@RequestBody HplPatientMaster patientMaster ) {
		Response result = null;
		try {
			Integer patientId = 0;
				String patientName = patientMaster.getPatientName();
				Date dob = patientMaster.getDob();
				String gender = patientMaster.getGender();
				String address = patientMaster.getAddress();
				String telephoneNo = patientMaster.getTelephoneNo();
				if(patientName == null || patientName.equals("") || dob == null || dob.equals("") ||
						gender == null || gender.equals("") || address == null || address.equals("") ||
						telephoneNo == null || telephoneNo.equals("")) {
					result = Response.status(Status.PRECONDITION_FAILED).build();
				}

				HplPatientMaster hplPatientMaster = restService.saveUpdatePatient(patientId, patientName, dob, gender, address, telephoneNo);
				result = Response.ok(hplPatientMaster).build();
		} catch(Exception e) {
			log.error("Error in save patient");
			e.printStackTrace();
			result = Response.status(Status.NOT_MODIFIED).build();
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
	public Response updatePatient(@PathVariable("patientId") Integer patientId,@RequestBody HplPatientMaster patientMaster ) {
		Response result = null;
		try {
				String patientName = patientMaster.getPatientName();
				Date dob = patientMaster.getDob();
				String gender = patientMaster.getGender();
				String address = patientMaster.getAddress();
				String telephoneNo = patientMaster.getTelephoneNo();
				if(patientId !=  0 || patientName == null || patientName.equals("") || dob == null || dob.equals("") ||
						gender == null || gender.equals("") || address == null || address.equals("") ||
						telephoneNo == null || telephoneNo.equals("")) {
					result = Response.status(Status.PRECONDITION_FAILED).build();
				}
				HplPatientMaster hplPatientMaster = restService.saveUpdatePatient(patientId, patientName, dob, gender, address, telephoneNo);
				result = Response.ok(hplPatientMaster).build();
		} catch(Exception e) {
			log.error("Error in update patient");
			e.printStackTrace();
			result = Response.status(Status.NOT_MODIFIED).build();
		}
		return result;
	}

	/**
	 * To delete a patient details
	 * @author Selvakumaran Subramanian
	 * @param patientId - patient table unique id
	 */
	@DeleteMapping("/patient/{patientId}")
	public Response deletePatient(@PathVariable("patientId") Integer patientId ) {
		Response result = null;
		try {
			if(patientId ==  0 ) {
				result = Response.status(Status.PRECONDITION_FAILED).build();
			}
			restService.deletePatient(patientId);
			result = Response.status(Status.OK).build();
		} catch(Exception e) { 
			log.error("Error in delete patient");
			e.printStackTrace();
			result = Response.status(Status.NOT_MODIFIED).build();
		}
		return result;
	}
	
	/**
	 * To Get all patient details
	 * @author Selvakumaran Subramanian
	 * @return List of patient detials
	 */
	@GetMapping("/patient")
	public Response getPatients() {
		Response result = null;
		try {
			List<HplPatientMaster> patientDetails = restService.getPatients();
			result = Response.ok(patientDetails).build();
		} catch(Exception e) { 
			log.error("Error in getting all patients detail ");
			e.printStackTrace();
			result = Response.status(Status.NOT_MODIFIED).build();
		}
		return result;
	}
	
	/**
	 * To Get all patient details with filters, sort and pagination
	 * @author Selvakumaran Subramanian
	 * @param filterSort - json sort/filter conditions
	 * @param page - current page number
	 * @param size - size of records to display in the current page
	 * @return List of patient detials
	 */
	@PostMapping("/patient/search")
	  public Response getPatientDetails(@RequestBody HplFilterSort filterSort,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "3") int size
	      ) {
		Response result = null;
		try {

			List<Object> sortProperties = new ArrayList<>();
			List<Object> sortTypes = new ArrayList<>();
			List<Object> operator = new ArrayList<>();
			List<Object> value = new ArrayList<>();
			List<Object> property = new ArrayList<>();
			if(filterSort != null) {
				List<HplSort> sorts = filterSort.getSorts();
				List<HplFilter> filters = filterSort.getFilters();
				if(sorts != null) {
					for(HplSort sort : sorts) {
						sortProperties.add(sort.getProperty());
						sortTypes.add(sort.getDir());
					}
				}
				if(filters != null) {
					for(HplFilter filter : filters) {
						operator.add(filter.getOperator());
						value.add(filter.getValue());
						property.add(filter.getProperty());
					}
				}
			}
			HplPatientMasterResponse patientDetails = restService.getPatientdetails(sortProperties, sortTypes, operator, value, property, page, size);
			result = Response.ok(patientDetails).build();
		} catch(Exception e) { 
			log.error("Error in getting all patients detail with filters and pagination ");
			e.printStackTrace();
			result = Response.status(Status.NOT_MODIFIED).build();
		}
		return result;
	  }

}
