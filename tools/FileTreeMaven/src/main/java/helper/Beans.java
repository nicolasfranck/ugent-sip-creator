/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import org.springframework.richclient.application.Application;

/**
 *
 * @author nicolas
 */
public class Beans {
    public static Object getBean(String id){
        return Application.instance().getApplicationContext().getBean(id);
    }
}
