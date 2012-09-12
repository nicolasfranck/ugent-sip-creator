/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.forms;

import com.anearalone.mets.MdSec;
import javax.swing.JComponent;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
/**
 *
 * @author nicolas
 */
public class MdSecForm extends AbstractForm{
    
    public MdSecForm(MdSec mdSec){                
        this(FormModelHelper.createFormModel(mdSec,"mdSecForm"));
    }
    public MdSecForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {
        logger.debug("entered createFormControl");
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();

        logger.debug("binding factory fetched");
        TableFormBuilder builder = new TableFormBuilder(bf);
        
        builder.setLabelAttributes("colSpan=1 align=left");
        builder.addSeparator("global");
        builder.row();
        String [] fields = {"ID","GROUPID","STATUS"};
        //CREATED lukt niet want setCREATED vereist een speciale behandeling
        for(String field:fields){
            logger.debug("adding field: "+field);
            builder.add(field);
            builder.row();        
        } 
        
        /*builder.add(new JXDatePickerDateFieldBinding(new JXDatePicker(),getFormModel(),"CREATED"));
        builder.row();*/
        logger.debug("fields added");
        return builder.getForm();
    }
}