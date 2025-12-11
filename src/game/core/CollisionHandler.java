package game.core;

import java.util.ArrayList;

import game.entities.food.Food;
import game.entities.hazard.Hazard;
import game.entities.hazard.HeavyIceBlock;
import game.entities.hazard.HoleInIce;
import game.entities.hazard.LightIceBlock;
import game.entities.hazard.SeaLion;
import game.entities.penguin.Penguin;
import game.entities.penguin.RockhopperPenguin;
import game.enums.Direction;
import game.utils.Position;

/**
 * CollisionHandler class - handles movement and collision processing.
 *
 * Responsibility: Process penguin and hazard slides, handle all collision types.
 */
public class CollisionHandler {

    private GameBoard board;
    private GameDisplay display;

    /**
     * Creates a new CollisionHandler.
     * @param board The game board
     * @param display The game display for messages
     */
    public CollisionHandler(GameBoard board, GameDisplay display) {
        this.board = board;
        this.display = display;
    }

    /**
     * Processes a penguin's slide in the given direction.
     * @param penguin The penguin sliding
     * @param dir The direction of movement
     */
    public void processPenguinSlide(Penguin penguin, Direction dir) {
        if (penguin.hasFallen()) {
            return;
        }

        penguin.setLastMoveDirection(dir);
        Position currentPos = penguin.getPosition();

        board.removeObjectAt(currentPos, penguin);

        while (true) {
            Position nextPos = currentPos.move(dir);

            // Check if falling off the grid
            if (!board.isPositionValid(nextPos)) {
                penguin.setFallen(true);
                display.displayFallIntoWater(penguin);
                return;
            }

            // Check for Rockhopper jump
            if (penguin instanceof RockhopperPenguin) {
                RockhopperPenguin rockhopper = (RockhopperPenguin) penguin;
                if (rockhopper.shouldJumpAt(nextPos)) {
                    if (handleRockhopperJump(rockhopper, nextPos, dir, currentPos)) {
                        return;
                    }
                    // If jump handling returned false but penguin didn't fall,
                    // continue with normal collision
                }
            }

            // Check for objects at next position
            ArrayList<ITerrainObject> objectsAtNext = board.getObjectsAt(nextPos);

            // Check for voluntary stop (King/Emperor ability)
            // Can only stop voluntarily on empty squares or squares with only food
            if (penguin.shouldStopHere(nextPos)) {
                boolean canStopHere = objectsAtNext.isEmpty() ||
                        objectsAtNext.stream().allMatch(obj -> obj instanceof Food);

                if (canStopHere) {
                    currentPos = nextPos;
                    penguin.setPosition(currentPos);
                    board.addObjectAt(currentPos, penguin);

                    // Collect any food at this position
                    for (ITerrainObject obj : new ArrayList<>(objectsAtNext)) {
                        if (obj instanceof Food) {
                            Food food = (Food) obj;
                            penguin.collectFood(food);
                            board.removeFood(food);
                            display.displayFoodCollection(penguin, food);
                        }
                    }

                    display.displayVoluntaryStop(penguin);
                    return;
                }
                // If can't stop here (hazard/penguin), continue to collision handling below
            }

            if (!objectsAtNext.isEmpty()) {
                // Check for food first
                for (ITerrainObject obj : new ArrayList<>(objectsAtNext)) {
                    if (obj instanceof Food) {
                        Food food = (Food) obj;
                        currentPos = nextPos;
                        penguin.setPosition(currentPos);
                        board.addObjectAt(currentPos, penguin);
                        penguin.collectFood(food);
                        board.removeFood(food);
                        display.displayFoodCollection(penguin, food);
                        return;
                    }
                }

                // Check for other objects
                for (ITerrainObject obj : objectsAtNext) {
                    if (obj instanceof Penguin) {
                        penguin.setPosition(currentPos);
                        board.addObjectAt(currentPos, penguin);
                        Penguin otherPenguin = (Penguin) obj;
                        display.displayPenguinCollision(penguin, otherPenguin, dir.getDisplayName());
                        processPenguinSlide(otherPenguin, dir);
                        return;
                    }

                    if (obj instanceof Hazard) {
                        handlePenguinHazardCollision(penguin, (Hazard) obj, currentPos, dir);
                        return;
                    }
                }
            }

            currentPos = nextPos;
        }
    }

    /**
     * Handles Rockhopper jump logic.
     * @return true if jump was handled (successfully or fell), false to continue normal collision
     */
    private boolean handleRockhopperJump(RockhopperPenguin rockhopper, Position hazardPos,
                                         Direction dir, Position currentPos) {
        Position landingPos = hazardPos.move(dir);

        if (!board.isPositionValid(landingPos)) {
            rockhopper.setFallen(true);
            display.displayRockhopperJumpFail(rockhopper);
            rockhopper.resetJump();
            return true;
        }

        ArrayList<ITerrainObject> landingObjects = board.getObjectsAt(landingPos);
        boolean canLand = landingObjects.isEmpty() ||
                landingObjects.stream().allMatch(obj -> obj instanceof Food);

        if (canLand) {
            display.displayRockhopperJump(rockhopper, getHazardNameAt(hazardPos));

            // Collect food at landing position
            for (ITerrainObject obj : new ArrayList<>(landingObjects)) {
                if (obj instanceof Food) {
                    Food food = (Food) obj;
                    rockhopper.collectFood(food);
                    board.removeFood(food);
                    display.displayFoodCollection(rockhopper, food);

                    // Stop at food
                    rockhopper.setPosition(landingPos);
                    board.addObjectAt(landingPos, rockhopper);
                    rockhopper.resetJump();
                    return true;
                }
            }

            // Continue sliding from landing position
            rockhopper.resetJump();
            rockhopper.setPosition(landingPos);
            board.addObjectAt(landingPos, rockhopper);
            board.removeObjectAt(landingPos, rockhopper);
            processPenguinSlide(rockhopper, dir);
            return true;
        } else {
            display.displayRockhopperJumpBlocked(rockhopper);
            return false; // Continue with normal collision handling
        }
    }

    /**
     * Handles collision between a penguin and a hazard.
     */
    private void handlePenguinHazardCollision(Penguin penguin, Hazard hazard,
                                              Position stopPos, Direction dir) {
        if (hazard instanceof HoleInIce) {
            HoleInIce hole = (HoleInIce) hazard;
            if (hole.isPlugged()) {
                Position nextPos = stopPos.move(dir);
                penguin.setPosition(nextPos);
                board.addObjectAt(nextPos, penguin);
                board.removeObjectAt(nextPos, penguin);
                processPenguinSlide(penguin, dir);
            } else {
                penguin.setFallen(true);
                display.displayFallIntoHole(penguin);
            }
        } else if (hazard instanceof LightIceBlock) {
            penguin.setPosition(stopPos);
            board.addObjectAt(stopPos, penguin);
            penguin.setStunned(true);
            display.displayStunned(penguin);
            display.displayHazardSlide("LightIceBlock", dir.getDisplayName());
            processHazardSlide((LightIceBlock) hazard, dir);
        } else if (hazard instanceof HeavyIceBlock) {
            penguin.setPosition(stopPos);
            board.addObjectAt(stopPos, penguin);
            display.displayHeavyIceBlockHit(penguin);
            Food droppedFood = penguin.dropLightestFood();
            if (droppedFood != null) {
                display.displayFoodDrop(penguin, droppedFood);
            }
        } else if (hazard instanceof SeaLion) {
            display.displaySeaLionBounce(penguin);
            Direction bounceDir = dir.getOpposite();

            display.displayHazardSlide("SeaLion", dir.getDisplayName());
            processHazardSlide((SeaLion) hazard, dir);

            penguin.setPosition(stopPos);
            board.addObjectAt(stopPos, penguin);
            display.displaySlideDirection(penguin, bounceDir.getDisplayName());
            board.removeObjectAt(stopPos, penguin);
            processPenguinSlide(penguin, bounceDir);
        }
    }

    /**
     * Processes a hazard's slide in the given direction.
     */
    public void processHazardSlide(Hazard hazard, Direction dir) {
        Position currentPos = hazard.getPosition();
        board.removeObjectAt(currentPos, hazard);

        while (true) {
            Position nextPos = currentPos.move(dir);

            // Check if falling off grid
            if (!board.isPositionValid(nextPos)) {
                board.removeHazard(hazard);
                display.displayHazardFallsInWater(hazard.getDisplaySymbol());
                return;
            }

            ArrayList<ITerrainObject> objectsAtNext = board.getObjectsAt(nextPos);

            if (!objectsAtNext.isEmpty()) {
                // First, check for blocking objects (penguin, hazards)
                ITerrainObject blockingObject = null;
                for (ITerrainObject obj : objectsAtNext) {
                    if (obj instanceof Penguin ||
                            (obj instanceof Hazard && !(obj instanceof HoleInIce && ((HoleInIce)obj).isPlugged()))) {
                        // Found a blocking object (penguin or unplugged hazard)
                        // Plugged holes are not blocking
                        if (obj instanceof HoleInIce && ((HoleInIce)obj).isPlugged()) {
                            continue;
                        }
                        blockingObject = obj;
                        break;
                    }
                }

                // If there's a blocking object, handle collision
                if (blockingObject != null) {
                    if (blockingObject instanceof Penguin) {
                        hazard.setPosition(currentPos);
                        board.addObjectAt(currentPos, hazard);
                        display.displayHazardStopsNear(hazard.getDisplaySymbol(), ((Penguin) blockingObject).getName());
                        return;
                    }

                    if (blockingObject instanceof HoleInIce) {
                        HoleInIce hole = (HoleInIce) blockingObject;
                        // Unplugged hole - hazard falls in and plugs it
                        hole.setPlugged(true);
                        board.removeHazard(hazard);
                        display.displayHazardPlugsHole(hazard.getDisplaySymbol());
                        return;
                    }

                    // LightIceBlock hits SeaLion → SeaLion slides
                    if (hazard instanceof LightIceBlock && blockingObject instanceof SeaLion) {
                        hazard.setPosition(currentPos);
                        board.addObjectAt(currentPos, hazard);
                        display.displayLightIceBlockHitsSeaLion();
                        display.displayHazardSlide("SeaLion", dir.getDisplayName());
                        processHazardSlide((SeaLion) blockingObject, dir);
                        return;
                    }

                    // SeaLion hits LightIceBlock → LightIceBlock slides
                    if (hazard instanceof SeaLion && blockingObject instanceof LightIceBlock) {
                        hazard.setPosition(currentPos);
                        board.addObjectAt(currentPos, hazard);
                        display.displaySeaLionHitsLightIceBlock();
                        display.displayHazardSlide("LightIceBlock", dir.getDisplayName());
                        processHazardSlide((LightIceBlock) blockingObject, dir);
                        return;
                    }

                    // Generic hazard collision - just stop
                    if (blockingObject instanceof Hazard) {
                        hazard.setPosition(currentPos);
                        board.addObjectAt(currentPos, hazard);
                        display.displayHazardHitsHazard(hazard.getDisplaySymbol(), ((Hazard) blockingObject).getDisplaySymbol());
                        return;
                    }
                }

                // No blocking object - destroy any food and continue
                for (ITerrainObject obj : new ArrayList<>(objectsAtNext)) {
                    if (obj instanceof Food) {
                        Food food = (Food) obj;
                        board.removeFood(food);
                        display.displayHazardDestroysFood(hazard.getDisplaySymbol(), food.getDisplayName());
                    }
                }
            }

            // Move to next position
            currentPos = nextPos;
        }
    }

    /**
     * Gets the name of the hazard at a position.
     */
    private String getHazardNameAt(Position pos) {
        for (ITerrainObject obj : board.getObjectsAt(pos)) {
            if (obj instanceof LightIceBlock) return "LightIceBlock";
            if (obj instanceof HeavyIceBlock) return "HeavyIceBlock";
            if (obj instanceof SeaLion) return "SeaLion";
            if (obj instanceof HoleInIce) return "HoleInIce";
        }
        return "hazard";
    }
}