package game.sprites;

import engine.Screen;
import engine.sprite.Animator;
import engine.sprite.BufferedImageLoader;
import engine.sprite.SpriteSheet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class SlimeSprite {

    private Screen screen;
    private int x;
    private int y;
    private final int MOVE_AMOUNT = 1;
    private final String SPRITE_SHEET_PATH = "/sprites/enemies/slime/chara_slime.png";
    private Animator currentAnimation;
    private Random rng;
    private final int TIME_BETWEEN_ATTACKS = 1000;
    private long lastAttack;

    private Animator IDLE, WALK_RIGHT, WALK_UP, WALK_DOWN, WALK_LEFT, ATTACK_UP, ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT;
    private ArrayList<Animator> dontInterrupt;

    public SlimeSprite(Screen screen, int x, int y){
        this.screen = screen;
        this.x = x;
        this.y = y;
        init();
        rng = new Random();
        lastAttack = System.currentTimeMillis();
        currentAnimation.start();
    }

    private void init(){
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

    private Animator getAnimator(SpriteSheet ss, int row, int numImages, int width, int height, long speed, boolean flip,
                                 boolean loop){
        ArrayList<BufferedImage> frames = new ArrayList<>();


        for(int i = 0; i < numImages; i++){
            int x = i * width ;
            int y = row * height;
            BufferedImage sprite = ss.grabSprite(x, y, width, height);
            if(flip){
                AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-sprite.getWidth(null), 0);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                sprite = op.filter(sprite, null);
            }
            frames.add(sprite);
        }

        return new Animator(frames, speed, loop);
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
                } else {
                    if(currentAnimation != WALK_RIGHT && !dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = WALK_RIGHT;
                        currentAnimation.start();
                    }
                    x += MOVE_AMOUNT;
                }
            } else {
                if(dy < 0){
                    if(currentAnimation != WALK_UP && !dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = WALK_UP;
                        currentAnimation.start();
                    }
                    y -= MOVE_AMOUNT;
                } else {
                    if(currentAnimation != WALK_DOWN && !dontInterrupt.contains(currentAnimation) || !currentAnimation.isRunning()){
                        currentAnimation.stop();
                        currentAnimation = WALK_DOWN;
                        currentAnimation.start();
                    }
                    y += MOVE_AMOUNT;
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

    public void onDraw(Graphics2D g2d) {
        currentAnimation.update(System.currentTimeMillis());
        g2d.drawImage(currentAnimation.getSprite(), x, y,
                currentAnimation.getSprite().getWidth() * 2,
                currentAnimation.getSprite().getHeight()* 2, null);
    }


}
