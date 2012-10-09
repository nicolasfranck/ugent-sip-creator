package gov.loc.repository.bagit.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.ManifestHelper;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.ManifestReader;
import gov.loc.repository.bagit.ManifestWriter;
import gov.loc.repository.bagit.Bag.BagConstants;
import gov.loc.repository.bagit.Bag.BagPartFactory;
import gov.loc.repository.bagit.ManifestReader.FilenameFixity;
import gov.loc.repository.bagit.utilities.MessageDigestHelper;

public class ManifestImpl extends LinkedHashMap<String, String> implements Manifest {
	
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(ManifestImpl.class);	
    private String name;
    private BagFile sourceBagFile = null;
    private String originalFixity = null;
    private BagConstants bagConstants;
    private BagPartFactory bagPartFactory;
    private String nonDefaultManifestSeparator = null;

    public ManifestImpl(String name, BagConstants bagConstants, BagPartFactory bagPartFactory) {
        init(name, bagConstants, bagPartFactory);
    }

    public ManifestImpl(String name, BagConstants bagConstants, BagPartFactory bagPartFactory, BagFile sourceBagFile) {
        this.init(name, bagConstants, bagPartFactory);
        this.sourceBagFile = sourceBagFile;
        ManifestReader reader = bagPartFactory.createManifestReader(sourceBagFile.newInputStream(), bagConstants.getBagEncoding());
        while(reader.hasNext()) {
            FilenameFixity filenameFixity = reader.next();
            this.put(filenameFixity.getFilename(), filenameFixity.getFixityValue());
        }
        reader.close();
        //Generate original fixity
        this.originalFixity = MessageDigestHelper.generateFixity(this.generatedInputStream(), Algorithm.MD5);
    }

    private void init(String name, BagConstants bagConstants, BagPartFactory bagPartFactory) {
        log.debug("Creating manifest for " + name);
        this.name = name;
        this.bagConstants = bagConstants;
        this.bagPartFactory = bagPartFactory;
        if(! (ManifestHelper.isPayloadManifest(name, bagConstants) || ManifestHelper.isTagManifest(name, bagConstants))) {
            throw new RuntimeException("Invalid name");
        }
    }

    @Override
    public InputStream newInputStream() {
        //If this hasn't changed, then return sourceBagFile's inputstream
        //Otherwise, generate a new inputstream
        //This is to account for junk in the file, e.g., LF/CRs that might effect the fixity of this manifest
        if (MessageDigestHelper.fixityMatches(generatedInputStream(), Algorithm.MD5,originalFixity)) {
            return sourceBagFile.newInputStream();
        }
        return generatedInputStream();
    }

    @Override
    public InputStream originalInputStream() {
        return sourceBagFile.newInputStream();
    }

    private InputStream generatedInputStream() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ManifestWriter writer = bagPartFactory.createManifestWriter(out,nonDefaultManifestSeparator);
        for(String filename:keySet()) {
            writer.write(filename,get(filename));
        }
        writer.close();
        return new ByteArrayInputStream(out.toByteArray());					
    }

    @Override
    public String getFilepath() {
        return name;
    }

    @Override
    public Algorithm getAlgorithm() {
        return ManifestHelper.getAlgorithm(name,bagConstants);
    }

    @Override
    public boolean isPayloadManifest() {
        return ManifestHelper.isPayloadManifest(name,bagConstants);
    }

    @Override
    public boolean isTagManifest() {
        return ManifestHelper.isTagManifest(name,bagConstants);
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public long getSize() {
        InputStream in = this.newInputStream();
        long size=0L;
        try {
            while(in.read() != -1) {
                size++;
            }
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        return size;
    }

    @Override
    public String getNonDefaultManifestSeparator() {
        return nonDefaultManifestSeparator;
    }

    @Override
    public void setNonDefaultManifestSeparator(String manifestSeparator) {
        nonDefaultManifestSeparator = manifestSeparator;
    }

    @Override
    public void setFilePath(String name) {        
    }	
}