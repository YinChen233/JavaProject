import Chessboard.ChessboardComponent;
import Chessboard.ChessboardFrame;
import Chessboard.MenuFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Main {/*
    public static int[] getSz(String s){

    }*/
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new MenuFrame();
            frame.setTitle("黑白棋");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });

    }
}
