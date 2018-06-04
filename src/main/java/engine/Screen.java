package engine;

import java.awt.*;

public abstract class Screen {

    private final ScreenFactory screenFactory;

    public Screen(ScreenFactory screenFactory){
        this.screenFactory = screenFactory;
    }

    public abstract void onCreate();

    public abstract void onUpdate();

    public abstract void onDraw(Graphics2D g2d);

    public ScreenFactory getScreenFactory() {
        return screenFactory;
    }
}
