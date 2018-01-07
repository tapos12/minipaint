/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import GUI.MainPanel;
import GUI.PanelMenu;

/**
 *
 * @author Taposh
 */
public class paint {
    public static void main(String[] args)
    {
        MainPanel mp = new MainPanel();
       PanelMenu menu = new PanelMenu("Untitled - Paint",mp);
        
        
        mp.setVisible(true);
        menu.setContentPane(mp);
        menu.setVisible(true);
    }
}
