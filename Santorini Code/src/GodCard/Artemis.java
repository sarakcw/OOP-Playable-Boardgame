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
 * Represents the god Artemis who can move a worker twice.
 */
public class Artemis extends GodCard {

    private static final String GOD_NAME = "Artemis";
    private Cell originalCell;
    private boolean awaitingSecondMove = false;

    /**
     * Constructs an Artemis god instance.
     */
    public Artemis() {
        super(GOD_NAME, PowerPhase.MOVE);
    }

    @Override
    public ImageIcon getCardImg() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/artemis.png")));
    }
    /**
     * Determines if Artemis' power is available to use.
     *
     * @param board The current board instance.
     * @param player The player attempting to use the god power.
     * @return true if Artemis' power can be used.
     */
    @Override
    public boolean godPowerAvailable(Board board, Player player, TurnState turnState, Worker worker) {
        System.out.println("Turn State built:" + turnState.hasBuilt());
        // If the player hasn't moved yet OR the player has already build OR the player has already made their second move
        if(!turnState.hasMoved() || turnState.hasBuilt()){
            System.out.println("Artemis does not meet conditions");
            return false;
        }
        return true;
    }

    /**
     * Activates Artemis' special power.
     *
     * @param board The current board instance.
     * @param player The player using the god power.
     */
    @Override
    public void usingGodPower(Board board, BoardHighlighter highlighter, Player player, Worker worker) {
        Cell selected = board.getLastMovedCell();

        if (selected != null && !selected.isOccupied()) {
            this.originalCell = selected;
            this.awaitingSecondMove = true;
            highlighter.highlightMovableExclude(originalCell);
            System.out.println("Artemis: Select a different cell to move again.");
        } else {
            System.out.println("No worker selected for Artemis's power.");
        }
    }


    /**
     * Performs the extra action of moving to a new cell for Artemis.
     * @param board The board instance.
     * @param worker The player who moved.
     * @param target The target cell for the extra move.
     * @return true if the move was successful, false otherwise.
     */
    @Override
    public boolean performExtraAction(Board board,BoardHighlighter highlighter, Worker worker, Cell target, TurnState turnState) {
        if (!awaitingSecondMove) return false;

//        Worker thisWorker = board.getSelected().getOccupiedBy();
        if (worker != null && target != originalCell && worker.canMoveTo(target)) {
            worker.move(target);
            awaitingSecondMove = false;
            turnState.setGodPowerUsedOrSkipped(true);
//            Cell currentPosition = worker.getPosition();
//            board.highlightBuildable(currentPosition.getRow(), currentPosition.getCol());

            return true;
        }

        System.out.println("Artemis: Invalid extra move.");
        return false;
    }
}
