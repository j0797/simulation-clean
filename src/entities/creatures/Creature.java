package entities.creatures;

import entities.Entity;
import worldmap.WorldMap;


public abstract class Creature extends Entity {
    protected int healthPoints;
    protected int speed;
    protected int hunger;
    protected int maxHunger;

    public Creature(int speed, int maxHunger, int healthPoints) {
        this.speed = speed;
        this.maxHunger = maxHunger;
        this.hunger = 0;
        this.healthPoints = healthPoints;
    }

    public abstract void makeMove(WorldMap worldMap);

    public int getHunger() {
        return hunger;
    }

    public int getMaxHunger() {
        return maxHunger;
    }

    public void updateState() {
        hunger++;
        if (hunger > maxHunger) {
            healthPoints--;
        }
    }

    public void consumeFood() {
        hunger = 0;
        healthPoints++;
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }
}