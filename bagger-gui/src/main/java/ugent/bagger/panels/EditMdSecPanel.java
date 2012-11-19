package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import ugent.bagger.forms.MdSecForm;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.XML;

/**
 *
 * @author nicolas
 */
final public class EditMdSecPanel extends JPanel{
    private JComponent buttonPanel;    
    private MdSecForm mdSecForm;
    private MdSec mdSec;    
    private JTextArea textArea;    
    
    public EditMdSecPanel(final MdSec mdSec){        
        assert(mdSec != null);
        this.mdSec = mdSec;         
        setLayout(new BorderLayout());
        add(createContentPane());                
    }  

    public JTextArea getTextArea() {
        if(textArea == null){
            textArea = new JTextArea();       
            textArea.setEditable(false);
            try{
                String [] data = new String [] {
                    XML.NodeToXML(getMdSec().getMdWrap().getXmlData().get(0),true) 
                };            
                for(String entry:data){
                    textArea.append(entry);
                }        
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return textArea;
    }
    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }    
    public MdSec getMdSec() {
        return mdSec;
    }
    public void setMdSec(MdSec mdSec) {
        this.mdSec = mdSec;
    }
    public MdSecForm getMdSecForm(){
        if(mdSecForm == null){
            mdSecForm = new MdSecForm(getMdSec());
        }
        return mdSecForm;
    }
    public void setMdSecForm(MdSecForm mdSecForm) {
        this.mdSecForm = mdSecForm;
    }      
    public void reset(final MdSec mdSec){
        getMdSecForm().setFormObject(mdSec);        
        this.mdSec = mdSec;        
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
    protected JComponent createContentPane() {
        final JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);        
        
        JPanel panel = new JPanel(new BorderLayout());                                
                          
        panel.add(getButtonPanel(),BorderLayout.NORTH);                         
        panel.add(getMdSecForm().getControl(),BorderLayout.CENTER);     
        
        final int w = 10;
        panel.setBorder(BorderFactory.createEmptyBorder(w,w,w,w));
        
        splitter.setTopComponent(panel);
        splitter.setBottomComponent(new JScrollPane(getTextArea()));        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                splitter.setDividerLocation(0.5);
            }
        });
        
        return splitter;
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));                
        
        final JButton saveButton = new JButton(Context.getMessage("EditMdSecPanel.saveButton.label"));
        saveButton.setEnabled(true);               
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(!getMdSecForm().hasErrors()){
                    getMdSecForm().commit();                    
                    firePropertyChange("save",null,null);
                }                
            }
        });              
        getMdSecForm().addValidationListener(new ValidationListener(){
            @Override
            public void validationResultsChanged(ValidationResults results) {
                saveButton.setEnabled(!results.getHasErrors());
            }
        });      
        panel.add(saveButton);        
        return panel;
    }     
}