package Board;

import artifacts.ArtifactCellCondition;
import frontend.HighlightType;
import frontend.SelectedStatus;

public class BoardHighlighter {
    private Board board;
    public BoardHighlighter(Board board){
        this.board = board;
    }

    /**
     * Clears the markings on the board.
     */
    public void clearHighlights() {
        this.board.clearMarkings();
    }

    /**
     * Highlights all valid neighbor cells around a center cell for either MOVE or BUILD action.
     * It applies the appropriate status and highlight type (e.g., yellow for move, green for build).
     *
     * @param centerX The x-coordinate (row) of the center cell.
     * @param centerY The y-coordinate (column) of the center cell.
     * @param type The type of action to validate and highlight (MOVE or BUILD).
     * @param excluded A specific cell to exclude from highlighting (can be null).
     */

    private void highlightNeighbors(int centerX, int centerY, HighlightType type, Cell excluded) {
        Cell center = board.getCell(centerX, centerY);
        if (center == null || center.getOccupiedBy() == null) return;

        center.setStatus(SelectedStatus.SELECTED);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int newX = centerX + dx;
                int newY = centerY + dy;
                Cell target = board.getCell(newX, newY);
                if (target == null || target == excluded) continue;

                boolean valid = switch (type) {
                    case MOVE -> center.getOccupiedBy().canMoveTo(target);
                    case BUILD -> center.getOccupiedBy().canBuildOn(target);
                    default -> false;
                };

                if (valid) {
                    target.setStatus(SelectedStatus.HIGHLIGHTED);
                    target.setHighlightType(type);
                } else {
                    target.setStatus(SelectedStatus.NONE);
                    target.setHighlightType(HighlightType.NONE);
                }
            }
        }

        // Optionally clear the excluded
        if (excluded != null) {
            excluded.setStatus(SelectedStatus.NONE);
            excluded.setHighlightType(HighlightType.NONE);
        }
    }

    /**
     * Highlights all valid cells a worker can move to from the given coordinates.
     *
     * @param x The row index of the starting cell.
     * @param y The column index of the starting cell.
     */
    public void highlightMovable(int x, int y) {
        highlightNeighbors(x, y, HighlightType.MOVE, null);
    }

    /**
     * Highlights all valid cells a worker can build on from the given coordinates.
     *
     * @param x The row index of the starting cell.
     * @param y The column index of the starting cell.
     */
    public void highlightBuildable(int x, int y) {
        highlightNeighbors(x, y, HighlightType.BUILD, null);
    }

    /**
     * Highlights valid movable cells for the currently selected worker,
     * excluding the original cell (used for Artemis second move).
     *
     * @param excluded The initial cell to exclude from highlighting.
     */
    public void highlightMovableExclude(Cell excluded) {
        Cell selected = board.getSelected();
        if (selected != null) {
            highlightNeighbors(selected.getRow(), selected.getCol(), HighlightType.MOVE, excluded);
        }
    }

    /**
     * Highlights valid buildable cells for the currently selected worker,
     * excluding the original cell (used for Demeter second build).
     *
     * @param excluded The initial cell to exclude from highlighting.
     */
    public void highlightBuildableExclude(Cell excluded) {
        Cell selected = board.getSelected();
        if (selected != null) {
            highlightNeighbors(selected.getRow(), selected.getCol(), HighlightType.BUILD, excluded);
        }
    }

    /**
     * Highlights all valid cells to use artifacts on
     * Valid cells are unOccupied and does not have a full building (
     */
    public void highlightUsableArtifactCells(ArtifactCellCondition condition){
        for (Cell cell : board.getAllCells()) {
            if (condition.isCellUsable(cell)) {
                cell.setHighlightType(HighlightType.ARTIFACT_USABLE);
                cell.setStatus(SelectedStatus.HIGHLIGHTED);
            } else {
                cell.setHighlightType(HighlightType.NONE);
                cell.setStatus(SelectedStatus.NONE);
            }
        }
    }
}
