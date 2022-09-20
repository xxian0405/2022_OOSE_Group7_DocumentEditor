package Pattern.Bridge.Abstraction;

import Pattern.Bridge.Implementor.FileSave;

// Abstraction，存放要儲存的檔案內容
public abstract class SavedFile {
    protected FileSave fileSave;
    protected String content;

    public SavedFile(FileSave fileSave, String content){
        this.fileSave = fileSave;
        this.content = content;
    }

    public abstract void SaveFile();
}
