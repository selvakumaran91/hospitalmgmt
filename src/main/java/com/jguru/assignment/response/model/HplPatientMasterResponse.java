package com.jguru.assignment.response.model;

import java.util.List;

import com.jguru.assignment.jpa.model.HplPatientMaster;

public class HplPatientMasterResponse extends PaginationResponse {
	
	private List<HplPatientMaster> records;

	public List<HplPatientMaster> getRecords() {
		return records;
	}

	public void setRecords(List<HplPatientMaster> records) {
		this.records = records;
	}
	

}
