package com.nibm.souschef.model;

import android.util.Log;
public class RecipeDLL {
    private StepNode head;
    private StepNode tail;
    private StepNode currentNode;
    private int size;
    public RecipeDLL() {
        this.head = null;
        this.tail = null;
        this.currentNode = null;
        this.size = 0;
    }

    public void insertNode(String instruction) {
        StepNode newNode = new StepNode(instruction);

        if (head == null) {
            head = tail = currentNode = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    public StepNode moveToNext() {
        if (currentNode != null && currentNode.next != null) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    public StepNode moveToPrev() {
        if (currentNode != null && currentNode.prev != null) {
            currentNode = currentNode.prev;
        }
        return currentNode;
    }

    public StepNode getHead() {
        return head;
    }
}
