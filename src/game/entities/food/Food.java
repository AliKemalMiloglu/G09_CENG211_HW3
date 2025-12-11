package game.entities.food;

import game.core.ITerrainObject;
import game.enums.FoodType;
import game.utils.Position;
import game.utils.RandomGenerator;

/**
 * Food class representing a food item on the icy terrain.
 *
 * Food items can be collected by penguins to score points. Each food item has:
 * - A type (Krill, Crustacean, Anchovy, Squid, Mackerel)
 * - A weight (1-5 units) that determines its point value
 * - A position on the grid
 *
 * Food items are stationary and cannot slide. When a penguin reaches a food item,
 * the penguin stops on that square and collects the food instantly.
 *
 * Implements ITerrainObject for polymorphic storage in the grid.
 */
public class Food implements ITerrainObject {

    /** The type of this food item */
    private FoodType foodType;

    /** The weight of this food item (1-5 units) */
    private int weight;

    /** The position of this food item on the grid */
    private Position position;

    /**
     * Creates a new Food item with the specified type, weight, and position.
     * @param foodType The type of food
     * @param weight The weight of the food (1-5 units)
     * @param position The position on the grid
     */
    public Food(FoodType foodType, int weight, Position position) {
        this.foodType = foodType;
        this.weight = weight;
        this.position = position;
    }

    /**
     * Creates a new Food item with random type and weight at the specified position.
     * @param position The position on the grid
     */
    public Food(Position position) {
        this.foodType = FoodType.random();
        this.weight = RandomGenerator.randomFoodWeight();
        this.position = position;
    }

    /**
     * Copy constructor - creates a deep copy of another Food item.
     * @param other The Food item to copy
     */
    public Food(Food other) {
        this.foodType = other.foodType;
        this.weight = other.weight;
        this.position = new Position(other.position);
    }

    /**
     * Returns the type of this food item.
     * @return The FoodType
     */
    public FoodType getFoodType() {
        return foodType;
    }

    /**
     * Returns the weight of this food item.
     * @return The weight (1-5 units)
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Returns the display symbol for this food item.
     * The symbol is based on the food type (e.g., "Kr", "Cr", "An", "Sq", "Ma").
     * @return The two-character display symbol
     */
    @Override
    public String getDisplaySymbol() {
        return foodType.getSymbol();
    }

    /**
     * Returns the current position of this food item.
     * @return The Position on the grid
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of this food item.
     * @param pos The new Position
     */
    @Override
    public void setPosition(Position pos) {
        this.position = pos;
    }

    /**
     * Creates a deep copy of this Food item.
     * @return A new Food object with the same properties
     */
    @Override
    public ITerrainObject deepCopy() {
        return new Food(this);
    }

    /**
     * Returns the full display name of this food type.
     * @return The food type name (e.g., "Krill", "Crustacean")
     */
    public String getDisplayName() {
        return foodType.getDisplayName();
    }

    /**
     * Returns a formatted string showing the food type and weight.
     * Format: "FoodType (weight units)" e.g., "Krill (3 units)"
     * @return A formatted description string
     */
    public String getDescription() {
        return foodType.getDisplayName() + " (" + weight + " units)";
    }

    /**
     * Returns a formatted string for scoreboard display.
     * Format: "Symbol (weight units)" e.g., "Kr (3 units)"
     * @return A formatted scoreboard string
     */
    public String getScoreboardFormat() {
        return foodType.getSymbol() + " (" + weight + " units)";
    }

    /**
     * Returns a string representation of this Food item.
     * @return A string describing this food item
     */
    @Override
    public String toString() {
        return "Food[" + foodType.getDisplayName() + ", weight=" + weight + ", pos=" + position + "]";
    }

    /**
     * Checks if this Food item is equal to another object.
     * Two food items are equal if they have the same type, weight, and position.
     * @param obj The object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Food other = (Food) obj;
        return this.weight == other.weight
                && this.foodType == other.foodType
                && this.position.equals(other.position);
    }

    /**
     * Returns a hash code for this Food item.
     * @return The hash code
     */
    @Override
    public int hashCode() {
        int result = foodType.hashCode();
        result = 31 * result + weight;
        result = 31 * result + position.hashCode();
        return result;
    }
}