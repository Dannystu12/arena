import org.junit.Test;
import models.utils.Dice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DiceTest{

    @Test
    public void canRollD20(){
        int roll;
        for(int i = 0; i < 100; i++){
            roll = Dice.roll(20);
            assertTrue( roll <= 20);
            assertTrue( roll >= 1);
        }
    }

    @Test
    public void canHandleNegativeSides(){
        assertEquals(0, Dice.roll(0));
        assertEquals(0, Dice.roll(-5));
    }
}
