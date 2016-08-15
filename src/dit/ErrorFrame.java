/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 *
 * @author christianprescott
 */
public class ErrorFrame extends javax.swing.JFrame {
    private class ErrorCellRenderer extends JPanel implements ListCellRenderer{
        //<editor-fold defaultstate="collapsed" desc="ErrorData Renderer">
        private JLabel icon, text;
        private Icon errorIcon, warningIcon;
        
        public ErrorCellRenderer(){

            errorIcon = UIManager.getIcon("OptionPane.errorIcon");
            warningIcon = UIManager.getIcon("OptionPane.warningIcon");
            // Build text icons if the system icons are not supported
            if(errorIcon == null || warningIcon == null){
                Font errFont = new Font(Font.MONOSPACED, Font.BOLD, 12);
                BufferedImage contextImg = new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB);
                Graphics2D contextGraphics = contextImg.createGraphics();
                
                Rectangle2D warnStrBounds = new TextLayout("WARNING", errFont, 
                        contextGraphics.getFontRenderContext()).getBounds();
                BufferedImage warnImg = new BufferedImage(
                        (int)(warnStrBounds.getWidth() * 1.3), 
                        (int)warnStrBounds.getHeight() + 2, 
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D warnGraphics = warnImg.createGraphics();
                warnGraphics.setColor(Color.orange);
                warnGraphics.drawString("WARNING", 2, 1 + (int)warnStrBounds.getHeight());
                warningIcon = new ImageIcon(warnImg);
                
                Rectangle2D errStrBounds = new TextLayout("ERROR", errFont,
                        contextGraphics.getFontRenderContext()).getBounds();
                BufferedImage errImg = new BufferedImage(
                        (int)(warnStrBounds.getWidth() * 1.3), 
                        (int)warnStrBounds.getHeight() + 2, 
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D errGraphics = errImg.createGraphics();
                errGraphics.setColor(Color.RED);
                errGraphics.drawString("ERROR", 
                        errImg.getWidth() - 2 - (int)(errStrBounds.getWidth() * 1.18), 
                        1 + (int)errStrBounds.getHeight());
                errorIcon = new ImageIcon(errImg);
            } else {
                errorIcon = scaleIcon(errorIcon, .5f);
                warningIcon = scaleIcon(warningIcon, .5f);
            }
            
            icon = new JLabel(warningIcon);
            text = new JLabel();
            GroupLayout layout = new GroupLayout(this);
            setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createSequentialGroup()
                        .add(icon)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(text, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(GroupLayout.LEADING)
                    .add(icon)
                    .add(text)
            );
        }
        
        private ImageIcon scaleIcon(Icon icon, float scale){
            BufferedImage bi = new BufferedImage(
                    (int)(icon.getIconWidth() * scale),
                    (int)(icon.getIconHeight() * scale),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bi.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.scale(scale, scale);
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            
            ImageIcon result = new ImageIcon(bi);
            
            return result;
        }

        @Override
        public JPanel getListCellRendererComponent(JList jlist, Object o, 
        int index, boolean isSelected, boolean isFocused) {
            ErrorData val = (ErrorData)o;
            switch(val.getType()){
                case ERROR:
                    icon.setIcon(errorIcon);
                    break;
                case WARNING:
                    icon.setIcon(warningIcon);
                    break;
            }

            if(isSelected){
                setBackground(new Color(237,237,237));
            } else {
                setBackground(jlist.getBackground());
            }
            
            text.setText("<html><p>" + val.getMessage() + "</p></html>");
            
//            this.setPreferredSize(new Dimension(128, text.getBounds().height));
//            text.setPreferredSize(new Dimension(128, text.getBounds().height));

            return this;
        }
        // </editor-fold>
    }
    
    private class ErrorData{
        private String message;
        private ERROR_TYPE type;
        public ErrorData(String message, ERROR_TYPE type){
            this.message = message;
            this.type = type;
        }
        
        public String getMessage(){ return message;}
        public ERROR_TYPE getType(){ return type;}
    }

    public static enum ERROR_TYPE {WARNING, ERROR};
    private Vector<ErrorData> errors;
    private File logFile;

    /**
     * Creates new form ErrorFrame
     */
    public ErrorFrame(File logFile) {
        initComponents();
        
        this.logFile = logFile;
        errors = new Vector<ErrorData>();
        jListError.setCellRenderer(new ErrorCellRenderer());
        jListError.setListData(errors);

        setTitle("Error Log");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public void addError(String message, ERROR_TYPE type) {
        errors.insertElementAt(new ErrorData(message, type), 0);
        jListError.setListData(errors);
        
        if(!isVisible()){
            setVisible(true);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jListError = new javax.swing.JList();
        jButtonOpenLog = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jListError.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jListError);

        jButtonOpenLog.setText("Open log file");
        jButtonOpenLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenLogActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(jButtonOpenLog))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonOpenLog))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOpenLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenLogActionPerformed
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(logFile);
            } catch (IOException ex) {
                DEIDGUI.log("couldn't Desktop.open(" + logFile.toString() + "): "
                        + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
            } catch (IllegalArgumentException e) {
                DEIDGUI.log(e.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
            }
        } else {
            DEIDGUI.log("Desktop operations not supported", DEIDGUI.LOG_LEVEL.ERROR);
        }
    }//GEN-LAST:event_jButtonOpenLogActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonOpenLog;
    private javax.swing.JList jListError;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
