package com.eddypastika.migration.dao.impl;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.eddypastika.migration.dao.TcashSubscriptionDao;
import com.eddypastika.migration.utility.LogWrapper;

public class TcashSubscriptionDaoImpl implements TcashSubscriptionDao {
	
	private static final Logger logger = new LogWrapper(LoggerFactory.getLogger(TcashSubscriptionDaoImpl.class));
	private String reflexSchemeName;
	private JdbcTemplate jdbcTemplate;	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public TcashSubscriptionDaoImpl(DataSource dataSource, String reflexSchemeName) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.reflexSchemeName = reflexSchemeName;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	

}