package dit.panels;

import dit.*;
import dit.DEIDGUI;
import dit.DeidData;
import dit.DemographicTableModel;
import dit.EditDemoDataFrame;
import dit.XlsFile;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author christianprescott && angelo
 */
public class LoadDemoPanel extends javax.swing.JPanel implements WizardPanel {
    private EditDemoDataFrame Editdemo;
     final JTextField textField = new JTextField();
     private int[] selectedCols; //for generalize data;
     public static String IdHeaderName = new String(""); 
    /**
     * Creates new form LoadDemoPanel
     */
    public LoadDemoPanel() {     
        
        initComponents();
        DEIDGUI.title = "Load Demographic Data";
        DEIDGUI.helpButton.setEnabled(true);
        
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        genrCurColButton.setEnabled(false);
        revColButton.setEnabled(false);
       
       
        // TODO: remove this auto-load line
        //        ReadDemographicFile(new File("/Users/christianprescott/Desktop/dataset/my_demo_data.txt"));
      //  jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setRowSelectionAllowed(false);
        jTable1.setRowHeight(26);
        
         if(DeidData.demographicData!=null)
         {
             lblInstrc.setVisible(false);
         }
         else
         {
             lblInstrc.setVisible(true);
         }
        
         
         ///////////////////////////////////////////////////////
         //change a single cell value in DData.demograph&jTable1
         ///////////////////////////////////////////////////////
        Action action = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                
                TableCellListener tcl = (TableCellListener)e.getSource();
                int i=tcl.getRow();
                int j=tcl.getColumn();
                String oldValue=tcl.getOldValue().toString();
                String newValue=tcl.getNewValue().toString();
                if (newValue.trim().equals("")){
                    JOptionPane.showMessageDialog(null,
                            "WARNING: This cell may not be empty!", "Warning Massage",
                            JOptionPane.WARNING_MESSAGE);
                }
                if(!oldValue.equals(newValue))
                {
                    final int choice;
                    choice= JOptionPane.showConfirmDialog(LoadDemoPanel.this, "Make sure this change?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
                    if(choice== JOptionPane.YES_OPTION)
                    {
                        DeidData.demographicData.setValueAt(newValue, i, j);
                        DeidData.demoFileModified=true;
                        //correctDemoModel.setValueAt(textField.getText(), i, j);
                        jTable1.setValueAt(newValue, i, j);
                        // System.out.println("hit one"+i+" and " + j+textField.getText());
                        jTable1.setDefaultRenderer(Object.class, new missingValueRenderer());
                        jTable1.clearSelection();
                    }
                    else
                    {
                        DeidData.demographicData.setValueAt(oldValue, i, j);
                        //correctDemoModel.setValueAt(textField.getText(), i, j);
                        jTable1.setValueAt(oldValue, i, j);
                        // System.out.println("hit one"+i+" and " + j+textField.getText());
                        jTable1.setDefaultRenderer(Object.class, new missingValueRenderer());
                        jTable1.clearSelection();
                    }
                }
              
            }
        };
        TableCellListener tcl = new TableCellListener(jTable1, action);
      //  jTable1.addPropertyChangeListener(tcl);
        
        if(DeidData.demographicData!=null)
        {
            jTable1.setModel(DeidData.demographicData);
        }
        
        //Sort the Table in according to this column
        jTable1.getTableHeader().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    int columnIndex = jTable1.getTableHeader().columnAtPoint(e.getPoint());
                    DeidData.IdColumn = columnIndex;
                    ArrayList rowList = new ArrayList(Arrays.asList(DeidData.demographicData.getData()));
                    Collections.sort(rowList, new DemoRowComparator());
                    Object[][] rows = new Object[rowList.size()][];
                    rowList.toArray(rows);
                    DeidData.demographicData = new DemographicTableModel(DeidData.demographicData.getDataFieldNames(), rows);
                    jTable1.getTableHeader().removeMouseListener(this);
                    jTable1.setModel(DeidData.demographicData);
                    jTable1.setColumnSelectionInterval( DeidData.IdColumn,  DeidData.IdColumn);
                    jTable1.getTableHeader().addMouseListener(this);
                // jTable1.setRowSelectionAllowed(true);
                // System.out.println("click");
                } 
            }
           
        });
        
        
        ///////////////////////////////////////////////////
        /////2.13
        //generalize the selected col.        
        
        jTable1.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                //  int columnIndex = jTable1.getSelectedColumn().columnAtPoint(e.getPoint()); 
                    //int colIndex = jTable1.columnAtPoint(e.getPoint());
                    selectedCols = jTable1.getSelectedColumns();
                    //selectedCol = colIndex;                   
                    Boolean noIdColumn = true;
                    Boolean isNum = true;
                    //Boolean noDateColumn = true;
                    
                    for(int col : selectedCols ){
                        String colName = jTable1.getColumnName(col); 
                        if(colName.equals(IdHeaderName)){
                            noIdColumn = false;
                        }
                        
                        for (int i = 0; i < jTable1.getRowCount(); i++) { 
                            String data;
                            if ( (data = jTable1.getValueAt(i, col).toString()) != null &&
                                data != "misV" && !isNumeric(data))
                                    isNum = false;

                                //if(data.indexOf('/') != -1 || data.indexOf('-') != -1){ //either one is date.
                                //    noDateColumn = false;
                                //}                  
                                                              
                        }
                    }
                    if(noIdColumn && isNum){ // no Id Col && no Date Col 
                        genrCurColButton.setEnabled(true);
                        revColButton.setEnabled(true);
                    }
                     else{
                        genrCurColButton.setEnabled(false);
                        revColButton.setEnabled(false);
                    }
                } 
        });

        
        if (DeidData.demographicData != null) {
            jTable1.setModel(DeidData.demographicData);
            jTable1.setColumnSelectionInterval(DeidData.IdColumn, DeidData.IdColumn);
            jTable1.setDefaultRenderer(Object.class, new missingValueRenderer());
            DEIDGUI.continueButton.setEnabled(true);
            DEIDGUI.log("Loaded existing demographic table data");
        } else {
            // DeidData.demographicData= DemographicTableModel.dummyModel;
            DEIDGUI.continueButton.setEnabled(false);
        }
        
        
        final JTextField textField = new JTextField();
        for(int i=0; i< jTable1.getColumnCount(); i++)
        {
            jTable1.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(textField));
        }
        jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        textField.setEditable(true);
        cbxDummy.setSelected(DeidData.isNoData);
        DEIDGUI.log("LoadDemoPanel initialized");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblInstrc = new javax.swing.JLabel();
        jButtonLoadDemo = new javax.swing.JButton();
        btnCancleChange = new javax.swing.JButton();
        cbxDummy = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        genrCurColButton = new javax.swing.JButton();
        revColButton = new javax.swing.JButton();

        lblInstrc.setText("<html><p>Select a data file, then click the column that will be used to match the images.</p><p>&nbsp;</p></html>");

        jButtonLoadDemo.setText("Choose Data File...");
        jButtonLoadDemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadDemoActionPerformed(evt);
            }
        });

        btnCancleChange.setText("Revert All Changes");
        btnCancleChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancleChangeActionPerformed(evt);
            }
        });

        cbxDummy.setText("No data file/image file share only");
        cbxDummy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxDummyActionPerformed(evt);
            }
        });

        jTable1.setModel(new DemographicTableModel(new String[]{"No data"}, new Object[1][1]));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(jTable1);

        genrCurColButton.setText("Generalize Selected Column(s)");
        genrCurColButton.setEnabled(false);
        genrCurColButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genrCurColButtonActionPerformed(evt);
            }
        });

        revColButton.setText("Revert Selected Column(s)");
        revColButton.setEnabled(false);
        revColButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revColButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1)
                    .add(lblInstrc, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jButtonLoadDemo)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cbxDummy)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(genrCurColButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(revColButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancleChange)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {btnCancleChange, genrCurColButton, revColButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(lblInstrc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonLoadDemo)
                    .add(cbxDummy))
                .add(7, 7, 7)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancleChange)
                    .add(genrCurColButton)
                    .add(revColButton))
                .add(6, 6, 6))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButtonLoadDemoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadDemoActionPerformed
        final javax.swing.JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new DemoFilter());
        String dirrec;
        File filename = new File("/tmp/textpath.txt");
        try{
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            dirrec = br.readLine();
            // System.out.println(dirrec);
            if (dirrec!= null)
            {
                fc.setCurrentDirectory(new File(dirrec));
            }
            
        }catch(IOException e)
        {
            fc.setCurrentDirectory(null);
        }
        
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file.canRead()) {
                getIdHeaderName(file);
                ReadDemographicFile(file);
                
                
                DeidData.isNoData=false; 
                 cbxDummy.setSelected(DeidData.isNoData);
            } else {
                JOptionPane.showMessageDialog(this, "This file could not "
                        + "be opened.", "Invalid Demographic File",
                        JOptionPane.ERROR_MESSAGE);
            }
            String dir = fc.getSelectedFile().getParent();
            //System.out.println(dir);
            
            
            if (!filename.exists()){
                try{
                    filename.createNewFile();
                }
                catch (IOException e) {
                    DEIDGUI.log("Fail to create file!" );
                }                
            }
            try
            {
                
                RandomAccessFile  pathfile = new RandomAccessFile (filename,"rw");
                pathfile.writeBytes(dir);
            }catch(IOException e)
            {
                DEIDGUI.log("No Parent Directory Found!" );
            }
        }
         if(DeidData.demographicData!=null)
         {
             lblInstrc.setVisible(false);
         }
         else
         {
             lblInstrc.setVisible(true);
         }
    }//GEN-LAST:event_jButtonLoadDemoActionPerformed
        
    private void btnCancleChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancleChangeActionPerformed
        
        if(DeidData.demoSourceFile != null)
        {
            int choice = JOptionPane.showConfirmDialog(this, "Would you discard all the change?",
                    "Revert", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(choice== JOptionPane.OK_OPTION) {
                ReadDemographicFile(DeidData.demoSourceFile);
                genrCurColButton.setEnabled(false);
                revColButton.setEnabled(false);
            }
            
        }
        else
        {
            JOptionPane.showMessageDialog(this, "No previous demographic file!","Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCancleChangeActionPerformed

    private void cbxDummyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxDummyActionPerformed
        if(cbxDummy.isSelected())
        {
            File dummyDemographic=new File(DeidData.outputPath+"/dummy.txt");
            if(!dummyDemographic.exists())
            {
                try {
                    dummyDemographic.createNewFile();
                    FileWriter fw=new FileWriter(dummyDemographic.getAbsoluteFile());
                    BufferedWriter bw=new BufferedWriter(fw);
                    String content="Fake Demographic File ID\nMissing ID";
                    bw.write(content);
                    bw.close();
                } catch (IOException ex) {
                    Logger.getLogger(LoadDemoPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ReadDemographicFile(dummyDemographic);
            lblInstrc.setVisible(false);
          //  DeidData.isNoData=true;
        }
        else
        {
           // DeidData.isNoData=false;
            DeidData.demographicData=null;
            DeidData.demoSourceFile=null;
            DEIDGUI.continueButton.setEnabled(false);
            jTable1.setModel(new DefaultTableModel());
            lblInstrc.setVisible(true);
        }
        DeidData.isNoData=cbxDummy.isSelected();
    }//GEN-LAST:event_cbxDummyActionPerformed

    
////////////////////////////////////////
///2.15
//recover the selected row.    
    private void revColButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revColButtonActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Revert Selected Column(s)?",
        "Revert", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(choice== JOptionPane.OK_OPTION){
            if(DeidData.sourcedemographicData != null){
                jTable1.setModel(DeidData.demographicData);
                for(int col : selectedCols){
                    for(int i = 0; i < jTable1.getRowCount(); i++){
                        
                        Object newvar = DeidData.sourcedemographicData.getValueAt(i, col);
                        Object oldvar = DeidData.demographicData.getValueAt(i, col);
                        //System.out.println(newvar+" "+oldvar);
                        DeidData.demographicData.setValueAt(newvar, i, col);                                                                           
                        jTable1.setValueAt(newvar, i, col);
                        
                        DeidData.demographicData.refresh();
                    }
                }

             }
            else{
                System.out.println("Failed to revert");
            }        
        }
    }//GEN-LAST:event_revColButtonActionPerformed

    
    ////////////////////////////////////////
    /////2.15
    //get the name and save in idHeaderName;
    private void getIdHeaderName(File file){ 
        
        //removed xlsx file format due to library copyright.
        if (file.getName().endsWith(".xls") ){
            XlsFile xls = new XlsFile();
            xls.setInputFile(file.getAbsolutePath());
            System.out.println(file.getAbsolutePath());
            String[] fields;
            try{
                fields = xls.readHeadings();
                IdHeaderName = fields[0];
            }
            catch(IOException e){
                 JOptionPane.showMessageDialog(this, "This file could not "
                  + "be opened.", "Invalid Demographic File",
                  JOptionPane.ERROR_MESSAGE);
            }
        }  
        
        else {
        
            Scanner inputStream = null;
            try {
                inputStream = new Scanner(file);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "This file could not "
                    + "be opened.", "Invalid Demographic File",
                    JOptionPane.ERROR_MESSAGE);
            }
            String[] fields;
            if (inputStream.hasNextLine()) {
                String line = null;
                while (inputStream.hasNextLine() &&
                    "".equals((line = inputStream.nextLine()).trim()));
                                           
                    // Read data field headings from first line
                fields = line.split("\t");
                
                // Read data field headings from first line
                //fields = inputStream.nextLine().split("\t");
                System.out.println("the name of Id field is: "+fields[0]);
                IdHeaderName = fields[0];
                inputStream.close();
                return;
            }
             else {
                // File is empty
                inputStream.close();
                JOptionPane.showMessageDialog(this, "This file does not contain "
                    + "any demographic data.", "Invalid Demographic File",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
///////////////////////////////////
///////2.15
//generalize data.
    private void genrCurColButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genrCurColButtonActionPerformed
        // perform generalize data for the selected row
        int choice = JOptionPane.showConfirmDialog(this, "Generalize Selected Column(s)?",
        "Generalize", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(choice== JOptionPane.OK_OPTION){
            //System.out.println("the chosen row is: "+colName);
            DeidData.demoFileModified=true;
            for(int selectedCol : selectedCols){
                for(int i = 0; i < jTable1.getRowCount(); i++){
                    //System.out.println("the old value is: "+jTable1.getValueAt(i, selectedCol));                   
                    Object oldvar = jTable1.getValueAt(i, selectedCol);
                    if ( ! oldvar.equals("misV")) {
                        Double tmpvar = Double.parseDouble(oldvar.toString() );
                        int var = tmpvar.intValue();
                        var = (var/5)*5;
                        //System.out.println("the new value is: "+var);
                        String newvar = Integer.valueOf(var).toString();
                        DeidData.demographicData.setValueAt(newvar, i, selectedCol);                                               
                        jTable1.setValueAt(newvar, i, selectedCol);
                        jTable1.setModel(DeidData.demographicData);
                        DeidData.demographicData.refresh();
                    }
                }
            }
        }
    }//GEN-LAST:event_genrCurColButtonActionPerformed
    
    private void writeBack(File dest) throws IOException, WriteException
    {
        String extension= FileUtils.getExtension(dest);
        FileWriter fw=new FileWriter(dest);
        if(extension.equals("txt"))
        {
            StringBuilder header=new StringBuilder();
            TableColumnModel headerColumn=jTable1.getTableHeader().getColumnModel();
            for(int i=0;i<headerColumn.getColumnCount();i++)
            {
               header.append(headerColumn.getColumn(i).getHeaderValue());
               if(i!=headerColumn.getColumnCount()-1)
                   header.append("\t");
            }
            header.append(System.getProperty("line.separator"));
            fw.write(header.toString());
            for(int i=0;i<jTable1.getRowCount();i++)
            {
                StringBuilder line=new StringBuilder();
                for(int j=0;j<jTable1.getColumnCount();j++)
                {
                   line.append((String)jTable1.getValueAt(i, j));
                   if(j!=jTable1.getColumnCount()-1)
                       line.append("\t");
                }
                if(i!=jTable1.getRowCount()-1)
                    line.append(System.getProperty("line.separator"));
                fw.write(line.toString());
            }
            fw.close();
        }
        else if(extension.equals("xls"))
        {
            WritableWorkbook workbook = Workbook.createWorkbook(dest);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            TableColumnModel headerColumn=jTable1.getTableHeader().getColumnModel();
            for(int i=0;i<headerColumn.getColumnCount();i++)
            {
                Label label = new Label(i, 0, headerColumn.getColumn(i).getHeaderValue().toString());
                sheet.addCell(label);
            }
            for(int i=0;i<jTable1.getRowCount();i++)
            {
                for(int j=0;j<jTable1.getColumnCount();j++)
                {
                    Label label = new Label(j, i+1, (String)jTable1.getValueAt(i, j));
                    sheet.addCell(label);
                }
            }
            workbook.write();
            workbook.close();
        }
        else if(extension.equals("xlsx"))
        {
            org.apache.poi.ss.usermodel.Workbook wb = new XSSFWorkbook();
            TableColumnModel headerColumn=jTable1.getTableHeader().getColumnModel();
            org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("new sheet");
            // Create a row and put some cells in it. Rows are 0 based.
            Row row = sheet.createRow((short) 0);
            for(int i=0;i<headerColumn.getColumnCount();i++)
            {
                row.createCell(i).setCellValue(headerColumn.getColumn(i).getHeaderValue().toString());
            }
            for(int i=0;i<jTable1.getRowCount();i++)
            {
                row = sheet.createRow((short) i+1);
                for(int j=0;j<jTable1.getColumnCount();j++)
                {
                    row.createCell(j).setCellValue((String)jTable1.getValueAt(i, j));
                }
            }
            try (FileOutputStream fileOut = new FileOutputStream(dest)) {
                wb.write(fileOut);
                fileOut.close();
            }
        }
        System.out.println("Write to:"+dest.getAbsolutePath());
        JOptionPane.showMessageDialog(this, "New File has been saved.","Congratulation", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @SuppressWarnings("empty-statement")
    private void ReadDemographicFile(File demoFile) {
       // System.out.println("haha");
        DeidData.demoSourceFile=demoFile;
        
        LoggerWrapper.myLogger.log(Level.INFO, "Demo path: {0}", demoFile.getAbsolutePath());
        LoggerWrapper.myLogger.log(Level.INFO, "Demo File size: {0}", demoFile.length());

        
        ArrayList<Object[]> rowList = new ArrayList<Object[]>();
        String[] fields = null;
      
        //removed xlsx file format due to library copyright.
            if (demoFile.getName().endsWith(".xls")){
                XlsFile xls = new XlsFile();
                xls.setInputFile(demoFile.getAbsolutePath());
                try{
                    fields = xls.readHeadings();
                    rowList = xls.read();
                }
                catch(Exception e){
                    /* JOptionPane.showMessageDialog(this, "This file could not "
                     * + "be opened.", "Invalid Demographic File",
                     * JOptionPane.ERROR_MESSAGE);*/
                }
            }
            else { //txt
                
                Scanner inputStream = null;
                try {
                    inputStream = new Scanner(demoFile);
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "This file could not "
                            + "be opened.", "Invalid Demographic File",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                //String[] fields;
                
                if (inputStream.hasNextLine()) {
                    String line = null;
                    while (inputStream.hasNextLine() &&
                            "".equals((line = inputStream.nextLine()).trim()));
                                           
                    // Read data field headings from first line
                    fields = line.split("\t");
                    //DITGUI.log("Demographic header row: " + StringUtils.join(fields, ','));
                } else {
                    // File is empty
                    inputStream.close();
                    JOptionPane.showMessageDialog(this, "This file does not contain "
                            + "any demographic data.", "Invalid Demographic File",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Read the data and fill a 2D array
                //ArrayList<Object[]> rowList = new ArrayList<Object[]>();
                int lineIndex = 1;
                while (inputStream.hasNextLine()) {
                    lineIndex++;
                    String line = inputStream.nextLine().trim();
                    if(!line.isEmpty()){
                        Object[] rowData = line.split("\t");
                        Object[] rowDataMatch;
                        rowDataMatch = new Object[fields.length];
                        for(int i = 0; i< rowData.length; i++){
                            if (StringFilter(rowData[i].toString()).equals("")){
                                
                                rowData[i] = "misV";
                                DeidData.errorlog.addElement("Missing value in column " + fields[i] +" in line " + (lineIndex - 1));
                                //  DEIDGUI.log("Missing value in column " + fields[i] +" in line " + (lineIndex - 1),DEIDGUI.LOG_LEVEL.WARNING);
                                
                            }
                        }
                      
                        if (rowData.length > fields.length) {
                            System.arraycopy(rowData, 0, rowDataMatch, 0, fields.length);
                            DeidData.errorlog.addElement("Mismatched data in demographic file line " +(lineIndex - 1) + " (" + rowData.length + "/" +fields.length + "), some data " + "may be unnecessary. Please correct your original file.");
                            /*DEIDGUI.log("Mismatched data in demographic file line " +
                             * (lineIndex - 1) + " (" + rowData.length + "/" +
                             * fields.length + "), some data " + "may be unnecessary. Please correct your original file.",
                             * DEIDGUI.LOG_LEVEL.WARNING);*/
                        }
                        if (rowData.length < fields.length) {
                            for(int k = 0; k< fields.length; k++){
                                if (k < rowData.length)
                                    rowDataMatch[k] = rowData[k];
                                else rowDataMatch[k] = "misV";
                            }
                            DeidData.errorlog.addElement("Mismatched data in demographic file line " +
                                    (lineIndex - 1) + " (" + rowData.length + "/" +
                                    fields.length + "), some data " + "may be missing. Please correct your original file. ");
                            /*DEIDGUI.log("Mismatched data in demographic file line " +
                             * (lineIndex - 1) + " (" + rowData.length + "/" +
                             * fields.length + "), some data " + "may be missing. Please correct your original file. ",
                             * DEIDGUI.LOG_LEVEL.WARNING);*/
                        }
                        if (rowData.length != fields.length) {
                            
                            rowList.add(rowDataMatch);
                        } else {
                            rowList.add(rowData);
                        }
                    }
                }
                inputStream.close();
            }
        
        if (rowList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "This file does not contain "
                    + "any demographic data.", "Invalid Demographic File",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
     
        // Sort the data by ID
        // The data may be alphanumeric, and the user may change the ID data later.
        // Collections.sort(rowList, new DemoRowComparator());
        
        Object[][] rows = new Object[rowList.size()][];
        rowList.toArray(rows);
        
        ///////////////2.13
        //save source data for convenience.        
        if(! DeidData.demoFileModified){
            DeidData.sourcedemographicData = new SourceDemographicTable(fields, rows);            
            int sidRow = DeidData.sourcedemographicData.getColumnNdx(LoadDemoPanel.IdHeaderName);
            //System.out.println("sidRow is:" + sidRow);
            //Object  newvar = DeidData.sourcedemographicData.getValueAt(2, 11);
        }      
        
        DeidData.demographicData = new DemographicTableModel(fields, rows);
        jTable1.setModel(DeidData.demographicData);
        for(int i=0; i< jTable1.getColumnCount(); i++)
        {
            jTable1.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(textField));
        }
        jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        textField.setEditable(true);
        // System.out.println(jTable1.getColumnClass(0));
        DEIDGUI.continueButton.setEnabled(true);
        int idColumn = DeidData.demographicData.getColumnNdx("id");
        if (idColumn < 0) {           
            idColumn = 0;
        }
        DeidData.IdColumn = idColumn;
        jTable1.setColumnSelectionInterval(idColumn, idColumn);
        //jTable1.setCellSelectionEnabled(true);
        jTable1.setDefaultRenderer(Object.class, new missingValueRenderer());
        DeidData.selectedIdentifyingFields = null;
        DeidData.deselectedIdentifyingFields = null;        
    }
    
   
    
    private   static   String StringFilter(String   str)   throws   PatternSyntaxException   {
        
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}‘”“’]";
        java.util.regex.Pattern   p   =   java.util.regex.Pattern.compile(regEx);
        Matcher   m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancleChange;
    private javax.swing.JCheckBox cbxDummy;
    private javax.swing.JButton genrCurColButton;
    private javax.swing.JButton jButtonLoadDemo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblInstrc;
    private javax.swing.JButton revColButton;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public WizardPanel getNextPanel() {
        File destFile=null;
        if(DeidData.demoFileModified)
        {
            int needSave= JOptionPane.showConfirmDialog(this,
                    "We detected that you changed the data file.  Do you want to save these changes to a new file?",
                    "Demographic file is changed",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
            if(needSave== JOptionPane.YES_OPTION)
            {
                JFileChooser fc=new JFileChooser(DeidData.demoSourceFile);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setFileFilter(new DemoFilter());
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if(file.exists())
                    {
                        int choice= JOptionPane.showConfirmDialog(this, "File already existed, would you want overwrite it?","Confirm", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                        if(choice== JOptionPane.OK_OPTION)
                            destFile=file;
                    }
                    else
                    {
                        int choice= JOptionPane.showConfirmDialog(this, "File does not existed, would you want create it?","Confirm", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                        if(choice== JOptionPane.OK_OPTION)
                        {
                            try {
                                file.createNewFile();
                            } catch (IOException ex) {
                            }
                            destFile=file;
                        }
                    }
                }              
              
                if(destFile!=null)
                    try {
                        writeBack(destFile);
                        DeidData.demoFileModified=false;
                    } catch ( WriteException | IOException ex) {
                        Logger.getLogger(LoadDemoPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch(Exception e)
                    {
                        JOptionPane.showMessageDialog(this, "Failed to save to file.","Error", JOptionPane.ERROR_MESSAGE);
                    }
            }
        }
        
        return new MatchDataPanel();
    }
    
    @Override
    public WizardPanel getPreviousPanel() {
        return new LoadImagesPanel();
    }
    
    public static boolean isNumeric(String str)  {  
        try  
        {  
          double d = Double.parseDouble(str);  
        }  
        catch(NumberFormatException nfe)  
        {  
          return false;  
        }  
        return true;  
    }
}
