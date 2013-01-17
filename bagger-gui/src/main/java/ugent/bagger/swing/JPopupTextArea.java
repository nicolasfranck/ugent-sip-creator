package ugent.bagger.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;
import ugent.bagger.helper.Context;

/**
 *
 * source: http://www.objectdefinitions.com/odblog/2007/jtextarea-with-popup-menu/
 * May 17th, 2007 Nick Posted in java, swing | 4 Comments »
 */
public class JPopupTextArea extends JTextArea{
    private HashMap actions;
    private boolean editable = false;

    public JPopupTextArea(){
        this(false);
    }
    public JPopupTextArea(boolean editable){
        this.editable = editable;
        addPopupMenu();        
    }

    private void addPopupMenu(){
        createActionTable();

        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = null;
        menuItem = menu.add(getActionByName(DefaultEditorKit.copyAction, "Copy"));
        menuItem.setText(Context.getMessage("copyCommand.label"));
        if(editable){
            menuItem = menu.add(getActionByName(DefaultEditorKit.cutAction, "Cut"));
            menuItem.setText(Context.getMessage("cutCommand.label"));
            menuItem = menu.add(getActionByName(DefaultEditorKit.pasteAction, "Paste"));
            menuItem.setText(Context.getMessage("pasteCommand.label"));
        }        
        menu.add(new JSeparator());
        menuItem = menu.add(getActionByName(DefaultEditorKit.selectAllAction, "Select All"));
        menuItem.setText(Context.getMessage("selectAllCommand.label"));
        add(menu);

        addMouseListener(
           new PopupTriggerMouseListener(
                   menu,
                   this
           )
        );
        
        //no need to hold the references in the map,
        // we have used the ones we need.
        actions.clear();
    }

    private Action getActionByName(String name, String description) {
        Action a = (Action)(actions.get(name));
        a.putValue(Action.NAME, description);
        return a;
    }


    private void createActionTable(){
        actions = new HashMap();
        Action[] actionsArray = getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
    }

    public static class PopupTriggerMouseListener extends MouseAdapter{
        private JPopupMenu popup;
        private JComponent component;

        public PopupTriggerMouseListener(JPopupMenu popup, JComponent component){
            this.popup = popup;
            this.component = component;
        }

        //some systems trigger popup on mouse press, others on mouse release, we want to cater for both
        private void showMenuIfPopupTrigger(MouseEvent e){
            if (e.isPopupTrigger())
            {
               popup.show(component, e.getX() + 3, e.getY() + 3);
            }
        }

        //according to the javadocs on isPopupTrigger, checking for popup trigger on mousePressed and mouseReleased 
        //should be all  that is required
        //public void mouseClicked(MouseEvent e)  
        //{
        //    showMenuIfPopupTrigger(e);
        //}

        @Override
        public void mousePressed(MouseEvent e){
            showMenuIfPopupTrigger(e);
        }

        @Override
        public void mouseReleased(MouseEvent e){
            showMenuIfPopupTrigger(e);
        }
    }
}