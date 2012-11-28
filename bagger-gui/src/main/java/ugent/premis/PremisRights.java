package ugent.premis;

import com.anearalone.mets.MdSec;
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
public class PremisRights implements ElementInterface {    
    //attributes
    private String xmlID;
    private String version;
    //elements
    ArrayList<PremisRightsStatement>rightsStatement;
    ArrayList<Element>rightsExtension;
    ArrayList<MdSec>mdSec;

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }    
    public String getXmlID() {
        return xmlID;
    }
    public void setXmlID(String xmlID) {
        this.xmlID = xmlID;
    }

    public ArrayList<PremisRightsStatement> getRightsStatement() {
        if(rightsStatement == null){
            rightsStatement = new ArrayList<PremisRightsStatement>();
        }
        return rightsStatement;
    }

    public ArrayList<Element> getRightsExtension() {
        if(rightsExtension == null){
            rightsExtension = new ArrayList<Element>();
        }
        return rightsExtension;
    }

    public ArrayList<MdSec> getMdSec() {
        if(mdSec == null){
            mdSec = new ArrayList<MdSec>();
        }
        return mdSec;
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
            if(localName.equals("rightsStatement")) {
                PremisRightsStatement r = new PremisRightsStatement();
                r.unmarshal(child);
                getRightsStatement().add(r);
            }else if(localName.equals("rightsExtension")) {
                getRightsExtension().add((Element) child.getFirstChild());                
            }else if(localName.equals("mdSec")) {
                MdSec m = new MdSec("");
                m.unmarshal(child);
                getMdSec().add(m);
            }
        }
    }

    @Override
    public void marshal(Element root, Document doc) {
        
        root.setAttribute("version",version);
        root.setAttribute("xmlID",xmlID);
        
        for(PremisRightsStatement s:getRightsStatement()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:rightsStatement");
            s.marshal(e,doc);                
            root.appendChild(e);
        }
        for(Element ext:getRightsExtension()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:rightsExtension");
            e.appendChild(doc.importNode(ext,true));
            root.appendChild(e);
        }        
        for(MdSec m:getMdSec()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:mdSec");
            m.marshal(e,doc);                
            root.appendChild(e);
        }
    }
    
    public static class PremisRightsStatement implements ElementInterface{
        PremisRightsStatementIdentifier rightsStatementIdentifier;
        String rightsBasis;
        PremisCopyrightInformation copyrightInformation;
        PremisLicenseInformation licenseInformation;
        ArrayList<PremisStatuteInformation> statuteInformation;
        PremisOtherRightsInformation otherRightsInformation;
        ArrayList<PremisRightsGranted>rightsGranted;
        ArrayList<PremisEvent.PremisLinkingObjectIdentifier>linkingObjectIdentifier;
        ArrayList<PremisEvent.PremisLinkingAgentIdentifier>linkingAgentIdentifier;

        public PremisRightsStatementIdentifier getRightsStatementIdentifier() {
            return rightsStatementIdentifier;
        }

        public void setRightsStatementIdentifier(PremisRightsStatementIdentifier rightsStatementIdentifier) {
            this.rightsStatementIdentifier = rightsStatementIdentifier;
        }

        public String getRightsBasis() {
            return rightsBasis;
        }

        public void setRightsBasis(String rightsBasis) {
            this.rightsBasis = rightsBasis;
        }

        public PremisCopyrightInformation getCopyrightInformation() {
            return copyrightInformation;
        }

        public void setCopyrightInformation(PremisCopyrightInformation copyrightInformation) {
            this.copyrightInformation = copyrightInformation;
        }

        public PremisLicenseInformation getLicenseInformation() {
            return licenseInformation;
        }

        public void setLicenseInformation(PremisLicenseInformation licenseInformation) {
            this.licenseInformation = licenseInformation;
        }

        public ArrayList<PremisStatuteInformation> getStatuteInformation() {
            if(statuteInformation == null){
                statuteInformation = new ArrayList<PremisStatuteInformation>();
            }
            return statuteInformation;
        }

        public PremisOtherRightsInformation getOtherRightsInformation() {
            return otherRightsInformation;
        }

        public void setOtherRightsInformation(PremisOtherRightsInformation otherRightsInformation) {
            this.otherRightsInformation = otherRightsInformation;
        }

        public ArrayList<PremisRightsGranted> getRightsGranted() {
            if(rightsGranted == null){
                rightsGranted = new ArrayList<PremisRightsGranted>();
            }
            return rightsGranted;
        }

        public ArrayList<PremisEvent.PremisLinkingObjectIdentifier> getLinkingObjectIdentifier() {
            if(linkingObjectIdentifier == null){
                linkingObjectIdentifier = new ArrayList<PremisEvent.PremisLinkingObjectIdentifier>();
            }
            return linkingObjectIdentifier;
        }

        public ArrayList<PremisEvent.PremisLinkingAgentIdentifier> getLinkingAgentIdentifier() {
            if(linkingAgentIdentifier == null){
                linkingAgentIdentifier = new ArrayList<PremisEvent.PremisLinkingAgentIdentifier>();
            }
            return linkingAgentIdentifier;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {           
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("rightsStatementIdentifier")) {
                    rightsStatementIdentifier = new PremisRightsStatementIdentifier();
                    rightsStatementIdentifier.unmarshal(child);                    
                }else if(localName.equals("rightsBasis")) {
                    rightsBasis = child.getTextContent();
                }else if(localName.equals("copyrightInformation")) {
                    copyrightInformation = new PremisCopyrightInformation();
                    copyrightInformation.unmarshal(child);
                }else if(localName.equals("licenseInformation")) {
                    licenseInformation = new PremisLicenseInformation();
                    licenseInformation.unmarshal(child);
                }else if(localName.equals("statuteInformation")) {
                    PremisStatuteInformation info = new PremisStatuteInformation();
                    info.unmarshal(child);        
                    getStatuteInformation().add(info);
                }else if(localName.equals("rightsGranted")) {
                    PremisRightsGranted g = new PremisRightsGranted();
                    g.unmarshal(child);
                    getRightsGranted().add(g);
                }else if(localName.equals("linkingObjectIdentifier")) {
                    PremisEvent.PremisLinkingObjectIdentifier id = new PremisEvent.PremisLinkingObjectIdentifier();
                    id.unmarshal(child);
                    getLinkingObjectIdentifier().add(id);
                }else if(localName.equals("linkingAgentIdentifier")) {
                    PremisEvent.PremisLinkingAgentIdentifier id = new PremisEvent.PremisLinkingAgentIdentifier();
                    id.unmarshal(child);
                    getLinkingAgentIdentifier().add(id);
                }                
            }
        }

        @Override
        public void marshal(Element root, Document doc) {        
            if(rightsStatementIdentifier != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:rightsStatementIdentifier");
                rightsStatementIdentifier.marshal(e,doc);                
                root.appendChild(e);
            }            
            
            Element b = doc.createElementNS(NS.PREMIS.ns(),"premis:rightsBasis");
            b.setTextContent(rightsBasis);
            root.appendChild(b);           
            
            if(copyrightInformation != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:copyrightInformation");
                copyrightInformation.marshal(e,doc);                
                root.appendChild(e);
            }
            if(licenseInformation != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseInformation");
                licenseInformation.marshal(e,doc);                
                root.appendChild(e);
            }
            for(PremisStatuteInformation info:getStatuteInformation()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:statuteInformation");
                info.marshal(e,doc);                
                root.appendChild(e);
            }
            for(PremisRightsGranted g:getRightsGranted()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:rightsGranted");
                g.marshal(e,doc);
                root.appendChild(e);
            }
            for(PremisEvent.PremisLinkingObjectIdentifier id:getLinkingObjectIdentifier()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingObjectIdentifier");
                id.marshal(e,doc);
                root.appendChild(e);
            }
            for(PremisEvent.PremisLinkingAgentIdentifier id:getLinkingAgentIdentifier()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingAgentIdentifier");
                id.marshal(e,doc);
                root.appendChild(e);
            }
        }        
        
    }
    public static class PremisRightsStatementIdentifier implements ElementInterface{
        String rightsStatementIdentifierType;
        String rightsStatementIdentifierValue;

        public String getRightsStatementIdentifierType() {
            return rightsStatementIdentifierType;
        }

        public void setRightsStatementIdentifierType(String rightsStatementIdentifierType) {
            this.rightsStatementIdentifierType = rightsStatementIdentifierType;
        }

        public String getRightsStatementIdentifierValue() {
            return rightsStatementIdentifierValue;
        }

        public void setRightsStatementIdentifierValue(String rightsStatementIdentifierValue) {
            this.rightsStatementIdentifierValue = rightsStatementIdentifierValue;
        }       
        @Override
        public void unmarshal(Element root) throws ParseException {           
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("rightsStatementIdentifierType")) {
                    rightsStatementIdentifierType = child.getTextContent();
                }else if(localName.equals("rightsStatementIdentifierValue")) {
                    rightsStatementIdentifierValue = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {           
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:rightsStatementIdentifierType");
            t.setTextContent(rightsStatementIdentifierType);
            root.appendChild(t);
            
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:rightsStatementIdentifierValue");
            v.setTextContent(rightsStatementIdentifierValue);
            root.appendChild(v);           
            
        }
    }
    public static class PremisCopyrightInformation implements ElementInterface{
        String copyrightStatus;
        String copyrightJurisdiction;
        String copyrightStatusDeterminationDate;
        ArrayList<String>copyrightNote;
        ArrayList<PremisCopyrightDocumentationIdentifier>copyrightDocumentationIdentifier;
        PremisStartAndEndDate copyrightApplicableDates;

        public ArrayList<String> getCopyrightNote() {
            if(copyrightNote == null){
                copyrightNote = new ArrayList<String>();
            }
            return copyrightNote;
        }
        public ArrayList<PremisCopyrightDocumentationIdentifier> getCopyrightDocumentationIdentifier() {
            if(copyrightDocumentationIdentifier == null){
                copyrightDocumentationIdentifier = new ArrayList<PremisCopyrightDocumentationIdentifier>();
            }
            return copyrightDocumentationIdentifier;
        }
        public PremisStartAndEndDate getCopyrightApplicableDates() {
            return copyrightApplicableDates;
        }
        public void setCopyrightApplicableDates(PremisStartAndEndDate copyrightApplicableDates) {
            this.copyrightApplicableDates = copyrightApplicableDates;
        }
        public String getCopyrightStatus() {
            return copyrightStatus;
        }
        public void setCopyrightStatus(String copyrightStatus) {
            this.copyrightStatus = copyrightStatus;
        }
        public String getCopyrightJurisdiction() {
            return copyrightJurisdiction;
        }
        public void setCopyrightJurisdiction(String copyrightJurisdiction) {
            this.copyrightJurisdiction = copyrightJurisdiction;
        }
        public String getCopyrightStatusDeterminationDate() {
            return copyrightStatusDeterminationDate;
        }
        public void setCopyrightStatusDeterminationDate(String copyrightStatusDeterminationDate) {
            this.copyrightStatusDeterminationDate = copyrightStatusDeterminationDate;
        }       
        @Override
        public void unmarshal(Element root) throws ParseException {           
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("copyrightStatus")) {
                    copyrightStatus = child.getTextContent();
                }else if(localName.equals("copyrightJurisdiction")) {
                    copyrightJurisdiction = child.getTextContent();
                }else if(localName.equals("copyrightStatusDeterminationDate")) {
                    copyrightStatusDeterminationDate = child.getTextContent();
                }else if(localName.equals("copyrightNote")) {
                    getCopyrightNote().add(child.getTextContent());
                }else if(localName.equals("copyrightDocumentationIdentifier")) {
                    PremisCopyrightDocumentationIdentifier id = new PremisCopyrightDocumentationIdentifier();
                    id.unmarshal(child);
                    getCopyrightDocumentationIdentifier().add(id);
                }else if(localName.equals("copyrightApplicableDates")) {
                    copyrightApplicableDates = new PremisStartAndEndDate();
                    copyrightApplicableDates.unmarshal(child);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {           
            Element s = doc.createElementNS(NS.PREMIS.ns(),"premis:copyrightStatus");
            s.setTextContent(copyrightStatus);
            root.appendChild(s);
            
            Element j = doc.createElementNS(NS.PREMIS.ns(),"premis:copyrightJurisdiction");
            j.setTextContent(copyrightJurisdiction);
            root.appendChild(j);
            
            Element d = doc.createElementNS(NS.PREMIS.ns(),"premis:copyrightStatusDeterminationDate");
            d.setTextContent(copyrightStatusDeterminationDate);
            root.appendChild(d);
            
            for(String note:getCopyrightNote()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:copyrightNote");
                e.setTextContent(note);
                root.appendChild(e);
            }
            for(PremisCopyrightDocumentationIdentifier id:getCopyrightDocumentationIdentifier()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:copyrightDocumentationIdentifier");
                id.marshal(e,doc);
                root.appendChild(e);
            }
            if(copyrightApplicableDates != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:copyrightApplicableDates");
                copyrightApplicableDates.marshal(e,doc);
                root.appendChild(e);
            }
            
        }
    }
    public static class PremisCopyrightDocumentationIdentifier implements ElementInterface{
        String copyrightDocumentationIdentifierType;
        String copyrightDocumentationIdentierValue;
        String copyrightDocumentationRole;

        public String getCopyrightDocumentationIdentifierType() {
            return copyrightDocumentationIdentifierType;
        }
        public void setCopyrightDocumentationIdentifierType(String copyrightDocumentationIdentifierType) {
            this.copyrightDocumentationIdentifierType = copyrightDocumentationIdentifierType;
        }
        public String getCopyrightDocumentationIdentierValue() {
            return copyrightDocumentationIdentierValue;
        }
        public void setCopyrightDocumentationIdentierValue(String copyrightDocumentationIdentierValue) {
            this.copyrightDocumentationIdentierValue = copyrightDocumentationIdentierValue;
        }
        public String getCopyrightDocumentationRole() {
            return copyrightDocumentationRole;
        }
        public void setCopyrightDocumentationRole(String copyrightDocumentationRole) {
            this.copyrightDocumentationRole = copyrightDocumentationRole;
        }        
        @Override
        public void unmarshal(Element root) throws ParseException {           
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("copyrightDocumentationIdentifierType")) {
                    copyrightDocumentationIdentifierType = child.getTextContent();
                }else if(localName.equals("copyrightDocumentationIdentierValue")) {
                    copyrightDocumentationIdentierValue = child.getTextContent();
                }else if(localName.equals("copyrightDocumentationRole")) {
                    copyrightDocumentationRole = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {           
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:copyrightDocumentationIdentifierType");
            t.setTextContent(copyrightDocumentationIdentifierType);
            root.appendChild(t);
            
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:copyrightDocumentationIdentierValue");
            v.setTextContent(copyrightDocumentationIdentierValue);
            root.appendChild(v);
            
            Element r = doc.createElementNS(NS.PREMIS.ns(),"premis:copyrightDocumentationRole");
            r.setTextContent(copyrightDocumentationRole);
            root.appendChild(r);
        }
    }   
    public static class PremisStartAndEndDate implements ElementInterface{
        String startDate;
        String endDate;

        public String getStartDate() {
           return startDate;
        }
        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
        public String getEndDate() {
            return endDate;
        }
        public void setEndDate(String endDate) {
            this.endDate = endDate;
        } 
        
        @Override
        public void unmarshal(Element root) throws ParseException {           
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("startDate")) {
                    startDate = child.getTextContent();
                }else if(localName.equals("endDate")) {
                    endDate = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {           
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:startDate");
            t.setTextContent(startDate);
            root.appendChild(t);
            
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:endDate");
            v.setTextContent(endDate);
            root.appendChild(v);            
        }
    }
    
    public static class PremisLicenseInformation implements ElementInterface {
        PremisLicenseIdentifier licenseIdentifier;
        ArrayList<PremisLicenseDocumentationIdentifier>licenseDocumentationIdentifier;
        String licenseTerms;
        ArrayList<String>licenseNote;
        PremisStartAndEndDate licenseApplicableDates;

        public ArrayList<PremisLicenseDocumentationIdentifier> getLicenseDocumentationIdentifier() {
            if(licenseDocumentationIdentifier == null){
                licenseDocumentationIdentifier = new ArrayList<PremisLicenseDocumentationIdentifier>();
            }
            return licenseDocumentationIdentifier;
        }

        public ArrayList<String> getLicenseNote() {
            if(licenseNote == null){
                licenseNote = new ArrayList<String>();
            }
            return licenseNote;
        }
        public PremisLicenseIdentifier getLicenseIdentifier() {
            return licenseIdentifier;
        }

        public void setLicenseIdentifier(PremisLicenseIdentifier licenseIdentifier) {
            this.licenseIdentifier = licenseIdentifier;
        }

        public String getLicenseTerms() {
            return licenseTerms;
        }
        public void setLicenseTerms(String licenseTerms) {
            this.licenseTerms = licenseTerms;
        }
        public PremisStartAndEndDate getLicenseApplicableDates() {
            return licenseApplicableDates;
        }
        public void setLicenseApplicableDates(PremisStartAndEndDate licenseApplicableDates) {
            this.licenseApplicableDates = licenseApplicableDates;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {               
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("licenseIdentifier")) {
                    licenseIdentifier = new PremisLicenseIdentifier();
                    licenseIdentifier.unmarshal(child);                    
                }else if(localName.equals("licenseDocumentationIdentifier")) {
                    PremisLicenseDocumentationIdentifier id = new PremisLicenseDocumentationIdentifier();
                    id.unmarshal(child);
                    getLicenseDocumentationIdentifier().add(id);                    
                }else if(localName.equals("licenseTerms")) {
                    licenseTerms = child.getTextContent();
                }else if(localName.equals("licenseNote")) {
                    getLicenseNote().add(child.getTextContent());
                }else if(localName.equals("licenseApplicableDates")) {
                    licenseApplicableDates = new PremisStartAndEndDate();
                    licenseApplicableDates.unmarshal(child);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                        
           
            if(licenseIdentifier != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseIdentifier");
                licenseIdentifier.marshal(e,doc);
                root.appendChild(e);          
            }
            for(PremisLicenseDocumentationIdentifier id:getLicenseDocumentationIdentifier()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseDocumentationIdentifier");
                id.marshal(e,doc);
                root.appendChild(e);          
            }
            
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseTerms");
            t.setTextContent(licenseTerms);
            root.appendChild(t);         
            
            for(String note:getLicenseNote()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseNote");
                e.setTextContent(note);
                root.appendChild(e);          
            }
            if(licenseApplicableDates != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseApplicableDates");
                licenseApplicableDates.marshal(e,doc);
                root.appendChild(e);          
            }
        }
        
    }
    public static class PremisLicenseIdentifier implements ElementInterface{
        String licenseIdentifierType;
        String licenseIdentifierValue;

        public String getLicenseIdentifierType() {
            return licenseIdentifierType;
        }

        public void setLicenseIdentifierType(String licenseIdentifierType) {
            this.licenseIdentifierType = licenseIdentifierType;
        }

        public String getLicenseIdentifierValue() {
            return licenseIdentifierValue;
        }

        public void setLicenseIdentifierValue(String licenseIdentifierValue) {
            this.licenseIdentifierValue = licenseIdentifierValue;
        }  
        @Override
        public void unmarshal(Element root) throws ParseException {               
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("licenseIdentifierType")) {
                    licenseIdentifierType = child.getTextContent();                    
                }else if(localName.equals("licenseIdentifierValue")) {
                    licenseIdentifierValue = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                        
           
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseIdentifierType");
            t.setTextContent(licenseIdentifierType);
            root.appendChild(t);          
            
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseIdentifierValue");
            v.setTextContent(licenseIdentifierValue);
            root.appendChild(v);           
        }
    }
    public static class PremisLicenseDocumentationIdentifier implements ElementInterface{
        String licenseDocumentationIdentifierType;
        String licenseDocumentationIdentifierValue;
        String licenseDocumentationRole;

        public String getLicenseDocumentationIdentifierType() {
            return licenseDocumentationIdentifierType;
        }

        public void setLicenseDocumentationIdentifierType(String licenseDocumentationIdentifierType) {
            this.licenseDocumentationIdentifierType = licenseDocumentationIdentifierType;
        }

        public String getLicenseDocumentationIdentifierValue() {
            return licenseDocumentationIdentifierValue;
        }

        public void setLicenseDocumentationIdentifierValue(String licenseDocumentationIdentifierValue) {
            this.licenseDocumentationIdentifierValue = licenseDocumentationIdentifierValue;
        }

        public String getLicenseDocumentationRole() {
            return licenseDocumentationRole;
        }

        public void setLicenseDocumentationRole(String licenseDocumentationRole) {
            this.licenseDocumentationRole = licenseDocumentationRole;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {               
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("licenseDocumentationIdentifierType")) {
                    licenseDocumentationIdentifierType = child.getTextContent();                    
                }else if(localName.equals("licenseDocumentationIdentifierValue")) {
                    licenseDocumentationIdentifierValue = child.getTextContent();
                }else if(localName.equals("licenseDocumentationRole")) {
                    licenseDocumentationRole = child.getTextContent();                    
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                        
           
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseDocumentationIdentifierType");
            t.setTextContent(licenseDocumentationIdentifierType);
            root.appendChild(t);          
            
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseDocumentationIdentifierValue");
            v.setTextContent(licenseDocumentationIdentifierValue);
            root.appendChild(v);
            
            Element r = doc.createElementNS(NS.PREMIS.ns(),"premis:licenseDocumentationRole");
            r.setTextContent(licenseDocumentationRole);
            root.appendChild(r);
            
        }
    }
    public static class PremisStatuteInformation implements ElementInterface{
        String statuteJurisdiction;
        String statuteCitation;
        String statuteInformationDeterminationDate;
        ArrayList<String>statuteNote;
        ArrayList<PremisStatuteDocumentIdentifier>statuteDocumentationIdentifier;
        PremisStartAndEndDate statuteApplicableDates;

        public ArrayList<String> getStatuteNote() {
            if(statuteNote == null){
                statuteNote = new ArrayList<String>();
            }
            return statuteNote;
        }

        public ArrayList<PremisStatuteDocumentIdentifier> getStatuteDocumentationIdentifier() {
            if(statuteDocumentationIdentifier == null){
                statuteDocumentationIdentifier = new ArrayList<PremisStatuteDocumentIdentifier>();
            }
            return statuteDocumentationIdentifier;
        }

        
        public String getStatuteJurisdiction() {
            return statuteJurisdiction;
        }

        public void setStatuteJurisdiction(String statuteJurisdiction) {
            this.statuteJurisdiction = statuteJurisdiction;
        }

        public String getStatuteCitation() {
            return statuteCitation;
        }

        public void setStatuteCitation(String statuteCitation) {
            this.statuteCitation = statuteCitation;
        }

        public String getStatuteInformationDeterminationDate() {
            return statuteInformationDeterminationDate;
        }

        public void setStatuteInformationDeterminationDate(String statuteInformationDeterminationDate) {
            this.statuteInformationDeterminationDate = statuteInformationDeterminationDate;
        }

        public PremisStartAndEndDate getStatuteApplicableDates() {
            return statuteApplicableDates;
        }

        public void setStatuteApplicableDates(PremisStartAndEndDate statuteApplicableDates) {
            this.statuteApplicableDates = statuteApplicableDates;
        }
        
        @Override
        public void unmarshal(Element root) throws ParseException {               
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("statuteJurisdiction")) {
                    statuteJurisdiction = child.getTextContent();                    
                }else if(localName.equals("statuteCitation")) {
                    statuteCitation = child.getTextContent();
                }else if(localName.equals("statuteInformationDeterminationDate")) {
                    statuteInformationDeterminationDate = child.getTextContent();                    
                }else if(localName.equals("statuteNote")) {
                    getStatuteNote().add(child.getTextContent());                    
                }else if(localName.equals("statuteDocumentationIdentifier")) {
                    PremisStatuteDocumentIdentifier id = new PremisStatuteDocumentIdentifier();                    
                    id.unmarshal(child);
                    getStatuteDocumentationIdentifier().add(id);
                }else if(localName.equals("statuteApplicableDates")) {
                    statuteApplicableDates = new PremisStartAndEndDate();
                    statuteApplicableDates.unmarshal(child);                    
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                        
           
            Element j = doc.createElementNS(NS.PREMIS.ns(),"premis:statuteJurisdiction");
            j.setTextContent(statuteJurisdiction);
            root.appendChild(j);          
                
            Element c = doc.createElementNS(NS.PREMIS.ns(),"premis:statuteCitation");
            c.setTextContent(statuteCitation);
            root.appendChild(c);          
            
            if(statuteInformationDeterminationDate != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:statuteInformationDeterminationDate");
                e.setTextContent(statuteInformationDeterminationDate);                
                root.appendChild(e);          
            }
            
            for(String note:getStatuteNote()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:statuteNote");
                e.setTextContent(note);                
                root.appendChild(e);          
            }
            
            for(PremisStatuteDocumentIdentifier id:getStatuteDocumentationIdentifier()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:statuteDocumentationIdentifier");
                id.marshal(e,doc);                
                root.appendChild(e);          
            }            
            
            if(statuteApplicableDates != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:statuteApplicableDates");
                statuteApplicableDates.marshal(e,doc);
                root.appendChild(e);          
            }
        }
    }
    public static class PremisStatuteDocumentIdentifier implements ElementInterface{
        String statuteDocumentationIdentifierType;
        String statuteDocumentationIdentifierValue;
        String statuteDocumentationRole;

        public String getStatuteDocumentationIdentifierType() {
            return statuteDocumentationIdentifierType;
        }

        public void setStatuteDocumentationIdentifierType(String statuteDocumentationIdentifierType) {
            this.statuteDocumentationIdentifierType = statuteDocumentationIdentifierType;
        }

        public String getStatuteDocumentationIdentifierValue() {
            return statuteDocumentationIdentifierValue;
        }

        public void setStatuteDocumentationIdentifierValue(String statuteDocumentationIdentifierValue) {
            this.statuteDocumentationIdentifierValue = statuteDocumentationIdentifierValue;
        }

        public String getStatuteDocumentationRole() {
            return statuteDocumentationRole;
        }

        public void setStatuteDocumentationRole(String statuteDocumentationRole) {
            this.statuteDocumentationRole = statuteDocumentationRole;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {               
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("statuteDocumentationIdentifierType")) {
                    statuteDocumentationIdentifierType = child.getTextContent();                    
                }else if(localName.equals("statuteDocumentationIdentifierValue")) {
                    statuteDocumentationIdentifierValue = child.getTextContent();
                }else if(localName.equals("statuteDocumentationRole")) {
                    statuteDocumentationRole = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                        
                       
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:statuteDocumentationIdentifierType");
            t.setTextContent(statuteDocumentationIdentifierType);
            root.appendChild(t);
                             
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:statuteDocumentationIdentifierValue");
            v.setTextContent(statuteDocumentationIdentifierValue);
            root.appendChild(v);
            
            Element r = doc.createElementNS(NS.PREMIS.ns(),"premis:statuteDocumentationRole");
            r.setTextContent(statuteDocumentationRole);
            root.appendChild(r);
        }
                
    }
    public static class PremisOtherRightsInformation implements ElementInterface{
        ArrayList<PremisOtherRightsDocumentationIdentifier>otherRightsDocumentationIdentifier;
        String otherRightsBasis;
        PremisStartAndEndDate otherRightsApplicableDates;
        ArrayList<String>otherRightsNote;

        public String getOtherRightsBasis() {
            return otherRightsBasis;
        }

        public void setOtherRightsBasis(String otherRightsBasis) {
            this.otherRightsBasis = otherRightsBasis;
        }

        public PremisStartAndEndDate getOtherRightsApplicableDates() {
            return otherRightsApplicableDates;
        }

        public void setOtherRightsApplicableDates(PremisStartAndEndDate otherRightsApplicableDates) {
            this.otherRightsApplicableDates = otherRightsApplicableDates;
        }

        public ArrayList<PremisOtherRightsDocumentationIdentifier> getOtherRightsDocumentationIdentifier() {
            if(otherRightsDocumentationIdentifier == null){
                otherRightsDocumentationIdentifier = new ArrayList<PremisOtherRightsDocumentationIdentifier>();                        
            }
            return otherRightsDocumentationIdentifier;
        }

        public ArrayList<String> getOtherRightsNote() {
            if(otherRightsNote == null){
                otherRightsNote = new ArrayList<String>();
            }
            return otherRightsNote;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {               
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("otherRightsDocumentationIdentifier")) {
                    PremisOtherRightsDocumentationIdentifier id = new PremisOtherRightsDocumentationIdentifier();
                    id.unmarshal(child);
                    getOtherRightsDocumentationIdentifier().add(id);                    
                }else if(localName.equals("otherRightsBasis")) {
                    otherRightsBasis = child.getTextContent();
                }else if(localName.equals("otherRightsApplicableDates")) {
                    otherRightsApplicableDates = new PremisStartAndEndDate();
                    otherRightsApplicableDates.unmarshal(child);
                }else if(localName.equals("otherRightsNote")) {
                    getOtherRightsNote().add(child.getTextContent());                    
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            
            for(PremisOtherRightsDocumentationIdentifier id:getOtherRightsDocumentationIdentifier()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:otherRightsDocumentationIdentifier");
                id.marshal(e,doc);
                root.appendChild(e);
            }
            
            Element b = doc.createElementNS(NS.PREMIS.ns(),"premis:otherRightsBasis");
            b.setTextContent(otherRightsBasis);
            root.appendChild(b);
            
            if(otherRightsApplicableDates != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:otherRightsApplicableDates");
                otherRightsApplicableDates.marshal(e,doc);
                root.appendChild(e);
            }
            
            for(String note:getOtherRightsNote()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:otherRightsNote");
                e.setTextContent(note);
                root.appendChild(e);
            }          
            
        }
    }
    public static class PremisOtherRightsDocumentationIdentifier implements ElementInterface{
        String otherRightsDocumentationIdentifierType;
        String otherRightsDocumentationIdentifierValue;
        String otherRightsDocumentationRole;

        public String getOtherRightsDocumentationIdentifierType() {
            return otherRightsDocumentationIdentifierType;
        }

        public void setOtherRightsDocumentationIdentifierType(String otherRightsDocumentationIdentifierType) {
            this.otherRightsDocumentationIdentifierType = otherRightsDocumentationIdentifierType;
        }

        public String getOtherRightsDocumentationIdentifierValue() {
            return otherRightsDocumentationIdentifierValue;
        }

        public void setOtherRightsDocumentationIdentifierValue(String otherRightsDocumentationIdentifierValue) {
            this.otherRightsDocumentationIdentifierValue = otherRightsDocumentationIdentifierValue;
        }

        public String getOtherRightsDocumentationRole() {
            return otherRightsDocumentationRole;
        }

        public void setOtherRightsDocumentationRole(String otherRightsDocumentationRole) {
            this.otherRightsDocumentationRole = otherRightsDocumentationRole;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {               
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("otherRightsDocumentationIdentifierType")) {
                    otherRightsDocumentationIdentifierType = child.getTextContent();
                }else if(localName.equals("otherRightsDocumentationIdentifierValue")) {
                    otherRightsDocumentationIdentifierValue = child.getTextContent();                   
                }else if(localName.equals("otherRightsDocumentationRole")) {
                    otherRightsDocumentationRole = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:otherRightsDocumentationIdentifierType");
            t.setTextContent(otherRightsDocumentationIdentifierType);
            root.appendChild(t);
            
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:otherRightsDocumentationIdentifierValue");
            v.setTextContent(otherRightsDocumentationIdentifierValue);
            root.appendChild(v);
            
            Element r = doc.createElementNS(NS.PREMIS.ns(),"premis:otherRightsDocumentationRole");
            r.setTextContent(otherRightsDocumentationRole);
            root.appendChild(r);
        }
    }
    public static class PremisRightsGranted implements ElementInterface{
        String act;
        ArrayList<String>restriction;
        PremisStartAndEndDate termOfGrant;
        PremisStartAndEndDate termOfRestriction;
        ArrayList<String>rightsGrantedNote;

        public String getAct() {
            return act;
        }

        public void setAct(String act) {
            this.act = act;
        }

        public PremisStartAndEndDate getTermOfGrant() {
            return termOfGrant;
        }

        public void setTermOfGrant(PremisStartAndEndDate termOfGrant) {
            this.termOfGrant = termOfGrant;
        }

        public PremisStartAndEndDate getTermOfRestriction() {
            return termOfRestriction;
        }

        public void setTermOfRestriction(PremisStartAndEndDate termOfRestriction) {
            this.termOfRestriction = termOfRestriction;
        }

        public ArrayList<String> getRestriction() {
            if(restriction == null){
                restriction = new ArrayList<String>();
            }
            return restriction;
        }

        public ArrayList<String> getRightsGrantedNote() {
            if(rightsGrantedNote == null){
                rightsGrantedNote = new ArrayList<String>();                         
            }
            return rightsGrantedNote;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {               
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("act")) {
                    act = child.getTextContent();
                }else if(localName.equals("restriction")) {
                    getRestriction().add(child.getTextContent());                    
                }else if(localName.equals("termOfGrant")) {
                    termOfGrant = new PremisStartAndEndDate();
                    termOfGrant.unmarshal(child);                    
                }else if(localName.equals("termOfRestriction")) {
                    termOfRestriction = new PremisStartAndEndDate();
                    termOfRestriction.unmarshal(child);                    
                }else if(localName.equals("rightsGrantedNote")) {
                    getRightsGrantedNote().add(child.getTextContent());
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            
            Element a = doc.createElementNS(NS.PREMIS.ns(),"premis:act");
            a.setTextContent(act);
            root.appendChild(a);
            
            for(String r:getRestriction()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:restriction");
                e.setTextContent(r);
                root.appendChild(e);
            }            
            if(termOfGrant != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:termOfGrant");
                termOfGrant.marshal(e,doc);
                root.appendChild(e);
            }
            if(termOfRestriction != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:termOfRestriction");
                termOfRestriction.marshal(e,doc);
                root.appendChild(e);
            }
            for(String r:getRightsGrantedNote()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:rightsGrantedNote");
                e.setTextContent(r);
                root.appendChild(e);
            }            
        }
        
    }
    
    /*
    public static class PremisLinkingObjectIdentifier implements ElementInterface{
        //attributes
        String LinkObjectXmlId;
        //elemements
        String linkingObjectIdentifierType;
        String linkingObjectIdentifierValue;
        ArrayList<String>linkingObjectRole;

        public String getLinkObjectXmlId() {
            return LinkObjectXmlId;
        }

        public void setLinkObjectXmlId(String LinkObjectXmlId) {
            this.LinkObjectXmlId = LinkObjectXmlId;
        }

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

        public ArrayList<String> getLinkingObjectRole() {
            if(linkingObjectRole == null){
                linkingObjectRole = new ArrayList<String>();
            }
            return linkingObjectRole;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {
            NamedNodeMap attrs = root.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Attr attr = (Attr) attrs.item(i);
                String name = attr.getName();
                String value = attr.getNodeValue();
                if(name.equals("LinkObjectXmlId")){
                    LinkObjectXmlId = value;
                    break;
                }
            }            
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("linkingObjectIdentifierType")) {
                    linkingObjectIdentifierType = child.getTextContent();
                }else if(localName.equals("linkingAgentIdentifierValue")) {
                    linkingObjectIdentifierValue = child.getTextContent();
                }else if(localName.equals("linkingObjectRole")) {
                    getLinkingObjectRole().add(child.getTextContent());
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {
            if(LinkObjectXmlId != null && !LinkObjectXmlId.isEmpty()){
                root.setAttribute("LinkObjectXmlId",LinkObjectXmlId);
            }
            
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingObjectIdentifierType");
            t.setTextContent(linkingObjectIdentifierType);
            root.appendChild(t);
            
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingObjectIdentifierValue");
            v.setTextContent(linkingObjectIdentifierValue);
            root.appendChild(v);
            
            for(String r:getLinkingObjectRole()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingObjectRole");
                e.setTextContent(r);
                root.appendChild(e);
            }
        }
        
    }
    public static class PremisLinkingAgentIdentifier implements ElementInterface{
        //attributes
        String LinkAgentXmlId;
        //elements
        String linkingAgentIdentifierType;
        String linkingAgentIdentifierValue;
        ArrayList<String>linkingAgentRole;

        public String getLinkAgentXmlId() {
            return LinkAgentXmlId;
        }

        public void setLinkAgentXmlId(String LinkAgentXmlId) {
            this.LinkAgentXmlId = LinkAgentXmlId;
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

        public ArrayList<String> getLinkingAgentRole() {
            if(linkingAgentRole == null){
                linkingAgentRole = new ArrayList<String>();
            }
            return linkingAgentRole;
        }
        
        @Override
        public void unmarshal(Element root) throws ParseException {
            NamedNodeMap attrs = root.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Attr attr = (Attr) attrs.item(i);
                String name = attr.getName();
                String value = attr.getNodeValue();
                if(name.equals("LinkAgentXmlId")){
                    LinkAgentXmlId = value;
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
                    getLinkingAgentRole().add(child.getTextContent());
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {
            if(LinkAgentXmlId != null && !LinkAgentXmlId.isEmpty()){
                root.setAttribute("LinkAgentXmlId",LinkAgentXmlId);
            }
            
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingAgentIdentifierType");
            t.setTextContent(linkingAgentIdentifierType);
            root.appendChild(t);
            
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingAgentIdentifierValue");
            v.setTextContent(linkingAgentIdentifierValue);
            root.appendChild(v);
            
            for(String r:getLinkingAgentRole()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingAgentRole");
                e.setTextContent(r);
                root.appendChild(e);
            }
        }
    }
    */ 
}

