package eamv.dmu17he.lancrewapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.model.Controller;

public class DeleteHallActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String hallName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_hall);
        selectHallToDeleteByName();
        deleteHall();
    }

    private void deleteHall(){
        Button deleteAccept = findViewById(R.id.deleteaccept);
        deleteAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.getInstance().deleteHall(hallName);
            }
        });
    }

    private void selectHallToDeleteByName() {
        Spinner spinnerDeleteHall = (Spinner) findViewById(R.id.spinnerdeletehall);
        Controller.getInstance().createHall("bob", 3,4);
        Controller.getInstance().createHall("fgfd", 3,4);
        Controller.getInstance().createHall("beef", 3,4);
        Controller.getInstance().createHall("beeflee", 3,4);

        ArrayList<String> hallNames = Controller.getInstance().getHallNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,hallNames );
        spinnerDeleteHall.setOnItemSelectedListener(this);
        for(String s : hallNames)
            Log.d("shit", s);

        spinnerDeleteHall.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d("yes", "you know da wey");
        hallName = parent.getItemAtPosition(pos).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
