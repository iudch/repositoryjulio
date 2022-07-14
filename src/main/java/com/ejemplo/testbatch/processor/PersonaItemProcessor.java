package com.ejemplo.testbatch.processor;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.ejemplo.testbatch.modelo.Personabatch;


public class PersonaItemProcessor implements ItemProcessor<Personabatch, Personabatch>{
	public static final Logger LOG = LoggerFactory.getLogger(PersonaItemProcessor.class);
	
	
	@Override
	public Personabatch process(Personabatch item) throws Exception{
		int id = item.getId();
		String nombre = item.getNombre().toUpperCase();
		String apepat = item.getApepat().toUpperCase();
		String tel = item.getTel();
		
		Personabatch persona = new Personabatch(id,nombre,apepat,tel);
		LOG.info("Convirtiendo(" + item + ")a( " + persona + ")");
		return persona;
		
}

}
