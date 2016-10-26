package com.diseasediag.controller;

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
	
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> retrieve(@RequestParam(required = true) String query) {
		log.info(query);
		String result = sparqlService.sparqlInferenceResponse(query);
		ResponseEntity<String> response = new ResponseEntity<String>(result, HttpStatus.OK);
		return response;
	}

}
