package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.handlers.UpdateBagHandler;
import gov.loc.repository.bagger.ui.util.LayoutUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.helper.Context;

public final class InfoFormsPane extends JPanel {
    private static final long serialVersionUID = -5988111446773491301L;   
    private JPanel bagSettingsPanel;
    private JPanel infoPanel;
    protected JPanel serializeGroupPanel;
    private InfoInputPane infoInputPane;
    private UpdateBagHandler updateBagHandler;
    protected JLabel bagNameValue;
    private JButton removeProjectButton;
    private JLabel bagVersionValue;    
    private JLabel holeyValue;
    private JLabel serializeLabel;
    private JLabel serializeValue;
    private JLabel saveLabel;
    protected JComboBox bagVersionList;
    private JCheckBox defaultProject;
    private JRadioButton noneButton;
    private JRadioButton zipButton;
    private JRadioButton tarButton;
    private JRadioButton tarGzButton;
    private JRadioButton tarBz2Button;
    private FileFilter noFilter;
    private FileFilter zipFilter;
    private FileFilter tarFilter;
   
    public FileFilter getNoFilter(){
        return noFilter;
    }
    public void setNoFilter(FileFilter noFilter) {
        this.noFilter = noFilter;
    }
    public FileFilter getZipFilter(){
        if(zipFilter == null){
            zipFilter = new FileExtensionFilter(new String [] {"zip"},"zip",true);
        }
        return zipFilter;
    }
    public void setZipFilter(FileFilter zipFilter) {
        this.zipFilter = zipFilter;
    }
    public FileFilter getTarFilter() {
        if(tarFilter == null){
            tarFilter = new FileExtensionFilter(new String [] {"tar","tar.gz"},"tar(.gz)",true);
        }
        return tarFilter;
    }
    public void setTarFilter(FileFilter tarFilter) {
        this.tarFilter = tarFilter;
    }
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
            serializeValue.setVisible(false);
        }
        return serializeValue;
    }

    public void setSerializeValue(JLabel serializeValue) {
        this.serializeValue = serializeValue;
    }    

    public JPanel getInfoPanel() {
        if(infoPanel == null){
            infoPanel = new JPanel(new GridBagLayout());
            infoPanel.setToolTipText(Context.getMessage("bagView.bagInfoInputPane.help"));
            Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
            infoPanel.setBorder(emptyBorder);

            /*
            GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);                       
            infoPanel.add(getBagSettingsPanel(), gbc);
            
            JLabel label = new JLabel(Context.getMessage("bagView.infoInputPane.name"));            
            gbc = LayoutUtil.buildGridBagConstraints(0,1, 1, 1,0, 0,GridBagConstraints.BOTH, GridBagConstraints.WEST);
            infoPanel.add(label,gbc);
            
            gbc = LayoutUtil.buildGridBagConstraints(0,2, 1, 1, 1, 1,GridBagConstraints.BOTH, GridBagConstraints.WEST);
            infoPanel.add(getInfoInputPane(),gbc);*/
            
            
            GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);                                               
            
            
            JLabel labelInfoInputPane = new JLabel(Context.getMessage("bagView.infoInputPane.name"));            
            gbc = LayoutUtil.buildGridBagConstraints(0,0,1,1,0, 0,GridBagConstraints.BOTH, GridBagConstraints.WEST);
            infoPanel.add(labelInfoInputPane,gbc);
            
            gbc = LayoutUtil.buildGridBagConstraints(0,1, 1, 1, 1, 1,GridBagConstraints.BOTH, GridBagConstraints.WEST);
            infoPanel.add(getInfoInputPane(),gbc);
            
            gbc = LayoutUtil.buildGridBagConstraints(0,2,1, 1, 1, 1,GridBagConstraints.BOTH, GridBagConstraints.WEST);
            
            
            JPanel saveBagPanel = new JPanel(new GridLayout(1,0));
            JLabel labelSaveBagPanel = new JLabel(Context.getMessage("bagView.saveBagPanel.name"));            
            saveBagPanel.add(labelSaveBagPanel);
            saveBagPanel.add(getSaveLabel());
            
            infoPanel.add(getBagSettingsPanel(), gbc);
        }
        return infoPanel;
    }
    public JLabel getSaveLabel() {
        if(saveLabel == null){            
            saveLabel = new JLabel(Context.getMessage("bagView.renameLabel.label"));            
            saveLabel.setEnabled(false);
            saveLabel.setHorizontalAlignment(SwingConstants.CENTER);            
            saveLabel.setIcon(BagView.getInstance().getPropertyImage("bagView.renameLabel.icon"));
            saveLabel.setToolTipText("Opslaan!");
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
    
    private JPanel createSettingsPanel() {
    	JPanel contentPane = new JPanel(new BorderLayout(5, 5));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.add(createBagSettingsPanel(),BorderLayout.CENTER);		
        contentPane.add(mainPanel, BorderLayout.NORTH);                        
    	return contentPane;
    }
    private JPanel createBagSettingsPanel() {
        JPanel pane = new JPanel(new GridBagLayout());        

        // bag name
        int row = 0;
        JLabel lblBagName = new JLabel(Context.getMessage("bag.label.name"));
        GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(lblBagName, gbc);
        
        gbc = LayoutUtil.buildGridBagConstraints(1, row, 3, 1, 3, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getBagNameValue(), gbc);


        // bag version
        row++;
        JLabel bagVersionLabel = new JLabel(Context.getMessage("bag.label.version"));
        bagVersionLabel.setVisible(false);
    	bagVersionLabel.setToolTipText(Context.getMessage("bag.versionlist.help"));
    	gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(bagVersionLabel, gbc);
        
    	gbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    	gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getBagVersionValue(), gbc);

        // is Holey bag?
        row++;
        JLabel holeyLabel = new JLabel(Context.getMessage("bag.label.isholey"));
        holeyLabel.setVisible(false);
        holeyLabel.setToolTipText(Context.getMessage("bag.isholey.help"));
    	gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(holeyLabel, gbc);
       
        gbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getHoleyValue(),gbc);
		
        // is packed?  
        row++;
    	gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getSerializeLabel(), gbc);
        
        gbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
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