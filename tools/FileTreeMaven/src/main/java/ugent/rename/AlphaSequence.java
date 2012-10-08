package ugent.rename;

/**
 *
 * @author nicolas
 */
public class AlphaSequence implements Sequence {
    /*
     * source: http://stackoverflow.com/questions/8710719/generating-an-alphabetic-sequence-in-java
     */
    private int now;
    private static char[] vs;
    private int padding = 0;
    private int step = 1;
    private boolean isSet = false;
    static {
        vs = new char['Z' - 'A' + 1];
        for(char i='A'; i<='Z';i++) {
            vs[i - 'A'] = i;
        }
    }   
    
    private StringBuilder alpha(int i){
        assert i > 0;        
        char r = vs[i % vs.length];
        int n = i / vs.length;
        return n == 0 ? new StringBuilder().append(r) : alpha(n).append(r);
    }    
     
    @Override
    public String next() {
        if(isSet){
            now += step;
        }else{
            isSet = true;
        }
        return alpha(now).toString();
    }
    @Override
    public String previous(){  
        if(isSet){
            now -= step;
        }else{
            isSet = true;
        }        
        return alpha(now).toString();
    }
    public static void main(String [] args){
        Sequence seq = new AlphaSequence();  
        
        seq.setStep(10);
        for(int i = 0;i< 20;i++){
            System.out.println(seq.next());            
        }       
    }
    @Override
    public void setPadding(int padding) {
        this.padding = padding;
    }
    @Override
    public int getPadding() {
        return padding;
    }

    @Override
    public int getCounter() {
        return now;
    }

    @Override
    public void setCounter(int i){        
        this.now = Math.abs(i);
    }   

    @Override
    public int getStep() {
        return step;
    }
    @Override
    public void setStep(int step) {
        this.step = Math.abs(step);
    }    
}