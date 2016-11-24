package com.diseasediag.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diseasediag.bo.DiseaseInfoResponse;
import com.diseasediag.bo.DiseaseResponse;
import com.diseasediag.service.SparqlService;


/**
*
* @author HARSHBHAVSAR (hhb140330@utdallas.edu)
*/

@Controller
public class DiseaseController {


	private static Logger log = LoggerFactory.getLogger(DiseaseController.class);

	
	@Autowired
	private SparqlService sparqlService;
	
	@RequestMapping(value = "/query1", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> retrieve(@RequestParam(required = true) String query) {
		log.info(query);
		String q = "prefix oio: <http://www.geneontology.org/formats/oboInOwl#> "
				+ "prefix owl: <http://www.w3.org/2002/07/owl#> "
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT ?baseClass ?baseLabel ?superClass ?superLabel "
				+ "WHERE { ?baseClass rdfs:subClassOf ?superClass . "
				+ "?baseClass rdfs:label ?baseLabel . "
				+ "?superClass rdfs:label ?superLabel . "
				+ "FILTER (!isBlank(?baseClass)) "
				+ "FILTER (!isBlank(?superClass)) }";
		
		String q1 = "PREFIX db:<http://www.geneontology.org/formats/oboInOwl#> "
				+ "PREFIX target:<http://www.w3.org/2002/07/owl#> "
				+ "select DISTINCT ?o ?p ?o1 ?p1 where { "
				+ "{<http://purl.obolibrary.org/obo/DOID_162> ?p ?o} "
				+ "UNION"
				+ "{?s target:annotatedSource <http://purl.obolibrary.org/obo/DOID_162> ."
				+ "?s ?p1 ?o1 }"
				+ "}";
		DiseaseInfoResponse result = sparqlService.sparqlInferenceResponse(q1);
		ResponseEntity<DiseaseInfoResponse> response = new ResponseEntity<DiseaseInfoResponse>(result, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/disease", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getDisease(@RequestParam(required = true) List<String> symptoms) {
		
		List<String> list = new ArrayList<>();
		list.add("Low Body Temperature");
		list.add("Chest Pain");
		list.add("Shortness of Breath");
		list.add("Slow Heart Beats");
		System.out.println(symptoms.toString());
		DiseaseResponse result = sparqlService.getDisease(list);
		ResponseEntity<DiseaseResponse> response = new ResponseEntity<DiseaseResponse>(result, HttpStatus.OK);
		return response;
	}

}
