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
   
	
    public BagView getBagView(){
        return BagView.getInstance();
    }
    public BagTree(String path, boolean isPayload) {
        super();
        
        setShowsRootHandles(true);       
        setRootVisible(false);        
        basePath = path;        
        
        initialize();        
       
        JTextField nameTextField = new JTextField();
        int fieldHeight = nameTextField.getFontMetrics(nameTextField.getFont()).getHeight() + 5;
        BAGTREE_ROW_MODIFIER = fieldHeight;
       
        getBagView().registerTreeListener(path,this);        
        
    }
	
    private void initialize() {
        setModel(new DefaultTreeModel(parentNode));
        rootPath = new TreePath(parentNode.getPath());
        setAnchorSelectionPath(rootPath);
        makeVisible(rootPath);        
        setLargeModel(true);        
        requestFocus();
    }
	
    public void populateNodes(DefaultBag bag, String path, File rootSrc, boolean isParent) {
        basePath = path;

        log.debug("BagTree.populateNodes");  
        
       
        if (bag.getPayload() != null && rootSrc.listFiles() != null) {
            addNodes(rootSrc, isParent);            
        }else{
            log.debug("BagTree.populateNodes listFiles NULL:" );
            
            List<String> payload = bag.getPayloadPaths();
            
            List<DefaultMutableTreeNode>structuredList = FUtils.listToStructure(payload.toArray(new String [] {}));                        
            
            if(structuredList.size() > 0){
                if(isParent) {
                    parentNode = structuredList.get(0);
                }
                else {
                    parentNode.add(structuredList.get(0));
                }
            }
            
            initialize();                        
            
            log.debug("BagTree rows: " + payload.size());
            BAGTREE_HEIGHT = BAGTREE_ROW_MODIFIER * (payload.size() + 1);
        
            invalidate();
        }
    }

    public boolean addNodes(File file, boolean isParent) {
        if(!nodeAlreadyExists(file.getName())){           
            
            DefaultMutableTreeNode node = FUtils.toTreeNode(file);            
           
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
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(filePath);                
        parentNode.add(node);
        initialize();
    }
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