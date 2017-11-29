package com.innovativeexecutables;

public class Score {
    private int scoreCounter = 2;
    
    // 3.1.2.2. Score must start at 200
    private int score = 200;
    
    public Score(){
        // 3.1.2.1. Score must subtract by 2 every 30 seconds
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task(){
            @Override
            public void run() {
                if(Gridlock.playFlag == true)
                    score -= scoreCounter;
            }
        }
                                              , 0        //    (delay)
                                              , 30     //    (seconds)
                                              );
    }
    
    public void setScore(int score){
        this.score = score;
    }
    
    public int getScore(){
        return score;
    }
    
}

