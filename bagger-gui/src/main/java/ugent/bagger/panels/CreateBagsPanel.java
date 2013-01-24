package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.utilities.namevalue.NameValueReader;
import gov.loc.repository.bagit.utilities.namevalue.impl.NameValueReaderImpl;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.TitlePane;
import org.springframework.richclient.progress.BusyIndicator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ugent.bagger.bagitmets.DefaultBagItMets;
import ugent.bagger.exceptions.DtdNoFixFoundException;
import ugent.bagger.exceptions.FileEmptyDirectoryException;
import ugent.bagger.exceptions.FileNameNotPortableException;
import ugent.bagger.exceptions.FileNotReadableException;
import ugent.bagger.exceptions.FileNotWritableException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.forms.CreateBagsParamsForm;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.helper.XML;
import ugent.bagger.params.CreateBagResult;
import ugent.bagger.params.CreateBagsParams;
import ugent.bagger.tables.ClassTable;
import ugent.bagger.workers.LongTask;

/**
 *
 * @author nicolas
 */
public final class CreateBagsPanel extends JPanel{
    static final Log log = LogFactory.getLog(CreateBagsPanel.class);
    CreateBagsParams createBagsParams;
    CreateBagsParamsForm createBagsParamsForm;
    ArrayList<CreateBagResult>createBagResults;
    ClassTable<CreateBagResult> createBagResultTable;
    JButton okButton;
    JLabel labelStatistics;

    public void reportStatistics(int total,int totalSuccess){        
        getLabelStatistics().setText(
            Context.getMessage(
                "CreateBagsPanel.labelStatistics",
                new Object [] {total,totalSuccess}
            )
        );
    }
    public JLabel getLabelStatistics() {
        if(labelStatistics == null){
            labelStatistics = new JLabel();
            labelStatistics.setAlignmentX(LEFT_ALIGNMENT);
            labelStatistics.setText(
                Context.getMessage(
                    "CreateBagsPanel.labelStatistics",
                    new Object [] {0,0}
                )
            );
        }
        return labelStatistics;
    }
    public void setLabelStatistics(JLabel labelStatistics) {
        this.labelStatistics = labelStatistics;
    }
    public CreateBagsPanel(){ 
        setLayout(new BorderLayout());
        add(createContentPane());        
    }
    public void reset(CreateBagsParams createBagsParams){             
        getCreateBagsParamsForm().setFormObject(createBagsParams);        
        this.createBagsParams = createBagsParams;           
    }
    protected JComponent createContentPane() {
                
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel,BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        
        TitlePane titlePane = new TitlePane(5);  
    	titlePane.setTitle(Context.getMessage("CreateBagsPanel.title"));
    	titlePane.setMessage(new DefaultMessage(Context.getMessage("CreateBagsPanel.description")));        
        JComponent titleComponent = titlePane.getControl();         
        panel.add(titleComponent);
        
        panel.add(new JSeparator(), BorderLayout.SOUTH); 
        
        JComponent form = getCreateBagsParamsForm().getControl();       
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(form);        
        
        JComponent buttonPanel = createButtonPanel();      
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(buttonPanel);
        
        getLabelStatistics().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(getLabelStatistics());
        
        JTable table = (JTable)getCreateBagResultTable().getControl();
        final Dimension tDimension = new Dimension(400,200);        
        JScrollPane scroller = new JScrollPane(table);
        scroller.setPreferredSize(tDimension);
        scroller.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(scroller);
        
        return panel;
    }

    public ClassTable<CreateBagResult> getCreateBagResultTable() {
        if(createBagResultTable == null){
            createBagResultTable = new ClassTable<CreateBagResult>(
                getCreateBagResults(),
                new String [] {"inputFile","outputFile","success","errorString"},
                "createBagResultTable"
            );
            createBagResultTable.setDoubleClickHandler(new ActionCommandExecutor(){
                @Override
                public void execute() {
                    CreateBagResult result = createBagResultTable.getSelected();
                    if(result != null && result.getOutputFile() != null && result.getOutputFile().exists()){
                        BusyIndicator.showAt(CreateBagsPanel.this);
                        BagView.getInstance().openBagHandler.openExistingBag(result.getOutputFile());
                        BusyIndicator.clearAt(CreateBagsPanel.this);
                    }
                }                
            });
        }
        return createBagResultTable;
    }

    public void setCreateBagResultTable(ClassTable<CreateBagResult> createBagResultTable) {
        this.createBagResultTable = createBagResultTable;
    }
    
    public CreateBagsParamsForm getCreateBagsParamsForm() {
        if(createBagsParamsForm == null){
            createBagsParamsForm = new CreateBagsParamsForm(getCreateBagsParams());
            createBagsParamsForm.addValidationListener(new ValidationListener(){
                @Override
                public void validationResultsChanged(ValidationResults vr) {
                    System.out.println("errors: "+vr.getMessages());
                    getOkButton().setEnabled(!vr.getHasErrors());                    
                }                
            });
        }
        return createBagsParamsForm;
    }
    public void setCreateBagsParamsForm(CreateBagsParamsForm createBagsParamsForm) {
        this.createBagsParamsForm = createBagsParamsForm;
    }

    public CreateBagsParams getCreateBagsParams() {
        if(createBagsParams == null){
            createBagsParams = new CreateBagsParams();
        }
        return createBagsParams;
    }

    public void setCreateBagsParams(CreateBagsParams createBagsParams) {
        this.createBagsParams = createBagsParams;
    }

    public ArrayList<CreateBagResult> getCreateBagResults() {
        if(createBagResults == null){
            createBagResults = new ArrayList<CreateBagResult>();
        }
        return createBagResults;
    }

    public void setCreateBagResults(ArrayList<CreateBagResult> createBagResults) {
        this.createBagResults = createBagResults;
    }
    protected void addCreateBagResult(CreateBagResult result){
        getCreateBagResults().add(result);
        getCreateBagResultTable().reset(getCreateBagResults());        
    }

    public JButton getOkButton() {
        if(okButton == null){
            okButton = new JButton(Context.getMessage("ok"));
            okButton.setEnabled(false);
            okButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if(getCreateBagsParamsForm().hasErrors()){
                        return;
                    }
                    getCreateBagsParamsForm().commit();
                    File outputDir = getCreateBagsParams().getOutputDir().size() > 0 ? getCreateBagsParams().getOutputDir().get(0):null;
                    //monitor
                    if(getCreateBagsParams().isBagInPlace()){
                        NewBagsInPlaceWorker worker = new NewBagsInPlaceWorker();                                          
                        SwingUtils.monitor(
                            CreateBagsPanel.this,
                            worker,
                            Context.getMessage("NewBagsDialog.NewBagsInPlaceWorker.title"),
                            Context.getMessage("NewBagsDialog.NewBagsInPlaceWorker.label")
                        );                   
                    }else if(!(outputDir != null && outputDir.isDirectory() && outputDir.canWrite()
                         && !FUtils.hasChildren(outputDir))){                
                        SwingUtils.ShowError(
                            Context.getMessage("NewBagsDialog.outputDir.browse.title"),
                            Context.getMessage("NewBagsDialog.outputDir.browse.title")
                        );
                    }else{
                        NewBagsWorker worker = new NewBagsWorker();                  
                        SwingUtils.monitor(
                            CreateBagsPanel.this,
                            worker,
                            Context.getMessage("NewBagsDialog.NewBagsWorker.title"),
                            Context.getMessage("NewBagsDialog.NewBagsWorker.label")
                        );                                   
                    }
                }

            });
        }
        return okButton;
    }
    
    public JComponent createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        panel.add(getOkButton());
        return panel;
    }    
    class NewBagsInPlaceWorker extends LongTask{
        @Override
        protected Object doInBackground() throws Exception {                      
            BusyIndicator.showAt(CreateBagsPanel.this);            
            
            try{
                int totalSuccess = 0;
                int total = getCreateBagsParams().getDirectories().size();                
               
                for(int i = 0; i< getCreateBagsParams().getDirectories().size();i++){                                                    
                    
                    File file = getCreateBagsParams().getDirectories().get(i);
                    
                    int percent = (int)Math.floor( ((i+1) / ((float)getCreateBagsParams().getDirectories().size()))*100);                    
                    ArrayList<String>errors = new ArrayList<String>();
                    
                    String error = null;
                    String errorTitle = null;
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
                        break;
                    }
                    
                    try{
                        //bestaat? leesbaar? schrijfbaar?
                        FUtils.checkFile(file,true);                                        
                        
                        //bestandsnamen cross platform overdraagbaar?
                        FUtils.checkSafeFiles(file);                        
                                               
                        //creëer bag in place
                        MetsBag metsBag = new MetsBag(null,null);
                        metsBag.setBagItMets(new DefaultBagItMets());            
                        metsBag.setFile(file);
                        Mets mets = new Mets();
                        metsBag.setMets(mets);
                        
                        //regular check
                        if(isCancelled()){
                            reportStatistics(total,totalSuccess);
                            break;
                        }

                        //zoek metadata bestanden (todo: haal die uit payload lijst)                         
                        for(String metadataPath:getCreateBagsParams().getMetadata()){
                            File metadataFile = new File(file,metadataPath);
                            if(metadataFile.exists() && metadataFile.isFile()){                                                            
                                if(metadataFile.getAbsolutePath().endsWith(".xml")){                    
                                    MdSec mdSec = MetsUtils.createMdSec(metadataFile);                                    
                                    mets.getDmdSec().add(mdSec);
                                }
                            }                           
                        }                                                                

                        //regular check
                        if(isCancelled()){
                            reportStatistics(total,totalSuccess);
                            break;
                        }

                        //voeg dc toe indien nodig, en mogelijk
                        if(getCreateBagsParams().isAddDC()){                            
                            Document dcDoc = MetsUtils.generateDCDoc(mets);
                            if(dcDoc != null){
                                mets.getDmdSec().add(MetsUtils.createMdSec(dcDoc));
                            }
                        }                    

                        //regular check
                        if(isCancelled()){
                            reportStatistics(total,totalSuccess);
                            break;
                        }

                        //schrijf naar bag-info
                        if(getCreateBagsParams().isWriteToBagInfo()){                            
                            ArrayList<Element>dcElements = new ArrayList<Element>();
                            ArrayList<Element>dcCandidates = new ArrayList<Element>();
                            MetsUtils.findDC(mets,dcElements,dcCandidates);

                            Document dcDoc = null;
                            if(dcElements.size() > 0){
                                Document doc = XML.createDocument();
                                Node node = doc.importNode(dcElements.get(0),true);
                                doc.appendChild(node);
                                dcDoc = doc;
                            }else{
                                dcDoc = MetsUtils.generateDCDoc(dcElements,dcCandidates);
                            }

                            if(dcDoc != null){                                                                
                                ByteArrayInputStream baginfoIn = new ByteArrayInputStream(MetsUtils.DCToBagInfo(dcDoc));
                                NameValueReaderImpl reader = new NameValueReaderImpl(
                                    "UTF-8",baginfoIn,"bagInfoTxt"
                                );
                                while(reader.hasNext()){
                                    NameValueReader.NameValue pair = reader.next();
                                    BagInfoField field = new BagInfoField();
                                    field.setName(pair.getName());
                                    field.setLabel(pair.getName());
                                    field.setValue(pair.getValue());
                                    field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);                                    
                                    metsBag.addField(field);
                                }
                            }                                                        
                        }                    

                        //regular check
                        if(isCancelled()){
                            reportStatistics(total,totalSuccess);
                            break;
                        }                    

                        String [] ignoreFiles = 
                            getCreateBagsParams().isKeepMetadata() ? 
                                new String [] {} : 
                                getCreateBagsParams().getMetadata();                                        

                        if(getCreateBagsParams().isKeepEmptyDirectories()){                                                                        
                            metsBag.createPreBagAddKeepFilesToEmptyFolders(file,getCreateBagsParams().getVersion(),ignoreFiles);                                                                       
                        }else{	                                                    
                            metsBag.createPreBag(file,getCreateBagsParams().getVersion(),ignoreFiles);                      
                        }
                        
                    }catch(FileNotWritableException e){
                        
                        error = Context.getMessage(
                            "CreateBagsPanel.NewBagsInPlaceWorker.FileNotWritableException",
                            new Object [] {e.getFile()}
                        );                         
                        
                    }catch(FileNotReadableException e){
                        
                        error = Context.getMessage(
                            "CreateBagsPanel.NewBagsInPlaceWorker.FileNotReadableException",
                            new Object [] {e.getFile()}
                        ); 
                        
                    }catch(FileNotFoundException e){
                        
                        error = Context.getMessage(
                            "CreateBagsPanel.NewBagsInPlaceWorker.FileNotFoundException",
                            new Object [] {file}
                        );                         
                        
                    }catch(FileNameNotPortableException e){
                        
                        //één of meerdere bestandsnamen zijn niet portabel                                                        
                        if(file.isDirectory()){
                            error = Context.getMessage(
                                "CreateBagsPanel.NewBagsInPlaceWorker.FileNameNotPortableException.directory",
                                new Object [] {file,e.getFile()}
                            );                    
                        }else{
                            error = Context.getMessage(
                                "CreateBagsPanel.NewBagsInPlaceWorker.FileNameNotPortableException.file",
                                new Object [] {file}
                            );                    
                        }
                        
                    }catch(IOException e){                                  
                        error = Context.getMessage("CreateBagsPanel.NewBagsInPlaceWorker.IOException",new Object []{
                            e.getMessage()
                        });                         

                    }catch(SAXException e){                                  
                        error = Context.getMessage("CreateBagsPanel.NewBagsInPlaceWorker.SAXException",new Object []{
                            e.getMessage()
                        });       
                        
                    }catch(ParserConfigurationException e){                                  
                        error = Context.getMessage("CreateBagsPanel.NewBagsInPlaceWorker.ParserConfigurationException",new Object []{
                            e.getMessage()
                        });                        

                    }catch(IllegalNamespaceException e){                                                      
                        error = Context.getMessage("CreateBagsPanel.NewBagsInPlaceWorker.IllegalNamespaceException",new Object []{
                            e.getNamespace()
                        });
                        
                    }catch(NoNamespaceException e){                            
                        error = Context.getMessage("CreateBagsPanel.NewBagsInPlaceWorker.NoNamespaceException");
                        
                    }catch(DtdNoFixFoundException e){                                    
                        error = Context.getMessage("CreateBagsPanel.NewBagsInPlaceWorker.DtdNoFixFoundException");
                        
                    }catch(Exception e){                                    
                        error = Context.getMessage("CreateBagsPanel.NewBagsInPlaceWorker.Exception",new Object []{
                            e.getMessage()
                        });                        
                    }
                    
                    //rapport
                    if(error != null){
                        errors.add(error);
                        log.error(error);
                        SwingUtils.ShowError(errorTitle,error);
                    }                    
                    reportStatistics(total,totalSuccess);
                    addCreateBagResult(new CreateBagResult(file,file,errors.toArray(new String [] {})));
                    if(!isDone()){
                        setProgress(percent);                
                    }
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
                        break;
                    }
                }
                
            }catch(Exception e){                                  
                log.error(e.getMessage());
            }
            
            BusyIndicator.clearAt(CreateBagsPanel.this);                
            return null;
        }               
    }
    
    class NewBagsWorker extends LongTask {        
        @Override
        protected Object doInBackground() throws Exception {
            
            BusyIndicator.showAt(CreateBagsPanel.this);           
            
            try{
                int total = getCreateBagsParams().getDirectories().size();
                int totalSuccess = 0;
                
                for(int i = 0; i< getCreateBagsParams().getDirectories().size();i++){
                    
                    File inputDir = getCreateBagsParams().getDirectories().get(i);                    
                    File out = new File(getCreateBagsParams().getOutputDir().get(0),inputDir.getName());                                        
                    ArrayList<String>errors = new ArrayList<String>();
                    int percent = (int)Math.floor( ((i+1) / ((float)getCreateBagsParams().getDirectories().size()))*100);
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
                        break;
                    }
                    
                    String error = null;
                    String errorTitle = null;
                    
                    try{
                        // -> input check
                        
                        try{               
                            //leesbaar? bestaat?
                            FUtils.checkFile(inputDir,true);                
                        }catch(FileNotWritableException e){
                            log.debug(e.getMessage());
                            //doe niets
                        }
                        
                        //bestandsnamen cross platform overdraagbaar?                
                        FUtils.checkSafeFiles(inputDir);
                        
                        //lege map verboden
                        if(inputDir.isDirectory()){
                            File [] children = inputDir.listFiles();
                            if(children == null || children.length == 0){
                                throw new FileEmptyDirectoryException(inputDir);
                            }
                        }
                        
                        /*
                         *  Lege bag:
                         *      rootDir: null, want map bestaat nog niet (opgeven File die nog niet bestaat geef IOException)                     
                         */
                        MetsBag metsBag = new MetsBag(null,getCreateBagsParams().getVersion());                    
                        metsBag.setFile(out);
                        metsBag.setBagItMets(new DefaultBagItMets());
                        Mets mets = new Mets();       
                        metsBag.setMets(mets);

                        //voeg payloads toe
                        File [] listPayloads = inputDir.listFiles();
                        if(listPayloads != null){
                            for(File file:listPayloads){
                                metsBag.addFileToPayload(file);
                            }
                        }

                        //regular check
                        if(isCancelled()){
                            reportStatistics(total,totalSuccess);
                            break;
                        }                    

                        //zoek metadata bestanden (todo: haal die uit payload lijst)                        
                        for(String metadataPath:getCreateBagsParams().getMetadata()){
                            File metadataFile = new File(inputDir,metadataPath);                       
                            if(metadataFile.exists() && metadataFile.isFile()){                            
                                //voeg toe aan mets
                                
                                if(metadataFile.getAbsolutePath().endsWith(".xml")){                    
                                    MdSec mdSec = MetsUtils.createMdSec(metadataFile);                                    
                                    mets.getDmdSec().add(mdSec);
                                }                                    
                                
                                //haal uit payload lijst
                                if(!getCreateBagsParams().isKeepMetadata()){
                                    String path = "data/"+metadataPath;                            
                                    metsBag.removeBagFile(path);
                                }                            
                            }                            
                        }                        

                        //regular check
                        if(isCancelled()){
                            reportStatistics(total,totalSuccess);
                            break;
                        }                    

                        //voeg dc toe indien nodig, en mogelijk
                        if(getCreateBagsParams().isAddDC()){                            
                            Document dcDoc = MetsUtils.generateDCDoc(mets);
                            if(dcDoc != null){
                                mets.getDmdSec().add(MetsUtils.createMdSec(dcDoc));
                            }                                                    
                        }

                        //regular check
                        if(isCancelled()){
                            reportStatistics(total,totalSuccess);
                            break;
                        }                    

                        //schrijf naar bag-info
                        if(getCreateBagsParams().isWriteToBagInfo()){
                            
                            ArrayList<Element>dcElements = new ArrayList<Element>();
                            ArrayList<Element>dcCandidates = new ArrayList<Element>();
                            MetsUtils.findDC(mets,dcElements,dcCandidates);

                            Document dcDoc = null;
                            if(dcElements.size() > 0){
                                Document doc = XML.createDocument();
                                Node node = doc.importNode(dcElements.get(0),true);
                                doc.appendChild(node);
                                dcDoc = doc;
                            }else{
                                dcDoc = MetsUtils.generateDCDoc(dcElements,dcCandidates);
                            }

                            if(dcDoc != null){                                                               

                                ByteArrayInputStream baginfoIn = new ByteArrayInputStream(MetsUtils.DCToBagInfo(dcDoc));
                                NameValueReaderImpl reader = new NameValueReaderImpl(
                                    "UTF-8",baginfoIn,"bagInfoTxt"
                                );                          
                                while(reader.hasNext()){
                                    NameValueReader.NameValue pair = reader.next();                            
                                    BagInfoField field = new BagInfoField();
                                    field.setName(pair.getName());
                                    field.setLabel(pair.getName());
                                    field.setValue(pair.getValue());
                                    field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
                                    metsBag.addField(field);
                                }
                            }                            

                        }

                        //regular check
                        if(isCancelled()){
                            reportStatistics(total,totalSuccess);
                            break;
                        }

                        //save!
                        Writer writer = new FileSystemWriter(new BagFactory());
                        boolean saveOk = metsBag.write(writer);

                        //save ok!
                        if(saveOk){
                            totalSuccess++;
                        }                        
                        
                        
                    }catch(FileNotReadableException e){
                        
                        error = Context.getMessage(
                            "CreateBagsPanel.NewBagsWorker.FileNotReadableException",
                            new Object [] {e.getFile()}
                        );                         
                        
                    }catch(FileNotFoundException e){                        
                       
                        error = Context.getMessage(
                            "CreateBagsPanel.NewBagsWorker.FileNotFoundException",
                            new Object [] {inputDir}
                        );                         
                        
                    }catch(FileNameNotPortableException e){
                        
                        //één of meerdere bestandsnamen zijn niet portabel                                                        
                        if(inputDir.isDirectory()){
                            error = Context.getMessage(
                                "CreateBagsPanel.NewBagsWorker.FileNameNotPortableException.directory",
                                new Object [] {inputDir,e.getFile()}
                            );                    
                        }else{
                            error = Context.getMessage(
                                "CreateBagsPanel.NewBagsWorker.FileNameNotPortableException.file",
                                new Object [] {inputDir}
                            );                    
                        }                                                
                        
                    }catch(FileEmptyDirectoryException e){
                        
                        error = Context.getMessage(
                            "CreateBagsPanel.NewBagsWorker.FileEmptyDirectoryException",
                            new Object [] {inputDir}
                        );                        
                        
                    }catch(IOException e){                                  
                        error = Context.getMessage("CreateBagsPanel.NewBagsWorker.IOException",new Object []{
                            e.getMessage()
                        });                         

                    }catch(SAXException e){                                  
                        error = Context.getMessage("CreateBagsPanel.NewBagsWorker.SAXException",new Object []{
                            e.getMessage()
                        });       
                        
                    }catch(ParserConfigurationException e){                                  
                        error = Context.getMessage("CreateBagsPanel.NewBagsWorker.ParserConfigurationException",new Object []{
                            e.getMessage()
                        });                        

                    }catch(IllegalNamespaceException e){                                
                        error = Context.getMessage("CreateBagsPanel.NewBagsWorker.IllegalNamespaceException",new Object []{
                            e.getNamespace()
                        });
                        
                    }catch(NoNamespaceException e){                                                 
                        error = Context.getMessage("CreateBagsPanel.NewBagsWorker.NoNamespaceException");
                        
                    }catch(DtdNoFixFoundException e){                                    
                        error = Context.getMessage("CreateBagsPanel.NewBagsWorker.DtdNoFixFoundException");
                        
                    }catch(Exception e){
                        error = Context.getMessage("CreateBagsPanel.NewBagsWorker.Exception",new Object []{
                            e.getMessage()
                        });
                    }
                    
                    //rapport                         
                    if(error != null){                         
                        errors.add(error);
                        SwingUtils.ShowError(errorTitle,error);
                        log.error(error);                                                                                
                    }                      
                    reportStatistics(total,totalSuccess);                    
                    addCreateBagResult(new CreateBagResult(inputDir,out,errors.toArray(new String [] {})));
                    if(!isDone()){
                        setProgress(percent);                
                    }
                }                
                
            }catch(Exception e){                               
                log.error(e.getMessage());
            }
        
            BusyIndicator.clearAt(CreateBagsPanel.this);
         
            return null;
        }               
    }        
}