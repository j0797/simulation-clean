package entities.creatures;

import entities.Entity;
import worldmap.Coordinates;
import worldmap.WorldMap;



public abstract class Creature extends Entity {
    private int speed;
    private int healthPoints;

    public Creature(int speed, int healthPoints) {
        this.speed = speed;
        this.healthPoints = healthPoints;
    }
    public abstract Coordinates makeMove(WorldMap worldMap, Coordinates currentPosition);

    public int getSpeed() {
        return speed;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void takeDamage(int damage) {
        this.healthPoints = Math.max(0, this.healthPoints - damage);
    }

    public boolean isAlive() {
        return healthPoints > 0;
    }
}