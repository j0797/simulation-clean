package renderer;

import entities.Entity;
import worldmap.Coordinates;
import worldmap.WorldMap;
import java.util.Optional;

public class ConsoleRenderer implements Renderer {

    @Override
    public void render(WorldMap map) {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("КАРТА СИМУЛЯЦИИ (" + map.getWidth() + "x" + map.getHeight() + ")");
        System.out.println("=".repeat(30));

        for (int row = 0; row < map.getHeight(); row++) {
            for (int column = 0; column < map.getWidth(); column++) {
                Coordinates coordinates = new Coordinates(row, column);
                Optional<Entity> entityOpt = map.getEntity(coordinates);

                String symbol = getSymbolForEntity(entityOpt);
                System.out.print(symbol + " ");
            }
            System.out.println();
        }

        System.out.println("=".repeat(30));
        printLegend();
    }

    private String getSymbolForEntity(Optional<Entity> entityOpt) {
        if (entityOpt.isEmpty()) {
            return ".";
        }

        Entity entity = entityOpt.get();
        return switch (entity.getClass().getSimpleName()) {
            case "Predator" -> "P";
            case "Herbivore" -> "H";
            case "Grass" -> "*";
            case "Rock" -> "#";
            case "Tree" -> "T";
            default -> "?";
        };
    }

    private void printLegend() {
        System.out.println("\nЛегенда:");
        System.out.println("  P - Хищник (Predator)");
        System.out.println("  H - Травоядное (Herbivore)");
        System.out.println("  * - Трава (Grass)");
        System.out.println("  # - Камень (Rock)");
        System.out.println("  T - Дерево (Tree)");
        System.out.println("  . - Пустая клетка");
        System.out.println();
    }
}