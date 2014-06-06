package controller;

import java.util.*;
import model.AutoAnnotator;
import model.CommonTriple;
import model.TypedTriple;
import org.semanticweb.owlapi.model.*;

public class Analiser {
    
    static String iri = "http://ime.eb.br";
    static Set ontologiesObjectProperties;
    static Set ontologiesDataProperties;

    public Analiser(){}

    public static OWLClass getThingClass(OWLOntologyManager manager)
    {
        return manager.getOWLDataFactory().getOWLThing();
    }
    
    public static HashSet getLabels(OWLEntity entity, OWLOntology ontology)
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

    public static String getIRI(OWLOntology ontology)
    {
        String newIRI = iri;
        IRI ontoIRI = ontology.getOntologyID().getOntologyIRI();
        if(ontoIRI != null)
            newIRI = ontoIRI.toString();
        return newIRI;
    }

    private static Set getOntologyDataProperties(OWLOntology ontology)
        throws Exception
    {
        if(ontologiesDataProperties == null)
            ontologiesDataProperties = ontology.getDataPropertiesInSignature();
        return ontologiesDataProperties;
    }

    public static Set getSeila(OWLOntology ontology, OWLEntity entity)
    {
        Set annotations = entity.getAnnotations(ontology);
        return annotations;
    }

    public static void x(OWLOntology ontology, OWLEntity entity)
    {
        Set annotations = getSeila(ontology, entity);
        OWLAnnotation owlAnnotation;
        for(Iterator annotationsIt = annotations.iterator(); annotationsIt.hasNext();)
            owlAnnotation = (OWLAnnotation)annotationsIt.next();

    }

    public static HashSet getTriples(OWLObject object, AutoAnnotator autoAnnotator)
        throws Exception
    {
        HashSet triples = new HashSet();
        if(object instanceof OWLClass)
        {
            OWLClass classe = (OWLClass)object;
            triples.add(getTypedTriples(classe, autoAnnotator.getOntology(), autoAnnotator.getManager()));
            triples.addAll(getLabels(classe, autoAnnotator.getOntology()));
        }
        if(object instanceof OWLNamedIndividual)
        {
            OWLNamedIndividual individual = (OWLNamedIndividual)object;
            triples.addAll(getPropertiesTriples(individual, autoAnnotator.getOntology()));
            triples.add(getTypedTriples(individual, autoAnnotator.getOntology(), autoAnnotator.getManager()));
            triples.addAll(getLabels(individual, autoAnnotator.getOntology()));
        }
        return triples;
    }

    public static HashSet getPropertiesTriples(OWLNamedIndividual individual, OWLOntology ontology)
        throws Exception
    {
        HashSet triples = new HashSet();
        if(!ontology.getDataPropertyAssertionAxioms(individual).isEmpty())
        {
            OWLDataPropertyAssertionAxiom axiom;
            for(Iterator datasIts = ontology.getDataPropertyAssertionAxioms(individual).iterator(); datasIts.hasNext(); triples.add(new CommonTriple(individual, axiom.getProperty(), axiom.getObject())))
                axiom = (OWLDataPropertyAssertionAxiom)datasIts.next();

        }
        if(!ontology.getObjectPropertyAssertionAxioms(individual).isEmpty())
        {
            OWLObjectPropertyAssertionAxiom axiom;
            for(Iterator objetcsIts = ontology.getObjectPropertyAssertionAxioms(individual).iterator(); objetcsIts.hasNext(); triples.add(new CommonTriple(individual, axiom.getProperty(), axiom.getObject())))
                axiom = (OWLObjectPropertyAssertionAxiom)objetcsIts.next();

        }
        return triples;
    }

    public static TypedTriple getTypedTriples(OWLEntity entity, OWLOntology ontology, OWLOntologyManager manager)
        throws Exception
    {
        TypedTriple typedTriple = new TypedTriple();
        if(entity instanceof OWLClass)
        {
            OWLClass classe = (OWLClass)entity;
            HashSet classes = new HashSet();
            classes.add(classe);
            if(!classe.getSuperClasses(ontology).isEmpty())
            {
                OWLClassExpression oWLClassExpression;
                for(Iterator supersIt = classe.getSuperClasses(ontology).iterator(); supersIt.hasNext(); classes.addAll(oWLClassExpression.getClassesInSignature()))
                    oWLClassExpression = (OWLClassExpression)supersIt.next();

            }
            typedTriple.setTypes(classes);
        }
        if(entity instanceof OWLNamedIndividual)
        {
            OWLNamedIndividual individual = (OWLNamedIndividual)entity;
            HashSet classes = new HashSet();
            if(!individual.getTypes(ontology).isEmpty())
            {
                OWLClassExpression oWLClassExpression;
                for(Iterator typesIt = individual.getTypes(ontology).iterator(); typesIt.hasNext(); classes.addAll(oWLClassExpression.getClassesInSignature()))
                    oWLClassExpression = (OWLClassExpression)typesIt.next();

            }
            classes.add(getThingClass(manager));
            typedTriple.setTypes(classes);
        }
        return typedTriple;
    }


}
