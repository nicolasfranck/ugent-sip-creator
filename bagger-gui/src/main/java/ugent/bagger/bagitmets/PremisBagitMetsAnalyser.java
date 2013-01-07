package ugent.bagger.bagitmets;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MdWrap;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.ArrayUtils;
import ugent.bagger.helper.ArrayUtils.ArrayDiff;
import ugent.bagger.helper.DateUtils;
import ugent.bagger.helper.PremisUtils;
import ugent.premis.DOMHelp;
import ugent.premis.Premis;
import ugent.premis.PremisEvent;
import ugent.premis.PremisIO;
import ugent.premis.PremisObject;
import ugent.premis.PremisObject.PremisObjectType;

/**
 *
 * @author nicolas
 */
public class PremisBagitMetsAnalyser implements BagitMetsAnalyser{
    
    static final Log log = LogFactory.getLog(PremisBagitMetsAnalyser.class);       
    
    @Override
    public void analyse(MetsBag metsBag, Mets mets) {        
        
        //premis        
        Premis premis = metsBag.getPremis();            
        
        //ledig alles met type 'representation' en xmlID 'bagit'
        Iterator<PremisObject>iteratorObject = premis.getObject().iterator();
        while(iteratorObject.hasNext()){
            PremisObject o = iteratorObject.next();
            if(o.getType() == PremisObjectType.representation && o.getXmlID() != null && o.getXmlID().equals("bagit")){
                iteratorObject.remove();
            }
        }        
        
        //maak nieuw object van type 'representation' en xmlID 'bagit' aan
        PremisObject pobject = new PremisObject(PremisObject.PremisObjectType.representation);          
        pobject.setXmlID("bagit");
        PremisObject.PremisObjectIdentifier id = new PremisObject.PremisObjectIdentifier();
        id.setObjectIdentifierType("name");
        id.setObjectIdentifierValue(metsBag.getName());
        pobject.getObjectIdentifier().add(id); 
        premis.getObject().add(pobject); 
        
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDate = dformat.format(new Date());        
        
        
        //eerste keer (indien oude bagit voor het eerst hier wordt ingeladen, dan is er nog geen eventlog)
        //en kan er bijgevolg geen verschil berekent worden        
        int numBagitEvents = 0;
        for(PremisEvent event:premis.getEvent()){                      
            if(event.getEventType() != null && event.getEventType().equals("bagit")){
                numBagitEvents++;                
            }
        }        
        
        if(numBagitEvents == 0){        
            String eventID = DOMHelp.createID();
            
            PremisEvent ev = new PremisEvent();            
            PremisEvent.PremisEventIdentifier evid = new PremisEvent.PremisEventIdentifier();
            evid.setEventIdentifierType("UUID");
            evid.setEventIdentifierValue(eventID);
            ev.setEventIdentifier(evid);
            ev.setXmlID(eventID);            
            ev.setEventType("bagit");
            
            ev.setEventDateTime(formattedDate);            
            ev.setEventDetail("files added to bagit");
            
            for(String file:metsBag.getPayloadPaths()){
                PremisEvent.PremisLinkingObjectIdentifier oid = new PremisEvent.PremisLinkingObjectIdentifier();
                oid.setLinkingObjectIdentifierType("URL");
                oid.setLinkingObjectIdentifierValue(file);
                ev.getLinkingObjectIdentifier().add(oid);
            }
            for(String file:metsBag.getPayloadPaths()){
                PremisEvent.PremisLinkingObjectIdentifier oid = new PremisEvent.PremisLinkingObjectIdentifier();
                oid.setLinkingObjectIdentifierType("URL");
                oid.setLinkingObjectIdentifierValue(file);
                ev.getLinkingObjectIdentifier().add(oid);
            }
            premis.getEvent().add(ev);
        }
        //er is wel een eventLog
        else{
            
            //verschil oud en nieuw berekenen
            ArrayList<String>oldFileList = metsBag.getOldFileList();
            ArrayList<String>newFileList = (ArrayList<String>) metsBag.getPayloadPaths();
            newFileList.addAll(metsBag.getTagFilePaths());

            ArrayDiff<String>diff = ArrayUtils.diff(oldFileList,newFileList);
            
            if(!diff.getAdded().isEmpty()){
                String eventID = DOMHelp.createID();
                
                PremisEvent ev = new PremisEvent();
                PremisEvent.PremisEventIdentifier evid = new PremisEvent.PremisEventIdentifier();
                evid.setEventIdentifierType("dateTime");
                evid.setEventIdentifierValue(eventID);
                ev.setEventIdentifier(evid);
                ev.setXmlID(eventID);
                ev.setVersion("2.2");
                ev.setEventType("bagit");
                ev.setEventDateTime(formattedDate);            
                ev.setEventDetail("files added to bagit");
                for(String file:diff.getAdded()){
                    PremisEvent.PremisLinkingObjectIdentifier oid = new PremisEvent.PremisLinkingObjectIdentifier();
                    oid.setLinkingObjectIdentifierType("URL");
                    oid.setLinkingObjectIdentifierValue(file);
                    ev.getLinkingObjectIdentifier().add(oid);
                }            
                premis.getEvent().add(ev);
            }
            if(!diff.getDeleted().isEmpty()){
                String eventID = DOMHelp.createID();
                
                PremisEvent ev = new PremisEvent();
                PremisEvent.PremisEventIdentifier evid = new PremisEvent.PremisEventIdentifier();
                evid.setEventIdentifierType("dateTime");
                evid.setEventIdentifierValue(eventID);
                ev.setEventIdentifier(evid);
                ev.setXmlID(eventID);
                ev.setVersion("2.2");
                ev.setEventType("bagit");
                ev.setEventDateTime(formattedDate);            
                ev.setEventDetail("files deleted from bagit");
                for(String file:diff.getDeleted()){
                    PremisEvent.PremisLinkingObjectIdentifier oid = new PremisEvent.PremisLinkingObjectIdentifier();
                    oid.setLinkingObjectIdentifierType("URL");
                    oid.setLinkingObjectIdentifierValue(file);
                    ev.getLinkingObjectIdentifier().add(oid);
                }            
                premis.getEvent().add(ev);
            }
        }   
                
        try{            
            AmdSec amdSec = PremisUtils.getAmdSecBagit((ArrayList<AmdSec>)mets.getAmdSec());
            if(amdSec == null){
                amdSec = new AmdSec();
                amdSec.setID("bagit");
                mets.getAmdSec().add(amdSec);
            }
            
            PremisUtils.cleanupDigiprovMD((ArrayList<MdSec>)amdSec.getDigiprovMD());
            
            MdSec mdSec = new MdSec("bagit_digiprovMD");            
            MdWrap mdWrap = new MdWrap(MdSec.MDTYPE.PREMIS);
            mdWrap.setMDTYPEVERSION("2.2");            
            mdWrap.getXmlData().add(PremisIO.toDocument(premis).getDocumentElement());            
            mdWrap.setMIMETYPE("text/xml");
            mdSec.setGROUPID(MdSec.MDTYPE.PREMIS.toString());         
            if(mdSec.getCREATED() == null){
                try{
                    mdSec.setCREATEDATE(DateUtils.DateToGregorianCalender());
                }catch(Exception e){
                    log.error(e.getMessage());
                    e.printStackTrace();
                }            
            }            
            mdSec.setMdWrap(mdWrap);
            amdSec.getDigiprovMD().add(mdSec);
        
        }catch(Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }        
    }    
}
