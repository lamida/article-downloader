/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lamida.nd.ui;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import net.lamida.nd.Constant;
import net.lamida.nd.Utils;
import net.lamida.nd.bean.SearchResult;
import net.lamida.nd.bean.SearchResultItem;
import net.lamida.nd.parser.AbstractParser;
import net.lamida.nd.parser.IParser;
import net.lamida.nd.pdf.INewsPdfWriter;
import net.lamida.nd.pdf.IPdfJoiner;
import net.lamida.nd.pdf.NewsPdfWriter;
import net.lamida.nd.pdf.PdfInputData;
import net.lamida.nd.pdf.PdfJoiner;
import net.lamida.nd.rest.AbstractRestSearch;
import net.lamida.nd.rest.IRestSearch;
import net.lamida.nd.rest.SearchProviderEnum;
import net.lamida.nd.rest.SearchResultBuilder;

/**
 *
 * @author lamida
 */
@SuppressWarnings("serial")
public class NewsDownloaderForm extends javax.swing.JFrame {

    private int resultStart = 1;
    private Map<Integer, SearchResult> searchCache;
    private DefaultTableModel model;
    private IRestSearch currentSearch;
    private boolean dummyData = true;
    private long totalResults;
    private boolean selectAll;

    /**
     * Creates new form NewsDownloaderForm
     */
    public NewsDownloaderForm() {
        searchCache = new HashMap<Integer, SearchResult>();
        initComponents();
        addLinkListener();
        addTableHeaderClickListener();
        model = (DefaultTableModel) table.getModel();
        textOutputFile.setText(new File("result/output.pdf").getAbsolutePath());
    }

	private void addTableHeaderClickListener() {
		table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                selectResultToText.setText("");
                selectAllDialog.setLocationRelativeTo(null);
                selectAllDialog.pack();
                selectAllDialog.setVisible(true);
            }
        ;
        });
	}

	private void addLinkListener() {
		table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int row = target.getSelectedRow();
                int column = target.getSelectedColumn();
                if (column != 3) {
                    return;
                }
                String link = (String) target.getValueAt(row, column);
                try {
                    System.out.println(Desktop.isDesktopSupported());
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI(link));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(NewsDownloaderForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
	}

	/**
	 * Currently Not Used
	 * @param mouseEvent
	 */
    private void toggleSelectAllCurrentPage(MouseEvent mouseEvent) {
        int index = table.columnAtPoint(mouseEvent.getPoint());
        if (index == 4) {
            selectAll = !selectAll;
            for (int row = 0; row < model.getRowCount(); row++) {
                model.setValueAt(selectAll, row, 4);
            }
        }
    }

    private void clearTableData() {
        while (model.getRowCount() != 0) {
            model.removeRow(model.getRowCount() - 1);
        }
    }

    private SearchResult getDummySearchResult() {
        SearchResultBuilder builder = new SearchResultBuilder();
        SearchResult result = null;
        try {
            result = builder.build(Utils.readFileToString(new File("resources/googleSearchCnn.txt")));
        } catch (IOException ex) {
            Logger.getLogger(NewsDownloaderForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private Object[][] convertToArray(SearchResult search) {
        Object[][] data = new Object[10][5];
        int row = 0;
        for (SearchResultItem item : search.getItems()) {
            data[row][0] = resultStart + row;
            data[row][1] = item.getTitle();
            data[row][2] = item.getSnippet();
            data[row][3] = item.getLink();
            data[row][4] = item.isSelected();
            row++;
        }
        return data;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewsDownloaderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewsDownloaderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewsDownloaderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewsDownloaderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NewsDownloaderForm newsDownloaderForm = new NewsDownloaderForm();
                newsDownloaderForm.setLocationRelativeTo(null);
                newsDownloaderForm.setVisible(true);
            }
        });
    }

    private void showResult(SearchResult result) {
        clearTableData();
        Object[][] data = convertToArray(result);
        for (Object[] row : data) {
            model.addRow(row);
        }
        int currentPage = resultStart / Constant.RESULTS_PER_PAGE;
        int lastPage = (int) (result.getSearchInformation().getTotalResults() / Constant.RESULTS_PER_PAGE);
        executionTimeLabel.setText("About " + result.getSearchInformation().getTotalResults() + " results (" + result.getSearchInformation().getSearchTime() + "seconds)");
        pagingInfoLabel.setText("Page " + (currentPage + 1) + " of " + lastPage);
        
        enableDisableButton(currentPage, lastPage);
    }

	private void enableDisableButton(int currentPage, int lastPage) {
		if (currentPage > 0) {
            prevButton.setEnabled(true);
        } else {
            prevButton.setEnabled(false);
        }
        if (currentPage < lastPage) {
            nextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
        }
	}

    private void updateSelected() {
        SearchResult currentResult = searchCache.get(resultStart);
        int row = 0;
        for (SearchResultItem it : currentResult.getItems()) {
            boolean selected = (Boolean) model.getValueAt(row, 4);
            it.setSelected(selected);
            row++;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectAllDialog = new javax.swing.JDialog();
        jLabel7 = new javax.swing.JLabel();
        selectAllAcceptButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        selectResultToText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        dataProviderCombo = new javax.swing.JComboBox(SearchProviderEnum.values());
        jScrollPane1 = new javax.swing.JScrollPane();
        queryText = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        countKeywordsCb = new javax.swing.JCheckBox();
        highlightKeywordsCb = new javax.swing.JCheckBox();
        searchButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        saveButton = new javax.swing.JButton();
        executionTimeLabel = new javax.swing.JLabel();
        pagingInfoLabel = new javax.swing.JLabel();
        prevButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        textOutputFile = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        dateFromText = new javax.swing.JTextField();
        dateToText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Select result");

        selectAllAcceptButton.setText("OK");
        selectAllAcceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllAcceptButtonActionPerformed(evt);
            }
        });

        jLabel10.setText("To:");

        javax.swing.GroupLayout selectAllDialogLayout = new javax.swing.GroupLayout(selectAllDialog.getContentPane());
        selectAllDialog.getContentPane().setLayout(selectAllDialogLayout);
        selectAllDialogLayout.setHorizontalGroup(
            selectAllDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, selectAllDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(selectAllAcceptButton)
                .addContainerGap())
            .addGroup(selectAllDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selectAllDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(selectAllDialogLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectResultToText)
                        .addGap(10, 10, 10))
                    .addGroup(selectAllDialogLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        selectAllDialogLayout.setVerticalGroup(
            selectAllDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectAllDialogLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(selectAllDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(selectResultToText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(selectAllAcceptButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Data Provider:");

        queryText.setColumns(20);
        queryText.setRows(5);
        queryText.setToolTipText("You can separate the keywords using space");
        jScrollPane1.setViewportView(queryText);

        jLabel2.setText("Search Query:");

        countKeywordsCb.setText("Count Keywords");
        countKeywordsCb.setToolTipText("When checked, in header of document result will show number of keyword inside document");

        highlightKeywordsCb.setText("Highlight Keywords");
        highlightKeywordsCb.setToolTipText("Highlight keyword in result document");

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Title", "Snippet", "URL", "Select All"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(table);
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setCellRenderer(new PathCellRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new PathCellRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new PathCellRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        executionTimeLabel.setText(" ");

        pagingInfoLabel.setText(" ");

        prevButton.setText("<");
        prevButton.setEnabled(false);
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });

        nextButton.setText(">");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Date From:");

        jLabel4.setText("Date To:");

        jLabel5.setText("yyyymmdd");

        jLabel6.setText("yyyymmdd");

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(countKeywordsCb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(highlightKeywordsCb))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(prevButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(saveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textOutputFile, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(1, 1, 1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchButton)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dataProviderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dateToText, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                    .addComponent(dateFromText))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(executionTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pagingInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateFromText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateToText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchButton)
                .addGap(9, 9, 9)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(executionTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pagingInfoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prevButton)
                    .addComponent(nextButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countKeywordsCb)
                    .addComponent(highlightKeywordsCb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(textOutputFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if (queryText.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please Enter Search Query");
            return;
        }
        searchCache.clear();
        if (dummyData) {
            SearchResult searchResult = getDummySearchResult();
            searchCache.put(resultStart, searchResult);
        } else {
            currentSearch = AbstractRestSearch.getSearchProvider(SearchProviderEnum.getSearchProviderEnumByName(dataProviderCombo.getSelectedItem().toString()));
            currentSearch.setQuery(queryText.getText());
            currentSearch.setDateFrom(dateFromText.getText().trim().isEmpty() ? null : dateFromText.getText().trim());
            currentSearch.setDateTo(dateToText.getText().trim().isEmpty() ? null : dateToText.getText().trim());
            String json = currentSearch.execute();
            SearchResult searchResult = new SearchResultBuilder().build(json);
            searchCache.put(resultStart, searchResult);
        }
        showResult(searchCache.get(resultStart));
    }//GEN-LAST:event_searchButtonActionPerformed

    private int selectedCount;
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed

        updateSelected();
        selectedCount = 0;
        for (SearchResult result : searchCache.values()) {
            for (SearchResultItem it : result.getItems()) {
                if (it.isSelected()) {
                    selectedCount++;
                }
            }
        }
        doDownload();
        if (selectedCount != 0) {
            if (saveFile == null) {
                saveFile = new File(textOutputFile.getText());
            }
            if (saveFile.exists()) {
                int x = JOptionPane.showConfirmDialog(NewsDownloaderForm.this, "File with same name exist. Would you like to overwrite?");
                if (x != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            String path = saveFile.getAbsolutePath();
            if (!path.endsWith(".pdf")) {
                path += ".pdf";
            }
            IPdfJoiner pdfJoiner = new PdfJoiner();
            File temp = new File("temp");
            if(!temp.exists()){
            	temp.mkdir();
            }
            pdfJoiner.joinPdf("temp", path);
            JOptionPane.showMessageDialog(NewsDownloaderForm.this, "File saved at " + path);
        } else {
            JOptionPane.showMessageDialog(NewsDownloaderForm.this, "Please select some checkbox first");
        }

	}//GEN-LAST:event_saveButtonActionPerformed

    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        if (resultStart == 0) {
            return;
        }
        navigatePrev();
    }//GEN-LAST:event_prevButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        navigateNext();
    }//GEN-LAST:event_nextButtonActionPerformed
    
    private File saveFile;
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        final JFileChooser fc = new JFileChooser();
        int i = fc.showSaveDialog(this);
        textOutputFile.setText(fc.getSelectedFile().getAbsolutePath());
        saveFile = fc.getSelectedFile();
    }//GEN-LAST:event_browseButtonActionPerformed

    /**
     * TODO
     * @param evt
     */
    private void selectAllAcceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllAcceptButtonActionPerformed
        try {
            if (selectResultToText.getText().isEmpty()) {
                selectResultToText.setText("0");
            }
            int selectAllTo = Integer.parseInt(selectResultToText.getText());
            if (selectAllTo > 100) {
                JOptionPane.showMessageDialog(this, "Maximum Article to Select is 100", "Error", JOptionPane.ERROR_MESSAGE);
                selectAllTo = 100;
            }
            int page = selectAllTo / Constant.RESULTS_PER_PAGE + 1;

            for (int i = 1; i < page; i++) {
                int resultStart = i;
                showResult(searchCache.get(resultStart));
                if (searchCache.get(resultStart) == null) {
                    navigateNext();
                }
                if (searchCache.get(resultStart) != null) {
                    for (SearchResultItem it : searchCache.get(resultStart).getItems()) {
                        it.setSelected(true);
                    }
                    resultStart += Constant.RESULTS_PER_PAGE;
                }
            }
            selectAllDialog.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_selectAllAcceptButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JCheckBox countKeywordsCb;
    private javax.swing.JComboBox dataProviderCombo;
    private javax.swing.JTextField dateFromText;
    private javax.swing.JTextField dateToText;
    private javax.swing.JLabel executionTimeLabel;
    private javax.swing.JCheckBox highlightKeywordsCb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel pagingInfoLabel;
    private javax.swing.JButton prevButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextArea queryText;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton selectAllAcceptButton;
    private javax.swing.JDialog selectAllDialog;
    private javax.swing.JTextField selectResultToText;
    private javax.swing.JTable table;
    private javax.swing.JTextField textOutputFile;
    // End of variables declaration//GEN-END:variables

    private void doDownload() {
        progressBar.setIndeterminate(false);
        progressBar.setMaximum(100);
        new SwingWorker<Object, Object>() {
            @Override
            protected Object doInBackground() throws Exception {
                IParser parser = AbstractParser.getParser(SearchProviderEnum.getSearchProviderEnumByName(dataProviderCombo.getSelectedItem().toString()));
                INewsPdfWriter pdfWriter = new NewsPdfWriter();
                int count = 0;
                for (SearchResult result : searchCache.values()) {
                    for (SearchResultItem it : result.getItems()) {
                        if (it.isSelected()) {
                            count++;
                            progressBar.setValue(100 * count / selectedCount);
                            parser.init(it.getLink());

                            PdfInputData data = new PdfInputData(queryText.getText(), it.getLink(), parser.getNewsTitle(), parser.getNewsContent(), parser.getNewsPostTime());
                            String targetFileName = it.getTitle().replace(":", "") + ".pdf";
                            pdfWriter.init(data, targetFileName, countKeywordsCb.isSelected(), highlightKeywordsCb.isSelected());
                            pdfWriter.writePdf();
                        }
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                progressBar.setValue(0);
                JOptionPane.showMessageDialog(NewsDownloaderForm.this, "All Articles have been downloaded!");
            }
        }
                .execute();
    }
    
    private SearchResultBuilder resultBuilder = new SearchResultBuilder();

    private void navigateNext() {
        updateSelected();
        resultStart += Constant.RESULTS_PER_PAGE;
        System.out.println("navigateNext " + resultStart);
        if (searchCache.get(resultStart) == null) {
            if (dummyData) {
                SearchResult searchResult = getDummySearchResult();
                searchCache.put(resultStart, searchResult);
            } else {
                String json = currentSearch.next();
                SearchResult searchResult = resultBuilder.build(json);
                searchCache.put(resultStart, searchResult);
            }
        }
        showResult(searchCache.get(resultStart));
    }

    private void navigatePrev() {
        updateSelected();
        resultStart -= Constant.RESULTS_PER_PAGE;
        if (searchCache.get(resultStart) == null) {
            if (dummyData) {
                SearchResult searchResult = getDummySearchResult();
                searchCache.put(resultStart, searchResult);
            } else {
                String json = currentSearch.prev();
                SearchResult searchResult = resultBuilder.build(json);
                searchCache.put(resultStart, searchResult);
            }
        }
        showResult(searchCache.get(resultStart));
    }
}

@SuppressWarnings("serial")
class PathCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setToolTipText(c.getText());
        return c;
    }
}
