package eamv.dmu17he.lancrewapp.activities;

import android.app.ActionBar;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        sqLiteDatabase db = sqLiteDatabase.getDatabase(this);
        db.sDAO().deleteTable();

        Schedule schedule = new Schedule();
        schedule.setGaName("Glenn Mortensen");
        schedule.setDate("2018-01-12");
        schedule.setStartTime("09:00:00");
        schedule.setEndTime("13:00:00");
        schedule.setName("Daniel C Sucker");
        schedule.setTitle("Opvasker i køkken");

        Schedule schedule1 = new Schedule();
        schedule1.setGaName("Glenn Mortensen");
        schedule1.setDate("2018-01-12");
        schedule1.setStartTime("10:00:00");
        schedule1.setEndTime("13:00:00");
        schedule1.setName("Mikkel VHN");
        schedule1.setTitle("Suutuuututtu");

        Schedule schedule2 = new Schedule();
        schedule2.setGaName("Glenn Mortensen");
        schedule2.setDate("2018-01-12");
        schedule2.setStartTime("10:00:00");
        schedule2.setEndTime("13:00:00");
        schedule2.setName("Mikkel VHN");
        schedule2.setTitle("rgrgrgrgrg");

        Schedule schedule3 = new Schedule();
        schedule3.setGaName("Glenn Mortensen");
        schedule3.setDate("2018-01-12");
        schedule3.setStartTime("10:00:00");
        schedule3.setEndTime("13:00:00");
        schedule3.setName("Mikkel VHN");
        schedule3.setTitle("DAvdav");


        db.sDAO().insertSchedule(schedule);
        db.sDAO().insertSchedule(schedule1);
        db.sDAO().insertSchedule(schedule2);
        db.sDAO().insertSchedule(schedule3);


        List<Schedule> listSchedules = db.sDAO().getAll();
        LinearLayout list = (LinearLayout) this.findViewById(R.id.list);
        list.removeAllViews();

        for(Schedule item : listSchedules){
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView titleView = new TextView(this);
            titleView.setLayoutParams(params);
            titleView.setText(item.getTitle());
            titleView.setTextSize(20f);
            titleView.setTextColor(Color.BLACK);
            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            textView.setText(item.toString());
            textView.setTextColor(Color.BLACK);
            list.addView(titleView);
            list.addView(textView);



        }
        //System.out.println(listSchedules.get(0));

        //TextView textelement = (TextView) findViewById(R.id.pissefed);
        //textelement.setText(listSchedules.get(0).toString());




    }
    private void goToContactsActivity(){
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
}
