package frontend;

import GameMode.Config;
import GodCard.GodCard;
import GodCard.PowerPhase;
import Player.Player;
import Player.Worker;
import Board.Board;
import Board.Cell;
import artifacts.Artifact;
import listeners.CellClickListener;
import listeners.TimerListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class BoardPanel extends JPanel implements CellClickListener {
    private final Config config;
    private final Board board;
    private final List<CellPanel> cellPanels = new ArrayList<>();
    private final String[] playerNames;

    private JLabel statusLabel;
    private List<JLabel> timerLabels = new ArrayList<>();


    /**
     * Constructs the game board UI and initialises the game state.
     *
     * @param playerNames Names of the players.
     * @param config      The game configuration logic.
     */
    public BoardPanel(String[] playerNames, Config config) {
        this.playerNames = playerNames;
        this.config = config;
        this.board = config.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();

        setLayout(null); // Use absolute layout for flexibility
        setPreferredSize(new Dimension(cols * 100, rows * 100));

        // Centering offsets
        int cellSize = 100;
        int boardWidth = cols * cellSize;
        int boardHeight = rows * cellSize;

        // Ensure actual size available during layout
        int panelWidth = getPreferredSize().width;
        int panelHeight = getPreferredSize().height;

        int offsetX = (panelWidth - boardWidth) / 2;
        int offsetY = (panelHeight - boardHeight) / 2;

        // Create and add cell panels from flexible board
        for (Cell cell : board.getAllCells()) {
            CellPanel panel = new CellPanel(cell, this);
            cellPanels.add(panel);
            add(panel);
            int x = offsetX + cell.getCol() * cellSize;
            int y = offsetY + cell.getRow() * cellSize;
            panel.setBounds(x, y, cellSize, cellSize);
        }

        // Randomly place workers for all players
        Worker.placeMultipleRandomly(board, Arrays.asList(config.getPlayers()));

        // Display initial status
        updateStatus();

        // Log first player info
        Player first = config.getCurrentPlayer();
        System.out.println(first.getName() + " goes first (" + first.getGod().getName() + ")");
    }

    /**
     * Sets the label used to display the current status.
     *
     * @param label The label to update.
     */
    public void setStatusLabel(JLabel label) {
        this.statusLabel = label;
        updateStatus();
    }


    public void setTimerLabels(List<JLabel> labels){
        timerLabels.clear();
        timerLabels.addAll(labels);
    }

    public void updateTimerLabel(int playerIndex, int secondsLeft) {
        if (playerIndex < 0 || playerIndex >= timerLabels.size()) return;
        JLabel playerTimeLabel = timerLabels.get(playerIndex);
        SwingUtilities.invokeLater(() -> {
            if (secondsLeft <= 0) {
                playerTimeLabel.setText("TIMES UP");
            } else {
                int minutes = secondsLeft / 60;
                int seconds = secondsLeft % 60;
                playerTimeLabel.setText(String.format("%02d:%02d", minutes, seconds));
            }
        });
    }


    /**
     * Updates the UI label to reflect the current player's turn and god timing hint.
     */
    private void updateStatus() {
        if (statusLabel == null) return;

        Player currentPlayer = config.getCurrentPlayer();
        String name = currentPlayer.getName();
        String god = currentPlayer.getGod().getName();
        GodCard godCard = currentPlayer.getGod();

        // Colour the player name
        String colouredName = name;
        if (playerNames != null && playerNames.length == 2) {
            if (name.equals(playerNames[0])) {
                colouredName = "<font color='blue'>" + name + "</font>";
            } else if (name.equals(playerNames[1])) {
                colouredName = "<font color='red'>" + name + "</font>";
            }
        }

        String turnMessage = "<b>" + colouredName + "</b> (" + god + ")'s Turn";

        // God power Timing hint
        String godHint = "";
        if (!config.isGodPowerUsedOrSkipped()) {
            PowerPhase phase = godCard.getPowerPhase();
            boolean moved = config.hasMoved();
            boolean built = config.hasBuilt();

            if (phase == PowerPhase.MOVE && moved && !built) {
                godHint = "<br><span style='color:gray; font-size:smaller;'>You may now use " + god + "'s move power or build to skip.</span>";
            } else if (phase == PowerPhase.BUILD && moved && built) {
                godHint = "<br><span style='color:gray; font-size:smaller;'>You may now use " + god + "'s build power or skip.</span>";
            }
        }
        statusLabel.setText("<html>" + turnMessage + godHint + "</html>");

    }

    /**
     * Invokes the use of the current player's god power.
     */
    public void useGodPower() {
        config.useGodPower();
        updateStatus();
        repaint();
    }

    /**
     * Skips the current player's god power.
     */
    public void skipGodPower() {
        config.skipGodPower();
        updateStatus();
        repaint();
        System.out.println(config.getCurrentPlayer().getName() + " skipped god power.");
    }

    /**
     *Invokes the use of the current player's chosen artifact.
     */
    public void useArtifact(Artifact artifact) {
        config.useArtifact(artifact);
        updateStatus();
        repaint();
    }
    /**
     * Asks the player which artifact to use.
     */
    public void chooseArtifact() {
        boolean success = config.canUseArtifact(); // check if player has moved or built
        System.out.println("Player deciding which artifact to use.");

        if(success){
            List<Artifact> artifacts = config.getCurrentPlayer().getArtifacts();

            if (artifacts == null || artifacts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You don't have any artifacts in your inventory.");
                return;
            }

            // Create a modal dialog
            Frame parentFrame = JOptionPane.getFrameForComponent(this);
            JDialog artifactsDialog = new JDialog(parentFrame, "Inventory", true);
            artifactsDialog.setLayout(new BorderLayout());

            // Instructions
            JLabel promptLabel = new JLabel("Select an artifact to use:", SwingConstants.CENTER);
            promptLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            artifactsDialog.add(promptLabel, BorderLayout.NORTH);

            // Add buttons for each artifact
            JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            Dimension buttonSize = new Dimension(200, 60); // width, height


            for (Artifact artifact : artifacts) {
                JButton artifactButton = new JButton(artifact.getName()); // Assumes getName() exists
                artifactButton.setPreferredSize(buttonSize);
                artifactButton.addActionListener(e -> {
                    System.out.println("Chosen artifact: " + artifact.getName());
                    useArtifact(artifact);
                    artifactsDialog.dispose();
                });
                buttonPanel.add(artifactButton);
            }

            artifactsDialog.add(buttonPanel, BorderLayout.CENTER);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> artifactsDialog.dispose());
            JPanel cancelPanel = new JPanel();
            cancelPanel.add(cancelButton);
            artifactsDialog.add(cancelPanel, BorderLayout.SOUTH);

            artifactsDialog.pack();
            artifactsDialog.setLocationRelativeTo(this);
            artifactsDialog.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(this, "You can only use an artifact at the beginning of your turn");
        }

    }

    /**
     * Called when the user clicks a board cell.
     *
     * @param row The row of the clicked cell.
     * @param col The column of the clicked cell.
     */
    @Override
    public void onCellClicked(int row, int col) {
        config.handleClick(row, col);
        updateStatus();
        repaint();

        Player winner = config.getWinner();
        if (winner != null) {
            SwingUtilities.invokeLater(() -> onPlayerWin(winner));
        }
    }

    /**
     * Called when a player wins. Displays a prompt to restart or exit.
     *
     * @param winner The player who won.
     */
    public void onPlayerWin(Player winner) {
        setEnabled(false);

        int option = JOptionPane.showOptionDialog(
                this,
                winner.getName() + " wins!\nWhat would you like to do?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Play Again", "Exit"},
                "Play Again"
        );

        if (option == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                topFrame.dispose();
                new GameSetUpMenu();
            });
        } else if (option == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }
}
