package DocumentEditor;

import Pattern.Composite.MenuComposite;
import Pattern.Composite.MenuLeaf;
import Pattern.Composite.WordStyleMenu;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

// 選擇字型、字體大小、粗體斜體、顏色的彈窗
public class AboutFormat extends JFrame implements ItemListener, ActionListener {

    private JComboBox choose_word_style, choose_word_big, choose_word_pattern, choose_word_color;    //下拉列表

    private JPanel paneNorth;//用于装四个ComboBox
    private JPanel paneSouth;//用来装按钮

    private JTextField showText;//演示文本

    private JButton btn_ok, btn_cancel;

    private SimpleAttributeSet simpleAttributeSet;

    private WordStyleMenu menuComponent;
    public AboutFormat() {
        simpleAttributeSet = new SimpleAttributeSet();

        initComboBoxItem();
        initBox();
        initButton();
        initLocation();
        initListener();
        addBtnListener();

        this.setSize(550,240);
        this.setTitle("文字格式");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // 註冊按鈕事件監聽
    private void addBtnListener() {
        // TODO Auto-generated method stub		//注册按钮监听
        btn_cancel.addActionListener(this);
        btn_ok.addActionListener(this);
    }

    // 註冊下拉選單事件監聽
    private void initListener() {		//注册下拉框监听
        // TODO Auto-generated method stub
        choose_word_style.addItemListener(this);
        choose_word_big.addItemListener(this);
        choose_word_pattern.addItemListener(this);
        choose_word_color.addItemListener(this);
    }

    //按鈕初始化
    private void initButton() {
        // TODO Auto-generated method stub
        btn_ok = new JButton("OK");
        btn_cancel = new JButton("CANCEL");
    }

    /**
     * 初始化布局
     * 将每个控件按照一定得布局排在this窗口中
     */
    public void initLocation() {
        paneNorth = new JPanel();
        Iterator<WordStyleMenu> iterator = menuComponent.GetAllChild().iterator();

        WordStyleMenu thisMenu = iterator.next();
        paneNorth.add(new JLabel(thisMenu.GetStyleName() + ":"));
        paneNorth.add(choose_word_style);

        thisMenu = iterator.next();
        paneNorth.add(new JLabel(thisMenu.GetStyleName() + ":"));
        paneNorth.add(choose_word_big);

        thisMenu = iterator.next();
        paneNorth.add(new JLabel(thisMenu.GetStyleName() + ":"));
        paneNorth.add(choose_word_pattern);

        thisMenu = iterator.next();
        paneNorth.add(new JLabel(thisMenu.GetStyleName() + ":"));
        paneNorth.add(choose_word_color);

        paneNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));	//让add的组件不置顶
        this.add(paneNorth,BorderLayout.NORTH);

        paneSouth = new JPanel();
        paneSouth.add(btn_ok);
        paneSouth.add(btn_cancel);
        this.add(paneSouth, BorderLayout.SOUTH);

    }

    /**
     * 初始化几个comboBox
     * 把相应的选项加入
     */
    public void initBox() {
        Iterator<WordStyleMenu> iterator = menuComponent.GetAllChild().iterator();

        while(iterator.hasNext()){
            WordStyleMenu thisMenu = iterator.next();
            String thisStyleName = thisMenu.GetStyleName();

            Vector thisMenuLeafs = thisMenu.GetAllStyle();

            if(thisStyleName.equals("字體")){
                choose_word_style = new JComboBox(thisMenuLeafs);
            }else if(thisStyleName.equals("大小")){
                choose_word_big = new JComboBox(thisMenuLeafs);
            }else if(thisStyleName.equals("樣式")){
                choose_word_pattern = new JComboBox(thisMenuLeafs);
            }else if(thisStyleName.equals("顏色")){
                choose_word_color = new JComboBox(thisMenuLeafs);
            }
        }
    }

    //下拉選單初始化，Composite Pattern
    private void initComboBoxItem(){
        menuComponent = new MenuComposite();

        WordStyleMenu font_Family = new MenuComposite("字體");
        font_Family.add(new MenuLeaf("SERIF"));
        font_Family.add(new MenuLeaf("MONOSPACED"));
        font_Family.add(new MenuLeaf("SANS_SERIF"));
        font_Family.add(new MenuLeaf("新細明體"));
        font_Family.add(new MenuLeaf("標楷體"));
        font_Family.add(new MenuLeaf("微軟正黑體"));


        WordStyleMenu font_Size = new MenuComposite("大小");
        font_Size.add(new MenuLeaf("2"));
        font_Size.add(new MenuLeaf("4"));
        font_Size.add(new MenuLeaf("8"));
        font_Size.add(new MenuLeaf("16"));
        font_Size.add(new MenuLeaf("32"));
        font_Size.add(new MenuLeaf("64"));
        font_Size.add(new MenuLeaf("72"));

        // font_Pattern理論上不該做成Drop Down List的，因為這些樣式要可以同時套用
        // 最好的方法是把他們都改成一個Button，然後根據事件觸發來執行換樣式
        // 但這樣的話會使font_Pattern不再是composite的一分子，請小心
        WordStyleMenu font_Pattern = new MenuComposite("樣式");
        font_Pattern.add(new MenuLeaf("正常"));
        font_Pattern.add(new MenuLeaf("粗體"));
        font_Pattern.add(new MenuLeaf("斜體"));
        font_Pattern.add(new MenuLeaf("底線"));

        WordStyleMenu font_Color = new MenuComposite("顏色");
        font_Color.add(new MenuLeaf("黑色"));
        font_Color.add(new MenuLeaf("白色"));
        font_Color.add(new MenuLeaf("紅色"));
        font_Color.add(new MenuLeaf("黃色"));
        font_Color.add(new MenuLeaf("藍色"));
        font_Color.add(new MenuLeaf("綠色"));

        menuComponent.add(font_Family);
        menuComponent.add(font_Size);
        menuComponent.add(font_Pattern);
        menuComponent.add(font_Color);
    }

    //按鈕onClick事件
    @Override
    public void actionPerformed(ActionEvent e) {	//对按钮的监听
        // TODO Auto-generated method stub
        if (e.getSource() == btn_cancel) {
            this.dispose();//销毁当前窗口
        }else if (e.getSource() == btn_ok) {
            DocumentEditor.getEdit_text_area().setCharacterAttributes(simpleAttributeSet, true);
            this.dispose();
        }
    }

    //下拉選單onChange事件
    @Override
    public void itemStateChanged(ItemEvent e) {	//对下拉框的监听
        // TODO Auto-generated method stub
        if (e.getItem() == "SERIF") {
            StyleConstants.setFontFamily(simpleAttributeSet, "SERIF");
        }else if (e.getItem() == "MONOSPACED") {
            StyleConstants.setFontFamily(simpleAttributeSet, "MONOSPACED");
        }else if (e.getItem() == "SANS_SERIF") {
            StyleConstants.setFontFamily(simpleAttributeSet, "SANS_SERIF");
        }else if (e.getItem() == "新細明體") {
            StyleConstants.setFontFamily(simpleAttributeSet, "新細明體");
        }else if (e.getItem() == "標楷體") {
            StyleConstants.setFontFamily(simpleAttributeSet, "標楷體");
        }else if (e.getItem() == "微軟正黑體") {
            StyleConstants.setFontFamily(simpleAttributeSet, "微軟正黑體");
        }else if (e.getItem() == "正常") {
            StyleConstants.setItalic(simpleAttributeSet, false);
            StyleConstants.setBold(simpleAttributeSet, false);
            StyleConstants.setUnderline(simpleAttributeSet, false);
        }else if (e.getItem() == "斜體") {
            StyleConstants.setItalic(simpleAttributeSet, true);
        }else if (e.getItem() == "粗體") {
            StyleConstants.setBold(simpleAttributeSet, true);
        }else if (e.getItem() == "底線") {
            StyleConstants.setUnderline(simpleAttributeSet, true);
        }else if (e.getItem() == "2") {
            StyleConstants.setFontSize(simpleAttributeSet, 2);
        }else if (e.getItem() == "4") {
            StyleConstants.setFontSize(simpleAttributeSet, 4);
        }else if (e.getItem() == "8") {
            StyleConstants.setFontSize(simpleAttributeSet, 8);
        }else if (e.getItem() == "16") {
            StyleConstants.setFontSize(simpleAttributeSet, 16);
        }else if (e.getItem() == "32") {
            StyleConstants.setFontSize(simpleAttributeSet, 32);
        }else if (e.getItem() == "64") {
            StyleConstants.setFontSize(simpleAttributeSet, 64);
        }else if (e.getItem() == "72") {
            StyleConstants.setFontSize(simpleAttributeSet, 72);
        }else if (e.getItem() == "紅色") {
            StyleConstants.setForeground(simpleAttributeSet, Color.red);
        }else if (e.getItem() == "黑色") {
            StyleConstants.setForeground(simpleAttributeSet, Color.black);
        }else if (e.getItem() == "藍色") {
            StyleConstants.setForeground(simpleAttributeSet, Color.blue);
        }else if (e.getItem() == "黃色") {
            StyleConstants.setForeground(simpleAttributeSet, Color.yellow);
        }else if (e.getItem() == "綠色") {
            StyleConstants.setForeground(simpleAttributeSet, Color.green);
        }else if (e.getItem() == "白色") {
            StyleConstants.setForeground(simpleAttributeSet, Color.WHITE);
        }
    }
}