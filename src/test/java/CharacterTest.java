import characters.Character;
import characters.enemies.Goblin;
import helpers.TestEnemy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterTest {

    Character character;

    @Before
    public void setup(){
        character = new Goblin();
    }

    @Test
    public void hasHp(){
        assertTrue(character.getHp() >= 2);
        assertTrue(character.getHp() <= 12);
    }

    @Test
    public void hasMaxHp(){
        assertTrue(character.getMAX_HP() >= 2);
        assertTrue(character.getMAX_HP() <= 12);
        assertEquals(character.getHp(), character.getMAX_HP());
        character.takeDamage(1);
        assertTrue(character.getMAX_HP() - character.getHp() == 1);
    }

    @Test
    public void hasArmourClass(){
        assertEquals(10, character.getArmourClass());
    }

    @Test
    public void hasDamageRoll(){
        assertEquals(6, character.getDamageRoll());
    }

    @Test
    public void canTakeDamage(){
        int hp = character.getHp();
        character.takeDamage(1);
        assertEquals(hp - 1, character.getHp());
    }

    @Test
    public void cantTakeMoreDamageThanHealth(){
        character.takeDamage(10000);
        assertEquals(0, character.getHp());
    }

    @Test
    public void isAliveTrue(){
        assertTrue(character.isAlive());
    }

    @Test
    public void isAliveFalse(){
        character.takeDamage(10000);
        assertFalse(character.isAlive());
    }

    @Test
    public void isDeadFalse(){
        character.takeDamage(10000);
        assertTrue(character.isDead());
    }

    @Test
    public void isDeadTrue(){
        assertFalse(character.isDead());
    }

    @Test
    public void canAttack(){
        TestEnemy targetDummy = new TestEnemy(100, 0, 6);
        character.attack(targetDummy);
        assertTrue(100 - targetDummy.getHp() >= 1);
        assertTrue(100 - targetDummy.getHp() <= 12);
    }

    @Test
    public void canMiss(){
        boolean hasMissed = false;
        TestEnemy targetDummy = new TestEnemy(100, 15, 6);
        int hpBefore = targetDummy.getHp();

        for(int i = 0; i < 100; i++){
            character.attack(targetDummy);
            if(hpBefore - targetDummy.getHp()  == 0){
                hasMissed = true;
                break;
            }
            targetDummy.restore(100);
        }
        assertTrue(hasMissed);
    }

    @Test
    public void canCrit(){
        boolean hasCrit = false;
        TestEnemy targetDummy = new TestEnemy(100, 1, 6);
        int hpBefore = targetDummy.getHp();

        for(int i = 0; i < 100; i++){
            character.attack(targetDummy);
            if(hpBefore - targetDummy.getHp()  >= 7){
                hasCrit = true;
                break;
            }
            targetDummy.restore(100);
        }

        assertTrue(hasCrit);
    }
}
