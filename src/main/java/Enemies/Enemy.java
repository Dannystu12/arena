package Enemies;

import behaviours.Attackable;
import behaviours.IAttack;
import utils.Dice;

public abstract class Enemy implements IAttack, Attackable{
    private int hp;
    private int armourClass;
    private int damageRoll;

    public Enemy(int hp, int armourClass, int damageRoll) {
        this.hp = hp;
        this.armourClass = armourClass;
        this.damageRoll = damageRoll;
    }

    public int getHp() {
        return hp;
    }

    public int getDamageRoll() {
        return damageRoll;
    }

    @Override
    public int getArmourClass() {
        return armourClass;
    }

    @Override
    public boolean isAlive(){
        return hp > 0;
    }

    @Override
    public boolean isDead(){
        return !isAlive();
    }

    @Override
    public void takeDamage(int damage){
        damage = Math.min(damage, hp);
        hp -= damage;
    }

    @Override
    public void attack(Attackable attackable){
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
