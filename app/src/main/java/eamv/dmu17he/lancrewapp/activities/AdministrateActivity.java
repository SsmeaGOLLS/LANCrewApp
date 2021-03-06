package eamv.dmu17he.lancrewapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.GlobalUserSingleton;

public class AdministrateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrate);

        Button createhall = findViewById(R.id.createhallbutton);
        createhall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministrateActivity.this, CreateHallActivity.class);
                startActivity(intent);
            }
        });

/*        Button deleteHall = findViewById(R.id.deletehall);
        deleteHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministrateActivity.this, EditOrDeleteHallActivity.class);
                startActivity(intent);
            }
        });*/

        Button editHall = findViewById(R.id.edithallbutton);
        editHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministrateActivity.this, EditOrDeleteHallActivity.class);
                startActivity(intent);
            }
        });

        GlobalUserSingleton.getGlobals(this).theCurrentUser.getIsAdmin();

    }

}
