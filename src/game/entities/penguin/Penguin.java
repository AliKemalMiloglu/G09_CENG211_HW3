package game.entities.penguin;

import java.util.ArrayList;
import java.util.Comparator;

import game.core.ISlidable;
import game.core.ITerrainObject;
import game.core.IcyTerrain;
import game.entities.food.Food;
import game.enums.Direction;
import game.enums.PenguinType;
import game.utils.Position;

/**
 * Penguin abstract class - base class for all penguin types.
 *
 * This abstract class provides common behavior and state for all penguin types
 * while enforcing specialization through abstract methods. All penguins can
 * slide on ice, collect food, and have a unique special action.
 *
 * Penguin types:
 * - KingPenguin: Can stop at the 5th square when sliding
 * - EmperorPenguin: Can stop at the 3rd square when sliding
 * - RoyalPenguin: Can safely move one adjacent square before sliding
 * - RockhopperPenguin: Can jump over one hazard in their path
 *
 * Implements ITerrainObject for grid storage and ISlidable for movement.
 */
public abstract class Penguin implements ITerrainObject, ISlidable {

    /** The name of this penguin ("P1", "P2", "P3") */
    protected String name;

    /** The type of this penguin */
    protected PenguinType type;

    /** The current position on the grid */
    protected Position position;

    /** The food items this penguin has collected */
    protected ArrayList<Food> collectedFood;

    /** Whether the special action has been used */
    protected boolean specialActionUsed;

    /** Whether this penguin is stunned (skips next turn) */
    protected boolean isStunned;

    /** Whether this penguin has fallen into water or a hole */
    protected boolean hasFallen;

    /** Whether this penguin is controlled by the player */
    protected boolean isPlayerControlled;

    /** The last direction this penguin moved in (for collision handling) */
    protected Direction lastMoveDirection;

    /**
     * Default constructor.
     */
    public Penguin() {
        this.collectedFood = new ArrayList<>();
        this.specialActionUsed = false;
        this.isStunned = false;
        this.hasFallen = false;
        this.isPlayerControlled = false;
        this.lastMoveDirection = null;
    }

    /**
     * Constructor with name, type, and position.
     * @param name The penguin's name ("P1", "P2", "P3")
     * @param type The penguin's type
     * @param position The starting position on the grid
     */
    public Penguin(String name, PenguinType type, Position position) {
        this();
        this.name = name;
        this.type = type;
        this.position = position;
    }

    /**
     * Copy constructor for creating a deep copy.
     * @param other The penguin to copy
     */
    public Penguin(Penguin other) {
        this.name = other.name;
        this.type = other.type;
        this.position = new Position(other.position);
        this.collectedFood = new ArrayList<>();
        for (Food food : other.collectedFood) {
            this.collectedFood.add((Food) food.deepCopy());
        }
        this.specialActionUsed = other.specialActionUsed;
        this.isStunned = other.isStunned;
        this.hasFallen = other.hasFallen;
        this.isPlayerControlled = other.isPlayerControlled;
        this.lastMoveDirection = other.lastMoveDirection;
    }

    // ==================== Abstract Methods ====================

    /**
     * Uses this penguin's special action.
     * Each penguin type has a unique special action that can be used once per game.
     * @param dir The direction for the special action (if applicable)
     * @param terrain The game terrain
     * @return true if the special action was successful, false otherwise
     */
    public abstract boolean useSpecialAction(Direction dir, IcyTerrain terrain);

    /**
     * Checks if this penguin should stop at the given position due to special ability.
     * Used by KingPenguin (5th square) and EmperorPenguin (3rd square).
     * @param pos The position to check
     * @return true if the penguin should stop here, false otherwise
     */
    public abstract boolean shouldStopHere(Position pos);

    /**
     * Creates a deep copy of this penguin.
     * Each concrete penguin class must implement its own copy logic.
     * @return A new Penguin that is a copy of this one
     */
    @Override
    public abstract ITerrainObject deepCopy();

    // ==================== Concrete Methods ====================

    /**
     * Collects a food item and adds it to this penguin's inventory.
     * @param food The food item to collect
     */
    public void collectFood(Food food) {
        if (food != null) {
            collectedFood.add(food);
        }
    }

    /**
     * Drops the lightest food item from this penguin's inventory.
     * Called when colliding with a HeavyIceBlock.
     * If the penguin has no food, nothing happens.
     * @return The dropped food item, or null if no food was dropped
     */
    public Food dropLightestFood() {
        if (collectedFood.isEmpty()) {
            return null;
        }

        // Find the food with minimum weight
        Food lightest = collectedFood.stream()
                .min(Comparator.comparingInt(Food::getWeight))
                .orElse(null);

        if (lightest != null) {
            collectedFood.remove(lightest);
        }

        return lightest;
    }

    /**
     * Calculates the total weight of all collected food items.
     * @return The total weight in units
     */
    public int calculateTotalWeight() {
        return collectedFood.stream()
                .mapToInt(Food::getWeight)
                .sum();
    }

    /**
     * Initiates a slide in the given direction.
     * The penguin will continue sliding until they hit something or fall off the grid.
     * @param dir The direction to slide
     * @param terrain The game terrain that handles the sliding logic
     */
    public void slide(Direction dir, IcyTerrain terrain) {
        this.lastMoveDirection = dir;
        terrain.processPenguinSlide(this, dir);
    }

    /**
     * Checks if this penguin can slide.
     * Penguins can slide unless they have fallen.
     * @return true if the penguin can slide, false otherwise
     */
    @Override
    public boolean canSlide() {
        return !hasFallen;
    }

    /**
     * Initiates sliding movement in the specified direction.
     * Implementation of ISlidable interface.
     * @param dir The direction to slide
     * @param terrain The game terrain that handles the sliding logic
     */
    @Override
    public void initiateSlide(Direction dir, IcyTerrain terrain) {
        slide(dir, terrain);
    }

    /**
     * Returns the display symbol for this penguin.
     * @return The penguin's name ("P1", "P2", "P3")
     */
    @Override
    public String getDisplaySymbol() {
        return name;
    }

    /**
     * Returns the list of collected food items.
     * @return The ArrayList of collected Food items
     */
    public ArrayList<Food> getCollectedFood() {
        return collectedFood;
    }

    /**
     * Returns the current position of this penguin.
     * @return The Position on the grid
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of this penguin.
     * @param pos The new Position
     */
    @Override
    public void setPosition(Position pos) {
        this.position = pos;
    }

    /**
     * Returns the name of this penguin.
     * @return The name ("P1", "P2", "P3")
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this penguin.
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of this penguin.
     * @return The PenguinType
     */
    public PenguinType getType() {
        return type;
    }

    /**
     * Sets the type of this penguin.
     * @param type The new PenguinType
     */
    public void setType(PenguinType type) {
        this.type = type;
    }

    /**
     * Checks if this penguin's special action has been used.
     * @return true if the special action has been used
     */
    public boolean isSpecialActionUsed() {
        return specialActionUsed;
    }

    /**
     * Sets the special action used state.
     * @param used The new state
     */
    public void setSpecialActionUsed(boolean used) {
        this.specialActionUsed = used;
    }

    /**
     * Checks if this penguin is stunned.
     * @return true if stunned
     */
    public boolean isStunned() {
        return isStunned;
    }

    /**
     * Sets the stunned state.
     * @param stunned The new stunned state
     */
    public void setStunned(boolean stunned) {
        this.isStunned = stunned;
    }

    /**
     * Checks if this penguin has fallen (into water or a hole).
     * @return true if fallen
     */
    public boolean hasFallen() {
        return hasFallen;
    }

    /**
     * Sets the fallen state.
     * @param fallen The new fallen state
     */
    public void setFallen(boolean fallen) {
        this.hasFallen = fallen;
    }

    /**
     * Checks if this penguin is controlled by the player.
     * @return true if player-controlled
     */
    public boolean isPlayerControlled() {
        return isPlayerControlled;
    }

    /**
     * Sets whether this penguin is controlled by the player.
     * @param playerControlled The new state
     */
    public void setPlayerControlled(boolean playerControlled) {
        this.isPlayerControlled = playerControlled;
    }

    /**
     * Returns the last direction this penguin moved in.
     * Used by hazards to determine collision effects.
     * @return The last Direction moved, or null if not yet moved
     */
    public Direction getLastMoveDirection() {
        return lastMoveDirection;
    }

    /**
     * Sets the last move direction.
     * @param dir The direction
     */
    public void setLastMoveDirection(Direction dir) {
        this.lastMoveDirection = dir;
    }

    /**
     * Checks if this penguin can take a turn.
     * Penguins cannot take turns if they have fallen or are stunned.
     * @return true if the penguin can take a turn
     */
    public boolean canTakeTurn() {
        return !hasFallen && !isStunned;
    }

    /**
     * Clears the stunned state after a turn is skipped.
     */
    public void clearStun() {
        this.isStunned = false;
    }

    /**
     * Resets the slide counter for abilities that track squares slid.
     * Override in subclasses that use slide counting (King, Emperor).
     */
    public void resetSlideCount() {
        // Default implementation does nothing
        // Override in KingPenguin and EmperorPenguin
    }

    /**
     * Returns a formatted string of all collected food for the scoreboard.
     * Format: "Kr (3 units), Sq (2 units), An (5 units)"
     * @return The formatted food list string
     */
    public String getCollectedFoodString() {
        if (collectedFood.isEmpty()) {
            return "None";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < collectedFood.size(); i++) {
            sb.append(collectedFood.get(i).getScoreboardFormat());
            if (i < collectedFood.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of this penguin.
     * @return A descriptive string
     */
    @Override
    public String toString() {
        return name + " [" + type.getDisplayName() + " at " + position +
                ", food=" + calculateTotalWeight() + " units]";
    }

    /**
     * Checks if this penguin is equal to another object.
     * Two penguins are equal if they have the same name.
     * @param obj The object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Penguin)) {
            return false;
        }
        Penguin other = (Penguin) obj;
        return this.name != null && this.name.equals(other.name);
    }

    /**
     * Returns a hash code for this penguin.
     * @return The hash code based on the name
     */
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}