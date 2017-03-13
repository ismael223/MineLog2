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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.graphics.Color.parseColor;

public class ActivityPanel extends AppCompatActivity {
    GridView activityView;

    public final DBHelper dbHelper = new DBHelper(ActivityPanel.this);
    public ArrayList<String> activitylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activitylist.add("End Shift");
        activitylist.add("Maintenance");
        activitylist.add("Downtime");
        activitylist.add("Standby");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_panel);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Bundle bundle = getIntent().getExtras();
        String equip_name = bundle.getString("message");

//initialization start
        //sqlextract
        Cursor cursor = db.rawQuery("SELECT * FROM EQUIPMENTLOG WHERE EQUIPMENTNAME = '" + equip_name +"'", null);
        cursor.moveToFirst();
        String uname = cursor.getString(cursor.getColumnIndex("EQUIPMENTTYPE"));
        cursor.close();
        String truck="truck";String truck1="Truck";String shovel="shovel"; String shovel1="Shovel";
        //sqlextract close
        if (uname.equals(truck)||uname.equals(truck1)){
            activitylist.add("Travelling");
            activitylist.add("Loading");
        }
        else if (uname.equals(shovel)||uname.equals(shovel1))
        {
            activitylist.add("Travelling");
            activitylist.add("Shovelling");
        }
        else{
        }
//initialization end
        //Gets Grid View from activity_activity_panel.xml
        activityView = (GridView) findViewById(R.id.activity_view);

        //Create and Adapter for activityView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,activitylist){
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
    public void add_activity() {

        final Dialog dialog = new Dialog(ActivityPanel.this);
        dialog.setContentView(R.layout.dialog_equipment_add);

        Button button = (Button) dialog.findViewById(R.id.dialog_ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit = (EditText) dialog.findViewById(R.id.new_eq_name);
                String text = edit.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    edit.setError("This field cannot be empty.");
                    return;
                }

                EditText edit_type = (EditText) dialog.findViewById(R.id.new_eq_type);
                String text_type = edit_type.getText().toString();
                if (TextUtils.isEmpty(text_type)) {
                    edit_type.setError("This field cannot be empty.");
                    return;
                }

/*                SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                dbHelper.AddDesiredTable(text);
                Toast.makeText(MainActivity.this, "Created Equipment " +text, Toast.LENGTH_SHORT).show();
                db1.close();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                dialog.dismiss();
                newname = text;
                newtype = text_type;
                ContentValues values = new ContentValues();

                values.put(DBContract.Table1.COLUMN_NAME_COL1, newname);
                values.put(DBContract.Table1.COLUMN_NAME_COL2, newtype);
                long newRowId;
                newRowId = db.insert(
                        DBContract.Table1.TABLE_NAME,
                        null,
                        values);

                finish();
                startActivity(getIntent());
                db.close();
                */
                }
        });
    }
}
