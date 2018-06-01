import Enemies.Enemy;
import Enemies.Goblin;
import helpers.TestEnemy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EnemyTest {
    Enemy enemy;

    @Before
    public void setup(){
        enemy = new Goblin();
    }

    @Test
    public void hasHp(){
        assertTrue(enemy.getHp() >= 2);
        assertTrue(enemy.getHp() <= 12);
    }

    @Test
    public void hasArmourClass(){
        assertEquals(10, enemy.getArmourClass());
    }

    @Test
    public void hasDamageRoll(){
        assertEquals(6, enemy.getDamageRoll());
    }

    @Test
    public void canTakeDamage(){
        int hp = enemy.getHp();
        enemy.takeDamage(1);
        assertEquals(hp - 1, enemy.getHp());
    }

    @Test
    public void cantTakeMoreDamageThanHealth(){
        enemy.takeDamage(10000);
        assertEquals(0, enemy.getHp());
    }

    @Test
    public void isAliveTrue(){
        assertTrue(enemy.isAlive());
    }

    @Test
    public void isAliveFalse(){
        enemy.takeDamage(10000);
        assertFalse(enemy.isAlive());
    }

    @Test
    public void isDeadFalse(){
        enemy.takeDamage(10000);
        assertTrue(enemy.isDead());
    }

    @Test
    public void isDeadTrue(){
        assertFalse(enemy.isDead());
    }

    @Test
    public void canAttack(){
        TestEnemy targetDummy = new TestEnemy(100, 0, 6);
        enemy.attack(targetDummy);
        assertTrue(100 - targetDummy.getHp() >= 1);
        assertTrue(100 - targetDummy.getHp() <= 12);
    }

    @Test
    public void canMiss(){
        boolean hasMissed = false;
        TestEnemy targetDummy = new TestEnemy(100, 15, 6);
        int hpBefore = targetDummy.getHp();

        for(int i = 0; i < 100; i++){
            enemy.attack(targetDummy);
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
            enemy.attack(targetDummy);
            if(hpBefore - targetDummy.getHp()  >= 7){
                hasCrit = true;
                break;
            }
            targetDummy.restore(100);
        }

        assertTrue(hasCrit);
    }

}
