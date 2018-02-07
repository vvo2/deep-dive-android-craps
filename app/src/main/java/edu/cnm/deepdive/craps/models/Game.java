package edu.cnm.deepdive.craps.models;

public class Game {

  private Craps craps;
  private long wins;
  private long losses;

  public Game(){
    craps = new Craps();
    reset();
  }

  public Craps.State play(){
    Craps.State result = craps.play();
    if (result == Craps.State.WIN) {
      wins++;
    } else {
      losses++;
    }
    return result;
  }

  public void reset(){
    wins = 0;
    losses = 0;
    craps.reset();
  }

  public Craps getCraps() {
    return craps;
  }

  public long getWins() {
    return wins;
  }

  public long getLosses() {
    return losses;
  }
}
