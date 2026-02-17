package com.nibm.souschef.model;

public class StepNode {
    public String instruction;
    public StepNode next;
    public StepNode prev;

    public StepNode(String instruction) {
        this.instruction = instruction;
        this.next = null;
        this.prev = null;
    }
}
