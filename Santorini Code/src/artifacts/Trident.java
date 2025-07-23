package artifacts;

import Board.Board;
import Board.Cell;
import Player.Player;

import java.util.List;

public class Trident extends Artifact{
    private static final String ARTIFACT_NAME = "Poseidon's Trident";

    // To buy this artifact, player has to offer one token
    private static final int COST = 2;

    public Trident() {
        super(ARTIFACT_NAME, COST);
    }

    /**
     * Retrieves the description of the Trident artifact.
     * Used in frontend
     * @return the string description of this artifact
     */
    @Override
    public String getDescription() {
        return "Command the tides to reshape the land. " +
                "Once per game, the player may flood a tile, turning it into impassable terrain. " +
                "Flooded tiles cannot be built upon or moved onto by any worker. " +
                "Use this power to block key paths or deny your opponents strategic positions.";
    }

    /**
     * Checks whether this artifact can be used at this moment
     * Condition: Cell cannot be occupied by a building or worker
     * @param board the current game board
     * @return true if the artifact can be used
     */
    @Override
    public boolean canUse(Board board) {
        List<Cell> cells = board.getAllCells();
        for(Cell cell : cells){
            int level = cell.getBlock().getLevel();
            if(level == 0 && !cell.isOccupied()){
                return true;
            }
        }
        return false;
    }


    public ArtifactCellCondition getArtifactCondition(){
        return cell ->
                !cell.isOccupied() && !cell.isFlooded() &&
                        cell.getBlock().getLevel() == 0;
    }


    @Override
    public boolean performAction(Cell target, Player player) {
        if (target.isOccupied() || target.getBlock().hasDome() || target.isFlooded()) {
            System.out.println("Invalid target for flooding.");
            return false;
        }
        target.flood();
        player.removeArtifact(this); // remove from player inventory
        return true;
    }


}
