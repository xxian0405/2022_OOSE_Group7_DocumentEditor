package Pattern.Bridge.Implementor;

import DocumentEditor.DocumentEditor;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

// Concrete Implementor，實作檔案儲存動作
public class SaveAsTextFile implements FileSave {

    // 儲存成文字檔案(.txt)
    @Override
    public void SaveFile(){
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (file == null) {
                return;
            }

            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            }

            try {
                fileChooser.setCurrentDirectory(file);
                DocumentEditor.getEdit_text_area().write(new OutputStreamWriter(new FileOutputStream(file),
                        "utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
