package eamv.dmu17he.lancrewapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.DateTimeAdapter;
import eamv.dmu17he.lancrewapp.model.Controller;

public class BookAWakeUp extends AppCompatActivity {

    private DateTimeAdapter mDateTimeAdapter;
    private MobileServiceTable <Calendar> mTable;
    private AzureServiceAdapter mAzureAdapter;
    private MobileServiceClient mClient;
    private ScheduleAdapter mScheduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_awake_up);

        ArrayList<Calendar> wakeuptimes = new ArrayList<Calendar>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1996,10,05,22,45);
        //Controller.getInstance().makeWakeUp("Jonassucks","æblekage",calendar,"hej");
        Spinner times = findViewById(R.id.spinner);
        ArrayAdapter<Calendar> localTimes = new ArrayAdapter<Calendar>(this,R.layout.activity_book_awake_up,wakeuptimes);
        localTimes.setDropDownViewResource(R.layout.activity_book_awake_up);

        wakeuptimes.add(calendar);
        calendar.set(1996,10,04,22,45);
        wakeuptimes.add(calendar);
        times.setAdapter(localTimes);

        DateTimeAdapter dateTimeAdapter = new DateTimeAdapter(this, R.layout.activity_book_awake_up);

    }
    public void additem (View view){
        if(mClient == null){
            return;
        }
        final Activity mActivity = this;

        final Calendar calendar = Calendar.getInstance();
        calendar.set(1996,10,05,22,45);

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void,Void,Void> task = (params)->{
            try{
                final Calendar entity = addItemInTabel(calendar);
                runOnUiThread(()->{
                    mDateTimeAdapter.add(entity);
                    });
            } catch(final Exception e){

            }
            return null;
        };
        runAsyncTask(task);
    }
    public AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else{
            return task.execute();
        }

    }
    public Calendar addItemInTabel(Calendar item) throws ExecutionException, InterruptedException{
        Calendar entity = mTable.insert(item).get();
        return entity;
    }
    private void initMobileService(){
        mAzureAdapter = AzureServiceAdapter.getInstance();
        mAzureAdapter.updateClient(this, this, mProgressBar);
        mClient = mAzureAdapter.getClient();
        mScheduleAdapter = new ScheduleAdapter(this, R.layout.activity_book_awake_up);
        mTable = mClient.getTable (BookAWakeUp.class);

    }

}
