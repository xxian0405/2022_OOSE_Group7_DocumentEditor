package Pattern.Command;

import DocumentEditor.DocumentEditor;
import Pattern.Command.Memento.Caretaker;
import Pattern.Command.Memento.Memento;
import Pattern.Command.Memento.Originator;

// 命令的接收者，知道如何執行命令
public class Receiver {

    // 此receiver具有紀錄state的caretaker以及取得Memento的originator
    private Originator originator;
    private Caretaker caretaker;

    public Receiver(Originator originator, Caretaker caretaker){
        this.originator = originator;
        this.caretaker = caretaker;
    }

    // originator透過caretaker根據current來執行undo取得所需的mementor state，並將當中保存的資料設置至文字輸入區域中
    public void Undo(int current){
        Memento memento = originator.RestoreFromMemento(caretaker.GetMemento(current - 1));
        DocumentEditor.getEdit_text_area().setFont(memento.GetFontStyle());
        DocumentEditor.getEdit_text_area().setText(memento.GetTextContent());
        DocumentEditor.getEdit_text_area().setForeground(memento.GetContentColor());
    }

    // originator透過caretaker根據current來執行redo取得所需的mementor state，並將當中保存的資料設置至文字輸入區域中
    public void Redo(int current){
        Memento memento = originator.RestoreFromMemento(caretaker.GetMemento(current + 1));
        DocumentEditor.getEdit_text_area().setFont(memento.GetFontStyle());
        DocumentEditor.getEdit_text_area().setText(memento.GetTextContent());
        DocumentEditor.getEdit_text_area().setForeground(memento.GetContentColor());
    }
}
