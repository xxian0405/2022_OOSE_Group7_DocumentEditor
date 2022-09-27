package Pattern.Strategy;

import DocumentEditor.DocumentEditor;

import javax.swing.*;
import java.awt.*;

//實作深色策略
public class DarkMode extends ModeStrategy {
    //把主畫面修改為深色模式
    public void ChangeBackgroundMode(){
        DocumentEditor.getEdit_text_area().setBackground(Color.DARK_GRAY);
        DocumentEditor.getEdit_text_area().setForeground(Color.white);
        DocumentEditor.GetMenuBar().setBackground(Color.DARK_GRAY);
        DocumentEditor.GetScrollBar().getVerticalScrollBar().setBackground(Color.DARK_GRAY);
        DocumentEditor.GetScrollBar().getHorizontalScrollBar().setBackground(Color.DARK_GRAY);

        for(JMenu menu : DocumentEditor.GetMenuGroup()){
            menu.setForeground(Color.WHITE);
        }

        for(JMenuItem menuItem : DocumentEditor.GetMenuItemsGroup()){
            menuItem.setBackground(Color.DARK_GRAY);
            menuItem.setForeground(Color.WHITE);
        }
    }
}
