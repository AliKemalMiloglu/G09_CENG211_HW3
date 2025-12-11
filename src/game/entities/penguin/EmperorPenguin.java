package game.entities.penguin;

import game.core.ITerrainObject;
import game.core.IcyTerrain;
import game.enums.Direction;
import game.enums.PenguinType;
import game.utils.Position;

/**
 * EmperorPenguin class - a penguin that can stop at the 3rd square when sliding.
 *
 * Special Ability: When activated before sliding, the Emperor Penguin can choose
 * to stop at the third square they slide into. If the direction chosen has
 * less than three free squares, the ability is still considered used.
 *
 * This ability is useful for short-range precise positioning to reach nearby
 * food items or stop before hazards.
 */
public class EmperorPenguin extends Penguin {

    /** The number of squares slid during the current movement */
    private int slideCount;

    /** Whether the ability is active for the current slide */
    private boolean abilityActiveThisTurn;

    /** The target square to stop at (3rd square) */
    private static final int STOP_AT_SQUARE = 3;

    /**
     * Default constructor.
     * Creates an Emperor Penguin with default values.
     */
    public EmperorPenguin() {
        super();
        this.type = PenguinType.EMPEROR;
        this.slideCount = 0;
        this.abilityActiveThisTurn = false;
    }

    /**
     * Constructor with name and position.
     * @param name The penguin's name ("P1", "P2", "P3")
     * @param position The starting position on the grid
     */
    public EmperorPenguin(String name, Position position) {
        super(name, PenguinType.EMPEROR, position);
        this.slideCount = 0;
        this.abilityActiveThisTurn = false;
    }

    /**
     * Copy constructor.
     * @param other The EmperorPenguin to copy
     */
    public EmperorPenguin(EmperorPenguin other) {
        super(other);
        this.slideCount = other.slideCount;
        this.abilityActiveThisTurn = other.abilityActiveThisTurn;
    }

    /**
     * Activates the Emperor Penguin's special ability.
     * When sliding, the penguin will stop at the 3rd square.
     * @param dir The direction to slide (used for context, actual sliding happens separately)
     * @param terrain The game terrain
     * @return true (ability activation always succeeds)
     */
    @Override
    public boolean useSpecialAction(Direction dir, IcyTerrain terrain) {
        specialActionUsed = true;
        abilityActiveThisTurn = true; // Activate for this turn only
        slideCount = 0; // Reset counter for this slide
        return true;
    }

    /**
     * Checks if the penguin should stop at the given position.
     * For Emperor Penguin, this returns true at the 3rd square if the ability is active THIS TURN.
     * @param pos The position to check
     * @return true if the penguin should stop here, false otherwise
     */
    @Override
    public boolean shouldStopHere(Position pos) {
        if (abilityActiveThisTurn && !hasFallen) {
            slideCount++;
            if (slideCount >= STOP_AT_SQUARE) {
                return true; // Stop at 3rd square
            }
        }
        return false;
    }

    /**
     * Resets the slide counter.
     * Called at the start of each new slide.
     * Note: abilityActiveThisTurn is reset at the START of each turn in IcyTerrain.playTurn()
     */
    @Override
    public void resetSlideCount() {
        this.slideCount = 0;
        // Do NOT reset abilityActiveThisTurn here - it's reset at the start of each turn
    }

    /**
     * Resets the ability active flag for the new turn.
     * Called at the start of each turn.
     */
    public void resetAbilityForNewTurn() {
        this.abilityActiveThisTurn = false;
        this.slideCount = 0;
    }

    /**
     * Returns the current slide count.
     * @return The number of squares slid
     */
    public int getSlideCount() {
        return slideCount;
    }

    /**
     * Creates a deep copy of this EmperorPenguin.
     * @return A new EmperorPenguin with the same properties
     */
    @Override
    public ITerrainObject deepCopy() {
        return new EmperorPenguin(this);
    }

    /**
     * Returns a string representation of this EmperorPenguin.
     * @return A descriptive string
     */
    @Override
    public String toString() {
        return name + " [Emperor Penguin at " + position +
                ", food=" + calculateTotalWeight() + " units" +
                ", ability " + (specialActionUsed ? "used" : "available") + "]";
    }
}