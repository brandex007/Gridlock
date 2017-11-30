package com.innovativeexecutables;

import com.badlogic.gdx.utils.Timer;

public class Score {
    private int scoreCounter = 2;
    
    // 3.1.2.2. Score must start at 200
    private int score = 200;
    private Timer timer;
    
    public Score(){
        // 3.1.2.1. Score must subtract by 2 every 30 seconds
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if(Gridlock.playFlag == true)
                    score -= scoreCounter;
            }
        }, 0, 30);

    }
    
    public void setScore(int score){
        this.score = score;
    }
    
    public int getScore(){
        return score;
    }

    public void removeTimer(){
        timer.stop();
    }
    
}

