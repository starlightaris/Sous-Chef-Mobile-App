package com.nibm.souschef.ui;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.nibm.souschef.R;
import com.nibm.souschef.model.RecipeDLL;
import com.nibm.souschef.model.RecipeRepository;


public class NavigatorActivity extends AppCompatActivity {

    TextView txtCurrent, txtPrev, txtNext, txtProgress;
    EditText txtTimer;
    ProgressBar progressBar;
    Button btnNext, btnPrev, btnStartTimer;
    Timer timer;
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

        timer = new Timer(new Timer.TimerListener() {
            @Override
            public void onTick(int remainingSeconds) {
                runOnUiThread(() -> {
                    txtTimer.setEnabled(false);
                    txtTimer.setText(String.valueOf(remainingSeconds));
                });
            }

            @Override
            public void onFinish() {
                runOnUiThread(() -> {
                    txtTimer.setEnabled(true);
                    txtTimer.setText("0");
                    Toast.makeText(NavigatorActivity.this,
                            "Step Complete!", Toast.LENGTH_SHORT).show();
                });
            }
        });


        if (dll == null || dll.getSize() == 0) {
            Toast.makeText(this, "No recipe loaded", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        updateUI();

        btnNext.setOnClickListener(v -> {
            timer.stopTimer();
            dll.moveToNext();
            updateUI();
        });


        btnPrev.setOnClickListener(v -> {
            timer.stopTimer();
            dll.moveToPrev();
            updateUI();
        });

        btnStartTimer.setOnClickListener(v -> {

            if (timer.isRunning()) return;

            String input = txtTimer.getText().toString().trim();

            int seconds;
            if (input.isEmpty()) {
                seconds = 10;
            } else {
                seconds = Integer.parseInt(input);
            }

            if (seconds <= 0) seconds = 10;

            timer.startTimer(seconds);
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
