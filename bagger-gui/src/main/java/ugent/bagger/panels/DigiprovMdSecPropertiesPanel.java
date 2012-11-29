package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import ugent.bagger.helper.PremisUtils;
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
    public DigiprovMdSecPropertiesPanel(ArrayList<MdSec>data){        
        super(data);        
        init();
    }
    @Override
    protected int getMax(){
        return 1;
    }
    protected void init(){
        addPropertyChangeListener("remove",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                System.out.println("property name:"+pce.getPropertyName());
                System.out.println("property value:"+pce.getNewValue());
                
                BagView bagView = BagView.getInstance();
                MetsBag metsBag = bagView.getBag();
                Premis premis = metsBag.getPremis();
                
                //indien premis-record, dan deze verwerken!
                MdSec mdSec = (MdSec) pce.getNewValue();
                
                System.out.println("is premis? => "+mdSec);
                
                if(!PremisUtils.isPremisMdSec(mdSec)){
                    return;
                }
                
                System.out.println("is premis!");
                Element element = mdSec.getMdWrap().getXmlData().get(0);   
                
                //cleanup
                premis.getAgent().clear();
                premis.getRights().clear();
                
                Iterator<PremisObject>itObject = premis.getObject().iterator();
                while(itObject.hasNext()){
                    PremisObject object = itObject.next();
                    if(object.getType() != PremisObjectType.bitstream){
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
        addPropertyChangeListener("mdSec",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                
                System.out.println("property name:"+pce.getPropertyName());
                System.out.println("property value:"+pce.getNewValue());
                
                BagView bagView = BagView.getInstance();
                MetsBag metsBag = bagView.getBag();
                Premis premis = metsBag.getPremis();
                
                //indien premis-record, dan deze verwerken!
                MdSec mdSec = (MdSec) pce.getNewValue();
                
                System.out.println("is premis?");
                
                if(!PremisUtils.isPremisMdSec(mdSec)){
                    return;
                }
                
                System.out.println("is premis!");
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
                    
                    /*
                     *  objects met type 'bitstream' en xmlID 'bagit' behoren toe aan 'bagit'!
                     */                    
                    ArrayList<PremisObject>bagitObjects = new ArrayList<PremisObject>();
                    for(PremisObject object:premis.getObject()){                        
                        if(object.getType() == PremisObjectType.bitstream && object.getXmlID() != null && object.getXmlID().equals("bagit")){
                            bagitObjects.add(object);
                        }
                    }
                    premis.getObject().clear();                    
                    premis.getObject().addAll(newPremis.getObject());                    
                    premis.getObject().addAll(bagitObjects);
                    
                    /*
                     *  events met type 'bagit' behoren toe aan 'bagit'!
                     */
                    ArrayList<PremisEvent>bagitEvents = new ArrayList<PremisEvent>();
                    for(PremisEvent event:premis.getEvent()){                        
                        if(event.getEventType() != null && event.getEventType().equals("bagit")){
                            bagitEvents.add(event);
                        }
                    }
                    premis.getEvent().clear();                  
                    premis.getEvent().addAll(newPremis.getEvent());
                    premis.getEvent().addAll(bagitEvents);    
                    
                    mdSec.setID("bagit");
                    mdSec.getMdWrap().getXmlData().set(0,PremisIO.toDocument(premis).getDocumentElement());
                    
                    
                }catch(ParseException e){
                    e.printStackTrace();
                }catch(ParserConfigurationException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
