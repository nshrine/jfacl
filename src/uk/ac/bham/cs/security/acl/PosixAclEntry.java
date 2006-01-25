/*
 * PosixAclEntry.java
 *
 * Created on 19 January 2006, 11:06
 *
 */

package uk.ac.bham.cs.security.acl;

import java.util.*;
import java.security.acl.*;
import java.security.Principal;
import uk.ac.bham.cs.util.IteratorEnumeration;

/**
 *
 * @author nrs
 */
public class PosixAclEntry implements AclEntry, Cloneable {
    
    public enum TagType { USER_OBJ, USER, GROUP_OBJ, GROUP, MASK, OTHER };
    
    private final TagType type;    
    private final Set<PosixPermission> permissions =
            new HashSet<PosixPermission>(3);
    
    private Principal qualifier;    
    
    public PosixAclEntry(TagType type) {
        this.type = type;
    }
                
    public PosixAclEntry(TagType type, Qualifier qualifier) {
        this.type = type;
        this.qualifier = qualifier;
    }

    public TagType getTagType() {
        return type;
    }
    
    /**
     * Specifies the principal for which permissions are granted or denied
     * by this ACL entry. If a principal was already set for this ACL entry,
     * false is returned, otherwise true is returned.
     * 
     * @param user the principal to be set for this entry.
     * 
     * @return true if the principal is set, false if there was
     * already a principal set for this entry.
     * 
     * @see #getPrincipal
     */
    public boolean setPrincipal(Principal user) {
        boolean notSet = (qualifier == null);
        
        if (notSet) {
            qualifier = user;
        }
        
        return notSet;
    }

    /**
     * Removes the specified permission from this ACL entry.
     * 
     * @param permission the permission to be removed from this entry.
     * 
     * @return true if the permission is removed, false if the
     * permission was not part of this entry's permission set.
     */
    public boolean removePermission(Permission permission) {
        return permissions.remove(permission);
    }

    /**
     * Checks if the specified permission is part of the
     * permission set in this entry.
     * 
     * @param permission the permission to be checked for.
     * 
     * @return true if the permission is part of the
     * permission set in this entry, false otherwise.
     */
    public boolean checkPermission(Permission permission) {
        return permissions.contains(permission);
    }

    /**
     * Adds the specified permission to this ACL entry. Note: An entry can
     * have multiple permissions.
     * 
     * @param permission the permission to be associated with
     * the principal in this entry.
     * 
     * @return true if the permission was added, false if the
     * permission was already part of this entry's permission set.
     */
    public boolean addPermission(Permission permission) {
        boolean result = (permission instanceof PosixPermission);
        
        if (result) {
            result = permissions.add((PosixPermission) permission);
        }
        
        return result;
    }

    /**
     * Sets this ACL entry to be a negative one. That is, the associated
     * principal (e.g., a user or a group) will be denied the permission set
     * specified in the entry.
     * 
     * Note: ACL entries are by default positive. An entry becomes a
     * negative entry only if this <code>setNegativePermissions</code>
     * method is called on it.
     */
    public void setNegativePermissions() {
        System.err.println("POSIX does not have negative permissions");
    }

    /**
     * Returns an enumeration of the permissions in this ACL entry.
     * 
     * @return an enumeration of the permissions in this ACL entry.
     */
    public Enumeration<Permission> permissions() {
        return new IteratorEnumeration<PosixPermission>(permissions.iterator());        
    }

    /**
     * Returns true if this is a negative ACL entry (one denying the
     * associated principal the set of permissions in the entry), false
     * otherwise.
     * 
     * @return true if this is a negative ACL entry, false if it's not.
     */
    public boolean isNegative() {
        return false;
    }

    /**
     * Returns the principal for which permissions are granted or denied by
     * this ACL entry. Returns null if there is no principal set for this
     * entry yet.
     * 
     * @return the principal associated with this entry.
     * 
     * @see #setPrincipal
     */
    public Principal getPrincipal() {
        return qualifier;
    }    
    
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
	return clone;
    }
    
    /**
     * Acl entries are equal (i.e. must be unique) if the tag types are equal
     * and they are one of the types for which there can only be one entry i.e.
     * not USER or GROUP type or if they are a USER or GROUP entry with the
     * same qualifier.
     */
    public boolean equals(Object other) {
        boolean areEqual = false;
        
        if (other instanceof PosixAclEntry) {
            PosixAclEntry entry = (PosixAclEntry) other;
            if (entry.getTagType() == type) {
                areEqual = (((type != TagType.USER) && (type != TagType.GROUP))
                        || (entry.getPrincipal().equals(qualifier)));
            }                
        }
        
        return areEqual;
    }
}
