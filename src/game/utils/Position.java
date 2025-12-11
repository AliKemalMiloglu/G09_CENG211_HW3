package game.utils;

import game.enums.Direction;

/**
 * Position class representing a coordinate (x, y) on the game grid.
 * This is a value object that encapsulates grid coordinates and provides
 * utility methods for movement calculations.
 *
 * Coordinate system:
 * - x represents the column (0 = leftmost, increases to the right)
 * - y represents the row (0 = topmost, increases downward)
 */
public class Position {

    /** The x-coordinate (column) of this position */
    private int x;

    /** The y-coordinate (row) of this position */
    private int y;

    /**
     * Creates a new Position with the specified coordinates.
     * @param x The x-coordinate (column)
     * @param y The y-coordinate (row)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor - creates a new Position with the same coordinates as another.
     * @param other The Position to copy
     */
    public Position(Position other) {
        this.x = other.x;
        this.y = other.y;
    }

    /**
     * Returns a new Position that results from moving one step in the given direction.
     * This method does not modify the current Position.
     * @param dir The direction to move
     * @return A new Position one step in the specified direction
     */
    public Position move(Direction dir) {
        return new Position(
                this.x + dir.getDeltaX(),
                this.y + dir.getDeltaY()
        );
    }

    /**
     * Returns a new Position that results from moving one step in the opposite
     * of the given direction (i.e., moving backward).
     * This method does not modify the current Position.
     * @param dir The direction to move back from
     * @return A new Position one step in the opposite direction
     */
    public Position moveBack(Direction dir) {
        return new Position(
                this.x - dir.getDeltaX(),
                this.y - dir.getDeltaY()
        );
    }

    /**
     * Checks if this position is within the bounds of the game grid.
     * @return true if the position is valid (within 0 to GRID_SIZE-1), false otherwise
     */
    public boolean isValid() {
        return x >= 0 && x < GameConstants.GRID_SIZE
                && y >= 0 && y < GameConstants.GRID_SIZE;
    }

    /**
     * Checks if this position is on the edge of the grid.
     * Edge positions are those where x or y is 0 or GRID_SIZE-1.
     * @return true if this position is on an edge, false otherwise
     */
    public boolean isEdge() {
        return x == 0 || x == GameConstants.GRID_SIZE - 1
                || y == 0 || y == GameConstants.GRID_SIZE - 1;
    }

    /**
     * Calculates the Manhattan distance to another position.
     * @param other The other position
     * @return The Manhattan distance (|x1-x2| + |y1-y2|)
     */
    public int distanceTo(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    /**
     * Returns the x-coordinate (column) of this position.
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate (row) of this position.
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the x-coordinate (column) of this position.
     * @param x The new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate (row) of this position.
     * @param y The new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Checks if this Position is equal to another object.
     * Two positions are equal if they have the same x and y coordinates.
     * @param obj The object to compare with
     * @return true if the positions are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position other = (Position) obj;
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Returns a hash code for this Position.
     * Consistent with equals() - equal positions have equal hash codes.
     * @return The hash code
     */
    @Override
    public int hashCode() {
        // Using a common hash code formula for two integers
        return 31 * x + y;
    }

    /**
     * Returns a string representation of this Position.
     * @return A string in the format "(x, y)"
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}