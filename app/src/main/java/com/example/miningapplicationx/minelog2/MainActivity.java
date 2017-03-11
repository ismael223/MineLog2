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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    public static int tracker;
    public final DBHelper dbHelper = new DBHelper(MainActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout myLayout=(LinearLayout) findViewById(R.id.equipment_list);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

/*SQL EXTRACT*/
        Cursor cursor = db.rawQuery("SELECT * FROM EQUIPMENTLOG", null);
        String[] equipments = new String[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()){
            String uname = cursor.getString(cursor.getColumnIndex("EQUIPMENTNAME"));
            equipments[i] = uname;
            i++;
        }
        cursor.close();
//END SQL EXTRACT
       int size = equipments.length;
        final TextView[] equipment = new TextView[size];
        for (int a=0; a<(size); a++) {
            equipment[a] = new TextView(this);
            equipment[a].setTextSize(30);
            equipment[a].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            equipment[a].setLayoutParams(lp);
            equipment[a].setId(a);
            final int b = a;
            tracker=b;
            equipment[a].setBackground(getResources().getDrawable(R.drawable.back));
            equipment[a].setText(equipments[a]);
            equipment[a].setTextColor(getResources().getColor(R.color.textcolor));
            equipment[a].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent shiftlog = new Intent(getApplicationContext(), Shift_log.class);
                    shiftlog.putExtra("message", equipment[b].getText().toString());
                    startActivity(shiftlog);}
            });
            myLayout.addView(equipment[a]);
            registerForContextMenu(equipment[a]);
            }

    }


/*END MAIN FUNCTION*/


/* DIALOGS*/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Menu");
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("Edit")){function1(item.getItemId());}
        else if(item.getTitle().equals("Delete")){function2(item.getItemId());}
        else {return false;}
        return true;
    }
//Add EQ
    public void function1(int id){

        final Dialog dialog1 = new Dialog(MainActivity.this);

        dialog1.setContentView(R.layout.dialog_edit);
        TextView toldv = (TextView) this.findViewById(id);
        final String myoldString= toldv.getText().toString();


        final int thisid = id;
        Button button = (Button) dialog1.findViewById(R.id.dialog_okedit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                EditText edit=(EditText)dialog1.findViewById(R.id.username);
                String text = edit.getText().toString();

                if(TextUtils.isEmpty(text)) {
                    edit.setError("This field cannot be empty.");
                    return;
                }
                ContentValues values = new ContentValues();
                values.put(DBContract.Table1.COLUMN_NAME_COL1,text );

                String selection = DBContract.Table1._ID + " LIKE ?";
                String[] selectionArgs = { String.valueOf(thisid +1) };

                int count = db.update(
                        DBContract.Table1.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                db.beginTransaction();
                try{
                    db.execSQL("ALTER TABLE " + myoldString  + " RENAME TO " + text+";");
                    db.setTransactionSuccessful();
                } finally{
                    db.endTransaction();
                }

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
//End Add Eq


    public void function2(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String idpass = String.valueOf(id);
        TextView tv = (TextView) this.findViewById(id);
        String myString= tv.getText().toString();
        Toast.makeText(this, "Deleted Equipment " + myString , Toast.LENGTH_SHORT).show();
        db.delete("EQUIPMENTLOG","EQUIPMENTNAME=? ",new String[]{myString});
        db.close();
        finish();
        startActivity(getIntent());
    }

    //ADDEQ
    public void function5(View s){

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_equipment_add);

        Button button = (Button) dialog.findViewById(R.id.dialog_ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit=(EditText)dialog.findViewById(R.id.new_eq_name);
                String text=edit.getText().toString();
                if(TextUtils.isEmpty(text)) {
                    edit.setError("This field cannot be empty.");
                    return;
                }

                EditText edit_type = (EditText)dialog.findViewById(R.id.new_eq_type);
                String text_type=edit_type.getText().toString();
                if(TextUtils.isEmpty(text_type)) {
                    edit_type.setError("This field cannot be empty.");
                    return;
                }

                SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                dbHelper.AddDesiredTable(text);
                Toast.makeText(MainActivity.this, "Created Equipment " + text , Toast.LENGTH_SHORT).show();
                db1.close();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                dialog.dismiss();
                newname=text;
                newtype=text_type;
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

/*END DIALOGS*/
protected void onDestroy() {
    dbHelper.close();
    super.onDestroy();
}


}


