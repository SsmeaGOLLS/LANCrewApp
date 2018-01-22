package eamv.dmu17he.lancrewapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.GlobalUserSingleton;
import eamv.dmu17he.lancrewapp.helper.ProfileAdapter;
import eamv.dmu17he.lancrewapp.helper.ScheduleAdapter;
import eamv.dmu17he.lancrewapp.helper.ToDialogError;
import eamv.dmu17he.lancrewapp.helper.SickAdapter;
import eamv.dmu17he.lancrewapp.model.Schedule;
import eamv.dmu17he.lancrewapp.model.User;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;

public class ProfileActivity extends AppCompatActivity {

    private Button refresh;
    private MobileServiceClient mClient;
    private MobileServiceTable<User> mTable;
    private MobileServiceTable<Schedule> mScheduleTable;
    private ProgressBar mProgressBar;
    private ProfileAdapter mProfileAdapter;
    private ScheduleAdapter mScheduleAdapter;
    private AzureServiceAdapter mAzureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initButtonAndProgressBar();
        initMobileService();
        createTable();
    }

    private void createTable() {
        try {
            initLocalStore().get();
            ListView listViewUser = (ListView) findViewById(R.id.profileListView);
            ListView listViewSchedule = (ListView) findViewById(R.id.profileScheduleListView);
            listViewUser.setAdapter(mProfileAdapter);
            listViewSchedule.setAdapter(mScheduleAdapter);
            refreshItemsFromTable();
            refreshItemsFromScheduleTable();

        } catch (InterruptedException | ExecutionException | MobileServiceLocalStoreException e) {
            ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", this);
        }
    }

    private void refreshItemsFromTable() {
        final Activity mActivity = this;

        @SuppressLint("StaticFieldLeak") // <-- Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<User> results = refreshItemsFromMobileServiceTable();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProfileAdapter.clear();

                            for (User item : results) {
                                mProfileAdapter.add(item);
                            }
                        }
                    });
                } catch (final Exception e){
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                }

                return null;
            }
        };

        task.execute();
    }

    private void refreshItemsFromScheduleTable() {
        final Activity mActivity = this;

        @SuppressLint("StaticFieldLeak") // <-- Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<Schedule> results = refreshScheduleItemsFromMobileServiceTable();

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

        task.execute();
    }

    public void refreshItems(View view){
        refreshItemsFromScheduleTable();
        refreshItemsFromTable();
    }

    private List<User> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return mTable.where().field("username").eq(GlobalUserSingleton.getGlobals(this).theCurrentUser.getUsername()).execute().get();
    }

    private List<Schedule> refreshScheduleItemsFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return mScheduleTable.where().field("nickName").eq(GlobalUserSingleton.getGlobals(this).theCurrentUser.getUsername()).execute().get();
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

                    Map<String, ColumnDataType> userTableDefinition = new HashMap<String, ColumnDataType>();
                    userTableDefinition.put("id", ColumnDataType.String);
                    userTableDefinition.put("name", ColumnDataType.String);
                    userTableDefinition.put("userName", ColumnDataType.String);
                    userTableDefinition.put("password", ColumnDataType.String);
                    userTableDefinition.put("phoneNumber", ColumnDataType.Integer);
                    userTableDefinition.put("nickName", ColumnDataType.String);
                    userTableDefinition.put("isAdmin", ColumnDataType.Boolean);

                    Map<String, ColumnDataType> scheduleTableDefinition = new HashMap<String, ColumnDataType>();
                    scheduleTableDefinition.put("id", ColumnDataType.String);
                    scheduleTableDefinition.put("startTime", ColumnDataType.String);
                    scheduleTableDefinition.put("endTime", ColumnDataType.String);
                    scheduleTableDefinition.put("date", ColumnDataType.String);
                    scheduleTableDefinition.put("title", ColumnDataType.String);
                    scheduleTableDefinition.put("nickName", ColumnDataType.String);
                    scheduleTableDefinition.put("gaName", ColumnDataType.String);

                    localStore.defineTable("User", userTableDefinition);

                    localStore.defineTable("Schedule", scheduleTableDefinition);

                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                } catch (final Exception e) {
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                }

                return null;
            }
        };

        return task.execute();
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
        mProfileAdapter = new ProfileAdapter(this, R.layout.profile);
        mScheduleAdapter = new ScheduleAdapter(this, R.layout.schedule);
        mTable = mClient.getTable(User.class);
        mScheduleTable = mClient.getTable(Schedule.class);
    }
}
