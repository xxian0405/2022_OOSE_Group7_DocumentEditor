package Model;

// 從資料庫中撈出來的檔案資料存放的Model，一個物件代表一筆資料，多筆資料就會存放在aggregate中並透過iterator取出使用
public class FileFromDatabase {
    private int fileNumber;
    private String fileName, fileContent;

    public FileFromDatabase(int id, String name, String content){
        this.fileNumber = id;
        this.fileName = name;
        this.fileContent = content;
    }

    public int GetFileNumber(){
        return fileNumber;
    }

    public String GetFileName(){
        return fileName;
    }

    public String GetFileContent(){
        return fileContent;
    }
}
