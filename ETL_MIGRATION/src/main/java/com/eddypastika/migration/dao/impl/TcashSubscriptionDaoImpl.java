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
	
	public TcashSubscriptionDaoImpl(DataSource dataSource, String schemeName) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.reflexSchemeName = schemeName;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	private static final String SQL_TRANSFORM_LOAD_TARGET_C2P =
			" INSERT INTO counter " +
					"SELECT " +
					"'msisdn-loopcash-period' as counter_type, " +
					"msisdn, " +
					"'' as imei, " +
					"new_product as offer_id, " +
					"sum(counter) as value, " +
					"reset_date as end_dt, " +
					"'MIGRATION' as created_by, " +
					"current_timestamp(3) as created_dt, " +
					"'MIGRATION' as last_updated_by, " +
					"current_timestamp(3) as last_updated_dt " +
					"from MI_T_PRODUCT_CONFIG as conf, tcash_subscription as source " +
					"where conf.old_product = source.product_id " +
					"group by msisdn, offer_id, end_dt";


	@Override
	public int transformLoadTarget() {
		logger.info("Transform and Load to Target");
		return jdbcTemplate.update(SQL_TRANSFORM_LOAD_TARGET_C2P);
	}
}