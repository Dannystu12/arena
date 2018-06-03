import characters.players.Player;
import characters.players.Warrior;
import enums.Consumable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest {
    Player player;

    @Before
    public void setup(){
        player = new Warrior();
    }

    @Test
    public void canHeal(){
        player.takeDamage(11);
        assertEquals(1, player.getHp());
        player.takeConsumable(Consumable.HEALTH_POTION);
        assertTrue(player.getHp() >= 5);
        assertTrue(player.getHp() <= 11);
    }

    @Test
    public void cantHealMoreThanMaxHp(){
        player.takeDamage(1);
        assertEquals(11, player.getHp());
        player.takeConsumable(Consumable.HEALTH_POTION);
        assertEquals(player.getMAX_HP(), player.getHp());
    }

}
