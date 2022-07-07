package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BlockShape extends JFrame {
    private int x = 4;
    private int y = 0;
    private int normal = 600;
    private long beginTime;
    private int delayTimeForMove = normal;
    private int moveLeftRight;
    private boolean cantGoDown = false;
    private final Board board;
    private final Color color;


    private int[][] coordinates;

    public BlockShape(int[][] coordinates, Board board, Color color) {
        this.coordinates = coordinates;
        this.board = board;
        this.color = color;
    }

    public int[][] getCoordinates() {
        return coordinates;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDelayTimeForMove(int s) {
        delayTimeForMove = s;
    }

    public int getNormal() {
        if (board.getScore() == 0) {
            normal = 600;
        } else if (board.getScore() > 10) {
            normal = 550;
        } else if (board.getScore() > 20) {
            normal = 500;
        } else if (board.getScore() > 30) {
            normal = 450;
        } else if (board.getScore() > 40) {
            normal = 400;
        } else if (board.getScore() > 50) {
            normal = 350;
        } else if (board.getScore() > 60) {
            normal = 300;
        } else if (board.getScore() > 80) {
            normal = 250;
        } else if (board.getScore() > 100) {
            normal = 200;
        } else if (board.getScore() > 150) {
            normal = 150;
        } else if (board.getScore() > 200) {
            normal = 100;
        }
        return normal;
    }

    public void clearLine() {
        int clearedLines;
        int fullLine = board.getBoard().length - 1;
        for (int i = fullLine; i > 0; i--) {
            clearedLines = 0;
            for (int j = 0; j < board.getBoard()[0].length; j++) {
                if (board.getBoard()[i][j] != null)
                    clearedLines++;
                board.getBoard()[fullLine][j] = board.getBoard()[i][j];
            }
            if (clearedLines < board.getBoard()[0].length) {
                fullLine--;
            }
        }
    }

    public void rotatedShape() {
        int[][] rotatedShape = transposeShape(coordinates);
        reverseRows(rotatedShape);
        //check collision for left, right, down
        if ((x + rotatedShape[0].length > Board.BOARD_WIDTH) || (y + rotatedShape.length > Board.BOARD_HEIGHT)) {
            return;
        }
        //check collision with other blocks
        for (int i = 0; i < rotatedShape.length; i++) {
            for (int j = 0; j < rotatedShape[i].length; j++) {
                if (rotatedShape[i][j] == 1) {
                    if (board.getBoard()[y + i][x + j] != null) {
                        return;
                    }
                }
            }
        }
        coordinates = rotatedShape;

    }


    public int[][] transposeShape(int[][] transposeShape) {
        int[][] tmp = new int[transposeShape[0].length][transposeShape.length];
        for (int i = 0; i < transposeShape.length; i++) {
            for (int j = 0; j < transposeShape[0].length; j++) {
                tmp[j][i] = transposeShape[i][j];
            }
        }
        return tmp;
    }

    public void reverseRows(int[][] transposeShape) {
        int mid = transposeShape.length / 2;
        for (int i = 0; i < mid; i++) {
            int[] tmp = transposeShape[i];
            transposeShape[i] = transposeShape[transposeShape.length - i - 1];
            transposeShape[transposeShape.length - i - 1] = tmp;
        }
    }


    public void spawnBlock() throws IOException {
        if (cantGoDown) {
            cantGoDown = false;
            //fill color for board
            for (int i = 0; i < coordinates.length; i++) {
                for (int j = 0; j < coordinates[0].length; j++) {
                    if (coordinates[i][j] != 0) {
                        board.getBoard()[y + i][x + j] = color;

                    }
                }
            }

            clearLine();
            board.addScore();
            board.setCurrentShape();
            return;
        }

        //move Left or Right
        boolean moveX = true;
        if (((x + moveLeftRight + coordinates[0].length) <= Board.BOARD_WIDTH) && (moveLeftRight + x >= 0)) {
            for (int i = 0; i < coordinates.length; i++) {
                for (int j = 0; j < coordinates[i].length; j++) {
                    if (coordinates[i][j] != 0) {
                        if (board.getBoard()[y + i][x + moveLeftRight + j] != null) {
                            moveX = false;
                        }
                    }
                }
            }
            if (moveX) {
                x += moveLeftRight;
            }
        }
        moveLeftRight = 0;

        if (System.currentTimeMillis() - beginTime > delayTimeForMove) {
            //stop block at the end
            if (y + coordinates.length < Board.BOARD_HEIGHT) {
                for (int i = 0; i < coordinates.length; i++) {
                    for (int j = 0; j < coordinates[0].length; j++) {
                        if (coordinates[i][j] != 0) {
                            //
                            if (board.getBoard()[y + i + 1][x + moveLeftRight + j] != null) {
                                cantGoDown = true;
                            }
                        }
                    }
                }
                //
                if (!cantGoDown)
                    y++;
            } else {
                cantGoDown = true;
            }
            beginTime = System.currentTimeMillis();
        }

    }


    public void drawBlock(Graphics g) {


        for (int i = 0; i < coordinates.length; i++) {
            for (int j = 0; j < coordinates[i].length; j++) {
                if (coordinates[i][j] == 1) {
                    g.setColor(color);
                    g.fillRect(Board.BLOCK_SIZE * j + x * Board.BLOCK_SIZE, Board.BLOCK_SIZE * i + y * Board.BLOCK_SIZE, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
                }
            }
        }

    }


    public void speedUp() {
        delayTimeForMove = 100;
    }

    public void speedDown() {
        delayTimeForMove = normal;

    }

    public void moveRight() {
        moveLeftRight = 1;
    }

    public void moveLeft() {
        moveLeftRight = -1;
    }


}
