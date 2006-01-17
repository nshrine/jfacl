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
 * UfsAclWin.java
 *
 * Created on January 23, 2003, 3:48 PM
 */

package uk.ac.bham.cs.security.acl;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.Point;
/**
 *
 * @author  nrs
 */
public class UfsAclWin extends JFrame {
    
    private UfsAcl acl = null;
    private UfsAclPanel aclpanel = null;
    private static Point lastopen = null;
    
    /** Creates new form UfsAclWin */
    public UfsAclWin() {        
        initComponents();        
    }
    
    public UfsAclWin(String path) throws Exception {        
        super(path);
        acl = new UfsAcl(path);
        aclpanel = new UfsAclPanel(this);
        initComponents();
        jTabbedPane1.add("Security", aclpanel);
        
        if (!acl.isOwner()) {
            JOptionPane.showMessageDialog(this,
                    "You are only allowed to view the permissions on " + path,
                    "Security", JOptionPane.INFORMATION_MESSAGE);
        }
        
        setSize(350, 380);        
        if (lastopen == null) {
            setLocationRelativeTo(null);
        } else {
            setLocation(lastopen.x + 50, lastopen.y +50);
        }
        lastopen = getLocation();
    }
    
    public UfsAcl getAcl() {        
        return acl;
    }
    
    public UfsAclPanel getPanel() {        
        return aclpanel;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        okButton.setText("OK");
        okButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        okButton.setPreferredSize(new java.awt.Dimension(70, 25));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jPanel2.add(okButton);

        cancelButton.setText("Cancel");
        cancelButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        cancelButton.setPreferredSize(new java.awt.Dimension(70, 25));
        cancelButton.setEnabled(acl.isOwner());
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel2.add(cancelButton);

        applyButton.setText("Apply");
        applyButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        applyButton.setPreferredSize(new java.awt.Dimension(70, 25));
        applyButton.setEnabled(acl.isOwner());
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        jPanel2.add(applyButton);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        if (aclpanel.dataChanged()) {
            try {
                acl.update();
                aclpanel.dataSaved();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_applyButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if (aclpanel.dataChanged()) {
            int answer = JOptionPane.showConfirmDialog(this, "Discard Changes?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) {
                return;
            }
        }
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if(acl.isOwner()) {
            applyButtonActionPerformed(evt);
        }
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {        
        if ((args.length < 1) || (args[0].length() == 0)) {
//            System.err.println("Usage: jfacl <file|dir name> [file|dir name[..]]");
//            System.exit(0);
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setDialogTitle("Set Permissions on File or Directory");
            int value = chooser.showDialog(null, "Select");
            if (value == JFileChooser.APPROVE_OPTION) {
                try {
                    new UfsAclWin(chooser.getSelectedFile().getAbsolutePath())
                            .setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            for(int i=0; i<args.length; i++) {
                try {
                    new UfsAclWin(args[i]).setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    
}
