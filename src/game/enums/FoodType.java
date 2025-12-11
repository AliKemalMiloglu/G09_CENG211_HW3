package game.enums;

import java.util.Random;

/**
 * FoodType enum representing the five types of food items in the game.
 * Each food type has a display symbol used in the game grid.
 */
public enum FoodType {
    KRILL("Kr"),
    CRUSTACEAN("Cr"),
    ANCHOVY("An"),
    SQUID("Sq"),
    MACKEREL("Ma");

    // The two-character symbol used to display this food type on the grid
    private final String symbol;

    // Random instance for generating random food types
    private static final Random random = new Random();

    /**
     * Constructor for FoodType enum.
     * @param symbol The display symbol for this food type
     */
    FoodType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the display symbol for this food type.
     * @return The two-character symbol (e.g., "Kr", "Cr", "An", "Sq", "Ma")
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the full name of the food type for display purposes.
     * @return The food type name with proper capitalization
     */
    public String getDisplayName() {
        switch (this) {
            case KRILL:
                return "Krill";
            case CRUSTACEAN:
                return "Crustacean";
            case ANCHOVY:
                return "Anchovy";
            case SQUID:
                return "Squid";
            case MACKEREL:
                return "Mackerel";
            default:
                return this.name();
        }
    }

    /**
     * Returns a random FoodType.
     * Each food type has an equal chance of being selected.
     * @return A randomly selected FoodType
     */
    public static FoodType random() {
        FoodType[] foodTypes = values();
        return foodTypes[random.nextInt(foodTypes.length)];
    }
}