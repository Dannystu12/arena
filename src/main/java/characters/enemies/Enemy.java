package characters.enemies;

import characters.Character;

public abstract class Enemy extends Character{

    public Enemy(int hp, int armourClass, int damageRoll) {
        super(hp, armourClass, damageRoll);
    }
}
