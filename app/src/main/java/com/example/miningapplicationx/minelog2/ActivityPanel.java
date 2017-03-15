package com.example.miningapplicationx.minelog2;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Timestamp;
import java.util.ArrayList;

import static android.graphics.Color.parseColor;

public class ActivityPanel extends AppCompatActivity {
    GridView activityView;

    public final DBHelper dbHelper = new DBHelper(ActivityPanel.this);
    public ArrayList<String> activitylist = new ArrayList<>();
    public static String eqdbname;
    public static String eqypenew;
    public static String equip_name;
    public static String shift_spec;
    public static String date;
    public static String lognum;
    public static String dbname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_panel);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Bundle bundle = getIntent().getExtras();
        equip_name = bundle.getString("message");
        shift_spec = bundle.getString("specshift");
        date= shift_spec.substring(0,8);
        lognum=shift_spec.substring(26,27);
        eqdbname = equip_name +"aclist";
        dbname = equip_name +"_" + date +"_"+ "logentry" +lognum;


        Cursor cursor = db.rawQuery("SELECT * FROM " + eqdbname , null);
        String[] activities = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            String unames = cursor.getString(cursor.getColumnIndex("ACTIVITY"));
            activities[i] = unames;
            i++;
        }
        cursor.close();
        db.close();

//initialization end
        //Gets Grid View from activity_activity_panel.xml
        activityView = (GridView) findViewById(R.id.activity_view);

        //Create and Adapter for activityView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,activities){
        };

        //Sets adapter to the Adpater of activity View
        activityView.setAdapter(adapter);


        //Function when an item in the activityView is selected
        activityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Time in Milli seconds
                Long ts = System.currentTimeMillis();
                String activity_time_str = ts.toString();

                //Activity_name
                String activity_name = (String) ((TextView) view).getText();


                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " +eqdbname+ " WHERE ACTIVITY = '" + activity_name +"'", null);
                cursor.moveToFirst();
                String activity_type = cursor.getString(cursor.getColumnIndex("TYPE"));

                ContentValues values = new ContentValues();
                values.put("ACTIVITY", activity_name);
                values.put("TIME",activity_time_str);
                values.put("TYPE",activity_type);
                long newRowId;
                newRowId = db.insert(
                        dbname,
                        null,
                        values);


                cursor.close();
                db.close();

            }
        });
    }
    public void add_activity_new(View s) {

        final Dialog dialog = new Dialog(ActivityPanel.this);
        dialog.setContentView(R.layout.dialog_activity_add);
        Spinner spinner = (Spinner)dialog.findViewById(R.id.ac_type);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.actype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view,
                                                                         int position, long id) {
                                                  Log.v("item", (String) parent.getItemAtPosition(position));
                                                  eqypenew= parent.getSelectedItem().toString();
                                                  eqypenew = eqypenew.substring(0,1);
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {

                                              }
                                          });

        Button button = (Button) dialog.findViewById(R.id.dialog_okac);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit = (EditText) dialog.findViewById(R.id.new_ac_name);
                String text = edit.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    edit.setError("This field cannot be empty.");
                    return;
                }

                Toast.makeText(ActivityPanel.this, "Created Activity " + text, Toast.LENGTH_SHORT).show();
                activitylist.add(text);

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                dialog.dismiss();

                ContentValues values = new ContentValues();
                values.put("ACTIVITY", text);
                values.put("TYPE", eqypenew);
                long newRowId;
                newRowId = db.insert(
                        eqdbname,
                        null,
                        values);

                db.close();
                finish();
                startActivity(getIntent());

          }
        });
        Button button1 = (Button) dialog.findViewById(R.id.dialog_cancelac);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        dialog.show();


    }
}