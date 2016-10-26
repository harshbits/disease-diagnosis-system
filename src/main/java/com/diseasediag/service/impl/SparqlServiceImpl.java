package com.diseasediag.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.diseasediag.service.SparqlService;

public class SparqlServiceImpl implements SparqlService {

	private static Logger log = LoggerFactory.getLogger(SparqlServiceImpl.class);
	
	@Override
	public String sparqlInferenceResponse(String queryString) {

		Model defaultModel = ModelFactory.createDefaultModel();
		OntModel ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, defaultModel);
		try {
			File file = new ClassPathResource("/ontology/doid.owl").getFile();
			ont.read(new FileInputStream(file), null, "RDF/XML");
		}catch(IOException e){
			e.printStackTrace();
		}

		Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
		reasoner = reasoner.bindSchema(ont);

		Dataset dataset = TDBFactory.createDataset();
		Model model = dataset.getDefaultModel();

		InfModel infModel = ModelFactory.createInfModel(reasoner, model);
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
				break;
			}
		} finally {
			exec.close();
		}

		res.append(disease);
		res.append("::");
		res.append(hospital);
		res.append("::");
		res.append(hospitalCode);
		return res.toString();
	}

}
