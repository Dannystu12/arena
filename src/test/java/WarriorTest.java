import models.characters.players.Warrior;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WarriorTest {
    Warrior warrior;

    @Before
    public void setup(){
        warrior = new Warrior();
    }

    @Test
    public void hasCorrectHp(){
        assertEquals(12, warrior.getHp());
    }

    @Test
    public void hasCorrectAc(){
        assertEquals(16, warrior.getArmourClass());
    }

    @Test
    public void hasCorrectDamageRoll(){
        assertEquals(8, warrior.getDamageRoll());
    }
}
