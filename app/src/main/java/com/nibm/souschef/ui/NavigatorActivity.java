package com.nibm.souschef.ui;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.nibm.souschef.R;
import com.nibm.souschef.model.RecipeDLL;
import com.nibm.souschef.model.RecipeRepository;

public class NavigatorActivity extends AppCompatActivity {

    TextView txtCurrent, txtPrev, txtNext, txtProgress, txtTimer;
    ProgressBar progressBar;
    Button btnNext, btnPrev, btnStartTimer, btnMergeNext, btnMergePrev;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;
    boolean isAlarmPlaying = false;
    boolean isTimerRunning = false;

    RecipeDLL dll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        txtCurrent = findViewById(R.id.txtCurrent);
        txtPrev = findViewById(R.id.txtPrevious);
        txtNext = findViewById(R.id.txtNext);
        txtProgress = findViewById(R.id.txtProgress);
        txtTimer = findViewById(R.id.txtTimer);
        progressBar = findViewById(R.id.progressBar);

        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnStartTimer = findViewById(R.id.btnStartTimer);
        btnMergeNext = findViewById(R.id.btnMergeNext);
        btnMergePrev = findViewById(R.id.btnMergePrev);

        dll = RecipeRepository.recipeDLL;

        if (dll == null || dll.getSize() == 0) {
            Toast.makeText(this, "No recipe loaded", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        updateUI();

        btnNext.setOnClickListener(v -> {

            if (isTimerRunning) {
                Toast.makeText(this,
                        "Timer is active!", Toast.LENGTH_SHORT).show();
                return;
            }

            dll.moveToNext();
            updateUI();
        });

        btnPrev.setOnClickListener(v -> {
            dll.moveToPrev();
            updateUI();
        });

        btnStartTimer.setOnClickListener(v -> {

            if (isTimerRunning) {

                stopTimerAndAlarm();

            } else {

                showTimerDialog();
            }
        });

        btnMergeNext.setOnClickListener(v -> {

            dll.concatenateWithNext();
            updateUI();

            Toast.makeText(this,
                    "Steps merged",
                    Toast.LENGTH_SHORT).show();
        });

        btnMergePrev.setOnClickListener(v -> {

            dll.concatenateWithPrev();
            updateUI();

            Toast.makeText(this,
                    "Merged with previous",
                    Toast.LENGTH_SHORT).show();
        });

    }

    private void updateUI() {

        txtCurrent.setText(dll.getCurrentInstruction());

        txtPrev.setText("Prev: " + dll.peekPrevInstruction());

        txtNext.setText("Next: " + dll.peekNextInstruction());

        int index = dll.getCurrentIndex() + 1;
        int total = dll.getSize();

        txtProgress.setText("Step " + index + " of " + total);

        progressBar.setMax(total);
        progressBar.setProgress(index);

        btnPrev.setEnabled(dll.getCurrentIndex() > 0);

        if (!isTimerRunning)
            btnNext.setEnabled(dll.getCurrentIndex() < total - 1);
    }

    private int[] extractTimeFromText(String text) {

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        text = text.toLowerCase();

        java.util.regex.Matcher h =
                java.util.regex.Pattern
                        .compile("(\\d+)\\s*(hour|hr)")
                        .matcher(text);

        if (h.find())
            hours = Integer.parseInt(h.group(1));


        java.util.regex.Matcher m =
                java.util.regex.Pattern
                        .compile("(\\d+)\\s*(minute|min)")
                        .matcher(text);

        if (m.find())
            minutes = Integer.parseInt(m.group(1));


        java.util.regex.Matcher s =
                java.util.regex.Pattern
                        .compile("(\\d+)\\s*(second|sec)")
                        .matcher(text);

        if (s.find())
            seconds = Integer.parseInt(s.group(1));


        return new int[]{hours, minutes, seconds};
    }

    private void showTimerDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_timer, null);

        EditText etHours = view.findViewById(R.id.etHours);
        EditText etMinutes = view.findViewById(R.id.etMinutes);
        EditText etSeconds = view.findViewById(R.id.etSeconds);

        // 🔥 AUTO FILL FROM STEP TEXT
        String currentStep =
                dll.getCurrentInstruction();

        int[] time =
                extractTimeFromText(currentStep);

        if(time[0] > 0)
            etHours.setText(String.valueOf(time[0]));

        if(time[1] > 0)
            etMinutes.setText(String.valueOf(time[1]));

        if(time[2] > 0)
            etSeconds.setText(String.valueOf(time[2]));


        new AlertDialog.Builder(this)
                .setTitle("Set Timer")
                .setView(view)
                .setPositiveButton("Start", (dialog, which) -> {

                    int hours =
                            parseInt(etHours.getText().toString());

                    int minutes =
                            parseInt(etMinutes.getText().toString());

                    int seconds =
                            parseInt(etSeconds.getText().toString());

                    int totalSeconds =
                            hours * 3600 +
                                    minutes * 60 +
                                    seconds;

                    if(totalSeconds > 0)
                        startTimer(totalSeconds);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private int parseInt(String value) {

        if (value == null || value.isEmpty())
            return 0;

        return Integer.parseInt(value);
    }

    private void playAlarmSound() {

        try {

            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

            if (mediaPlayer != null) {

                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    mediaPlayer = null;
                });

                mediaPlayer.start();
            }
            else {
                Toast.makeText(this,
                        "Audio failed to load",
                        Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(this,
                    "Audio error",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void playAlarmLoop() {

        try {

            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

            if (mediaPlayer != null) {

                mediaPlayer.setLooping(true);

                mediaPlayer.start();

                isAlarmPlaying = true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }



    private void startTimer(int totalSeconds) {

        isTimerRunning = true;

        btnNext.setEnabled(false);

        btnStartTimer.setText("STOP TIMER");
        btnStartTimer.setBackgroundColor(
                getResources().getColor(android.R.color.holo_red_dark)
        );

        countDownTimer = new CountDownTimer(totalSeconds * 1000L, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                int remaining = (int) (millisUntilFinished / 1000);

                int hrs = remaining / 3600;
                int mins = (remaining % 3600) / 60;
                int secs = remaining % 60;

                txtTimer.setText(
                        String.format("%02d:%02d:%02d", hrs, mins, secs)
                );
            }

            @Override
            public void onFinish() {

                txtTimer.setText("00:00:00");

                playAlarmLoop();
            }
        }.start();
    }

    private void stopTimerAndAlarm() {

        // Stop countdown
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        // Stop alarm sound
        if (mediaPlayer != null && isAlarmPlaying) {

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            isAlarmPlaying = false;
        }

        isTimerRunning = false;

        btnNext.setEnabled(
                dll.getCurrentIndex() < dll.getSize() - 1
        );

        btnStartTimer.setText("START TIMER");

        btnStartTimer.setBackgroundColor(
                getResources().getColor(R.color.purple_500)
        );
    }


}
