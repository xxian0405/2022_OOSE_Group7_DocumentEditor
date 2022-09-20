package Pattern.Strategy;

import javax.swing.*;

// 工廠方法介面
public interface ModeFactory {
    public ModeStrategy CreateBackgroundMode();
}
