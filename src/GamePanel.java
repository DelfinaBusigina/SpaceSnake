import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer = new Timer(DELAY, this);
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        
        ImageIcon icon = new ImageIcon("space_bg.png");
        JLabel bg = new JLabel(icon);
        bg.setBounds(0,0,600,600);
        this.add(bg);

        this.setBackground(Color.BLACK); // change to a picture of space?
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        showName();
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // draw grid lines
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            // draw an apple
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // draw snake body
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    //multicolor snake
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Ink Free", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten,(SCREEN_WIDTH-metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }else{
            gameOver(g);
        }

    }

    public void newApple() {
        appleX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // checks if head collidees with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;

        }
        // check if head touches top border
        if (y[0] < 0) {
            running = false;

        }
        // check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;

        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        //Game OVer text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH-metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten,(SCREEN_WIDTH-metrics2.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        
        JButton showHighscore = new JButton("Show Highscore");
        showHighscore.setBackground(Color.BLACK);
        showHighscore.setForeground(Color.RED);
        showHighscore.setFont(new Font("Ink Free", Font.BOLD, 18));
        showHighscore.setBounds(150, 350, 200, 50);
        this.add(showHighscore);
        showHighscore.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JDialog highscoreDialog = new JDialog();

                JPanel panel = new JPanel();
                panel.setBounds(0,0,600,600);
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBackground(Color.BLACK);
                highscoreDialog.add(panel);

                // ArrayList<String> names = new ArrayList<String>();
                // try {
                //     FileReader reader = new FileReader("src/Name.txt");
                //     BufferedReader bufferedReader = new BufferedReader(reader);
                //     String fileRow;
                //     while((fileRow=bufferedReader.readLine())!=null){
                //         names.add(fileRow);
                //         JLabel name = new JLabel();
                //         name.setSize(200, 30);
                //         name.setText(names<0>);
                //     }

                // } catch (Exception e2) {
                //     //TODO: handle exception
                // }

                JLabel highscoreLabel = new JLabel("Highscore");

                highscoreLabel.setBounds(275, 20, 200, 40);
                highscoreLabel.setFont(new Font("Ink Free", Font.BOLD, 30));
                highscoreLabel.setForeground(Color.RED);
                panel.add(highscoreLabel);

                try {
                        FileReader reader = new FileReader("src/Name.txt");
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        String fileRow;
                        while((fileRow=bufferedReader.readLine())!=null){
                            
                            JLabel name = new JLabel(fileRow);
                            name.setBounds(0,20,200,30);
                            // name.setSize(200, 30);
                            name.setForeground(Color.WHITE);
                            name.setFont(new Font("Ink Free", Font.BOLD, 20));
                            panel.add(name);
                        }
    
                    } catch (Exception e2) {
                        //TODO: handle exception
                    }
                

                highscoreDialog.setLayout(null);
                highscoreDialog.setSize(600, 600);
                highscoreDialog.setVisible(true);
            }
        }); 

    

        // restart.addActionListener(new ActionListener(){
        //     @Override
        //     public void actionPerformed(ActionEvent e){

        //     }
        // });

    }

    // public void restartGame(Button restart){
       
    //     restart.addActionListener(new ActionListener(){
    //         @Override
    //         public void actionPerformed(ActionEvent e){
    //             GamePanel restartbtn = new GamePanel();
    //             this.remove(this);
    //             bodyParts = 6;
    //             applesEaten = 0;
    //             running = false;
    //             timer = new Timer(DELAY, this);
    //             startGame();
    //         }
    //     }); 
    // }

    // public void showHighscore(){
       
    //     restart.addActionListener(new ActionListener(){
    //         @Override
    //         public void actionPerformed(ActionEvent e){
    //             GamePanel restartbtn = new GamePanel();
    //             this.remove(this);
    //             bodyParts = 6;
    //             applesEaten = 0;
    //             running = false;
    //             timer = new Timer(DELAY, this);
    //             startGame();
    //         }
    //     }); 
    // }

    public void showName(){
        boolean visib = true;
        JDialog name = new JDialog();

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 600, 600);
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);
        name.add(panel);

        JLabel input = new JLabel("Input name");
        input.setForeground(Color.RED);
        input.setFont(new Font("Ink Free", Font.BOLD, 35));
        input.setBounds(250, 20, 200, 40);
        panel.add(input);

        

        JTextField inname = new JTextField();
        inname.setBounds(250, 160, 200, 40);
        panel.add(inname);

        

        JButton send = new JButton("Confirm");
        send.setBounds(250, 80, 200, 40);
        panel.add(send);

        send.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                        String playerName = inname.getText();
                        FileWriter file = new FileWriter("src/Name.txt");
                        BufferedWriter writer = new BufferedWriter(file);
                        writer.write(playerName+" "+applesEaten+"\n");
                        writer.close();
                    
                    
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        name.setSize(600,600);
        name.setLayout(null);
        name.setVisible(visib);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

}
