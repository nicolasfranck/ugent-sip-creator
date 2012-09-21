package gov.loc.repository.bagger.ui.handlers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BagTreeTransferHandler extends TransferHandler {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(BagTreeTransferHandler.class);
    private static DataFlavor uriListFlavor = new DataFlavor("text/uri-list;class=java.lang.String", null);
    private static boolean debugImport = true;
    DataFlavor nodesFlavor;
    DataFlavor[] flavors = new DataFlavor[1];
    private boolean isPayload;
    private DefaultMutableTreeNode[] nodesToRemove;

    /*
     * Nicolas Franck
     */
    private DataFlavor currentFlavor;

    public BagTreeTransferHandler(boolean isPayload) {
    	super();
    	this.isPayload = isPayload;
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=\""+javax.swing.tree.DefaultMutableTreeNode[].class.getName()+"\"";           
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch(ClassNotFoundException e) {
            log.error("ClassNotFound: " + e.getMessage());
        }
    }

    private void display(String s) {
    	String msg = "BagTreeTransferHandler." + s;
    	log.info(msg);
    }

    @Override
    public int getSourceActions(JComponent c) {
    	return TransferHandler.COPY;
    }
    /*
     * Nicolas Franck: vreemd, canImport meent allerlei imports aan te kunnen, maar hier worden enkel strings geïmporteerd..
     * => test hieronder
     */
    
    @Override
    public boolean importData(JComponent comp,Transferable t){
        if(!(comp instanceof JTree))return false;
        else if (this.currentFlavor == null)return false;

        JTree tree = (JTree)comp;              
        System.out.println("\nIMPORTING!!!!!!\n");
        System.out.println("current flavor: "+this.currentFlavor);

        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        JTree.DropLocation dropLoc = (JTree.DropLocation) tree.getDropLocation();
        TreePath dropPath = dropLoc.getPath();
        DefaultMutableTreeNode receivingNode = (DefaultMutableTreeNode) dropPath.getLastPathComponent();
        
        /*
         * Nicolas Franck
         */
        try{
            DefaultMutableTreeNode [] newNodes = null;
            if(this.currentFlavor.equals(nodesFlavor)){
                newNodes = (DefaultMutableTreeNode []) t.getTransferData(nodesFlavor);
            }           
            
            if(newNodes != null){
                for(DefaultMutableTreeNode node:newNodes){
                    receivingNode.add(node);
                }                
                model.reload();
                System.out.println(newNodes);
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }

        /*
        try{
            String n = (String) t.getTransferData(DataFlavor.stringFlavor);
            root.add(new DefaultMutableTreeNode(n));
            model.reload();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }*/

        return false;
    }

    
/*
    @Override
    public boolean importData(TransferSupport ts) {
        return super.importData(ts);
    }
 * 
 */

    @Override
    public boolean canImport(JComponent comp, DataFlavor transferFlavors[]){       

        for (int i = 0; i < transferFlavors.length; i++) {
            System.out.println(transferFlavors[i]);

            Class representationclass = transferFlavors[i].getRepresentationClass();
            // URL from Explorer or Firefox, KDE
            if ((representationclass != null) && URL.class.isAssignableFrom(representationclass)) {
                    if(debugImport){
                        display("canImport accepted " + transferFlavors[i]);
                    }
                    /*
                     * Nicolas Franck
                     */
                    this.currentFlavor = transferFlavors[i];
                    return true;
            }
            // Drop from Windows Explorer
            if (DataFlavor.javaFileListFlavor.equals(transferFlavors[i])) {
                    if(debugImport){
                        display("canImport accepted " + transferFlavors[i]);
                    }

                    /*
                     * Nicolas Franck
                     */
                    this.currentFlavor = transferFlavors[i];

                    return true;
            }
           
            // Drop from GNOME
            if (DataFlavor.stringFlavor.equals(transferFlavors[i])) {
                if (debugImport) {
                    display("canImport accepted " + transferFlavors[i]);
                }

                /*
                 * Nicolas Franck
                 */
                this.currentFlavor = transferFlavors[i];

                return true;
            }
            if (uriListFlavor.equals(transferFlavors[i])) {
                if (debugImport) {
                    display("canImport accepted " + transferFlavors[i]);
                }
                
                /*
                 * Nicolas Franck
                 */
                this.currentFlavor = transferFlavors[i];
                return true;
            }
            /*
             * Nicolas Franck: drop van eigen file list?
             * <start>
             */
            if(nodesFlavor.equals(transferFlavors[i])){
                System.out.println("AAH! Moving nodes, are you?");
                this.currentFlavor = transferFlavors[i];
                return true;
            }
            /*
             * Nicolas Franck: drop van eigen file list?
             * <end>
             */

            if(debugImport){
                log.error("canImport " + i + " unknown import " + transferFlavors[i]);
            }
    	}
    	if (debugImport) {
    		log.error("canImport rejected");                
    	}
        /*
         * Nicolas Franck
         */
        this.currentFlavor = null;
    	return false;
        
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree)c;
       
        TreePath[] paths = tree.getSelectionPaths();
        if(paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            List<DefaultMutableTreeNode> copies = new ArrayList<DefaultMutableTreeNode>();
            List<DefaultMutableTreeNode> toRemove =	new ArrayList<DefaultMutableTreeNode>();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
            DefaultMutableTreeNode copy = copy(node);
            copies.add(copy);
            toRemove.add(node);
            for(int i = 1; i < paths.length; i++) {
                DefaultMutableTreeNode next = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                // Do not allow higher level nodes to be added to list.
                if(next.getLevel() < node.getLevel()) {
                    break;
                } else if(next.getLevel() > node.getLevel()) {  // child node
                    copy.add(copy(next));
                    // node already contains child
                } else {                                        // sibling
                    copies.add(copy(next));
                    toRemove.add(next);
                }
            }
            DefaultMutableTreeNode[] nodes = copies.toArray(new DefaultMutableTreeNode[copies.size()]);
            nodesToRemove =	toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
            System.out.println("\nand now we're moving nodes!!!!!!\n");
            return new NodesTransferable(nodes);
        }
        return null;
    }

    /** Defensive copy used in createTransferable. */
    private DefaultMutableTreeNode copy(TreeNode node) {
        return new DefaultMutableTreeNode(node);
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action){
        /*
         * Nicolas Franck: deze methode wordt uitgevoerd, maar de kopiëen worden nooit geplaatst..
         */

    //if((action & MOVE) == MOVE) {
        JTree tree = (JTree)source;
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Remove nodes saved in nodesToRemove in createTransferable.
        for(int i = 0; i < nodesToRemove.length; i++) {
            display("exportDonevnodesToRemove: " + nodesToRemove[i] );
            model.removeNodeFromParent(nodesToRemove[i]);
        }
        if (this.isPayload){
            //bagView.removeDataHandler.removeData();
        }else{
            //bagView.removeTagFileHandler.removeTagFile();
        }
    //}
    }

    public static String getCanonicalFileURL(File file) {
    	String path = file.getAbsoluteFile().getPath();
    	if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
    	}
    	// Not network path
    	if (!path.startsWith("//")) {
            if(path.startsWith("/")) {
                path = "//" + path;
            }else{
                path = "///" + path;
            }
    	}
    	return "file:" + path;
    }

    public class NodesTransferable implements Transferable {
        DefaultMutableTreeNode[] nodes;

        public NodesTransferable(DefaultMutableTreeNode[] nodes) {
            this.nodes = nodes;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if(!isDataFlavorSupported(flavor))throw new UnsupportedFlavorException(flavor);           
            return nodes;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}