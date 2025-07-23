package GodCard;

import Board.Board;
import Board.BoardHighlighter;
import GameMode.gameutils.TurnState;
import Player.Player;
import Player.Worker;
import Board.Cell;

import javax.swing.*;
import java.util.Objects;


public class Triton extends GodCard{

    private static final String GOD_NAME = "Triton";
    private boolean awaitingSecondMove = false;

    /**
     * Constructs an Artemis god instance.
     */
    public Triton() {
        super(GOD_NAME, PowerPhase.MOVE);
    }

    @Override
    public ImageIcon getCardImg() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/triton.png")));
    }

    @Override
    public boolean godPowerAvailable(Board board, Player player, TurnState turnState, Worker worker) {
        // If the player hasn't moved yet OR the player has already build OR the player has already made their second move
        return turnState.hasMoved() && !turnState.hasBuilt() && worker.isOnPerimeter(board) && !turnState.hasUsedOrSkippedGodPower();
    }

    @Override
    public void usingGodPower(Board board, BoardHighlighter highlighter, Player player, Worker worker) {
        Cell position = worker.getPosition();
        this.awaitingSecondMove = true;
        highlighter.highlightMovable(position.getRow(), position.getCol());
    }

    /**
     * Perform extra action for Triton
     * @param board The board instance.
     * @param worker The current worker.
     * @param target The target cell for the god power.
     * @param turnState
     * @return
     */
    @Override
    public boolean performExtraAction(Board board, BoardHighlighter highlighter, Worker worker, Cell target, TurnState turnState) {

        if (!awaitingSecondMove) return false;

        if (worker != null && worker.canMoveTo(target)) {
            worker.move(target);
            if (worker.isOnPerimeter(board) && !turnState.hasUsedOrSkippedGodPower()) {
                Cell newPos = worker.getPosition();
                highlighter.clearHighlights();
                highlighter.highlightMovable(newPos.getRow(), newPos.getCol());
                // stay in extra move state

            } else {
                turnState.setGodPowerUsedOrSkipped(true);
                awaitingSecondMove = false; // end power if not on perimeter
            }
            return true;
        }
        System.out.println("Triton: Invalid extra move.");
        return false;
    }
}
