package com.example.stephaniefong.timer;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends Activity {
    private long START_TIME_IN_MILLIS = 600000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private EditText mTimeSelection;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;

    private long ogTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimeSelection = findViewById((R.id.timeSelection));

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mTextViewCountDown.setVisibility(View.INVISIBLE);

        mTimeSelection.setVisibility(View.VISIBLE);

        mButtonStartPause = findViewById(R.id.button_start_pause);

        mButtonReset = findViewById(R.id.button_reset);

        mTimeLeftInMillis = START_TIME_IN_MILLIS;

        ogTime = mTimeLeftInMillis;

        mButtonStartPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    findViewById(R.id.note).setVisibility(View.INVISIBLE);
                    if(!(mTimeLeftInMillis < ogTime)) {
                        if (mTimeSelection.getText().toString().length() > 0) {
                            String[] time = mTimeSelection.getText().toString().split(":");
                            if (time.length == 1) {
                                mTimeLeftInMillis = Long.parseLong(time[0]) * 60 * 1000;
                            } else if (time[0].length() == 0){
                                mTimeLeftInMillis = Long.parseLong(time[1]) * 1000;
                            } else {
                                mTimeLeftInMillis = Long.parseLong(time[0]) * 60 * 1000 + Long.parseLong(time[1]) * 1000;
                            }
                            ogTime = mTimeLeftInMillis;
                        }
                    }
                    startTimer();
                    mTextViewCountDown.setVisibility(View.VISIBLE);
                    mTimeSelection.setVisibility(View.INVISIBLE);
                }

            }
        });


        mButtonReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
    }


    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
//                updateCountDownText();
                mButtonStartPause.setText(R.string.START);
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
                mTextViewCountDown.setText("0:00");
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText(R.string.PAUSE);
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText(R.string.START);
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        mTimeLeftInMillis = ogTime;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        mTextViewCountDown.setVisibility(View.INVISIBLE);
        mTimeSelection.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }
}

