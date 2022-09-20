package Model;

import Pattern.Iterator.Files;

import java.sql.*;

// 儲存檔案, 存取資料庫用的Service Model
public class FileSaveService {
    //連線字串
    private String connectionString = new CommonTool().GetConnectionString();

    //將檔案儲存至資料庫中
    public void SaveFile(String fileName, String fileContent){
        // use String.format() to prevent SQL Injection
        String sql = String.format("INSERT INTO FileStorage (FileName, FileContent) VALUES ('%s', '%s')", fileName, fileContent);

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            preparedStatement.execute();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //取得資料庫中所有檔案的資訊
    public Files GetAllFileData(){

        String sql = "SELECT FileNumber, FileName, FileContent FROM FileStorage ORDER BY FileNumber";
        ResultSet resultSet = null;
        Files files = new Files();

        try (Connection connection = DriverManager.getConnection(connectionString);
                Statement statement = connection.createStatement();){

            resultSet = statement.executeQuery(sql); //執行sql後會把取得的資料塞到resultSet中
            connection.close();

            // 透過迴圈把屬性塞入Aggregate裡，可再透過iterator取用
            while(resultSet.next()){
                files.Add(new FileFromDatabase(resultSet.getInt("FileNumber"), resultSet.getNString("FileName"), resultSet.getNString("FileContent")));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return files;
    }

    //根據檔案編號刪除資料庫中對應的檔案
    public void DeleteFileFromDatabase(int fileNumber){
        // use String.format() to prevent SQL Injection
        String sql = String.format("DELETE FROM FileStorage WHERE FileNumber = %d", fileNumber);

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            preparedStatement.execute();
            connection.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
