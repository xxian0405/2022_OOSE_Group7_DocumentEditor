package Pattern.Strategy;

// 實作淺色模式工廠
public class LightModeFactory implements ModeFactory{

    @Override
    public ModeStrategy CreateBackgroundMode(){
        return new LightMode();
    }
}
