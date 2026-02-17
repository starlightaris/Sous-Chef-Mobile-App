package com.nibm.souschef.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
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

            // For demo: 10 second timer
            int seconds = 10;

            isTimerRunning = true;
            btnNext.setEnabled(false); // 🔥 LOCK navigation

            countDownTimer = new CountDownTimer(seconds * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    long remaining = millisUntilFinished / 1000;
                    txtTimer.setText("00:" + String.format("%02d", remaining));
                }

                @Override
                public void onFinish() {
                    txtTimer.setText("00:00");
                    isTimerRunning = false;
                    btnNext.setEnabled(true); // 🔥 UNLOCK navigation
                    Toast.makeText(NavigatorActivity.this,
                            "Step Complete!", Toast.LENGTH_SHORT).show();
                }
            }.start();
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

        // Optional: Disable buttons at edges
        btnPrev.setEnabled(dll.getCurrentIndex() > 0);
        btnNext.setEnabled(dll.getCurrentIndex() < total - 1);
    }
}
