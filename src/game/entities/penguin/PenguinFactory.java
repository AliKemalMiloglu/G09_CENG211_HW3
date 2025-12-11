package game.entities.penguin;

import game.enums.PenguinType;
import game.utils.Position;
import game.utils.RandomGenerator;

/**
 * PenguinFactory class - factory for creating penguin instances.
 *
 * This factory encapsulates the logic for creating different penguin types,
 * making it easier to create penguins without knowing the concrete class names.
 *
 * Usage:
 *   Penguin p = PenguinFactory.createPenguin(PenguinType.KING, "P1", position);
 *   Penguin random = PenguinFactory.createRandomPenguin("P2", position);
 */
public class PenguinFactory {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private PenguinFactory() {
        throw new UnsupportedOperationException("PenguinFactory cannot be instantiated");
    }

    /**
     * Creates a penguin of the specified type.
     * @param type The type of penguin to create
     * @param name The penguin's name ("P1", "P2", "P3")
     * @param position The starting position on the grid
     * @return A new Penguin instance of the specified type
     * @throws IllegalArgumentException if the type is null or unknown
     */
    public static Penguin createPenguin(PenguinType type, String name, Position position) {
        if (type == null) {
            throw new IllegalArgumentException("Penguin type cannot be null");
        }

        switch (type) {
            case KING:
                return new KingPenguin(name, position);
            case EMPEROR:
                return new EmperorPenguin(name, position);
            case ROYAL:
                return new RoyalPenguin(name, position);
            case ROCKHOPPER:
                return new RockhopperPenguin(name, position);
            default:
                throw new IllegalArgumentException("Unknown penguin type: " + type);
        }
    }

    /**
     * Creates a penguin with a random type.
     * Each penguin type has an equal chance of being selected.
     * @param name The penguin's name ("P1", "P2", "P3")
     * @param position The starting position on the grid
     * @return A new Penguin instance with a random type
     */
    public static Penguin createRandomPenguin(String name, Position position) {
        PenguinType randomType = RandomGenerator.randomEnum(PenguinType.class);
        return createPenguin(randomType, name, position);
    }

    /**
     * Creates a deep copy of an existing penguin.
     * The copy will be of the same concrete type as the original.
     * @param penguin The penguin to copy
     * @return A new Penguin instance that is a copy of the original
     */
    public static Penguin copyPenguin(Penguin penguin) {
        if (penguin == null) {
            return null;
        }
        return (Penguin) penguin.deepCopy();
    }
}