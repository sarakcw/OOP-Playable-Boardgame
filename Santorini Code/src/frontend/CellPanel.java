package frontend;

import Board.Cell;
import Player.Worker;
import listeners.CellClickListener;

import javax.swing.*;
import java.awt.*;

/**
 * A single square on the game board grid.
 * Handles rendering of buildings, domes, and workers, and responds to clicks.
 */
public class CellPanel extends JPanel {
    private final Cell cell;
    private final CellClickListener listener;

    // Constants for sizing and spacing
    private static final int PANEL_SIZE = 100;
    private static final int BASE_BLOCK_SIZE = 80;
    private static final int BLOCK_HEIGHT = 12;
    private static final int BLOCK_STEP = 12;
    private static final int DOME_SIZE = 30;
    private static final int DOME_OFFSET_Y = 8;
    private static final int WORKER_SIZE = 40;

    /**
     * Creates a visual panel linked to a specific game board cell.
     *
     * @param cell     The logical cell.
     * @param listener Click listener to notify when cell is clicked.
     */
    public CellPanel(Cell cell, CellClickListener listener) {
        this.cell = cell;
        this.listener = listener;

        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (listener != null) {
                    listener.onCellClicked(cell.getRow(), cell.getCol());
                }

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (cell.isFlooded()) {

            // == Gradient Background == //
            Graphics2D g2d = (Graphics2D) g.create();
            GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 102, 204),
                    getWidth(), getHeight(), new Color(0, 153, 255));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // == Ripple effect ==//
            g2.setColor(new Color(255, 255, 255, 60)); // semi-transparent white
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            for (int i = 0; i < 3; i++) {
                int rippleSize = 30 + i * 15;
                g2.drawOval(centerX - rippleSize / 2, centerY - rippleSize / 2,
                        rippleSize, rippleSize);
            }
        } else {
            setBackground(cell.getStatus().getColor(cell));
        }

        int baseX = getWidth() / 2;
        int baseY = getHeight() / 2;

        int level = cell.getBlock().getLevel();
        boolean dome = cell.getBlock().hasDome();

        // Draw building levels (stacked coloured squares)
        for (int i = 0; i < level; i++) {
            Color levelColor = switch (i) {
                case 0 -> new Color(173, 216, 230);
                case 1 -> new Color(250, 227, 135);
                case 2 -> new Color(180, 70, 129);
                default -> new Color(197, 70, 70);
            };
            g2.setColor(levelColor);

            int size = BASE_BLOCK_SIZE - i * BLOCK_STEP;
            int x = baseX - size / 2;
            int y = baseY - (i + 1) * BLOCK_HEIGHT - size / 2;

            g2.fillRect(x, y, size, size);

            // Draw "L#" on each block
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            String label = "L" + (i + 1);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, x + (size - fm.stringWidth(label)) / 2, y + size - 4);
        }

        // Draw dome
        if (dome) {
            int x = baseX - DOME_SIZE / 2;
            int y = baseY - DOME_SIZE / 2 - level * BLOCK_HEIGHT - DOME_OFFSET_Y;

            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillOval(x, y, DOME_SIZE, DOME_SIZE);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            FontMetrics fm = g2.getFontMetrics();
            String dLabel = "D";
            g2.drawString(dLabel, x + (DOME_SIZE - fm.stringWidth(dLabel)) / 2,
                    y + (DOME_SIZE + fm.getAscent()) / 2);
        }

        // Draw worker
        Worker w = cell.getOccupiedBy();
        if (w != null) {
            Color workerColor = w.getOwner().getColor();
            int x = baseX - WORKER_SIZE / 2;
            int y = baseY - WORKER_SIZE / 2 - level * BLOCK_HEIGHT - (dome ? 12 : 0);

            g2.setColor(workerColor);
            g2.fillRect(x, y, WORKER_SIZE, WORKER_SIZE);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            String wLabel = "W" + (w.getId() + 1);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(wLabel, x + (WORKER_SIZE - fm.stringWidth(wLabel)) / 2,
                    y + (WORKER_SIZE + fm.getAscent()) / 2 - 3);
        }
    }
}
