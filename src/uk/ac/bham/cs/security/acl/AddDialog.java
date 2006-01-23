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
 * AddDialog.java
 *
 * Created on January 30, 2003, 3:38 PM
 */

package uk.ac.bham.cs.security.acl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
/**
 *
 * @author  nrs
 */
public class AddDialog extends javax.swing.JDialog implements ListCellRenderer {
    
    public final static int ONE_SECOND = 1000;
    
    private final JFrame parent;
    public final boolean DEFAULTS;
    private final Collection<UfsAclEntry> names;
    private DefaultListModel listmodel;        
    
    /** Creates new form AddDialog */
    public AddDialog(UfsAclWin parent, Collection<UfsAclEntry> names,
            boolean defaults, boolean modal) {        
        super(parent, "Select Users or Groups", modal);        
        this.parent = parent;
        this.names = names;
        DEFAULTS = defaults;
        initComponents();
        namesList.setCellRenderer(parent.getPanel());                
        namesList.setModel(new NamesListModel(names));
        listmodel = new DefaultListModel();
        selectedList.setCellRenderer(parent.getPanel());
        selectedList.setModel(listmodel);
        okButton.addActionListener(parent.getPanel());
        cancelButton.addActionListener(parent.getPanel());
        setSize(500, 300);
        setLocationRelativeTo(parent);
    }
    
    public JFrame getParent() {
        return parent;
    }
    
    public Enumeration getSelected() {        
        return listmodel.elements();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        namesList = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        defButton = new javax.swing.JButton();
        nameButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        selectedList = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jScrollPane1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(260, 132));
        namesList.setMinimumSize(new java.awt.Dimension(0, 260));
        jScrollPane1.setViewportView(namesList);

        getContentPane().add(jScrollPane1);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel3.setMinimumSize(new java.awt.Dimension(107, 32));
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 32));
        addButton.setText("Add");
        addButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        addButton.setOpaque(false);
        addButton.setPreferredSize(new java.awt.Dimension(70, 25));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        jPanel3.add(addButton);

        defButton.setText("Add Default");
        defButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        defButton.setEnabled(DEFAULTS);
        defButton.setPreferredSize(new java.awt.Dimension(100, 25));
        defButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defButtonActionPerformed(evt);
            }
        });

        jPanel3.add(defButton);

        nameButton.setText("Enter Name");
        nameButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        nameButton.setPreferredSize(new java.awt.Dimension(100, 25));
        nameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameButtonActionPerformed(evt);
            }
        });

        jPanel3.add(nameButton);

        getContentPane().add(jPanel3);

        jScrollPane2.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jScrollPane2.setMaximumSize(new java.awt.Dimension(32767, 100));
        selectedList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        selectedList.setVisibleRowCount(4);
        jScrollPane2.setViewportView(selectedList);

        getContentPane().add(jScrollPane2);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel1.setMinimumSize(new java.awt.Dimension(71, 32));
        jPanel1.setPreferredSize(new java.awt.Dimension(155, 32));
        okButton.setText("OK");
        okButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        okButton.setPreferredSize(new java.awt.Dimension(70, 25));
        jPanel1.add(okButton);

        cancelButton.setText("Cancel");
        cancelButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        cancelButton.setPreferredSize(new java.awt.Dimension(70, 25));
        jPanel1.add(cancelButton);

        getContentPane().add(jPanel1);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void nameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameButtonActionPerformed
        NameDialog ndialog = new NameDialog(this, true);
        ndialog.setVisible(true);
    }//GEN-LAST:event_nameButtonActionPerformed

    private void defButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defButtonActionPerformed
        addEntry(true);
    }//GEN-LAST:event_defButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        addEntry(false);
    }//GEN-LAST:event_addButtonActionPerformed
    
    private void addEntry(boolean def) {        
        if (namesList.isSelectionEmpty()) {
            return;
        }
        
        Object[] values = namesList.getSelectedValues();        
        UfsAclEntry[] entries = new UfsAclEntry[values.length];
        for (int i = 0; i < values.length; i++) {
            entries[i] = (UfsAclEntry) values[i];
        }
        addEntries(entries, def);
    }
        
    public void addEntries(UfsAclEntry[] uentries, boolean def) {
        for (UfsAclEntry entry : uentries) {                
            if (def) {
                entry = entry.getDefault();
            }
            Enumeration sentries = listmodel.elements();
            while (sentries.hasMoreElements()) {
                UfsAclEntry next = (UfsAclEntry) sentries.nextElement();
                if (next.isDuplicate(entry)) {
                    return;
                }
            }
            listmodel.addElement(entry);
        }
    }
    
    public void enableDefaultsButton(boolean enable) {        
        defButton.setEnabled(enable);
    }
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        listmodel.removeAllElements();
        setVisible(false);                
    }//GEN-LAST:event_closeDialog
     
    public void unDisplay() {
        closeDialog(null);
    }
    
    /** Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell.  If it is necessary to compute the dimensions
     * of a list because the list cells do not have a fixed size, this method
     * is called to generate a component on which <code>getPreferredSize</code>
     * can be invoked.
     *
     * @param list The JList we're painting.
     * @param value The value returned by list.getModel().getElementAt(index).
     * @param index The cells index.
     * @param isSelected True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     *
     * @see JList
     * @see ListSelectionModel
     * @see ListModel
     *
     */
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) value;
        label.setOpaque(true);
        if (isSelected) {
            label.setBackground(list.getSelectionBackground());
            label.setForeground(list.getSelectionForeground());
        } else {
            label.setBackground(list.getBackground());
            label.setForeground(list.getForeground());
        }
        return label;
    }
    
    public UfsAclEntry getEntryByName(String name, int type) {
        UfsAclEntry match = null;
        
        for (UfsAclEntry entry : names) {
            if (name.equals(entry.getUserName()) && (type == entry.getType())) {
                match = entry;
                break;
            }            
        }
        
        return match;
    }
    
    class NamesListModel extends AbstractListModel {
        
        private Collection data;        
        
        public NamesListModel(Collection names) {            
            data = names;
        }        
               
        public int getSize() {                    
            return data.size();
        }                

        public Object getElementAt(int index) {                        
            return data.toArray()[index];
        }                        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton defButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton nameButton;
    private javax.swing.JList namesList;
    private javax.swing.JButton okButton;
    private javax.swing.JList selectedList;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {        
        TreeSet entries = UfsAcl.getall();
        for (Iterator i = entries.iterator(); i.hasNext(); ) {
            System.out.println(i.next());
        }
    }
}
