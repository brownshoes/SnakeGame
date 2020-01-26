package SnakePro;

import javax.swing.*;
import java.awt.*;

public class SnakeJFrame extends JFrame {
    private static String FRAME_TITLE = "Java Snake";

    SnakeJFrame() {
        this.setTitle(FRAME_TITLE);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //centerGui();
    }

    private void centerGui() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
    }
}
