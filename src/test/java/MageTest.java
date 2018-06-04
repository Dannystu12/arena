import models.characters.players.Mage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MageTest {
    Mage mage;

    @Before
    public void setup(){
        mage = new Mage();
    }

    @Test
    public void hasCorrectHp(){
        assertEquals(8, mage.getHp());
    }

    @Test
    public void hasCorrectAc(){
        assertEquals(12, mage.getArmourClass());
    }

    @Test
    public void hasCorrectDamageRoll(){
        assertEquals(10, mage.getDamageRoll());
    }
}
