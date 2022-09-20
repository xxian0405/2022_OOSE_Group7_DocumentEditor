package Pattern.Bridge.Implementor;

import Controller.DocumentEditorController;
import DocumentEditor.DocumentEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Concrete Implementor，實作檔案儲存動作
// 與儲存成文字檔案不同，使用此功能時會先跳出彈窗(JFrame)讓使用者輸入檔案名稱後才進行儲存
public class SaveToDatabase extends JFrame implements FileSave, ActionListener {
    private JPanel panel, btn_Panel;
    private JButton btn_Ok, btn_Cancle;
    private JTextField file_Name;
    private JLabel title;

    public SaveToDatabase(){}

    // 當Refined Abstraction(DatabaseFile)被建立時，為了讓Abstraction擁有一個Implementor(詳情參考class diagram)
    // 因此會同時建立出此Concrete Implementor，並透過此之SaveFile()來觸發initAll()
    @Override
    public void SaveFile(){
        initAll();
    }

    // 初始化所有UI物件
    private void initAll(){
        initButton();
        initTextField();

        panel = new JPanel();
        panel.add(title, BorderLayout.CENTER);
        panel.add(file_Name, BorderLayout.CENTER);

        btn_Panel = new JPanel();
        btn_Panel.add(btn_Ok, BorderLayout.SOUTH);
        btn_Panel.add(btn_Cancle, BorderLayout.SOUTH);

        this.add(panel, BorderLayout.CENTER);
        this.add(btn_Panel, BorderLayout.SOUTH);
        this.setSize(400,100);
        this.setTitle("存檔至資料庫");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // 初始化按鈕
    private void initButton(){
        btn_Ok = new JButton("儲存");
        btn_Ok.addActionListener(this);

        btn_Cancle = new JButton("取消");
        btn_Cancle.addActionListener(this);
    }

    // 初始化輸入方塊
    private void initTextField(){
        title = new JLabel("存進資料庫的檔案名稱：");
        file_Name = new JTextField(16);
    }

    // 按鈕onClick事件
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==btn_Cancle){
            //this代表這個class，因為這個class有繼承JFrame，因此這個class也是一個View的物件，因此this.dispose()=把這個View的物件拋棄(關掉)
            this.dispose();
        }else if(e.getSource()==btn_Ok){
            // 由於要儲存至資料庫，因此從View(也就是此class，因為此class同時是彈窗)告訴Controller要儲存檔案
            // 而Controller會再告訴model(FileSaveService)要儲存資料
            // Controller只是告訴model而已，實際要實作儲存動作的是model
            DocumentEditorController.GetController().SaveFileToDatabase(file_Name.getText(), DocumentEditor.getEdit_text_area().getText());
            this.dispose();
        }
    }
}
