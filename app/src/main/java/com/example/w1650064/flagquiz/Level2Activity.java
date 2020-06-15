package com.example.w1650064.flagquiz;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.VISIBLE;

public class Level2Activity extends Activity {

    private final int MAX_GUESSES = 3;
    private final int MAX_TIME_4_TIMER = 15;
    // for other functions to see the answer
    private String answer;
    // to store the textView underscores
    private String clue;
    // keeps attempts count
    private int attemptCount = 0;

    // various views
    private Database db;
    private ImageView imgLvl2;
    private int lastRand;
    private Button btn;
    private EditText editText;
    private TextView underscoresTv;
    private TextView tvShowCountryName;
    private TextView tvFeedback;
    private TextView timerText;

    // timer related objects
    private boolean timerOn;
    private int timeLeft = MAX_TIME_4_TIMER;
    private Timer timer;
    private TimerTask timerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2);
        start();
    }

    // find views by ids
    public void findViews() {
        imgLvl2 = findViewById(R.id.imgLvl2);
        underscoresTv = findViewById(R.id.tvLvl2);
        btn = findViewById(R.id.btnLvl2);
        editText = findViewById(R.id.lvl2Et);
        tvShowCountryName = findViewById(R.id.tvlvl2ShowCountryName);
        tvFeedback = findViewById(R.id.tvlvl2Feedback);
        timerText = findViewById(R.id.lvl2Timer);
    }

    // stores the answer
    public void storeAnswer() {
        lastRand = Database.getLastRandomIndex();
        answer = db.getCountryName(lastRand);
        answer = answer.toUpperCase();
    }


    public void setUnderscores() {
        clue = "";
        for (int i = 0; i < answer.length(); i++) {
            if (!(answer.charAt(i) == (' '))) {
                clue = clue + "_ ";
            } else
                clue = clue + "   ";
        }
        System.out.println(underscoresTv.toString());
        //System.out.println(answer);
        System.out.println(clue);
        underscoresTv.setText(clue);
        underscoresTv.setVisibility(VISIBLE);
    }

    public void start() {
        db = new Database();

        findViews();

        // Set random image
        imgLvl2.setImageResource(db.getRandomFlag());
        imgLvl2.setVisibility(VISIBLE);

        storeAnswer();

        //test line
        System.out.println(answer);

        // puts the right amount of underscores into TextView
        setUnderscores();

        // forces the keyboard out, as sometimes it seems to be stuck...
        final EditText editText2 = findViewById(R.id.lvl2Et);
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.showSoftInput(editText2, InputMethodManager.SHOW_IMPLICIT);
                    editText2.setHint("");
                } else
                    editText2.setHint("Enter a Letter!");
            }
        });

        // checks if the timer is on and starts it
        timerOn = getIntent().getBooleanExtra("timer", false);
        if (timerOn) startTimer();
    }

    // timer's run method
    public void startTimer() {
        timerTask = new TimerTask() {

            @Override
            public void run() {
                if ((timeLeft >= 0)) {
                    if (attemptCount >= MAX_GUESSES) {
                        guessesUsed();
                        stopTimer();

                    } else if (underscoresTv.getText().equals(getString(R.string.CORRECT))) {
                        //stop timer if "correct" feedback was given
                        setCounterTextInvisible();
                        stopTimer();
                    } else if (btn.getText().equals(getString(R.string.next))) {
                        //stop timer if button is "next"
                        stopTimer();
                    }

                    else{
                        //System.out.println("TimerTask counter is: " + timeLeft);
                        setCounterText(Integer.toString(timeLeft));
                        timeLeft--;
                    }
                } else {
                    //setFeedbackWrong();
                    //btn.setText("Next");
                    guessesUsed();
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
            setCounterText(" ");
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

    public void setCounterTextInvisible() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                timerText.setVisibility(View.INVISIBLE);
            }
        });
    }


    public void newFlag() {
        editText.setVisibility(VISIBLE);
        btn.setText(getString(R.string.submit));
        attemptCount = 0;
        tvShowCountryName.setVisibility(View.INVISIBLE);
        underscoresTv.setTextColor(Color.rgb(255, 64, 129));
        start();
    }

    public void onClickListener(View v) {

        //* Taken from https://stackoverflow.com/questions/13593069/androidhide-keyboard-after-button-click *\\
        //================ Hide Virtual Key Board When  Clicking Button============//
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btn.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        //=================== Hide Virtual Keyboard===============================//
        //*                                                                                                 *\\
        boolean found = false;

        // if already all guessed well, reset values and start again from beginning
        if (btn.getText().equals(getString(R.string.next))) {
            tvFeedback.setVisibility(View.INVISIBLE);
            newFlag();
        } else if ((editText.getText().toString().equals("")
                || (editText.getText().toString().equals(getString(R.string.enter_a_letter))))) {
            // do nothing -- prevents user from empty input giving error
            System.out.println("No letter entered!");

        } else {
            // it gets the last char of editText
            // and its fine, because max 1 letter can be inserted in editText
            char guess = editText.getText().toString().toUpperCase().charAt(0);

            StringBuilder newClue = new StringBuilder(clue);
            // need to remake the answer with the same amount of spaces as there is in String clue
            String answerRemake = "";

            for (int i = 0; i < answer.length(); i++) {
                if (answer.charAt(i) == ' ') { // if empty space, put three empty spaces

                    answerRemake = answerRemake + "   ";
                } else if (answer.charAt(i) == ',') { // if comma, leave comma
                    answerRemake = answerRemake + ",";
                } else { // else remake = answer + space
                    answerRemake = answerRemake + answer.charAt(i) + " ";
                }

            }

            // for the length of the clue, if answRemake at specific char equals the guessed letter
            // reshape newClue with the letter

            for (int j = 0; j < clue.length(); j++) {
                if (answerRemake.charAt(j) == guess) {
                    newClue.setCharAt(j, guess);
                    found = true;
                }

            }

            // so when user enters correct letter, it does not say that guess is incorrect
            if (found) {
                tvFeedback.setText("");
                tvFeedback.setVisibility(View.INVISIBLE);
            }
            // if it was guess was correct, change the feedback to tell that guess is incorrect
            // if it was not found, count it as wrong attempt
            else {
                attemptCount++;

                tvFeedback.setTextColor(Color.rgb(255, 255, 0));
                tvFeedback.setText(getString(R.string.incorrect_guess));
                tvFeedback.setVisibility(VISIBLE);
            }

            // exporting out of the function so modified clue can be accessed by another guessing action
            clue = newClue.toString();

            underscoresTv.setText(clue);
            editText.setText("");

            // if no underscores are found, change textview to CORRECT! in green + change button
            if (!(clue.contains("_"))) {
                underscoresTv.setText(getString(R.string.CORRECT));
                underscoresTv.setTextColor(Color.rgb(0, 255, 0));
                editText.setVisibility(View.INVISIBLE);
                btn.setText(getString(R.string.next));
                stopTimer();
            }

            //if all the guesses are used, change to WRONG in red + change button
            if (attemptCount >= MAX_GUESSES) {
                guessesUsed();
            }
        }
    }

    public void guessesUsed() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                tvShowCountryName.setText(answer);
                tvShowCountryName.setVisibility(VISIBLE);
                underscoresTv.setText(getString(R.string.WRONG));
                underscoresTv.setTextColor(Color.rgb(255, 0, 0));
                editText.setVisibility(View.INVISIBLE);
                btn.setText(getString(R.string.next));
            }
        });
    }

}
