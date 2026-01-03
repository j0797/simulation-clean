package entities.creatures;


public class Herbivore extends Creature {
    public Herbivore() {
        super(1, 20, 10);
    }

    public Herbivore(int speed, int maxHunger, int healthpoints) {
        super(speed, maxHunger, healthpoints);
    }
}
