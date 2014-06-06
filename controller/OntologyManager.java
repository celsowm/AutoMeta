// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 10/08/2012 11:16:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   OntologyManager.java

package controller;

import java.util.Iterator;
import java.util.Set;
import org.fife.ui.autocomplete.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

// Referenced classes of package controller:
//            Util

public class OntologyManager
{

    public OWLOntologyManager getManager()
    {
        return manager;
    }

    private OntologyManager()
    {
        manager = OWLManager.createOWLOntologyManager();
    }

    public static OntologyManager getSingleton() {
        // needed because once there is singleton available no need to acquire
        // monitor again & again as it is costly
        if (singleton == null) {
            synchronized (OntologyManager.class) {
                // this is needed if two threads are waiting at the monitor at the
                // time when singleton was getting instantiated
                if (singleton == null) {
                    singleton = new OntologyManager();
                }
            }
        }
        return singleton;
    }

    public OWLOntology loadOntology(IRI ontIRI)
    {
        try
        {
            if(!ontIRI.equals(ontologyIRI))
            {
                ontologyIRI = ontIRI;
                ontologySingleton = manager.loadOntologyFromOntologyDocument(ontIRI);
            }
        }
        catch(Exception e)
        {
            Util.getException(e);
        }
        return ontologySingleton;
    }

    public CompletionProvider createCompletionProvider(OWLOntology ontology)
    {
        String typeOf = "typeof=";
        String about = "about=";
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        Set classes = ontology.getClassesInSignature(true);
        OWLClass oWLClass;
        for(Iterator itClasses = classes.iterator(); itClasses.hasNext(); provider.addCompletion(new BasicCompletion(provider, (new StringBuilder()).append(typeOf).append(Util.quote(Util.getShort(oWLClass, false))).toString())))
            oWLClass = (OWLClass)itClasses.next();

        Set individuals = ontology.getIndividualsInSignature(true);
        OWLNamedIndividual oWLNamedIndividual;
        for(Iterator itIndividuals = individuals.iterator(); itIndividuals.hasNext(); provider.addCompletion(new BasicCompletion(provider, (new StringBuilder()).append(about).append(Util.quote(Util.getShort(oWLNamedIndividual, false))).toString())))
            oWLNamedIndividual = (OWLNamedIndividual)itIndividuals.next();

        ontology.getObjectPropertiesInSignature(true);
        return provider;
    }

    private static volatile OntologyManager singleton;
    private OWLOntologyManager manager;
    private IRI ontologyIRI;
    private OWLOntology ontologySingleton;
}
