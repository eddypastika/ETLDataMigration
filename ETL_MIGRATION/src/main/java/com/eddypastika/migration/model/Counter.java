package com.eddypastika.migration.model;

import java.util.Date;

public class Counter {
	
	private String counter_type;
	private String msisdn;
	private String imei;
	private String offer_id;
	private Integer value;
	private Date created_dt;
	private String created_by;
	private Date last_updated_dt;
	private String last_updated_by;
	
	
	public Counter() {

	}

	public Counter(String counter_type, String msisdn, String imei, String offer_id, Integer value, Date created_dt,
			String created_by, Date last_updated_dt, String last_updated_by) {
		
		this.counter_type = counter_type;
		this.msisdn = msisdn;
		this.imei = imei;
		this.offer_id = offer_id;
		this.value = value;
		this.created_dt = created_dt;
		this.created_by = created_by;
		this.last_updated_dt = last_updated_dt;
		this.last_updated_by = last_updated_by;
	}

	public String getCounter_type() {
		return counter_type;
	}

	public void setCounter_type(String counter_type) {
		this.counter_type = counter_type;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getOffer_id() {
		return offer_id;
	}

	public void setOffer_id(String offer_id) {
		this.offer_id = offer_id;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Date getCreated_dt() {
		return created_dt;
	}

	public void setCreated_dt(Date created_dt) {
		this.created_dt = created_dt;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Date getLast_updated_dt() {
		return last_updated_dt;
	}

	public void setLast_updated_dt(Date last_updated_dt) {
		this.last_updated_dt = last_updated_dt;
	}

	public String getLast_updated_by() {
		return last_updated_by;
	}

	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}
	
}
