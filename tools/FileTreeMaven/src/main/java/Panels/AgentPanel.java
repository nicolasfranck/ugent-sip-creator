/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Panels;

import Forms.AgentForm;
import Mets.AgentWrapper;
import Tables.AgentTable;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nicolas
 */
public class AgentPanel extends JPanel{
    private JComponent buttonPanel;
    private AgentTable agentTable; 
    private ArrayList<Agent>data;   
    private AgentForm agentForm; 
    private JButton addButton;
    private JButton updateButton;
    private JButton removeButton;
    private ActionListener removeAgentListener = new RemoveAgentListener();
    
    public AgentPanel(final ArrayList<Agent>data){        
        assert(data != null);
        this.data = data; 
        setLayout(new BorderLayout());
        add(createContentPane());        
    }
    public void reset(final ArrayList<Agent>data){             
        getAgentForm().setFormObject(newAgent());
        getAgentTable().reset(data);
        this.data = data;        
    }
    public Agent newAgent(){
        return new Agent(Agent.ROLE.ARCHIVIST,"");
    }
    public AgentForm getAgentForm() {
        if(agentForm == null){
            agentForm = new AgentForm(newAgent());
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
        final AgentTable t = new AgentTable(data,new String [] {"name","ROLE","AGENTTYPE","OTHERROLE"},"agentTable");
        
        t.getSelectionModel().addListSelectionListener(new ListSelectionListener() {            
            @Override
            public void valueChanged(ListSelectionEvent lse) {                
                if(lse.getValueIsAdjusting()) {
                    return;
                }                      
                Agent agent = t.getSelected();
                if(agent == null){
                    return;
                }
                AgentWrapper.dump(agent);
                addButton.setEnabled(false);
                updateButton.setEnabled(true);
                getAgentForm().setFormObject(agent);               
            }
        });
        return t;
    }
    public void setAgentTable(AgentTable agentTable) {
        this.agentTable = agentTable;
    }  
    protected JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(getAgentTable().getControl()),BorderLayout.NORTH);
        panel.add(getButtonPanel(),BorderLayout.CENTER);
        panel.add(getAgentForm().getControl(),BorderLayout.SOUTH);
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("add");        
        updateButton = new JButton("update");       
        removeButton = new JButton("remove");
        updateButton.setEnabled(false);
        
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(removeButton);
        
        updateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getAgentForm().commit();
                getAgentTable().refresh();
                getAgentForm().setFormObject(newAgent());
                
                updateButton.setEnabled(false);
                addButton.setEnabled(true);
            }        
        });
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){                
                getAgentForm().commit();               
                getAgentTable().addAgent((Agent)(getAgentForm().getFormObject()));                               
                getAgentForm().setFormObject(newAgent());
                getAgentTable().refresh();                
            }
        });
        removeButton.addActionListener(removeAgentListener);
        return panel;
    }  
   
    private class RemoveAgentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            updateButton.setEnabled(false);
            addButton.setEnabled(true);
            Agent agentInForm = (Agent) getAgentForm().getFormObject();
            Agent [] selectedAgents = getAgentTable().getSelections();            
            if(selectedAgents == null || selectedAgents.length == 0){
                return;
            }
            //haal agent uit form als hij in de selectie voorkomt
            for(Agent agent:selectedAgents){
                if(agent == agentInForm){
                    getAgentForm().setFormObject(newAgent());
                }
            }
            getAgentTable().deleteSelectedAgent();
            getAgentTable().refresh();
        }        
    }
}
