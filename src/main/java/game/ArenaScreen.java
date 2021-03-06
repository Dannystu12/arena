package game;

import engine.Screen;
import engine.ScreenFactory;
import engine.sprite.BufferedImageLoader;
import game.environments.Wall;
import game.sounds.Announcer;
import game.sounds.SoundEffect;
import game.sprites.*;
import game.sprites.enemies.*;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;

public class ArenaScreen extends Screen {

    private int x = 0, y = 0;
    private final PlayerSprite player;
    private ArrayList<EnemySprite> enemies;
    private ArrayList<Collidable> collidables;
    private ArrayList<DamagePopup> damagePopups;
    private ArrayList<Pickupable> pickups;
    private Random rng;
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 580;
    private long lastSpawn;
    private int spawnDelay = 4500;
    private final int SPAWN_REDUCTION = 100;
    private final int SPAWN_MIN = 1000;
    private int killCount;
    private static Font font;
    private static Font hudFont;
    private BufferedImage background;
    private boolean paused;
    private boolean spaceReleased;
    private boolean gameOver;
    private ArrayList<Class> enemyTypes;
    private int maxEnemyCount = 5;


    public ArenaScreen(ScreenFactory screenFactory) {
        super(screenFactory);

        rng = new Random();
        player = new PlayerSprite(this, getSpawnX(), getSpawnY());
        damagePopups = new ArrayList<>();
        enemies = new ArrayList<>();
        pickups = new ArrayList<>();
        collidables = new ArrayList<>();
        enemyTypes = new ArrayList<>();
        enemyTypes.add(RatSprite.class);
        collidables.add(player);
        lastSpawn = System.currentTimeMillis();

        addWalls();

        try {
            //Load background
            background = new BufferedImageLoader().loadImage("/environments/Arena Map.png");
            //Get font
            InputStream is = this.getClass().getResourceAsStream("/fonts/Minercraftory.ttf");
            Font fontBase = Font.createFont(Font.TRUETYPE_FONT, is);
            font = fontBase.deriveFont(Font.PLAIN,32);
            hudFont = fontBase.deriveFont(Font.PLAIN,20);
            //Set font for damage popup
            DamagePopup.setFont(fontBase.deriveFont(Font.PLAIN, 12));
            DamagePopup.setCritFont(fontBase.deriveFont(Font.PLAIN, 20));
        } catch (Exception e){
            e.printStackTrace();
        }

        //Load sound effects
        SoundEffect.init();

        startBackgroundMusic("/music/bgm.wav");
        SoundEffect.FIGHT.play();
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
        addCollidable(new Wall(-w + 32, 0, w, h));
        addCollidable(new Wall(SCREEN_WIDTH - 32, 0, w, h));

        w = SCREEN_WIDTH;
        h = 64;

        addCollidable(new Wall(0, -h + 16, w, h));
        addCollidable(new Wall(0, SCREEN_HEIGHT - 36, w, h));
    }

    private void addCollidable(Collidable c){
        collidables.add(c);
    }

    private void addEnemy(EnemySprite enemySprite) {
        enemies.add(enemySprite);
        collidables.add(enemySprite);
    }

    public ArrayList<EnemySprite> getEnemies() {
        return enemies;
    }

    public PlayerSprite getPlayer() {
        return player;
    }

    public ArrayList<Collidable> getCollidables(){
        return collidables;
    }

    public ArrayList<Pickupable> getPickups(){
        return pickups;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onUpdate() {

        // Dont update if player is dead
        if(player.getPlayer().isDead()){
            if(!gameOver){
                gameOver = true;
                SoundEffect.LAUGH.play();
            }
            return;
        }

        // Dont update if game is paused
        if(getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_SPACE)
                && spaceReleased){
            spaceReleased = false;
            paused = !paused;
        } else if(getScreenFactory().getGame().getKeyboardListener().isKeyReleased(KeyEvent.VK_SPACE)){
            spaceReleased = true;
        }


        if(paused) return;

        for(int i = 0; i < pickups.size(); i++){
            if(pickups.get(i) != null) ((HealthPotionSprite) pickups.get(i)).onUpdate();
        }

        //Spawn enemies if appropriate
        if(enemies.isEmpty() || System.currentTimeMillis() - lastSpawn >= spawnDelay && enemies.size() < maxEnemyCount){
            Class enemyClass = enemyTypes.get(rng.nextInt(enemyTypes.size()));
            EnemySprite enemy = null;
            try {
                enemy = (EnemySprite) enemyClass.getDeclaredConstructor(Screen.class, int.class, int.class).newInstance(this, getSpawnX(), getSpawnY());
            }catch (Exception e){
                e.printStackTrace();
            }
            addEnemy(enemy);
            while(enemy.checkCollisions()){
                enemy.setX(getSpawnX());
                enemy.setY(getSpawnY());
            }

            lastSpawn = System.currentTimeMillis();
            reduceSpawnDelay();
        }

        for(int i = 0; i < damagePopups.size(); i++){
            DamagePopup d = damagePopups.get(i);
            d.onUpdate();
        }

        player.onUpdate();
         // remove consumables taken
        pickups.removeIf(Objects::isNull);


        for(int i = 0; i < enemies.size(); i++){
            EnemySprite enemy = enemies.get(i);
            if(enemy.isDead()){
                enemies.set(i, null);
                killCount += 1;

                //Add new enemies after certain number of kills
                if(killCount == 5){
                    enemyTypes.add(BatSprite.class);
                } else if(killCount == 10) {
                    enemyTypes.add(SpiderSprite.class);
                } else if (killCount == 20){
                    enemyTypes.add(SlimeSprite.class);
                } else if (killCount == 50){
                    enemyTypes.add(GoblinSprite.class);
                } else if (killCount == 75){
                    enemyTypes.add(OrcSprite.class);
                } else if(killCount == 100){
                    enemyTypes.add(TrollSprite.class);
                }


                // Create potion
                if(rng.nextInt(11) == 10){
                    pickups.add(new HealthPotionSprite(this,enemy.getCenterX() - 8, enemy.getCenterY() - 8));
                }

                // Play announcer
                if(rng.nextInt(11) == 10){
                    Announcer.values()[rng.nextInt(Announcer.values().length)].play();
                }

            } else {
                enemy.onUpdate(player);
            }
        }

        //cleanup null references in enemies
        enemies.removeIf(Objects::isNull);

        //Cleanup collidables for deleted enemies
        for(int i = 0; i < collidables.size(); i++){
            Collidable c = collidables.get(i);
            if(c instanceof EnemySprite && !enemies.contains(c)){
                collidables.set(i, null);
            }
        }

        //cleanup null references in collidables
        collidables.removeIf(Objects::isNull);

        //adjust max enemy count
        maxEnemyCount = Math.min(Math.max(killCount / 10, maxEnemyCount), 20);

    }

    @Override
    public void onDraw(Graphics2D g2d) {
        g2d.drawImage(background, 0,0,null);

        for(int i = 0; i < pickups.size(); i++){
            if(pickups.get(i) != null) ((Sprite) pickups.get(i)).onDraw(g2d);
        }

        for(int i = 0; i < enemies.size(); i++){
            EnemySprite enemy =  enemies.get(i);
            if(enemy != null) enemy.onDraw(g2d);
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

        drawHealth(g2d);
        drawKillCount(g2d);


        if(player.getPlayer().isDead()){
            drawCenteredString(g2d, "Game Over", new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT),
                    font);
        }

        if(paused && player.getPlayer().isAlive()){
            drawCenteredString(g2d, "Paused", new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT),
                    font);
        }

    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setColor(Color.decode("#ffffff"));
        g.setFont(font);
        g.drawString(text, x, y);
    }

    private void drawKillCount(Graphics g){
        String text = String.format("Kills % 3d", killCount);
        FontMetrics metrics = g.getFontMetrics(hudFont);

        //Create rectangle to make easier to read
        Color c = Color.decode("#8c1d04");
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
        g.fillRect(SCREEN_WIDTH - metrics.stringWidth(text) - 20, 0, metrics.stringWidth(text) + 8,
                metrics.getHeight());

        g.setFont(hudFont);
        g.setColor(Color.decode("#ffffff"));
        g.drawString(text, SCREEN_WIDTH - metrics.stringWidth(text) - 16, 24);
    }

    private void drawHealth(Graphics g){
        double healthPct = player.getPlayer().getHp() / ((double) player.getPlayer().getMAX_HP()) * 100;
        String text = String.format("Health % 3.0f", healthPct);
        FontMetrics metrics = g.getFontMetrics(hudFont);

        //Create rectangle to make easier to read
        Color c = Color.decode("#8c1d04");
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
        g.fillRect(12, 0, metrics.stringWidth(text) + 8,
                metrics.getHeight());


        g.setFont(hudFont);
        g.setColor(Color.decode("#ffffff"));
        g.drawString(text, 16, 24);
    }

    private void startBackgroundMusic(String path){
        ContinuousAudioDataStream loop;
        try {
            AudioStream s = new AudioStream(getClass().getResourceAsStream(path));
            AudioData audiodata = s.getData();
            loop = new ContinuousAudioDataStream(audiodata);
            AudioPlayer.player.start(loop);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void reduceSpawnDelay(){
        spawnDelay -= SPAWN_REDUCTION;
        spawnDelay = Math.max(spawnDelay, SPAWN_MIN);
    }

}
