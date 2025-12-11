package game.entities.hazard;

import game.core.ITerrainObject;
import game.core.IcyTerrain;
import game.entities.penguin.Penguin;
import game.utils.Position;

/**
 * HeavyIceBlock class - an immovable obstacle hazard.
 *
 * This ice block cannot be moved by anything. When a penguin collides with it:
 * - The penguin stops in its tracks (one square before the block)
 * - The penguin loses their lightest food item as a penalty
 * - If the penguin has no food, nothing happens
 *
 * HeavyIceBlock does NOT implement ISlidable because it cannot slide.
 */
public class HeavyIceBlock extends Hazard {

    /**
     * Default constructor.
     */
    public HeavyIceBlock() {
        super();
    }

    /**
     * Constructor with position.
     * @param position The position of this hazard on the grid
     */
    public HeavyIceBlock(Position position) {
        super(position);
    }

    /**
     * Copy constructor.
     * @param other The HeavyIceBlock to copy
     */
    public HeavyIceBlock(HeavyIceBlock other) {
        super(other);
    }

    /**
     * Applies the heavy ice block's effect when a penguin collides with it.
     * The penguin stops before the collision point and drops their lightest food.
     * @param penguin The penguin that collided with this hazard
     * @param terrain The game terrain (unused for this hazard)
     */
    @Override
    public void applyEffect(Penguin penguin, IcyTerrain terrain) {
        // Penguin stops one square before the collision
        Position stopPos = penguin.getPosition().moveBack(penguin.getLastMoveDirection());
        penguin.setPosition(stopPos);

        // Penguin drops lightest food as penalty
        penguin.dropLightestFood();
    }

    /**
     * Returns the display symbol for HeavyIceBlock.
     * @return "HB"
     */
    @Override
    public String getDisplaySymbol() {
        return "HB";
    }

    /**
     * Creates a deep copy of this HeavyIceBlock.
     * @return A new HeavyIceBlock with the same properties
     */
    @Override
    public ITerrainObject deepCopy() {
        return new HeavyIceBlock(this);
    }

    /**
     * Returns a string representation of this HeavyIceBlock.
     * @return A descriptive string
     */
    @Override
    public String toString() {
        return "HeavyIceBlock[pos=" + position + "]";
    }
}