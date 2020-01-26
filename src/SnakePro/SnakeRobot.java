package SnakePro;

import java.awt.geom.Rectangle2D;
import java.util.*;

public class SnakeRobot implements Runnable{
    private static final int Timer_SPEED = 10;
    private Snake snake;
    private ArrayList<Snake> snakes;
    private SnakePellet snakePellet;

    private class Node {
        int xLocation;
        int yLocation;

        int gScore;
        int fScore;

        Node parent = null;

        Node(int xLocation, int yLocation){
            this.xLocation = xLocation;
            this.yLocation = yLocation;
        }

        Node (int fScore){
            this.fScore = fScore;
        }

        Node (Rectangle2D.Double rectangle) {
            this.xLocation = (int) rectangle.x;
            this.yLocation = (int) rectangle.y;
        }

        public Rectangle2D.Double convertToRectangle() {
            return new Rectangle2D.Double(this.xLocation, this.yLocation, Snake.SNAKE_SIZE, Snake.SNAKE_SIZE);
        }

        @Override
        public int hashCode(){
            return (this.xLocation * 1000) + this.yLocation;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            Node compare = (Node) obj;
            if(this.xLocation == compare.xLocation && this.yLocation == compare.yLocation){
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return this.xLocation + " " + this.yLocation;
        }
    }

    public SnakeRobot(Snake snake, ArrayList<Snake> snakes, SnakePellet snakePellet) {
        this.snake = snake;
        this.snakes = snakes;
        this.snakePellet = snakePellet;
    }

    public void run() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Node currentLocation = new Node(snake.xHeadLocation, snake.yHeadLocation);
                Node goalLocation = new Node (snakePellet.getPelletRectangle());

                if(!currentLocation.equals(goalLocation)) {
                    //get snake locations
                    LinkedList<Rectangle2D.Double> linkedSnake = new LinkedList<>();
                    for(Snake snake : snakes){
                        linkedSnake.addAll(snake.copySnake(snake.getSnakeName()));
                    }
                    Node next = aStar(currentLocation, goalLocation, linkedSnake);
                    if(next != null) {
                        if(next.xLocation < currentLocation.xLocation) {
                            snake.currentDirection = Snake.LEFT;
                            //System.out.println("LEFT");
                        } else if (next.xLocation > currentLocation.xLocation) {
                            snake.currentDirection = Snake.RIGHT;
                            //System.out.println("RIGHT");
                        } else if (next.yLocation < currentLocation.yLocation) {
                            snake.currentDirection = Snake.UP;
                            //System.out.println("UP");
                        } else if (next.yLocation > currentLocation.yLocation) {
                            snake.currentDirection = Snake.DOWN;
                            //System.out.println("DOWN");
                        }
                    } else {
                        System.out.println("ERROR: Next is null!");
                    }
                } else {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 50 , 50);
    }

    private Node aStar(Node start, Node goal, LinkedList<Rectangle2D.Double> body) {
        HashSet<Node> openSet = new HashSet<>(); //Convert to min heap
        HashSet<Node> closedSet = new HashSet<>();

        start.gScore = 0;
        start.fScore = start.gScore + heuristic(start, goal);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            //Find node with lowest score. Will be the current node.
            Node current = new Node(Integer.MAX_VALUE);
            for (Node node : openSet) {
                if(node.fScore <= current.fScore){
                    current = node;
                }
            }

            if(current.equals(goal)){
                return constructPath(current);
            } else {
                openSet.remove(current);
                closedSet.add(current);
            }

            for(Node neighbor : getNeighborSet(current, body)){
                if(closedSet.contains(neighbor)){
                    continue;
                }
                if(!openSet.contains(neighbor)){
                    openSet.add(neighbor);
                }

                int gScore = current.gScore + distanceBetweenNodes(current, neighbor);
                if(gScore >= neighbor.gScore) {
                    neighbor.parent = current;
                    neighbor.gScore = gScore;
                    neighbor.fScore = gScore + heuristic(neighbor, goal);
                }
            }
        }
        return null;
    }

    private Node constructPath(Node node){
        while(node.parent.parent != null){
            node = node.parent;
        }
        return node;
    }

    private int distanceBetweenNodes(Node current, Node neighbor){
        return Snake.SNAKE_SIZE;
    }

    private int heuristic(Node current, Node goal) {
        return Math.abs(current.xLocation - goal.xLocation) + Math.abs(current.yLocation - goal.yLocation);
    }

    private HashSet<Node> getNeighborSet(Node node, LinkedList<Rectangle2D.Double> body){
        HashSet<Node> neighborSet = new HashSet<>();

        //north Neighbor
        if(node.yLocation - Snake.SNAKE_SIZE >= SnakeMain.BORDER_THICKNESS) {
            Node north = new Node(node.xLocation, node.yLocation - Snake.SNAKE_SIZE);
            if(!body.contains(north.convertToRectangle())) {
                neighborSet.add(north);
            }
        }

        //south
        if(node.yLocation + Snake.SNAKE_SIZE <= SnakeMain.FRAME_HEIGHT - Snake.SNAKE_SIZE - SnakeMain.BORDER_THICKNESS) {
            Node south = new Node(node.xLocation, node.yLocation + Snake.SNAKE_SIZE);
            if(!body.contains(south.convertToRectangle())) {
                neighborSet.add(south);
            }
        }

        //west
        if(node.xLocation - Snake.SNAKE_SIZE >= SnakeMain.BORDER_THICKNESS){
            Node west = new Node(node.xLocation - Snake.SNAKE_SIZE, node.yLocation);
            if(!body.contains(west.convertToRectangle())) {
                neighborSet.add(west);
            }
        }

        //east
        if(node.xLocation + Snake.SNAKE_SIZE <= SnakeMain.FRAME_WIDTH - Snake.SNAKE_SIZE - SnakeMain.BORDER_THICKNESS) {
            Node east = new Node(node.xLocation + Snake.SNAKE_SIZE, node.yLocation);
            if(!body.contains(east.convertToRectangle())) {
                neighborSet.add(east);
            }
        }

        return neighborSet;
    }
}
