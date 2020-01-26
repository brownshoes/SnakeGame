package SnakePro;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class SnakeComponent extends JPanel {
    private ArrayList<Snake> snakes;
    private SnakePellet snakePellet;

    SnakeComponent(ArrayList<Snake> snakes, SnakePellet snakePellet) {
        this.snakes = snakes;
        this.snakePellet = snakePellet;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        drawBorder(g2);
        snakePellet.drawPellet(g2);
        for(Snake snake : snakes) {
            snake.drawSnake(g2);
        }
    }

    private void drawBorder(Graphics2D g2) {
        Rectangle2D.Double east;
        Rectangle2D.Double west;
        Rectangle2D.Double north;
        Rectangle2D.Double south;

        east = new Rectangle2D.Double(0,0, SnakeMain.BORDER_THICKNESS, SnakeMain.FRAME_HEIGHT);
        west = new Rectangle2D.Double(SnakeMain.FRAME_WIDTH - SnakeMain.BORDER_THICKNESS,
                0 , SnakeMain.BORDER_THICKNESS, SnakeMain.FRAME_HEIGHT);
        north = new Rectangle2D.Double(SnakeMain.BORDER_THICKNESS,
                0, SnakeMain.FRAME_WIDTH - SnakeMain.BORDER_THICKNESS, SnakeMain.BORDER_THICKNESS);
        south = new Rectangle2D.Double(SnakeMain.BORDER_THICKNESS,
                SnakeMain.FRAME_HEIGHT - SnakeMain.BORDER_THICKNESS,
                SnakeMain.FRAME_WIDTH - SnakeMain.BORDER_THICKNESS, SnakeMain.BORDER_THICKNESS);

        g2.setPaint(SnakeMain.BORDER_COLOR);

        g2.fill(east);
        g2.draw(east);

        g2.fill(west);
        g2.draw(west);

        g2.fill(north);
        g2.draw(north);

        g2.fill(south);
        g2.draw(south);
    }
}
