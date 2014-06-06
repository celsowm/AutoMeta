/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import uk.ac.manchester.cs.factplusplus.owlapiv3.*;

/**
 *
 * @author fontesc
 */
public class FactPlusPlusReasonerImplementation extends AbstractReasonerImplementation {

    @Override
    public OWLReasoner getReasoner(OWLOntology ontology) {

        if (reasoner == null || !(reasoner instanceof FaCTPlusPlusReasoner)) {
            this.setup();
            reasoner = new FaCTPlusPlusReasonerFactory().createReasoner(ontology);
            System.out.println("Using " + reasoner.getReasonerName() + " reasoner");
        }
        return reasoner;
    }

    public void setup() {
        
        String os = System.getProperty("os.name").toLowerCase();
        
        String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        //absolutePath = absolutePath.replace("classes/", "");

        System.out.println(absolutePath);

        Runtime.getRuntime().load(absolutePath+"oslibs/FaCTPlusPlusJNI.dll");

        /*final int BUFFER = 2048;

        String os = System.getProperty("os.name").toLowerCase();

        try {
            //System.load("FaCTPlusPlusJNI.dll");
            
            URL path = Manager.class.getProtectionDomain().getCodeSource().getLocation();
            System.out.println("path: " + path);
            
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream("");
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];
                // write the files to the disk
                FileOutputStream fos = new FileOutputStream(entry.getName());
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
        }
*/
    }
    //@Override

}
