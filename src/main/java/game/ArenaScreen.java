package game;

import engine.Screen;
import engine.ScreenFactory;
import game.environments.Wall;
import game.sprites.Collidable;
import game.sprites.DamagePopup;
import game.sprites.PlayerSprite;
import game.sprites.SlimeSprite;
import models.characters.enemies.Slime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;

public class ArenaScreen extends Screen {

    private int x = 0, y = 0;
    private final PlayerSprite player;
    private ArrayList<SlimeSprite> enemies;
    private ArrayList<Collidable> collidables;
    private ArrayList<DamagePopup> damagePopups;
    private Random rng;
    private int enemyCount = 2;
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;

    public ArenaScreen(ScreenFactory screenFactory) {
        super(screenFactory);
        rng = new Random();
        player = new PlayerSprite(this, getSpawnX(), getSpawnY());
        damagePopups = new ArrayList<>();
        enemies = new ArrayList<>();
        collidables = new ArrayList<>();
        collidables.add(player);
        for(int i = 0; i < enemyCount; i++){
            addEnemy(new SlimeSprite(this, getSpawnX(), getSpawnY()));
        }
        addWalls();
    }

    public void addDamagePopup(DamagePopup dp){
        damagePopups.add(dp);
    }

    public void removeDamagePopup(DamagePopup dp){
        damagePopups.remove(dp);
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

    public PlayerSprite getPlayer() {
        return player;
    }

    public ArrayList<Collidable> getCollidables(){
        return collidables;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onUpdate() {
        Iterator itr = damagePopups.iterator();
        while(itr.hasNext()){
            ((DamagePopup) itr.next()).onUpdate();
        }

        player.onUpdate();

        itr = enemies.listIterator();
        while(itr.hasNext()){
            SlimeSprite slime = (SlimeSprite) itr.next();
            if(slime.getEnemy().isDead()){
                itr.remove();
            } else {
                slime.onUpdate(player);
            }
            
        }
    }

    @Override
    public void onDraw(Graphics2D g2d) {
        for(SlimeSprite slime : enemies){
            slime.onDraw(g2d);
        }
        player.onDraw(g2d);

        ListIterator itr = damagePopups.listIterator();
        while(itr.hasNext()){
            DamagePopup dp = ((DamagePopup) itr.next());
            if(dp.isComplete()){
                itr.remove();
            }else{
                dp.onDraw(g2d);
            }
        }
    }
}
