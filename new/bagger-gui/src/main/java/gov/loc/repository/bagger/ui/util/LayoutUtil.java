package gov.loc.repository.bagger.ui.util;

import java.awt.GridBagConstraints;

public class LayoutUtil {
    public static GridBagConstraints buildGridBagConstraints(
        int x, int y, int w, int h,int wx, int wy, int fill, int anchor
    ){
        GridBagConstraints gbc = new GridBagConstraints();
        
        //start cell in a row        
        gbc.gridx = x; 
        //start cell in a column
        gbc.gridy = y; 
        //how many column does the control occupy in the row
        gbc.gridwidth = w; 
        //how many column does the control occupy in the column        
        gbc.gridheight = h;                                                 
        //relative horizontal size
        gbc.weightx = wx; 
        //relative vertical size
        gbc.weighty = wy; 
        //the way how the control fills cells
        gbc.fill = fill; 
        //alignment
        gbc.anchor = anchor; 
        return gbc;
    }
}