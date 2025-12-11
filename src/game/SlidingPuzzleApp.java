package game;

import game.core.IcyTerrain;

/**
 * SlidingPuzzleApp - Main entry point for the Sliding Penguins Puzzle Game.
 *
 * This class contains the main method that initializes and launches the game.
 * The main method is deliberately minimal, delegating all game logic to IcyTerrain.
 * This follows the Single Responsibility Principle.
 *
 * Game Overview:
 * - 3 penguins compete on a 10x10 icy terrain
 * - Each penguin has 4 turns to collect food
 * - Penguins slide on ice until they hit something
 * - Various hazards affect penguin movement
 * - The penguin with the most food weight wins
 */
public class SlidingPuzzleApp {

    /**
     * Main entry point for the application.
     * Displays welcome message and starts the game.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Display welcome message
        System.out.println("Welcome to Sliding Penguins Puzzle Game App.");
        System.out.println("A 10x10 icy terrain grid is being generated.");
        System.out.println("Penguins, Hazards, and Food items are also being generated.");
        System.out.println();

        // Initialize and start the game
        IcyTerrain terrain = new IcyTerrain();
        terrain.startGame();
    }
}