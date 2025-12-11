package game.core;

import game.enums.Direction;

/**
 * ISlidable interface - marks objects that can slide across the ice.
 *
 * This interface separates slidable objects (Penguins, LightIceBlock, SeaLion)
 * from static ones (HeavyIceBlock, HoleInIce, Food). This distinction is crucial
 * for the collision detection system because when checking what happens during
 * a collision, we need to know if an object can start sliding as a result.
 *
 * Slidable objects continue moving in a direction until they:
 * - Hit another object
 * - Fall off the edge of the grid (into water)
 * - Use a special ability to stop (penguins only)
 */
public interface ISlidable {

    /**
     * Checks if this object is capable of sliding.
     * @return true if this object can slide, false otherwise
     */
    boolean canSlide();

    /**
     * Initiates sliding movement in the specified direction.
     * The object will continue sliding until it hits something or falls off the grid.
     * @param direction The direction to slide
     * @param terrain The game terrain that handles the sliding logic
     */
    void initiateSlide(Direction direction, IcyTerrain terrain);
}