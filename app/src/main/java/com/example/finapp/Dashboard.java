package com.example.finapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void create(View v)  {
        Intent i = new Intent(Dashboard.this, createOp.class);
        startActivity(i);
    }

    public void statement(View v)  {
        Intent i = new Intent(Dashboard.this, statementList.class);
        startActivity(i);
    }

    public void search(View v)  {
    }

    public void orderedList(View v)  {
        Intent i = new Intent(Dashboard.this, orderedList.class);
        startActivity(i);
    }

    void exit(View v)  {
        finish();
    }

}