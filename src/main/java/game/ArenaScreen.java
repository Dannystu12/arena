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
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.*;

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
    private static final Font font = new Font("Helvetica", Font.BOLD, 12);

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
        if(player.getPlayer().isDead()) return;

        for(int i = 0; i < damagePopups.size(); i++){
            DamagePopup d = damagePopups.get(i);
            d.onUpdate();
        }

        player.onUpdate();


        for(int i = 0; i < enemies.size(); i++){
            SlimeSprite slime = enemies.get(i);
            if(slime.getEnemy().isDead()){
                enemies.set(i, null);
            } else {
                slime.onUpdate(player);
            }
        }

        //cleanup null references in enemies
        enemies.removeIf(Objects::isNull);

        //Cleanup collidables for deleted enemies
        for(int i = 0; i < collidables.size(); i++){
            Collidable c = collidables.get(i);
            if(c instanceof SlimeSprite && !enemies.contains(c)){
                collidables.set(i, null);
            }
        }

        //cleanup null references in collidables
        collidables.removeIf(Objects::isNull);

    }

    @Override
    public void onDraw(Graphics2D g2d) {
        for(SlimeSprite slime : enemies){
            slime.onDraw(g2d);
        }
        player.onDraw(g2d);

        for(int i = 0; i < damagePopups.size(); i++){
            DamagePopup dp = damagePopups.get(i);
            if(dp.isComplete()){
                damagePopups.set(i, null);
            } else {
                dp.onDraw(g2d);
            }
        }

        //cleanup null references in popups
        damagePopups.removeIf(Objects::isNull);


        if(player.getPlayer().isDead()){
            drawCenteredString(g2d, "Game Over", new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT),
                    font);
        }
    }

    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

}
