package com.eddypastika.migration.model;

import java.util.Date;

/**
 * @author ig.eddy.p.putra
 * 
 * Dec 4, 2018 7:41:21 PM
 * @eddypastika
 */
public class TcashSubcription {
	
	private String msisdn;
	private String category;
	private String product_id;
	private Integer counter;
	private Date end_date;
	private Date reset_date;
	
	
	public TcashSubcription() {

	}

	public TcashSubcription(String msisdn, String category, String product_id, Integer counter, Date end_date,
			Date reset_date) {
		this.msisdn = msisdn;
		this.category = category;
		this.product_id = product_id;
		this.counter = counter;
		this.end_date = end_date;
		this.reset_date = reset_date;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getReset_date() {
		return reset_date;
	}

	public void setReset_date(Date reset_date) {
		this.reset_date = reset_date;
	}

}
