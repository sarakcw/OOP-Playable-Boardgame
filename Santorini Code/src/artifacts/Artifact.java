package artifacts;

import Board.Board;
import Board.Cell;
import Player.Player;
import Player.Worker;

public abstract class Artifact{

    //Name of the artifact
    private String name;

    // Cost of the artifact
    private int cost;

    public Artifact(String name, int cost){
        this.name = name;
        this.cost = cost;
    }
    public String getName(){
        return this.name;
    }

    public abstract String getDescription();


    public int getCost(){
        return this.cost;
    }

    public abstract boolean canUse(Board board);

    public abstract ArtifactCellCondition getArtifactCondition();

    public abstract boolean performAction(Cell target, Player player);
}
