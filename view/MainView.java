/*
 * MainView.java
 */
package view;

import controller.Extractor;
import controller.Manager;
import controller.OntologyManager;
import controller.Util;
import java.awt.Font;
import java.awt.event.MouseWheelEvent;
import javax.swing.JFileChooser;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.AutoAnnotator;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * The application's main frame.
 */
public class MainView extends FrameView {

    private String document;
    private String fileOntology;
    long start, end, total;
    private OWLOntology ontology;
    //int mb = 1024*1024;

    public MainView(SingleFrameApplication app) {
        super(app);

        initComponents();

        //new Timer(10000, )

        //jMemoryLabel.setText(""+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() / mb));

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = AutoMeta2App.getApplication().getMainFrame();
            aboutBox = new About(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        AutoMeta2App.getApplication().show(aboutBox);
    }
    
    private void openDocument() throws Exception{
        
        int returnVal = jDocumentFileChooser.showOpenDialog(mainPanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //This is where a real application would open the file.
            File selectedFile = jDocumentFileChooser.getSelectedFile();
            document = selectedFile.getPath();
            jDocumentTextField.setText(document);
            jResultHtml.setText(Util.documentToString(document));
        }
    }
    
    private void openOntology(){
        
        int returnVal = jOntologyFileChooser.showOpenDialog(mainPanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //This is where a real application would open the file.
            File selectedFile = jOntologyFileChooser.getSelectedFile();
            fileOntology = selectedFile.getPath();
            jOntologyTextField.setText(fileOntology);


            String  urlOntology     = Util.fileToUrl(fileOntology);
            ontology                = OntologyManager.getSingleton().
                    loadOntology(IRI.create(urlOntology));

            if(ontology != null){
                jAutoCompleteCheckBox.setEnabled(true);
            }

        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jDocumentTextField = new javax.swing.JTextField();
        jOntologyTextField = new javax.swing.JTextField();
        jOntologyOpenBtn = new javax.swing.JButton();
        jDocumentOpenBtn = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableResultados = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jRefreshTriplesBtn = new javax.swing.JButton();
        jClearButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTotalTriples = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jMessages = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jResultHtml = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();
        jAutometaToolBar = new javax.swing.JToolBar();
        jBrowserButton = new javax.swing.JButton();
        jRunButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jAutoCompleteCheckBox = new javax.swing.JCheckBox();
        jRdfsLabelCheckBox = new javax.swing.JCheckBox();
        jExhaustiveCheckBox = new javax.swing.JCheckBox();
        jReasoningCheckBox = new javax.swing.JCheckBox();
        jReasonerCombo = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuSaveAs = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuConvertToRDF = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        jMenuCLIUsage = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jRunTimeElapsedLabel = new javax.swing.JLabel();
        jMemoryLabel = new javax.swing.JLabel();
        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jDocumentFileChooser = new javax.swing.JFileChooser();
        jOntologyFileChooser = new javax.swing.JFileChooser();
        jSaveRdfaFileChooser = new javax.swing.JFileChooser();
        jConvertToRDFFileChooser = new javax.swing.JFileChooser();

        mainPanel.setName("mainPanel"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jDocumentTextField.setEditable(false);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(view.AutoMeta2App.class).getContext().getResourceMap(MainView.class);
        jDocumentTextField.setText(resourceMap.getString("jDocumentTextField.text")); // NOI18N
        jDocumentTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDocumentTextField.setName("jDocumentTextField"); // NOI18N

        jOntologyTextField.setEditable(false);
        jOntologyTextField.setText(resourceMap.getString("jOntologyTextField.text")); // NOI18N
        jOntologyTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jOntologyTextField.setName("jOntologyTextField"); // NOI18N

        jOntologyOpenBtn.setIcon(resourceMap.getIcon("jOntologyOpenBtn.icon")); // NOI18N
        jOntologyOpenBtn.setText(resourceMap.getString("jOntologyOpenBtn.text")); // NOI18N
        jOntologyOpenBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jOntologyOpenBtn.setName("jOntologyOpenBtn"); // NOI18N
        jOntologyOpenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOntologyOpenBtnActionPerformed(evt);
            }
        });

        jDocumentOpenBtn.setIcon(resourceMap.getIcon("jDocumentOpenBtn.icon")); // NOI18N
        jDocumentOpenBtn.setText(resourceMap.getString("jDocumentOpenBtn.text")); // NOI18N
        jDocumentOpenBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDocumentOpenBtn.setMaximumSize(new java.awt.Dimension(159, 33));
        jDocumentOpenBtn.setMinimumSize(new java.awt.Dimension(159, 33));
        jDocumentOpenBtn.setName("jDocumentOpenBtn"); // NOI18N
        jDocumentOpenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDocumentOpenBtnActionPerformed(evt);
            }
        });

        jTabbedPane2.setName("jTabbedPane2"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTableResultados.setFont(resourceMap.getFont("jTableResultados.font")); // NOI18N
        jTableResultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Subject", "Property", "Object"
            }
        ));
        jTableResultados.setName("jTableResultados"); // NOI18N
        jScrollPane3.setViewportView(jTableResultados);
        jTableResultados.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTableResultados.columnModel.title0")); // NOI18N
        jTableResultados.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTableResultados.columnModel.title1")); // NOI18N
        jTableResultados.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTableResultados.columnModel.title2")); // NOI18N

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jRefreshTriplesBtn.setIcon(resourceMap.getIcon("jRefreshTriplesBtn.icon")); // NOI18N
        jRefreshTriplesBtn.setText(resourceMap.getString("jRefreshTriplesBtn.text")); // NOI18N
        jRefreshTriplesBtn.setName("jRefreshTriplesBtn"); // NOI18N
        jRefreshTriplesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRefreshTriplesBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(jRefreshTriplesBtn);

        jClearButton.setIcon(resourceMap.getIcon("jClearButton.icon")); // NOI18N
        jClearButton.setText(resourceMap.getString("jClearButton.text")); // NOI18N
        jClearButton.setFocusable(false);
        jClearButton.setName("jClearButton"); // NOI18N
        jClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClearButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(jClearButton);

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTotalTriples.setText(resourceMap.getString("jTotalTriples.text")); // NOI18N
        jTotalTriples.setName("jTotalTriples"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTotalTriples)
                        .addGap(53, 53, 53))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTotalTriples))
                .addContainerGap())
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jMessages.setColumns(20);
        jMessages.setRows(5);
        jMessages.setName("jMessages"); // NOI18N
        jScrollPane2.setViewportView(jMessages);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jResultHtml.setColumns(20);
        jResultHtml.setRows(5);
        jResultHtml.setName("jResultHtml"); // NOI18N
        jResultHtml.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jResultHtmlMouseWheelMoved(evt);
            }
        });
        jScrollPane1.setViewportView(jResultHtml);
        jResultHtml.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);

        jResultHtml.setAutoIndentEnabled(true);
        jResultHtml.setLineWrap(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jAutometaToolBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jAutometaToolBar.setRollover(true);
        jAutometaToolBar.setName("jAutometaToolBar"); // NOI18N

        jBrowserButton.setIcon(resourceMap.getIcon("jBrowserButton.icon")); // NOI18N
        jBrowserButton.setText(resourceMap.getString("jBrowserButton.text")); // NOI18N
        jBrowserButton.setToolTipText(resourceMap.getString("jBrowserButton.toolTipText")); // NOI18N
        jBrowserButton.setFocusable(false);
        jBrowserButton.setName("jBrowserButton"); // NOI18N
        jBrowserButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBrowserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBrowserButtonActionPerformed(evt);
            }
        });
        jAutometaToolBar.add(jBrowserButton);

        jRunButton.setIcon(resourceMap.getIcon("jRunButton.icon")); // NOI18N
        jRunButton.setText(resourceMap.getString("jRunButton.text")); // NOI18N
        jRunButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jRunButton.setName("jRunButton"); // NOI18N
        jRunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRunButtonActionPerformed(evt);
            }
        });
        jAutometaToolBar.add(jRunButton);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jAutometaToolBar.add(jSeparator1);

        jAutoCompleteCheckBox.setText(resourceMap.getString("jAutoCompleteCheckBox.text")); // NOI18N
        jAutoCompleteCheckBox.setToolTipText(resourceMap.getString("jAutoCompleteCheckBox.toolTipText")); // NOI18N
        jAutoCompleteCheckBox.setEnabled(false);
        jAutoCompleteCheckBox.setFocusable(false);
        jAutoCompleteCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jAutoCompleteCheckBox.setName("jAutoCompleteCheckBox"); // NOI18N
        jAutoCompleteCheckBox.setPreferredSize(new java.awt.Dimension(117, 33));
        jAutoCompleteCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jAutoCompleteCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAutoCompleteCheckBoxActionPerformed(evt);
            }
        });
        jAutometaToolBar.add(jAutoCompleteCheckBox);

        jRdfsLabelCheckBox.setSelected(true);
        jRdfsLabelCheckBox.setText(resourceMap.getString("jRdfsLabelCheckBox.text")); // NOI18N
        jRdfsLabelCheckBox.setToolTipText(resourceMap.getString("jRdfsLabelCheckBox.toolTipText")); // NOI18N
        jRdfsLabelCheckBox.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jRdfsLabelCheckBox.setFocusable(false);
        jRdfsLabelCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jRdfsLabelCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jRdfsLabelCheckBox.setName("jRdfsLabelCheckBox"); // NOI18N
        jAutometaToolBar.add(jRdfsLabelCheckBox);

        jExhaustiveCheckBox.setText(resourceMap.getString("jExhaustiveCheckBox.text")); // NOI18N
        jExhaustiveCheckBox.setToolTipText(resourceMap.getString("jExhaustiveCheckBox.toolTipText")); // NOI18N
        jExhaustiveCheckBox.setFocusable(false);
        jExhaustiveCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jExhaustiveCheckBox.setMargin(new java.awt.Insets(2, 5, 2, 2));
        jExhaustiveCheckBox.setName("jExhaustiveCheckBox"); // NOI18N
        jExhaustiveCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jAutometaToolBar.add(jExhaustiveCheckBox);

        jReasoningCheckBox.setSelected(true);
        jReasoningCheckBox.setText(resourceMap.getString("jReasoningCheckBox.text")); // NOI18N
        jReasoningCheckBox.setToolTipText(resourceMap.getString("jReasoningCheckBox.toolTipText")); // NOI18N
        jReasoningCheckBox.setActionCommand(resourceMap.getString("jReasoningCheckBox.actionCommand")); // NOI18N
        jReasoningCheckBox.setFocusable(false);
        jReasoningCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jReasoningCheckBox.setName("jReasoningCheckBox"); // NOI18N
        jReasoningCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jReasoningCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jReasoningCheckBoxActionPerformed(evt);
            }
        });
        jAutometaToolBar.add(jReasoningCheckBox);

        jReasonerCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "pellet", "hermit"}));
        jReasonerCombo.setSelectedItem(1);
        jReasonerCombo.setMaximumSize(new java.awt.Dimension(70, 22));
        jReasonerCombo.setName("jReasonerCombo"); // NOI18N
        jAutometaToolBar.add(jReasonerCombo);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jAutometaToolBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jAutometaToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDocumentTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                            .addComponent(jOntologyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jOntologyOpenBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDocumentOpenBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDocumentTextField)
                    .addComponent(jDocumentOpenBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jOntologyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jOntologyOpenBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setName("jPanel2"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 713, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuSaveAs.setText(resourceMap.getString("jMenuSaveAs.text")); // NOI18N
        jMenuSaveAs.setName("jMenuSaveAs"); // NOI18N
        jMenuSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSaveAsActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuSaveAs);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem3);

        jMenuConvertToRDF.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuConvertToRDF.setText(resourceMap.getString("jMenuConvertToRDF.text")); // NOI18N
        jMenuConvertToRDF.setName("jMenuConvertToRDF"); // NOI18N
        jMenuConvertToRDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuConvertToRDFActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuConvertToRDF);

        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem4);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        jMenuCLIUsage.setText(resourceMap.getString("jMenuCLIUsage.text")); // NOI18N
        jMenuCLIUsage.setName("jMenuCLIUsage"); // NOI18N
        jMenuCLIUsage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCLIUsageActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuCLIUsage);

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem1);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(view.AutoMeta2App.class).getContext().getActionMap(MainView.class, this);
        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jRunTimeElapsedLabel.setText(resourceMap.getString("jRunTimeElapsedLabel.text")); // NOI18N
        jRunTimeElapsedLabel.setName("jRunTimeElapsedLabel"); // NOI18N

        jMemoryLabel.setText(resourceMap.getString("jMemoryLabel.text")); // NOI18N
        jMemoryLabel.setName("jMemoryLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusMessageLabel)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRunTimeElapsedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(statusPanelLayout.createSequentialGroup()
                                .addGap(164, 164, 164)
                                .addComponent(statusAnimationLabel))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jMemoryLabel)
                                .addGap(24, 24, 24)))
                        .addGap(53, 53, 53)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jMemoryLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusMessageLabel)
                            .addComponent(statusAnimationLabel))
                        .addGap(3, 3, 3))
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jRunTimeElapsedLabel)))
                        .addContainerGap())))
        );

        jDialog1.setName("jDialogDocument"); // NOI18N

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jDialog2.setName("jDialogOntology"); // NOI18N

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jDocumentFileChooser.setFileFilter(new FileNameExtensionFilter("TEXT file", "txt"));
        jDocumentFileChooser.setName("jDocumentFileChooser"); // NOI18N

        jOntologyFileChooser.setFileFilter(new FileNameExtensionFilter("ONTOLOGY file (owl,rdf,n3)", "owl", "rdf", "n3"));
        jOntologyFileChooser.setName("jOntologyFileChooser"); // NOI18N

        jSaveRdfaFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        jSaveRdfaFileChooser.setFileFilter(new FileNameExtensionFilter("HTML file (htm,html,xhtml)", "htm", "html", "xhtml"));
        jSaveRdfaFileChooser.setName("jSaveRdfaFileChooser"); // NOI18N

        jConvertToRDFFileChooser.setApproveButtonToolTipText(resourceMap.getString("jConvertToRDFFileChooser.approveButtonToolTipText")); // NOI18N
        jConvertToRDFFileChooser.setBackground(resourceMap.getColor("jConvertToRDFFileChooser.background")); // NOI18N
        jConvertToRDFFileChooser.setDialogTitle(resourceMap.getString("jConvertToRDFFileChooser.dialogTitle")); // NOI18N
        jConvertToRDFFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        jConvertToRDFFileChooser.setFileFilter(new FileNameExtensionFilter("Resource Description Framework (*.rdf)", "rdf"));
        jConvertToRDFFileChooser.setName("jConvertToRDFFileChooser"); // NOI18N

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jDocumentOpenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDocumentOpenBtnActionPerformed
        // TODO add your handling code here:
        try {
            openDocument();
        } catch (Exception e) {
            System.out.print(e);
        }
        

    }//GEN-LAST:event_jDocumentOpenBtnActionPerformed

    private void jOntologyOpenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOntologyOpenBtnActionPerformed
        // TODO add your handling code here:
        openOntology();
    }//GEN-LAST:event_jOntologyOpenBtnActionPerformed

    private void jRunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRunButtonActionPerformed
         try
        {
            start = System.currentTimeMillis();
            boolean reasoning       = jReasoningCheckBox.isSelected();
            boolean label           = jRdfsLabelCheckBox.isSelected();
            boolean exhaustive      = jExhaustiveCheckBox.isSelected();
            String  reasoner        = jReasonerCombo.getSelectedItem().toString();
            

            if(ontology != null){
                
                String documentText = Util.htmlToText(jResultHtml.getText());            
                AutoAnnotator autoAnnotator = new AutoAnnotator(documentText, ontology, reasoning, label, exhaustive, reasoner);
                String rdfa = Manager.getDocumentoAnotado(autoAnnotator); 

                String formattedRdfa = Util.formatXML(rdfa);
                if(formattedRdfa != null){
                  jResultHtml.setText(formattedRdfa);  
                }else{
                  jResultHtml.setText(rdfa);  
                }

                File temp = Util.textToTempFile(rdfa);
                System.out.println(temp.getAbsolutePath());
                jTableResultados.setModel(Extractor.parseRDFaToTableModel(temp.getAbsolutePath()));
                jTotalTriples.setText((new StringBuilder()).append("").append(jTableResultados.getRowCount()).toString());
            }else{
                
                JOptionPane.showMessageDialog(null, "Please, load the ontology first !");
                
            }
            
            
        }
        catch(Exception e)
        {
            Util.getException(e);
        }
        end = System.currentTimeMillis();
        total = end - start;
        String timeElapsed = String.format("%d min, %d sec, %d mil", new Object[] {
            Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(total)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(total)), Long.valueOf(TimeUnit.MILLISECONDS.toMillis(total) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total)))
        });
        System.out.println((new StringBuilder()).append("Run Time Elapsed:").append(timeElapsed).toString());
        jRunTimeElapsedLabel.setText(timeElapsed);


    }//GEN-LAST:event_jRunButtonActionPerformed

    private void jRefreshTriplesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRefreshTriplesBtnActionPerformed
        // TODO add your handling code here:
        try {

            String rdfa = jResultHtml.getText();
            File temp = Util.textToTempFile(rdfa);
            jTableResultados.setModel(Extractor.parseRDFaToTableModel(temp.getAbsolutePath()));
            jTotalTriples.setText(Integer.toString(jTableResultados.getRowCount()));

        } catch (Exception e) {
            Util.getException(e);
            jMessages.setText(e.getMessage());
        }
    }//GEN-LAST:event_jRefreshTriplesBtnActionPerformed

    private void jClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearButtonActionPerformed
        // TODO add your handling code here:
        jTableResultados.setModel(new DefaultTableModel());
        
    }//GEN-LAST:event_jClearButtonActionPerformed

    private void jResultHtmlMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jResultHtmlMouseWheelMoved
        // TODO add your handling code here:

        //CTRL + MOUSE WHEEL !!!
        if ((evt.getModifiers() & MouseWheelEvent.CTRL_MASK) == MouseWheelEvent.CTRL_MASK) {

            int rotation = evt.getWheelRotation();
            Font font = jResultHtml.getFont();

            if (rotation < 0) {

                jResultHtml.setFont(new Font(font.getFontName(),
                        font.getStyle(), font.getSize() + 1));

            } else {

                jResultHtml.setFont(new Font(font.getFontName(),
                        font.getStyle(), font.getSize() - 1));

            }

        }


    }//GEN-LAST:event_jResultHtmlMouseWheelMoved

    private void jAutoCompleteCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAutoCompleteCheckBoxActionPerformed
        // TODO add your handling code here:
        if(jAutoCompleteCheckBox.isSelected()){

            CompletionProvider provider = OntologyManager.getSingleton().
                createCompletionProvider(ontology);
            AutoCompletion ac = new AutoCompletion(provider);
            ac.install(jResultHtml);

        }

    }//GEN-LAST:event_jAutoCompleteCheckBoxActionPerformed

private void jMenuSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSaveAsActionPerformed
// TODO add your handling code here:
    if (jSaveRdfaFileChooser.showSaveDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
        File file = jSaveRdfaFileChooser.getSelectedFile();
        
        try {
            
            File fileSave = new File (Util.addFileExtIfNecessary(file.getCanonicalPath(), "html"));
            Util.saveTextInFile(jResultHtml.getText(), fileSave);
            
        } catch (Exception e) {
            Util.getException(e);
        }
        
        
        
        // save to file
    }
}//GEN-LAST:event_jMenuSaveAsActionPerformed

private void jMenuConvertToRDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuConvertToRDFActionPerformed
// TODO add your handling code here:
    if (jConvertToRDFFileChooser.showSaveDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
        
        File file = jConvertToRDFFileChooser.getSelectedFile();
        String rdfaText = jResultHtml.getText();
        
        try {
            
            File fileSave = new File (Util.addFileExtIfNecessary(file.getCanonicalPath(), "rdf"));
            Extractor.rdfaToRDF(rdfaText,fileSave);
            
        } catch (Exception e) {
            
            Util.getException(e);
            
        }
        
        
        // save to file
    }
}//GEN-LAST:event_jMenuConvertToRDFActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
// TODO add your handling code here:
    openOntology();
}//GEN-LAST:event_jMenuItem2ActionPerformed

private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
// TODO add your handling code here:
    try{
       openDocument(); 
    }catch(Exception e){
        System.out.println(e);
    }
}//GEN-LAST:event_jMenuItem3ActionPerformed

private void jBrowserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBrowserButtonActionPerformed
// TODO add your handling code here:
    Util.openURLInBrowser(Util.textToTempFile(jResultHtml.getText()).toURI());
}//GEN-LAST:event_jBrowserButtonActionPerformed

private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_aboutMenuItemActionPerformed

private void jMenuCLIUsageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCLIUsageActionPerformed
// TODO add your handling code here:
    if (aboutBox == null) {
            JFrame mainFrame = AutoMeta2App.getApplication().getMainFrame();
            aboutBox = new CLIUsageDialog(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        AutoMeta2App.getApplication().show(aboutBox);
    //CLIUsage.
}//GEN-LAST:event_jMenuCLIUsageActionPerformed

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
// TODO add your handling code here:
    try {
        Util.openURLInBrowser(new URI("http://thedatahub.org/group/lodcloud"));
    } catch (Exception e) {
        System.out.print(e);
    }
    
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
// TODO add your handling code here:
    try {
        
        String URL = "http://www.google.com/webmasters/tools/richsnippets?url=&html="+(URLEncoder.encode(jResultHtml.getText()));
        
        Util.openURLInBrowser(new URI(URL));
    } catch (Exception e) {
        System.out.print(e);
    }
}//GEN-LAST:event_jMenuItem4ActionPerformed

private void jReasoningCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jReasoningCheckBoxActionPerformed
// TODO add your handling code here:
    if(jReasoningCheckBox.isSelected()){
        jReasonerCombo.enable();
    }else{
        jReasonerCombo.disable();
    }
}//GEN-LAST:event_jReasoningCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jAutoCompleteCheckBox;
    private javax.swing.JToolBar jAutometaToolBar;
    private javax.swing.JButton jBrowserButton;
    private javax.swing.JButton jClearButton;
    private javax.swing.JFileChooser jConvertToRDFFileChooser;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JFileChooser jDocumentFileChooser;
    private javax.swing.JButton jDocumentOpenBtn;
    private javax.swing.JTextField jDocumentTextField;
    private javax.swing.JCheckBox jExhaustiveCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jMemoryLabel;
    private javax.swing.JMenuItem jMenuCLIUsage;
    private javax.swing.JMenuItem jMenuConvertToRDF;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuSaveAs;
    private javax.swing.JTextArea jMessages;
    private javax.swing.JFileChooser jOntologyFileChooser;
    private javax.swing.JButton jOntologyOpenBtn;
    private javax.swing.JTextField jOntologyTextField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JCheckBox jRdfsLabelCheckBox;
    private javax.swing.JComboBox jReasonerCombo;
    private javax.swing.JCheckBox jReasoningCheckBox;
    private javax.swing.JButton jRefreshTriplesBtn;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea jResultHtml;
    private javax.swing.JButton jRunButton;
    private javax.swing.JLabel jRunTimeElapsedLabel;
    private javax.swing.JFileChooser jSaveRdfaFileChooser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableResultados;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel jTotalTriples;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}
