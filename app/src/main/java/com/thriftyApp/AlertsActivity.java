package com.thriftyApp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class AlertsActivity extends AppCompatActivity {

    boolean flagFloatingButton;
    FloatingActionButton floatingActionButton;
    Button reminderAdd, budgetAdd;
    ListView myListView;
    DatabaseHelper databaseHelper;
    TextView budgetDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_alerts);
        databaseHelper = new DatabaseHelper (this);
        flagFloatingButton = false;
        floatingActionButton =  findViewById (R.id.floatingActionButtonA);
        budgetDetails =  findViewById (R.id.budgetDetailsTextView);
        budgetDetails.setText ("Current Budget : "+ Utils.budget);

        reminderAdd = findViewById (R.id.remAddButton);
        budgetAdd = findViewById (R.id.budgetAddButton);

        TextView home = findViewById (R.id.homeTextViewA);
        TextView thrifty =  findViewById (R.id.thriftyTitleA);

        myListView = findViewById (R.id.alertsList);
        setList ();
        reminderAdd.setVisibility (View.INVISIBLE);
        budgetAdd.setVisibility (View.INVISIBLE);

        reminderAdd.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), AddReminderActivity.class);
                startActivity (intent);
                finish ();
            }
        });

        budgetAdd.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), AddBudgetActivity.class);
                startActivity (intent);
                finish ();
            }
        });

        home.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext (), Dashboard.class);
                startActivity (intent);
                finish ();
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



/*
        final ArrayList<String> myFriends = new ArrayList<String>(asList("Varsha","Samyuktha","Tejaswini","Sivakami","Ashu","Atsh", "Dhak", "Bhava","Prash","Kavin"));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myFriends);

        myListView.setAdapter(arrayAdapter);
        */
    }


    public void FloatingButtonToggle(View view) {

        if (flagFloatingButton) {

            flagFloatingButton = false;
            reminderAdd.setVisibility (View.INVISIBLE);
            budgetAdd.setVisibility (View.INVISIBLE);
        }
        else {

            flagFloatingButton = true;
            reminderAdd.setVisibility (View.VISIBLE);
            budgetAdd.setVisibility (View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (getApplicationContext (),Dashboard.class);
        startActivity (intent);
        finish ();
    }

    public void setList() {

        ArrayList<String> reminder = databaseHelper.getReminders ();
        if (reminder.size () == 0) {
            Toast.makeText (AlertsActivity.this, "No reminders yet.", Toast.LENGTH_LONG).show ( );

        } else {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, reminder);

            myListView.setAdapter (arrayAdapter);

            final ArrayList<String> finalTransact = reminder;
            myListView.setOnItemClickListener (new AdapterView.OnItemClickListener ( ) {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText (AlertsActivity.this, "Reminder Alert : " + finalTransact.get (position), Toast.LENGTH_LONG).show ( );
                }
            });
        }
    }
}
