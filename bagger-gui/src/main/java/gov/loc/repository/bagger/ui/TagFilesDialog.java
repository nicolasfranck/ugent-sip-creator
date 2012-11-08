package gov.loc.repository.bagger.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author nicolas: based on gov.loc.repository.bagger.ui.TagFilesFrame
 */
public class TagFilesDialog extends JDialog implements ActionListener{
    public TagFilesDialog(final JFrame frame,boolean isModal,String title){
        super(frame,isModal);
        setTitle(title);             
    }
    public void addComponents(JTabbedPane tabs) {
    	getContentPane().removeAll();
    	getContentPane().add(tabs,BorderLayout.CENTER);
    	setPreferredSize(tabs.getPreferredSize());
    	pack();
    }    
    @Override
    public void actionPerformed(ActionEvent e) {
    	invalidate();
    	repaint();
    }
}
