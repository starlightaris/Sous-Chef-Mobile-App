package com.nibm.souschef.algorithm;

import com.nibm.souschef.model.StepNode;

public class Timer {

    public interface TimerListener {
        void onTick(int remainingSeconds);
        void onFinish();
    }

    private boolean paused;
    private boolean running;

    private int remainingSeconds;
    private StepNode currentStep;
    private TimerListener listener;

    public Timer(TimerListener listener) {
        this.listener = listener;
        this.paused = false;
        this.running = false;
    }

    public void startTimer(StepNode stepNode) {
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
                        if (listener != null) {
                            listener.onTick(remainingSeconds);
                        }
                    }
                }

                if (running && remainingSeconds == 0) {
                    running = false;
                    if (listener != null) {
                        listener.onFinish();
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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

    public boolean isRunning() {
        return running;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public StepNode getCurrentStep() {
        return currentStep;
    }
}
