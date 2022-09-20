package DocumentEditor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import Controller.DocumentEditorController;
import Pattern.Bridge.Abstraction.DatabaseFile;
import Pattern.Bridge.Abstraction.SavedFile;
import Pattern.Bridge.Abstraction.TextFile;
import Pattern.Command.*;
import Pattern.Command.Memento.Caretaker;
import Pattern.Command.Memento.Originator;

//主畫面
public class DocumentEditor extends JFrame implements ActionListener {
    private DocumentEditorController documentEditorController;

    private Originator originator;
    private Caretaker caretaker;
    private Invoker invoker;
    private Receiver receiver;
    private Command undoCommand, redoCommand;
    private SavedFile savedFile;
    private int current = -1; //當前caretaker中所保存的最新的位置

    //菜单栏
    private JMenuBar menuBar;
    //菜单项
    private JMenu menu_File, menu_Edit, menu_DarkModeSelector, menu_Format;
    //file菜单项内容
    private JMenuItem item_new, item_newwindow, item_open, item_open_from_dataBase, item_save, item_save_to_dataBase, item_exit;
    //edit菜单项内容
    private JMenuItem item_undo, item_redo, item_cut, item_copy, item_stick, item_delete;

    //format菜单项内容
    private JMenuItem item_word_format;

    //深色模式菜單項內容
    private JMenuItem dark_Mode;

    //文本区域
    private static JTextPane edit_text_area;    //設為static是因為要在其他彈窗(改style或要將資料庫中讀出的內容寫入)時呼叫
    //文本滚动条
    private JScrollPane scroll_bar;

    //系统剪切板
    private Clipboard clipboard;
    //文件选择器
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

        this.setJMenuBar(menuBar);
        this.setSize(800,600);
        this.add(scroll_bar);
        this.setTitle("文件編輯器");
        this.setVisible(true);
        this.setLocationRelativeTo(null);	//窗体位于正中间位置
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	// EXIT_ON_CLOSE  https://www.cnblogs.com/lihaiming93/p/4752422.html 后续还要改，加上退出询问

        originator.SetState(edit_text_area.getFont(), "", Color.BLACK);
        caretaker.AddMemento(originator.CreateMemento());
        current++;
    }

    //初始化選單列
    private void initMenuBar() {

        //初始化菜单栏
        menuBar = new JMenuBar();

        //file菜单项
        menu_File = new JMenu("文件(F)");
        menu_File.setMnemonic('f');		// 设置快捷键-alt+f:打开 - 也可以传入KeyEvent.VK_F
        item_new = new JMenuItem("新建");
        item_newwindow = new JMenuItem("新窗口");
        item_open = new JMenuItem("打开");
        item_open_from_dataBase = new JMenuItem("從資料庫開啟");
        item_save = new JMenuItem("保存");
        item_save.setAccelerator(KeyStroke.getKeyStroke('S',java.awt.Event.CTRL_MASK,false));//给item添加快捷键 CTRL_MASK = ctrl键
        item_save_to_dataBase = new JMenuItem("存檔至資料庫");
        item_exit = new JMenuItem("退出");
        menu_File.add(item_new);
        menu_File.add(item_newwindow);
        menu_File.add(item_open);
        menu_File.add(item_open_from_dataBase);
        menu_File.add(item_save);
        menu_File.add(item_save_to_dataBase);
        menu_File.add(item_exit);

        //edit菜单项
        menu_Edit = new JMenu("编辑(E)");
        menu_Edit.setMnemonic('e');

        item_undo = new JMenuItem("撤销");
        item_undo.setAccelerator(KeyStroke.getKeyStroke('Z',java.awt.Event.CTRL_MASK,false));

        item_redo = new JMenuItem("恢复");
        item_redo.setAccelerator(KeyStroke.getKeyStroke('Y',java.awt.Event.CTRL_MASK,false));

        item_cut = new JMenuItem("剪切");
        item_cut.setAccelerator(KeyStroke.getKeyStroke('X',java.awt.Event.CTRL_MASK,false));

        item_copy = new JMenuItem("复制");
        item_copy.setAccelerator(KeyStroke.getKeyStroke('C',java.awt.Event.CTRL_MASK,false));

        item_stick = new JMenuItem("粘贴");
        item_stick.setAccelerator(KeyStroke.getKeyStroke('V',java.awt.Event.CTRL_MASK,false));

        item_delete = new JMenuItem("删除");
        menu_Edit.add(item_undo);
        menu_Edit.add(item_redo);
        menu_Edit.add(item_cut);
        menu_Edit.add(item_copy);
        menu_Edit.add(item_stick);
        menu_Edit.add(item_delete);

        //format菜单项
        menu_Format = new JMenu("格式(O)");
        menu_Format.setMnemonic('o');
        item_word_format = new JMenuItem("字体(F)");			//CTRL_MASK = ctrl键
        item_word_format.setAccelerator(KeyStroke.getKeyStroke('F',java.awt.Event.CTRL_MASK,false));//给item添加快捷键
        menu_Format.add(item_word_format);

        //深色模式菜單項
        menu_DarkModeSelector = new JMenu("深淺色模式");
        dark_Mode = new JMenuItem("模式切換");
        menu_DarkModeSelector.add(dark_Mode);

        //加入菜单栏
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

        clipboard = this.getToolkit().getSystemClipboard();
    }

    //開啟檔案
    private void openFile() {

        File file = null;
        int result;
        fileChooser =new JFileChooser("C:\\Users\\62473\\Desktop\\");
        result = fileChooser.showOpenDialog(rootPane);

        if(result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile(); // 若点击了确定按钮，给file填文件路径
        }
        else return;	//按取消键后不会运行下面代码

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
        //https://zhidao.baidu.com/question/2204274734088183468.html

        if (e.getSource()==item_word_format) {
            new AboutFormat();
        }
        else if (e.getSource()==item_new) {
            savedFile = new TextFile(edit_text_area.getText());
            savedFile.SaveFile();
            new DocumentEditor();
            this.dispose();
        }
        else if (e.getSource()==item_newwindow) {
            new DocumentEditor();		//添加新窗口，父窗口不会退出
        }
        else if (e.getSource()==item_exit){
            savedFile = new TextFile(edit_text_area.getText());
            savedFile.SaveFile();
            this.dispose();			//退出询问(目前有无更改都会弹出保存窗口)
        }
        else if (e.getSource()==item_save) {
            savedFile = new TextFile(edit_text_area.getText());
            savedFile.SaveFile();
        }
        else if(e.getSource()==item_save_to_dataBase){
            savedFile = new DatabaseFile(edit_text_area.getText());
            savedFile.SaveFile();
        }
        else if (e.getSource()==item_open) {
            this.openFile();
        }
        else if (e.getSource()==item_open_from_dataBase) {
            new SelectFileFromDatabase();
        }
        else if (e.getSource()==item_undo && current > -1) {		//撤销可以撤销是撤销上一步文本操作
            invoker.SetCommand(undoCommand);
            invoker.InvokeCommand(current);
            current--;

            // (還沒測試)Line:246跟252好像在undo/redo後也會將異動結果新增到Memento(因為undo/redo畫面也會動，可能會觸發edit_text_area的事件)
            // 所以要將執行undo/redo時加入的Memento移除
            caretaker.RemoveMemento(caretaker.GetMementoAmount() - 1);
        }
        else if (e.getSource()==item_redo && current > -1) {		//恢复就是恢复上一步文本操作(需要被撤销)
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
            documentEditorController.ChangeColorMode();
        }
    }

    //初始化選單內容事件監聽
    public void initListener()
    {
        // 菜单iter监听
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
            }

            // 在textArea上backspace或delete時
            @Override
            public void removeUpdate(DocumentEvent e) {
                current--;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    public static JTextPane getEdit_text_area() {
        //返回文本编辑区给其他窗口
        return edit_text_area;
    }
}