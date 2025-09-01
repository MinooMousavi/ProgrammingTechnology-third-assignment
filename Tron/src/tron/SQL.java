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
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class SQL {
    private static final String URL = "jdbc:mysql://localhost:3306/tron";
    private static final String USER = "root";          
    private static final String PASSWORD = "root"; 

    private Connection connect() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
   }

    public void updatePlayer(String name, boolean playerWon) {
        System.out.println("Updating player: " + name + ", playerWon: " + playerWon);

        if (playerExists(name)) {
            System.out.println("Player exists, updating score...");
            updateScore(name, playerWon);
        } else {
            System.out.println("Player does not exist, inserting...");
            insertPlayer(name, playerWon ? 1 : 0);
        }
    }
    private boolean playerExists(String name) {
        String sql = "SELECT COUNT(*) FROM tron WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void insertPlayer(String name, int initialScore) {
        String sql = "INSERT INTO scoreboard (name, score) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, initialScore);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateScore(String name, boolean playerWon) {
        String sql;
        if (playerWon) {
            sql = "UPDATE scoreboard SET score = score + 1  WHERE name = ?";
        } else {
            sql = "UPDATE scoreboard SET score = score WHERE name = ?";
        }

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void showDatabaseTable() {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM scoreboard ORDER BY score DESC LIMIT 10");
             ResultSet resultSet = pstmt.executeQuery()) {

            JDialog dialog = new JDialog();
            dialog.setTitle("SCOREBOARD");
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(null);

            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);

            dialog.add(scrollPane);
            dialog.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to convert ResultSet to DefaultTableModel
    private DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        java.sql.ResultSetMetaData metaData = resultSet.getMetaData();

        // Column names
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> row = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.add(resultSet.getObject(columnIndex));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }
}


