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


	// SOURCE
	@Bean
	@Primary
	@ConfigurationProperties("source.reflex.db0.datasource")
	public DataSourceProperties dbPropertiesReflexDB0() {
		return new DataSourceProperties();
	}
	
	@Bean("reflexDB0DataSource")
	@ConfigurationProperties(prefix = "source.reflex.db0.datasource")
	public DataSource reflexDB0DataSource() {
		DataSource dataSource = dbPropertiesReflexDB0()
				.initializeDataSourceBuilder()
				.build();
		
		return dataSource;
	}

	// TARGET
	@Bean
	@ConfigurationProperties("target.c2p.even.datasource")
	public DataSourceProperties dbPropertiesC2P(){
		return new DataSourceProperties();
	}

	@Bean("targetC2PDataSource")
	@Primary
	@ConfigurationProperties(prefix = "target.c2p.even.datasource")
	public DataSource targetC2PDataSource(){
		DataSource dataSource = dbPropertiesC2P()
				.initializeDataSourceBuilder()
				.build();

		return dataSource;
	}
}
