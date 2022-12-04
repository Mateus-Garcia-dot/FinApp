package com.example.finapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class createOp extends AppCompatActivity {
    EditText valor;
    EditText inputData;
    RadioGroup tipoPagamento;
    Spinner filterSpinner;
    SQLiteDatabase db;
    ArrayAdapter<String> spinnerCreditoAdapterArray;
    ArrayAdapter<String> spinnerDebitoAdapterArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_op);
        db = new DatabaseHandler(this).getWritableDatabase();

        valor = findViewById(R.id.editTextInputValor);
        inputData = findViewById(R.id.editTextInputData);
        inputData.addTextChangedListener(new DateInputMask());
        tipoPagamento = findViewById(R.id.radioGroupTipoPagamento);
        filterSpinner = findViewById(R.id.spinnerInputFilter);

        spinnerCreditoAdapterArray = new ArrayAdapter<>(this,   R.layout.custom_spinner);
        spinnerCreditoAdapterArray.add("Salário");
        spinnerCreditoAdapterArray.add("Outros");
        spinnerCreditoAdapterArray.setDropDownViewResource(R.layout.custom_dropdown);

        spinnerDebitoAdapterArray = new ArrayAdapter<>(this,   R.layout.custom_spinner);
        spinnerDebitoAdapterArray.add("Moradia");
        spinnerDebitoAdapterArray.add("Saúde");
        spinnerDebitoAdapterArray.add("Outros");
        spinnerDebitoAdapterArray.setDropDownViewResource(R.layout.custom_dropdown);

        tipoPagamento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonCredito:
                        filterSpinner.setAdapter(spinnerDebitoAdapterArray);
                        break;
                    case R.id.radioButtonMetodoDebito:
                        filterSpinner.setAdapter(spinnerCreditoAdapterArray);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void save(View v)  {
       if (valor.getText().toString().matches("")) {
           Toast.makeText(this, "Valor está vazio", Toast.LENGTH_SHORT).show();
       }
        if (inputData.getText().toString().matches("")) {
            Toast.makeText(this, "A Data está vazia", Toast.LENGTH_SHORT).show();
        }
        if (tipoPagamento.getCheckedRadioButtonId() == -1 ) {
            Toast.makeText(this, "Selecione a forma de pagamento", Toast.LENGTH_SHORT).show();
        }
        Date dateParsed;
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false);
            dateParsed = df.parse(inputData.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Data invalida", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.c1, Integer.parseInt(valor.getText().toString()));
        values.put(DatabaseHandler.c2, dateParsed.getTime());
        long newRowId = db.insert(DatabaseHandler.TABLE_NAME, null, values);
    }

}