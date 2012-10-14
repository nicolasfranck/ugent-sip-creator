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
 * Nicolas Franck: code based on NewBagFrame
 */
package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.ui.util.LayoutUtil;
import gov.loc.repository.bagit.BagFactory.Version;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.TitlePane;
import org.springframework.richclient.util.GuiStandardUtils;

public class NewBagDialog extends JDialog implements ActionListener {
    private static final Log log = LogFactory.getLog(NewBagDialog.class);
    private static final long serialVersionUID = 1L;    
    private JComboBox bagVersionList;
    private JComboBox profileList;
    protected static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";
    protected static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";
    private ActionCommand finishCommand;
    private ActionCommand cancelCommand;
   
    public BagView getBagView(){
        return BagView.getInstance();
    }
    public NewBagDialog(JFrame frame,boolean isModal, String title) {     
        super(frame,isModal);                        
        setTitle(title);

        //Nicolas Franck
        /*
        if (component != null) {
            this.bagView = BagView.instance;
        }
        else {
            this.bagView = bagView;
        }*/

        JPanel createPanel;
        if(getBagView() != null){
            getContentPane().removeAll();
            createPanel = createComponents();
        } else {
            createPanel = new JPanel();
        }
        getContentPane().add(createPanel, BorderLayout.CENTER);

        //Nicolas Franck: eerder iets wat je extern wil controleren
        //setPreferredSize(new Dimension(400, 200));
        //setLocation(300, 200);        
        //Nicolas Franck: als je dit hier doet, krijg je in Ubuntu een X11 error. Beter: bij oproepende code
        //pack();       
        
    }

    public ActionCommand getFinishCommand() {
        if(finishCommand == null){
            finishCommand = new ActionCommand(getFinishCommandId()) {
                @Override
                public void doExecuteCommand() {
                    log.info("BagVersionFrame.OkNewBagHandler");
                    NewBagDialog.this.setVisible(false);
                    getBagView().startNewBagHandler.createNewBag(
                        (String)bagVersionList.getSelectedItem(),
                        (String)profileList.getSelectedItem()
                    );
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
                    NewBagDialog.this.setVisible(false);
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
    
    private JPanel createComponents() {
    	TitlePane titlePane = new TitlePane();
        //initStandardCommands();
        JPanel pageControl = new JPanel(new BorderLayout());
        JPanel titlePaneContainer = new JPanel(new BorderLayout());
        titlePane.setTitle(getBagView().getPropertyMessage("NewBagFrame.title"));
        titlePane.setMessage( new DefaultMessage(getBagView().getPropertyMessage("NewBagFrame.description")));
        titlePaneContainer.add(titlePane.getControl());
        titlePaneContainer.add(new JSeparator(), BorderLayout.SOUTH);
        pageControl.add(titlePaneContainer, BorderLayout.NORTH);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());

        int row = 0;
        layoutBagVersionSelection(contentPane, row++);
        layoutProfileSelection(contentPane, row++);

        if(getPreferredSize() != null){
            contentPane.setPreferredSize(getPreferredSize());
        }
		
        GuiStandardUtils.attachDialogBorder(contentPane);
        pageControl.add(contentPane);
        JComponent buttonBar = createButtonBar();
        pageControl.add(buttonBar,BorderLayout.SOUTH);

        //Nicolas Franck: als je dit hier doet, krijg je in Ubuntu een X11 error. Beter: bij oproepende code
        //this.pack();
        return pageControl;
    }

	

    private void layoutBagVersionSelection(JPanel contentPane, int row) {
        //contents
        // Bag version dropdown list
        JLabel bagVersionLabel = new JLabel(getBagView().getPropertyMessage("bag.label.version"));
        bagVersionLabel.setToolTipText(getBagView().getPropertyMessage("bag.versionlist.help"));
        
        //Nicolas Franck: zie getBagVersionList()
        /*
        ArrayList<String> versionModel = new ArrayList<String>();
        Version[] vals = Version.values();
        for (int i=0; i < vals.length; i++) {
            versionModel.add(vals[i].versionString);
        }
        bagVersionList = new JComboBox(versionModel.toArray());
        bagVersionList.setName(getBagView().getPropertyMessage("bag.label.versionlist"));
        bagVersionList.setSelectedItem(Version.V0_96.versionString);
        bagVersionList.setToolTipText(getBagView().getPropertyMessage("bag.versionlist.help"));        
        */

        JLabel spacerLabel = new JLabel();
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 5, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        contentPane.add(bagVersionLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 40, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
        contentPane.add(getBagVersionList(), glbc);
        glbc = LayoutUtil.buildGridBagConstraints(2, row, 1, 1, 40, 50, GridBagConstraints.NONE, GridBagConstraints.EAST);
        contentPane.add(spacerLabel, glbc);
    }

    public JComboBox getBagVersionList() {
        if(bagVersionList == null){
            ArrayList<String> versionModel = new ArrayList<String>();
            Version[] vals = Version.values();
            for (int i=0; i < vals.length; i++) {
                versionModel.add(vals[i].versionString);
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
	
    private void layoutProfileSelection(JPanel contentPane, int row) {
        // content
        // profile selection
        JLabel bagProfileLabel = new JLabel(getBagView().getPropertyMessage("Select Profile:"));
        bagProfileLabel.setToolTipText(getBagView().getPropertyMessage("bag.projectlist.help"));

        //Nicolas Franck: zie getProfileList()
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
                log.info("BagVersionFrame.OkNewBagHandler");
                NewBagFrame.this.setVisible(false);
                bagView.startNewBagHandler.createNewBag(
                    (String)bagVersionList.getSelectedItem(),
                    (String)profileList.getSelectedItem()
                );
            }
        };
        cancelCommand = new ActionCommand(getCancelCommandId()) {
            @Override
            public void doExecuteCommand() {
                NewBagFrame.this.setVisible(false);
            }
        };
    }*/
	
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
}