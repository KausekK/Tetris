package com.company;

import javax.swing.*;

public class Game extends JFrame {


    public Game()  {
        Board board = new Board();
        this.add(board);
        setVisible(true);
        setTitle("Tetris");
        setSize(650, 635);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        add(board);
        addKeyListener(board);

    }


}







