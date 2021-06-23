package game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PongGame {
    static class MyFrame extends JFrame {
        static class MyPanel extends  JPanel {
            public MyPanel() {
                this.setSize(800, 400);
                this.setBackground(Color.BLACK);
            }

            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                g2d.fillRect(barLeftPos.x, barLeftPos.y, 20, barHeight);
                g2d.fillRect(barRightPos.x, barRightPos.y, 20, barHeight);
                g2d.setFont(new Font("TimesRoman", Font.BOLD, 50));
                g2d.drawString(scoreLeft + " vs " + scoreRight, 400 - 50, 50);
                g2d.fillOval(ballPos.x, ballPos.y, 20, 20);
            }
        }
            static Timer timer = null;
            static int dir = 0;
            static Point ballPos = new Point(400-10, 200 -10);
            static final float BALL_SPEED = 5;
            static float ballSpeedX = BALL_SPEED;
            static float ballSpeedY = BALL_SPEED;
            static int ballWidth = 20;
            static int ballHeight = 20;
            static int barHeight = 80;
            static Point barLeftPos = new Point(50, 200-40);
            static Point barRightPos = new Point(800- 50 -20, 200 - 40);
            static int barLeftYTarget = barLeftPos.y;
            static int barRightYTarget = barRightPos.y;
            static MyPanel myPanel = null;
            static int scoreLeft = 0;
            static int scoreRight = 0;

            public MyFrame(String title){
                super(title);
                this.setVisible(true);
                this.setSize(800, 400);
                this.setLayout(new BorderLayout());
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                myPanel = new MyPanel();
                this.add( "Center", myPanel);

                setKeyListener();
                startTimer();

            }
            public void setKeyListener(){
                this.addKeyListener(new KeyAdapter() {
                  //  @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode() == KeyEvent.VK_UP){
                            System.out.println("Pressed Up Key.");
                            barLeftYTarget -= 20;
                            if(barLeftPos.y < barLeftYTarget){
                                barLeftYTarget = barLeftPos.y - 20;
                            }
                            barRightYTarget -= 20;
                            if(barRightPos.y < barRightYTarget){
                                barRightYTarget = barRightPos.y - 20;
                            }
                        } else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                            System.out.println("Pressed Down Key.");
                            barLeftYTarget += 20;
                            if(barLeftPos.y > barLeftYTarget){
                                barLeftYTarget = barLeftPos.y + 20;
                            }
                            barRightYTarget += 20;
                            if(barRightPos.y > barRightYTarget){
                                barRightYTarget = barRightPos.y + 20;
                            }
                        }

                    }
                });

            }
            public void startTimer(){
                timer = new Timer(20, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(barLeftPos.y > barLeftYTarget)
                            barLeftPos.y -= 5;
                        else if(barLeftPos.y < barLeftYTarget){
                            barLeftPos.y += 5;
                        }
                        if(barRightPos.y > barRightYTarget)
                            barRightPos.y -= 5;
                        else if(barRightPos.y < barRightYTarget)
                            barRightPos.y += 5;
                        if(dir == 0){
                            // 0 -> up right
                            ballPos.x += ballSpeedX;
                            ballPos.y -= ballSpeedY;
                        } else if(dir == 1){
                            // 1 -> down right
                            ballPos.x += ballSpeedX;
                            ballPos.y += ballSpeedY;
                        } else if(dir == 2){
                            // 2-> up left
                            ballPos.x -= ballSpeedX;
                            ballPos.y -= ballSpeedY;
                        } else if(dir == 3){
                            // 3 -> down left
                            ballPos.x -= ballSpeedX;
                            ballPos.y += ballSpeedY;
                        }
                        checkCollision();
                        myPanel.repaint();
                    }
                });
                timer.start();
            }
            public void setSpeed(Point barPos){
                int barCenterY = barPos.y + (barHeight/2);
                int ballCenterY = ballPos.y + 10;

                float offset = Math.abs(barCenterY - ballCenterY);
                float offSetRate = (offset * 1.5f) / (barHeight/2);
                if(offSetRate < 0.1f){
                    offSetRate = 0.1f;
                }
                if(offSetRate > 1.5f){
                    offSetRate = 1.5f;
                }
                ballSpeedY = BALL_SPEED * offSetRate;
                if( ballSpeedY < 1)
                    ballSpeedY = 1;

            }
            public void checkCollision(){
                if(dir == 0){
                    if(ballPos.y < 0) {
                        dir = 1;
                    }
                    if(ballPos.x > 800-ballWidth){
                        dir = 2;
                        scoreLeft += 1;
                    }
                    if(ballPos.x > barRightPos.x - ballWidth &&
                            (ballPos.y >= barRightPos.y && ballPos.y <= barRightPos.y + barHeight)){
                        dir  = 2;
                        setSpeed(barLeftPos);
                    }
                } else if (dir == 1){
                    if(ballPos.y > 400 - ballHeight - 20){
                        dir = 0;
                    }
                    if(ballPos.x > 800 - ballWidth){
                        dir = 3;
                        scoreLeft += 1;
                    }
                    if(ballPos.x > barRightPos.x - ballWidth &&
                            (ballPos.y >= barRightPos.y && ballPos.y <= barRightPos.y + barHeight)){
                        dir = 3;
                        setSpeed(barLeftPos);
                    }
                } else if(dir ==2){
                    if(ballPos.y < 0){
                        dir = 3;
                    }
                    if(ballPos.x < 0){
                        dir = 0;
                        scoreRight += 1;
                    }
                    if(ballPos.x < barLeftPos.x + ballWidth &&
                            (ballPos.y >= barLeftPos.y && ballPos.y <= barLeftPos.y + barHeight)){
                        dir = 0;
                        setSpeed(barLeftPos);
                    }
                } else if(dir == 3){
                    if(ballPos.y > 400-ballHeight-20){
                        dir = 2;
                    }
                    if(ballPos.x < 0){
                        dir = 1;
                        scoreRight += 1;
                    }
                    if(ballPos.x < barLeftPos.x + ballWidth &&
                            (ballPos.y >= barLeftPos.y && ballPos.y <= barLeftPos.y + barHeight)){
                        dir = 1;
                        setSpeed( barLeftPos);
                    }
                }
            }
    }

    public static void main(String[] args) {
        new MyFrame("Pong Game");

    }
}
