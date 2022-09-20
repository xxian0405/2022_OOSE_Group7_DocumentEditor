package Pattern.Strategy;

import DocumentEditor.DocumentEditor;

import java.awt.*;

//實作淺色模式
public class LightMode extends ModeStrategy {
    public void ChangeBackgroundMode(){
        DocumentEditor.getEdit_text_area().setBackground(Color.white);
        DocumentEditor.getEdit_text_area().setForeground(Color.black);
    }
}
