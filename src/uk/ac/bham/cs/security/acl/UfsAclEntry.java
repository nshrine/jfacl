/*
 * UfsAclEntry.java
 *
 * Created on January 15, 2003, 2:14 PM
 */

package uk.ac.bham.cs.security.acl;

import java.util.*;
/**
 *
 * @author  nrs
 */
public class UfsAclEntry implements Comparable {
    
    public static final int MAX_ACL_ENTRIES = 1024;
    
    public static final int USER_OBJ = 0x01;		/* object owner */
    public static final int USER = 0x02;		/* additional users */
    public static final int GROUP_OBJ = 0x04;		/* owning group of the object */
    public static final int GROUP = 0x08;		/* additional groups */
    public static final int CLASS_OBJ = 0x10;		/* file group class and mask entry */
    public static final int OTHER_OBJ = 0x20;		/* other entry for the object */
    public static final int ACL_DEFAULT = 0x1000;	/* default flag */
    public static final int DEF_USER_OBJ = (ACL_DEFAULT | USER_OBJ);    /* default object owner */
    public static final int DEF_USER = (ACL_DEFAULT | USER);            /* defalut additional users */
    public static final int DEF_GROUP_OBJ = (ACL_DEFAULT | GROUP_OBJ);  /* default owning group */
    public static final int DEF_GROUP = (ACL_DEFAULT | GROUP);          /* default additional groups */
    public static final int DEF_CLASS_OBJ = (ACL_DEFAULT | CLASS_OBJ);  /* default mask entry */
    public static final int DEF_OTHER_OBJ = (ACL_DEFAULT | OTHER_OBJ);  /* default other entry */
    
    public static final int PRIMARY_MASK = (USER_OBJ | GROUP_OBJ | CLASS_OBJ | OTHER_OBJ);
    public static final int USER_MASK = (USER_OBJ | USER);
    
    /* cmd arg to acl(2) */
    public static final int GETACL = 1;
    public static final int SETACL = 2;
    public static final int GETACLCNT = 3;

    /* minimal acl entries from GETACLCNT */
    public static final int MIN_ACL_ENTRIES = 4;
        
    private static final int USERNAME = 0;
    private static final int REALNAME = 1;
    
    private int a_type;
    private int a_id;
    private String name;
    private UnixPerm a_perm;        
    
    /** Creates a new instance of AclEnt */
    public UfsAclEntry(int type, int id, String name, UnixPerm perm) {
        this.a_type = type;
        this.a_id = id;
        this.a_perm = perm;
        this.name = name;
    }
    
    public UfsAclEntry(int type, int id, String name, int mode) {
            
        this(type, id, name, new UnixPerm(mode));
    }
    
    public UfsAclEntry(int type, int mode) {
        
        this(type, 0, "root", mode);
    }
    
    public int getType() {
        
        return a_type;
    }
    
    public int getId() {
        
        return a_id;
    }
    
    public String getName() {

        return name;
    }
    
    public String getUserName() {
        int from = name.indexOf('[');
        int to = name.indexOf(']');
        
        if(from != -1 && from < to)
            return name.substring(from + 1, to);
        return name;
    }
    
    public UnixPerm getPerm() {
        
        return a_perm;
    }   
    
    public void setPerm(UnixPerm perm) {
        
        a_perm = perm;
    }
    
    public int getMode() {
            
        return a_perm.getMode();
    }
    
    public boolean isDuplicate(UfsAclEntry other) {
        
        if((other.getType() == a_type) && (other.getId() == a_id)) {
            return true;
        } 
        return false;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        switch(a_type) {
            case USER_OBJ: sb.append("user::");
                break;
            case USER: sb.append("user:");
                sb.append(a_id + ":");
                break;
            case GROUP_OBJ: sb.append("group::");
                break;
            case GROUP: sb.append("group:");
                sb.append(a_id + ":");
                break;
            case CLASS_OBJ: sb.append("mask:");
                break;
            case OTHER_OBJ: sb.append("other:");
                break;
            case DEF_USER_OBJ: sb.append("default:user::");
                break;
            case DEF_USER: sb.append("default:user:");
                sb.append(a_id + ":");
                break;
            case DEF_GROUP_OBJ: sb.append("default:group::");
                break;
            case DEF_GROUP: sb.append("default:group:");
                sb.append(a_id + ":");
                break;
            case DEF_CLASS_OBJ: sb.append("default:mask:");
                break;
            case DEF_OTHER_OBJ: sb.append("default:other:");
                break;
        }
        sb.append(a_perm.toString());
        return sb.toString();
    }
        
    /** Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.<p>
     *
     * In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i>
     * is negative, zero or positive.
     *
     * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)<p>
     *
     * The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.<p>
     *
     * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.<p>
     *
     * It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * @param   o the Object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     * 		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this Object.
     *
     */
    public int compareTo(Object other) {
        
        int result = getName().compareToIgnoreCase(((UfsAclEntry)other).getName());
        if(result == 0) {
            result = getType() - ((UfsAclEntry)other).getType();
        }

        return result;
    }
    
    public UfsAclEntry getDefault() {
        
        return new UfsAclEntry(a_type + ACL_DEFAULT, a_id, name, a_perm);
    }
    
}
