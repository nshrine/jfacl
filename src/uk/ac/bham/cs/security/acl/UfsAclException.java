/*
 * UfsAclException.java
 *
 * Created on January 21, 2003, 11:37 AM
 */

package uk.ac.bham.cs.security.acl;

import java.util.Hashtable;
import java.io.IOException;
/**
 *
 * @author  nrs
 */
public class UfsAclException extends IOException {
    
    /* acl check errors */
    private static final Hashtable errmsg = new Hashtable();
    static {
        errmsg.put("CHK1", "There is more than 1 Group Owner entry.");
        errmsg.put("CHK2", "There is more than 1 Owner entry.");
        errmsg.put("CHK3", "There is more than 1 Other entry.");
        errmsg.put("CHK4", "There is more than 1 Mask entry.");
        errmsg.put("CHK5", "There is a duplicate User or Group entry.");
        errmsg.put("CHK6", "There is a required Owner, Group Owner, Mask or Other entry missing.");
        errmsg.put("CHK7", "The system cannot allocate any memory.");
        errmsg.put("CHK8", "The entry type is invalid.");
        errmsg.put("ACL2", "No such file or directory.");
        errmsg.put("ACL13", "Permission denied.");
        errmsg.put("ACL95", "Operation not supported\n\n"
                + "Either your kernel does not support acls\n"
                + "or the filesystem is not mounted with\n"
                + "the \"acl\" option.\n");               
    }
    
    /**
     * Creates a new instance of <code>UfsAclException</code> without detail message.
     */
    public UfsAclException() {
    }
    
    /**
     * Constructs an instance of <code>UfsAclException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UfsAclException(String msg) {        
        super(msg);
    }
    
    public String getMessage() {        
        String message = (String) errmsg.get(super.getMessage());
        
        if (message != null) {
            return message;
        }
        
        return super.getMessage();
    }
}
