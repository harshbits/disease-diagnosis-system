package com.diseasediag.service;

import org.apache.jena.rdf.model.InfModel;
import org.springframework.stereotype.Service;

@Service
public interface SparqlInfernceModel {

	public InfModel getInferenceModel();
}
