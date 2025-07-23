package Board;

import artifacts.ArtifactCellCondition;
import frontend.HighlightType;
import frontend.SelectedStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board.
 * Each cell can have a building and can be occupied by a worker.
 */
public class Board {
    private final int rows;
    private final int cols;
    private final List<Cell> cells = new ArrayList<>();
    private Cell lastBuiltCell = null;
    private Cell lastMovedCell = null;


    /**
     * Initialises the game board with specified rows and columns.
     * @param rows the number of rows the board has
     * @param cols the number of columns the board has
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells.add(new DefaultCell(row, col));
            }
        }
    }

    /**
     * Retrieves the number of rows of the board.
     * @return the number of rows.
     */
    public int getRows() { return rows; }

    /**
     * Retrieves the number of columns the board has.
     * @return the number columns.
     */
    public int getCols() { return cols; }

    /**
     * Retrieves the cell at the given coordinates.
     * @param row The row index (0-based).
     * @param col The column index (0-based).
     * @return The Cell at the specified position, or null if out of bounds.
     */
    public Cell getCell(int row, int col) {
        return cells.stream()
                .filter(cell -> cell.getRow() == row && cell.getCol() == col)
                .findFirst().orElse(null);
    }

    /**
     * Retrieves all the cells on the board.
     * This allows iteration over the entire board without relying on
     * a fixed two-dimensional structure, enabling flexible board layouts.
     *
     * @return a list of all active Cell objects on the board.
     */
    public List<Cell> getAllCells() {
        return cells;
    }

    /**
     * Checks if the specified position is within the board boundaries.
     * @param row The row index.
     * @param col The column index.
     * @return true if the position is valid; false otherwise.
     */
    public boolean isValidPosition(int row, int col) {
        return getCell(row, col) != null;
    }

    /**
     * Builds on the specified cell by increasing its level, if possible.
     * @param row The target row.
     * @param col The target column.
     */
    public void build(int row, int col) {
        Cell targetCell = getCell(row, col);
        if (targetCell != null && targetCell.canBuild()) {
            targetCell.getBlock().build();
            lastBuiltCell = targetCell;
        }
    }

    /**
     * Clears the markings on the board.
     */
    public void clearMarkings() {
        for (Cell cell : cells) {
            cell.setStatus(SelectedStatus.NONE);
            cell.setHighlightType(HighlightType.NONE);
        }
    }


    /**
     * Retrieves the selected cell that has the SelectStatus.Selected tag.
     *
     * @see SelectedStatus
     * @return the selected cell.
     */
    public Cell getSelected() {
        return cells.stream()
                .filter(cell -> cell.getStatus() == SelectedStatus.SELECTED)
                .findFirst().orElse(null);
    }

    /**
     * Retrieves the latest cell that had been built on.
     * @return the last built cell.
     */
    public Cell getLastBuiltCell() {
        return lastBuiltCell;
    }


    /**
     * Retrieves the cell a worker has previously moved to
     * @return the previous cell
     */
    public Cell getLastMovedCell() {

        return this.lastMovedCell;
    }

    /**
     * Sets the last moved cell
     * @param lastMovedCell the cell to be set
     */
    public void setLastMovedCell(Cell lastMovedCell) {
        this.lastMovedCell = lastMovedCell;
    }
}

