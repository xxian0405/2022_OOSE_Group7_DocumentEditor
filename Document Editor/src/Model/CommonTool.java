package Model;

// 提供共用method的class，只是目前只有GetConnectionString()而已
public class CommonTool {
    public CommonTool(){}

    /*
    * 實作時請先照一般網路上的做法, 成功就成功
    * 若失敗就可能會出現PKIX問題
    * 請將trustServerCertificate改為true
    * 且新加一行trustStore=路徑\\路徑\\....\\cacerts;trustStorePassword=changeit;
    * 就可確保成功連線
    * PKIX解決方法參考: https://techcommunity.microsoft.com/t5/azure-database-support-blog/pkix-path-building-failed-unable-to-find-valid-certification/ba-p/2591304
    */
    private String connectionString = "jdbc:sqlserver://localhost:1433;" //資料庫伺服器主機位置，一般測試用在本機(localhost)
            + "database=DocumentEditor;" // 資料庫名稱
            + "user=sa;" // 使用者帳戶
            + "password=32887297;" // 使用者密碼
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "loginTimeout=30;"
            + "trustStore=C:\\Program Files\\Java\\jdk-18.0.2.1\\lib\\security\\cacerts;trustStorePassword=changeit;";

    public String GetConnectionString(){
        return connectionString;
    }
}
