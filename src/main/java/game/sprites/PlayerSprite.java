package game.sprites;

import engine.Screen;
import engine.sprite.Animator;
import engine.sprite.BufferedImageLoader;
import engine.sprite.SpriteSheet;
import game.ArenaScreen;
import game.sounds.SoundEffect;
import models.characters.enemies.Enemy;
import models.characters.players.Player;
import models.characters.players.Warrior;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayerSprite extends Sprite implements Collidable{

    private final int MOVE_AMOUNT = 2;
    private final String SPRITE_SHEET_PATH = "/sprites/hero/chara_hero.png";
    private final int SCALE_FACTOR = 2;
    private Animator currentAnimation;
    private Player player;
    private final int TIME_BETWEEN_ATTACKS = 750;
    private long lastAttack;

    private Animator IDLE, WALK_RIGHT, WALK_UP, WALK_DOWN, WALK_LEFT, ATTACK_UP, ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT,
            HIT_FROM_LEFT, HIT_FROM_RIGHT, HIT_FROM_ABOVE, HIT_FROM_BELOW, CONSUME;
    private ArrayList<Animator> dontInterrupt;

    public PlayerSprite(Screen screen, int x, int y){
        super(screen,x, y);
        player = new Warrior();
        currentAnimation.start();
        lastAttack = System.currentTimeMillis();
    }

    public Player getPlayer(){
        return player;
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

    public void checkPickups(){
        ArrayList<Pickupable> pickups= ((ArenaScreen) screen).getPickups();
        for(int i = 0; i < pickups.size(); i++){
            Pickupable pickup = pickups.get(i);
            if(getBounds().intersects(pickup.getBounds())){
                currentAnimation.stop();
                currentAnimation = CONSUME;
                currentAnimation.start();
                SoundEffect.TAKE_POTION.play();
                pickups.set(i, null);
            }
        }

    }

    public int getCenterX(){
        return x + currentAnimation.getSprite().getWidth() * SCALE_FACTOR / 2;
    }

    public int getCenterY(){
        return y + currentAnimation.getSprite().getHeight() * SCALE_FACTOR / 2;
    }

    public void attack(Rectangle attackBox, Direction direction){
        boolean hasHit = false;
        boolean hasKilled = false;
        for(SlimeSprite enemy : ((ArenaScreen) screen).getEnemies()){
            if(attackBox.intersects(enemy.getBounds())){
                Enemy e = enemy.getEnemy();
                int healthBefore = e.getHp();
                player.attack(enemy.getEnemy());
                int healthAfter = e.getHp();
                if(healthBefore > healthAfter){
                    enemy.takeHit(direction);
                    ((ArenaScreen) screen).addDamagePopup(
                            new DamagePopup(healthBefore - healthAfter,
                                    enemy.getCenterX(),
                                    enemy.getCenterY(), e.lastAttackWasCrit()));
                    hasHit = true;
                    if(e.isDead()) hasKilled =  true;
                }
            }
        }

        if(hasKilled){
            SoundEffect.PUFF_OF_SMOKE.play();
        } else if(hasHit){
            SoundEffect.HERO_HIT.play();
        } else {
            SoundEffect.HERO_MISS.play();
        }
    }

    @Override
    public Rectangle getBounds(){
        //Gets inner 16 x 16 collision box
        int w = currentAnimation.getSprite().getWidth() * SCALE_FACTOR / 3;
        int h = currentAnimation.getSprite().getHeight() * SCALE_FACTOR / 3;
        return new Rectangle(x + w,y + h, w, h);
    }

    @Override
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
            CONSUME = getAnimator(ss, 1, 3, 48, 48,100, false, false);


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
            dontInterrupt.add(CONSUME);

            currentAnimation = IDLE;
        }catch (Exception e){
            e.printStackTrace();
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

    public void onUpdate() {

        checkPickups();
        if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_UP)
                    && !dontInterrupt.contains(currentAnimation)){
            if(currentAnimation != ATTACK_UP && canAttack()){
                lastAttack = System.currentTimeMillis();
                currentAnimation.stop();
                currentAnimation = ATTACK_UP;
                currentAnimation.start();
                attack(new Rectangle(x + 16 * SCALE_FACTOR, y, 16 * SCALE_FACTOR, 16 * SCALE_FACTOR), Direction.UP);
            }
        } else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_DOWN)
                    && !dontInterrupt.contains(currentAnimation)){
            if(currentAnimation != ATTACK_DOWN && canAttack()){
                lastAttack = System.currentTimeMillis();
                currentAnimation.stop();
                currentAnimation = ATTACK_DOWN;
                currentAnimation.start();
                attack(new Rectangle(x + 16 * SCALE_FACTOR, y + 16 * SCALE_FACTOR * 2, 16 * SCALE_FACTOR, 16 * SCALE_FACTOR), Direction.DOWN);
            }
        }else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_LEFT)
                    && !dontInterrupt.contains(currentAnimation)) {
            if (currentAnimation != ATTACK_LEFT && canAttack()) {
                lastAttack = System.currentTimeMillis();
                currentAnimation.stop();
                currentAnimation = ATTACK_LEFT;
                currentAnimation.start();
                attack(new Rectangle(x, y + 16 * SCALE_FACTOR, 16 * SCALE_FACTOR, 16 * SCALE_FACTOR), Direction.LEFT);
            }
        }else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (currentAnimation != ATTACK_RIGHT && canAttack()) {
                lastAttack = System.currentTimeMillis();
                currentAnimation.stop();
                currentAnimation = ATTACK_RIGHT;
                currentAnimation.start();
                attack(new Rectangle(x + 16 * SCALE_FACTOR * 2, y + 16 * SCALE_FACTOR, 16 * SCALE_FACTOR, 16 * SCALE_FACTOR), Direction.RIGHT);
            }
        }else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_A)
                    && !dontInterrupt.contains(currentAnimation)){
                x -= MOVE_AMOUNT;
                if(checkCollisions()){
                    x += MOVE_AMOUNT;
                }
                if(currentAnimation != WALK_LEFT) {
                    currentAnimation.stop();
                    currentAnimation = WALK_LEFT;
                    currentAnimation.start();
                }
            }else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_D)
                    && !dontInterrupt.contains(currentAnimation)){
                x += MOVE_AMOUNT;
                //Unwind movement if collision
                if(checkCollisions()){
                    x -= MOVE_AMOUNT;
                }

                if(currentAnimation != WALK_RIGHT){
                    currentAnimation.stop();
                    currentAnimation = WALK_RIGHT;
                    currentAnimation.start();
                }
            }else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_W)
                    && !dontInterrupt.contains(currentAnimation)){
                y -= MOVE_AMOUNT;
                if(checkCollisions()){
                    y += MOVE_AMOUNT;
                }
                if(currentAnimation != WALK_UP){
                    currentAnimation.stop();
                    currentAnimation = WALK_UP;
                    currentAnimation.start();
                }
            } else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_S)
                    && !dontInterrupt.contains(currentAnimation)){
                y += MOVE_AMOUNT;
                if(checkCollisions()){
                    y -= MOVE_AMOUNT;
                }
                if(currentAnimation != WALK_DOWN){
                    currentAnimation.stop();
                    currentAnimation = WALK_DOWN;
                    currentAnimation.start();
                }

            }else if(!dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()) {
                if(currentAnimation != IDLE){
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

    private boolean canAttack(){
        return System.currentTimeMillis() - lastAttack >= TIME_BETWEEN_ATTACKS;
    }

}
