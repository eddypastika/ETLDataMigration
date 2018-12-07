package com.eddypastika.migration.configuration;

import javax.sql.DataSource;

import com.eddypastika.migration.listener.JobDurationCalculationListener;
import com.eddypastika.migration.listener.StepDurationCalculationListener;
import com.eddypastika.migration.model.Counter;
import com.eddypastika.migration.tasklet.TransformLoadTargetTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.eddypastika.migration.dao.TcashSubscriptionDao;
import com.eddypastika.migration.dao.impl.TcashSubscriptionDaoImpl;
import com.eddypastika.migration.model.TcashSubcription;
import com.eddypastika.migration.processor.DataMigrationProcessor;

/**
 * @author ig.eddy.p.putra
 * 
 * Dec 5, 2018 10:58:41 AM
 * @eddypastika
 */
@Configuration
@EnableBatchProcessing
@Import(DataSourceConfiguration.class)
public class DataMigrationConfiguration extends DefaultBatchConfigurer {
	
	@Autowired
	@Qualifier("reflexDB0DataSource")
	private DataSource reflexDB0DataSource;

	@Autowired
	@Qualifier("targetC2PDataSource")
	private DataSource targetC2PDataSource;
	
	@Value("${source.reflex.scheme}")
	private String reflexSchemeName;

	@Value("${target.c2p.scheme}")
	private String targetSchemeName;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobRepository jobRepository;
	
	//private Resource outputPath = new FileSystemResource("output/tcash_subscription.csv");
	
	@Bean
    @Transactional(isolation=Isolation.READ_COMMITTED)
    public JobLauncher jobLaunchers() {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return simpleJobLauncher;
    }
	 
	@Override
	@Autowired
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(reflexDB0DataSource);
	}

	private static final String QUERY_FIND_TCASH_SUBSCRIPTION = 
			"SELECT "
			+ "msisdn, "
			+ "category, "
			+ "product_id, "
			+ "counter, "
			+ "end_date, "
			+ "reset_date "
			+ "FROM belajar.tcash_subscription ";

	
//	@Bean
//	public TcashSubscriptionDao tcashSubscriptionDao() {
//		return new TcashSubscriptionDaoImpl(targetC2PDataSource, targetSchemeName);
//	}

	@Bean
	public TcashSubscriptionDaoImpl tcashSubscriptionDaoTransfomLoadTarget() {
		return new TcashSubscriptionDaoImpl(targetC2PDataSource, targetSchemeName);
	}
	
	@Bean
	public JdbcCursorItemReader<TcashSubcription> readerExtract(DataSource dataSource){
		JdbcCursorItemReader<TcashSubcription> databaseReader = new JdbcCursorItemReader<>();
		databaseReader.setDataSource(dataSource);
		databaseReader.setSql(QUERY_FIND_TCASH_SUBSCRIPTION);
		databaseReader.setRowMapper(new BeanPropertyRowMapper<>(TcashSubcription.class));
		
		return databaseReader;
	}
	
	@Bean
	public DataMigrationProcessor processorExtract() {
		return new DataMigrationProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<TcashSubcription> writerExtract(){
//		FlatFileItemWriter<TcashSubcription> csvWriter = new FlatFileItemWriter<>();
//		csvWriter.setResource(outputPath);
//		csvWriter.setAppendAllowed(true);
//		csvWriter.setLineAggregator(new DelimitedLineAggregator<TcashSubcription>() {{
//			setDelimiter("|");
//			setFieldExtractor(new BeanWrapperFieldExtractor<TcashSubcription>() {{
//				setNames(new String[] {"msisdn", "category", "product_id", "counter", "end_date", "reset_date"});
//			}});
//		}});

		JdbcBatchItemWriter<TcashSubcription> writerToStaging = new JdbcBatchItemWriter<>();
		writerToStaging.setDataSource(targetC2PDataSource);
		writerToStaging.setSql("INSERT INTO tcash_subscription VALUES (:msisdn , :category , :product_id , :counter , :end_date , :reset_date)");
		writerToStaging.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<TcashSubcription>());
		
		return writerToStaging;
	}
	
	@Bean
	public Step step1Extract() {
		return stepBuilderFactory.get("step1").<TcashSubcription, TcashSubcription> chunk(5000)
				.reader(readerExtract(reflexDB0DataSource))
				.processor(processorExtract())
				.writer(writerExtract())
				.listener(new StepDurationCalculationListener())
				.build();
		
	}

	// TASKLET (STEP 2)
	@Bean
	@StepScope
	public TransformLoadTargetTasklet transformLoadTargetTasklet(){
		return new TransformLoadTargetTasklet(tcashSubscriptionDaoTransfomLoadTarget());
	}

	@Bean
	public Step step2TransformLoadTarget(){
		return  stepBuilderFactory.get("step2TransformLoadTarget")
				.tasklet(transformLoadTargetTasklet())
				.build();
	}
	
	@Bean
	public Job extractJob() {
		return jobBuilderFactory.get("extractJob")
				.incrementer(new RunIdIncrementer())
				.start(step1Extract())
				.next(step2TransformLoadTarget())
				.listener(new JobDurationCalculationListener())
				.build();
	}
	

}
