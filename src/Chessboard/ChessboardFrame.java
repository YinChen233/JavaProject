package Chessboard;//import GameControler.GameControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sun.media.jfxmedia.MediaManager.getPlayer;

public class ChessboardFrame extends JFrame {
    private JPanel buttonPanel;
    public static double DEFAULT_WIDTH = 600;
    public static double DEFAULT_HEIGHT = 600;
    public static StatusPanel statusPanel = new StatusPanel();
    public ChessboardComponent chessboardComponent;
    public static int current_Player;


    public static void setCurrent_Player(int current_Player) {
        ChessboardFrame.current_Player = current_Player;
    }

    public static int getCurrent_Player() {
        return current_Player;
    }

    public static void setDEFAULT_WIDTH(double x) {
        DEFAULT_WIDTH = x;
    }

    public static void setDEFAULT_HEIGHT(double x) {
        DEFAULT_HEIGHT = x;
    }

    public static double getDefaultHeight() {
        return DEFAULT_HEIGHT;
    }

    public static double getDefaultWidth() {
        return DEFAULT_WIDTH;
    }

    public ChessboardFrame() {
        setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        chessboardComponent = new ChessboardComponent();
//        chessboardComponent.judgeAI();
//        监听窗口大小变化
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ChessboardFrame.setDEFAULT_HEIGHT(getHeight());
                ChessboardFrame.setDEFAULT_WIDTH(getWidth());
                repaint();
                double size = Math.min(DEFAULT_WIDTH / 10, DEFAULT_HEIGHT * 4 / 5 / 10);
                chessboardComponent.setDefault_Height(DEFAULT_HEIGHT * 4 / 5);
                chessboardComponent.setDefault_Width(DEFAULT_WIDTH);
                chessboardComponent.setBlankwidth(size);
                chessboardComponent.setBlankheight(size);
                repaint();
            }
        });
//        监听窗口大小变化

//        重新开始按钮
        JButton button1 = new JButton("Restart");
        button1.setFont(new Font("Calibri", Font.BOLD, 20));
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chessboardComponent.reset();
                chessboardComponent.judgeAI();
            }
        });
//        重新开始按钮

//        读档按钮
        JButton button2 = new JButton("Load");
        button2.setFont(new Font("Calibri", Font.BOLD, 20));
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = JOptionPane.showInputDialog(null, "input the path here");
                judgeInput(filePath);
                chessboardComponent.judgeAI();
            }
        });
//        读档按钮

//        存档按钮
        JButton button3 = new JButton("Save");
        button3.setFont(new Font("Calibri", Font.BOLD, 20));
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = JOptionPane.showInputDialog(null, "input the saving path here");
                int p = 0;
                try {
                    FileWriter fileWriter = new FileWriter(filePath);
                    int[][] currentBoard = new int[8][8];
                    currentBoard = chessboardComponent.getBoard();
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            int s;
                            s = currentBoard[i][j];
                            if (j != 7)
                                fileWriter.append(s + " ");
                            else
                                fileWriter.append(s + "");
                        }
                        fileWriter.append('\n');
                    }//棋盘->字符串
                    List<String> steps = chessboardComponent.getSteps();
                    for (String s : steps) {
                        fileWriter.append(s + '\n');
                    }
//                    int stepCount = chessboardComponent.getStepcount();
                    int current_Player = chessboardComponent.getCurrent_Player();
                    fileWriter.append(current_Player + "");
//                    步数与当前玩家->字符串
                    fileWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
//        存档按钮

//        菜单按钮
        JButton button4 = new JButton("Menu");
        button4.setFont(new Font("Calibri", Font.BOLD, 20));
//        更改模式时更改StatusPanel里的model
//                “Player VS Player”为玩家对玩家
//                “Player VS Computer”为玩家对电脑，玩家执黑
//                    将chessboardComponent的player_color改为0
//                “Computer VS Player”为玩家对电脑，玩家执白
//                    将chessboardComponent的player_color改为1
//        每次更改完执行一次 chessboardComponent.judgeAI();
//        不重置棋盘

//        菜单按钮

//        作弊按钮
        JButton button5 = new JButton("Cheat:OFF");
        button5.setFont(new Font("Calibri", Font.BOLD, 20));
        button5.setForeground(Color.BLACK);
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chessboardComponent.getCheatingModel() == 0) {
                    button5.setText("Cheat:ON");
                    button5.setForeground(Color.RED);
                    chessboardComponent.setCheatingModel(1);
                } else {
                    button5.setText("Cheat:OFF");
                    button5.setForeground(Color.BLACK);
                    chessboardComponent.setCheatingModel(0);
                }
            }
        });
//        作弊按钮

        JButton button6 = new JButton("Undo");
        button6.setFont(new Font("Calibri", Font.BOLD, 20));
        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chessboardComponent.getModel() == 1 && chessboardComponent.getStepcount() != chessboardComponent.getStepcount_load() + 1) {
                    if (chessboardComponent.getStepcount() < chessboardComponent.getStepcount_load() + 2) return;
                    chessboardComponent.setBoard(chessboardComponent.copy(chessboardComponent.getCopyBoard(chessboardComponent.getStepcount() - 2)));
                    chessboardComponent.setStepcount(chessboardComponent.getStepcount() - 2);
                } else {
                    if (chessboardComponent.getStepcount() < chessboardComponent.getStepcount_load() + 1) return;
                    chessboardComponent.setBoard(chessboardComponent.copy(chessboardComponent.getCopyBoard(chessboardComponent.getStepcount() - 1)));
                    //Q : 这里怎么把chessboaComponent里面的CopyBoardList,remove(stepcount)
                    chessboardComponent.setCurrent_Player(1 - chessboardComponent.getCurrent_Player());
                    chessboardComponent.setStepcount(chessboardComponent.getStepcount() - 1);
                }
//                System.out.printf("%d\n", chessboardComponent.getStepcount());
//                chessboardComponent.judgeAI();
                repaint();
            }
        });

        JButton button7 = new JButton("ChangeModel");
        button7.setFont(new Font("Calibri", Font.BOLD, 20));
        button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chessboardComponent.getModel() == 0) {
                    if (chessboardComponent.getPlayer_color() == 0)
                        statusPanel.setModelText("Player VS Computer");
                    else
                        statusPanel.setModelText("Computer VS Player");
                } else
                    statusPanel.setModelText("Player VS Player");
//                chessboardComponent.setModel(chessboardComponent.ChangeModel());
                chessboardComponent.ChangeModel();
                chessboardComponent.judgeAI();
                repaint();
            }
        });

        JButton button8 = new JButton("ChangeSide");
        button8.setFont(new Font("Calibri", Font.BOLD, 20));
        button8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chessboardComponent.getModel() == 0) return;
                if (chessboardComponent.getPlayer_color() == 0)
                    statusPanel.setModelText("Computer VS Player");
                else
                    statusPanel.setModelText("Player VS Computer");
                chessboardComponent.setPlayer_color(chessboardComponent.ChangeSide());
                chessboardComponent.judgeAI();
                repaint();
            }
        });

//        置入按钮
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

        buttonPanel.add(button5);
        buttonPanel.add(button6);
        buttonPanel.add(button7);
        buttonPanel.add(button8);
//        置入按钮
        add(statusPanel, BorderLayout.NORTH);//置入状态栏
        add(chessboardComponent, BorderLayout.CENTER);//置入棋盘
        add(buttonPanel, BorderLayout.SOUTH);//置入按钮

        pack();
        setVisible(true);
        setTitle("黑白棋");
    }

    //    错误弹窗
    public void showError(int num, String s) {
        JOptionPane.showMessageDialog(null, "Error " + num + "\n" + s);
    }
//    错误弹窗


    //    读入判断
    public void judgeInput(String filePath) {
        if (filePath.length() <= 4) {
            showError(106, "文件路径错误");
            return;
        }
        if (!filePath.substring(filePath.length() - 4, filePath.length()).equals(".txt")) {
            showError(104, "文件类型错误");
            return;
        }
        int[][] inputBoard = new int[8][8];
        ChessboardComponent inputChessboardComponent = new ChessboardComponent();
        inputChessboardComponent.setModel(0);
        List<String> steps = new ArrayList<String>();
        try {
            FileReader fileReader1 = new FileReader(filePath);
            BufferedReader fileReader = new BufferedReader(fileReader1);
            String s = "";
            int linecount = 0;
            int whiteC = 0, blackC = 0, spaceC = 0;
            int ifcurrent = 0;
            while ((s = fileReader.readLine()) != null) {
                int[] space = new int[9];
                space = getSpace(s);
                if (linecount <= 7) {//读入棋盘
                    if (space[0] != 7) {
                        showError(101, "棋盘大小错误");
                        fileReader.close();
                        return;
                    }
                    space[8] = s.length();
                    space[0] = -1;
                    for (int i = 0; i < 8; i++) {
                        String num = s.substring(space[i] + 1, space[i + 1]);
                        if (num.equals("0")) {
                            inputBoard[linecount][i] = 0;
                            spaceC++;
                        } else if (num.equals("-1")) {
                            inputBoard[linecount][i] = -1;
                            blackC++;
                        } else if (num.equals("1")) {
                            inputBoard[linecount][i] = 1;
                            whiteC++;
                        } else {
                            showError(102, "多余棋子：" + num);
                            fileReader.close();
                            return;
                        }
                    }
                } else {
                    if (whiteC == 0 || blackC == 0 || spaceC == 0) {//判断棋子种类
                        String wrong = "";
                        if (whiteC == 0) wrong += "缺少白棋";
                        if (blackC == 0) {
                            if (!wrong.equals("")) wrong += '、';
                            wrong += "缺少黑旗";
                        }
                        if (spaceC == 0) {
                            if (!wrong.equals("")) wrong += '、';
                            wrong += "缺少空位";
                        }
                        showError(102, wrong);
                        fileReader.close();
                        return;
                    }
                    if (s.length() <= 2) {//读入执棋方
                        String s1 = fileReader.readLine();
                        if (s1 != null) {
                            showError(106, "多余数据");
                            fileReader.close();
                            return;
                        }
                        int currentPlayer = changeS(s);
                        if (currentPlayer != 0 && currentPlayer != 1) {
                            showError(106, "执棋方错误");
                            fileReader.close();
                            return;
                        }
                        ifcurrent = 1;
                        if (!boardEqual(inputChessboardComponent.getBoard(), inputBoard)) {
                            showError(106, "棋盘与步骤不对应");
                            fileReader.close();
                            return;
                        }
                        chessboardComponent.setSteps(steps);
                        chessboardComponent.setCurrent_Player(currentPlayer);
                        chessboardComponent.setStepcount(linecount - 8);
                        chessboardComponent.setBoard(inputBoard);
//                        chessboardComponent.setStepcount_load(linecount - 8);
                        chessboardComponent.setCopyBoardList(inputChessboardComponent.getCopyBoardList());
                        return;
                    }
//                    读入步骤
                    if (space[0] != 3) {
                        showError(105, "第" + (linecount - 7) + "步 非合法步骤");
                        fileReader.close();
                        return;
                    }
                    space[4] = s.length();
                    String playerS = s.substring(0, space[1]);
                    int player = 0;
                    if (playerS.equals("Black")) player = 0;
                    else if (playerS.equals("White")) player = 1;
                    else {
                        showError(105, "第" + (linecount - 7) + "步 玩家错误");
                        fileReader.close();
                        return;
                    }
                    String rowS = "", colS = "";
                    rowS = s.substring(space[1] + 1, space[2]);
                    colS = s.substring(space[2] + 1, space[3]);
                    int row = changeS(rowS), col = changeS(colS);
                    if (row < 0 || row > 7 || col < 0 || col > 7) {
                        showError(105, "第" + (linecount - 7) + "步 非合法步骤");
                        fileReader.close();
                        return;
                    }
                    String cheatS = s.substring(space[3] + 1, space[4]);
                    int cheat = changeS(cheatS);
                    if (cheat != 0 && cheat != 1) {
                        showError(105, "第" + (linecount - 7) + "步 读入错误");
                        fileReader.close();
                        return;
                    }
                    inputChessboardComponent.setCheatingModel(cheat);
                    if (!inputChessboardComponent.isLegal(row, col, player)) {
                        showError(105, "第" + (linecount - 7) + "步 非合法步骤");
                        fileReader.close();
                        return;
                    }
                    inputChessboardComponent.setCurrent_Player(player);
                    inputChessboardComponent.operation(row, col);
                    steps.add(s);
//                    读入步骤
                }
                linecount++;
            }
            if (ifcurrent == 0) {
                showError(103, "无行棋方");
                fileReader.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
//    读入判断

    private boolean boardEqual(int[][] board1, int[][] board2) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board1[i][j] != board2[i][j])
                    return false;
            }
        }
        return true;
    }

    private int changeS(String s) {
        for (int i = 0; i < 8; i++) {
            if (s.equals(i + ""))
                return i;
        }
        return -1;
    }

    public int[] getSpace(String s) {
        int[] ret = new int[9];
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ')
                ret[++ret[0]] = i;
        }
        return ret;
    }

    public static void setPlayer(int current_player) {
        statusPanel.setPlayerText(statusPanel.get_Player(current_player));
    }


    public class ClickingAction extends AbstractAction {
        public ClickingAction(String name, Icon icon, Color c) {
            putValue(Action.NAME, name);
            putValue(Action.SMALL_ICON, icon);
            putValue(Action.SHORT_DESCRIPTION, "Set panel color to" + name.toLowerCase());
            putValue("color", c);
        }

        public void actionPerformed(ActionEvent event) {
            Color c = (Color) getValue("color");
            buttonPanel.setBackground(c);
        }
    }


    public static void setCount(int black, int white) {
        statusPanel.setScoreText(black, white);
    }

}
