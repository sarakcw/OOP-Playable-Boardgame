package GodCard;

import Board.Cell;
import GameMode.gameutils.TurnState;
import Player.Player;
import Player.Worker;

import Board.Board;
import Board.BoardHighlighter;


import javax.swing.*;
import java.util.Objects;

/**
 * Represents the god Demeter who can build twice per turn.
 */
public class Demeter extends GodCard {

    private static final String GOD_NAME = "Demeter";
    private Cell firstBuildCell;
    private boolean awaitingSecondBuild = false;

    /**
     * Constructs a Demeter god instance.
     */
    public Demeter() {
        super(GOD_NAME, PowerPhase.BUILD);
    }

    @Override
    public ImageIcon getCardImg() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/demeter.png")));
    }
    /**
     * Determines if Demeter's power is available to use.
     *
     * @param board The current board instance.
     * @param player The player attempting to use the god power.
     * @return true if Demeter's power can be used.
     */
    @Override
    public boolean godPowerAvailable(Board board, Player player, TurnState turnState, Worker worker) {
        // If the player has made a move, has built once and has not used/skipped the god power = return true
        return turnState.hasMoved() && turnState.hasBuilt() && !turnState.hasUsedOrSkippedGodPower();
    }

    /**
     * Activates Demeter's special power.
     *
     * @param board The current board instance.
     * @param player The player using the god power.
     */
    @Override
    public void usingGodPower(Board board, BoardHighlighter highlighter, Player player, Worker worker) {
        Cell selected = board.getSelected();
        if (selected != null && selected.isOccupied()) {
            this.firstBuildCell = board.getLastBuiltCell();
            this.setAwaitingSecondBuild(true);
            highlighter.highlightBuildableExclude(firstBuildCell);
            System.out.println("Demeter: Select a different space to build again.");
        } else {
            System.out.println("No worker selected for Demeter's power.");
        }
    }

    public void setAwaitingSecondBuild(boolean value) {
        this.awaitingSecondBuild = value;
    }

    @Override
    public boolean performExtraAction(Board board, BoardHighlighter highlighter, Worker worker, Cell target, TurnState turnState) {
        if (!awaitingSecondBuild) return false;

        if (target != null && target != firstBuildCell &&
            target.getOccupiedBy() == null && !target.getBlock().hasDome()) {

            target.build();
            this.setAwaitingSecondBuild(false);
            turnState.setGodPowerUsedOrSkipped(true);
            System.out.println("Demeter built successfully on (" + target.getRow() + "," + target.getCol() + ")");
            return true;
        }

        System.out.println("Demeter: Invalid second build.");
        return false;
    }


}
