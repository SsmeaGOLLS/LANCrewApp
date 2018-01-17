package eamv.dmu17he.lancrewapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.BookingListAdapter;
import eamv.dmu17he.lancrewapp.helper.ToDialogError;
import eamv.dmu17he.lancrewapp.model.Controller;
import eamv.dmu17he.lancrewapp.model.Hall;
import eamv.dmu17he.lancrewapp.model.Space;
import eamv.dmu17he.lancrewapp.model.WakeUp;


public class BookAWakeUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private MobileServiceClient mClient;
    private MobileServiceTable<WakeUp> mWakeUpTable;
    private MobileServiceTable<Hall> mHallTable;
    private MobileServiceTable<Space> mSpaceTable;
    private ProgressBar mProgressBar;
    private BookingListAdapter mBookingAdapter;
    private AzureServiceAdapter mAzureAdapter;

    private ArrayList<String> wakeupss;
    Calendar selectedWakeUpTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_awake_up);
        initProgressBar();
        initMobileService();


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
                Controller.getInstance().createWakeUp("find out how to get username", selectedWakeUpTime, getComment());
                AddWakeUp();

            }
        });
    }

    private void AddWakeUp(){
        final Activity mActivity = this;
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    WakeUp test = new WakeUp();
                    test.setComment(getComment());
                    test.setTime(selectedWakeUpTime.getTime().toString());
                    WakeUp entity = mWakeUpTable.insert(test).get();


                } catch (InterruptedException | ExecutionException e) {
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.execute();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

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

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        mProgressBar.setVisibility(ProgressBar.GONE);
    }

    private void initMobileService() {
        mAzureAdapter = AzureServiceAdapter.getInstance();
        mAzureAdapter.updateClient(this, this, mProgressBar);
        mClient = mAzureAdapter.getClient();
        mBookingAdapter = new BookingListAdapter(this, R.layout.item);
        mSpaceTable = mClient.getTable(Space.class);
        mHallTable = mClient.getTable(Hall.class);
        mWakeUpTable = mClient.getTable(WakeUp.class);
    }
}
