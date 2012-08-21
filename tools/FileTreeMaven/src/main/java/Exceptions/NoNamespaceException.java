/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author nicolas
 */
public class NoNamespaceException extends Exception {
   
    public NoNamespaceException() {
        super();
    }
    public NoNamespaceException(String message) {
        super(message);
    }   
   
}
