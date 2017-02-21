package com.example.miningapplicationx.minelog2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Shift_log extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_log);
        LinearLayout myLayout=(LinearLayout) findViewById(R.id.shift_list);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        String[] shifts = {"shift 1","shift 2","shift 3","shift 4","shift 5","shift A", "shift B", "shift C", "shift D","shift E", "shift F", "shift G"};
        int shiftsize = shifts.length;
        TextView[] shift = new TextView[shiftsize];
        for (int l=0; l<(shiftsize); l++) {
            shift[l] = new TextView(this);
            shift[l].setTextSize(40);
            shift[l].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            shift[l].setLayoutParams(lp);
            shift[l].setId(l);
            shift[l].setBackground(getResources().getDrawable(R.drawable.back_shiftdone));
            shift[l].setText(shifts[l]);
            shift[l].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View s) {
                    Intent shiftlog = new Intent(getApplicationContext(), ActivityPanel.class);
                    startActivity(shiftlog);}
            });
            myLayout.addView(shift[l]);
        }
    }
}
