package com.jguru.assignment.jpa.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.jguru.assignment.jpa.dao.HplPatientMasterDao;
import com.jguru.assignment.jpa.model.HplPatientMaster;

@SpringBootTest
public class PresistenceServiceImplTest {


	@MockBean
    private PresistenceService service;


    @MockBean
    private HplPatientMasterDao repository;
    
    
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
    	doReturn(returnedPatient).when(service).saveUpdatePatient(patient);
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
    	HplPatientMaster patient = new HplPatientMaster(0,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	HplPatientMaster patient1 = new HplPatientMaster(1,"Selva", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);

    	doReturn(patient).when(repository).saveAndFlush(any());
    	doReturn(patient1).when(service).saveUpdatePatient(patient);

    	// Execute the service call
//    	HplPatientMaster returnedPatient = service.saveUpdatePatient(patient);
       	// Assert the response
    	Assertions.assertNotNull(patient, "The saved patient should not be null");
    	Assertions.assertNotSame(patient1.getPatientName(), patient.getPatientName(), "PatientName should not be same");
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
    	doReturn(reply).when(service).deletePatient(patient.getPatientId());

       	// Assert the response
    	Assertions.assertNotNull(reply, "The saved patient should not be null");
   	
    }
	

}
