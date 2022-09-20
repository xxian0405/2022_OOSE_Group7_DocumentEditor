package Pattern.Iterator;

import Model.FileFromDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Concrete Aggregate，此為FileFromDatabase這類物件的Aggregate
public class Files implements Aggregate{
    ArrayList<FileFromDatabase> databasesFiles = new ArrayList<>();

    //將FileFromDatabase的物件加入集合中
    @Override
    public void Add(FileFromDatabase fileFromDatabase){
        databasesFiles.add(fileFromDatabase);
    }

    //將集合中對應index的FileFromDatabase物件移除
    @Override
    public void Remove(int index){
        databasesFiles.remove(index);
    }

    // 建立Iterator時將自己(this)作為建構子參數傳入Iterator，供Iterator使用
    @Override
    public Iterator CreateIterator(){
        return new FileFromDatabaseIterator(this);
    }

    // 確認集合的大小
    @Override
    public int Size(){
        return databasesFiles.size();
    }
}
