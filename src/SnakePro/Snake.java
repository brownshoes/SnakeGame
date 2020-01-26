package SnakePro;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class Snake {

    //size values
    public static final int SNAKE_SIZE = 10;
    private static final int GROWTH_AMOUNT = 5;

    //directional constants
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;

    //current status
    public int xHeadLocation;
    public int yHeadLocation;
    public int currentDirection;
    private int numberOfPelletsEaten = 0;

    //start values
    private final String snakeName;
    private final int startX;
    private final int startY;
    private final int startDirection;
    private final Color startColor;
    private final boolean changeColorWhenPelletEaten;

    private int growCounter = 0;
    private Color growColor;

    private LinkedList<Rectangle2D.Double> snakeBodyList = new LinkedList<>();
    private HashMap<Integer, Color> colorMap = new HashMap<>();

    public Snake(String snakeName, int startX, int startY, int startDirection, Color startColor, boolean changeColorWhenPelletEaten) {
        this.snakeName = snakeName;
        this.startX = startX;
        this.startY = startY;
        this.startDirection = startDirection;
        this.startColor = startColor;
        this.changeColorWhenPelletEaten = changeColorWhenPelletEaten;
        placeSnakeIntoStartState();
    }

    public synchronized void drawSnake(Graphics2D g2) {
        int bodySegmentNumber = colorMap.size() - 1;
        Iterator<Rectangle2D.Double> iterator = snakeBodyList.iterator();
        while(iterator.hasNext()) {
            Rectangle2D.Double segment = iterator.next();
            g2.setPaint(colorMap.get(bodySegmentNumber));
            g2.fill(segment);
            g2.draw(segment);
            bodySegmentNumber--;
        }
    }

    public synchronized void updateSnake(ArrayList<Snake> snakes) {
        if(snakeBodyList.size() != 0) {
            addBodySegment(this.xHeadLocation, this.yHeadLocation);
            if(growCounter > 0 ) {
                addSegmentColor(growColor);
                growCounter--;
            } else {
                removeBodySegment();
            }
        } else {
            if (respawnSnake(snakes)) {
                placeSnakeIntoStartState();
            }
        }
    }

    //TODO FIX: There is a bug in this code that sometimes prevents respawn.
    private boolean respawnSnake(ArrayList<Snake> snakes) {
        Rectangle2D.Double three;
        Rectangle2D.Double two;
        Rectangle2D.Double one;
        Rectangle2D.Double head;

        three = new Rectangle2D.Double(this.xHeadLocation - (Snake.SNAKE_SIZE * 3), this.yHeadLocation, Snake.SNAKE_SIZE, Snake.SNAKE_SIZE);
        two = new Rectangle2D.Double(this.xHeadLocation - (Snake.SNAKE_SIZE * 2), this.yHeadLocation, Snake.SNAKE_SIZE, Snake.SNAKE_SIZE);
        one = new Rectangle2D.Double(this.xHeadLocation - Snake.SNAKE_SIZE , this.yHeadLocation, Snake.SNAKE_SIZE, Snake.SNAKE_SIZE);
        head = new Rectangle2D.Double(this.xHeadLocation, this.yHeadLocation, Snake.SNAKE_SIZE, Snake.SNAKE_SIZE);

        //get snake locations
        LinkedList<Rectangle2D.Double> linkedSnake = new LinkedList<Rectangle2D.Double>();
        for(Snake snake : snakes) {

            if(!snake.snakeName.equals(this.snakeName)) {
                linkedSnake.addAll(snake.copySnake(this.snakeName));
            }

            Iterator<Rectangle2D.Double> iterator = linkedSnake.iterator();
            while(iterator.hasNext()){
                Rectangle2D.Double segment = iterator.next();
                //check that another snake is not currently in the respawn location
                if(head.intersects(segment) || one.intersects(segment) || two.intersects(segment) || three.intersects(segment)){
                    System.out.println("Snake in way of respawning: " + snakeName);
                    return false;
                }
            }
        }
        return true;
    }

    public void grow(Color pelletColor) {
        growCounter = Snake.GROWTH_AMOUNT + 1;
        growColor = pelletColor;
        numberOfPelletsEaten++;
    }

    public void resetSnake() {
        snakeBodyList.clear();
        colorMap.clear();
        growCounter = 0;
        numberOfPelletsEaten = 0;
    }

    private void placeSnakeIntoStartState() {
        this.xHeadLocation = this.startX;
        this.yHeadLocation = this.startY;
        this.currentDirection = this.startDirection;

        addBodySegment(this.xHeadLocation - (Snake.SNAKE_SIZE * 3) , this.yHeadLocation);
        addSegmentColor(this.startColor);

        addBodySegment(this.xHeadLocation - (Snake.SNAKE_SIZE * 2) , this.yHeadLocation);
        addSegmentColor(this.startColor);

        addBodySegment(this.xHeadLocation - Snake.SNAKE_SIZE , this.yHeadLocation);
        addSegmentColor(this.startColor);

        addBodySegment(this.xHeadLocation, this.yHeadLocation);
        addSegmentColor(this.startColor);
    }

    public void addBodySegment(int xLocation, int yLocation) {
        snakeBodyList.addFirst(new Rectangle2D.Double(xLocation, yLocation, SNAKE_SIZE, SNAKE_SIZE));
    }

    public void addSegmentColor(Color color) {
        if(changeColorWhenPelletEaten){
            colorMap.put(colorMap.keySet().size(), color);
        } else {
            colorMap.put(colorMap.keySet().size(), startColor);
        }
    }

    public void removeBodySegment() {
        //remove tail
        if(!snakeBodyList.isEmpty()) {
            snakeBodyList.removeLast();
        } else {
            System.out.println("Error. Empty");
        }
    }

    public synchronized boolean pelletLocationOverlapsSnake(Rectangle2D.Double pellet) {
        Iterator<Rectangle2D.Double> iterator = snakeBodyList.iterator();
        while (iterator.hasNext()){
            Rectangle2D.Double segment = iterator.next();
            if(pellet.intersects(segment)){
                return true;
            }
        }
        return false;
    }

    public synchronized  LinkedList<Rectangle2D.Double> copySnake(String name){
        LinkedList<Rectangle2D.Double> snakeList = new LinkedList<>();
        Iterator<Rectangle2D.Double> iterator;

        if(!name.equals(this.snakeName)){
            iterator = snakeBodyList.iterator();
        } else {
            //exclude head if self
            if(snakeBodyList.size() > 0) {
                iterator = snakeBodyList.listIterator(1);
            } else {
                return snakeList;
            }
        }

        while (iterator.hasNext()) {
            Rectangle2D.Double rect = iterator.next();
            snakeList.add(new Rectangle2D.Double(rect.x, rect.y, Snake.SNAKE_SIZE, Snake.SNAKE_SIZE));
        }
        return snakeList;
    }

    public synchronized  boolean checkForCollision(ArrayList<Snake> snakes) {
        if (snakeBodyList.size() > 0) {
            Rectangle2D.Double head = snakeBodyList.getFirst();
            LinkedList<Rectangle2D.Double> linkedSnake = new LinkedList<>();
            for (Snake snake : snakes) {
                //get other snakes' locations
                linkedSnake.addAll(snake.copySnake(snake.getSnakeName()));
                Iterator<Rectangle2D.Double> iterator = linkedSnake.iterator();
                while(iterator.hasNext()) {
                    Rectangle2D.Double segment = iterator.next();
                    if(head.intersects(segment)){
                        System.out.println("Collision");
                        return true;
                    }


                }
                //TODO
                //check later
                linkedSnake.clear();
            }
        }
        return false;
    }


    public String getSnakeName(){
        return snakeName;
    }

    public int getNumberOfPelletsEaten(){
        return numberOfPelletsEaten;
    }
}
