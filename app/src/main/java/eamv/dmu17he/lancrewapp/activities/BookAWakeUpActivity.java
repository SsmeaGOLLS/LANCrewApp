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
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.ToDialogError;
import eamv.dmu17he.lancrewapp.model.Hall;
import eamv.dmu17he.lancrewapp.model.Space;
import eamv.dmu17he.lancrewapp.model.WakeUp;


public class BookAWakeUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private MobileServiceClient mClient;
    private MobileServiceTable<WakeUp> mWakeUpTable;
    private MobileServiceTable<Hall> mHallTable;
    private MobileServiceTable<Space> mSpaceTable;

    private ProgressBar mProgressBar;
    private AzureServiceAdapter mAzureAdapter;

    private ArrayList<String> wakeupss;
    Calendar selectedWakeUpTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_awake_up);

        initProgressBar();
        initMobileService();
        try {
            initLocalStore();
        } catch (InterruptedException | ExecutionException | MobileServiceLocalStoreException e) {
            e.printStackTrace();
        }

        createTimesForWakeups();
        bookWakeUp();

        getHallNamesForSpinner();

    }

    private void createTimesForWakeups() {
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();

        wakeupss = new ArrayList<String>();

        for(int i = 0; i< 24; i++){
            calendar.add(Calendar.MINUTE, 30);
            wakeupss.add(calendar.getTime().toString());
        }

        Spinner spinnerWakeUp = (Spinner) findViewById(R.id.timespinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, wakeupss);
        spinnerWakeUp.setOnItemSelectedListener(this);
        spinnerWakeUp.setAdapter(adapter);



    }

    private void getHallNamesForSpinner() {

        try {
            List<Hall> halls = mHallTable.execute().get();

            Spinner pickHallSpinner = (Spinner) findViewById(R.id.pickhallspinner);
            ArrayAdapter<Hall> adapter = new ArrayAdapter<Hall>(this, android.R.layout.simple_spinner_dropdown_item, halls);

            pickHallSpinner.setAdapter(adapter);
            pickHallSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    getColumnsForSpinner((Hall) adapterView.getItemAtPosition(i));
                    getRowsForSpinner((Hall) adapterView.getItemAtPosition(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (InterruptedException | MobileServiceException | ExecutionException e) {
            e.printStackTrace();
            ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", this);
        }
    }

    private void getColumnsForSpinner(Hall hall) {
            ArrayList<Integer> columns = new ArrayList<Integer>();
            for (int x = 1; x <= hall.getNumberOfColumns(); x++) {
                columns.add(x);
            }

            Spinner spinnerWakeUp = (Spinner) findViewById(R.id.pickhallspinner);
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, columns);
            spinnerWakeUp.setOnItemSelectedListener(this);
            spinnerWakeUp.setAdapter(adapter);
    }

    private void getRowsForSpinner(Hall hall) {
            ArrayList<Integer> rows = new ArrayList<Integer>();
            for (int x = 1; x <= hall.getNumberOfRows(); x++) {
                rows.add(x);
            }

            Spinner spinnerWakeUp = (Spinner) findViewById(R.id.pickhallspinner);
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, rows);
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

    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {
        final Activity mActivity = this;
        @SuppressLint("StaticFieldLeak") // <-- Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStoreWakeUp = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinitionWakeUp = new HashMap<String, ColumnDataType>();
                    tableDefinitionWakeUp.put("id", ColumnDataType.String);
                    tableDefinitionWakeUp.put("startTime", ColumnDataType.DateTimeOffset);
                    tableDefinitionWakeUp.put("endTime", ColumnDataType.DateTimeOffset);
                    tableDefinitionWakeUp.put("pokeCounter", ColumnDataType.Integer);
                    tableDefinitionWakeUp.put("time", ColumnDataType.String);
                    tableDefinitionWakeUp.put("comment", ColumnDataType.String);

                    localStoreWakeUp.defineTable("WakeUp", tableDefinitionWakeUp);

                    SQLiteLocalStore localStoreHall = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinitionHall = new HashMap<String, ColumnDataType>();
                    tableDefinitionHall.put("id", ColumnDataType.String);
                    tableDefinitionHall.put("startTime", ColumnDataType.String);
                    tableDefinitionHall.put("endTime", ColumnDataType.String);
                    tableDefinitionHall.put("hallName", ColumnDataType.String);
                    tableDefinitionHall.put("numberOfColumns", ColumnDataType.Integer);
                    tableDefinitionHall.put("numberOfRows", ColumnDataType.Integer);

                    localStoreHall.defineTable("Hall", tableDefinitionHall);

                    SQLiteLocalStore localStoreSpace = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinitionSpace = new HashMap<String, ColumnDataType>();
                    tableDefinitionSpace.put("id", ColumnDataType.String);
                    tableDefinitionSpace.put("startTime", ColumnDataType.String);
                    tableDefinitionSpace.put("endTime", ColumnDataType.String);
                    tableDefinitionSpace.put("column", ColumnDataType.Integer);
                    tableDefinitionSpace.put("row", ColumnDataType.Integer);
                    tableDefinitionSpace.put("userName", ColumnDataType.String);
                    tableDefinitionSpace.put("hallName", ColumnDataType.String);

                    localStoreSpace.defineTable("Space", tableDefinitionSpace);

                    SimpleSyncHandler handlerSpace = new SimpleSyncHandler();
                    SimpleSyncHandler handlerHall = new SimpleSyncHandler();
                    SimpleSyncHandler handlerWakeUp = new SimpleSyncHandler();

                    syncContext.initialize(localStoreSpace, handlerSpace).get();
                    syncContext.initialize(localStoreHall, handlerHall).get();
                    syncContext.initialize(localStoreWakeUp, handlerWakeUp).get();

                } catch (final Exception e) {
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                }
                return null;
            }
        };
        return runAsyncTask(task);
    }

    public AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        mProgressBar.setVisibility(ProgressBar.GONE);
    }

    private void initMobileService() {
        mAzureAdapter = AzureServiceAdapter.getInstance();
        mAzureAdapter.updateClient(this, this, mProgressBar);
        mClient = mAzureAdapter.getClient();
        mWakeUpTable = mClient.getTable(WakeUp.class);
        mHallTable = mClient.getTable(Hall.class);
        mSpaceTable = mClient.getTable(Space.class);
    }
}