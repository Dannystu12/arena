package game.sprites;

import engine.Screen;
import engine.sprite.Animator;
import engine.sprite.SpriteSheet;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Sprite {
    protected int x, y;
    protected Screen screen;

    public Sprite(Screen screen, int x, int y){
        this.x = x;
        this.y = y;
        this.screen = screen;
    }

    protected abstract void init();

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Screen getScreen() {
        return screen;
    }


    protected Animator getAnimator(SpriteSheet ss, int row, int numImages, int width, int height, long speed, boolean flip,
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

    public abstract void onDraw(Graphics2D g2d);


}
