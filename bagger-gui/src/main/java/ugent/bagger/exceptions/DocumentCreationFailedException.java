/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.exceptions;

/**
 *
 * @author nicolas
 */
public class DocumentCreationFailedException extends Exception{
    public DocumentCreationFailedException() {
        super();
    }
    public DocumentCreationFailedException(String message) {
        super(message);
    }
}
