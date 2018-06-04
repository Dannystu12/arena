package game.sprites;

import engine.Screen;
import engine.sprite.Animator;
import engine.sprite.BufferedImageLoader;
import engine.sprite.SpriteSheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayerSprite {

    private Screen screen;
    private int x;
    private int y;
    private final int MOVE_AMOUNT = 2;
    private final String SPRITE_SHEET_PATH = "/sprites/hero/chara_hero.png";
    private Animator currentAnimation;

    private Animator IDLE, WALK_RIGHT, WALK_UP, WALK_DOWN, WALK_LEFT, ATTACK_UP, ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT;
    private ArrayList<Animator> dontInterrupt;

    public PlayerSprite(Screen screen, int x, int y){
        this.screen = screen;
        this.x = x;
        this.y = y;
        init();
        currentAnimation.start();
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
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

    public void onUpdate() {
    if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_UP)
                && !dontInterrupt.contains(currentAnimation)){
        if(currentAnimation != ATTACK_UP){
            currentAnimation.stop();
            currentAnimation = ATTACK_UP;
            currentAnimation.start();
        }
    } else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_DOWN)
                && !dontInterrupt.contains(currentAnimation)){
        if(currentAnimation != ATTACK_DOWN){
            currentAnimation.stop();
            currentAnimation = ATTACK_DOWN;
            currentAnimation.start();
        }
    }else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_LEFT)
                && !dontInterrupt.contains(currentAnimation)) {
        if (currentAnimation != ATTACK_LEFT) {
            currentAnimation.stop();
            currentAnimation = ATTACK_LEFT;
            currentAnimation.start();
        }
    }else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_RIGHT))

    {
        if (currentAnimation != ATTACK_RIGHT) {
            currentAnimation.stop();
            currentAnimation = ATTACK_RIGHT;
            currentAnimation.start();
        }
    }else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_A)
                && !dontInterrupt.contains(currentAnimation)){
            x -= MOVE_AMOUNT;
            if(currentAnimation != WALK_LEFT) {
                currentAnimation.stop();
                currentAnimation = WALK_LEFT;
                currentAnimation.start();
            }
        }else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_D)
                && !dontInterrupt.contains(currentAnimation)){
            x += MOVE_AMOUNT;
            if(currentAnimation != WALK_RIGHT){
                currentAnimation.stop();
                currentAnimation = WALK_RIGHT;
                currentAnimation.start();
            }
        }else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_W)
                && !dontInterrupt.contains(currentAnimation)){
            y -= MOVE_AMOUNT;
            if(currentAnimation != WALK_UP){
                currentAnimation.stop();
                currentAnimation = WALK_UP;
                currentAnimation.start();
            }
        } else if(screen.getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_S)
                && !dontInterrupt.contains(currentAnimation)){
            y += MOVE_AMOUNT;
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
    }

    public void onDraw(Graphics2D g2d) {
        currentAnimation.update(System.currentTimeMillis());
            g2d.drawImage(currentAnimation.getSprite(), x, y,
                    currentAnimation.getSprite().getWidth() * 2,
                    currentAnimation.getSprite().getHeight()* 2, null);
    }

}
