package com.eddypastika.migration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eddypastika.migration.utility.LogWrapper;

@RestController
@RequestMapping("/ETLMigration")
public class DataMigrationController {
	
    private static final Logger logger = new LogWrapper(LoggerFactory.getLogger(DataMigrationController.class));
    
    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private Job extractJob;
    
    @GetMapping("/extract")
    public ResponseEntity<Void> acceptRequest(){
    	
    	logger.debug("Test Run Migration with Spring!");
    	
    	JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    	jobParametersBuilder.addLong("Time", System.currentTimeMillis());
    	
    	try {
			jobLauncher.run(extractJob, jobParametersBuilder.toJobParameters());
		} catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


}
