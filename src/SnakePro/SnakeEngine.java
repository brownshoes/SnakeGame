package SnakePro;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeEngine {

    private SnakePellet snakePellet;
    private SnakeComponent snakeComponent;
    private ArrayList<Snake> snakes;

    public SnakeEngine(ArrayList<Snake> snakes, SnakeComponent snakeComponent, SnakePellet snakePellet){
        this.snakes = snakes;
        this.snakeComponent = snakeComponent;
        this.snakePellet = snakePellet;
    }

    public void startEngine(){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Snake snake: snakes){
                    checkForBoundary(snake);
                    if(snake.checkForCollision(snakes)){
                        snake.resetSnake();
                        //snakePellet.resetPelletsEaten();
                    }
                    if(snakePellet.checkForPelletEaten(snake)){
                        snake.grow(snakePellet.getPelletColor());
                        snakePellet.createSnakePellet(snakes);
                    }

                    switch(snake.currentDirection){
                        case Snake.LEFT:
                            snake.xHeadLocation -= SnakeMain.MOVEMENT_INCREMENT;
                            break;
                        case Snake.RIGHT:
                            snake.xHeadLocation += SnakeMain.MOVEMENT_INCREMENT;
                            break;
                        case Snake.UP:
                            snake.yHeadLocation -= SnakeMain.MOVEMENT_INCREMENT;
                            break;
                        case Snake.DOWN:
                            snake.yHeadLocation += SnakeMain.MOVEMENT_INCREMENT;
                    }

                    snake.updateSnake(snakes);
                    snakeComponent.repaint();
                }

            }
        }, 0, SnakeMain.TIMER_SPEED);
    }

    private void checkForBoundary(Snake snake){
        switch(snake.currentDirection){
            case Snake.LEFT:
                if(snake.xHeadLocation <= SnakeMain.BORDER_THICKNESS){
                    if(checkUpperLeftCorner(snake)){
                        snake.currentDirection = Snake.DOWN;
                    }else if(checkLowerLeftCorner(snake)){
                        snake.currentDirection = Snake.UP;
                    } else {
                        boolean direction = new Random().nextBoolean();
                        if(direction) {
                            snake.currentDirection = Snake.UP;
                        }else {
                            snake.currentDirection = Snake.DOWN;
                        }
                    }
                    snake.xHeadLocation = SnakeMain.BORDER_THICKNESS;
                }
                break;

            case Snake.RIGHT:
                if(snake.xHeadLocation >= SnakeMain.FRAME_WIDTH - SnakeMain.BORDER_THICKNESS - Snake.SNAKE_SIZE) {
                    if(checkUpperRightCorner(snake)){
                        snake.currentDirection = Snake.DOWN;
                    }else if(checkLowerRightCorner(snake)){
                        snake.currentDirection = Snake.UP;
                    } else {
                        boolean direction = new Random().nextBoolean();
                        if(direction) {
                            snake.currentDirection = Snake.UP;
                        }else {
                            snake.currentDirection = Snake.DOWN;
                        }
                    }
                    snake.xHeadLocation = SnakeMain.FRAME_WIDTH - SnakeMain.BORDER_THICKNESS - Snake.SNAKE_SIZE;
                }
                break;

            case Snake.UP:
                if(snake.yHeadLocation <= SnakeMain.BORDER_THICKNESS){
                    if(checkUpperLeftCorner(snake)){
                        snake.currentDirection = Snake.RIGHT;
                    }else if(checkUpperRightCorner(snake)){
                        snake.currentDirection = Snake.LEFT;
                    } else {
                        boolean direction = new Random().nextBoolean();
                        if(direction) {
                            snake.currentDirection = Snake.LEFT;
                        }else {
                            snake.currentDirection = Snake.RIGHT;
                        }
                    }
                    snake.yHeadLocation = SnakeMain.BORDER_THICKNESS;
                }
                break;

            case Snake.DOWN:
                if(snake.yHeadLocation >= SnakeMain.FRAME_HEIGHT - SnakeMain.BORDER_THICKNESS - Snake.SNAKE_SIZE){
                    if(checkUpperLeftCorner(snake)){
                        snake.currentDirection = Snake.RIGHT;
                    }else if(checkLowerRightCorner(snake)){
                        snake.currentDirection = Snake.LEFT;
                    } else {
                        boolean direction = new Random().nextBoolean();
                        if(direction) {
                            snake.currentDirection = Snake.LEFT;
                        }else {
                            snake.currentDirection = Snake.RIGHT;
                        }
                    }
                    snake.yHeadLocation = SnakeMain.FRAME_HEIGHT - SnakeMain.BORDER_THICKNESS - Snake.SNAKE_SIZE;
                }
                break;
        }
    }

    private boolean checkUpperLeftCorner(Snake snake) {
        return snake.xHeadLocation == SnakeMain.BORDER_THICKNESS
                && snake.yHeadLocation == SnakeMain.BORDER_THICKNESS;
    }
    private boolean checkLowerLeftCorner(Snake snake) {
        return snake.xHeadLocation == SnakeMain.BORDER_THICKNESS
                && snake.yHeadLocation == SnakeMain.FRAME_HEIGHT - SnakeMain.BORDER_THICKNESS - Snake.SNAKE_SIZE;
    }
    private boolean checkUpperRightCorner(Snake snake) {
        return  snake.xHeadLocation == SnakeMain.FRAME_WIDTH - SnakeMain.BORDER_THICKNESS - Snake.SNAKE_SIZE &&
                snake.yHeadLocation == SnakeMain.BORDER_THICKNESS;
    }
    private boolean checkLowerRightCorner(Snake snake) {
        return snake.xHeadLocation == SnakeMain.FRAME_WIDTH - SnakeMain.BORDER_THICKNESS - Snake.SNAKE_SIZE &&
                snake.yHeadLocation == SnakeMain.FRAME_HEIGHT - SnakeMain.BORDER_THICKNESS - Snake.SNAKE_SIZE;
    }
}
