package eamv.dmu17he.lancrewapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import eamv.dmu17he.lancrewapp.R;

import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.ScheduleAdapter;
import eamv.dmu17he.lancrewapp.helper.ToDialogError;
import eamv.dmu17he.lancrewapp.model.Schedule;

public class CreateScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        initButtonAndProgressBar();
        initMobileService();

        createTable();
    }

    public void saveSchedule(View view) throws ExecutionException, InterruptedException{
        final Schedule item = new Schedule();
        EditText editText = (EditText) findViewById(R.id.titleET);
        item.setTitle(editText.getText().toString());

        editText = (EditText) findViewById(R.id.dateET);
        item.setDate(editText.getText().toString());

        editText = (EditText) findViewById(R.id.startTimeET);
        item.setStartTime(editText.getText().toString());

        editText = (EditText) findViewById(R.id.endTimeET);
        item.setEndTime(editText.getText().toString());

        editText = (EditText) findViewById(R.id.nickNameET);
        item.setNickName(editText.getText().toString());

        editText = (EditText) findViewById(R.id.gaNickET);
        item.setGaName(editText.getText().toString());


        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    mTable.insert(item).get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (final Exception e){

                }

                return null;
            }
        };

        runAsyncTask(task);

        Context context = getApplicationContext();
        CharSequence text = "Saved";
        int duration = Toast.LENGTH_SHORT;


        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 20, 0);
        toast.show();

        editText.setText("");

        editText = (EditText) findViewById(R.id.nickNameET);
        editText.setText("");

        editText = (EditText) findViewById(R.id.endTimeET);
        editText.setText("");

        editText = (EditText) findViewById(R.id.startTimeET);
        editText.setText("");

        editText = (EditText) findViewById(R.id.dateET);
        editText.setText("");

        editText = (EditText) findViewById(R.id.titleET);
        editText.setText("");
    }

    private Button refresh;
    private MobileServiceClient mClient;
    private MobileServiceTable<Schedule> mTable;
    private ProgressBar mProgressBar;
    private ScheduleAdapter mScheduleAdapter;
    private AzureServiceAdapter mAzureAdapter;


    private void createTable() {
        try {
            initLocalStore().get();
          
            //  ListView listViewSchedule = (ListView) findViewById(R.id.scheduleListView);
            //  listViewSchedule.setAdapter(mScheduleAdapter);
            //  refreshItemsFromTable();

        } catch (InterruptedException | ExecutionException | MobileServiceLocalStoreException e) {
            ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", this);
        }
    }

    public void addItem(View view) {
        if (mClient == null) {
            return;
        }

        final Activity mActivity = this;

        // Insert the new item
        @SuppressLint("StaticFieldLeak") //Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    //    final Schedule entity = addItemInTable(schedule);

                    //  final Schedule entity1 = addItemInTable(schedule1);
                    //final Schedule entity2 = addItemInTable(schedule2);
                    // final Schedule entity3 = addItemInTable(schedule3);



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                           //       mScheduleAdapter.add(entity);


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

    public Schedule addItemInTable(Schedule item) throws ExecutionException, InterruptedException {
        Schedule entity = mTable.insert(item).get();
        return entity;
    }

    private void refreshItemsFromTable() {
        final Activity mActivity = this;

        @SuppressLint("StaticFieldLeak") // <-- Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<Schedule> results = refreshItemsFromMobileServiceTable();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mScheduleAdapter.clear();

                            for (Schedule item : results) {
                                mScheduleAdapter.add(item);
                            }
                        }
                    });
                } catch (final Exception e){
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                }

                return null;
            }
        };

        runAsyncTask(task);
    }

    private List<Schedule> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return mTable.execute().get();
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

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("id", ColumnDataType.String);
                    tableDefinition.put("startTime", ColumnDataType.String);
                    tableDefinition.put("endTime", ColumnDataType.String);
                    tableDefinition.put("date", ColumnDataType.String);
                    tableDefinition.put("title", ColumnDataType.String);
                    tableDefinition.put("nickName", ColumnDataType.String);
                    tableDefinition.put("gaName", ColumnDataType.String);

                    localStore.defineTable("Schedule", tableDefinition);

                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

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

    private void initButtonAndProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        mProgressBar.setVisibility(ProgressBar.GONE);
        refresh = (Button) findViewById(R.id.refresh);
    }

    private void initMobileService() {
        mAzureAdapter = AzureServiceAdapter.getInstance();
        mAzureAdapter.updateClient(this, this, mProgressBar);
        mClient = mAzureAdapter.getClient();
        mScheduleAdapter = new ScheduleAdapter(this, R.layout.activity_create_schedule);
        mTable = mClient.getTable(Schedule.class);
    }

    public void goToCreateSchedule(View view){
        Intent intent = new Intent(this, CreateScheduleActivity.class);
        startActivity(intent);
    }
}
