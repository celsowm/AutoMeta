/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author fontesc
 */
public class ReasonerFactory {
    
    private static volatile AbstractReasonerImplementation singleton;
    private static String REASONER_NAME = "";
    
    private ReasonerFactory(){}
    
    public static AbstractReasonerImplementation getSingleton(String reasonerName) {
        // needed because once there is singleton available no need to acquire
        // monitor again & again as it is costly
        if (singleton == null || !(REASONER_NAME.equals(reasonerName))) {

            System.out.println("start..................");
            
                //System.out.println("s"+REASONER_NAME);
            
            if ("pellet".equals(reasonerName)) {

                synchronized (PelletReasonerImplementation.class) {
                    // this is needed if two threads are waiting at the monitor at the
                    // time when singleton was getting instantiated
                    if (singleton == null || !(REASONER_NAME.equals(reasonerName))) {
                        singleton = new PelletReasonerImplementation();
                    }
                }

            }
            
            if ("hermit".equals(reasonerName)) {
                
               //System.out.println("entrou hermit");

                synchronized (HermitReasonerImplementation.class) {
                    // this is needed if two threads are waiting at the monitor at the
                    // time when singleton was getting instantiated
                    if (singleton == null || !(REASONER_NAME.equals(reasonerName))) {
                        singleton = new HermitReasonerImplementation();
                    }
                }

            }
            
            if ("fact++".equals(reasonerName)) {

                synchronized (FactPlusPlusReasonerImplementation.class) {
                    // this is needed if two threads are waiting at the monitor at the
                    // time when singleton was getting instantiated
                    if (singleton == null || !(REASONER_NAME.equals(reasonerName))) {
                        singleton = new FactPlusPlusReasonerImplementation();
                    }
                }

            }
            
            REASONER_NAME = reasonerName;
            
            //System.out.println("e"+REASONER_NAME);
            
        }
        return singleton;
    }
    
}
