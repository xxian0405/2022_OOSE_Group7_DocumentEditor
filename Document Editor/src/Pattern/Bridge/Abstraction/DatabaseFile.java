package Pattern.Bridge.Abstraction;

import Pattern.Bridge.Implementor.FileSave;
import Pattern.Bridge.Implementor.SaveToDatabase;

// Refined Abstraction，儲存到資料庫
public class DatabaseFile extends SavedFile {
    public DatabaseFile(FileSave fileSave) {
        super(fileSave);
    }

}