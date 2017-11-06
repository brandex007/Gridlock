package com.innovativeexecutables;

public class Score {
    private float scoreCounter = 0;
    private int score = 200;

    public Score(){
        // update score every 1 second
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task(){
                                                  @Override
                                                  public void run() {
                                                      scoreCounter -= 1.0;

                                                      score += (int) scoreCounter;
                                                  }
                                              }
                , 0        //    (delay)
                , 1     //    (seconds)
        );
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }

}
