package eamv.dmu17he.lancrewapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.ToDialogError;
import eamv.dmu17he.lancrewapp.helper.WakeUpAdapter;
import eamv.dmu17he.lancrewapp.model.WakeUp;

public class BookAWakeUp extends AppCompatActivity {

    private Button refresh;
    private MobileServiceClient mClient;
    private MobileServiceTable<WakeUp> mTable;
    private ProgressBar mProgressBar;
    private WakeUpAdapter mWakeUpAdapter;
    private AzureServiceAdapter mAzureAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_awake_up);
        AzureServiceAdapter.Initialize();

        initButtonAndProgressBar();
        initMobileService();

        createTable();

    }

    private void createTable() {
        try {
            initLocalStore().get();
            Spinner spinnerWakeUp = (Spinner) findViewById(R.id.spinner);
            mWakeUpAdapter.setDropDownViewResource(R.layout.textviewcalendar);
            Calendar calendar = Calendar.getInstance();
            calendar.set(1991, 10, 24,12,30);
            final WakeUp wakeUp = new WakeUp(calendar,"hey");

            ArrayList<String> wakeupss = new ArrayList<String>();
            wakeupss.add(wakeUp.getTime().getTime().toString());
            Log.d("hmm", wakeUp.getTime().getTime().toString());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, wakeupss);

            spinnerWakeUp.setAdapter(adapter);
            refreshItemsFromTable();
        } catch (InterruptedException | ExecutionException | MobileServiceLocalStoreException e) {
            ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", this);
        }
    }

    public void addItem(View view) {
        if (mClient == null) {
            return;
        }


        final Activity mActivity = this;

        Calendar calendar = Calendar.getInstance();
        calendar.set(1991, 10, 24,12,30);

        final WakeUp wakeUp = new WakeUp(calendar,"hey");

        // Insert the new item
        @SuppressLint("StaticFieldLeak") //Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    final WakeUp entity = addItemInTable(wakeUp);
                    /*
                    final Schedule entity1 = addItemInTable(schedule1);
                    final Schedule entity2 = addItemInTable(schedule2);
                    final Schedule entity3 = addItemInTable(schedule3);
                    */

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWakeUpAdapter.add(entity);


                        }
                    });
                } catch (final Exception e) {
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                    e.printStackTrace();
                }
                return null;
            }
        };
        //runAsyncTask(task);
    }

    public WakeUp addItemInTable(WakeUp item) throws ExecutionException, InterruptedException {
        WakeUp entity = mTable.insert(item).get();
        return entity;
    }

    private void refreshItemsFromTable() {
        final Activity mActivity = this;

        @SuppressLint("StaticFieldLeak") // <-- Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<WakeUp> results = refreshItemsFromMobileServiceTable();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWakeUpAdapter.clear();

                            for (WakeUp item : results) {
                                mWakeUpAdapter.add(item);
                                Log.d("wee", "run: bbbb");
                            }
                        }
                    });
                } catch (final Exception e){
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                }

                return null;
            }
        };

        //runAsyncTask(task);
    }

    private List<WakeUp> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
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
                    tableDefinition.put("time", ColumnDataType.String);
                    tableDefinition.put("comment", ColumnDataType.String);

                    localStore.defineTable("WakeUp", tableDefinition);

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

    }

    private void initMobileService() {
        mAzureAdapter = AzureServiceAdapter.getInstance();
        mAzureAdapter.updateClient(this, this, mProgressBar);
        mClient = mAzureAdapter.getClient();
        mWakeUpAdapter = new WakeUpAdapter(this, R.layout.activity_book_awake_up);
        mTable = mClient.getTable(WakeUp.class);
    }

}
