/**
 * 
 * Copyright 2008 The Australian National University (ANU)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.edu.apsr.mtk.base;

/**
 * Class containing METS-related constants
 * 
 * @author Scott Yeadon
 *
 */
public class Constants
{
    public static final String SCHEMA_METS = "http://www.loc.gov/standards/mets/mets.xsd";
    
    /** XLink namespace URL */
    public static final String NS_XLINK = "http://www.w3.org/1999/xlink";
    public static final String NS_METS = "http://www.loc.gov/METS/";

    /** Name of the METS root element */
    public static final String ELEMENT_METS = "mets";

    /** Name of the PROFILE attribute */
    public static final String ATTRIBUTE_PROFILE = "PROFILE";

    /** Name of the TYPE attribute */
    public static final String ATTRIBUTE_TYPE = "TYPE";

    /** Name of the OBJID attribute */
    public static final String ATTRIBUTE_OBJID = "OBJID";
    
    /** Name of the ID attribute */
    public static final String ATTRIBUTE_ID = "ID";
    
    /** Name of the metsHdr root element */
    public static final String ELEMENT_METSHDR = "metsHdr";

    /** Name of the CREATEDATE attribute */
    public static final String ATTRIBUTE_CREATEDATE = "CREATEDATE";

    /** Name of the LASTMODDATE attribute */
    public static final String ATTRIBUTE_LASTMODDATE = "LASTMODDATE";
    
    /** Name of the RECORDSTATUS attribute */
    public static final String ATTRIBUTE_RECORDSTATUS = "RECORDSTATUS";

    /** Name of the agent element */
    public static final String ELEMENT_AGENT = "agent";

    /** Name of the ROLE attribute */
    public static final String ATTRIBUTE_ROLE = "ROLE";

    /** Name of the OTHERROLE attribute */
    public static final String ATTRIBUTE_OTHERROLE = "OTHERROLE";

    /** Name of the OTHERTYPE attribute */
    public static final String ATTRIBUTE_OTHERTYPE = "OTHERTYPE";

    /** Name of the name element */
    public static final String ELEMENT_NAME = "name";
    
    /** Name of the note element */
    public static final String ELEMENT_NOTE = "note";

    /** Name of the altRecordID element */
    public static final String ELEMENT_ALTRECORDID = "altRecordID";

    /** Name of the GROUPID attribute */
    public static final String ATTRIBUTE_GROUPID = "GROUPID";
    
    /** Name of the ADMID attribute */
    public static final String ATTRIBUTE_ADMID = "ADMID";

    /** Name of the CREATED attribute */
    public static final String ATTRIBUTE_CREATED = "CREATED";

    /** Name of the STATUS attribute */
    public static final String ATTRIBUTE_STATUS = "STATUS";

    /** Name of the metsDocumentID element */
    public static final String ELEMENT_METSDOCUMENTID = "metsDocumentID";

    /** Name of the dmdSec element */
    public static final String ELEMENT_DMDSEC = "dmdSec";

    /** Name of the mdRef element */
    public static final String ELEMENT_MDREF = "mdRef";

    /** Name of the XPTR attribute */
    public static final String ATTRIBUTE_XPTR = "XPTR";

    /** Name of the MDTYPE attribute */
    public static final String ATTRIBUTE_MDTYPE = "MDTYPE";

    /** Name of the MDTYPEVERSION attribute */
    public static final String ATTRIBUTE_MDTYPEVERSION = "MDTYPEVERSION";

    /** Name of the OTHERMDTYPE attribute */
    public static final String ATTRIBUTE_OTHERMDTYPE = "OTHERMDTYPE";
    
    /** Name of the mdWrap element */
    public static final String ELEMENT_MDWRAP = "mdWrap";
    
    /** Name of the amdSec element */
    public static final String ELEMENT_AMDSEC = "amdSec";
    
    /** Name of the structLink element */
    public static final String ELEMENT_STRUCTLINK = "structLink";

    /** Name of the smLink element */
    public static final String ELEMENT_SMLINK = "smLink";

    /** Name of the smLinkGrp element */
    public static final String ELEMENT_SMLINKGRP = "smLinkGrp";

    /** Name of the ARCLINKORDER attribute */
    public static final String ATTRIBUTE_ARCLINKORDER = "ARCLINKORDER";

    /** Name of the smLocatorLink element */
    public static final String ELEMENT_SMLOCATORLINK = "smLocatorLink";

    /** Name of the smArcLink element */
    public static final String ELEMENT_SMARCLINK = "smArcLink";

    /** Name of the ARCTYPE attribute */
    public static final String ATTRIBUTE_ARCTYPE = "ARCTYPE";
    
    /** Local name of the xlink:arcrole attribute */
    public static final String ATTRIBUTE_XLINK_ARCROLE = "xlink:arcrole";

    /** Local name of the xlink:arcrole attribute */
    public static final String ATTRIBUTE_XLINK_ARCROLE_LOCAL = "arcrole";

    /** Qualified name of the xlink:title attribute */
    public static final String ATTRIBUTE_XLINK_TITLE = "xlink:title";

    /** Local name of the xlink:title attribute */
    public static final String ATTRIBUTE_XLINK_TITLE_LOCAL = "title";

    /** Qualified name of the xlink:show attribute */
    public static final String ATTRIBUTE_XLINK_SHOW = "xlink:show";

    /** Local name of the xlink:show attribute */
    public static final String ATTRIBUTE_XLINK_SHOW_LOCAL = "show";

    /** Qualified name of the xlink:arcrole attribute */
    public static final String ATTRIBUTE_XLINK_ACTUATE = "xlink:actuate";

    /** Local name of the xlink:arcrole attribute */
    public static final String ATTRIBUTE_XLINK_ACTUATE_LOCAL = "actuate";

    /** Qualified name of the xlink:to attribute */
    public static final String ATTRIBUTE_XLINK_TO = "xlink:to";

    /** Local name of the xlink:to attribute */
    public static final String ATTRIBUTE_XLINK_TO_LOCAL = "to";

    /** Qualified name of the xlink:from attribute */
    public static final String ATTRIBUTE_XLINK_FROM = "xlink:from";
    
    /** Local name of the xlink:from attribute */
    public static final String ATTRIBUTE_XLINK_FROM_LOCAL = "from";

    /** Qualified name of the xlink:type attribute */
    public static final String ATTRIBUTE_XLINK_TYPE = "xlink:type";

    /** Local name of the xlink:type attribute */
    public static final String ATTRIBUTE_XLINK_TYPE_LOCAL = "type";

    /** "simple" value of the xlink:type attribute */
    public static final String ATTRIBUTE_XLINK_TYPE_VALUE_SIMPLE = "simple";

    /** Name of the behaviorSec element */
    public static final String ELEMENT_BEHAVIORSEC = "behaviorSec";

    /** Name of the behavior element */
    public static final String ELEMENT_BEHAVIOR = "behavior";
        
    /** Name of the STRUCTID attribute */
    public static final String ATTRIBUTE_STRUCTID = "STRUCTID";

    /** Name of the BTYPE attribute */
    public static final String ATTRIBUTE_BTYPE = "BTYPE";

    /** Name of the interfaceDef element */
    public static final String ELEMENT_INTERFACEDEF = "interfaceDef";
    
    /** Qualified name of the xlink:href attribute */
    public static final String ATTRIBUTE_XLINK_HREF = "xlink:href";
    
    /** Local name of the xlink:href attribute */
    public static final String ATTRIBUTE_XLINK_HREF_LOCAL = "href";

    /** Qualified name of the xlink:role attribute */
    public static final String ATTRIBUTE_XLINK_ROLE = "xlink:role";

    /** Local name of the xlink:role attribute */
    public static final String ATTRIBUTE_XLINK_ROLE_LOCAL = "role";

    /** Name of the mechanism element */
    public static final String ELEMENT_MECHANISM = "mechanism";

    /** Name of the techMD element */
    public static final String ELEMENT_TECHMD = "techMD";

    /** Name of the sourceMD element */
    public static final String ELEMENT_SOURCEMD = "sourceMD";
    
    /** Name of the rightsMD element */
    public static final String ELEMENT_RIGHTSMD = "rightsMD";

    /** Name of the digiProvMD element */
    public static final String ELEMENT_DIGIPROVMD = "digiprovMD";

    /** Name of the fileSec element */
    public static final String ELEMENT_FILESEC = "fileSec";
    
    /** Name of the FileGrp element */
    public static final String ELEMENT_FILEGRP = "fileGrp";
    
    /** Name of the VERSDATE attribute */
    public static final String ATTRIBUTE_VERSDATE = "VERSDATE";
    
    /** Name of the File element */
    public static final String ELEMENT_FILE = "file";

    /** Name of the DMDID attribute */
    public static final String ATTRIBUTE_DMDID = "DMDID";
    
    /** Name of the SEQ attribute */
    public static final String ATTRIBUTE_SEQ = "SEQ";
    
    /** Name of the USE attribute */
    public static final String ATTRIBUTE_USE = "USE";

    /** Name of the OWNERID attribute */
    public static final String ATTRIBUTE_OWNERID = "OWNERID";

    /** Name of the MIMETYPE attribute */
    public static final String ATTRIBUTE_MIMETYPE = "MIMETYPE";

    /** Name of the SIZE attribute */
    public static final String ATTRIBUTE_SIZE = "SIZE";

    /** Name of the CHECKSUM attribute */
    public static final String ATTRIBUTE_CHECKSUM = "CHECKSUM";

    /** Name of the CHECKSUMTYPE attribute */
    public static final String ATTRIBUTE_CHECKSUMTYPE = "CHECKSUMTYPE";    
    
    /** Name of the binData element */
    public static final String ELEMENT_BINDATA = "binData";
    
    /** Name of the FContent element */
    public static final String ELEMENT_FCONTENT = "FContent";

    /** Name of the xmlData element */
    public static final String ELEMENT_XMLDATA = "xmlData";

    /** Name of the FLocat element */
    public static final String ELEMENT_FLOCAT = "FLocat";

    /** Name of the transformFile element */
    public static final String ELEMENT_TRANSFORMFILE = "transformFile";

    /** Name of the TRANSFORMTYPE attribute */
    public static final String ATTRIBUTE_TRANSFORMTYPE = "TRANSFORMTYPE";

    /** Name of the TRANSFORMALGORITHM attribute */
    public static final String ATTRIBUTE_TRANSFORMALGORITHM = "TRANSFORMALGORITHM";

    /** Name of the TRANSFORMKEY attribute */
    public static final String ATTRIBUTE_TRANSFORMKEY = "TRANSFORMKEY";

    /** Name of the TRANSFORMBEHAVIOR attribute */
    public static final String ATTRIBUTE_TRANSFORMBEHAVIOR = "TRANSFORMBEHAVIOR";

    /** Name of the TRANSFORMORDER attribute */
    public static final String ATTRIBUTE_TRANSFORMORDER = "TRANSFORMORDER";

    /** Name of the stream element */
    public static final String ELEMENT_STREAM = "stream";

    /** Name of the STREAMTYPE attribute */
    public static final String ATTRIBUTE_STREAMTYPE = "streamType";

    /** Name of the LOCTYPE attribute */
    public static final String ATTRIBUTE_LOCTYPE = "LOCTYPE";
    
    /** Name of the OTHERLOCTYPE attribute */
    public static final String ATTRIBUTE_OTHERLOCTYPE = "OTHERLOCTYPE";
    
    /** Name of the LOCTYPE attribute URL value */
    public static final String ATTRIBUTE_VALUE_URL = "URL";
    
    /** Name of the structMap element */
    public static final String ELEMENT_STRUCTMAP = "structMap";
    
    /** Name of the LABEL attribute */
    public static final String ATTRIBUTE_LABEL = "LABEL";
    
    /** Name of the div element */
    public static final String ELEMENT_DIV = "div";
    
    /** Name of the ORDER attribute */
    public static final String ATTRIBUTE_ORDER = "ORDER";
    
    /** Name of the ORDERLABEL attribute */
    public static final String ATTRIBUTE_ORDERLABEL = "ORDERLABEL";
    
    /** Name of the CONTENTIDS attribute */
    public static final String ATTRIBUTE_CONTENTIDS = "CONTENTIDS";
    
    /** Local name of the xlink:label attribute */
    public static final String ATTRIBUTE_XLINK_LABEL_LOCAL = "label";

    /** Qualified name of the xlink:label attribute */
    public static final String ATTRIBUTE_XLINK_LABEL = "xlink:label";
    
    /** Name of the mptr element */
    public static final String ELEMENT_MPTR = "mptr";

    /** Name of the fptr element */
    public static final String ELEMENT_FPTR = "fptr";

    /** Name of the FILEID attribute */
    public static final String ATTRIBUTE_FILEID = "FILEID";
    
    /** Name of the par element */
    public static final String ELEMENT_PAR = "par";

    /** Name of the seq element */
    public static final String ELEMENT_SEQ = "seq";

    /** Name of the area element */
    public static final String ELEMENT_AREA = "area";
    
    /** Name of the SHAPE attribute */
    public static final String ATTRIBUTE_SHAPE = "SHAPE";

    /** Name of the COORDS attribute */
    public static final String ATTRIBUTE_COORDS = "COORDS";

    /** Name of the BEGIN attribute */
    public static final String ATTRIBUTE_BEGIN = "BEGIN";

    /** Name of the END attribute */
    public static final String ATTRIBUTE_END = "END";

    /** Name of the BETYPE attribute */
    public static final String ATTRIBUTE_BETYPE = "BETYPE";

    /** Name of the EXTENT attribute */
    public static final String ATTRIBUTE_EXTENT = "EXTENT";

    /** Name of the EXTTYPE attribute */
    public static final String ATTRIBUTE_EXTTYPE = "EXTTYPE";
}