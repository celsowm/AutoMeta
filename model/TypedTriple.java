/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Set;

// Referenced classes of package model:
//            Triple
public class TypedTriple extends Triple {
    
    private Set types;

    @Override
    public String toString() {
        return getTypes().toString();
    }

    public TypedTriple(Set types) {
        this.types = types;
    }

    public TypedTriple() {
    }

    public Set getTypes() {
        return types;
    }

    public void setTypes(Set types) {
        this.types = types;
    }
    
}
