package com.thriftyApp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TakeActivity extends AppCompatActivity {

    EditText take, tag;
    DatabaseHelper databaseHelper;
    FloatingActionButton addIncome;
    TextView thrifty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_take);

        take =  findViewById (R.id.takeEditText);
        tag =  findViewById (R.id.tagEditTextTake);
        databaseHelper = new DatabaseHelper (this);
        thrifty = findViewById (R.id.thriftyTitleTake);

        addIncome = findViewById (R.id.floatingActionButtonTake);
        findViewById(R.id.close_take).setOnClickListener(
                new View.OnClickListener () {

                    @Override
                    public void onClick(View arg0) {
                            onBackPressed ();
                    }
                });

        final TextView dateTextView = (TextView) findViewById (R.id.dateTextViewTake);
        String date = new SimpleDateFormat ("MMM dd", Locale.getDefault()).format(new Date ());
        dateTextView.setText (date + ", Income");

        addIncome.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (take.getText ().toString ().equals ("") || tag.getText ().toString ().equals ("")) {
                    Toast.makeText (getApplicationContext (),"Enter valid amount and tag.",Toast.LENGTH_SHORT).show ();
                }
                else
                addTake();
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

    }
    public void addTake () {
        Transactions t = new Transactions ();
        t.setExin (1);
        t.setAmount (Long.parseLong (take.getText ().toString ()));
        t.setTag (tag.getText ().toString ());
        t.setUid (Integer.parseInt (Utils.userId));
        databaseHelper.insertTransaction (t);
        Toast.makeText (getApplicationContext (),"Added Income", Toast.LENGTH_SHORT).show ();
        databaseHelper.getTransactions ();
        Intent intent = new Intent (getApplicationContext (), TransactionsActivity.class);
        startActivity (intent);
        finish ();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (getApplicationContext (),TransactionsActivity.class);
        startActivity (intent);
        finish ();
    }
}