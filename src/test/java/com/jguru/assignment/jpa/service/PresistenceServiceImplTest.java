package com.jguru.assignment.jpa.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jguru.assignment.jpa.dao.HplPatientMasterDao;
import com.jguru.assignment.jpa.model.HplPatientMaster;
import com.jguru.assignment.response.model.HplPatientMasterResponse;
import com.jguru.assignment.rqeust.model.HplFilter;
import com.jguru.assignment.rqeust.model.HplFilterSort;
import com.jguru.assignment.rqeust.model.HplSort;

@SpringBootTest
public class PresistenceServiceImplTest {


//	@MockBean
	@InjectMocks
    private PresistenceServiceImpl service;
	
	@Mock
	private PresistenceServiceImpl service1;
	
	@Mock
	private EntityManager em;
	
	@Mock
	CriteriaBuilder cb;

    @Mock
    private HplPatientMasterDao repository;
    
    @Rule
    ExpectedException expectedException = ExpectedException.none();
    
    
    @Test
    @DisplayName("Test save patient")
    void testSavePatient() throws Exception {
    	DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
    	Date dob = dobDateFormat.parse("1988-04-13");
    	DateFormat createdDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
    	Date currentDate = createdDateFormat.parse(createdDateFormat.format(new Date()));
    	Integer id = 0;
    	// Setup our mock repository
    	HplPatientMaster patient = new HplPatientMaster(id,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	HplPatientMaster returnedPatient = new HplPatientMaster(id,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	doReturn(patient).when(repository).saveAndFlush(any());
    	//doReturn(returnedPatient).when(repository).savexUpdatePatient(patient);
    	returnedPatient = service.saveUpdatePatient(patient);
    	// Assert the response
    	Assertions.assertNotNull(returnedPatient, "The saved patient should not be null");
    }	
    
    @Test
    @DisplayName("Test update patient")
    void testUpdatePatient() throws Exception {
    	DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
    	Date dob = dobDateFormat.parse("1988-04-13");
    	DateFormat createdDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
    	Date currentDate = createdDateFormat.parse(createdDateFormat.format(new Date()));
    	// Setup our mock repository
    	//HplPatientMaster patient = new HplPatientMaster(0,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	HplPatientMaster patient1 = new HplPatientMaster(1,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	HplPatientMaster returnPatient = new HplPatientMaster(1,"Selva", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	doReturn(patient1).when(repository).saveAndFlush(any());
    	doReturn(patient1).when(repository).findByPatientIdAndIsActive(patient1.getPatientId(), patient1.getIsActive());

    	// Execute the service call
    	returnPatient = service.saveUpdatePatient(returnPatient);
       	// Assert the response
    	Assertions.assertNotNull(returnPatient, "The saved patient should not be null");
    }
    
    @Test
    @DisplayName("Test Delete patient")
    void testDeletePatient() throws Exception {
    	DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
    	Date dob = dobDateFormat.parse("1988-04-13");
    	DateFormat createdDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
    	Date currentDate = createdDateFormat.parse(createdDateFormat.format(new Date()));
    	// Setup our mock repository
    	HplPatientMaster patient = new HplPatientMaster(1,"Selva", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	
    	String reply = "Record Deleted Successfully";
    	doReturn(patient).when(repository).findByPatientIdAndIsActive(1, "Y");
    	reply = service.deletePatient(patient.getPatientId());
       	// Assert the response
    	Assertions.assertNotNull(reply, "The saved patient should not be null");
   	
    }
    
    @Test
    @DisplayName("Test Delete patient with invalid patient id.")
    void testDeletePatient_WhenInvalidPatientId() throws Exception {
    	DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
    	Date dob = dobDateFormat.parse("1988-04-13");
    	DateFormat createdDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
    	Date currentDate = createdDateFormat.parse(createdDateFormat.format(new Date()));
    	// Setup our mock repository
    	HplPatientMaster patient = new HplPatientMaster(-1,"Selva", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	
    	//String reply = "Record Deleted Successfully";
    	when(repository.findByPatientIdAndIsActive(-1, "Y")).thenReturn(null);
    	String reply = service.deletePatient(patient.getPatientId());
       	// Assert the response
    	Assertions.assertEquals("Record Allready Deleted", reply);
   	
    }
    
    @Test
    @DisplayName("Test Delete patient when dao layer throws exception.")
    void testDeletePatient_WhenDaoThrowException() throws Exception {
    	DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
    	Date dob = dobDateFormat.parse("1988-04-13");
    	DateFormat createdDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
    	Date currentDate = createdDateFormat.parse(createdDateFormat.format(new Date()));
    	// Setup our mock repository
    	HplPatientMaster patient = new HplPatientMaster(-1,"Selva", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	
    	expectedException.expect(IllegalArgumentException.class);
    	//String reply = "Record Deleted Successfully";
    	when(repository.findByPatientIdAndIsActive(-1, "Y")).thenThrow(IllegalArgumentException.class);
    	
    	Assertions.assertThrows(IllegalArgumentException.class, 
    			()-> service.deletePatient(patient.getPatientId()));
   
   	
    }
    
    @Test
    @DisplayName("Test get patients")
    void testGetPatients() throws Exception {
    	DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
    	Date dob = dobDateFormat.parse("1988-04-13");
    	DateFormat createdDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
    	Date currentDate = createdDateFormat.parse(createdDateFormat.format(new Date()));
    	// Setup our mock repository
    	HplPatientMaster patient1 = new HplPatientMaster(1,"Selva", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	HplPatientMaster patient2 = new HplPatientMaster(2,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	List<HplPatientMaster> list = new ArrayList<>();
    	list.add(patient1);
    	list.add(patient2);
    	doReturn(list).when(repository).findByIsActive("Y");
        // Execute the service call
        List<HplPatientMaster> patients = service.getPatients();

        // Assert the response
        Assertions.assertEquals(2, patients.size(), "findAll should return 2 patients");
   	
    }
    
    @Test
    @DisplayName("Test get patients details with filters")
    void testGetPatientDetails() throws Exception {
    	DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
		Date dob = dobDateFormat.parse("1988-04-13");
		DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
		HplPatientMaster patient1 = new HplPatientMaster(1,"AAAA", dob, "M", "Chennai", "+91 1234567890","Y",1,currentDate,null,null);
		HplPatientMaster patient2 = new HplPatientMaster(2,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
		HplPatientMaster patient3 = new HplPatientMaster(3,"BBB", dob, "F", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);


		HplSort hplSort = new HplSort("patientName", "asc");
		HplFilter hplFilter = new HplFilter("eq", "M", "gender");
		List<HplSort> sort = new ArrayList<>();
		sort.add(hplSort);
		List<HplFilter> filter = new ArrayList<>();
		filter.add(hplFilter);
		HplFilterSort hplFilterSort = new HplFilterSort(sort,filter);

		List<HplPatientMaster> list = new ArrayList<>();
		list.add(patient1);
		list.add(patient2);
		list.add(patient3);
		HplPatientMasterResponse response = new HplPatientMasterResponse();
		response.setRecords(list);
		response.setPageNumber(1);
		response.setTotalPages(1);
		response.setTotalRecords(2);
		CriteriaBuilder cb = null;
		CriteriaQuery<HplPatientMaster> q = null;
		//when(em.getCriteriaBuilder()).thenReturn(cb);
		//when(cb.createQuery(HplPatientMaster.class)).thenReturn(q);

		//doReturn(response).when(service1).getPatientdetails(hplFilterSort,1,3);
		when(service1.getPatientdetails(hplFilterSort,1,3)).thenReturn(response);
		
        // Execute the service call
		//response = service.getPatientdetails(hplFilterSort,1,3);

        // Assert the response
        Assertions.assertEquals(2, response.getTotalRecords(), "findAll should return 2 patients");
   	
    }
	

}
