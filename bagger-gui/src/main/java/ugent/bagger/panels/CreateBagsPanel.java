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
import javax.swing.JLabel;
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
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));       
        
        JComponent form = getCreateBagsParamsForm().getControl();
        form.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(form);        
        
        JComponent buttonPanel = createButtonPanel();
        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(buttonPanel);
        
        panel.add(getLabelStatistics());
        
        JComponent table = getCreateBagResultTable().getControl();
        table.setAlignmentX(LEFT_ALIGNMENT);
        final Dimension tDimension = new Dimension(500,200);        
        JScrollPane scroller = new JScrollPane(table);
        scroller.setPreferredSize(tDimension);
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
    private class NewBagsInPlaceWorker extends LongTask{
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
            
            
            try{ 
                int totalSuccess = 0;
                int total = getCreateBagsParams().getDirectories().size();
               
                for(int i = 0; i< getCreateBagsParams().getDirectories().size();i++){                                                    
                    File file = getCreateBagsParams().getDirectories().get(i);
                    
                    MetsBag metsBag = new MetsBag(null,null);
                    metsBag.setBagItMets(new DefaultBagItMets());            
                    metsBag.setFile(file);
                    //ledig mets
                    Mets mets = new Mets();
                    metsBag.setMets(mets);
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
                        break;
                    }
                    
                    int percent = (int)Math.floor( ((i+1) / ((float)getCreateBagsParams().getDirectories().size()))*100);                                                                        
                    
                    ArrayList<String>errors = new ArrayList<String>();
                    
                    
                    
                    //leesbaar? schrijfbaar?
                    if(isBadRWDir(file)){
                        String error = Context.getMessage("CreateBagsPanel.NewBagsInPlaceWorker.error.badrwdirs",new Object [] {
                            file
                        });
                        errors.add(error);
                        addCreateBagResult(new CreateBagResult(file,file,errors.toArray(new String [] {})));
                        if(!isDone()){
                            setProgress(percent);                
                        }
                        continue;
                    }
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
                        break;
                    }
                    
                    //zoek metadata bestanden (todo: haal die uit payload lijst)
                    boolean ok = false;
                    for(String metadataPath:getCreateBagsParams().getMetadata()){
                        File metadataFile = new File(file,metadataPath);                                       
                        
                        if(metadataFile.exists() && metadataFile.isFile()){                            
                            try{
                                if(metadataFile.getAbsolutePath().endsWith(".xml")){                    
                                    MdSec mdSec = MetsUtils.createMdSec(metadataFile);                                    
                                    mets.getDmdSec().add(mdSec);
                                }
                                ok = true;
                            }catch(Exception e){                                    
                                log.error(e.getMessage());
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
                        if(!isDone()){
                            setProgress(percent);                
                        }
                        continue;
                    }                    
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
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
                            log.error(e.getMessage());
                            errors.add(e.getMessage());
                            addCreateBagResult(new CreateBagResult(file,file,errors.toArray(new String [] {})));
                            if(!isDone()){
                                setProgress(percent);                
                            }
                            continue;
                        }                        
                    }                    
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
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
                        }catch(Exception e){                            
                            log.error(e.getMessage());
                            errors.add(e.getMessage());
                            addCreateBagResult(new CreateBagResult(file,file,errors.toArray(new String [] {})));
                            if(!isDone()){
                                setProgress(percent);                
                            }
                            continue;
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
                        try {
                            metsBag.createPreBagAddKeepFilesToEmptyFolders(file,getCreateBagsParams().getVersion(),ignoreFiles);                                           
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            String title = Context.getMessage("DefaultBag.createPreBag.Exception.title");
                            String message = Context.getMessage(
                                "DefaultBag.createPreBag.Exception.description", 
                                new Object [] {file,e.getMessage()}
                            );                            
                            SwingUtils.ShowError(title,message);    	   
                        }
                    }else{	                        
                        try {
                            metsBag.createPreBag(file,getCreateBagsParams().getVersion(),ignoreFiles);                      
                        } catch (Exception e) {
                            log.error(e.getMessage());

                            String title = Context.getMessage("DefaultBag.createPreBag.Exception.title");
                            String message = Context.getMessage(
                                "DefaultBag.createPreBag.Exception.description", 
                                new Object [] {file,e.getMessage()}
                            );
                            //log(message);
                            SwingUtils.ShowError(title,message);    	   
                        }
                    }
                    
                    if(!isDone()){
                        setProgress(percent);                
                    }
                    
                    totalSuccess++;
                    
                    reportStatistics(total,totalSuccess);
                    addCreateBagResult(new CreateBagResult(file,file,errors.toArray(new String [] {})));
                }                
                
            }catch(Exception e){                                  
                log.error(e.getMessage());
            }
            
            BusyIndicator.clearAt(CreateBagsPanel.this);    
            
            return null;
        }               
    }
    
    private class NewBagsWorker extends LongTask {        
        @Override
        protected Object doInBackground() throws Exception {
            
            BusyIndicator.showAt(CreateBagsPanel.this);           
            
            
            //ledig payloads en tags (anders hoopt alles zich in de opeenvolgende bags..)           
            
            try{
                int total = getCreateBagsParams().getDirectories().size();
                int totalSuccess = 0;
                
                for(int i = 0; i< getCreateBagsParams().getDirectories().size();i++){
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
                        break;
                    }                    
                    
                    int percent = (int)Math.floor( ((i+1) / ((float)getCreateBagsParams().getDirectories().size()))*100);                                                                        
                    
                    File inputDir = getCreateBagsParams().getDirectories().get(i);
                    ArrayList<String>errors = new ArrayList<String>();
                    
                    //ledig mets                    
                    Mets mets = new Mets();
                    
                    File out = new File(getCreateBagsParams().getOutputDir().get(0),inputDir.getName());                    
                    
                    /*
                     *  Lege bag:
                     *      rootDir: null, want map bestaat nog niet (opgeven File die nog niet bestaat geef IOException)                     
                     */
                    MetsBag metsBag = new MetsBag(null,getCreateBagsParams().getVersion());                    
                    metsBag.setFile(out);
                    metsBag.setBagItMets(new DefaultBagItMets());
                    metsBag.setMets(mets);
                    
                    //voeg payloads toe
                    ArrayList<File>listPayloads = FUtils.listFiles(inputDir);
                    
                    for(File file:listPayloads){
                        if(file.isFile()){                                                        
                            metsBag.addFileToPayload(file);
                        }
                    }
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
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
                                log.error(e.getMessage());
                                errors.add(e.getMessage());
                            }
                            //haal uit payload lijst
                            if(!getCreateBagsParams().isKeepMetadata()){
                                String path = "data/"+metadataPath;                            
                                metsBag.removeBagFile(path);
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
                        if(!isDone()){
                            setProgress(percent);                
                        }
                        continue;
                    }
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
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
                            log.error(e.getMessage());
                            errors.add(e.getMessage());
                            addCreateBagResult(new CreateBagResult(inputDir,out,errors.toArray(new String [] {})));
                            if(!isDone()){
                                setProgress(percent);                
                            }
                            continue;
                        }                        
                    }
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
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
                        }catch(Exception e){                            
                            log.error(e.getMessage());
                            errors.add(e.getMessage());
                            addCreateBagResult(new CreateBagResult(inputDir,out,errors.toArray(new String [] {})));
                            if(!isDone()){
                                setProgress(percent);                
                            }
                            continue;
                        }
                    }
                    
                    //regular check
                    if(isCancelled()){
                        reportStatistics(total,totalSuccess);
                        break;
                    }
                    
                    reportStatistics(total,totalSuccess);
                    
                    
                    
                    Writer writer = new FileSystemWriter(new BagFactory());
                    
                    boolean saveOk = metsBag.write(writer);

                    if(!saveOk){                        
                        String message = Context.getMessage("bag.warning.savingFailed");
                        SwingUtils.ShowError(null,message);
                        log.error(message);                        
                        addCreateBagResult(new CreateBagResult(inputDir,out,errors.toArray(new String [] {})));
                        if(!isDone()){
                            setProgress(percent);                
                        }
                        continue;
                    }                                   
                    
                    totalSuccess++;
                    
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