/*
 * PermTableModel.java
 *
 * Created on January 27, 2003, 2:47 PM
 */

package uk.ac.bham.cs.security.acl;

/**
 *
 * @author  nrs
 */
public class PermTableModel extends javax.swing.table.AbstractTableModel {
    
    private final String[] colnames = { "Permissions:", "Allow", "Effective" };
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
