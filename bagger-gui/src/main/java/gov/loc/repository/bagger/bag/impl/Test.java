/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.loc.repository.bagger.bag.impl;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.PreBag;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.springframework.richclient.progress.ProgressBarProgressMonitor;
import org.springframework.richclient.progress.ProgressMonitor;

/**
 *
 * @author nicolas
 */
public class Test {
    public static void main(String [] args){
        /*
        BagFactory bf = new BagFactory();
        PreBag preBag = bf.createPreBag(new File("/home/nicolas/test/BHSL-HS-III-0024-000204"));
        System.out.println("prebag file: "+preBag.getFile());
        //arg0: version, arg1: keep source directory
        Bag bag = preBag.makeBagInPlace(BagFactory.LATEST,false);*/
        
        
        TestDialog dialog = new TestDialog(null,true);
        dialog.pack();
        dialog.setVisible(true);
        ProgressMonitor monitor = new ProgressBarProgressMonitor(dialog.getProgressBar());        
        monitor.taskStarted("work a",100);
        monitor.worked(50);
        
    }
    public static final class TestDialog extends JDialog {
        JProgressBar progressBar;
        JLabel label;
        public TestDialog(JFrame frame,boolean isModal){
            super(frame,isModal);
            setLayout(new BoxLayout(getContentPane(),BoxLayout.PAGE_AXIS));
            getContentPane().add(getLabel());
            getContentPane().add(getProgressBar());
        }

        public JProgressBar getProgressBar() {
            if(progressBar == null){                
                progressBar = new JProgressBar(0,100);                
                progressBar.setValue(20);
                //progressBar.setIndeterminate(true);                
            }
            return progressBar;
        }

        public void setProgressBar(JProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        public JLabel getLabel() {
            if(label == null){
                label = new JLabel();
            }
            return label;
        }

        public void setLabel(JLabel label) {
            this.label = label;
        }
        
    }
}
