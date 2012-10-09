package gov.loc.repository.bagit.impl;

import gov.loc.repository.bagit.Bag.BagConstants;
import gov.loc.repository.bagit.Bag.BagPartFactory;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.FetchTxt;
import gov.loc.repository.bagit.FetchTxt.FilenameSizeUrl;
import gov.loc.repository.bagit.FetchTxtReader;
import gov.loc.repository.bagit.FetchTxtWriter;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.utilities.MessageDigestHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FetchTxtImpl extends ArrayList<FilenameSizeUrl> implements FetchTxt {
	
    private static final Log log = LogFactory.getLog(FetchTxtImpl.class);
    private static final long serialVersionUID = 1L;
    private String name;
    private BagConstants bagConstants;
    private BagPartFactory bagPartFactory;
    private BagFile sourceBagFile = null;
    private String originalFixity = null;

    public FetchTxtImpl(BagConstants bagConstants, BagPartFactory bagPartFactory) {
        log.info("Creating new fetch.txt.");
        this.init(bagConstants, bagPartFactory);
    }

    public FetchTxtImpl(BagConstants bagConstants, BagPartFactory bagPartFactory, BagFile sourceBagFile) {
        log.info("Creating fetch.txt.");
        this.init(bagConstants, bagPartFactory);
        this.sourceBagFile = sourceBagFile;
        FetchTxtReader reader = bagPartFactory.createFetchTxtReader(sourceBagFile.newInputStream(), this.bagConstants.getBagEncoding());

        while(reader.hasNext()) {
                this.add(reader.next());
        }
        reader.close();
        //Generate original fixity
        this.originalFixity = MessageDigestHelper.generateFixity(this.generatedInputStream(), Manifest.Algorithm.MD5);
    }

    private void init(BagConstants bagConstants, BagPartFactory bagPartFactory) {
        this.name = bagConstants.getFetchTxt();
        this.bagConstants = bagConstants;
        this.bagPartFactory = bagPartFactory;
    }

    @Override
    public InputStream newInputStream() {
        //If this hasn't changed, then return sourceBagFile's inputstream
        //Otherwise, generate a new inputstream
        //This is to account for junk in the file, e.g., LF/CRs that might effect the fixity of this manifest
        if(MessageDigestHelper.fixityMatches(generatedInputStream(),Manifest.Algorithm.MD5,originalFixity)) {
            return sourceBagFile.newInputStream();
        }
        return this.generatedInputStream();
    }
    private InputStream generatedInputStream() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FetchTxtWriter writer = this.bagPartFactory.createFetchTxtWriter(out);
        for(FilenameSizeUrl filenameSizeUrl : this) {
            writer.write(filenameSizeUrl.getFilename(), filenameSizeUrl.getSize(), filenameSizeUrl.getUrl());
        }
        writer.close();
        return new ByteArrayInputStream(out.toByteArray());					
    }


    @Override
    public String getFilepath() {
        return name;
    }
    @Override
    public long getSize() {
        InputStream in = this.newInputStream();
        long size=0L;
        try {
            while(in.read() != -1){
                size++;
            }
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        return size;
    }
    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public void setFilePath(String name) {        
    }
}