package GameMode.gameutils;

/**
 * Class that represents a turn state.
 * A turn is considered complete when a player has moved, built, and used/skipped a god card.
 */
public class TurnState {

    // True if a worker has been moved
    private boolean moved = false;

    // True if a worker has built
    private boolean built = false;

    // True if a god power has been used or skipped
    private boolean godPowerUsedOrSkipped = false;

    // True if Buy Phase it is currently the buy phase
    private boolean buyPhaseCompleted = false;

    private boolean artifactUsedOrSkipped = false;

    /**
     * Constructor for turn state object
     */
    public TurnState(){
        resetTurn();
    }

    /**
     * Resets the Turn State Object
     */
    public void resetTurn(){
        moved = false;
        built = false;
        godPowerUsedOrSkipped = false;
        buyPhaseCompleted = false;
        artifactUsedOrSkipped = false;
    }

    /**
     * Checks is the player's turn is complete
     * @return true if all conditions are met
     */
    public boolean isTurnComplete(){
        return moved && built && godPowerUsedOrSkipped;
    }

    /**
     * Check if the player has moved a worker
     * @return true if a player's worker has been moved
     */
    public boolean hasMoved(){
        return moved;
    }

    /**
     * Sets the moved attribute of the turn
     * @param moved the boolean to set the moved attribute
     */
    public void setMoved(boolean moved){
        this.moved = moved;
    }

    /**
     * Check if the player has built a block
     * @return true if a block has been built during the turn
     */
    public boolean hasBuilt(){
        return built;
    }

    /**
     * Sets the built attribute of the turn
     * @param built the boolean to set the built attribute
     */
    public void setBuilt(boolean built){
        this.built = built;
    }

    /**
     * Check if the player has used or skipped a god power
     * @return true if a player has used or skipped a god power
     */
    public boolean hasUsedOrSkippedGodPower(){
        return godPowerUsedOrSkipped;
    }

    /**
     * Sets the status of the god power attribute of the turn
     * @param used the boolean to set the status of god power attribute
     */
    public void setGodPowerUsedOrSkipped(boolean used){
        this.godPowerUsedOrSkipped = used;
    }

    /**
     * Check if the player has used or skipped an artifact
     * @return true if a player has used or skipped an artifact
     */
    public boolean hasUsedOrSkippedArtifact(){
        return artifactUsedOrSkipped;
    }

    /**
     * Sets the status of the artifact usage of the turn
     * @param used the boolean to set the status of artifact usage
     */
    public void setUsedOrSkippedArtifact(boolean used){
        this.artifactUsedOrSkipped = used;
    }

    public boolean hasCompletedBuyPhase(){
        return this.buyPhaseCompleted;
    }

    public void setBuyPhaseCompleted(boolean buyPhaseCompleted){
        this.buyPhaseCompleted = buyPhaseCompleted;
    }

    /**
     * Sets the status of all the flags to true which would mean that the turn is complete.
     *
     */
    public void completeTurn(){
        this.moved = true;
        this.built = true;
        this.godPowerUsedOrSkipped = true;
        this.buyPhaseCompleted = true;
        this.artifactUsedOrSkipped = true;
    }


}
