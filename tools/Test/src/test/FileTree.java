/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import RenameWandLib.FileUnit;
import RenameWandLib.RenameFilePair;
import RenameWandLib.RenameListener;
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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
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

    private int numFiles = 0;
    private int numDirectories = 0;
    
    public FileTree(String title,String path){
        this(title,new java.io.File(path));
    }
    public FileTree(String title,File file){

        //global layout
        super(title);       
        setSize(500,700);
        setResizable(false);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();


        //file tree
        final int [] stats = new int[2];
        final DefaultMutableTreeNode root = FileUtils.getTreeNode(file,true,stats);
        numFiles = stats[1];
        numDirectories = stats[0];        
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
        progress.setVisible(true);
        progress.setStringPainted(true);

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
        checkMimeTypeButton.setPreferredSize(new Dimension(40,40));
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

                        progress.setStringPainted(true);
                        
                        final int [] i = new int[] {0};

                        Thread t = new Thread(){
                            @Override
                            public void run(){
                                checkMimeType(root,new  FileNodeIteratorListener(){
                                    public void call(DefaultMutableTreeNode node, File file) {
                                        i[0]++;                    
                                        int percent = (int) Math.floor((i[0] / (double) numFiles)*100);
                                        progress.setValue(percent);

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
                                progress.setValue(0);
                                progress.setStringPainted(false);
                            }
                        };
                        t.start();
                       
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

        final JPanel renamePanel = new JPanel(layout);
        renamePanel.setEnabled(false);
        renamePanel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();
        renamePanel.add(createSeparator("Give source and target pattern"),cc.xyw(1,1,7));
        renamePanel.add(new JLabel("source pattern"),cc.xy(1,3));
        final JTextField sourcePatternField = new JTextField();
        renamePanel.add(sourcePatternField,cc.xyw(3,3,5));
        final JTextField targetPatternField = new JTextField();
        renamePanel.add(new JLabel("target pattern"),cc.xy(1,5));
        renamePanel.add(targetPatternField,cc.xyw(3,5,5));

        final JLabel errorMessageLabel = new JLabel();
        renamePanel.add(errorMessageLabel,cc.xyw(1,9,5));

        JButton renameButton = new JButton("rename");
        renameButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                //check sourcePatternString
                String sourcePatternString = sourcePatternField.getText();
                if(sourcePatternString == null || sourcePatternString.equals("")){
                    errorMessageLabel.setText("source pattern is not given");
                    sourcePatternField.requestFocus();
                    sourcePatternField.selectAll();
                    return;
                }
                String targetPatternString = targetPatternField.getText();
                if(targetPatternString == null || targetPatternString.equals("")){
                    errorMessageLabel.setText("target pattern is not given");
                    targetPatternField.requestFocus();
                    targetPatternField.selectAll();
                    return;
                }
                errorMessageLabel.setText("");

                //get selected node
                final TreePath tpath = tree.getSelectionPath();
                if(tpath == null){
                    errorMessageLabel.setText("no directory selected");
                    return;
                }
                final DefaultMutableTreeNode n = (DefaultMutableTreeNode)tpath.getLastPathComponent();
                File dir = (File)n.getUserObject();
                if(dir.isFile()){
                    errorMessageLabel.setText("selected node is not a directory");
                    return;
                }
                System.out.println("collecting nodes");
                //verzamel file objecten
                try{
                    RenameWand renamer = new RenameWand();
                    renamer.setSimulateOnly(true);
                    renamer.setCurrentDirectory(dir);
                    System.out.println("dir:"+dir.getAbsolutePath());
                    renamer.setSourcePatternString(sourcePatternString);
                    renamer.setTargetPatternString(targetPatternString);
                    renamer.setRecurseIntoSubdirectories(true);

                    List<FileUnit> files = renamer.getMatchCandidates();
                    System.out.println("num:"+files.size());
                    if(files.size() == 0)return;
                    /* perform matching */
                    files = renamer.performSourcePatternMatching(files);
                    System.out.println("num matching:"+files.size());
                    if(files.size() == 0)return;
                    renamer.evaluateTargetPattern(files.toArray(new FileUnit[files.size()]));

                    final List<FileUnit> fileUnits = files;
                    System.out.println("file units:"+fileUnits.size());

                    RenameListener rl = new RenameListenerAdapter(){
                        @Override
                        public void onRenameSuccess(RenameFilePair pair){
                            System.out.println(pair.getSource().getAbsolutePath()+" => "+pair.getTarget());
                        }
                    };

                    /*RenameListener rl = new RenameListenerAdapter(){
                        @Override
                        public void onRenameSuccess(RenameFilePair pair){
                            //zwaar: terug path opzoeken in de boomstructuur
                            Enumeration<DefaultMutableTreeNode> list = n.depthFirstEnumeration();
                            while(list.hasMoreElements()){
                                DefaultMutableTreeNode node = list.nextElement();
                                FileSource fs = (FileSource) node.getUserObject();
                                if(
                                        pair.getSource().getAbsolutePath().equals(
                                            fs.getAbsolutePath()
                                        )
                                ){
                                    fs.setTarget(pair.getTarget());
                                    fs.setLabel(pair.getTarget().getName());
                                    node.setUserObject(fs);
                                }
                            }
                        }
                    };*/

                    FileUtils.walkTree(n,new IteratorListener(){
                        public void execute(Object o) {                            
                            FileSource fileSource = (FileSource)o;
                            System.out.println("file source:"+fileSource.getAbsolutePath());
                            //zoek nu naar file in lijst 'files'                            
                            for(FileUnit fu:fileUnits){
                                if(
                                        fu.getSource().getAbsolutePath().equals(
                                            fileSource.getAbsolutePath()
                                        )
                                ){
                                    fileSource.setCopy(fu.getTarget());
                                    break;
                                }
                            }
                        }
                    });

                    
                    tree.repaint();
                    
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
       JFileChooser fchooser = new JFileChooser();
       fchooser.setDialogTitle("choose a directory");
       fchooser.setFileFilter(new FileFilter(){
            @Override
            public boolean accept(File file) {              
                return file.isDirectory();
            }
            @Override
            public String getDescription() {
                return "directories only";
            }
        });
        fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fchooser.setMultiSelectionEnabled(false);
        int freturn = fchooser.showOpenDialog(null);
        if(freturn == JFileChooser.APPROVE_OPTION){
            java.io.File file = fchooser.getSelectedFile();
            if(file != null)
                new FileTree("test",file);
        }

    }
}
