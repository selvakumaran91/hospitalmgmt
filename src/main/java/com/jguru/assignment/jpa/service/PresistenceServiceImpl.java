package com.jguru.assignment.jpa.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jguru.assignment.jpa.dao.HplPatientMasterDao;
import com.jguru.assignment.jpa.model.HplPatientMaster;
import com.jguru.assignment.response.model.HplPatientMasterResponse;
import com.jguru.assignment.rqeust.model.HplFilter;
import com.jguru.assignment.rqeust.model.HplFilterSort;
import com.jguru.assignment.rqeust.model.HplSort;

@Repository(value = "presistenceService")
@Transactional
public class PresistenceServiceImpl implements PresistenceService {

	@Autowired
	private HplPatientMasterDao hplPatientMasterDao;
	
	@PersistenceContext
	EntityManager em;
	
	static Logger log = Logger.getLogger(PresistenceServiceImpl.class);
	
	@Override
	public HplPatientMaster saveUpdatePatient(HplPatientMaster patientMaster) throws Exception {
		HplPatientMaster hplPatientMaster = new HplPatientMaster();
		String IsActive = "Y";
		try {
			if(patientMaster.getPatientId() != 0) {
				hplPatientMaster = findByPatientIdAndIsActive(patientMaster.getPatientId(), IsActive);
				if(hplPatientMaster == null) {
					return null;
				}
			} 
			Date currentDate = getCurrentDate();
			//DateFormat dobDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
			//dobDateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
			hplPatientMaster.setPatientName(patientMaster.getPatientName());
			hplPatientMaster.setDob(patientMaster.getDob());
			hplPatientMaster.setGender(patientMaster.getGender());
			hplPatientMaster.setAddress(patientMaster.getAddress());
			hplPatientMaster.setTelephoneNo(patientMaster.getTelephoneNo());
			hplPatientMaster.setIsActive("Y");
			if(patientMaster.getPatientId() == 0) {
				//TODO : The current user should be session user
				hplPatientMaster.setCreatedby(1);
				hplPatientMaster.setCreatedDate(currentDate);
			} else {
				//TODO : The current user should be session user
				hplPatientMaster.setModifiedBy(1);
				hplPatientMaster.setModifiedDate(currentDate);
			}
			hplPatientMaster = hplPatientMasterDao.saveAndFlush(hplPatientMaster);
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
		return hplPatientMaster;
	}
	
	@Override
	public HplPatientMaster findByPatientIdAndIsActive(Integer patientId, String IsActive) {
		return  hplPatientMasterDao.findByPatientIdAndIsActive(patientId, IsActive);
	}


	/*
	 * To change the date format to the required format
	 */
	public Date getCurrentDate() throws ParserConfigurationException {
		Date currentDate = null;
		try {
			DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			currentDate = dateFormat.parse(dateFormat.format(new Date()));
		} catch(Exception e) {
			throw new ParserConfigurationException("Error Parsing Date");
		}
		return currentDate;
	}

	@Override
	public String deletePatient(Integer patientId) throws Exception {
		String response;
		try {
			HplPatientMaster hplPatientMaster = findByPatientIdAndIsActive(patientId, "Y");
			if(hplPatientMaster != null) {
			Date currentDate = getCurrentDate();
			//TODO : The current user should be session user
			Integer currentUser = 1;
			hplPatientMasterDao.deleteParentid(patientId, currentUser, currentDate);
			response = "Record Deleted Successfully";
			} else {
				response = "Record Allready Deleted";
			}
			
		} catch(Exception e) {
			throw new IllegalArgumentException();
		}
		return response;
	}

	@Override
	public List<HplPatientMaster> getPatients() throws Exception {
		List<HplPatientMaster> patientsDetail = new ArrayList<>();
		try {
			patientsDetail = hplPatientMasterDao.findByIsActive("Y");
			
		} catch(Exception e) {
			throw new Exception();
		}
		return patientsDetail;
	}

	@Override
	public HplPatientMasterResponse getPatientdetails(HplFilterSort filterSort, int page, int size) throws Exception {
		HplPatientMasterResponse response = new HplPatientMasterResponse();
		try {
			
			List sortProperties = new ArrayList<>();
			List sortTypes = new ArrayList<>();
			List operator = new ArrayList<>();
			List value = new ArrayList<>();
			List property = new ArrayList<>();
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
		
			
			List<HplPatientMaster> records = new ArrayList<>();
			String isActive = "Y";
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<HplPatientMaster> q = cb.createQuery(HplPatientMaster.class);
			Root<HplPatientMaster> root = q.from(HplPatientMaster.class);
			CriteriaQuery<HplPatientMaster> criteriaQuery = q.select(root);
			List<Order> orderList = new ArrayList<>();
			orderList = getSort(cb, root, sortProperties, sortTypes);
			q.orderBy(orderList);
			Predicate filterCondition = cb.conjunction();
			filterCondition = cb.and(filterCondition,cb.equal(root.get("isActive"), isActive));
			if(operator != null && operator.size() > 0) {
				/*
				 * Dynamic Filter condition adding function call
				 */
				filterCondition = getFilters(filterCondition,  cb,  root, property, operator, value);
			}
			q.where(filterCondition);
			int start = 0;
			if(size != 0) {
				start = (page -1) * size;
			}
			TypedQuery<HplPatientMaster> tq = em.createQuery(q);
			if(size != 0) {
				records = tq.setFirstResult(start).setMaxResults(size).getResultList();
			} else {
				records = tq.getResultList();
			}
			TypedQuery<HplPatientMaster> queryToal = em.createQuery(criteriaQuery);
			long totalRecords = (long) queryToal.getResultList().size();
			if(size != 0) {
				int rem = (totalRecords % size) > 0 ? new Integer(1) : 0;
				int total = (int) (totalRecords / size + rem);
				response.setTotalPages(total);
				log.info("TotalRecords : "+ totalRecords +"Total Pages : " +total);
			}
			response.setRecords(records);
			response.setPageNumber(page);
			response.setTotalRecords(totalRecords);
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return response;
	}


	private List<Order> getSort(CriteriaBuilder cb, Root<HplPatientMaster> root, List<String> sortProperties, List<String> sortTypes) {
		List<Order> orderList = new ArrayList<>();
			if(sortProperties != null) {
			Order order = null;
			for( int i = 0; i < sortProperties.size(); i++) {
				String columnName = sortProperties.get(i);
				switch(sortTypes.get(i)) {
				case "asc" :
					order = cb.asc(root.get(columnName));
					break;
				case "desc" :
					order = cb.desc(root.get(columnName));
					break;
				}
			}
			orderList.add(order);
		}
		return orderList;
	}

	private Predicate getFilters(Predicate filterCondition, CriteriaBuilder cb, Root<HplPatientMaster> root, List<String> property, List<String> operator,
			List<String> value) {
		if(property != null) {
			for( int i = 0; i < property.size(); i++) {
				String columnName = property.get(i);
				String val = value.get(i);
				switch (operator.get(i)) {
				case "lt":
					filterCondition = cb.and(filterCondition,cb.le(root.get(columnName), (Number) Integer.parseInt(val)));
					break;
				case "gt":
					filterCondition = cb.and(filterCondition,cb.ge(root.get(columnName), (Number) Integer.parseInt(val)));
					break;
				case "dtgt"	:
					filterCondition = cb.and(filterCondition,cb.greaterThanOrEqualTo(root.get(columnName), new Date(Long.parseLong(val))));
					break;
				case "dtlt"	:
					filterCondition = cb.and(filterCondition,cb.lessThanOrEqualTo(root.get(columnName), new Date(Long.parseLong(val))));
					break;
				case "dton"	:
					filterCondition = cb.and(filterCondition,cb.equal(root.get(columnName), new Date(Long.parseLong(val))));
					break;
				case "startw"	:
					filterCondition = cb.and(filterCondition,cb.like(root.get(columnName), val +"%"));
					break;
				case "endw"	:
					filterCondition = cb.and(filterCondition,cb.like(root.get(columnName), "%"+val));
					break;
				case "bln"	:
					filterCondition = cb.and(filterCondition,cb.equal(root.get(columnName), Boolean.parseBoolean(val)));
					break;
				case "eq":
					filterCondition = cb.and(filterCondition,cb.equal(root.get(columnName), val));
					break;
				case "in"	:
					// TODO
					break;
				default :
					filterCondition = cb.and(filterCondition,cb.like(root.get(columnName), "%"+val+"%"));
					break;
				}
				
			}
		}
		return filterCondition;
	}
}
