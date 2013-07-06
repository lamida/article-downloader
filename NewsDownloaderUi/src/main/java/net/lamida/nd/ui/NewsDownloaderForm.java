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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.lamida.nd.Constant;
import net.lamida.nd.Utils;
import net.lamida.nd.bean.SearchResult;
import net.lamida.nd.bean.SearchResultItem;
import net.lamida.nd.parser.AbstractParser;
import net.lamida.nd.parser.IParser;
import net.lamida.nd.pdf.NewsPdfWriter;
import net.lamida.nd.rest.AbstractRestSearch;
import net.lamida.nd.rest.IRestSearch;
import net.lamida.nd.rest.SearchProviderEnum;
import net.lamida.nd.rest.SearchResultBuilder;


/**
 *
 * @author lamida
 */
public class NewsDownloaderForm extends javax.swing.JFrame {

    private int resultStart = 1;
    private Map<Integer, SearchResult> searchCache;
    private DefaultTableModel model;
    private IRestSearch currentSearch;
    private boolean dummyData = false;
    private long totalResults;
    /**
     * Creates new form NewsDownloaderForm
     */
    public NewsDownloaderForm() {
        searchCache = new HashMap<Integer, SearchResult>();
        initComponents();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int row = target.getSelectedRow();
                int column = target.getSelectedColumn();
                if(column != 3){
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
        model = (DefaultTableModel) table.getModel();
        textOutputFile.setText(new File("output.pdf").getAbsolutePath());
    }
    
    private void clearTableData(){
        while(model.getRowCount() != 0)
        model.removeRow(model.getRowCount() - 1);
    }
    
    private SearchResult getDummySearchResult(){
        SearchResultBuilder builder = new SearchResultBuilder();
        SearchResult result = null;
        try {
            result = builder.build(Utils.readFileToString(new File("googleSearchAljazeera.txt")));
        } catch (IOException ex) {
            Logger.getLogger(NewsDownloaderForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private Object[][] convertToArray(SearchResult search) {
        Object[][] data = new Object[10][5];
        int row = 0;
        for (SearchResultItem item : search.getItems()) {
            data[row][0] = resultStart + row ;
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
    
    private void showResult(SearchResult result){
        clearTableData();
        Object[][] data = convertToArray(result);
        for(Object[] row : data){
            model.addRow(row);
        }
        int currentPage = resultStart / Constant.RESULTS_PER_PAGE;
        int lastPage = (int) (result.getSearchInformation().getTotalResults() / Constant.RESULTS_PER_PAGE);
        executionTimeLabel.setText("About " + result.getSearchInformation().getTotalResults() + " results (" + result.getSearchInformation().getSearchTime() + "seconds)");
        pagingInfoLabel.setText("Page " + (currentPage + 1) + " of " + lastPage);
        if(currentPage > 0){
            prevButton.setEnabled(true);
        }else{
            prevButton.setEnabled(false);
        }
        if(currentPage < lastPage){
            nextButton.setEnabled(true);
        }else{
            nextButton.setEnabled(false);
        }
    }
    
    private void updateSelected(){
        SearchResult currentResult = searchCache.get(resultStart);
        int row = 0;
        for(SearchResultItem it:currentResult.getItems()){
            boolean selected = (Boolean)model.getValueAt(row, 4);
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

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
                "No", "Title", "Snippet", "URL", "Select"
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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(searchButton)
                                    .addComponent(dataProviderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(executionTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pagingInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(saveButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textOutputFile, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseButton)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                .addGap(18, 18, 18)
                .addComponent(searchButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(executionTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pagingInfoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prevButton)
                    .addComponent(nextButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countKeywordsCb)
                    .addComponent(highlightKeywordsCb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(textOutputFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if(queryText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Please Enter Search Query");
            return;
        }
        searchCache.clear();
        if(dummyData){
            SearchResult searchResult = getDummySearchResult();
            searchCache.put(resultStart, searchResult);
        }else{
            currentSearch = AbstractRestSearch.getSearchProvider(SearchProviderEnum.getSearchProviderEnumByName(dataProviderCombo.getSelectedItem().toString()));
            currentSearch.setQuery(queryText.getText());
            String json = currentSearch.execute();
            SearchResult searchResult = new SearchResultBuilder().build(json);
            searchCache.put(resultStart, searchResult);
        }
        showResult(searchCache.get(resultStart));
    }//GEN-LAST:event_searchButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        
        updateSelected();
        IParser parser = AbstractParser.getParser(SearchProviderEnum.getSearchProviderEnumByName(dataProviderCombo.getSelectedItem().toString()));
        NewsPdfWriter pdfWriter = null;
        int count = 0;
        for(SearchResult result : searchCache.values()){
            for(SearchResultItem it:result.getItems()){
               if(it.isSelected()){
                    count++;
                    parser.init(it.getLink());
                    pdfWriter = new NewsPdfWriter(queryText.getText(), it.getLink(), parser.getNewsTitle(), parser.getNewsContent(), parser.getNewsPostTime(), countKeywordsCb.isSelected(), highlightKeywordsCb.isSelected());
                    pdfWriter.writePdf(new File("temp", it.getTitle().replace(":", "") + ".pdf"));
               }
            }
        }
        if(count != 0){
            if(saveFile == null){
                saveFile = new File(textOutputFile.getText());
            }
            if(saveFile.exists()){
                int x = JOptionPane.showConfirmDialog(this, "File with same name exist. Would you like to overwrite?");
                if(x != JOptionPane.YES_OPTION){
                    return;
                }
            }
            String path = saveFile.getAbsolutePath();
            if(!path.endsWith(".pdf")){ 
                path += ".pdf";
            }
            new NewsPdfWriter().joinPdf("temp", path);
            JOptionPane.showMessageDialog(this, "File saved at " + path);
        }else{
            JOptionPane.showMessageDialog(this, "Please select some checkbox first");
        }
	}//GEN-LAST:event_saveButtonActionPerformed

    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        if(resultStart == 0){
            return;
        }
        updateSelected();
        resultStart -= Constant.RESULTS_PER_PAGE;
        if(searchCache.get(resultStart) == null){
            if(dummyData){
                SearchResult searchResult = getDummySearchResult();
                searchCache.put(resultStart, searchResult);
            }else{
                String json = currentSearch.prev();
                SearchResult searchResult = new SearchResultBuilder().build(json);
                searchCache.put(resultStart, searchResult);
            }
        }
        showResult(searchCache.get(resultStart));
    }//GEN-LAST:event_prevButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        updateSelected();
        resultStart += Constant.RESULTS_PER_PAGE;
        if(searchCache.get(resultStart) == null){
            if(dummyData){
                SearchResult searchResult = getDummySearchResult();
                searchCache.put(resultStart, searchResult);
            }else{
                String json = currentSearch.next();
                SearchResult searchResult = new SearchResultBuilder().build(json);
                searchCache.put(resultStart, searchResult);
            }
        }
        showResult(searchCache.get(resultStart));
    }//GEN-LAST:event_nextButtonActionPerformed

    private File saveFile;
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        final JFileChooser fc = new JFileChooser();
        int i = fc.showSaveDialog(this);
        textOutputFile.setText(fc.getSelectedFile().getAbsolutePath());
        saveFile = fc.getSelectedFile();
    }//GEN-LAST:event_browseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JCheckBox countKeywordsCb;
    private javax.swing.JComboBox dataProviderCombo;
    private javax.swing.JLabel executionTimeLabel;
    private javax.swing.JCheckBox highlightKeywordsCb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel pagingInfoLabel;
    private javax.swing.JButton prevButton;
    private javax.swing.JTextArea queryText;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTable table;
    private javax.swing.JTextField textOutputFile;
    // End of variables declaration//GEN-END:variables

    private File cleanTempFolder(final JFileChooser fc) {
        File saveFile = fc.getSelectedFile();
        File temp = new File("temp");
        temp.mkdir();
        for(File f:temp.listFiles()){
            f.delete();
        }
        return saveFile;
    }

    

}
class PathCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
                        JTable table, Object value,
                        boolean isSelected, boolean hasFocus,
                        int row, int column) {
        JLabel c = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setToolTipText(c.getText());
        return c;
    }
}
