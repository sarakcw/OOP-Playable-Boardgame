package Board;

import Block.Block;
import Player.Worker;
import frontend.HighlightType;
import frontend.SelectedStatus;


/**
 * Abstract base class representing a single cell on the game board.
 * Each cell holds a block and may be occupied by a worker.
 */
public abstract class Cell {
    protected Block block;        // <<< was private before, now protected
    private Worker occupiedBy;
    private int row;
    private int col;
    private SelectedStatus selectStatus;
    private boolean flooded = false;

    private HighlightType highlightType = HighlightType.NONE;

    public void setHighlightType(HighlightType type) {
        this.highlightType = type;
    }

    public HighlightType getHighlightType() {
        return this.highlightType;
    }

    /**
     * Constructs a Cell at the specified (row, col) position.
     * Initializes with a standard block and no worker.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.occupiedBy = null;
        this.block = null; // will be assigned by subclass (e.g., DefaultCell)
        this.selectStatus = SelectedStatus.NONE;
    }

    /**
     * Retrieve the status of the cell.
     * @return the status of the cell.
     */
    public SelectedStatus getStatus() {
        return this.selectStatus;
    }

    /**
     * Sets the status of the cell.
     * @param status the status to be set
     */
    public void setStatus(SelectedStatus status) {
        this.selectStatus = status;
    }
    /**
     * Checks if the cell is currently occupied by a worker.
     *
     * @return true if occupied, false otherwise.
     */
    public boolean isOccupied() {
        return occupiedBy != null;
    }


    /**
     * Determines whether a worker can build on this cell.
     * A cell is buildable if it is unoccupied and has no dome.
     *
     * @return true if building is allowed.
     */
    public abstract boolean canBuild();  // leave abstract (already overridden in DefaultCell)
    /**
     * Increases the level of the block or places a dome, depending on current level.
     */
    public void build() {
        if (block != null) {
            block.build();
        }
    }

    public void flood() {
        this.flooded = true;
        this.setStatus(SelectedStatus.NONE);
        this.setHighlightType(HighlightType.NONE);
    }

    public boolean isFlooded() {
        return flooded;
    }

    /**
     * Gets the block on this cell.
     *
     * @return The block instance.
     */
    public Block getBlock() { return this.block; }
    /**
     * Gets the worker occupying this cell, if any.
     *
     * @return The worker or null if unoccupied.
     */
    public Worker getOccupiedBy() {
        return this.occupiedBy;
    }
    /**
     * Sets or clears the worker occupying this cell.
     *
     * @param worker The worker to set, or null to clear.
     */
    public void setOccupiedBy(Worker worker) {
        this.occupiedBy = worker;
    }
    /**
     * Gets the row index of this cell.
     *
     * @return Row index.
     */
    public int getRow() {
        return this.row;
    }
    /**
     * Gets the column index of this cell.
     *
     * @return Column index.
     */
    public int getCol() {
        return this.col;
    }
}
