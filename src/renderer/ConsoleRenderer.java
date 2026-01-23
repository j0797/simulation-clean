package renderer;

import entities.Entity;
import worldmap.*;


public class ConsoleRenderer implements Renderer {

    public void render(WorldMap map) {

        System.out.println("\nКАРТА СИМУЛЯЦИИ:\n");
        for (int row = 0; row < map.getHeight(); row++) {
            for (int column = 0; column < map.getWidth(); column++) {
                Coordinates coordinates = new Coordinates(row, column);
                Entity entity = map.getEntity(coordinates);
                String symbol = getSymbolForEntity(entity);
                System.out.print(centerSymbol(symbol));
            }
            System.out.println();
        }
        printLegend();
    }

    private String getSymbolForEntity(Entity entity) {
        if (entity == null) {
            return "⬜";
        }
        return switch (entity.getClass().getSimpleName()) {
            case "Predator" -> "🐺";
            case "Herbivore" -> "🐇";
            case "Grass" -> "🌿";
            case "Rock" -> "🪨";
            case "Tree" -> "🌳";
            default -> "❓";
        };
    }

    private String centerSymbol(String symbol) {
        return String.format(" %s ", symbol);
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
