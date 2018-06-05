package game;

import engine.Screen;
import engine.ScreenFactory;
import game.environments.Wall;
import game.sprites.Collidable;
import game.sprites.PlayerSprite;
import game.sprites.SlimeSprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class ArenaScreen extends Screen {

    private int x = 0, y = 0;
    private final PlayerSprite player;
    private ArrayList<SlimeSprite> enemies;
    private ArrayList<Collidable> collidables;
    private Random rng;
    private int enemyCount = 2;
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;

    public ArenaScreen(ScreenFactory screenFactory) {
        super(screenFactory);
        rng = new Random();
        player = new PlayerSprite(this, getSpawnX(), getSpawnY());
        enemies = new ArrayList<>();
        collidables = new ArrayList<>();
        collidables.add(player);
        for(int i = 0; i < enemyCount; i++){
            addEnemy(new SlimeSprite(this, getSpawnX(), getSpawnY()));
        }
        addWalls();
    }

    private int getSpawnX(){
        return 100 + rng.nextInt(SCREEN_WIDTH - 200);
    }

    private int getSpawnY(){
        return 100 + rng.nextInt(SCREEN_HEIGHT - 200);
    }

    private void addWalls(){
        int w = 64;
        int h = SCREEN_HEIGHT;
        addCollidable(new Wall(-w, 0, w, h));
        addCollidable(new Wall(SCREEN_WIDTH, 0, w, h));

        w = SCREEN_WIDTH;
        h = 64;

        addCollidable(new Wall(0, -h, w, h));
        addCollidable(new Wall(0, SCREEN_HEIGHT - 20, w, h));
    }

    private void addCollidable(Collidable c){
        collidables.add(c);
    }

    private void addEnemy(SlimeSprite slimeSprite) {
        enemies.add(slimeSprite);
        collidables.add(slimeSprite);
    }

    public ArrayList<SlimeSprite> getEnemies() {
        return enemies;
    }

    public ArrayList<Collidable> getCollidables(){
        return collidables;
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
