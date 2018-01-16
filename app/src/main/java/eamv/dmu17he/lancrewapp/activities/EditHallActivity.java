package eamv.dmu17he.lancrewapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.model.Controller;

public class EditHallActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String hallName = "";
    TextView hallNameTextView;
    TextView rowsTextView;
    TextView columnsTextView;
    Spinner spinner;
    ArrayList<String> hallNames;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hall);
        hallNameTextView = findViewById(R.id.edithallname);
        rowsTextView = findViewById(R.id.editnumberofrows);
        columnsTextView = findViewById(R.id.editnumberofcolumns);

        hallNameList();
        editHall();

    }

    private void hallNameList () {
        spinner = findViewById(R.id.hallnamelist);

        hallNames = Controller.getInstance().getHallNames();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hallNames);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);

    }

    private void editHall() {
        Button saveChanges = findViewById(R.id.savechangesbutton);
        saveChanges.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               
                Controller.getInstance().editHall(hallName, hallNameTextView.getText().toString(), Integer.parseInt(columnsTextView.getText().toString()),
                        Integer.parseInt(rowsTextView.getText().toString()));
                hallNames = Controller.getInstance().getHallNames();
                adapter = new ArrayAdapter<String>(EditHallActivity.this, android.R.layout.simple_spinner_dropdown_item, hallNames);
                spinner.setAdapter(adapter);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        hallName = adapterView.getItemAtPosition(i).toString();
        hallNameTextView.setText(hallName);
        ArrayList<Integer> list = Controller.getInstance().getRowsAndColumnsByHallName(hallName);
        rowsTextView.setText(list.get(0).toString());
        columnsTextView.setText(list.get(1).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
