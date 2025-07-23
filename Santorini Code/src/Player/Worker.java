package Player;
import Board.Board;
import Board.Cell;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Represents a worker controlled by a player.
 * A worker can move between cells on the board.
 */
public class Worker {
    final private Player owner;      // The player who owns this worker
    private Cell position;           // The current position of the worker on the board
    final private int id;            // Unique ID (0 or 1) to distinguish between two workers

    /**
     * Constructs a worker with an owner and a unique ID.
     * @param owner The player who owns this worker.
     * @param id The unique ID of the worker (typically 0 or 1).
     */
    public Worker(Player owner, int id) {
        this.owner = owner;
        this.id = id;
        this.position = null;  // Initially not placed on any cell
    }

    public static void placeMultipleRandomly(Board board, List<Player> players) {
        List<Point> available = new ArrayList<>();

        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                Cell cell = board.getCell(r, c);
                if (!cell.isOccupied() && !cell.getBlock().hasDome()) {
                    available.add(new Point(r, c));
                }
            }
        }

        Collections.shuffle(available);

        for (int i = 0; i < 4; i++) {
            Point p = available.get(i);
            Player owner = players.get(i < 2 ? 0 : 1);
            int workerId = i % 2;

            Worker w = new Worker(owner, workerId);
            w.move(board.getCell(p.x, p.y));

            System.out.println(owner.getName() + " placed Worker " + (workerId + 1) + " at (" + p.x + "," + p.y + ")");
        }
    }

    /**
     * Moves the worker to a new cell.
     * Updates both the previous cell and the new cell's occupancy.
     * @param newPosition The target cell to move to.
     */
    public boolean move(Cell newPosition) {
        if (this.getPosition() != null) {
            this.getPosition().setOccupiedBy(null); // Clear previous position
        }
        this.setPosition(newPosition);
        newPosition.setOccupiedBy(this);  // Set this worker on the new cell

        return newPosition.getBlock().getLevel() == 3;
    }

    public Cell getPosition() {
        return this.position;
    }

    public void setPosition(Cell position) {
        this.position = position;
    }
 
    public boolean canMoveTo(Cell newPosition) {
        // Must be unoccupied, no dome, and height difference ≤ 1, and not flooded
        return newPosition.getOccupiedBy() == null &&
                !newPosition.isFlooded() &&
                !newPosition.getBlock().hasDome() &&
                newPosition.getBlock().getLevel() <= this.position.getBlock().getLevel() + 1;
    }

    public boolean canBuildOn(Cell newPosition) {
        if (newPosition.getOccupiedBy() == null && !newPosition.isFlooded()) {
            if (!newPosition.getBlock().hasDome()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the player who owns this worker.
     * @return The Player who owns the worker.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Gets the worker's unique ID.
     * @return The ID of the worker (0 or 1), or -1 if unspecified.
     */
    public int getId() {
        return id;
    }

    public boolean isOnPerimeter(Board board) {
        int row = this.position.getRow();
        int col = this.position.getCol();
        int maxRow = board.getRows() - 1;
        int maxCol = board.getCols() - 1;

        return row == 0 || row == maxRow || col == 0 || col == maxCol;
    }

    
}
