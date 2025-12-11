package game.core;

import java.util.Scanner;

import game.entities.penguin.Penguin;
import game.enums.Direction;

/**
 * InputHandler class - handles all user input.
 * 
 * Responsibility: Prompt for and validate user input.
 */
public class InputHandler {
    
    private Scanner scanner;

    /**
     * Creates a new InputHandler.
     */
    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Creates a new InputHandler with an existing scanner.
     * @param scanner The scanner to use
     */
    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Prompts the user for a yes/no response.
     * @param penguin The penguin the question is about
     * @return true for yes, false for no
     */
    public boolean promptSpecialAction(Penguin penguin) {
        String prompt = "Will " + penguin.getName() + " use its special action? Answer with Y or N";
        return promptYesNo(prompt);
    }

    /**
     * Prompts the user for a direction.
     * @param penguin The penguin that will move
     * @return The chosen Direction
     */
    public Direction promptMoveDirection(Penguin penguin) {
        String prompt = "Which direction will " + penguin.getName() + 
                       " move? Answer with U (Up), D (Down), L (Left), R (Right)";
        return promptDirection(prompt);
    }

    /**
     * Prompts the user for a special action direction.
     * @param penguin The penguin using the special action
     * @return The chosen Direction
     */
    public Direction promptSpecialActionDirection(Penguin penguin) {
        String prompt = "Which direction will " + penguin.getName() + 
                       " use its special action? Answer with U (Up), D (Down), L (Left), R (Right)";
        return promptDirection(prompt);
    }

    /**
     * Prompts the user for a yes/no response.
     * @param prompt The prompt to display
     * @return true for yes, false for no
     */
    private boolean promptYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " --> ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) {
                return true;
            } else if (input.equals("N")) {
                return false;
            }
            System.out.println("Invalid input. Please enter Y or N.");
        }
    }

    /**
     * Prompts the user for a direction.
     * @param prompt The prompt to display
     * @return The chosen Direction
     */
    private Direction promptDirection(String prompt) {
        while (true) {
            System.out.print(prompt + " --> ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return Direction.fromString(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please enter U, D, L, or R.");
            }
        }
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
