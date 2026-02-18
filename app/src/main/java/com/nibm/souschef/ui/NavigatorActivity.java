package com.nibm.souschef.ui;

import android.app.AlertDialog;
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
    Button btnNext, btnPrev, btnStartTimer;

    CountDownTimer countDownTimer;
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

            if (isTimerRunning) return;

            showTimerDialog();
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

    private void showTimerDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_timer, null);

        EditText etHours = view.findViewById(R.id.etHours);
        EditText etMinutes = view.findViewById(R.id.etMinutes);
        EditText etSeconds = view.findViewById(R.id.etSeconds);

        new AlertDialog.Builder(this)
                .setTitle("Set Timer")
                .setView(view)
                .setPositiveButton("Start", (dialog, which) -> {

                    int hours = parseInt(etHours.getText().toString());
                    int minutes = parseInt(etMinutes.getText().toString());
                    int seconds = parseInt(etSeconds.getText().toString());

                    int totalSeconds =
                            hours * 3600 +
                                    minutes * 60 +
                                    seconds;

                    if (totalSeconds > 0)
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

    private void startTimer(int totalSeconds) {

        isTimerRunning = true;
        btnNext.setEnabled(false);

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

                isTimerRunning = false;

                btnNext.setEnabled(
                        dll.getCurrentIndex() < dll.getSize() - 1
                );

                Toast.makeText(NavigatorActivity.this,
                        "Step Complete!",
                        Toast.LENGTH_SHORT).show();
            }
        }.start();
    }
}
