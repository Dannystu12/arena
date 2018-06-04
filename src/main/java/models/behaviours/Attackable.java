package models.behaviours;

public interface Attackable {
    int getArmourClass();
    boolean isAlive();
    boolean isDead();
    void takeDamage(int damage);
}
