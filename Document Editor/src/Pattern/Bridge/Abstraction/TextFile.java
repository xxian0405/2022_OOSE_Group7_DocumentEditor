package Pattern.Bridge.Abstraction;

import Pattern.Bridge.Implementor.FileSave;
import Pattern.Bridge.Implementor.SaveAsTextFile;

// Refined Abstraction，儲存成文字檔案(.txt)
public class TextFile extends SavedFile {
    public TextFile( FileSave fileSave ){
        super(fileSave);
    }
}
