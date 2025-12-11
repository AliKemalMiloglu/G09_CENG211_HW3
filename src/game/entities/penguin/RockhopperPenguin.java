package game.entities.penguin;

import java.util.ArrayList;

import game.core.IHazard;
import game.core.ITerrainObject;
import game.core.IcyTerrain;
import game.entities.hazard.HoleInIce;
import game.enums.Direction;
import game.enums.PenguinType;
import game.utils.Position;

/**
 * RockhopperPenguin class - a penguin that can jump over one hazard in their path.
 *
 * Special Ability: Before sliding, the Rockhopper Penguin can prepare to jump
 * over one hazard in their path. When the penguin reaches the hazard during
 * their slide, they will jump over it instead of colliding.
 *
 * Conditions:
 * - The ability must be activated before sliding
 * - Only the first hazard in the path can be jumped
 * - Can only land on an empty square after the hazard
 * - If the landing square is occupied, the jump fails and collision occurs
 * - If no hazard is in the path, the ability is wasted
 * - Plugged HoleInIce is not considered a hazard (can pass through normally)
 *
 * This ability is useful for bypassing dangerous hazards like HoleInIce or SeaLion.
 */
public class RockhopperPenguin extends Penguin {

    /** Whether the penguin is prepared to jump */
    private boolean preparedToJump;

    /** The position of the hazard to jump over */
    private Position hazardToJump;

    /**
     * Default constructor.
     * Creates a Rockhopper Penguin with default values.
     */
    public RockhopperPenguin() {
        super();
        this.type = PenguinType.ROCKHOPPER;
        this.preparedToJump = false;
        this.hazardToJump = null;
    }

    /**
     * Constructor with name and position.
     * @param name The penguin's name ("P1", "P2", "P3")
     * @param position The starting position on the grid
     */
    public RockhopperPenguin(String name, Position position) {
        super(name, PenguinType.ROCKHOPPER, position);
        this.preparedToJump = false;
        this.hazardToJump = null;
    }

    /**
     * Copy constructor.
     * @param other The RockhopperPenguin to copy
     */
    public RockhopperPenguin(RockhopperPenguin other) {
        super(other);
        this.preparedToJump = other.preparedToJump;
        this.hazardToJump = other.hazardToJump != null ? new Position(other.hazardToJump) : null;
    }

    /**
     * Activates the Rockhopper Penguin's special ability.
     * Scans the path for the first hazard and prepares to jump over it.
     * @param dir The direction to scan for hazards
     * @param terrain The game terrain
     * @return true if a hazard was found to jump, false if no hazard in path
     */
    @Override
    public boolean useSpecialAction(Direction dir, IcyTerrain terrain) {
        preparedToJump = true;
        hazardToJump = null;

        // Scan the path for the first hazard
        Position scanPos = new Position(position);

        while (true) {
            scanPos = scanPos.move(dir);

            // Check if out of bounds
            if (!terrain.isPositionValid(scanPos)) {
                break;
            }

            // Check for objects at this position
            ArrayList<ITerrainObject> objects = terrain.getObjectsAt(scanPos);

            for (ITerrainObject obj : objects) {
                // Check if this is a hazard that can be jumped
                // Plugged HoleInIce is not considered a hazard (can pass through)
                if (obj instanceof IHazard) {
                    // Skip plugged holes - they're not obstacles
                    if (obj instanceof HoleInIce && ((HoleInIce) obj).isPlugged()) {
                        continue;
                    }

                    // Found a hazard to jump
                    hazardToJump = new Position(scanPos);
                    specialActionUsed = true;
                    return true;
                }
            }
        }

        // No hazard found in path - ability is wasted
        specialActionUsed = true;
        return false;
    }

    /**
     * Checks if the penguin should stop at the given position.
     * Rockhopper Penguins don't have voluntary stopping during slides.
     * @param pos The position to check
     * @return false (Rockhopper Penguins don't stop voluntarily)
     */
    @Override
    public boolean shouldStopHere(Position pos) {
        return false;
    }

    /**
     * Checks if the penguin should jump at the given position.
     * Called during the slide to determine if a jump should occur.
     * @param pos The current position during the slide
     * @return true if the penguin should jump over this position
     */
    public boolean shouldJumpAt(Position pos) {
        if (preparedToJump && hazardToJump != null) {
            return pos.equals(hazardToJump);
        }
        return false;
    }

    /**
     * Checks if the penguin is prepared to jump.
     * @return true if the ability is active and jump is prepared
     */
    public boolean isPreparedToJump() {
        return preparedToJump;
    }

    /**
     * Returns the position of the hazard to jump over.
     * @return The hazard position, or null if no hazard targeted
     */
    public Position getHazardToJump() {
        return hazardToJump;
    }

    /**
     * Resets the jump state after a slide is complete.
     * Should be called after each slide to clear the jump preparation.
     */
    public void resetJump() {
        preparedToJump = false;
        hazardToJump = null;
    }

    /**
     * Creates a deep copy of this RockhopperPenguin.
     * @return A new RockhopperPenguin with the same properties
     */
    @Override
    public ITerrainObject deepCopy() {
        return new RockhopperPenguin(this);
    }

    /**
     * Returns a string representation of this RockhopperPenguin.
     * @return A descriptive string
     */
    @Override
    public String toString() {
        return name + " [Rockhopper Penguin at " + position +
                ", food=" + calculateTotalWeight() + " units" +
                ", ability " + (specialActionUsed ? "used" : "available") +
                (preparedToJump ? ", jump prepared" : "") + "]";
    }
}