package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    // 8 направлений (включая диагонали)
    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        // Ваше решение
        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        boolean[][] blocked = new boolean[WIDTH][HEIGHT];
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] prev = new Edge[WIDTH][HEIGHT];

        for (Unit unit : existingUnitList) {
            if (!unit.isAlive()) continue;
            if (unit == attackUnit || unit == targetUnit) continue;

            blocked[unit.getxCoordinate()][unit.getyCoordinate()] = true;
        }

        Queue<Edge> queue = new ArrayDeque<>();
        queue.add(new Edge(startX, startY));
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            Edge current = queue.poll();

            if (current.getX() == targetX && current.getY() == targetY) {
                break;
            }

            for (int[] d : DIRECTIONS) {
                int nx = current.getX() + d[0];
                int ny = current.getY() + d[1];

                if (!isInside(nx, ny)) continue;
                if (visited[nx][ny]) continue;
                if (blocked[nx][ny]) continue;

                visited[nx][ny] = true;
                prev[nx][ny] = current;
                queue.add(new Edge(nx, ny));
            }
        }

        return buildPath(prev, startX, startY, targetX, targetY);
    }

    private boolean isInside(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    private List<Edge> buildPath(Edge[][] prev,
                                 int startX, int startY,
                                 int targetX, int targetY) {

        List<Edge> path = new ArrayList<>();

        if (prev[targetX][targetY] == null &&
                !(startX == targetX && startY == targetY)) {
            return path;
        }

        int x = targetX;
        int y = targetY;

        while (x != startX || y != startY) {
            path.add(new Edge(x, y));
            Edge p = prev[x][y];
            x = p.getX();
            y = p.getY();
        }

        path.add(new Edge(startX, startY));
        Collections.reverse(path);
        return path;
    }
}
