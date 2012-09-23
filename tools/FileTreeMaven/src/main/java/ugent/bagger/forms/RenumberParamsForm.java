package ugent.bagger.forms;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.bindings.JSpinnerNumberBinding;
import ugent.bagger.helper.Context;
import ugent.bagger.params.RenumberParams;
import ugent.rename.ErrorAction;
import ugent.rename.PaddingChar;
import ugent.rename.StartPosRelative;
import ugent.rename.StartPosType;

/**
 *
 * @author nicolas
 */
public class RenumberParamsForm extends AbstractForm{
    
    public RenumberParamsForm(RenumberParams renumberParams){
        this(FormModelHelper.createFormModel(renumberParams,"renumberForm"));
    }
    public RenumberParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        RenumberParams renumberParams = (RenumberParams)getFormObject();
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");
        
        builder.addSeparator(Context.getMessage("renumberFiles"));
        builder.row();
                   
        builder.add(new JSpinnerNumberBinding(
            getFormModel(),"start",renumberParams.getStart(),0,Integer.MAX_VALUE,1
        ));              
        builder.row();
        
        builder.add(new JSpinnerNumberBinding(
            getFormModel(),"end",renumberParams.getEnd(),0,Integer.MAX_VALUE,1
        ));              
        builder.row();
        
        builder.add(new JSpinnerNumberBinding(
            getFormModel(),"step",renumberParams.getStep(),1,Integer.MAX_VALUE,1
        ));              
        builder.row();
        
        /*
        Binding bindingStartPosType = bf.createBoundComboBox("startPosType",StartPosType.values());        
        builder.add(bindingStartPosType);
        builder.row();*/
        
        final JComponent [] startPosComponents = builder.add(new JSpinnerNumberBinding(
            getFormModel(),"startPos",renumberParams.getStartPos(),0,Integer.MAX_VALUE,1
        ));              
        builder.row();
        
        /*
        final Binding startPosRelativeBinding = bf.createBoundComboBox("startPosRelative",StartPosRelative.values());
        builder.add(startPosRelativeBinding); 
        builder.row();
        
        getFormModel().getValueModel("startPosType").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                StartPosType startPosType = (StartPosType)pce.getNewValue();                
                if(startPosType.equals(StartPosType.ABSOLUTE)){
                    startPosRelativeBinding.getControl().setEnabled(false);
                    startPosComponents[1].setEnabled(true);
                }else{
                    startPosRelativeBinding.getControl().setEnabled(true);
                    startPosComponents[1].setEnabled(false);
                }
            }            
        });*/
                
        
        builder.add(new JSpinnerNumberBinding(
            getFormModel(),"padding",renumberParams.getPadding(),0,Integer.MAX_VALUE,1
        ));              
        builder.row();
              
        //paddingChar
        Binding paddingCharBinding = bf.createBoundComboBox("paddingChar",PaddingChar.values());        
        builder.add(paddingCharBinding);
        builder.row();
        
        //overWrite
        builder.add("overWrite");
        builder.row();
        
        //onError
        Binding b = bf.createBoundComboBox("onErrorAction",ErrorAction.values());
        final JComboBox comboBox = ((JComboBox)b.getControl());
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                comboBox.setEditable(false)                ;
                comboBox.setSelectedItem(ErrorAction.ignore);
            }        
        });
        builder.add(b);
        builder.row();
        
        //relations        
        getFormModel().getValueModel("start").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {               
                Integer oldValue = (Integer)pce.getOldValue();
                Integer newValue = (Integer)pce.getNewValue();
                Integer endValue = (Integer)getValueModel("end").getValue();                
                if(newValue.intValue() > endValue.intValue()){                    
                    getValueModel("start").setValue(oldValue);
                }
            }                        
        });
        
        getFormModel().getValueModel("end").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce){                
                Integer oldValue = (Integer)pce.getOldValue();
                Integer newValue = (Integer)pce.getNewValue();
                Integer startValue = (Integer)getValueModel("start").getValue();                
                if(newValue.intValue() < startValue.intValue()){                    
                    getValueModel("end").setValue(oldValue);
                }
            }                        
        });        
        getFormModel().commit();       
        return builder.getForm();
    }
}