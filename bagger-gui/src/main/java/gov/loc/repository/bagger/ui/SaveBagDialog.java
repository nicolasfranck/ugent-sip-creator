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

//based on SaveBagFrame

package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagit.Manifest.Algorithm;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
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
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;

public final class SaveBagDialog extends JDialog implements ActionListener {
    private static final Log log = LogFactory.getLog(SaveBagDialog.class);
    private static final long serialVersionUID = 1L;   
    private File bagFile;
    private String bagFileName = "";
    private final Dimension preferredDimension = new Dimension(500,350);
    private JPanel savePanel;
    private JPanel serializeGroupPanel;
    private JTextField bagNameField;    
    private JButton browseButton;
    private JButton okButton;
    private JButton cancelButton;
    private JRadioButton noneButton;
    private JRadioButton zipButton;
    private JRadioButton tarButton;
    private JRadioButton tarGzButton;
    private JRadioButton tarBz2Button;    
    private JComboBox tagAlgorithmList;
    private JComboBox payAlgorithmList;        
    private JComboBox metsFileDateCreatedCombobox;    
    protected static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";
    protected static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";
    private ActionCommand finishCommand;
    private ActionCommand cancelCommand;

    public BagView getBagView(){
        return BagView.getInstance();
    }
    public SaveBagDialog(final JFrame frame,boolean isModal,String title) {
        super(frame,isModal);
        setTitle(title);
             
        if (getBagView() != null) {
            getContentPane().removeAll();
            savePanel = createComponents();
        }else{
            savePanel = new JPanel();
        }
        getContentPane().add(savePanel, BorderLayout.CENTER);        
        setPreferredSize(preferredDimension);        
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
    public ActionCommand getFinishCommand() {
        if(finishCommand == null){
            finishCommand = new ActionCommand(getFinishCommandId()) {
                @Override
                public void doExecuteCommand(){
                    new OkSaveBagHandler().actionPerformed(null);
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
            cancelCommand = new ActionCommand(getCancelCommandId()){
                @Override
                public void doExecuteCommand(){
                    new CancelSaveBagHandler().actionPerformed(null);
                }
            };
        }
        return cancelCommand;
    }
    public void setCancelCommand(ActionCommand cancelCommand) {
        this.cancelCommand = cancelCommand;
    }
    protected String getFinishCommandId() {
        return DEFAULT_FINISH_COMMAND_ID;
    }
    protected String getCancelCommandId() {
        return DEFAULT_CANCEL_COMMAND_ID;
    }

    private JPanel createComponents() {
        Border border = new EmptyBorder(5, 5, 5, 5);
        
        TitlePane titlePane = new TitlePane();     
        JPanel pageControl = new JPanel(new BorderLayout());
        JPanel titlePaneContainer = new JPanel(new BorderLayout());
        titlePane.setTitle(Context.getMessage("SaveBagDialog.title"));
        titlePane.setMessage(new DefaultMessage(Context.getMessage("SaveBagDialog.description")));
        titlePaneContainer.add(titlePane.getControl());
        titlePaneContainer.add(new JSeparator(), BorderLayout.SOUTH);
        pageControl.add(titlePaneContainer, BorderLayout.NORTH);
        JPanel contentPane = new JPanel();

        // TODO: Add bag name field
    	// TODO: Add save name file selection button
        JLabel location = new JLabel(Context.getMessage("SaveBagDialog.browseLabel.label"));
    	browseButton = new JButton(Context.getMessage("SaveBagDialog.browseButton.label"));
    	browseButton.addActionListener(new SaveBagAsHandler());
        browseButton.setEnabled(true);
        browseButton.setToolTipText(Context.getMessage("SaveBagDialog.browseButton.tooltip"));
    	String fileName = "";
    	DefaultBag bag = getBagView().getBag();
    	if (bag != null) {
            fileName = bag.getName();
    	}
    	bagNameField = new JTextField(fileName);
        bagNameField.setCaretPosition(fileName.length());
        bagNameField.setEditable(false);
        bagNameField.setEnabled(false);
        
        // TODO: Add format label
    	JLabel serializeLabel;
        serializeLabel = new JLabel(Context.getMessage("bag.label.ispackage"));
        serializeLabel.setToolTipText(Context.getMessage("bag.serializetype.help"));

        
        
    	// TODO: Add format selection panel
        noneButton = new JRadioButton(Context.getMessage("bag.serializetype.none"));
        noneButton.setEnabled(true);
        AbstractAction serializeListener = new SerializeBagHandler();
        noneButton.addActionListener(serializeListener);
        noneButton.setToolTipText(Context.getMessage("bag.serializetype.none.help"));

        zipButton = new JRadioButton(Context.getMessage("bag.serializetype.zip"));
        zipButton.setEnabled(true);        
        zipButton.addActionListener(serializeListener);
        zipButton.setToolTipText(Context.getMessage("bag.serializetype.zip.help"));

        tarButton = new JRadioButton(Context.getMessage("bag.serializetype.tar"));
        tarButton.setEnabled(true);        
        tarButton.addActionListener(serializeListener);
        tarButton.setToolTipText(Context.getMessage("bag.serializetype.tar.help"));
        
        tarGzButton = new JRadioButton(Context.getMessage("bag.serializetype.targz"));
        tarGzButton.setEnabled(true);        
        tarGzButton.addActionListener(serializeListener);
        tarGzButton.setToolTipText(Context.getMessage("bag.serializetype.targz.help"));
        
        tarBz2Button = new JRadioButton(Context.getMessage("bag.serializetype.tarbz2"));
        tarBz2Button.setEnabled(true);        
        tarBz2Button.addActionListener(serializeListener);
        tarBz2Button.setToolTipText(Context.getMessage("bag.serializetype.tarbz2.help"));

        short mode = bag.getSerialMode();
        
    	if (mode == DefaultBag.NO_MODE) {
            noneButton.setSelected(true);
    	} else if (mode == DefaultBag.ZIP_MODE) {
            zipButton.setSelected(true);
    	} else if (mode == DefaultBag.TAR_MODE) {
            tarButton.setSelected(true);
    	} else if (mode == DefaultBag.TAR_GZ_MODE) {
            tarGzButton.setSelected(true);
    	} else if (mode == DefaultBag.TAR_BZ2_MODE) {
            tarBz2Button.setSelected(true);
    	} else {
            noneButton.setSelected(true);
    	}
        
        ButtonGroup serializeGroup = new ButtonGroup();
        serializeGroup.add(noneButton);
        serializeGroup.add(zipButton);
        serializeGroup.add(tarButton);
        serializeGroup.add(tarGzButton);
        serializeGroup.add(tarBz2Button);
        serializeGroupPanel = new JPanel(new FlowLayout());
        serializeGroupPanel.add(serializeLabel);
        serializeGroupPanel.add(noneButton);
        serializeGroupPanel.add(zipButton);
        serializeGroupPanel.add(tarButton);
        serializeGroupPanel.add(tarGzButton);
        serializeGroupPanel.add(tarBz2Button);
        serializeGroupPanel.setBorder(border);
        serializeGroupPanel.setEnabled(true);
        serializeGroupPanel.setToolTipText(Context.getMessage("bag.serializetype.help"));

        //manifest prohibited!
        getBagView().getBag().isBuildTagManifest(true);        

        JLabel tagAlgorithmLabel = new JLabel(Context.getMessage("bag.label.tagalgorithm"));
        tagAlgorithmLabel.setToolTipText(Context.getMessage("bag.label.tagalgorithm.help"));
        ArrayList<String> listModel = new ArrayList<String>();
        for(Algorithm algorithm : Algorithm.values()) {
            listModel.add(algorithm.bagItAlgorithm);
        }
        tagAlgorithmList = new JComboBox(listModel.toArray());
        tagAlgorithmList.setName(Context.getMessage("bag.tagalgorithmlist"));
        tagAlgorithmList.setSelectedItem(bag.getTagManifestAlgorithm());
        tagAlgorithmList.addActionListener(new TagAlgorithmListHandler());
        tagAlgorithmList.setToolTipText(Context.getMessage("bag.tagalgorithmlist.help"));    	
        
        //manifest prohibited!
        getBagView().getBag().isBuildPayloadManifest(true);        

        JLabel payAlgorithmLabel = new JLabel(Context.getMessage("bag.label.payalgorithm"));
        payAlgorithmLabel.setToolTipText(Context.getMessage("bag.payalgorithm.help"));
        payAlgorithmList = new JComboBox(listModel.toArray());
        payAlgorithmList.setName(Context.getMessage("bag.payalgorithmlist"));
        payAlgorithmList.setSelectedItem(bag.getPayloadManifestAlgorithm());
        payAlgorithmList.addActionListener(new PayAlgorithmListHandler());
        payAlgorithmList.setToolTipText(Context.getMessage("bag.payalgorithmlist.help"));
        
    	GridBagLayout layout = new GridBagLayout();
        GridBagConstraints glbc = new GridBagConstraints();
        JPanel panel = new JPanel(layout);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        int row = 0;
        
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(location, glbc);
        panel.add(location);
        
        buildConstraints(glbc, 2, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.EAST); 
        glbc.ipadx=5;
        layout.setConstraints(browseButton, glbc);
        glbc.ipadx=0;
        panel.add(browseButton);
        
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        glbc.ipadx=5;
        layout.setConstraints(bagNameField, glbc);
        glbc.ipadx=0;
        panel.add(bagNameField);        
        
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
        
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(serializeLabel, glbc);
    	panel.add(serializeLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); 
        layout.setConstraints(serializeGroupPanel, glbc);
    	panel.add(serializeGroupPanel);        
        
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(tagAlgorithmLabel, glbc);
    	panel.add(tagAlgorithmLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(tagAlgorithmList, glbc);
    	panel.add(tagAlgorithmList);        
        
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(payAlgorithmLabel, glbc);
    	panel.add(payAlgorithmLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(payAlgorithmList, glbc);
    	panel.add(payAlgorithmList);        
        
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(metsFileDateCreatedLabel, glbc);
    	panel.add(metsFileDateCreatedLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(metsFileDateCreatedCombobox, glbc);
    	panel.add(metsFileDateCreatedCombobox);        
        
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
    	
    	GuiStandardUtils.attachDialogBorder(contentPane);
        pageControl.add(panel);
        JComponent buttonBar = createButtonBar();
        pageControl.add(buttonBar,BorderLayout.SOUTH);
        
        return pageControl; 	
 
    }
    
    public void setBag(DefaultBag bag) {
        BagView bagView = getBagView();
    	bagNameField.setText(bag.getName());
    	short mode = bag.getSerialMode();
    	if (mode == DefaultBag.NO_MODE) {
            noneButton.setEnabled(true);
            noneButton.setSelected(true);
            bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.NO_LABEL);
    	} else if (mode == DefaultBag.ZIP_MODE) {
            zipButton.setEnabled(true);
            zipButton.setSelected(true);
            bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.ZIP_LABEL);
    	} else if (mode == DefaultBag.TAR_MODE) {
            tarButton.setEnabled(true);
            tarButton.setSelected(true);
            bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.TAR_LABEL);
    	} else if (mode == DefaultBag.TAR_GZ_MODE) {
            tarGzButton.setEnabled(true);
            tarGzButton.setSelected(true);
            bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.TAR_GZ_LABEL);
    	} else if (mode == DefaultBag.TAR_BZ2_MODE) {
            tarBz2Button.setEnabled(true);
            tarBz2Button.setSelected(true);
            bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.TAR_BZ2_LABEL);
    	} else {
            noneButton.setEnabled(true);
            noneButton.setSelected(true);
            bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.NO_LABEL);
    	}
    	savePanel.invalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	invalidate();
    	repaint();
    }

    public class SerializeBagHandler extends AbstractAction {
    	private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton cb = (JRadioButton)e.getSource();
            boolean isSel = cb.isSelected();
            BagView bagView = getBagView();
            DefaultBag bag = bagView.getBag();
            if (isSel) {
            	if (cb == noneButton) {
                    bag.isSerial(false);
                    bag.setSerialMode(DefaultBag.NO_MODE);
                    bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.NO_LABEL);
            	} else if (cb == zipButton) {
                    bag.isSerial(true);
                    bag.setSerialMode(DefaultBag.ZIP_MODE);
                    bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.ZIP_LABEL);
            	} else if (cb == tarButton) {
                    bag.isSerial(true);
                    bag.setSerialMode(DefaultBag.TAR_MODE);
                    bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.TAR_LABEL);
            	} else if (cb == tarGzButton) {
                    bag.isSerial(true);
                    bag.setSerialMode(DefaultBag.TAR_GZ_MODE);
                    bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.TAR_GZ_LABEL);
            	} else if (cb == tarBz2Button) {
                    bag.isSerial(true);
                    bag.setSerialMode(DefaultBag.TAR_BZ2_MODE);
                    bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.TAR_BZ2_LABEL);
            	} else {
                    bag.isSerial(false);
                    bag.setSerialMode(DefaultBag.NO_MODE);
                    bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.NO_LABEL);
            	}
            }
        }
    }

    private class SaveBagAsHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            BagView bagView = getBagView();
            File selectFile = new File(File.separator+".");
            JFrame frame = new JFrame();
            JFileChooser fs = new JFileChooser(selectFile);           
            fs.setDialogType(JFileChooser.SAVE_DIALOG);
            fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fs.addChoosableFileFilter(bagView.getInfoFormsPane().getNoFilter());
            fs.addChoosableFileFilter(bagView.getInfoFormsPane().getZipFilter());
            fs.addChoosableFileFilter(bagView.getInfoFormsPane().getTarFilter());
            fs.setDialogTitle(Context.getMessage("SaveBagDialog.title"));
            DefaultBag bag = bagView.getBag();
            fs.setCurrentDirectory(bag.getRootDir());
            if (bag.getName() != null && !bag.getName().equalsIgnoreCase(Context.getMessage("bag.label.noname"))) {
                String selectedName = bag.getName();
                if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                    selectedName += "."+DefaultBag.ZIP_LABEL;
                    fs.setFileFilter(bagView.getInfoFormsPane().getZipFilter());
                }
                else if (
                    bag.getSerialMode() == DefaultBag.TAR_MODE ||
                    bag.getSerialMode() == DefaultBag.TAR_GZ_MODE ||
                    bag.getSerialMode() == DefaultBag.TAR_BZ2_MODE
                ){
                    selectedName += "."+DefaultBag.TAR_LABEL;
                    fs.setFileFilter(bagView.getInfoFormsPane().getTarFilter());
                }
                else {
                    fs.setFileFilter(bagView.getInfoFormsPane().getNoFilter());
                }
                fs.setSelectedFile(new File(selectedName));
            } else {
                fs.setFileFilter(bagView.getInfoFormsPane().getNoFilter());
            }
            int	option = fs.showSaveDialog(frame);

            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fs.getSelectedFile();
                bagFile = file;
                bagFileName = bagFile.getAbsolutePath();
                String name = bagFileName; 
                bagView.getInfoFormsPane().setBagName(name);
                bagNameField.setText(bagFileName);
                bagNameField.setCaretPosition(bagFileName.length());
                bagNameField.invalidate();
            }
        }
    }

    private class OkSaveBagHandler extends AbstractAction {
	private static final long serialVersionUID = 1L;

        @Override
	public void actionPerformed(ActionEvent e) {
            BagView bagView = getBagView();
            if (bagNameField.getText().trim().isEmpty() || bagNameField.getText().equalsIgnoreCase(Context.getMessage("bag.label.noname"))) {
                SwingUtils.ShowError(
                    Context.getMessage("SaveBagDialog.filenamemissing.title"),
                    Context.getMessage("SaveBagDialog.filenamemissing.label")
                );
                return;
            }
            bagView.getInfoFormsPane().getHoleyValue().setText("false");           
           
            SaveBagDialog.this.dispose(); 
            
            bagView.getBag().setName(bagFileName);
            bagView.saveBagHandler.save(bagFile);           
        }
    }

    private class CancelSaveBagHandler extends AbstractAction {
	private static final long serialVersionUID = 1L;
        @Override
	public void actionPerformed(ActionEvent e) {
            SaveBagDialog.this.dispose();            
        }
    }

    private class TagManifestHandler extends AbstractAction {
    	private static final long serialVersionUID = 75893358194076314L;
        @Override
    	public void actionPerformed(ActionEvent e) {
            JCheckBox cb = (JCheckBox)e.getSource();
            // Determine status 
            getBagView().getBag().isBuildTagManifest(cb.isSelected());           
    	}
    }
    
    private class TagAlgorithmListHandler extends AbstractAction {
    	private static final long serialVersionUID = 75893358194076314L;
        @Override
    	public void actionPerformed(ActionEvent e) {
            JComboBox jlist = (JComboBox)e.getSource();
            String alg = (String) jlist.getSelectedItem();
            getBagView().getBag().setTagManifestAlgorithm(alg);
    	}
    }
    
    private class PayloadManifestHandler extends AbstractAction {
    	private static final long serialVersionUID = 75893358194076314L;
        @Override
    	public void actionPerformed(ActionEvent e) {
            JCheckBox cb = (JCheckBox)e.getSource();                
            // Determine status    		
            getBagView().getBag().isBuildPayloadManifest(cb.isSelected());           
    	}
    }

    private class PayAlgorithmListHandler extends AbstractAction {
    	private static final long serialVersionUID = 75893358194076314L;
        @Override
    	public void actionPerformed(ActionEvent e) {
            JComboBox jlist = (JComboBox)e.getSource();
            String alg = (String) jlist.getSelectedItem();
            getBagView().getBag().setPayloadManifestAlgorithm(alg);
    	}
    }
    
    private void buildConstraints(GridBagConstraints gbc,int x, int y, int w, int h, int wx, int wy, int fill, int anchor) {
    	gbc.gridx = x; // start cell in a row
    	gbc.gridy = y; // start cell in a column
    	gbc.gridwidth = w; // how many column does the control occupy in the row
    	gbc.gridheight = h; // how many column does the control occupy in the column
    	gbc.weightx = wx; // relative horizontal size
    	gbc.weighty = wy; // relative vertical size
    	gbc.fill = fill; // the way how the control fills cells
    	gbc.anchor = anchor; // alignment
    }    
}