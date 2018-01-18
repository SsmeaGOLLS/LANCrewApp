package eamv.dmu17he.lancrewapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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

public class EditOrDeleteHallActivity extends AppCompatActivity {
    private MobileServiceClient mClient;
    private MobileServiceTable<WakeUp> mWakeUpTable;
    private MobileServiceTable<Hall> mHallTable;
    private MobileServiceTable<Space> mSpaceTable;
    private ProgressBar mProgressBar;
    private AzureServiceAdapter mAzureAdapter;

    String hallName = "";
    TextView hallNameTextView;
    TextView rowsTextView;
    TextView columnsTextView;
    Spinner spinner;

    ArrayAdapter<Hall> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_hall);
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

        hallNameTextView = findViewById(R.id.displayhall);
        rowsTextView = findViewById(R.id.displayrow);
        columnsTextView = findViewById(R.id.editnumberofcolumns);

        hallNameList();


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

    private void hallNameList () {
        final Context mContext = this;
        final Activity mActivity = this;

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final List<Hall> mhallNames = mHallTable.execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner = findViewById(R.id.hallnamelist);

                            adapter = new ArrayAdapter<Hall>(mContext, android.R.layout.simple_spinner_dropdown_item, mhallNames);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Hall selected = (Hall)((Spinner)findViewById(R.id.hallnamelist)).getSelectedItem();
                                    hallNameTextView.setText(selected.getHallName());
                                    rowsTextView.setText(""+selected.getNumberOfRows());
                                    columnsTextView.setText(""+selected.getNumberOfColumns());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            spinner.setAdapter(adapter);
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

    public void saveChanges(View view) {
        final Hall selected = (Hall)((Spinner)findViewById(R.id.hallnamelist)).getSelectedItem();

        selected.setHallName(((TextView)findViewById(R.id.displayhall)).getText().toString());
        selected.setNumberOfRows(Integer.parseInt(((TextView)findViewById(R.id.displayrow)).getText().toString()));
        selected.setNumberOfColumns(Integer.parseInt(((TextView)findViewById(R.id.editnumberofcolumns)).getText().toString()));

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mHallTable.update(selected).get();
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

    public void deleteHall(View view) {
        final Hall selected = (Hall)((Spinner)findViewById(R.id.hallnamelist)).getSelectedItem();

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mHallTable.delete(selected).get();
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


    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        mProgressBar.setVisibility(ProgressBar.GONE);
        Button saveChanges = findViewById(R.id.savechangesbutton);
    }

    private void initMobileService() {
        mAzureAdapter = AzureServiceAdapter.getInstance();
        mAzureAdapter.updateClient(this, this, mProgressBar);
        mClient = mAzureAdapter.getClient();
        mSpaceTable = mClient.getTable(Space.class);
        mHallTable = mClient.getTable(Hall.class);
        mWakeUpTable = mClient.getTable(WakeUp.class);
    }
}
