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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import net.lamida.nd.parser.AbstractParser;
import net.lamida.nd.parser.IParser;
import net.lamida.nd.pdf.INewsPdfWriter;
import net.lamida.nd.pdf.IPdfJoiner;
import net.lamida.nd.pdf.NewsPdfWriter;
import net.lamida.nd.pdf.PdfInputData;
import net.lamida.nd.pdf.PdfJoiner;
import net.lamida.nd.rest.SearchProviderEnum;
import net.lamida.nd.rest.neo.AbstractSearch;
import net.lamida.nd.rest.neo.IResultEntry;
import net.lamida.nd.rest.neo.ISearch;
import net.lamida.nd.rest.neo.SearchResult;
import net.lamida.nd.rest.neo.SortBy;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author lamida
 */
@SuppressWarnings("serial")
public class NewsDownloaderForm extends javax.swing.JFrame {
	private Log log = LogFactory.getLog(this.getClass().toString());
    private DefaultTableModel model;
    private ISearch currentSearch;
    private int selectedCount;
    private File saveFile;
    

    /**
     * Creates new form NewsDownloaderForm
     */
    public NewsDownloaderForm() {
    	log.info("init");
        initComponents();
        addLinkListener();
        addTableHeaderClickListener();
        model = (DefaultTableModel) table.getModel();
        textOutputFile.setText(new File("result/output.pdf").getAbsolutePath());
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        comboModel.addElement(SortBy.ALJAZEERA_RELEVANCE.getDescription());
        comboModel.addElement(SortBy.ALJAZEERA_DATE.getDescription());
        sortByCombo.setModel(comboModel);
    }

	private void addTableHeaderClickListener() {
		table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                selectResultToText.setText("");
                selectResultToText.requestFocusInWindow();
                selectAllDialog.pack();
                selectAllDialog.setModal(true);
                selectAllDialog.setLocationRelativeTo(null);
                selectAllDialog.setModal(true);
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
                if (column != 4) {
                    return;
                }
                String link = (String) target.getValueAt(row, column);
                try {
                    log.info(Desktop.isDesktopSupported());
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI(link));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(NewsDownloaderForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
	}

    private void clearTableData() {
        while (model.getRowCount() != 0) {
            model.removeRow(model.getRowCount() - 1);
        }
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
        //int currentPage = currentSearch.getCurrentStartPage() / currentSearch.getResultPerPage();
        //int lastPage = (int) (Math.ceil((result.getSearchInformation().getTotalResults() * 1.0) / Constant.RESULTS_PER_PAGE));
        //executionTimeLabel.setText("About " + result.getSearchInformation().getTotalResults() + " results (" + result.getSearchInformation().getSearchTime() + "seconds)");
        //pagingInfoLabel.setText("Result Start " + loadedSearch.getResultStart() + " Page " + (currentPage + 1) + " of " + lastPage);
        pagingInfoLabel.setText(currentSearch.getSearchMetaInfo());
        //enableDisableButton(currentPage, lastPage);
    }
    
    private Object[][] convertToArray(SearchResult search) {
        Object[][] data = new Object[search.getResultList().size()][6];
        int row = 0;
        for (IResultEntry item : search.getResultList()) {
            data[row][0] = row + 1;
            data[row][1] = item.getDate();
            data[row][2] = item.getTitle();
            data[row][3] = item.getSnipet();
            data[row][4] = item.getUrl();
            data[row][5] = item.isSelected();
            row++;
        }
        return data;
    }

    private void updateSelected() {
        SearchResult currentResult = currentSearch.getSearchResult();
        int row = 0;
        for (IResultEntry it : currentResult.getResultList()) {
            boolean selected = (Boolean) model.getValueAt(row, 5);
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
        aboutDialog = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
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
        pagingInfoLabel = new javax.swing.JLabel();
        nextButton = new javax.swing.JButton();
        textOutputFile = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        jLabel8 = new javax.swing.JLabel();
        sortByCombo = new javax.swing.JComboBox(SearchProviderEnum.values());
        jButtonExportResult = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

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

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("NewsDownloader 1.0\n\nDownload news from:\nAljazeera\nCNN\nChannel News Asia\n\nEspecially dedicated for lovely Indri Rizkina Hapsari <3\n\nSource Code:\nhttps://github.com/lamida/article-downloader\n\nme@lamida.net\n\n2013");
        jScrollPane3.setViewportView(jTextArea1);

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout aboutDialogLayout = new javax.swing.GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        aboutDialogLayout.setVerticalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Data Provider:");

        dataProviderCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataProviderComboActionPerformed(evt);
            }
        });

        queryText.setColumns(20);
        queryText.setRows(5);
        queryText.setToolTipText("You can separate the keywords using space");
        jScrollPane1.setViewportView(queryText);

        jLabel2.setText("Search Query:");

        countKeywordsCb.setSelected(true);
        countKeywordsCb.setText("Count Keywords");
        countKeywordsCb.setToolTipText("When checked, in header of document result will show number of keyword inside document");

        highlightKeywordsCb.setSelected(true);
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
                "No", "Date", "Title", "Snippet", "URL", "Select All"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(table);
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(2).setCellRenderer(new PathCellRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new PathCellRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new PathCellRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
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

        jLabel8.setText("Sort By:");

        jButtonExportResult.setText("Export Result");
        jButtonExportResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportResultActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItem1.setText("About");
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
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(saveButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textOutputFile, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(countKeywordsCb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(highlightKeywordsCb)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 272, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pagingInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(searchButton)
                                            .addComponent(dataProviderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(sortByCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nextButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExportResult)))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(sortByCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchButton)
                .addGap(31, 31, 31)
                .addComponent(pagingInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextButton)
                    .addComponent(jButtonExportResult))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countKeywordsCb)
                    .addComponent(highlightKeywordsCb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(textOutputFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
    	log.info("searchButtonActionPerformed");
        if (queryText.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please Enter Search Query");
            return;
        }
        
        currentSearch = AbstractSearch.getSearchProvider(SearchProviderEnum.getSearchProviderEnumByName(dataProviderCombo.getSelectedItem().toString()));
        // TBD
        currentSearch.init(queryText.getText(), null, SortBy.getEnumByDescription(dataProviderCombo.getSelectedItem().toString(), sortByCombo.getSelectedItem().toString()));
        currentSearch.search();
        showResult(currentSearch.getSearchResult());
    }//GEN-LAST:event_searchButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
    	log.info("saveButtonActionPerformed");
        updateSelected();
        selectedCount = 0;
        for (IResultEntry it : currentSearch.getSearchResult().getResultList()) {
            if (it.isSelected()) {
                selectedCount++;
            }
        }
        if (selectedCount != 0) {
        	String folderPath = textOutputFile.getText().substring(0,textOutputFile.getText().lastIndexOf(File.separatorChar));
        	File folder = new File(folderPath);
        	if(!folder.exists()){
        		if(!folder.mkdirs()){
        			JOptionPane.showMessageDialog(this, "Cannot create directory " + folderPath); 
        			return;
        		}
        	}
            saveFile = new File(textOutputFile.getText());
            if (saveFile.exists()) {
                int x = JOptionPane.showConfirmDialog(NewsDownloaderForm.this, "File with same name exist. Would you like to overwrite?");
                if (x != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            doDownload();
        } else {
            JOptionPane.showMessageDialog(NewsDownloaderForm.this, "Please select some checkbox first");
        }

	}//GEN-LAST:event_saveButtonActionPerformed

	private void joinPdf() throws FileNotFoundException {
		log.info("joinPdf");
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
	}

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        navigateNext();
    }//GEN-LAST:event_nextButtonActionPerformed
    
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "pdf";
			}
			
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(".pdf");
			}
		});
        int i = fc.showSaveDialog(this);
        if(i == JFileChooser.APPROVE_OPTION){
        	String file = fc.getSelectedFile().getAbsolutePath();
	        textOutputFile.setText(file.endsWith(".pdf") ? file : file + ".pdf");
	        saveFile = fc.getSelectedFile();
	        
        }
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
            int selectAllFrom = 0;
            int selectAllTo = 0;
            
            if(selectResultToText.getText().indexOf(',') != -1){
            	selectAllFrom = Integer.parseInt(selectResultToText.getText().split(",")[0]);
            	selectAllTo = Integer.parseInt(selectResultToText.getText().split(",")[1]);
            }else{
            	selectAllTo = Integer.parseInt(selectResultToText.getText());
            }
            
            if (selectAllTo < selectAllFrom ){
            	JOptionPane.showMessageDialog(this, "From must be > To", "Error", JOptionPane.ERROR_MESSAGE);
            	return;
            }
            
            if (selectAllTo - selectAllFrom > 100) {
                JOptionPane.showMessageDialog(this, "Maximum Article to Select is 100", "Error", JOptionPane.ERROR_MESSAGE);
                selectAllFrom = 0;
                selectAllTo = 100;
            }
            if(currentSearch.getSearchResult().getResultList().isEmpty()){
        		JOptionPane.showMessageDialog(this, "Empty Search Result", "Error", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
            selectAllData(selectAllFrom, selectAllTo);
            selectAllDialog.setVisible(false);
            showResult(currentSearch.getSearchResult());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_selectAllAcceptButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        aboutDialog.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.pack();
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void dataProviderComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataProviderComboActionPerformed
        // TODO add your handling code here:
        String selectedProvider = dataProviderCombo.getSelectedItem().toString();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        if(selectedProvider.equals(SearchProviderEnum.CNN.toString())){
            model.addElement(SortBy.CNN_DATE);
            model.addElement(SortBy.CNN_RELEVANCE);
        }else if(selectedProvider.equals(SearchProviderEnum.CNA.toString())){
            model.addElement(SortBy.CNA_LATEST);
            model.addElement(SortBy.CNA_POPULARITY);
        }else{
            model.addElement(SortBy.ALJAZEERA_RELEVANCE);
            model.addElement(SortBy.ALJAZEERA_DATE);
        }
        sortByCombo.setModel(model);
    }//GEN-LAST:event_dataProviderComboActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        aboutDialog.pack();
        aboutDialog.setLocationRelativeTo(null);
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButtonExportResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportResultActionPerformed
        // TODO add your handling code here:
    	exportResultToCsv();
    }//GEN-LAST:event_jButtonExportResultActionPerformed

    private void exportResultToCsv(){
    	StringBuffer csv = new StringBuffer("No,Date,Title,Snipet,Url\n");
    	int i = 1;
    	for(IResultEntry entry : currentSearch.getSearchResult().getResultList()){
    		csv
    		.append(i++)
    		.append(",")
    		.append("\"").append(entry.getDate()).append("\"")
    		.append(",")
    		.append("\"").append(entry.getTitle()).append("\"")
    		.append(",")
    		.append("\"").append(entry.getSnipet()).append("\"")
    		.append(",")
    		.append(entry.getUrl())
    		.append("\n");
    	}
    	String fileName = new Date().getTime() + "_" + queryText.getText().replace(" ", "_") + "_" + dataProviderCombo.getSelectedItem().toString() + ".csv";
    	try {
    		File out = new File("result",fileName);
    		FileUtils.writeStringToFile(out, csv.toString());
    		JOptionPane.showMessageDialog(this, "Result succesfully exported to " + out.getAbsolutePath());
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
    }
    
    private void selectAllData(int selectAllFrom, int selectAllTo){
    	while(selectAllTo > currentSearch.getSearchResult().getResultList().size()){
    		System.out.println(selectAllTo + ">>" + currentSearch.getSearchResult().getResultList().size());
    		if(!navigateNext()){
    			break;
    		}
    	}
    	for(int i = 0; i <= currentSearch.getSearchResult().getResultList().size() - 1; i++){
    		IResultEntry item = currentSearch.getSearchResult().getResultList().get(i);
    		item.setSelected(false);
    	}
    	for(int i = 0; i <= (selectAllTo < currentSearch.getSearchResult().getResultList().size() ? selectAllTo : currentSearch.getSearchResult().getResultList().size() - 1); i++){
            IResultEntry item = currentSearch.getSearchResult().getResultList().get(i);
            if(i >= selectAllFrom-1 && i < selectAllTo){
                    item.setSelected(true);
            }else{
                    item.setSelected(false); 
            }
    	}
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog aboutDialog;
    private javax.swing.JButton browseButton;
    private javax.swing.JCheckBox countKeywordsCb;
    private javax.swing.JComboBox dataProviderCombo;
    private javax.swing.JCheckBox highlightKeywordsCb;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonExportResult;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel pagingInfoLabel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextArea queryText;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton selectAllAcceptButton;
    private javax.swing.JDialog selectAllDialog;
    private javax.swing.JTextField selectResultToText;
    private javax.swing.JComboBox sortByCombo;
    private javax.swing.JTable table;
    private javax.swing.JTextField textOutputFile;
    // End of variables declaration//GEN-END:variables

    private void doDownload() {
    	log.info("doDownload");
    	progressBar.setStringPainted(true);
        progressBar.setIndeterminate(false);
        progressBar.setMaximum(100);
        new SwingWorker<Object, Object>() {
        	boolean saveError = false;
            @Override
            protected Object doInBackground() {
                IParser parser = AbstractParser.getParser(SearchProviderEnum.getSearchProviderEnumByName(dataProviderCombo.getSelectedItem().toString()));
                INewsPdfWriter pdfWriter = new NewsPdfWriter();

                
                String prefixCount = "00000";
                
                int count = 0;
                for (IResultEntry it : currentSearch.getSearchResult().getResultList()) {
                    if (it.isSelected()) {
                        count++;
                        progressBar.setValue(100 * count / selectedCount);
                        progressBar.setString("Downloading: " + it.getTitle());
                        progressBar.setToolTipText("Downloading: " + it.getTitle());
                        parser.init(it.getUrl());
                        
                        
                		String prefix = prefixCount;
                        String countSt = count + "";
                        prefix = prefixCount.substring(0, prefixCount.length() - countSt.length()) + count;
                        
                        String targetFileName = prefix + " - " + it.getTitle().replace(":", "") + ".pdf";
                        log.info("Downloading " + targetFileName);
                        PdfInputData data = null;
                        try{
                        	data = new PdfInputData(queryText.getText(), it.getUrl(), parser.getNewsTitle(), parser.getNewsContent(), parser.getNewsPostTime(), count);
                        	pdfWriter.init(data, targetFileName, countKeywordsCb.isSelected(), highlightKeywordsCb.isSelected(), currentSearch.getSearchResult().getResultList().size());
                        	pdfWriter.writePdf();
                        }catch(Exception e){
                        	log.error("Warning: some error is happening: " + e.getMessage());
                        }
                    }
                }
                try {
                	joinPdf();
        		} catch (FileNotFoundException e) {
        			saveError = true;
        			JOptionPane.showMessageDialog(NewsDownloaderForm.this, "Error Saving " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        		}
                
                return null;
            }

            @Override
            protected void done() {
                progressBar.setValue(0);
                progressBar.setString(null);
                if(!saveError){
                	JOptionPane.showMessageDialog(NewsDownloaderForm.this, "All Articles have been downloaded!");
                }
            }
        }.execute();
    }
    
//    private void navigateTo(int resultStart){
//    	if(resultStart > loadedSearch.getResultStart()){
//    		while(resultStart != loadedSearch.getResultStart()){
//    			navigateNext();
//    		}
//    	}else{
//    		while(resultStart != loadedSearch.getResultStart()){
//    			navigatePrev();
//    		}
//    	}
//    }
    
    private boolean navigateNext() {
        updateSelected();
        boolean available = currentSearch.next();
        if(available){
        	showResult(currentSearch.getSearchResult());
        }else{
        	JOptionPane.showMessageDialog(this, "No more search result");
        }
        return available;
    }

    private boolean navigatePrev() {
    	// NOT IMPLEMENTED
    	return false;
    }
}

/**
 * Tooltip for column
 * @author lamida
 *
 */
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