/*
 * UnixGroup.java
 *
 * Created on 24 January 2006, 13:52
 *
 */

package uk.ac.bham.cs.security.auth;

import java.util.*;
import com.sun.security.auth.UnixPrincipal;

/**
 *
 * @author nrs
 */
public class UnixGroup extends UnixPrincipal {
    
    static {
        System.loadLibrary("posix");
    }
    
    
    private String password;
    private long gid;
    private Set<String> members;
    
    private UnixGroup(String name, String password, long gid, String members) {
        super(name);
        
        this.members = new HashSet<String>();
        String[] names = members.trim().split(",");
        for (String n : names) {
            this.members.add(n.trim());
        }
    }    
            
    public Set<String> getMembers() {
        return members;
    }
}
