package com.example.w1650064.flagquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn1 = (Button) findViewById(R.id.level1);
        Button btn2 = (Button) findViewById(R.id.level2);
        Button btn3 = (Button) findViewById(R.id.level3);
        Button btn4 = (Button) findViewById(R.id.level4);
        final Switch timerSwitch = (Switch) findViewById(R.id.timerSwitch);


        // taken and adapted from https://stackoverflow.com/questions/11278507/android-widget-switch-on-off-event-listener
        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // true if the switch is in the On position
                if (!isChecked) timerSwitch.setText("Timer Off");
                else timerSwitch.setText("Timer On");
            }
        });
        //------------//

        btn1.setOnClickListener(this);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Switch timer = (Switch) findViewById(R.id.timerSwitch);
                Intent i = new Intent(view.getContext(), Level1Activity.class);
                i.putExtra("timer", timer.isChecked());
                view.getContext().startActivity(i);
            }
        });

        btn2.setOnClickListener(this);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch timer = (Switch) findViewById(R.id.timerSwitch);
                Intent i = new Intent(view.getContext(), Level2Activity.class);
                i.putExtra("timer", timer.isChecked());
                view.getContext().startActivity(i);
            }
        });

        btn3.setOnClickListener(this);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch timer = (Switch) findViewById(R.id.timerSwitch);
                Intent i = new Intent(view.getContext(), Level3Activity.class);
                i.putExtra("timer", timer.isChecked());
                view.getContext().startActivity(i);
            }
        });

        btn4.setOnClickListener(this);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch timer = (Switch) findViewById(R.id.timerSwitch);
                Intent i = new Intent(view.getContext(), Level4Activity.class);
                i.putExtra("timer", timer.isChecked());

                view.getContext().startActivity(i);
            }
        });

    }


    @Override
    public void onClick(View v) {

    }
}


