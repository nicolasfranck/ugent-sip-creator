package ugent.premis;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author nicolas
 */
public class PremisEvent implements ElementInterface{
    
    private PremisEventIdentifier eventIdentifier;
    private String eventType;
    private String eventDateTime;
    private String eventDetail;
    private ArrayList<PremisEventOutcomeInformation>eventOutcomeInformation;      
    private ArrayList<PremisLinkingAgentIdentifier>linkingAgentIdentifier;
    private ArrayList<PremisLinkingObjectIdentifier>linkingObjectIdentifier;
    
    //attributes
    private String xmlID;
    private String version;    

    public ArrayList<PremisLinkingAgentIdentifier> getLinkingAgentIdentifier() {
        if(linkingAgentIdentifier == null){
            linkingAgentIdentifier = new ArrayList<PremisLinkingAgentIdentifier>();
        }
        return linkingAgentIdentifier;
    }
    public ArrayList<PremisLinkingObjectIdentifier> getLinkingObjectIdentifier() {
        if(linkingObjectIdentifier == null){
            linkingObjectIdentifier = new ArrayList<PremisLinkingObjectIdentifier>();
        }
        return linkingObjectIdentifier;
    }   
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }    
    public PremisEventIdentifier getEventIdentifier() {
        return eventIdentifier;
    }
    public void setEventIdentifier(PremisEventIdentifier eventIdentifier) {
        this.eventIdentifier = eventIdentifier;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String getXmlID() {
        return xmlID;
    }

    public void setXmlID(String xmlID) {
        this.xmlID = xmlID;
    }

    public ArrayList<PremisEventOutcomeInformation> getEventOutcomeInformation() {
        if(eventOutcomeInformation == null){
            eventOutcomeInformation = new ArrayList<PremisEventOutcomeInformation>();
        }
        return eventOutcomeInformation;
    }
    

    @Override
    public void unmarshal(Element root) throws ParseException {
        DateFormat dformatter = DateFormat.getDateInstance();
        NamedNodeMap attrs = root.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr) attrs.item(i);
            String name = attr.getName();
            String value = attr.getNodeValue();
            if(name.equals("xmlID")){
                xmlID = value;                
            }else if(name.equals("version")){
                version = value;
            }
        }
        ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
        for(Element child:children) {
            String localName = child.getLocalName();
            if(localName.equals("eventIdentifier")) {
                eventIdentifier = new PremisEventIdentifier();
                eventIdentifier.unmarshal(child);                
            }else if(localName.equals("eventType")){
                eventType = child.getTextContent();
            }else if(localName.equals("eventDateTime")){                
                eventDateTime = child.getTextContent();                
            }else if(localName.equals("eventDetail")){                
                eventDetail = child.getTextContent();                
            }else if(localName.equals("eventOutcomeInformation")){                
                PremisEventOutcomeInformation inf = new PremisEventOutcomeInformation();
                inf.unmarshal(child);
                getEventOutcomeInformation().add(inf);
            }else if(localName.equals("linkingAgentIdentifier")){                
                PremisLinkingAgentIdentifier id = new PremisLinkingAgentIdentifier();
                id.unmarshal(child);
                getLinkingAgentIdentifier().add(id);
            }else if(localName.equals("linkingObjectIdentifier")){                
                PremisLinkingObjectIdentifier id = new PremisLinkingObjectIdentifier();
                id.unmarshal(child);
                getLinkingObjectIdentifier().add(id);
            }
        }
    }

    @Override
    public void marshal(Element root, Document doc) {
        if(xmlID != null && !xmlID.isEmpty()){
            root.setAttribute("xmlID",xmlID);
        }
        if(version != null && !version.isEmpty()){
            root.setAttribute("version",version);
        }else{
            root.setAttribute("version","2.2");
        }
        
        
        Element evi = doc.createElementNS(NS.PREMIS.ns(),"premis:eventIdentifier");
        if(eventIdentifier != null){
            eventIdentifier.marshal(evi,doc);
        }            
        root.appendChild(evi);        
        
        Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:eventType");
        e.setTextContent(eventType);
        root.appendChild(e);        
        
        Element datee = doc.createElementNS(NS.PREMIS.ns(),"premis:eventDateTime");       
        datee.setTextContent(eventDateTime);
        root.appendChild(datee);        
        
        Element dete = doc.createElementNS(NS.PREMIS.ns(),"premis:eventDetail");
        dete.setTextContent(eventDetail);
        root.appendChild(dete);
        
        for(PremisEventOutcomeInformation info:getEventOutcomeInformation()){
            Element ie = doc.createElementNS(NS.PREMIS.ns(),"premis:eventOutcomeInformation");
            info.marshal(ie,doc);
            root.appendChild(ie);
        }
        for(PremisLinkingAgentIdentifier laid:getLinkingAgentIdentifier()){
            Element le = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingAgentIdentifier");
            laid.marshal(le,doc);
            root.appendChild(le);
        }
        for(PremisLinkingObjectIdentifier loid:getLinkingObjectIdentifier()){
            Element le = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingObjectIdentifier");
            loid.marshal(le,doc);
            root.appendChild(le);
        }
    }

    
    
    public static class PremisEventIdentifier implements ElementInterface{
        private String eventIdentifierType;
        private String eventIdentifierValue;

        public String getEventIdentifierType() {
            return eventIdentifierType;
        }
        public void setEventIdentifierType(String eventIdentifierType) {
            this.eventIdentifierType = eventIdentifierType;
        }
        public String getEventIdentifierValue() {
            return eventIdentifierValue;
        }
        public void setEventIdentifierValue(String eventIdentifierValue) {
            this.eventIdentifierValue = eventIdentifierValue;
        }       

        @Override
        public void unmarshal(Element root) throws ParseException {
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("eventIdentifierType")){
                    eventIdentifierType = child.getTextContent();
                }else if(localName.equals("eventIdentifierValue")){
                    eventIdentifierValue = child.getTextContent();
                }
            }
        }
        @Override
        public void marshal(Element root, Document doc) {
            if(eventIdentifierType != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:eventIdentifierType");
                e.setTextContent(eventIdentifierType);
                root.appendChild(e);
            }
            if(eventIdentifierValue != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:eventIdentifierValue");
                e.setTextContent(eventIdentifierValue);
                root.appendChild(e);
            }
        }
    }
    public static class PremisEventOutcomeInformation implements ElementInterface{
        private String eventOutcome;
        private ArrayList<Element> eventOutcomeDetail;
        private ArrayList<MdSec> mdSec;
        
        public ArrayList<MdSec> getMdSec() {
            if(mdSec == null){
                mdSec = new ArrayList<MdSec>();
            }
            return mdSec;
        }
        public String getEventOutcome() {
            return eventOutcome;
        }
        public void setEventOutcome(String eventOutcome) {
            this.eventOutcome = eventOutcome;
        }
        public ArrayList<Element> getEventOutcomeDetail() {
            if(eventOutcomeDetail == null){
                eventOutcomeDetail = new ArrayList<Element>();
            }
            return eventOutcomeDetail;
        }               

        @Override
        public void unmarshal(Element root) throws ParseException {
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("eventOutcome")) {
                    eventOutcome = child.getTextContent();
                }else if(localName.equals("eventOutcomeDetail")) {
                    getEventOutcomeDetail().add(child);                    
                }else if(localName.equals("mdSec")) {
                    MdSec m = new MdSec("");
                    m.unmarshal(child);
                    getMdSec().add(m);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {
            if(eventOutcome != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:eventOutcome");
                e.setTextContent(eventOutcome);
                root.appendChild(e);
            }
            for(Element element:getEventOutcomeDetail()){
                root.appendChild(element);
            }
            for(MdSec m:getMdSec()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:mdSec");
                m.marshal(e,doc);
                root.appendChild(e);
            }
        }
    }
    public static class PremisLinkingAgentIdentifier implements ElementInterface{
        private String linkAgentXmlID;
        private String linkingAgentIdentifierType;
        private String linkingAgentIdentifierValue;
        private ArrayList<String>linkingAgentRoles;        

        public String getLinkAgentXmlID() {
            return linkAgentXmlID;
        }
        public void setLinkAgentXmlID(String linkAgentXmlID) {
            this.linkAgentXmlID = linkAgentXmlID;
        }       
        public String getLinkingAgentIdentifierType() {
            return linkingAgentIdentifierType;
        }
        public void setLinkingAgentIdentifierType(String linkingAgentIdentifierType) {
            this.linkingAgentIdentifierType = linkingAgentIdentifierType;
        }
        public String getLinkingAgentIdentifierValue() {
            return linkingAgentIdentifierValue;
        }
        public void setLinkingAgentIdentifierValue(String linkingAgentIdentifierValue) {
            this.linkingAgentIdentifierValue = linkingAgentIdentifierValue;
        }

        public ArrayList<String> getLinkingAgentRoles() {
            if(linkingAgentRoles == null){
                linkingAgentRoles = new ArrayList<String>();
            }
            return linkingAgentRoles;
        }       

        @Override
        public void unmarshal(Element root) throws ParseException {
            NamedNodeMap attrs = root.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Attr attr = (Attr) attrs.item(i);
                String name = attr.getName();
                String value = attr.getNodeValue();
                if(name.equals("LinkAgentXmlID")){
                    linkAgentXmlID = value;
                    break;
                }
            }
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("linkingAgentIdentifierType")) {
                    linkingAgentIdentifierType = child.getTextContent();
                }else if(localName.equals("linkingAgentIdentifierValue")) {
                    linkingAgentIdentifierValue = child.getTextContent();
                }else if(localName.equals("linkingAgentRole")) {
                    getLinkingAgentRoles().add(child.getTextContent());
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {
            //attributes
            if(linkAgentXmlID != null){
                root.setAttribute("LinkAgentXmlID",linkAgentXmlID);
            }
            
            //elements
            if(linkingAgentIdentifierType != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingAgentIdentifierType");
                e.setTextContent(linkingAgentIdentifierType);
                root.appendChild(e);
            }
            if(linkingAgentIdentifierValue != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingAgentIdentifierValue");
                e.setTextContent(linkingAgentIdentifierValue);
                root.appendChild(e);
            }
            for(String role:getLinkingAgentRoles()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingAgentRole");
                e.setTextContent(role);
                root.appendChild(e);
            }
        }
        
    }
    public static class PremisLinkingObjectIdentifier implements ElementInterface{
        private String linkingObjectIdentifierType;
        private String linkingObjectIdentifierValue;
        private String linkObjectXmlID;

        public String getLinkingObjectIdentifierType() {
            return linkingObjectIdentifierType;
        }
        public void setLinkingObjectIdentifierType(String linkingObjectIdentifierType) {
            this.linkingObjectIdentifierType = linkingObjectIdentifierType;
        }
        public String getLinkingObjectIdentifierValue() {
            return linkingObjectIdentifierValue;
        }
        public void setLinkingObjectIdentifierValue(String linkingObjectIdentifierValue) {
            this.linkingObjectIdentifierValue = linkingObjectIdentifierValue;
        }
        public String getLinkObjectXmlID() {
            return linkObjectXmlID;
        }
        public void setLinkObjectXmlID(String linkObjectXmlID) {
            this.linkObjectXmlID = linkObjectXmlID;
        }        

        @Override
        public void unmarshal(Element root) throws ParseException {
            NamedNodeMap attrs = root.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Attr attr = (Attr) attrs.item(i);
                String name = attr.getName();
                String value = attr.getNodeValue();
                if(name.equals("LinkObjectXmlID")){
                    linkObjectXmlID = value;
                    break;
                }
            }
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("linkingObjectIdentifierType")) {
                    linkingObjectIdentifierType = child.getTextContent();
                }else if(localName.equals("linkingObjectIdentifierValue")) {
                    linkingObjectIdentifierValue = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {
            //attributes
            if(linkObjectXmlID != null){
                root.setAttribute("LinkObjectXmlID",linkObjectXmlID);
            }
            
            //elements
            if(linkingObjectIdentifierType != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingObjectIdentifierType");
                e.setTextContent(linkingObjectIdentifierType);
                root.appendChild(e);
            }
            if(linkingObjectIdentifierValue != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingObjectIdentifierValue");
                e.setTextContent(linkingObjectIdentifierValue);
                root.appendChild(e);
            }             
        }
    }    
}
