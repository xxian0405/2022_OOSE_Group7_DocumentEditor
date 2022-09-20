package Pattern.Iterator;

import Model.FileFromDatabase;

// 集合的介面，定義不同種集合都至少會有的動作
public interface Aggregate {
    public Iterator CreateIterator();
    public void Add(FileFromDatabase file);
    public void Remove(int index);
    public int Size();
}
