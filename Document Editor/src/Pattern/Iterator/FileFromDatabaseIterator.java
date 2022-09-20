package Pattern.Iterator;

// Concrete Iterator，此為其中一種Aggregate(Files)的Iterator
public class FileFromDatabaseIterator implements Iterator{
    private Files files;
    private int current = 0;

    public FileFromDatabaseIterator(Files files){
        this.files = files;
    }

    // 取得Aggregate中的第一個物件
    @Override
    public Object First(){
        return files.databasesFiles.get(0);
    }

    // 取得Aggregate當前位置的下一個物件
    @Override
    public Object Next(){
        Object ret = null;
        current++;
        if(current < files.databasesFiles.size()){
            ret = files.databasesFiles.get(current);
        }

        return ret;
    }

    @Override
    public boolean HasNext(){
        return current < files.databasesFiles.size() ? true : false;
    }

    @Override
    public Object CurrentItem(){
        return files.databasesFiles.get(current);
    }

    @Override
    public int Size(){
        return files.Size();
    }

    @Override
    public void MoveToFirst(){
        this.current = 0;
    }
}
