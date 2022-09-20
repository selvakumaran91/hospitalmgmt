package com.jguru.assignment.jpa.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jguru.assignment.jpa.model.HplPatientMaster;

public interface HplPatientMasterDao extends CrudRepository<HplPatientMaster, Integer>, JpaRepository<HplPatientMaster, Integer> {

	HplPatientMaster findByPatientId(Integer patientId);

	@Modifying
	@Query("update HplPatientMaster p set p.isActive = 'N', p.modifiedBy = ?2, p.modifiedDate = ?3 where p.patientId = ?1 and p.isActive = 'Y'")
	void deleteParentid(Integer patientid, Integer currentUser, Date currentDate);

	List<HplPatientMaster> findByIsActive(String isActive);
	

}
