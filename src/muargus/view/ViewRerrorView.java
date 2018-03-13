/*
 * Argus Open Source
 * Software to apply Statistical Disclosure Control techniques
 *
 * Copyright 2014 Statistics Netherlands
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the European Union Public Licence 
 * (EUPL) version 1.1, as published by the European Commission.
 *
 * You can find the text of the EUPL v1.1 on
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * This software is distributed on an "AS IS" basis without 
 * warranties or conditions of any kind, either express or implied.
 */
package muargus.view;

import argus.model.ArgusException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author pwof
 */
public class ViewRerrorView extends javax.swing.JDialog{

    /**
    * Creates new form ViewRerrorView
    * @param parent
    * @param modal 
    */
    public ViewRerrorView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        RerrorArea = new javax.swing.JTextArea();
        OKbutton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MU ARGUS ERROR INFO");
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/mu32.png")).getImage());

        RerrorArea.setColumns(20);
        RerrorArea.setRows(5);
        jScrollPane1.setViewportView(RerrorArea);

        OKbutton.setText("OK");
        OKbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKbuttonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Error running R script");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                    .addComponent(OKbutton)
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OKbutton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OKbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKbuttonActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_OKbuttonActionPerformed

    public void clearText(){
        RerrorArea.setText("");  
    }
    
    public void addTextFile (String fn) throws ArgusException{
        String hs;  
        clearText();
        File RoutFile = new File(fn);
        if (!RoutFile.exists()){
            RerrorArea.setText("The file " + fn + " could not be displayed; sorry"); 
        }
        else { 
            try (BufferedReader reader = new BufferedReader(new FileReader(fn))) {
                while ( (hs = reader.readLine()) != null){
                    RerrorArea.append(hs + "\n");
                }
            }
            catch (IOException ex){ throw new ArgusException("An error occured reading the file "+fn); }
            RerrorArea.setCaretPosition(1);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OKbutton;
    private javax.swing.JTextArea RerrorArea;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
