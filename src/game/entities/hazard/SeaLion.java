package game.entities.hazard;

import game.core.ISlidable;
import game.core.ITerrainObject;
import game.core.IcyTerrain;
import game.entities.penguin.Penguin;
import game.enums.Direction;
import game.utils.Position;

/**
 * SeaLion class - a slidable hazard that bounces penguins.
 *
 * When a penguin collides with a SeaLion:
 * - The penguin bounces and starts sliding in the OPPOSITE direction
 * - The SeaLion starts sliding in the ORIGINAL direction (movement transfer)
 * - Both can then collide with other objects or fall off the grid
 *
 * When a LightIceBlock collides with a SeaLion:
 * - The LightIceBlock stops
 * - The SeaLion starts sliding (movement transfer)
 *
 * A sliding SeaLion can:
 * - Fall off the edge of the grid
 * - Plug a HoleInIce if it falls in
 * - Stop when hitting a penguin (penguin unaffected)
 * - Destroy food items in its path
 *
 * Implements ISlidable because it can slide across the ice.
 */
public class SeaLion extends Hazard implements ISlidable {

    /**
     * Default constructor.
     */
    public SeaLion() {
        super();
    }

    /**
     * Constructor with position.
     * @param position The position of this hazard on the grid
     */
    public SeaLion(Position position) {
        super(position);
    }

    /**
     * Copy constructor.
     * @param other The SeaLion to copy
     */
    public SeaLion(SeaLion other) {
        super(other);
    }

    /**
     * Applies the sea lion's effect when a penguin collides with it.
     * The penguin bounces backward and the sea lion slides forward.
     * @param penguin The penguin that collided with this hazard
     * @param terrain The game terrain for processing the chain reactions
     */
    @Override
    public void applyEffect(Penguin penguin, IcyTerrain terrain) {
        // Get the direction the penguin was moving
        Direction originalDir = penguin.getLastMoveDirection();
        Direction bounceDir = originalDir.getOpposite();

        // Penguin bounces and slides in the opposite direction
        terrain.processPenguinSlide(penguin, bounceDir);

        // SeaLion starts sliding in the original direction (movement transfer)
        terrain.processHazardSlide(this, originalDir);
    }

    /**
     * Checks if this sea lion can slide.
     * SeaLion can always slide.
     * @return true
     */
    @Override
    public boolean canSlide() {
        return true;
    }

    /**
     * Initiates sliding movement in the specified direction.
     * @param direction The direction to slide
     * @param terrain The game terrain that handles the sliding logic
     */
    @Override
    public void initiateSlide(Direction direction, IcyTerrain terrain) {
        terrain.processHazardSlide(this, direction);
    }

    /**
     * Returns the display symbol for SeaLion.
     * @return "SL"
     */
    @Override
    public String getDisplaySymbol() {
        return "SL";
    }

    /**
     * Creates a deep copy of this SeaLion.
     * @return A new SeaLion with the same properties
     */
    @Override
    public ITerrainObject deepCopy() {
        return new SeaLion(this);
    }

    /**
     * Returns a string representation of this SeaLion.
     * @return A descriptive string
     */
    @Override
    public String toString() {
        return "SeaLion[pos=" + position + "]";
    }
}