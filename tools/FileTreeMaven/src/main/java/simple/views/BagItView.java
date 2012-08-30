/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.views;

import Filters.FileExtensionFilter;
import handlers.FileTreeTransferHandler;
import handlers.FileTreeTransferHandler.Mode;
import helper.Context;
import helper.FileSource;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.validation.Schema;
import org.springframework.richclient.application.support.AbstractView;

/**
 *
 * @author nicolas
 */
public class BagItView extends DefaultView{
    private JPanel rootPanel;
    private JPanel payloadPanel;
    private JTree payloadTree;
    private JPanel tagfilePanel;
    private JTree tagfileTree;
    private JTextArea console;    
    private HashMap xsdMap;
    private HashSet<String> xmlFiles = new HashSet<String>();
    private HashMap<String,Schema>xsdCache = new HashMap<String,Schema>();
    private TransferHandler payloadTransferHandler;
    private TransferHandler tagfileTransferHandler;
    private DefaultMutableTreeNode [] payloadNodesSelected;
    private DefaultMutableTreeNode [] tagfileNodesSelected;
        
    public TransferHandler getPayloadTransferHandler() {
        if(payloadTransferHandler == null) {
            payloadTransferHandler = new FileTreeTransferHandler();
        }
        return payloadTransferHandler;
    }

    public void setPayloadTransferHandler(TransferHandler payloadTransferHandler) {
        this.payloadTransferHandler = payloadTransferHandler;
    }

    public TransferHandler getTagfileTransferHandler() {
        if(tagfileTransferHandler == null) {
            tagfileTransferHandler = new FileTreeTransferHandler(Mode.FILES_ONLY);
        }
        return tagfileTransferHandler;
    }

    public void setTagfileTransferHandler(TransferHandler tagfileTransferHandler) {
        this.tagfileTransferHandler = tagfileTransferHandler;
    }
    private JPanel constructButtonPanel(final JButton [] buttons){
       JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
       for(JButton button:buttons) {
            panel.add(button);
        }
       return panel;
    }

    private JPanel constructPayloadButtonPanel(final JTree tree){

        JButton addButton = new JButton(Context.getMessage("add"));
        JButton removeButton = new JButton(Context.getMessage("remove"));        
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                File [] files = selectPayloads();
                for(int i = 0;i<files.length;i++){
                    logger.debug("file: "+files[i].getAbsolutePath());
                    DefaultMutableTreeNode node = helper.FileUtils.toTreeNode(new FileSource(files[i]));
                    DefaultMutableTreeNode simularNode = helper.TreeUtils.findSimularNode(tree,node);
                    if(simularNode != null){
                        logger.debug("removing old child");
                        rootNode.remove(simularNode);
                    }
                    rootNode.add(node);
                }
                ((DefaultTreeModel)tree.getModel()).reload();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(payloadNodesSelected != null){
                    logger.debug("payloadNodes found: "+payloadNodesSelected.length);
                    for(DefaultMutableTreeNode node:payloadNodesSelected){
                        logger.debug("removing node: "+node);
                        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                        parent.remove(node);
                    }
                    payloadNodesSelected = null;
                    
                }
                ((DefaultTreeModel)tree.getModel()).reload();
            }
        });
        return constructButtonPanel(new JButton [] {addButton,removeButton});
    }
    private JPanel constructTagfileButtonPanel(final JTree tree){

        JButton addButton = new JButton(Context.getMessage("add"));
        JButton removeButton = new JButton(Context.getMessage("remove"));
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                logger.debug("constructTagFileButtonPanel: actionPerformed");
                File [] files = selectTagFiles();
                for(int i = 0;i<files.length;i++){
                    logger.debug("file: "+files[i].getAbsolutePath());
                    DefaultMutableTreeNode node = helper.FileUtils.toTreeNode(new FileSource(files[i]));
                    DefaultMutableTreeNode simularNode = helper.TreeUtils.findSimularNode(tree,node);
                    if(simularNode != null){
                        logger.debug("removing old child");
                        rootNode.remove(simularNode);
                    }
                    rootNode.add(node);
                }
                ((DefaultTreeModel)tree.getModel()).reload();
            }
        });
        
        return constructButtonPanel(new JButton [] {addButton,removeButton});
    }
    
    public JTextArea getConsole() {
        if(console == null){
            console = new JTextArea();
            console.setEditable(false);
            console.setColumns(100);
            console.setRows(50);
        }
        return console;
    }
    public void setConsole(JTextArea console) {
        this.console = console;
    }
    public JTree getPayloadTree(){
        if(payloadTree == null){
            final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("data");
            rootNode.setAllowsChildren(true);
            payloadTree = new JTree(new DefaultTreeModel(rootNode));
            payloadTree.setShowsRootHandles(true);
            payloadTree.setRootVisible(false);
            payloadTree.setDragEnabled(true);
            payloadTree.setDropMode(DropMode.ON_OR_INSERT);
            payloadTree.setTransferHandler(getPayloadTransferHandler());
            payloadTree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent tse){
                    TreePath [] selectedPaths = payloadTree.getSelectionPaths();
                    payloadNodesSelected = new DefaultMutableTreeNode[selectedPaths.length];
                    
                    for(int i = 0;i<selectedPaths.length;i++){
                        TreePath path = selectedPaths[i];
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        logger.debug("adding node "+node+" to black list");
                        payloadNodesSelected[i] = node;
                    }
                    
                }
            });
        }
        return payloadTree;
    }  
    public void setPayloadTree(JTree payloadTree){
        this.payloadTree = payloadTree;
    }
    public JTree getTagfileTree() {
        if(tagfileTree == null){
            final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("data");
            rootNode.setAllowsChildren(true);
            tagfileTree = new JTree(new DefaultTreeModel(rootNode));
            tagfileTree.setShowsRootHandles(true);
            tagfileTree.setRootVisible(false);
            tagfileTree.setDragEnabled(true);
            tagfileTree.setDropMode(DropMode.ON_OR_INSERT);
            tagfileTree.setTransferHandler(getTagfileTransferHandler());
        }
        return tagfileTree;
    }
    public void setTagfileTree(JTree tagfileTree) {
        this.tagfileTree = tagfileTree;
    }
    public HashSet<String> getXmlFiles() {
        return xmlFiles;
    }
    public void setXmlFiles(HashSet<String> xmlFiles) {
        this.xmlFiles = xmlFiles;
    }
    public HashMap<String, Schema> getXsdCache() {
        return xsdCache;
    }
    public void setXsdCache(HashMap<String, Schema> xsdCache) {
        this.xsdCache = xsdCache;
    }
    public HashMap getXsdMap() {
        return xsdMap;
    }
    public void setXsdMap(HashMap xsdMap) {
        this.xsdMap = xsdMap;
    }
    public JPanel getPayloadPanel() {
        if(payloadPanel == null){
            payloadPanel = new JPanel(new BorderLayout());
            payloadPanel.add(new JScrollPane(getPayloadTree()));
        }
        return payloadPanel;
    }
    public void setPayloadPanel(JPanel payloadPanel) {
        this.payloadPanel = payloadPanel;
    }
    public JPanel getRootPanel(){
        if(rootPanel == null)
            rootPanel = new JPanel(new GridLayout(0,1));
        return rootPanel;
    }
    public void setRootPanel(JPanel rootPanel) {
        this.rootPanel = rootPanel;
    }
    public JPanel getTagfilePanel() {
        if(tagfilePanel == null){
            tagfilePanel = new JPanel(new BorderLayout());
            tagfilePanel.add(new JScrollPane(getTagfileTree()));
        }
        return tagfilePanel;
    }
    public void setTagfilePanel(JPanel tagfilePanel) {
        this.tagfilePanel = tagfilePanel;
    }   
    public File [] selectPayloads(){
        JFileChooser fileSelector = new JFileChooser();
        fileSelector.setDialogTitle("payloads");
        fileSelector.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileSelector.setMultiSelectionEnabled(true);
        return selectFilesFrom(fileSelector);
    }
    public File [] selectTagFiles(){
        logger.debug("selectTagFiles");
        JFileChooser fileSelector = new JFileChooser();
        fileSelector.setDialogTitle("Tag Files");
        fileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileSelector.setMultiSelectionEnabled(true);
        fileSelector.setFileFilter(new FileExtensionFilter(new String [] {"xml"},"xml files only",true));
        return selectFilesFrom(fileSelector);
    }
    public static File [] selectFilesFrom(JFileChooser fileSelector){
        int freturn = fileSelector.showOpenDialog(null);
        File [] files = {};
        if(freturn == JFileChooser.APPROVE_OPTION) {
            files = fileSelector.getSelectedFiles();
        }
        return files;
    }
    @Override
    protected JComponent createControl(){
        getRootPanel().add(constructPayloadButtonPanel(getPayloadTree()));
        getRootPanel().add(new JLabel("Payloads:"));
        getRootPanel().add(getPayloadPanel());
        getRootPanel().add(constructTagfileButtonPanel(getTagfileTree()));
        getRootPanel().add(new JLabel("Tag files:"));
        getRootPanel().add(getTagfilePanel());
        return getRootPanel();
    }
}
