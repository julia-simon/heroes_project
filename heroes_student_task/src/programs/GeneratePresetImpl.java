package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_UNIT_COUNT = 11;
    private static final int START_X = 0;  // минимальная X для компьютера
    private static final int LAYERS_X = 3;   // 0,1,2
    private static final int GRID_HEIGHT = 21;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // Ваше решение
        if (unitList == null || unitList.isEmpty() || maxPoints <= 0) return new Army();

        unitList.sort((a, b) -> Double.compare(
                (double)(b.getBaseAttack() + b.getHealth()) / b.getCost(),
                (double)(a.getBaseAttack() + a.getHealth()) / a.getCost()
        ));

        List<Unit> armyUnits = new ArrayList<>();
        Map<String, Integer> typeCounts = new HashMap<>();
        boolean[][] occupiedY = new boolean[LAYERS_X][GRID_HEIGHT];
        int remainingPoints = maxPoints;
        Random random = new Random();

        for (Unit template : unitList) {
            int unitsAdded = 0;
            while (unitsAdded < MAX_UNIT_COUNT && template.getCost() <= remainingPoints) {
                int xLayer = unitsAdded % LAYERS_X;
                int yCoord;
                do {
                    yCoord = random.nextInt(GRID_HEIGHT);
                } while (occupiedY[xLayer][yCoord]);

                occupiedY[xLayer][yCoord] = true;

                String unitName = template.getUnitType() + " " + (typeCounts.getOrDefault(template.getUnitType(), 0) + 1);
                Unit newUnit = new Unit(
                        unitName,
                        template.getUnitType(),
                        template.getHealth(),
                        template.getBaseAttack(),
                        template.getCost(),
                        template.getAttackType(),
                        template.getAttackBonuses(),
                        template.getDefenceBonuses(),
                        START_X + xLayer,
                        yCoord
                );

                armyUnits.add(newUnit);
                remainingPoints -= template.getCost();
                unitsAdded++;
                typeCounts.put(template.getUnitType(), typeCounts.getOrDefault(template.getUnitType(), 0) + 1);

                if (remainingPoints <= 0) break;
            }
            if (remainingPoints <= 0) break;
        }

        Army computerArmy = new Army();
        computerArmy.setUnits(armyUnits);
        computerArmy.setPoints(maxPoints - remainingPoints);
        return computerArmy;
    }
}