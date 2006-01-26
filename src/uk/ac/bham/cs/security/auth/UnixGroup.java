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
        
    private String password;
    private long gid;
    private Set<String> members;
    
    public UnixGroup(String name, String password, long gid,
            String[] names) {
        super(name);
        this.setPassword(password);
        this.setGid(gid);
        
        this.setMembers(new HashSet<String>());
        for (String n : names) {
            this.getMembers().add(n.trim());
        }
    }
    
    public UnixGroup(String name, String password, long gid, String members) {                                
        this(name, password, gid, members.trim().split(","));
    }    
            
    public Set<String> getMembers() {
        return members;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + getName() + "\n");
        sb.append("Password: " + getPassword() + "\n");
        sb.append("Gid: " + getGid() + "\n");
        sb.append("Members:");
        for (String member : members) {
            sb.append(" " + member + ",");
        }
        int indexOfLastComma = sb.lastIndexOf(",");
        if (indexOfLastComma != -1) {
            sb.deleteCharAt(indexOfLastComma);
        }
        sb.append("\n");
        return sb.toString();
    }
}
