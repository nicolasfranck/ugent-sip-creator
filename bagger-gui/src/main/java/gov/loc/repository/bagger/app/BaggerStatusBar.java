package gov.loc.repository.bagger.app;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import org.springframework.richclient.application.statusbar.support.DefaultStatusBar;
import org.springframework.richclient.application.statusbar.support.StatusBarProgressMonitor;

/**
 *
 * @author nicolas
 */
public class BaggerStatusBar extends DefaultStatusBar{
    JProgressBar progressBar;
    private Dimension progressDimension = new Dimension(300,20);

    @Override
    protected StatusBarProgressMonitor createStatusBarProgressMonitor() {
        return new StatusBarProgressMonitor(){
            @Override
            protected JProgressBar createProgressBar() {                
                progressBar = super.createProgressBar();               
                progressBar.setPreferredSize(progressDimension);               
                return progressBar;
            }
        };
    }
    @Override
    protected JComponent createControl() {
        JComponent comp = super.createControl();                
        ((StatusBarProgressMonitor)getProgressMonitor()).getControl().setPreferredSize(progressDimension);
        return comp;
    }
}