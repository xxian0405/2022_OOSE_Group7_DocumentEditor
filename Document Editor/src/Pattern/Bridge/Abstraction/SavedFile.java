package Pattern.Bridge.Abstraction;

import Pattern.Bridge.Implementor.FileSave;

// Abstraction，存放要儲存的檔案內容
public abstract class SavedFile {
    protected FileSave fileSave;

    public SavedFile(FileSave fileSave){
        this.fileSave = fileSave;
    }

    public void SaveFile(){
        this.fileSave.SaveFile();
    }

}
