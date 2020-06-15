package com.example.w1650064.flagquiz;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.VISIBLE;

public class Level1Activity extends Activity {

    private final int MAX_TIME_4_TIMER = 10;
    private TextView timerText;
    private boolean timerOn;
    private int timeLeft = MAX_TIME_4_TIMER;
    private Timer timer;
    private TimerTask timerTask;
    private TextView feedback;
    private int lastRand;
    private Database db;
    private Button btn;
    private Spinner dropdown;
    private String answer;
    private ImageView flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_level1);

        db = new Database();

        findViews(); // finds views for the start of the program

        // checks if the timer is on and starts it
        timerOn = getIntent().getBooleanExtra("timer", false);
        if (timerOn) startTimer();


        // Set random image
        flag.setImageResource(db.getRandomFlag());
        flag.setVisibility(VISIBLE);

        // setting up the spinner
        String[] answersArray = db.getAnswersArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, answersArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        // outputs the name of the country for testing
        printAnswer();

    }

    public void findViews() {
        timerText = findViewById(R.id.lvl1Timer);
        flag = findViewById(R.id.imgLvl1);
        dropdown = findViewById(R.id.spinnerLvl1);
        feedback = findViewById(R.id.textViewLvl1);
        btn = findViewById(R.id.btnLvl1);
        db = new Database();
    }

    // outputs the name of the country for testing
    public void printAnswer() {
        lastRand = Database.getLastRandomIndex();
        answer = db.getCountryName(lastRand);
        System.out.println(answer);
    }

    // timer's run method
    public void startTimer() {
        timerTask = new TimerTask() {

            @Override
            public void run() {
                if ((timeLeft >= 0)) { // if there is time left, update the counter

                    //System.out.println("TimerTask counter is: " + timeLeft);
                    setCounterText(Integer.toString(timeLeft));
                    timeLeft--;
                } else {
                    setFeedbackWrong();
                    btn.setText("Next");
                    stopTimer();
                }
            }
        };
        timer = new Timer("TheTimer");
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public void stopTimer() {
        if (timerOn) {
            timeLeft = MAX_TIME_4_TIMER;
            timer.cancel();
            timer.purge();
            setCounterText("");
        }
    }


    public void setCounterText(final String counterValue) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                timerText.setVisibility(View.VISIBLE);
                timerText.setText(counterValue);
            }
        });
    }


    public boolean isCorrect() {

        lastRand = Database.getLastRandomIndex();
        answer = db.getCountryName(lastRand);

        feedback.setText(answer);
        feedback.setTextColor(Color.rgb(0, 255, 0));

        if (answer.equals(dropdown.getSelectedItem().toString())) {
            return true;
        } else return false;
    }

    public void btn1OnClick(View view) {


        if (btn.getText().toString().equals("Submit")) {
            if (isCorrect()) {
                //if correct change color to green
                setFeedbackCorrect();
            } else { //else to red
                setFeedbackWrong();
            }

            if (timerOn) {
                stopTimer();
            }

            btn.setText("Next");

        } else if (btn.getText().toString().equals("Next")) {
            flag.setImageResource(db.getRandomFlag());
            flag.setVisibility(VISIBLE);
            btn.setText("Submit");
            feedback.setText("");

            printAnswer();
            if(timerOn) startTimer();
        }
    }

    public void setFeedbackCorrect() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                feedback.setText(getString(R.string.Correct));
                feedback.setTextColor(Color.rgb(0, 255, 0));
                feedback.setVisibility(VISIBLE);
            }
        });
    }

    public void setFeedbackWrong() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                feedback.setText("Wrong");
                feedback.setTextColor(Color.rgb(255, 0, 0));
                feedback.setVisibility(VISIBLE);
            }
        });
    }


}
