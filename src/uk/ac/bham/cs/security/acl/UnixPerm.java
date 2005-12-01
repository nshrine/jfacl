/*
 * Copyright (C) 2003-2005 Nick Shrine <N.R.Shrine@cs.bham.ac.uk>
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
 * UnixPerm.java
 *
 * Created on January 20, 2003, 12:20 PM
 */
package uk.ac.bham.cs.security.acl;

/**
 *
 * @author  nrs
 */
public class UnixPerm {
       
    static final int READ = 4;
    static final int WRITE = 2;
    static final int EXECUTE = 1;    
    static final String[] MODES =
        { "---", "--x", "-w-", "-wx", "r--", "r-x", "rw-", "rwx" };
        
    protected int mode;
    
    public UnixPerm(int mode) {    
        setMode(mode);
    }
    
    public UnixPerm(boolean read, boolean write, boolean execute) {        
        setPerms(read, write, execute);
    }
    
    public int getMode() {        
        return mode;
    }
    
    public void setMode(int mode) {        
        this.mode = mode;
    }
    
    public boolean[] getPerms() {        
        return new boolean[] {
            (mode & READ) == READ, 
            (mode & WRITE) == WRITE, 
            (mode & EXECUTE) == EXECUTE 
        };
    }
    
    public void setPerms(boolean read, boolean write, boolean execute) {        
        if(read) mode |= READ;
        if(write) mode |= WRITE;
        if(execute) mode |= EXECUTE;
    }
        
    public String toString() {        
        return MODES[mode];
    }     
}
