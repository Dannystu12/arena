import enums.Consumable;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class TestConsumable {

    @Test
    public void canGetEffectFromHealthPotion(){
        int effect;
        for(int i = 0; i < 100; i++){
            effect = Consumable.HEALTH_POTION.getEffect();
            assertTrue(effect <= 10);
            assertTrue(effect >= 4);
        }
    }
}
