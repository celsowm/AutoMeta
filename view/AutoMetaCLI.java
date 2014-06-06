// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 10/08/2012 11:19:27
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AutoMetaCLI.java

package view;

import controller.*;
import java.io.File;
import java.util.Iterator;
import model.AutoAnnotator;
import model.TextFileFilter;
import org.apache.commons.cli.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

public class AutoMetaCLI
{
    
    public static final String ONTOLOGY = "ontology";
    public static final String DOCUMENT = "document";
    public static final String DOCUMENTPATH = "documentpath";
    public static final String OUTPATH = "outpath";
    public static final String REASONING = "reasoning";
    public static final String LABEL = "label";
    public static final String EXHAUSTIVE = "exhaustive";
    public static final String REASONER = "reasoner";
    
    public static final String OUTFORMAT = "outformat";

    public AutoMetaCLI() {
    }

    public static void launch(String s, String s1) {
    }

    static void launchTeste(String args[]) {

        String urlOntology = Util.fileToUrl("c://pizza.owl");
        OWLOntology ontology = OntologyManager.getSingleton().loadOntology(IRI.create(urlOntology));
        PelletReasonerImplementation reasoner = new PelletReasonerImplementation();
        HermitReasonerImplementation reasoner2 = new HermitReasonerImplementation();
        Iterator i = ontology.getSignature(true).iterator();

        try {
            /*while (i.hasNext()) {
                OWLEntity owlEntity = (OWLEntity) i.next();
                System.out.println(reasoner.getTypedTriples(owlEntity, ontology));
            }*/
            
            while (i.hasNext()) {
                OWLEntity owlEntity = (OWLEntity) i.next();
                System.out.println(reasoner2.getTypedTriples(owlEntity, ontology));
            }

        } catch (Exception e) {
        }


        //System.out.println(fileOntology);

    }

    static void launch(String args[])
    {
        try
        {
            
            Options options = new Options();
            options.addOption(ONTOLOGY, true, "Ontology (RDF, OWL) file");
            options.addOption(DOCUMENT, true, "Document (TXT) file");
            options.addOption(DOCUMENTPATH, true, "Path Document (TXT) files");
            options.addOption(OUTPATH, true, "Path for out Document (HTM) files");
            options.addOption(REASONING, true, "Using Reasoner (true or false)");
            options.addOption(LABEL, true, "Using rdfs:label (true or false)");
            options.addOption(EXHAUSTIVE, true, "Replace (annotate) all ocurrences of a term (default false)");
            options.addOption(OUTFORMAT, true, "The format of files generated");
            options.addOption(REASONER, true, "The reasoner name (pellet or hermit)");
            
            CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args);
            boolean reasoning = true;
            boolean label = true;
            boolean exhaustive = false;
            String reasoner = "pellet";
            
            File outFolder = null;
            
            
            if(cmd.hasOption(ONTOLOGY))
            {
                
                if(!cmd.hasOption(DOCUMENT) && !cmd.hasOption(DOCUMENTPATH)){
                    throw new Exception("No document (use -document or -documentpath)");
                }

                String fileOntology = cmd.getOptionValue(ONTOLOGY);
                String urlOntology = Util.fileToUrl(fileOntology);
                OWLOntology ontology = OntologyManager.getSingleton().loadOntology(IRI.create(urlOntology));
                System.out.println(fileOntology);

                if (cmd.hasOption(REASONING)) {
                    System.out.println("Reasoning: "+Boolean.valueOf(cmd.getOptionValue(REASONING)));
                    reasoning = Boolean.valueOf(cmd.getOptionValue(REASONING)).booleanValue();
                }
                if (cmd.hasOption(LABEL)) {
                    System.out.println("Label: "+Boolean.valueOf(cmd.getOptionValue(LABEL)));
                    label = Boolean.valueOf(cmd.getOptionValue(LABEL)).booleanValue();
                }

                if (cmd.hasOption(EXHAUSTIVE)) {
                    System.out.println(Boolean.valueOf(cmd.getOptionValue(EXHAUSTIVE)));
                    exhaustive = Boolean.valueOf(cmd.getOptionValue(EXHAUSTIVE)).booleanValue();
                }
                
                if (cmd.hasOption(REASONER)) {
                    System.out.println("Reasoning: "+String.valueOf(cmd.getOptionValue(REASONER)).toLowerCase());
                    reasoner = String.valueOf(cmd.getOptionValue(REASONER)).toLowerCase();
                }
                
                if (cmd.hasOption(OUTPATH)) {
                    String outpath = cmd.getOptionValue(OUTPATH);
                    outFolder = new File(outpath);
                }else{
                    throw new Exception("No outpath  (use -outpath)");
                }

                if (cmd.hasOption(DOCUMENT)) {
                    String fileDocument = cmd.getOptionValue(DOCUMENT);
                    File file = new File(fileDocument);

                    String htmFile = Util.removeExtension(file.getName()).concat(".htm");
                    if (!(new File(htmFile)).exists()) {
                        
                        String documentText = Util.documentToString(fileDocument);
                        AutoAnnotator autoAnnotator = new AutoAnnotator(documentText, ontology, reasoning, label, exhaustive, reasoner);

                        String result = Manager.getDocumentoAnotado(autoAnnotator);
                        Util.writeTextInFile(result, (new StringBuilder()).append(outFolder.toString()).append(System.getProperty("file.separator")).append(htmFile).toString());
                        
                        
                    }
                }
                
                if(cmd.hasOption(DOCUMENTPATH))
                {
                    String documentpath = cmd.getOptionValue(DOCUMENTPATH);
                    File folder = new File(documentpath);
                   
                        if(folder.isDirectory())
                        {
                            File listOfFiles[] = folder.listFiles(new TextFileFilter());
                            for(int i = 0; i < listOfFiles.length; i++)
                            {
                                File file = listOfFiles[i];
                                String htmFile = Util.removeExtension(file.getName()).concat(".htm");
                                if(!(new File(htmFile)).exists())
                                {
                                    String documentText = Util.documentToString(file.getCanonicalPath());
                                    AutoAnnotator autoAnnotator = new AutoAnnotator(documentText, ontology, reasoning, label, exhaustive, reasoner);
                                    String result = Manager.getDocumentoAnotado(autoAnnotator);
                                    Util.writeTextInFile(result, (new StringBuilder()).append(outFolder.toString()).append(System.getProperty("file.separator")).append(htmFile).toString());
                                }
                            }

                        }
                        System.out.println(documentpath);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Util.getException(e);
        }
    }
}
