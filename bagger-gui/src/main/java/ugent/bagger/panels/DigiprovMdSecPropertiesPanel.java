package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ugent.bagger.helper.MetsUtils;
import ugent.premis.Premis;
import ugent.premis.PremisEvent;
import ugent.premis.PremisIO;
import ugent.premis.PremisObject;

/**
 *
 * @author nicolas
 */
public class DigiprovMdSecPropertiesPanel extends MdSecPropertiesPanel{
    public DigiprovMdSecPropertiesPanel(ArrayList<MdSec>data){        
        super(data);        
    }
    @Override
    protected int getMax(){
        return 1;
    }
    protected void init(){
        addPropertyChangeListener("mdSec",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                
                BagView bagView = BagView.getInstance();
                MetsBag metsBag = bagView.getBag();
                Premis premis = metsBag.getPremis();
                
                //indien premis-record, dan deze verwerken!
                MdSec mdSec = (MdSec) pce.getNewValue();
                if(
                    mdSec == null || mdSec.getMdWrap() == null || 
                    mdSec.getMdWrap().getXmlData() == null || mdSec.getMdWrap().getXmlData().size() == 0
                        
                ){
                    return;
                }                
                Element element = mdSec.getMdWrap().getXmlData().get(0);
                String namespace = element.getNamespaceURI();
                if(!namespace.equals(MetsUtils.getNamespaceMap().get("PREMIS"))){
                    return;
                }           
                
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
                     *  objects met type 'bagit' behoren toe aan 'bagit'!
                     */                    
                    ArrayList<PremisObject>bagitObjects = new ArrayList<PremisObject>();
                    for(PremisObject object:premis.getObject()){
                        if(object.getType() != null && object.getType().equals("bagit")){
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
                    
                    Document doc = PremisIO.toDocument(premis);
                    
                    mdSec.getMdWrap().getXmlData().set(0,doc.getDocumentElement());
                    
                    
                }catch(ParseException e){
                    e.printStackTrace();
                }catch(ParserConfigurationException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
