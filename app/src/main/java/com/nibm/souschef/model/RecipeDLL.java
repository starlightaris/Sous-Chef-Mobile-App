package com.nibm.souschef.model;

public class RecipeDLL {
    private StepNode head;
    private StepNode tail;

    public void insertNode(String instruction, int time) {

        StepNode newNode = new StepNode(instruction, time);

        if (head == null) {
            head = tail = newNode;
        }
        else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    public StepNode getHead() {
        return head;
    }
}
