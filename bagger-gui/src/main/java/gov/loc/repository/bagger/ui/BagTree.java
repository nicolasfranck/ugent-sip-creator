package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.Dimension;
import java.io.File;
import java.util.*;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.FUtils;

public final class BagTree extends JTree {
    private static final long serialVersionUID = -5361474872106399068L;
    private static final Log log = LogFactory.getLog(BagTree.class);
    private int BAGTREE_WIDTH = 400;
    private int BAGTREE_HEIGHT = 160;
    private int BAGTREE_ROW_MODIFIER = 22;
    private File bagDir;
    private DefaultTreeModel bagTreeModel;
    private TreePath rootPath;
    private String basePath;
    private DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(AbstractBagConstants.DATA_DIRECTORY);
    
    //Nicolas Franck: nergens gebruikt
    //private ArrayList<DefaultMutableTreeNode> srcNodes = new ArrayList<DefaultMutableTreeNode>();
	
    public BagView getBagView(){
        return BagView.getInstance();
    }
    public BagTree(String path, boolean isPayload) {
        super();
        
        setShowsRootHandles(true);
        /*
         * Nicolas Franck: is het nodig om de root 'data' te tonen?
         * nieuwe bagit supporteert data1, data2 .., maar wij niet
         */
        setRootVisible(false);
        
        basePath = path;
        
        //i.e. 'data' => maar dit is al hierboven ingesteld??
        //parentNode = new DefaultMutableTreeNode(basePath);
        initialize();
        
        //initListeners();
        JTextField nameTextField = new JTextField();
        int fieldHeight = nameTextField.getFontMetrics(nameTextField.getFont()).getHeight() + 5;
        BAGTREE_ROW_MODIFIER = fieldHeight;

        /*
         * Nicolas Franck: dragEnabled enkel nuttig indien de interne nodelist naar buiten geëxporteerd kan worden!
         * kan volgens BagTreeTransferHandler, maar wat houdt dat precies in?
         */
        /*
        this.setDragEnabled(true);
        this.setDropMode(DropMode.ON_OR_INSERT);
        this.setTransferHandler(new BagTreeTransferHandler(isPayload));
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        */        
        getBagView().registerTreeListener(path,this);        
        
    }
	
    private void initialize() {
        setModel(new DefaultTreeModel(parentNode));
        rootPath = new TreePath(parentNode.getPath());

        //setCheckingPath(rootPath);
        setAnchorSelectionPath(rootPath);
        makeVisible(rootPath);
        //getCheckingModel().setCheckingMode(TreeCheckingModel.CheckingMode.PROPAGATE);
        setLargeModel(true);
        //setPreferredSize(getTreeSize());
        requestFocus();
        //setScrollsOnExpand(true);
        
    }
	
    public void populateNodes(DefaultBag bag, String path, File rootSrc, boolean isParent) {
        basePath = path;

        log.debug("BagTree.populateNodes");  
        
       
        if (bag.getPayload() != null && rootSrc.listFiles() != null) {
            addNodes(rootSrc, isParent);            
        }else{
            log.debug("BagTree.populateNodes listFiles NULL:" );
            
            List<String> payload = bag.getPayloadPaths();
            
            //Nicolas Franck
            /*
            if(!bag.isHoley()){                
                log.debug("BagTree.populateNodes getPayloadPaths:" );
                payload = bag.getPayloadPaths();                
            }else{
                log.debug("BagTree.populateNodes getFetchPayload:");
                payload = bag.getPayloadPaths(); //bag.getFetchPayload();                
                //basePath = bag.getFetch().getBaseURL();
            }*/            
                        
            List<DefaultMutableTreeNode>structuredList = FUtils.listToStructure(payload.toArray(new String [] {}));                        
            if(isParent) {
                parentNode = structuredList.get(0);
            }
            else {
                parentNode.add(structuredList.get(0));
            }
            initialize();            
            
            //Nicolas Franck: toon structuur ipv lijst
            /*
            for(Iterator<String> it=payload.iterator(); it.hasNext(); ){
                String filePath = it.next();
                try{
                    String normalPath;
                    if(bag.isHoley()){
                        normalPath = BaggerFileEntity.removeBasePath("data", filePath);
                    }else{
                        normalPath = BaggerFileEntity.removeBasePath(basePath, filePath);
                    }
                    if(!nodeAlreadyExists(normalPath)){
                        addNode(normalPath);
                    }
                }catch(Exception e){
                    if(!nodeAlreadyExists(filePath)){
                        addNode(filePath);
                    }
                    log.error("BagTree.populateNodes: " + e.getMessage());
                }
            }*/
            log.debug("BagTree rows: " + payload.size());
            BAGTREE_HEIGHT = BAGTREE_ROW_MODIFIER * (payload.size() + 1);
            //setPreferredSize(getTreeSize());
            invalidate();
        }
    }

    public boolean addNodes(File file, boolean isParent) {
        if(!nodeAlreadyExists(file.getName())){           
            
            //Nicolas Franck
            //DefaultMutableTreeNode node = createNodeTree(null, null, file);                        
            
            DefaultMutableTreeNode node = FUtils.toTreeNode(file);
            
            //Nicolas Franck: nergens gebruikt
            //srcNodes.add(node);
            if(isParent) {
                parentNode = node;
            }
            else {
                parentNode.add(node);
            }
            initialize();
        }else{
            return true;
        }
        return false;
    }

    private boolean nodeAlreadyExists(String path) {        
        DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(path);
        String node = aNode.toString();
        boolean b = parentNode.isNodeChild(aNode);
        if(b){
            return b;
        }
        for (int i=0; i < parentNode.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
            String child = childNode.toString();
            if(child.equalsIgnoreCase(node)){
                b = true;
                break;
            }
        }
        return b;
    }

    public void addNode(String filePath) {
        //DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TNode(filePath,filePath));        
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(filePath);        
        //Nicolas Franck: nooit gebruikt
        //srcNodes.add(node);
        parentNode.add(node);
        initialize();
    }
	
    /** Add nodes from under "dir" into curTop. Highly recursive. */
    
    //Nicolas Franck: vervangen door FUtils.toTreeNode(File file)
    /*
    private DefaultMutableTreeNode createNodeTree(DefaultMutableTreeNode curTop, DefaultMutableTreeNode displayTop, File dir) {
        String curPath = dir.getPath();
        String displayPath = dir.getName();
        DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
        DefaultMutableTreeNode displayDir = new DefaultMutableTreeNode(displayPath);
        if (curTop != null) { // should only be null at root
            curTop.add(curDir);
            displayTop.add(displayDir);
        }
        
        ArrayList<String>ol = new ArrayList<String>();
        
        //Nicolas Franck: obsolete in this context: vector are synchronized version of arraylist
        //Vector<String> ol = new Vector<String>();
        //display("addNodes: " + dir.list());
        String[] tmp = dir.list();
        if (tmp != null && tmp.length > 0) {
            //for (int i = 0; i < tmp.length; i++)ol.addElement(tmp[i]);
            ol.addAll(Arrays.asList(tmp));
        }

        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        File f;
        
        //Nicolas Franck: obsolete in this context: vector are synchronized version of arraylist
        //Vector<String> files = new Vector<String>();
        ArrayList<String> files = new ArrayList<String>();
        
        // Make two passes, one for Dirs and one for Files. This is #1.
        for (int i = 0; i < ol.size(); i++) {
            //String thisObject = (String) ol.elementAt(i);
            
            String thisObject = (String) ol.get(i);
            
            String newPath;
            if (curPath.equals(".")) {
                newPath = thisObject;
            }
            else {
                newPath = curPath + File.separator + thisObject;
            }
            if ((f = new File(newPath)).isDirectory()) {
                createNodeTree(curDir, displayDir, f);
            }
            else {
                //files.addElement(thisObject);
                files.add(thisObject);
            }
        }
        // Pass two: for files.
        //display("createBagManagerTree: files.size: " + files.size());
        for(int fnum = 0; fnum < files.size(); fnum++){
            //String elem = files.elementAt(fnum);
            String elem = files.get(fnum);            
            DefaultMutableTreeNode elemNode = new DefaultMutableTreeNode(elem);
            curDir.add(elemNode);
            displayDir.add(elemNode);
        }

        //return curDir;
        return displayDir;
    }*/

    public void setParentNode(DefaultMutableTreeNode parent) {
        this.parentNode = parent;
    }	
    public DefaultMutableTreeNode getParentNode() {
        return parentNode;
    }
    public File getBagDir() {
        return bagDir;
    }
    public void setBagDir(File file) {
        this.bagDir = file;
    }
    public DefaultTreeModel getBagTreeModel() {
        return bagTreeModel;
    }
    public void setBagTreeModel(DefaultTreeModel model) {
        this.bagTreeModel = model;
    }
    public Dimension getTreeSize() {
    	return new Dimension(BAGTREE_WIDTH, BAGTREE_HEIGHT);
    }    
}