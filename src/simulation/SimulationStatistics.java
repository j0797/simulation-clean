package simulation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SimulationStatistics implements Simulation.SimulationListener {
    private final Simulation simulation;
    private final List<TurnRecord> history = new ArrayList<>();
    private TurnRecord currentRecord;

    public SimulationStatistics(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public void onSimulationStarted() {
        recordTurn();
    }

    @Override
    public void onTurnCompleted(int turnNumber) {
        recordTurn();
    }

    @Override
    public void onSimulationStopped(String reason) {
        if (currentRecord != null) {
            currentRecord.stopReason = reason;
        }
        System.out.println("\n=== ФИНАЛЬНАЯ СТАТИСТИКА ===");
        displayCurrentStats();
        if (currentRecord != null && currentRecord.stopReason != null) {
            System.out.println("Причина завершения: " + currentRecord.stopReason);
        }
    }

    @Override
    public void onEntityCountChanged(int herbivores, int predators, int grass) {
        if (currentRecord != null) {
            currentRecord.herbivores = herbivores;
            currentRecord.predators = predators;
            currentRecord.grass = grass;
        }
    }

    private void recordTurn() {
        currentRecord = new TurnRecord();
        currentRecord.turnNumber = simulation.getTurnCount();
        currentRecord.timestamp = LocalDateTime.now();
        history.add(currentRecord);
    }

    public void displayCurrentStats() {
        if (currentRecord == null) return;

        System.out.println("\n=== СТАТИСТИКА ===");
        System.out.println("Текущий ход: " + currentRecord.turnNumber);
        System.out.println("Статус: " + getStatusText());
        System.out.println("Травоядные: " + currentRecord.herbivores);
        System.out.println("Хищники: " + currentRecord.predators);
        System.out.println("Трава: " + currentRecord.grass);

        if (history.size() > 1) {
            TurnRecord previous = history.get(history.size() - 2);
            System.out.println("\nИзменения с предыдущего хода:");
            System.out.println("Травоядные: " +
                    formatChange(currentRecord.herbivores, previous.herbivores));
            System.out.println("Хищники: " +
                    formatChange(currentRecord.predators, previous.predators));
            System.out.println("Трава: " +
                    formatChange(currentRecord.grass, previous.grass));
        }
    }

    private String formatChange(int current, int previous) {
        int diff = current - previous;
        if (diff > 0) {
            return current + " (+" + diff + ")";
        } else if (diff < 0) {
            return current + " (" + diff + ")";
        } else {
            return current + " (без изменений)";
        }
    }

    private String getStatusText() {
        if (!simulation.isRunning()) {
            return "Остановлена";
        } else if (simulation.isPaused()) {
            return "На паузе";
        } else {
            return "Выполняется";
        }
    }

    private static class TurnRecord {
        int turnNumber;
        LocalDateTime timestamp;
        int herbivores;
        int predators;
        int grass;
        String stopReason;
    }
}