package ugent.bagger.panels;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.ProgressListener;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import gov.loc.repository.bagit.verify.impl.ParallelManifestChecksumVerifier;
import gov.loc.repository.bagit.verify.impl.ValidVerifierImpl;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import org.springframework.richclient.command.ActionCommandExecutor;
import ugent.bagger.forms.BagValidateParamsForm;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.BagValidateParams;
import ugent.bagger.params.BagValidationResult;
import ugent.bagger.tables.ClassTable;
import ugent.bagger.workers.DefaultWorker;

/**
 *
 * @author nicolas
 */
public final class BagValidationResultPanel extends JPanel{
    private JComponent buttonPanel;
    private ClassTable<BagValidationResult> bagValidationResultTable;         
    private ArrayList<BagValidationResult>data = new ArrayList<BagValidationResult>();
    private BagValidateParams bagValidateParams;
    private BagValidateParamsForm bagValidateParamsForm;
    
    public BagValidationResultPanel(){
        setLayout(new BorderLayout());
        add(createContentPane());        
    }          
    public void reset(final ArrayList<BagValidationResult>data){                       
        getBagValidationResultTable().reset(data);
        this.data = data;        
    }
    public void addBagValidationResult(BagValidationResult result){
        data.add(result);
        getBagValidationResultTable().refresh();
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
    public ClassTable<BagValidationResult> createBagValidationResultTable(){                        
        return new ClassTable<BagValidationResult>(data,new String [] {"file","valid","complete"},"bagValidationResultTable");
    }
    public ClassTable<BagValidationResult> getBagValidationResultTable() {
        if(bagValidationResultTable == null){
            bagValidationResultTable = createBagValidationResultTable();
            bagValidationResultTable.setDoubleClickHandler(new ActionCommandExecutor(){
                @Override
                public void execute() {
                    BagValidationResult vresult = bagValidationResultTable.getSelected();
                    if(vresult != null && vresult.getFile() != null){
                        BagView.getInstance().openBagHandler.openExistingBag(vresult.getFile());
                    }
                }                
            });
        }
        return bagValidationResultTable;
    }

    public void setBagValidationResultTable(ClassTable<BagValidationResult> bagValidationResultTable) {
        this.bagValidationResultTable = bagValidationResultTable;
    }
    protected JComponent createContentPane() {
        JPanel panel = new JPanel();        
        BoxLayout layout = new BoxLayout(panel,BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        
        JComponent form = getBagValidateParamsForm().getControl();
        panel.add(form);
        panel.add(new JScrollPane(getBagValidationResultTable().getControl()));        
        panel.add(getButtonPanel());        
        
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        return panel;
    }
    public JComponent createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        JButton okButton = new JButton("ok");
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                BagValidationResultPanel.this.firePropertyChange("ok",null,null);
            }            
        });
        JButton startButton = new JButton("start");
        startButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(!getBagValidateParamsForm().hasErrors()){
                    getBagValidateParamsForm().commit();
                    startValidate();
                }
            }            
        });
        panel.add(okButton);
        panel.add(startButton);
        return panel;
    }
    public BagValidateParams getBagValidateParams() {
        if(bagValidateParams == null){
            bagValidateParams = new BagValidateParams();
        }
        return bagValidateParams;
    }
    public void setBagValidateParams(BagValidateParams bagValidateParams) {
        this.bagValidateParams = bagValidateParams;
    }
    public BagValidateParamsForm getBagValidateParamsForm() {
        if(bagValidateParamsForm == null){
            bagValidateParamsForm = new BagValidateParamsForm(getBagValidateParams());            
        }
        return bagValidateParamsForm;
    }
    public void setBagValidateParamsForm(BagValidateParamsForm bagValidateParamsForm) {
        this.bagValidateParamsForm = bagValidateParamsForm;
    }    
    protected void startValidate(){
        SwingUtils.monitor(this,new ValidateBagsWorker(),"","%s");        
    }
    private class ValidateBagsWorker extends DefaultWorker implements ProgressListener{
        @Override
        protected Void doInBackground(){  
            SwingUtils.ShowBusy();
            
            try{
                reset(new ArrayList<BagValidationResult>());                
                
                ArrayList<File>files = getBagValidateParams().getFiles();
                for(int i = 0;i < files.size();i++){
                    MetsBag bag = new MetsBag(files.get(i),null);

                    CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
                    SimpleResult result;
                    boolean valid = false;
                    boolean complete = false;

                    if(getBagValidateParams().isValid()){                
                        ParallelManifestChecksumVerifier manifestVerifier = new ParallelManifestChecksumVerifier();
                        ValidVerifierImpl validVerifier = new ValidVerifierImpl(completeVerifier, manifestVerifier);                          
                        validVerifier.addProgressListener(this);
                        result = bag.validateBag(validVerifier);
                        valid = result.isSuccess();
                        complete = valid;
                    }else{
                        completeVerifier.addProgressListener(this);
                        result = bag.completeBag(completeVerifier);
                        valid = false;
                        complete = result.isSuccess();
                    }
                    
                    BagValidationResult vresult = new BagValidationResult(
                        files.get(i),
                        result,
                        valid,
                        complete
                    );
                    addBagValidationResult(vresult);
                    

                    int percent = (int)Math.floor( ((i+1) / ((float)files.size()))*100);                                                                        
                    setProgress(percent);                

                }
            }catch(Exception e){
                e.printStackTrace();
            }
            
            SwingUtils.ShowDone();
            return null;
        }

        @Override
        public void reportProgress(String activity, Object o, Long count, Long total) {
            if(count == null || total == null){
                return;
            }
            System.out.println("activity '"+activity+"' at "+count+"/"+total);
        }
    }
}