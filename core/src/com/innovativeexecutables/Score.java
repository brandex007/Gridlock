package com.innovativeexecutables;

import com.badlogic.gdx.utils.Timer;

public class Score {
    private int scoreCounter = 2;
    
    // 3.1.2.2. Score must start at 200
    private int score = 200;
    //private Timer timer;

    
    public void updateScore(int time){

       /* timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                System.out.println(Gridlock.time);

               // System.out.println(score+","+Gridlock.time);
                if(Gridlock.playFlag == true&&Gridlock.time%30==0)
                {
                    score -= scoreCounter;

                }


            }
        }, 0, 30);*/

        // 3.1.2.1. Score must subtract by 2 every 30 seconds
        if(Gridlock.playFlag == true&& time%30==0)
        {
            score -= scoreCounter;

        }

    }
    
    public void setScore(int score){
        this.score = score;
    }
    
    public int getScore(){
        return score;
    }

    //public void removeTimer(){timer.stop(); }
    
}

