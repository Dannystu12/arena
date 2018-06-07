package game.sprites;

import cucumber.api.groovy.DE;
import engine.Screen;
import engine.sprite.Animator;
import engine.sprite.BufferedImageLoader;
import engine.sprite.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HealthPotionSprite extends Sprite implements Pickupable{
    private final int MOVE_AMOUNT = 2;
    private final String SPRITE_SHEET_PATH = "/sprites/powerups/health_potion.png";
    private final int SCALE_FACTOR = 2;
    private Animator currentAnimation;
    private static Animator DEFAULT;

    public HealthPotionSprite(Screen screen, int x, int y) {
        super(screen, x, y);
        init();
    }

    @Override
    protected void init() {
        try {
            BufferedImageLoader loader = new BufferedImageLoader();
            BufferedImage spritesheet = loader.loadImage(SPRITE_SHEET_PATH);
            SpriteSheet ss = new SpriteSheet(spritesheet);
            DEFAULT = getAnimator(ss, 0, 1,  16,16, 0, false, true);
            currentAnimation = DEFAULT;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDraw(Graphics2D g2d) {
        g2d.drawImage(currentAnimation.getSprite(), x, y,
                currentAnimation.getSprite().getWidth() * SCALE_FACTOR,
                currentAnimation.getSprite().getHeight()* SCALE_FACTOR, null);
    }

    @Override
    public Rectangle getBounds() {
        int w = currentAnimation.getSprite().getWidth() * SCALE_FACTOR;
        int h = currentAnimation.getSprite().getHeight() * SCALE_FACTOR;
        //Divide by 2 to let player choose to walk over potion
        return new Rectangle(x + w/2 ,y + h/2, w/2, h/2);
    }
}
