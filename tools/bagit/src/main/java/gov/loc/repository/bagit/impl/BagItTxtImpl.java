package gov.loc.repository.bagit.impl;

import gov.loc.repository.bagit.Bag.BagConstants;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.BagItTxt;
import gov.loc.repository.bagit.utilities.namevalue.impl.AbstractNameValueBagFile;

public class BagItTxtImpl extends AbstractNameValueBagFile implements BagItTxt {

    public static final String VERSION_KEY = "BagIt-Version";
    public static final String CHARACTER_ENCODING_KEY = "Tag-File-Character-Encoding";	
    private static final long serialVersionUID = 1L;

    public BagItTxtImpl(BagFile bagFile, BagConstants bagConstants) {
        super(bagConstants.getBagItTxt(), bagFile, bagConstants.getBagEncoding());
    }

    public BagItTxtImpl(BagConstants bagConstants) {
        super(bagConstants.getBagItTxt(), bagConstants.getBagEncoding());
        put(VERSION_KEY, bagConstants.getVersion().versionString);
        put(CHARACTER_ENCODING_KEY, bagConstants.getBagEncoding());
    }
	
    @Override
    public String getCharacterEncoding() {
        return get(CHARACTER_ENCODING_KEY);
    }
    @Override
    public String getVersion() {
        return get(VERSION_KEY);
    }
    @Override
    public String getType() {
            return TYPE;
    }	

    @Override
    public void setFilePath(String name) {        
    }
}