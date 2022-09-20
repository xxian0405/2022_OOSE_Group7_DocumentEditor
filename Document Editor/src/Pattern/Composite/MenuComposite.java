package Pattern.Composite;

import java.util.Iterator;
import java.util.Vector;

// Composite，用來保存Style項目
public class MenuComposite extends WordStyleMenu{
    // Collection選擇使用Vector是因為這個類型才能存入JComboBox中
    Vector menuComponents = new Vector();
    private String styleName = ""; //可能為字型、字體大小、粗體斜體、顏色

    public MenuComposite(){}

    public MenuComposite(String styleName){
        this.styleName = styleName;
    }

    @Override
    public void add(WordStyleMenu menuComponent){
        menuComponents.add((menuComponent));
    }

    @Override
    public void remove(WordStyleMenu menuComponent){
        menuComponents.remove(menuComponent);
    }

    // 取得當前Style名稱
    @Override
    public String GetStyleName(){
        return this.styleName;
    }

    // 取得所有Composite或所有Leaf
    @Override
    public Vector GetAllChild(){
        return menuComponents;
    }

    // 取得所有Leaf
    @Override
    public Vector GetAllStyle(){
        Vector allStyle = new Vector();

        Iterator iterator = menuComponents.iterator();
        while(iterator.hasNext()){
            MenuLeaf leaf = (MenuLeaf) iterator.next();
            allStyle.add(leaf.GetStyle());
        }

        return allStyle;
    }
}
