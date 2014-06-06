/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.clarkparsia.owlapi.explanation.PelletExplanation;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 *
 * @author celso
 */
public class MetaAnnotator {
    
    
    private static int ID = 0;
    
    
    public static String getNextId(){
        
        return "_" + ++ID;
    }
    
    public static void resetId(){
        
        ID = 0;
    }

    public static String axiomTypeToRDF(AxiomType type) {

        String  typeString = null;
        HashMap types      = new HashMap();
        
        types.put(AxiomType.ASYMMETRIC_OBJECT_PROPERTY, "owl:assymetric");
        types.put(AxiomType.CLASS_ASSERTION, "rdfs:typeof");
        types.put(AxiomType.DATATYPE_DEFINITION, "???");
        types.put(AxiomType.DATA_PROPERTY_ASSERTION, "???");
        types.put(AxiomType.DATA_PROPERTY_DOMAIN, "rdfs:domain");
        types.put(AxiomType.DATA_PROPERTY_RANGE, "rdfs:range");
        types.put(AxiomType.DECLARATION, "???");
        types.put(AxiomType.DIFFERENT_INDIVIDUALS, "owl:differentFrom");
        types.put(AxiomType.DISJOINT_CLASSES, "owl:disjointWith");
        types.put(AxiomType.DISJOINT_DATA_PROPERTIES, "???");
        types.put(AxiomType.DISJOINT_OBJECT_PROPERTIES, "???");
        types.put(AxiomType.DISJOINT_UNION, "???");
        types.put(AxiomType.EQUIVALENT_CLASSES, "owl:disjointWith");
        types.put(AxiomType.EQUIVALENT_DATA_PROPERTIES, "owl:equivalentProperty");
        types.put(AxiomType.EQUIVALENT_OBJECT_PROPERTIES, "owl:equivalentProperty");
        types.put(AxiomType.FUNCTIONAL_DATA_PROPERTY, "owl:FunctionalProperty");
        types.put(AxiomType.FUNCTIONAL_OBJECT_PROPERTY, "owl:FunctionalProperty");
        types.put(AxiomType.HAS_KEY, "owl:hasKey");
        types.put(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY, "owl:InverseFunctionalProperty");
        types.put(AxiomType.INVERSE_OBJECT_PROPERTIES, "owl:inverseOf");
        types.put(AxiomType.IRREFLEXIVE_OBJECT_PROPERTY, "owl:IrreflexiveProperty");
        types.put(AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION, "owl:NegativePropertyAssertion");
        types.put(AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION, "owl:NegativePropertyAssertion");
        types.put(AxiomType.OBJECT_PROPERTY_ASSERTION, "???");
        types.put(AxiomType.OBJECT_PROPERTY_DOMAIN, "rdfs:domain");
        types.put(AxiomType.OBJECT_PROPERTY_RANGE, "rdfs:range");
        types.put(AxiomType.REFLEXIVE_OBJECT_PROPERTY, "owl:ReflexiveProperty");
        types.put(AxiomType.SAME_INDIVIDUAL, "owl:sameAs");
        types.put(AxiomType.SUBCLASS_OF, "rdfs:subClassOf");
        types.put(AxiomType.SUB_ANNOTATION_PROPERTY_OF, "???");
        types.put(AxiomType.SUB_DATA_PROPERTY, "rdfs:subPropertyOf");
        types.put(AxiomType.SUB_OBJECT_PROPERTY, "rdfs:subPropertyOf");
        types.put(AxiomType.SUB_PROPERTY_CHAIN_OF, "owl:propertyChainAxiom");
        types.put(AxiomType.SWRL_RULE, "auto:swrl");
        types.put(AxiomType.SYMMETRIC_OBJECT_PROPERTY, "owl:SymmetricProperty");
        types.put(AxiomType.TRANSITIVE_OBJECT_PROPERTY, "owl:TransitiveProperty");
        
       
        if(types.containsKey(type)){

            typeString = types.get(type).toString();


        }

        return typeString;


        //ontology.
    }

    public static void getExplanations(PelletReasoner reasoner, OWLAxiom axiom, OWLOntology ontology, PelletExplanation expGen) {
        
    }

    public static void _getExplanations(PelletReasoner reasoner, OWLAxiom axiom, OWLOntology ontology, PelletExplanation expGen) {


        if (!ontology.containsAxiom(axiom) && !axiom.toString().
                contains("owl:Thing")) {


            Set<Set<OWLAxiom>> explanations = expGen.getEntailmentExplanations(axiom);
            Iterator<Set<OWLAxiom>> explantionsIt = explanations.iterator();

            while (explantionsIt.hasNext()) {

                Set<OWLAxiom> set = explantionsIt.next();
                //Iterator<OWLAxiom> setIt = set.iterator();

                metaToRda(axiom, expGen);

                /*while (setIt.hasNext()) {

                OWLAxiom expAxiom = setIt.next();

                }*/

            }

        }



        /* reasoner.getU

        Set<Set<OWLAxiom>> explanations = expGen.getEntailmentExplanations(axiom);

        if (explanations.size() > 0) {

        Iterator<Set<OWLAxiom>> explantionsIt = explanations.iterator();

        while (explantionsIt.hasNext()) {

        Set<OWLAxiom> set = explantionsIt.next();

        System.out.println("----" + set.size());

        Iterator<OWLAxiom> setIt = set.iterator();

        while (setIt.hasNext()) {

        OWLAxiom expAxiom = setIt.next();

        System.out.println("*****" + axiom);



        }

        }

        }

         */


        /*System.out.println("----------------------");
        System.out.println(axiom);
        System.out.println(reasoner.isEntailed(axiom));
        axiom.getAxiomType();
        System.out.println("***********************");
         *
         */

    }

    public static void metaToRda(OWLAxiom axiom, PelletExplanation expGen) {

        Set<Set<OWLAxiom>> explanations = expGen.getEntailmentExplanations(axiom);

        Iterator<Set<OWLAxiom>> explantionsIt = explanations.iterator();

        StringBuilder meta = new StringBuilder();

        while (explantionsIt.hasNext()) {

            Set<OWLAxiom> axioms = explantionsIt.next();
            //Iterator<OWLAxiom> setIt = set.iterator();


            Object subject  = null;
            Object property = null;
            Object object   = null;

            Iterator<OWLAxiom> axiomsIt = axioms.iterator();
            while (axiomsIt.hasNext()) {

                OWLAxiom owlAxiom = axiomsIt.next();

                System.out.println("*****type:" + owlAxiom.getAxiomType());

                 axiomTypeToRDF(owlAxiom.getAxiomType());


                if (owlAxiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION)) {

                    

                    Iterator<OWLNamedIndividual> propIt = owlAxiom.getIndividualsInSignature().iterator();


                    while (propIt.hasNext()) {

                        subject = propIt.next();
                        object = propIt.next();

                    }

                    owlAxiom.getIndividualsInSignature().iterator().next();


                    property = owlAxiom.getObjectPropertiesInSignature().iterator().next();



                    /**/



                }

                if (owlAxiom.isOfType(AxiomType.INVERSE_OBJECT_PROPERTIES)) {


                    
                    System.out.println("sdfsdf: "+owlAxiom);

                     Iterator<OWLObjectProperty> propertyIt = owlAxiom.
                             getObjectPropertiesInSignature().iterator();

                     while (propertyIt.hasNext()) {
                        //OWLObjectProperty oWLObjectProperty = propertyIt.next();
                        //System.out.println("bla:...."+oWLObjectProperty);
                         subject = propertyIt.next();
                         object = propertyIt.next();
                         
                    }




                }

                if (owlAxiom.isOfType(AxiomType.TRANSITIVE_OBJECT_PROPERTY)) {

                    subject = owlAxiom.getObjectPropertiesInSignature().
                            iterator().next();

                    property = "rdf:type";
                    

                    object = axiomTypeToRDF(owlAxiom.getAxiomType());

                }

                
                subject     =   subject   instanceof OWLObject ? Util.getShort((OWLObject)subject, false) : subject;
                property    =  property instanceof OWLObject ? Util.getShort((OWLObject)property, false) : property;
                object      =    object     instanceof OWLObject ? Util.getShort((OWLObject)object, false) : object;

                 meta.append("<span about=")
                         .append(Util.quote(subject.toString()))
                         .append(" rel=")
                            .append(Util.quote(property.toString()))
                         .append(" resource=")
                            .append(Util.quote(object.toString()))
                          .append("></span>");

                    System.out.println("xxxx:" + meta);


            }

        }

    }
}
