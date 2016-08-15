package dit.panels;

import dit.DEIDGUI;
import dit.AnalyzeConverterTask;
import dit.DicomConverterTask;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author christianprescott
 */
public class ConvertingProgressPanel extends javax.swing.JPanel implements WizardPanel{
    private Thread dicomConvThread, analyzeConvThread, convertThread;
    
    /**
     * Creates new form ConvertingImagesPanel
     */
    public ConvertingProgressPanel(HashSet<File> dicomDirs, HashSet<File> analyzePairs) {
        initComponents();
        DEIDGUI.helpButton.setEnabled(false);
        DEIDGUI.continueButton.setEnabled(false);
        
        startConvert(dicomDirs, analyzePairs);
        
        DEIDGUI.log("ConvertingProgressPanel initialized");
    }
    
    private void startConvert(HashSet<File> dicomDirs, HashSet<File> analyzePairs){
        if (dicomDirs.size() > 0) {
            // Build the dcm2nii command
            boolean platformValid = true;
            DicomConverterTask dicomConverter = null;
            try {
                dicomConverter = new DicomConverterTask();
                platformValid = true;
                dicomConverter.setProgressBar(jProgressConversion);
                Iterator<File> dicomParentIt = dicomDirs.iterator();
                while (dicomParentIt.hasNext()) {
                    File ff = dicomParentIt.next();
                    dicomConverter.addInputDirectory(ff);
                    dicomConverter.addOutputDirectory(ff);
                }
            } catch (RuntimeException e) {
                platformValid = false;
                DEIDGUI.log("DICOM conversion couldn't be started: " + 
                        e.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);

            } finally {
                if (platformValid) {
                    dicomConvThread = new Thread(dicomConverter);
                } else {
                    dicomConvThread = null;
                }
            }
        } else {
            dicomConvThread = null;
        }

        if (analyzePairs.size() > 0) {
            // Build the fslchfiletype command
            boolean platformValid = true;
            AnalyzeConverterTask analyzeConverter = null;
            try {
                analyzeConverter = new AnalyzeConverterTask();
                platformValid = true;
                analyzeConverter.setProgressBar(jProgressConversion);
                Iterator<File> analyzePairIt = analyzePairs.iterator();
                while (analyzePairIt.hasNext()) {
                    analyzeConverter.addInputPair(analyzePairIt.next());
                }
            } catch (RuntimeException e) {
                platformValid = false;
                DEIDGUI.log("Analyze conversion couldn't be started: " 
                        + e.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);

            } finally {
                if (platformValid) {
                    analyzeConvThread = new Thread(analyzeConverter);
                } else {
                    analyzeConvThread = null;
                }
            }
        } else {
            analyzeConvThread = null;
        }

        // Begin the conversion
        Runnable conversionRunner = new Runnable() {

            @Override
            public void run() {
                if (dicomConvThread != null) {
                    synchronized (dicomConvThread) {
                        jLabel2.setText("<html><p>Converting DICOM images...</p><p>&nbsp;</p></html>");
                        jProgressConversion.setValue(0);
                        dicomConvThread.start();
                        try {
                            dicomConvThread.wait();
                        } catch (InterruptedException ex) {
                            DEIDGUI.log("dcm2nii was interrupted, some DICOM "
                                    + "images may not have been converted: "
                                    + ex.getMessage(),DEIDGUI.LOG_LEVEL.WARNING);
                            return;
                        }
                    }
                }

                if (analyzeConvThread != null) {
                    synchronized (analyzeConvThread) {
                        jLabel2.setText("<html><p>Converting Analyze images...</p><p>&nbsp;</p></html>");
                        jProgressConversion.setValue(0);
                        analyzeConvThread.start();
                        try {
                            analyzeConvThread.wait();
                        } catch (InterruptedException ex) {
                            DEIDGUI.log("fslchfiletype was interrupted, some "
                                    + "Analyze images may not have been converted: "
                                    + ex.getMessage(),DEIDGUI.LOG_LEVEL.WARNING);
                            return;
                        }
                    }
                }

                DEIDGUI.continueButton.setEnabled(true);
                DEIDGUI.advance();
            }
        };
        convertThread = new Thread(conversionRunner);
        convertThread.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jProgressConversion = new javax.swing.JProgressBar();

        jLabel2.setText("<html><p>Converting images...</p><p>&nbsp;</p></html>");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jProgressConversion, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jProgressConversion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(236, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressConversion;
    // End of variables declaration//GEN-END:variables

    @Override
    public WizardPanel getNextPanel() {
        return new LoadDemoPanel();
    }

    @Override
    public WizardPanel getPreviousPanel() {
        // Interrupt conversion threads on back button press (cancelled by user)
        if(convertThread != null && convertThread.isAlive()){
            convertThread.interrupt();
        }
        return new LoadImagesPanel();
    }
}
