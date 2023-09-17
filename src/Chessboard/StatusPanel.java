//状态栏
package Chessboard;

import Chessboard.ChessboardComponent;
import Chessboard.ChessboardFrame;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private static JLabel playerLabel, scoreLabel, spaceLabel;
    private static JLabel modelLabel;

    public StatusPanel() {
        int width = (int) ChessboardFrame.getDefaultWidth();
        int height = (int) ChessboardFrame.getDefaultHeight() / 5;

//        当前玩家
        playerLabel = new JLabel();
        playerLabel.setSize((int) (width * 0.4), height);
        playerLabel.setFont(new Font("Calibri", Font.ITALIC, 25));
        setPlayerText(get_Player(ChessboardFrame.getCurrent_Player()));
        add(playerLabel);
//        当前玩家

//        空格
        spaceLabel = new JLabel();
        spaceLabel.setText(setSpace(10));
        add(spaceLabel);
//        空格

//        模式
        modelLabel = new JLabel();
        modelLabel.setText("Player VS Player");
        modelLabel.setFont(new Font("Calibri", Font.BOLD, 25));
        add(modelLabel);
//        模式

//        空格
        spaceLabel = new JLabel();
        spaceLabel.setText(setSpace(7));
        add(spaceLabel);
//        空格

//        双方子数
        scoreLabel = new JLabel();
        scoreLabel.setLocation((int) (width * 0.4), 10);
        scoreLabel.setSize((int) (width * 0.5), height);
        scoreLabel.setFont(new Font("Calibri", Font.ITALIC, 20));
        this.setScoreText(2, 2);
        add(scoreLabel);
//        双方子数
    }

    private String setSpace(int x) {
        String ret = "";
        for (int i = 1; i <= x; i++)
            ret += ' ';
        return ret;
    }

    public String get_Player(int a) {
        if (a == 0) return "BLACK";
        else return "WHITE";
    }

    public void setScoreText(int black, int white) {
        scoreLabel.setText(String.format("BLACK: %d  WHITE: %d", black, white));
    }

    public void setPlayerText(String playerText) {
        playerLabel.setText(playerText + "'s turn");
    }

    public void setModelText(String modelText) {
        modelLabel.setText(modelText);
    }

    public void draw() {
        repaint();
    }
}
