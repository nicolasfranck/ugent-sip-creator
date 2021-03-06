package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.handlers.*;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.util.Assert;
import ugent.bagger.helper.Context;
import ugent.bagger.views.DefaultView;

public final class BagView extends DefaultView {
    static final Log log = LogFactory.getLog(BagView.class);
    static BagView instance;
    
    MetsBag bag;    
    BagTree bagPayloadTree;
    InfoFormsPane infoFormsPane;
    BagTreePanel bagPayloadTreePanel;
    JPanel bagButtonPanel; 
    JLabel addDataToolBarAction;
    JLabel removeDataToolBarAction;        
    JComponent mainPanel;
    JComponent leftPanel;    
    JComponent renameComponent;
    JLabel renameLabel;
    
    public StartNewBagHandler startNewBagHandler = new StartNewBagHandler();
    public StartExecutor startExecutor = new StartExecutor();
    public OpenBagHandler openBagHandler = new OpenBagHandler();
    public OpenExecutor openExecutor = new OpenExecutor();
    public CreateBagsHandler createBagsHandler = new CreateBagsHandler();
    public CreateBagsExecutor createBagsExecutor = new CreateBagsExecutor();    
    public SaveBagHandler saveBagHandler = new SaveBagHandler();    
    public SaveBagExecutor saveBagExecutor = new SaveBagExecutor();
    public SaveBagAsHandler saveBagAsHandler = new SaveBagAsHandler();
    public SaveBagAsExecutor saveBagAsExecutor = new SaveBagAsExecutor();    
    public ValidateBagHandler validateBagHandler = new ValidateBagHandler();    
    public ValidateExecutor validateExecutor = new ValidateExecutor();    
    public CompleteBagHandler completeBagHandler = new CompleteBagHandler();
    public CompleteExecutor completeExecutor = new CompleteExecutor();    
    public ClearBagHandler clearBagHandler = new ClearBagHandler();
    public ClearBagExecutor clearExecutor = new ClearBagExecutor();
    public AddDataHandler addDataHandler = new AddDataHandler();    	
    public AddDataExecutor addDataExecutor = new AddDataExecutor();
    public RemoveDataHandler removeDataHandler = new RemoveDataHandler();
    public RenameExecutor renameExecutor = new RenameExecutor();
    public ExportExecutor exportExecutor = new ExportExecutor();
    public ValidateManifestExecutor validateManifestExecutor = new ValidateManifestExecutor();
    public ValidateBagsExecutor validateBagsExecutor = new ValidateBagsExecutor();    
    public OpenLogFileExecutor openLogFileExecutor = new OpenLogFileExecutor();
   
    public JLabel getRenameLabel() {
        if(renameLabel == null){            
            renameLabel = new JLabel(Context.getMessage("bagView.renameLabel.label"));            
            renameLabel.setEnabled(false);
            renameLabel.setHorizontalAlignment(SwingConstants.CENTER);            
            renameLabel.setIcon(getPropertyImage("bagView.renameLabel.icon"));
            renameLabel.setToolTipText(Context.getMessage("bagView.renameLabel.tooltip"));
            renameLabel.setBorder(new LineBorder(renameLabel.getBackground(),1));
            renameLabel.addMouseListener(new MouseAdapter(){			
                @Override
                public void mousePressed(MouseEvent e) {
                    if(renameLabel.isEnabled()){
                        renameExecutor.execute();
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    renameLabel.setBorder(new LineBorder(renameLabel.getBackground(),1));
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(renameLabel.isEnabled()){
                        renameLabel.setBorder(new LineBorder(Color.GRAY,1));
                    }                            
                }
            });             
        }        
        return renameLabel;
    }
    public JComponent getRenameComponent() {
        if(renameComponent == null){
            renameComponent = new JPanel(new GridLayout(1,0));            
            JLabel labelTitle = new JLabel(Context.getMessage("bagView.renameComponent.title"));            
            renameComponent.add(labelTitle);
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightPanel.add(getRenameLabel());
            renameComponent.add(rightPanel);            
            renameComponent.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        }
        return renameComponent;
    }
    public void setRenameComponent(JComponent renameComponent) {
        this.renameComponent = renameComponent;
    }
    public JComponent getLeftPanel() {
        if(leftPanel == null){
            leftPanel = createBagPanel();
        }
        return leftPanel;
    }    
    public JComponent getMainPanel(){        
        if(mainPanel == null){
            JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,getLeftPanel(),getInfoFormsPane());
            splitter.setDividerLocation(0.3);
            splitter.setResizeWeight(0.5);            
            mainPanel = new JScrollPane(splitter);             
        }
        return mainPanel;
    }       
    public BagView() {
        //verhinder meer dan één instantie
        Assert.isNull(instance);
        instance = this;
    }
    public JPanel getBagButtonPanel() {
        if(bagButtonPanel == null){
            bagButtonPanel = createBagButtonPanel();
        }
        return bagButtonPanel;
    }
    public void setBagButtonPanel(JPanel bagButtonPanel) {
        this.bagButtonPanel = bagButtonPanel;
    }
    public InfoFormsPane getInfoFormsPane() {
        if(infoFormsPane == null){
            infoFormsPane = new InfoFormsPane();
            infoFormsPane.getInfoInputPane().enableForms(false);
        }
        return infoFormsPane;
    }
    public void setInfoFormsPane(InfoFormsPane infoFormsPane) {
        this.infoFormsPane = infoFormsPane;
    }
    public void setBag(MetsBag bag) {
        this.bag = bag;
    }
    public MetsBag getBag() {
        if(bag == null){
            try{
                bag = new MetsBag();
            }catch(Exception e){
                log.error(e.getMessage());
            }            
        }
        return bag;
    }  
    public ImageIcon getPropertyImage(String name) {        
        return new ImageIcon(getImageSource().getImage(name));        
    }
    public void setBagPayloadTree(BagTree bagPayloadTree) {
        this.bagPayloadTree = bagPayloadTree;
    }
    public BagTree createBagPayloadTree(String path, boolean isPayload){
        final BagTree tree = new BagTree(path,isPayload);                
        ActionMap actionMap = tree.getActionMap();                        
        actionMap.put("removeData",new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                removeDataHandler.removeData();
            }               
        });
        InputMap inputMap = tree.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);                                        
        inputMap.put(KeyStroke.getKeyStroke((char)127),"removeData");            
       
        return tree;
    }
    public BagTree getBagPayloadTree() {
        if(bagPayloadTree == null){            
            bagPayloadTree = createBagPayloadTree(AbstractBagConstants.DATA_DIRECTORY, true);                       
        }
        return bagPayloadTree;
    }

    public BagTreePanel getBagPayloadTreePanel() {
        if(bagPayloadTreePanel == null){
            bagPayloadTreePanel = new BagTreePanel(getBagPayloadTree());
        }
        return bagPayloadTreePanel;
    }    
    public void setBagPayloadTreePanel(BagTreePanel bagPayloadTreePanel) {
        this.bagPayloadTreePanel = bagPayloadTreePanel;
    }
    // This populates the default view descriptor declared as the startingPageId
    // property in the richclient-application-context.xml file.
    @Override    
    protected JComponent createControl(){
    	initializeCommands();
        return getMainPanel();
    }    
    
    JComponent createBagPanel(){        
    	
    	LineBorder border = new LineBorder(Color.GRAY,1);

    	getBagPayloadTree().setEnabled(false);
        getBagPayloadTreePanel().setEnabled(false);    	
    	getBagPayloadTreePanel().setBorder(border);
    	getBagPayloadTreePanel().setToolTipText(getMessage("bagTree.help"));     
        
        JPanel bagPanel = new JPanel(new BorderLayout());
        
        bagPanel.add(getRenameComponent(),BorderLayout.NORTH);
        

        JPanel payloadPanel = new JPanel();        
        payloadPanel.setLayout(new BorderLayout(0, 0));

        JPanel payLoadToolBarPanel = new JPanel();        
        payloadPanel.add(payLoadToolBarPanel, BorderLayout.NORTH);
        payLoadToolBarPanel.setLayout(new GridLayout(1, 0, 0, 0));

        JPanel payloadLabelPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) payloadLabelPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        payLoadToolBarPanel.add(payloadLabelPanel);

        JLabel lblPayloadTree = new JLabel(getMessage("bagView.payloadTree.name"));
        payloadLabelPanel.add(lblPayloadTree);

        payLoadToolBarPanel.add(getBagButtonPanel());

        payloadPanel.add(getBagPayloadTreePanel(), BorderLayout.CENTER);        
        
        bagPanel.add(payloadPanel);
        
        return bagPanel;
    }
    
    JPanel createBagButtonPanel() {
    	    	
    	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
		
        addDataToolBarAction = new JLabel(Context.getMessage("bagView.payloadTree.addButton.label"));
        addDataToolBarAction.setEnabled(false);
        addDataToolBarAction.setHorizontalAlignment(SwingConstants.CENTER);
        addDataToolBarAction.setBorder(new LineBorder(addDataToolBarAction.getBackground(),1));
        addDataToolBarAction.setIcon(getPropertyImage("Bag_Content.add.icon"));
        addDataToolBarAction.setToolTipText(getMessage("bagView.payloadTree.addbutton.tooltip"));

        addDataToolBarAction.addMouseListener(new MouseAdapter(){			
            @Override
            public void mousePressed(MouseEvent e) {
                if(addDataToolBarAction.isEnabled()){
                    addDataHandler.actionPerformed(null);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                addDataToolBarAction.setBorder(new LineBorder(addDataToolBarAction.getBackground(),1));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if(addDataToolBarAction.isEnabled()){
                    addDataToolBarAction.setBorder(new LineBorder(Color.GRAY,1));
                }                            
            }
        });
        buttonPanel.add(addDataToolBarAction);

        removeDataToolBarAction = new JLabel(Context.getMessage("bagView.payloadTree.removeButton.label"));
        removeDataToolBarAction.setEnabled(false);
        removeDataToolBarAction.setHorizontalAlignment(SwingConstants.CENTER);
        removeDataToolBarAction.setBorder(new LineBorder(removeDataToolBarAction.getBackground(),1));
        removeDataToolBarAction.setIcon(getPropertyImage("Bag_Content.minus.icon"));
        removeDataToolBarAction.setToolTipText(getMessage("bagView.payloadTree.remove.tooltip"));
        buttonPanel.add(removeDataToolBarAction);
        removeDataToolBarAction.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if(removeDataToolBarAction.isEnabled()){
                    removeDataHandler.actionPerformed(null);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                removeDataToolBarAction.setBorder(new LineBorder(removeDataToolBarAction.getBackground(),1));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if(removeDataToolBarAction.isEnabled()){
                    removeDataToolBarAction.setBorder(new LineBorder(Color.GRAY,1));
                }
            }
        });
		
        final JLabel spacerLabel = new JLabel("    ");
        buttonPanel.add(spacerLabel);
	
        return buttonPanel;
    }    

    public void enableBagSettings(boolean b) {
    	getBagPayloadTree().setEnabled(b);
    	getBagPayloadTreePanel().setEnabled(b);    
        getInfoFormsPane().getInfoInputPane().setEnabled(b);
    }   
    void initializeCommands() {
    	startExecutor.setEnabled(true);
    	openExecutor.setEnabled(true);
        createBagsExecutor.setEnabled(true);        
    	clearExecutor.setEnabled(false);
        validateExecutor.setEnabled(false);
        validateBagHandler.setEnabled(false);
        completeExecutor.setEnabled(false);
        completeBagHandler.setEnabled(false);
        addDataExecutor.setEnabled(false);
        saveBagExecutor.setEnabled(false);
        saveBagAsExecutor.setEnabled(false);
        renameExecutor.setEnabled(true);
        getRenameLabel().setEnabled(true);
        validateBagsExecutor.setEnabled(true);
        validateManifestExecutor.setEnabled(true);
        openLogFileExecutor.setEnabled(true);
    }

    public void updateClearBag() {
    	enableBagSettings(false);    
        openExecutor.setEnabled(true);
    	getInfoFormsPane().getHoleyValue().setText("");
        getInfoFormsPane().getBagVersionValue().setText("");
        getInfoFormsPane().getBagNameValue().setText("");
        getInfoFormsPane().getSerializeValue().setText("");
        getInfoFormsPane().getSaveLabel().setEnabled(false);
    	addDataToolBarAction.setEnabled(false);
    	removeDataToolBarAction.setEnabled(false);
    	addDataExecutor.setEnabled(false);
    	saveBagExecutor.setEnabled(false);
    	saveBagAsExecutor.setEnabled(false);    	    	
    	clearExecutor.setEnabled(false);
    	validateExecutor.setEnabled(false);
        validateBagHandler.setEnabled(false);
    	completeExecutor.setEnabled(false);
        completeBagHandler.setEnabled(false);        
        getRenameLabel().setEnabled(true);
        renameExecutor.setEnabled(true);
    	getBagButtonPanel().invalidate();            
    }

    public void updateNewBag() {        
        enableBagSettings(true);
        openExecutor.setEnabled(false);
        clearExecutor.setEnabled(true);
        addDataToolBarAction.setEnabled(true);
        addDataExecutor.setEnabled(true);        
        exportExecutor.setEnabled(false);
        getInfoFormsPane().getSaveLabel().setEnabled(true);
        getRenameLabel().setEnabled(false);
        renameExecutor.setEnabled(false);        
        getBagButtonPanel().invalidate();        
        setCompleteExecutor();  // Disables the Is Complete Bag Button for Holey Bags  
        setValidateExecutor();  // Disables the Validate Bag Button for Holey Bags     
    }

    public void updateOpenBag() {
        addDataToolBarAction.setEnabled(true);  
        openExecutor.setEnabled(false);
        getInfoFormsPane().getSaveLabel().setEnabled(true);
        addDataExecutor.setEnabled(true);
        saveBagExecutor.setEnabled(getBag().getPayload().size() > 0 && getBag().getFile() != null && getBag().getFile().exists());     
        saveBagAsExecutor.setEnabled(getBag().getPayload().size() > 0);
        getBagButtonPanel().invalidate();
        clearExecutor.setEnabled(true);
        exportExecutor.setEnabled(true);
        getRenameLabel().setEnabled(false);   
        renameExecutor.setEnabled(false);
        setCompleteExecutor();  // Disables the Is Complete Bag Button for Holey Bags  
        setValidateExecutor();  // Disables the Validate Bag Button for Holey Bags                
    }  
    
    public void updateSaveBag() {        
        openExecutor.setEnabled(false);
        addDataToolBarAction.setEnabled(true);        
        addDataExecutor.setEnabled(true);
        saveBagExecutor.setEnabled(getBag().getPayload().size() > 0 && getBag().getFile() != null && getBag().getFile().exists());       
        saveBagAsExecutor.setEnabled(getBag().getPayload().size() > 0);
        getBagButtonPanel().invalidate();
        clearExecutor.setEnabled(true);
        exportExecutor.setEnabled(true);
        renameExecutor.setEnabled(false);
        getRenameLabel().setEnabled(false);
        setCompleteExecutor();  // Disables the Is Complete Bag Button for Holey Bags  
        setValidateExecutor();  // Disables the Validate Bag Button for Holey Bags     
    }
    
    public void updateAddData() {   
        openExecutor.setEnabled(false);
    	saveBagAsExecutor.setEnabled(getBag().getPayload().size() > 0);
        saveBagExecutor.setEnabled(getBag().getPayload().size() > 0 && getBag().getFile() != null && getBag().getFile().exists());
        exportExecutor.setEnabled(false);
        renameExecutor.setEnabled(false);        
        validateExecutor.setEnabled(false);
        validateBagHandler.setEnabled(false);
        completeExecutor.setEnabled(false);
        completeBagHandler.setEnabled(false);        
        getRenameLabel().setEnabled(false);
    	getBagButtonPanel().invalidate();    	
    }    

    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context) {
    	context.register("startCommand", startExecutor);
    	context.register("openCommand", openExecutor);
        context.register("createBagsCommand", createBagsExecutor);
    	context.register("clearCommand", clearExecutor);
    	context.register("validateCommand", validateExecutor);
    	context.register("completeCommand", completeExecutor);
    	context.register("addDataCommand", addDataExecutor);
    	context.register("saveBagCommand", saveBagExecutor);
    	context.register("saveBagAsCommand", saveBagAsExecutor);
        context.register("validateBagsCommand",validateBagsExecutor);    
        context.register("validateManifestCommand",validateManifestExecutor);        
        context.register("exportCommand",exportExecutor);
        context.register("renameCommand",renameExecutor);
        context.register("openLogFileCommand",openLogFileExecutor);
    }
    
    public void registerTreeListener(String label,final JTree tree){
    	if(AbstractBagConstants.DATA_DIRECTORY.equals(label)){
            tree.addTreeSelectionListener(new TreeSelectionListener(){
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    TreePath[] paths = tree.getSelectionPaths();
                    if(paths == null || paths.length == 0) {
                        return;
                    }
                    for(TreePath path: paths){
                        if(path.getPathCount() == 1){
                            removeDataToolBarAction.setEnabled(false);
                            return;
                        }
                    }
                    removeDataToolBarAction.setEnabled(true);
                }
            });
    	}else{
            /*
            tree.addTreeSelectionListener(new TreeSelectionListener(){
                @Override
                public void valueChanged(TreeSelectionEvent e){
                    TreePath[] paths = tree.getSelectionPaths();
                    if(paths == null || paths.length == 0) {
                        return;
                    }
                    for(TreePath path: paths){
                        if(path.getPathCount() == 1){
                            removeTagFileToolbarAction.setEnabled(false);
                            return;
                        }
                    }
                    removeTagFileToolbarAction.setEnabled(true);
                }
            });*/
    	}
    }
    public static BagView getInstance() {
        return instance;
    } 
    public void setCompleteExecutor() {        
        boolean enable = 
            getBag().getFetchTxt() == null && 
            getBag().getFile() != null && 
            getBag().getFile().exists() &&
            !getBag().isChanged()
        ;
        completeExecutor.setEnabled(enable);       
        completeBagHandler.setEnabled(enable);
    }    
    public void setValidateExecutor() {       
        boolean enable = 
            getBag().getFetchTxt() == null && 
            getBag().getFile() != null && 
            getBag().getFile().exists() &&
            !getBag().isChanged()
        ;
        validateExecutor.setEnabled(enable);       
        validateBagHandler.setEnabled(enable);
    }       
}