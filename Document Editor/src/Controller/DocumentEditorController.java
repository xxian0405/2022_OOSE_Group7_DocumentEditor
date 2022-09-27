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
    * According to Context(DocumentEditor), to choose which Color Mode we want the Factory to create.
    */
    public ModeStrategy ChangeColorMode(){
        ModeFactory factory = null;

        if(DocumentEditor.IsNowDarkMode()){
            factory = new LightModeFactory();
            DocumentEditor.SetIsNowDarkMode(false);
        }else{
            factory = new DarkModeFactory();
            DocumentEditor.SetIsNowDarkMode(true);
        }

        return factory.CreateBackgroundMode();
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
