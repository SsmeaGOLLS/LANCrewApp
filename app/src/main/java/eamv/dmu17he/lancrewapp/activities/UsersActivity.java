package eamv.dmu17he.lancrewapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import eamv.dmu17he.lancrewapp.R;

import eamv.dmu17he.lancrewapp.model.Schedule;

import eamv.dmu17he.lancrewapp.model.User;
import eamv.dmu17he.lancrewapp.sql.sqLiteDatabase;

public class UsersActivity extends AppCompatActivity {

    private TextView textViewName;
    private User user;
    sqLiteDatabase db = sqLiteDatabase.getDatabase(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        getadminuser();

        textViewName = (TextView) findViewById(R.id.text1);
        String nameFromIntent = getIntent().getStringExtra("USERNAME");
        textViewName.setText("Welcome " + nameFromIntent);



        TextView textviewi= (TextView) findViewById(R.id.adminName);
        textviewi.setText("" + user.getName());
        }

    public void getadminuser(){

        sqLiteDatabase db = sqLiteDatabase.getDatabase(this);
        user.setName("daniel cocksucker");
        user.setPhoneNumber(84989846);

        System.out.print(user);
    }


    }





