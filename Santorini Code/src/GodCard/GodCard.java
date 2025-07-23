package GodCard;

import Board.Cell;
import GameMode.gameutils.TurnState;
import Player.Player;
import Player.Worker;
import Board.Board;
import Board.BoardHighlighter;

import javax.swing.*;

/**
 * Abstract base class representing a God with special powers.
 *
 */
public abstract class GodCard {
    protected String name;
    final private PowerPhase powerPhase;

    /**
     * Constructs a God with a given name.
     *
     * @param name The name of the god.
     * @param powerPhase the phase at which the god card can be used (move/build phase).
     */
    public GodCard(String name, PowerPhase powerPhase) {
        this.name = name;
        this.powerPhase = powerPhase;
    }


    public abstract ImageIcon getCardImg();

    /**
     * Determines if the player can use the god's power at this time.
     *
     * @param board The current board instance.
     * @param player The player attempting to use the god power.
     * @return true if the god power can be used; false otherwise.
     */
    public abstract boolean godPowerAvailable(Board board, Player player, TurnState turnState, Worker worker);

    /**
     * Applies the god's special power effect to the game.
     *
     * @param board The current board instance.
     * @param player The player using the god power.
     */
    public abstract void usingGodPower(Board board, BoardHighlighter highlighter, Player player, Worker worker);

    /**
     * Gets the name of the god.
     *
     * @return The name of the god.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Performs the special action enabled by the god (extra move or build).
     * Must return true if the action was successful to update turn status.
     *
     * @param board The board instance.
     * @param worker The current worker.
     * @param target The target cell for the god power.
     * @return true if the action was valid and completed.
     * @since Sprint 2
     */
    public boolean performExtraAction(Board board, BoardHighlighter highlighter,  Worker worker, Cell target, TurnState turnState) { return false; }

    /**
     * Retrieves the phase when the god card is usable.
     * @return phase of god card.
     */
    public PowerPhase getPowerPhase(){
        return this.powerPhase;
    }

}
