package ugent.bagger.panels;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import gov.loc.repository.bagit.verify.impl.ParallelManifestChecksumVerifier;
import gov.loc.repository.bagit.verify.impl.ValidVerifierImpl;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileSystemException;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.TitlePane;
import ugent.bagger.exceptions.BagFetchForbiddenException;
import ugent.bagger.exceptions.BagNoDataException;
import ugent.bagger.forms.BagValidateParamsForm;
import ugent.bagger.helper.Context;
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
    static final Log log = LogFactory.getLog(BagValidationResultPanel.class);
    
    JComponent buttonPanel;
    ClassTable<BagValidationResult> bagValidationResultTable;         
    ArrayList<BagValidationResult>data = new ArrayList<BagValidationResult>();
    BagValidateParams bagValidateParams;
    BagValidateParamsForm bagValidateParamsForm;  
    JLabel labelStatistics;

    public JLabel getLabelStatistics() {
        if(labelStatistics == null){
            labelStatistics = new JLabel();
        }
        return labelStatistics;
    }
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
            CommandGroup commandGroup = bagValidationResultTable.getPopupCommandGroup();
            
            if(commandGroup != null){
                commandGroup.add(new ActionCommand("bagValidationResultOpenCommand"){            
                    @Override
                    protected void doExecuteCommand() {                
                        BagValidationResult vresult = bagValidationResultTable.getSelected();
                        if(vresult != null && vresult.getFile() != null){
                            BagView.getInstance().openBagHandler.openExistingBag(vresult.getFile());
                        }
                    }
                });
                commandGroup.add(new ActionCommand("bagValidationResultShowErrorsCommand"){            
                    @Override
                    protected void doExecuteCommand() {
                     
                    }                    
                });                
            }
        }
        return bagValidationResultTable;
    }

    public void setBagValidationResultTable(ClassTable<BagValidationResult> bagValidationResultTable) {
        this.bagValidationResultTable = bagValidationResultTable;
    }
    protected JComponent createContentPane() {
        
        JPanel panel = new JPanel();        
        BoxLayout layout = new BoxLayout(panel,BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        
        TitlePane titlePane = new TitlePane(3);  
    	titlePane.setTitle(Context.getMessage("BagValidationResultPanel.title"));
    	titlePane.setMessage(new DefaultMessage(Context.getMessage("BagValidationResultPanel.description")));        
        JComponent titleComponent = titlePane.getControl();        
        
        panel.add(titleComponent);
        panel.add(new JSeparator(), BorderLayout.SOUTH);    
        
        JComponent form = getBagValidateParamsForm().getControl();
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(form);
        
        panel.add(getLabelStatistics());
        
        JTable table = (JTable) getBagValidationResultTable().getControl();        
        JScrollPane scrollerTable = new JScrollPane(table);
        scrollerTable.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(scrollerTable);        
        
        getButtonPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(getButtonPanel());               
        
        
        return panel;
    }
    public JComponent createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        JButton cancelButton = new JButton(Context.getMessage("BagValidationResultPanel.cancelButton.label"));
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                BagValidationResultPanel.this.firePropertyChange("cancel",null,null);
            }            
        });
        JButton startButton = new JButton(Context.getMessage("BagValidationResultPanel.startButton.label"));
        startButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(!getBagValidateParamsForm().hasErrors()){
                    getBagValidateParamsForm().commit();
                    startValidate();
                }
            }            
        });      
        JButton openButton = new JButton(Context.getMessage("BagValidationResultPanel.openButton.label"));
        openButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                BagValidationResult vresult = bagValidationResultTable.getSelected();
                if(vresult != null && vresult.getFile() != null){
                    BagView.getInstance().openBagHandler.openExistingBag(vresult.getFile());
                }
            }        
        });
        panel.add(startButton);
        panel.add(openButton);
        panel.add(cancelButton);
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
            bagValidateParamsForm.addFormValueChangeListener("files",new PropertyChangeListener(){
                @Override
                public void propertyChange(PropertyChangeEvent pce){
                    
                    //ledig result table
                    getBagValidationResultTable().reset(new ArrayList<BagValidationResult>());
                    getLabelStatistics().setText(" ");
                }            
            });
        }
        return bagValidateParamsForm;
    }
    public void setBagValidateParamsForm(BagValidateParamsForm bagValidateParamsForm) {
        this.bagValidateParamsForm = bagValidateParamsForm;
    }    
    protected void startValidate(){
        SwingUtils.monitor(
            this,
            new ValidateBagsWorker(),
            Context.getMessage("ValidateBagsWorker.validating.title"),
            Context.getMessage("ValidateBagsWorker.validating.description")
        );        
    }
    class ValidateBagsWorker extends DefaultWorker {
        @Override
        protected Void doInBackground(){            
            
            int numComplete = 0;
            int numValid = 0;
            ArrayList<Exception>loadExceptions = new ArrayList<Exception>();
            
            try{
                reset(new ArrayList<BagValidationResult>()); 
                
                ArrayList<File>files = getBagValidateParams().getFiles();
                
                log.error(Context.getMessage("ValidateBagsHandler.start",new Object []{
                    files.size()
                }));
                
                if(getBagValidateParams().isValid()){
                    log.error(Context.getMessage("ValidateBagsHandler.validate"));
                }
                
                
                for(int i = 0;i < files.size();i++){
                    
                    log.error(
                        Context.getMessage(
                            "ValidateBagsHandler.validateBag.start",
                            new Object [] {files.get(i),(i+1)}
                        )
                    );
                    
                    MetsBag bag = null;                    
                    boolean valid = false;
                    boolean complete = false;
                    SimpleResult result = null;
                    
                    //load bag
                    Exception loadException = null;
                    try{                        
                        bag = new MetsBag(files.get(i),null);
                    }catch(BagFetchForbiddenException e){
                        loadException = e;
                        log.error(Context.getMessage("ValidateBagsHandler.BagFetchForbiddenException",new String [] {e.getMessage()}));
                    }catch(FileSystemException e){
                        log.error(Context.getMessage("ValidateBagsHandler.FileSystemException",new String [] {e.getMessage()}));
                        loadException = e;
                    }catch(BagNoDataException e){
                        log.error(Context.getMessage("ValidateBagsHandler.BagNoDataException",new String [] {e.getMessage()}));
                        loadException = e;
                    }catch(Exception e){
                        log.error(Context.getMessage("ValidateBagsHandler.Exception",new String [] {e.getMessage()}));
                        loadException = e;
                    }
                    
                    if(loadException != null){
                        loadExceptions.add(loadException);                        
                    }else{

                        //validate bag                    
                        CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();

                        if(getBagValidateParams().isValid()){                
                            ParallelManifestChecksumVerifier manifestVerifier = new ParallelManifestChecksumVerifier();
                            ValidVerifierImpl validVerifier = new ValidVerifierImpl(completeVerifier, manifestVerifier);                          

                            result = bag.validateBag(validVerifier);
                            valid = result.isSuccess();
                            complete = valid;
                        }else{                        
                            result = bag.completeBag(completeVerifier);
                            valid = false;
                            complete = result.isSuccess();
                        }
                        
                    }
                    
                    BagValidationResult vresult = new BagValidationResult(
                        files.get(i),
                        result,
                        valid,
                        complete
                    );
                    addBagValidationResult(vresult);                    

                    int percent = (int)Math.floor( ((i+1) / ((float)files.size()))*100);                                                                        
                    if(!isDone()){
                       setProgress(percent);                
                    }
                    
                    //bag is volledig
                    if(complete){
                        
                        numComplete++;
                        
                        log.error(
                            Context.getMessage(
                                "ValidateBagsHandler.completeBag.success",
                                new Object []{files.get(i)}
                            )
                        );
                        
                        //'valideer' niet aangevinkt, dus validatie onbekend
                        if(!getBagValidateParams().isValid()){
                            log.error(
                                Context.getMessage(
                                    "ValidateBagsHandler.validateBag.unknown",
                                    new Object []{files.get(i)}
                                )
                            );
                        }
                        //bag is niet geldig
                        else if(!valid){
                            log.error(
                                Context.getMessage(
                                    "ValidateBagsHandler.validateBag.failed",
                                    new Object []{files.get(i)}
                                )
                            );
                        }
                        //bag is geldig
                        else{
                            
                            numValid++;
                            
                            log.error(
                                Context.getMessage(
                                    "ValidateBagsHandler.validateBag.success",
                                    new Object []{files.get(i)}
                                )
                            );
                        }
                    }
                    //bag is niet volledig
                    else if(loadException == null){                        
                        
                        log.error(
                            Context.getMessage(
                                "ValidateBagsHandler.completeBag.failed",
                                new Object []{files.get(i)}
                            )
                        );
                    }
                    
                    getLabelStatistics().setText(Context.getMessage(
                        "BagValidationResultPanel.statistics.label",new Object []{
                            files.size(),numComplete,numValid
                        }
                    ));
                    
                    
                    log.error(
                        Context.getMessage(
                            "ValidateBagsHandler.validateBag.end",
                            new Object [] {files.get(i),(i+1)}
                        )
                    );
                }
                
                log.error(Context.getMessage("ValidateBagsHandler.end",new Object []{
                    files.size()
                }));
            }catch(Exception e){
                log.error(e.getMessage());
            }            
            
            if(!loadExceptions.isEmpty()){
                String message = Context.getMessage("ValidateBagsHandler.LoadException");
                log.error(message);
                SwingUtils.ShowError(null,message);
            }
          
            return null;
        }        
    }
}