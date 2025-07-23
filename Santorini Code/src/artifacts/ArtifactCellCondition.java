package artifacts;

import Board.Cell;

/**
 * Class representing different conditions that would need to be met
 * when using different artifacts
 */
public interface ArtifactCellCondition {

    /**
     * Checks if the artifact can be used on this cell
     * @param cell the cell to apply artifact
     * @return true if the artifact can be used on the cell
     */
    boolean isCellUsable(Cell cell);
}
