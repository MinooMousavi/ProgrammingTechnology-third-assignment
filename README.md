# Tron Light Motorcycle Battle Game

A top-view **Java game** inspired by the light-motorcycle battles from the *Tron* movie. Developed using **NetBeans**.

## Game Overview

Two players control light motorcycles that leave a trail of light behind them. The objective is to outmaneuver the opponent without crashing into walls or the other player’s trail.

- **Player 1 Controls:** `W`, `A`, `S`, `D`
- **Player 2 Controls:** Arrow keys

### Game Rules

- Each motorcycle moves continuously in the last direction chosen by the player.
- A player loses if:
  - The motorcycle hits the boundary of the game level.
  - The motorcycle collides with the opponent's light trail.
- Before the game starts, players:
  - Enter their names.
  - Choose the color of their light trails.

### Features

- **Highscore Tracking:**  
  - Winner's score is increased by one in the database.
  - If a player does not exist in the database, a new record is created.
- **Menu Options:**  
  - View top 10 highscore table.
  - Restart the game at any time.

## Installation

1. Clone the repository:

```bash
git clone git@github.com:MinooMousavi/ProgrammingTechnology-third-assignment.git
