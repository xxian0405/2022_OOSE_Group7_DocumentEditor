package Pattern.Command;

// 呼叫receiver執行undo command
public class UndoCommand implements Command{
    private Receiver receiver;

    public UndoCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(int current){
        receiver.Undo(current);
    }
}
