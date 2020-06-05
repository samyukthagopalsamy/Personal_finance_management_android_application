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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    boolean flagFloatingButton;
    FloatingActionButton floatingActionButton;
    Button scan, take, pay;
    PieChart pieChart;
    TextView income, expense;
    TextView userName, logout;

    public void FloatingButtonToggle(View view) {

        if (flagFloatingButton) {

            flagFloatingButton = false;
            scan.setVisibility (View.INVISIBLE);
            take.setVisibility (View.INVISIBLE);
            pay.setVisibility (View.INVISIBLE);
        }
        else {

            flagFloatingButton = true;
            scan.setVisibility (View.VISIBLE);
            take.setVisibility (View.VISIBLE);
            pay.setVisibility (View.VISIBLE);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_dashboard);

        income =  findViewById (R.id.income);
        expense = findViewById (R.id.expense);
        logout = findViewById (R.id.logoutTextView);

        scan = findViewById (R.id.scanButton);
        take = findViewById (R.id.takeButton);
        pay = findViewById (R.id.payButton);
        scan.setVisibility (View.INVISIBLE);
        take.setVisibility (View.INVISIBLE);
        pay.setVisibility (View.INVISIBLE);
        pieChart =  findViewById (R.id.pieChart);

        databaseHelper = new DatabaseHelper (this);
        flagFloatingButton =false;
        floatingActionButton = findViewById (R.id.floatingActionButtonD);

        databaseHelper.setIncomeExpenses ();

        income.setText ("₹ " + Utils.income);
        expense.setText ("₹ " + Utils.expense);

        scan.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), scanActivity.class);
                startActivity (intent);
                finish ();

            }
        });

        take.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), TakeActivity.class);
                startActivity (intent);
                finish ();
            }
        });

        logout.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext (), MainActivity.class);
                startActivity (intent);
                finish ();
            }
        });

        pay.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), PayActivity.class);
                startActivity (intent);
                finish ();
            }
        });

        TextView showAll =  findViewById (R.id.showAllTextView);
        showAll.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), TransactionsActivity.class);
                startActivity (intent);
                finish ();

            }
        });

        TextView alert =  findViewById (R.id.alertTextView);
        alert.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext (), AlertsActivity.class);
                startActivity (intent);
                finish ();
            }
        });

        getTList();
        getTChart();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);
        finish();
    }

    public void getTList() {
        ListView myListView = findViewById(R.id.transactionsListDash);

        ArrayList<String> transact = databaseHelper.getTransactions ();
        if (transact.size ( ) == 0) {
            Toast.makeText (Dashboard.this, "No transactions yet.", Toast.LENGTH_LONG).show ( );

        }

        else {
            List<String> transactMini = new ArrayList<> ( );
            transactMini.add (transact.get (0));
            if (transact.size () >= 2)
            transactMini.add (transact.get (1));

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, transactMini);

                myListView.setAdapter (arrayAdapter);

                myListView.setOnItemClickListener (new AdapterView.OnItemClickListener ( ) {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Toast.makeText (Dashboard.this, "Tap *Show All* to view all transactions.", Toast.LENGTH_LONG).show ( );
                    }
                });

        }
    }

    public void getTChart() {

        List<PieEntry> value = new ArrayList<> ( );
        if (Utils.expense != 0 && Utils.income !=0) {

            pieChart.setHoleRadius (15f);
            pieChart.setTransparentCircleRadius (15f);

            value.add (new PieEntry (Utils.expense, "Expenses"));
            value.add (new PieEntry (Utils.income, "Income"));

            PieDataSet pieDataSet = new PieDataSet (value, "Transactions");
            PieData pieData = new PieData (pieDataSet);
            pieChart.setData (pieData);

            pieChart.setContentDescription (null);
            pieChart.getLegend ( ).setEnabled (false);
            pieChart.getDescription ( ).setEnabled (false);
            pieChart.setEntryLabelTextSize (10f);
            pieChart.animateXY (1000, 1000);
            pieDataSet.setColors (ColorTemplate.PASTEL_COLORS);
        }
    }
}