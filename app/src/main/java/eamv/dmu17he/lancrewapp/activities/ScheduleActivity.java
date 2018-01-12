package eamv.dmu17he.lancrewapp.activities;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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



    }

    public  void postDataToSDAO(){

        Schedule schedule = new Schedule();
        schedule.setId(0);
        schedule.setDate("2018-01-12");
        schedule.setStartTime("09:00:00");
        schedule.setEndTime("13:00:00");
        schedule.setName("Daniel Suckerberg");
        schedule.setTitle("Opvasker i køkken");

        sqLiteDatabase db = sqLiteDatabase.getDatabase(this);
        db.sDAO().insertSchedule(schedule);
    }

}
