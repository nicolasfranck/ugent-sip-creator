/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Panels;

import Tables.DmdSecTable;
import com.anearalone.mets.MdSec;
import helper.Context;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author nicolas
 */
public class MdSecPanel extends JPanel{
    private JComponent buttonPanel;
    private DmdSecTable dmdSecTable;     
    private JTextArea console;
    private ArrayList<MdSec>data;    
    
    public MdSecPanel(final ArrayList<MdSec>data){        
        assert(data != null);
        this.data = data;         
        setLayout(new BorderLayout());
        add(createContentPane());        
    }    
    public void reset(final ArrayList<MdSec>data){                
        getDmdSecTable().reset(data);
        this.data = data;        
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
    public JTextArea getConsole() {
        if(console == null){
           console = new JTextArea();
           console.setLineWrap(false);                     
           console.setEditable(false);
        }
        return console;
    }
    public void setConsole(JTextArea console) {
        this.console = console;
    }      
    public DmdSecTable getDmdSecTable() {
        if(dmdSecTable == null){
            dmdSecTable = createMdSecTable();
        }
        return dmdSecTable;
    }
    public DmdSecTable createMdSecTable(){                
        return new DmdSecTable(data,new String [] {"ID","GROUPID","STATUS","CREATED"},"mdSecTable");
    }
    public void setMdSecTable(DmdSecTable dmdSecTable) {
        this.dmdSecTable = dmdSecTable;
    }  
    protected JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());        
        panel.add(new JScrollPane(getDmdSecTable().getControl()));        
        panel.add(getButtonPanel(),BorderLayout.NORTH);        
        JPanel consolePanel = new JPanel(new BorderLayout());     
        consolePanel.add(new JLabel("log:"));
        JScrollPane pane = new JScrollPane();
        pane.setPreferredSize(new Dimension(0,100));
        pane.setViewportView(getConsole());
        pane.setPreferredSize(new Dimension(0,80));
        consolePanel.add(pane,BorderLayout.SOUTH);        
        panel.add(consolePanel,BorderLayout.SOUTH);        
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton(Context.getMessage("MdSecPanel.addButton.label"));                
        JButton importButton = new JButton(Context.getMessage("MdSecPanel.importButton.label"));       
        importButton.setToolTipText(Context.getMessage("MdSecPanel.importButton.toolTip"));
        JButton removeButton = new JButton(Context.getMessage("MdSecPanel.removeButton.label"));  
        JButton transformButton = new JButton(Context.getMessage("MdSecPanel.transformButton.label"));
        transformButton.setToolTipText(Context.getMessage("MdSecPanel.transformButton.toolTip"));
        
        panel.add(addButton);                
        panel.add(importButton);        
        panel.add(removeButton);
        panel.add(transformButton);
        
        removeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getDmdSecTable().deleteSelectedMdSec();
            }        
        });     
        
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){                
                monitor(new Workers.TaskAddMdSecFromFile(),"importing..");
            }
        });
        importButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                monitor(new Workers.TaskAddMdSecFromImport(),"importing..");
            }
        });
        transformButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                monitor(new Workers.TaskAddMdSecFromTransform(),"transforming..");               
            }
        });
        return panel;
    }
    private void monitor(SwingWorker worker,String title){
        helper.SwingUtils.monitor(worker,title,"trying to import",getPropertyListeners());        
    }   
    private List<PropertyChangeListener> getPropertyListeners(){        
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {                
                if(pce.getPropertyName().equals("state") && pce.getNewValue() == SwingWorker.StateValue.STARTED){
                    console.setText("");
                }else if(pce.getPropertyName().equals("log")){
                    console.append(pce.getNewValue().toString());
                }else if(pce.getPropertyName().equals("send")){
                    getDmdSecTable().addMdSec((MdSec)pce.getNewValue());
                }else if(
                    pce.getPropertyName().equals("report") && 
                    pce.getNewValue().toString().compareTo("success") == 0
                ){
                    getDmdSecTable().refresh();
                }
            }
        };
        return Arrays.asList(new PropertyChangeListener [] {listener});        
    }    
}