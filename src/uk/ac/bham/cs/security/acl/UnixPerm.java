/*
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
