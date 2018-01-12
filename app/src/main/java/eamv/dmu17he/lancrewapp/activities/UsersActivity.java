package eamv.dmu17he.lancrewapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.sql.sqLiteDatabase;

public class UsersActivity extends AppCompatActivity {

    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        textViewName = (TextView) findViewById(R.id.text1);
        String nameFromIntent = getIntent().getStringExtra("USERNAME");
        textViewName.setText("Welcome " + nameFromIntent);

        }
    }
