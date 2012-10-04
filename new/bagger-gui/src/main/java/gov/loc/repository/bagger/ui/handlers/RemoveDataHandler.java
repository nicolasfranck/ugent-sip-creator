package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.BaggerFileEntity;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RemoveDataHandler extends AbstractAction {
    private static final Log log = LogFactory.getLog(RemoveDataHandler.class);
    private static final long serialVersionUID = 1L;   

    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public RemoveDataHandler() {
        super();            
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        removeData();
    }

    public void removeData() {
    	String message = "";
        BagView bagView = BagView.getInstance();
    	DefaultBag bag = bagView.getBag();

    	TreePath[] paths = bagView.getBagPayloadTree().getSelectionPaths();
    	
    	if (paths != null) {
            DefaultTreeModel model = (DefaultTreeModel)bagView.getBagPayloadTree().getModel();
            for (int i=0; i < paths.length; i++) {
                TreePath path = paths[i];
                Object node = path.getLastPathComponent();
                log.debug("removeData: " + path.toString());
                log.debug("removeData pathCount: " + path.getPathCount());
                File filePath = null;
                String fileName = null;
                if (path.getPathCount() > 0){                    
                    filePath = new File(""+path.getPathComponent(0));
                    for (int j=1; j<path.getPathCount(); j++) {
                        filePath = new File(filePath, ""+path.getPathComponent(j));
                        log.debug("\t" + filePath);
                    }
                }
                if (filePath != null) {
                    fileName = BaggerFileEntity.normalize(filePath.getPath());
                }
                log.debug("removeData filePath: " + fileName);
                if (fileName != null && !fileName.isEmpty()) {
                    try {
                        bag.removeBagFile(fileName);
                        ApplicationContextUtil.addConsoleMessage("Payload data removed: " + fileName);
                        if (node instanceof MutableTreeNode) {
                            model.removeNodeFromParent((MutableTreeNode)node);
                        } else {
                            DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(node);
                            model.removeNodeFromParent((MutableTreeNode)aNode);
                        }
                    } catch (Exception e) {                        
                        e.printStackTrace();
                        try {
                            e.printStackTrace();
                            bag.removePayloadDirectory(fileName);
                            File file = new File(bag.getRootDir(),fileName);                            
                            if(file.exists()){
                                if(file.isFile()){
                                    file.delete();                                    
                                }else{
                                    FileUtils.deleteDirectory(file);
                                }
                            }                            
                            if (node instanceof MutableTreeNode) {
                                model.removeNodeFromParent((MutableTreeNode)node);
                            } else {
                                DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(node);
                                model.removeNodeFromParent((MutableTreeNode)aNode);
                            }
                        } catch (Exception ex) {                            
                            message = "Error trying to remove: " + fileName + "\n";
                            bagView.showWarningErrorDialog("Error - file not removed", message + ex.getMessage());
                        }
                    }
                }
            }

            bagView.getBagPayloadTree().removeSelectionPaths(paths);
            bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
    	}
    }
}