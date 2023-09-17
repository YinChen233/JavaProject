package Chessboard;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Timer;


public class ChessboardComponent extends JComponent {
    private double Default_Width = ChessboardFrame.getDefaultWidth();
    private double Default_Height = ChessboardFrame.getDefaultHeight();
    private int model = 0;//AI
    private int player_color = 0;//玩家颜色 0为黑，1为白
    private int bz = 0;//是否AI回合
    private int cheatingModel = 0;//作弊
    private double blankwidth = Default_Width / 8;
    private double blankheight = Default_Height / 8;
    private double Board_x = Default_Width / 2 - 4 * blankwidth;//棋盘位置
    private double Board_y = Default_Height / 2 - 4 * blankheight;//棋盘位置
    private double CHESS_RADIUS = 18;
    private int stepcount = 0, current_Player = 0;//这是已经下过的步数  当前玩家
    private int stepcount_load = 0;
    private int[][] Board;

    private int[][] CopyBoard;
    private ArrayList<int[][]> CopyBoardList = new ArrayList<>();

    private int[][] Tips = new int[8][8];//提示
    private int[][] dir = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};//方向
    private Color gridColor = new Color(255, 150, 50);
    private List<String> steps = new ArrayList<String>();//记录每一步

//    JLabel scoreLabel = new JLabel();//双方子数
//    JLabel playerLabel = new JLabel();//当前玩家


    private ArrayList<Ellipse2D> chess;
    private Ellipse2D current;//这两行没用
    private Image image;

    public void setPlayer_color(int player_color) {
        this.player_color = player_color;
    }

    //    更改作弊模式
    public void setCheatingModel(int cheatintModel) {
        this.cheatingModel = cheatintModel;
        if (Finished()) {
            String winner = getWinner();
            if (winner.equals("null"))
                showEnding("It ends in a draw");
            else
                showEnding(winner + " Win ! ! !");
            return;
        }
        judgeAI();
    }

    //    更改作弊模式

    //    结束弹窗
    private void showEnding(String s) {
        JOptionPane.showMessageDialog(null, s);
    }
//    结束弹窗


    public void setCopyBoardList(ArrayList<int[][]> copyBoardList) {
        CopyBoardList = copyBoardList;
    }

    public int getStepcount_load() {
        return stepcount_load;
    }

    public void setStepcount_load(int stepcount_load) {
        this.stepcount_load = stepcount_load;
    }

    public void setBz(int bz) {
        this.bz = bz;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public void setBoard(int[][] board) {
        Board = copy(board);
//        CopyBoardList.set(stepcount, copy(Board));
//        steps = new ArrayList<String>();
        repaint();
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public int getCheatingModel() {
        return cheatingModel;
    }

    public int[][] getCopyBoard(int i) {
        return CopyBoardList.get(i);
    }

    public int getCurrent_Player() {
        return current_Player;
    }

    public double getBlankheight() {
        return blankheight;
    }

    public double getBlankwidth() {
        return blankwidth;
    }

    public int[][] getBoard() {
        return Board;
    }

    public int getStepcount() {
        return stepcount;
    }

    public List<String> getSteps() {
        return steps;
    }

    //    更改棋盘大小
    public void setDefault_Height(double default_Height) {
        Default_Height = default_Height;
        Board_y = Default_Height / 2.0 - 5.0 * blankheight;
    }

    public void setDefault_Width(double default_Width) {
        Default_Width = default_Width;
        Board_x = Default_Width / 2.0 - 4.3 * blankwidth;
    }

    public void setBlankheight(double blankheight) {
        this.blankheight = blankheight;
        CHESS_RADIUS = blankheight / 3.0;
    }

    public void setBlankwidth(double blankwidth) {
        this.blankwidth = blankwidth;
        CHESS_RADIUS = blankwidth / 3.0;
    }

    public void setStepcount(int stepcount) {
        this.stepcount = stepcount;
    }

    public void setCurrent_Player(int current_Player) {
        this.current_Player = current_Player;
    }

    //    更改棋盘大小

    public ChessboardComponent() {
        image = new ImageIcon("chessblank.png").getImage();
        Board = new int[8][8];

        chess = new ArrayList<>();
        current = null;//没用

        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());

        Board[3][3] = 1;
        Board[4][4] = 1;
        Board[3][4] = -1;
        Board[4][3] = -1;
        steps = new ArrayList<String>();
        Tips = copy(Board);

        CopyBoardList.add(copy(getBoard()));
    }

    public void reset() {
        Board = new int[8][8];
        Board[3][3] = 1;
        Board[4][4] = 1;
        Board[3][4] = -1;
        Board[4][3] = -1;
        stepcount = 0;
        stepcount_load = 0;
        current_Player = 0;
        Tips = copy(Board);
        steps = new ArrayList<String>();
        repaint();

        CopyBoardList = new ArrayList<>();
        CopyBoardList.add(copy(getBoard()));
        CopyBoard = new int[8][8];
    }

    public int getPlayer_color() {
        return player_color;
    }

    public int getModel() {
        return model;
    }

    public void ChangeModel() {
        model = 1 - model;
    }

    public int ChangeSide() {
        if (player_color == 0) {
            player_color = 1;
        } else {
            player_color = 0;
        }
        return player_color;
    }

    public void paintComponent(Graphics g) {
        //    更改状态栏
        ChessboardFrame.setCurrent_Player(current_Player);
        int[] counts = getCounts();
        ChessboardFrame.setCount(counts[0], counts[1]);
        ChessboardFrame.setPlayer(current_Player);
        //    更改状态栏
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle2D grid = new Rectangle2D.Double(Board_x + i * blankwidth + 1.0, Board_y + j * blankheight + 1.0, blankwidth - 2.0, blankheight - 2.0);
                g2.setColor(gridColor);
                g2.fill(grid);
                g2.draw(grid);
            }
        }
        double chess_x = blankwidth / 2.0 - CHESS_RADIUS;
        double chess_y = blankheight / 2.0 - CHESS_RADIUS;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Board[j][i] == -1) {
                    Ellipse2D BlackChess = new Ellipse2D.Double(Board_x + i * blankwidth + chess_x, Board_y + j * blankheight + chess_y, CHESS_RADIUS * 2.0, CHESS_RADIUS * 2.0);
                    g2.setColor(Color.BLACK);
                    g2.fill(BlackChess);
                    g2.draw(BlackChess);
                } else if (Board[j][i] == 1) {
                    Ellipse2D WhiteChess = new Ellipse2D.Double(Board_x + i * blankwidth + chess_x, Board_y + j * blankheight + chess_y, CHESS_RADIUS * 2.0, CHESS_RADIUS * 2.0);
                    g2.setColor(Color.WHITE);
                    g2.fill(WhiteChess);
                    g2.draw(WhiteChess);
                } else if (Tips[j][i] == 2) {
                    Ellipse2D TipChess = new Ellipse2D.Double(Board_x + i * blankwidth + chess_x, Board_y + j * blankheight + chess_y, CHESS_RADIUS * 2.0, CHESS_RADIUS * 2.0);
                    g2.setColor(Color.BLACK);
                    g2.draw(TipChess);
                }
            }
        }
        Tips = copy(Board);
        getTips(current_Player);
        repaint();
    }

    //    计数
    private int[] getCounts() {
        int[] ret = {0, 0};
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Board[i][j] == -1) ret[0]++;
                if (Board[i][j] == 1) ret[1]++;
            }
        }
        return ret;
    }

    //    计数
    public void operation(int row, int column) {
        if ((isLegal(row, column, current_Player) && (model == 0 || current_Player == player_color)) || bz == 1) {
            if (bz == 0) {          //非AI回合
                String player = "";
                if (current_Player == 0) player = "Black";
                else player = "White";
                String s = player + " " + row + " " + column + " " + cheatingModel;
                steps.add(s);

                setChess(row, column, current_Player);
                stepcount++;
                current_Player = 1 - current_Player;

                CopyBoardList.add(copy(getBoard()));
//                CopyBoard = CopyBoardList.get(stepcount - 1);

                playMusic();

            } else
                bz = 0;
            repaint();
            if (Finished()) {
                String winner = getWinner();
                if (winner.equals("null"))
                    showEnding("It ends in a draw");
                else
                    showEnding(winner + " Win ! ! !");
                return;
            }
            if (nonPlace(current_Player)) {
                current_Player = 1 - current_Player;
                return;
            }
            Tips = copy(Board);
            getTips(current_Player);
            repaint();
            if (model == 1) { //AI模式
//                    延时1s
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        AI_Model();
                        if (Finished()) {
                            String winner = getWinner();
                            if (winner.equals("null"))
                                showEnding("It ends in a draw");
                            else
                                showEnding(winner + " Win ! ! !");
                            return;
                        }
                        while (!Finished() && nonPlace(current_Player)) {
                            repaint();
                            current_Player = 1 - current_Player;
//                                鼠标点击，AI再行一步
                            try {
                                bz = 1;
                                Robot robot = new Robot();
                                robot.mouseMove((int) blankwidth * 3 + 1, (int) blankheight * 3 + 1);
                                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            } catch (AWTException ex) {
                                ex.printStackTrace();
                            }
//                                鼠标点击，AI再行一步
                        }
                        if (Finished()) {
                            String winner = getWinner();
                            if (winner.equals("null"))
                                showEnding("It ends in a draw");
                            else
                                showEnding(winner + " Win ! ! !");
                            return;
                        }
                    }
                }, 1000);
//                    延时1s
            }
        }
        if (Finished()) {
            String winner = getWinner();
            if (winner.equals("null"))
                showEnding("It ends in a draw");
            else
                showEnding(winner + " Win ! ! !");
            return;
        }
        if (nonPlace(current_Player)) {
            current_Player = 1 - current_Player;
        }
    }

    public ArrayList<int[][]> getCopyBoardList() {
        return CopyBoardList;
    }

    private class MouseHandler extends MouseAdapter {
        public void mousePressed(MouseEvent event) {
            double chessX = event.getX() - Board_x;
            double chessY = event.getY() - Board_y;
            double x = chessX / blankwidth, y = chessY / blankheight;
            int row = (int) chessY / (int) blankheight, column = (int) chessX / (int) blankwidth;
            operation(row, column);
        }
    }

    //    获取提示
    private void getTips(int current_Player) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isLegal(i, j, current_Player)) {
                    Tips[i][j] = 2;
                }
            }
        }
    }
    //    获取提示

    //    无处下子
    public boolean nonPlace(int chr) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isLegal(i, j, chr) || (Board[i][j] == 0 && cheatingModel == 1 && current_Player == player_color))
                    return false;
            }
        }
        return true;
    }

    //    无处下子

    //    是否合法
    public boolean isLegal(int x, int y, int chr) {
        if (!inBoard(x, y)) return false;
        if (Board[x][y] != 0) return false;
        if (cheatingModel == 1 && (chr == player_color && model == 1 || model == 0))
            return true;
        int c = 1, ic = 1;
        if (chr == 0) c = -c;
        else ic = -ic;
        for (int k = 0; k < 8; k++) {
            int x1 = x + dir[k][0], y1 = y + dir[k][1];
            if (!inBoard(x1, y1) || Board[x1][y1] != ic) continue;
            while (inBoard(x1, y1) && Board[x1][y1] == ic) {
                x1 += dir[k][0];
                y1 += dir[k][1];
            }
            if (inBoard(x1, y1) && Board[x1][y1] == c) {
                return true;
            }
        }
        return false;
    }
    //    是否合法

    //    下子
    public void setChess(int x, int y, int chr) {

        int c = 1, ic = 1;
        if (chr == 0) c = -1;
        else ic = -ic;
        Board[x][y] = c;
        for (int k = 0; k < 8; k++) {
            int x1 = x + dir[k][0], y1 = y + dir[k][1];
            if (!inBoard(x1, y1) || Board[x1][y1] != ic) continue;
            while (inBoard(x1, y1) && Board[x1][y1] == ic) {
                x1 += dir[k][0];
                y1 += dir[k][1];
            }
            if (inBoard(x1, y1) && Board[x1][y1] == c) {
                int x2 = x, y2 = y;
                while (x2 != x1 || y2 != y1) {
                    Board[x2][y2] = c;
                    x2 += dir[k][0];
                    y2 += dir[k][1];
                }
            }
        }
    }

    //    下子
    //    数组复制
    public int[][] copy(int[][] map) {
        int[][] ret = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ret[i][j] = map[i][j];
            }
        }
        return ret;
    }
    //    数组复制

    private boolean inBoard(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public boolean Finished() {
        return (nonPlace(0)) && (nonPlace(1));
    }

    //     检查当前玩家是否合法
    public void changeCurrent_Player() {
        current_Player = 1 - current_Player;
    }

    public void judgeAI() {
        if (Finished()) {
            String winner = getWinner();
            if (winner.equals("null"))
                showEnding("It ends in a draw");
            else
                showEnding(winner + " Win ! ! !");
            return;
        }
        if ((!AIturn())) {
            if (nonPlace(getCurrent_Player()))
                changeCurrent_Player();
        }
        if (AIturn()) {
            setBz(1);
            operation(3, 3);
            /*
            try {
                MouseEvent e = null;
                int x = e.getX();
                int y = e.getY();
                Robot robot = new Robot();
//                robot.mouseMove((int) getBlankwidth() * 3 + 1, (int) getBlankheight() * 3 + 1);
//                robot.mouseMove(x, y - 50);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//                robot.mouseMove(x, y);
            } catch (AWTException ex) {
                ex.printStackTrace();
            }*/
        }//点击鼠标，AI先行
        repaint();
    }
    //    检查当前玩家是否合法


    //    是否AI回合
    public boolean AIturn() {
        if (model == 1 && current_Player != player_color) {
            return true;
        }
        return false;
    }
    //    是否AI回合

    //    AI回合
    private void AI_Model() {
        if (current_Player == player_color) return;
        if (!nonPlace(current_Player)) {
            AI_turn();
            stepcount++;
            CopyBoardList.add(copy(getBoard()));
//            CopyBoard = CopyBoardList.get(stepcount - 1);
            current_Player = 1 - current_Player;
            repaint();
        }
    }
    //    AI回合

    public String getWinner() {
        int black = 0, white = 0;
        int[] count = getCounts();
        if (count[0] == count[1]) return "null";
        return count[0] > count[1] ? "BLACK" : "WHITE";
    }


    //    AI落子
    private void AI_turn() {
        int[][] map = copy(Board);
        int x = -1, y = -1, maxPoint = -2147483648;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isLegal(i, j, current_Player)) {
                    setChess(i, j, current_Player);
                    int point = getPoint(Board, current_Player);
                    if (x == -1 || point > maxPoint) {
                        maxPoint = point;
                        x = i;
                        y = j;
                    }
                    Board = copy(map);
                }
            }
        }
        Board = copy(map);

        String player = "";
        if (current_Player == 0) player = "Black";
        else player = "White";
        String s = player + " " + x + " " + y + " " + cheatingModel;
        steps.add(s);

        setChess(x, y, current_Player);

        playMusic();
    }
    //    AI落子

    //    AI计算权重
    private int getPoint(int[][] board, int chr) {
        int ret = 0;
        int[][] value = getValue();
        int c = 1;
        if (chr == 0) c = -c;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Board[i][j] == c)
                    ret += value[i][j];
            }
        }
        return ret;
    }
    //    AI计算权重

    //    初始化
    private int[][] getValue() {
        int[][] dir = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        int[][] cor = {{0, 0}, {0, 7}, {7, 0}, {7, 7}};
        int[][] value = new int[8][8];
        for (int i = 0; i < 4; i++) {
            int x = cor[i][0], y = cor[i][1];
            value[x][y] = 90;
            for (int j = 0; j < 4; j++) {
                int x1 = x + dir[j][0], y1 = y + dir[j][1];
                if (inBoard(x1, y1))
                    value[x1][y1] = -60;
                x1 = x + dir[j + 4][0];
                y1 = y + dir[j + 4][1];
                if (inBoard(x1, y1))
                    value[x1][y1] = -80;
            }
        }
        for (int i = 2; i < 6; i++) {
            value[0][i] = value[7][i] = value[i][0] = value[i][7] = 10;
            value[1][i] = value[6][i] = value[i][1] = value[i][6] = 5;
            for (int j = 2; j < 6; j++) {
                value[i][j] = 1;
            }
        }
        return value;
    }
    //    初始化


    private class MouseMotionHandler implements MouseMotionListener {
        public void mouseMoved(MouseEvent event) {
            //set the mouse cursor to cross hairs if it is inside
            //a rectangle
//            int chessX = event.getX() - (int) Board_x;
//            int chessY = event.getY() - (int) Board_y;
//            int row = chessY / (int) blankheight, column = chessX / (int) blankwidth;
//            if (Board[row][column] != 0) setCursor(Cursor.getDefaultCursor());
//            else setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }

        public void mouseDragged(MouseEvent event) {
//            int chessX = event.getX();
//            int chessY = event.getY();
//            if (Board[chessX / blankwidth][chessY / blankheight] != 0) {
//
//                //drag the current rectangle to center it at (x,y)
//                current.setFrame(((chessX / blankwidth) + 0.5) * blankwidth, ((chessY / blankheight) + 0.5) * blankheight, CHESS_RADIUS * 2, CHESS_RADIUS * 2);
//                repaint();
//            }
        }

    }

    public Dimension getPreferredSize() {
        return new Dimension((int) ChessboardFrame.DEFAULT_WIDTH, (int) ChessboardFrame.DEFAULT_HEIGHT);
    }

    public void playMusic() {//加入落子音效
        try {
            File f = new File("C:\\Users\\18219\\Music\\落子.wav");//文件路径可以改，但是只能是wav格式的音频
            URI CB = f.toURI();
            URL cb = CB.toURL();
            AudioClip aau;
            aau = Applet.newAudioClip(cb);
            aau.play();
            // aau.stop();  停止播放
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
