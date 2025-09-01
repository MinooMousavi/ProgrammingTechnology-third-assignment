/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 *
 * @author macbook
 */

package tron;


import java.util.ArrayList;
import java.util.List;
public class Database {
    public List<Player> players;
    private String colorMotor1;
    private String colorMotor2;
    private String player1Name;
    private String player2Name;
    public Database() {
        this.players = new ArrayList<>();
    }
    public void setColors(){
        colorMotor1 = players.get(0).getColor();
        colorMotor2 = players.get(1).getColor();

        System.out.println("Color Motor 1: " + colorMotor1);
        System.out.println("Color Motor 2: " + colorMotor2);
    }
    public void setNames(){
        player1Name = players.get(0).getName();
        player2Name = players.get(1).getName();

        System.out.println("Player 1 Name: " + player1Name);
        System.out.println("Player 2 Name: " + player2Name);
    }
    public void reset() {
        players.clear();
        colorMotor1 = null;
        colorMotor2 = null;
        player1Name = null;
        player2Name = null;
    }
    public boolean checkNames(){
        if(player1Name == null){
            player1Name = "Player 1";
            return false;
        }
        else if(player2Name == null){
            player2Name = "Player 2";
            return false;
        }
        else return player2Name.equals(player1Name);
    }
    public String getColorMotor1(){
        return colorMotor1;
    }
    public String getColorMotor2(){
        return colorMotor2;
    }
    public String getPlayer1Name(){
        return player1Name;
    }
    public String getPlayer2Name(){
        return player2Name;
    }
    
    public List<Player> getplayersList()
    {
        return players;
    }
    
    public void addPlayer(Player p)
    {
        players.add(p);
    }
}

