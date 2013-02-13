package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.handlers.UpdateBagHandler;
import gov.loc.repository.bagger.ui.util.LayoutUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.LineBorder;
import ugent.bagger.helper.Context;

public final class InfoFormsPane extends JPanel {
    
    JPanel bagSettingsPanel;
    JPanel infoPanel;
    protected JPanel serializeGroupPanel;
    InfoInputPane infoInputPane;
    UpdateBagHandler updateBagHandler;
    protected JLabel bagNameValue;    
    JLabel bagVersionValue;    
    JLabel holeyValue;
    JLabel serializeLabel;
    JLabel serializeValue;
    JLabel saveLabel;
    protected JComboBox bagVersionList;   
    
    
    public JLabel getHoleyValue() {
        if(holeyValue == null){
            holeyValue = new JLabel("");
            holeyValue.setVisible(false);
        }
        return holeyValue;
    }
    public void setHoleyValue(JLabel holeyValue) {
        this.holeyValue = holeyValue;
    }    
    public BagView getBagView(){
        return BagView.getInstance();
    }
    public DefaultBag getBag(){
        return getBagView().getBag();
    }
    public UpdateBagHandler getUpdateBagHandler() {
        if(updateBagHandler == null){
            updateBagHandler = new UpdateBagHandler();
        }
        return updateBagHandler;
    }
    public void setUpdateBagHandler(UpdateBagHandler updateBagHandler) {
        this.updateBagHandler = updateBagHandler;
    }    
    public InfoFormsPane() {
    	super();       
        setLayout(new BorderLayout());
        add(getInfoPanel());       
    }        

    public JLabel getSerializeValue() {
        if(serializeValue == null){
            serializeValue = new JLabel("");                        
        }
        return serializeValue;
    }

    public void setSerializeValue(JLabel serializeValue) {
        this.serializeValue = serializeValue;
    }    

    public JPanel getInfoPanel() {
        if(infoPanel == null){
            
            infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
            
            JLabel labelInfoInputPane = new JLabel(Context.getMessage("bagView.infoInputPane.name"));                                   
            labelInfoInputPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            labelInfoInputPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            infoPanel.add(labelInfoInputPane);            
            
            getInfoInputPane().setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(getInfoInputPane());            
           
            JPanel saveBagPanel = new JPanel(new GridLayout(1,0));
            JLabel labelSaveBagPanel = new JLabel(Context.getMessage("bagView.saveBagPanel.name"));                                    
            saveBagPanel.add(labelSaveBagPanel);
            JPanel buttonsSaveBagPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonsSaveBagPanel.add(getSaveLabel());
            saveBagPanel.add(buttonsSaveBagPanel);   
            saveBagPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            saveBagPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            infoPanel.add(saveBagPanel);
            
            getBagSettingsPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
            getBagSettingsPanel().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            infoPanel.add(getBagSettingsPanel());
        }
        return infoPanel;
    }
    public JLabel getSaveLabel() {
        if(saveLabel == null){            
            saveLabel = new JLabel(Context.getMessage("saveBag.label"));            
            saveLabel.setEnabled(false);
            saveLabel.setHorizontalAlignment(SwingConstants.CENTER);            
            saveLabel.setIcon(getBagView().getPropertyImage("bag.save.thumbnail.icon"));
            saveLabel.setToolTipText(Context.getMessage("saveBag.label"));
            saveLabel.setBorder(new LineBorder(saveLabel.getBackground(),1));
            saveLabel.addMouseListener(new MouseAdapter(){			
                @Override
                public void mousePressed(MouseEvent e) {
                    if(saveLabel.isEnabled()){
                        if(BagView.getInstance().saveBagAsExecutor.isEnabled()){
                            BagView.getInstance().saveBagAsExecutor.execute();
                        }                        
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    saveLabel.setBorder(new LineBorder(saveLabel.getBackground(),1));
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(saveLabel.isEnabled()){
                        saveLabel.setBorder(new LineBorder(Color.GRAY,1));
                    }                            
                }
            });     
        }
        return saveLabel;
    }
    public void setInfoPanel(JPanel infoPanel) {
        this.infoPanel = infoPanel;
    }  

    public InfoInputPane getInfoInputPane() {
        if(infoInputPane == null){
            infoInputPane = new InfoInputPane();
            infoInputPane.setToolTipText(Context.getMessage("bagView.bagInfoInputPane.help"));
            infoInputPane.setEnabled(false);
        }
        return infoInputPane;
    }
    public void setInfoInputPane(InfoInputPane bagInfoInputPane) {
        this.infoInputPane = bagInfoInputPane;
    }
    public JPanel getBagSettingsPanel() {
        if(bagSettingsPanel == null){
            bagSettingsPanel = createSettingsPanel();
        }
        return bagSettingsPanel;
    }
    public void setBagSettingsPanel(JPanel bagSettingsPanel) {
        this.bagSettingsPanel = bagSettingsPanel;
    }

    public JLabel getBagVersionValue() {
        if(bagVersionValue == null){
            bagVersionValue = new JLabel("");
            bagVersionValue.setVisible(false);
        }
        return bagVersionValue;
    }
    public void setBagVersionValue(JLabel bagVersionValue) {
        this.bagVersionValue = bagVersionValue;
    }

    public JLabel getSerializeLabel() {
        if(serializeLabel == null){
            serializeLabel = new JLabel(Context.getMessage("bag.label.ispackage"));
            serializeLabel.setToolTipText(Context.getMessage("bag.serializetype.help"));
        }
        return serializeLabel;
    }

    public void setSerializeLabel(JLabel serializeLabel) {
        this.serializeLabel = serializeLabel;
    }    

    public JLabel getBagNameValue() {
        if(bagNameValue == null){
            bagNameValue = new JLabel(Context.getMessage("bag.label.noname"));
        }
        return bagNameValue;
    }

    public void setBagNameValue(JLabel bagNameValue) {
        this.bagNameValue = bagNameValue;
    }  
    
    JPanel createSettingsPanel() {    
        JPanel panel = new JPanel(new BorderLayout());        
        panel.add(createBagSettingsPanel(),BorderLayout.NORTH);
        return panel;
    }
    JPanel createBagSettingsPanel() {
        JPanel pane = new JPanel(new GridBagLayout());        

        // bag name
        int row = 0;
        JLabel lblBagName = new JLabel(Context.getMessage("bag.label.name"));
        GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE,GridBagConstraints.FIRST_LINE_START);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(lblBagName, gbc);        
        gbc = LayoutUtil.buildGridBagConstraints(1, row, 3, 1, 3, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.FIRST_LINE_START);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getBagNameValue(), gbc);


        // bag version
        /*row++;
        JLabel bagVersionLabel = new JLabel(Context.getMessage("bag.label.version"));
        bagVersionLabel.setVisible(false);
    	bagVersionLabel.setToolTipText(Context.getMessage("bag.versionlist.help"));
    	gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(bagVersionLabel, gbc);        
    	gbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    	gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getBagVersionValue(), gbc);*/

        // is Holey bag?
        /*row++;
        JLabel holeyLabel = new JLabel(Context.getMessage("bag.label.isholey"));
        holeyLabel.setVisible(false);
        holeyLabel.setToolTipText(Context.getMessage("bag.isholey.help"));
    	gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(holeyLabel, gbc);
       
        gbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getHoleyValue(),gbc);*/
		
        // is packed?          
        row++;
    	gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.FIRST_LINE_START);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getSerializeLabel(), gbc);        
        gbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.FIRST_LINE_START);
    	gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getSerializeValue(), gbc);
               
        return pane;
    }
    public void setBagVersion(String value) {
    	getBagVersionValue().setText(value);
    }
    public String getBagVersion() {
    	return getBagVersionValue().getText();
    }
    public void setHoley(String value) {
    	getHoleyValue().setText(value);
    }
    public void setBagName(String name) {
    	if (name == null || name.length() < 1) {
            return;
        }
    	getBagNameValue().setText(name);
    }    
    public String getBagName() {
    	return getBagNameValue().getText();
    }    
    public void updateInfoForms() {
    	getInfoInputPane().populateForms();
    	getInfoInputPane().enableForms(false);
    	getInfoInputPane().invalidate();
    }        
}