package artifacts;

import Board.Board;
import Board.Cell;
import Player.Player;
import Player.Worker;

import java.util.List;

/**
 * Class representation of Zeus' Thunderbolt.
 * Players can use this artifact to destroy the highest block on a tower.
 * This artifact can only be used on towers with NO dome.
 */
public class Thunderbolt extends Artifact {
    private static final String ARTIFACT_NAME = "Zeus' Thunderbolt";

    // To buy this artifact, player has to offer three tokens
    private static final int COST = 3;

    public Thunderbolt() {
        super(ARTIFACT_NAME, COST);
    }

    /**
     * Checks whether this artifact can be used at this moment
     * Condition: there must be at least ONE cell that has a block
     * @param board the current game board
     * @return true if the artifact can be used
     */
    @Override
    public boolean canUse(Board board){
        List<Cell> cells = board.getAllCells();
        for(Cell cell : cells){
            int level = cell.getBlock().getLevel();
            if(level >= 1){
                return true;
            }
        }
        return false;
    }

    public ArtifactCellCondition getArtifactCondition(){
        return cell ->
                cell.getBlock() != null &&
                        !cell.isOccupied() &&
                        cell.getBlock().getLevel() >= 1 &&
                        cell.getBlock().getLevel() <= 3;
    }

    /**
     * Performs the action cause by the artifact.
     * It destroys a block on a tower and removes the artifact from the player's inventory.
     *
     * @param target the cell which contains the tower
     * @param player the player using the artifact
     * @return true if the action is successful
     */
    @Override
    public boolean performAction(Cell target, Player player) {
        if(target.isOccupied()){ // cannot destroy a block that a worker is standing on
            System.out.println("Cannot destroy a block that a worker is standing on");
            return false;
        }else{
            target.getBlock().destroy(); // destroy one block on the building, so level -1
            player.removeArtifact(this); // remove from player inventory
            return true;
        }

    }

    /**
     * Retrieves the description of the artifact and its effects.
     * Only used for UI.
     * @return description of the artifact
     */
    @Override
    public String getDescription(){
        return "Unleash the fury of the gods. " +
                "Once per game, the player may target an incomplete tower (without a dome) and destroy its topmost block. " +
                "The power cannot be used on completed towers. " +
                "Use it wisely to level the battlefield—or halt an opponent’s climb to victory.";
    }
}
