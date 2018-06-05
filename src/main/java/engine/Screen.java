package engine;

import game.sprites.SlimeSprite;

import java.awt.*;
import java.util.ArrayList;

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
