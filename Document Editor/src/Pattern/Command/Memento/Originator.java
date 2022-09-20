package Pattern.Command.Memento;

import Controller.DocumentEditorController;

import java.awt.*;

//用於保存state還有建立以及取回Memento
public class Originator {
    private Font fontStyle;
    private String textContent;
    private Color contentColor;

    public void SetState(Font style, String content, Color color){
        this.fontStyle = style;
        this.textContent = content;
        this.contentColor = color;
    }

    public Memento CreateMemento(){
        return new Memento(fontStyle, textContent, contentColor);
    }

    public Memento RestoreFromMemento(Memento memento){
        return memento;
    }
}
