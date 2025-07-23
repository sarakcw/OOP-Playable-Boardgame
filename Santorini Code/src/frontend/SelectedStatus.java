package frontend;

import java.awt.Color;
import Board.Cell;

/**
 * Enum representing the selection or highlight status of a cell on the game board.
 * The status determines how the cell is visually represented:
 * - NONE: Unmarked/default state (light gray)
 * - SELECTED: A cell selected by the player (white)
 * - HIGHLIGHTED: A valid target for movement or building
 *     - If MOVE: highlighted yellow
 *     - If BUILD: highlighted green
 *     - If undefined: falls back to light gray
 */

public enum SelectedStatus {
    NONE,
    SELECTED,
    HIGHLIGHTED;

    public Color getColor(Cell cell) {
        return switch (this) {
            case NONE -> new Color(176, 216, 144);
            case SELECTED -> Color.WHITE;
            case HIGHLIGHTED -> switch (cell.getHighlightType()) {
                case MOVE -> new Color(218, 178, 178);
                case BUILD -> new Color(250, 176, 113);
                case ARTIFACT_USABLE -> new Color(255, 205, 74);
                default -> new Color(176, 216, 144);
            };
        };
    }
}
