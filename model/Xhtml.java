/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import controller.Util;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author celso
 */
public class Xhtml {

    static String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    static String docType = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\""
            + " \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\">";

    static final String xmlnsDefault = "http://www.w3.org/1999/xhtml";

    static final String htmlTag = "html";
    
    public static String getCreatorMeta(){
        
        String creator = null;
        
        String dc = Util.DCPREFIX;
        
        try {
             creator = Util.getUserName(); 
        } catch (Exception e) {
            
        }
        
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        return "<meta property=\""+dc+":creator\" content=\""+creator+"\"></meta>"
                + "<meta property=\""+dc+":date\" content=\""+date+"\"></meta>";
        
    }

    public static String getDocType() {
        return docType;
    }

    public static String getXml() {
        return xml;
    }

    public static String getHTMLWithPrefixs(){
        return "";
    }

}
