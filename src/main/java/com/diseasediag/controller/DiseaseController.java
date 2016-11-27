package com.diseasediag.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diseasediag.bo.DiseaseInfoResponse;
import com.diseasediag.bo.DiseaseResponse;
import com.diseasediag.bo.ErrorObject;
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

	@RequestMapping(value = "/diseaseinfo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> retrieve(@RequestParam(required = true) String disease) {
		try {
			DiseaseInfoResponse result = sparqlService.sparqlInferenceResponse(disease);
			ResponseEntity<DiseaseInfoResponse> response = new ResponseEntity<DiseaseInfoResponse>(result,
					HttpStatus.OK);
			return response;
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			ErrorObject result = new ErrorObject();
			result.setCode("500");
			result.setMessage(e.getMessage());
			ResponseEntity<ErrorObject> response = new ResponseEntity<ErrorObject>(result,
					HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}

	}

	@RequestMapping(value = "/disease", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getDisease(@RequestParam(required = true) List<String> symptoms) {

		try {
			List<String> list = new ArrayList<>(symptoms);
			DiseaseResponse result = sparqlService.getDisease(list);
			ResponseEntity<DiseaseResponse> response = new ResponseEntity<DiseaseResponse>(result, HttpStatus.OK);
			return response;

		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			ErrorObject result = new ErrorObject();
			result.setCode("500");
			result.setMessage(e.getMessage());
			ResponseEntity<ErrorObject> response = new ResponseEntity<ErrorObject>(result,
					HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}

	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> hadnleException(Exception e) {
		e.printStackTrace();
		log.error(e.getStackTrace().toString());
		ErrorObject result = new ErrorObject();
		result.setCode("500");
		result.setMessage(e.getMessage());
		ResponseEntity<ErrorObject> response = new ResponseEntity<ErrorObject>(result,
				HttpStatus.INTERNAL_SERVER_ERROR);
		return response;
	}

}
