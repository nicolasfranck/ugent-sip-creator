package ugent.premis;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ugent.bagger.helper.XML;
import ugent.premis.PremisEvent.PremisLinkingAgentIdentifier;
import ugent.premis.PremisEvent.PremisLinkingObjectIdentifier;

/**
 *
 * @author nicolas
 */
public class Premis implements ElementInterface{      
    
    private ArrayList<PremisObject>object;
    private ArrayList<PremisEvent>event;
    private ArrayList<PremisAgent>agent;
    private ArrayList<PremisRights>rights;

    public ArrayList<PremisObject> getObject() {
        if(object == null){
            object = new ArrayList<PremisObject>();
        }
        return object;
    }
    public ArrayList<PremisEvent> getEvent() {
        if(event == null){
            event = new ArrayList<PremisEvent>();
        }
        return event;
    }
    public ArrayList<PremisAgent> getAgent() {
        if(agent == null){
            agent = new ArrayList<PremisAgent>();
        }
        return agent;
    }
    public ArrayList<PremisRights> getRights() {
        if(rights == null){
            rights = new ArrayList<PremisRights>();
        }
        return rights;
    }

    @Override
    public void unmarshal(Element root) throws ParseException {
        ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
        for(Element child:children) {
            String localName = child.getLocalName();
            if(localName.equals("event")) {
                PremisEvent event = new PremisEvent();
                event.unmarshal(child);
                getEvent().add(event);
            }else if(localName.equals("object")){
                PremisObject object = new PremisObject();
                object.unmarshal(child);
                getObject().add(object);                
            }else if(localName.equals("agent")){
                PremisAgent agent = new PremisAgent();
                agent.unmarshal(child);
                getAgent().add(agent);                
            }
            else if(localName.equals("rights")){
                PremisRights rights = new PremisRights();
                rights.unmarshal(child);
                getRights().add(rights);                
            }
        }
    }

    @Override
    public void marshal(Element root, Document doc) {       
        
        // set up namespace declarations and schema references
        root.setAttribute("xmlns:premis",NS.PREMIS.ns());
        root.setAttribute("xmlns:xlink", NS.XLINK.ns());
        root.setAttribute("xmlns:xsi", NS.XSI.ns());
        root.setAttributeNS(NS.XSI.ns(),"xsi:schemaLocation",NS.PREMIS.schemaLoc());
        
        
        for(PremisEvent event:getEvent()){
            Element eventE = doc.createElementNS(NS.PREMIS.ns(),"premis:event");
            event.marshal(eventE,doc);
            root.appendChild(eventE);
        }        
        for(PremisAgent agent:getAgent()){
            Element agentE = doc.createElementNS(NS.PREMIS.ns(),"premis:agent");
            agent.marshal(agentE,doc);
            root.appendChild(agentE);
        }
        for(PremisObject object:getObject()){
            Element objectE = doc.createElementNS(NS.PREMIS.ns(),"premis:object");
            object.marshal(objectE,doc);
            root.appendChild(objectE);
        }
        for(PremisRights rights:getRights()){
            Element rightsE = doc.createElementNS(NS.PREMIS.ns(),"premis:rights");
            rights.marshal(rightsE,doc);
            root.appendChild(rightsE);
        }
        
    }
    public static void main(String [] args){
        try{
            Document doc = XML.XMLToDocument(new File("/home/nicolas/premis.xml"));
            System.out.println("document loaded"); 
            Premis premis = new Premis();
            premis.unmarshal(doc.getDocumentElement());
            
            ArrayList<PremisEvent> events = premis.getEvent();
            for(PremisEvent event:events){
                System.out.println("xmlID:"+event.getXmlID());
                System.out.println("eventDetail:"+event.getEventDetail());
                System.out.println("eventDateTime:"+event.getEventDateTime());
                System.out.println("eventType:"+event.getEventType());
                System.out.println("version:"+event.getVersion());
                
                ArrayList<PremisLinkingAgentIdentifier> laids = event.getLinkingAgentIdentifier();
                
                for(PremisLinkingAgentIdentifier laid:laids){
                    System.out.println("LinkAgentXmlID: "+laid.getLinkAgentXmlID());
                    System.out.println("LinkingAgentIdentifierType: "+laid.getLinkingAgentIdentifierType());
                    System.out.println("LinkingAgentIdentifierValue: "+laid.getLinkingAgentIdentifierValue());                    
                }
                
                ArrayList<PremisLinkingObjectIdentifier> loids = event.getLinkingObjectIdentifier();
                
                for(PremisLinkingObjectIdentifier loid:loids){
                    System.out.println("LinkObjectXmlID:"+loid.getLinkObjectXmlID());
                    System.out.println("LinkingObjectIdentifierType:"+loid.getLinkingObjectIdentifierType());
                    System.out.println("LinkingObjectIdentifierValue:"+loid.getLinkObjectXmlID());
                }
                
                System.out.println();
            }
            
            PremisIO.write(premis,System.out,true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
