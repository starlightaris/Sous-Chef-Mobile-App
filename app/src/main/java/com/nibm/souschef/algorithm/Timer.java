package com.nibm.souschef.algorithm;

import com.nibm.souschef.model.StepNode;

public class Timer {
    public void startTimer(StepNode stepNode){
        int duration = stepNode.getDuration();

        for(int i = 0; i <= duration; i++) {
            setTime(i);
        }
    }

    public int setTime(int second){
        return second;
    }
}
