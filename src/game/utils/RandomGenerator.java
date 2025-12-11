package game.utils;

import java.util.List;
import java.util.Random;

/**
 * RandomGenerator class providing centralized random number generation.
 *
 * Centralizing randomness in one class provides several benefits:
 * - Easier to test (can use seeded Random for deterministic tests)
 * - Consistent random behavior across the application
 * - Single point of modification for random logic
 *
 * This class cannot be instantiated - all methods are static.
 */
public final class RandomGenerator {

    /** The shared Random instance used for all random operations */
    private static Random random = new Random();

    /**
     * Private constructor to prevent instantiation.
     */
    private RandomGenerator() {
        throw new UnsupportedOperationException("RandomGenerator cannot be instantiated");
    }

    /**
     * Sets the Random instance to use.
     * Useful for testing with a seeded Random for deterministic results.
     * @param newRandom The Random instance to use
     */
    public static void setRandom(Random newRandom) {
        random = newRandom;
    }

    /**
     * Resets the Random instance with a specific seed.
     * Useful for reproducible testing.
     * @param seed The seed for the Random instance
     */
    public static void setSeed(long seed) {
        random = new Random(seed);
    }

    /**
     * Returns a random integer between 0 (inclusive) and the specified bound (exclusive).
     * @param bound The upper bound (exclusive)
     * @return A random integer in the range [0, bound)
     */
    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }

    /**
     * Returns a random integer between min (inclusive) and max (inclusive).
     * @param min The minimum value (inclusive)
     * @param max The maximum value (inclusive)
     * @return A random integer in the range [min, max]
     */
    public static int nextInt(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    /**
     * Returns true with the given probability.
     * @param probability The probability (0.0 to 1.0) of returning true
     * @return true with the specified probability, false otherwise
     */
    public static boolean chance(double probability) {
        return random.nextDouble() < probability;
    }

    /**
     * Returns a random element from the given list.
     * @param <T> The type of elements in the list
     * @param list The list to select from
     * @return A randomly selected element from the list
     * @throws IllegalArgumentException if the list is null or empty
     */
    public static <T> T randomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Returns a random enum value from the given enum class.
     * @param <T> The enum type
     * @param enumClass The Class object of the enum
     * @return A randomly selected enum constant
     * @throws IllegalArgumentException if the enum class is null or has no constants
     */
    public static <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("Enum class cannot be null");
        }
        T[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null || enumConstants.length == 0) {
            throw new IllegalArgumentException("Enum class has no constants");
        }
        return enumConstants[random.nextInt(enumConstants.length)];
    }

    /**
     * Returns a random boolean value (50% chance of true).
     * @return A random boolean
     */
    public static boolean nextBoolean() {
        return random.nextBoolean();
    }

    /**
     * Returns a random double between 0.0 (inclusive) and 1.0 (exclusive).
     * @return A random double in the range [0.0, 1.0)
     */
    public static double nextDouble() {
        return random.nextDouble();
    }

    /**
     * Generates a random Position on the edge of the grid.
     * Edge positions have either x or y at 0 or GRID_SIZE-1.
     * @return A random Position on the grid edge
     */
    public static Position randomEdgePosition() {
        int edge = random.nextInt(4); // 0=top, 1=bottom, 2=left, 3=right
        int pos = random.nextInt(GameConstants.GRID_SIZE);

        switch (edge) {
            case 0: // Top edge
                return new Position(pos, 0);
            case 1: // Bottom edge
                return new Position(pos, GameConstants.GRID_SIZE - 1);
            case 2: // Left edge
                return new Position(0, pos);
            case 3: // Right edge
                return new Position(GameConstants.GRID_SIZE - 1, pos);
            default:
                return new Position(0, 0); // Should never happen
        }
    }

    /**
     * Generates a random Position anywhere on the grid.
     * @return A random Position within the grid bounds
     */
    public static Position randomPosition() {
        return new Position(
                random.nextInt(GameConstants.GRID_SIZE),
                random.nextInt(GameConstants.GRID_SIZE)
        );
    }

    /**
     * Generates a random food weight between MIN_FOOD_WEIGHT and MAX_FOOD_WEIGHT.
     * @return A random weight value
     */
    public static int randomFoodWeight() {
        return nextInt(GameConstants.MIN_FOOD_WEIGHT, GameConstants.MAX_FOOD_WEIGHT);
    }
}