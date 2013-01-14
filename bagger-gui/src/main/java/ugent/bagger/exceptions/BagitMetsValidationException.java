package ugent.bagger.exceptions;

import ugent.bagger.bagitmets.validation.RESULT;

/**
 *
 * @author nicolas
 */
public class BagitMetsValidationException extends Exception {
    RESULT result;    
    public BagitMetsValidationException() {
        super();
    }
    public BagitMetsValidationException(RESULT result,String message) {
        super(message);
        this.result = result;
    } 
    public RESULT getResult() {
        return result;
    }
}