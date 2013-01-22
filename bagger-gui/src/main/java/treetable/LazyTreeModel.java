package treetable;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * adrian.tarau
 * http://stackoverflow.com/questions/1974670/java-dynamic-jtree
 */
public abstract class LazyTreeModel extends DefaultTreeModel implements TreeWillExpandListener {
    static final Log log = LogFactory.getLog(LazyTreeModel.class);

    public LazyTreeModel(TreeNode root, JTree tree) {
        super(root);
        setAsksAllowsChildren(true);
        tree.addTreeWillExpandListener(this);
        tree.setModel(this);
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        LazyTreeNode node = (LazyTreeNode) event.getPath().getLastPathComponent();
        if (node.isLoaded()) {
            return;
        }
        setLoading(node,false);        
        new LoadNodesWorker(node).run();
    }

    public void reloadNode(String id) {
        LazyTreeNode node = findNode(id);
        if (node != null) {
            node.setLoaded(false);
            setLoading(node, true);            
            new LoadNodesWorker(node).run();
        }
    }

    public void reloadParentNode(String id) {
        LazyTreeNode node = findParent(id);
        if (node != null) {
            node.setLoaded(false);
            setLoading(node, true);            
            new LoadNodesWorker(node).run();
        }
    }

    public LazyTreeNode findParent(String id) {
        LazyTreeNode node = findNode(id);
        if (node != null && node.getParent() != null) {
            return (LazyTreeNode) node.getParent();
        }
        return null;
    }

    public void loadFirstLevel() {
        setLoading((LazyTreeNode) getRoot(), false);
        new LoadNodesWorker((LazyTreeNode) getRoot()).run();        
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
    }

    protected void setChildren(LazyTreeNode parentNode, LazyTreeNode... nodes) {
        if (nodes == null) {
            return;
        }
        int childCount = parentNode.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                removeNodeFromParent((MutableTreeNode) parentNode.getChildAt(0));
            }
        }
        for (int i = 0; i < nodes.length; i++) {
            insertNodeInto(nodes[i], parentNode, i);
        }
    }

    void setLoading2(final LazyTreeNode parentNode, final boolean reload) {
        if (reload) {
            setChildren(parentNode, createReloadingNode());
        } else {
            setChildren(parentNode, createLoadingNode());
        }
    }

    void setLoading(final LazyTreeNode parentNode, final boolean reload) {
        if (SwingUtilities.isEventDispatchThread()) {
            setLoading2(parentNode, reload);
        }else{
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        setLoading2(parentNode, reload);
                    }
                });
            } catch (Exception e) {
                log.error(e.getMessage());                
            }            
        }
    }

    LazyTreeNode findNode(String id) {
        return findNode(id, (LazyTreeNode) getRoot());
    }

    LazyTreeNode findNode(String id, LazyTreeNode parent) {
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            LazyTreeNode node = (LazyTreeNode) parent.getChildAt(i);
            if (id.equals(node.getId())) {
                return node;
            }
            if (node.isLoaded()) {
                node = findNode(id, node);
                if (node != null) {
                    return node;
                }
            }
        }
        return null;
    }

    public abstract LazyTreeNode[] loadChildren(LazyTreeNode parentNode);       
    

    protected LazyTreeNode createLoadingNode() {
        return new LazyTreeNode(null, "Loading...", false);
    }

    protected LazyTreeNode createReloadingNode() {
        return new LazyTreeNode(null, "Refreshing...", false);
    }

    class LoadNodesWorker implements Runnable {

        LazyTreeNode parentNode;
        LoadNodesWorker(LazyTreeNode parent) {
            this.parentNode = parent;
        }
        public LazyTreeNode getParentNode() {
            return parentNode;
        }
        public String getName() {
            return "Lazy loading of node " + parentNode.getId();
        }
        @Override
        public void run() {
            final LazyTreeNode[] treeNodes = loadChildren(getParentNode());
            if (treeNodes == null) {
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    getParentNode().setLoaded(true);
                    setChildren(getParentNode(), treeNodes);
                }
            });
        }
    }
}