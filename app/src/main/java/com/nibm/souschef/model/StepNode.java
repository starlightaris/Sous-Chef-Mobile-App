package com.nibm.souschef.model;

public class StepNode {
    public String instruction;
    public StepNode next;
    public StepNode prev;
    private int duration;

    public StepNode(String instruction, int duration) {
        this.instruction = instruction;
        this.next = null;
        this.prev = null;
        this.duration = duration;
    }

    public int getDuration(){
        return duration;
    }
}
