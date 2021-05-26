import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {

    final int BALLD = 20, PADDLEW = 20, PADDLEH = 80, BORDER = 10;

    int ballX = 0, ballY = 0, paddlePX = 0, paddlePY = 0, paddleCX = 0, paddleCY = 0;
    int ballDX = 3, ballDY = 4, paddleCDY = 3, paddlePDY = 10;
    int playerScore = 0, computerScore = 0;

    Timer timer;

    boolean MENU = true, PLAY = false, GAMEOVER = false;

    public Board() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        timer = new Timer(1000 / 60, this);
    }

    //INITIALIZATION
    public void init() {
        ballX = getWidth() / 2 - BALLD / 2;
        ballY = getHeight() / 2 - BALLD / 2;

        paddlePX = 0 + BORDER;
        paddlePY = getHeight() / 2 - PADDLEH / 2;

        paddleCX = getWidth() - BORDER - PADDLEW;
        paddleCY = getHeight() / 2 - PADDLEH / 2;
        timer.start();

        if(playerScore >= 2 || computerScore >= 2){
            timer.stop();
            GAMEOVER = true;
            PLAY = false;
        }
    }

    public void checkCollision() {
        Rectangle paddle1 = new Rectangle(paddlePX, paddlePY, PADDLEW, PADDLEH);
        Rectangle paddle2 = new Rectangle(paddleCX, paddleCY, PADDLEW, PADDLEH);
        Rectangle ball = new Rectangle(ballX, ballY, BALLD, BALLD);
        if (ball.intersects(paddle1) || ball.intersects(paddle2)) {
            ballDX *= -1;
        }
    }

    public void move() {
        //BALL MOVEMENT
        if (ballX + BALLD >= getWidth()) {
            playerScore += 1;
            init();
            ballDX *= -1;
            timer.stop();
        }
        if (ballX <= 0) {
            computerScore += 1;
            init();
            ballDX *= -1;
            timer.stop();
        }

        if (ballY + BALLD >= getHeight() || ballY <= 0) {

            ballDY *= -1;
        }

        ballX += ballDX;
        ballY += ballDY;

    if(ballX +BALLD/2 > getWidth()/2){
        if (ballY > paddleCY + PADDLEH / 2) {
            paddleCY += paddleCDY;
        }
        if (ballY <= paddleCY + PADDLEH / 2) {
            paddleCY -= paddleCDY;
        }
    }

}



    public void playerUP(){
        if(paddlePY > 0){
            paddlePY -= paddlePDY;
        }
    }

    public void playerDOWN(){
        if(paddlePY+PADDLEH < getHeight()){
            paddlePY += paddlePDY;
        }
    }

    public void startPause(){
        if(timer.isRunning()){
            timer.stop();
        }else{
            timer.start();
        }
    }

    public void reset(){
        playerScore = 0;
        computerScore = 0;
        init();
        MENU = false;
        GAMEOVER = false;
        PLAY = true;
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        checkCollision();
        move();
        repaint();
    }



    public void paintComponent(Graphics g){

        super.paintComponent(g);

        g.setColor(Color.white);

        if(MENU){
            g.setFont(new Font("Serif", Font.BOLD, 52));
            printSimpleString("PONG", getWidth(), 0, getHeight()/3, g);
            printSimpleString("PRESS P TO PLAY", getWidth(), 0, getHeight()/3 *2, g);

        }else if(PLAY) {
            g.fillOval(ballX, ballY, BALLD, BALLD);
            g.fillRect(paddlePX, paddlePY, PADDLEW, PADDLEH);
            g.fillRect(paddleCX, paddleCY, PADDLEW, PADDLEH);

            g.setFont(new Font("Serif", Font.BOLD, 52));
            printSimpleString(Integer.toString(playerScore), getWidth()/2, 0, BORDER*5, g);
            printSimpleString(Integer.toString(computerScore), getWidth()/2, getWidth()/2, BORDER*5, g);

            g.fillRect(getWidth()/2-BORDER/2, 0, BORDER, getHeight());
        } else if (GAMEOVER) {
            g.setFont(new Font("Serif", Font.BOLD, 52));
            printSimpleString("GAME OVER", getWidth(), 0, getHeight()/3, g);
            printSimpleString("PRESS P TO PLAY AGAIN", getWidth(), 0, getHeight()/3 *2, g);
        }
    }

    private void printSimpleString(String s, int width, int XPos, int YPos, Graphics g2d){
        int stringLen = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        int start = width/2 - stringLen/2;
        g2d.drawString(s, start + XPos, YPos);
    }
}