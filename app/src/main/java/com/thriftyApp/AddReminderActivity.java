package com.thriftyApp;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddReminderActivity extends AppCompatActivity {

    EditText dateText, timeText, message;
    DatabaseHelper databaseHelper;
    TextView thrifty;
    public void addRem() {

        AlertsTable a = new AlertsTable ();
        String dateTime = dateText.getText ().toString () + " " + timeText.getText ().toString ();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault ( ));

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "YYYY-MM-dd HH:mm:ss", Locale.getDefault());
        Date d = new Date ();
        try {
            d = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace ( );
        }
        if(d!=null)
            a.setalert_at (dateFormat.format (d));
        else
            a.setalert_at (dateFormat.format (d));
        a.setMessage (message.getText ().toString ());
        a.setUid (Integer.parseInt (Utils.userId));
        databaseHelper.insertRemainder (a);
        databaseHelper.getReminders();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_reminder);
        dateText = findViewById (R.id.dateRemEditText);
        timeText =  findViewById (R.id.timeRemEditText);
        message = findViewById (R.id.describeReminderEditText);
        thrifty = findViewById (R.id.thriftyTitleAddRem);
        databaseHelper = new DatabaseHelper (this);

        findViewById (R.id.close_addrem).setOnClickListener (
                new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View arg0) {
                        onBackPressed ();
                    }
                });

        thrifty.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext (), Dashboard.class);
                startActivity (intent);
                finish ();
            }
        });


        findViewById (R.id.floatingActionButtonAddRem).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (message.getText ().toString ().equals ("") || dateText.getText ().toString ().equals ("") || timeText.getText ().toString ().equals ("") )
                {
                    Toast.makeText (getApplicationContext (),"Enter valid message, date and time.",Toast.LENGTH_SHORT).show ();
                }
                else {
                    addRem ( );
                    Toast.makeText (getApplicationContext ( ), "Added Reminder", Toast.LENGTH_SHORT).show ( );
                    Intent intent = new Intent (getApplicationContext ( ), AlertsActivity.class);
                    startActivity (intent);
                    finish ( );
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent (getApplicationContext (),AlertsActivity.class);
        startActivity (intent);
        finish ();
    }
}
