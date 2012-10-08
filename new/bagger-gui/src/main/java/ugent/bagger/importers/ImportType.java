package ugent.bagger.importers;

/**
 *
 * @author nicolas
 */
public enum ImportType {
    CSV("csv"),BAG_INFO("bag-info.txt");
    private String c;
    private ImportType(String c){
        this.c = c;
    }
    @Override
    public String toString(){
        return c;
    }
}
