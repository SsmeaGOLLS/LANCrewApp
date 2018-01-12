package eamv.dmu17he.lancrewapp.activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import eamv.dmu17he.lancrewapp.R;

public class ActivityWakeUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_Start_wake_up);

        Button bookwakeup = findViewById(R.id.BookWakeUp);
        bookwakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityWakeUp.this, BookAWakeUp.class);
                startActivity(intent);


            }
        });

        Button bookingoverview = findViewById(R.id.BookingsOverview);
        bookingoverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityWakeUp.this, BookingOverview.class);
                startActivity(intent);


            }
        });

        Button administrate = findViewById(R.id.Administrate);
        administrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityWakeUp.this, Administrate.class);
                startActivity(intent);


            }
        });
    }



}
