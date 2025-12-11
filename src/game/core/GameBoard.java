package game.core;

import java.util.ArrayList;

import game.entities.food.Food;
import game.entities.hazard.Hazard;
import game.entities.penguin.Penguin;
import game.utils.GameConstants;
import game.utils.Position;

/**
 * GameBoard class - manages the grid data structure.
 * 
 * Responsibility: Store and manage terrain objects on a 10x10 grid.
 * Each cell can contain multiple objects (e.g., penguin on plugged hole).
 */
public class GameBoard {
    
    /** The 10x10 grid where each cell contains a list of terrain objects */
    private ArrayList<ArrayList<ArrayList<ITerrainObject>>> grid;
    
    /** List of all penguins in the game */
    private ArrayList<Penguin> penguins;
    
    /** List of all food items remaining on the grid */
    private ArrayList<Food> foodItems;
    
    /** List of all hazards on the grid */
    private ArrayList<Hazard> hazards;

    /**
     * Creates a new empty game board.
     */
    public GameBoard() {
        this.penguins = new ArrayList<>();
        this.foodItems = new ArrayList<>();
        this.hazards = new ArrayList<>();
        initializeGrid();
    }

    /**
     * Initializes the 10x10 grid with empty cell lists.
     */
    private void initializeGrid() {
        grid = new ArrayList<>();
        for (int y = 0; y < GameConstants.GRID_SIZE; y++) {
            ArrayList<ArrayList<ITerrainObject>> row = new ArrayList<>();
            for (int x = 0; x < GameConstants.GRID_SIZE; x++) {
                row.add(new ArrayList<ITerrainObject>());
            }
            grid.add(row);
        }
    }

    // ==================== Grid Access Methods ====================

    /**
     * Returns all objects at the specified position.
     * @param pos The position to check
     * @return ArrayList of ITerrainObjects at that position (empty list if none)
     */
    public ArrayList<ITerrainObject> getObjectsAt(Position pos) {
        if (!isPositionValid(pos)) {
            return new ArrayList<>();
        }
        return grid.get(pos.getY()).get(pos.getX());
    }

    /**
     * Adds an object to the specified position on the grid.
     * @param pos The position to add the object
     * @param obj The object to add
     */
    public void addObjectAt(Position pos, ITerrainObject obj) {
        if (isPositionValid(pos) && obj != null) {
            grid.get(pos.getY()).get(pos.getX()).add(obj);
        }
    }

    /**
     * Removes an object from the specified position on the grid.
     * @param pos The position to remove from
     * @param obj The object to remove
     */
    public void removeObjectAt(Position pos, ITerrainObject obj) {
        if (isPositionValid(pos) && obj != null) {
            grid.get(pos.getY()).get(pos.getX()).remove(obj);
        }
    }

    /**
     * Checks if a position is within the grid bounds.
     * @param pos The position to check
     * @return true if the position is valid (0-9 for both x and y)
     */
    public boolean isPositionValid(Position pos) {
        return pos != null && 
               pos.getX() >= 0 && pos.getX() < GameConstants.GRID_SIZE &&
               pos.getY() >= 0 && pos.getY() < GameConstants.GRID_SIZE;
    }

    /**
     * Checks if a position is empty (no objects at that position).
     * @param pos The position to check
     * @return true if the position is valid and has no objects
     */
    public boolean isPositionEmpty(Position pos) {
        if (!isPositionValid(pos)) {
            return false;
        }
        return getObjectsAt(pos).isEmpty();
    }

    // ==================== Entity List Management ====================

    /**
     * Adds a penguin to the board and tracking list.
     * @param penguin The penguin to add
     */
    public void addPenguin(Penguin penguin) {
        penguins.add(penguin);
        addObjectAt(penguin.getPosition(), penguin);
    }

    /**
     * Adds a hazard to the board and tracking list.
     * @param hazard The hazard to add
     */
    public void addHazard(Hazard hazard) {
        hazards.add(hazard);
        addObjectAt(hazard.getPosition(), hazard);
    }

    /**
     * Adds a food item to the board and tracking list.
     * @param food The food to add
     */
    public void addFood(Food food) {
        foodItems.add(food);
        addObjectAt(food.getPosition(), food);
    }

    /**
     * Removes a penguin from the game (fell into water or hole).
     * @param penguin The penguin to remove
     */
    public void removePenguin(Penguin penguin) {
        if (penguin != null) {
            removeObjectAt(penguin.getPosition(), penguin);
            penguin.setFallen(true);
        }
    }

    /**
     * Removes a food item from the board and tracking list.
     * @param food The food to remove
     */
    public void removeFood(Food food) {
        if (food != null) {
            removeObjectAt(food.getPosition(), food);
            foodItems.remove(food);
        }
    }

    /**
     * Removes a hazard from the board and tracking list.
     * @param hazard The hazard to remove
     */
    public void removeHazard(Hazard hazard) {
        if (hazard != null) {
            removeObjectAt(hazard.getPosition(), hazard);
            hazards.remove(hazard);
        }
    }

    // ==================== Getters ====================

    public ArrayList<Penguin> getPenguins() {
        return penguins;
    }

    public ArrayList<Food> getFoodItems() {
        return foodItems;
    }

    public ArrayList<Hazard> getHazards() {
        return hazards;
    }

    public ArrayList<ArrayList<ArrayList<ITerrainObject>>> getGrid() {
        return grid;
    }
}
