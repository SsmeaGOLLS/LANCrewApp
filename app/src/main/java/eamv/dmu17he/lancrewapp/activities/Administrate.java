package eamv.dmu17he.lancrewapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import eamv.dmu17he.lancrewapp.activities.CreateHall;
import eamv.dmu17he.lancrewapp.R;

public class Administrate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrate);

        Button createhall = findViewById(R.id.createhallbutton);
        createhall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Administrate.this, CreateHall.class);
                startActivity(intent);
            }
        });

        Button deleteHall = findViewById(R.id.deletehall);
        deleteHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Administrate.this, DeleteHall.class);
                startActivity(intent);
            }
        });
    }
}
