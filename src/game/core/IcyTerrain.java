package game.core;

import java.util.ArrayList;

import game.entities.food.Food;
import game.entities.hazard.Hazard;
import game.entities.hazard.HeavyIceBlock;
import game.entities.hazard.HoleInIce;
import game.entities.hazard.LightIceBlock;
import game.entities.hazard.SeaLion;
import game.entities.penguin.EmperorPenguin;
import game.entities.penguin.KingPenguin;
import game.entities.penguin.Penguin;
import game.entities.penguin.RockhopperPenguin;
import game.entities.penguin.RoyalPenguin;
import game.enums.Direction;
import game.utils.GameConstants;
import game.utils.Position;

/**
 * IcyTerrain class - the main game orchestrator.
 *
 * This class has been refactored to follow the Single Responsibility Principle.
 * It delegates specific responsibilities to specialized classes:
 * - GameBoard: Grid data structure and object management
 * - GameInitializer: Entity generation and placement
 * - GameDisplay: Visual output and messages
 * - InputHandler: User input handling
 * - AIController: AI decision-making
 * - CollisionHandler: Movement and collision processing
 *
 * IcyTerrain orchestrates the game flow and provides a facade for penguin
 * classes that need to interact with the game.
 */
public class IcyTerrain {

    // Component classes
    private GameBoard board;
    private GameDisplay display;
    private InputHandler input;
    private AIController ai;
    private CollisionHandler collision;

    // Game state
    private Penguin playerPenguin;
    private int currentTurn;

    /**
     * Creates a new IcyTerrain and initializes the game.
     */
    public IcyTerrain() {
        // Initialize components
        this.board = new GameBoard();
        this.display = new GameDisplay(board);
        this.input = new InputHandler();
        this.ai = new AIController(board);
        this.collision = new CollisionHandler(board, display);

        // Initialize game
        GameInitializer initializer = new GameInitializer(board);
        this.playerPenguin = initializer.initializeGame();
        this.currentTurn = 1;
    }

    /**
     * Starts and runs the main game loop.
     */
    public void startGame() {
        // Display initial state
        display.displayInitialGridMessage();
        display.displayGrid();
        display.displayPenguinInfo(playerPenguin);

        // Run 4 turns
        for (currentTurn = 1; currentTurn <= GameConstants.TOTAL_TURNS; currentTurn++) {
            for (Penguin penguin : board.getPenguins()) {
                if (!penguin.hasFallen()) {
                    playTurn(penguin);
                }
            }
        }

        // Game over
        display.displayGameOver();
    }

    /**
     * Plays a single turn for a penguin.
     */
    private void playTurn(Penguin penguin) {
        display.displayTurnHeader(currentTurn, penguin);

        // Reset Rockhopper jump state at the start of each turn
        if (penguin instanceof RockhopperPenguin) {
            ((RockhopperPenguin) penguin).resetJump();
        }

        // Reset King/Emperor ability flag at the start of each turn
        if (penguin instanceof KingPenguin) {
            ((KingPenguin) penguin).resetAbilityForNewTurn();
        } else if (penguin instanceof EmperorPenguin) {
            ((EmperorPenguin) penguin).resetAbilityForNewTurn();
        }

        // Check if penguin is stunned
        if (penguin.isStunned()) {
            display.displayStunnedSkip(penguin);
            penguin.clearStun();
            display.displayGrid();
            return;
        }

        // Check if penguin has fallen
        if (penguin.hasFallen()) {
            display.displayFallenMessage(penguin);
            return;
        }

        // Handle turn
        if (penguin.isPlayerControlled()) {
            handlePlayerTurn(penguin);
        } else {
            handleAITurn(penguin);
        }

        // Display new grid state
        display.displayNewGridMessage();
        display.displayGrid();
    }

    // ==================== Turn Handling ====================

    /**
     * Handles a player-controlled penguin's turn.
     */
    private void handlePlayerTurn(Penguin penguin) {
        boolean useSpecial = false;
        Direction specialDirection = null;
        Direction slideDirection = null;

        if (!penguin.isSpecialActionUsed()) {
            useSpecial = input.promptSpecialAction(penguin);
        }

        // RoyalPenguin needs separate directions
        if (penguin instanceof RoyalPenguin) {
            if (useSpecial) {
                specialDirection = input.promptSpecialActionDirection(penguin);
                display.displaySpecialActionChoice(penguin, true);
                executeSpecialAction(penguin, specialDirection);
            } else if (!penguin.isSpecialActionUsed()) {
                display.displaySpecialActionChoice(penguin, false);
            }

            if (!penguin.hasFallen()) {
                slideDirection = input.promptMoveDirection(penguin);
                display.displayMoveChoice(penguin, slideDirection.getDisplayName());
                penguin.resetSlideCount();
                collision.processPenguinSlide(penguin, slideDirection);
            }
        } else {
            slideDirection = input.promptMoveDirection(penguin);

            if (useSpecial) {
                display.displaySpecialActionChoice(penguin, true);
                executeSpecialAction(penguin, slideDirection);
            } else if (!penguin.isSpecialActionUsed()) {
                display.displaySpecialActionChoice(penguin, false);
            }

            if (!penguin.hasFallen()) {
                display.displayMoveChoice(penguin, slideDirection.getDisplayName());
                penguin.resetSlideCount();
                collision.processPenguinSlide(penguin, slideDirection);
            }
        }
    }

    /**
     * Handles an AI-controlled penguin's turn.
     */
    private void handleAITurn(Penguin penguin) {
        Direction slideDirection = ai.chooseSlideDirection(penguin);

        if (!penguin.isSpecialActionUsed()) {
            boolean useSpecial = ai.shouldUseSpecialAction(penguin, slideDirection);

            if (useSpecial) {
                // Rockhopper uses ability automatically when hazard is in path
                if (penguin instanceof RockhopperPenguin) {
                    display.displayRockhopperAutoUse(penguin);
                } else {
                    display.displaySpecialActionChoice(penguin, true);
                }

                if (penguin instanceof RoyalPenguin) {
                    Direction stepDirection = ai.chooseRoyalStepDirection(penguin);
                    executeSpecialAction(penguin, stepDirection);

                    if (!penguin.hasFallen()) {
                        slideDirection = ai.chooseSlideDirection(penguin);
                    }
                } else {
                    executeSpecialAction(penguin, slideDirection);
                }
            } else {
                display.displaySpecialActionChoice(penguin, false);
            }
        }

        if (!penguin.hasFallen()) {
            display.displayMoveChoice(penguin, slideDirection.getDisplayName());
            penguin.resetSlideCount();
            collision.processPenguinSlide(penguin, slideDirection);
        }
    }

    // ==================== Special Action Handling ====================

    /**
     * Executes a penguin's special action.
     */
    private void executeSpecialAction(Penguin penguin, Direction direction) {
        boolean success = penguin.useSpecialAction(direction, this);

        switch (penguin.getType()) {
            case KING:
            case EMPEROR:
                // Passive during slide - no message needed
                break;
            case ROYAL:
                handleRoyalSpecialResult(penguin, direction, success);
                break;
            case ROCKHOPPER:
                if (success) {
                    display.displayRockhopperPrepareJump(penguin);
                }
                break;
        }
    }

    /**
     * Handles the result of a RoyalPenguin's special action.
     */
    private void handleRoyalSpecialResult(Penguin penguin, Direction direction, boolean success) {
        if (success) {
            display.displayRoyalStep(penguin, direction.getDisplayName());

            // Collect any food at new position
            ArrayList<ITerrainObject> objects = new ArrayList<>(board.getObjectsAt(penguin.getPosition()));
            for (ITerrainObject obj : objects) {
                if (obj instanceof Food) {
                    Food food = (Food) obj;
                    penguin.collectFood(food);
                    board.removeFood(food);
                    display.displayFoodCollection(penguin, food);
                }
            }
        } else if (penguin.hasFallen()) {
            Position targetPos = penguin.getPosition().move(direction);
            if (!board.isPositionValid(targetPos)) {
                display.displayRoyalStepIntoWater(penguin);
            } else {
                display.displayRoyalStepIntoHole(penguin);
            }
        } else {
            Position targetPos = penguin.getPosition().move(direction);
            String blockerName = getBlockerName(targetPos);
            display.displayRoyalStepBlocked(penguin, blockerName);
        }
    }

    /**
     * Gets the name of what's blocking a position.
     */
    private String getBlockerName(Position pos) {
        ArrayList<ITerrainObject> objects = board.getObjectsAt(pos);
        for (ITerrainObject obj : objects) {
            if (obj instanceof Penguin) return ((Penguin) obj).getName();
            if (obj instanceof LightIceBlock) return "LightIceBlock";
            if (obj instanceof HeavyIceBlock) return "HeavyIceBlock";
            if (obj instanceof SeaLion) return "SeaLion";
            if (obj instanceof Hazard) return "a hazard";
        }
        return "an obstacle";
    }

    // ==================== Facade Methods for Penguin Classes ====================

    public ArrayList<ITerrainObject> getObjectsAt(Position pos) {
        return board.getObjectsAt(pos);
    }

    public void addObjectAt(Position pos, ITerrainObject obj) {
        board.addObjectAt(pos, obj);
    }

    public void removeObjectAt(Position pos, ITerrainObject obj) {
        board.removeObjectAt(pos, obj);
    }

    public boolean isPositionValid(Position pos) {
        return board.isPositionValid(pos);
    }

    public void removePenguinFromGame(Penguin penguin) {
        board.removePenguin(penguin);
    }

    public void processPenguinSlide(Penguin penguin, Direction dir) {
        collision.processPenguinSlide(penguin, dir);
    }

    public void processHazardSlide(Hazard hazard, Direction dir) {
        collision.processHazardSlide(hazard, dir);
    }

    // ==================== Getters ====================

    public GameBoard getBoard() {
        return board;
    }

    public Penguin getPlayerPenguin() {
        return playerPenguin;
    }
}