package ugent.bagger.params;

import java.util.UUID;

/**
 *
 * @author nicolas
 */
public class NewBagParams {
    String version;
    String bagId;

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }    
    public String getBagId() {
        if(bagId == null){
            bagId = "UUID-"+UUID.randomUUID().toString();
        }
        return bagId;
    }
    public void setBagId(String bagId) {
        this.bagId = bagId;
    }
    
}
