/*
 * Posix.java
 *
 * Created on 24 January 2006, 16:38
 *
 */

package org.flimbar.security.auth.module;

import org.flimbar.security.auth.*;

/**
 *
 * @author nrs
 */
public class Posix {
    
    private static Posix instance;
        
    private Posix() {
        System.loadLibrary("posix");        
    }
    
    public static Posix getInstance() {
        if (instance == null) {
            instance = new Posix();
        }
        return instance;
    }
    
    public native UnixUser getUserByName(String name);
    public native UnixUser getUserByUid(long uid);
    public native UnixGroup getGroupByName(String name);
    public native UnixGroup getGroupByGid(long gid);           
}
