package com.diseasediag.service;

import org.springframework.stereotype.Service;

@Service
public interface SparqlService {
	public String sparqlInferenceResponse(String query);
}
