package game.sprites.enemies;

import engine.Screen;
import game.ArenaScreen;
import game.sounds.SoundEffect;
import game.sprites.DamagePopup;
import game.sprites.Direction;
import game.sprites.PlayerSprite;
import models.characters.enemies.Slime;
import models.characters.enemies.Spider;
import models.characters.players.Player;

import java.awt.*;

public class SpiderSprite extends EnemySprite{

    public SpiderSprite(Screen screen, int x, int y){
        super(screen, x, y, new Spider(), "/sprites/enemies/chara_spider.png", SoundEffect.SPIDER_HIT);
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
