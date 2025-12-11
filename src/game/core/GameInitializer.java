package game.core;

import game.entities.food.Food;
import game.entities.hazard.Hazard;
import game.entities.hazard.HazardFactory;
import game.entities.penguin.Penguin;
import game.entities.penguin.PenguinFactory;
import game.utils.GameConstants;
import game.utils.Position;
import game.utils.RandomGenerator;

/**
 * GameInitializer class - handles game setup and entity generation.
 * 
 * Responsibility: Generate and place penguins, hazards, and food on the board.
 */
public class GameInitializer {
    
    private GameBoard board;

    /**
     * Creates a new GameInitializer for the given board.
     * @param board The game board to initialize
     */
    public GameInitializer(GameBoard board) {
        this.board = board;
    }

    /**
     * Initializes the entire game by generating all entities.
     * @return The player-controlled penguin
     */
    public Penguin initializeGame() {
        generatePenguins();
        generateHazards();
        generateFood();
        return assignPlayerPenguin();
    }

    /**
     * Generates 3 penguins and places them on random edge positions.
     */
    private void generatePenguins() {
        for (int i = 0; i < GameConstants.PENGUIN_COUNT; i++) {
            String name = "P" + (i + 1);
            Position pos = getRandomEdgePosition();
            
            Penguin penguin = PenguinFactory.createRandomPenguin(name, pos);
            board.addPenguin(penguin);
        }
    }

    /**
     * Generates 15 hazards and places them on random empty positions.
     */
    private void generateHazards() {
        for (int i = 0; i < GameConstants.HAZARD_COUNT; i++) {
            Position pos = getRandomEmptyPosition();
            
            Hazard hazard = HazardFactory.createRandomHazard(pos);
            board.addHazard(hazard);
        }
    }

    /**
     * Generates 20 food items and places them on random empty positions.
     */
    private void generateFood() {
        for (int i = 0; i < GameConstants.FOOD_COUNT; i++) {
            Position pos = getRandomEmptyPosition();
            
            Food food = new Food(pos);
            board.addFood(food);
        }
    }

    /**
     * Randomly assigns one penguin to the player.
     * @return The player-controlled penguin
     */
    private Penguin assignPlayerPenguin() {
        int playerIndex = RandomGenerator.nextInt(board.getPenguins().size());
        Penguin playerPenguin = board.getPenguins().get(playerIndex);
        playerPenguin.setPlayerControlled(true);
        return playerPenguin;
    }

    /**
     * Returns a random edge position that is not already occupied.
     * @return A random empty edge Position
     */
    private Position getRandomEdgePosition() {
        Position pos;
        do {
            pos = RandomGenerator.randomEdgePosition();
        } while (!board.isPositionEmpty(pos));
        return pos;
    }

    /**
     * Returns a random position that is not occupied.
     * @return A random empty Position
     */
    private Position getRandomEmptyPosition() {
        Position pos;
        do {
            pos = RandomGenerator.randomPosition();
        } while (!board.isPositionEmpty(pos));
        return pos;
    }
}
