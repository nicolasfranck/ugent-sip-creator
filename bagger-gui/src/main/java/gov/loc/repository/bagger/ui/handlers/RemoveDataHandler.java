package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.BaggerFileEntity;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;

public class RemoveDataHandler extends AbstractAction /*implements Loggable*/{
    static final Log log = LogFactory.getLog(RemoveDataHandler.class);    

    public RemoveDataHandler() {
        super();            
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        removeData();
    }

    public void removeData() {
    	SwingUtils.ShowBusy();
        
        BagView bagView = BagView.getInstance();
    	DefaultBag bag = bagView.getBag();

    	TreePath[] paths = bagView.getBagPayloadTree().getSelectionPaths();
        
    	if (paths != null) {
            
            boolean doDelete = SwingUtils.confirm(
                Context.getMessage("RemoveDataHandler.confirm.title"),
                Context.getMessage(
                    "RemoveDataHandler.confirm.description",
                    new Object [] {paths.length}
                )
            );
            if(!doDelete){
                SwingUtils.ShowDone();
                return;
            }
            
            
            DefaultTreeModel model = (DefaultTreeModel)bagView.getBagPayloadTree().getModel();
            for (int i=0; i < paths.length; i++) {
                TreePath path = paths[i];
                Object node = path.getLastPathComponent();
                
                log.error("removeData: " + path.toString());
                log.error("removeData pathCount: " + path.getPathCount());
                
                File filePath = null;
                String fileName = null;
                if (path.getPathCount() > 0){                    
                    filePath = new File(""+path.getPathComponent(0));
                    for (int j = 1; j < path.getPathCount(); j++) {
                        filePath = new File(filePath, ""+path.getPathComponent(j));
                        log.debug("\t" + filePath);
                    }
                }
                if(filePath != null) {
                    fileName = BaggerFileEntity.normalize(filePath.getPath());
                }
                
                log.debug("removeData filePath: " + fileName); 
                
                if (fileName != null && !fileName.isEmpty()) {
                    try {
                        bag.removeBagFile(fileName);
                        
                        log.error(Context.getMessage("RemoveDataHandler.removeData.success.description",new Object []{fileName}));
                        
                        if (node instanceof MutableTreeNode) {
                            model.removeNodeFromParent((MutableTreeNode)node);
                        } else {
                            DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(node);
                            model.removeNodeFromParent((MutableTreeNode)aNode);
                        }
                    } catch (Exception e) {     
                        log.error(e.getMessage());
                        
                        try {                                                       
                            bag.removePayloadDirectory(fileName);                                                       
                            
                            if(node instanceof MutableTreeNode) {
                                model.removeNodeFromParent((MutableTreeNode)node);
                            } else {
                                DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(node);
                                model.removeNodeFromParent((MutableTreeNode)aNode);
                            }
                        }catch(Exception ex){                            
                            log.error(e.getMessage());
                            String title = Context.getMessage("RemoveDataHandler.removeData.Exception.title");
                            String message = Context.getMessage(
                                "RemoveDataHandler.removeData.Exception.description",
                                new Object [] {fileName,ex.getMessage()}
                            );
                            log.error(message);                            
                            SwingUtils.ShowError(title,message);
                        }
                    }
                }
            }

            bagView.getBagPayloadTree().removeSelectionPaths(paths);
            bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
            bagView.updateAddData();
            
            //Nicolas Franck: geen validate of complete nuttig
            bagView.validateExecutor.setEnabled(false);
            bagView.validateBagHandler.setEnabled(false);
            bagView.completeExecutor.setEnabled(false);
            bagView.completeBagHandler.setEnabled(false);            
    	}
        
        SwingUtils.ShowDone();
    }    
}