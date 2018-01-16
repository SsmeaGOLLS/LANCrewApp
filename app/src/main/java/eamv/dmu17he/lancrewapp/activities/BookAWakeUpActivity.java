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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.model.Controller;
import eamv.dmu17he.lancrewapp.model.WakeUp;


public class BookAWakeUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ArrayList<String> wakeupss;
    Calendar selectedWakeUpTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_awake_up);

        createTimesForWakeups();
        bookWakeUp();
    }

    private void createTimesForWakeups() {
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();

        wakeupss = new ArrayList<String>();

        for(int i = 0; i< 24; i++){
            calendar.add(Calendar.MINUTE, 30);
            wakeupss.add(calendar.getTime().toString());
        }

        Spinner spinnerWakeUp = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, wakeupss);
        spinnerWakeUp.setOnItemSelectedListener(this);
        spinnerWakeUp.setAdapter(adapter);
    }

    private String getComment(){
        TextView comment = findViewById(R.id.commettextfield);
        return comment.getText().toString();
    }

    private void bookWakeUp(){
        Button acceptWakeUp = findViewById(R.id.bookwakeupaccept);

        acceptWakeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.getInstance().makeWakeUp("find out how to get username", selectedWakeUpTime, getComment());

            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d("gf", parent.getItemAtPosition(pos).toString());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        ;
        try {
            calendar.setTime(ft.parse(parent.getItemAtPosition(pos).toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        selectedWakeUpTime = calendar;

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
