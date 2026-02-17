package com.nibm.souschef.model;

public class StepNode {
    public String instruction;
    public int timerSeconds;
    public StepNode next;
    public StepNode prev;

    public StepNode(String instruction) {
        this.instruction = instruction;
        this.next = null;
        this.prev = null;
        this.timerSeconds = 0;
    }
}
