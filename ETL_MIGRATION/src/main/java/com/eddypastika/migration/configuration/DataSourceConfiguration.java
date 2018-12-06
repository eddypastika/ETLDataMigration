package com.eddypastika.migration.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.eddypastika.migration.dao.TcashSubscriptionDao;
import com.eddypastika.migration.dao.impl.TcashSubscriptionDaoImpl;

@Configuration
public class DataSourceConfiguration {
	
	@Autowired
	private Environment environment;
	
	@Bean
	@Primary
	@ConfigurationProperties("source.reflex.db0.datasource")
	public DataSourceProperties dbPropertiesReflexDB0() {
		return new DataSourceProperties();
	}
	
	@Bean("reflexDB0DataSource")
	@Primary
	@ConfigurationProperties(prefix = "source.reflex.db0.datasource")
	public DataSource reflexDB0DataSource() {
		DataSource dataSource = dbPropertiesReflexDB0()
				.initializeDataSourceBuilder()
				.build();
		
		return dataSource;
	}
}
