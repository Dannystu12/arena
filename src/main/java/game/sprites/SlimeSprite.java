package game.sprites;

import engine.Screen;
import engine.sprite.Animator;
import engine.sprite.BufferedImageLoader;
import engine.sprite.SpriteSheet;
import game.ArenaScreen;
import game.sounds.SoundEffect;
import models.characters.enemies.Enemy;
import models.characters.enemies.Slime;
import models.characters.players.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class SlimeSprite extends Sprite implements Collidable{

    private final int MOVE_AMOUNT = 1;
    private final String SPRITE_SHEET_PATH = "/sprites/enemies/slime/chara_slime.png";
    private Animator currentAnimation;
    private Random rng;
    private final int TIME_BETWEEN_ATTACKS = 1000;
    private long lastAttack;
    private final int SCALE_FACTOR = 2;
    private Enemy enemy;


    private Animator IDLE, WALK_RIGHT, WALK_UP, WALK_DOWN, WALK_LEFT, ATTACK_UP, ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT,
        HIT_FROM_LEFT, HIT_FROM_RIGHT, HIT_FROM_ABOVE, HIT_FROM_BELOW;

    private ArrayList<Animator> dontInterrupt;

    public SlimeSprite(Screen screen, int x, int y){
        super(screen, x, y);
        rng = new Random();
        enemy = new Slime();
        lastAttack = System.currentTimeMillis();
        currentAnimation.start();
    }

    public int getCenterX(){
        return x + currentAnimation.getSprite().getWidth() * SCALE_FACTOR / 2;
    }

    public int getCenterY(){
        return y + currentAnimation.getSprite().getHeight() * SCALE_FACTOR / 2;
    }

    public Enemy getEnemy(){
        return enemy;
    }

    @Override
    public Rectangle getBounds(){
        //Gets inner 16 x 16 collision box
        int w = currentAnimation.getSprite().getWidth() * SCALE_FACTOR  /3;
        int h = currentAnimation.getSprite().getHeight() * SCALE_FACTOR /3;
        return new Rectangle(x + w,y + h, w, h);
    }

    public boolean checkCollisions(){
        ArrayList<Collidable> collidables= ((ArenaScreen) screen).getCollidables();
        for(Collidable collidable : collidables){
            if(collidable != this && getBounds().intersects(collidable.getBounds())){
                return true;
            }
        }
        return false;
    }

    protected void init(){
        try {
            BufferedImageLoader loader = new BufferedImageLoader();
            BufferedImage spritesheet = loader.loadImage(SPRITE_SHEET_PATH);
            SpriteSheet ss = new SpriteSheet(spritesheet);
            IDLE = getAnimator(ss, 0, 3,  48,48, 300, false, true);
            WALK_RIGHT = getAnimator(ss, 3, 4,  48,48, 100, false, true);
            WALK_DOWN = getAnimator(ss, 2, 4,  48, 48, 100, false, true);
            WALK_UP = getAnimator(ss, 4, 4,  48, 48,100, false, true);
            WALK_LEFT = getAnimator(ss, 3, 4,  48, 48,100, true, true);
            ATTACK_UP = getAnimator(ss, 7, 4,  48, 48,100, false, false);
            ATTACK_DOWN = getAnimator(ss, 5, 4,  48, 48,100, false, false);
            ATTACK_LEFT = getAnimator(ss, 6, 4,  48, 48,100, true, false);
            ATTACK_RIGHT = getAnimator(ss, 6, 4, 48, 48,100, false, false);
            HIT_FROM_LEFT = getAnimator(ss, 9, 4, 48, 48,100, true, false);
            HIT_FROM_RIGHT = getAnimator(ss, 9, 4, 48, 48,100, false, false);
            HIT_FROM_ABOVE = getAnimator(ss, 10, 4, 48, 48,100, false, false);
            HIT_FROM_BELOW = getAnimator(ss, 8, 4, 48, 48,100, false, false);



            //Create an array list in which certain animations cannot be interrupted
            dontInterrupt = new ArrayList<>();
            dontInterrupt.add(ATTACK_UP);
            dontInterrupt.add(ATTACK_DOWN);
            dontInterrupt.add(ATTACK_LEFT);
            dontInterrupt.add(ATTACK_RIGHT);
            dontInterrupt.add(HIT_FROM_LEFT);
            dontInterrupt.add(HIT_FROM_RIGHT);
            dontInterrupt.add(HIT_FROM_ABOVE);
            dontInterrupt.add(HIT_FROM_BELOW);

            currentAnimation = IDLE;
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void attack(Rectangle attackBox, Direction direction){
        PlayerSprite ps = ((ArenaScreen) screen).getPlayer();
        if(attackBox.intersects(ps.getBounds())) {
            Player p = ps.getPlayer();
            int healthBefore = p.getHp();
            enemy.attack(p);
            int healthAfter = p.getHp();
            if (healthBefore > healthAfter) {
                ps.takeHit(direction);
                ((ArenaScreen) screen).addDamagePopup(
                        new DamagePopup(healthBefore - healthAfter,
                                ps.getCenterX(),
                                ps.getCenterY(), p.lastAttackWasCrit()));
                SoundEffect.SLIME_HIT.play();
            } else {
                SoundEffect.SLIME_MISS.play();
            }

        }
    }

    public void takeHit(Direction d){
        currentAnimation.stop();
        switch(d){
            case UP:
                currentAnimation = HIT_FROM_BELOW;
                break;
            case DOWN:
                currentAnimation = HIT_FROM_ABOVE;
                break;
            case LEFT:
                currentAnimation = HIT_FROM_RIGHT;
                break;
            case RIGHT:
                currentAnimation = HIT_FROM_LEFT;
                break;
        }

        currentAnimation.start();


    }


    public void onUpdate(PlayerSprite player) {

        int dx = player.getX() - x;
        int dy = player.getY() - y;

        //Check if need to move closer
        if(Math.abs(dx) >= currentAnimation.getSprite().getWidth() / 3 * SCALE_FACTOR + 5||
                Math.abs(dy) >= currentAnimation.getSprite().getWidth() / 3 * SCALE_FACTOR + 5){
            if(Math.abs(dx) >= Math.abs(dy)){
                if(dx < 0){
                    if(currentAnimation != WALK_LEFT && !dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = WALK_LEFT;
                        currentAnimation.start();
                    }
                    x -= MOVE_AMOUNT;
                    if(checkCollisions()){
                        x += MOVE_AMOUNT;
                    }
                } else {
                    if(currentAnimation != WALK_RIGHT && !dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = WALK_RIGHT;
                        currentAnimation.start();
                    }
                    x += MOVE_AMOUNT;
                    if(checkCollisions()){
                        x -= MOVE_AMOUNT;
                    }
                }
            } else {
                if(dy < 0){
                    if(currentAnimation != WALK_UP && !dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = WALK_UP;
                        currentAnimation.start();
                    }
                    y -= MOVE_AMOUNT;
                    if(checkCollisions()){
                        y += MOVE_AMOUNT;
                    }
                } else {
                    if(currentAnimation != WALK_DOWN && !dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = WALK_DOWN;
                        currentAnimation.start();
                    }
                    y += MOVE_AMOUNT;
                    if(checkCollisions()){
                        y -= MOVE_AMOUNT;
                    }
                }

            }

        } else if(System.currentTimeMillis() - lastAttack >= TIME_BETWEEN_ATTACKS){
            //ATTACK
            if(Math.abs(dx) >= Math.abs(dy)){
                if(dx < 0){
                    if(!dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = ATTACK_LEFT;
                        currentAnimation.start();
                        lastAttack = System.currentTimeMillis();
                        attack(new Rectangle(x, y + 16 * SCALE_FACTOR, 16 * SCALE_FACTOR, 16 * SCALE_FACTOR), Direction.LEFT);
                    }
                } else {
                    if(!dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = ATTACK_RIGHT;
                        currentAnimation.start();
                        lastAttack = System.currentTimeMillis();
                        attack(new Rectangle(x + 16 * SCALE_FACTOR * 2, y + 16 * SCALE_FACTOR, 16 * SCALE_FACTOR, 16 * SCALE_FACTOR), Direction.RIGHT);
                    }
                }
            } else {
                if(dy < 0){
                    if(!dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = ATTACK_UP;
                        currentAnimation.start();
                        lastAttack = System.currentTimeMillis();
                        attack(new Rectangle(x + 16 * SCALE_FACTOR, y, 16 * SCALE_FACTOR, 16 * SCALE_FACTOR), Direction.UP);
                    }
                } else {
                    if(!dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = ATTACK_DOWN;
                        currentAnimation.start();
                        lastAttack = System.currentTimeMillis();
                        attack(new Rectangle(x + 16 * SCALE_FACTOR, y + 16 * SCALE_FACTOR * 2, 16 * SCALE_FACTOR, 16 * SCALE_FACTOR), Direction.DOWN);
                    }
                }

            }

        } else {
            if(!dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                currentAnimation.stop();
                currentAnimation = IDLE;
                currentAnimation.start();
            }

        }

        currentAnimation.update(System.currentTimeMillis());

    }

    @Override
    public void onDraw(Graphics2D g2d) {
        g2d.drawImage(currentAnimation.getSprite(), x, y,
                currentAnimation.getSprite().getWidth() * SCALE_FACTOR,
                currentAnimation.getSprite().getHeight()* SCALE_FACTOR, null);
    }


    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }
}
