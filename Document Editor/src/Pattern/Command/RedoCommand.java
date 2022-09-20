package Pattern.Command;

// 呼叫receiver執行redo command
public class RedoCommand implements Command{
    private Receiver receiver;

    public RedoCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(int current){
        receiver.Redo(current);
    }
}
