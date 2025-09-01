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
import java.awt.*;
import java.util.Objects;

public class GUI {
    private final JFrame frame = new JFrame("Tron Game!");
    private final JPanel panel = new JPanel();
    private final JTextField player1 = new JTextField("Player 1");
    private final JTextField player2 = new JTextField("Player 2");
    private JComboBox<String> player1color = new JComboBox<>();
    private JComboBox<String> player2color = new JComboBox<>();
    private final Database database = new Database();
    private String player1Name;
    private String player2Name;
    private String player1Color;
    private String player2Color;
    private int width = 600;
    private int height = width;
    private  BoardGame board;
    private final SQL sql = new SQL();

    public void displayFirstScreen() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250, 250);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel playerName = new JLabel("Enter your names:");

        JLabel colorLabel1 = new JLabel("Player 1 Color:");
        JLabel colorLabel2 = new JLabel("Player 2 Color:");
        String[] colors = {"green", "yellow", "white", "blue"};
        player1color = new JComboBox<>(colors);
        player2color = new JComboBox<>(colors);

        JButton start = new JButton("Start");
        start.addActionListener(e -> {
            database.reset();
            getInfo();
            if(database.checkNames()){
                JDialog error = new JDialog();
                JLabel errorMessage = new JLabel("Please enter different names");
                error.add(errorMessage);
                error.setSize(200, 100);
                error.setVisible(true);
                error.setLocationRelativeTo(null);
            } else {
                frame.setVisible(false);
                board = new BoardGame(width, height, database);
                board();
            }
        });

        JButton showScore = new JButton("Show Score");
        showScore.addActionListener(e -> {
            sql.showDatabaseTable();
        });

        player1.setPreferredSize(new Dimension(100, 20));
        player2.setPreferredSize(new Dimension(100, 20));
        player1color.setPreferredSize(new Dimension(50, 20));
        player2color.setPreferredSize(new Dimension(50, 20));

        panel.add(playerName);
        panel.add(player1);
        panel.add(player2);
        panel.add(colorLabel1);
        panel.add(player1color);
        panel.add(colorLabel2);
        panel.add(player2color);
        panel.add(start);
        panel.add(showScore);

        frame.add(panel);
    }
    public void getInfo(){
        player1Name = player1.getText();
        player2Name = player2.getText();
        player1Color = Objects.requireNonNull(player1color.getSelectedItem()).toString();
        player2Color = Objects.requireNonNull(player2color.getSelectedItem()).toString();
        addPlayer();
        database.setColors();
        database.setNames();
    }

    private void addPlayer() {
        Player player = new Player();
        player.setName(player1Name);
        player.setColor(player1Color);
        player.setScore(0);
        database.players.add(player);
        System.out.println("Adding Player 1: " + player);

        player = new Player();
        player.setName(player2Name);
        player.setColor(player2Color);
        player.setScore(0);
        database.players.add(player);
        System.out.println("Adding Player 2: " + player);

    }
    public void board() {
        JFrame frame = new JFrame("Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(board);
        frame.pack();
        board.requestFocus();
    }
}

