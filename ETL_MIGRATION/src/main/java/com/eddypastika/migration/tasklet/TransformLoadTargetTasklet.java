package com.eddypastika.migration.tasklet;

import com.eddypastika.migration.dao.TcashSubscriptionDao;
import com.eddypastika.migration.utility.LogWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class TransformLoadTargetTasklet implements Tasklet {

    private static final Logger logger = new LogWrapper(LoggerFactory.getLogger(TransformLoadTargetTasklet.class));

    private TcashSubscriptionDao tcashSubscriptionDao;

    public TransformLoadTargetTasklet(TcashSubscriptionDao tcashSubscriptionDao) {
        this.tcashSubscriptionDao = tcashSubscriptionDao;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        logger.info("Start transfom and load to target tasklet.");
        tcashSubscriptionDao.transformLoadTarget();
        return RepeatStatus.FINISHED;
    }
}
