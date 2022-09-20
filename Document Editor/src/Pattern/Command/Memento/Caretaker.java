package Pattern.Command.Memento;

import java.util.ArrayList;

// 用於保存以及提交Memento
public class Caretaker {
    private ArrayList<Memento> mementoArrayList = new ArrayList<>();

    public void AddMemento(Memento memento){
        mementoArrayList.add(memento);
    }

    public Memento GetMemento(int index){
        return mementoArrayList.get(index);
    }

    public void RemoveMemento(int index){
        mementoArrayList.remove(index);
    }

    public int GetMementoAmount(){
        return mementoArrayList.size();
    }
}