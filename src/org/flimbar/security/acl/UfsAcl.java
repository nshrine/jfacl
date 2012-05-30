/*
 * Copyright (C) 2003-2006 Nick Shrine <N.R.Shrine@cs.bham.ac.uk>
 *
 * This file is part of Jfacl.
 *
 * Jfacl is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Jfacl is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jfacl; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * UfsAcl.java
 *
 * Created on January 15, 2003, 11:10 AM
 *
 */

package org.flimbar.security.acl;

import java.util.ArrayList;
import java.util.TreeSet;
import java.io.File;

/**
 *
 * @author  nrs
 */
public class UfsAcl extends ArrayList<UfsAclEntry> {
    
    public native static ArrayList<UfsAclEntry> getacl(String path)
            throws UfsAclException;
    public native static int setacl(String path,
            ArrayList<UfsAclEntry> aclentries, int size) throws UfsAclException;
    public native static TreeSet<UfsAclEntry> getall();
    
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
        checkMask();
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
        
        UfsAclEntry mask = getUniqueEntry(UfsAclEntry.CLASS_OBJ);
        int type = entry.getType();
        
        if (((type == UfsAclEntry.USER) || (type == UfsAclEntry.GROUP))
                && (getUniqueEntry(UfsAclEntry.CLASS_OBJ) == null)) {
            add(getMask(entry));
        } else if (((type == UfsAclEntry.DEF_USER) || (type == UfsAclEntry.DEF_GROUP))
                && (getUniqueEntry(UfsAclEntry.DEF_CLASS_OBJ) == null)) {
            add(getMask(entry));
        }
            
        add(entry);
    }
    
    public UfsAclEntry getMask(UfsAclEntry entry) {
        UfsAclEntry group = getUniqueEntry(UfsAclEntry.GROUP_OBJ);
        
        int type = UfsAclEntry.CLASS_OBJ;
        if (entry.getType() > UfsAclEntry.ACL_DEFAULT) {
            type = UfsAclEntry.DEF_CLASS_OBJ;
        }
        
        int mode = entry.getMode() | group.getMode();
        
        return new UfsAclEntry(type, mode);
    }
    
    private void checkMask() {
        int def = 0;
        while (def <= UfsAclEntry.ACL_DEFAULT) {    
            if (getUniqueEntry(UfsAclEntry.CLASS_OBJ | def) == null) {                    
                UfsAclEntry group = getUniqueEntry(UfsAclEntry.GROUP_OBJ | def);
                if (group == null) {
                    return;
                }
                int mode = group.getMode();
                boolean needsMask = false;
                for (int i = 0; i < size(); i++) {
                    UfsAclEntry entry = getEntry(i);
                    if ((entry.getType() == (UfsAclEntry.USER | def))
                            || (entry.getType() == (UfsAclEntry.GROUP | def))) {
                        mode |= entry.getMode();
                        needsMask = true;
                    }
                }
                if (needsMask) {
                    add(new UfsAclEntry((UfsAclEntry.CLASS_OBJ | def), mode));
                }
            }
            def += UfsAclEntry.ACL_DEFAULT;
        }
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
            UfsAclEntry mask = getUniqueEntry(UfsAclEntry.CLASS_OBJ);
            if (mask != null) {
                addEntry(mask.getDefault());
            }
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
        ArrayList<UfsAclEntry> entries = new ArrayList<UfsAclEntry>();
        
        for (int i = 0; i < size(); i++) {
            UfsAclEntry entry = getEntry(i);
            if (entry.getType() == type) {
                entries.add(entry);
            }
        }
        
        UfsAclEntry[] result = (UfsAclEntry[]) entries.toArray();
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
