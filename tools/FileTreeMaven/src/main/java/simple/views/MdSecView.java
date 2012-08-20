/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.views;

import bindings.MdSecTable;
import com.anearalone.mets.MdSec;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.springframework.richclient.application.support.AbstractView;

/**
 *
 * @author nicolas
 */
public class MdSecView extends AbstractView{
    private MdSecTable mdSecTable;
    private int lastIndex = 0;

    public MdSecTable getMdSecTable() {
        if(mdSecTable == null){
            mdSecTable = createMdSecTable();
        }
        return mdSecTable;
    }
    public MdSecTable createMdSecTable(){
        return new MdSecTable(new ArrayList<MdSec>(),new String [] {"ID","GROUPID"},"mdSecTable");
    }
    public void setMdSecTable(MdSecTable mdSecTable) {
        this.mdSecTable = mdSecTable;
    }
    @Override
    protected JComponent createControl() {
        JPanel panel = new JPanel(new BorderLayout());
        
        panel.add(new JScrollPane(getMdSecTable().getControl()));
        
        panel.add(createButtonPanel(),BorderLayout.NORTH);
        
        return panel;
    }
    public JComponent createButtonPanel(){
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("add");
        JButton removeButton = new JButton("remove");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);        
        
        removeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getMdSecTable().deleteSelectedMdSec();
            }        
        });
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                getMdSecTable().addMdSec(new MdSec(){{
                    setID(""+lastIndex);
                    setGROUPID(""+lastIndex);
                }});
                getMdSecTable().refresh();
                lastIndex++;
            }        
        });
        return buttonPanel;
    }
}
