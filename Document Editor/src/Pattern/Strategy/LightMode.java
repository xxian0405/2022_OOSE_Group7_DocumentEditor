package Pattern.Strategy;

import DocumentEditor.DocumentEditor;

import javax.swing.*;
import java.awt.*;

//實作淺色模式
public class LightMode extends ModeStrategy {
    public void ChangeBackgroundMode(){
        DocumentEditor.getEdit_text_area().setBackground(Color.white);
        DocumentEditor.getEdit_text_area().setForeground(Color.black);
        DocumentEditor.GetMenuBar().setBackground(Color.white);

        for(JMenu menu : DocumentEditor.GetMenuGroup()){
            menu.setForeground(Color.BLACK);
        }

        for(JMenuItem menuItem : DocumentEditor.GetMenuItemsGroup()){
            menuItem.setBackground(Color.WHITE);
            menuItem.setForeground(Color.BLACK);
        }
    }
}
