/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.exceptions;

/**
 *
 * @author nicolas
 */
public class NoTransformationFoundException extends Exception{
    public NoTransformationFoundException() {
        super();
    }
    public NoTransformationFoundException(String message) {
        super(message);
    }
}
