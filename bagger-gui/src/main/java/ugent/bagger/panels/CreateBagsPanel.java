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
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.progress.BusyIndicator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ugent.bagger.bagitmets.DefaultBagItMets;
import ugent.bagger.forms.CreateBagsParamsForm;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.helper.XML;
import ugent.bagger.params.CreateBagResult;
import ugent.bagger.params.CreateBagsParams;
import ugent.bagger.tables.CreateBagResultTable;
import ugent.bagger.workers.LongTask2;

/**
 *
 * @author nicolas
 */
public final class CreateBagsPanel extends JPanel{
    private static final Log log = LogFactory.getLog(CreateBagsPanel.class);
    private CreateBagsParams createBagsParams;
    private CreateBagsParamsForm createBagsParamsForm;
    private ArrayList<CreateBagResult>createBagResults;
    private CreateBagResultTable createBagResultTable;
    private JButton okButton;
    
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
        BoxLayout layout = new BoxLayout(panel,BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));       
        
        JComponent form = getCreateBagsParamsForm().getControl();
        panel.add(form);        
        
        JComponent buttonPanel = createButtonPanel();
        panel.add(buttonPanel);     
        
        JComponent table = getCreateBagResultTable().getControl();
        final Dimension tDimension = new Dimension(500,200);
        table.setPreferredSize(tDimension);
        JScrollPane scroller = new JScrollPane(table);
        scroller.setPreferredSize(tDimension);
        panel.add(scroller);
        
        return panel;
    }

    public CreateBagResultTable getCreateBagResultTable() {
        if(createBagResultTable == null){
            createBagResultTable = new CreateBagResultTable(
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

    public void setCreateBagResultTable(CreateBagResultTable createBagResultTable) {
        this.createBagResultTable = createBagResultTable;
    }
    
    public CreateBagsParamsForm getCreateBagsParamsForm() {
        if(createBagsParamsForm == null){
            createBagsParamsForm = new CreateBagsParamsForm(getCreateBagsParams());
            createBagsParamsForm.addValidationListener(new ValidationListener(){
                @Override
                public void validationResultsChanged(ValidationResults vr) {
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
        getCreateBagResultTable().refresh();
    }

    public JButton getOkButton() {
        if(okButton == null){
            okButton = new JButton("ok");
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
                    }else if(!(outputDir != null && outputDir.isDirectory() && outputDir.canWrite() && !FUtils.hasChildren(outputDir))){                
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
    private class NewBagsInPlaceWorker extends LongTask2{
        public boolean isBadRWDir(File file){
            return file.isDirectory() && (!file.canRead() || !file.canWrite());            
        }
        public ArrayList<File> getBadRWDirs(File file){
            ArrayList<File>badDirs = new ArrayList<File>();

            if(isBadRWDir(file)){
                badDirs.add(file);
            }
            if(file.isDirectory()){               
                for(File f:file.listFiles()){  
                    badDirs.addAll(getBadRWDirs(f));
                }                
            }
            return badDirs;
        }        
        
        @Override
        protected Object doInBackground() throws Exception {          
            
            BusyIndicator.showAt(CreateBagsPanel.this);
            
            
            BagView bagView = BagView.getInstance();
            MetsBag bag = bagView.getBag();
            
            try{ 
               
                for(int i = 0; i< getCreateBagsParams().getDirectories().size();i++){                                
                    //regular check
                    if(isCancelled()){
                        break;
                    }
                    
                    int percent = (int)Math.floor( ((i+1) / ((float)getCreateBagsParams().getDirectories().size()))*100);                                                                        
                    
                    ArrayList<String>errors = new ArrayList<String>();
                    
                    File file = getCreateBagsParams().getDirectories().get(i);
                    
                    //leesbaar? schrijfbaar?
                    if(isBadRWDir(file)){
                        String error = Context.getMessage("CreateBagsPanel.NewBagsInPlaceWorker.error.badrwdirs",new Object [] {
                            file
                        });
                        errors.add(error);
                        addCreateBagResult(new CreateBagResult(file,file,errors.toArray(new String [] {})));
                        setProgress(percent);
                        continue;
                    }
                    
                    //regular check
                    if(isCancelled()){
                        break;
                    }
                    
                    //ledig mets
                    bagView.getInfoFormsPane().getInfoInputPane().setMets(new Mets());
                    Mets mets = bagView.getInfoFormsPane().getInfoInputPane().getMets();
                    
                                        
                    
                    //zoek metadata bestanden (todo: haal die uit payload lijst)
                    boolean ok = false;
                    for(String metadataPath:getCreateBagsParams().getMetadata()){
                        File metadataFile = new File(file,metadataPath);                                       
                        
                        if(metadataFile.exists() && metadataFile.isFile()){                            
                            try{
                                if(metadataFile.getAbsolutePath().endsWith(".xml")){                    
                                    MdSec mdSec = MetsUtils.createMdSec(metadataFile);                                    
                                    mets.getDmdSec().add(mdSec);
                                }else if(metadataFile.getAbsolutePath().endsWith(".csv")){
                                
                                }
                                ok = true;
                            }catch(Exception e){    
                                log(e.getMessage());
                                log.debug(e);
                                errors.add(e.getMessage());                                                              
                            }
                        }else{
                            ok = true;
                        }
                        if(!ok){
                            break;                            
                        }
                    }                    
                    if(!ok){
                        addCreateBagResult(new CreateBagResult(file,file,errors.toArray(new String [] {})));
                        setProgress(percent);
                        continue;
                    }
                    
                    //regular check
                    if(isCancelled()){
                        break;
                    }
                    
                    //voeg dc toe indien nodig, en mogelijk
                    if(getCreateBagsParams().isAddDC()){
                        try{
                            Document dcDoc = MetsUtils.generateDCDoc(mets);
                            if(dcDoc != null){
                                mets.getDmdSec().add(MetsUtils.createMdSec(dcDoc));
                            }
                        }catch(Exception e){
                            log(e.getMessage());                      
                            e.printStackTrace(); 
                            errors.add(e.getMessage());
                            addCreateBagResult(new CreateBagResult(file,file,errors.toArray(new String [] {})));
                            setProgress(percent);
                            continue;
                        }                        
                    }
                    
                    //regular check
                    if(isCancelled()){
                        break;
                    }
                    
                    //schrijf naar bag-info
                    if(getCreateBagsParams().isWriteToBagInfo()){
                        try{
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
                                byte [] baginfo = MetsUtils.DCToBagInfo(dcDoc);
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
                                    bag.addField(field);
                                }
                            }                            
                        }catch(Exception e){
                            log(e.getMessage());                         
                            e.printStackTrace();
                            errors.add(e.getMessage());
                            addCreateBagResult(new CreateBagResult(file,file,errors.toArray(new String [] {})));
                            setProgress(percent);
                            continue;
                        }
                    }
                    
                    //regular check
                    if(isCancelled()){
                        break;
                    }                    
                    
                    String [] ignoreFiles = getCreateBagsParams().isKeepMetadata() ? new String [] {} : getCreateBagsParams().getMetadata();                    
                    
                    if(bag.isAddKeepFilesToEmptyFolders()){                    
                        bagView.createBagsHandler.createPreBagAddKeepFilesToEmptyFolders(
                            file,
                            getCreateBagsParams().getVersion(),                            
                            ignoreFiles
                        );					
                    }else{							
                        bagView.createBagsHandler.createPreBag(
                            file, 
                            getCreateBagsParams().getVersion(),                           
                            ignoreFiles
                        );
                    }
                    
                    setProgress(percent);
                    
                    addCreateBagResult(new CreateBagResult(file,file,errors.toArray(new String [] {})));
                }
                
            }catch(Exception e){  
                e.printStackTrace(); 
                log(e.getMessage());                
                log.error(e);
            }
            
            BusyIndicator.clearAt(CreateBagsPanel.this);    
            
            return null;
        }      
    }
    
    private class NewBagsWorker extends LongTask2 {
        
        @Override
        protected Object doInBackground() throws Exception {
            
            BusyIndicator.showAt(CreateBagsPanel.this);
            
            BagView bagView = BagView.getInstance();                
            
            MetsBag bag = null;
            
            //ledig payloads en tags (anders hoopt alles zich in de opeenvolgende bags..)           
            
            try{
                
                for(int i = 0; i< getCreateBagsParams().getDirectories().size();i++){
                    
                    //regular check
                    if(isCancelled()){
                        break;
                    }
                    
                    int percent = (int)Math.floor( ((i+1) / ((float)getCreateBagsParams().getDirectories().size()))*100);                                                                        
                    
                    File inputDir = getCreateBagsParams().getDirectories().get(i);
                    ArrayList<String>errors = new ArrayList<String>();
                    
                    //ledig mets                    
                    Mets mets = new Mets();
                    bagView.getInfoFormsPane().getInfoInputPane().setMets(mets);
                    
                    
                    File out = new File(getCreateBagsParams().getOutputDir().get(0),inputDir.getName());                    
                    
                    /*
                     *  Lege bag:
                     *      rootDir: null, want map bestaat nog niet (opgeven File die nog niet bestaat geef IOException)                     
                     */
                    bag = new MetsBag(null,getCreateBagsParams().getVersion());                    
                    bag.setRootDir(out);
                    bagView.setBag(bag);                   
                    
                    //voeg payloads toe
                    ArrayList<File>listPayloads = FUtils.listFiles(inputDir);
                    
                    for(File file:listPayloads){
                        if(file.isFile()){                            
                            System.out.println("adding "+file+" to payload");
                            bag.addFileToPayload(file);
                        }
                    }
                    
                    //regular check
                    if(isCancelled()){
                        break;
                    }
                    
                    //zoek metadata bestanden (todo: haal die uit payload lijst)
                    boolean ok = false;
                    for(String metadataPath:getCreateBagsParams().getMetadata()){
                        File metadataFile = new File(inputDir,metadataPath);                       
                    
                        if(metadataFile.exists() && metadataFile.isFile()){                            
                            //voeg toe aan mets
                            try{
                                if(metadataFile.getAbsolutePath().endsWith(".xml")){                    
                                    MdSec mdSec = MetsUtils.createMdSec(metadataFile);                                    
                                    mets.getDmdSec().add(mdSec);
                                }else if(metadataFile.getAbsolutePath().endsWith(".csv")){
                                
                                }
                                ok = true;
                            }catch(Exception e){ 
                                e.printStackTrace(); 
                                log(e.getMessage());
                                log.debug(e);
                                errors.add(e.getMessage());
                            }
                            //haal uit payload lijst
                            if(!getCreateBagsParams().isKeepMetadata()){
                                String path = "data/"+metadataPath;                            
                                bag.removeBagFile(path);
                            }                            
                        }else{
                            ok = true;
                        }
                        if(!ok){
                            break;
                        }                            
                    }
                    if(!ok){
                        addCreateBagResult(new CreateBagResult(inputDir,out,errors.toArray(new String [] {})));
                        setProgress(percent);
                        continue;
                    }
                    
                    //regular check
                    if(isCancelled()){
                        break;
                    }
                    
                    //voeg dc toe indien nodig, en mogelijk
                    if(getCreateBagsParams().isAddDC()){
                        try{
                            Document dcDoc = MetsUtils.generateDCDoc(mets);
                            if(dcDoc != null){
                                mets.getDmdSec().add(MetsUtils.createMdSec(dcDoc));
                            }                           
                        }catch(Exception e){
                            log(e.getMessage());
                            e.printStackTrace();
                            errors.add(e.getMessage());
                            addCreateBagResult(new CreateBagResult(inputDir,out,errors.toArray(new String [] {})));
                            setProgress(percent);
                            continue;
                        }                        
                    }
                    
                    //regular check
                    if(isCancelled()){
                        break;
                    }
                    
                    //schrijf naar bag-info
                    if(getCreateBagsParams().isWriteToBagInfo()){
                        try{
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
                                
                                byte [] baginfo = MetsUtils.DCToBagInfo(dcDoc);
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
                                    bag.addField(field);
                                }
                            }                            
                        }catch(Exception e){
                            log(e.getMessage());
                            e.printStackTrace();
                            errors.add(e.getMessage());
                            addCreateBagResult(new CreateBagResult(inputDir,out,errors.toArray(new String [] {})));
                            setProgress(percent);
                            continue;
                        }
                    }
                    
                    //regular check
                    if(isCancelled()){
                        break;
                    }
                    
                    bag.setBagItMets(new DefaultBagItMets());
                    
                    Writer writer = new FileSystemWriter(new BagFactory());
                    
                    String messages = bag.write(writer);

                    if(messages != null && !messages.trim().isEmpty()){                        
                        log("Warning - bag not saved"+"Problem saving bag:\n" + messages);                        
                        errors.add("Warning - bag not saved"+"Problem saving bag:\n" + messages);
                        addCreateBagResult(new CreateBagResult(inputDir,out,errors.toArray(new String [] {})));
                        setProgress(percent);
                        continue;
                    }                                   
                    
                    addCreateBagResult(new CreateBagResult(inputDir,out,errors.toArray(new String [] {})));
                    setProgress(percent);                     
                }
                
            }catch(Exception e){
                e.printStackTrace(); 
                log(e.getMessage());
                log.error(e);                
            }
        
            BusyIndicator.clearAt(CreateBagsPanel.this);
         
            return null;
        }               
    }
}