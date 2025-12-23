package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {
    private final PrintBattleLog printBattleLog;

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    public SimulateBattleImpl() {
        this.printBattleLog = null;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        if (playerArmy == null || computerArmy == null) {
            throw new IllegalArgumentException("Армии не должны быть null");
        }

        // Пока у обеих армий есть живые юниты
        while (hasAlive(playerArmy) && hasAlive(computerArmy)) {

            // 1️⃣ Формируем очередь текущего раунда
            List<Unit> queue = new ArrayList<>();

            for (Unit u : playerArmy.getUnits()) {
                if (u.isAlive()) queue.add(u);
            }
            for (Unit u : computerArmy.getUnits()) {
                if (u.isAlive()) queue.add(u);
            }

            // 2️⃣ Сортируем по убыванию атаки
            queue.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            // 3️⃣ Юниты ходят по очереди
            int index = 0;
            while (index < queue.size()) {
                Unit attacker = queue.get(index);

                // Если юнит погиб до своего хода — исключаем
                if (!attacker.isAlive()) {
                    queue.remove(index);
                    continue;
                }

                Unit target = attacker.getProgram().attack();

                if (printBattleLog != null) {
                    printBattleLog.printBattleLog(attacker, target);
                }

                // 4️⃣ Если цель умерла и ещё была в очереди — удаляем её
                if (target != null && !target.isAlive()) {
                    queue.remove(target);

                    // Пересортировываем очередь
                    queue.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
                    index = queue.indexOf(attacker);
                }

                index++;

                // Если одна из армий закончилась — раунд можно прервать
                if (!hasAlive(playerArmy) || !hasAlive(computerArmy)) {
                    break;
                }
            }

            // 5️⃣ Конец раунда
            Thread.sleep(1000);
        }
    }

    private boolean hasAlive(Army army) {
        for (Unit u : army.getUnits()) {
            if (u.isAlive()) return true;
        }
        return false;
    }
}