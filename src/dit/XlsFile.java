/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 *
 * @author angelo
 */
public class XlsFile {
    private String inputFile;

  public void setInputFile(String inputFile) {
    this.inputFile = inputFile;
  }
  public String[] readHeadings() throws IOException{
      File inputWorkbook = new File(inputFile);
      Workbook w;
      boolean flag = true;
      String[] headings = null;
      try {
      w = Workbook.getWorkbook(inputWorkbook);
      // Get the first sheet
      Sheet sheet = w.getSheet(0);
      //find the fields
      String[] fields;
      fields = new String[sheet.getColumns()];
      for (int j = 0; j < sheet.getColumns(); j++)
      {
          Cell cell = sheet.getCell(j, 0);
          if (cell.getContents().equals("") )
            fields[j] = "MissCol"+j;
          else
            fields[j] = cell.getContents();
      }
      headings = fields;
      } catch (BiffException e) {
      e.printStackTrace();
      flag = false;
    }
      if (flag) 
   return headings;
      else return null;
  }
  public ArrayList<Object[]> read() throws IOException  {
    File inputWorkbook = new File(inputFile);
    Workbook w;
    
    ArrayList<Object[]> rowList = new ArrayList<Object[]>();
    try {
      w = Workbook.getWorkbook(inputWorkbook);
      // Get the first sheet
      Sheet sheet = w.getSheet(0);
      //find the fields
      String[] fields;
      fields = readHeadings();
      if (fields != null){
      
        for (int i = 1; i < sheet.getRows(); i++){
            Object[] rowData;
            rowData = new Object[fields.length];
          for(int k = 0; k < fields.length; k++){
          Cell cell = sheet.getCell(k, i);
          String cellcont = cell.getContents();
          if (StringFilter(cellcont).equals("")) {
          cellcont = "misV";
          DeidData.errorlog.addElement("Missing value in column " + fields[k] +" in line " + i );  
                      
                    
          }          
          rowData[k] =  cellcont; 
          
          }
          
          rowList.add(rowData);
          
      
      
        }
      }
      else return null;
      
    } catch (BiffException e) {
      e.printStackTrace();
    }
    
    return rowList; 
  }
  
  private   static   String StringFilter(String   str)   throws   PatternSyntaxException   {     
                 
          String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~@#%……&*——+|{}‘”“’]";  
          Pattern   p   =   Pattern.compile(regEx);     
          Matcher   m   =   p.matcher(str);     
          return   m.replaceAll("").trim();     
          } 
}
