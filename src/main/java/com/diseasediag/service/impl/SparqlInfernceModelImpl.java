package com.diseasediag.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.core.io.ClassPathResource;

import com.diseasediag.service.SparqlInfernceModel;

public class SparqlInfernceModelImpl implements SparqlInfernceModel{

	@Override
	public InfModel getInferenceModel() {
		try{
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
			return infModel;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}

}
