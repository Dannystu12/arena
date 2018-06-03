import characters.enemies.Goblin;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GoblinTest {
    Goblin goblin;

    @Before
    public void setup(){
        goblin = new Goblin();
    }

    @Test
    public void hasCorrectHp(){
        assertTrue(goblin.getHp() <= 12);
        assertTrue(goblin.getHp() >= 2);
    }

    @Test
    public void hasCorrectAc(){
        assertEquals(10, goblin.getArmourClass());
    }

    @Test
    public void hasCorrectDamageRoll(){
        assertEquals(6, goblin.getDamageRoll());
    }

}
