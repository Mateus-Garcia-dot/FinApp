package com.example.finapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class statementList extends ListActivity {

    private final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();;
    private TextView creditoTotal;
    private TextView debitoTotal;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_list);
        db = new DatabaseHandler(this).getReadableDatabase();

        creditoTotal = findViewById(R.id.totalCredit);
        debitoTotal = findViewById(R.id.totalDebit);

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                list,
                R.layout.row_layout,
                new String[] {"valor","data","tipo","filtro"},
                new int[] {R.id.FirstText,R.id.SecondText, R.id.ThirdText, R.id.FourthText }
        );

        HashMap<String,String> header = new HashMap<String,String>();
        header.put("valor","VALOR");
        header.put("data", "DATA");
        header.put("tipo", "TIPO");
        header.put("filtro", "FILTRO");
        list.add(header);

        Cursor cursor = db.rawQuery("select * from " + DatabaseHandler.TABLE_NAME + " ORDER BY " + DatabaseHandler.c2 + " DESC LIMIT 15" ,null);
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

                cursor.moveToNext();
            }
        }
        cursor.close();
        setListAdapter(adapter);

        Cursor creditoTotalCursor = db.rawQuery("SELECT SUM(" + DatabaseHandler.c1 + ") as total FROM " + DatabaseHandler.TABLE_NAME+ " WHERE " + DatabaseHandler.c3 + "= 'credito'", null);
        if (creditoTotalCursor.moveToFirst()) {
            creditoTotal.setText(String.valueOf(creditoTotalCursor.getFloat(creditoTotalCursor.getColumnIndex("total"))));
        }
        creditoTotalCursor.close();

        Cursor debitoTotalCursor = db.rawQuery("SELECT SUM(" + DatabaseHandler.c1 + ") as total FROM " + DatabaseHandler.TABLE_NAME+ " WHERE " + DatabaseHandler.c3 + "= 'debito'", null);
        if (debitoTotalCursor.moveToFirst()) {
            debitoTotal.setText(String.valueOf(debitoTotalCursor.getFloat(debitoTotalCursor.getColumnIndex("total"))));
        }
        debitoTotalCursor.close();
    }
}