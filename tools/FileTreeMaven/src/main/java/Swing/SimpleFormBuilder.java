/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Swing;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import org.springframework.richclient.util.SpringLayoutUtils;

/**
 *
 * @author nicolas
 */
public class SimpleFormBuilder {
    private ArrayList<InputPair> inputPairs = new ArrayList<InputPair>();
    private int x = 0;
    private int y = 0;
    private int xPad = 5;
    private int yPad = 5;
    private Dimension leftDimension;
    private Dimension rightDimension;
    private SpringLayout layout = new SpringLayout();

    public SimpleFormBuilder(){
        
    }
    public SimpleFormBuilder(int x,int y,int xPad,int yPad){        
        setX(x);
        setY(y);
        setXPad(xPad);
        setY(yPad);
    }  
    public SimpleFormBuilder(int x,int y,int xPad,int yPad,Dimension leftDimension,Dimension rightDimension){
        setX(x);
        setY(y);
        setXPad(xPad);
        setY(yPad);        
    }
    
    public void add(Component a,Component b){
        inputPairs.add(new InputPair(a,b));
    }
    private void setDimension(Component component,Dimension dimension){
        component.setMaximumSize(dimension);
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
    }
    public JPanel createForm(){
        JPanel panel = new JPanel(layout);                 
        for(InputPair inputPair:inputPairs){
            Component left = inputPair.getLeftComponent();
            Component right = inputPair.getRightComponent();
            if(leftDimension != null){
               setDimension(left, leftDimension);
            }
            if(rightDimension != null){
               setDimension(right,rightDimension);
            }
            panel.add(left);
            panel.add(right);
        }        
        SpringLayoutUtils.makeCompactGrid(panel,inputPairs.size(),2,getX(),getY(),getXPad(),getYPad());        
        return panel;
    }    
    public Dimension getLeftDimension() {
        return leftDimension;
    }

    public void setLeftDimension(Dimension leftDimension) {
        this.leftDimension = leftDimension;
    }

    public Dimension getRightDimension() {
        return rightDimension;
    }

    public void setRightDimension(Dimension rightDimension) {
        this.rightDimension = rightDimension;
    }
    
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getXPad() {
        return xPad;
    }
    public void setXPad(int xPad) {
        this.xPad = xPad;
    }
    public int getYPad() {
        return yPad;
    }
    public void setYPad(int yPad) {
        this.yPad = yPad;
    }
    private class InputPair {
        private Component leftComponent;
        private Component rightComponent;
        
        public InputPair(Component leftComponent,Component rightComponent){
            setLeftComponent(leftComponent);
            setRightComponent(rightComponent);
        }

        public Component getLeftComponent() {
            return leftComponent;
        }
        public void setLeftComponent(Component leftComponent) {
            this.leftComponent = leftComponent;
        }
        public Component getRightComponent() {
            return rightComponent;
        }
        public void setRightComponent(Component rightComponent) {
            this.rightComponent = rightComponent;
        }
    }
}
