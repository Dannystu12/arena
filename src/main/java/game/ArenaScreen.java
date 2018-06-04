package game;

import engine.Screen;
import engine.ScreenFactory;
import game.sprites.PlayerSprite;
import game.sprites.SlimeSprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ArenaScreen extends Screen {

    private int x = 0, y = 0;
    private final PlayerSprite player;
    private ArrayList<SlimeSprite> enemies;

    public ArenaScreen(ScreenFactory screenFactory) {
        super(screenFactory);
        player = new PlayerSprite(this, 0, 0);
        enemies = new ArrayList<>();
        SlimeSprite slime = new SlimeSprite(this, 100, 100);
        enemies.add(slime);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onUpdate() {
        player.onUpdate();
        for(SlimeSprite slime : enemies){
            slime.onUpdate(player);
        }
    }

    @Override
    public void onDraw(Graphics2D g2d) {
        for(SlimeSprite slime : enemies){
            slime.onDraw(g2d);
        }
        player.onDraw(g2d);
    }
}
