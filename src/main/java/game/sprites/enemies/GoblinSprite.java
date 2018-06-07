package game.sprites.enemies;

import engine.Screen;
import game.ArenaScreen;
import game.sounds.SoundEffect;
import game.sprites.*;
import models.characters.enemies.Goblin;
import models.characters.enemies.Slime;
import models.characters.players.Player;

import java.awt.*;

public class GoblinSprite extends EnemySprite{

    public GoblinSprite(Screen screen, int x, int y){
        super(screen, x, y, new Goblin(), "/sprites/enemies/chara_goblin.png", SoundEffect.GOBLIN_HIT);
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
                SoundEffect.GOBLIN_MISS.play();
            }

        }
    }
}
