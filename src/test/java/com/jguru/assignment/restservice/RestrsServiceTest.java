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
	}

	@Test
	@DisplayName("POST /v1/patient")
	void testSavePatientPreConditionFailed() throws Exception {
		// Setup our mocked service

		DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
		Date dob = dobDateFormat.parse("1988-04-13");
		System.out.println(dob);
		Integer id = 0;
		// Setup our mock repository
		HplPatientMaster widgetToPost = new HplPatientMaster(id,"Selvakumaran", null, "M", "Chennai", "+91 9876765423",null,null,null,null,null);
		HplPatientMaster widgetToReturn = new HplPatientMaster(1,"Selvakumaran", dob, "M", null, "",null,null,null,null,null);


		doReturn(widgetToReturn).when(service).saveUpdatePatient(any());
		ObjectMapper Obj = new ObjectMapper();  
		String jsonStr = Obj.writeValueAsString(widgetToPost);  
		// Execute the POST request
		mockMvc.perform(post("/v1/patient")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonStr))

		// Validate the response code and content type
		.andExpect(status().isPreconditionFailed());
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
			HplPatientMaster widgetToReturnSave = new HplPatientMaster(1,"Selvakumaran", dob, "M", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
			doThrow(new RuntimeException()).when(service).findByPatientIdAndIsActive(id,"Y");
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
	@DisplayName("PUT /v1/patient/{patientId}")
	void testUpdatePatientPreconditionFail() throws Exception {
		// Setup our mocked service
		try {
			DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
			Date dob = dobDateFormat.parse("1988-04-13");
			DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
			System.out.println(dob);
			Integer id = 1;
			// Setup our mocked service
			HplPatientMaster widgetToPut = new HplPatientMaster(id,"Selvakumaran", dob, "M", "", "+91 9876765423","Y",null,null,null,null);
			HplPatientMaster widgetToReturnSave = new HplPatientMaster(1,"Selvakumaran", dob, "", "Chennai", "+91 9876765423","Y",1,currentDate,null,null);
			doThrow(new RuntimeException()).when(service).findByPatientIdAndIsActive(id,"Y");
			doReturn(widgetToReturnSave).when(service).saveUpdatePatient(any());

			ObjectMapper Obj = new ObjectMapper();  
			String jsonStr = Obj.writeValueAsString(widgetToPut);  
			// Execute the POST request
			mockMvc.perform(put("/v1/patient/{patientId}",1)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonStr))

			// Validate the response code and content type
			.andExpect(status().isPreconditionFailed());

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
	@DisplayName("DELETE /v1/patient/{patientId}")
	void testDeletePatientPreConditionFailed() throws Exception {
		// Setup our mocked service
		try {

			Integer id = 1;
			doThrow(new RuntimeException()).when(service).findByPatientIdAndIsActive(id,"Y");
			//doReturn(Optional.empty()).when(service).findByPatientIdAndIsActive(1 ,"Y");

			// Execute the POST request
			mockMvc.perform(delete("/v1/patient/{patientId}",0)
					.contentType(MediaType.APPLICATION_JSON)
					.content(""))
			// Validate the response code and content type
			.andExpect(status().isPreconditionFailed());
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

		doReturn(response).when(service).getPatientdetails(hplFilterSort,1,3);

		mockMvc.perform(post("/v1/patient/search")
				.queryParam("page", "1")
				.queryParam("size", "3")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonStr))
		// Validate the response code and content type
		.andExpect(status().isOk());

		// Validate the returned fields
//		.andExpect(jsonPath("$.totalPages", is(1)))
//		.andExpect(jsonPath("$.totalRecords", is(2)))
//		.andExpect(jsonPath("$.records", hasSize(2)))
//		.andExpect(jsonPath("$.records[0].patientId", is(1))) 
//		.andExpect(jsonPath("$.records[0].patientName", is("AAA")))
//		.andExpect(jsonPath("$.records[0].dob", is("1988-04-13T07:00:00.000+00:00")))
//		.andExpect(jsonPath("$.records[0].gender", is("M")))
//		.andExpect(jsonPath("$.records[0].address", is("Chennai")))
//		.andExpect(jsonPath("$.records[0].telephoneNo", is("+91 1234567890")))
//		.andExpect(jsonPath("$.records[1].patientId", is(2))) 
//		.andExpect(jsonPath("$.records[1].patientName", is("Selvakumaran")))
//		.andExpect(jsonPath("$.records[1].dob", is("1988-04-13T07:00:00.000+00:00")))
//		.andExpect(jsonPath("$.records[1].gender", is("M")))
//		.andExpect(jsonPath("$.records[1].address", is("Chennai")))
//		.andExpect(jsonPath("$.records[1].telephoneNo", is("+91 9876765423")));
	}
	
}


