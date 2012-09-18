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
package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagit.Manifest.Algorithm;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public final class SaveBagFrame extends JFrame implements ActionListener {
    private static final Log log = LogFactory.getLog(SaveBagFrame.class);
    private static final long serialVersionUID = 1L;
   
    File bagFile;
    String bagFileName = "";
    private Dimension preferredDimension = new Dimension(600, 400);
    JPanel savePanel;
    JPanel serializeGroupPanel;
    JTextField bagNameField;
    
    //Nicolas Franck: disable make holey
    //JLabel urlLabel;
    //JTextField urlField;
    
    JButton browseButton;
    JButton okButton;
    JButton cancelButton;
    JRadioButton noneButton;
    JRadioButton zipButton;
    JRadioButton tarButton;
    JRadioButton tarGzButton;
    JRadioButton tarBz2Button;

    //Nicolas Franck: disable make holey
    //JCheckBox holeyCheckbox;
    JCheckBox isTagCheckbox;
    JCheckBox isPayloadCheckbox;
    JComboBox tagAlgorithmList;
    JComboBox payAlgorithmList;

    public BagView getBagView(){
        return BagView.getInstance();
    }
    public SaveBagFrame(String title) {
        super(title);        
        if (getBagView() != null) {
            getContentPane().removeAll();
            savePanel = createComponents();
        }else{
            savePanel = new JPanel();
        }
        getContentPane().add(savePanel, BorderLayout.CENTER);
        setPreferredSize(preferredDimension);
        setBounds(300,200, 600, 400);

        //Nicolas Franck: elementen toevoegen na 'pack' zorgt voor XError!
        //pack();
        //Nicolas Franck
    }

    protected JComponent createButtonBar() {
        CommandGroup dialogCommandGroup = CommandGroup.createCommandGroup(null, getCommandGroupMembers());
        JComponent buttonBar = dialogCommandGroup.createButtonBar();
        GuiStandardUtils.attachDialogBorder(buttonBar);
        return buttonBar;
    }


    protected Object[] getCommandGroupMembers() {
        return new AbstractCommand[] { finishCommand, cancelCommand };
    }
	
	
    /**
    * Initialize the standard commands needed on a Dialog: Ok/Cancel.
    */
    private void initStandardCommands() {
        finishCommand = new ActionCommand(getFinishCommandId()) {
            @Override
            public void doExecuteCommand(){
                new OkSaveBagHandler().actionPerformed(null);
            }
        };

        cancelCommand = new ActionCommand(getCancelCommandId()){
            @Override
            public void doExecuteCommand(){
                new CancelSaveBagHandler().actionPerformed(null);
            }
        };
    }
    
    protected String getFinishCommandId() {
        return DEFAULT_FINISH_COMMAND_ID;
    }
    protected String getCancelCommandId() {
        return DEFAULT_CANCEL_COMMAND_ID;
    }
    protected static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";

    protected static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";

    private ActionCommand finishCommand;

    private ActionCommand cancelCommand;
	
	

    private JPanel createComponents() {
        Border border = new EmptyBorder(5, 5, 5, 5);
        
        TitlePane titlePane = new TitlePane();
        initStandardCommands();
        JPanel pageControl = new JPanel(new BorderLayout());
        JPanel titlePaneContainer = new JPanel(new BorderLayout());
        titlePane.setTitle(getBagView().getPropertyMessage("SaveBagFrame.title"));
        titlePane.setMessage( new DefaultMessage(getBagView().getPropertyMessage("Define the Bag settings")));
        titlePaneContainer.add(titlePane.getControl());
        titlePaneContainer.add(new JSeparator(), BorderLayout.SOUTH);
        pageControl.add(titlePaneContainer, BorderLayout.NORTH);
        JPanel contentPane = new JPanel();

        // TODO: Add bag name field
    	// TODO: Add save name file selection button
        JLabel location = new JLabel("Save in:");
    	browseButton = new JButton(getMessage("bag.button.browse"));
    	browseButton.addActionListener(new SaveBagAsHandler());
        browseButton.setEnabled(true);
        browseButton.setToolTipText(getMessage("bag.button.browse.help"));
    	String fileName = "";
    	DefaultBag bag = getBagView().getBag();
    	if (bag != null) {
            fileName = bag.getName();
    	}
    	bagNameField = new JTextField(fileName);
        bagNameField.setCaretPosition(fileName.length());
        bagNameField.setEditable(false);
        bagNameField.setEnabled(false);

        // Holey bag control
        /*
         * Nicolas Franck: disable make holey
         */
        /*
        JLabel holeyLabel = new JLabel(bagView.getPropertyMessage("bag.label.isholey"));
        holeyLabel.setToolTipText(bagView.getPropertyMessage("bag.isholey.help"));
        holeyCheckbox = new JCheckBox(bagView.getPropertyMessage("bag.checkbox.isholey"));
        holeyCheckbox.setBorder(border);
        holeyCheckbox.setSelected(bag.isHoley());
        holeyCheckbox.addActionListener(new HoleyBagHandler());
        holeyCheckbox.setToolTipText(bagView.getPropertyMessage("bag.isholey.help"));

        urlLabel = new JLabel(bagView.getPropertyMessage("baseURL.label"));
        urlLabel.setToolTipText(bagView.getPropertyMessage("baseURL.description"));
        urlLabel.setEnabled(bag.isHoley());
    	urlField = new JTextField("");
    	try {
            urlField.setText(bag.getFetch().getBaseURL());
    	} catch (Exception e) {
            log.error("fetch baseURL: " + e.getMessage());
    	}
        urlField.setEnabled(false);
        * 
        */
        
        // TODO: Add format label
    	JLabel serializeLabel;
        serializeLabel = new JLabel(getMessage("bag.label.ispackage"));
        serializeLabel.setToolTipText(getMessage("bag.serializetype.help"));

    	// TODO: Add format selection panel
        noneButton = new JRadioButton(getMessage("bag.serializetype.none"));
        noneButton.setEnabled(true);
        AbstractAction serializeListener = new SerializeBagHandler();
        noneButton.addActionListener(serializeListener);
        noneButton.setToolTipText(getMessage("bag.serializetype.none.help"));

        zipButton = new JRadioButton(getMessage("bag.serializetype.zip"));
        zipButton.setEnabled(true);
        zipButton.addActionListener(serializeListener);
        zipButton.setToolTipText(getMessage("bag.serializetype.zip.help"));

        tarButton = new JRadioButton(getMessage("bag.serializetype.tar"));
        tarButton.setEnabled(true);
        tarButton.addActionListener(serializeListener);
        tarButton.setToolTipText(getMessage("bag.serializetype.tar.help"));
        
        tarGzButton = new JRadioButton(getMessage("bag.serializetype.targz"));
        tarGzButton.setEnabled(true);
        tarGzButton.addActionListener(serializeListener);
        tarGzButton.setToolTipText(getMessage("bag.serializetype.targz.help"));
        
        tarBz2Button = new JRadioButton(getMessage("bag.serializetype.tarbz2"));
        tarBz2Button.setEnabled(true);
        tarBz2Button.addActionListener(serializeListener);
        tarBz2Button.setToolTipText(getMessage("bag.serializetype.tarbz2.help"));

        short mode = bag.getSerialMode();
    	if (mode == DefaultBag.NO_MODE) {
            this.noneButton.setEnabled(true);
    	} else if (mode == DefaultBag.ZIP_MODE) {
            this.zipButton.setEnabled(true);
    	} else if (mode == DefaultBag.TAR_MODE) {
            this.tarButton.setEnabled(true);
    	} else if (mode == DefaultBag.TAR_GZ_MODE) {
            this.tarGzButton.setEnabled(true);
    	} else if (mode == DefaultBag.TAR_BZ2_MODE) {
            this.tarBz2Button.setEnabled(true);
    	} else {
            this.noneButton.setEnabled(true);
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
        serializeGroupPanel.setToolTipText(getBagView().getPropertyMessage("bag.serializetype.help"));

        JLabel tagLabel = new JLabel(getMessage("bag.label.istag"));
        tagLabel.setToolTipText(getMessage("bag.label.istag.help"));
        isTagCheckbox = new JCheckBox();
        isTagCheckbox.setBorder(border);
        isTagCheckbox.setSelected(bag.isBuildTagManifest());
        isTagCheckbox.addActionListener(new TagManifestHandler());
        isTagCheckbox.setToolTipText(getMessage("bag.checkbox.istag.help"));

        JLabel tagAlgorithmLabel = new JLabel(getMessage("bag.label.tagalgorithm"));
        tagAlgorithmLabel.setToolTipText(getMessage("bag.label.tagalgorithm.help"));
        ArrayList<String> listModel = new ArrayList<String>();
		for(Algorithm algorithm : Algorithm.values()) {
			listModel.add(algorithm.bagItAlgorithm);
		}
        tagAlgorithmList = new JComboBox(listModel.toArray());
        tagAlgorithmList.setName(getMessage("bag.tagalgorithmlist"));
        tagAlgorithmList.setSelectedItem(bag.getTagManifestAlgorithm());
        tagAlgorithmList.addActionListener(new TagAlgorithmListHandler());
        tagAlgorithmList.setToolTipText(getMessage("bag.tagalgorithmlist.help"));
    	
        JLabel payloadLabel = new JLabel(getMessage("bag.label.ispayload"));
        payloadLabel.setToolTipText(getMessage("bag.ispayload.help"));
        isPayloadCheckbox = new JCheckBox();
        isPayloadCheckbox.setBorder(border);
        isPayloadCheckbox.setSelected(bag.isBuildPayloadManifest());
        isPayloadCheckbox.addActionListener(new PayloadManifestHandler());
        isPayloadCheckbox.setToolTipText(getMessage("bag.ispayload.help"));

        JLabel payAlgorithmLabel = new JLabel(getBagView().getPropertyMessage("bag.label.payalgorithm"));
        payAlgorithmLabel.setToolTipText(getMessage("bag.payalgorithm.help"));
        payAlgorithmList = new JComboBox(listModel.toArray());
        payAlgorithmList.setName(getMessage("bag.payalgorithmlist"));
        payAlgorithmList.setSelectedItem(bag.getPayloadManifestAlgorithm());
        payAlgorithmList.addActionListener(new PayAlgorithmListHandler());
        payAlgorithmList.setToolTipText(getMessage("bag.payalgorithmlist.help"));
        
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
        
        /*
         * Nicolas Franck:disable make holey
         */
        /*
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(holeyLabel, glbc);
        panel.add(holeyLabel);
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.WEST, GridBagConstraints.WEST); 
        layout.setConstraints(holeyCheckbox, glbc);
        panel.add(holeyCheckbox);
        
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(urlLabel, glbc);
        panel.add(urlLabel);
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(urlField, glbc);
        panel.add(urlField);
        */
        
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(serializeLabel, glbc);
    	panel.add(serializeLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); 
        layout.setConstraints(serializeGroupPanel, glbc);
    	panel.add(serializeGroupPanel);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(tagLabel, glbc);
    	panel.add(tagLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(isTagCheckbox, glbc);
    	panel.add(isTagCheckbox);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(tagAlgorithmLabel, glbc);
    	panel.add(tagAlgorithmLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(tagAlgorithmList, glbc);
    	panel.add(tagAlgorithmList);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(payloadLabel, glbc);
    	panel.add(payloadLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(isPayloadCheckbox, glbc);
    	panel.add(isPayloadCheckbox);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        layout.setConstraints(payAlgorithmLabel, glbc);
    	panel.add(payAlgorithmLabel);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
        layout.setConstraints(payAlgorithmList, glbc);
    	panel.add(payAlgorithmList);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST); 
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER); 
    	
    	GuiStandardUtils.attachDialogBorder(contentPane);
        pageControl.add(panel);
        JComponent buttonBar = createButtonBar();
        pageControl.add(buttonBar,BorderLayout.SOUTH);
        //Nicolas Franck: elementen toevoegen na 'pack' zorgt voor XError!
        //this.pack();
        //Nicolas Franck
        return pageControl; 	
 
    }
    
    public void setBag(DefaultBag bag) {
        BagView bagView = getBagView();
    	bagNameField.setText(bag.getName());
    	short mode = bag.getSerialMode();
    	if (mode == DefaultBag.NO_MODE) {
            noneButton.setEnabled(true);
            noneButton.setSelected(true);
            bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.NO_LABEL);
    	} else if (mode == DefaultBag.ZIP_MODE) {
            zipButton.setEnabled(true);
            zipButton.setSelected(true);
            bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.ZIP_LABEL);
    	} else if (mode == DefaultBag.TAR_MODE) {
            tarButton.setEnabled(true);
            tarButton.setSelected(true);
            bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.TAR_LABEL);
    	} else if (mode == DefaultBag.TAR_GZ_MODE) {
            tarGzButton.setEnabled(true);
            tarGzButton.setSelected(true);
            bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.TAR_GZ_LABEL);
    	} else if (mode == DefaultBag.TAR_BZ2_MODE) {
            tarBz2Button.setEnabled(true);
            tarBz2Button.setSelected(true);
            bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.TAR_BZ2_LABEL);
    	} else {
            noneButton.setEnabled(true);
            noneButton.setSelected(true);
            bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.NO_LABEL);
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
                    bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.NO_LABEL);
            	} else if (cb == zipButton) {
                    bag.isSerial(true);
                    bag.setSerialMode(DefaultBag.ZIP_MODE);
                    bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.ZIP_LABEL);
            	} else if (cb == tarButton) {
                    bag.isSerial(true);
                    bag.setSerialMode(DefaultBag.TAR_MODE);
                    bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.TAR_LABEL);
            	} else if (cb == tarGzButton) {
                    bag.isSerial(true);
                    bag.setSerialMode(DefaultBag.TAR_GZ_MODE);
                    bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.TAR_GZ_LABEL);
            	} else if (cb == tarBz2Button) {
                    bag.isSerial(true);
                    bag.setSerialMode(DefaultBag.TAR_BZ2_MODE);
                    bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.TAR_BZ2_LABEL);
            	} else {
                    bag.isSerial(false);
                    bag.setSerialMode(DefaultBag.NO_MODE);
                    bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.NO_LABEL);
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
            fs.addChoosableFileFilter(bagView.getInfoInputPane().getNoFilter());
            fs.addChoosableFileFilter(bagView.getInfoInputPane().getZipFilter());
            fs.addChoosableFileFilter(bagView.getInfoInputPane().getTarFilter());
            fs.setDialogTitle("Save Bag As");
            DefaultBag bag = bagView.getBag();
            fs.setCurrentDirectory(bag.getRootDir());
            if (bag.getName() != null && !bag.getName().equalsIgnoreCase(bagView.getPropertyMessage("bag.label.noname"))) {
                String selectedName = bag.getName();
                if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                    selectedName += "."+DefaultBag.ZIP_LABEL;
                    fs.setFileFilter(bagView.getInfoInputPane().getZipFilter());
                }
                else if (
                    bag.getSerialMode() == DefaultBag.TAR_MODE ||
                    bag.getSerialMode() == DefaultBag.TAR_GZ_MODE ||
                    bag.getSerialMode() == DefaultBag.TAR_BZ2_MODE
                ){
                    selectedName += "."+DefaultBag.TAR_LABEL;
                    fs.setFileFilter(bagView.getInfoInputPane().getTarFilter());
                }
                else {
                    fs.setFileFilter(bagView.getInfoInputPane().getNoFilter());
                }
                fs.setSelectedFile(new File(selectedName));
            } else {
                fs.setFileFilter(bagView.getInfoInputPane().getNoFilter());
            }
            int	option = fs.showSaveDialog(frame);

            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fs.getSelectedFile();
                bagFile = file;
                bagFileName = bagFile.getAbsolutePath();
                String name = bagFileName; 
                bagView.infoInputPane.setBagName(name);
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
            if (bagNameField.getText().trim().isEmpty() || bagNameField.getText().equalsIgnoreCase(bagView.getPropertyMessage("bag.label.noname"))) {
                bagView.showWarningErrorDialog("Error - bag not saved", "The bag must have a file name.");
                return;
            }
            bagView.getInfoInputPane().getHoleyValue().setText("false");
            /*
             * Nicolas Franck: disable make holey
             */
            /*
            if (bagView.getBag().isHoley()) {
                if (urlField.getText().trim().isEmpty()) {
                    bagView.showWarningErrorDialog("Error - bag not saved", "A holey bag must have a URL value.");
                    return;
                } else {
                    bagView.getBag().getFetch().setBaseURL(urlField.getText().trim());
                }
                bagView.infoInputPane.holeyValue.setText("true");
            } else {
                bagView.infoInputPane.holeyValue.setText("false");
            }
            * 
            */
            //bagView.saveBagHandler.setValidateOnSave(bagView.getBag().isValidateOnSave());
            setVisible(false);
            bagView.getBag().setName(bagFileName);
            bagView.saveBagHandler.save(bagFile);           
        }
    }

    private class CancelSaveBagHandler extends AbstractAction {
	private static final long serialVersionUID = 1L;
        @Override
	public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }

    private class TagManifestHandler extends AbstractAction {
    	private static final long serialVersionUID = 75893358194076314L;
        @Override
    	public void actionPerformed(ActionEvent e) {
            JCheckBox cb = (JCheckBox)e.getSource();
            // Determine status 
            getBagView().getBag().isBuildTagManifest(cb.isSelected());
            //Nicolas Franck
            /*
            boolean isSelected = cb.isSelected();
            if (isSelected) {
                    bagView.getBag().isBuildTagManifest(true);
            } else {
                    bagView.getBag().isBuildTagManifest(false);
            }*/
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
            //Nicolas Franck
            /*
            boolean isSelected = cb.isSelected();
            if (isSelected) {
                    bagView.getBag().isBuildPayloadManifest(true);
            } else {
                    bagView.getBag().isBuildPayloadManifest(false);
            }*/
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
    
    //Nicolas Franck: disable make holey
    /*
    private class HoleyBagHandler extends AbstractAction {
       	private static final long serialVersionUID = 1L;

        @Override
    	public void actionPerformed(ActionEvent e) {
            JCheckBox cb = (JCheckBox)e.getSource();

            // Determine status
            boolean isSelected = cb.isSelected();
            if (isSelected) {
                bagView.getBag().isHoley(true);
                bagView.infoInputPane.serializeValue.setText("true");
                urlLabel.setEnabled(true);
                urlField.setEnabled(true);
                urlField.requestFocus();
            } else {
                bagView.getBag().isHoley(false);
                bagView.infoInputPane.serializeValue.setText("false");
                urlLabel.setEnabled(false);
                urlField.setEnabled(false);
            }
       	}
    }
    * 
    */
    
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
    
    private String getMessage(String property) {
    	return getBagView().getPropertyMessage(property);
    }
}