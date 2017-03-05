package com.example.miningapplicationx.minelog2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.BaseColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextMenu.ContextMenuInfo;

public class Shift_log extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_log);
        LinearLayout myLayout = (LinearLayout) findViewById(R.id.shift_list);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Intent intent = getIntent();
        int temp = intent.getIntExtra("int_value", 0);

        String[] shifts = {"shift 1", "shift 2", "shift 3", "shift 4", "shift 5", "shift A", "shift B", "shift C", "shift D", "shift E", "shift F", "shift G"};
        int shiftsize = shifts.length;
        TextView[] shift = new TextView[shiftsize];
        for (int l = 0; l < (shiftsize); l++) {
            shift[l] = new TextView(this);
            shift[l].setTextSize(30);
            shift[l].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            shift[l].setLayoutParams(lp);
            shift[l].setId(l);
            shift[l].setBackground(getResources().getDrawable(R.drawable.back_shiftdone));
            shift[l].setTextColor(getResources().getColor(R.color.textcolor));
            shift[l].setText(shifts[l]);
            shift[l].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View s) {
                    Intent shiftlog = new Intent(getApplicationContext(), ActivityPanel.class);
                    startActivity(shiftlog);
                }
            });
            myLayout.addView(shift[l]);
            registerForContextMenu(shift[l]);
        }
        Toast.makeText(this, "Log Import Successful " + (temp + 1), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Menu");
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0, v.getId(), 0, "Export Shift to CSV file");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Edit")) {
            function1(item.getItemId());
        } else if (item.getTitle().equals("Delete")) {
            function2(item.getItemId());
        } else if (item.getTitle().equals("Export Shift to CSV file")) {
            function3(item.getItemId());
        } else {
            return false;
        }
        return true;
    }

    public void function1(int id) {
        Toast.makeText(this, "Shift Edit Successful", Toast.LENGTH_SHORT).show();
    }

    public void function2(int id) {
        Toast.makeText(this, "Deleted Shift", Toast.LENGTH_SHORT).show();
    }

    public void function3(int id) {
        Toast.makeText(this, "Shift Log Exported", Toast.LENGTH_SHORT).show();
    }
    public void function4(View s){
        AlertDialog.Builder builder = new AlertDialog.Builder(Shift_log.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("New shift added. Returning to Shift Log");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

