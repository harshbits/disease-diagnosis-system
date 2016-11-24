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

import com.diseasediag.service.SparqlInfernceModel;
import com.diseasediag.service.SparqlService;

public class SparqlServiceImpl implements SparqlService {

	private static Logger log = LoggerFactory.getLogger(SparqlServiceImpl.class);
	
	
	@Autowired
	SparqlInfernceModel model;
	
//	@Override
	public String sparqlInferenceResponse(String queryString) {

		InfModel infModel = model.getInferenceModel();
		if(model == null){
			return "error";
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
		return "done";
	}

	@Override
	public String getDisease(List<String> symptoms) {
		// TODO Auto-generated method stub
		return null;
	}

}
