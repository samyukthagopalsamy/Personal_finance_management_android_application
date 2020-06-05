package com.thriftyApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class PayActivity extends AppCompatActivity {

    EditText pay, tag;
    FloatingActionButton addExpense;
    DatabaseHelper databaseHelper;
    TextView thrifty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_pay);


        databaseHelper = new DatabaseHelper (this);
        pay = findViewById (R.id.payEditText);
        tag = findViewById (R.id.tagEditText);
        addExpense =  findViewById (R.id.floatingActionButtonPay);

        thrifty = findViewById (R.id.thriftyTitlePay);
        findViewById(R.id.close_pay).setOnClickListener(
                new View.OnClickListener () {

                    @Override
                    public void onClick(View arg0) {

                        onBackPressed ();

                    }
                });

        Intent intent = getIntent ();
        if (intent.getStringExtra ("ocr") != null) {
            String str = intent.getStringExtra ("ocr");
            pay.setText (str);
        }

        TextView dateTextView =  findViewById (R.id.dateTextViewPay);
        String date = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date());
        dateTextView.setText (date + ", Expense");

        addExpense.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (pay.getText ().toString ().equals ("") || tag.getText ().toString ().equals ("")) {
                    Toast.makeText (getApplicationContext (),"Enter valid amount and tag.",Toast.LENGTH_SHORT).show ();
                }
                else
                    addPay ();
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


        findViewById (R.id.transportImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Transport");
            }
        });
        findViewById (R.id.travelImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Travel");
            }
        });
        findViewById (R.id.foodImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Food");
            }
        });
        findViewById (R.id.billsImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Bills");
            }
        });
        findViewById (R.id.sportsImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Sports");
            }
        });
        findViewById (R.id.homeImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Home");
            }
        });
        findViewById (R.id.petsImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Pets");
            }
        });
        findViewById (R.id.educationImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Education");
            }
        });
        findViewById (R.id.beautyImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Beauty");
            }
        });
        findViewById (R.id.kidsImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Kids");
            }
        });
        findViewById (R.id.healthImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Health Care");
            }
        });
        findViewById (R.id.movieImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Movies");
            }
        });
    }

    public void addPay () {
        Transactions t = new Transactions ();
        t.setExin (0);
        Double d = Double.parseDouble (pay.getText ().toString ());
        Log.i("Omg", d.toString ());
        long l = Math.round (d);
        t.setAmount (l);
        t.setTag (tag.getText ().toString ());
        t.setUid (Integer.parseInt (Utils.userId));
        databaseHelper.insertTransaction (t);
        databaseHelper.getTransactions ();
        databaseHelper.setIncomeExpenses ();
        int exp = Utils.expense;
        int bud = Integer.parseInt (Utils.budget);
        Log.i("Alert build", bud*0.5 +" "+ exp);
        if (exp > (bud/2)) {
            Toast.makeText (getApplicationContext (),"Added Expense", Toast.LENGTH_SHORT).show ();
            buildAlertExpense ( );
        }
        else {
            Intent intent = new Intent (getApplicationContext ( ), TransactionsActivity.class);
            startActivity (intent);
            finish ( );
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (getApplicationContext (),TransactionsActivity.class);
        startActivity (intent);
        finish ();
    }


    public void buildAlertExpense () {


            AlertDialog.Builder builder = new AlertDialog.Builder (this);
            builder.setCancelable (true);
            builder.setTitle ("Alert");
            builder.setMessage ("Spend money wisely. More than half of the allocated budget amount has already been spent in this month." );
            builder.setPositiveButton ("OK", new DialogInterface.OnClickListener ( ) {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText (getApplicationContext (),"Spend Less, Save more.",Toast.LENGTH_SHORT).show ();
                    Intent intent = new Intent (getApplicationContext (), TransactionsActivity.class);
                    startActivity (intent);
                    finish ();
                }
            });
            builder.show ();

    }
}