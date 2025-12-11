package game.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import game.entities.food.Food;
import game.entities.hazard.Hazard;
import game.entities.penguin.Penguin;
import game.utils.GameConstants;

/**
 * GameDisplay class - handles all visual output.
 *
 * Responsibility: Display the grid, messages, and scoreboard.
 */
public class GameDisplay {

    private GameBoard board;

    /**
     * Creates a new GameDisplay for the given board.
     * @param board The game board to display
     */
    public GameDisplay(GameBoard board) {
        this.board = board;
    }

    /**
     * Displays the welcome message.
     */
    public void displayWelcome() {
        System.out.println("Welcome to Sliding Penguins Puzzle Game App.");
        System.out.println("A 10x10 icy terrain grid is being generated.");
        System.out.println("Penguins, Hazards, and Food items are also being generated.");
        System.out.println();
    }

    /**
     * Displays the initial grid message.
     */
    public void displayInitialGridMessage() {
        System.out.println("The initial icy terrain grid:");
    }

    /**
     * Displays information about all penguins.
     * @param playerPenguin The player-controlled penguin
     */
    public void displayPenguinInfo(Penguin playerPenguin) {
        System.out.println("These are the penguins on the icy terrain:");
        for (Penguin penguin : board.getPenguins()) {
            String playerMarker = penguin.isPlayerControlled() ? " ---> YOUR PENGUIN" : "";
            System.out.println("- Penguin " + penguin.getName().substring(1) + " (" +
                    penguin.getName() + "): " + penguin.getType().getDisplayName() + playerMarker);
        }
        System.out.println();
    }

    /**
     * Displays the current state of the grid.
     */
    public void displayGrid() {
        String separator = GameConstants.GRID_SEPARATOR;

        System.out.println(separator);

        for (int y = 0; y < GameConstants.GRID_SIZE; y++) {
            System.out.print("|");
            for (int x = 0; x < GameConstants.GRID_SIZE; x++) {
                ArrayList<ITerrainObject> objects = board.getGrid().get(y).get(x);
                String cellContent = "  ";

                if (!objects.isEmpty()) {
                    // Priority: Penguin > Hazard > Food
                    for (ITerrainObject obj : objects) {
                        if (obj instanceof Penguin) {
                            cellContent = obj.getDisplaySymbol();
                            break;
                        }
                    }
                    if (cellContent.equals("  ")) {
                        for (ITerrainObject obj : objects) {
                            if (obj instanceof Hazard) {
                                cellContent = obj.getDisplaySymbol();
                                break;
                            }
                        }
                    }
                    if (cellContent.equals("  ")) {
                        for (ITerrainObject obj : objects) {
                            if (obj instanceof Food) {
                                cellContent = obj.getDisplaySymbol();
                                break;
                            }
                        }
                    }
                }

                System.out.print(" " + String.format("%-2s", cellContent) + " |");
            }
            System.out.println();
            System.out.println(separator);
        }
        System.out.println();
    }

    /**
     * Displays the new grid state message.
     */
    public void displayNewGridMessage() {
        System.out.println("New state of the grid:");
    }

    /**
     * Displays the turn header.
     * @param turn Current turn number
     * @param penguin The penguin taking the turn
     */
    public void displayTurnHeader(int turn, Penguin penguin) {
        System.out.println("*** Turn " + turn + " – " + penguin.getName() +
                (penguin.isPlayerControlled() ? " (Your Penguin):" : ":"));
    }

    /**
     * Displays the final scoreboard.
     */
    public void displayScoreboard() {
        System.out.println("***** SCOREBOARD FOR THE PENGUINS *****");

        List<Penguin> sortedPenguins = board.getPenguins().stream()
                .sorted(Comparator.comparingInt(Penguin::calculateTotalWeight).reversed())
                .collect(Collectors.toList());

        String[] places = {"1st", "2nd", "3rd"};

        for (int i = 0; i < sortedPenguins.size(); i++) {
            Penguin penguin = sortedPenguins.get(i);
            String playerMarker = penguin.isPlayerControlled() ? " (Your Penguin)" : "";

            System.out.println("* " + places[i] + " place: " + penguin.getName() + playerMarker);
            System.out.println("  |---> Food items: " + penguin.getCollectedFoodString());
            System.out.println("  |---> Total weight: " + penguin.calculateTotalWeight() + " units");
        }
    }

    /**
     * Displays the game over message.
     */
    public void displayGameOver() {
        System.out.println();
        System.out.println("***** GAME OVER *****");
        System.out.println();
        displayScoreboard();
    }

    // ==================== Action Messages ====================

    public void displayStunnedSkip(Penguin penguin) {
        System.out.println(penguin.getName() + "'s turn is SKIPPED due to being stunned.");
    }

    public void displayFallenMessage(Penguin penguin) {
        System.out.println(penguin.getName() + " has fallen and cannot move.");
    }

    public void displaySpecialActionChoice(Penguin penguin, boolean uses) {
        if (uses) {
            System.out.println(penguin.getName() + " chooses to USE its special action.");
        } else {
            System.out.println(penguin.getName() + " does NOT use its special action.");
        }
    }

    public void displayRockhopperAutoUse(Penguin penguin) {
        System.out.println(penguin.getName() + " will automatically USE its special action.");
    }

    public void displayMoveChoice(Penguin penguin, String direction) {
        System.out.println(penguin.getName() + " chooses to move " + direction + ".");
    }

    public void displayFoodCollection(Penguin penguin, Food food) {
        System.out.println(penguin.getName() + " takes the " + food.getDisplayName() +
                " on the ground. (Weight=" + food.getWeight() + " units)");
    }

    public void displayFallIntoWater(Penguin penguin) {
        System.out.println(penguin.getName() + " falls into the water!");
        System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
    }

    public void displayFallIntoHole(Penguin penguin) {
        System.out.println(penguin.getName() + " falls into the HoleInIce!");
        System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
    }

    public void displayRemovedFromGame(Penguin penguin) {
        System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
    }

    public void displayPenguinCollision(Penguin moving, Penguin hit, String direction) {
        System.out.println(moving.getName() + " collides with " + hit.getName() + "!");
        System.out.println(hit.getName() + " starts sliding " + direction + ".");
    }

    public void displayStunned(Penguin penguin) {
        System.out.println(penguin.getName() + " hits a LightIceBlock and is STUNNED!");
    }

    public void displayHeavyIceBlockHit(Penguin penguin) {
        System.out.println(penguin.getName() + " hits a HeavyIceBlock!");
    }

    public void displayFoodDrop(Penguin penguin, Food food) {
        System.out.println(penguin.getName() + " drops " + food.getDisplayName() +
                " (" + food.getWeight() + " units) as a penalty.");
    }

    public void displaySeaLionBounce(Penguin penguin) {
        System.out.println(penguin.getName() + " hits a SeaLion and bounces back!");
    }

    public void displayHazardSlide(String hazardSymbol, String direction) {
        System.out.println(hazardSymbol + " starts sliding " + direction + ".");
    }

    public void displayHazardFallsInWater(String hazardSymbol) {
        System.out.println(hazardSymbol + " falls into the water!");
    }

    public void displayHazardDestroysFood(String hazardSymbol, String foodName) {
        System.out.println(hazardSymbol + " destroys " + foodName + "!");
    }

    public void displayHazardStopsNear(String hazardSymbol, String target) {
        System.out.println(hazardSymbol + " stops near " + target + ".");
    }

    public void displayHazardPlugsHole(String hazardSymbol) {
        System.out.println(hazardSymbol + " falls into HoleInIce and PLUGS it!");
    }

    public void displayHazardHitsHazard(String movingSymbol, String stationarySymbol) {
        System.out.println(movingSymbol + " stops at " + stationarySymbol + ".");
    }

    public void displaySeaLionHitsLightIceBlock() {
        System.out.println("SeaLion hits LightIceBlock!");
    }

    public void displayLightIceBlockHitsSeaLion() {
        System.out.println("LightIceBlock hits SeaLion!");
    }

    // Royal Penguin specific messages
    public void displayRoyalStep(Penguin penguin, String direction) {
        System.out.println(penguin.getName() + " moves one square to the " + direction + ".");
    }

    public void displayRoyalStepIntoWater(Penguin penguin) {
        System.out.println(penguin.getName() + " accidentally steps into the water!");
        displayRemovedFromGame(penguin);
    }

    public void displayRoyalStepIntoHole(Penguin penguin) {
        System.out.println(penguin.getName() + " accidentally steps into the HoleInIce!");
        displayRemovedFromGame(penguin);
    }

    public void displayRoyalStepBlocked(Penguin penguin, String blockerName) {
        System.out.println(penguin.getName() + " cannot step there! Blocked by " + blockerName + ".");
        System.out.println(penguin.getName() + "'s special action is wasted.");
    }

    // Rockhopper specific messages
    public void displayRockhopperPrepareJump(Penguin penguin) {
        System.out.println(penguin.getName() + " prepares to jump over a hazard in its path.");
    }

    public void displayRockhopperJump(Penguin penguin, String hazardName) {
        System.out.println(penguin.getName() + " jumps over " + hazardName + " in its path.");
    }

    public void displayRockhopperJumpFail(Penguin penguin) {
        System.out.println(penguin.getName() + " jumps but falls into the water!");
        displayRemovedFromGame(penguin);
    }

    public void displayRockhopperJumpBlocked(Penguin penguin) {
        System.out.println(penguin.getName() + " tries to jump but the landing spot is occupied!");
    }

    // King/Emperor specific
    public void displayVoluntaryStop(Penguin penguin) {
        System.out.println(penguin.getName() + " stops at an empty square using its special action.");
    }

    public void displaySlideDirection(Penguin penguin, String direction) {
        System.out.println(penguin.getName() + " slides " + direction + ".");
    }
}