/*
 * Copyright 2002-2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/*
 * Nicolas Franck: based on code of NewBagInPlaceFrame
 */

package gov.loc.repository.bagger.ui;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.util.LayoutUtil;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFactory.Version;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.TitlePane;
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.richclient.util.GuiStandardUtils;
import ugent.bagger.bagitmets.DefaultBagItMets;
import ugent.bagger.bagitmets.MetsFileDateCreated;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.LongTask2;

public final class NewBagsDialog extends JDialog implements ActionListener {
    private static final Log log = LogFactory.getLog(NewBagFrame.class);
    private static final long serialVersionUID = 1L;
    private final Dimension preferredDimension = new Dimension(400, 230);
    private JPanel contentPanel;
    private JTextField selectionInfoField;    
    private ArrayList<File>selectedDirectories = new ArrayList<File>();    
    private boolean bagInPlace = true;
    private File outputDir;
    private JButton saveAsButton;
    private JComboBox bagVersionList;
    private JComboBox profileList;
    private JCheckBox addKeepFilesToEmptyFoldersCheckBox;
    protected static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";
    protected static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";	
    private ActionCommand finishCommand;
    private ActionCommand cancelCommand;    
    private JComboBox metsFileDateCreatedCombobox;
    private ArrayList<String> metadataPaths = new ArrayList<String>();
    
    protected void parseMetadataPaths(String text){        
        String [] data = text.replaceAll("\\s","").split(",");
        if(data == null){
            data = new String [] {};
        }                
        getMetadataPaths().clear();
        getMetadataPaths().addAll(Arrays.asList(data));
    }
    public ArrayList<String> getMetadataPaths() {
        if(metadataPaths == null){
            metadataPaths = new ArrayList<String>();
        }
        return metadataPaths;
    }
    public void setMetadataPaths(ArrayList<String> metadataPaths) {
        this.metadataPaths = metadataPaths;
    }
    public BagView getBagView(){
        return BagView.getInstance();
    }
    public DefaultBag getBag(){
        return BagView.getInstance().getBag();
    }
    public JPanel getContentPanel() {
        if(contentPanel == null){
            if (getBagView() != null) {          
                getContentPane().removeAll();        
                contentPanel = createComponents();            
            } else {
                contentPanel = new JPanel();
            }
        }
        return contentPanel;
    }
    public void setContentPanel(JPanel contentPanel) {
        this.contentPanel = contentPanel;
    }    
    public NewBagsDialog(JFrame frame,boolean isModal, String title) {
        super(frame,isModal);
        setTitle(title);
        getContentPane().add(getContentPanel(), BorderLayout.CENTER);       
    }
    public boolean isBagInPlace() {
        return bagInPlace;
    }
    public void setBagInPlace(boolean bagInPlace) {
        this.bagInPlace = bagInPlace;
    }
    public File getOutputDir() {
        return outputDir;
    }
    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }
    public JButton getSaveAsButton() {
        if(saveAsButton == null){
            saveAsButton = new JButton(Context.getMessage("bag.button.browse"));
            saveAsButton.addActionListener(new BrowseFileHandler());
            saveAsButton.setEnabled(true);
            saveAsButton.setToolTipText(Context.getMessage("bag.button.browse.help"));
        }
        return saveAsButton;
    }
    public void setSaveAsButton(JButton saveAsButton) {
        this.saveAsButton = saveAsButton;
    }   

    public ActionCommand getFinishCommand() {
        if(finishCommand == null){
            finishCommand = new ActionCommand(getFinishCommandId()) {
                @Override
                public void doExecuteCommand() {
                    new OkNewBagHandler().actionPerformed(null);
                }
            };
        }
        return finishCommand;
    }
    public void setFinishCommand(ActionCommand finishCommand) {
        this.finishCommand = finishCommand;
    }
    public ActionCommand getCancelCommand() {
        if(cancelCommand == null){
            cancelCommand = new ActionCommand(getCancelCommandId()) {
                @Override
                public void doExecuteCommand() {
                    new CancelNewBagHandler().actionPerformed(null);
                }
            };
        }
        return cancelCommand;
    }
    public void setCancelCommand(ActionCommand cancelCommand) {
        this.cancelCommand = cancelCommand;
    }

    public JComboBox getProfileList() {
        if(profileList == null){
            profileList = new JComboBox(getBagView().getProfileStore().getProfileNames());
            profileList.setName(Context.getMessage("bag.label.projectlist"));
            profileList.setSelectedItem(Context.getMessage("bag.project.noproject"));
            profileList.setToolTipText(Context.getMessage("bag.projectlist.help"));
        }
        return profileList;
    }

    public void setProfileList(JComboBox profileList) {
        this.profileList = profileList;
    }

    public JCheckBox getAddKeepFilesToEmptyFoldersCheckBox() {
        if(addKeepFilesToEmptyFoldersCheckBox == null){
            addKeepFilesToEmptyFoldersCheckBox = new JCheckBox();
            addKeepFilesToEmptyFoldersCheckBox.setSelected(getBag().isAddKeepFilesToEmptyFolders());
            addKeepFilesToEmptyFoldersCheckBox.addActionListener(new AddKeepFilesToEmptyFoldersHandler());
        }
        return addKeepFilesToEmptyFoldersCheckBox;
    }

    public void setAddKeepFilesToEmptyFoldersCheckBox(JCheckBox addKeepFilesToEmptyFoldersCheckBox) {
        this.addKeepFilesToEmptyFoldersCheckBox = addKeepFilesToEmptyFoldersCheckBox;
    }

    public JComboBox getMetsFileDateCreatedCombobox() {
        if(metsFileDateCreatedCombobox == null){            
            metsFileDateCreatedCombobox = new JComboBox(MetsFileDateCreated.values());
            metsFileDateCreatedCombobox.setSelectedItem(MetsFileDateCreated.CURRENT_DATE);
            metsFileDateCreatedCombobox.addItemListener(new ItemListener(){
                @Override
                public void itemStateChanged(ItemEvent ie) {
                    if(ie.getStateChange() != ItemEvent.SELECTED){
                        return;
                    }                    
                    getBagView().setMetsFileDateCreated((MetsFileDateCreated)ie.getItem());
                }

            });
        }
        return metsFileDateCreatedCombobox;
    }

    public void setMetsFileDateCreatedCombobox(JComboBox metsFileDateCreatedCombobox) {
        this.metsFileDateCreatedCombobox = metsFileDateCreatedCombobox;
    }

    public JTextField getSelectionInfoField() {
        if(selectionInfoField == null){
            String defaultMessage = Context.getMessage("NewBagsDialog.selectInfoField.label",new String [] {"0"});
            selectionInfoField = new JTextField(defaultMessage);
            selectionInfoField.setCaretPosition(defaultMessage.length());
            selectionInfoField.setEditable(false);            
        }
        return selectionInfoField;
    }

    public void setSelectionInfoField(JTextField selectionInfoField) {        
        this.selectionInfoField = selectionInfoField;
    }

    public JComboBox getBagVersionList() {
        if(bagVersionList == null){
            ArrayList<String> versionModel = new ArrayList<String>();                
            for(Version version:Version.values()){
                versionModel.add(version.versionString);
            }
            bagVersionList = new JComboBox(versionModel.toArray());
            bagVersionList.setName(Context.getMessage("bag.label.versionlist"));
            bagVersionList.setSelectedItem(Version.V0_96.versionString);
            bagVersionList.setToolTipText(Context.getMessage("bag.versionlist.help")); 
        }
        return bagVersionList;
    }

    public void setBagVersionList(JComboBox bagVersionList) {
        this.bagVersionList = bagVersionList;
    }
    
    
    private JPanel createComponents() {

    	TitlePane titlePane = new TitlePane();    	
    	JPanel pageControl = new JPanel(new BorderLayout());
    	JPanel titlePaneContainer = new JPanel(new BorderLayout());
    	titlePane.setTitle(Context.getMessage("NewBagsDialog.title"));
    	titlePane.setMessage(new DefaultMessage(Context.getMessage("NewBagsDialog.description")));
    	titlePaneContainer.add(titlePane.getControl());
    	titlePaneContainer.add(new JSeparator(), BorderLayout.SOUTH);
    	pageControl.add(titlePaneContainer, BorderLayout.NORTH);
    	
    	JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        int row = 0;
        layoutSelectDataContent(panel,row++);
        layoutBagVersionContent(panel,row++);
        layoutProfileSelectionContent(panel,row++);
        layoutAddKeepFilesToEmptyCheckBox(panel,row++);
        layoutSpacer(panel,row++);
       
        //metsFileDateCreated
        row++;
        JLabel metsFileDateCreatedLabel = new JLabel(Context.getMessage("NewBagsDialog.metsFileDateCreated.label"));  
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        panel.add(metsFileDateCreatedLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); 
        panel.add(getMetsFileDateCreatedCombobox(), glbc);
        
        //metadata paths
        row++;
        JLabel metadataPathsLabel = new JLabel(Context.getMessage("NewBagsDialog.metadataPaths.label"));
        metadataPathsLabel.setToolTipText(Context.getMessage("NewBagsDialog.metadataPaths.tooltip"));
        glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        panel.add(metadataPathsLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        glbc.ipadx=5;
        glbc.ipadx=0;
        final JTextField metadataPathTextField = new JTextField(15);
        panel.add(metadataPathTextField,glbc);
        metadataPathTextField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent de) {                
                parseMetadataPaths(metadataPathTextField.getText());                
            }
            @Override
            public void removeUpdate(DocumentEvent de) {  
                parseMetadataPaths(metadataPathTextField.getText());                
            }
            @Override
            public void changedUpdate(DocumentEvent de) {                
            }            
        });
        
        //bagInPlace
        row++;
        JLabel bagInPlaceLabel = new JLabel(Context.getMessage("NewBagsDialog.bagInPlace.label"));  
        glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        panel.add(bagInPlaceLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);         
        final JCheckBox bagInPlaceCheckBox = new JCheckBox();
        bagInPlaceCheckBox.setSelected(bagInPlace);                
        panel.add(bagInPlaceCheckBox,glbc);        
        
        //outputDir
        row++;        
        final JLabel outputDirLabel = new JLabel(Context.getMessage("NewBagsDialog.outputDir.label"));        
        outputDirLabel.setEnabled(!bagInPlace);
        glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST);
        panel.add(outputDirLabel,glbc);
        
        final JTextField outputDirTextField = new JTextField(15);
        outputDirTextField.setEnabled(!bagInPlace);
        outputDirTextField.setEditable(false);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        glbc.ipadx=5;
        glbc.ipadx=0;
        panel.add(outputDirTextField,glbc);
        
        final JButton outputDirBrowseButton = new JButton(Context.getMessage("bag.button.browse"));
        outputDirBrowseButton.setEnabled(!bagInPlace);        
        glbc = LayoutUtil.buildGridBagConstraints(2, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
        glbc.ipadx=5;
        glbc.ipadx=0;
        panel.add(outputDirBrowseButton,glbc);
        
        //relatie bagInPlace - outputDir
        bagInPlaceCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {                
                bagInPlace = bagInPlaceCheckBox.isSelected();
                outputDirLabel.setEnabled(!bagInPlace);
                outputDirTextField.setEnabled(!bagInPlace);
                outputDirBrowseButton.setEnabled(!bagInPlace);
            }
        });
        outputDirBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                File [] selectedFiles = SwingUtils.chooseFiles(
                    Context.getMessage("NewBagsDialog.outputDir.browse.title"),
                    null,
                    JFileChooser.DIRECTORIES_ONLY,
                    false,
                    SwingUtils.getFrame(),
                    JFileChooser.SAVE_DIALOG
                );                
                if(selectedFiles.length == 0){
                    SwingUtils.ShowError(
                        Context.getMessage("NewBagsDialog.outputDir.browse.error.noselected.title"),
                        Context.getMessage("NewBagsDialog.outputDir.browse.error.noselected.label")
                    );
                }else if(!selectedFiles[0].canWrite()){
                    SwingUtils.ShowError(
                        Context.getMessage("NewBagsDialog.outputDir.browse.error.cannotwrite.title"),
                        Context.getMessage("NewBagsDialog.outputDir.browse.error.cannotwrite.label",new String [] {
                            selectedFiles[0].getAbsolutePath()
                        })
                    );
                }else if(selectedFiles[0].listFiles().length > 0){
                    SwingUtils.ShowError(                            
                        Context.getMessage("NewBagsDialog.outputDir.browse.error.notempty.title"),
                        Context.getMessage("NewBagsDialog.outputDir.browse.error.notempty.label",new String [] {
                            selectedFiles[0].getAbsolutePath()
                        })
                    );
                }else{                
                    outputDirTextField.setText(selectedFiles[0].getAbsolutePath());                    
                    outputDir = selectedFiles[0];
                    outputDirTextField.invalidate();
                }                
            }
        });
        
        GuiStandardUtils.attachDialogBorder(panel);
        pageControl.add(panel);        
        pageControl.add(createButtonBar(),BorderLayout.SOUTH);
       
        return pageControl;
        
    }
    
    private void layoutSelectDataContent(JPanel contentPanel, int row) {
        BagView bagView = getBagView();
        DefaultBag bag = bagView.getBag();
        
    	JLabel location = new JLabel(Context.getMessage("NewBagsDialog.selectData.label"));        
        
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        contentPanel.add(location, glbc);
        
        glbc = LayoutUtil.buildGridBagConstraints(2, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.EAST); 
        glbc.ipadx=5;
        glbc.ipadx=0;
        contentPanel.add(getSaveAsButton(), glbc);
        
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        glbc.ipadx=5;
        glbc.ipadx=0;
        contentPanel.add(getSelectionInfoField(), glbc);
    }
	
    private void layoutBagVersionContent(JPanel contentPanel, int row) {
        
        JLabel bagVersionLabel = new JLabel(Context.getMessage("bag.label.version"));
        bagVersionLabel.setToolTipText(Context.getMessage("bag.versionlist.help"));
		
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        contentPanel.add(bagVersionLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); 
        contentPanel.add(getBagVersionList(), glbc);
    }
	
    private void layoutProfileSelectionContent(JPanel contentPane, int row) {
        // content
        // profile selection
        JLabel bagProfileLabel = new JLabel(Context.getMessage("NewBagsDialog.profile.label"));
        bagProfileLabel.setToolTipText(Context.getMessage("NewBagsDialog.profile.description"));
	
        JLabel spacerLabel = new JLabel();
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 5, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        contentPane.add(bagProfileLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 40, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
        contentPane.add(getProfileList(), glbc);
        glbc = LayoutUtil.buildGridBagConstraints(2, row, 1, 1, 40, 50, GridBagConstraints.NONE, GridBagConstraints.EAST);
        contentPane.add(spacerLabel, glbc);
    }
    

    /*
     *  The actionPerformed method in this class
     *  is called each time the ".keep Files in Empty Folder(s):" Check Box
     *  is Selected
     */   
    private class AddKeepFilesToEmptyFoldersHandler extends AbstractAction {	       	
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox cb = (JCheckBox)e.getSource();
            // Determine status            
            if (cb.isSelected()) {
                getBagView().getBag().isAddKeepFilesToEmptyFolders(true);                                
                getBagView().getInfoFormsPane().getSerializeValue().setText("true");
            } else {
                getBagView().getBag().isAddKeepFilesToEmptyFolders(false);
            }
        }
    }
	
	/*
     *  Setting and displaying the ".keep Files in Empty Folder(s):" Check Box 
     *  on the Create Bag In Place Pane
     */
    private void layoutAddKeepFilesToEmptyCheckBox(JPanel contentPane, int row) {
        // Delete Empty Folder(s)
        JLabel addKeepFilesToEmptyFoldersCheckBoxLabel = new JLabel(Context.getMessage("bag.label.addkeepfilestoemptyfolders"));
        addKeepFilesToEmptyFoldersCheckBoxLabel.setToolTipText(Context.getMessage("bag.addkeepfilestoemptyfolders.help"));        
        
        JLabel spacerLabel = new JLabel();
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 5, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        contentPane.add(addKeepFilesToEmptyFoldersCheckBoxLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 40, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
        contentPane.add(getAddKeepFilesToEmptyFoldersCheckBox(), glbc);
        glbc = LayoutUtil.buildGridBagConstraints(2, row, 1, 1, 40, 50, GridBagConstraints.NONE, GridBagConstraints.EAST);
        contentPane.add(spacerLabel, glbc);
    }
	
    private void layoutSpacer(JPanel contentPanel, int row) {        
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST);         
        contentPanel.add(new JLabel(""),glbc);
    }

    protected JComponent createButtonBar() {
        CommandGroup dialogCommandGroup = CommandGroup.createCommandGroup(null, getCommandGroupMembers());
        JComponent buttonBar = dialogCommandGroup.createButtonBar();
        GuiStandardUtils.attachDialogBorder(buttonBar);
        return buttonBar;
    }

    public ArrayList<File> getSelectedDirectories() {
        return selectedDirectories;
    }

    public void setSelectedDirectories(ArrayList<File> selectedDirectories) {
        this.selectedDirectories = selectedDirectories;
    }
    
	
    protected Object[] getCommandGroupMembers() {
        return new AbstractCommand[] { getFinishCommand(), getCancelCommand() };
    }
    protected String getFinishCommandId() {
        return DEFAULT_FINISH_COMMAND_ID;
    }
    protected String getCancelCommandId() {
        return DEFAULT_CANCEL_COMMAND_ID;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    	invalidate();
    	repaint();
    }

    private class BrowseFileHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            BagView bagView = getBagView();
            DefaultBag bag = bagView.getBag();
           
            File selectFile = new File(File.separator+".");
            JFrame frame = new JFrame();
            JFileChooser fs = new JFileChooser(selectFile);
            fs.setDialogType(JFileChooser.OPEN_DIALOG);
            fs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fs.addChoosableFileFilter(bagView.getInfoFormsPane().getNoFilter());
            fs.setFileFilter(bagView.getInfoFormsPane().getNoFilter());
            fs.setMultiSelectionEnabled(true);
            fs.setDialogTitle(Context.getMessage("NewBagsDialog.selectData.label"));
            if (getBagView().getBagRootPath() != null) {
                fs.setCurrentDirectory(bagView.getBagRootPath().getParentFile());
            }
            fs.setCurrentDirectory(bag.getRootDir());
            if (bag.getName() != null && !bag.getName().equalsIgnoreCase(Context.getMessage("bag.label.noname"))) {
                String selectedName = bag.getName();
                if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                    selectedName += "."+DefaultBag.ZIP_LABEL;
                    fs.setFileFilter(bagView.getInfoFormsPane().getZipFilter());
                }
                else if (bag.getSerialMode() == DefaultBag.TAR_MODE || bag.getSerialMode() == DefaultBag.TAR_GZ_MODE || bag.getSerialMode() == DefaultBag.TAR_BZ2_MODE) {                    
                    selectedName += "."+DefaultBag.TAR_LABEL;
                    fs.setFileFilter(getBagView().getInfoFormsPane().getTarFilter());
                }
                else {
                    fs.setFileFilter(getBagView().getInfoFormsPane().getNoFilter());
                }
                fs.setSelectedFile(new File(selectedName));
            } else {
                fs.setFileFilter(bagView.getInfoFormsPane().getNoFilter());
            }
            int	option = fs.showOpenDialog(frame);

            if(option == JFileChooser.APPROVE_OPTION){                
                if(fs.getSelectedFiles() != null){
                    selectedDirectories.clear();
                    selectedDirectories.addAll(Arrays.asList(fs.getSelectedFiles()));
                    selectionInfoField.setText(
                        Context.getMessage("NewBagsDialog.selectInfoField.label",new String [] {
                            ""+fs.getSelectedFiles().length
                        })
                    );      
                    selectionInfoField.invalidate();
                }
            }
        }
    }

    /*
     *  The actionPerformed method in this class
     *  is called each time the "OK" button is clicked.
     *  The Create Bag In Place is created based on the 
     *  ".keep Files in Empty Folder(s):" Check Box being selected
     */
    private class OkNewBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {            
            
            if(bagInPlace){
                NewBagsInPlaceWorker worker = new NewBagsInPlaceWorker(getSelectedDirectories(),(String)bagVersionList.getSelectedItem(),(String)profileList.getSelectedItem());
                NewBagsDialog.this.dispose();                        
                SwingUtils.monitor(
                    worker,
                    Context.getMessage("NewBagsDialog.NewBagsInPlaceWorker.title"),
                    Context.getMessage("NewBagsDialog.NewBagsInPlaceWorker.label")
                );                   
            }else if(!(outputDir != null && outputDir.isDirectory() && outputDir.canWrite() && !FUtils.hasChildren(outputDir))){                
                SwingUtils.ShowError(
                    Context.getMessage("NewBagsDialog.outputDir.browse.title"),
                    Context.getMessage("NewBagsDialog.outputDir.browse.title")
                );
            }else{
                NewBagsWorker worker = new NewBagsWorker(getSelectedDirectories(),getOutputDir(),(String)bagVersionList.getSelectedItem(),(String)profileList.getSelectedItem());
                NewBagsDialog.this.dispose();
                SwingUtils.monitor(
                    worker,
                    Context.getMessage("NewBagsDialog.NewBagsWorker.title"),
                    Context.getMessage("NewBagsDialog.NewBagsWorker.label")
                );                                   
            }
        }
    }
    private class CancelNewBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            NewBagsDialog.this.dispose();            
        }
    }
    private class NewBagsInPlaceWorker extends LongTask2{
        private ArrayList<File>files = new ArrayList<File>();
        private String version;
        private String profile;
        public NewBagsInPlaceWorker(ArrayList<File>files,String version,String profile){
            this.files = files;
            this.version = version;
            this.profile = profile;
        }

        public ArrayList<File> getFiles() {
            if(files == null){
                files = new ArrayList<File>();
            }
            return files;
        }

        public void setFiles(ArrayList<File> files) {
            this.files = files;
        }
        
        public ArrayList<File> getBadRWDirs(File file){
            ArrayList<File>badDirs = new ArrayList<File>();

            if(file.isDirectory()){
                if(!file.canRead() || !file.canWrite()){
                    badDirs.add(file);
                }else{
                    for(File f:file.listFiles()){  
                        badDirs.addAll(getBadRWDirs(f));
                    }
                }
            }
            return badDirs;
        }        
        
        @Override
        protected Object doInBackground() throws Exception {          
            
            BusyIndicator.showAt(SwingUtils.getFrame());
            
            try{
                
                //pre check: zijn alle mappen schrijfbaar?
                ArrayList<File>badDirs = new ArrayList<File>();
                for(File file:files){                    
                    badDirs.addAll(getBadRWDirs(file));
                }
                if(!badDirs.isEmpty()){                    
                    SwingUtils.ShowError(null,
                        Context.getMessage("NewBagsDialog.NewBagsInPlaceWorker.error.badrwdirs",new Object [] {
                            new Integer(badDirs.size())
                        })                            
                    );
                    BusyIndicator.clearAt(SwingUtils.getFrame());
                    return null;
                }
                
                ArrayList<Integer>succeeded = new ArrayList<Integer>();
                BagView bagView = getBagView();
                MetsBag bag = bagView.getBag();                 
               
                for(int i = 0; i< files.size();i++){                                
                    
                    //ledig mets
                    bagView.getInfoFormsPane().getInfoInputPane().setMets(new Mets());
                    Mets mets = bagView.getInfoFormsPane().getInfoInputPane().getMets();
                    
                    File file = files.get(i);                    
                    
                    //zoek metadata bestanden (todo: haal die uit payload lijst)
                    for(String metadataPath:getMetadataPaths()){
                        File metadataFile = new File(file,metadataPath);                   
                    
                        if(metadataFile.exists() && metadataFile.isFile()){                            
                            try{
                                if(metadataFile.getAbsolutePath().endsWith(".xml")){                    
                                    MdSec mdSec = MetsUtils.createMdSec(metadataFile);                                    
                                    mets.getDmdSec().add(mdSec);
                                }else if(metadataFile.getAbsolutePath().endsWith(".csv")){
                                
                                }
                            }catch(Exception e){                                
                                log.debug(e);
                            }
                        }
                    }
                    
                    if(bag.isAddKeepFilesToEmptyFolders()){                    
                        bagView.createBagsHandler.createPreBagAddKeepFilesToEmptyFolders(
                            file,
                            version,
                            profile,
                            getMetadataPaths().toArray(new String [] {})
                        );					
                    }else{							
                        bagView.createBagsHandler.createPreBag(
                            file, 
                            version,
                            profile,
                            getMetadataPaths().toArray(new String [] {})
                        );
                    }
                    int percent = (int)Math.floor( ((i+1) / ((float)getSelectedDirectories().size()))*100);                                                                        
                    setProgress(percent); 
                    succeeded.add(i);
                }
                
                //open laatste geslaagde bagit
                if(succeeded.size() > 0){
                    int last = succeeded.get(succeeded.size() - 1);
                    File file = getSelectedDirectories().get(last);
                    bagView.openBagHandler.openExistingBag(file);
                }
                
            }catch(Exception e){
                log.error(e);                
            }
            
            BusyIndicator.clearAt(SwingUtils.getFrame());
            
            return null;
        }

        @Override
        public void cancel(){            
        }        
    }
    
    private class NewBagsWorker extends LongTask2 {
        private ArrayList<File>files = new ArrayList<File>();
        private File dir;
        private String version;
        private String profile;  
        
        public NewBagsWorker(ArrayList<File>files,File dir,String version,String profile){
            this.files = files;
            this.dir = dir;
            this.version = version;
            this.profile = profile;
        }        
        
        @Override
        protected Object doInBackground() throws Exception {
            
            BusyIndicator.showAt(SwingUtils.getFrame());
            
            try{
            
                ArrayList<File>succeeded = new ArrayList<File>();
                BagView bagView = getBagView();                
                MetsBag bag = bagView.getBag(); 
                
                
                for(int i = 0; i< files.size();i++){                                                                        
                    
                    //ledig mets
                    bagView.getInfoFormsPane().getInfoInputPane().setMets(new Mets());
                    Mets mets = bagView.getInfoFormsPane().getInfoInputPane().getMets();
                    
                    File inputDir = files.get(i);
                    File out = new File(dir,inputDir.getName());
                    
                    //clear huidige toestand
                    bag.clear();
                    bag.clearProfile();
                    bag.setRootDir(out);
                    bag.setName(out.getName());
                    bag.setVersion(version);                    
                    
                    //voeg payloads toe
                    ArrayList<File>listPayloads = FUtils.listFiles(inputDir);
                    
                    for(File file:listPayloads){
                        if(file.isFile()){                            
                            bag.addFileToPayload(file);
                        }
                    }
                    
                    //zoek metadata bestanden (todo: haal die uit payload lijst)
                    for(String metadataPath:getMetadataPaths()){
                        File metadataFile = new File(inputDir,metadataPath);                       
                    
                        if(metadataFile.exists() && metadataFile.isFile()){                            
                            //voeg toe aan mets
                            try{
                                if(metadataFile.getAbsolutePath().endsWith(".xml")){                    
                                    MdSec mdSec = MetsUtils.createMdSec(metadataFile);                                    
                                    mets.getDmdSec().add(mdSec);
                                }else if(metadataFile.getAbsolutePath().endsWith(".csv")){
                                
                                }
                            }catch(Exception e){                               
                                log.debug(e);
                            }
                            //haal uit payload lijst
                            String path = "data/"+metadataPath;                            
                            bag.removeBagFile(path);
                        }
                    }
                    
                    bag.setBagItMets(new DefaultBagItMets());
                    
                    Writer writer = new FileSystemWriter(new BagFactory());
                    
                    String messages = bag.write(writer);

                    if(messages != null && !messages.trim().isEmpty()){
                        bagView.showWarningErrorDialog("Warning - bag not saved", "Problem saving bag:\n" + messages);
                    }
               
                    if(bag.isSerialized()){
                        succeeded.add(out);                        
                    }
                    
                    int percent = (int)Math.floor( ((i+1) / ((float)getSelectedDirectories().size()))*100);                                                                        
                    setProgress(percent);                     
                }
                
                //open laatste geslaagde bagit
                if(succeeded.size() > 0){                                
                    File file = succeeded.get(succeeded.size()-1);            
                    bagView.openBagHandler.openExistingBag(file);                                    
                }
            }catch(Exception e){
                log.error(e);                
            }
            
            BusyIndicator.clearAt(SwingUtils.getFrame());
            
            return null;
        }
        @Override
        public void cancel() {         
        }        
    }
}