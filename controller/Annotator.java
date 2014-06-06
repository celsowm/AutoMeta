// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 10/08/2012 11:07:38
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Annotator.java

package controller;

import java.util.*;
import java.util.regex.Pattern;
import model.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;

// Referenced classes of package controller:
//            Util, Reasoner, Analiser

public class Annotator{
    
    public static OWLAnnotationProperty labelProperty;
    public static final String regex = "(?![^<>]*+([>]|</))";
    public static final String insentiveCase = "(?i)";
    public static final String regexTerm = "$0";
    public static final String boundary = "\\b";
    public static final String prefix = "";
    public static final String SPAN   = "span";
    public static final String LESSTHANSIGN   = "<";
    public static final String SPAN_CLOSE = "</span>";

    public Annotator()
    {
    }

    private static StringBuilder typedTripleToRdfa(TypedTriple typedTriple)
        throws Exception
    {
        StringBuilder typeof = new StringBuilder();
        Iterator typesiT = typedTriple.getTypes().iterator();
        do
        {
            if(!typesiT.hasNext())
                break;
            OWLClass owlClass = (OWLClass)typesiT.next();
            typeof.append("").append(Util.getPrefixed(owlClass));
            if(typesiT.hasNext())
                typeof.append(" ");
        } while(true);
        return typeof;
    }

    public static StringBuilder triplesToRdfa(HashSet triplas, OWLEntity entity)
        throws Exception
    {
        StringBuilder retorno = new StringBuilder();
        StringBuilder typeof = new StringBuilder();
        StringBuilder propertyRDFa = new StringBuilder();
        Iterator triplesIt = triplas.iterator();
        do
        {
            if(!triplesIt.hasNext())
                break;
            Triple triple = (Triple)triplesIt.next();
            if(triple instanceof TypedTriple)
            {
                TypedTriple typedTriple = (TypedTriple)triple;
                typeof.append(typedTripleToRdfa(typedTriple));
            }
            if(triple instanceof CommonTriple)
            {
                CommonTriple commonTriple = (CommonTriple)triple;
                propertyRDFa.append(commonTripletoRdfa(commonTriple));
            }
        } while(true);
        retorno.append(LESSTHANSIGN).append(SPAN).append(" id='").append(MetaAnnotator.getNextId()).append("' about='").append("").append(Util.getPrefixed(entity));
        if(!typeof.toString().isEmpty())
            retorno.append("' typeof='").append(typeof.toString().trim());
        else
            retorno.append("' typeof='").append("owl:Thing");
        retorno.append("'>");
        retorno.append(propertyRDFa);
        retorno.append("$0");
        retorno.append(SPAN_CLOSE);
        return retorno;
    }

    public static OWLAnnotationProperty getLabelProperty(OWLOntologyManager manager)
        throws Exception
    {
        if(labelProperty == null)
        {
            OWLDataFactory df = manager.getOWLDataFactory();
            labelProperty = df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        }
        return labelProperty;
    }
    
    private static String annotateLabels(String document_mod, OWLEntity entity, AutoAnnotator autoAnnotator) throws Exception{
        
        HashSet labels = getLabels(entity, autoAnnotator);
        if(!labels.isEmpty())
        {
            Iterator labelIt = labels.iterator();
            do
            {
                if(!labelIt.hasNext())
                    break;
                OWLLiteral owlAnnotationValue = (OWLLiteral)labelIt.next();
                if(compare(owlAnnotationValue, autoAnnotator.getDocumentText()))
                {
                    StringBuilder annotations = getAnotacoes(entity, autoAnnotator);
                    document_mod = annotateInDocument(owlAnnotationValue, annotations, document_mod, autoAnnotator);
                }
            } while(true);
        }
        
        return document_mod;

    }

    private static StringBuilder getAnotacoes(OWLEntity entity, AutoAnnotator autoAnnotator)
        throws Exception
    {
        HashSet triples = new HashSet();
        StringBuilder annotations = new StringBuilder();
        if(autoAnnotator.isReasoning())
            //triples = OldPellet.getTriples(entity, autoAnnotator);
            triples = ReasonerFactory.getSingleton(autoAnnotator.getReasoner()).getTriples(entity, autoAnnotator);
        else
            triples = Analiser.getTriples(entity, autoAnnotator);
        
        annotations = triplesToRdfa(triples, entity);
        
        return annotations;
    }

    public static String annotateInDocument(OWLObject term, StringBuilder annotations, String document_mod, AutoAnnotator autoAnnotator)
        throws Exception
    {
        StringBuilder termoRegexed = new StringBuilder();
        
        //decompose
        String stringTerm = Util.getShort(term, true);
        termoRegexed.append("(?i)").append("\\b").append(stringTerm).append("\\b").append("(?![^<>]*+([>]|</))");
        if(autoAnnotator.isExhaustive()){
            document_mod = document_mod.replaceAll(termoRegexed.toString(), annotations.toString());
        }else{
            document_mod = document_mod.replaceFirst(termoRegexed.toString(), annotations.toString());
        }
        
        //decompose not
        String stringTermN = Util.getShort(term, false);
        if (!stringTermN.equals(stringTerm)) {
            stringTerm = Util.getShort(term, false);
            termoRegexed = new StringBuilder();
            termoRegexed.append("(?i)").append("\\b").append(stringTerm).append("\\b").append("(?![^<>]*+([>]|</))");
            if (autoAnnotator.isExhaustive()) {
                document_mod = document_mod.replaceAll(termoRegexed.toString(), annotations.toString());
            } else {
                document_mod = document_mod.replaceFirst(termoRegexed.toString(), annotations.toString());
            }
        }

        
        return document_mod;
    }

    public static String annotate(OWLEntity entity, String document_text, AutoAnnotator autoAnnotator)
        throws Exception
    {
        
        String document_mod = document_text;
        if(compare(entity, document_text)) {
            
            StringBuilder annotations = getAnotacoes(entity, autoAnnotator);
            document_mod = annotateInDocument(entity, annotations, document_mod, autoAnnotator);
        }
        
        if(autoAnnotator.isLabel() == true){
            document_mod = annotateLabels(document_mod, entity, autoAnnotator);
        }
        
        return document_mod;
    }

    public static HashSet getLabels(OWLEntity entity, AutoAnnotator autoAnnotator)
        throws Exception
    {
        HashSet entityLabels = new HashSet();
        
        Iterator labelsIterator = entity.getAnnotations(autoAnnotator.getOntology(), getLabelProperty(autoAnnotator.getManager())).iterator();
        do
        {
            if(!labelsIterator.hasNext())
                break;
            OWLAnnotation annotation = (OWLAnnotation)labelsIterator.next();
            if(annotation.getValue() instanceof OWLLiteral)
            {
                OWLLiteral val = (OWLLiteral)annotation.getValue();
                entityLabels.add(val);
            }
        } while(true);
        return entityLabels;
    }

    private static boolean compare(OWLObject entity, String document_text)
        throws Exception{
        
        boolean match = false;
        //if(Util.isRestriction(entity.toString())) return false;
        
        String term = Util.getShort(entity, true);
        StringBuilder regexedTerm = new StringBuilder();
        regexedTerm = regexedTerm.append("\\b").append(Pattern.quote(term)).append("\\b").append("(?![^<>]*+([>]|</))");
        match = Pattern.compile(regexedTerm.toString(), 2).matcher(document_text).find();
        if(!match)
        {
            term = Util.getShort(entity, false);
            regexedTerm = new StringBuilder();
            regexedTerm = regexedTerm.append("\\b").append(Pattern.quote(term)).append("\\b").append("(?![^<>]*+([>]|</))");
            match = Pattern.compile(regexedTerm.toString(), 2).matcher(document_text).find();
        }
        if(term.trim().isEmpty())
            match = false;
        return match;
    }

    private static StringBuilder commonTripletoRdfa(CommonTriple commonTriple)
        throws Exception
    {
        StringBuilder returnTriples = new StringBuilder();
        StringBuilder propertyRDFa = new StringBuilder();
        StringBuilder objRdfa = new StringBuilder();
        String predicate = Util.getPrefixed(commonTriple.getPredicate());
        OWLObject object = commonTriple.getObject();
        if(object instanceof OWLLiteral)
        {
            OWLLiteral literal = (OWLLiteral)object;
            propertyRDFa.append("property=").append(Util.quote((new StringBuilder()).append("").append(predicate).toString()));
            objRdfa.append("content=").append(Util.quote(literal.getLiteral())).append(" datatype=").append(Util.quote(literal.getDatatype().toString()));
            if(!literal.getLang().trim().isEmpty())
                objRdfa.append(" xml:lang=").append(Util.quote(literal.getLang()));
        }
        if(object instanceof OWLNamedIndividualImpl)
        {
            OWLNamedIndividual individual = (OWLNamedIndividual)object;
            propertyRDFa.append("rel=").append(Util.quote((new StringBuilder()).append("").append(predicate).toString()));
            objRdfa.append("resource=").append(Util.quote((new StringBuilder()).append("").append(Util.getPrefixed(individual)).toString()));
        }
        
        
        returnTriples.append(LESSTHANSIGN).append(SPAN).append(" id='").append
                (MetaAnnotator.getNextId()).append("' ").append(propertyRDFa).append(" ").append(objRdfa).append(">").append(SPAN_CLOSE);
        return returnTriples;
    }

    
}
