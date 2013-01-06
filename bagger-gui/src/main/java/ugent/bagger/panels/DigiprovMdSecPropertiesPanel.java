package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.PremisUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.TaskAddMdSecFromFile;
import ugent.premis.Premis;
import ugent.premis.PremisEvent;
import ugent.premis.PremisIO;
import ugent.premis.PremisObject;
import ugent.premis.PremisObject.PremisObjectType;

/**
 *
 * @author nicolas
 */
public class DigiprovMdSecPropertiesPanel extends MdSecPropertiesPanel{
    public DigiprovMdSecPropertiesPanel(ArrayList<MdSec>data,String id){        
        super(data,id,deriveExceptions(data));        
        init();
    }  
    public static ArrayList<MdSec>deriveExceptions(ArrayList<MdSec>list){
        ArrayList<MdSec>exceptions = new ArrayList<MdSec>();
        for(MdSec m:list){
            if(m.getID().equals("bagit_digiprovMD") && PremisUtils.isPremisMdSec(m)){
                exceptions.add(m);
            }
        }
        return exceptions;
    }
    protected void init(){
        if(!(getId() != null && getId().equals("bagit"))){
            return;
        }       
        
        JButton mergeButton = new JButton(Context.getMessage("digiprovMDSecPropertiesPanel.mergeButton.label"));
        getButtonPanel().add(mergeButton);
        
        mergeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){    
                if(getMax() > 0 && data.size() >= getMax()){
                    return;
                }
                if(!(getId() != null && getId().equals("bagit"))){
                    return;
                }
                
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
        
        addPropertyChangeListener("remove",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
             
                if(!(getId() != null && getId().equals("bagit"))){
                    return;
                }
                
                BagView bagView = BagView.getInstance();
                MetsBag metsBag = bagView.getBag();
                Premis premis = metsBag.getPremis();
                
                //indien premis-record, dan deze verwerken!
                MdSec mdSec = (MdSec) pce.getNewValue();
                
                if(!mdSec.getID().equals("bagit_digiprovMD") && PremisUtils.isPremisMdSec(mdSec)){
                    return;
                }
                
                Element element = mdSec.getMdWrap().getXmlData().get(0);   
                
                //verwijder alles wat niet aan bagit toebehoort uit premis-record
                premis.getAgent().clear();
                premis.getRights().clear();
                
                Iterator<PremisObject>itObject = premis.getObject().iterator();
                while(itObject.hasNext()){
                    PremisObject object = itObject.next();
                    if(!(object.getType() == PremisObjectType.representation && object.getXmlID() != null && object.getXmlID().equals("bagit"))){
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
    }
    
    protected List<PropertyChangeListener> getPropertyMergeListeners(){    
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {                
                final BagView bagView = BagView.getInstance();
                if(pce.getPropertyName().equals("state") && pce.getNewValue() == SwingWorker.StateValue.STARTED){                    
                    log.error(Context.getMessage("mdSecTable.addMdSec.start"));
                    //ApplicationContextUtil.addConsoleMessage(Context.getMessage("mdSecTable.addMdSec.start"));
                }else if(pce.getPropertyName().equals("log")){
                    log.error(pce.getNewValue().toString());
                    //ApplicationContextUtil.addConsoleMessage(pce.getNewValue().toString());                    
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
                    
                    Element element = mdSec.getMdWrap().getXmlData().get(0);                


                    //merge premis record met dat van systeem
                    try{
                        Premis newPremis = PremisIO.toPremis(element);

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
                            if(object.getType() == PremisObjectType.representation && object.getXmlID() != null && object.getXmlID().equals("bagit")){
                                iteratorObject.remove();
                            }
                        }                        
                        //haal alle niet-bagit objecten uit huidige premis
                        iteratorObject = premis.getObject().iterator();
                        while(iteratorObject.hasNext()){
                            PremisObject object = iteratorObject.next();
                            if(!(object.getType() == PremisObjectType.representation && object.getXmlID() != null && object.getXmlID().equals("bagit"))){
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
                        

                        mdSec.setID("bagit");
                        mdSec.getMdWrap().getXmlData().set(0,PremisIO.toDocument(premis).getDocumentElement());
                        
                        MdSec digiprovMDBagit = PremisUtils.getPremisMdSec(data);                        
                        
                        
                        //hermaak dmdSec
                        try{
                            
                            Document doc = PremisIO.toDocument(premis);
                            
                            //kan verwijderd zijn
                            if(digiprovMDBagit == null){
                                try{
                                    digiprovMDBagit = MetsUtils.createMdSec(doc,false);                            
                                    digiprovMDBagit.setID("bagit");
                                    getEditDmdSecPropertiesTable().add(digiprovMDBagit);
                                }catch(Exception e){
                                    log.error(e);                                    
                                }
                            }else{
                                digiprovMDBagit.getMdWrap().getXmlData().clear();
                                digiprovMDBagit.getMdWrap().getXmlData().add(doc.getDocumentElement());
                            }
                            
                            
                        }catch(Exception e){
                            log.error(e);
                        }
                        

                    }catch(ParseException e){
                        log.error(e);
                    }catch(ParserConfigurationException e){
                        log.error(e);
                    }
                    
                    
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
}
