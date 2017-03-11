package com.example.miningapplicationx.minelog2;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextMenu.ContextMenuInfo;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Shift_log extends AppCompatActivity {

    public final DBHelper dbHelper = new DBHelper(Shift_log.this);
    public String placeholder;
    public final String date = new SimpleDateFormat("yyyyMMdd").format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_log);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        placeholder=message;
        setTitle("Shift log for " + placeholder);
        LinearLayout myLayout = (LinearLayout) findViewById(R.id.shift_list);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//SQLEXTRACT

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + placeholder, null);
        String[] shifts = new String[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()){
            String uname = cursor.getString(cursor.getColumnIndex("EQSHIFTMD"));
            shifts[i] = uname;
            i++;
        }
        cursor.close();
//END SQL EXXTRACT
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

        Toast.makeText(this, "Log Import Successful from " + placeholder + date, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Menu");
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0, v.getId(), 0, "Export Shift to CSV file");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Delete")) {
            function2(item.getItemId());
        } else if (item.getTitle().equals("Export Shift to CSV file")) {
            function3(item.getItemId());
        } else {
            return false;
        }
        return true;
    }

    public void function2(int id) {
        Toast.makeText(this, "Deleted Shift", Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        TextView tv = (TextView) this.findViewById(id);
        String myString= tv.getText().toString();
        db.delete(placeholder,"EQSHIFTMD=? ",new String[]{myString});
        db.close();
        finish();
        startActivity(getIntent());
    }

    public void function3(int id) {
        Toast.makeText(this, "Shift Log Exported", Toast.LENGTH_SHORT).show();

        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "AnalysisData" + placeholder +".csv";
        String filePath = baseDir + File.separator+ "Download" + File.separator+ fileName;
        File f = new File(filePath);

        List<String[]> data = new ArrayList<String[]>();
        data.add(new String[] {"India", "New Delhi"});
        data.add(new String[] {"United States", "Washington D.C"});
        data.add(new String[] {"Germany", "Berlin"});

        try {
            CSVWriter writer = new CSVWriter(new FileWriter(f));
            writer.writeAll(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void function4(View s){
        AlertDialog.Builder builder = new AlertDialog.Builder(Shift_log.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("New shift added. Returning to Shift Log");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("EQSHIFTMD", date);
                long newRowId;
                newRowId = db.insert(
                        placeholder,
                        null,
                        values);
                String shifts;
                Cursor cursor = db.rawQuery("SELECT * FROM " + placeholder + " WHERE EQSHIFTMD =" + date, null);
                String uname= "0", uId="0";
                if( cursor != null && cursor.moveToFirst() ) {
                    uname = cursor.getString(cursor.getColumnIndex("EQSHIFTMD"));
                    uId = cursor.getString(cursor.getColumnIndex("EQSHIFTID"));
                }
                    shifts = uname + " log entry number " + uId;
                    ContentValues values1 = new ContentValues();
                    values1.put("EQSHIFTMD",shifts);

                    String selection = "EQSHIFTID" + " LIKE ?";
                    String[] selectionArgs = { String.valueOf(uId) };

                    int count = db.update(
                            placeholder,
                            values1,
                            selection,
                            selectionArgs);
                cursor.close();

                finish();
                startActivity(getIntent());
                db.close();
                dialog.dismiss();
                Toast.makeText(Shift_log.this, date , Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel New Shift", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

