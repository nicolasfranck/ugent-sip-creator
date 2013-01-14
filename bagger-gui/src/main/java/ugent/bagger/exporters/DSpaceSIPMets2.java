/*
 * Copyright (c) 2008, Hewlett-Packard Company and Massachusetts
 * Institute of Technology.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of the Hewlett-Packard Company nor the name of the
 * Massachusetts Institute of Technology nor the names of their
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package ugent.bagger.exporters;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.FileSec;
import com.anearalone.mets.FileSec.FileGrp;
import com.anearalone.mets.LocatorElement;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MdWrap;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsHdr;
import com.anearalone.mets.MetsHdr.Agent;
import com.anearalone.mets.MetsWriter;
import com.anearalone.mets.SharedEnums;
import com.anearalone.mets.StructMap;
import com.anearalone.mets.StructMap.Div;
import com.anearalone.mets.StructMap.Div.Fptr;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.utilities.MessageDigestHelper;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.vfs2.FileObject;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ugent.bagger.helper.DateUtils;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.XML;
import ugent.premis.Premis;
import ugent.premis.PremisIO;
import ugent.premis.PremisObject;

/**
 *
 * @author nicolas
 * 
 * code based on edu.mit.libraries.facade.app.DSpaceSIP
 * https://wiki.duraspace.org/download/attachments/19006203/DSpaceSIP.java?version=1&modificationDate=1291085198767
 */
public class DSpaceSIPMets2 {
    // Describes the DSpace SIP version implemented here
    static final String METS_PROFILE = "DSpace METS SIP Profile 1.0";

    // default value for validate
    static final boolean VALIDATE_DEFAULT = true;

    // Filename of manifest, relative to package toplevel
    static final String METS_FILE = "mets.xml";    

    // Put all DMD sections for the Item into one group with this ID
    String dmdGroupID = createID();

    // attempt to validate the METS manifest before writing SIP
    boolean validate = VALIDATE_DEFAULT;

    // Zip file compression level
    int compression = 0;

    /**
     * Table of files to add to package, such as mdRef'd metadata.
     * Key is relative pathname of file, value a record of associated paths.
     */
    //Map<String,PackageFile> zipFiles = new HashMap<String,PackageFile>();

    // map of bundle name to list of relative-file-paths in that bundle.
    HashMap<String,ArrayList<PackageFile>>bundles = new HashMap<String,ArrayList<PackageFile>>();

    // relative path of Primary Bitstream (PBS) if any
    PackageFile primaryPackageFile = null;

    // METS manifest object
    Mets mets = null;

    // DMDs to refer back to from structmap
    List<String> dmdIDs = new ArrayList<String>();

    // Simple record, holds the data about each file in this package.
    
    public static abstract class PackageFile {
        protected com.anearalone.mets.FileSec.FileGrp.File metsFile;        
        public FileGrp.File getMetsFile() {
            return metsFile;
        }
        public void setMetsFile(FileGrp.File metsFile) {                        
            this.metsFile = metsFile;
        }
        public abstract String getLongName();        
        public abstract String getShortName();                
        public abstract long lastModified() throws Exception;
        public abstract long getSize() throws Exception;
        public abstract InputStream newInputStream() throws IOException;
    }
    public static class PackageFileSimple extends PackageFile{        
        File file = null;       // absolute path on disk                
        PackageFileSimple(File file){            
            this.file = file;            
        }
        @Override
        public String getLongName() {
            return file.getAbsolutePath();
        }
        @Override
        public String getShortName() {
            return file.getName();
        }
        @Override
        public InputStream newInputStream() throws IOException{
            return new BufferedInputStream(new FileInputStream(file));
        }

        @Override
        public long lastModified() throws Exception{
            return file.lastModified();
        }
        @Override
        public long getSize() throws Exception{
            return file.length();
        }
    }
    public static class PackageFileObject extends PackageFile {
        FileObject fileObject;
        String shortName;
        String longName;
        public PackageFileObject(FileObject fileObject,String shortName,String longName){
            this.fileObject = fileObject;
            this.shortName = shortName;
            this.longName = longName;
        }
        @Override
        public String getLongName() {            
            return longName;
        }
        @Override
        public String getShortName() {
            return shortName;
        }
        @Override
        public InputStream newInputStream() throws IOException {
            return fileObject.getContent().getInputStream();
        }        
        @Override
        public long lastModified() throws Exception{
            return fileObject.getContent().getLastModifiedTime();
        }
        @Override
        public long getSize() throws Exception{
            return fileObject.getContent().getSize();
        }
    }

    /**
     * Default constructor.
     */
    public DSpaceSIPMets2(){        
        this(VALIDATE_DEFAULT, Deflater.DEFAULT_COMPRESSION);
    }

    /**
     * Detailed constructor.
     * @param validate whether or not to validate the resulting METS
     * @param compression level of compression (0-9) to use in Zipfile.
     */
    public DSpaceSIPMets2(boolean validate, int compression){                
        this.validate = validate;
        this.compression = compression;        
    }
    public static String createID(){
        //xsd NCName: moet starten met letter of _, colon (':') mag niet voorkomen
        return "_"+UUID.randomUUID().toString();
    }

    protected Mets getMets(){
        if(mets == null){
            // Initialize manifest -- Create the METS manifest structure
            mets = new Mets();

            // Top-level stuff
            mets.setID(createID());     
            mets.setLabel("DSpace Item");        
            mets.setPROFILE(METS_PROFILE);

            // MetsHdr
            MetsHdr metsHdr = new MetsHdr();        
            mets.setMetsHdr(metsHdr);
        }
        return mets;
    }       

    /**
     * Set the OBJID attribute in the METS manifest
     * @param o new value for OBJID
     */
    public void setOBJID(String o){
        if(o != null){
            getMets().setOBJID(o);
        }
    }

    /**
     * Adds a a Bitstream to this Item, using contents of a File in the filesystem.
     * @param path the File containing the data of this Bitstream.
     * @param name logical pathname within the Item (DSpace Bitstream's "name" attribute)
     * @param isPrimaryBitstream true if this is the Item' Primary Bitstream, i.e. index page of a website.
     */
    public void addBitstream(File path,String name,String bundle,boolean isPrimaryBitstream) throws DatatypeConfigurationException{        
        FileGrp.File mfile = createMetsFile(path,name);        
        PackageFile pfile = new PackageFileSimple(path);        
        pfile.setMetsFile(mfile);        
        addPackageFile(pfile,bundle,isPrimaryBitstream);                
    }
    public void addPackageFile(PackageFile packageFile,String bundle,boolean isPrimaryBitstream){
        if(!bundles.containsKey(bundle)){
            bundles.put(bundle,new ArrayList<PackageFile>());            
        }
        bundles.get(bundle).add(packageFile);        
        if(isPrimaryBitstream) {
            primaryPackageFile = packageFile;
        }
    }
    protected com.anearalone.mets.FileSec.FileGrp.File createMetsFile(File file,String relativePath) throws DatatypeConfigurationException{
        FileGrp.File mfile = new FileGrp.File(createID());        
        
        mfile.setCHECKSUM(MessageDigestHelper.generateFixity(file,Manifest.Algorithm.MD5));
        mfile.setCHECKSUMTYPE(SharedEnums.CHECKSUMTYPE.MD_5);                
        mfile.setMIMETYPE(FUtils.getMimeType(file));               
        mfile.setCREATED(DateUtils.DateToGregorianCalender());       
        mfile.setSIZE(file.length());
        
        FileGrp.File.FLocat flocat = new FileGrp.File.FLocat();
        flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
        flocat.setXlinkHREF(relativePath);
        
        mfile.getFLocat().add(flocat);
        
        return mfile;
    }
    
    /**
     * Adds a section of descriptive metadata (DMD) to the METS document,
     * based on a JDOM element.  The element will be the
     * contents of the dmdSec in the METS document.
     * Note that all DMD sections apply to the entire Item in the SIP.
     * @param type METS metadata type name (e.g. "MODS")
     * @param md JDOM element containing the DMD
     */
    public void addDescriptiveMD(String type, Element element){                

        String dmdID = createID();
        dmdIDs.add(dmdID);
        
        MdSec dmdSec = new MdSec(dmdID);        
        dmdSec.setGROUPID(dmdGroupID);                
        
        MdWrap mdWrap;
        try{
            mdWrap = new MdWrap(MdSec.MDTYPE.fromValue(type));
            mdWrap.setMIMETYPE("text/xml");           
        }catch(Exception e){
            mdWrap = new MdWrap(MdSec.MDTYPE.OTHER);
            mdWrap.setOTHERMDTYPE(type);
        }
        
        mdWrap.getXmlData().add(element);        
        dmdSec.setMdWrap(mdWrap);        
        getMets().getDmdSec().add(dmdSec);       
        
    }

    /**
     * Adds a section of descriptive metadata (DMD) to the METS document,
     * based on String containing serialized XML.  The string is expected
     * to contain one element, which becomes the
     * contents of the dmdSec in the METS document.
     * Note that all DMD sections apply to the entire Item in the SIP.
     * @param type METS metadata type name (e.g. "MODS")
     * @param md serialized XML metadata
     */
    public void addDescriptiveMD(String type, String md) throws SAXException, ParserConfigurationException, IOException{        
        addDescriptiveMD(type,XML.XMLToDocument(new StringReader(md)).getDocumentElement());
    }

    
    /**
     * Adds Agent element to the METS header.
     * @param role one of the acceptable METS roles, e.g. "CUSTODIAN"
     * @param type one of the acceptable METS types e.g. "ORGANIZATION"
     * @param aname proper name of the agent
     */
    public void addAgent(String role, String type, String aname){
        Agent agent;
        try{
            agent = new Agent(Agent.ROLE.fromValue(role),aname);            
        }catch (Exception e){
            agent = new Agent(Agent.ROLE.OTHER,aname);            
            agent.setOTHERROLE(role);            
        }
        try{
            agent.setAGENTTYPE(Agent.AGENTTYPE.fromValue(type.toUpperCase()));            
        }
        catch (Exception e){            
            agent.setAGENTTYPE(Agent.AGENTTYPE.OTHER);
            agent.setOTHERTYPE(type);
        }
        agent.setName(aname);        
        
        getMets().getMetsHdr().getAgent().add(agent);        
    }

    // Create fileSec and structMap, add them to METS manifest.
    // The structMap just lists Bitstreams, and identifies PBS if there is one.
    protected void finishManifest(OutputStream out) throws UnsupportedEncodingException, DatatypeConfigurationException, ParserConfigurationException, TransformerException{
        // fileSec - all non-metadata bundles go into fileGrp,
        // and each bitstream therein into a file.
        // Create the bitstream-level techMd and div's for structmap
        // at the same time so we can connec the IDREFs to IDs.
        FileSec fileSec = new FileSec();

        // log the primary bitstream for structmap
        String primaryBitstreamFileID = null;

        // accumulate content DIV items to put in structMap later.
        List<Div> contentDivs = new ArrayList<Div>();

        for(Map.Entry<String,ArrayList<PackageFile>> e:bundles.entrySet()){
            ArrayList<PackageFile>packageFiles = (ArrayList<PackageFile>)e.getValue();

            // Create a fileGrp
            FileGrp fileGrp = new FileGrp();

            // Bundle name for USE attribute            
            fileGrp.setUse(e.getKey());

            for (PackageFile packageFile : packageFiles){                                                             
                
                // log primary bitstream for later (structMap)
                if(primaryPackageFile != null && primaryPackageFile == packageFile) {
                    primaryBitstreamFileID = packageFile.getMetsFile().getID();
                }

                // if this is content, add to structmap too:
                Div div = new Div();
                div.setID(createID());                
                div.setType("DSpace Content Bitstream");
                Fptr fptr = new Fptr();
                fptr.setFILEID(packageFile.getMetsFile().getID());
                div.getFptr().add(fptr);                
                contentDivs.add(div);

                // Make bitstream techMD metadata, add to file.
                String techID = createID();
                AmdSec fAmdSec = new AmdSec();
                fAmdSec.setID(techID);
                
                MdSec techMd = new MdSec(createID());                
                MdWrap mdWrap = new MdWrap(MdSec.MDTYPE.PREMIS);
                
                mdWrap.getXmlData().add(makeFilePREMIS(packageFile));
                
                techMd.setMdWrap(mdWrap);
                
                fAmdSec.getTechMD().add(techMd);
                
                getMets().getAmdSec().add(fAmdSec);           

                packageFile.getMetsFile().getADMID().add(techID);                

                
                fileGrp.getFile().add(packageFile.getMetsFile());                
            }

            // Add fileGrp to fileSec
            fileSec.getFileGrp().add(fileGrp);            
        }

        // Add fileSec only if it has contents
        // XXX NOTE: METS schema *allows* fileSec to be left out,
        //  but as of 1.5, DSpace ingester does not..
        if (!fileSec.getFileGrp().isEmpty()) {
            getMets().setFileSec(fileSec);            
        }

        // Create simple structMap: initial div represents the Item,
        // and user-visible content bitstreams are in its child divs.
        
        StructMap structMap = new StructMap();
        structMap.setID(createID());
        structMap.setType("LOGICAL");
        structMap.setLabel("DSpace");
        Div div0 = new Div();
        div0.setID(createID());
        div0.setLabel("DSpace Item");
        div0.getDMDID().addAll(dmdIDs);        

        // if there is a primary bitstream, add FPTR to it.
        if(primaryBitstreamFileID != null){
            Fptr fptr = new Fptr();
            fptr.setFILEID(primaryBitstreamFileID);
            div0.getFptr().add(fptr);            
        }

        // add DIV for each content bitstream
        div0.getDiv().addAll(contentDivs);
        
        structMap.setDiv(div0);
        
        getMets().getStructMap().add(structMap);        
        
        new MetsWriter().writeToOutputStream(getMets(),out);
    }


    /**
     *
     * Construct minimal PREMIS for a bitstream:
     *   object/objectIdentifier = URL, name
     *   object/originalName = name
     *   object/objectCategory = "File"
     *   object/objectCharacteristics/size = len
     *   object/fixity/messageDigestAlgorithm (OPT)
     *   object/fixity/messageDigest (OPT)
     */
    protected Element makeFilePREMIS(PackageFile packageFile) throws UnsupportedEncodingException, ParserConfigurationException{
        Premis premis = new Premis();
        
        PremisObject pobject = new PremisObject(PremisObject.PremisObjectType.bitstream);        
        premis.getObject().add(pobject);
        
        //ID
        PremisObject.PremisObjectIdentifier poid = new PremisObject.PremisObjectIdentifier();
        poid.setObjectIdentifierType("URL");
        poid.setObjectIdentifierValue(URLEncoder.encode(packageFile.getLongName(), "UTF-8"));         
        pobject.getObjectIdentifier().add(poid);
        
        pobject.setOriginalName(packageFile.getLongName());    
        
        //SIZE
        PremisObject.PremisObjectCharacteristics chars = new PremisObject.PremisObjectCharacteristics();        
        chars.setSize(packageFile.getMetsFile().getSIZE());        
        pobject.getObjectCharacteristics().add(chars);        
        
        //CHECKSUMS
        PremisObject.PremisFixity fixity = new PremisObject.PremisFixity();
        fixity.setMessageDigestAlgorithm(packageFile.getMetsFile().getCHECKSUMTYPE().value());
        fixity.setMessageDigest(packageFile.getMetsFile().getCHECKSUM());
        chars.getFixity().add(fixity);
        
        //MIMETYPE
        PremisObject.PremisFormat format = new PremisObject.PremisFormat();
        PremisObject.PremisFormatDesignation formatDesign = new PremisObject.PremisFormatDesignation();
        formatDesign.setFormatName(packageFile.getMetsFile().getMIMETYPE());
                
        chars.getFormat().add(format);
        
        return PremisIO.toDocument(premis).getDocumentElement();        
    }   
   

    // copy from one stream to another
    
    protected static void copyStream(final InputStream input, final OutputStream output)throws IOException{
        final int BUFFER_SIZE = 1024 * 4;
        final byte[] buffer = new byte[BUFFER_SIZE];
        
        int bytesRead = 0;
        
        while((bytesRead = input.read(buffer,0,BUFFER_SIZE)) > 0){
            output.write(buffer,0,bytesRead);
        }        
    }

    /**
     * Write out the package to filesystem at the designated path.
     * @param path the File to which to write this SIP.
     */
    public void write(File path)throws IOException, UnsupportedEncodingException, DatatypeConfigurationException, ParserConfigurationException, TransformerException, Exception{        
        write(new FileOutputStream(path));
    }

    /**
     * Write out the package to a stream.
     * @param out OutputStream to which it is written.
     */
    public void write(OutputStream out) throws IOException,UnsupportedEncodingException,DatatypeConfigurationException,ParserConfigurationException,TransformerException, Exception{
        ZipOutputStream zip = new ZipOutputStream(out);
        zip.setComment("METS archive created by DSpaceSIPMets");

        // NOTE: Never set method to ZipOutputStream.STORED since that mode
        // demands setting size of each entry, we don't know it for manifest.
        // Oddly, this works even when compression is NO_COMPRESSION.
        zip.setLevel(compression);
        zip.setMethod(ZipOutputStream.DEFLATED);

        // write manifest first.
        ZipEntry me = new ZipEntry(METS_FILE);        
        zip.putNextEntry(me);
        finishManifest(zip);
        zip.closeEntry();

        // copy all files, incl. bitstreams, into zip
        for(Map.Entry<String,ArrayList<PackageFile>>e:bundles.entrySet()){
            for(PackageFile packageFile:e.getValue()){
                ZipEntry ze = new ZipEntry(packageFile.getMetsFile().getFLocat().get(0).getXlinkHREF());
                ze.setTime(packageFile.lastModified());
                zip.putNextEntry(ze);            
                copyStream(packageFile.newInputStream(),zip);
                zip.closeEntry();
            }
        }       
        zip.close();
       
    }
}
