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

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.util.LayoutUtil;
import gov.loc.repository.bagit.BagFactory.Version;
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
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.TitlePane;
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.richclient.util.GuiStandardUtils;
import ugent.bagger.bagitmets.MetsFileDateCreated;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.LongTask2;

public final class NewBagsDialog extends JDialog implements ActionListener {
    private static final Log log = LogFactory.getLog(NewBagFrame.class);
    private static final long serialVersionUID = 1L;
    private final Dimension preferredDimension = new Dimension(400, 230);
    private JPanel contentPanel;
    private JTextField selectionInfoField;    
    private ArrayList<File>selectedDirectories = new ArrayList<File>();    
    private JButton saveAsButton;
    private JComboBox bagVersionList;
    private JComboBox profileList;
    private JCheckBox addKeepFilesToEmptyFoldersCheckBox;
    protected static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";
    protected static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";	
    private ActionCommand finishCommand;
    private ActionCommand cancelCommand;    
    private JComboBox metsFileDateCreatedCombobox;

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

    public JButton getSaveAsButton() {
        if(saveAsButton == null){
            saveAsButton = new JButton(getBagView().getPropertyMessage("bag.button.browse"));
            saveAsButton.addActionListener(new BrowseFileHandler());
            saveAsButton.setEnabled(true);
            saveAsButton.setToolTipText(getBagView().getPropertyMessage("bag.button.browse.help"));
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
            profileList.setName(getBagView().getPropertyMessage("bag.label.projectlist"));
            profileList.setSelectedItem(getBagView().getPropertyMessage("bag.project.noproject"));
            profileList.setToolTipText(getBagView().getPropertyMessage("bag.projectlist.help"));
        }
        return profileList;
    }

    public void setProfileList(JComboBox profileList) {
        this.profileList = profileList;
    }

    public JCheckBox getAddKeepFilesToEmptyFoldersCheckBox() {
        if(addKeepFilesToEmptyFoldersCheckBox == null){
            addKeepFilesToEmptyFoldersCheckBox = new JCheckBox(getBagView().getPropertyMessage(""));
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
                    BagView.getInstance().setMetsFileDateCreated((MetsFileDateCreated)ie.getItem());
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
            String defaultMessage = "0 geselecteerd";
            selectionInfoField = new JTextField(defaultMessage);
            selectionInfoField.setCaretPosition(defaultMessage.length());
            selectionInfoField.setEditable(false);
            selectionInfoField.setEnabled(false);
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
            bagVersionList.setName(getBagView().getPropertyMessage("bag.label.versionlist"));
            bagVersionList.setSelectedItem(Version.V0_96.versionString);
            bagVersionList.setToolTipText(getBagView().getPropertyMessage("bag.versionlist.help")); 
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
    	titlePane.setTitle(getBagView().getPropertyMessage("NewBagInPlace.title"));
    	titlePane.setMessage(new DefaultMessage(getBagView().getPropertyMessage("NewBagInPlace.description")));
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
       
        row++;
        JLabel metsFileDateCreatedLabel = new JLabel("Date created:");        
       
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        panel.add(metsFileDateCreatedLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); 
        panel.add(getMetsFileDateCreatedCombobox(), glbc);
        
        GuiStandardUtils.attachDialogBorder(panel);
        pageControl.add(panel);        
        pageControl.add(createButtonBar(),BorderLayout.SOUTH);
       
        return pageControl;
        
    }
    
    private void layoutSelectDataContent(JPanel contentPanel, int row) {
        BagView bagView = getBagView();
        DefaultBag bag = bagView.getBag();
        
    	JLabel location = new JLabel("Select Data:");        
        
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
        
        JLabel bagVersionLabel = new JLabel(getBagView().getPropertyMessage("bag.label.version"));
        bagVersionLabel.setToolTipText(getBagView().getPropertyMessage("bag.versionlist.help"));
		
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        contentPanel.add(bagVersionLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); 
        contentPanel.add(getBagVersionList(), glbc);
    }
	
    private void layoutProfileSelectionContent(JPanel contentPane, int row) {
        // content
        // profile selection
        JLabel bagProfileLabel = new JLabel(getBagView().getPropertyMessage("Select Profile:"));
        bagProfileLabel.setToolTipText(getBagView().getPropertyMessage("bag.projectlist.help"));
	
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
        JLabel addKeepFilesToEmptyFoldersCheckBoxLabel = new JLabel(getBagView().getPropertyMessage("bag.label.addkeepfilestoemptyfolders"));
        addKeepFilesToEmptyFoldersCheckBoxLabel.setToolTipText(getBagView().getPropertyMessage("bag.addkeepfilestoemptyfolders.help"));        
        
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
            fs.setDialogTitle("Selecteer een reeks mappen");
            if (getBagView().getBagRootPath() != null) {
                fs.setCurrentDirectory(bagView.getBagRootPath().getParentFile());
            }
            fs.setCurrentDirectory(bag.getRootDir());
            if (bag.getName() != null && !bag.getName().equalsIgnoreCase(bagView.getPropertyMessage("bag.label.noname"))) {
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
                    for(File file:fs.getSelectedFiles()){                        
                        selectedDirectories.add(file);
                    }
                    selectionInfoField.setText(""+fs.getSelectedFiles().length+" geselecteerd");                    
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
            NewBagsInPlaceWorker worker = new NewBagsInPlaceWorker(getSelectedDirectories(),(String)bagVersionList.getSelectedItem(),(String)profileList.getSelectedItem());
            NewBagsDialog.this.dispose();                        
            SwingUtils.monitor(worker,"bag in place","making bag in place: ");                   
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
        
        @Override
        protected Object doInBackground() throws Exception {          
            
            BusyIndicator.showAt(SwingUtils.getFrame());
            
            ArrayList<Integer>succeeded = new ArrayList<Integer>();
            BagView bagView = getBagView();
            DefaultBag bag = bagView.getBag(); 
            
            try{
               
                for(int i = 0; i< getSelectedDirectories().size();i++){                                
                    
                    File file = getSelectedDirectories().get(i);
                    
                    System.out.println("working on "+file);
                    
                    if(bag.isAddKeepFilesToEmptyFolders()){                    
                        bagView.createBagsHandler.createPreBagAddKeepFilesToEmptyFolders(
                            file,
                            version,
                            profile
                        );					
                    }else{							
                        bagView.createBagsHandler.createPreBag(
                            file, 
                            version,
                            profile
                        );
                    }
                    int percent = (int)Math.floor( ((i+1) / ((float)getSelectedDirectories().size()))*100);                                                                        
                    setProgress(percent); 
                    succeeded.add(i);
                }
                
            }catch(Exception e){
                throw e;
            }
            
            BusyIndicator.clearAt(SwingUtils.getFrame());
            
            //open laatste geslaagde bagit
            if(succeeded.size() > 0){
                int last = succeeded.get(succeeded.size() - 1);
                File file = getSelectedDirectories().get(last);
                bagView.openBagHandler.openExistingBag(file);
            }
            
            return null;
        }

        @Override
        public void cancel() {
            System.out.println("this is mine to finish!");
        }        
    }
}