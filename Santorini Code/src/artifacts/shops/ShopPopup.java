package artifacts.shops;

import GameMode.gameutils.TurnState;
import Player.Player;
import artifacts.Artifact;
import artifacts.Thunderbolt;
import artifacts.Trident;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ShopPopup extends JFrame {
    private List<Artifact> artifactOptions = new ArrayList<>();
    public ShopPopup(Player player, ShopManager shopManager, TurnState turnState){

        // Populate artifacts list
        artifactOptions.add(new Trident());
        artifactOptions.add(new Thunderbolt());

        setTitle("Buy Phase");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // When player closes the window instead of clicking "End Buy Phase" button
                System.out.println("Shop popup was closed by player.");
                turnState.setBuyPhaseCompleted(true);
            }
        });

        // == TOP PANEL == //
        JPanel topPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Divine Artifact Shop", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(201, 152, 31));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Token Display
        JPanel tokenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tokenPanel.setBackground(new Color(144, 171, 111));
        JLabel tokenLabel = new JLabel("Your Current Tokens: " + player.getTokens());
        tokenLabel.setForeground(Color.WHITE);
        tokenLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tokenPanel.add(tokenLabel);
        topPanel.add(tokenPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // == CENTER PANEL == //
        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));

        // Artifact Options
        JPanel artifactPanel = new JPanel();
        artifactPanel.setLayout(new BoxLayout(artifactPanel, BoxLayout.Y_AXIS));

        // Artifact Descriptions
        JTextArea descriptionArea = new JTextArea(10, 25);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionArea.setBorder(BorderFactory.createTitledBorder("Artifact Description"));

        // Add artifact buttons
        for(Artifact artifact: artifactOptions){
            JButton artifactButton = new JButton(artifact.getName() + "     " + artifact.getCost() + " Token(s)");

            Dimension buttonSize = new Dimension(Integer.MAX_VALUE, 40);
            artifactButton.setMaximumSize(buttonSize);

            artifactButton.addActionListener(e->{
                // Process purchase
                boolean success = shopManager.processPayment(artifact, player);

                if(success){
                    JOptionPane.showMessageDialog(this,
                            "You purchased: " + artifact.getName(),
                            "Purchase Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    shopManager.closeShop(turnState);
                    dispose(); // Close the popup after successful purchase
                }
                else{
                    JOptionPane.showMessageDialog(this,
                            "You don't have enough tokens to buy " + artifact.getName(),
                            "Purchase Failed",
                            JOptionPane.WARNING_MESSAGE);
                }
            });

            // Update description on hover
            artifactButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    descriptionArea.setText(artifact.getDescription());
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    descriptionArea.setText(""); // Clear when not hovered
                }
            });

            artifactPanel.add(Box.createVerticalStrut(5));
            artifactPanel.add(artifactButton);
        }
        add(artifactPanel, BorderLayout.CENTER);
        JScrollPane artifactScroll = new JScrollPane(artifactPanel);
        centerPanel.add(artifactScroll, BorderLayout.CENTER);
        centerPanel.add(descriptionArea, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        // == BOTTOM PANEL == //
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton endButton = new JButton("End Buy Phase");
        endButton.addActionListener(e -> {
            shopManager.closeShop(turnState);
            dispose();
        }); // Close the popup
        bottomPanel.add(endButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);

    }


}
