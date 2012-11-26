package ugent.bagger.params;

import ugent.bagger.helper.MetsUtils;

/**
 *
 * @author nicolas
 */
public class NewBagParams {
    private String version;
    private String bagId;

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }    
    public String getBagId() {
        if(bagId == null){
            bagId = MetsUtils.createID();
        }
        return bagId;
    }
    public void setBagId(String bagId) {
        this.bagId = bagId;
    }
    
}
