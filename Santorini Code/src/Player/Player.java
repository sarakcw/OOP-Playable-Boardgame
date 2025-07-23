package Player;

import GodCard.GodCard;
import artifacts.Artifact;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 * Each player has a name, an assigned GodCard (special ability),
 * a color for UI distinction, and a win status.
 */
public class Player {
    final private String name;
    final private GodCard god;
    private boolean winner;

    /* The colour representing the player in the UI. */
    final private Color color;

    private int tokens = 5;
    private List<Artifact> artifacts = new ArrayList<>();


    /**
     * Constructs a Player with a specified name, GodCard, and colour.
     *
     * @param name the name of the player
     * @param god the GodCard assigned to the player
     * @param color the colour representing the player
     */
    public Player(String name, GodCard god, Color color) {
        this.name = name;
        this.god = god;
        this.color = color;
        this.winner = false;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the GodCard assigned to the player.
     *
     * @return the player's GodCard
     */
    public GodCard getGod() {
        return god;
    }

    /**
     * Returns whether the player has won the game.
     *
     * @return true if the player has won, false otherwise
     */
    public boolean isWinner() {
        return winner;
    }

    /**
     * Sets the player's status to winner.
     */
    public void setWinner() {
        this.winner = true;
    }

    /**
     * Returns the colour representing the player.
     *
     * @return the player's colour
     */
    public Color getColor() {
        return color;
    }

    public int getTokens(){
        return this.tokens;
    }

    public void increaseTokens(int tokens){
        this.tokens += tokens;
    }
    public void decreaseTokens(int tokens){
        this.tokens -= tokens;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void addArtifact(Artifact artifact){
        this.artifacts.add(artifact);
    }

    public void removeArtifact(Artifact artifact){
        this.artifacts.remove(artifact);
    }
}
