package game.utils;

/**
 * GameConstants class containing all constant values used throughout the game.
 * This class centralizes magic numbers and configuration values to prevent
 * hardcoded values scattered throughout the codebase.
 *
 * This class cannot be instantiated - all fields are static final.
 */
public final class GameConstants {

    // Grid dimensions
    /** The size of the game grid (10x10) */
    public static final int GRID_SIZE = 10;

    // Entity counts
    /** The number of penguins in the game */
    public static final int PENGUIN_COUNT = 3;

    /** The number of hazards to generate */
    public static final int HAZARD_COUNT = 15;

    /** The number of food items to generate */
    public static final int FOOD_COUNT = 20;

    // Game mechanics
    /** The total number of turns each penguin gets */
    public static final int TOTAL_TURNS = 4;

    /** The minimum weight a food item can have */
    public static final int MIN_FOOD_WEIGHT = 1;

    /** The maximum weight a food item can have */
    public static final int MAX_FOOD_WEIGHT = 5;

    // AI behavior
    /** The probability (0.0 to 1.0) that an AI penguin will use its special action */
    public static final double AI_SPECIAL_ACTION_CHANCE = 0.30;

    // Display constants
    /** The width of each cell in the grid display */
    public static final int CELL_WIDTH = 4;

    /** The separator line used in grid display */
    public static final String GRID_SEPARATOR = "-".repeat((CELL_WIDTH + 1) * GRID_SIZE + 1);

    /**
     * Private constructor to prevent instantiation.
     * This class should only be used to access static constants.
     */
    private GameConstants() {
        throw new UnsupportedOperationException("GameConstants cannot be instantiated");
    }
}