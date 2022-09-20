package Pattern.Composite;

import java.util.Vector;

// Component，具有Composite以及Leaf會用到的所有method
public abstract class WordStyleMenu {

    public void add(WordStyleMenu menuComponent){
        throw new UnsupportedOperationException();
    }

    public void remove(WordStyleMenu menuComponent){
        throw new UnsupportedOperationException();
    }

    public String GetStyleName(){
        throw new UnsupportedOperationException();
    }

    public String GetStyle(){
        throw new UnsupportedOperationException();
    }

    public Vector GetAllChild(){
        throw new UnsupportedOperationException();
    }

    public Vector GetAllStyle(){
        throw new UnsupportedOperationException();
    }
}
