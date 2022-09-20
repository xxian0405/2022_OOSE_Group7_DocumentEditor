package DocumentEditor;

import Controller.DocumentEditorController;
import Model.FileFromDatabase;
import Pattern.Iterator.Iterator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//從資料庫開啟檔案的彈窗
public class SelectFileFromDatabase extends JFrame implements ActionListener {
    private JPanel button_Panel;
    private JButton btn_Choose, btn_Delete, btn_Cancle;
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollBar;
    private DocumentEditorController documentEditorController = DocumentEditorController.GetController();
    private Iterator iterator;
    private String[] columnNames = {"檔案編號", "檔案名稱", "內容預覽"};
    private String[][] filesData;

    public SelectFileFromDatabase(){
        //要先取得FileFromDatabase的iterator
        iterator = documentEditorController.GetAllFileData().CreateIterator();

        initData(); //才可以initData(因為要先有iterator才可以把資料塞進二維陣列中)
        initAll(); //接著才可以initAll(因為二維陣列中要先有資料才可以把資料放進table中)
        this.setSize(500, 200);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    //初始化所有UI物件
    private void initAll(){
        model = new DefaultTableModel(filesData, columnNames); //簡單來說就是紀錄table產生後的原始樣貌，若要刪除row的話要從這個model中remove掉
        table = new JTable(model);
        table.setBounds(30, 40, 200, 300);
        scrollBar = new JScrollPane(table);
        scrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        button_Panel = new JPanel();

        btn_Choose = new JButton("選擇");
        btn_Choose.addActionListener(this);

        btn_Delete = new JButton("刪除");
        btn_Delete.addActionListener(this);

        btn_Cancle = new JButton("取消");
        btn_Cancle.addActionListener(this);

        button_Panel.add(btn_Choose);
        button_Panel.add(btn_Delete);
        button_Panel.add(btn_Cancle);

        this.add(scrollBar, BorderLayout.CENTER);
        this.add(button_Panel, BorderLayout.SOUTH);
    }

    //按鈕onClick事件
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==btn_Cancle){
            this.dispose();
        }else if(e.getSource()==btn_Choose && table.getSelectedRow() != -1){
            this.SetSelectedFileContentToTextArea();
            this.dispose();
        }else if(e.getSource()==btn_Delete && table.getSelectedRow() != -1){
            //刪除資料庫的檔案時
            documentEditorController.DeleteFileFromDatabase(this.GetSelectedFileNumber()); //要到資料庫裡面刪

            //同時透過model來刪除畫面上的row(這個無傷大雅，彈窗重開之後就會消失了可以不用這行，但為了UX(使用者體驗)，還是在刪除的當下就連畫面一起處理)
            model.removeRow(table.getSelectedRow());
        }
    }

    //初始化從資料庫開啟檔案之資訊(檔案編號、檔案名稱)
    private void initData(){
        int filesSize = iterator.Size();
        filesData = new String[filesSize][3]; //將存資料用的二維陣列的長度以資料總數定義
        FileFromDatabase file;
        int current = 0;

        //透過iterator把資料塞進二維陣列中，就可將資料設置到此彈窗的table中
        while(iterator.HasNext()) {
            file = (FileFromDatabase) iterator.CurrentItem(); //取得當前檔案資料
            filesData[current][0] = Integer.toString(file.GetFileNumber()); //存放編號
            filesData[current][1] = file.GetFileName(); //存放名稱

            /* Start of 內容預覽 */
            String fullContent = file.GetFileContent(); //先取得檔案內容
            String previewContent = ""; //宣告內容預覽字串變數
            int previewContentMaxLength = 10; //宣告預覽內容最高上限字數(預設10字元)

            if(fullContent.length() > 10){
                //如果檔案內容超過10字元，須將超出的部分改成...
                for(int i = 0; i < previewContentMaxLength; i++){
                    if(fullContent.charAt(i) == ' '){
                        // 空格不算在字數內，因此調高內容預覽最高上限字數(只是為了跳過空格且仍保有10字的內容而已)
                        previewContentMaxLength ++;
                    }

                    previewContent += fullContent.charAt(i); //將完整內容一字一字拼進去預覽內容內
                }

                previewContent += "..."; //最後在最後面加上...
            }
            filesData[current][2] = previewContent;
            /* End of 內容預覽 */

            iterator.Next();//下一筆
            current++;
        }
    }

    //將選擇的檔案之檔案內容設置到文字輸入區域內
    private void SetSelectedFileContentToTextArea(){
        int fileNumber = this.GetSelectedFileNumber();
        FileFromDatabase file;

        iterator.MoveToFirst(); //執行完initData()後，iterator內部的current(不是initData()的current)應該已經不是0了，因此先將iterator內的current重置為0
        while (iterator.HasNext()){
            file = (FileFromDatabase) iterator.CurrentItem(); //取得當前檔案資料

            if(fileNumber == file.GetFileNumber()){
                // 若選擇的檔案編號 = 當前iterator執行到的檔案資料的編號，就將此筆資料的檔案內容設置到文字輸入區域內並break迴圈
                DocumentEditor.getEdit_text_area().setText(file.GetFileContent());
                break;
            }

            iterator.Next();
        }
    }

    //取得被選取的檔案的檔案編號
    private int GetSelectedFileNumber(){
        return Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
    }
}
