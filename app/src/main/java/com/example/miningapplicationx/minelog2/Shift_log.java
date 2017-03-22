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
import android.util.Log;
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
    public static String eqdbname;
    public static String uname1;
    public static String equip_name;
    public static String shift_spec;
    public static String datetodb;
    public static String lognum;
    public static String dbname;
    public static int previous_shift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_log);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        placeholder=message;
        setTitle("Shift log for " + placeholder);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        eqdbname =  message +"aclist";
        ContentValues values = new ContentValues();
        values.put("ACTIVITY","End Shift");
        values.put("TYPE","E");
        long newRowId;
        newRowId = db.insert(
                eqdbname,
                null,
                values);
        values.put("ACTIVITY","Maintenance");
        values.put("TYPE","D");
        long newRowId1;
        newRowId = db.insert(
                eqdbname,
                null,
                values);
        values.put("ACTIVITY","Downtime");
        values.put("TYPE","D");
        long newRowId2;
        newRowId = db.insert(
                eqdbname,
                null,
                values);
        values.put("ACTIVITY","Standby");
        values.put("TYPE","D");
        long newRowId3;
        newRowId = db.insert(
                eqdbname,
                null,
                values);
//initialization start
        //sqlextract
        Cursor cursor = db.rawQuery("SELECT * FROM EQUIPMENTLOG WHERE EQUIPMENTNAME = '" + message + "'", null);
        cursor.moveToFirst();
        uname1 = cursor.getString(cursor.getColumnIndex("EQUIPMENTTYPE"));
        String truck1="Truck"; String shovel1="Shovel";
        //sqlextract close
        if (uname1 != null && uname1.equalsIgnoreCase(truck1)){
            values.put("ACTIVITY","Travelling");
            values.put("TYPE","P");
            long newRowId4;
            newRowId = db.insert(
                    eqdbname,
                    null,
                    values);
            values.put("ACTIVITY","Loading");
            values.put("TYPE","P");
            long newRowId5;
            newRowId = db.insert(
                    eqdbname,
                    null,
                    values);
        }
        else if (uname1 != null && uname1.equalsIgnoreCase(shovel1))
        {
            values.put("ACTIVITY","Travelling");
            values.put("TYPE","P");
            long newRowId6;
            newRowId = db.insert(
                    eqdbname,
                    null,
                    values);
            values.put("ACTIVITY","Shovelling");
            values.put("TYPE","P");
            long newRowId7;
            newRowId = db.insert(
                    eqdbname,
                    null,
                    values);
        }
        else{
        }
        LinearLayout myLayout = (LinearLayout) findViewById(R.id.shift_list);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//SQLEXTRACT

        cursor = db.rawQuery("SELECT * FROM " + placeholder, null);
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
        final TextView[] shift = new TextView[shiftsize];
        for (int l = 0; l < (shiftsize); l++) {
            shift[l] = new TextView(this);
            shift[l].setTextSize(30);
            shift[l].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            shift[l].setLayoutParams(lp);
            final int b = l;
            shift[l].setId(l);
            shift[l].setBackground(getResources().getDrawable(R.drawable.back_shiftdone));
            shift[l].setTextColor(getResources().getColor(R.color.textcolor));
            shift[l].setText(shifts[l]);
            shift[l].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View s) {
                    Intent shiftlog = new Intent(getApplicationContext(), ActivityPanel.class);
                    shiftlog.putExtra("message", placeholder);
                    shiftlog.putExtra("specshift", shift[b].getText().toString());
                    startActivity(shiftlog);
                }
            });
            myLayout.addView(shift[l]);
            registerForContextMenu(shift[l]);
        }

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



        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<String[]> data = new ArrayList<String[]>();
        TextView tv = (TextView) this.findViewById(id);
        shift_spec= tv.getText().toString();
        datetodb= shift_spec.substring(0,8);
        lognum=shift_spec.substring(26,27);

        eqdbname = placeholder +"aclist";
        dbname = placeholder +"_" + date +"_"+ "logentry" +lognum;
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = dbname+".csv";
        String filePath = baseDir + File.separator+ "Download" + File.separator+ fileName;
        File f = new File(filePath);
        data.add(new String[] {"Equipment", placeholder});
        data.add(new String[] {"Type", uname1});
        data.add(new String[] {"Date", datetodb});
        data.add(new String[] {"Shift", lognum});
        data.add(new String[] {"Type", "Activity", "Time Start", "Other"});

        Cursor cursor = db.query(dbname, new String[]{"ACTIVITY","TIME","TYPE"},null, null, null, null, null);

        if(cursor.moveToFirst());
        {
            data.add(new String[] {cursor.getString(2), cursor.getString(0), cursor.getString(1)});
            while (cursor.moveToNext()) {
                data.add(new String[] {cursor.getString(2), cursor.getString(0),cursor.getString(1)});
            }
        }
        cursor.close();
        db.close();

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
                Cursor cursor;
                values.put("EQSHIFTMD", date);

                db.beginTransaction();
                try {

                    cursor = db.rawQuery("SELECT * FROM " + placeholder + " WHERE EQSHIFTID = (SELECT MAX (EQSHIFTID) FROM " + placeholder + ");",null);
                    cursor.moveToFirst();
                    previous_shift=cursor.getInt(cursor.getColumnIndex("EQNUM"));
                    Toast.makeText(Shift_log.this,"Created " + previous_shift, Toast.LENGTH_SHORT).show();
                    db.setTransactionSuccessful();
                }catch(Exception e){
                    previous_shift=0;
                } finally {
                    db.endTransaction();
                }

                if (previous_shift==3)
                {
                    previous_shift= 1;
                }
                else
                {
                    previous_shift=previous_shift+1;
                }
                values.put("EQNUM",previous_shift);

                long newRowId;
                newRowId = db.insert(
                        placeholder,
                        null,
                        values);
                String shifts;
                cursor = db.rawQuery("SELECT * FROM " + placeholder + " WHERE EQSHIFTMD = " + date + " AND EQNUM = " + previous_shift, null);
                String uname= "0", uId="0"; int uNum=0;
                if( cursor != null && cursor.moveToFirst() ) {
                    uname = cursor.getString(cursor.getColumnIndex("EQSHIFTMD"));
                    uId = cursor.getString(cursor.getColumnIndex("EQSHIFTID"));
                    uNum = cursor.getInt(cursor.getColumnIndex("EQNUM"));
                }
                    shifts = uname + " log entry number " + uNum;
                    ContentValues values1 = new ContentValues();
                    values1.put("EQSHIFTMD",shifts);

                    String selection = "EQSHIFTID" + " LIKE ?";
                    String[] selectionArgs = { String.valueOf(uId) };

                    datetodb= shifts.substring(0,8);
                    lognum=shifts.substring(26,27);
                    eqdbname = placeholder +"aclist";
                    dbname = placeholder +"_" + date +"_"+ "logentry" +lognum;
                    dbHelper.AddActivityLog(dbname);


                db.beginTransaction();
                 try {
                     int count = db.update(

                             placeholder,
                             values1,
                             selection,
                             selectionArgs);
                     db.setTransactionSuccessful();
                 }
                 catch(Exception e){
                     Toast.makeText(Shift_log.this, "Duplicates Error  " + date, Toast.LENGTH_SHORT).show();
                     db.delete(placeholder,"EQSHIFTMD=? ",new String[]{date});
                     // TODO duplicates error here

                     finish();
                     startActivity(getIntent());
                }
                finally {
                     db.endTransaction();
                 }
                cursor.close();

                finish();
                startActivity(getIntent());
                db.close();
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

