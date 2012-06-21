/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.loc.repository.bagger.constants;

/**
 *
 * @author nicolas
 */
public enum Schema {
    /*
        XSD schema's
    */
    METS("http://www.loc.gov/standards/mets/mets.xsd"),
    EAD("http://www.loc.gov/ead/ead.xsd"),
    PREMIS("http://www.loc.gov/standards/premis/premis.xsd"),
    MODS("http://www.loc.gov/standards/mods/mods.xsd"),
    MADS("http://www.loc.gov/standards/mads/mads.xsd"),
    MARC21("http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd"),
    RDF("http://www.w3.org/2009/XMLSchema/XMLSchema.xsd"),
    SIMPLE_DC("http://dublincore.org/schemas/xmls/simpledc20021212.xsd"),
    QUALIFIED_DC("http://dublincore.org/schemas/xmls/qdc/dc.xsd"),
    QUALIFIED_DCTERMS("http://dublincore.org/schemas/xmls/qdc/dcterms.xsd"),
    QUALIFIED_DCMITYPE("http://dublincore.org/schemas/xmls/qdc/dcmitype.xsd");
    
    private String s;
    Schema(String s){
        this.s = s;
    }
    @Override
    public String toString(){
        return this.s;
    }
}
