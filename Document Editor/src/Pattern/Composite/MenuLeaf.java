package Pattern.Composite;

// Leaf，Style的內容
public class MenuLeaf extends WordStyleMenu{
    private String style; //可能為不同的字體名稱、不同的大小、粗體斜體、不同的顏色

    public MenuLeaf(String style){
        this.style = style;
    }

    //取得style
    @Override
    public String GetStyle(){
        return style;
    }
}
