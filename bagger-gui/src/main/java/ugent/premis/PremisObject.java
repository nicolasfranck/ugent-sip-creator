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
    private PremisObjectType type;
    private String xmlID;
    private String version;
    
    //elements
    private ArrayList<PremisObjectIdentifier> objectIdentifier;
    private String preservationLevel;
    private String objectCategory;
    private ArrayList<PremisObjectCharacteristics>objectCharacteristics;
    private ArrayList<PremisStorage> storage;

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
    public String getPreservationLevel() {
        return preservationLevel;
    }

    public void setPreservationLevel(String preservationLevel) {
        this.preservationLevel = preservationLevel;
    }

    public String getObjectCategory() {
        return objectCategory;
    }

    public void setObjectCategory(String objectCategory) {
        this.objectCategory = objectCategory;
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
        private String c;
        private PremisObjectType(String c){
            this.c = c;
        }
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
                type = PremisObjectType.valueOf(value);
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
                preservationLevel = child.getTextContent();
            }else if(localName.equals("objectCategory")) {
                objectCategory = child.getTextContent();                
            }else if(localName.equals("objectCharacteristics")) {
                PremisObjectCharacteristics oc = new PremisObjectCharacteristics();
                oc.unmarshal(child);
                getObjectCharacteristics().add(oc);
            }else if(localName.equals("storage")) {
                PremisStorage s = new PremisStorage();
                s.unmarshal(child);
                getStorage().add(s);
            }
        }
    }

    @Override
    public void marshal(Element root, Document doc) {
        //attributes
        root.setAttribute("xmlID",xmlID);
        root.setAttribute("version",version);
        root.setAttributeNS(NS.XSI.ns(),"xsi:type",type.toString());        
        
        //elements
        if(preservationLevel != null){
            Element pe = doc.createElementNS(NS.PREMIS.ns(),"premis:preservationLevel");
            pe.setTextContent(preservationLevel);
            root.appendChild(pe);
        }
        if(objectCategory != null){
            Element pl = doc.createElementNS(NS.PREMIS.ns(),"premis:objectCategory");
            pl.setTextContent(objectCategory);
            root.appendChild(pl);
        }
        for(PremisObjectIdentifier oi:getObjectIdentifier()){
            Element oie = doc.createElementNS(NS.PREMIS.ns(),"premis:objectIdentifier");
            oi.marshal(oie,doc);
            root.appendChild(oie);
        }
        for(PremisObjectCharacteristics oc:getObjectCharacteristics()){
            Element oce = doc.createElementNS(NS.PREMIS.ns(),"premis:objectCharacteristics");
            oc.marshal(oce,doc);
            root.appendChild(oce);
        }
        
    }
    
    public class PremisObjectIdentifier implements ElementInterface {
        private String objectIdentifierType;
        private String objectIdentifierValue;

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
        public void marshal(Element e, Document d) {
            
        }
        
    }
    public static class PremisObjectCharacteristics implements ElementInterface {
        private int compositionLevel;
        private ArrayList<PremisFixity>fixity;
        private long size;
        private PremisFormat format;
        private ArrayList<Element> significantProperties;
     
        public ArrayList<Element> getSignificantProperties() {
            if(significantProperties == null){
                significantProperties = new ArrayList<Element>();
            }
            return significantProperties;
        }        
        public PremisFormat getFormat() {
            return format;
        }
        public void setFormat(PremisFormat format) {
            this.format = format;
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
                    format = new PremisFormat();
                    format.unmarshal(child);                    
                }else if(localName.equals("significantProperties")) {
                    getSignificantProperties().add(child);
                }
            }
        }

        @Override
        public void marshal(Element e, Document d) {
            
        }
        
        public static class PremisFixity implements ElementInterface{
            private String messageDigestAlgorithm;
            private String messageDigest;
            private String messageDigestOriginator;

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
            public void unmarshal(Element e) throws ParseException {
                
            }

            @Override
            public void marshal(Element e, Document d) {
                
            }            
        }
        public static class PremisFormat implements ElementInterface{
            private PremisFormatDesignation formatDesignation;
            private PremisFormatRegistry formatRegistry;

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
            public void unmarshal(Element e) throws ParseException {
                
            }

            @Override
            public void marshal(Element e, Document d) {
                
            }
            
            public static class PremisFormatDesignation implements ElementInterface{
                private String formatName;
                private String formatVersion;

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
                public void unmarshal(Element e) throws ParseException {
                    
                }
                @Override
                public void marshal(Element e, Document d) {
                    
                }                
            }
            public static class PremisFormatRegistry implements ElementInterface{
                private String formatRegistryName;
                private String formatRegistryKey;
                private String formatRegistryRole;

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
                public void unmarshal(Element e) throws ParseException {
                    
                }
                @Override
                public void marshal(Element e, Document d) {
                    
                }                
            }
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
        public void marshal(Element e, Document d) {
            
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
            public void marshal(Element e, Document d) {
                
            }

        }            
    }
}
