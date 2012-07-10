/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import javax.swing.JFrame;

/**
 *
 * @author nicolas
 */
public class ExitJFrame extends JFrame{
    public ExitJFrame(String title){
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
