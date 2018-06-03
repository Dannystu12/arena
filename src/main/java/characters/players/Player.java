package characters.players;

import characters.Character;
import enums.Consumable;


public abstract class Player extends Character{

    public Player(int hp, int armourClass, int damageRoll){
        super(hp, armourClass, damageRoll);
    }

    public void takeConsumable(Consumable consumable){
        switch (consumable){
            case HEALTH_POTION:
                hp += consumable.getEffect();
                hp = Math.min(hp, MAX_HP);
                break;
        }
    }

}
