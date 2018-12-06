package com.eddypastika.migration.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
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
	
	@Value("${source.reflex.scheme}")
	private String reflexSchemeName;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobRepository jobRepository;
	
	private Resource outputPath = new FileSystemResource("output/tcash_subscription.csv");
	
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

	
	@Bean
	public TcashSubscriptionDao tcashSubscriptionDao() {
		return new TcashSubscriptionDaoImpl(reflexDB0DataSource, reflexSchemeName);
	}
	
	@Bean
	public JdbcCursorItemReader<TcashSubcription> reader(DataSource dataSource){
		JdbcCursorItemReader<TcashSubcription> databaseReader = new JdbcCursorItemReader<>();
		databaseReader.setDataSource(dataSource);
		databaseReader.setSql(QUERY_FIND_TCASH_SUBSCRIPTION);
		databaseReader.setRowMapper(new BeanPropertyRowMapper<>(TcashSubcription.class));
		
		return databaseReader;
	}
	
	@Bean
	public DataMigrationProcessor processor() {
		return new DataMigrationProcessor();
	}
	
	@Bean
	public FlatFileItemWriter<TcashSubcription> writer(){
		FlatFileItemWriter<TcashSubcription> csvWriter = new FlatFileItemWriter<>();
		csvWriter.setResource(outputPath);
		csvWriter.setAppendAllowed(true);
		csvWriter.setLineAggregator(new DelimitedLineAggregator<TcashSubcription>() {{
			setDelimiter("|");
			setFieldExtractor(new BeanWrapperFieldExtractor<TcashSubcription>() {{
				setNames(new String[] {"msisdn", "category", "product_id", "counter", "end_date", "reset_date"});
			}});
		}});
		
		return csvWriter;
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<TcashSubcription, TcashSubcription> chunk(5000)
				.reader(reader(reflexDB0DataSource))
				.processor(processor())
				.writer(writer())
				.build();
		
	}
	
	@Bean
	public Job extractJob() {
		return jobBuilderFactory.get("extractJob")
				.incrementer(new RunIdIncrementer())
				.flow(step1())
				.end()
				.build();
	}
	

}
