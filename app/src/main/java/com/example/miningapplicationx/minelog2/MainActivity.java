package com.example.miningapplicationx.minelog2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout myLayout=(LinearLayout) findViewById(R.id.equipment_list);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        String[] equipments = {"Shovel","Truck","Backhoe","LHD","Drill","Equipment A", "Equipment B", "Eq C", "Eq D","Eq E", "Eq F", "Eq G"};
        int size = equipments.length;
        TextView[] equipment = new TextView[size];
        for (int l=0; l<(size); l++) {
            equipment[l] = new TextView(this);
            equipment[l].setTextSize(40);
            equipment[l].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            equipment[l].setLayoutParams(lp);
            equipment[l].setId(l);
            equipment[l].setBackground(getResources().getDrawable(R.drawable.back));
            equipment[l].setText(equipments[l]);
            equipment[l].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent shiftlog = new Intent(getApplicationContext(), Shift_log.class);
                    startActivity(shiftlog);}
            });
            myLayout.addView(equipment[l]);
        }
    }

    }

