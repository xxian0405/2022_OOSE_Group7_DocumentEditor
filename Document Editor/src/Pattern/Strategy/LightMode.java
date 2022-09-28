package Pattern.Strategy;

import DocumentEditor.DocumentEditor;

import javax.swing.*;
import java.awt.*;

//實作淺色模式
public class LightMode extends ModeStrategy {
    //把主畫面修改為淺色模式
    public void ChangeBackgroundMode(){
        DocumentEditor.getEdit_text_area().setBackground(Color.white);
        DocumentEditor.getEdit_text_area().setForeground(Color.black);
        DocumentEditor.GetMenuBar().setBackground(Color.white);
        DocumentEditor.GetScrollBar().getVerticalScrollBar().setBackground(Color.WHITE);
        DocumentEditor.GetScrollBar().getHorizontalScrollBar().setBackground(Color.WHITE);
        DocumentEditor.getCount_words().setBackground(Color.white);
        DocumentEditor.getCount_words().setForeground(Color.black);

        for(JMenu menu : DocumentEditor.GetMenuGroup()){
            menu.setForeground(Color.BLACK);
        }

        for(JMenuItem menuItem : DocumentEditor.GetMenuItemsGroup()){
            menuItem.setBackground(Color.WHITE);
            menuItem.setForeground(Color.BLACK);
        }
    }
}
