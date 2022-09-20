package Pattern.Strategy;

//實作深色模式工廠
public class DarkModeFactory implements ModeFactory{

    @Override
    public ModeStrategy CreateBackgroundMode(){
        return new DarkMode();
    }
}
