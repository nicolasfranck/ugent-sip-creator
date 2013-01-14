package ugent.premis;

/*
 * Kloon van com.anearalone.mets.NS
 * Copyright van Jon Stroop (2011)
 */

public enum NS {    
    DC("http://purl.org/dc/elements/1.1/"),   
    DCTERMS("http://purl.org/dc/terms/"),    
    EAD("urn:isbn:1-931666-22-9", "http://www.loc.gov/ead/ead.xsd"),    
    METS("http://www.loc.gov/METS/", "http://www.loc.gov/standards/mets/mets.xsd"),    
    MODS("http://www.loc.gov/mods/v3", "http://www.loc.gov/standards/mods/v3/mods.xsd"),   
    PREMIS("info:lc/xmlns/premis-v2", "http://www.loc.gov/standards/premis/premis.xsd"),    
    RDF("http://www.w3.org/1999/02/22-rdf-syntax-ns#"),    
    TEI("http://www.tei-c.org/ns/1.0"),    
    VRA_4("http://www.vraweb.org/vracore4.htm", "http://www.vraweb.org/projects/vracore4/vra-4.0-restricted.xsd"),    
    VRA_3("http://www.vraweb.org/vracore3.htm"),   
    XLINK("http://www.w3.org/1999/xlink"),
    XSI("http://www.w3.org/2001/XMLSchema-instance"),    
    XMP("adobe:ns:meta/"),
    XMLNS("http://www.w3.org/2000/xmlns/");

    String ns;
    String schema;

    NS(String ns) {
        this.ns = ns;
    };

    NS(String ns, String schemaLoc) {
        this.ns = ns;
        this.schema = schemaLoc;
    };

    /**
     * @return a String representing the namespace URI associated with the object.
     */
    public String ns() {
        return ns;
    };

    /**
     * @return a String representing the namespace URI associated with the object.
     */
    public String value() {
        return ns;
    };

    /**
     * @return A String representing the URL for the schema, if one exists, else null.
     */
    public String loc() {
        return schema;
    }

    // will this work?
    /**
     * @return An String to be used as a value for {@code xsi:schemaLocation} if a schema exists,
     *         else null.
     */
    public String schemaLoc() {
        if (schema != null) {
            return ns + " " + schema;
        }
        else {
            return null;
        }
    }

    public static NS fromNamespace(String namespace) {
        for (NS ns : NS.values()) {
            if (ns.ns.equals(namespace)) {
                return ns;
            }
        }
        throw new IllegalArgumentException(namespace);
    }

}
