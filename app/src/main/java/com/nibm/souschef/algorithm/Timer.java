package com.nibm.souschef.algorithm;

import com.nibm.souschef.model.StepNode;

public class Timer {

    private boolean paused = false;
    private boolean running = false;

    private int remainingSeconds;
    private StepNode currentStep;

    public void startTimer(StepNode stepNode) {

        // stop any existing timer
        stopTimer();

        this.currentStep = stepNode;
        this.remainingSeconds = stepNode.getDuration();

        this.running = true;
        this.paused = false;

        new Thread(() -> {
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
        System.out.println("Remaining: " + seconds);
    }

    private void timerFinished() {
        System.out.println("Step completed!");
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
