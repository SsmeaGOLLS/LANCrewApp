package eamv.dmu17he.lancrewapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.GlobalUserSingleton;
import eamv.dmu17he.lancrewapp.helper.ToDialogError;
import eamv.dmu17he.lancrewapp.model.BookingListViewItem;
import eamv.dmu17he.lancrewapp.model.Hall;
import eamv.dmu17he.lancrewapp.model.Space;
import eamv.dmu17he.lancrewapp.model.User;
import eamv.dmu17he.lancrewapp.model.WakeUp;

public class DeleteWakeUpActivity extends AppCompatActivity {
    private MobileServiceClient mClient;
    private MobileServiceTable<WakeUp> mWakeUpTable;
    private MobileServiceTable<Hall> mHallTable;
    private MobileServiceTable<Space> mSpaceTable;
    private ProgressBar mProgressBar;
    private AzureServiceAdapter mAzureAdapter;
    private AdapterView.OnItemSelectedListener onClicky;
    List<Space> mspaceTable;
    List<WakeUp> mwakeupTable;

    String hallName = "";
    TextView displaytime;
    TextView displayhall;
    TextView displayrow;
    TextView displaycolumn;
    Spinner spinner;

    ArrayAdapter<WakeUp> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_wake_up);
        initProgressBar();
        initMobileService();

        try {
            initLocalStore().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (MobileServiceLocalStoreException e) {
            e.printStackTrace();
        }

        displayhall = findViewById(R.id.displayhall);
        displayrow = findViewById(R.id.displayrow);
        displaycolumn = findViewById(R.id.displaycolumn);
        dataList();

    }
    private void dataList() {
        final Context mContext = this;
        final Activity mActivity = this;

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mspaceTable = mSpaceTable.where().field("userName").eq(GlobalUserSingleton.getGlobals(mContext).theCurrentUser.getUsername().toString()).execute().get();
                    mwakeupTable = mWakeUpTable.execute().get();

                    Log.d("gås", String.valueOf(mSpaceTable.where().field("userName").eq(GlobalUserSingleton.getGlobals(mContext).theCurrentUser.getUsername())));


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeList();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (MobileServiceException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.execute();
    }
    private void timeList() {
        final Context mContext = this;
        final Activity mActivity = this;
        final List<WakeUp> wakeUpTimes = new ArrayList<WakeUp>() {
        };
        Log.d("gås", String.valueOf(mspaceTable.isEmpty()));
        for (Space space: mspaceTable) {

            for(WakeUp wakeUp: mwakeupTable){

                if(wakeUp.getId().equals(space.getWakeUpID()))    {
                    wakeUpTimes.add(wakeUp);

                }
            }
        }
        adapter = new ArrayAdapter<WakeUp>(mContext, android.R.layout.simple_spinner_dropdown_item, wakeUpTimes);
        spinner.setAdapter(adapter);
        setListener();}



    private void setListener() {
        onClicky = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final WakeUp selected = (WakeUp) ((Spinner) findViewById(R.id.timespinner)).getSelectedItem();
                @SuppressLint("StaticFieldLeak")
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            List<Space> spaceList = mSpaceTable.where().field("wakeUpId").eq(selected.getId()).execute().get();
                            if (spaceList.size() > 0) {
                                Space space = spaceList.get(0);
                                if (selected.getId().equals(space.getWakeUpID())) {
                                    displayhall.setText(space.getHallName());
                                    displayrow.setText("" + space.getRow());
                                    displaycolumn.setText("" + space.getColumn());
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                task.execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        spinner.setOnItemSelectedListener(onClicky);
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

                    SQLiteLocalStore localStoreHall = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinitionHall = new HashMap<String, ColumnDataType>();
                    tableDefinitionHall.put("id", ColumnDataType.String);
                    tableDefinitionHall.put("startTime", ColumnDataType.String);
                    tableDefinitionHall.put("endTime", ColumnDataType.String);
                    tableDefinitionHall.put("hallName", ColumnDataType.String);
                    tableDefinitionHall.put("numberOfColumns", ColumnDataType.Integer);
                    tableDefinitionHall.put("numberOfRows", ColumnDataType.Integer);

                    localStoreHall.defineTable("Hall", tableDefinitionHall);

                    SQLiteLocalStore localStoreWakeUp = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinitionWakeUp = new HashMap<String, ColumnDataType>();
                    tableDefinitionWakeUp.put("id", ColumnDataType.String);
                    tableDefinitionWakeUp.put("startTime", ColumnDataType.DateTimeOffset);
                    tableDefinitionWakeUp.put("endTime", ColumnDataType.DateTimeOffset);
                    tableDefinitionWakeUp.put("pokeCounter", ColumnDataType.Integer);
                    tableDefinitionWakeUp.put("time", ColumnDataType.String);
                    tableDefinitionWakeUp.put("comment", ColumnDataType.String);

                    localStoreWakeUp.defineTable("WakeUp", tableDefinitionWakeUp);

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
        return task.execute();
    }


    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        mProgressBar.setVisibility(ProgressBar.GONE);
        Button saveChanges = findViewById(R.id.savechangesbutton);
        spinner = findViewById(R.id.timespinner);
    }

    private void initMobileService() {
        mAzureAdapter = AzureServiceAdapter.getInstance();
        mAzureAdapter.updateClient(this, this, mProgressBar);
        mClient = mAzureAdapter.getClient();
        mSpaceTable = mClient.getTable(Space.class);
        mHallTable = mClient.getTable(Hall.class);
        mWakeUpTable = mClient.getTable(WakeUp.class);
    }

    public void deleteSpace(View view) {
        final WakeUp selected = (WakeUp) ((Spinner) findViewById(R.id.timespinner)).getSelectedItem();

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    List<Space> spaceList = mSpaceTable.where().field("wakeUpId").eq(selected.getId()).execute().get();
                    if (spaceList.size() > 0) {
                        Space space = spaceList.get(0);
                        mSpaceTable.delete(space);
                    }
                    mWakeUpTable.delete(selected).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                refreshItemsFromTable();
                return null;
            }
        };
        task.execute();
    }

    private void refreshItemsFromTable() {
        final Activity mActivity = this;
        final Context mContext = this;

        @SuppressLint("StaticFieldLeak") // <-- Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<WakeUp> mwakeupTime = mWakeUpTable.execute().get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<WakeUp>(mContext, android.R.layout.simple_spinner_dropdown_item, mwakeupTime);
                            spinner.setAdapter(adapter);
                            setListener();
                        }
                    });
                } catch (final Exception e){
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.execute();
    }
}


