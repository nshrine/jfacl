/*
 * UfsAcl.java
 *
 * Created on January 15, 2003, 11:10 AM
 */

package uk.ac.bham.cs.security.acl;

import java.util.ArrayList;
import java.util.TreeSet;
import java.io.File;

/**
 *
 * @author  nrs
 */
public class UfsAcl extends ArrayList {
    
    public native static ArrayList getacl(String path) throws UfsAclException;
    public native static int setacl(String path, ArrayList aclentries, int size)
        throws UfsAclException;
    public native static TreeSet getall();
    
    static {
        System.loadLibrary("jacl");
    }
    
    private static int nameCount;
    public static int getNameCount() { return nameCount;}
    public static void setNameCount(int count) { nameCount = count;}
    
    protected String path;
    protected boolean owner;
    public final boolean ISDIR;
    
    public UfsAcl(String path) throws Exception {        
        this.path = path;
        ISDIR = (new File(path)).isDirectory();
        addAll(getacl(path));
        owner = System.getProperty("user.name").
            equals(getUniqueEntry(UfsAclEntry.USER_OBJ).getUserName()) 
            || System.getProperty("user.name").equals("root");
    }
    
    public static void main(String[] args) {       
        try {
            UfsAcl acl = new UfsAcl(args[0]);
            System.out.println(acl.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void addEntry(UfsAclEntry entry) throws UfsAclException {       
        if (isPresent(entry)) {
            throw new UfsAclException("CHK5");
        }
        
        add(entry);
    }
    
    public boolean isOwner() {        
        return owner;
    }
    
    public boolean hasDefaults() {        
        boolean result = false;
        
        for (int i=0; i<size(); i++) {
            UfsAclEntry entry = getEntry(i);
            if (entry.getType() > UfsAclEntry.ACL_DEFAULT) {
                result = true;
                break;
            }
        }
        
        return result;
    }        
    
    public void addDefaults() throws UfsAclException {        
        if (!hasDefaults()) {
            addEntry(getUniqueEntry(UfsAclEntry.USER_OBJ).getDefault());
            addEntry(getUniqueEntry(UfsAclEntry.GROUP_OBJ).getDefault());
            addEntry(getUniqueEntry(UfsAclEntry.CLASS_OBJ).getDefault());
            addEntry(getUniqueEntry(UfsAclEntry.OTHER_OBJ).getDefault());
        }
    }
    
    public void removeDefaults() {        
        for (int i=0; i<size(); i++) {
            UfsAclEntry entry = getEntry(i);
            if (entry.getType() > UfsAclEntry.ACL_DEFAULT) {
                remove(entry);
                i--;
            }
        }
    }
    
    private UfsAclEntry getUniqueEntry(int type) {        
        UfsAclEntry result = null;
        
        for (int i=0; i<size(); i++) {
            UfsAclEntry entry = getEntry(i);
            if (entry.getType() == type) {
                result = entry;
                break;
            }
        }
        
        return result;
    }
    
    public UfsAclEntry[] getEntries(int type) {        
        ArrayList entries = new ArrayList();
        
        for (int i=0; i<size(); i++) {
            UfsAclEntry entry = getEntry(i);
            if (entry.getType() == type) {
                entries.add(entry);
            }
        }
        
        UfsAclEntry[] result = (UfsAclEntry[])entries.toArray();
        return result;
    }
    
    public void changeEntry(int i, UfsAclEntry entry) {        
        set(i, entry);
    }
    
    public UfsAclEntry getEntry(int i) {        
        return (UfsAclEntry)get(i);
    }
    
    public UnixPerm getEffective(UfsAclEntry entry) {        
        UnixPerm perm =  entry.getPerm();
        UfsAclEntry maskEntry = getUniqueEntry(UfsAclEntry.CLASS_OBJ);
        UnixPerm mask = (maskEntry != null ? maskEntry.getPerm()
            : new UnixPerm(true, true, true));
        int type = entry.getType();
        
        if ((type == UfsAclEntry.USER) || (type == UfsAclEntry.GROUP)
                || (type == UfsAclEntry.GROUP_OBJ)) {
            return new UnixPerm(perm.getMode() & mask.getMode());
        }
        
        return perm;
    }
        
    public boolean isPresent(UfsAclEntry entry) {        
        boolean result = false;
        
        for (int i=0; i<size(); i++) {
            if (getEntry(i).isDuplicate(entry)) {
                result = true;
                break;
            }
        }
        
        return result;
    }
    
    public void update() throws UfsAclException {        
        setacl(path, this, size());
    }
    
    public String toString() {        
        StringBuffer sb = new StringBuffer();
        
        for (int i=0; i<size(); i++) {
            sb.append(((UfsAclEntry)get(i)).toString() + '\n');
        }
        
        return sb.toString();
    }    
}
