package ugent.bagger.params;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum BagMode {
    NO_MODE("NO_MODE",0),
    ZIP_MODE("ZIP_MODE",1),
    TAR_MODE("TAR_MODE",2),
    TAR_GZ_MODE("TAR_GZ_MODE",3),
    TAR_BZ2_MODE("TAR_BZ2_MODE",4);
    String label;
    int bagitMode;
    private BagMode(String label,int bagitMode){
        this.label = label;
        this.bagitMode = bagitMode;
    }
    public String getLabel() {
        return label;
    }
    public int getBagitMode() {
        return bagitMode;
    }
    @Override
    public String toString(){
        String t = null;
        try{
            t = Context.getMessage("BagMode."+label);
        }catch(Exception e){}
        return t != null ? t : label;
    }
}
