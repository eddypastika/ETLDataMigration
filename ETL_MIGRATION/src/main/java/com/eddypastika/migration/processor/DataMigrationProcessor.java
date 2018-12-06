package com.eddypastika.migration.processor;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.eddypastika.migration.model.Counter;
import com.eddypastika.migration.model.TcashSubcription;
import com.eddypastika.migration.utility.LogWrapper;

public class DataMigrationProcessor implements ItemProcessor<TcashSubcription, TcashSubcription> {
	
    private static final Logger logger = new LogWrapper(LoggerFactory.getLogger(DataMigrationProcessor.class));


	@Override
	public TcashSubcription process(TcashSubcription source) throws Exception {
		
//		Counter target = new Counter();
//		target.setCounter_type("msisdn-loopcash-period");
//		target.setMsisdn(source.getMsisdn());
//		target.setImei("");
//		target.setOffer_id(source.getProduct_id());
//		target.setValue(source.getCounter());
//		target.setCreated_dt(new Date());
//		target.setCreated_by("MIGRATION");
//		target.setLast_updated_dt(new Date());
//		target.setLast_updated_by("MIGRATION");
		return source;
	}



}
