package com.ejemplo.testbatch.joblistener;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.ejemplo.testbatch.modelo.Personabatch;






@Component
public class Joblistener extends JobExecutionListenerSupport{
	public static final Logger LOG = LoggerFactory.getLogger(Joblistener.class);
	private JdbcTemplate jdbcTemplate;
	@Autowired
	public Joblistener(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus()==BatchStatus.COMPLETED) {
			LOG.info("FINALIZADO EL JOB !! VERIFICA RESULTADOS..");
			jdbcTemplate.query("SELECT id,nombre,apepat,tel FROM PERSONABATCH",
					(rs,row) -> new Personabatch(rs.getInt(1),rs.getString(2),
							rs.getString(3),rs.getString(4)))
			.forEach(persona->LOG.info("REGISTRO<" + persona + ">"));
			
					
		}
	}

}
