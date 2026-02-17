package com.nibm.souschef.algorithm;

import com.nibm.souschef.model.StepNode;

public class Timer {

    private boolean paused = false;
    private boolean running = false;

    private int remainingSeconds;
    private StepNode currentStep;
    public void startTimer(StepNode stepNode) {

        stopTimer(); //if any timer is running we need to stop it first.

        this.currentStep = stepNode;
        this.remainingSeconds = stepNode.getDuration();

        this.running = true;
        this.paused = false;

        new Thread(() -> { //using threads so we can use sleep method for pausing timer.
            try {
                while (running && remainingSeconds > 0) {

                    if (!paused) {
                        Thread.sleep(1000);
                        remainingSeconds--;
                        updateDisplay(remainingSeconds);
                    }
                }

                if (remainingSeconds == 0) {
                    timerFinished();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void pauseTimer() {
        paused = true;
    }

    public void resumeTimer() {
        paused = false;
    }

    public void stopTimer() {
        running = false;
    }

    private void updateDisplay(int seconds) {
        //logic to show on display.
        //testing on console
        System.out.println("Remaining: " + seconds);
    }

    private void timerFinished() {
        //logic to show on display
        //console output
        System.out.println("Step completed!");
        running = false;
    }

    public boolean isRunning() { //method to find if step is still running or not
        return running;
    }
}
