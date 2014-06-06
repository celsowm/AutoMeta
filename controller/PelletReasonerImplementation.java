/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 *
 * @author fontesc
 */
public class PelletReasonerImplementation extends AbstractReasonerImplementation{

    @Override
    public OWLReasoner getReasoner(OWLOntology ontology) {
        
        if(reasoner == null || ! (reasoner instanceof PelletReasoner))
        {
            reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
            System.out.println("Using "+reasoner.getReasonerName()+" reasoner");
        }
        return reasoner;
    }
    
    //@Override
     
    
}
