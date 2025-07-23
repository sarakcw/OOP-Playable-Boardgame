package frontend;

import javax.swing.*;
import java.awt.*;

/**
 * Simple panel that shows two control buttons without any action logic.
 */
public class Buttons extends JPanel {
    private final JButton useButton;
    private final JButton skipButton;
    private final JButton artifactButton;
    private final JButton exitButton;

    public Buttons() {
        setLayout(new GridLayout(2, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        useButton = new JButton("Use God Power");
        skipButton = new JButton("Skip God Power");
        artifactButton = new JButton("Use an Artifact");
        exitButton = new JButton("Exit Game");

        Dimension buttonSize = new Dimension(200, 60); // width, height
        useButton.setPreferredSize(buttonSize);
        skipButton.setPreferredSize(buttonSize);
        artifactButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        useButton.setFont(new Font("Arial", Font.BOLD, 16));
        skipButton.setFont(new Font("Arial", Font.BOLD, 16));
        artifactButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));

        add(useButton);
        add(skipButton);
        add(artifactButton);
        add(exitButton);
    }

    public JButton getUseButton() {
        return useButton;
    }
//
    public JButton getSkipButton() {
        return skipButton;
    }

    public JButton getArtifactButton() {
        return artifactButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

}
