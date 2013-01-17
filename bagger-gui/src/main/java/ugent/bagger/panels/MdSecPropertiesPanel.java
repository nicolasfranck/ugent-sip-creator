package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.PremisUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.swing.JPopupTextArea;
import ugent.bagger.tables.EditMdSecPropertiesTable;
import ugent.bagger.wizards.BagInfoImportWizard;
import ugent.bagger.wizards.BagInfoImportWizardDialog;
import ugent.bagger.wizards.CSVWizard;
import ugent.bagger.wizards.CSVWizardDialog;
import ugent.bagger.wizards.CrosswalkParamsDialog;
import ugent.bagger.wizards.CrosswalkParamsWizard;
import ugent.bagger.workers.TaskAddMdSecFromFile;
import ugent.premis.Premis;
import ugent.premis.PremisEvent;
import ugent.premis.PremisIO;
import ugent.premis.PremisObject;

/**
 *
 * @author nicolas
 */
public class MdSecPropertiesPanel extends JPanel{
    static final Log log = LogFactory.getLog(MdSecPropertiesPanel.class);
    JComponent buttonPanel;
    EditMdSecPropertiesTable editDmdSecPropertiesTable;         
    protected ArrayList<MdSec>data;     
    JButton removeButton;    
    JButton addButton;   
    JButton premisButton;
    String id;
    ArrayList<MdSec>exceptions;
    JPopupMenu premisMenu;

    public JButton getPremisButton() {
        if(premisButton == null){
            premisButton = new JButton(Context.getMessage("MdSecPanel.premisButton.label"));
            premisButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    getPremisMenu().show(premisButton,0,premisButton.getHeight());
                }                
            });
        }
        return premisButton;
    }
    
    public JPopupMenu getPremisMenu() {
        if(premisMenu == null){
            JMenuItem showPremisMenuItem = new JMenuItem(Context.getMessage("showPremisMenuItem.label"));
            JMenuItem clearPremisMenuItem = new JMenuItem(Context.getMessage("clearPremisMenuItem.label"));
            JMenuItem editPremisMenuItem = new JMenuItem(Context.getMessage("editPremisMenuItem.label"));            
            premisMenu = new JPopupMenu();
            premisMenu.add(showPremisMenuItem);
            premisMenu.add(clearPremisMenuItem);
            premisMenu.add(editPremisMenuItem);          
            
            clearPremisMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {                    
                    
                    boolean confirm = SwingUtils.confirm(null,Context.getMessage("clearPremisMenuItem.confirmRemoval"));
                    
                    if(!confirm){
                        return;
                    }
                                      
                    Premis premis = BagView.getInstance().getBag().getPremis();             
                
                    //verwijder alles wat niet aan bagit toebehoort uit premis-record
                    premis.getAgent().clear();
                    premis.getRights().clear();

                    Iterator<PremisObject>itObject = premis.getObject().iterator();
                    while(itObject.hasNext()){
                        PremisObject object = itObject.next();
                        if(!(object.getType() == PremisObject.PremisObjectType.representation && object.getXmlID() != null && object.getXmlID().equals("bagit"))){
                            itObject.remove();
                        }
                    }
                
                    Iterator<PremisEvent>itEvent = premis.getEvent().iterator();
                    while(itEvent.hasNext()){
                        PremisEvent event = itEvent.next();
                        if(!event.getEventType().equals("bagit")){
                            itEvent.remove();
                        }
                    }
                    
                }            
            });
            editPremisMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    File [] files = SwingUtils.chooseFiles(
                        Context.getMessage("digiprovMDSecPropertiesPanel.choosePremis.title"),                    
                        new FileExtensionFilter(
                            new String [] {"xml"},
                            Context.getMessage("addXMLMenuItem.fileFilter.label"),
                            true
                        ),
                        JFileChooser.FILES_ONLY,
                        false
                    );
                    if(files.length > 0){
                        SwingUtils.monitor(
                            new TaskAddMdSecFromFile(files),
                            Context.getMessage("digiprovMDSecPropertiesPanel.mergePremis.title"),
                            Context.getMessage("digiprovMDSecPropertiesPanel.mergePremis.note"),
                            getPropertyMergeListeners()
                        );
                    }
                }            
            });
            showPremisMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    Premis premis = BagView.getInstance().getBag().getPremis();
                    JTextArea textArea = new JPopupTextArea();
                    textArea.setEditable(false);
                    textArea.setBackground(Color.WHITE);
                    textArea.setEnabled(true);
                    
                    try{                        
                        StringWriter writer = new StringWriter();
                        PremisIO.write(premis,writer,true);
                        textArea.setText(writer.toString());
                    }catch(Exception e){
                        textArea.setText("");
                        e.printStackTrace();
                    }
                    
                    JPanel panel = new JPanel(new BorderLayout());
                    panel.setPreferredSize(new Dimension(500,600));
                    panel.add(new JScrollPane(textArea));
                    JDialog dialog = new JDialog(SwingUtils.getFrame(),true);
                    dialog.setContentPane(panel);
                    dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);  
                    SwingUtils.centerOnParent(dialog,true);
                    dialog.setVisible(true);
                }                
            });
        }
        return premisMenu;
    }
    protected List<PropertyChangeListener> getPropertyMergeListeners(){    
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {                
                final BagView bagView = BagView.getInstance();
                if(pce.getPropertyName().equals("state") && pce.getNewValue() == SwingWorker.StateValue.STARTED){                    
                    log.error(Context.getMessage("mdSecTable.addMdSec.start"));                    
                }else if(pce.getPropertyName().equals("log")){
                    log.error(pce.getNewValue().toString());                    
                }else if(pce.getPropertyName().equals("send")){                    

                    MetsBag metsBag = bagView.getBag();                    
                    Premis premis = metsBag.getPremis();

                    //indien premis-record, dan deze verwerken!
                    MdSec mdSec = (MdSec) pce.getNewValue();

                    if(!PremisUtils.isPremisMdSec(mdSec)){
                        SwingUtils.ShowError(
                            Context.getMessage("digiprovMDSecPropertiesPanel.mergePremisDenied.title"),
                            Context.getMessage("digiprovMDSecPropertiesPanel.mergePremisDenied.description")
                        );
                        return;
                    }            


                    //merge premis record met dat van systeem
                    try{
                        Premis newPremis = PremisIO.toPremis(mdSec.getMdWrap().getXmlData().get(0));

                        //agents
                        premis.getAgent().clear();
                        premis.getAgent().addAll(newPremis.getAgent());

                        //rights
                        premis.getRights().clear();
                        premis.getRights().addAll(newPremis.getRights());

                        //objects met type 'representation' en xmlID 'bagit' behoren toe aan 'bagit'!                     
                        
                        //filter nieuwe premis
                        Iterator<PremisObject>iteratorObject = newPremis.getObject().iterator();
                        while(iteratorObject.hasNext()){
                            PremisObject object = iteratorObject.next();
                            if(object.getType() == PremisObject.PremisObjectType.representation && object.getXmlID() != null && object.getXmlID().equals("bagit")){
                                iteratorObject.remove();
                            }
                        }                        
                        //haal alle niet-bagit objecten uit huidige premis
                        iteratorObject = premis.getObject().iterator();
                        while(iteratorObject.hasNext()){
                            PremisObject object = iteratorObject.next();
                            if(!(object.getType() == PremisObject.PremisObjectType.representation && object.getXmlID() != null && object.getXmlID().equals("bagit"))){
                                iteratorObject.remove();
                            }
                        }
                        
                        premis.getObject().addAll(newPremis.getObject());                    
                        

                        //events met type 'bagit' behoren toe aan 'bagit'!                     
                        
                        //filter nieuwe premis 
                        Iterator<PremisEvent>iteratorEvent = newPremis.getEvent().iterator();
                        while(iteratorEvent.hasNext()){
                            PremisEvent event = iteratorEvent.next();
                            if(event.getEventType() != null && event.getEventType().equals("bagit")){
                                iteratorEvent.remove();
                            }
                        }
                        //haal alle niet-bagit events uit huidige premis
                        iteratorEvent = premis.getEvent().iterator();
                        while(iteratorEvent.hasNext()){
                            PremisEvent event = iteratorEvent.next();
                            if(!(event.getEventType() != null && event.getEventType().equals("bagit"))){
                                iteratorEvent.remove();
                            }
                        }                                        
                        premis.getEvent().addAll(newPremis.getEvent());

                    }catch(ParseException e){
                        log.error(e.getMessage());
                    }
                }
            }
        };
        return Arrays.asList(new PropertyChangeListener [] {listener});     
    }
   
    public MdSecPropertiesPanel(final ArrayList<MdSec>data,String id){        
        this(data,id,new ArrayList<MdSec>());
    }
    public MdSecPropertiesPanel(final ArrayList<MdSec>data,String id,ArrayList<MdSec>exceptions){        
        Assert.notNull(data);
        this.data = data;         
        this.id = id;
        setLayout(new BorderLayout());
        setExceptions(exceptions);
        add(createContentPane());        
    }

    public JButton getRemoveButton() {
        if(removeButton == null){
            removeButton = new JButton(Context.getMessage("MdSecPanel.removeButton.label"));
        }
        return removeButton;
    }
    public JButton getAddButton() {
        if(addButton == null){
            addButton = new JButton(Context.getMessage("MdSecPanel.addButton.label"));   
        }
        return addButton;
    }
    public ArrayList<MdSec> getExceptions() {
        if(exceptions == null){
            exceptions = new ArrayList<MdSec>();
        }
        return exceptions;
    }    
    public void setExceptions(ArrayList<MdSec> exceptions) {
        this.exceptions = exceptions;        
    }   
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public MdSec newMdSec(){
        return new MdSec("");        
    }      
    public void reset(final ArrayList<MdSec>data){                       
        getEditDmdSecPropertiesTable().reset(data);
        this.data = data;       
        enableButtons(getMax() > 0 && data.size() >= getMax() ? false:true);
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
    public EditMdSecPropertiesTable getEditDmdSecPropertiesTable() {
        if(editDmdSecPropertiesTable == null){
            editDmdSecPropertiesTable = createMdSecPropertiesTable();
            editDmdSecPropertiesTable.addPropertyChangeListener("remove",new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent pce) {                    
                    MdSecPropertiesPanel.this.firePropertyChange("remove",pce.getOldValue(),pce.getNewValue());                    
                    if(getMax() > 0 && data.size() < getMax()){
                       enableButtons(true);
                    }
                }
            });
        }
        return editDmdSecPropertiesTable;
    }
    public EditMdSecPropertiesTable createMdSecPropertiesTable(){                        
        return new EditMdSecPropertiesTable(data,new String [] {"namespace","MDTYPE","rootName"},"mdSecTable",getExceptions());
    }
    public void setMdSecPropertiesTable(EditMdSecPropertiesTable editDmdSecPropertiesTable) {
        this.editDmdSecPropertiesTable = editDmdSecPropertiesTable;
    }  
    protected JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());        
        panel.add(new JScrollPane(getEditDmdSecPropertiesTable().getControl()));        
        panel.add(getButtonPanel(),BorderLayout.NORTH);        
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        boolean buttonsEnabled = getMax() > 0 && data.size() >= getMax() ? false:true;                
        
        enableButtons(buttonsEnabled);
        
        //voeg xml-bestand toe
        JMenuItem addXMLMenuItem = new JMenuItem(Context.getMessage("addXMLMenuItem.label"));
        JMenuItem crosswalkMenuItem = new JMenuItem(Context.getMessage("crosswalkMenuItem.label"));
        
        final JPopupMenu addPopupMenu = new JPopupMenu();
        addPopupMenu.add(addXMLMenuItem);
        addPopupMenu.add(crosswalkMenuItem);                
        
        getAddButton().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                addPopupMenu.show(getAddButton(),0,getAddButton().getHeight());
            }
        });
        
        //voeg xml mapping toe
        crosswalkMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(getMax() > 0 && data.size() >= getMax()){
                    return;
                }
                
                CrosswalkParamsWizard wizard = new CrosswalkParamsWizard("CrosswalkParamsWizard");
                wizard.addPropertyChangeListener("mdSec",new PropertyChangeListener(){
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {                        
                        MdSec mdSec = (MdSec) pce.getNewValue();
                        getEditDmdSecPropertiesTable().add(mdSec);
                        getEditDmdSecPropertiesTable().refresh();
                        if(getMax() > 0 && data.size() >= getMax()){
                            enableButtons(false);
                        }
                        MdSecPropertiesPanel.this.firePropertyChange("crosswalkMdSec",null,mdSec);
                    }                    
                });
                CrosswalkParamsDialog wdialog = new CrosswalkParamsDialog(wizard);
                wdialog.setResizable(false);
                wdialog.showDialog();
            }
            
        });
        addXMLMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){    
                if(getMax() > 0 && data.size() >= getMax()){
                    return;
                }
                
                File [] files = SwingUtils.chooseFiles(
                    Context.getMessage("addXMLMenuItem.fileChooser.title"),
                    new FileExtensionFilter(
                        new String [] {"xml"},
                        Context.getMessage("addXMLMenuItem.fileFilter.label"),
                        true
                    ),
                    JFileChooser.FILES_ONLY,
                    true
                );
                if(files.length > 0){
                    monitor(new TaskAddMdSecFromFile(files));                
                }                
            }
        });
        
        //verwijder selectie(s)
        getRemoveButton().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getEditDmdSecPropertiesTable().deleteSelected();
                getEditDmdSecPropertiesTable().refresh();
                
                if(getMax() > 0 && data.size() < getMax()){
                    enableButtons(true);
                }                
            }        
        });        
        
        //importeer CSV
        JMenuItem importCSVItem = new JMenuItem(Context.getMessage("importCSVItem.label"));
        JMenuItem importBagInfoItem = new JMenuItem(Context.getMessage("importBagInfoItem.label"));
        
        importCSVItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(getMax() > 0 && data.size() >= getMax()){
                    return;
                }
                
                CSVWizard wizard = new CSVWizard("CSVWizard");
                CSVWizardDialog dialog = new CSVWizardDialog(wizard);
                wizard.addPropertyChangeListener("addMdSec",new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {
                        MdSec mdSec = (MdSec) pce.getNewValue();
                        getEditDmdSecPropertiesTable().add(mdSec);
                    }
                });
                wizard.addPropertyChangeListener("doneMdSec",new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {                        
                        getEditDmdSecPropertiesTable().refresh();
                        
                        if(getMax() > 0 && data.size() >= getMax()){
                            enableButtons(false);
                        }
                    }
                });
                dialog.showDialog();
            }            
        });
        
        //importeer bag-info.txt
        importBagInfoItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(getMax() > 0 && data.size() >= getMax()){
                    return;
                }
                
                BagInfoImportWizard wizard = new BagInfoImportWizard("BagInfoImportWizard");
                BagInfoImportWizardDialog dialog = new BagInfoImportWizardDialog(wizard);
                wizard.addPropertyChangeListener("addMdSec",new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {
                        MdSec mdSec = (MdSec) pce.getNewValue();
                        getEditDmdSecPropertiesTable().add(mdSec);
                    }
                });
                wizard.addPropertyChangeListener("doneMdSec",new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {                        
                        getEditDmdSecPropertiesTable().refresh();
                        if(getMax() > 0 && data.size() >= getMax()){
                            enableButtons(false);
                        }
                    }
                });
                dialog.showDialog();
            }            
        });        
        
        addPopupMenu.add(importCSVItem);
        addPopupMenu.add(importBagInfoItem);
        
        //voeg toe
        panel.add(getAddButton());                            
        panel.add(getRemoveButton()); 
        panel.add(getPremisButton());
        
        return panel;
    }
    protected void monitor(SwingWorker worker){
        SwingUtils.monitor(
            worker,
            Context.getMessage("MdSecPropertiesPanel.monitoring.title"),
            Context.getMessage("MdSecPropertiesPanel.monitoring.description"),
            getPropertyListeners()
        );        
    }   
    protected List<PropertyChangeListener> getPropertyListeners(){        
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {                
                final BagView bagView = BagView.getInstance();
                if(pce.getPropertyName().equals("state") && pce.getNewValue() == SwingWorker.StateValue.STARTED){                    
                    log.error(Context.getMessage("mdSecTable.addMdSec.start"));                    
                }else if(pce.getPropertyName().equals("log")){
                    log.error(pce.getNewValue().toString());                    
                }else if(pce.getPropertyName().equals("send")){
                    getEditDmdSecPropertiesTable().add((MdSec)pce.getNewValue());                    
                    if(getMax() > 0 && data.size() >= getMax()){
                        enableButtons(false);
                    }                    
                    MdSecPropertiesPanel.this.firePropertyChange("mdSec",null,pce.getNewValue());                    
                }else if(
                    pce.getPropertyName().equals("report") && 
                    pce.getNewValue().toString().compareTo("success") == 0
                ){                    
                    getEditDmdSecPropertiesTable().refresh();
                }
            }
        };
        return Arrays.asList(new PropertyChangeListener [] {listener});        
    }
    protected int getMax(){
        return -1;
    }
    public void enableButtons(boolean enabled){
        getAddButton().setEnabled(enabled);              
        getRemoveButton().setEnabled(enabled);
        getPremisButton().setEnabled(enabled);
    }
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        enableButtons(enabled);
    }
}