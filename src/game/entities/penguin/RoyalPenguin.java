package game.entities.penguin;

import java.util.ArrayList;

import game.core.ITerrainObject;
import game.core.IcyTerrain;
import game.entities.food.Food;
import game.entities.hazard.HoleInIce;
import game.enums.Direction;
import game.enums.PenguinType;
import game.utils.Position;

/**
 * RoyalPenguin class - a penguin that can safely move one square before sliding.
 *
 * Special Ability: Before sliding, the Royal Penguin can choose to safely move
 * into an adjacent square (horizontally or vertically). This movement does not
 * cause sliding - the penguin simply steps to the adjacent square.
 *
 * Cautions:
 * - It is possible to accidentally step out of the grid and fall into water
 * - Cannot step onto a square occupied by another object (except food)
 * - If stepping onto food, the food is collected
 *
 * This ability is useful for repositioning before a slide or avoiding
 * hazards that are directly adjacent.
 */
public class RoyalPenguin extends Penguin {

    /**
     * Default constructor.
     * Creates a Royal Penguin with default values.
     */
    public RoyalPenguin() {
        super();
        this.type = PenguinType.ROYAL;
    }

    /**
     * Constructor with name and position.
     * @param name The penguin's name ("P1", "P2", "P3")
     * @param position The starting position on the grid
     */
    public RoyalPenguin(String name, Position position) {
        super(name, PenguinType.ROYAL, position);
    }

    /**
     * Copy constructor.
     * @param other The RoyalPenguin to copy
     */
    public RoyalPenguin(RoyalPenguin other) {
        super(other);
    }

    /**
     * Activates the Royal Penguin's special ability.
     * Attempts to move one square in the given direction without sliding.
     * Note: Food collection is handled by IcyTerrain after this method returns.
     * @param dir The direction to step
     * @param terrain The game terrain
     * @return true if the step was successful, false if it failed (blocked or fell)
     */
    @Override
    public boolean useSpecialAction(Direction dir, IcyTerrain terrain) {
        // Calculate the new position
        Position newPos = position.move(dir);

        // Check if stepping into water (out of bounds)
        if (!terrain.isPositionValid(newPos)) {
            // Accidentally steps into water
            hasFallen = true;
            terrain.removeObjectAt(position, this);
            specialActionUsed = true;
            return false;
        }

        // Check if landing spot is occupied
        ArrayList<ITerrainObject> objectsAtDest = terrain.getObjectsAt(newPos);

        // Check for HoleInIce - this is a "similar accident" to falling into water
        for (ITerrainObject obj : objectsAtDest) {
            if (obj instanceof HoleInIce) {
                HoleInIce hole = (HoleInIce) obj;
                if (!hole.isPlugged()) {
                    // Accidentally steps into hole
                    hasFallen = true;
                    terrain.removeObjectAt(position, this);
                    specialActionUsed = true;
                    return false;
                }
                // Plugged hole - can step over safely, continue checking
            }
        }

        // Can land if empty, only has food, or only has plugged holes
        boolean canLand = objectsAtDest.isEmpty() ||
                objectsAtDest.stream().allMatch(obj ->
                        obj instanceof Food ||
                                (obj instanceof HoleInIce &&
                                        ((HoleInIce) obj).isPlugged()));

        if (canLand) {
            // Remove from current position
            terrain.removeObjectAt(position, this);

            // Update position
            position = newPos;

            // Add to new position
            terrain.addObjectAt(position, this);

            // Food collection is handled by IcyTerrain.executeSpecialAction()
            // so that the message can be printed properly

            specialActionUsed = true;
            return true;
        }

        // Cannot land - square is occupied by non-food object (hazard or penguin)
        specialActionUsed = true;
        return false;
    }

    /**
     * Checks if the penguin should stop at the given position.
     * Royal Penguins don't have voluntary stopping during slides.
     * @param pos The position to check
     * @return false (Royal Penguins don't stop voluntarily)
     */
    @Override
    public boolean shouldStopHere(Position pos) {
        return false; // Royal penguins don't have voluntary stopping
    }

    /**
     * Creates a deep copy of this RoyalPenguin.
     * @return A new RoyalPenguin with the same properties
     */
    @Override
    public ITerrainObject deepCopy() {
        return new RoyalPenguin(this);
    }

    /**
     * Returns a string representation of this RoyalPenguin.
     * @return A descriptive string
     */
    @Override
    public String toString() {
        return name + " [Royal Penguin at " + position +
                ", food=" + calculateTotalWeight() + " units" +
                ", ability " + (specialActionUsed ? "used" : "available") + "]";
    }
}