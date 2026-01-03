package actions;

import entities.Entity;
import entities.creatures.Herbivore;
import entities.creatures.Predator;
import entities.objects.Grass;
import entities.objects.Rock;
import entities.objects.Tree;
import map.WorldMap;

import java.util.Random;

public class InitActions implements Action {
    private final Random random = new Random();
    private static final int INITIAL_TREES = 15;
    private static final int INITIAL_ROCKS = 10;
    private static final int INITIAL_GRASS = 10;
    private static final int INITIAL_HERBIVORES = 10;
    private static final int INITIAL_PREDATORS = 5;

    @Override
    public void perform(WorldMap map) {
        System.out.println("Инициализация мира...");

        for (int i = 0; i < INITIAL_TREES; i++) placeRandomEntity(map, new Tree());
        for (int i = 0; i < INITIAL_ROCKS; i++) placeRandomEntity(map, new Rock());
        for (int i = 0; i < INITIAL_GRASS; i++) placeRandomEntity(map, new Grass());
        for (int i = 0; i < INITIAL_HERBIVORES; i++) placeRandomEntity(map, new Herbivore());
        for (int i = 0; i < INITIAL_PREDATORS; i++) placeRandomEntity(map, new Predator());

        System.out.println("Мир создан!");
        printInitialStats(map);
    }

    private void placeRandomEntity(WorldMap map, Entity entity) {
        map.getRandomEmptyCoordinate(random).ifPresent(coordinates -> map.placeEntity(coordinates, entity));
    }

    private void printInitialStats(WorldMap map) {
        System.out.println("Начальная статистика:");
        System.out.println("   Размер мира: " + map.getWidth() + "x" + map.getHeight());
        System.out.println("   Деревья: " + INITIAL_TREES);
        System.out.println("   Камни: " + INITIAL_ROCKS);
        System.out.println("   Трава: " + INITIAL_GRASS);
        System.out.println("   Травоядные: " + INITIAL_HERBIVORES);
        System.out.println("   Хищники: " + INITIAL_PREDATORS);
        System.out.println();
    }
}
