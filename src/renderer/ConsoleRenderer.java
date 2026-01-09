package renderer;

import entities.Entity;
import worldmap.*;

import java.util.Optional;


public class ConsoleRenderer implements Renderer {

    public void render(WorldMap map) {

        System.out.println("\n=== КАРТА СИМУЛЯЦИИ ===");
        for (int row = 0; row < map.getHeight(); row++) {
            for (int column = 0; column < map.getWidth(); column++) {

                Coordinates coordinates = new Coordinates(row, column);
                Optional<Entity> entityOpt = map.getEntity(coordinates);
                String symbol = getSymbolForEntity(entityOpt);
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
        System.out.println("====================\n");
        printLegend();
    }

    private String getSymbolForEntity(Optional<Entity> entityOpt) {
        if (entityOpt.isEmpty()) {
            return "·";
        }

        Entity entity = entityOpt.get();
        return switch (entity.getClass().getSimpleName()) {
            case "Predator" -> "🐺";
            case "Herbivore" -> "🐰";
            case "Grass" -> "🌿";
            case "Rock" -> "🪨";
            case "Tree" -> "🌳";
            default -> "?";
        };
    }

    private void printLegend() {
        System.out.println("Легенда:");
        System.out.println("  🐺 - Хищник");
        System.out.println("  🐰 - Травоядное");
        System.out.println("  🌿 - Трава");
        System.out.println("  🌳 - Дерево");
        System.out.println("  🪨 - Камень");
        System.out.println("  ·  - Пустая клетка");
        System.out.println();
    }
}
