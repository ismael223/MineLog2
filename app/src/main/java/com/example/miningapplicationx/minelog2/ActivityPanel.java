package com.example.miningapplicationx.minelog2;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Color.parseColor;

public class ActivityPanel extends AppCompatActivity {
    GridView activityView;

    static final String[] activities = new String[]{
            "Shovelling","Travelling","Downtime","Standby","End Shift"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_panel);

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
                //Creates a toast when item in the gridview/activity is selected
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
