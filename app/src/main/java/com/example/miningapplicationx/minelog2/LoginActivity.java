package com.example.miningapplicationx.minelog2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 0;
    public final DBHelper dbHelper = new DBHelper(LoginActivity.this);
    public String acct_type;
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper.UsernameTables();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put("USERNAME", "engineer");
        values1.put("PASSWORD", "mining");
        values1.put("TYPE", "engineer");
        long newRowId;
        newRowId = db.insert(
                "USERNAMETABLES",
                null,
                values1);

        ContentValues values2 = new ContentValues();
        values2.put("USERNAME", "operator");
        values2.put("PASSWORD", "minelog");
        values2.put("TYPE", "operator");
        newRowId = db.insert(
                "USERNAMETABLES",
                null,
                values2);
        db.close();

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mCreateAccount = (Button) findViewById(R.id.create_account);
        mCreateAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                function_add_acc();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void function_add_acc(){
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.dialog_acc_add);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.acc_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.acctype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String text_type =  parent.getItemAtPosition(position).toString();
                acct_type=text_type;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String text_type = parent.getSelectedItem().toString();
                acct_type=text_type;


            }
        });


        Button button = (Button) dialog.findViewById(R.id.dialog_acc_ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit = (EditText) dialog.findViewById(R.id.acc_name);
                String text = edit.getText().toString();
                EditText edit1 = (EditText) dialog.findViewById(R.id.acc_pass);
                String edit_pass = edit1.getText().toString();
                EditText edit2 = (EditText) dialog.findViewById(R.id.acc_pass_rep);
                String edit_pass_rep = edit2.getText().toString();
                EditText edit3 = (EditText) dialog.findViewById(R.id.admin_pass);
                String admin_pw = edit3.getText().toString();

                String admin_pass_default= getResources().getString(R.string.set_admin_pass);
                if (TextUtils.isEmpty(text)) {
                    edit.setError("This field cannot be empty.");
                    return;
                }else if(Character.isDigit(text.charAt(0))) {
                    edit.setError("Account Name cannot start with a number");
                    return;
                }else if(!edit_pass.equals(edit_pass_rep)){
                    edit2.setError("Passwords Do Not Match");
                    return;
                }else if(!admin_pw.equals(admin_pass_default)){
                    edit3.setError("Incorrect Admin Password");
                    return;
                }else if (TextUtils.isEmpty(edit_pass)) {
                    edit1.setError("This field cannot be empty.");
                    return;
                }else if (TextUtils.isEmpty(edit_pass_rep)) {
                    edit2.setError("This field cannot be empty.");
                    return;
                }else if (TextUtils.isEmpty(admin_pw)) {
                    edit3.setError("This field cannot be empty.");
                    return;
                }else if (edit_pass.length()<=4){
                    edit1.setError(getString(R.string.error_invalid_password));
                    return;
                }


                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String sql = "INSERT INTO 'USERNAMETABLES'(USERNAME, PASSWORD, TYPE) "
                        + "VALUES (?, ?, ?)";
                try {
                    db.execSQL(sql, new String[]{text, edit_pass, acct_type});
                }catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Duplicates error username already exists", Toast.LENGTH_SHORT).show();

                }
                finish();
                startActivity(getIntent());
                Cursor cursor5 = db.rawQuery("SELECT * FROM 'USERNAMETABLES' WHERE USERNAME = '" + text +"'", null);
                if(cursor5.moveToFirst()){
                String name_acc = cursor5.getString(cursor5.getColumnIndex("USERNAME"));
                Toast.makeText(LoginActivity.this, "Created Account " + name_acc, Toast.LENGTH_SHORT).show();}

                cursor5.close();
                dialog.dismiss();
                db.close();
            }
        });
        Button button1 = (Button) dialog.findViewById(R.id.dialog_acc_cancel);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        dialog.show();

    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        //Check if username exists in database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectString = "SELECT * FROM USERNAMETABLES WHERE USERNAME =?" ;
        Cursor cursor = db.rawQuery(selectString, new String[] {email});
        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
        }
        cursor.close();
        db.close();
        if (!hasObject) {
            mEmailView.setError(getString(R.string.error_email_not_in_db));
            focusView = mEmailView;
            cancel = true;
         }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return false;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String selectString = "SELECT * FROM USERNAMETABLES WHERE USERNAME =?" ;
            Cursor cursor = db.rawQuery(selectString, new String[] {mEmail});
            cursor.moveToFirst();
            String pass_check = cursor.getString(cursor.getColumnIndex("PASSWORD"));
            if (!pass_check.equals(mPassword)){
                db.close();
                cursor.close();
                return false;
            }else
            {
                db.close();
                cursor.close();
                return true;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String selectString = "SELECT * FROM USERNAMETABLES WHERE USERNAME =?" ;
                Cursor cursor6 = db.rawQuery(selectString, new String[] {mEmail});
                cursor6.moveToFirst();
                String user_type = cursor6.getString(cursor6.getColumnIndex("TYPE"));
                cursor6.close();
                db.close();

                Intent main_act = new Intent(getApplicationContext(),MainActivity.class);
                main_act.putExtra("user",mEmail);
                main_act.putExtra("pass",mPassword);
                main_act.putExtra("type",user_type);
                finish();
                startActivity(main_act);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                LoginActivity.this);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", null);

        alertDialog.setMessage("Do you want to exit Mine Log?");
        alertDialog.setTitle("Exit");
        alertDialog.show();
    }
}

