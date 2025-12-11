package game.enums;

import java.util.Random;

/**
 * Direction enum representing the four cardinal directions.
 * Contains utility methods for movement calculations and parsing user input.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    // Delta values for row (Y) and column (X) movement
    private final int deltaY;
    private final int deltaX;

    // Random instance for generating random directions
    private static final Random random = new Random();

    /**
     * Constructor for Direction enum.
     * @param deltaY The change in Y (row) when moving in this direction
     * @param deltaX The change in X (column) when moving in this direction
     */
    Direction(int deltaY, int deltaX) {
        this.deltaY = deltaY;
        this.deltaX = deltaX;
    }

    /**
     * Returns the opposite direction.
     * UP ↔ DOWN, LEFT ↔ RIGHT
     * @return The opposite Direction
     */
    public Direction getOpposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return this; // Should never happen
        }
    }

    /**
     * Returns the change in X (column) when moving in this direction.
     * @return -1 for LEFT, 1 for RIGHT, 0 for UP/DOWN
     */
    public int getDeltaX() {
        return deltaX;
    }

    /**
     * Returns the change in Y (row) when moving in this direction.
     * @return -1 for UP, 1 for DOWN, 0 for LEFT/RIGHT
     */
    public int getDeltaY() {
        return deltaY;
    }

    /**
     * Parses a string input into a Direction.
     * Accepts "U", "D", "L", "R" (case-insensitive).
     * @param input The string to parse
     * @return The corresponding Direction
     * @throws IllegalArgumentException if the input is not valid
     */
    public static Direction fromString(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Direction input cannot be null or empty");
        }

        switch (input.toUpperCase()) {
            case "U":
                return UP;
            case "D":
                return DOWN;
            case "L":
                return LEFT;
            case "R":
                return RIGHT;
            default:
                throw new IllegalArgumentException(
                        "Invalid direction: " + input + ". Use U, D, L, or R.");
        }
    }

    /**
     * Returns a random Direction.
     * @return A randomly selected Direction
     */
    public static Direction random() {
        Direction[] directions = values();
        return directions[random.nextInt(directions.length)];
    }

    /**
     * Returns the full name of the direction for display purposes.
     * @return The direction name (e.g., "UPWARDS", "DOWNWARDS", "LEFT", "RIGHT")
     */
    public String getDisplayName() {
        switch (this) {
            case UP:
                return "UPWARDS";
            case DOWN:
                return "DOWNWARDS";
            case LEFT:
                return "LEFT";
            case RIGHT:
                return "RIGHT";
            default:
                return this.name();
        }
    }
}