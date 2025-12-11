package game.entities.hazard;

import game.core.IHazard;
import game.core.ITerrainObject;
import game.core.IcyTerrain;
import game.entities.penguin.Penguin;
import game.utils.Position;

/**
 * Hazard abstract class - base class for all hazard types.
 *
 * This abstract class provides shared structure for all hazard types while
 * enforcing effect implementation through abstract methods. All hazards
 * have a position on the grid and implement both ITerrainObject and IHazard.
 *
 * Hazard types:
 * - LightIceBlock: Slidable ice block that stuns penguins
 * - HeavyIceBlock: Immovable block that causes food loss
 * - SeaLion: Slidable hazard that bounces penguins
 * - HoleInIce: Static hole that removes objects (can be plugged)
 */
public abstract class Hazard implements ITerrainObject, IHazard {

    /** The position of this hazard on the grid */
    protected Position position;

    /**
     * Whether this hazard is plugged (only relevant for HoleInIce).
     * For other hazard types, this is always false.
     */
    protected boolean plugged;

    /**
     * Default constructor.
     * Creates a hazard with no position set.
     */
    public Hazard() {
        this.position = null;
        this.plugged = false;
    }

    /**
     * Constructor with position.
     * @param position The position of this hazard on the grid
     */
    public Hazard(Position position) {
        this.position = position;
        this.plugged = false;
    }

    /**
     * Copy constructor.
     * @param other The hazard to copy
     */
    public Hazard(Hazard other) {
        this.position = new Position(other.position);
        this.plugged = other.plugged;
    }

    /**
     * Applies the hazard's effect when a penguin collides with it.
     * Each concrete hazard class must implement its specific behavior.
     * @param penguin The penguin that collided with this hazard
     * @param terrain The game terrain for processing chain reactions
     */
    @Override
    public abstract void applyEffect(Penguin penguin, IcyTerrain terrain);

    /**
     * Returns the display symbol for this hazard.
     * Each concrete hazard class must provide its symbol.
     * @return The display symbol (e.g., "LB", "HB", "SL", "HI")
     */
    @Override
    public abstract String getDisplaySymbol();

    /**
     * Creates a deep copy of this hazard.
     * Each concrete hazard class must implement its own copy logic.
     * @return A new Hazard that is a copy of this one
     */
    @Override
    public abstract ITerrainObject deepCopy();

    /**
     * Returns the current position of this hazard.
     * @return The Position on the grid
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of this hazard.
     * @param pos The new Position
     */
    @Override
    public void setPosition(Position pos) {
        this.position = pos;
    }

    /**
     * Checks if this hazard is plugged.
     * Only relevant for HoleInIce; other hazards always return false.
     * @return true if plugged, false otherwise
     */
    @Override
    public boolean isPlugged() {
        return plugged;
    }

    /**
     * Sets the plugged state of this hazard.
     * Only has effect for HoleInIce; other hazards ignore this.
     * @param plugged The new plugged state
     */
    @Override
    public void setPlugged(boolean plugged) {
        this.plugged = plugged;
    }

    /**
     * Returns a string representation of this hazard.
     * @return A string describing this hazard
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[pos=" + position + ", plugged=" + plugged + "]";
    }
}