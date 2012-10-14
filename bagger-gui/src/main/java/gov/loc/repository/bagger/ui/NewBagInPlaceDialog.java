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
import org.springframework.richclient.util.GuiStandardUtils;
import ugent.bagger.bagitmets.MetsFileDateCreated;

public final class NewBagInPlaceDialog extends JDialog implements ActionListener {
    private static final Log log = LogFactory.getLog(NewBagFrame.class);
    private static final long serialVersionUID = 1L;
    
    //Nicolas Franck: can be fetched in different way!
    /*
    private BagView bagView;
    private DefaultBag bag = null;*/
    
    private final Dimension preferredDimension = new Dimension(400, 230);
    private JPanel contentPanel;
    private JTextField bagNameField;
    private File bagFile;
    private String bagFileName = "";
    private JButton saveAsButton;
    private JComboBox bagVersionList;
    private JComboBox profileList;
    private JCheckBox addKeepFilesToEmptyFoldersCheckBox;
    protected static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";
    protected static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";	
    private ActionCommand finishCommand;
    private ActionCommand cancelCommand;
    
    //Nicolas Franck: metsFileDateCreated
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
    
    public NewBagInPlaceDialog(JFrame frame,boolean isModal, String title) {
        super(frame,isModal);
        setTitle(title);
        
        
        /*Application app = Application.instance();
        ApplicationPage page = app.getActiveWindow().getPage();
        PageComponent component = page.getActiveComponent();        
        
        this.bagView = (component != null) ? BagView.instance : bagView;*/
        
        //Nicolas Franck
        /*if (component != null) this.bagView = BagView.instance;
        else this.bagView = bagView;       
        
        if (getBagView() != null) {          
            getContentPane().removeAll();        
            contentPanel = createComponents();            
        } else {
            contentPanel = new JPanel();
        }*/
        
        getContentPane().add(getContentPanel(), BorderLayout.CENTER);
        
        //Nicolas Franck: liefst extern in te stellen
        /*setPreferredSize(preferredDimension);
        setLocation(200, 100);*/
        
        //Nicolas Franck: als je dit hier doet, krijg je in Ubuntu een X11 error. Beter: bij oproepende code
        //pack();
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
    
    
    private JPanel createComponents() {

    	TitlePane titlePane = new TitlePane();
    	//initStandardCommands();
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
        
        
        //Nicolas Franck - start
        row++;
        JLabel metsFileDateCreatedLabel = new JLabel("Date created:");
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
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        panel.add(metsFileDateCreatedLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); 
        panel.add(metsFileDateCreatedCombobox, glbc);
        //Nicolas Frank - end
        
        
        GuiStandardUtils.attachDialogBorder(panel);
        pageControl.add(panel);        
        pageControl.add(createButtonBar(),BorderLayout.SOUTH);

        //Nicolas Franck: als je dit hier doet, krijg je in Ubuntu een X11 error. Beter: bij oproepende code
        //pack();
        return pageControl;
        
    }
    
    private void layoutSelectDataContent(JPanel contentPanel, int row) {
        BagView bagView = getBagView();
        DefaultBag bag = bagView.getBag();
        
    	JLabel location = new JLabel("Select Data:");
        
        //Nicolas Franck
        /*
    	saveAsButton = new JButton(bagView.getPropertyMessage("bag.button.browse"));
    	saveAsButton.addActionListener(new BrowseFileHandler());
    	saveAsButton.setEnabled(true);
    	saveAsButton.setToolTipText(bagView.getPropertyMessage("bag.button.browse.help"));*/
    	
    	//String fileName = "";
        
        String fileName = (bag != null)? bag.getName():"";
        
        //Nicolas Franck
        /*
    	if (bag != null) {
            fileName = bag.getName();
        }*/
    	bagNameField = new JTextField(fileName);
        bagNameField.setCaretPosition(fileName.length());
        bagNameField.setEditable(false);
        bagNameField.setEnabled(false);
        
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        contentPanel.add(location, glbc);
        
        glbc = LayoutUtil.buildGridBagConstraints(2, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.EAST); 
        glbc.ipadx=5;
        glbc.ipadx=0;
        contentPanel.add(getSaveAsButton(), glbc);
        
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        glbc.ipadx=5;
        glbc.ipadx=0;
        contentPanel.add(bagNameField, glbc);
    }
	
    private void layoutBagVersionContent(JPanel contentPanel, int row) {
        
        JLabel bagVersionLabel = new JLabel(getBagView().getPropertyMessage("bag.label.version"));
        bagVersionLabel.setToolTipText(getBagView().getPropertyMessage("bag.versionlist.help"));
        ArrayList<String> versionModel = new ArrayList<String>();        
        
        for(Version version:Version.values()){
            versionModel.add(version.versionString);
        }        

        bagVersionList = new JComboBox(versionModel.toArray());
        bagVersionList.setName(getBagView().getPropertyMessage("bag.label.versionlist"));
        bagVersionList.setSelectedItem(Version.V0_96.versionString);
        bagVersionList.setToolTipText(getBagView().getPropertyMessage("bag.versionlist.help"));        
		
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        contentPanel.add(bagVersionLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); 
        contentPanel.add(bagVersionList, glbc);
    }
	
    private void layoutProfileSelectionContent(JPanel contentPane, int row) {
        // content
        // profile selection
        JLabel bagProfileLabel = new JLabel(getBagView().getPropertyMessage("Select Profile:"));
        bagProfileLabel.setToolTipText(getBagView().getPropertyMessage("bag.projectlist.help"));

        //Nicolas Franck
        /*
        profileList = new JComboBox(bagView.getProfileStore().getProfileNames());
        profileList.setName(bagView.getPropertyMessage("bag.label.projectlist"));
        profileList.setSelectedItem(bagView.getPropertyMessage("bag.project.noproject"));
        profileList.setToolTipText(bagView.getPropertyMessage("bag.projectlist.help"));*/
	
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
        
        //Nicolas Franck
        /*
        addKeepFilesToEmptyFoldersCheckBox = new JCheckBox(bagView.getPropertyMessage(""));
        addKeepFilesToEmptyFoldersCheckBox.setSelected(bag.isAddKeepFilesToEmptyFolders());
        addKeepFilesToEmptyFoldersCheckBox.addActionListener(new AddKeepFilesToEmptyFoldersHandler());*/
               
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
	
    protected Object[] getCommandGroupMembers() {
        return new AbstractCommand[] { getFinishCommand(), getCancelCommand() };
    }
	
    /**
	 * Initialize the standard commands needed on a Dialog: Ok/Cancel.
	 */
    
    //Nicolas Franck: zie getter en setter
    /*
    private void initStandardCommands() {
        finishCommand = new ActionCommand(getFinishCommandId()) {
            @Override
            public void doExecuteCommand() {
                new OkNewBagHandler().actionPerformed(null);
            }
        };
        cancelCommand = new ActionCommand(getCancelCommandId()) {
            @Override
            public void doExecuteCommand() {
                new CancelNewBagHandler().actionPerformed(null);
            }
        };
    }*/
	
    protected String getFinishCommandId() {
        return DEFAULT_FINISH_COMMAND_ID;
    }
    protected String getCancelCommandId() {
        return DEFAULT_CANCEL_COMMAND_ID;
    }

    //Nicolas Franck: never used
    /*
    public void setBag(DefaultBag bag) {
    	this.bag = bag;
    	contentPanel.invalidate();
    }*/

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
            fs.setDialogTitle("Existing Data Location");
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

            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fs.getSelectedFile();
                bagFile = file;
                bagFileName = bagFile.getAbsolutePath();
                // TODO: bag name is bag_<filename>
                //bagView.bagNameField.setText(bagFile.getName());
                bagNameField.setText(bagFileName);
                bagNameField.setCaretPosition(bagFileName.length());
                bagNameField.invalidate();
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
            BagView bagView = getBagView();
            DefaultBag bag = bagView.getBag();
            
            log.info("BagVersionFrame.OkNewBagHandler");
            setVisible(false);			
            if(bag.isAddKeepFilesToEmptyFolders()){
                bagView.createBagInPlaceHandler.createPreBagAddKeepFilesToEmptyFolders(
                    bagFile,
                    (String)bagVersionList.getSelectedItem(),
                    (String)profileList.getSelectedItem()
                );					
            }else{							
                bagView.createBagInPlaceHandler.createPreBag(
                    bagFile, 
                    (String)bagVersionList.getSelectedItem(),
                    (String)profileList.getSelectedItem()
                );
            }
        }
    }
    private class CancelNewBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }
}