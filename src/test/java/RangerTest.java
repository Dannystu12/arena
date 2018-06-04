import models.characters.players.Ranger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RangerTest {
    Ranger ranger;

    @Before
    public void setup(){
        ranger = new Ranger();
    }

    @Test
    public void hasCorrectHp(){
        assertEquals(9, ranger.getHp());
    }

    @Test
    public void hasCorrectAc(){
        assertEquals(14, ranger.getArmourClass());
    }

    @Test
    public void hasCorrectDamageRoll(){
        assertEquals(6, ranger.getDamageRoll());
    }
}
