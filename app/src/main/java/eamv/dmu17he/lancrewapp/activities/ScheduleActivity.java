package eamv.dmu17he.lancrewapp.activities;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.model.Schedule;
import eamv.dmu17he.lancrewapp.sql.scheduleDAO;
import eamv.dmu17he.lancrewapp.sql.sqLiteDatabase;
import eamv.dmu17he.lancrewapp.sql.userDAO;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Button insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                postDataToSDAO();
            }
        });

    }

    public  void postDataToSDAO(){

        Schedule schedule = new Schedule();
        schedule.setGaName("Glenn Mortensen");
        schedule.setDate("2018-01-12");
        schedule.setStartTime("09:00:00");
        schedule.setEndTime("13:00:00");
        schedule.setName("Daniel C Sucker");
        schedule.setTitle("Opvasker i køkken");

        Schedule schedule1 = new Schedule();
        schedule.setGaName("Glenn Mortensen");
        schedule.setDate("2018-01-12");
        schedule.setStartTime("10:00:00");
        schedule.setEndTime("13:00:00");
        schedule.setName("Mikkel VHN");
        schedule.setTitle("KøkkenChef");

        sqLiteDatabase db = sqLiteDatabase.getDatabase(this);
        db.sDAO().insertSchedule(schedule);
        db.sDAO().insertSchedule(schedule1);

        List<Schedule> listSchedules = db.sDAO().getAll();
        System.out.println(listSchedules.get(0));


    }
    private void goToContactsActivity(){
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
}
