import models.characters.enemies.Slime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SlimeTest {
    Slime slime;

    @Before
    public void setup(){
        slime = new Slime();
    }

    @Test
    public void hasCorrectHp(){
        assertTrue(slime.getHp() <= 8);
        assertTrue(slime.getHp() >= 1);
    }

    @Test
    public void hasCorrectAc(){
        assertEquals(8, slime.getArmourClass());
    }

    @Test
    public void hasCorrectDamageRoll(){
        assertEquals(6, slime.getDamageRoll());
    }

}
