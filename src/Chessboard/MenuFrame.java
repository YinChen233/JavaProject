package Chessboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URL;

public class MenuFrame extends JFrame implements ActionListener {
    private JPanel buttonpanel;
    public static int DEFAULT_WIDTH = 600;
    public static int DEFAULT_HEIGHT = 600;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    public static Color DarkGreen = new Color(50,150,110);
    AudioClip aau;

    public MenuFrame() {
        buttonpanel = new JPanel();

        button1 = new JButton("Enter Game");
        button1.setFont(new Font("Calibri", Font.BOLD, 30));
        button1.addActionListener(this);
        button1.setBackground(DarkGreen);

        button2 = new JButton("Quit");
        button2.setFont(new Font("Calibri", Font.BOLD, 30));
        button2.addActionListener(this);
        button2.setBackground(DarkGreen);

        button3 = new JButton("Music");
        button3.setFont(new Font("Calibri",Font.BOLD,30));
        button3.addActionListener(this);
        button3.setBackground(DarkGreen);

        button4 = new JButton("Stop Music");
        button4.setFont(new Font("Calibri",Font.BOLD,30));
        button4.addActionListener(this);
        button4.setBackground(DarkGreen);

        buttonpanel.setLayout(null);//设置绝对位置
        button1.setBounds(100,200,200,100);
        button2.setBounds(300,200,200,100);
        button3.setBounds(100,300,200,100);
        button4.setBounds(300,300,200,100);
        //按键的位置还要调整

        buttonpanel.add(button1);
        buttonpanel.add(button2);
        buttonpanel.add(button3);
        buttonpanel.add(button4);

        //以下7步是导入背景图片的
        ImageIcon imageIcon = new ImageIcon("background.jpg");
        JLabel backgroundlabel = new JLabel(imageIcon);
        getLayeredPane().add(backgroundlabel,new Integer(Integer.MIN_VALUE));
        backgroundlabel.setBounds(0,0,DEFAULT_WIDTH,DEFAULT_HEIGHT);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        ((JPanel)cp).setOpaque(false);

        //背景图片的container，也就是这个backgroundlabel必须加在其他东西前面，不然会覆盖其他的按钮等等
        add(backgroundlabel);
        add(buttonpanel);

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            this.dispose();
            new ChessboardFrame();
        }
        if (e.getSource() == button2){
            this.dispose();
        }
        if (e.getSource() == button3){
            playMusic();
        }
        if (e.getSource() == button4){
            aau.stop();
        }
    }

    public void playMusic(){//加入bgm
        try {
            File f = new File("C:\\Users\\18219\\Music\\梶浦由记 (かじうら ゆき) - light your sword [mqms2]_124834_track0_124834.wav");//文件路径可以改，但是只能是wav格式的音频
            URI CB = f.toURI();
            URL cb = CB.toURL();
            aau = Applet.newAudioClip(cb);
            aau.play();
            aau.loop();//循环播放
            // aau.stop();  停止播放
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
