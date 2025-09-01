/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author macbook
 */

package tron;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.Random;

public class BoardGame extends JPanel implements ActionListener, KeyListener {
    private final int width;
    private final int height;
    private final int tileSize = 15;
    
    /* 
    Motors (heads and bodies)
    */
    private Motor headMotor1;
    private Motor headMotor2;
    private ArrayList<Motor> bodyMotor1;
    private ArrayList<Motor> bodyMotor2;
    /*
    Game Timer
    */
    private Timer gameLoop;
    /*
    End-of-match lining
    */
    private JDialog dialog;
    /*
    players data and their color 
    */
    private final String p1Color;
    private final String p2Color;
    private final String p1Name;
    private final String p2Name;
    /*
    here is the Color map that we use it in order to convert color strings to actual Color objects
    */
    private final Map<String, Color> colorMap = new HashMap<>();
    
    private boolean gameOver = false;
    private boolean gameRunning = false;
    private boolean pl1Won;
    private int player1Score = 0;
    private int player2Score = 0;
    private final SQL sql = new SQL();
    private String winner ;
    Random random = new Random();
    private int headOneX;
    private int headOneY;
    private int headTwoX;
    private int headTwoY;

    public BoardGame(int width, int height, Database database ){
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        p1Color = database.getColorMotor1();
        p2Color = database.getColorMotor2();

        p1Name = database.getPlayer1Name();
        p2Name = database.getPlayer2Name();
        
        headOneX = random.nextInt(40);
        headOneY = random.nextInt(40);
        headTwoX = random.nextInt(40);
        headTwoY = random.nextInt(40);

        System.out.println("color1 = " + p1Color);
        System.out.println("color2 = " + p2Color);

        colorMap.put("green", Color.GREEN);
        colorMap.put("yellow", Color.YELLOW);
        colorMap.put("white", Color.WHITE);
        colorMap.put("blue", Color.BLUE);

        start();
    }

    private boolean restartPending = false;
    private void start(){
        if (!gameRunning) {
            headMotor1 = new Motor(headOneX, headOneY);
            headMotor2 = new Motor(headTwoX, headTwoY);
            //now we initialize the motor bodies 
            bodyMotor1 = new ArrayList<>();
            bodyMotor2 = new ArrayList<>();
            
            pl1Won = false;
            /*
            here we set a timer with 100ms delay
            */
            gameLoop = new Timer(100, this);
            gameLoop.start();

            gameRunning = true; // Seting the flag to indicate that the game is running
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
         g.setFont(new Font("Arial", Font.PLAIN, 20));
         //THE GRID
        for(int i = 0; i < width/tileSize; i++){
            g.drawLine(i*tileSize, 0, i*tileSize, height);
            g.drawLine(0, i*tileSize, width, i*tileSize);
        }

        g.setColor(Color.WHITE);
        g.drawString("Time: " +  (bodyMotor1.size()/10) + " sec", tileSize - 16, tileSize);
         //PLAYER 1
        g.setColor(colorMap.get(p1Color));
        g.fillRect(headMotor1.x * tileSize, headMotor1.y * tileSize, tileSize, tileSize);
        for (Motor segment : bodyMotor1) {
            g.fillRect(segment.x * tileSize, segment.y * tileSize, tileSize, tileSize);
        }
        //PLAYER 2
        g.setColor(colorMap.get(p2Color));
        g.fillRect(headMotor2.x * tileSize, headMotor2.y * tileSize, tileSize, tileSize);
        for (Motor segment : bodyMotor2) {
            g.fillRect(segment.x * tileSize, segment.y * tileSize, tileSize, tileSize);
        }
    }

    private boolean collision (Motor m1, Motor m2){
         return m1.x == m2.x && m1.y == m2.y;
    }
    private boolean outOfBounds(Motor m){
         return m.x < 0 || m.x > width/tileSize -1 ||
                 m.y < 0 || m.y > height/tileSize -1;
    }
    private Motor initialHeadMotor1(){
        return new Motor(headOneX,headOneY);
    }
    private Motor initialHeadMotor2(){
        return new Motor(headTwoX,headTwoY);
    }
    private boolean bodyCollisionMotor1(){
        if (bodyMotor1.isEmpty()){
            return gameOver;
        }else {
            for (int i = 0; i < bodyMotor1.size(); i++) {
                if  (headMotor1.x != initialHeadMotor1().x || headMotor1.y != initialHeadMotor1().y) {
                    if (collision(headMotor1, bodyMotor1.get(i))) {
                        gameOver = true;
                    }
                    else if (collision(headMotor1, bodyMotor2.get(i))) {
                        gameOver = true;
                    }
                }
            }
            return gameOver;
        }
    }
    private boolean bodyCollisionMotor2(){
        if (bodyMotor2.isEmpty()){
            return gameOver;
        }else {
            for (int i = 0; i < bodyMotor2.size() ; i++) {
                if (headMotor2.x != initialHeadMotor2().x || headMotor2.y != initialHeadMotor2().y){
                    if (collision(headMotor2, bodyMotor2.get(i))) {
                        gameOver = true;
                        pl1Won = true;
                    } else if (collision(headMotor2, bodyMotor1.get(i))) {
                        gameOver = true;
                        pl1Won = true;
                    }
                }
            }
            return gameOver;
        }
    }

    private String winner() {
        if (pl1Won) {
            player1Score++;
            sql.updatePlayer(p1Name, true);
            sql.updatePlayer(p2Name, false);
            winner = p1Name + " won the game!";
        } else {
            player2Score++;
            sql.updatePlayer(p2Name, true);
            winner = p2Name + " won the game!";
        }
        return winner;
    }

    public void move(){
         if (collision(headMotor1, headMotor2) || outOfBounds(headMotor1) || outOfBounds(headMotor2)) {
             restart();
         }else{
             Motor newSegmentMotor1;
             Motor newSegmentMotor2;

                 newSegmentMotor1 = new Motor(headMotor1.x, headMotor1.y);
                 bodyMotor1.add(newSegmentMotor1);

                 newSegmentMotor2 = new Motor(headMotor2.x, headMotor2.y);
                 bodyMotor2.add(newSegmentMotor2);

             for(int i = bodyMotor1.size() - 1; i >= 0; i--){
                 if(i == 0){
                     bodyMotor1.get(i).x = headMotor1.x;
                     bodyMotor1.get(i).y = headMotor1.y;
                     bodyMotor2.get(i).x = headMotor2.x;
                     bodyMotor2.get(i).y = headMotor2.y;
                 }else{
                     bodyMotor1.get(i).x = bodyMotor1.get(i - 1).x;
                     bodyMotor1.get(i).y = bodyMotor1.get(i - 1).y;
                     bodyMotor2.get(i).x = bodyMotor2.get(i - 1).x;
                     bodyMotor2.get(i).y = bodyMotor2.get(i - 1).y;
                 }
             }
             /*
             moving the heads forward
             */
             headMotor1.x += headMotor1.velocityX;
             headMotor1.y += headMotor1.velocityY;

             headMotor2.x += headMotor2.velocityX;
             headMotor2.y += headMotor2.velocityY;

             if (!bodyMotor1.isEmpty())
             {
                bodyCollisionMotor1();
             }
             if (!bodyMotor2.isEmpty()) 
             {
                bodyCollisionMotor2();
             }
         }
    }

    private void restart(){
        if (!restartPending) {
            restartPending = true; // Set the restart flag to not call it multiple times 
            gameLoop.stop();
            gameRunning = false;
            
            /*
            generating random points
            */
            headOneX = random.nextInt(40);
            headOneY = random.nextInt(40);
            headTwoX = random.nextInt(40);
            headTwoY = random.nextInt(40);
            if (outOfBounds(headMotor1) || outOfBounds(headMotor2)) {
                // decide the winner if out of bounds
                pl1Won = !outOfBounds(headMotor1);
                winner = winner();
            } else {
                // or we Determine the winner based on collisions
                winner = winner();
            }
            player1Score = 0;
            player2Score = 0;
            gameOver = false;

            bodyMotor1.clear();
            bodyMotor2.clear();

            dialog = new JDialog();
            dialog.setSize(300, 250);
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(new GridLayout(5, 1));

            JLabel label = new JLabel("Game Over! " + winner);

            JButton restartButton = new JButton("Restart");
            JButton exitButton = new JButton("Exit");
            JButton newGameButton = new JButton("Start a new game");
            GUI gui = new GUI();
            
            //new session 
            newGameButton.setSize(50, 50);
            newGameButton.addActionListener(e -> {
                dialog.setVisible(false);
                gui.displayFirstScreen();
            });

            exitButton.setSize(50, 50);
            exitButton.addActionListener(e -> System.exit(0));

            restartButton.setSize(50, 50);
            restartButton.addActionListener(e -> {
                dialog.setVisible(false);
                restartCompleted();
                start();
            });

            JButton showScore = new JButton("Show Score");
            showScore.addActionListener(e -> {
                sql.showDatabaseTable();
            });

            dialog.add(label);
            dialog.add(restartButton);
            dialog.add(newGameButton);
            dialog.add(exitButton);
            dialog.add(showScore);
        }
    }
    private void restartCompleted() {
        restartPending = false; // Reset
    }
    private final Set<Integer> pressedKeys = new HashSet<>();
    //player1 uses WASD keys , player2 uses arrow keys
    private void updateMotorVelocities() {
        if (pressedKeys.contains(KeyEvent.VK_W) && headMotor1.velocityY != 1) {
            headMotor1.velocityX = 0;
            headMotor1.velocityY = -1;
        }
        if (pressedKeys.contains(KeyEvent.VK_S) && headMotor1.velocityY != -1) {
            headMotor1.velocityX = 0;
            headMotor1.velocityY = 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_A) && headMotor1.velocityX != 1) {
            headMotor1.velocityX = -1;
            headMotor1.velocityY = 0;
        }
        if (pressedKeys.contains(KeyEvent.VK_D) && headMotor1.velocityX != -1) {
            headMotor1.velocityX = 1;
            headMotor1.velocityY = 0;
        }

        if (pressedKeys.contains(KeyEvent.VK_UP) && headMotor2.velocityY != 1) {
            headMotor2.velocityX = 0;
            headMotor2.velocityY = -1;
        }
        if (pressedKeys.contains(KeyEvent.VK_DOWN) && headMotor2.velocityY != -1) {
            headMotor2.velocityX = 0;
            headMotor2.velocityY = 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_LEFT) && headMotor2.velocityX != 1) {
            headMotor2.velocityX = -1;
            headMotor2.velocityY = 0;
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT) && headMotor2.velocityX != -1) {
            headMotor2.velocityX = 1;
            headMotor2.velocityY = 0;
        }
    }
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        move();
        repaint();
        if (gameOver) restart();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        updateMotorVelocities();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        updateMotorVelocities();
    }
}

