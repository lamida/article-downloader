/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lamida.restui;

import java.util.Map;
import javax.swing.*;
import net.lamida.rest.Job;
import net.lamida.rest.RestParameter;
import net.lamida.rest.RestResponse;
import net.lamida.rest.client.facade.GuardianFacade;
import net.lamida.util.ProgressReporter;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author lamida
 */
public class UiMainDeprecated extends javax.swing.JFrame implements ProgressReporter {

    Map<String, RestParameter> parameters;
    private RestParameter restParameter;

    /**
     * Creates new form Main
     */
    public UiMainDeprecated(Map<String, RestParameter> parameters) {
        this.parameters = parameters;
        // get first
        restParameter = parameters.values().iterator().next();
        initComponents();
        TextAreaAppender appender = new TextAreaAppender();
        appender.setLayout(new PatternLayout("%d{ABSOLUTE} %5p %c{1}:%L - %m%n"));
        TextAreaAppender.setTextArea(textLog);
        Logger.getRootLogger().addAppender(appender);
    }

    public void init() {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UiMainDeprecated.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UiMainDeprecated.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UiMainDeprecated.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UiMainDeprecated.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                UiMainDeprecated.this.setLocationRelativeTo(null);
                UiMainDeprecated.this.setVisible(true);
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        helpDialog = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        dataProviderCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        queryText = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        orderByCombo = new javax.swing.JComboBox();
        searchButton = new javax.swing.JToggleButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fromDateText = new javax.swing.JTextField();
        toDateText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textLog = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        pageSizeText = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        countKeywordsCb = new javax.swing.JCheckBox();
        fileFormatCombo = new javax.swing.JComboBox();
        highlightKeywordsCb = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        jTextArea2.setColumns(20);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setText("Article Fetcher v0.1\n\nDear Chubby ini applikasi pertama untuk Chubby. Cara pakainya sangat sederhana juga dengan user interface yang seadanya.\n\nUntuk sementara data providernya hanya the guardian. Nanti akan ditambah sesuai dengan keperluan.\n\nUntuk search masukan keyword dengan pemisah spasi. Set rentang waktu artikel dan order sortir jika diperlukan. Set maksimum limit search. Limit maksimal adalah 50 result. Sebetulnya limit ini bisa diakali jika diperlukan untuk mencari result yang lebih banyak. Namun mengakalinya butuh waktu :p.\n\nSetelah semua parameter dimasukan tinggal tekan search. Textbox kosong di bawah search and download button adalah log area yang menunjukan apa yang sedang dilakukan aplikasi. Progress searching juga bisa dilihat dari progress bar yang berjalan. Jika search dan download selesai article yang dimaksud ada di folder results dan subfolder dalam format yyyymmdd_hhmmss misalnya 20121122_004744. Di dalam folder hasil ini outputnya berupa file text biasa. Nanti bisa dirubah ke format yang diinginkan jika mau. Tambah sedikit koding lagi. File hasil download dipisah berdasar article dengan nama file diawali urutan search dan id article.\n\nKarena ini merupakan prototype awal yang dikoding dengan ngebut, pasti masih banyak bugnya. Tolong berikan feedback juga requirement seperti apa yang bunda butuhkan.\n\n\nPS: I love you");
        jTextArea2.setWrapStyleWord(true);
        jScrollPane4.setViewportView(jTextArea2);

        javax.swing.GroupLayout helpDialogLayout = new javax.swing.GroupLayout(helpDialog.getContentPane());
        helpDialog.getContentPane().setLayout(helpDialogLayout);
        helpDialogLayout.setHorizontalGroup(
            helpDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        helpDialogLayout.setVerticalGroup(
            helpDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Article Fetcher");

        jLabel1.setText("Data Provider:");

        dataProviderCombo.setModel(new DefaultComboBoxModel(parameters.keySet().toArray()));

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${providers}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, dataProviderCombo, eLProperty, dataProviderCombo, "");
        bindingGroup.addBinding(jComboBoxBinding);

        jLabel2.setText("Search Query:");

        queryText.setColumns(20);
        queryText.setRows(5);
        queryText.setToolTipText("You can separate the keywords using space");
        jScrollPane1.setViewportView(queryText);

        jLabel3.setText("Order By:");

        orderByCombo.setModel(new DefaultComboBoxModel(restParameter.getOrderByList().split(",")));

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("To Date:");

        jLabel5.setText("From Date:");

        jLabel6.setText("YYYY-MM-DD (optional)");

        jLabel7.setText("YYYY-MM-DD (optional)");

        textLog.setColumns(20);
        textLog.setRows(5);
        jScrollPane2.setViewportView(textLog);

        jLabel8.setText("Limit Search:");

        jLabel9.setText("1-50 (default is 50)");

        jLabel10.setText("Log:");

        jLabel11.setText("File Format");

        countKeywordsCb.setText("Count Keywords");
        countKeywordsCb.setToolTipText("When checked, in header of document result will show number of keyword inside document");

        fileFormatCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PDF" }));

        highlightKeywordsCb.setText("Highlight Keywords");
        highlightKeywordsCb.setToolTipText("Highlight keyword in result document");

        jMenu1.setText("File");

        jMenuItem1.setText("Help");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(1, 1, 1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(fileFormatCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(searchButton))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(orderByCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(toDateText, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dataProviderCombo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fromDateText, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pageSizeText, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel9)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(countKeywordsCb)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(highlightKeywordsCb)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(dataProviderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(fromDateText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(toDateText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(pageSizeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(orderByCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchButton)
                    .addComponent(jLabel11)
                    .addComponent(fileFormatCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countKeywordsCb)
                    .addComponent(highlightKeywordsCb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
        progressBar.setIndeterminate(true);
        progressBar.setValue(0);
        restParameter.setOrderBy(orderByCombo.getSelectedItem().toString());
        restParameter.setQuery(queryText.getText());
        restParameter.setFromDate(fromDateText.getText());
        restParameter.setToDate(toDateText.getText());
        restParameter.setCountKeyWords(countKeywordsCb.isSelected());
        restParameter.setHighlightKeyWords(highlightKeywordsCb.isSelected());
        
        String pageSize = pageSizeText.getText();
        if (pageSize.isEmpty()) {
            pageSize = "50";
        }
        restParameter.setPageSize(pageSize);
        
        final Job job = new Job(restParameter);
        final RestResponse responseData = new GuardianFacade().fetchResult(job);
        progressBar.setIndeterminate(false);
        progressBar.setMaximum(responseData.getResults().size());
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                new GuardianFacade().download(job, responseData, UiMainDeprecated.this);
                return null;
            }

            @Override
            protected void done() {
                progressBar.setValue(0);
                JOptionPane.showMessageDialog(UiMainDeprecated.this, "All Articles have been downloaded!");
            }
        }.execute();
    }//GEN-LAST:event_searchButtonActionPerformed

//    private void searchButtonActionPerformedOld(java.awt.event.ActionEvent evt) {                                             
//        // TODO add your handling code here:
//        progressBar.setIndeterminate(true);
//        progressBar.setValue(0);
//        restParameter.setOrderBy(orderByCombo.getSelectedItem().toString());
//        restParameter.setQuery(queryText.getText());
//        restParameter.setFromDate(fromDateText.getText());
//        restParameter.setToDate(toDateText.getText());
//        String pageSize = pageSizeText.getText();
//        if (pageSize.isEmpty()) {
//            pageSize = "50";
//        }
//        restParameter.setPageSize(pageSize);
//
//        final RestClient client = new GuardianRestClient(restParameter);
//        client.setProgressReporter(this);
//        String jsonResponse = client.getJsonResponse();
//        final RestResponse responseData = client.buildDataFromJson(jsonResponse);
//        progressBar.setIndeterminate(false);
//        progressBar.setMaximum(responseData.getResults().size());
//        new SwingWorker<Object, Object>() {
//
//            @Override
//            protected Object doInBackground() throws Exception {
//                client.crawlAndSaveFiles(responseData);
//                return null;
//            }
//
//            @Override
//            protected void done() {
//                progressBar.setValue(0);
//                JOptionPane.showMessageDialog(Main.this, "All Articles have been downloaded!");
//            }
//        }.execute();
//    }          
    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        helpDialog.pack();
        helpDialog.setLocationRelativeTo(null);
        helpDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox countKeywordsCb;
    private javax.swing.JComboBox dataProviderCombo;
    private javax.swing.JComboBox fileFormatCombo;
    private javax.swing.JTextField fromDateText;
    private javax.swing.JDialog helpDialog;
    private javax.swing.JCheckBox highlightKeywordsCb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JComboBox orderByCombo;
    private javax.swing.JTextField pageSizeText;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextArea queryText;
    private javax.swing.JToggleButton searchButton;
    private javax.swing.JTextArea textLog;
    private javax.swing.JTextField toDateText;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    public Map<String, RestParameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, RestParameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void updateCurrentProcess(int i) {
        progressBar.setValue(i);
    }

    @Override
    public void updateCurrentStatus(String string) {
        progressBar.setString(string);
    }
}
