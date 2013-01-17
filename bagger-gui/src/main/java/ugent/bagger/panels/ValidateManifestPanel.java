package ugent.bagger.panels;

import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.Manifest.Algorithm;
import gov.loc.repository.bagit.ManifestReader;
import gov.loc.repository.bagit.ManifestReader.FilenameFixity;
import gov.loc.repository.bagit.utilities.MessageDigestHelper;
import gov.loc.repository.bagit.v0_93.impl.BagPartFactoryImpl;
import gov.loc.repository.bagit.v0_96.impl.BagConstantsImpl;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.TitlePane;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.forms.ValidateManifestParamsForm;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.ValidateManifestParams;
import ugent.bagger.params.ValidateManifestResult;
import ugent.bagger.tables.ClassTable;
import ugent.bagger.workers.LongTask;

/**
 *
 * @author nicolas
 */
public class ValidateManifestPanel extends JPanel{
    static final Log log = LogFactory.getLog(ValidateManifestPanel.class);
    ValidateManifestParams validateManifestParams;
    ValidateManifestParamsForm validateManifestParamsForm;
    ClassTable<ValidateManifestResult> validateManifestResultTable;
    ArrayList<ValidateManifestResult>validateManifestResults;
    
    public ValidateManifestPanel(){
        init();
    }
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        TitlePane titlePane = new TitlePane(5);  
    	titlePane.setTitle(Context.getMessage("ValidateManifestPanel.title"));
    	titlePane.setMessage(new DefaultMessage(Context.getMessage("ValidateManifestPanel.description")));        
        JComponent titleComponent = titlePane.getControl();         
        add(titleComponent);
        
        JComponent form = getValidateManifestParamsForm().getControl();        
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(form);
        
        JComponent buttonPanel = createButtonPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(buttonPanel); 
        
        JTable table = (JTable)getValidateManifestResultTable().getControl();
        JScrollPane scroller = new JScrollPane(table);
        scroller.setPreferredSize(new Dimension(500,200));
        scroller.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(scroller);
        
        
    }
    public ValidateManifestParams getValidateManifestParams() {
        if(validateManifestParams == null){
            validateManifestParams = new ValidateManifestParams();
        }
        return validateManifestParams;
    }
    public ValidateManifestParamsForm getValidateManifestParamsForm() {
        if(validateManifestParamsForm == null){
            validateManifestParamsForm = new ValidateManifestParamsForm(getValidateManifestParams());
        }
        return validateManifestParamsForm;
    }
    public JComponent createButtonPanel(){        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton okButton = new JButton(Context.getMessage("ok"));
        JButton cancelButton = new JButton(Context.getMessage("cancel"));
        
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(getValidateManifestParamsForm().hasErrors()){
                    return;                    
                }                   
                getValidateManifestParamsForm().commit();
                SwingUtils.monitor(
                    ValidateManifestPanel.this,
                    new ValidateManifestWorker(),
                    Context.getMessage("ValidateManifestPanel.monitoring.title"),
                    Context.getMessage("ValidateManifestPanel.monitoring.description")
                );
                ValidateManifestPanel.this.firePropertyChange("ok",null,null);
            }            
        });
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                ValidateManifestPanel.this.firePropertyChange("cancel",null,null);
            }            
        });
        
        panel.add(okButton);
        panel.add(cancelButton);
        
        return panel;
    }
    public void reset(ArrayList<ValidateManifestResult>validateManifestResults){
        getValidateManifestResultTable().reset(validateManifestResults);
        this.validateManifestResults = validateManifestResults;
    }
    public void addValidateManifestResult(ValidateManifestResult result){
        getValidateManifestResults().add(result);
        getValidateManifestResultTable().reset(getValidateManifestResults());        
    }
    public ClassTable<ValidateManifestResult> getValidateManifestResultTable() {
        if(validateManifestResultTable == null){
            validateManifestResultTable  = new ClassTable<ValidateManifestResult>(
                getValidateManifestResults(),
                new String [] {"manifestFile","file","checksumFound","checksumComputed","success"},
                "validateManifestResultTable"
            );
        }
        return validateManifestResultTable;
    }
    public void setValidateManifestResultTable(ClassTable<ValidateManifestResult> validateManifestResultTable) {
        this.validateManifestResultTable = validateManifestResultTable;
    }
    public ArrayList<ValidateManifestResult> getValidateManifestResults() {
        if(validateManifestResults == null){
            validateManifestResults  = new ArrayList<ValidateManifestResult>();
        }
        return validateManifestResults;
    }
    public void setValidateManifestResults(ArrayList<ValidateManifestResult> validateManifestResults) {
        this.validateManifestResults = validateManifestResults;
    }    
    class ValidateManifestWorker extends LongTask {                
        @Override
        protected Object doInBackground() {            
            
            BusyIndicator.showAt(ValidateManifestPanel.this);            
            
            reset(new ArrayList<ValidateManifestResult>());
            
            BagFactory bagFactory = new BagFactory();            
            BagPartFactoryImpl bagPartFactory = new BagPartFactoryImpl(bagFactory,new BagConstantsImpl());
            
            int i = 0;                        
            for(File manifestFile:getValidateManifestParams().getFiles()){
                try{                    
                    File baseDir = manifestFile.getParentFile();    
                    
                    Algorithm algorithm = Algorithm.MD5;
                    
                    String manifestFileLowerCase = manifestFile.getName().toLowerCase();
                    for(Algorithm alg:Algorithm.values()){                        
                        if(manifestFileLowerCase.contains(alg.bagItAlgorithm)){                            
                            algorithm = alg;
                            break;
                        }
                    }
                    
                    ManifestReader manifestReader = bagPartFactory.createManifestReader(new FileInputStream(manifestFile),"UTF-8");
                    while(manifestReader.hasNext()){
                        FilenameFixity fixity = manifestReader.next();                            
                        File childFile = new File(baseDir,fixity.getFilename());                                                                        
                        String checksumComputed = MessageDigestHelper.generateFixity(childFile,algorithm);                            
                        addValidateManifestResult(new ValidateManifestResult(manifestFile,childFile,fixity.getFixityValue(),checksumComputed));
                    }  
                    
                    int percent = (int)Math.floor( ((i+1) / ((float)getValidateManifestParams().getFiles().size()))*100);
                    setProgress(percent);
                }catch(FileNotFoundException e){
                    log.error(e.getMessage());
                }
                int percent = (int)Math.floor( ((++i) / ((float)getValidateManifestParams().getFiles().size()))*100);                    
                setProgress(percent);                                
            }            
            
            BusyIndicator.clearAt(ValidateManifestPanel.this);
            return null;            
        }
    }
}