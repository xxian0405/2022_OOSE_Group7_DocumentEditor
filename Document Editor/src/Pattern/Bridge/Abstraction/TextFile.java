package Pattern.Bridge.Abstraction;

import Pattern.Bridge.Implementor.SaveAsTextFile;

// Refined Abstraction，儲存成文字檔案(.txt)
public class TextFile extends SavedFile {
    public TextFile(String content){
        super(new SaveAsTextFile(), content);
    }

    @Override
    public void SaveFile(){
        fileSave.SaveFile();
    }
}
