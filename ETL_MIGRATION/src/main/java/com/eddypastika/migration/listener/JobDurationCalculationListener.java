package com.eddypastika.migration.listener;

import com.eddypastika.migration.utility.LogWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Created by Eddy Pastika.
 * User: ig.eddy.p.putra
 * Date: 2018/12/06
 * Time: 9:08 PM
 * @eddypastika
 */

public class JobDurationCalculationListener implements JobExecutionListener {

    private static final Logger logger = new LogWrapper(LoggerFactory.getLogger(JobDurationCalculationListener.class));

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info("Job duration. parameters: {} time: {}", jobExecution.getJobParameters().getParameters(), duration);
    }
}
