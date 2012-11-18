package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.handlers.*;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.util.Assert;
import ugent.bagger.bagitmets.MetsFileDateCreated;
import ugent.bagger.dialogs.ValidateManifestDialog;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.views.DefaultView;

public class BagView extends DefaultView {
    private static final Log log = LogFactory.getLog(BagView.class);
    public static BagView instance;
    private final static int ONE_SECOND = 1000;
    private int DEFAULT_WIDTH = 1024;
    private int DEFAULT_HEIGHT = 768;    
    //Nicolas Franck: long running process are now managed by monitor function in ugent.bagger.helper.SwingUtils    
    //public ProgressMonitor progressMonitor;
    //public LongTask task;
    //public Cancellable longRunningProcess = null;
    //private final Timer timer = new Timer(ONE_SECOND/10, null);    
    //private Bagger bagger;
    //private DefaultBag bag;    
    //Nicolas Franck
    private MetsBag bag;
    //private BaggerProfileStore profileStore;
    private BagTree bagPayloadTree;
    private BagTree bagTagFileTree;
    private File bagRootPath;
    private String userHomeDir;
    private TagManifestPane tagManifestPane;
    private InfoFormsPane infoFormsPane;
    private BagTreePanel bagPayloadTreePanel;
    private BagTreePanel bagTagFileTreePanel;
    private JPanel bagButtonPanel;
    private JPanel bagTagButtonPanel;
    //private JPanel topButtonPanel;
    public StartNewBagHandler startNewBagHandler = new StartNewBagHandler();
    public StartExecutor startExecutor = new StartExecutor();
    public OpenBagHandler openBagHandler = new OpenBagHandler();
    public OpenExecutor openExecutor = new OpenExecutor();
    
    //Nicolas Franck: ondersteunt enkel één bag in place per keer    
    //public CreateBagInPlaceHandler createBagInPlaceHandler = new CreateBagInPlaceHandler();
    //public CreateBagInPlaceExecutor createBagInPlaceExecutor = new CreateBagInPlaceExecutor();
    
    //Nicolas Franck: creëer meerdere bags in place of door copy
    public CreateBagsHandler createBagsHandler = new CreateBagsHandler();
    public CreateBagsExecutor createBagsExecutor = new CreateBagsExecutor();    
    
    public SaveBagHandler6 saveBagHandler = new SaveBagHandler6();    
    public SaveBagExecutor saveBagExecutor = new SaveBagExecutor();
    public SaveBagAsHandler saveBagAsHandler = new SaveBagAsHandler();
    public SaveBagAsExecutor saveBagAsExecutor = new SaveBagAsExecutor();    
    public ValidateBagHandler2 validateBagHandler = new ValidateBagHandler2();    
    public ValidateExecutor validateExecutor = new ValidateExecutor();    
    public CompleteBagHandler2 completeBagHandler = new CompleteBagHandler2();
    public CompleteExecutor completeExecutor = new CompleteExecutor();    
    public ClearBagHandler clearBagHandler = new ClearBagHandler();
    public ClearBagExecutor clearExecutor = new ClearBagExecutor();
    public AddDataHandler addDataHandler;
    public AddDataExecutor addDataExecutor = new AddDataExecutor();
    public RemoveDataHandler removeDataHandler;
    public RemoveTagFileHandler removeTagFileHandler;
    public AddTagFileHandler addTagFileHandler;
    private JLabel addDataToolBarAction;
    private JLabel removeDataToolBarAction;
    private JLabel viewTagFilesToolbarAction;
    private JLabel addTagFileToolBarAction;
    private JLabel removeTagFileToolbarAction;
    //Nicolas Franck: vldocking sucks!
    private JSplitPane mainPanel;
    private JSplitPane leftPanel;
    //interpretatie van file attribuut 'CREATED' (CURRENT_DATE,LAST_MODIFIED)
    private MetsFileDateCreated metsFileDateCreated = MetsFileDateCreated.CURRENT_DATE;

    public MetsFileDateCreated getMetsFileDateCreated() {
        return metsFileDateCreated;
    }
    public void setMetsFileDateCreated(MetsFileDateCreated metsFileDateCreated) {
        this.metsFileDateCreated = metsFileDateCreated;
    }
    public JSplitPane getLeftPanel() {
        if(leftPanel == null){
            leftPanel = createBagPanel();
        }
        return leftPanel;
    }
    public void setLeftPanel(JSplitPane leftPanel) {
        this.leftPanel = leftPanel;
    }    
    
    public JSplitPane getMainPanel(){        
        if(mainPanel == null){
            mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,getLeftPanel(),getInfoFormsPane());                                                
            mainPanel.setDividerLocation(0.3);
            mainPanel.setResizeWeight(0.5);
        }
        return mainPanel;
    }
    public void setMainPanel(JSplitPane mainPanel) {
        this.mainPanel = mainPanel;
    }    
    /*
     * Nicolas Franck
     */
    //private final TimerListener timerListener = new TimerListener();    
    
    public BagView() {
        //verhinder twee instantie
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
    public JPanel getBagTagButtonPanel() {
        if(bagTagButtonPanel == null){
            bagTagButtonPanel = createBagTagButtonPanel();
        }
        return bagTagButtonPanel;
    }

    public void setBagTagButtonPanel(JPanel bagTagButtonPanel) {
        this.bagTagButtonPanel = bagTagButtonPanel;
    }

    public TagManifestPane getTagManifestPane() {
        if(tagManifestPane == null){
            tagManifestPane = new TagManifestPane();
        }
        return tagManifestPane;
    }

    public void setTagManifestPane(TagManifestPane tagManifestPane) {
        this.tagManifestPane = tagManifestPane;
    }
    /*
     * Nicolas Franck: dit wordt niet gevisualiseerd in deze view, maar
     * dient als panel voor metaView. Op die manier kan men vanuit deze
     * view de metaView manipuleren     * 
     */
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
    /*
    public void setBagger(Bagger bagger) {
        Assert.notNull(bagger, "The bagger property is required");
        this.bagger = bagger;
    }
    
    public Bagger getBagger() {
    	return bagger;
    }*/
    
    public void setBag(MetsBag bag) {
        this.bag = bag;
    }

    public MetsBag getBag() {
        if(bag == null){
            bag = new MetsBag();
        }
        return bag;
    }
    
    public void setBagRootPath(File bagRootPath) {
    	this.bagRootPath = bagRootPath;
    }
    
    public File getBagRootPath() {
    	return bagRootPath;
    }
    
    public Dimension getMinimumSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }    
    public ImageIcon getPropertyImage(String name) {        
        return new ImageIcon(getImageSource().getImage(name));        
    }

    public void setBagPayloadTree(BagTree bagPayloadTree) {
        this.bagPayloadTree = bagPayloadTree;
    }
    public BagTree createBagPayloadTree(String path, boolean isPayload){
        final BagTree tree = new BagTree(path,isPayload);
        
        //Nicolas Franck    
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

    public BagTreePanel getBagTagFileTreePanel() {
        if(bagTagFileTreePanel == null){
            bagTagFileTreePanel = new BagTreePanel(getBagTagFileTree());
        }
        return bagTagFileTreePanel;
    }

    public void setBagTagFileTreePanel(BagTreePanel bagTagFileTreePanel) {
        this.bagTagFileTreePanel = bagTagFileTreePanel;
    }
    
    public void setBagTagFileTree(BagTree bagTree) {        
    	this.bagTagFileTree = bagTree;
    }
    
    public BagTree getBagTagFileTree() {
        if(bagTagFileTree == null){
            bagTagFileTree = new BagTree(getMessage("bag.label.noname"), false);
            //Nicolas Franck    
            InputMap inputMap = bagTagFileTree.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            inputMap.put(KeyStroke.getKeyStroke((char)127),"removeTagfile");
            ActionMap actionMap = bagTagFileTree.getActionMap();
            actionMap.put("removeTagfile",new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    removeTagFileHandler.removeTagFile();
                }               
            }); 
        }
    	return bagTagFileTree;
    }
    
    // This populates the default view descriptor declared as the startingPageId
    // property in the richclient-application-context.xml file.
    @Override    
    protected JComponent createControl() {    	

        /*
         * Nicolas Franck: vreemd dat dit nergens gebruikt wordt..
         * zou handig zijn voor basismap bij selecteren van bestanden (i.p.v root of C:/)
         */
    	this.userHomeDir = System.getProperty("user.home");        
        
    	initializeCommands();

        //ApplicationServices services = getApplicationServices();     
	
        //Nicolas Franck: rol? nergens wordt dit aan toegevoegd?
        /*
    	Color bgColor = new Color(20,20,100);
    	topButtonPanel = createTopButtonPanel();
    	topButtonPanel.setBackground(bgColor);
        */                  	
    	
        return getMainPanel();        

        //Nicolas Franck
        /*
    	GridBagLayout layout = new GridBagLayout();
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, 0, 1, 1, 50, 100,GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        layout.setConstraints(bagPanel,glbc);

        JPanel mainPanel = new JPanel(layout);
        mainPanel.add(bagPanel);        
    	JPanel bagViewPanel = new JPanel(new BorderLayout(2, 2));
        bagViewPanel.setBackground(bgColor);
    	bagViewPanel.add(mainPanel, BorderLayout.CENTER);
        
        return bagViewPanel;*/
    }
    //Nicolas Franck: enig nut van deze panel is de instelling van de handlers blijkbaar
    /*
    private JPanel createTopButtonPanel(){
    	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    	startNewBagHandler = new StartNewBagHandler();
    	openBagHandler = new OpenBagHandler();
    	createBagInPlaceHandler = new CreateBagInPlaceHandler();
        saveBagHandler = new SaveBagHandler2();
    	saveBagAsHandler = new SaveBagAsHandler();
    	completeBagHandler = new CompleteBagHandler2();
    	validateBagHandler = new ValidateBagHandler2();
    	clearBagHandler = new ClearBagHandler();
        return buttonPanel;
    }*/
    
    private JSplitPane createBagPanel(){        
    	
    	LineBorder border = new LineBorder(Color.GRAY,1);

    	getBagPayloadTree().setEnabled(false);
        getBagPayloadTreePanel().setEnabled(false);    	
    	getBagPayloadTreePanel().setBorder(border);
    	getBagPayloadTreePanel().setToolTipText(getMessage("bagTree.help"));

    	getBagTagFileTree().setEnabled(false);    	
    	getBagTagFileTreePanel().setEnabled(false);
    	getBagTagFileTreePanel().setBorder(border);
    	getBagTagFileTreePanel().setToolTipText(getMessage("bagTree.help"));

    	getTagManifestPane().setToolTipText(getMessage("compositePane.tab.help"));    	

        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.5);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        JPanel payloadPannel = new JPanel();
        splitPane.setLeftComponent(payloadPannel);
        payloadPannel.setLayout(new BorderLayout(0, 0));

        JPanel payLoadToolBarPanel = new JPanel();
        payloadPannel.add(payLoadToolBarPanel, BorderLayout.NORTH);
        payLoadToolBarPanel.setLayout(new GridLayout(1, 0, 0, 0));

        JPanel payloadLabelPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) payloadLabelPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        payLoadToolBarPanel.add(payloadLabelPanel);

        JLabel lblPayloadTree = new JLabel(getMessage("bagView.payloadTree.name"));
        payloadLabelPanel.add(lblPayloadTree);

        payLoadToolBarPanel.add(getBagButtonPanel());

        payloadPannel.add(getBagPayloadTreePanel(), BorderLayout.CENTER);

        JPanel tagFilePanel = new JPanel();
        splitPane.setRightComponent(tagFilePanel);
        tagFilePanel.setLayout(new BorderLayout(0, 0));

        JPanel tagFileToolBarPannel = new JPanel();
        tagFilePanel.add(tagFileToolBarPannel, BorderLayout.NORTH);
        tagFileToolBarPannel.setLayout(new GridLayout(0, 2, 0, 0));

        JPanel TagFileLabelPanel = new JPanel();
        FlowLayout tagFileToolbarFlowLayout = (FlowLayout) TagFileLabelPanel.getLayout();
        tagFileToolbarFlowLayout.setAlignment(FlowLayout.LEFT);
        tagFileToolBarPannel.add(TagFileLabelPanel);

        JLabel tagFileTreeLabel = new JLabel(getMessage("bagView.TagFilesTree.name"));
        TagFileLabelPanel.add(tagFileTreeLabel);

        tagFileToolBarPannel.add(getBagTagButtonPanel());

        tagFilePanel.add(getBagTagFileTreePanel(), BorderLayout.CENTER);

        return splitPane;
    }
    
    private JPanel createBagButtonPanel() {
    	
    	addDataHandler = new AddDataHandler();
    	removeDataHandler = new RemoveDataHandler();
    	
    	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
		
        addDataToolBarAction = new JLabel("");
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

        removeDataToolBarAction = new JLabel("");
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
		
    	addDataHandler = new AddDataHandler();
    	removeDataHandler = new RemoveDataHandler();

        return buttonPanel;
    }
    
    private JPanel createBagTagButtonPanel() {
    	
    	JPanel buttonPanel = new JPanel();
    	
    	final ShowTagFilesHandler showTageFileHandler = new ShowTagFilesHandler();
    	addTagFileHandler = new AddTagFileHandler();
    	removeTagFileHandler = new RemoveTagFileHandler();
    	
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));

        viewTagFilesToolbarAction = new JLabel("");
        viewTagFilesToolbarAction.setEnabled(false);
        viewTagFilesToolbarAction.setHorizontalAlignment(SwingConstants.CENTER);
        viewTagFilesToolbarAction.setBorder(new LineBorder(viewTagFilesToolbarAction.getBackground(),1));
        viewTagFilesToolbarAction.setIcon(getPropertyImage("Bag_ViewTagFile.icon"));
        viewTagFilesToolbarAction.setToolTipText(getMessage("bagView.TagFilesTree.viewfile.tooltip"));
		
        viewTagFilesToolbarAction.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if(viewTagFilesToolbarAction.isEnabled()) {
                    showTageFileHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                viewTagFilesToolbarAction.setBorder(new LineBorder(viewTagFilesToolbarAction.getBackground(),1));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(viewTagFilesToolbarAction.isEnabled()) {
                    viewTagFilesToolbarAction.setBorder(new LineBorder(Color.GRAY,1));
                }
            }
        });
        buttonPanel.add(viewTagFilesToolbarAction);

        addTagFileToolBarAction = new JLabel("");
        addTagFileToolBarAction.setEnabled(false);
        addTagFileToolBarAction.setHorizontalAlignment(SwingConstants.CENTER);
        addTagFileToolBarAction.setBorder(new LineBorder(addTagFileToolBarAction.getBackground(),1));
        addTagFileToolBarAction.setIcon(getPropertyImage("Bag_Content.add.icon"));
        addTagFileToolBarAction.setToolTipText(getMessage("bagView.TagFilesTree.addbutton.tooltip"));

        addTagFileToolBarAction.addMouseListener(new MouseAdapter(){			
            @Override
            public void mousePressed(MouseEvent e) {
                if(addTagFileToolBarAction.isEnabled()) {
                    addTagFileHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addTagFileToolBarAction.setBorder(new LineBorder(addTagFileToolBarAction.getBackground(),1));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(addTagFileToolBarAction.isEnabled()) {
                    addTagFileToolBarAction.setBorder(new LineBorder(Color.GRAY,1));
                }
            }
        });
        buttonPanel.add(addTagFileToolBarAction);
		
        removeTagFileToolbarAction = new JLabel("");
        removeTagFileToolbarAction.setEnabled(false);
        removeTagFileToolbarAction.setHorizontalAlignment(SwingConstants.CENTER);
        removeTagFileToolbarAction.setBorder(new LineBorder(removeTagFileToolbarAction.getBackground(),1));
        removeTagFileToolbarAction.setIcon(getPropertyImage("Bag_Content.minus.icon"));
        removeTagFileToolbarAction.setToolTipText(getMessage("bagView.TagFilesTree.remove.tooltip"));

        buttonPanel.add(removeTagFileToolbarAction);
        removeTagFileToolbarAction.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if(removeTagFileToolbarAction.isEnabled()) {
                    removeTagFileHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                removeTagFileToolbarAction.setBorder(new LineBorder(removeTagFileToolbarAction.getBackground(),1));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(removeTagFileToolbarAction.isEnabled()) {
                    removeTagFileToolbarAction.setBorder(new LineBorder(Color.GRAY,1));
                }
            }
        });

        final JLabel spacerLabel = new JLabel("    ");
        buttonPanel.add(spacerLabel);

    	addTagFileHandler = new AddTagFileHandler();
    	removeTagFileHandler = new RemoveTagFileHandler();

        return buttonPanel;
    }

    public void enableBagSettings(boolean b) {
    	getBagPayloadTree().setEnabled(b);
    	getBagPayloadTreePanel().setEnabled(b);
    	getBagTagFileTree().setEnabled(b);
    	getBagTagFileTreePanel().setEnabled(b);
        getInfoFormsPane().getInfoInputPane().setEnabled(b);
    }

    public String updateBaggerRules() {        
        getBag().updateStrategy();        
        return "";
    }

    public void showWarningErrorDialog(String title, String msg) {    	
        new MessageDialog(title,msg).showDialog();
    }

    private void initializeCommands() {
    	startExecutor.setEnabled(true);
    	openExecutor.setEnabled(true);
    	//createBagInPlaceExecutor.setEnabled(true);
        
        createBagsExecutor.setEnabled(true);
        
    	clearExecutor.setEnabled(false);
        validateExecutor.setEnabled(false);
        completeExecutor.setEnabled(false);
        addDataExecutor.setEnabled(false);
        saveBagExecutor.setEnabled(false);
        saveBagAsExecutor.setEnabled(false);
    }

    public void updateClearBag() {
    	enableBagSettings(false);    	
    	getInfoFormsPane().getHoleyValue().setText("");
        getInfoFormsPane().getBagVersionValue().setText("");
        getInfoFormsPane().getBagNameValue().setText("");
        getInfoFormsPane().getSerializeValue().setText("");
    	addDataToolBarAction.setEnabled(false);
    	removeDataToolBarAction.setEnabled(false);
    	addDataExecutor.setEnabled(false);
    	saveBagExecutor.setEnabled(false);
    	saveBagAsExecutor.setEnabled(false);
    	viewTagFilesToolbarAction.setEnabled(false);
    	addTagFileToolBarAction.setEnabled(false);
    	removeTagFileToolbarAction.setEnabled(false);
    	clearExecutor.setEnabled(false);
    	validateExecutor.setEnabled(false);
    	completeExecutor.setEnabled(false);
    	getBagButtonPanel().invalidate();
    	//topButtonPanel.invalidate();
    }

    public void updateNewBag() {
        viewTagFilesToolbarAction.setEnabled(true);
        enableBagSettings(true);
        addDataToolBarAction.setEnabled(true);
        addDataExecutor.setEnabled(true);
        addTagFileToolBarAction.setEnabled(true);
        getBagButtonPanel().invalidate();
    }

    public void updateOpenBag() {
        addDataToolBarAction.setEnabled(true);
        addDataExecutor.setEnabled(true);
        saveBagExecutor.setEnabled(true);
        addTagFileToolBarAction.setEnabled(true);
        viewTagFilesToolbarAction.setEnabled(true);
        saveBagAsExecutor.setEnabled(true);
        getBagButtonPanel().invalidate();
        clearExecutor.setEnabled(true);
        setCompleteExecutor();  // Disables the Is Complete Bag Button for Holey Bags  
        setValidateExecutor();  // Disables the Validate Bag Button for Holey Bags
        //topButtonPanel.invalidate();
    }
    
    public void updateBagInPlace() {
    	addDataToolBarAction.setEnabled(true);
        addDataExecutor.setEnabled(true);
        saveBagExecutor.setEnabled(false);
        saveBagAsExecutor.setEnabled(true);
        addTagFileToolBarAction.setEnabled(true);
        viewTagFilesToolbarAction.setEnabled(true);
        getBagButtonPanel().invalidate();
        completeExecutor.setEnabled(true);
        validateExecutor.setEnabled(true);
        getBagButtonPanel().invalidate();
        //topButtonPanel.invalidate();
    }
    
    public void updateSaveBag() {
        addDataToolBarAction.setEnabled(true);
        addDataExecutor.setEnabled(true);
        saveBagExecutor.setEnabled(true);
        addTagFileToolBarAction.setEnabled(true);
        viewTagFilesToolbarAction.setEnabled(true);
        saveBagAsExecutor.setEnabled(true);
        getBagButtonPanel().invalidate();
        clearExecutor.setEnabled(true);
        setCompleteExecutor();  // Disables the Is Complete Bag Button for Holey Bags  
        setValidateExecutor();  // Disables the Validate Bag Button for Holey Bags
        //topButtonPanel.invalidate();
    }
    
    public void updateAddData() {
    	saveBagAsExecutor.setEnabled(true);
    	getBagButtonPanel().invalidate();
    	//topButtonPanel.invalidate();
    }
    
    public void updateManifestPane() {
        bagTagFileTree = new BagTree(getBag().getName(), false);
        Collection<BagFile> tags = getBag().getTags();
        for(Iterator<BagFile> it=tags.iterator(); it.hasNext(); ) {            
            bagTagFileTree.addNode(it.next().getFilepath());
        }
        getBagTagFileTreePanel().refresh(bagTagFileTree);
    }

    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context) {
    	context.register("startCommand", startExecutor);
    	context.register("openCommand", openExecutor);
    	//context.register("createBagInPlaceCommand", createBagInPlaceExecutor);
        
        context.register("createBagsCommand", createBagsExecutor);
        
    	context.register("clearCommand", clearExecutor);
    	context.register("validateCommand", validateExecutor);
    	context.register("completeCommand", completeExecutor);
    	context.register("addDataCommand", addDataExecutor);
    	context.register("saveBagCommand", saveBagExecutor);
    	context.register("saveBagAsCommand", saveBagAsExecutor);
        
        context.register("validateBagsCommand",new ValidateBagsExecutor(){
            {
                setEnabled(true);
            }
        });    
        context.register("validateManifestCommand",new AbstractActionCommandExecutor(){
            {
                setEnabled(true);
            }
            @Override
            public void execute(){
                ValidateManifestDialog dialog = new ValidateManifestDialog(SwingUtils.getFrame(),true);
                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);  
                dialog.pack();
                dialog.setVisible(true);
            }
        });
               
    }

    /**
     * The actionPerformed method in this class
     * is called each time the Timer "goes off".
     */
    //Nicolas Franck
    /*
    class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            // check if task is completed or user has clicked cancel button
            if (task.hasUserTriedToCancel() || task.isDone()) {
            	// we are done
                progressMonitor.close();
                Toolkit.getDefaultToolkit().beep();                
                timer.stop();                
                log.info("Stopped the timer");
                // getting an array of Action Listeners from Timer Listener (will have only one element)
                ActionListener[] als = (ActionListener[])(timer.getListeners(ActionListener.class));
                // Removing Action Listener from timer
		if (als.length > 0)timer.removeActionListener(als[0]);
                
                //Nicolas Franck
      
                if (longRunningProcess != null && !task.isDone()) {
                    log.info("Trying to cancel the long running process: " + longRunningProcess);
                    longRunningProcess.cancel();
                }
            } 
        }
    }*/
   
    //Nicolas Franck
    /*
    public void statusBarBegin(Progress progress, String message, String activityMonitored) {
    	BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());
        task = new LongTask();
        task.setActivityMonitored(activityMonitored);
        //Nicolas Franck: Progress heeft execute methode!
        task.setProgress(progress);

        //timer.addActionListener(new TimerListener());
        timer.addActionListener(timerListener);

        progressMonitor = new ProgressMonitor(getControl(),
        		message, "Preparing the operation...", 0, 1);
        progressMonitor.setMillisToDecideToPopup(ONE_SECOND);
        task.setMonitor(progressMonitor);        
        task.go();
        timer.start();
    }
    public void statusBarEnd() {
    	BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());    	
    }*/ 
    
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
            });
    	}
    }
    
    /*
    public BaggerProfileStore getProfileStore() {
        return profileStore;
    }

    public void setProfileStore(BaggerProfileStore profileStore) {
        this.profileStore = profileStore;
    }*/

    public static BagView getInstance() {
        return instance;
    }

    public String getPropertyMessage(String propertyName) {
        return getMessage(propertyName);
    }

    /*
     * Returns true if the Fetch.txt file exists.
     * This would be true in the case of a Holey Bag
     * Returns false for all other types of Bags
     */
    private boolean checkFetchTxtFile() {
        /*
         * Nicolas Franck
         */
        return (getBag().getFetchTxt() != null);
        /*
    	if (bag.getFetchTxt() != null)
    		return true;
    	else 
    		return false;
         *
         */
    }

    /*
     * Disables the Is Complete Bag Button if Fetch.txt file exists.
     * This is true in the case of a Holey Bag
     * The Validate Button is enabled for all other types of Bags
     */
    private void setCompleteExecutor() {
        /*
         * Nicolas Franck
         */
        completeExecutor.setEnabled(!checkFetchTxtFile());
        /*
    	if (checkFetchTxtFile())
    		completeExecutor.setEnabled(false);
    	else
    		completeExecutor.setEnabled(true);
         *
         */
    }
    
    /*
     * Disables the Validate Bag Button if Fetch.txt file exists.
     * This is true in the case of a Holey Bag
     * The Validate Button is enabled for all other types of Bags
     */
    private void setValidateExecutor() {
        /*
         * Nicolas Franck
         */
        validateExecutor.setEnabled(!checkFetchTxtFile());
        /*
    	if (checkFetchTxtFile())
    		validateExecutor.setEnabled(false);
    	else
    		validateExecutor.setEnabled(true);
         * 
         */        
        
    }       
}