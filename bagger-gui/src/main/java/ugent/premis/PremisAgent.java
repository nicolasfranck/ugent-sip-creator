package ugent.premis;

import com.anearalone.mets.MdSec;
import java.text.ParseException;
import java.util.ArrayList;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import ugent.premis.PremisObject.PremisLinkingEventIdentifier;
import ugent.premis.PremisObject.PremisLinkingRightsStatementIdentifier;

/**
 *
 * @author nicolas
 */
public class PremisAgent implements ElementInterface{
    //attribute
    String xmlID;
    String version;
    ArrayList<PremisAgentIdentifier>agentIdentifier;
    ArrayList<String>agentName;
    ArrayList<String>agentType;
    ArrayList<String>agentNote;
    ArrayList<Element>agentExtension;
    ArrayList<MdSec>mdSec;    
    ArrayList<PremisObject.PremisLinkingEventIdentifier>linkingEventIdentifier;
    ArrayList<PremisObject.PremisLinkingRightsStatementIdentifier>linkingRightsStatementIdentifier;
   
    public String getXmlID() {
        return xmlID;
    }
    public void setXmlID(String xmlID) {
        this.xmlID = xmlID;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<PremisAgentIdentifier> getAgentIdentifier() {
        if(agentIdentifier == null){
            agentIdentifier = new ArrayList<PremisAgentIdentifier>();
        }
        return agentIdentifier;
    }

    public ArrayList<String> getAgentName() {
        if(agentName == null){
            agentName = new ArrayList<String>();
        }
        return agentName;
    }

    public ArrayList<String> getAgentType() {
        if(agentType == null){
            agentType = new ArrayList<String>();
        }
        return agentType;
    }

    public ArrayList<String> getAgentNote() {
        if(agentNote == null){
            agentNote = new ArrayList<String>();
        }
        return agentNote;
    }

    public ArrayList<Element> getAgentExtension() {
        if(agentExtension == null){
            agentExtension = new ArrayList<Element>();
        }
        return agentExtension;
    }

    public ArrayList<MdSec> getMdSec() {
        if(mdSec == null){
            mdSec = new ArrayList<MdSec>();
        }
        return mdSec;
    }

    public ArrayList<PremisLinkingEventIdentifier> getLinkingEventIdentifier() {
        if(linkingEventIdentifier == null){
            linkingEventIdentifier = new ArrayList<PremisLinkingEventIdentifier>();
        }
        return linkingEventIdentifier;
    }

    public ArrayList<PremisLinkingRightsStatementIdentifier> getLinkingRightsStatementIdentifier() {
        if(linkingRightsStatementIdentifier == null){
            linkingRightsStatementIdentifier = new ArrayList<PremisLinkingRightsStatementIdentifier>();
        }
        return linkingRightsStatementIdentifier;
    }
    
    @Override
    public void unmarshal(Element root) throws ParseException {
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
            if(localName.equals("agentIdentifier")) {
                PremisAgentIdentifier id = new PremisAgentIdentifier();
                id.unmarshal(child);
                getAgentIdentifier().add(id);
            }else if(localName.equals("agentName")) {
                getAgentName().add(child.getTextContent());
            }else if(localName.equals("agentType")) {
                getAgentType().add(child.getTextContent());
            }else if(localName.equals("agentNote")) {
                getAgentNote().add(child.getTextContent());
            }else if(localName.equals("agentExtension")) {
                getAgentExtension().add((Element)child.getFirstChild());
            }else if(localName.equals("mdSec")) {
                MdSec m = new MdSec("");
                m.unmarshal(child);
                getMdSec().add(m);
            }else if(localName.equals("linkingEventIdentifier")) {
                PremisLinkingEventIdentifier id = new PremisLinkingEventIdentifier();
                id.unmarshal(child);
                getLinkingEventIdentifier().add(id);
            }else if(localName.equals("linkingRightsStatementIdentifier")) {
                PremisLinkingRightsStatementIdentifier id = new PremisLinkingRightsStatementIdentifier();
                id.unmarshal(child);
                getLinkingRightsStatementIdentifier().add(id);
            }
        }
    }

    @Override
    public void marshal(Element root, Document doc) {
        
    }
    public static class PremisAgentIdentifier implements ElementInterface{
        String agentIdentifierType;
        String agentIdentifierValue;

        public String getAgentIdentifierType() {
            return agentIdentifierType;
        }

        public void setAgentIdentifierType(String agentIdentifierType) {
            this.agentIdentifierType = agentIdentifierType;
        }

        public String getAgentIdentifierValue() {
            return agentIdentifierValue;
        }

        public void setAgentIdentifierValue(String agentIdentifierValue) {
            this.agentIdentifierValue = agentIdentifierValue;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("agentIdentifierType")) {
                    agentIdentifierType = child.getTextContent();                    
                }else if(localName.equals("agentIdentifierValue")) {
                    agentIdentifierValue = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {
            
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:agentIdentifierType");
            t.setTextContent(agentIdentifierType);
            root.appendChild(t);
           
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:agentIdentifierValue");
            v.setTextContent(agentIdentifierValue);
            root.appendChild(v);
                   
        }
    }
}
