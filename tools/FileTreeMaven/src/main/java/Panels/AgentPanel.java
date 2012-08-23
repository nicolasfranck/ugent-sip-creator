/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Panels;

import Bindings.AgentTable;
import Forms.AgentForm;
import com.anearalone.mets.MetsHdr.Agent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author nicolas
 */
public class AgentPanel extends JPanel{
    private JComponent buttonPanel;
    private AgentTable agentTable; 
    private ArrayList<Agent>data;   
    private AgentForm agentForm;
    
    public AgentPanel(ArrayList<Agent>data){        
        assert(data != null);
        this.data = data; 
        setLayout(new BorderLayout());
        add(createContentPane());        
    }

    public AgentForm getAgentForm() {
        if(agentForm == null){
            agentForm = new AgentForm(new Agent(Agent.ROLE.ARCHIVIST,"nieuwe agent"));
        }
        return agentForm;
    }
    public void setAgentForm(AgentForm agentForm) {
        this.agentForm = agentForm;
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
     
    public AgentTable getAgentTable() {
        if(agentTable == null){
            agentTable = createAgentTable();
        }
        return agentTable;
    }
    public AgentTable createAgentTable(){
        return new AgentTable(data,new String [] {"name","ROLE","AGENTTYPE"},"agentTable");
    }
    public void setAgentTable(AgentTable agentTable) {
        this.agentTable = agentTable;
    }  
    protected JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout()); 
        panel.add(getButtonPanel(),BorderLayout.NORTH); 
        panel.add(new JScrollPane(getAgentTable().getControl()));
        panel.add(getAgentForm().getControl());
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("add");        
        JButton removeButton = new JButton("update");       
        
        panel.add(addButton);
        panel.add(removeButton);
        
        removeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getAgentTable().deleteSelectedAgent();
            }        
        });     
        
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
               
                
                
            }
        });
        return panel;
    }  
   
}
