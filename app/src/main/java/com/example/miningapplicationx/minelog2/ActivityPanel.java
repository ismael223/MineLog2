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

import java.util.ArrayList;

import static android.graphics.Color.parseColor;

public class ActivityPanel extends AppCompatActivity {
    GridView activityView;

    public final DBHelper dbHelper = new DBHelper(ActivityPanel.this);
    public ArrayList<String> activitylist = new ArrayList<>();
    public static String eqdbname;
    public static String eqypenew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_panel);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Bundle bundle = getIntent().getExtras();
        String equip_name = bundle.getString("message");
        eqdbname = equip_name +"aclist";


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
                //TODO: Add functions here for activity recording

            }
        });
    }
    public void add_activity_new(View s) {

        final Dialog dialog = new Dialog(ActivityPanel.this);
        dialog.setContentView(R.layout.dialog_activity_add);
        Spinner spinner = (Spinner)dialog.findViewById(R.id.ac_type);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.actype_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view,
                                                                         int position, long id) {
                                                  Log.v("item", (String) parent.getItemAtPosition(position));
                                                  eqypenew= parent.getSelectedItem().toString();
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
/////////////////////
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