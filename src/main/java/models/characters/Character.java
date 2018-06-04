package models.characters;

import models.behaviours.Attackable;
import models.behaviours.IAttack;
import models.utils.Dice;

public abstract class Character implements IAttack, Attackable {

    protected int hp;
    protected final int MAX_HP;
    private int armourClass;
    private int damageRoll;

    public Character(int hp, int armourClass, int damageRoll){
        this.hp = hp;
        this.MAX_HP = hp;
        this.armourClass = armourClass;
        this.damageRoll = damageRoll;
    }

    public int getHp() {
        return hp;
    }

    public int getDamageRoll() {
        return damageRoll;
    }

    public int getMAX_HP() {
        return MAX_HP;
    }

    @Override
    public int getArmourClass() {
        return armourClass;
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    @Override
    public boolean isDead() {
        return !isAlive();
    }

    @Override
    public void takeDamage(int damage) {
        damage = Math.min(hp, damage);
        hp -= damage;

    }

    @Override
    public void attack(Attackable attackable) {
        int hitRoll = Dice.roll(20);
        if(hitRoll < attackable.getArmourClass()) return;

        int damage = getDamage();
        if(hitRoll == 20){
            damage += getDamage();
        }

        attackable.takeDamage(damage);
    }

    private int getDamage(){
        return Dice.roll(damageRoll);
    }
}
