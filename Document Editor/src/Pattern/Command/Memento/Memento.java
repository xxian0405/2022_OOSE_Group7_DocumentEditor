package Pattern.Command.Memento;

import java.awt.*;

// 就是個Memento
public class Memento {
    private Font fontStyle;
    private String textContent;
    private Color contentColor;

    public Memento(Font style, String content, Color color){
        this.fontStyle = style;
        this.textContent = content;
        this.contentColor = color;
    }

    public Font GetFontStyle(){
        return this.fontStyle;
    }

    public String GetTextContent(){
        return this.textContent;
    }

    public Color GetContentColor(){
        return this.contentColor;
    }
}
