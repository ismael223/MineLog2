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
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
/*MAIN FUNCTION*/
public class MainActivity extends AppCompatActivity {
    public static String newname;
    public static String newtype;
    public final DBHelper dbHelper = new DBHelper(MainActivity.this);

    public static String user;
    public static String pass;
    public static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        type =bundle.getString("type");
        setContentView(R.layout.activity_main);
        Button button_add = (Button) findViewById(R.id.add_new_eq);
        if (type.equals("engineer")){
            button_add.setEnabled(true);
        }else{
            button_add.setEnabled(false);
        }

        LinearLayout myLayout = (LinearLayout) findViewById(R.id.equipment_list);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        setTitle("Mine Log (" + user +" is logged in)");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

/*SQL EXTRACT*/
        Cursor cursor = db.rawQuery("SELECT * FROM EQUIPMENTLOG", null);
        String[] equipments = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            String uname = cursor.getString(cursor.getColumnIndex("EQUIPMENTNAME"));
            equipments[i] = uname;
            i++;
        }
        cursor.close();
//END SQL EXTRACT
        int size = equipments.length;
        final TextView[] equipment = new TextView[size];
        for (int a = 0; a < (size); a++) {
            equipment[a] = new TextView(this);
            equipment[a].setTextSize(30);
            equipment[a].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            equipment[a].setLayoutParams(lp);
            equipment[a].setId(a);
            final int b = a;
            equipment[a].setBackground(getResources().getDrawable(R.drawable.back));
            equipment[a].setText(equipments[a]);
            equipment[a].setTextColor(getResources().getColor(R.color.textcolor));
            equipment[a].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shiftlog = new Intent(getApplicationContext(), Shift_log.class);
                    shiftlog.putExtra("message", equipment[b].getText().toString());
                    shiftlog.putExtra("user",user);
                    shiftlog.putExtra("pass",pass);
                    shiftlog.putExtra("type",type);
                    startActivity(shiftlog);
                }
            });
            myLayout.addView(equipment[a]);
            registerForContextMenu(equipment[a]);
        }

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
                        MainActivity.this);

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
    /*END MAIN FUNCTION*/

    /* DIALOGS*/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Menu");
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId()+500, 0, "Delete");
        if (type.equals("engineer")){
            menu.getItem(0).setEnabled(true);
        }else{
            menu.getItem(0).setEnabled(false);
        }
        if (type.equals("engineer")){
            menu.getItem(1).setEnabled(true);
        }else{
            menu.getItem(1).setEnabled(false);
        }
    }


    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Edit")) {
            function1(item.getItemId());
        } else if (item.getTitle().equals("Delete")) {
            function2(item.getItemId());
        } else {
            return false;
        }
        return true;
    }
//EDIT EQ
    public void function1(int id) {
        final Dialog dialog1 = new Dialog(MainActivity.this);

        dialog1.setContentView(R.layout.dialog_edit);
        TextView toldv = (TextView) this.findViewById(id);
        final String myoldString = toldv.getText().toString();


        final int thisid = id;
        Button button = (Button) dialog1.findViewById(R.id.dialog_okedit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                EditText edit = (EditText) dialog1.findViewById(R.id.username);
                String text = edit.getText().toString();


                if (TextUtils.isEmpty(text)) {
                    edit.setError("This field cannot be empty");
                    return;
                }else if(Character.isDigit(text.charAt(0))) {
                    edit.setError("Equipment Name cannot start with a number");
                    return;
                }

                ContentValues values = new ContentValues();
                values.put(DBContract.Table1.COLUMN_NAME_COL1,  text);

                String selection = DBContract.Table1._ID + " LIKE ?";
                String[] selectionArgs = {String.valueOf(thisid + 1)};

                int count = db.update(
                        DBContract.Table1.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                db.beginTransaction();
                try {
                    db.execSQL("ALTER TABLE " + myoldString + " RENAME TO " + text + ";");
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                dbHelper.AddDesiredTable(text);
                dbHelper.AddActivityList(text+"aclist" );

                db.close();

                finish();
                startActivity(getIntent());
            }

        });
        Button button1 = (Button) dialog1.findViewById(R.id.dialog_canceledit);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
        dialog1.show();

    }
//End Edit Eq

//Delete Equipment
    public void function2(int id) {
        TextView tv = (TextView) this.findViewById(id-500);
        final String myString = tv.getText().toString();
        final Dialog dialog1 = new Dialog(MainActivity.this);
        dialog1.setContentView(R.layout.dialog_delete_auth);

        Button button = (Button) dialog1.findViewById(R.id.dialog_ok_delete);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                EditText edit = (EditText) dialog1.findViewById(R.id.pass_auth);
                String text = edit.getText().toString();

                if (TextUtils.isEmpty(text)) {
                    edit.setError("This field cannot be empty");
                    return;
                }

                if (text.equals(pass)){
                    db.delete("EQUIPMENTLOG", "EQUIPMENTNAME=? ", new String[]{myString});
                    finish();
                    startActivity(getIntent());
                }else{
                    edit.setError("Incorrect Password");
                    return;
                }
                db.close();

            }

        });
        Button button1 = (Button) dialog1.findViewById(R.id.dialog_cancel_pass_auth);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
        dialog1.show();
    }
//End Delete Equipment
//Add Equipment
    public void function5(View s) {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_equipment_add);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.neq_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.eqtype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String text_type =  parent.getItemAtPosition(position).toString();
                newtype=text_type;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String text_type = parent.getSelectedItem().toString();
                newtype=text_type;


            }
        });


        Button button = (Button) dialog.findViewById(R.id.dialog_ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit = (EditText) dialog.findViewById(R.id.new_eq_name);
                String text = edit.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    edit.setError("This field cannot be empty.");
                    return;
                }else if(Character.isDigit(text.charAt(0))) {
                    edit.setError("Equipment Name cannot start with a number");
                    return;
                }


                SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                dbHelper.AddDesiredTable(text);
                dbHelper.AddActivityList(text+"aclist");
                db1.close();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                dialog.dismiss();
                newname = text;
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

                Toast.makeText(MainActivity.this, "Created Equipment " + newname, Toast.LENGTH_SHORT).show();

            }
        });
        Button button1 = (Button) dialog.findViewById(R.id.dialog_cancel);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        dialog.show();

    }
//End Edit Equipment
    /*END DIALOGS*/
public void onBackPressed() {

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
            MainActivity.this);

    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent login_page= new Intent(getApplicationContext(), LoginActivity.class);
            finish();
            startActivity(login_page);
        }
    });

    alertDialog.setNegativeButton("No", null);

    alertDialog.setMessage("Do you want to exit?\nYour account will be Logged Out.");
    alertDialog.setTitle("Exit");
    alertDialog.show();
}
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

}


