package com.nibm.souschef.model;

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

    public void insertNode(String instruction, int duration) {

        StepNode newNode = new StepNode(instruction, duration);

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

    public void concatenateWithNext() {
        if (currentNode == null || currentNode.next == null) return;

        StepNode nodeToMerge = currentNode.next;

        currentNode.instruction += " " + nodeToMerge.instruction;
        currentNode.next = nodeToMerge.next;

        if (nodeToMerge.next != null) {
            nodeToMerge.next.prev = currentNode;
        } else {
            tail = currentNode;
        }
        size--;
    }

    public void concatenateWithPrev() {
        if (currentNode == null || currentNode.prev == null) return;

        StepNode nodeToMergeInto = currentNode.prev;

        nodeToMergeInto.instruction += " " + currentNode.instruction;
        nodeToMergeInto.next = currentNode.next;

        if (currentNode.next != null) {
            currentNode.next.prev = nodeToMergeInto;
        } else {
            tail = nodeToMergeInto;
        }
        currentNode = nodeToMergeInto;
        size--;
    }

    public StepNode getHead() {
        return head;
    }

    public StepNode getTail() {
        return tail;
    }

    public StepNode getCurrentNode() {
        return currentNode;
    }

    public int getSize() {
        return size;
    }

    public boolean isCurrentStepTimed() {
        return currentNode != null && currentNode.timerSeconds > 0;
    }
}
