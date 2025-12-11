package game.core;

import game.entities.penguin.Penguin;

/**
 * IHazard interface - marks hazard objects and defines collision effects.
 *
 * This interface enables polymorphic hazard handling. When a penguin collides
 * with any hazard, the collision system can call the same method regardless
 * of hazard type, and each hazard will execute its unique effect through
 * method overriding.
 *
 * Hazard types include:
 * - LightIceBlock: Slidable, stuns penguin on collision
 * - HeavyIceBlock: Immovable, causes penguin to drop lightest food
 * - SeaLion: Slidable, bounces penguin backward
 * - HoleInIce: Removes objects that fall in, can be plugged
 */
public interface IHazard {

    /**
     * Applies the hazard's effect when a penguin collides with it.
     * Each hazard type implements its own unique collision behavior.
     * @param penguin The penguin that collided with this hazard
     * @param terrain The game terrain for processing chain reactions
     */
    void applyEffect(Penguin penguin, IcyTerrain terrain);

    /**
     * Checks if this hazard is plugged (relevant for HoleInIce).
     * For non-hole hazards, this always returns false.
     * @return true if the hazard is plugged, false otherwise
     */
    boolean isPlugged();

    /**
     * Sets the plugged state of this hazard (relevant for HoleInIce).
     * For non-hole hazards, this method has no effect.
     * @param plugged The new plugged state
     */
    void setPlugged(boolean plugged);
}