package game.sprites;

import models.enums.Consumable;

import java.awt.*;

public interface Pickupable {
    Rectangle getBounds();
    Consumable getConsumable();
    boolean isActive();
}
