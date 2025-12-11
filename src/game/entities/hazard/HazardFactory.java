package game.entities.hazard;

import game.utils.Position;
import game.utils.RandomGenerator;

/**
 * HazardFactory class - factory for creating hazard instances.
 *
 * This factory encapsulates the logic for creating different hazard types,
 * making it easier to create hazards without knowing the concrete class names.
 *
 * Usage:
 *   Hazard h = HazardFactory.createHazard(HazardType.LIGHT_ICE_BLOCK, position);
 *   Hazard random = HazardFactory.createRandomHazard(position);
 */
public class HazardFactory {

    /**
     * Enum representing the types of hazards.
     */
    public enum HazardType {
        LIGHT_ICE_BLOCK,
        HEAVY_ICE_BLOCK,
        SEA_LION,
        HOLE_IN_ICE
    }

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private HazardFactory() {
        throw new UnsupportedOperationException("HazardFactory cannot be instantiated");
    }

    /**
     * Creates a hazard of the specified type.
     * @param type The type of hazard to create
     * @param position The position on the grid
     * @return A new Hazard instance of the specified type
     * @throws IllegalArgumentException if the type is null or unknown
     */
    public static Hazard createHazard(HazardType type, Position position) {
        if (type == null) {
            throw new IllegalArgumentException("Hazard type cannot be null");
        }

        switch (type) {
            case LIGHT_ICE_BLOCK:
                return new LightIceBlock(position);
            case HEAVY_ICE_BLOCK:
                return new HeavyIceBlock(position);
            case SEA_LION:
                return new SeaLion(position);
            case HOLE_IN_ICE:
                return new HoleInIce(position);
            default:
                throw new IllegalArgumentException("Unknown hazard type: " + type);
        }
    }

    /**
     * Creates a hazard with a random type.
     * Each hazard type has an equal chance of being selected.
     * @param position The position on the grid
     * @return A new Hazard instance with a random type
     */
    public static Hazard createRandomHazard(Position position) {
        HazardType randomType = RandomGenerator.randomEnum(HazardType.class);
        return createHazard(randomType, position);
    }

    /**
     * Creates a deep copy of an existing hazard.
     * The copy will be of the same concrete type as the original.
     * @param hazard The hazard to copy
     * @return A new Hazard instance that is a copy of the original
     */
    public static Hazard copyHazard(Hazard hazard) {
        if (hazard == null) {
            return null;
        }
        return (Hazard) hazard.deepCopy();
    }
}