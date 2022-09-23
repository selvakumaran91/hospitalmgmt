package com.jguru.assignment.jpa.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.annotation.CreatedDate;

import com.jguru.assignment.jpa.dao.HplPatientMasterDao;
import com.jguru.assignment.jpa.model.HplPatientMaster;

@SpringBootTest
public class PresistenceServiceImplTest {


    @Autowired
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
       	doReturn(patient).when(repository).saveAndFlush(any());

    	// Execute the service call
    	HplPatientMaster returnedPatient = service.saveUpdatePatient(patient);

    	// Assert the response
    	Assertions.assertNotNull(returnedPatient, "The saved patient should not be null");
    	Assertions.assertNotSame(2, returnedPatient.getPatientId(), "The id should be incremented");
    }	
    
    @Test
    @DisplayName("Test update patient")
    void testUpdatePatient() throws Exception {
    	DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
    	Date dob = dobDateFormat.parse("1988-04-13");
    	DateFormat createdDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
    	Date currentDate = createdDateFormat.parse(createdDateFormat.format(new Date()));
    	Integer id = 1;
    	// Setup our mock repository
    	 HplPatientMaster patientToReturnFindBy = new HplPatientMaster(0,"Name", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
    	 HplPatientMaster patient = new HplPatientMaster(id,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);

     	doReturn(patientToReturnFindBy).when(repository).saveAndFlush(any());
    	doThrow(new RuntimeException()).when(repository).findByPatientIdAndIsActive(id, "Y");

    	// Execute the service call
    	System.out.println(patientToReturnFindBy.getPatientId());
    	HplPatientMaster returnedPatient = service.saveUpdatePatient(patient);

    	// Assert the response
		Assertions.assertNotNull(returnedPatient, "The saved patient should not be null");
		Assertions.assertSame(patient.getPatientId(), returnedPatient.getPatientId(), "This id should be same");
		Assertions.assertNotSame(patient.getPatientName(), returnedPatient.getPatientName(), "This id should not be same");

    }
    
	

//	
//	@Test
//	@DisplayName("Test for update patient")
//	void updatePatient() {
//		try {
//			DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
//			Date dob = dobDateFormat.parse("1988-04-13");
//			Integer id = 1;
//			// Setup our mock repository
//			HplPatientMaster patient = new HplPatientMaster(id,"Name", dob, "M", "Chennai", "+91 9876765423","Y",null,null,null,null);
//			//HplPatientMaster patientToReturnFindBy = service.findByPatientIdAndIsActive(1, "Y");
//			doReturn(ResponseEntity.ok(patient)).when(repository).saveUpdatePatient(id,"Name", dob, "M", "Chennai", "+91 9876765423")
//			
//			// Execute the service call
//			HplPatientMaster returnedPatient;
//			returnedPatient = service.saveUpdatePatient(id,"Name", dob, "M", "Chennai", "+91 9876765423");
//
//			// Assert the response
//			Assertions.assertNotNull(returnedPatient, "The saved patient should not be null");
//			Assertions.assertSame(patientToReturnFindBy.getPatientId(), returnedPatient.getPatientId(), "This id should be same");
//			Assertions.assertNotSame(patientToReturnFindBy.getPatientName(), returnedPatient.getPatientName(), "This id should not be same");
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//
//	}
}
