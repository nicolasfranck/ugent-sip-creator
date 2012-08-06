/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bindings;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.AbstractBinding;

/**
 *
 * @author nicolas
 */
public class JTableBinding extends AbstractBinding{
    private JTable table;
    
    public JTableBinding(FormModel formModel,String property,JTable table){
        super(formModel,property,String.class);      
    }
    @Override
    protected JComponent doBindControl(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(table);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(buttonPanel);
        JButton addButton = new JButton("add");
        JButton removeButton = new JButton("remove");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                ((DefaultTableModel)table.getModel()).insertRow(
                    table.getRowCount(),new Object [table.getColumnCount()]
                );
            }

        });
        removeButton.addActionListener(new ActionListener(){

        });

        return panel;
    }
    @Override
    protected void readOnlyChanged() {        
    }
    @Override
    protected void enabledChanged() {        
    }
    public JTable getTable() {
        return table;
    }
    public void setTable(JTable table) {
        this.table = table;
    }
    
}
