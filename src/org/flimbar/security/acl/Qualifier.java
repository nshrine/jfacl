/*
 * Qualifier.java
 *
 * Created on 19 January 2006, 11:39
 *
 */

package org.flimbar.security.acl;

import java.security.Principal;

/**
 *
 * @author nrs
 */
public class Qualifier implements Principal {
    
    private String name;
    
    /** Creates a new instance of Qualifier */
    public Qualifier(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this principal.
     * 
     * @return the name of this principal.
     */
    public String getName() {
        return name;
    }
    
    public boolean equals(Object other) {
        if (!(other instanceof Qualifier)) {
            return false;
        } else {
            return ((Qualifier) other).getName().equals(name);
        }
    }
}
