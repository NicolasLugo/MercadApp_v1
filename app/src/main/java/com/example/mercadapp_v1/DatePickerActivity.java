package com.example.mercadapp_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DatePickerActivity extends AppCompatActivity {
    private DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        datePicker = findViewById(R.id.datePicker);
        Button seleccionarFecha = findViewById(R.id.seleccionarFecha);

        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.YEAR, -5);

        datePicker.setMaxDate(calendario.getTimeInMillis());

        seleccionarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();

                String formattedDay = String.format("%02d", day);
                String formattedMonth = String.format("%02d", month);

                String fechaSeleccionada = formattedDay + "/" + formattedMonth + "/" + year;

                Intent resultIntent = new Intent();
                resultIntent.putExtra("fechaSeleccionada", fechaSeleccionada);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}