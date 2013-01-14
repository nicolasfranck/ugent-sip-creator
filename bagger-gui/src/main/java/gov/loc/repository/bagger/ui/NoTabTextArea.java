package gov.loc.repository.bagger.ui;

import java.awt.event.KeyEvent;
import javax.swing.JTextArea;

public class NoTabTextArea extends JTextArea {    

    public NoTabTextArea(int row, int cols) {
        super(row, cols);
    }
    @Override
    protected void processComponentKeyEvent( KeyEvent e ) {
    	if(e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_TAB ){
            e.consume();
            if(e.isShiftDown()){
                transferFocusBackward();
            }else{
                transferFocus();
            }
    	} else {
            super.processComponentKeyEvent( e );
    	}
    }
}