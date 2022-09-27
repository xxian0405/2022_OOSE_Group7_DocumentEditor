package DocumentEditor;

import Controller.DocumentEditorController;
import Pattern.Bridge.Abstraction.DatabaseFile;
import Pattern.Bridge.Abstraction.SavedFile;
import Pattern.Bridge.Abstraction.TextFile;
import Pattern.Command.*;
import Pattern.Command.Memento.Caretaker;
import Pattern.Command.Memento.Originator;
import Pattern.Strategy.ModeStrategy;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

//主畫面
public class DocumentEditor extends JFrame implements ActionListener {
    private DocumentEditorController documentEditorController;

    // This class is a Context of Strategy Pattern because it has a Strategy type object
    private ModeStrategy strategy;

    private Originator originator;
    private Caretaker caretaker;
    private Invoker invoker;
    private Receiver receiver;
    private Command undoCommand, redoCommand;
    private SavedFile savedFile;

    private int current = -1; //當前caretaker中所保存的最新的位置

    //選單列
    private static JMenuBar menuBar;
    //選單項
    private static JMenu menu_File, menu_Edit, menu_DarkModeSelector, menu_Format;
    private static JMenu[] menuGroup = new JMenu[4];

    //file選單項內容
    private static JMenuItem item_new, item_newwindow, item_open, item_open_from_dataBase, item_save, item_save_to_dataBase, item_exit;

    //edit選單項內容
    private static JMenuItem item_undo, item_redo, item_cut, item_copy, item_stick, item_delete;

    //format選單項内容
    private static JMenuItem item_word_format;

    //深色模式選單項內容
    private static JMenuItem dark_Mode;

    //記錄當下是否為深色模式
    private static boolean isDarkMode = false;
    private static JMenuItem[] menuItemsGroup = new JMenuItem[15];

    //文字輸入的地方
    private static JTextPane edit_text_area;

    private static JTextPane count_words;
    //輸入區域滾輪
    private static JScrollPane scroll_bar;

    //系统剪切板
    private Clipboard clipboard;
    //文件選擇器
    private JFileChooser fileChooser;

    public DocumentEditor()
    {
        // Controller
        documentEditorController = DocumentEditorController.GetController();

        // Command+Memento
        invoker = new Invoker();
        originator = new Originator();
        caretaker = new Caretaker();
        receiver = new Receiver(originator, caretaker);
        undoCommand = new UndoCommand(receiver);
        redoCommand = new RedoCommand(receiver);

        initMenuBar();
        initEditText();
        initListener();
        countWords();
        count();

        this.setJMenuBar(menuBar);
        this.setSize(800,600);
        this.add(scroll_bar);
        this.setTitle("G7 Document Editor");
        this.setVisible(true);
        this.setLocationRelativeTo(null);	//設定視窗位於畫面最中央
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	// EXIT_ON_CLOSE

        originator.SetState(edit_text_area.getFont(), "", Color.BLACK);
        caretaker.AddMemento(originator.CreateMemento());
        current++;
    }

    //初始化選單列
    private void initMenuBar() {

        //初始化選單列
        menuBar = new JMenuBar();

        //file選單項
        menu_File = new JMenu("文件(F)");
        menu_File.setMnemonic('f');
        item_new = new JMenuItem("開新文件");
        item_newwindow = new JMenuItem("新視窗");
        item_open = new JMenuItem("開啟");
        item_open_from_dataBase = new JMenuItem("從資料庫開啟");
        item_save = new JMenuItem("存檔");
        item_save.setAccelerator(KeyStroke.getKeyStroke('S',java.awt.Event.CTRL_MASK,false));
        item_save_to_dataBase = new JMenuItem("存檔至資料庫");
        item_exit = new JMenuItem("退出");
        menu_File.add(item_new);
        menu_File.add(item_newwindow);
        menu_File.add(item_open);
        menu_File.add(item_open_from_dataBase);
        menu_File.add(item_save);
        menu_File.add(item_save_to_dataBase);
        menu_File.add(item_exit);

        //edit選單項
        menu_Edit = new JMenu("編輯(E)");
        menu_Edit.setMnemonic('e');

        item_undo = new JMenuItem("上一步");
        item_undo.setAccelerator(KeyStroke.getKeyStroke('Z',java.awt.Event.CTRL_MASK,false));

        item_redo = new JMenuItem("下一步");
        item_redo.setAccelerator(KeyStroke.getKeyStroke('Y',java.awt.Event.CTRL_MASK,false));

        item_cut = new JMenuItem("剪下");
        item_cut.setAccelerator(KeyStroke.getKeyStroke('X',java.awt.Event.CTRL_MASK,false));

        item_copy = new JMenuItem("複製");
        item_copy.setAccelerator(KeyStroke.getKeyStroke('C',java.awt.Event.CTRL_MASK,false));

        item_stick = new JMenuItem("貼下");
        item_stick.setAccelerator(KeyStroke.getKeyStroke('V',java.awt.Event.CTRL_MASK,false));

        item_delete = new JMenuItem("删除");
        menu_Edit.add(item_undo);
        menu_Edit.add(item_redo);
        menu_Edit.add(item_cut);
        menu_Edit.add(item_copy);
        menu_Edit.add(item_stick);
        menu_Edit.add(item_delete);

        //format選單項
        menu_Format = new JMenu("格式(O)");
        menu_Format.setMnemonic('o');
        item_word_format = new JMenuItem("字體(F)");			//CTRL_MASK = ctrl键
        item_word_format.setAccelerator(KeyStroke.getKeyStroke('F',java.awt.Event.CTRL_MASK,false));
        menu_Format.add(item_word_format);

        //深色模式菜單項
        menu_DarkModeSelector = new JMenu("深淺色模式");
        dark_Mode = new JMenuItem("模式切換");
        menu_DarkModeSelector.add(dark_Mode);

        //把選單項加到選單列中
        menuBar.add(menu_File);
        menuBar.add(menu_Edit);
        menuBar.add(menu_Format);
        menuBar.add(menu_DarkModeSelector);
    }

    //初始化文字輸入區
    private void initEditText() {
        edit_text_area = new JTextPane();
        scroll_bar = new JScrollPane(edit_text_area);

        scroll_bar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_bar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        clipboard = this.getToolkit().getSystemClipboard();//建立剪貼簿
    }
    private void countWords(){
        count_words = new JTextPane();
        count_words.setSize(50,50);
        count_words.setEditable(false);
        count_words.setFont(new Font(Font.SERIF, Font.PLAIN,  15));
        this.add(count_words,BorderLayout.SOUTH);
    }

    private void count(){ //數字數
        String words;
        words = edit_text_area.getText();
        count_words.setText("總共有"+ words.length()+"個字");
    }

    //開啟檔案
    private void openFile() {

        File file = null;
        int result;
        fileChooser =new JFileChooser("C:\\Users\\62473\\Desktop\\");
        result = fileChooser.showOpenDialog(rootPane);

        if(result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile(); // 若點擊了確定按鈕，把路徑傳給file
        }
        else return;	//按取消鍵後不會執行下面程式碼

        if(file.isFile() && file.exists()) {
            try {
                edit_text_area.setText("");		//清空目前文本内容
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file),"UTF-8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String readLine;
                while ((readLine = reader.readLine()) != null) {
                    edit_text_area.setText(edit_text_area.getText() + readLine + '\n');
                }
                reader.close();
            }
            catch (IOException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

    }

    //選單內容onClick事件
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        if (e.getSource()==item_word_format) {
            new AboutFormat();
        }
        else if (e.getSource()==item_new) {
            savedFile = new TextFile();
            savedFile.SaveFile();
            new DocumentEditor();
            this.dispose();
        }
        else if (e.getSource()==item_newwindow) {
            new DocumentEditor();		//添加新視窗，父視窗不會退出
        }
        else if (e.getSource()==item_exit){
            savedFile = new TextFile();
            savedFile.SaveFile();
            this.dispose();			//退出詢問(有無更改都會跳出)
        }
        else if (e.getSource()==item_save) {
            savedFile = new TextFile();
            savedFile.SaveFile();
        }
        else if(e.getSource()==item_save_to_dataBase){
            savedFile = new DatabaseFile();
            savedFile.SaveFile();
        }
        else if (e.getSource()==item_open) {
            this.openFile();
        }
        else if (e.getSource()==item_open_from_dataBase) {
            new SelectFileFromDatabase();
        }
        else if (e.getSource()==item_undo && current > -1) {		//如果可以undo,才執行
            invoker.SetCommand(undoCommand);
            invoker.InvokeCommand(current);//觸發undo
            current--;

            // (還沒測試)Line:254跟260好像在undo/redo後也會將異動結果新增到Memento(因為undo/redo畫面也會動，可能會觸發edit_text_area的事件)
            // 所以要將執行undo/redo時加入的Memento移除
            caretaker.RemoveMemento(caretaker.GetMementoAmount() - 1);
        }
        else if (e.getSource()==item_redo && current > -1) {
            invoker.SetCommand(redoCommand);
            invoker.InvokeCommand(current);
            current++;
            caretaker.RemoveMemento(caretaker.GetMementoAmount() - 1);
        }
        else if (e.getSource()==item_copy) {
            edit_text_area.copy();
        }
        else if (e.getSource()==item_cut) {
            edit_text_area.cut();
        }
        else if (e.getSource()==item_stick) {
            edit_text_area.paste();
        }
        else if (e.getSource()==item_delete) {
            edit_text_area.replaceSelection("");
        }
        else if (e.getSource() == dark_Mode) {
            strategy = documentEditorController.ChangeColorMode();
            strategy.ChangeBackgroundMode();
        }
    }

    //初始化選單內容事件監聽
    public void initListener()
    {
        // 選單item監聽
        item_new.addActionListener(this);
        item_newwindow.addActionListener(this);
        item_open.addActionListener(this);
        item_open_from_dataBase.addActionListener(this);
        item_save.addActionListener(this);
        item_save_to_dataBase.addActionListener(this);
        item_exit.addActionListener(this);
        item_undo.addActionListener(this);
        item_redo.addActionListener(this);
        item_cut.addActionListener(this);
        item_copy.addActionListener(this);
        item_stick.addActionListener(this);
        item_delete.addActionListener(this);
        item_word_format.addActionListener(this);
        dark_Mode.addActionListener(this);

        edit_text_area.getDocument().addDocumentListener(new DocumentListener() {

            // 在textArea上輸入時
            @Override
            public void insertUpdate(DocumentEvent e) {
                // 畫面上每輸入一個字，就要保存一次state到originator中
                originator.SetState(edit_text_area.getFont(), edit_text_area.getText(), edit_text_area.getForeground());


                // 並透過originator產生一個Memento並交由caretaker保管
                caretaker.AddMemento(originator.CreateMemento());

                // 更新caretaker當前的位置
                current++;
                count();
            }

            // 在textArea上backspace或delete時
            @Override
            public void removeUpdate(DocumentEvent e) {
                current--;
                count();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    public static JTextPane getEdit_text_area() {
        return edit_text_area;
    }

    //取得MenuBar，目前用於深色模式功能
    public static JMenuBar GetMenuBar(){
        return menuBar;
    }

    //取得所有Menu，可一次操作所有Menu，目前用於深色模式功能
    public static JMenu[] GetMenuGroup(){
        menuGroup[0] = menu_File;
        menuGroup[1] = menu_Edit;
        menuGroup[2] = menu_Format;
        menuGroup[3] = menu_DarkModeSelector;

        return menuGroup;
    }

    //取得所有MenuItem，可一次操作所有MenuItem，目前用於深色模式功能
    public static JMenuItem[] GetMenuItemsGroup(){
        menuItemsGroup[0] = item_new;
        menuItemsGroup[1] = item_newwindow;
        menuItemsGroup[2] = item_open;
        menuItemsGroup[3] = item_open_from_dataBase;
        menuItemsGroup[4] = item_save;
        menuItemsGroup[5] = item_save_to_dataBase;
        menuItemsGroup[6] = item_exit;
        menuItemsGroup[7] = item_undo;
        menuItemsGroup[8] = item_redo;
        menuItemsGroup[9] = item_cut;
        menuItemsGroup[10] = item_copy;
        menuItemsGroup[11] = item_stick;
        menuItemsGroup[12] = item_delete;
        menuItemsGroup[13] = item_word_format;
        menuItemsGroup[14] = dark_Mode;

        return menuItemsGroup;
    }

    public static JScrollPane GetScrollBar(){
        return scroll_bar;
    }

    //確認當前是否為深色模式
    public static boolean IsNowDarkMode(){
        return isDarkMode;
    }

    //修改當前是否為深色模式
    public static void SetIsNowDarkMode(boolean flag){
        isDarkMode = flag;
    }
}