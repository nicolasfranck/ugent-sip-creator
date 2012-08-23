/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Forms;

import com.anearalone.mets.MetsHdr;
import com.anearalone.mets.MetsHdr.Agent;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 *
 * @author nicolas
 */
public class MetsAgentForm extends AbstractForm{
    
    public MetsAgentForm(Agent agent){
        this(FormModelHelper.createFormModel(agent,"metsAgentForm"));
    }
    public MetsAgentForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();

        TableFormBuilder builder = new TableFormBuilder(bf);
        
        builder.add("name","colSpan=1 align=left");
        builder.row();
        
        Binding agentTypeBinding = bf.createBoundComboBox("AGENTTYPE",Agent.AGENTTYPE.values());
        builder.add(agentTypeBinding,"colSpan=1 align=left");        
        builder.row();
        
        Binding roleBinding = bf.createBoundComboBox("ROLE",Agent.ROLE.values());
        builder.add(roleBinding,"colSpan=1 align=left");                
        builder.row();
        
        final JComponent [] componentsOtherRole = builder.add("OTHERROLE","colSpan=1 align=left");
        helper.SwingUtils.setJComponentsEnabled(componentsOtherRole,false);
        builder.row();
        final JComponent [] componentsOtherType = builder.add("OTHERTYPE","colSpan=1 align=left");
        helper.SwingUtils.setJComponentsEnabled(componentsOtherType,false);
        builder.row();        
        
        getFormModel().getValueModel("ROLE").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                MetsHdr.Agent.ROLE role = (MetsHdr.Agent.ROLE) pce.getNewValue();     
                boolean enabled = false;
                if(role == MetsHdr.Agent.ROLE.OTHER){
                    enabled = true;                    
                }else{
                    enabled = false;
                }
                helper.SwingUtils.setJComponentsEnabled(componentsOtherRole,enabled);
                helper.SwingUtils.setJComponentsEnabled(componentsOtherRole,enabled);
            }
        });

        return builder.getForm();
    }
}
