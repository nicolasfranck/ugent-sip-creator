package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Loggable;

public class RemoveTagFileHandler extends AbstractAction implements Loggable{
    private static final long serialVersionUID = 1L;   
    
    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public RemoveTagFileHandler() {
        super();        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        removeTagFile();
    }

    public void removeTagFile() {
        BagView bagView = BagView.getInstance();
    	
    	DefaultBag bag = bagView.getBag();

    	TreePath[] paths = bagView.getBagTagFileTree().getSelectionPaths();
    	if (paths != null) {
            DefaultTreeModel model = (DefaultTreeModel)bagView.getBagTagFileTree().getModel();
            for (int i=0; i < paths.length; i++) {
                TreePath path = paths[i];
                Object node = path.getLastPathComponent();
                String fileName = node.toString();
                try {
                    if (node != null) {
                        if (node instanceof MutableTreeNode) {
                            bag.removeBagFile(fileName);
                            log(Context.getMessage(
                                "RemoveTagFileHandler.removeTagFile.success.description", 
                                new Object [] {fileName}
                            ));
                            model.removeNodeFromParent((MutableTreeNode)node);
                        } else {
                            bag.removeBagFile((String)node);
                            ApplicationContextUtil.addConsoleMessage("Tag file removed: " + fileName);
                            DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(node);
                            model.removeNodeFromParent((MutableTreeNode)aNode);
                        }
                    }
                }catch (Exception e){
                    String title = Context.getMessage("RemoveTagFileHandler.removeTagFile.Exception.title");
                    String message = Context.getMessage(
                        "RemoveTagFileHandler.removeTagFile.Exception.description",
                        new Object [] {fileName,e.getMessage()}
                    );
                    log(message);                            
                    SwingUtils.ShowError(title,message);
                }
            }
            bagView.getBagTagFileTree().removeSelectionPaths(paths);
            bagView.getBagTagFileTreePanel().refresh(bagView.getBagTagFileTree());
    	}
        
    }

    @Override
    public void log(String message) {
        ApplicationContextUtil.addConsoleMessage(message);
    }
}