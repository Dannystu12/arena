package helpers;

import characters.enemies.Enemy;

public class TestEnemy extends Enemy {

    public TestEnemy(int hp, int armourClass, int damageRoll){
        super(hp, armourClass, damageRoll);
    }

    public void restore(int hp){
        this.hp = hp;
    }
}
