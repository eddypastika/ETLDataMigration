package com.eddypastika.migration.listener;


import com.eddypastika.migration.utility.LogWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.util.concurrent.TimeUnit;

/**
 * Created by Eddy Pastika.
 * User: ig.eddy.p.putra
 * Date: 2018/12/07
 * Time: 10:44 AM
 * @eddypastika
 */
public class StepDurationCalculationListener implements StepExecutionListener {

    private static final Logger logger = new LogWrapper(LoggerFactory.getLogger(StepDurationCalculationListener.class));

    private long startTime;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        long inSecond = TimeUnit.MILLISECONDS.toSeconds(duration);
        logger.info("Step duration. step name: {} time(second): {}", stepExecution.getStepName(), inSecond);
        return stepExecution.getExitStatus();
    }
}
