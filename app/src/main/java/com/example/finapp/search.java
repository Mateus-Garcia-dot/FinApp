package com.example.finapp;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class search extends ListActivity {

    private final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();;
    SQLiteDatabase db;
    private EditText startDate;
    private EditText endDate;
    private Spinner filters;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        startDate = findViewById(R.id.editTextDataInico);
        startDate.addTextChangedListener(new DateInputMask());
        endDate = findViewById(R.id.editTextDataFim);
        endDate.addTextChangedListener(new DateInputMask());
        filters = findViewById(R.id.spinnerFilter);

        ArrayAdapter<String> spinnerFilterAdapterArray = new ArrayAdapter<>(this,   R.layout.custom_spinner);
        spinnerFilterAdapterArray.add("Todas as operações");
        spinnerFilterAdapterArray.add("Débito");
        spinnerFilterAdapterArray.add("Crédito");
        spinnerFilterAdapterArray.setDropDownViewResource(R.layout.custom_dropdown);
        filters.setAdapter(spinnerFilterAdapterArray);

        db = new DatabaseHandler(this).getReadableDatabase();
         adapter = new SimpleAdapter(
                this,
                list,
                R.layout.row_layout,
                new String[] {"valor","data","tipo","filtro"},
                new int[] {R.id.FirstText,R.id.SecondText, R.id.ThirdText, R.id.FourthText }
        );

        setListAdapter(adapter);
    }


    public void searchDateFilter(View v){
        if (startDate.getText().toString().matches("") | endDate.getText().toString().matches("")) {
            Toast.makeText(this, "Data está vazia", Toast.LENGTH_SHORT).show();
            return;
        }

        Date startDateParsed;
        Date endDateParsed;
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt","BR"));
            df.setLenient(false);
            startDateParsed = df.parse(startDate.getText().toString());
            endDateParsed = df.parse(endDate.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Data invalida", Toast.LENGTH_SHORT).show();
            return;
        }

        String filterSearch = "";
        if (filters.getSelectedItem().toString().equals("Débito")) {
            filterSearch = "AND " + DatabaseHandler.c3 + "='debito'";
        };
        if (filters.getSelectedItem().toString().equals("Crédito")) {
            filterSearch = "AND " + DatabaseHandler.c3 + "='credito'";
        };

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE %s>=%d AND %s<=%d %s ORDER BY %s DESC",
                DatabaseHandler.TABLE_NAME,
                DatabaseHandler.c2,
                startDateParsed.getTime(),
                DatabaseHandler.c2,
                endDateParsed.getTime(),
                filterSearch,
                DatabaseHandler.c2),null);


        list.clear();
        HashMap<String,String> header = new HashMap<String,String>();
        header.put("valor","VALOR");
        header.put("data", "DATA");
        header.put("tipo", "TIPO");
        header.put("filtro", "FILTRO");
        list.add(header);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Float valor = cursor.getFloat(cursor.getColumnIndex(DatabaseHandler.c1));
                Long dataMili = cursor.getLong(cursor.getColumnIndex(DatabaseHandler.c2));
                Date dateParsed = new Date(dataMili);
                String tipo = cursor.getString(cursor.getColumnIndex(DatabaseHandler.c3));
                String filtro = cursor.getString(cursor.getColumnIndex(DatabaseHandler.c4));

                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("valor", valor.toString());
                temp.put("data", new SimpleDateFormat("dd/MM/yyyy").format(dateParsed));
                temp.put("tipo", tipo);
                temp.put("filtro", filtro);
                list.add(temp);
                adapter.notifyDataSetChanged();
                cursor.moveToNext();
            }
        }
    }
}
