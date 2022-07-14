package com.ejemplo.testbatch.configuration;




import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.ejemplo.testbatch.joblistener.Joblistener;
import com.ejemplo.testbatch.modelo.Personabatch;
import com.ejemplo.testbatch.processor.PersonaItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	public FlatFileItemReader<Personabatch> reader(){
		return new FlatFileItemReaderBuilder<Personabatch>()
				.name("personaItemReader")
				.resource(new ClassPathResource("sample-data.csv"))
				.delimited()
				.names(new String[] {"id", "nombre", "apepat", "tel"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Personabatch>(){{
					setTargetType(Personabatch.class);
				}})
			.build();
	}

	@Bean 
	public PersonaItemProcessor processor() {
		return new PersonaItemProcessor();
	}
	@Bean 
	public JdbcBatchItemWriter<Personabatch> writer (DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Personabatch>()
				.itemSqlParameterSourceProvider(
				new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO PERSONABATCH (ID,NOMBRE,APEPAT, TEL) VALUES (:id,"
						+ ":nombre, :apepat, :tel)")
				.dataSource(dataSource)
				.build();
	}
	@Bean 
	public Job importPersonabatchJob(Joblistener listener, Step step1) {
		return jobBuilderFactory.get("importPersonabatchJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}
	@Bean 
	Step step1(JdbcBatchItemWriter<Personabatch> writer) {
		return stepBuilderFactory.get("step1")
				.<Personabatch, Personabatch>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer)
				.build();
	}
}

