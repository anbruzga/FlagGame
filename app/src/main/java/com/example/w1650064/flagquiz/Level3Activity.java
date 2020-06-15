package com.example.w1650064.flagquiz;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Level3Activity extends Activity {

    // keeping them not local because start() is looped
    private final int MAX_TIME_4_TIMER = 10;
    private Timer timer;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private TimerTask timerTask;
    private int correctFlagNo;
    private TextView tvCountry;
    private ImageView flag1;
    private ImageView flag2;
    private ImageView flag3;
    private TextView feedback;
    private TextView timerText;
    private boolean timerOn;
    private int timeLeft = MAX_TIME_4_TIMER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level3);
        start();
        timerOn = getIntent().getBooleanExtra("timer", false);
        if (timerOn) startTimer();
    }

    public void startTimer() {
        timerTask = new TimerTask() {

            @Override
            public void run() {
                if (timeLeft >= 0) {

                    //System.out.println("TimerTask counter is: " + timeLeft);
                    setCounterText(Integer.toString(timeLeft));
                    timeLeft--;
                } else {
                    incorrectSubroutine();
                }
            }
        };

        timer = new Timer("TheTimer");

        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    // update timer's number
    public void setCounterText(final String counterValue) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                timerText.setVisibility(View.VISIBLE);
                timerText = findViewById(R.id.lvl3Timer);
                timerText.setText(counterValue);
            }
        });
    }

    public void start() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                tvCountry = findViewById(R.id.tv2lvl3_country);
                flag1 = findViewById(R.id.lvl3Flag1);
                flag2 = findViewById(R.id.lvl3Flag2);
                flag3 = findViewById(R.id.lvl3Flag3);
                timerText = findViewById(R.id.lvl3Timer);


                Database db = new Database();


                // set up 3 random flags and make one random correct
                correctFlagNo = getRandomNumber(1, 3);

                System.out.println("\n\n\n");
                System.out.println(correctFlagNo);
                System.out.println("\n\n\n");

                flag1.setImageResource(db.getRandomFlag());
                if (correctFlagNo == 1) {
                    tvCountry.setText(db.getCountryName(Database.getLastRandomIndex()));
                }
                flag2.setImageResource(db.getRandomFlag());
                if (correctFlagNo == 2) {
                    tvCountry.setText(db.getCountryName(Database.getLastRandomIndex()));
                }
                flag3.setImageResource(db.getRandomFlag());
                if (correctFlagNo == 3) {
                    tvCountry.setText(db.getCountryName(Database.getLastRandomIndex()));
                }
            }
        });
    }


    // flag click listeners \\
    public void flag1OnClick(View view) {
        if (correctFlagNo == 1) {
            correctSubroutine();
        } else incorrectSubroutine();
    }

    public void flag2OnClick(View view) {
        if (correctFlagNo == 2) {
            correctSubroutine();
        } else incorrectSubroutine();
    }

    public void flag3OnClick(View view) {
        if (correctFlagNo == 3) {
            correctSubroutine();
        } else incorrectSubroutine();
    }

    // correct subroutine
    public void correctSubroutine() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                stopTimer();
                feedback = findViewById(R.id.tvLvl3Feedback);
                feedback.setText(getString(R.string.correct));
                feedback.setTextColor(Color.rgb(0, 255, 0));
                feedback.setVisibility(View.VISIBLE);
            }
        });
    }

    // incorrect subroutine
    public void incorrectSubroutine() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                stopTimer();
                feedback = findViewById(R.id.tvLvl3Feedback);
                feedback.setText(getString(R.string.incorrect));
                feedback.setTextColor(Color.rgb(255, 0, 0));
                feedback.setVisibility(View.VISIBLE);
            }
        });
    }

    public void onButtonClick(View view) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                feedback = findViewById(R.id.tvLvl3Feedback);
                feedback.setVisibility(View.INVISIBLE);
                stopTimer();
                if (timerOn) {
                    timeLeft = MAX_TIME_4_TIMER;
                    startTimer();
                }
                start();
            }
        });
    }

    public void stopTimer() {
        if (timerOn) {
            timer.cancel();
            timer.purge();
            setCounterText("");

        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) (Math.random() * max + min);
    }


}

