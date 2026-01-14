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
                System.out.print(padSymbol(symbol));
            }
            System.out.println();
        }

        System.out.println("=".repeat(30));
        printLegend();
    }

    private String getSymbolForEntity(Optional<Entity> entityOpt) {
        if (entityOpt.isEmpty()) {
            return "⬜";
        }

        Entity entity = entityOpt.get();
        return switch (entity.getClass().getSimpleName()) {
            case "Predator" -> "🐺";
            case "Herbivore" -> "🐇";
            case "Grass" -> "🌿";
            case "Rock" -> "🪨";
            case "Tree" -> "🌳";
            default -> "❓";
        };
    }


    private String padSymbol(String symbol) {

        if (symbol.equals("⬜")) {
            return symbol + " ";
        } else {
            return symbol + " ";
        }
    }

    private void printLegend() {
        System.out.println("\n📖 Легенда:");
        System.out.println("  🐺 - Хищник (Predator)");
        System.out.println("  🐇 - Травоядное (Herbivore)");
        System.out.println("  🌿 - Трава (Grass)");
        System.out.println("  🪨 - Камень (Rock)");
        System.out.println("  🌳 - Дерево (Tree)");
        System.out.println("  ⬜ - Пустая клетка");
        System.out.println();
    }
}