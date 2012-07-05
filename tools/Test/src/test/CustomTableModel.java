/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nicolas
 */
public class CustomTableModel extends DefaultTableModel{
    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
