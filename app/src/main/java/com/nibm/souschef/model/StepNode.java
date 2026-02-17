package com.nibm.souschef.model;

public class StepNode {
    public String instruction;
    public StepNode next;
    public StepNode prev;
    private int duration; //in seconds

    public StepNode(String instruction, int time) {
        this.instruction = instruction;
        this.next = null;
        this.prev = null;
        this.duration = time;
    }

    public int getDuration(){
        return duration;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }
}
