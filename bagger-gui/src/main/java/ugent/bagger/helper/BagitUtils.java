package ugent.bagger.helper;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFactory.LoadOption;
import gov.loc.repository.bagit.utilities.SimpleResult;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.filechooser.FileFilter;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.params.BagError;
import ugent.bagger.params.BagErrorFileNotAllowedInBagDir;
import ugent.bagger.params.BagErrorFixityFailurePayload;
import ugent.bagger.params.BagErrorFixityFailureTag;
import ugent.bagger.params.BagErrorNoBagDir;
import ugent.bagger.params.BagErrorNoBagitTxt;
import ugent.bagger.params.BagErrorNoPayloadManifests;
import ugent.bagger.params.BagErrorPayloadMissing;
import ugent.bagger.params.BagErrorTagMissing;

/**
 *
 * @author nicolas
 */
public class BagitUtils {
    private static Pattern tagsMissingPattern = Pattern.compile("File (\\S+) in manifest tagmanifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");
    private static Pattern payloadsMissingPattern = Pattern.compile("File (\\S+) in manifest manifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");
    private static Pattern tagsFixityFailurePattern = Pattern.compile("Fixity failure in manifest tagmanifest-(?:md5|sha1|sha256|sha512)\\.txt: (\\S+)");
    private static Pattern payloadsFixityFailurePattern = Pattern.compile("Fixity failure in manifest manifest-(?:md5|sha1|sha256|sha512)\\.txt: (\\S+)");
    private static Pattern fileNotAllowedInBagDirPattern = Pattern.compile("(?:Directory|File) (\\S+) not allowed in bag_dir.");
    private static String noPayloadManifests = "Bag does not have any payload manifests.";
    private static String noBagitTxt = "Bag does not have bagit.txt.";
    private static FileFilter noFilter;
    private static FileFilter zipFilter;
    private static FileFilter tarFilter;
    private static FileFilter tarGzFilter;
    private static FileFilter tarBz2Filter;
    
    //opgelet: deze opmerking zit NIET SimpleResult, maar in een RuntimeException
    private static String noBagDir = "Unable to find bag_dir in serialized bag";

    public static BagError parseBagError(String error){
        Matcher matcherTagsMissing = tagsMissingPattern.matcher(error);
        Matcher matcherPayloadsMissingPattern = payloadsMissingPattern.matcher(error);
        Matcher matcherTagsFixityFailurePattern = tagsFixityFailurePattern.matcher(error);
        Matcher matcherPayloadsFixityFailurePattern = payloadsFixityFailurePattern.matcher(error);
        Matcher matcherFileNotAllowedInBagDirPattern = fileNotAllowedInBagDirPattern.matcher(error);
        
        
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
        }else if(matcherFileNotAllowedInBagDirPattern.matches()){
            bagError = new BagErrorFileNotAllowedInBagDir(
                matcherFileNotAllowedInBagDirPattern.group(1)
                
            );
        }else if(noPayloadManifests.compareTo(error) == 0){
            bagError = new BagErrorNoPayloadManifests();
        }else if(noBagitTxt.compareTo(error) == 0){
            bagError = new BagErrorNoBagitTxt();
        }else if(noBagDir.compareTo(error) == 0){
            bagError = new BagErrorNoBagDir();
        }        
        
        return bagError;
    }

    public static FileFilter getNoFilter() {
        return noFilter;
    }

    public static FileFilter getZipFilter() {
        if(zipFilter == null){
            zipFilter = new FileExtensionFilter(new String [] {"zip"},"zip",true);
        }
        return zipFilter;
    }

    public static FileFilter getTarFilter() {
        if(tarFilter == null){
            tarFilter = new FileExtensionFilter(new String [] {"tar"},"tar",true);
        }
        return tarFilter;
    }

    public static FileFilter getTarGzFilter() {
        if(tarGzFilter == null){
            tarGzFilter = new FileExtensionFilter(new String [] {"tar.gz"},"tar.gz",true);
        }
        return tarGzFilter;
    }

    public static FileFilter getTarBz2Filter() {
        if(tarBz2Filter == null){
            tarBz2Filter = new FileExtensionFilter(new String [] {"tar.bz2"},"tar.bz2",true);
        }
        return tarBz2Filter;
    }
    
    public static void main(String [] args){
        File file = new File("/home/nicolas/client.zip");
        BagFactory factory = new BagFactory();
        Bag bag = factory.createBag(file,LoadOption.BY_PAYLOAD_MANIFESTS);
        SimpleResult result = bag.verifyComplete();
        for(String error:result.getMessages()){
            System.out.println("error: '"+error+"'");
            System.out.println("error translated: "+parseBagError(error));
        }
    }
}
