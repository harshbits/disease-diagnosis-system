package com.diseasediag.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.diseasediag.bo.DiseaseInfoResponse;
import com.diseasediag.bo.DiseaseResponse;

@Service
public interface SparqlService {
	public DiseaseInfoResponse sparqlInferenceResponse(String query) throws Exception;
	
	public DiseaseResponse getDisease(List<String> symptoms) throws Exception;
}
