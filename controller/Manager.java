// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 10/08/2012 11:13:29
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Manager.java

package controller;

import com.clarkparsia.owlapi.explanation.PelletExplanation;
import com.hp.hpl.jena.sparql.pfunction.library.str;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.AutoAnnotator;
import model.Xhtml;
import org.semanticweb.owlapi.model.OWLEntity;

// Referenced classes of package controller:
//            Util, OntologyManager, Annotator, Analiser

public class Manager
{

    public Manager()
    {
    }

    public static String getDocumentoAnotado(AutoAnnotator autoAnnotator)
        throws Exception
    {
        
        MetaAnnotator.resetId();
        
        String document_mod = "";
        PelletExplanation.setup();
        String document_text = autoAnnotator.getDocumentText();
        document_mod = document_text;
        
        for(Iterator entitiesIt = autoAnnotator.getOntology().getSignature(true).iterator(); entitiesIt.hasNext();)
        {
            OWLEntity owlEntity = (OWLEntity)entitiesIt.next();
            document_mod = Annotator.annotate(owlEntity, document_mod, autoAnnotator);
        }

        String defaultXmlns = "xmlns=\"http://www.w3.org/1999/xhtml\"";
        String version = " version=\"XHTML+RDFa 1.0\" ";
        String prefixs = Util.getXMLNS();
        
        document_mod = (new StringBuilder()).append(
                    Xhtml.getXml())
                    .append("<html ")
                        .append(defaultXmlns)
                        .append(" ")
                        .append(prefixs)
                        .append(" ")
                        .append(version)
                    .append(">")
                    //.append("<head>")
                    //.append(Xhtml.getCreatorMeta())
                    //.append("</head>")
                    .append("<body>")
                        .append(document_mod)
                    .append("</body>")
                    .append("</html>").toString();
        return document_mod;
    }
}
