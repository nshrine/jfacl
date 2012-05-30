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
 * PermTableModel.java
 *
 * Created on January 27, 2003, 2:47 PM
 */

package org.flimbar.security.acl;

/**
 *
 * @author  nrs
 */
public class PermTableModel extends javax.swing.table.AbstractTableModel {
    
    private final String[] colnames = { "Permissions", "Allow", "Effective" };
    private final Object[][] data = {
        { new String(" Read"), new Boolean(false), new Boolean(false) },
        { new String(" Write"), new Boolean(false), new Boolean(false) },
        { new String(" Execute"), new String(""), new String("") }
    };
    private final boolean EDITABLE;
                                          
    public PermTableModel(boolean editable) {
        EDITABLE = editable;
    }
    
    public int getColumnCount() {        
        return colnames.length;
    }
    
    public int getRowCount() {        
        return data.length;
    }
    
    public String getColumnName(int col) {        
        return colnames[col];
    }
    
    public Object getValueAt(int row, int col) {                
        return data[row][col];
    }
    
    public void setValueAt(Object obj, int row, int col) {        
        data[row][col] = obj;
        fireTableCellUpdated(row, col);
    }
    
    public boolean isCellEditable(int row, int col) {        
        if (EDITABLE && (col == 1)) {
            return true;
        } else {
            return false;
        }
    }
    
    public void setPerms(int type, UnixPerm p, UnixPerm e) {        
        boolean[] perms = p.getPerms();
        boolean[] effective = e.getPerms();
        
        for(int i=0; i<3; i++) {
            data[i][1] = new Boolean(perms[i]);
            if((type == UfsAclEntry.USER) || (type == UfsAclEntry.GROUP_OBJ)
                    || (type == UfsAclEntry.GROUP)) {
                data[i][2] = new Boolean(effective[i]);
            } else {
                data[i][2] = new String("");
            }
        }
        
        fireTableDataChanged();
    }
    
    public Class getColumnClass(int col) {        
        return getValueAt(0, col).getClass();
    }
}
