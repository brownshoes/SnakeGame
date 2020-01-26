package SnakePro;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class SnakePellet {
    private Rectangle2D.Double pellet;
    private Color pelletColor;

    public synchronized SnakePellet createSnakePellet(ArrayList<Snake> snakes) {
        Random rand = new Random();
        boolean overLap;

        do {
            overLap = false;

            int xFormula = (SnakeMain.FRAME_WIDTH - SnakeMain.PELLET_SIZE - (SnakeMain.BORDER_THICKNESS * 2)) / Snake.SNAKE_SIZE;
            int xRand = rand.nextInt(xFormula);
            int xLocationPelletFormula = (xRand * Snake.SNAKE_SIZE) + SnakeMain.BORDER_THICKNESS;

            int yFormula = (SnakeMain.FRAME_HEIGHT - SnakeMain.PELLET_SIZE - (SnakeMain.BORDER_THICKNESS * 2)) / Snake.SNAKE_SIZE;
            int yRand = rand.nextInt(yFormula);
            int yLocationPelletFormula = (yRand * Snake.SNAKE_SIZE) + SnakeMain.BORDER_THICKNESS;

            pellet = new Rectangle2D.Double(xLocationPelletFormula, yLocationPelletFormula, SnakeMain.PELLET_SIZE, SnakeMain.PELLET_SIZE);

            //check that pellet is not being created on top of a snake
            for (Snake snake : snakes) {
                overLap = snake.pelletLocationOverlapsSnake(pellet);
                if(overLap){
                    break;
                }
            }
        } while (overLap);

        pelletColor = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        return this;
    }

    public boolean checkForPelletEaten(Snake snake) {
        Rectangle2D.Double head = new Rectangle2D.Double(snake.xHeadLocation, snake.yHeadLocation, Snake.SNAKE_SIZE, Snake.SNAKE_SIZE);
        if(head.intersects(pellet)){
            System.out.println(snake.getSnakeName() + " has eaten a pellet");
            return true;
        }
        return false;
    }

    public void drawPellet(Graphics2D g2) {
        g2.setPaint(pelletColor);
        g2.fill(pellet);
        g2.draw(pellet);
    }

    public Color getPelletColor() {
        return pelletColor;
    }
    public Rectangle2D.Double getPelletRectangle(){
        return pellet;
    }
}
