/*
 * PosixPermission.java
 *
 * Created on 19 January 2006, 11:06
 *
 */

package uk.ac.bham.cs.security.acl;

import java.security.acl.Permission;

/**
 *
 * @author nrs
 */
public enum PosixPermission implements Permission {
    
    READ, WRITE, EXECUTE;            
}
