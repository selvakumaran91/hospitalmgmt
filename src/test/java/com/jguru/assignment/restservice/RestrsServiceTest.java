package com.jguru.assignment.restservice;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jguru.assignment.jpa.model.HplPatientMaster;
import com.jguru.assignment.jpa.service.PresistenceServiceImpl;
import com.jguru.assignment.response.model.HplPatientMasterResponse;
import com.jguru.assignment.rqeust.model.HplFilter;
import com.jguru.assignment.rqeust.model.HplFilterSort;
import com.jguru.assignment.rqeust.model.HplSort;


@SpringBootTest
@AutoConfigureMockMvc
class RestrsServiceTest {

	@MockBean
	RestService service;
	
	@Autowired
	PresistenceServiceImpl repo;
	
	@Autowired
    private MockMvc mockMvc;
	
	
	
	@Test
    @DisplayName("POST /v1/patient")
	void testSavePatient() throws Exception {
		// Setup our mocked service
		try {
		DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
		Date dob = dobDateFormat.parse("1988-04-13");
		System.out.println(dob);
		Integer id = 0;
		// Setup our mock repository
		HplPatientMaster widgetToPost = new HplPatientMaster(id,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423",null,null,null,null,null);
		HplPatientMaster widgetToReturn = new HplPatientMaster(1,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423",null,null,null,null,null);


		doReturn(widgetToReturn).when(service).saveUpdatePatient(any());
		ObjectMapper Obj = new ObjectMapper();  
		String jsonStr = Obj.writeValueAsString(widgetToPost);  
		// Execute the POST request
		mockMvc.perform(post("/v1/patient")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonStr))

		// Validate the response code and content type
		.andExpect(status().isCreated())
	
		// Validate the returned fields
		.andExpect(jsonPath("$.patientId", is(1))) 
		.andExpect(jsonPath("$.patientName", is("Selvakumaran")))
		.andExpect(jsonPath("$.dob", is("1988-04-13T07:00:00.000+00:00")))
		.andExpect(jsonPath("$.gender", is("M")))
		.andExpect(jsonPath("$.address", is("Chennai")))
		.andExpect(jsonPath("$.telephoneNo", is("+91 9876765423")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
    @DisplayName("PUT /v1/patient/{patientId}")
	void testUpdatePatient() throws Exception {
		// Setup our mocked service
		try {
			DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
			Date dob = dobDateFormat.parse("1988-04-13");
			DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
			System.out.println(dob);
			Integer id = 1;
			// Setup our mocked service
			HplPatientMaster widgetToPut = new HplPatientMaster(id,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",null,null,null,null);
			//HplPatientMaster widgetToReturnFindBy = new HplPatientMaster(1,"Selva", dob, "M", "Chennai", "+91 1234567890","Y",1,currentDate,null,null);
			HplPatientMaster widgetToReturnSave = new HplPatientMaster(1,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
			doThrow(new RuntimeException()).when(service).findByPatientIdAndIsActive(id,"Y");
			//doReturn(Optional.of(widgetToReturnFindBy)).when(repo).findById(1);
		   doReturn(widgetToReturnSave).when(service).saveUpdatePatient(any());

			ObjectMapper Obj = new ObjectMapper();  
			String jsonStr = Obj.writeValueAsString(widgetToPut);  
			// Execute the POST request
			mockMvc.perform(put("/v1/patient/{patientId}",1)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonStr))

			// Validate the response code and content type
			.andExpect(status().isOk())
			// Validate the returned fields
			.andExpect(jsonPath("$.patientId", is(1))) 
			.andExpect(jsonPath("$.patientName", is("Selvakumaran")))
			.andExpect(jsonPath("$.dob", is("1988-04-13T07:00:00.000+00:00")))
			.andExpect(jsonPath("$.gender", is("M")))
			.andExpect(jsonPath("$.address", is("Chennai")))
			.andExpect(jsonPath("$.telephoneNo", is("+91 9876765423")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Test
    @DisplayName("DELETE /v1/patient/{patientId}")
	void testDeletePatient() throws Exception {
		// Setup our mocked service
		try {

			Integer id = 1;
			doThrow(new RuntimeException()).when(service).findByPatientIdAndIsActive(id,"Y");
			//doReturn(Optional.empty()).when(service).findByPatientIdAndIsActive(1 ,"Y");
			  
			// Execute the POST request
			mockMvc.perform(delete("/v1/patient/{patientId}",1)
					.contentType(MediaType.APPLICATION_JSON)
					.content(""))
			// Validate the response code and content type
			.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	  @Test
	    @DisplayName("GET /patient success")
	    void testGetPatientSuccess() throws Exception {
	        // Setup our mocked service
			DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
			Date dob = dobDateFormat.parse("1988-04-13");
			DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
		  	HplPatientMaster patient1 = new HplPatientMaster(1,"Selva", dob, "M", "Chennai", "+91 1234567890","Y",1,currentDate,null,null);
			HplPatientMaster patient2 = new HplPatientMaster(2,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
			List<HplPatientMaster> list = new ArrayList<>();
			list.add(patient1);
			list.add(patient2);
			doReturn(list).when(service).getPatients();

	        // Execute the GET request
	        mockMvc.perform(get("/v1/patient"))
	                // Validate the response code and content type
	                .andExpect(status().isOk())
	 
	               // Validate the returned fields
	                .andExpect(jsonPath("$", hasSize(2)))
	                .andExpect(jsonPath("$[0].patientId", is(1))) 
	    			.andExpect(jsonPath("$[0].patientName", is("Selva")))
	    			.andExpect(jsonPath("$[0].dob", is("1988-04-13T07:00:00.000+00:00")))
	    			.andExpect(jsonPath("$[0].gender", is("M")))
	    			.andExpect(jsonPath("$[0].address", is("Chennai")))
	    			.andExpect(jsonPath("$[0].telephoneNo", is("+91 1234567890")))
	        		.andExpect(jsonPath("$[1].patientId", is(2))) 
	    			.andExpect(jsonPath("$[1].patientName", is("Selvakumaran")))
	    			.andExpect(jsonPath("$[1].dob", is("1988-04-13T07:00:00.000+00:00")))
	    			.andExpect(jsonPath("$[1].gender", is("M")))
	    			.andExpect(jsonPath("$[1].address", is("Chennai")))
	    			.andExpect(jsonPath("$[1].telephoneNo", is("+91 9876765423")));
	    }

	  @Test
	    @DisplayName("POST /patient/search success")
	    void testGetPatientDetailsSuccess() throws Exception {
	        // Setup our mocked service
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


			ObjectMapper Obj = new ObjectMapper();  
			String jsonStr = Obj.writeValueAsString(hplFilterSort);
			
			List<HplPatientMaster> list = new ArrayList<>();
			list.add(patient1);
			list.add(patient2);
			list.add(patient3);
			
			HplPatientMasterResponse response = new HplPatientMasterResponse();
			response.setRecords(list);
			response.setPageNumber(1);
			response.setTotalPages(1);
			response.setTotalRecords(2);
			
			doThrow(new RuntimeException()).when(service).getPatientdetails(hplFilterSort,1,3);
			//doReturn(response).when(service).getPatientdetails(hplFilterSort,1,3);

			mockMvc.perform(post("/v1/patient/search")
					.queryParam("page", "1")
					.queryParam("size", "3")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonStr))
	                // Validate the response code and content type
	                .andExpect(status().isOk())
	 
	               // Validate the returned fields
	                
	                .andExpect(jsonPath("$.pageNumber", is(1)))
	                .andExpect(jsonPath("$.totalPages", is(1)))
	                .andExpect(jsonPath("$.totalRecords", is(2)))
	                .andExpect(jsonPath("$.records", hasSize(2)))
	                .andExpect(jsonPath("$.records[0].patientId", is(1))) 
	    			.andExpect(jsonPath("$.records[0].patientName", is("AAA")))
	    			.andExpect(jsonPath("$.records[0].dob", is("1988-04-13T07:00:00.000+00:00")))
	         		.andExpect(jsonPath("$.records[0].gender", is("M")))
	    			.andExpect(jsonPath("$.records[0].address", is("Chennai")))
	    			.andExpect(jsonPath("$.records[0].telephoneNo", is("+91 1234567890")))
	        		.andExpect(jsonPath("$.records[1].patientId", is(2))) 
	    			.andExpect(jsonPath("$.records[1].patientName", is("Selvakumaran")))
	    			.andExpect(jsonPath("$.records[1].dob", is("1988-04-13T07:00:00.000+00:00")))
	    			.andExpect(jsonPath("$.records[1].gender", is("M")))
	    			.andExpect(jsonPath("$.records[1].address", is("Chennai")))
	    			.andExpect(jsonPath("$.records[1].telephoneNo", is("+91 9876765423")));
	    }

//
//	@Test
//    @DisplayName("PUT /patient/1 - Test cases for update patient ")
//    void testUpdatePatient() throws Exception {
////		DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
////		Date dob = dobDateFormat.parse("1988-04-13");
////		DateFormat createdDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
////		Date createdDate = dobDateFormat.parse("2022-09-19 05:43:35");
////		Integer id = 0;
////		
////		
////		
////		HplPatientMaster patientToPut = new HplPatientMaster(null,"Name 1", dob, "M", "Chennai", "+91 9876765423","Y",null,null,null,null);
////		HplPatientMaster patientToReturnFindBy = service.findByPatientIdAndIsActive(1, "Y");
////				//new HplPatientMaster(1,"Selva", dob, "M", "Karur, Tamil Nadu, India", "+91 9787316185","Y",1,createdDate,null,null);
////		HplPatientMaster patientToReturnSave = new HplPatientMaster(1,"Name 2", dob, "F", "Chennai, Tamil Nadu", "+91 9876764423","Y",null,null,null,null);
////		//HplPatientMaster patient = new HplPatientMaster(null,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",null,null,null,null);
////			
////		//doReturn(Optional.of(widget)).when(repository).findById(1l);
////		//doReturn(Response.ok(patientToReturnFindBy).build()).when(service).findByPatientIdAndIsActive(1, "Y");
////		//doReturn(response).when(result);
////        //doReturn(Optional.of(patientToReturnFindBy)).when(service).findByPatientIdAndIsActive(1, "Y");
////		doReturn(ResponseEntity.ok(patientToPut)).when(service).saveUpdatePatient(1,"Name 1", dob, "M", "Chennai", "+91 9876765423");
////		
////        //doReturn(patientToReturnSave).when(service).saveUpdatePatient(id,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423");
////
////		// Assert the response
////					Assertions.assertNotNull(returnedWidget, "The saved patient should not be null");
////					Assertions.assertNotSame(2, returnedWidget.getPatientId(), "The id should be incremented");
////
////        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
////        String json = ow.writeValueAsString(patientToPut);
////        // Execute the POST request
////        mockMvc.perform(put("/v1/patient/{id}", 1)
////                .contentType(MediaType.APPLICATION_JSON)
////                .header(HttpHeaders.IF_MATCH, 2)
////                .content(json))
////
////                // Validate the response code and content type
////                .andExpect(status().isOk())
////                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
////
////                // Validate headers
////                .andExpect(header().string(HttpHeaders.LOCATION, "/v1/patient/1"))
////                .andExpect(header().string(HttpHeaders.ETAG, "\"3\""))
////
////                // Validate the returned fields 
////                .andExpect(jsonPath("$.patientId", is(1))) 
////                .andExpect(jsonPath("$.patientName", is("Name 1")))
////                .andExpect(jsonPath("$.dob", is(dob)))
////                .andExpect(jsonPath("$.gender", is("M")))
////                .andExpect(jsonPath("$.address", is("Chennai")))
////                .andExpect(jsonPath("$.telephoneNo", is("+91 9876765423")))
////                .andExpect(jsonPath("$.isActive", is("Y")));
//    }
//	
//	@Test
//	@DisplayName("Test for delete patient")
//	void deletePatient() {
//		try {
//			DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
//			Date dob = dobDateFormat.parse("1988-04-13");
//			Integer id = 1;
//			// Setup our mock repository
//			/HplPatientMaster patient = new HplPatientMaster(id,"Name", dob, "M", "Chennai", "+91 9876765423","Y",null,null,null,null);
//			HplPatientMaster patientToReturnFindBy = service.findByPatientIdAndIsActive(1, "Y");
//			doReturn(ResponseEntity.ok()).when(repository).deletePatient(id);
//			
//			// Execute the service call
//			String returnString = service.deletePatient(id);
//
//			// Assert the response
//			Assertions.assertNotNull(returnString, "The saved patient should not be null");
//			Assertions.assertSame(patientToReturnFindBy.getPatientId(), returnedWidget.getPatientId(), "This id should be same");
//			Assertions.assertNotSame(patientToReturnFindBy.getPatientName(), returnedWidget.getPatientName(), "This id should not be same");
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//
//	}		
	}


