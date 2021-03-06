package ugent.premis;

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
public class PremisObject implements ElementInterface{
    //attributes
    PremisObjectType type;
    String xmlID;
    String version;
    
    //elements
    ArrayList<PremisObjectIdentifier> objectIdentifier;
    ArrayList<PremisPreservationLevel>preservationLevel;    
    ArrayList<PremisSignificantProperties>significantProperties;    
    ArrayList<PremisObjectCharacteristics>objectCharacteristics;
    String originalName;
    ArrayList<PremisStorage> storage;
    ArrayList<PremisEnvironment>environment;
    ArrayList<PremisSignatureInformation>signatureInformation;    
    ArrayList<PremisRelationship>relationship;
    ArrayList<PremisLinkingEventIdentifier>linkingEventIdentifier;
    ArrayList<PremisLinkingIntellectualEntityIdentifier>linkingIntellectualEntityIdentifier;
    ArrayList<PremisLinkingRightsStatementIdentifier>linkingRightsStatementIdentifier;
    

    public PremisObject(PremisObjectType type){
        this.type = type;
    }

    public ArrayList<PremisRelationship> getRelationship() {
        if(relationship == null){
            relationship = new ArrayList<PremisRelationship>();
        }
        return relationship;
    }    
    public ArrayList<PremisSignatureInformation> getSignatureInformation() {
        if(signatureInformation == null){
            signatureInformation = new ArrayList<PremisSignatureInformation>();
        }
        return signatureInformation;
    }    
    public ArrayList<PremisEnvironment> getEnvironment() {
        if(environment == null){
            environment = new ArrayList<PremisEnvironment>();
        }
        return environment;
    }    
    public String getOriginalName() {
        return originalName;
    }
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }   
    public ArrayList<PremisSignificantProperties> getSignificantProperties() {
        if(significantProperties == null){
            significantProperties = new ArrayList<PremisSignificantProperties>();
        }
        return significantProperties;
    }    
    
    public ArrayList<PremisStorage> getStorage() {
        if(storage == null){
            storage = new ArrayList<PremisStorage>();
        }
        return storage;
    }   
    public ArrayList<PremisObjectCharacteristics> getObjectCharacteristics() {
        if(objectCharacteristics == null){
            objectCharacteristics = new ArrayList<PremisObjectCharacteristics>();
        }
        return objectCharacteristics;
    }   
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<PremisObjectIdentifier> getObjectIdentifier() {
        if(objectIdentifier == null){
            objectIdentifier = new ArrayList<PremisObjectIdentifier>();
        }
        return objectIdentifier;
    }
    public ArrayList<PremisPreservationLevel> getPreservationLevel() {
        if(preservationLevel == null){
            preservationLevel = new ArrayList<PremisPreservationLevel>();
        }
        return preservationLevel;
    }    
    public PremisObjectType getType() {
        return type;
    }
    public void setType(PremisObjectType type) {
        this.type = type;
    }   
    public String getXmlID() {
        return xmlID;
    }
    public void setXmlID(String xmlID) {
        this.xmlID = xmlID;
    }

    public static enum PremisObjectType {
        representation("representation"),file("file"),bitstream("bitstream");
        String c;
        PremisObjectType(String c){
            this.c = c;
        }
    }

    public ArrayList<PremisLinkingEventIdentifier> getLinkingEventIdentifier() {
        if(linkingEventIdentifier == null){
            linkingEventIdentifier = new ArrayList<PremisLinkingEventIdentifier>();
        }
        return linkingEventIdentifier;
    }

    public ArrayList<PremisLinkingIntellectualEntityIdentifier> getLinkingIntellectualEntityIdentifier() {
        if(linkingIntellectualEntityIdentifier == null){
            linkingIntellectualEntityIdentifier = new ArrayList<PremisLinkingIntellectualEntityIdentifier>();
        }
        return linkingIntellectualEntityIdentifier;
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
            }else if(name.equals("type")){
                //premis:type als value! Jammer kan de waarde niet verwerkt worden door de API                                
                type = PremisObjectType.valueOf(value.replaceAll("^\\w+:",""));
            }
        }
        
        ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
        for(Element child:children) {
            String localName = child.getLocalName();
            if(localName.equals("objectIdentifier")) {
                PremisObjectIdentifier oid  =new PremisObjectIdentifier();
                oid.unmarshal(child);
                getObjectIdentifier().add(oid);
            }else if(localName.equals("preservationLevel")) {
                PremisPreservationLevel level = new PremisPreservationLevel();
                level.unmarshal(child);
                getPreservationLevel().add(level);                
            }else if(localName.equals("significantProperties")) {
                PremisSignificantProperties props = new PremisSignificantProperties();
                props.unmarshal(child);
                getSignificantProperties().add(props);
            }else if(localName.equals("objectCharacteristics")) {
                PremisObjectCharacteristics oc = new PremisObjectCharacteristics();
                oc.unmarshal(child);
                getObjectCharacteristics().add(oc);
            }else if(localName.equals("originalName")) {
                originalName = child.getTextContent();
            }else if(localName.equals("storage")) {
                PremisStorage s = new PremisStorage();
                s.unmarshal(child);
                getStorage().add(s);
            }else if(localName.equals("environment")) {
                PremisEnvironment env = new PremisEnvironment();
                env.unmarshal(child);
                getEnvironment().add(env);
            }else if(localName.equals("signatureInformation")) {
                PremisSignatureInformation sinfo = new PremisSignatureInformation();
                sinfo.unmarshal(child);
                getSignatureInformation().add(sinfo);
            }else if(localName.equals("relationship")) {
                PremisRelationship rel = new PremisRelationship();
                rel.unmarshal(child);
                getRelationship().add(rel);
            }else if(localName.equals("linkingEventIdentifier")) {
                PremisLinkingEventIdentifier id = new PremisLinkingEventIdentifier();
                id.unmarshal(child);
                getLinkingEventIdentifier().add(id);
            }else if(localName.equals("linkingIntellectualEntityIdentifier")) {
                PremisLinkingIntellectualEntityIdentifier id = new PremisLinkingIntellectualEntityIdentifier();
                id.unmarshal(child);
                getLinkingIntellectualEntityIdentifier().add(id);
            }else if(localName.equals("linkingRightsStatementIdentifier")) {
                PremisLinkingRightsStatementIdentifier id = new PremisLinkingRightsStatementIdentifier();
                id.unmarshal(child);
                getLinkingRightsStatementIdentifier().add(id);
            }
        }
    }

    @Override
    public void marshal(Element root, Document doc) {
        //attributes
        if(xmlID != null && !xmlID.isEmpty()){
            root.setAttribute("xmlID",xmlID);
        }else{
            root.setAttribute("xmlID",DOMHelp.createID());
        }
        if(version != null && !version.isEmpty()){
            root.setAttribute("version",version);
        }else{
            root.setAttribute("version","2.2");
        }        
        root.setAttributeNS(NS.XSI.ns(),"xsi:type","premis:"+type.toString());        
        
        //elements -> sequence is important!
        for(PremisObjectIdentifier oi:getObjectIdentifier()){
            Element oie = doc.createElementNS(NS.PREMIS.ns(),"premis:objectIdentifier");
            oi.marshal(oie,doc);
            root.appendChild(oie);
        }
        for(PremisPreservationLevel pr:getPreservationLevel()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:preservationLevel");
            pr.marshal(e,doc);            
            root.appendChild(e);
        }             
        for(PremisSignificantProperties props:getSignificantProperties()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:significantProperties");
            props.marshal(e,doc);
            root.appendChild(e);
        }
        
        for(PremisObjectCharacteristics oc:getObjectCharacteristics()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:objectCharacteristics");
            oc.marshal(e,doc);
            root.appendChild(e);
        }
        
        if(originalName != null && !originalName.isEmpty()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:originalName");
            e.setTextContent(originalName);
            root.appendChild(e);
        }
        for(PremisStorage stor:getStorage()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:storage");
            stor.marshal(e,doc);
            root.appendChild(e);
        }
        
        for(PremisEnvironment env:getEnvironment()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:environment");
            env.marshal(e,doc);
            root.appendChild(e);
        }
        
        for(PremisSignatureInformation sinfo:getSignatureInformation()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:signatureInformation");
            sinfo.marshal(e,doc);
            root.appendChild(e);
        }
        
        for(PremisRelationship rel:getRelationship()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:relationship");
            rel.marshal(e,doc);
            root.appendChild(e);
        }
        
        for(PremisLinkingEventIdentifier ev:getLinkingEventIdentifier()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingEventIdentifier");
            ev.marshal(e,doc);
            root.appendChild(e);
        }
        
        for(PremisLinkingIntellectualEntityIdentifier in:getLinkingIntellectualEntityIdentifier()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingIntellectualEntityIdentifier");
            in.marshal(e,doc);
            root.appendChild(e);
        }
        
        for(PremisLinkingRightsStatementIdentifier r:getLinkingRightsStatementIdentifier()){
            Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingRightsStatementIdentifier");
            r.marshal(e,doc);
            root.appendChild(e);
        }
    }
    public static class PremisPreservationLevel implements ElementInterface{
        String preservationLevelValue;
        String preservationLevelRole;
        ArrayList<String>preservationLevelRationale;
        String preservationLevelDateAssigned;

        public String getPreservationLevelValue() {
            return preservationLevelValue;
        }

        public void setPreservationLevelValue(String preservationLevelValue) {
            this.preservationLevelValue = preservationLevelValue;
        }

        public String getPreservationLevelRole() {
            return preservationLevelRole;
        }

        public void setPreservationLevelRole(String preservationLevelRole) {
            this.preservationLevelRole = preservationLevelRole;
        }

        public String getPreservationLevelDateAssigned() {
            return preservationLevelDateAssigned;
        }

        public void setPreservationLevelDateAssigned(String preservationLevelDateAssigned) {
            this.preservationLevelDateAssigned = preservationLevelDateAssigned;
        }

        public ArrayList<String> getPreservationLevelRationale() {
            if(preservationLevelRationale == null){
                preservationLevelRationale = new ArrayList<String>();
            }
            return preservationLevelRationale;
        }

        @Override
        public void unmarshal(Element root) throws ParseException {       
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("preservationLevelValue")) {
                    preservationLevelValue = child.getTextContent();
                }else if(localName.equals("preservationLevelRole")) {
                    preservationLevelRole = child.getTextContent();
                }else if(localName.equals("preservationLevelRationale")) {
                    getPreservationLevelRationale().add(child.getTextContent());                    
                }else if(localName.equals("preservationLevelDateAssigned")) {
                    preservationLevelDateAssigned = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                    
            
            //elements
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:preservationLevelValue");
            v.setTextContent(preservationLevelValue);
            root.appendChild(v);
            
            Element r = doc.createElementNS(NS.PREMIS.ns(),"premis:preservationLevelRole");
            r.setTextContent(preservationLevelRole);
            root.appendChild(r);
            
            for(String value:getPreservationLevelRationale()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:preservationLevelRationale");
                e.setTextContent(value);
                root.appendChild(e);
            }
            
            if(preservationLevelDateAssigned != null){
                Element d = doc.createElementNS(NS.PREMIS.ns(),"premis:preservationLevelDateAssigned");
                d.setTextContent(preservationLevelDateAssigned);
                root.appendChild(d);
            }
        } 
    }
    public static class PremisObjectIdentifier implements ElementInterface {
        String objectIdentifierType;
        String objectIdentifierValue;

        public String getObjectIdentifierType() {
            return objectIdentifierType;
        }

        public void setObjectIdentifierType(String objectIdentifierType) {
            this.objectIdentifierType = objectIdentifierType;
        }

        public String getObjectIdentifierValue() {
            return objectIdentifierValue;
        }

        public void setObjectIdentifierValue(String objectIdentifierValue) {
            this.objectIdentifierValue = objectIdentifierValue;
        }        

        @Override
        public void unmarshal(Element root) throws ParseException {
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("objectIdentifierType")) {
                    objectIdentifierType = child.getTextContent();
                }else if(localName.equals("objectIdentifierValue")) {
                    objectIdentifierValue = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:objectIdentifierType");
            t.setTextContent(objectIdentifierType);
            root.appendChild(t);
            
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:objectIdentifierValue");
            v.setTextContent(objectIdentifierValue);
            root.appendChild(v);
        }
        
    }
    public static class PremisObjectCharacteristics implements ElementInterface {
        int compositionLevel;
        ArrayList<PremisFixity>fixity;
        long size;
        ArrayList<PremisFormat>format;        
        ArrayList<PremisCreatingApplication>creatingApplication;
        ArrayList<PremisInhibitors>inhibitors;
        ArrayList<Element> objectCharacteristicsExtension;  
        ArrayList<MdSec>mdSec;
        
        public ArrayList<MdSec> getMdSec() {
            if(mdSec == null){
                mdSec = new ArrayList<MdSec>();
            }
            return mdSec;
        }

        public ArrayList<Element> getObjectCharacteristicsExtension() {
            if(objectCharacteristicsExtension == null){
                objectCharacteristicsExtension = new ArrayList<Element>();
            }
            return objectCharacteristicsExtension;
        }
                
        public ArrayList<PremisInhibitors> getInhibitors() {
            if(inhibitors == null){
                inhibitors = new ArrayList<PremisInhibitors>();
            }
            return inhibitors;
        }        
        public ArrayList<PremisCreatingApplication> getCreatingApplication() {
            if(creatingApplication == null){
                creatingApplication = new ArrayList<PremisCreatingApplication>();
            }
            return creatingApplication;
        }             
        public ArrayList<PremisFormat>getFormat() {
            if(format == null){
                format = new ArrayList<PremisFormat>();
            }
            return format;
        }        
        public long getSize() {
            return size;
        }
        public void setSize(long size) {
            this.size = size;
        }
        public ArrayList<PremisFixity> getFixity() {
            if(fixity == null){
                fixity = new ArrayList<PremisFixity>();
            }
            return fixity;
        }

        public int getCompositionLevel() {
            return compositionLevel;
        }
        public void setCompositionLevel(int compositionLevel) {
            this.compositionLevel = compositionLevel;
        }        
        @Override
        public void unmarshal(Element root) throws ParseException {
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("compositionLevel")) {
                    compositionLevel = Integer.parseInt(child.getTextContent());
                }else if(localName.equals("fixity")) {
                    PremisFixity f = new PremisFixity();
                    f.unmarshal(child);
                    getFixity().add(f);
                }else if(localName.equals("size")) {
                    size = Long.parseLong(child.getTextContent());
                }else if(localName.equals("format")) {
                    PremisFormat f = new PremisFormat();
                    f.unmarshal(child);                    
                    getFormat().add(f);
                }else if(localName.equals("creatingApplication")) {
                    PremisCreatingApplication app = new PremisCreatingApplication();
                    app.unmarshal(child);
                    getCreatingApplication().add(app);
                }else if(localName.equals("inhibitors")) {
                    PremisInhibitors inh = new PremisInhibitors();
                    inh.unmarshal(child);
                    getInhibitors().add(inh);
                }else if(localName.equals("objectCharacteristicsExtension")) {
                    getObjectCharacteristicsExtension().add((Element) child.getFirstChild());                    
                }else if(localName.equals("mdSec")) {
                    MdSec m = new MdSec("");
                    m.unmarshal(child);
                    getMdSec().add(m);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {
            Element cl = doc.createElementNS(NS.PREMIS.ns(),"premis:compositionLevel");
            cl.setTextContent(""+compositionLevel);
            root.appendChild(cl);
            
            for(PremisFixity fix:getFixity()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:fixity");
                fix.marshal(e,doc);
                root.appendChild(e);
            }
            
            Element s = doc.createElementNS(NS.PREMIS.ns(),"premis:size");
            s.setTextContent(""+size);
            root.appendChild(s);
            
            for(PremisFormat fm:getFormat()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:format");
                fm.marshal(e,doc);
                root.appendChild(e);
            }
            
            for(PremisCreatingApplication app:getCreatingApplication()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:creatingApplication");
                app.marshal(e,doc);
                root.appendChild(e);
            }
            
            for(PremisInhibitors inh:getInhibitors()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:inhibitors");
                inh.marshal(e,doc);
                root.appendChild(e);
            }
            for(Element ext:getObjectCharacteristicsExtension()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:objectCharacteristicsExtension");                
                e.appendChild(doc.importNode(ext,true));
                root.appendChild(e);
            }
            for(MdSec m:getMdSec()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:mdSec");
                m.marshal(e,doc);
                root.appendChild(e);
            }
        }
        
                      
    }
    public static class PremisFixity implements ElementInterface{
        String messageDigestAlgorithm;
        String messageDigest;
        String messageDigestOriginator;

        public String getMessageDigestAlgorithm() {
            return messageDigestAlgorithm;
        }
        public void setMessageDigestAlgorithm(String messageDigestAlgorithm) {
            this.messageDigestAlgorithm = messageDigestAlgorithm;
        }
        public String getMessageDigest() {
            return messageDigest;
        }
        public void setMessageDigest(String messageDigest) {
            this.messageDigest = messageDigest;
        }
        public String getMessageDigestOriginator() {
            return messageDigestOriginator;
        }
        public void setMessageDigestOriginator(String messageDigestOriginator) {
            this.messageDigestOriginator = messageDigestOriginator;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("messageDigestAlgorithm")) {
                    messageDigestAlgorithm = child.getTextContent();
                }else if(localName.equals("messageDigest")) {
                    messageDigest = child.getTextContent();
                }else if(localName.equals("messageDigestOriginator")) {
                    messageDigestOriginator = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                                 
            //elements
            Element n = doc.createElementNS(NS.PREMIS.ns(),"premis:messageDigestAlgorithm");
            n.setTextContent(messageDigestAlgorithm);
            root.appendChild(n);
            
            Element k = doc.createElementNS(NS.PREMIS.ns(),"premis:messageDigest");
            k.setTextContent(messageDigest);
            root.appendChild(k);
            
            Element r = doc.createElementNS(NS.PREMIS.ns(),"premis:messageDigestOriginator");
            r.setTextContent(messageDigestOriginator);
            root.appendChild(r);
        }             
    }
    public static class PremisFormat implements ElementInterface{
        PremisFormatDesignation formatDesignation;
        PremisFormatRegistry formatRegistry;
        String formatNote;

        public String getFormatNote() {
            return formatNote;
        }
        public void setFormatNote(String formatNote) {
            this.formatNote = formatNote;
        }           
        public PremisFormatDesignation getFormatDesignation() {
            return formatDesignation;
        }
        public void setFormatDesignation(PremisFormatDesignation formatDesignation) {
            this.formatDesignation = formatDesignation;
        }
        public PremisFormatRegistry getFormatRegistry() {
            return formatRegistry;
        }
        public void setFormatRegistry(PremisFormatRegistry formatRegistry) {
            this.formatRegistry = formatRegistry;
        }

        @Override
        public void unmarshal(Element root) throws ParseException {            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("formatDesignation")) {
                    formatDesignation = new PremisFormatDesignation();
                    formatDesignation.unmarshal(child);                    
                }else if(localName.equals("formatRegistry")) {
                    formatRegistry = new PremisFormatRegistry();
                    formatRegistry.unmarshal(child);                    
                }else if(localName.equals("formatNote")) {
                    formatNote = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                                 
            //elements
            if(formatDesignation != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:formatDesignation");
                formatDesignation.marshal(e,doc);
                root.appendChild(e);
            }
            if(formatRegistry != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:formatRegistry");
                formatRegistry.marshal(e,doc);
                root.appendChild(e);
            }
            if(formatNote != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:formatNote");
                e.setTextContent(formatNote);
                root.appendChild(e);
            }            
        }
    }  
    public static class PremisFormatDesignation implements ElementInterface{
        String formatName;
        String formatVersion;

        public String getFormatName() {
            return formatName;
        }

        public void setFormatName(String formatName) {
            this.formatName = formatName;
        }

        public String getFormatVersion() {
            return formatVersion;
        }

        public void setFormatVersion(String formatVersion) {
            this.formatVersion = formatVersion;
        }              


        @Override
        public void unmarshal(Element root) throws ParseException {            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("formatName")) {
                    formatName = child.getTextContent();
                }else if(localName.equals("formatVersion")) {
                    formatVersion = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                                 
            //elements
            Element n = doc.createElementNS(NS.PREMIS.ns(),"premis:formatName");
            n.setTextContent(formatName);
            root.appendChild(n);
            
            Element k = doc.createElementNS(NS.PREMIS.ns(),"premis:formatVersion");
            k.setTextContent(formatVersion);
            root.appendChild(k);           
        }           
    }
    public static class PremisFormatRegistry implements ElementInterface{
        String formatRegistryName;
        String formatRegistryKey;
        String formatRegistryRole;

        public String getFormatRegistryName() {
            return formatRegistryName;
        }
        public void setFormatRegistryName(String formatRegistryName) {
            this.formatRegistryName = formatRegistryName;
        }
        public String getFormatRegistryKey() {
            return formatRegistryKey;
        }
        public void setFormatRegistryKey(String formatRegistryKey) {
            this.formatRegistryKey = formatRegistryKey;
        }
        public String getFormatRegistryRole() {
            return formatRegistryRole;
        }
        public void setFormatRegistryRole(String formatRegistryRole) {
            this.formatRegistryRole = formatRegistryRole;
        }                
        @Override
        public void unmarshal(Element root) throws ParseException {            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("formatRegistryName")) {
                    formatRegistryName = child.getTextContent();
                }else if(localName.equals("formatRegistryKey")) {
                    formatRegistryKey = child.getTextContent();
                }else if(localName.equals("formatRegistryRole")) {
                    formatRegistryRole = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                                 
            //elements
            Element n = doc.createElementNS(NS.PREMIS.ns(),"premis:formatRegistryName");
            n.setTextContent(formatRegistryName);
            root.appendChild(n);
            
            Element k = doc.createElementNS(NS.PREMIS.ns(),"premis:formatRegistryKey");
            k.setTextContent(formatRegistryKey);
            root.appendChild(k);
            
            Element r = doc.createElementNS(NS.PREMIS.ns(),"premis:formatRegistryRole");
            r.setTextContent(formatRegistryRole);
            root.appendChild(r);
        }                 
    }
    public static class PremisStorage implements ElementInterface {
        PremisContentLocation contentLocation;
        String storageMedium;

        public PremisContentLocation getContentLocation() {
            return contentLocation;
        }
        public void setContentLocation(PremisContentLocation contentLocation) {
            this.contentLocation = contentLocation;
        }
        public String getStorageMedium() {
            return storageMedium;
        }
        public void setStorageMedium(String storageMedium) {
            this.storageMedium = storageMedium;
        }

        @Override
        public void unmarshal(Element root) throws ParseException {
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("contentLocation")) {
                    contentLocation = new PremisContentLocation();
                    contentLocation.unmarshal(child);
                }else if(localName.equals("storageMedium")) {
                    storageMedium = child.getTextContent();
                }
            } 
        }

        @Override
        public void marshal(Element root, Document doc) {
            if(contentLocation != null){                
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:contentLocation");
                contentLocation.marshal(e,doc);
                root.appendChild(e);
            }
            if(storageMedium != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:storageMedium");
                e.setTextContent(storageMedium);
                root.appendChild(e);
            }
        }
                   
    }
    public static class PremisContentLocation implements ElementInterface {
        String contentLocationType;
        String contentLocationValue;

        @Override
        public void unmarshal(Element root) throws ParseException {
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("contentLocationType")) {
                    contentLocationType = child.getTextContent();
                }else if(localName.equals("contentLocationValue")) {
                    contentLocationValue = child.getTextContent();
                }
            }                
        }

        @Override
        public void marshal(Element root, Document doc) {
            if(contentLocationType != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:contentLocationType");
                e.setTextContent(contentLocationType);
                root.appendChild(e);
            }
            if(contentLocationValue != null){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:contentLocationValue");
                e.setTextContent(contentLocationValue);
                root.appendChild(e);
            }
        }
    } 
    public static class PremisSignificantProperties implements ElementInterface{        
        String significantPropertiesType;
        String significantPropertiesValue;
        ArrayList<Element>significantPropertiesExtension;        
        ArrayList<MdSec> mdSec;
        
        public ArrayList<MdSec> getMdSec() {
            if(mdSec == null){
                mdSec = new ArrayList<MdSec>();
            }
            return mdSec;
        }

        public ArrayList<Element> getSignificantPropertiesExtension() {
            if(significantPropertiesExtension == null){
                significantPropertiesExtension = new ArrayList<Element>();
            }
            return significantPropertiesExtension;
        }       
        public String getSignificantPropertiesType() {
            return significantPropertiesType;
        }
        public void setSignificantPropertiesType(String significantPropertiesType) {
            this.significantPropertiesType = significantPropertiesType;
        }
        public String getSignificantPropertiesValue() {
            return significantPropertiesValue;
        }
        public void setSignificantPropertiesValue(String significantPropertiesValue) {
            this.significantPropertiesValue = significantPropertiesValue;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("significantPropertiesType")) {
                    significantPropertiesType = child.getTextContent();
                }else if(localName.equals("significantPropertiesValue")) {
                    significantPropertiesValue = child.getTextContent();
                }else if(localName.equals("significantPropertiesExtension")) {
                    getSignificantPropertiesExtension().add((Element) child.getFirstChild());
                }else if(localName.equals("mdSec")) {
                    MdSec m = new MdSec("");
                    m.unmarshal(child);
                    getMdSec().add(m);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                    
            //elements
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:significantPropertiesType");
            t.setTextContent(significantPropertiesType);
            root.appendChild(t);
            
            if(significantPropertiesValue != null){
                Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:significantPropertiesValue");
                v.setTextContent(significantPropertiesValue);
                root.appendChild(v);
            }
            for(Element sp:getSignificantPropertiesExtension()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:significantPropertiesExtension");
                e.appendChild(doc.importNode(sp,true));
                root.appendChild(e);
            }
            for(MdSec m:getMdSec()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:mdSec");
                m.marshal(e,doc);
                root.appendChild(e);
            }
        }
    }
    public static class PremisCreatingApplication implements ElementInterface{
        String creatingApplicationName;
        String creatingApplicationVersion;
        String dateCreatedByApplication;
        ArrayList<Element>creatingApplicationExtension;        
        ArrayList<MdSec>mdSec;

        public ArrayList<MdSec> getMdSec() {
            if(mdSec == null){
                mdSec = new ArrayList<MdSec>();
            }
            return mdSec;
        }
        public String getCreatingApplicationName() {
            return creatingApplicationName;
        }

        public void setCreatingApplicationName(String creatingApplicationName) {
            this.creatingApplicationName = creatingApplicationName;
        }

        public String getCreatingApplicationVersion() {
            return creatingApplicationVersion;
        }

        public void setCreatingApplicationVersion(String creatingApplicationVersion) {
            this.creatingApplicationVersion = creatingApplicationVersion;
        }

        public String getDateCreatedByApplication() {
            return dateCreatedByApplication;
        }

        public void setDateCreatedByApplication(String dateCreatedByApplication) {
            this.dateCreatedByApplication = dateCreatedByApplication;
        }

        public ArrayList<Element> getCreatingApplicationExtension() {
            if(creatingApplicationExtension == null){
                creatingApplicationExtension = new ArrayList<Element>();
            }
            return creatingApplicationExtension;
        }

        @Override
        public void unmarshal(Element root) throws ParseException {
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("creatingApplicationName")) {
                    creatingApplicationName = child.getTextContent();
                }else if(localName.equals("creatingApplicationVersion")) {
                    creatingApplicationVersion = child.getTextContent();
                }else if(localName.equals("dateCreatedByApplication")) {
                    dateCreatedByApplication = child.getTextContent();
                }else if(localName.equals("creatingApplicationExtension")) {
                    getCreatingApplicationExtension().add((Element) child.getFirstChild());
                }else if(localName.equals("mdSec")) {
                    MdSec m = new MdSec("");
                    m.unmarshal(child);
                    getMdSec().add(m);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {           
            
            //elements
            Element n = doc.createElementNS(NS.PREMIS.ns(),"premis:creatingApplicationName");
            n.setTextContent(creatingApplicationName);
            root.appendChild(n);
            
            if(creatingApplicationVersion != null){
                Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:creatingApplicationVersion");
                v.setTextContent(creatingApplicationVersion);
                root.appendChild(v);
            }
            if(dateCreatedByApplication != null){
                Element d = doc.createElementNS(NS.PREMIS.ns(),"premis:dateCreatedByApplication");
                d.setTextContent(creatingApplicationVersion);
                root.appendChild(d);
            }
            for(Element appe:getCreatingApplicationExtension()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:creatingApplicationExtension");
                e.appendChild(doc.importNode(appe,true));
                root.appendChild(e);
            }
            for(MdSec m:getMdSec()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:mdSec");
                m.marshal(e,doc);
                root.appendChild(e);
            }
        } 
    }
    public static class PremisInhibitors implements ElementInterface{
        String inhibitorType;
        ArrayList<String>inhibitorTarget;
        String inhibitorKey;

        public String getInhibitorType() {
            return inhibitorType;
        }

        public void setInhibitorType(String inhibitorType) {
            this.inhibitorType = inhibitorType;
        }

        public String getInhibitorKey() {
            return inhibitorKey;
        }

        public void setInhibitorKey(String inhibitorKey) {
            this.inhibitorKey = inhibitorKey;
        }

        public ArrayList<String> getInhibitorTarget() {
            if(inhibitorTarget == null){
                inhibitorTarget = new ArrayList<String>();
            }
            return inhibitorTarget;
        }   
        @Override
        public void unmarshal(Element root) throws ParseException {
                        
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("inhibitorType")) {
                    inhibitorType = child.getTextContent();
                }else if(localName.equals("inhibitorTarget")) {
                    getInhibitorTarget().add(child.getTextContent());                    
                }else if(localName.equals("inhibitorKey")) {
                    inhibitorKey = child.getTextContent();                    
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            
            //elements
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:inhibitorType");
            t.setTextContent(inhibitorType);
            root.appendChild(t);
            
            for(String target:getInhibitorTarget()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:inhibitorTarget");
                e.setTextContent(target);
                root.appendChild(e);
            }
            
            Element k = doc.createElementNS(NS.PREMIS.ns(),"premis:inhibitorKey");
            k.setTextContent(inhibitorKey);
            root.appendChild(k);
        }
    }
    public static class PremisEnvironment implements ElementInterface{
        String environmentCharacteristic;
        ArrayList<String>environmentPurpose;
        ArrayList<String>environmentNote;
        ArrayList<PremisDependency>dependency;
        ArrayList<PremisSoftware>software;
        ArrayList<PremisHardware>hardware;        
        ArrayList<Element>environmentExtension; 
        ArrayList<MdSec>mdSec;

        public ArrayList<MdSec> getMdSec() {
            if(mdSec == null){
                mdSec = new ArrayList<MdSec>();
            }
            return mdSec;
        }
        public String getEnvironmentCharacteristic() {
            return environmentCharacteristic;
        }
        public void setEnvironmentCharacteristic(String environmentCharacteristic) {
            this.environmentCharacteristic = environmentCharacteristic;
        }

        public ArrayList<String> getEnvironmentPurpose() {
            if(environmentPurpose == null){
                environmentPurpose = new ArrayList<String>();
            }
            return environmentPurpose;
        }

        public ArrayList<String> getEnvironmentNote() {
            if(environmentNote == null){
                environmentNote = new ArrayList<String>();
            }
            return environmentNote;
        }

        public ArrayList<PremisDependency> getDependency() {
            if(dependency == null){
                dependency = new ArrayList<PremisDependency>();
            }
            return dependency;
        }

        public ArrayList<PremisSoftware> getSoftware() {
            if(software == null){
                software = new ArrayList<PremisSoftware>();
            }
            return software;
        }

        public ArrayList<PremisHardware> getHardware() {
            if(hardware == null){
                hardware = new ArrayList<PremisHardware>();
            }
            return hardware;
        }

        public ArrayList<Element> getEnvironmentExtension() {
            if(environmentExtension == null){
                environmentExtension = new ArrayList<Element>();
            }
            return environmentExtension;
        }

              
        @Override
        public void unmarshal(Element root) throws ParseException {            
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("environmentCharacteristic")) {
                    environmentCharacteristic = child.getTextContent();
                }else if(localName.equals("environmentPurpose")) {
                    getEnvironmentPurpose().add(child.getTextContent());                    
                }else if(localName.equals("environmentNote")) {                    
                    getEnvironmentNote().add(child.getTextContent());
                }else if(localName.equals("dependency")) {                    
                    PremisDependency dep = new PremisDependency();
                    dep.unmarshal(child);
                    getDependency().add(dep);
                }else if(localName.equals("software")) {                    
                    PremisSoftware soft = new PremisSoftware();
                    soft.unmarshal(child);
                    getSoftware().add(soft);
                }else if(localName.equals("hardware")) {                    
                    PremisHardware hard = new PremisHardware();
                    hard.unmarshal(child);
                    getHardware().add(hard);
                }else if(localName.equals("environmentExtension")) {                    
                    getEnvironmentExtension().add((Element) child.getFirstChild());                                     
                }else if(localName.equals("mdSec")) {
                    MdSec m = new MdSec("");
                    m.unmarshal(child);
                    getMdSec().add(m);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                       
            Element ece = doc.createElementNS(NS.PREMIS.ns(),"premis:environmentCharacteristic");
            ece.setTextContent(environmentCharacteristic);
            root.appendChild(ece);
            
            for(String p:getEnvironmentPurpose()){
                Element epe = doc.createElementNS(NS.PREMIS.ns(),"premis:environmentPurpose");
                epe.setTextContent(p);
                root.appendChild(epe);
            }            
            for(String n:getEnvironmentNote()){
                Element ne = doc.createElementNS(NS.PREMIS.ns(),"premis:environmentNote");
                ne.setTextContent(n);
                root.appendChild(ne);
            }
            for(PremisDependency dep:getDependency()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:dependency");
                dep.marshal(e,doc);
                root.appendChild(e);
            }
            for(PremisSoftware soft:getSoftware()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:software");
                soft.marshal(e,doc);
                root.appendChild(e);
            }
            for(PremisHardware hard:getHardware()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:software");
                hard.marshal(e,doc);
                root.appendChild(e);
            }
            for(Element ext:getEnvironmentExtension()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:environmentExtension");
                e.appendChild(doc.importNode(ext,true));
                root.appendChild(e);
            }  
            for(MdSec m:getMdSec()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:mdSec");
                m.marshal(e,doc);
                root.appendChild(e);
            }
        }
    }
    public static class PremisDependency implements ElementInterface{
        ArrayList<String>dependencyName;
        ArrayList<PremisDependencyIdentifier>dependencyIdentifier;

        public ArrayList<String> getDependencyName() {
            if(dependencyName == null){
                dependencyName = new ArrayList<String>();
            }
            return dependencyName;
        }
        public ArrayList<PremisDependencyIdentifier> getDependencyIdentifier() {
            if(dependencyIdentifier == null){
                dependencyIdentifier = new ArrayList<PremisDependencyIdentifier>();
            }
            return dependencyIdentifier;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {
                       
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("dependencyName")) {
                    getDependencyName().add(child.getTextContent());
                }else if(localName.equals("dependencyIdentifier")) {
                    PremisDependencyIdentifier id = new PremisDependencyIdentifier();
                    id.unmarshal(child);
                    getDependencyIdentifier().add(id);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            
            //elements
            for(String dname:getDependencyName()){
                Element ne = doc.createElementNS(NS.PREMIS.ns(),"premis:dependencyName");
                ne.setTextContent(dname);
                root.appendChild(ne);
            }
            for(PremisDependencyIdentifier di:getDependencyIdentifier()){
                Element die = doc.createElementNS(NS.PREMIS.ns(),"premis:dependencyIdentifier");
                di.marshal(die,doc);
                root.appendChild(die);
            }
            
            
        }
        
        
    }
    public static class PremisDependencyIdentifier implements ElementInterface{
        String depencyIdentifierType;
        String depencyIdentifierValue;

        public String getDepencyIdentifierType() {
            return depencyIdentifierType;
        }
        public void setDepencyIdentifierType(String depencyIdentifierType) {
            this.depencyIdentifierType = depencyIdentifierType;
        }
        public String getDepencyIdentifierValue() {
            return depencyIdentifierValue;
        }
        public void setDepencyIdentifierValue(String depencyIdentifierValue) {
            this.depencyIdentifierValue = depencyIdentifierValue;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {

            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("depencyIdentifierType")) {
                    depencyIdentifierType = child.getTextContent();
                }else if(localName.equals("depencyIdentifierValue")) {
                    depencyIdentifierValue = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                            
            //elements
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:depencyIdentifierType");
            t.setTextContent(depencyIdentifierType);
            root.appendChild(t);

            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:depencyIdentifierValue");
            v.setTextContent(depencyIdentifierValue);
            root.appendChild(v);
        }  
    }
    public static class PremisSoftware implements ElementInterface{
        String swName;
        String swVersion;
        String swType;
        ArrayList<String>swOtherInformation;
        ArrayList<String>swDependency;

        public String getSwName() {
            return swName;
        }

        public void setSwName(String swName) {
            this.swName = swName;
        }

        public String getSwVersion() {
            return swVersion;
        }

        public void setSwVersion(String swVersion) {
            this.swVersion = swVersion;
        }

        public String getSwType() {
            return swType;
        }

        public void setSwType(String swType) {
            this.swType = swType;
        }

        public ArrayList<String> getSwOtherInformation() {
            if(swOtherInformation == null){
                swOtherInformation = new ArrayList<String>();
            }
            return swOtherInformation;
        }
        public ArrayList<String> getSwDependency() {
            if(swDependency == null){
                swDependency = new ArrayList<String>();
            }
            return swDependency;
        }  
         @Override
        public void unmarshal(Element root) throws ParseException {
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("swName")) {
                    swName = child.getTextContent();
                }else if(localName.equals("swVersion")) {
                    swVersion = child.getTextContent();
                }else if(localName.equals("swType")) {
                    swType = child.getTextContent();
                }else if(localName.equals("swOtherInformation")) {
                    getSwOtherInformation().add(child.getTextContent());
                }else if(localName.equals("swDependency")) {
                    getSwDependency().add(child.getTextContent());
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                       
            //elements
            Element ne = doc.createElementNS(NS.PREMIS.ns(),"premis:swName");
            ne.setTextContent(swName);
            root.appendChild(ne);
            
            Element ve = doc.createElementNS(NS.PREMIS.ns(),"premis:swVersion");
            ve.setTextContent(swVersion);
            root.appendChild(ve);
            
            Element te = doc.createElementNS(NS.PREMIS.ns(),"premis:swType");
            te.setTextContent(swType);
            root.appendChild(te);
            
            for(String oi:getSwOtherInformation()){
                Element oie = doc.createElementNS(NS.PREMIS.ns(),"premis:swOtherInformation");
                oie.setTextContent(oi);
                root.appendChild(oie);
            }
            for(String d:getSwDependency()){
                Element de = doc.createElementNS(NS.PREMIS.ns(),"premis:swDependency");
                de.setTextContent(d);
                root.appendChild(de);
            }
        }
    }
    public static class PremisHardware implements ElementInterface{
        String hwName;
        String hwType;
        ArrayList<String>hwOtherInformation;

        public String getHwName() {
            return hwName;
        }

        public void setHwName(String hwName) {
            this.hwName = hwName;
        }

        public String getHwType() {
            return hwType;
        }

        public void setHwType(String hwType) {
            this.hwType = hwType;
        }

        public ArrayList<String> getHwOtherInformation() {
            if(hwOtherInformation == null){
                hwOtherInformation = new ArrayList<String>();
            }
            return hwOtherInformation;
        }    
        @Override
        public void unmarshal(Element root) throws ParseException {           
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("hwName")) {
                    hwName = child.getTextContent();
                }else if(localName.equals("hwType")) {
                    hwType = child.getTextContent();
                }else if(localName.equals("hwOtherInformation")) {
                    getHwOtherInformation().add(child.getTextContent());
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            
            //elements
            Element ne = doc.createElementNS(NS.PREMIS.ns(),"premis:hwName");
            ne.setTextContent(hwName);
            root.appendChild(ne);
            
            Element te = doc.createElementNS(NS.PREMIS.ns(),"premis:hwType");
            te.setTextContent(hwType);
            root.appendChild(te);
            
            for(String oi:getHwOtherInformation()){
                Element oie = doc.createElementNS(NS.PREMIS.ns(),"premis:hwOtherInformation");
                oie.setTextContent(oi);
                root.appendChild(oie);
            }
        }
    }
    public static class PremisSignatureInformation implements ElementInterface{
        PremisSignature signature;
        ArrayList<Element>signatureInformationExtension;
        ArrayList<MdSec> mdSec;
        
        public ArrayList<MdSec> getMdSec() {
            if(mdSec == null){
                mdSec = new ArrayList<MdSec>();
            }
            return mdSec;
        }

        public ArrayList<Element> getSignatureInformationExtension() {
            if(signatureInformationExtension == null){
                signatureInformationExtension = new ArrayList<Element>();
            }
            return signatureInformationExtension;
        }        
        public PremisSignature getSignature() {            
            return signature;
        }
        public void setSignature(PremisSignature signature) {
            this.signature = signature;
        }   
        @Override
        public void unmarshal(Element root) throws ParseException {
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("signature")) {
                    signature = new PremisSignature();
                    signature.unmarshal(child);                    
                }else if(localName.equals("signatureInformationExtension")) {                    
                    getSignatureInformationExtension().add((Element) child.getFirstChild());                    
                }else if(localName.equals("mdSec")) {
                    MdSec m = new MdSec("");
                    m.unmarshal(child);
                    getMdSec().add(m);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                                    
            //elements
            if(signature != null){
                Element s = doc.createElementNS(NS.PREMIS.ns(),"premis:signature");
                signature.marshal(s,doc);
                root.appendChild(s);
            }
            for(Element sinfo:getSignatureInformationExtension()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:signatureInformationExtension");
                e.appendChild(doc.importNode(sinfo,true));
                root.appendChild(e);
            }
            for(MdSec m:getMdSec()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:mdSec");
                m.marshal(e,doc);
                root.appendChild(e);
            }
        }  
    }
    public static class PremisSignature implements ElementInterface{
        String signatureEncoding;
        String signer;
        String signatureMethod;
        String signatureValue;
        String signatureValidationRules;
        ArrayList<String>signatureProperties;
        ArrayList<Element> keyInformation;
        ArrayList<MdSec> mdSec;
        
        public ArrayList<MdSec> getMdSec() {
            if(mdSec == null){
                mdSec = new ArrayList<MdSec>();
            }
            return mdSec;
        }

        public String getSignatureEncoding() {
            return signatureEncoding;
        }

        public void setSignatureEncoding(String signatureEncoding) {
            this.signatureEncoding = signatureEncoding;
        }

        public String getSigner() {
            return signer;
        }

        public void setSigner(String signer) {
            this.signer = signer;
        }

        public String getSignatureMethod() {
            return signatureMethod;
        }

        public void setSignatureMethod(String signatureMethod) {
            this.signatureMethod = signatureMethod;
        }

        public String getSignatureValue() {
            return signatureValue;
        }

        public void setSignatureValue(String signatureValue) {
            this.signatureValue = signatureValue;
        }

        public String getSignatureValidationRules() {
            return signatureValidationRules;
        }

        public void setSignatureValidationRules(String signatureValidationRules) {
            this.signatureValidationRules = signatureValidationRules;
        }

        public ArrayList<String> getSignatureProperties() {
            if(signatureProperties == null){
                signatureProperties = new ArrayList<String>();
            }
            return signatureProperties;
        }

        public ArrayList<Element> getKeyInformation() {
            return keyInformation;
        }
       
        @Override
        public void unmarshal(Element root) throws ParseException {            
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("signatureEncoding")) {
                    signatureEncoding = child.getTextContent();
                }else if(localName.equals("signer")) {                    
                    signer = child.getTextContent();
                }else if(localName.equals("signatureMethod")) {
                    signatureMethod = child.getTextContent();
                }else if(localName.equals("signatureValue")) {
                    signatureValue = child.getTextContent();
                }else if(localName.equals("signatureValidationRules")) {
                    signatureValidationRules = child.getTextContent();
                }else if(localName.equals("signatureProperties")) {
                    getSignatureProperties().add(child.getTextContent());
                }else if(localName.equals("keyInformation")) {
                    getKeyInformation().add((Element) child.getFirstChild());                    
                }else if(localName.equals("mdSec")) {
                    MdSec m = new MdSec("");
                    m.unmarshal(child);
                    getMdSec().add(m);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            
            Element see = doc.createElementNS(NS.PREMIS.ns(),"premis:signatureEncoding");
            see.setTextContent(signatureEncoding);
            root.appendChild(see);
            
            if(signer != null){
                Element se = doc.createElementNS(NS.PREMIS.ns(),"premis:signer");
                se.setTextContent(signer);
                root.appendChild(se);
            }
            
            Element sme = doc.createElementNS(NS.PREMIS.ns(),"premis:signatureMethod");
            sme.setTextContent(signatureMethod);
            root.appendChild(sme);
            
            Element sve = doc.createElementNS(NS.PREMIS.ns(),"premis:signatureValue");
            sve.setTextContent(signatureValue);
            root.appendChild(sve);
            
            Element svre = doc.createElementNS(NS.PREMIS.ns(),"premis:signatureValidationRules");
            svre.setTextContent(signatureValidationRules);
            root.appendChild(svre);
            
            for(String sp:getSignatureProperties()){
                Element spe = doc.createElementNS(NS.PREMIS.ns(),"premis:signatureProperties");
                spe.setTextContent(sp);
                root.appendChild(spe);
            }
            
            for(Element k:getKeyInformation()){                
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:keyInformation");
                e.appendChild(doc.importNode(k,true));
                root.appendChild(e);
            }
            for(MdSec m:getMdSec()){
                Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:mdSec");
                m.marshal(e,doc);
                root.appendChild(e);
            }
        }
    }
    public static class PremisRelationship implements ElementInterface{
        String relationshipType;
        String relationshipSubType;
        ArrayList<PremisRelatedObjectIdentification>relatedObjectIdentification;
        ArrayList<PremisRelatedEventIdentification>relatedEventIdentification;

        public String getRelationshipType() {
            return relationshipType;
        }

        public void setRelationshipType(String relationshipType) {
            this.relationshipType = relationshipType;
        }

        public String getRelationshipSubType() {
            return relationshipSubType;
        }

        public void setRelationshipSubType(String relationshipSubType) {
            this.relationshipSubType = relationshipSubType;
        }

        public ArrayList<PremisRelatedObjectIdentification> getRelatedObjectIdentification() {
            if(relatedObjectIdentification == null){
                relatedObjectIdentification = new ArrayList<PremisRelatedObjectIdentification>();
            }
            return relatedObjectIdentification;
        }

        public ArrayList<PremisRelatedEventIdentification> getRelatedEventIdentification() {
            if(relatedEventIdentification == null){
                relatedEventIdentification = new ArrayList<PremisRelatedEventIdentification>();
            }
            return relatedEventIdentification;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {            
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("relationshipType")) {
                    relationshipType = child.getTextContent();
                }else if(localName.equals("relationshipSubType")) {
                    relationshipSubType = child.getTextContent();
                }else if(localName.equals("relatedObjectIdentification")) {
                    PremisRelatedObjectIdentification oi = new PremisRelatedObjectIdentification();
                    oi.unmarshal(child);
                    getRelatedObjectIdentification().add(oi);
                }else if(localName.equals("relatedEventIdentification")) {
                    PremisRelatedEventIdentification ei = new PremisRelatedEventIdentification();
                    ei.unmarshal(child);
                    getRelatedEventIdentification().add(ei);
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                                   
            
            //elements
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:relationshipType");
            t.setTextContent(relationshipType);
            root.appendChild(t);
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:relationshipSubType");
            v.setTextContent(relationshipSubType);
            root.appendChild(v);
            
            for(PremisRelatedObjectIdentification oi:getRelatedObjectIdentification()){
                Element oe = doc.createElementNS(NS.PREMIS.ns(),"premis:relatedObjectIdentification");
                oi.marshal(oe,doc);
                root.appendChild(oe);
            }
            for(PremisRelatedEventIdentification ei:getRelatedEventIdentification()){
                Element ee = doc.createElementNS(NS.PREMIS.ns(),"premis:relatedEventIdentification");
                ei.marshal(ee,doc);
                root.appendChild(ee);
            }            
        }
        
    }
    public static class PremisRelatedObjectIdentification implements ElementInterface{
        String relationObjectIdentifierType;
        String relationObjectIdentifierValue;
        int relatedObjectSequence;        
        //=>attributes
        String RelObjectXmlID;

        public String getRelationObjectIdentifierType() {
            return relationObjectIdentifierType;
        }

        public void setRelationObjectIdentifierType(String relationObjectIdentifierType) {
            this.relationObjectIdentifierType = relationObjectIdentifierType;
        }

        public String getRelationObjectIdentifierValue() {
            return relationObjectIdentifierValue;
        }

        public void setRelationObjectIdentifierValue(String relationObjectIdentifierValue) {
            this.relationObjectIdentifierValue = relationObjectIdentifierValue;
        }

        public int getRelatedObjectSequence() {
            return relatedObjectSequence;
        }

        public void setRelatedObjectSequence(int relatedObjectSequence) {
            this.relatedObjectSequence = relatedObjectSequence;
        }

        public String getRelObjectXmlID() {
            return RelObjectXmlID;
        }

        public void setRelObjectXmlID(String RelObjectXmlID) {
            this.RelObjectXmlID = RelObjectXmlID;
        }
        @Override
        public void unmarshal(Element root) throws ParseException {
            NamedNodeMap attrs = root.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Attr attr = (Attr) attrs.item(i);
                String name = attr.getName();
                String value = attr.getNodeValue();
                if(name.equals("RelObjectXmlID")){
                    RelObjectXmlID = value;
                    break;
                }
            }
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("relationObjectIdentifierType")) {
                    relationObjectIdentifierType = child.getTextContent();
                }else if(localName.equals("relationObjectIdentifierValue")) {
                    relationObjectIdentifierValue = child.getTextContent();
                }else if(localName.equals("relatedObjectSequence")) {
                    relatedObjectSequence = Integer.parseInt(child.getTextContent());
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            //attributes
            if(RelObjectXmlID != null){
                root.setAttribute("RelObjectXmlID",RelObjectXmlID);
            }
            
            //elements
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:relationObjectIdentifierType");
            t.setTextContent(relationObjectIdentifierType);
            root.appendChild(t);
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:relationObjectIdentifierValue");
            v.setTextContent(relationObjectIdentifierValue);
            root.appendChild(v);
            
            Element s = doc.createElementNS(NS.PREMIS.ns(),"premis:relatedObjectSequence");
            s.setTextContent(""+relatedObjectSequence);
            root.appendChild(s);
        }
        
    }
    public static class PremisRelatedEventIdentification implements ElementInterface{
        String relatedEventIdentifierType;
        String relatedEventIdentifierValue;
        int relatedEventSequence;        
        //=>attributes
        String RelEventXmlID;

        public String getRelatedEventIdentifierType() {
            return relatedEventIdentifierType;
        }

        public void setRelatedEventIdentifierType(String relatedEventIdentifierType) {
            this.relatedEventIdentifierType = relatedEventIdentifierType;
        }

        public String getRelatedEventIdentifierValue() {
            return relatedEventIdentifierValue;
        }

        public void setRelatedEventIdentifierValue(String relatedEventIdentifierValue) {
            this.relatedEventIdentifierValue = relatedEventIdentifierValue;
        }

        public int getRelatedEventSequence() {
            return relatedEventSequence;
        }

        public void setRelatedEventSequence(int relatedEventSequence) {
            this.relatedEventSequence = relatedEventSequence;
        }

        public String getRelEventXmlID() {
            return RelEventXmlID;
        }

        public void setRelEventXmlID(String RelEventXmlID) {
            this.RelEventXmlID = RelEventXmlID;
        }

        @Override
        public void unmarshal(Element root) throws ParseException {
            NamedNodeMap attrs = root.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Attr attr = (Attr) attrs.item(i);
                String name = attr.getName();
                String value = attr.getNodeValue();
                if(name.equals("RelEventXmlID")){
                    RelEventXmlID = value;
                    break;
                }
            }
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("relatedEventIdentifierType")) {
                    relatedEventIdentifierType = child.getTextContent();
                }else if(localName.equals("relatedEventIdentifierValue")) {
                    relatedEventIdentifierValue = child.getTextContent();
                }else if(localName.equals("relatedEventSequence")) {
                    relatedEventSequence = Integer.parseInt(child.getTextContent());
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            //attributes
            if(RelEventXmlID != null){
                root.setAttribute("RelEventXmlID",RelEventXmlID);
            }
            
            //elements
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:relatedEventIdentifierType");
            t.setTextContent(relatedEventIdentifierType);
            root.appendChild(t);
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:relatedEventIdentifierValue");
            v.setTextContent(relatedEventIdentifierValue);
            root.appendChild(v);
            
            Element s = doc.createElementNS(NS.PREMIS.ns(),"premis:relatedEventSequence");
            s.setTextContent(""+relatedEventSequence);
            root.appendChild(s);
        }
        
    }
    public static class PremisLinkingEventIdentifier implements ElementInterface{
        String linkingEventIdentifierType;
        String linkingEventIdentifierValue;        
        //attribute
        String LinkEventXmlID;
        public String getLinkingEventIdentifierType() {
            return linkingEventIdentifierType;
        }
        public void setLinkingEventIdentifierType(String linkingEventIdentifierType) {
            this.linkingEventIdentifierType = linkingEventIdentifierType;
        }
        public String getLinkingEventIdentifierValue() {
            return linkingEventIdentifierValue;
        }
        public void setLinkingEventIdentifierValue(String linkingEventIdentifierValue) {
            this.linkingEventIdentifierValue = linkingEventIdentifierValue;
        }
        public String getLinkEventXmlID() {
            return LinkEventXmlID;
        }
        public void setLinkEventXmlID(String LinkEventXmlID) {
            this.LinkEventXmlID = LinkEventXmlID;
        }     
        @Override
        public void unmarshal(Element root) throws ParseException {
            NamedNodeMap attrs = root.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Attr attr = (Attr) attrs.item(i);
                String name = attr.getName();
                String value = attr.getNodeValue();
                if(name.equals("LinkEventXmlID")){
                    LinkEventXmlID = value;
                    break;
                }
            }
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("linkingEventIdentifierType")) {
                    linkingEventIdentifierType = child.getTextContent();
                }else if(localName.equals("linkingEventIdentifierValue")) {
                    linkingEventIdentifierValue = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            //attributes
            if(LinkEventXmlID != null){
                root.setAttribute("LinkEventXmlID",LinkEventXmlID);
            }
            
            //elements
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingEventIdentifierType");
            t.setTextContent(linkingEventIdentifierType);
            root.appendChild(t);
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingEventIdentifierValue");
            v.setTextContent(linkingEventIdentifierValue);
            root.appendChild(v);
        }
    }    
    public static class PremisLinkingIntellectualEntityIdentifier implements ElementInterface{
        String linkingIntellectualEntityIdentifierType;
        String linkingIntellectualEntityIdentifierValue;        

        public String getLinkingIntellectualEntityIdentifierType() {
            return linkingIntellectualEntityIdentifierType;
        }

        public void setLinkingIntellectualEntityIdentifierType(String linkingIntellectualEntityIdentifierType) {
            this.linkingIntellectualEntityIdentifierType = linkingIntellectualEntityIdentifierType;
        }

        public String getLinkingIntellectualEntityIdentifierValue() {
            return linkingIntellectualEntityIdentifierValue;
        }

        public void setLinkingIntellectualEntityIdentifierValue(String linkingIntellectualEntityIdentifierValue) {
            this.linkingIntellectualEntityIdentifierValue = linkingIntellectualEntityIdentifierValue;
        }       
               
        @Override
        public void unmarshal(Element root) throws ParseException {            
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("linkingIntellectualEntityIdentifierType")) {
                    linkingIntellectualEntityIdentifierType = child.getTextContent();
                }else if(localName.equals("linkingIntellectualEntityIdentifierValue")) {
                    linkingIntellectualEntityIdentifierValue = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {                        
            //elements
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingIntellectualEntityIdentifierType");
            t.setTextContent(linkingIntellectualEntityIdentifierType);
            root.appendChild(t);
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingIntellectualEntityIdentifierValue");
            v.setTextContent(linkingIntellectualEntityIdentifierValue);
            root.appendChild(v);
        }
    }
    public static class PremisLinkingRightsStatementIdentifier implements ElementInterface {
        String linkingRightsStatementIdentifierType;
        String linkingRightsStatementIdentifierValue;
        //attributes
        String LinkPermissionStatementXmlID;

        public String getLinkingRightsStatementIdentifierType() {
            return linkingRightsStatementIdentifierType;
        }

        public void setLinkingRightsStatementIdentifierType(String linkingRightsStatementIdentifierType) {
            this.linkingRightsStatementIdentifierType = linkingRightsStatementIdentifierType;
        }

        public String getLinkingRightsStatementIdentifierValue() {
            return linkingRightsStatementIdentifierValue;
        }

        public void setLinkingRightsStatementIdentifierValue(String linkingRightsStatementIdentifierValue) {
            this.linkingRightsStatementIdentifierValue = linkingRightsStatementIdentifierValue;
        }

        public String getLinkPermissionStatementXmlID() {
            return LinkPermissionStatementXmlID;
        }

        public void setLinkPermissionStatementXmlID(String LinkPermissionStatementXmlID) {
            this.LinkPermissionStatementXmlID = LinkPermissionStatementXmlID;
        }

        @Override
        public void unmarshal(Element root) throws ParseException {
            NamedNodeMap attrs = root.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Attr attr = (Attr) attrs.item(i);
                String name = attr.getName();
                String value = attr.getNodeValue();
                if(name.equals("LinkPermissionStatementXmlID")){
                    LinkPermissionStatementXmlID = value;
                    break;
                }
            }
            
            ArrayList<Element>children = (ArrayList<Element>) DOMHelp.getChildElements(root);
            for(Element child:children) {
                String localName = child.getLocalName();
                if(localName.equals("linkingRightsStatementIdentifierType")) {
                    linkingRightsStatementIdentifierType = child.getTextContent();
                }else if(localName.equals("linkingRightsStatementIdentifierValue")) {
                    linkingRightsStatementIdentifierValue = child.getTextContent();
                }
            }
        }

        @Override
        public void marshal(Element root, Document doc) {            
            //attributes
            if(LinkPermissionStatementXmlID != null){
                root.setAttribute("LinkPermissionStatementXmlID",LinkPermissionStatementXmlID);
            }
            
            //elements
            Element t = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingRightsStatementIdentifierType");
            t.setTextContent(linkingRightsStatementIdentifierType);
            root.appendChild(t);
            Element v = doc.createElementNS(NS.PREMIS.ns(),"premis:linkingRightsStatementIdentifierValue");
            v.setTextContent(linkingRightsStatementIdentifierValue);
            root.appendChild(v);
        }        
    }
}
