package eamv.dmu17he.lancrewapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.MobileServiceConnection;
import com.microsoft.windowsazure.mobileservices.http.RequestAsyncTask;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.Headers;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.BookingListAdapter;
import eamv.dmu17he.lancrewapp.helper.ToDialogError;
import eamv.dmu17he.lancrewapp.model.BookingListViewItem;
import eamv.dmu17he.lancrewapp.model.Hall;
import eamv.dmu17he.lancrewapp.model.Space;
import eamv.dmu17he.lancrewapp.model.WakeUp;

public class BookingsListOverviewActivity extends AppCompatActivity {
    private MobileServiceClient mClient;
    private MobileServiceTable<WakeUp> mWakeUpTable;
    private MobileServiceTable<Hall> mHallTable;
    private MobileServiceTable<Space> mSpaceTable;
    private ProgressBar mProgressBar;
    private BookingListAdapter mBookingAdapter;
    private AzureServiceAdapter mAzureAdapter;
    private List<BookingListViewItem> results;
    private ListView listViewBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_overview);


        initProgressBar();
        initMobileService();

        createTable();
    }

    private void createTable() {
        final Activity mActivity = this;
        try {
            initLocalStore().get();
            listViewBooking = (ListView) findViewById(R.id.bookingslistoverview);
            listViewBooking.setAdapter(mBookingAdapter);

            refreshItemsFromTable();
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
                    results = refreshBookingListView();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mBookingAdapter.clear();

                            for (BookingListViewItem item : results) {
                                mBookingAdapter.add(item);
                            }
                        }
                    });
                } catch (final Exception e){

                    e.printStackTrace();
                }
                return null;
            }
        };
        runAsyncTask(task);

    }

    private List<BookingListViewItem> refreshBookingListView() throws InterruptedException, ExecutionException, MobileServiceException {
        List<BookingListViewItem> merge = new ArrayList<>();
        List<WakeUp> WakeUps = refreshWakeUpFromMobileServiceTable();
        if(!WakeUps.isEmpty()){
            System.out.println("1212");
            BookingListViewItem booking;
            for (WakeUp wakeUp : WakeUps){
                booking = new BookingListViewItem();
                booking.setTime(wakeUp.getTime());
                booking.setComment(wakeUp.getComment());
                booking.setPoke(wakeUp.getPokeCounter());
                booking.setWakeUpID(wakeUp.getId());
                booking.setActivity(this);
                List<Space> spaceList = mSpaceTable.where().field("wakeUpID").eq(wakeUp.getId()).execute().get();
                if (spaceList.size() == 1) {
                    Space space = spaceList.get(0);
                    booking.setColumn(space.getColumn());
                    booking.setRow(space.getRow());
                    booking.setNickName(space.getUserName());
                    booking.setHallName(space.getHallName());
                }
                merge.add(booking);
            }
        }
        return merge;
    }

    private List<WakeUp> refreshWakeUpFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return mWakeUpTable.execute().get();
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
                    tableDefinitionSpace.put("wakeUpID", ColumnDataType.String);

                    localStoreSpace.defineTable("Space", tableDefinitionSpace);

                    SQLiteLocalStore localStoreHall = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinitionHall = new HashMap<String, ColumnDataType>();
                    tableDefinitionHall.put("id", ColumnDataType.String);
                    tableDefinitionHall.put("startTime", ColumnDataType.String);
                    tableDefinitionHall.put("endTime", ColumnDataType.String);
                    tableDefinitionHall.put("date", ColumnDataType.String);
                    tableDefinitionHall.put("title", ColumnDataType.String);
                    tableDefinitionHall.put("name", ColumnDataType.String);
                    tableDefinitionHall.put("gaName", ColumnDataType.String);

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
        mBookingAdapter = new BookingListAdapter(this, R.layout.bookinglistitem);
        mSpaceTable = mClient.getTable(Space.class);
        mHallTable = mClient.getTable(Hall.class);
        mWakeUpTable = mClient.getTable(WakeUp.class);
    }

    public void updatePoke(View view, String wakeUpID, Context mContext) {
        final Button poke = (Button) view.findViewById(R.id.pokebutton);
        int p = (Integer.parseInt(poke.getText().toString()));

        final int x = (1 +(Integer.parseInt(poke.getText().toString())));
        poke.setText("" + (1 +(Integer.parseInt(poke.getText().toString()))));
        Log.d("beef", wakeUpID);
        final String mWakeUpID = wakeUpID;
        final Context mmContext = mContext;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updatePokeInWakeupTable(x, mWakeUpID, mmContext);
            }
        });


    }

    private void updatePokeInWakeupTable(final int pokeCounter,final String wakeUpID, final Context mContext) {
        final Activity mActivity = this;

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = runAsyncTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    List<WakeUp> wakeUpList = mWakeUpTable.where().field("id").eq(wakeUpID).execute().get();
                    if(wakeUpList.size()>0) {
                        WakeUp wakeup = wakeUpList.get(0);
                        wakeup.setPokeCounter(pokeCounter);
                        WakeUp entity = mWakeUpTable.update(wakeup).get();

                    }


                } catch (InterruptedException | ExecutionException  e) {
                    e.printStackTrace();
                }


                return null;
            }
        });

        task.execute();

        Eksempel

        @SuppressLint("StaticFieldLeak")
        RequestAsyncTask task = new RequestAsyncTask(mClient.getServiceFilter(), mClient.createConnection()) {
            @Override
            public void executeTask() {
                super.executeTask();
            }
        };

        Så skal der bare lige gennemskues hvordan man giver den de rigtige ting. ;)

    }

    private void updatePokeInWakeupTabl(final WakeUp wakeUp) {
        final Activity mActivity = this;

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = runAsyncTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                mWakeUpTable.update(wakeUp);




                return null;
            }
        });
        task.execute();


    }
}