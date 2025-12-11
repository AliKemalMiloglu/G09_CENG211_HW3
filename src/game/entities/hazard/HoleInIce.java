package game.entities.hazard;

import game.core.ITerrainObject;
import game.core.IcyTerrain;
import game.entities.penguin.Penguin;
import game.utils.Position;

/**
 * HoleInIce class - a hole hazard that removes objects from the game.
 *
 * When an object slides into an unplugged HoleInIce:
 * - Penguins fall in and are removed from the game (but their collected food still counts)
 * - LightIceBlocks and SeaLions fall in and plug the hole
 *
 * When a hole is plugged:
 * - Objects can safely pass over it without falling
 * - The display symbol changes from "HI" to "PH"
 *
 * HoleInIce does NOT implement ISlidable because it cannot move.
 */
public class HoleInIce extends Hazard {

    /**
     * Default constructor.
     * Creates an unplugged hole.
     */
    public HoleInIce() {
        super();
        this.plugged = false;
    }

    /**
     * Constructor with position.
     * Creates an unplugged hole at the specified position.
     * @param position The position of this hazard on the grid
     */
    public HoleInIce(Position position) {
        super(position);
        this.plugged = false;
    }

    /**
     * Constructor with position and plugged state.
     * @param position The position of this hazard on the grid
     * @param plugged Whether the hole starts plugged
     */
    public HoleInIce(Position position, boolean plugged) {
        super(position);
        this.plugged = plugged;
    }

    /**
     * Copy constructor.
     * @param other The HoleInIce to copy
     */
    public HoleInIce(HoleInIce other) {
        super(other);
        this.plugged = other.plugged;
    }

    /**
     * Applies the hole's effect when a penguin collides with it.
     * If unplugged, the penguin falls in and is removed from the game.
     * If plugged, the penguin safely passes over.
     * @param penguin The penguin that collided with this hazard
     * @param terrain The game terrain for removing the penguin if they fall
     */
    @Override
    public void applyEffect(Penguin penguin, IcyTerrain terrain) {
        if (!plugged) {
            // Penguin falls into the hole
            penguin.setFallen(true);
            terrain.removePenguinFromGame(penguin);
        }
        // If plugged, penguin passes over safely (continues sliding)
        // The sliding logic in IcyTerrain handles the continuation
    }

    /**
     * Attempts to plug this hole with a falling object.
     * Only LightIceBlocks and SeaLions can plug holes.
     * @param obj The object falling into the hole
     * @return true if the hole was plugged, false otherwise
     */
    public boolean plugHole(ITerrainObject obj) {
        if (!plugged && (obj instanceof LightIceBlock || obj instanceof SeaLion)) {
            plugged = true;
            return true;
        }
        return false;
    }

    /**
     * Checks if this hole is plugged.
     * @return true if plugged, false otherwise
     */
    @Override
    public boolean isPlugged() {
        return plugged;
    }

    /**
     * Sets the plugged state of this hole.
     * @param plugged The new plugged state
     */
    @Override
    public void setPlugged(boolean plugged) {
        this.plugged = plugged;
    }

    /**
     * Returns the display symbol for HoleInIce.
     * Returns "HI" for unplugged holes, "PH" for plugged holes.
     * @return "HI" or "PH"
     */
    @Override
    public String getDisplaySymbol() {
        return plugged ? "PH" : "HI";
    }

    /**
     * Creates a deep copy of this HoleInIce.
     * @return A new HoleInIce with the same properties
     */
    @Override
    public ITerrainObject deepCopy() {
        return new HoleInIce(this);
    }

    /**
     * Returns a string representation of this HoleInIce.
     * @return A descriptive string
     */
    @Override
    public String toString() {
        return "HoleInIce[pos=" + position + ", plugged=" + plugged + "]";
    }
}