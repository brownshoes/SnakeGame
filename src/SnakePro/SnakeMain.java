package SnakePro;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeMain {
    public static final int FRAME_WIDTH = 900;
    public static final int FRAME_HEIGHT = 600;
    public static final int PELLET_SIZE = 10;
    public static final int BORDER_THICKNESS = 10;
    public static final int TIMER_SPEED = 50;
    public static final int MOVEMENT_INCREMENT = 10;

    public static final Color BORDER_COLOR = Color.gray;
    private static final Color START_COLOR = Color.red;

    private final int startX;
    private final int startY;
    private final int startDirection;

    private static final int NUMBER_OF_ROBOT_SNAKES = 2;
    private static final boolean PLAYER_SNAKE = true;

    private SnakeJFrame snakeJFrame = new SnakeJFrame();
    private SnakePellet snakePellet = new SnakePellet();
    private SnakeComponent snakeComponent;
    private Snake snakePlayer;
    private ArrayList<Snake> robotSnakes = new ArrayList<>();
    private ArrayList<Snake> snakes = new ArrayList<>();

    SnakeMain() {
        this.startX = 40;
        this.startY = 10;
        this.startDirection = Snake.RIGHT;

        startUp();
    }

    private void startUp() {
        initializeSnakes();
        snakePellet = new SnakePellet().createSnakePellet(snakes);
        snakeComponent = new SnakeComponent(snakes, snakePellet);
        initializeKeyboardListener();
        initializeComponent();
        new SnakeEngine(snakes, snakeComponent, snakePellet).startEngine();
        startRobots();
    }

    private void initializeSnakes() {
        if (SnakeMain.PLAYER_SNAKE) {
            snakePlayer = new Snake("player", this.startX, this.startY, startDirection, SnakeMain.START_COLOR, true);
            snakes.add(snakePlayer);
        }

        //TODO clean up snake initialization
        if (SnakeMain.NUMBER_OF_ROBOT_SNAKES > 0) {
            Random rand = new Random();
            int x = this.startX;
            int y = this.startY;

            for (int count = 0; count < SnakeMain.NUMBER_OF_ROBOT_SNAKES; count++) {

                if (y > SnakeMain.FRAME_HEIGHT - 40) {
                    x += 80;
                } else {
                    y += 40;
                }

                String snakeName = "robot" + String.valueOf(count);
                Snake snakeRobot = new Snake(snakeName, x, y, this.startDirection,
                        new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)), false);
                snakes.add(snakeRobot);
                robotSnakes.add(snakeRobot);
            }
        }
    }

    private void initializeKeyboardListener() {
        snakeJFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent key) {
                switch (key.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (snakePlayer.currentDirection != Snake.RIGHT) {
                            snakePlayer.currentDirection = Snake.LEFT;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (snakePlayer.currentDirection != Snake.LEFT) {
                            snakePlayer.currentDirection = Snake.RIGHT;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (snakePlayer.currentDirection != Snake.UP) {
                            snakePlayer.currentDirection = Snake.DOWN;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (snakePlayer.currentDirection != Snake.DOWN) {
                            snakePlayer.currentDirection = Snake.UP;
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { }

            @Override
            public void keyTyped(KeyEvent e) { }
        });
    }

    private void initializeComponent() {
        snakeComponent.setBackground(Color.BLACK);
        snakeJFrame.add(snakeComponent);
        Dimension gameSize = new Dimension(SnakeMain.FRAME_WIDTH, SnakeMain.FRAME_HEIGHT);
        snakeJFrame.getContentPane().setPreferredSize(gameSize);
        snakeJFrame.pack();
        snakeJFrame.setVisible(true);
    }

    private void startRobots() {
        for (Snake robotSnake: robotSnakes) {
            new Thread(new SnakeRobot(robotSnake, snakes, snakePellet)).start();
        }
    }

    public static void main(String args []) {
        new SnakeMain();
    }
}
