package game.enums;

import java.util.Random;

/**
 * PenguinType enum representing the four types of penguins in the game.
 * Each penguin type has a unique special ability and display name.
 */
public enum PenguinType {
    KING("King Penguin"),
    EMPEROR("Emperor Penguin"),
    ROYAL("Royal Penguin"),
    ROCKHOPPER("Rockhopper Penguin");

    // The full display name for this penguin type
    private final String displayName;

    // Random instance for generating random penguin types
    private static final Random random = new Random();

    /**
     * Constructor for PenguinType enum.
     * @param displayName The full display name for this penguin type
     */
    PenguinType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the full display name of this penguin type.
     * @return The display name (e.g., "King Penguin", "Emperor Penguin")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns a random PenguinType.
     * Each penguin type has an equal chance of being selected.
     * @return A randomly selected PenguinType
     */
    public static PenguinType random() {
        PenguinType[] penguinTypes = values();
        return penguinTypes[random.nextInt(penguinTypes.length)];
    }

    /**
     * Returns a brief description of this penguin type's special ability.
     * @return A description of the special ability
     */
    public String getAbilityDescription() {
        switch (this) {
            case KING:
                return "Can stop at the fifth square when sliding";
            case EMPEROR:
                return "Can stop at the third square when sliding";
            case ROYAL:
                return "Can safely move one adjacent square before sliding";
            case ROCKHOPPER:
                return "Can jump over one hazard in their path";
            default:
                return "Unknown ability";
        }
    }
}