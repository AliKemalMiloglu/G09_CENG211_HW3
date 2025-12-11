package game.core;

import java.util.ArrayList;
import java.util.List;

import game.entities.food.Food;
import game.entities.hazard.Hazard;
import game.entities.hazard.HoleInIce;
import game.entities.penguin.Penguin;
import game.entities.penguin.RockhopperPenguin;
import game.enums.Direction;
import game.utils.GameConstants;
import game.utils.Position;
import game.utils.RandomGenerator;

/**
 * AIController class - handles AI decision-making.
 *
 * Responsibility: Make movement and special action decisions for AI penguins.
 */
public class AIController {

    private GameBoard board;

    /**
     * Creates a new AIController for the given board.
     * @param board The game board
     */
    public AIController(GameBoard board) {
        this.board = board;
    }

    /**
     * Decides whether the AI should use its special action.
     * @param penguin The AI penguin
     * @param slideDirection The direction the penguin will slide
     * @return true if the AI should use its special action
     */
    public boolean shouldUseSpecialAction(Penguin penguin, Direction slideDirection) {
        if (penguin.isSpecialActionUsed()) {
            return false;
        }

        // Rockhopper automatically uses ability when heading toward hazard
        if (penguin instanceof RockhopperPenguin) {
            return hasHazardInPath(penguin.getPosition(), slideDirection);
        }

        // Other penguins: 30% chance
        return RandomGenerator.chance(GameConstants.AI_SPECIAL_ACTION_CHANCE);
    }

    /**
     * Chooses the best direction for an AI penguin to slide.
     * Priority: food > hazard (except hole) > water
     * @param penguin The AI penguin
     * @return The chosen direction
     */
    public Direction chooseSlideDirection(Penguin penguin) {
        Position pos = penguin.getPosition();

        // Priority 1: Direction with food
        for (Direction dir : Direction.values()) {
            if (hasFoodInPath(pos, dir)) {
                return dir;
            }
        }

        // Priority 2: Direction with hazard (except HoleInIce)
        for (Direction dir : Direction.values()) {
            if (hasNonHoleHazardInPath(pos, dir)) {
                return dir;
            }
        }

        // Priority 3: Direction that doesn't lead immediately to water
        for (Direction dir : Direction.values()) {
            Position next = pos.move(dir);
            if (board.isPositionValid(next)) {
                return dir;
            }
        }

        // Fallback: any direction (will fall into water)
        return Direction.random();
    }

    /**
     * Chooses a safe direction for RoyalPenguin's step (special action).
     * According to the rules: "RoyalPenguins will use their single step in a
     * random direction that does not lead them to a Hazard or falling to water,
     * unless they have no other choice."
     * @param penguin The Royal Penguin
     * @return A random safe direction
     */
    public Direction chooseRoyalStepDirection(Penguin penguin) {
        Position pos = penguin.getPosition();
        List<Direction> safeDirections = new ArrayList<>();

        // Collect all safe directions (no hazard, no water, empty or food only)
        for (Direction dir : Direction.values()) {
            Position nextPos = pos.move(dir);
            if (board.isPositionValid(nextPos)) {
                ArrayList<ITerrainObject> objects = board.getObjectsAt(nextPos);
                boolean safe = objects.isEmpty() ||
                        objects.stream().allMatch(obj ->
                                obj instanceof Food ||
                                        (obj instanceof HoleInIce && ((HoleInIce) obj).isPlugged()));
                if (safe) {
                    safeDirections.add(dir);
                }
            }
        }

        // If there are safe directions, pick one randomly
        if (!safeDirections.isEmpty()) {
            return RandomGenerator.randomElement(safeDirections);
        }

        // Fallback: any valid direction that doesn't have unplugged hole
        List<Direction> validDirections = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            Position nextPos = pos.move(dir);
            if (board.isPositionValid(nextPos)) {
                ArrayList<ITerrainObject> objects = board.getObjectsAt(nextPos);
                boolean hasUnpluggedHole = objects.stream()
                        .anyMatch(obj -> obj instanceof HoleInIce && !((HoleInIce) obj).isPlugged());
                if (!hasUnpluggedHole) {
                    validDirections.add(dir);
                }
            }
        }

        if (!validDirections.isEmpty()) {
            return RandomGenerator.randomElement(validDirections);
        }

        // Last resort: random (will fall into water or hole)
        return Direction.random();
    }

    /**
     * Checks if there is food in the path from a position in a direction.
     */
    private boolean hasFoodInPath(Position start, Direction dir) {
        Position current = new Position(start);
        while (true) {
            current = current.move(dir);
            if (!board.isPositionValid(current)) {
                return false;
            }

            for (ITerrainObject obj : board.getObjectsAt(current)) {
                if (obj instanceof Food) {
                    return true;
                }
                if (!(obj instanceof Food)) {
                    return false;
                }
            }
        }
    }

    /**
     * Checks if there is a hazard (not HoleInIce) in the path.
     * Stops checking at Food or other Penguins (because sliding stops there).
     */
    private boolean hasNonHoleHazardInPath(Position start, Direction dir) {
        Position current = new Position(start);
        while (true) {
            current = current.move(dir);
            if (!board.isPositionValid(current)) {
                return false;
            }

            ArrayList<ITerrainObject> objects = board.getObjectsAt(current);

            for (ITerrainObject obj : objects) {
                // If there's food, penguin stops here
                if (obj instanceof Food) {
                    return false;
                }

                // If there's another penguin, sliding stops
                if (obj instanceof Penguin) {
                    return false;
                }

                // Non-hole hazard found
                if (obj instanceof Hazard && !(obj instanceof HoleInIce)) {
                    return true;
                }

                // Unplugged hole - avoid this path
                if (obj instanceof HoleInIce && !((HoleInIce) obj).isPlugged()) {
                    return false;
                }
            }
        }
    }

    /**
     * Checks if there is any hazard in the path that the penguin will encounter.
     * Stops checking at Food or other Penguins (because sliding stops there).
     * Used to determine if Rockhopper should automatically use ability.
     */
    public boolean hasHazardInPath(Position start, Direction dir) {
        Position current = new Position(start);
        while (true) {
            current = current.move(dir);
            if (!board.isPositionValid(current)) {
                return false;
            }

            ArrayList<ITerrainObject> objects = board.getObjectsAt(current);

            for (ITerrainObject obj : objects) {
                // If there's food, penguin stops here - no hazard encounter
                if (obj instanceof Food) {
                    return false;
                }

                // If there's another penguin, sliding stops - no hazard encounter
                if (obj instanceof Penguin) {
                    return false;
                }

                // Check for hazard
                if (obj instanceof Hazard) {
                    // Plugged holes are not obstacles
                    if (obj instanceof HoleInIce && ((HoleInIce) obj).isPlugged()) {
                        continue;
                    }
                    return true;  // Found a hazard that penguin will encounter
                }
            }
        }
    }
}