package com.diseasediag.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface SparqlService {
	public String sparqlInferenceResponse(String query);
	
	public String getDisease(List<String> symptoms);
}
