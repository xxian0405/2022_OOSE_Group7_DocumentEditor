package Controller;

import DocumentEditor.DocumentEditor;
import Model.FileSaveService;
import Pattern.Iterator.Files;
import Pattern.Strategy.*;

import java.awt.*;

public class DocumentEditorController {
    private static DocumentEditorController documentEditorController = null;
    private FileSaveService fileSaveService = new FileSaveService();

    /*
    * Lazy Singleton Pattern
    * If controller is null, create a controller and return it.
    * If controller is exist, then does nothing.
    */
    public static DocumentEditorController GetController() {
        if (documentEditorController == null) {
            documentEditorController = new DocumentEditorController();
        }

        return documentEditorController;
    }

    /*
    * Strategy And Factory Method Pattern
    * According to Context(currentBackgroundColor), to choose which Color Mode we want the Factory to create.
    */
    public void ChangeColorMode(){
        ModeStrategy strategy = null;
        Color currentBackgroundColor = DocumentEditor.getEdit_text_area().getBackground();
        ModeFactory factory;

        switch (currentBackgroundColor.getRGB()) {
            // After we get background, we can use method getRGB() to get an integer represent background
            case -1:
                // -1 represent white background
                factory = new DarkModeFactory();
                strategy = factory.CreateBackgroundMode();
                break;
            case -12566464:
                // -12566464 represent dark gray background
                factory = new LightModeFactory();
                strategy = factory.CreateBackgroundMode();
                break;
        }

        strategy.ChangeBackgroundMode();
    }

    //Save file to Database via FileSaveService
    public void SaveFileToDatabase(String fileName, String content){
        fileSaveService.SaveFile(fileName, content);
    }

    //Get all file datas from Database via FileSaveService
    public Files GetAllFileData(){
        return fileSaveService.GetAllFileData();
    }

    //Delete file in Database via FileSaveService
    public void DeleteFileFromDatabase(int fileNumber){
        fileSaveService.DeleteFileFromDatabase(fileNumber);
    }
}
