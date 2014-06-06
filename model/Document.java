/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author celso
 */
public class Document {

    private volatile static Document singleton;

    

    private Document() {
    }

    public static Document getSingleton() {

        if (singleton == null) {

            synchronized (Document.class) {
                if (singleton == null) {
                    singleton = new Document();
                }
            }
        }
        return singleton;
    }
}
