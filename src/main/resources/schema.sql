CREATE TABLE hpl_patient_master (
  patient_id INTEGER NOT NULL AUTO_INCREMENT,
  patient_name varchar(400) NOT NULL,
  dob date NOT NULL,
  gender char(1) NOT NULL,
  address text NOT NULL,
  telephone_no varchar(45) NOT NULL,
  is_active char(1) NOT NULL,
  created_by INTEGER NOT NULL,
  created_date TIMESTAMP NOT NULL DEFAULT NOW(),
  modified_by INTEGER DEFAULT NULL,
  modified_date TIMESTAMP DEFAULT NULL,
  PRIMARY KEY (patient_id));