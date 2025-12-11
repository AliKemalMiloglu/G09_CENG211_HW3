package game.core;

import game.utils.Position;

/**
 * ITerrainObject interface - the foundation of polymorphic design.
 *
 * This interface defines the contract that all objects placeable on the grid must fulfill.
 * By having all grid objects implement this interface, the IcyTerrain class can store
 * heterogeneous collections of penguins, food, and hazards in a unified data structure.
 *
 * This is a classic application of the Liskov Substitution Principle - any ITerrainObject
 * can be used wherever an ITerrainObject is expected, regardless of its concrete type.
 */
public interface ITerrainObject {

    /**
     * Returns the current position of this object on the grid.
     * @return The Position of this object
     */
    Position getPosition();

    /**
     * Sets the position of this object on the grid.
     * @param pos The new Position for this object
     */
    void setPosition(Position pos);

    /**
     * Returns the display symbol for this object.
     * Used when rendering the grid to the console.
     * @return A short string symbol (e.g., "P1", "Kr", "HB")
     */
    String getDisplaySymbol();

    /**
     * Creates a deep copy of this object.
     * Useful for state management and undo functionality.
     * @return A new ITerrainObject that is a copy of this one
     */
    ITerrainObject deepCopy();
}