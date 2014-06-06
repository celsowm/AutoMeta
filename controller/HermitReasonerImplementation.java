/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 *
 * @author fontesc
 */
public class HermitReasonerImplementation extends AbstractReasonerImplementation{

    @Override
    public OWLReasoner getReasoner(OWLOntology ontology) {
        
        if(reasoner == null || ! (reasoner instanceof org.semanticweb.HermiT.Reasoner))
        {
            reasoner = new Reasoner(ontology);
            System.out.println("Using "+reasoner.getReasonerName()+" reasoner");
        }
        return reasoner;
    }
    
    
}
