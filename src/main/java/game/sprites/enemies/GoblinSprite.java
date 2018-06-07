package game.sprites.enemies;

import engine.Screen;
import engine.sprite.Animator;
import engine.sprite.BufferedImageLoader;
import engine.sprite.SpriteSheet;
import game.ArenaScreen;
import game.sounds.SoundEffect;
import game.sprites.*;
import models.characters.enemies.Enemy;
import models.characters.enemies.Slime;
import models.characters.players.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GoblinSprite extends EnemySprite{

    public GoblinSprite(Screen screen, int x, int y){
        super(screen, x, y, new Slime(), "/sprites/enemies/goblin/chara_goblin.png");
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
                SoundEffect.ENEMY_HIT_PLAYER.play();
            } else {
                SoundEffect.SLIME_MISS.play();
            }

        }
    }
}
