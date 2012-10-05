package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.handlers.UpdateBagHandler;
import gov.loc.repository.bagger.ui.util.LayoutUtil;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import ugent.bagger.filters.FileExtensionFilter;

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
    private JLabel bagProfileValue;
    private JLabel holeyValue;
    private JLabel serializeLabel;
    private JLabel serializeValue;
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

    /*
     * Nicolas Franck: filters zijn null!! => waarom worden dan gebruikt?
     */
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
    public void setProfile(String profileName) {
    	bagProfileValue.setText(profileName);
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
            infoPanel = new JPanel(new GridBagLayout());
            infoPanel.setToolTipText(getBagView().getPropertyMessage("bagView.bagInfoInputPane.help"));
            Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
            infoPanel.setBorder(emptyBorder);
            GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
            infoPanel.add(getBagSettingsPanel(), gbc);
            gbc = LayoutUtil.buildGridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);
            infoPanel.add(getInfoInputPane(),gbc);            
        }
        return infoPanel;
    }
    public void setInfoPanel(JPanel infoPanel) {
        this.infoPanel = infoPanel;
    }  

    public InfoInputPane getInfoInputPane() {
        if(infoInputPane == null){
            infoInputPane = new InfoInputPane(false);
            infoInputPane.setToolTipText(getBagView().getPropertyMessage("bagView.bagInfoInputPane.help"));
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
        }
        return bagVersionValue;
    }
    public void setBagVersionValue(JLabel bagVersionValue) {
        this.bagVersionValue = bagVersionValue;
    }

    public JLabel getSerializeLabel() {
        if(serializeLabel == null){
            serializeLabel = new JLabel(getBagView().getPropertyMessage("bag.label.ispackage"));
            serializeLabel.setToolTipText(getBagView().getPropertyMessage("bag.serializetype.help"));
        }
        return serializeLabel;
    }

    public void setSerializeLabel(JLabel serializeLabel) {
        this.serializeLabel = serializeLabel;
    }    

    public JLabel getBagProfileValue() {
        if(bagProfileValue == null){
            bagProfileValue = new JLabel("");
        }
        return bagProfileValue;
    }

    public void setBagProfileValue(JLabel bagProfileValue) {
        this.bagProfileValue = bagProfileValue;
    }

    public JLabel getBagNameValue() {
        if(bagNameValue == null){
            bagNameValue = new JLabel(getBagView().getPropertyMessage("bag.label.noname"));
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
        JLabel lblBagName = new JLabel(getBagView().getPropertyMessage("bag.label.name"));
        GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(lblBagName, gbc);
        
        gbc = LayoutUtil.buildGridBagConstraints(1, row, 3, 1, 3, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getBagNameValue(), gbc);

        // bag profile
        row++;
        JLabel bagProfileLabel = new JLabel("Profile:");
        gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(bagProfileLabel, gbc);
        
        gbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getBagProfileValue(), gbc);

        // bag version
        JLabel bagVersionLabel = new JLabel(getBagView().getPropertyMessage("bag.label.version"));
    	bagVersionLabel.setToolTipText(getBagView().getPropertyMessage("bag.versionlist.help"));
    	gbc = LayoutUtil.buildGridBagConstraints(2, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(bagVersionLabel, gbc);
        
    	gbc = LayoutUtil.buildGridBagConstraints(3, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    	gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getBagVersionValue(), gbc);

        // is Holey bag?
        row++;
        JLabel holeyLabel = new JLabel(getBagView().getPropertyMessage("bag.label.isholey"));
        holeyLabel.setToolTipText(getBagView().getPropertyMessage("bag.isholey.help"));
    	gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(holeyLabel, gbc);
       
        gbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getHoleyValue(),gbc);
		
        // is packed?                
    	gbc = LayoutUtil.buildGridBagConstraints(2, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(getSerializeLabel(), gbc);
        
        gbc = LayoutUtil.buildGridBagConstraints(3, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
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
    	getInfoInputPane().populateForms(false);
    	getInfoInputPane().enableForms(false);
    	getInfoInputPane().invalidate();
    }

    public void updateInfoFormsPane(boolean enabled) {
        
        //Nicolas Franck: plaatst nu enkel baginfo-form opnieuw
        for(int i = 0;i< getInfoInputPane().getComponentCount();i++){                      
            if(getInfoInputPane().getComponentAt(i) == getInfoInputPane().getBagInfoForm().getControl()){
                //ervoor zorgen dat get-methode 'getBagInfoForm' zichzelf opnieuw initialiseert!
                getInfoInputPane().setBagInfoForm(null);
                getInfoInputPane().setComponentAt(i,getInfoInputPane().getBagInfoForm().getControl());
                break;
            }
        }        
        getInfoInputPane().validate();        
        validate();
        
        //Nicolas Franck: verwijdert volledige inputpane, inclusief mets-tab
    	// need to remove something?
        /*
        getInfoPanel().remove(getInfoInputPane());
        getInfoPanel().validate();      
    	setInfoInputPane(new InfoInputPane(enabled));
    	getInfoInputPane().setToolTipText(getBagView().getPropertyMessage("bagView.bagInfoInputPane.help"));
        
    	GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);
        getInfoPanel().add(getInfoInputPane(),gbc);
        validate();
        */
        
    }     
}