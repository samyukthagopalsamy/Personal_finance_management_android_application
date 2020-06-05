package com.thriftyApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddBudgetActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    EditText budgetEdit;
    TextView thrifty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_budget);
        databaseHelper = new DatabaseHelper (this);
        budgetEdit = findViewById (R.id.budAmountEditText);

        thrifty = findViewById (R.id.thriftyTitleAddBud);
        budgetEdit.setText (Utils.budget);

        findViewById(R.id.close_addbud).setOnClickListener(
                new View.OnClickListener () {

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

        findViewById (R.id.floatingActionButtonAddBud).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if(budgetEdit.getText ().toString ().equals ("") )
                {
                    Toast.makeText (getApplicationContext (),"Enter valid budget amount and end date.",Toast.LENGTH_SHORT).show ();
                }
                else {

                    Utils.budget = budgetEdit.getText ().toString ();
                    databaseHelper.changeBudget ();
                    Toast.makeText (getApplicationContext ( ), "Changed Budget Amount.", Toast.LENGTH_SHORT).show ( );
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
