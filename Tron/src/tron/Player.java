/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author macbook
 */

package tron;



public class Player {
    private String name;
    private int score;
    private String color;
    public Player() {
        this.name = null;
        this.score = 0;
        this.color = null;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getColor() {
        return this.color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", score=" + score +
                '}';
    }
}
