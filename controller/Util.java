package controller;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

public class Util {
    
    private static String quote = "\"";
    public static NamespaceUtil NAMESPACES;
    private static String COLLON = ":";
    private static String XMLNS = "xmlns";
    private static String EQUAL = "=";
    private static String RESTRICTION[] = {
        ".pdf", ".png", ".gif", ".jpg", "skos:", "rdf:", "&", "rdfs:", "owl:", "xsd:",
        ".htm", ".html", ".php", ".asp", ".cfm", ".JPG", "upload.wikimedia", ".PNG"
    };
    private static String BREAK_LINE_HTML = " <br /> ";
    private static String BREAK_LINE_HTML_REGEX_PATTERN = "(\\s*<[Bb][Rr]\\s*/?>)+\\s*$";
    public  static String DCPREFIX = ""; 

    public static void saveTextInFile(String text, File file){
        
        try {

            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(text);
            out.close();
            
        } catch (Exception e) {

            getException(e);

        }
        
    }

    public Util()
    {
    }

    public static boolean hasFileExtension(String s)
    {
        return s.matches("^[\\w\\d\\:\\/\\.]+\\.\\w{3,4}(\\?[\\w\\W]*)?$");
    }

    public static boolean isRestriction(String URI)
    {
        return StringUtils.indexOfAny(URI, RESTRICTION) != -1;
    }

    public static String getCURIE(String URI)
    {
        return (new StringBuilder()).append("[").append(URI).append("]").toString();
    }

    public static String getXMLNS()
        throws Exception
    {
        StringBuilder xmlNS = new StringBuilder();
        String prefix;
        String ns;
        boolean hasDcPrefix = false;
        
        Set<Entry<String, String>> entrySet = getNamespace().getNamespace2PrefixMap().entrySet();
        
        for(Iterator nsMap = entrySet.iterator(); nsMap.hasNext(); xmlNS.append(XMLNS).append(COLLON).append(prefix).append(EQUAL).append(quote(ns)).append(" ")){

            java.util.Map.Entry entry = (java.util.Map.Entry)nsMap.next();
            prefix = (String)entry.getValue();
            ns = (String)entry.getKey();
            if(ns.isEmpty()){
                ns = (new StringBuilder()).append("http://ime.eb.br/").append(prefix).append("/").toString();
            }
            if(ns.toLowerCase().contains("http://purl.org/dc/elements/1.1/")){
                DCPREFIX = prefix;
                hasDcPrefix = true;
            }
                
        }
        
        if("".equals(DCPREFIX)){
            DCPREFIX = "dublincorens";
        }
        
        if(hasDcPrefix == false){
            
            xmlNS.append("xmlns:").append(DCPREFIX).append("=").append(quote("http://purl.org/dc/elements/1.1/"));
        }
            
        

        return xmlNS.toString();
    }

    public static void fixUnNamespaced(String prefix, NamespaceUtil ns)
    {
        String setNs = "http://ime.eb.br/auto";
        String setPrefix = prefix;
        if(prefix.contains("owl:"))
        {
            setNs = Namespaces.OWL.toString();
            setPrefix = "owl";
        }
        if(prefix.contains("rdf:"))
        {
            setNs = Namespaces.RDF.toString();
            setPrefix = "rdf";
        }
        if(prefix.contains("rdfs:"))
        {
            setNs = Namespaces.RDFS.toString();
            setPrefix = "rdfs";
        }
        if(prefix.contains("xsd:"))
        {
            setNs = Namespaces.XSD.toString();
            setPrefix = "xsd";
        }
        if(prefix.contains("dc:"))
        {
            setNs = "http://purl.org/dc/elements/1.1/";
            setPrefix = "dc";
        }
        if(prefix.contains("skos:"))
        {
            setNs = Namespaces.SKOS.toString();
            setPrefix = "skos";
        }
        NAMESPACES.setPrefix(setNs, setPrefix);
    }

    public static NamespaceUtil getNamespace()
    {
        if(NAMESPACES == null)
            NAMESPACES = new NamespaceUtil();
        return NAMESPACES;
    }

    public static String removeExtension(String file)
    {
        String separator = System.getProperty("file.separator");
        String filename = file;
        int extensionIndex = filename.lastIndexOf(".");
        if(extensionIndex == -1)
            return filename;
        else
            return filename.substring(0, extensionIndex);
    }

    public static File writeTextInFile(String text, String pathName)
    {
        File file = null;
        try
        {
            file = new File(pathName);
            if(!file.exists())
                file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(text);
            out.close();
        }
        catch(Exception e)
        {
            getException(e);
        }
        return file;
    }

    public static String quote(String string)
    {
        return (new StringBuilder(quote)).append(StringEscapeUtils.escapeXml(string)).append(quote).toString();
    }

    public static String getPrefixed(OWLObject object)
        throws Exception
    {
        String shorty = object.toString();
        if(object instanceof OWLEntity)
        {
            OWLEntity entity = (OWLEntity)object;
            ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
            shorty = shortFormProvider.getShortForm(entity);
        }
        if(object instanceof OWLLiteral)
        {
            OWLLiteral literal = (OWLLiteral)object;
            shorty = literal.getLiteral();
        }
        String uri = object.toString();
        if(StringUtils.indexOfAny(uri, RESTRICTION) != -1)
        {
            shorty = object.toString().replace("<", "").replace(">", "");
            if(shorty.contains(":") && !shorty.contains("http"))
                fixUnNamespaced(shorty, getNamespace());
            else
                shorty = getCURIE(shorty);
        } else
        if(!shorty.isEmpty())
        {
            String uriNamespace = uri.replace(shorty, "").replace("<", "").replace(">", "");
            String XMLprefix = getNamespace().getPrefix(uriNamespace);
            shorty = (new StringBuilder()).append(XMLprefix).append(COLLON).append(shorty).toString();
        }
        return shorty;
    }

    public static String getShort(OWLObject object, boolean decompose)
    {
        String shorty = object.toString();
        try
        {
            if(object instanceof OWLEntity)
            {
                OWLEntity entity = (OWLEntity)object;
                ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
                shorty = shortFormProvider.getShortForm(entity);
            }
            if(object instanceof OWLLiteral)
            {
                OWLLiteral literal = (OWLLiteral)object;
                shorty = literal.getLiteral();
            }
            if(decompose)
                shorty = decompose(shorty);
        }
        catch(Exception e)
        {
            getException(e);
        }
        return shorty;
    }

    public static String documentToString(String document)
        throws Exception
    {
        BufferedReader reader = new BufferedReader(new FileReader(document));
        StringBuilder textoOriginal = new StringBuilder();
        String text = null;
        while((text = reader.readLine()) != null)
        {
            text = StringEscapeUtils.escapeXml(text);
            textoOriginal.append(text).append(BREAK_LINE_HTML);
        }
        return textoOriginal.toString();
    }

    public static String decompose(String token) {
        String retorno = token;
        try {
            if (retorno.contains("_")) {
                retorno = retorno.replace("_", " ");
            } else {
                retorno = token.replaceAll(
                        String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"),
                        " ").trim();
            }
        } catch (Exception e) {
            getException(e);
        }
        return retorno;
    }

    public static void getException(Exception e)
    {
        if(e instanceof InconsistentOntologyException)
        {
            InconsistentOntologyException x = (InconsistentOntologyException)e;
            System.out.println("***Inconsistent Ontology-----------");
            x.getMessage();
            System.out.println("-----------");
        }
        System.out.println("-----------");
        System.out.println((new StringBuilder()).append("Exception Localized Message: ").append(e.getLocalizedMessage()).toString());
        System.out.println((new StringBuilder()).append("Exception Type: ").append(e.getClass()).toString());
        System.out.println((new StringBuilder()).append("Exception Message: ").append(e.getMessage()).toString());
        StackTraceElement arr$[] = e.getStackTrace();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            StackTraceElement stack = arr$[i$];
            System.out.println((new StringBuilder()).append("    Exception Stack Trace: ").append(stack.getMethodName()).toString());
        }

        System.out.println((new StringBuilder()).append("Method Name: ").append(e.getStackTrace()[0].getMethodName()).toString());
        System.out.println((new StringBuilder()).append("Method Thread Name: ").append(Thread.currentThread().getStackTrace()[3].getMethodName()).toString());
        System.out.println("-----------");
        StringWriter stringWriter = new StringWriter();
        String stackTrace = null;
        e.printStackTrace(new PrintWriter(stringWriter));
        stackTrace = stringWriter.toString();
        System.out.println(stackTrace);
    }

    public static String fileToUrl(String file)
    {
        String urlFile = "";
        File fileName = new File(file);
        URI uri = null;
        try
        {
            uri = fileName.toURI();
            urlFile = uri.toString();
        } catch (Exception e) {
            getException(e);
        }
        return urlFile;
    }

    public void teste(OWLOntology owlontology) {
    }

    public static File textToTempFile(String text) {
        try {
            File temp = File.createTempFile("document", ".htm");
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
            out.write(text);
            out.close();
            return temp;
        } catch (Exception e) {
            getException(e);
        }
        return null;
    }

    public static String htmlToText(String html) {

        String noBr   = html.replace(BREAK_LINE_HTML, System.getProperty("line.separator"));
        String noHtml = noBr.replaceAll("<[^>]+>","");
        noHtml =  StringEscapeUtils.unescapeXml(noHtml);
        noHtml =  StringEscapeUtils.escapeXml(noHtml);
        
        String TextWithBr = noHtml.replace(System.getProperty("line.separator"), BREAK_LINE_HTML);
        
        return TextWithBr;

    }
    

    public static void openURLInBrowser(URI URI) {

        if (!java.awt.Desktop.isDesktopSupported()) {

            System.err.println("Desktop is not supported (fatal)");
            System.exit(1);
        }

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {

            System.err.println("Desktop doesn't support the browse action (fatal)");
            System.exit(1);
        }

        try {

            desktop.browse(URI);
            
        } catch (Exception e) {

            System.err.println(e.getMessage());
        }

    }

    public static String addFileExtIfNecessary(String file, String ext) {
        
        char dot = '.';
        
        if (file.lastIndexOf(dot) == -1) {
            file += dot+ext;
        }

        return file;
    }
    
    public static String getUserName() throws Exception {

        String      username = null;

        Class<?>    c        = null;
        Object      o        = null;
        Method      method   = null;

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {

            c = Class.forName("com.sun.security.auth.module.NTSystem");

            o = Class.forName("com.sun.security.auth.module.NTSystem").newInstance();

            method = c.getDeclaredMethod("getName");

        }

        if (System.getProperty("os.name").toLowerCase().contains("linux")) {

            c = Class.forName("com.sun.security.auth.module.UnixSystem");

            o = Class.forName("com.sun.security.auth.module.UnixSystem").newInstance();

            method = c.getDeclaredMethod("getUsername");

        }

        if (System.getProperty("os.name").toLowerCase().contains("solaris")) {

            c = Class.forName("com.sun.security.auth.module.SolarisSystem");

            o = Class.forName("com.sun.security.auth.module.SolarisSystem").newInstance();

            method = c.getDeclaredMethod("getUsername");

        }

        if (c != null) {
            Object invoke   = method.invoke(o);
            if(invoke != null)
                username = invoke.toString();
        }

        return username;

    }
    
    public static String formatXML(String xml) {

        try {
            final InputSource src = new InputSource(new StringReader(xml));
            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            final Boolean keepDeclaration = Boolean.valueOf(xml.startsWith("<?xml"));

            System.setProperty(DOMImplementationRegistry.PROPERTY,"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");


            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration); // Set this to true if the declaration is needed to be outputted.

            return writer.writeToString(document);
            
        } catch (Exception e) {
            Util.getException(e);
            return null;
        }
    }
    


}