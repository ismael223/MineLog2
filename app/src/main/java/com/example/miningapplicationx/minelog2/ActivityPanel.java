package com.example.miningapplicationx.minelog2;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    public static String user;
    public static String pass;
    public static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_panel);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Bundle bundle = getIntent().getExtras();
        equip_name = bundle.getString("message");
        shift_spec = bundle.getString("specshift");
        Cursor cursor_title = db.rawQuery("SELECT ASSIGNED FROM "+equip_name +" WHERE EQSHIFTMD ='" + shift_spec+"'" , null);
        cursor_title.moveToFirst();
        String assigned_person = cursor_title.getString(0);
        cursor_title.close();

        setTitle("Activity log (assigned to " + assigned_person +")" );
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        type = bundle.getString("type");
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
                if (activity_name.equals("End Shift")){
                    new AlertDialog.Builder(ActivityPanel.this)
                            .setTitle("End Shift")
                            .setMessage("Are you sure you want End Shift? \n You will be redirected to the Shift Log page.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent shift_log = new Intent(getApplicationContext(), Shift_log.class);
                                    shift_log.putExtra("message",equip_name);
                                    shift_log.putExtra("user",user);
                                    shift_log.putExtra("pass",pass);
                                    shift_log.putExtra("type",type);
                                    startActivity(shift_log);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " +eqdbname+ " WHERE ACTIVITY = '" + activity_name +"'", null);
                cursor.moveToFirst();
                String activity_type = cursor.getString(cursor.getColumnIndex("TYPE"));

                ContentValues values = new ContentValues();
                values.put("ACTIVITY", activity_name);
                values.put("TIME",activity_time_str);
                values.put("TYPE",activity_type);
                values.put("OPERATOR",user);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        switch (item.getItemId()){
            case R.id.logout:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        ActivityPanel.this);

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login_page= new Intent(getApplicationContext(), LoginActivity.class);
                        login_page.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(login_page);
                    }
                });

                alertDialog.setNegativeButton("No", null);

                alertDialog.setMessage("Do you want to exit?\nYour account will be Logged Out.");
                alertDialog.setTitle("Exit");
                alertDialog.show();
                CharSequence text= "Logout";
                Toast toast = Toast.makeText(context,text,duration);
                toast.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    @Override
    public void onBackPressed() {
        Intent main_act= new Intent(this, Shift_log.class);
        main_act.putExtra("user",user);
        main_act.putExtra("pass",pass);
        main_act.putExtra("type",type);
        main_act.putExtra("message",equip_name);
        startActivity(main_act);
    }
}