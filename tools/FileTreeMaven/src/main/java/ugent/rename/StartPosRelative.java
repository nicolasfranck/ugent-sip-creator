/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum StartPosRelative {
    BEGIN("begin"),END("end"),BEFORE_EXTENSION("voor extensie");
    private String c;
    private StartPosRelative(String c){
        this.c = c;
    }
    @Override
    public String toString(){
        String s = Context.getMessage(c);
        if(s == null){
            s = c;
        }
        return s;
    }
}