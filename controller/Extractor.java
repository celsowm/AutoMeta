/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import model.ExtractedTriple;
import net.rootdev.javardfa.ParserFactory;
import net.rootdev.javardfa.ParserFactory.Format;
import net.rootdev.javardfa.StatementSink;
import net.rootdev.javardfa.uri.URIResolver;
import org.xml.sax.XMLReader;

/**
 *
 * @author celso
 */
public class Extractor {

    private static String myBase      = "";
    private static String myPrefix    = "";
    private static String myUri       = "";
    
    public static StatementSink getSink(final List<ExtractedTriple> triples){

        StatementSink sink = new StatementSink() {

            public void setBase(String base) {

                myBase = base.replace("///", "/");

                //System.out.println(base);
                //System.out.println(myBase);

            }

            public void start() {}

            public void end() {}

            public void addPrefix(String prefix, String uri) {

                myPrefix = prefix;
                myUri    = uri;

            }

            public void addObject(String subject, String predicate, String object) {

                ExtractedTriple t = new ExtractedTriple();

                t.s = subject;
                t.p = predicate;
                t.o = object;

                triples.add(t);
            }

            public void addLiteral(String subject, String predicate, String lex, String lang, String datatype) {
                ExtractedTriple t = new ExtractedTriple();
                t.s = subject;
                t.p = predicate;
                t.o = lex;
                triples.add(t);
            }
        };
        
        return sink;
        
        
    }
    
    
    public static void rdfaToRDF(File rdfaFile) throws Exception{
        
        Class.forName("net.rootdev.javardfa.jena.RDFaReader");

        Model model = ModelFactory.createDefaultModel();

        model.read(rdfaFile.getCanonicalPath(), "XHTML");

        FileOutputStream fout = new FileOutputStream(
                "C:\\rdf.rdf");
        model.write(fout);

    }

    public static void rdfaToRDF(String rdfaText, File rdf) throws Exception {

        Class.forName("net.rootdev.javardfa.jena.RDFaReader");

        Model model = ModelFactory.createDefaultModel();

        File rdfaFile = Util.textToTempFile(rdfaText);
        
        FileOutputStream fout = new FileOutputStream(
                rdf.getCanonicalPath());

        try {
            
            model.read(rdfaFile.getCanonicalPath(), "XHTML");
            
        } catch (RuntimeException e) {
            
            if(e.getMessage().contains("SAX Error when parsing")){
                
                System.out.println("Parsing XHTML Error, verify the Syntax");
                
            }
            
        }
        
        model.write(fout);

    }

    


    public static TableModel parseRDFaToTableModel(String rdfaFile) throws Exception {




        final List<ExtractedTriple> triples = new ArrayList<ExtractedTriple>();

        StatementSink sink = new StatementSink() {

            public void setBase(String base) {

                myBase = base.replace("///", "/");

                //System.out.println(base);
                //System.out.println(myBase);

            }

            public void start() {}

            public void end() {}

            public void addPrefix(String prefix, String uri) {

                myPrefix = prefix;
                myUri    = uri;

            }

            public void addObject(String subject, String predicate, String object) {

                ExtractedTriple t = new ExtractedTriple();

                t.s = subject;
                t.p = predicate;
                t.o = object;

                triples.add(t);
            }

            public void addLiteral(String subject, String predicate, String lex, String lang, String datatype) {
                ExtractedTriple t = new ExtractedTriple();
                t.s = subject;
                t.p = predicate;
                t.o = lex;
                triples.add(t);
            }
        };



        XMLReader reader = ParserFactory.createReaderForFormat(sink, Format.XHTML, new URIResolver());
        reader.parse(rdfaFile);


        Iterator<ExtractedTriple> it = triples.iterator();

        while (it.hasNext()) {
            
            ExtractedTriple triple = it.next();

        }

        // A table model wrapper around the statement array
        TableModel tm = new AbstractTableModel() {

            @Override
            public String getColumnName(int column) {
                switch(column) {
                    case 0 : return "Subject";
                    case 1 : return "Property";
                    case 2 : return "Object";
                }
                return null;
            }

            public int getRowCount() {
                return triples.size();
            }

            public int getColumnCount() {
                return 3;
            }

            public Object getValueAt(int row, int column) {
                switch (column) {
                    case 0:
                        return triples.get(row).s.replace(myBase, "");
                    case 1:
                        return triples.get(row).p.replace(myUri, "");
                    case 2:
                        return triples.get(row).o.replace(myUri, "").replace(myBase, "");
                }
                return null; // if I get here something went wrong
            }
        };


        return tm;


    }

    public static TableModel getTriplesFromRdfa(String rdfa) throws Exception {

        FileInputStream fstream = new FileInputStream(rdfa);


        // Init java-rdfa in jena
        Class.forName("net.rootdev.javardfa.jena.RDFaReader");
        Model model = ModelFactory.createDefaultModel();

        // Read RDFa
        model.read(rdfa, "XHTML");
        

        final Statement[] stmts = new Statement[(int) model.size()];

        // Collect all the triples (statements) in the model in an array, to preserve order
        int i = 0;
        for (StmtIterator si = model.listStatements(); si.hasNext(); i++) {
            stmts[i] = si.next();
        }

        // A table model wrapper around the statement array
        TableModel tm = new AbstractTableModel() {

            public int getRowCount() {
                return stmts.length;
            }

            public int getColumnCount() {
                return 3;
            }

            public Object getValueAt(int row, int column) {
                switch (column) {
                    case 0:
                        return stmts[row].getSubject();
                    case 1:
                        return stmts[row].getPredicate();
                    case 2:
                        return stmts[row].getObject();
                }
                return null; // if I get here something went wrong
            }
        };

        fstream.close();

        return tm;


    }
    
    
    public static void rdfaToRdf(String rdfa, File rdf) throws Exception{
                
        // Init java-rdfa in jena
        Class.forName("net.rootdev.javardfa.jena.RDFaReader");
        Model model = ModelFactory.createDefaultModel();

        System.out.println(rdfa);
        
        // Read RDFa
        model.read(rdfa, "XHTML");
        
        BufferedWriter out = new BufferedWriter(new FileWriter(rdf));
        
        model.write(out);
        out.close();
        
    }
    
}
