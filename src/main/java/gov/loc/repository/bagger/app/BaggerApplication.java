/*
 * Copyright 2002-2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gov.loc.repository.bagger.app;

import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyAccessException;
import org.springframework.richclient.application.ApplicationLauncher;


/**
 * Main driver that starts the Bagger spring rich client application.
 */
public class BaggerApplication {

    static final Log log = LogFactory.getLog(BaggerApplication.class);

    public static void main(String[] args) {
        String rootContextDirectoryClassPath = "/gov/loc/repository/bagger/ctx";      
        String startupContextPath = rootContextDirectoryClassPath+"/common/richclient-startup-context.xml";
        String richclientApplicationContextPath = rootContextDirectoryClassPath+"/common/richclient-application-context.xml";     
       
        try {
            new ApplicationLauncher(
                startupContextPath, 
                new String[] { richclientApplicationContextPath }
            );
        }catch (IllegalStateException e) {
            
            log.error("IllegalStateException during startup",e);                     
            JOptionPane.showMessageDialog(null,"An illegal state error occured.\n", "Bagger startup error!",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            
        }catch (PropertyAccessException e) {      
            
            log.error("PropertyAccessException during startup",e);
            JOptionPane.showMessageDialog(null, "An error occured loading properties.\n", "Bagger startup error!",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            
        }catch (RuntimeException e) { 
            
            log.error("RuntimeException during startup", e);            
            if (e.getMessage().contains("SAXParseException")){
                JOptionPane.showMessageDialog(null, "An error occured parsing application context.  You may have no internet access.\n" , "Bagger startup error!",JOptionPane.ERROR_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "An error occured during startup.\n" , "Bagger startup error!",JOptionPane.ERROR_MESSAGE);
            }
            System.exit(1);
            
        }
    }
}