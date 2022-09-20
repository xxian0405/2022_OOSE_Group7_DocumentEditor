package Pattern.Bridge.Abstraction;

import Pattern.Bridge.Implementor.SaveToDatabase;

// Refined Abstraction，儲存到資料庫
public class DatabaseFile extends SavedFile {
    public DatabaseFile(String content){
        super(new SaveToDatabase(), content);
    }

    @Override
    public void SaveFile(){
        fileSave.SaveFile();
    }
}
