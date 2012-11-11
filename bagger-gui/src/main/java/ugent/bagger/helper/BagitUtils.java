package ugent.bagger.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ugent.bagger.params.BagError;
import ugent.bagger.params.BagErrorFixityFailurePayload;
import ugent.bagger.params.BagErrorFixityFailureTag;
import ugent.bagger.params.BagErrorPayloadMissing;
import ugent.bagger.params.BagErrorTagMissing;

/**
 *
 * @author nicolas
 */
public class BagitUtils {
    private static Pattern tagsMissingPattern = Pattern.compile("File (\\S+) in manifest tagmanifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");
    private static Pattern payloadsMissingPattern = Pattern.compile("File (\\S+) in manifest manifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");
    private Pattern tagsFixityFailurePattern = Pattern.compile("Fixity failure in manifest tagmanifest-(?:md5|sha1|sha256|sha512)\\.txt: (\\S+)");
    private Pattern payloadsFixityFailurePattern = Pattern.compile("Fixity failure in manifest manifest-(?:md5|sha1|sha256|sha512)\\.txt: (\\S+)");

    public static BagError parseBagError(String error){
        Matcher matcherTagsMissing = tagsMissingPattern.matcher(error);
        Matcher matcherPayloadsMissingPattern = tagsMissingPattern.matcher(error);
        Matcher matcherTagsFixityFailurePattern = tagsMissingPattern.matcher(error);
        Matcher matcherPayloadsFixityFailurePattern = tagsMissingPattern.matcher(error);
        
        BagError bagError = null;
        
        if(matcherTagsMissing.matches()){
            bagError = new BagErrorTagMissing(matcherTagsMissing.group(1));
        }else if(matcherPayloadsMissingPattern.matches()){
            bagError = new BagErrorPayloadMissing(matcherPayloadsMissingPattern.group(1));
        }else if(matcherTagsFixityFailurePattern.matches()){
            bagError = new BagErrorFixityFailureTag(
                matcherTagsFixityFailurePattern.group(1),
                matcherTagsFixityFailurePattern.group(2)
            );
        }else if(matcherPayloadsFixityFailurePattern.matches()){
            bagError = new BagErrorFixityFailurePayload(
                matcherPayloadsFixityFailurePattern.group(1),
                matcherPayloadsFixityFailurePattern.group(2)
            );
        }        
        return bagError;
    }
}
