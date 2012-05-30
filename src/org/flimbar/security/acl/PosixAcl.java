/*
 * PosixAcl.java
 *
 * Created on 19 January 2006, 11:01
 *
 */

package org.flimbar.security.acl;

import java.util.*;
import java.security.acl.*;
import java.security.Principal;

/**
 *
 * @author nrs
 */
public class PosixAcl extends HashSet<AclEntry> implements Acl {
    
    private final Qualifier owner;
    
    /** Creates a new instance of PosixAcl */
    public PosixAcl(Qualifier owner) {
        this.owner = owner;
    }

    /**
     * Checks whether or not the specified principal has the specified 
     * permission. If it does, true is returned, otherwise false is returned.
     * 
     * More specifically, this method checks whether the passed permission
     * is a member of the allowed permission set of the specified principal.
     * The allowed permission set is determined by the same algorithm as is 
     * used by the <code>getPermissions</code> method.
     * 
     * @param principal the principal, assumed to be a valid authenticated 
     * Principal.
     * 
     * @param permission the permission to be checked for.
     * 
     * @return true if the principal has the specified permission, false 
     * otherwise.
     * 
     * @see #getPermissions
     */
    public boolean checkPermission(Principal principal, Permission permission) {
        return false;
    }

    /**
     * Returns true if the given principal is an owner of the ACL.
     * 
     * @param owner the principal to be checked to determine whether or not 
     * it is an owner.
     * 
     * @return true if the passed principal is in the list of owners, false 
     * if not.
     */
    public boolean isOwner(Principal owner) {
        return this.owner.equals(owner);
    }

    /**
     * Returns an enumeration for the set of allowed permissions for the 
     * specified principal (representing an entity such as an individual or 
     * a group). This set of allowed permissions is calculated as
     * follows:<p>
     * 
     * <ul>
     *  
     * <li>If there is no entry in this Access Control List for the 
     * specified principal, an empty permission set is returned.<p>
     * 
     * <li>Otherwise, the principal's group permission sets are determined.
     * (A principal can belong to one or more groups, where a group is a 
     * group of principals, represented by the Group interface.)
     * The group positive permission set is the union of all 
     * the positive permissions of each group that the principal belongs to.
     * The group negative permission set is the union of all 
     * the negative permissions of each group that the principal belongs to.
     * If there is a specific permission that occurs in both 
     * the positive permission set and the negative permission set, 
     * it is removed from both.<p>
     * 
     * The individual positive and negative permission sets are also 
     * determined. The positive permission set contains the permissions 
     * specified in the positive ACL entry (if any) for the principal. 
     * Similarly, the negative permission set contains the permissions
     * specified in the negative ACL entry (if any) for the principal. 
     * The individual positive (or negative) permission set is considered 
     * to be null if there is not a positive (negative) ACL entry for the
     * principal in this ACL.<p>
     * 
     * The set of permissions granted to the principal is then calculated 
     * using the simple rule that individual permissions always override 
     * the group permissions. That is, the principal's individual negative
     * permission set (specific denial of permissions) overrides the group 
     * positive permission set, and the principal's individual positive 
     * permission set overrides the group negative permission set. 
     * 
     * </ul>
     * 
     * @param user the principal whose permission set is to be returned.
     * 
     * @return the permission set specifying the permissions the principal 
     * is allowed. 
     */
    public Enumeration<Permission> getPermissions(Principal user) {
        AclEntry entry = null;
        
        if (isOwner(user)) {
            
        }
        return null;
    }

    /**
     * Sets the name of this ACL.
     * 
     * @param caller the principal invoking this method. It must be an
     * owner of this ACL.
     * 
     * @param name the name to be given to this ACL.
     * 
     * @exception NotOwnerException if the caller principal
     * is not an owner of this ACL.  
     * 
     * @see #getName
     */
    public void setName(java.security.Principal caller, String name) throws NotOwnerException {
    }

    /**
     * Removes an ACL entry from this ACL.
     * 
     * @param caller the principal invoking this method. It must be an
     * owner of this ACL.
     *  
     * @param entry the ACL entry to be removed from this ACL.
     * 
     * @return true on success, false if the entry is not part of this ACL.
     * 
     * @exception NotOwnerException if the caller principal is not
     * an owner of this Acl.
     */
    public boolean removeEntry(java.security.Principal caller, AclEntry entry) throws NotOwnerException {
        return false;
    }

    /**
     * Adds an ACL entry to this ACL. An entry associates a principal
     * (e.g., an individual or a group) with a set of
     * permissions. Each principal can have at most one positive ACL
     * entry (specifying permissions to be granted to the principal)
     * and one negative ACL entry (specifying permissions to be
     * denied). If there is already an ACL entry of the same type
     * (negative or positive) already in the ACL, false is returned.
     * 
     * @param caller the principal invoking this method. It must be an
     * owner of this ACL.
     * 
     * @param entry the ACL entry to be added to this ACL.
     * 
     * @return true on success, false if an entry of the same type
     * (positive or negative) for the same principal is already
     * present in this ACL.
     * 
     * @exception NotOwnerException if the caller principal
     *  is not an owner of this ACL.  
     */
    public boolean addEntry(java.security.Principal caller, AclEntry entry) throws NotOwnerException {
        return false;
    }

    /**
     * Returns the name of this ACL. 
     * 
     * @return the name of this ACL.
     * 
     * @see #setName
     */
    public String getName() {
        return null;
    }

    /**
     * Returns an enumeration of the entries in this ACL. Each element in 
     * the enumeration is of type AclEntry.
     * 
     * @return an enumeration of the entries in this ACL.
     */
    public java.util.Enumeration<AclEntry> entries() {
        return null;
    }

    /**
     * 
     * Deletes an owner. If this is the last owner in the ACL, an exception is 
     * raised.<p>
     * 
     * The caller principal must be an owner of the ACL in order to invoke 
     * this method. 
     * 
     * @param caller the principal invoking this method. It must be an owner 
     * of the ACL.
     * 
     * @param owner the owner to be removed from the list of owners.
     * 
     * @return true if the owner is removed, false if the owner is not part 
     * of the list of owners.
     * 
     * @exception NotOwnerException if the caller principal is not an owner 
     * of the ACL.
     * 
     * @exception LastOwnerException if there is only one owner left, so that
     * deleteOwner would leave the ACL owner-less.
     */
    public boolean deleteOwner(java.security.Principal caller, java.security.Principal owner) throws NotOwnerException, LastOwnerException {
        return false;
    }

    /**
     * Adds an owner. Only owners can modify ACL contents. The caller 
     * principal must be an owner of the ACL in order to invoke this method.
     * That is, only an owner can add another owner. The initial owner is 
     * configured at ACL construction time. 
     * 
     * @param caller the principal invoking this method. It must be an owner 
     * of the ACL.
     * 
     * @param owner the owner that should be added to the list of owners.
     * 
     * @return true if successful, false if owner is already an owner.
     * @exception NotOwnerException if the caller principal is not an owner 
     * of the ACL.
     */
    public boolean addOwner(java.security.Principal caller, java.security.Principal owner) throws NotOwnerException {
        return false;
    }
    
}
