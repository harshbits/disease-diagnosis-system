package com.diseasediag.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
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
	public DiseaseInfoResponse sparqlInferenceResponse(String disease) throws Exception{
		DiseaseInfoResponse response = new DiseaseInfoResponse();
		Set<String> synonymsMaps = new LinkedHashSet<>();
		Set<String> xRefsMaps = new LinkedHashSet<>();
		Set<String> linksMaps = new LinkedHashSet<>();
		String definition = "Not Available";
		String DOID = "Not Available";
		boolean getDOID = false;
		boolean getDefinition = false;
		int xrefcount = 0;
		
		String queryString = "PREFIX db:<http://www.geneontology.org/formats/oboInOwl#> "
				+ "PREFIX target:<http://www.w3.org/2002/07/owl#> "
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "select DISTINCT * where { "
				+ "?s ?label \""+disease.toLowerCase()+"\" . " 
//				+ "?s ?label \"cancer\" . "
				+ "{ ?s db:id ?o1 . "
				+ "} "
				+ "UNION "
				+ "{ "
				+ "?s db:hasRelatedSynonym ?o2 . "
				+ "} UNION "
				+ "{ "
				+ "?s db:hasExactSynonym ?o2 . "
				+ "?s db:hasDbXref ?o3 . "
				+ "} "
				+ "UNION "
				+ "{ "
				+ "?x target:annotatedSource ?s . "
				+ "?x target:annotatedTarget ?o4 . "
				+ "?x db:hasDbXref ?o5 . "
				+ "} "
				+ "}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution exec = QueryExecutionFactory.create(query, model.getInferenceModel());
		try {
			ResultSet rs = exec.execSelect();
			while (rs.hasNext()) {
				QuerySolution soln = rs.nextSolution();
				try{
					if(!getDOID){
						DOID = soln.getLiteral("o1").toString();	
					}
				}catch(Exception e){
					log.error("o1 Literal Select Error: "+e.getMessage());
				}
				
				try{
					synonymsMaps.add(soln.getLiteral("o2").toString());
				}catch(Exception e){
					log.error("o2 Literal Select Error: "+e.getMessage());
				}
				
				try{
					if(xrefcount < 15){
						xRefsMaps.add(soln.getLiteral("o3").toString());	
						xrefcount = xrefcount + 3;
					}
				}catch(Exception e){
					log.error("o2 Literal Select Error: "+e.getMessage());
				}
				
				try{
					if(!getDefinition){
						definition = soln.getLiteral("o4").toString();
					}
				}catch(Exception e){
					log.error("o4 Literal Select Error: "+e.getMessage());
				}
				try{
					String link = soln.getLiteral("o5").toString();
					if(link.contains("url:")){
						link = link.replace("url:", "");
						linksMaps.add(link);
					}
				}catch(Exception e){
					log.error("o5 Literal Select Error: "+e.getMessage());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			exec.close();
		}

		response.setDOID(DOID);
		response.setXrefs(new ArrayList<>(xRefsMaps));
		response.setSynonyms(new ArrayList<>(synonymsMaps));
		response.setLinks(new ArrayList<>(linksMaps));
		response.setName(disease);
		response.setDefinition(definition);
		
		return response;
	}

	@Override
	public DiseaseResponse getDisease(List<String> symptoms) throws Exception{
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		

		for (int i = 0; i < symptoms.size(); i++) {
			System.out.println("\n\n"+symptoms.get(i));
			sb.append("?s" + i + " smf:symptomName " + "?o" + i
					+ " FILTER (regex(?o" + i + ", '"
					+ symptoms.get(i).toString() + "'))");
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
		
		String disease ="Not Found";
		String risk = "Not Found";
		String hospital = "Not Found";
		
		Query query = QueryFactory.create(qry);
		QueryExecution exec = QueryExecutionFactory.create(query, model.getDiseaseInferenceModel());
		try {
			ResultSet rs = exec.execSelect();
			while (rs.hasNext()) {
				QuerySolution soln = rs.nextSolution();
				System.out.println("\n\n"+ soln.toString());
				disease = soln.get("dn").toString();
				hospital = soln.get("hn").toString();
				risk = soln.get("rn").toString();
			}
		} finally {
			exec.close();
		}

		DiseaseResponse d = new DiseaseResponse();
		d.setDisesae(disease);
		d.setHospital(hospital);
		d.setRisk(risk);
		return d;
	}

}
