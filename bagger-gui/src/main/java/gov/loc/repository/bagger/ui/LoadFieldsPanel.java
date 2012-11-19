package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.ui.handlers.MdSecSourceExecutor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public final class LoadFieldsPanel extends JPanel {
    private MdSecSourceExecutor mdSecSourceExecutor = new MdSecSourceExecutor();
    
    public LoadFieldsPanel(){
        init();
    }
    public void init(){
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JButton loadFromLocalSourceButton = new JButton(Context.getMessage("LoadFieldsPanel.loadFromLocalSourceButton.label"));        
        add(loadFromLocalSourceButton);        
        
        loadFromLocalSourceButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                mdSecSourceExecutor.execute();
            }        
        });
    }        
}
