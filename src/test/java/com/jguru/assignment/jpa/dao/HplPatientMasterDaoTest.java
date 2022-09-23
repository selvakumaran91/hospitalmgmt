package com.jguru.assignment.jpa.dao;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.jguru.assignment.jpa.model.HplPatientMaster;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class HplPatientMasterDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private HplPatientMasterDao repository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

   
    @Test
    @DataSet("hpl_patient_master.yml")
    void testfindByPatientId() {
        HplPatientMaster patient = repository.findByPatientId(1);
        Assertions.assertEquals(patient.getPatientId(), 1,"We should find a widget with ID 1");
        Assertions.assertEquals(1, patient.getPatientId(), "The widget ID should be 1");
        Assertions.assertEquals("Name 1", patient.getPatientName(), "Incorrect widget name");

    }
    
    @Test
    @DataSet("hpl_patient_master.yml")
    void testfindByIsActive() {
        HplPatientMaster patient = repository.findByPatientIdAndIsActive(1, "Y");
        Assertions.assertEquals(patient.getPatientId(), 1,"We should find a widget with ID 1");
        Assertions.assertEquals(1, patient.getPatientId(), "The widget ID should be 1");
        Assertions.assertEquals("Name 1", patient.getPatientName(), "Incorrect widget name");

    }
    
 }
