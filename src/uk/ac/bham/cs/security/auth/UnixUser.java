/*
 * UnixUser.java
 *
 * Created on 24 January 2006, 13:54
 *
 */

package uk.ac.bham.cs.security.auth;

import java.io.File;
import com.sun.security.auth.UnixPrincipal;

/**
 *
 * @author nrs
 */
public class UnixUser extends UnixPrincipal {    
    
    private String password;
    private long uid;    
    private long gid;
    private String gecos;
    private File directory;
    private File shell;
        
    public UnixUser(String name) {
        super(name);
    }
    
    public UnixUser(String name, String password, long uid, long gid,
            String gecos, String directory, String shell) {
        super(name);
        this.password = password;
        this.uid = uid;
        this.gid = gid;
        this.gecos = gecos;
        this.directory = new File(directory);
        this.shell = new File(shell);
    }
    
    public long getGid() {
        return gid;
    }

    public String getGecos() {
        return gecos;
    }

    public File getDirectory() {
        return directory;
    }

    public File getShell() {
        return shell;
    }           

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public void setGecos(String gecos) {
        this.gecos = gecos;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void setShell(File shell) {
        this.shell = shell;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + getName() + "\n");
        sb.append("Password: " + getPassword() + "\n");
        sb.append("Uid: " + getUid() + "\n");
        sb.append("Gid: " + getGid() + "\n");
        sb.append("Gecos: " + getGecos() + "\n");
        sb.append("Directory: " + getDirectory() + "\n");
        sb.append("Shell: " + getShell() + "\n");
        return sb.toString();
    }
}
