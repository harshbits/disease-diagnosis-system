package com.diseasediag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.diseasediag.service.SparqlInfernceModel;
import com.diseasediag.service.SparqlService;
import com.diseasediag.service.impl.SparqlInfernceModelImpl;
import com.diseasediag.service.impl.SparqlServiceImpl;

@SpringBootApplication
@ComponentScan("com.diseasediag")
@EnableConfigurationProperties
@EnableAutoConfiguration
public class DiseaseDiagnosisApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiseaseDiagnosisApplication.class, args);
	}
	
	@Bean
	public SparqlService sparqlService(){
		return new SparqlServiceImpl();
	}
	
	@Bean
	public SparqlInfernceModel sparqlInfernceModel(){
		return new SparqlInfernceModelImpl();
	}
	
}
