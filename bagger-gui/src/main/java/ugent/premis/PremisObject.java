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
    private ArrayList<PremisObjectStorage> storage;

    public ArrayList<PremisObjectStorage> getStorage() {
        if(storage == null){
            storage = new ArrayList<PremisObjectStorage>();
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
                PremisObjectStorage s = new PremisObjectStorage();
                s.unmarshal(child);
                getStorage().add(s);
            }
        }
    }

    @Override
    public void marshal(Element e, Document d) {
        
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
        private ArrayList<PremisObjectCharacteristicsFixity>fixity;
        private long size;
        private PremisObjectCharacteristicsFormat format;
        private ArrayList<Element> significantProperties;
     
        public ArrayList<Element> getSignificantProperties() {
            if(significantProperties == null){
                significantProperties = new ArrayList<Element>();
            }
            return significantProperties;
        }        
        public PremisObjectCharacteristicsFormat getFormat() {
            return format;
        }
        public void setFormat(PremisObjectCharacteristicsFormat format) {
            this.format = format;
        }
        public long getSize() {
            return size;
        }
        public void setSize(long size) {
            this.size = size;
        }
        public ArrayList<PremisObjectCharacteristicsFixity> getFixity() {
            if(fixity == null){
                fixity = new ArrayList<PremisObjectCharacteristicsFixity>();
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
                    PremisObjectCharacteristicsFixity f = new PremisObjectCharacteristicsFixity();
                    f.unmarshal(child);
                    getFixity().add(f);
                }else if(localName.equals("size")) {
                    size = Long.parseLong(child.getTextContent());
                }else if(localName.equals("format")) {
                    format = new PremisObjectCharacteristicsFormat();
                    format.unmarshal(child);                    
                }else if(localName.equals("significantProperties")) {
                    getSignificantProperties().add(child);
                }
            }
        }

        @Override
        public void marshal(Element e, Document d) {
            
        }
        
        public static class PremisObjectCharacteristicsFixity implements ElementInterface{
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
        public static class PremisObjectCharacteristicsFormat implements ElementInterface{
            private PremisObjectCharacteristicsFormatDesignation formatDesignation;
            private PremisObjectCharacteristicsFormatRegistry formatRegistry;

            public PremisObjectCharacteristicsFormatDesignation getFormatDesignation() {
                return formatDesignation;
            }
            public void setFormatDesignation(PremisObjectCharacteristicsFormatDesignation formatDesignation) {
                this.formatDesignation = formatDesignation;
            }
            public PremisObjectCharacteristicsFormatRegistry getFormatRegistry() {
                return formatRegistry;
            }
            public void setFormatRegistry(PremisObjectCharacteristicsFormatRegistry formatRegistry) {
                this.formatRegistry = formatRegistry;
            }
            
            @Override
            public void unmarshal(Element e) throws ParseException {
                
            }

            @Override
            public void marshal(Element e, Document d) {
                
            }
            
            public static class PremisObjectCharacteristicsFormatDesignation implements ElementInterface{
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
            public static class PremisObjectCharacteristicsFormatRegistry implements ElementInterface{
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
    public static class PremisObjectStorage implements ElementInterface {
        PremisObjectStorageContentLocation contentLocation;
        String storageMedium;

        public PremisObjectStorageContentLocation getContentLocation() {
            return contentLocation;
        }
        public void setContentLocation(PremisObjectStorageContentLocation contentLocation) {
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
                    contentLocation = new PremisObjectStorageContentLocation();
                    contentLocation.unmarshal(child);
                }else if(localName.equals("storageMedium")) {
                    storageMedium = child.getTextContent();
                }
            } 
        }

        @Override
        public void marshal(Element e, Document d) {
            
        }

        public static class PremisObjectStorageContentLocation implements ElementInterface {
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
