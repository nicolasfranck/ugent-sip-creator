/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.panels;

import ugent.bagger.tables.StructMapTable;
import com.anearalone.mets.StructMap;
import ugent.bagger.helper.Context;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 *
 * @author nicolas
 */
public class StructMapPanel extends JPanel{
    private JComponent buttonPanel;
    private StructMapTable structMapTable;
    private ArrayList<StructMap>data;  
    private JPanel structMapView;
    
    public StructMapPanel(final ArrayList<StructMap>data){        
        assert(data != null);
        this.data = data; 
        setLayout(new BorderLayout());        
        add(createContentPane());        
    }    
    public void reset(final ArrayList<StructMap>data){                
        getStructMapTable().reset(data);
        this.data = data;        
    }
    public JPanel getStructMapView() {
        if(structMapView == null){
            structMapView = new JPanel(new BorderLayout());
        }
        return structMapView;
    }
    public void setStructMapView(JPanel structMapView) {
        this.structMapView = structMapView;
    }    
    public JTree newStructMapTree(TreeNode rootNode){        
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        JTree tree = new JTree(model);     
        return tree;
    }    
    public JComponent getButtonPanel() {
        if(buttonPanel == null){
            buttonPanel = createButtonPanel();
        }
        return buttonPanel;
    }
    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }   
    public StructMapTable getStructMapTable() {
        if(structMapTable == null){
            structMapTable = createStructMapTable();
        }
        return structMapTable;
    }
    public StructMapTable createStructMapTable(){
        return new StructMapTable(data,new String [] {"ID","label","type"},"structMapTable");
    }
    public void setStructMapTable(StructMapTable structMapTable) {
        this.structMapTable = structMapTable;
    }  
    protected JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());        
        panel.add(new JScrollPane(getStructMapTable().getControl()));        
        panel.add(getButtonPanel(),BorderLayout.NORTH);        
        JScrollPane pane = new JScrollPane(getStructMapView());
        panel.add(pane,BorderLayout.SOUTH);
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton(Context.getMessage("StructMapPanel.addButton.label"));                        
        JButton removeButton = new JButton(Context.getMessage("StructMapPanel.removeButton.label"));                  
        panel.add(addButton);                        
        panel.add(removeButton);                
        return panel;
    }
    private void monitor(SwingWorker worker,String title){
        ugent.bagger.helper.SwingUtils.monitor(worker,title,"");        
    }    
}