/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import RenameWandLib.RenameFilePair;
import RenameWandLib.RenameListenerAdapter;
import RenameWandLib.RenameWand;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.medsea.mimeutil.MimeType;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


/**
 *
 * @author nicolas
 */
public class FileTree extends ExitJFrame {
    
    public FileTree(String title,String path){

        //global layout
        super(title);       
        setSize(500,700);
        setResizable(false);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();


        //file tree
        final DefaultMutableTreeNode root = FileUtils.getTreeNode(new java.io.File(path));
        final TreeModel model = new DefaultTreeModel(root,true);
        final JTree tree = new JTree(model);
        tree.setAutoscrolls(true);
        tree.setSize(500,300);
        FileTreeCellRenderer renderer = new FileTreeCellRenderer();
        tree.setCellRenderer(renderer);
        JScrollPane scroller = new JScrollPane();
        scroller.setViewportView(tree);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setPreferredSize(new Dimension(500,300));
        gbc.gridx = gbc.gridy = 0;
        gbc.gridheight = gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scroller,gbc);


        //common progressbar
        final JProgressBar progress = new JProgressBar(0,100);
        progress.setVisible(false);        
        progress.setIndeterminate(true);

        gbc.gridy = 2;
        add(progress,gbc);


        //tabs below file tree
        JTabbedPane tabs = new JTabbedPane();

        //tab 1: check mimetype
        JPanel mimeTypePanel = new JPanel();
        mimeTypePanel.setLayout(new BorderLayout());

        final DefaultTableModel tableModel = new CustomTableModel();
        final JTable fileTable = new JTable(tableModel);        
        fileTable.setSize(new Dimension(500,200));
        fileTable.setAutoCreateRowSorter(true);

        JScrollPane scrollTable = new JScrollPane();
        scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTable.setPreferredSize(new Dimension(200,200));
        scrollTable.setViewportView(fileTable);

        tableModel.addColumn("file");
        tableModel.addColumn("mimetype");
      
        TableColumnModel columnModel = fileTable.getColumnModel();
        for(int i = 0;i < tableModel.getColumnCount();i++){
            columnModel.getColumn(i).setWidth(250);
        }    

        final JButton checkMimeTypeButton = new JButton("check mimetype");
        checkMimeTypeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                checkMimeTypeButton.setText("checking..");
                checkMimeTypeButton.setEnabled(false);
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        //clear data
                        for(int i = 0;i<tableModel.getRowCount();i++){
                            tableModel.removeRow(i);
                        }
                        progress.setVisible(true);
                        progress.setIndeterminate(true);                        
                        
                        checkMimeType(root,new  FileNodeIteratorListener(){
                            public void call(DefaultMutableTreeNode node, File file) {
                                Iterator<MimeType> mimeTypes = ((FileSource)file).getMimeTypes().iterator();
                                while(mimeTypes.hasNext()){
                                    MimeType mimeType = mimeTypes.next();                                    
                                    tableModel.addRow(new String [] {
                                        file.getAbsolutePath(),
                                        mimeType.toString()
                                    });
                                }
                            }
                        });                        
                        
                        progress.setVisible(false);
                       
                        tableModel.fireTableDataChanged();                       
                        checkMimeTypeButton.setText("check mimetype");
                        checkMimeTypeButton.setEnabled(true);
                    }
                });              
            }
        });

        gbc.gridy = 1;

        mimeTypePanel.add(scrollTable,BorderLayout.NORTH);
        checkMimeTypeButton.setPreferredSize(new Dimension(100,20));
        mimeTypePanel.add(checkMimeTypeButton,BorderLayout.SOUTH);        
        tabs.addTab("mimetype",mimeTypePanel);

        //rename tab
        FormLayout layout = new FormLayout(
                "right:max(40dlu;pref), 3dlu, 70dlu, 7dlu, "
              + "right:max(40dlu;pref), 3dlu, 70dlu",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " +
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " +
                "p, 3dlu, p, 3dlu, p, 3dlu, p");

        JPanel renamePanel = new JPanel(layout);
        renamePanel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();
        renamePanel.add(createSeparator("Give source and target pattern"),cc.xyw(1,1,7));
        renamePanel.add(new JLabel("source pattern"),cc.xy(1,3));
        final JTextField sourcePatternField = new JTextField();
        renamePanel.add(sourcePatternField,cc.xyw(3,3,5));
        final JTextField targetPatternField = new JTextField();
        renamePanel.add(new JLabel("target pattern"),cc.xy(1,5));
        renamePanel.add(targetPatternField,cc.xyw(3,5,5));

        JButton renameButton = new JButton("rename");
        renameButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                //check sourcePatternString
                String sourcePatternString = sourcePatternField.getText();
                if(sourcePatternString == null || sourcePatternString.equals("")){
                    sourcePatternField.requestFocus();
                    sourcePatternField.selectAll();
                    return;
                }
                String targetPatternString = targetPatternField.getText();
                if(targetPatternString == null || targetPatternString.equals("")){
                    targetPatternField.requestFocus();
                    targetPatternField.selectAll();
                    return;
                }

                //get selected node
                TreePath tpath = tree.getSelectionPath();
                if(tpath == null)return;
                DefaultMutableTreeNode n = (DefaultMutableTreeNode)tpath.getLastPathComponent();
                File dir = (File)n.getUserObject();
                if(dir.isFile())return;
                
                //verzamel file objecten
                try{
                    RenameWand renamer = new RenameWand();
                    renamer.setSimulateOnly(true);
                    renamer.setCurrentDirectory(dir);
                    renamer.setSourcePatternString(sourcePatternString);
                    renamer.setTargetPatternString(targetPatternString);
                    renamer.setRecurseIntoSubdirectories(true);
                    renamer.setRenameListener(new RenameListenerAdapter(){
                        @Override
                        public void onRenameStart(RenameFilePair pair) {
                            System.out.println("renaming "+pair.getSource().getAbsolutePath()+" to "+pair.getTarget().getAbsolutePath());
                        }
                        @Override
                        public void onRenameSuccess(RenameFilePair pair){
                            System.out.println("yes!");
                        }
                    });
                    renamer.rename();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        renamePanel.add(renameButton,cc.xy(1,7));
        

        tabs.addTab("rename",renamePanel);

        //add tabs to contentPane
        add(tabs,gbc);        
     
        pack();
    }
    private Component createSeparator(String text) {
        return DefaultComponentFactory.getInstance().createSeparator(text);
    }
    public void checkMimeType(DefaultMutableTreeNode node,FileNodeIteratorListener il){
        int cc = node.getChildCount();
        for(int i = 0;i < cc;i++){
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) node.getChildAt(i);
            File file = (File)(n.getUserObject());
            if(!n.isLeaf()){
                checkMimeType(n,il);
            }else if(!file.isDirectory()){
                il.call(node, file);
            }
        }
    }
    public static void main(String []args){        
       new FileTree("test","/home/nicolas/testdir");
    }
}
