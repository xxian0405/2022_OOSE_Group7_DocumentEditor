package Pattern.Strategy;

import DocumentEditor.DocumentEditor;

import java.awt.*;

//實作深色策略
public class DarkMode extends ModeStrategy {
    public void ChangeBackgroundMode(){
        DocumentEditor.getEdit_text_area().setBackground(Color.DARK_GRAY);
        DocumentEditor.getEdit_text_area().setForeground(Color.white);
    }
}
