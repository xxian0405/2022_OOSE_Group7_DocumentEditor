package Pattern.Iterator;

// Iterator介面，讓不同種Aggregate的Iterator來實作此介面，如Files這個Aggregate的Iterator(FileFromDatabaseIterator)
public interface Iterator {
    public Object First();
    public Object Next();
    public boolean HasNext();
    public Object CurrentItem();
    public int Size();
    public void MoveToFirst();
}
