package frontend;

import GameMode.Config;
import GameMode.gameutils.GameTimer;
import GameMode.TwoPlayerConfig;
import GodCard.Artemis;
import GodCard.Demeter;
import GodCard.Triton;
import GodCard.GodCard;
import Player.Player;
import listeners.TimerListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;
import java.util.Objects;
import java.util.Vector;

/**
 * Game setup menu for entering player names and starting the game.
 */
public class GameSetUpMenu extends JFrame {
    private final JTextField player1NameField;
    private final JTextField player2NameField;
    private final JButton startGameButton;

    public GameSetUpMenu() {
        setTitle("Game Setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Image
        JLabel logoLabel = new JLabel();
        ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/title.png")));

        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 60, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        logoLabel.setIcon(scaledIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(logoLabel, gbc);

        // Player 1 input
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel player1Label = new JLabel("Player 1:");
        player1Label.setForeground(Color.BLUE);
        mainPanel.add(player1Label, gbc);

        gbc.gridx = 1;
        player1NameField = new JTextField("Player 1", 15);
        mainPanel.add(player1NameField, gbc);

        // Player 2 input
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel player2Label = new JLabel("Player 2:");
        player2Label.setForeground(Color.RED);
        mainPanel.add(player2Label, gbc);

        gbc.gridx = 1;
        player2NameField = new JTextField("Player 2", 15);
        mainPanel.add(player2NameField, gbc);

        // Start Game Button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        startGameButton = new JButton("Start Game");
        startGameButton.setFocusPainted(false);
        startGameButton.addActionListener(e -> launchGame());
        mainPanel.add(startGameButton, gbc);

        add(mainPanel);
        setVisible(true);

    }

    /**
     * Launches the full game after entering player names.
     */
    private void launchGame() {
        String player1Name = player1NameField.getText().trim();
        String player2Name = player2NameField.getText().trim();

        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter names for both players.");
            return;
        }

        if (player1Name.length() > 20 || player2Name.length() > 20) {
            JOptionPane.showMessageDialog(this, "Player names must be 20 characters or less.");
            return;
        }

        if (player1Name.equalsIgnoreCase(player2Name)) {
            JOptionPane.showMessageDialog(this, "Player names must be different.");
            return;
        }


        Vector<String> playerVector = new Vector<>();
        playerVector.add(player1Name);
        playerVector.add(player2Name);
        String[] playerNames = playerVector.toArray(new String[0]);

        Vector<GodCard> godCards = new Vector<>();
        godCards.add(new Artemis());
        godCards.add(new Demeter());
        godCards.add(new Triton());
        Collections.shuffle(godCards);

        // Create a new Game and ensure it listens to the game timer
        Config config = new TwoPlayerConfig(playerVector, godCards);
        GameTimer timer = new GameTimer(config.getTurnTime());
        timer.addListener(config);
        config.setup();

        // Modal shows assigned god cards
        Player[] players = config.getPlayers();
        showGodCardIntro(players[0], players[1]);

        // Create main game window
        JFrame frame = new JFrame("Santorini Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 720);
        frame.setLayout(new BorderLayout());

        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 3, 0));
        statusLabel.setPreferredSize(new Dimension(600, 50));

        // Create a label for the timer
        JLabel p1TimerLabel = new JLabel("15:00", SwingConstants.LEFT);
        JLabel p2TimerLabel = new JLabel("15:00", SwingConstants.RIGHT);

        p1TimerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        p1TimerLabel.setForeground(Color.BLUE);
        p1TimerLabel.setPreferredSize(new Dimension(600, 50));

        p2TimerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        p2TimerLabel.setForeground(Color.RED);
        p2TimerLabel.setPreferredSize(new Dimension(600, 50));

        JPanel timerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        timerPanel.add(p1TimerLabel);
        timerPanel.add(p2TimerLabel);

        // Place both status and timer in the same panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(statusLabel, BorderLayout.NORTH);
        topPanel.add(timerPanel,BorderLayout.SOUTH);

        BoardPanel boardPanel = new BoardPanel(playerNames, config);
        boardPanel.setStatusLabel(statusLabel);
        boardPanel.setTimerLabels(List.of(p1TimerLabel, p2TimerLabel));

        // Ensure JLabel updates for each GameTimer
        List<GameTimer> timers = config.getPlayerTimers();
        for (int i = 0; i < timers.size(); i++) { // for each timer,
            final int index = i;

            // Attach new TimerListener to each GameTimer
            timers.get(i).addListener(new TimerListener() {
                @Override
                public void onTick(int secs) {
                    boardPanel.updateTimerLabel(index, secs); // update the UI with every tick
                }
                @Override
                public void onTimeExpired() {
                    boardPanel.updateTimerLabel(index, 0); // update UI
                    config.onTimeExpired();

                    // Show winner pop up
                    Player winner = config.getWinner();
                    if (winner != null) {
                        SwingUtilities.invokeLater(() -> boardPanel.onPlayerWin(winner));
                    }
                }
            });
        }

        Buttons buttons = new Buttons();
        buttons.getUseButton().addActionListener(e -> boardPanel.useGodPower());
        buttons.getSkipButton().addActionListener(e -> boardPanel.skipGodPower());
        buttons.getArtifactButton().addActionListener(e -> boardPanel.chooseArtifact());

        buttons.getExitButton().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to exit the game and return to main menu? The current game will be lost.",
                "Exit Game",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                config.getCurrentShopPopup().dispose();
                frame.dispose(); // closes the game window
                new GameSetUpMenu();  // optional: exit the whole app
            }
        });


        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(boardPanel);
        frame.add(topPanel, BorderLayout.NORTH); // Add the timer to the frame

        frame.add(centerWrapper, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //Start the turn of the first player
        config.startTurn(config.getCurrentPlayer());

        dispose();
    }

    /**
     * Displays a modal with each player's god card and name.
     */
    private void showGodCardIntro(Player p1, Player p2) {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Get the god card image of each player's god
        ImageIcon p1Icon = p1.getGod().getCardImg();
        ImageIcon p2Icon = p2.getGod().getCardImg();


        Image p1Scaled = p1Icon.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);
        Image p2Scaled = p2Icon.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);

        // Player 1 Panel
        JPanel p1Panel = new JPanel(new BorderLayout(5, 5));
        JLabel p1Image = new JLabel(new ImageIcon(p1Scaled));
        JLabel p1Label = new JLabel(
                "<html><div style='text-align:center;'><b style='color:blue'>" +
                        p1.getName() + "</b><br>(" + p1.getGod().getName() + ")</div></html>",
                SwingConstants.CENTER
        );
        p1Panel.add(p1Label, BorderLayout.NORTH);
        p1Panel.add(p1Image, BorderLayout.CENTER);

        // Player 2 Panel
        JPanel p2Panel = new JPanel(new BorderLayout(5, 5));
        JLabel p2Image = new JLabel(new ImageIcon(p2Scaled));
        JLabel p2Label = new JLabel(
                "<html><div style='text-align:center;'><b style='color:red'>" +
                        p2.getName() + "</b><br>(" + p2.getGod().getName() + ")</div></html>",
                SwingConstants.CENTER
        );
        p2Panel.add(p2Label, BorderLayout.NORTH);
        p2Panel.add(p2Image, BorderLayout.CENTER);

        // Add to main
        mainPanel.add(p1Panel);
        mainPanel.add(p2Panel);

        JOptionPane.showMessageDialog(this, mainPanel, "Assigned God Cards", JOptionPane.INFORMATION_MESSAGE);
    }

}