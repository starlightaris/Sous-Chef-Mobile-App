package com.nibm.souschef.model;

public class RecipeDLL {
    private StepNode head;
    private StepNode tail;
    private StepNode currentNode;
    private int size;
    private int currentIndex;
    public RecipeDLL() {
        this.head = null;
        this.tail = null;
        this.currentNode = null;
        this.size = 0;
        this.currentIndex = 0;
    }

    public void insertNode(String instruction) {

        StepNode newNode = new StepNode(instruction);

        if (head == null) {
            head = tail = currentNode = newNode;
            currentIndex = 0;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public StepNode moveToNext() {
        if (currentNode != null && currentNode.next != null) {
            currentNode = currentNode.next;
            currentIndex++;
        }
        return currentNode;
    }

    public StepNode moveToPrev() {
        if (currentNode != null && currentNode.prev != null) {
            currentNode = currentNode.prev;
            currentIndex--;
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

    public String peekNextInstruction() {
        if (currentNode != null && currentNode.next != null)
            return currentNode.next.instruction;
        return "";
    }

    public String peekPrevInstruction() {
        if (currentNode != null && currentNode.prev != null)
            return currentNode.prev.instruction;
        return "";
    }

    public String getCurrentInstruction() {
        if (currentNode != null)
            return currentNode.instruction;
        return "";
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

    public int getCurrentIndex() {
        return currentIndex;
    }

}
