package eamv.dmu17he.lancrewapp.activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.model.Controller;

public class CreateHallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hall);

        Button createbtn = findViewById(R.id.createbutton);
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText hallname = findViewById(R.id.hallName);
                final String hallName = hallname.getText().toString();

                EditText numberofrows = findViewById(R.id.numberOfRows);
                final int numberOfRows = Integer.parseInt(numberofrows.getText().toString());

                EditText numberofcolumns = findViewById(R.id.numberOfColumns);
                final int numberOfColumns = Integer.parseInt(numberofcolumns.getText().toString());

                Controller.getInstance().createHall(hallName, numberOfColumns, numberOfRows);

                Intent intent = new Intent(CreateHallActivity.this, AdministrateActivity.class);
                startActivity(intent);
            }
        });
    }
}
