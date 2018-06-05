package game.sprites;

import engine.Screen;
import engine.sprite.Animator;
import engine.sprite.BufferedImageLoader;
import engine.sprite.SpriteSheet;
import game.ArenaScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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


    private Animator IDLE, WALK_RIGHT, WALK_UP, WALK_DOWN, WALK_LEFT, ATTACK_UP, ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT;
    private ArrayList<Animator> dontInterrupt;

    public SlimeSprite(Screen screen, int x, int y){
        super(screen, x, y);
        rng = new Random();
        lastAttack = System.currentTimeMillis();
        currentAnimation.start();
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

            //Create an array list in which certain animations cannot be interrupted
            dontInterrupt = new ArrayList<>();
            dontInterrupt.add(ATTACK_UP);
            dontInterrupt.add(ATTACK_DOWN);
            dontInterrupt.add(ATTACK_LEFT);
            dontInterrupt.add(ATTACK_RIGHT);

            currentAnimation = IDLE;
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void onUpdate(PlayerSprite player) {

        int dx = player.getX() - x;
        int dy = player.getY() - y;

        //Check if need to move closer
        if(Math.abs(dx) > this.currentAnimation.getSprite().getWidth() ||
                Math.abs(dy) > this.currentAnimation.getSprite().getHeight()){
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
                    }
                } else {
                    if(!dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = ATTACK_RIGHT;
                        currentAnimation.start();
                        lastAttack = System.currentTimeMillis();

                    }
                }
            } else {
                if(dy < 0){
                    if(!dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = ATTACK_UP;
                        currentAnimation.start();
                        lastAttack = System.currentTimeMillis();

                    }
                } else {
                    if(!dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = ATTACK_DOWN;
                        currentAnimation.start();
                        lastAttack = System.currentTimeMillis();

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



    }

    @Override
    public void onDraw(Graphics2D g2d) {
        currentAnimation.update(System.currentTimeMillis());
        g2d.drawImage(currentAnimation.getSprite(), x, y,
                currentAnimation.getSprite().getWidth() * SCALE_FACTOR,
                currentAnimation.getSprite().getHeight()* SCALE_FACTOR, null);
    }


}
