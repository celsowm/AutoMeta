/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.OntologyManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 * @author FONTESC
 */
public class AutoAnnotator {

    private String documentText;
    private OWLOntology ontology;
    private OWLOntologyManager manager;
    private boolean reasoning;
    private boolean label;
    private boolean exhaustive;
    private String reasoner;

    public AutoAnnotator(String documentText, OWLOntology ontology, boolean reasoning, boolean label, boolean exhaustive, String reasoner) {
        this.documentText = documentText;
        this.ontology = ontology;
        this.reasoning = reasoning;
        this.label = label;
        this.exhaustive = exhaustive;
        this.reasoner = reasoner;
    }

    public String getDocumentText() {
        return documentText;
    }

    public void setDocumentText(String documentText) {
        this.documentText = documentText;
    }

    public boolean isExhaustive() {
        return exhaustive;
    }

    public void setExhaustive(boolean exhaustive) {
        this.exhaustive = exhaustive;
    }

    public boolean isLabel() {
        return label;
    }

    public void setLabel(boolean label) {
        this.label = label;
    }

    public OWLOntologyManager getManager() {

        if (manager == null) {
            manager = OntologyManager.getSingleton().getManager();
        }

        return manager;
    }

    private void setManager(OWLOntologyManager manager) {
        this.manager = manager;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    public boolean isReasoning() {
        return reasoning;
    }

    public void setReasoning(boolean reasoning) {
        this.reasoning = reasoning;
    }

    public String getReasoner() {
        return reasoner;
    }

    public void setReasoner(String reasoner) {
        this.reasoner = reasoner;
    }
}
