package com.eddypastika.migration.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.eddypastika.migration.model.TcashSubcription;

public class TcashSubscriptionRowMapper implements RowMapper<TcashSubcription> {

	@Override
	public TcashSubcription mapRow(ResultSet rs, int i) throws SQLException {
		
		TcashSubcription tcashSubcription =  new TcashSubcription();
		
		tcashSubcription.setMsisdn(rs.getString("msisdn"));
		tcashSubcription.setCategory(rs.getString("category"));
		tcashSubcription.setProduct_id(rs.getString("product_id"));
		tcashSubcription.setCounter(rs.getInt("counter"));
		tcashSubcription.setEnd_date(rs.getDate("end_date"));
		tcashSubcription.setReset_date(rs.getDate("reset_date"));
		
		return tcashSubcription;
	}

}
