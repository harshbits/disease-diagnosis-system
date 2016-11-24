package com.diseasediag.service.impl;

import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.diseasediag.bo.DiseaseInfoResponse;
import com.diseasediag.bo.DiseaseResponse;
import com.diseasediag.service.SparqlInfernceModel;
import com.diseasediag.service.SparqlService;

public class SparqlServiceImpl implements SparqlService {

	private static Logger log = LoggerFactory.getLogger(SparqlServiceImpl.class);
	
	
	@Autowired
	SparqlInfernceModel model;
	
//	@Override
	public DiseaseInfoResponse sparqlInferenceResponse(String queryString) {

		InfModel infModel = model.getInferenceModel();
		if(model == null){
			return null;
		}
		String disease = null;
		String hospital = null;
		String hospitalCode = null;
		StringBuilder res = new StringBuilder();
		Query query = QueryFactory.create(queryString);
		QueryExecution exec = QueryExecutionFactory.create(query, infModel);
		try {
			System.out.println("here");
			ResultSet rs = exec.execSelect();
			while (rs.hasNext()) {
				QuerySolution soln = rs.nextSolution();
//				disease = soln.get("dn").toString();
//				hospital = soln.get("hn").toString();
//				hospitalCode = soln.get("hc").toString();
				disease = soln.toString();
//				soln.get
				System.out.println("\n\n"+ soln.toString());
//				break;
			}
		} finally {
			exec.close();
		}

		res.append(disease);
//		res.append("::");
//		res.append(hospital);
//		res.append("::");
//		res.append(hospitalCode);
//		return res.toString();
		DiseaseInfoResponse response = new DiseaseInfoResponse();
		response.setDefinition("Definition");
		
		return response;
	}

	@Override
	public DiseaseResponse getDisease(List<String> symptoms) {
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();

		for (int i = 0; i < symptoms.size(); i++) {
			sb.append("?s" + i + " smf:symptomName " + "?o" + i
					+ " FILTER (regex(?o" + i + ", '"
					+ symptoms.get(i) + "'))");
			sb.append("\n");
		}

		for (int i = 0; i < symptoms.size(); i++) {
			sb1.append("?d smf:hasSymptom ?s" + i + ".");
			sb.append("\n");
		}
		
		String qry = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
				+ "PREFIX smf: <http://www.semanticweb.org/harsh/ontologies/2015/3/projectOnt_AI#>"
				+ " SELECT *" + " WHERE { "
				+ sb
				+ sb1
				+ " ?d smf:diseaseName ?dn."
				+ " ?d smf:diseaseType ?dt."
				+ " ?d smf:hasRisk ?r. "
				+ " ?r smf:riskName  ?rn."
				+ " ?l rdf:type smf:Location."
				+ " ?l smf:locationName ?lo FILTER (regex(?lo, '"
				+ "The University of Texas at Dallas"
				+ "')) "
				+ " ?l smf:locationArea ?ln."
				+ " ?lnm smf:areaName ?lno FILTER(regex(?lno, ?ln)) "
				+ " ?d smf:hasSpecialityHospital ?hs."
				+ " ?lnm smf:ContainsHospital ?lh."
				+ " ?lh smf:hasSpeciality ?lhs."
				+ " ?d smf:hasHospitalSpeciality ?dhs."
				+ " ?lhs1 smf:hospitalType ?lho FILTER(regex(?lho, 'General Hospital'))"
				+ " ?lhs1 smf:containsSpeciality ?fho FILTER(?fho IN(?lh))"
				+ " BIND(IF (?lhs != ?dhs, ?fho,?hs) AS ?h1)"
				+ " BIND(IF (?rn != 'High Risk', ?hs, ?h1) AS ?h)"
				+ " ?h smf:hospitalName ?hn."
				+ " ?h smf:hospitalCode ?hc " + " }";
		
		String disease ="";
		
		StringBuilder res = new StringBuilder();
		Query query = QueryFactory.create(qry);
		QueryExecution exec = QueryExecutionFactory.create(query, model.getDiseaseInferenceModel());
		try {
			System.out.println("here");
			ResultSet rs = exec.execSelect();
			while (rs.hasNext()) {
				QuerySolution soln = rs.nextSolution();
				disease = soln.get("dn").toString();
//				hospital = soln.get("hn").toString();
//				hospitalCode = soln.get("hc").toString();
//				disease = soln.toString();
//				soln.get
				System.out.println("\n\n"+ soln.toString());
//				break;
			}
		} finally {
			exec.close();
		}

		res.append(disease);
//		res.append("::");
//		res.append(hospital);
//		res.append("::");
//		res.append(hospitalCode);
//		return res.toString();
		System.out.println(disease);
		System.out.println("\n\n\n"+ res.toString());
		DiseaseResponse d = new DiseaseResponse();
		d.setDisesae(disease);
		return d;
	}

}
