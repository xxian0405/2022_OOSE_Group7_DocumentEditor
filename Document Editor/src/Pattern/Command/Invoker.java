package Pattern.Command;

// Command觸發者，設置並根據current觸發命令
public class Invoker {
    private Command command;
    public void SetCommand(Command command){
        this.command = command;
    }

    public void InvokeCommand(int current){
        command.execute(current);
    }
}
