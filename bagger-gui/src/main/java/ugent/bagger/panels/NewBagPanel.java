package ugent.bagger.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.TitlePane;
import ugent.bagger.forms.NewBagParamsForm;
import ugent.bagger.helper.Context;
import ugent.bagger.params.NewBagParams;

/**
 *
 * @author nicolas
 */
public class NewBagPanel extends JPanel{
    private NewBagParams newBagParams;
    private NewBagParamsForm newBagParamsForm;    

    public NewBagPanel(){
        init();
    }
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        
        TitlePane titlePane = new TitlePane();  
    	titlePane.setTitle(Context.getMessage("NewBagFrame.title"));
    	titlePane.setMessage(new DefaultMessage(Context.getMessage("NewBagFrame.description")));
        
        add(titlePane.getControl());
    	add(new JSeparator(), BorderLayout.SOUTH);
    	
        JComponent form = getNewBagParamsForm().getControl();
        form.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(form);
        add(createButtonPanel());
    }
    public NewBagParams getNewBagParams() {
        if(newBagParams == null){
            newBagParams = new NewBagParams();
        }
        return newBagParams;
    }
    public NewBagParamsForm getNewBagParamsForm() {
        if(newBagParamsForm == null){
            newBagParamsForm = new NewBagParamsForm(getNewBagParams());
        }
        return newBagParamsForm;
    }
    public JComponent createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton okButton = new JButton("ok");
        JButton cancelButton = new JButton("cancel");
        
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                NewBagPanel.this.firePropertyChange("ok",null,null);
            }            
        });
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                NewBagPanel.this.firePropertyChange("cancel",null,null);
            }            
        });
        
        panel.add(okButton);
        panel.add(cancelButton);
        
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        return panel;
    }
}
