package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();
        if (unitsByRow.isEmpty()) return suitableUnits;

        int startLayer = isLeftArmyTarget ? unitsByRow.size() - 1 : 0;
        int endLayer = isLeftArmyTarget ? -1 : unitsByRow.size();
        int step = isLeftArmyTarget ? -1 : 1;

        Set<Integer> occupiedY = new HashSet<>();

        for (int layer = startLayer; layer != endLayer; layer += step) {
            List<Unit> row = unitsByRow.get(layer);

            for (Unit unit : row) {
                // Пропускаем пустые клетки и мёртвых юнитов
                if (unit == null || !unit.isAlive()) continue;

                int y = unit.getyCoordinate();

                if (!occupiedY.contains(y)) {
                    suitableUnits.add(unit);
                    occupiedY.add(y);
                }
            }
        }
        return suitableUnits;
    }
}

