package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Board extends JPanel implements KeyListener {
    public static final int BOARD_WIDTH = 15;
    public static final int BOARD_HEIGHT = 20;
    public static final int BLOCK_SIZE = 30;
    private final Timer looper;

    public static final Color VERY_LIGHT_BLUE = new Color(51, 204, 255);
    public static final Color VERY_LIGHT_GREEN = new Color(102, 255, 102);
    public static final Color PURPLE = new Color(102, 0, 153);
    public static final Color VERY_DARK_RED = new Color(153, 0, 0);


    private final BufferedImage background;
    private final Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];

    private final BlockShape[] shape = new BlockShape[8];
    private BlockShape currentShape;
    private int score = 0;
    private int maxscore = 0;
    private boolean gameOver = false;



    public Board() {

        background = ImageLoader.loadImage("image.png");
        shape[0] = new BlockShape(new int[][]{
                {1, 0}, {1, 1}, {0, 1}
        }, this, Color.blue);
        shape[1] = new BlockShape(new int[][]{
                {1, 1}, {1, 0}, {1, 0}, {1, 0}
        }, this, VERY_LIGHT_GREEN);
        shape[2] = new BlockShape(new int[][]{
                {1, 0}, {1, 1}, {1, 0}
        }, this, Color.green);
        shape[3] = new BlockShape(new int[][]{
                {1, 0}, {1, 0}, {1, 1}
        }, this, VERY_LIGHT_BLUE);
        shape[4] = new BlockShape(new int[][]{
                {1, 1}, {1, 1}
        }, this, VERY_DARK_RED);
        shape[5] = new BlockShape(new int[][]{
                {1, 1, 1, 1}
        }, this, PURPLE);
        shape[6] = new BlockShape(new int[][]{
                {1, 0, 1}, {1, 1, 1}
        }, this, Color.CYAN);
        shape[7] = new BlockShape(new int[][]{
                {1, 1, 0}, {0, 1, 1}
        }, this, Color.PINK);
        currentShape = randomShape();


        looper = new Timer(0, e -> {
            try {
                fallBlock();
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        time();
        looper.start();
    }

    ThreadTime threadTime = new ThreadTime();

    public void time() {
        threadTime.start();
    }


    public BlockShape randomShape() {
        Random random = new Random();
        int randomIndex = random.nextInt(shape.length);
        return shape[randomIndex];
    }

    public void fallBlock() throws IOException {
        if (!gameOver) {
            currentShape.spawnBlock();
            repaint();
        }
    }


    public void addScore() throws IOException {
        score++;
        File file = new File("Score.txt");
        PrintWriter print = new PrintWriter(file);
        if (score > maxscore) {
            maxscore = score;
        }
        print.println(maxscore);
        print.close();
    }

    public int getScore() {
        return score;
    }

    public Color[][] getBoard() {
        return board;
    }

    public void setCurrentShape() {
        currentShape = randomShape();
        currentShape.setDelayTimeForMove(currentShape.getNormal());
        currentShape.setX((int) (Math.random() * 11));
        currentShape.setY(0);
        isGameOver();
    }

    public void isGameOver() {
        int[][] coordinates = currentShape.getCoordinates();
        for (int i = 0; i < coordinates.length; i++) {
            for (int j = 0; j < coordinates[0].length; j++) {
                if (coordinates[i][j] != 0) {
                    if (board[i + currentShape.getY()][j + currentShape.getX()] != null) {
                        gameOver = true;

                    }
                }
            }
        }
    }

    public void startGame() {
        stopGame();
        setCurrentShape();
        gameOver = false;
        looper.start();
    }

    public void stopGame() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = null;
            }
        }
        score = 0;
        looper.stop();
    }

    protected void paintComponent(Graphics g) {

        //background
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        //draw shape
        currentShape.drawBlock(g);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null) {
                    g.setColor(board[i][j]);
                    g.fillRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
        //draw board
        g.setColor(Color.white);
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            g.drawLine(0, BLOCK_SIZE * i, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * i);
        }
        for (int i = 0; i < BOARD_WIDTH + 1; i++) {
            g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, BLOCK_SIZE * BOARD_HEIGHT);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 18));
        g.drawString(" Score: " + score, 450, 60);
        g.drawString(" Time in Game " + threadTime.time, 450, 450);
        g.drawString(" Esc to quit", 450, 550);
        g.drawString(" Enter to restart game", 450, 500);
        g.drawString(" Best score: " + maxscore, 450, 90);
        if (gameOver) {
            g.setColor(Color.gray);
            g.setFont(new Font("Arial Black", Font.BOLD, 60));
            g.drawString("GAME OVER!!!", 50, 300);
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 32 -> currentShape.rotatedShape();
            case 37 -> currentShape.moveLeft();
            case 38 -> currentShape.speedDown();
            case 39 -> currentShape.moveRight();
            case 40 -> currentShape.speedUp();
            case KeyEvent.VK_ESCAPE -> System.exit(0);
            case KeyEvent.VK_ENTER -> startGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_DOWN) {
            currentShape.speedDown();
        }
    }

}

