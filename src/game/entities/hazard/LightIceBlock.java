package game.entities.hazard;

import game.core.ISlidable;
import game.core.ITerrainObject;
import game.core.IcyTerrain;
import game.entities.penguin.Penguin;
import game.enums.Direction;
import game.utils.Position;

/**
 * LightIceBlock class - a slidable ice block hazard.
 *
 * This ice block can be pushed and will slide when hit. When a penguin
 * or another sliding hazard collides with it:
 * - The colliding object stops at the point of collision
 * - The LightIceBlock starts sliding in the same direction
 * - The colliding penguin is stunned (next turn skipped)
 *
 * A sliding LightIceBlock can:
 * - Fall off the edge of the grid
 * - Plug a HoleInIce if it falls in
 * - Push a SeaLion (transfers movement)
 * - Destroy food items in its path
 *
 * Implements ISlidable because it can slide across the ice.
 */
public class LightIceBlock extends Hazard implements ISlidable {

    /**
     * Default constructor.
     */
    public LightIceBlock() {
        super();
    }

    /**
     * Constructor with position.
     * @param position The position of this hazard on the grid
     */
    public LightIceBlock(Position position) {
        super(position);
    }

    /**
     * Copy constructor.
     * @param other The LightIceBlock to copy
     */
    public LightIceBlock(LightIceBlock other) {
        super(other);
    }

    /**
     * Applies the light ice block's effect when a penguin collides with it.
     * The penguin is stunned and the ice block starts sliding.
     * @param penguin The penguin that collided with this hazard
     * @param terrain The game terrain for processing the chain reaction
     */
    @Override
    public void applyEffect(Penguin penguin, IcyTerrain terrain) {
        // Penguin is stunned - their next turn is skipped
        penguin.setStunned(true);

        // This ice block starts sliding in the same direction the penguin was moving
        Direction slideDir = penguin.getLastMoveDirection();
        terrain.processHazardSlide(this, slideDir);
    }

    /**
     * Checks if this ice block can slide.
     * LightIceBlock can always slide.
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
     * Returns the display symbol for LightIceBlock.
     * @return "LB"
     */
    @Override
    public String getDisplaySymbol() {
        return "LB";
    }

    /**
     * Creates a deep copy of this LightIceBlock.
     * @return A new LightIceBlock with the same properties
     */
    @Override
    public ITerrainObject deepCopy() {
        return new LightIceBlock(this);
    }

    /**
     * Returns a string representation of this LightIceBlock.
     * @return A descriptive string
     */
    @Override
    public String toString() {
        return "LightIceBlock[pos=" + position + "]";
    }
}