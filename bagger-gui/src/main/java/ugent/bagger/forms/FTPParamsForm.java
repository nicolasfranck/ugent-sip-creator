package ugent.bagger.forms;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.bindings.JSpinnerNumberBinding;
import ugent.bagger.params.FTPParams;

/**
 *
 * @author nicolas
 */
public class FTPParamsForm extends AbstractForm{
    
    public FTPParamsForm(FTPParams ftpParams){
        this(FormModelHelper.createFormModel(ftpParams,"ftpParamsForm"));
    }
    public FTPParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {
        FTPParams ftpParams = (FTPParams)getFormObject();
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");        
        
        builder.add("host");
        builder.row();        
        builder.add(new JSpinnerNumberBinding(
            getFormModel(),"port",ftpParams.getPort(),1,65536,1
        ));         
        builder.row();
        
        builder.add("userName");
        builder.row();
        builder.addPasswordField("password");
        builder.row();
        
        builder.add("passiveMode");
        builder.row();
        
        Binding protocolBinding = bf.createBoundComboBox("protocol",FTPParams.FTPS_PROTOCOL.values());  
        ((JComboBox)protocolBinding.getControl()).setSelectedItem(FTPParams.FTPS_PROTOCOL.NONE);
        
        builder.add(protocolBinding);
        builder.row();
        
        return builder.getForm();
    }
}