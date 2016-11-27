package com.diseasediag.bo;

import java.util.List;

public class DiseaseInfoResponse {

	private String DOID;
	
	private String name;

	private String definition = "Not Available";

	private List<String> links ;

	private List<String> synonyms;

	private List<String> Xrefs;

	public String getDOID() {
		return DOID;
	}

	public void setDOID(String dOID) {
		DOID = dOID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public List<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}

	public List<String> getXrefs() {
		return Xrefs;
	}

	public void setXrefs(List<String> xrefs) {
		Xrefs = xrefs;
	}

}
