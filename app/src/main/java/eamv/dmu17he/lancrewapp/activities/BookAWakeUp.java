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

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.DateTimeAdapter;
import eamv.dmu17he.lancrewapp.helper.ToDialogError;
import eamv.dmu17he.lancrewapp.model.Controller;

public class BookAWakeUp extends AppCompatActivity {

    private DateTimeAdapter mDateTimeAdapter;
    private MobileServiceTable <Calendar> mTable;
    private AzureServiceAdapter mAzureAdapter;
    private MobileServiceClient mClient;
    private DateTimeAdapter mScheduleAdapter;

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
        if (mClient == null) {
            return;
        }

        final Calendar name = Calendar.getInstance();
        name.set(1991, 10, 12, 23, 10);
        final Activity mActivity = this;

        // Insert the new item
        @SuppressLint("StaticFieldLeak") //Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    final Calendar entity = addItemInTable(name);
                    /*
                    final Schedule entity1 = addItemInTable(schedule1);
                    final Schedule entity2 = addItemInTable(schedule2);
                    final Schedule entity3 = addItemInTable(schedule3);
                    */

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mScheduleAdapter.add(entity);

                        }
                    });
                } catch (final Exception e) {
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                    e.printStackTrace();
                }
                return null;
            }
        };
        runAsyncTask(task);
    }
    public Calendar addItemInTable(Calendar item) throws ExecutionException, InterruptedException {
        Calendar entity = mTable.insert(item).get();
        return entity;

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
        mScheduleAdapter = new DateTimeAdapter(this, R.layout.activity_book_awake_up);
        mTable = mClient.getTable (BookAWakeUp.class);

    }

}
