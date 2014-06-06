/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.Node;
import model.TypedTriple;
import java.util.*;
import model.AutoAnnotator;
import model.CommonTriple;

/**
 *
 * @author fontesc
 */
public abstract class AbstractReasonerImplementation {
    
    static OWLReasoner reasoner;
    static Set ontologiesObjectProperties;
    static Set ontologiesDataProperties;
    
    private  Set getOntologyObjectProperties(OWLOntology ontology)
        throws Exception
    {
        if(ontologiesObjectProperties == null)
            ontologiesObjectProperties = ontology.getObjectPropertiesInSignature();
        return ontologiesObjectProperties;
    }
    
     private  Set getOntologyDataProperties(OWLOntology ontology)
        throws Exception
    {
        if(ontologiesDataProperties == null)
            ontologiesDataProperties = ontology.getDataPropertiesInSignature();
        return ontologiesDataProperties;
    }
     
     public abstract OWLReasoner getReasoner(OWLOntology ontology);
   
     
     public TypedTriple getTypedTriples(OWLEntity entity, OWLOntology ontology)
        throws Exception
    {
        TypedTriple typedTriple = new TypedTriple();
        if(entity instanceof OWLNamedIndividual)
        {
            OWLNamedIndividual individual = (OWLNamedIndividual)entity;
            Iterator typesIt = getReasoner(ontology).getTypes(individual, false).iterator();
            HashSet triples = new HashSet();
            Node node;
            for(; typesIt.hasNext(); triples.addAll(node.getEntities()))
                node = (Node)typesIt.next();

            typedTriple.setTypes(triples);
        }
        if(entity instanceof OWLClass)
        {
            OWLClass classe = (OWLClass)entity;
            Iterator typesIt = getReasoner(ontology).getSuperClasses(classe, false).iterator();
            HashSet triples = new HashSet();
            Node node;
            for(; typesIt.hasNext(); triples.addAll(node.getEntities()))
                node = (Node)typesIt.next();

            typedTriple.setTypes(triples);
        }
        return typedTriple;
    }
     
     public  HashSet getPropertiesTriples(OWLNamedIndividual individual, OWLOntology ontology)
        throws Exception
    {
        HashSet triples = new HashSet();
        for(Iterator i$ = getOntologyDataProperties(ontology).iterator(); i$.hasNext();)
        {
            OWLDataProperty predicate = (OWLDataProperty)i$.next();
            Iterator nodeIts = getReasoner(ontology).getDataPropertyValues(individual, predicate).iterator();
            while(nodeIts.hasNext()) 
            {
                OWLLiteral owlLiteral = (OWLLiteral)nodeIts.next();
                triples.add(new CommonTriple(individual, predicate, owlLiteral));
            }
        }

        for(Iterator i$ = getOntologyObjectProperties(ontology).iterator(); i$.hasNext();)
        {
            OWLObjectProperty predicate = (OWLObjectProperty)i$.next();
            Iterator nodeIts = getReasoner(ontology).getObjectPropertyValues(individual, predicate).iterator();
            while(nodeIts.hasNext()) 
            {
                Iterator indIt = ((Node)nodeIts.next()).iterator();
                while(indIt.hasNext()) 
                {
                    OWLNamedIndividual obj = (OWLNamedIndividual)indIt.next();
                    triples.add(new CommonTriple(individual, predicate, obj));
                }
            }
        }

        OWLLiteral oWLLiteral;

        if (reasoner instanceof PelletReasoner) {

            PelletReasoner reasonerTemp = (PelletReasoner) getReasoner(ontology);

            for (Iterator literalsIt = reasonerTemp.getAnnotationPropertyValues(individual, Annotator.getLabelProperty(ontology.getOWLOntologyManager())).iterator(); literalsIt.hasNext(); triples.add(new CommonTriple(individual, Annotator.getLabelProperty(ontology.getOWLOntologyManager()), oWLLiteral))) {
                oWLLiteral = (OWLLiteral) literalsIt.next();
            }

        } else {

            //Annotations
            Iterator literalsIt = individual.getAnnotations(ontology).iterator();
            while (literalsIt.hasNext()) {

                oWLLiteral = (OWLLiteral) literalsIt.next();
                triples.add(new CommonTriple(individual, Annotator.getLabelProperty(ontology.getOWLOntologyManager()), oWLLiteral));

            }

        }

        return triples;
    }
     
      public  HashSet getTriples(OWLObject object, AutoAnnotator autoAnnotator)
        throws Exception
    {
        HashSet triples = new HashSet();
        if(object instanceof OWLClass)
        {
            OWLClass classe = (OWLClass)object;
            triples.add(getTypedTriples(classe, autoAnnotator.getOntology()));
            triples.addAll(getLabels(classe, autoAnnotator.getOntology()));
        }
        if(object instanceof OWLNamedIndividual)
        {
            OWLNamedIndividual individual = (OWLNamedIndividual)object;
            triples.addAll(getPropertiesTriples(individual, autoAnnotator.getOntology()));
            triples.add(getTypedTriples(individual, autoAnnotator.getOntology()));
            triples.addAll(getLabels(individual, autoAnnotator.getOntology()));
        }
        return triples;
    }

    public  HashSet getLabels(OWLEntity entity, OWLOntology ontology)
    {
        HashSet triples = new HashSet();
        org.semanticweb.owlapi.model.OWLAnnotationProperty property;
        org.semanticweb.owlapi.model.OWLAnnotationValue obj;
        for(Iterator entitiesIt = entity.getAnnotations(ontology).iterator(); entitiesIt.hasNext(); triples.add(new CommonTriple(entity, property, obj)))
        {
            OWLAnnotation oWLAnnotation = (OWLAnnotation)entitiesIt.next();
            property = oWLAnnotation.getProperty();
            obj = oWLAnnotation.getValue();
        }

        return triples;
    }

    
}
