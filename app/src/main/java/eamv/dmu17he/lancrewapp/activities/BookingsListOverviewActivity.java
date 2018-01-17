package eamv.dmu17he.lancrewapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;

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
import eamv.dmu17he.lancrewapp.helper.*;
import eamv.dmu17he.lancrewapp.model.*;

public class BookingsListOverviewActivity extends AppCompatActivity {
    private MobileServiceClient mClient;
    private MobileServiceTable<WakeUp> mWakeUpTable;
    private MobileServiceTable<Hall> mHallTable;
    private MobileServiceTable<Space> mSpaceTable;
    private ProgressBar mProgressBar;
    private BookingListAdapter mBookingAdapter;
    private AzureServiceAdapter mAzureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_overview);

        initProgressBar();
        initMobileService();

        createTable();
    }

    private void createTable() {
        try {
            initLocalStore().get();
            ListView listViewBooking = (ListView) findViewById(R.id.bookingslistoverview);
            listViewBooking.setAdapter(mBookingAdapter);
            AddWakeUpTest();
            refreshItemsFromTable();
        } catch (InterruptedException | ExecutionException | MobileServiceLocalStoreException e) {
            ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", this);
        }
    }
/*
    public void addItem(View view) {
        if (mClient == null) {
            return;
        }

        // Create a new item
        final Schedule schedule = new Schedule();
        schedule.setId("1");
        schedule.setGaName("123Glenn Mortensen");
        schedule.setDate("2018-01-12");
        schedule.setStartTime("09:00:00");
        schedule.setEndTime("13:00:00");
        schedule.setName("Daniel C Sucker");
        schedule.setTitle("Opvasker i køkken");

        final Schedule schedule1 = new Schedule();
        schedule1.setId("2");
        schedule1.setGaName("Glenn Mortensen");
        schedule1.setDate("2018-01-12");
        schedule1.setStartTime("10:00:00");
        schedule1.setEndTime("13:00:00");
        schedule1.setName("Mikkel VHN");
        schedule1.setTitle("Suutuuututtu");

        final Schedule schedule2 = new Schedule();
        schedule2.setId("3");
        schedule2.setGaName("Glenn Mortensen");
        schedule2.setDate("2018-01-12");
        schedule2.setStartTime("10:00:00");
        schedule2.setEndTime("13:00:00");
        schedule2.setName("Mikkel VHN");
        schedule2.setTitle("rgrgrgrgrg");

        final Schedule schedule3 = new Schedule();
        schedule3.setId("4");
        schedule3.setGaName("Glenn Mortensen");
        schedule3.setDate("2018-01-12");
        schedule3.setStartTime("10:00:00");
        schedule3.setEndTime("13:00:00");
        schedule3.setName("Mikkel VHN");
        schedule3.setTitle("DAvdav");

        final Activity mActivity = this;

        // Insert the new item
        @SuppressLint("StaticFieldLeak") //Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    final Schedule entity = addItemInTable(schedule3);

                    final Schedule entity1 = addItemInTable(schedule1);
                    final Schedule entity2 = addItemInTable(schedule2);
                    final Schedule entity3 = addItemInTable(schedule3);


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
*/
    public Space addSpaceInTable(Space item) throws ExecutionException, InterruptedException {
        Space entity = mSpaceTable.insert(item).get();
        return entity;
    }

    public Hall addSpaceInTable(Hall item) throws ExecutionException, InterruptedException {
        Hall entity = mHallTable.insert(item).get();
        return entity;
    }

    public WakeUp addSpaceInTable(WakeUp item) throws ExecutionException, InterruptedException {
        WakeUp entity = mWakeUpTable.insert(item).get();
        return entity;
    }

    private void AddWakeUpTest(){
        final Activity mActivity = this;
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    WakeUp test = new WakeUp();
                    test.setComment("Maah");
                    test.setTime("16:00");
                    WakeUp entity = mWakeUpTable.insert(test).get();
                    Space testy = new Space(2,5);
                    testy.setWakeUpID(entity.getId());
                    testy.setHallName("A");
                    testy.setUserName("smeaGOLL");
                    mSpaceTable.insert(testy);
                } catch (InterruptedException | ExecutionException e) {
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.execute();

    }

    private void refreshItemsFromTable() {
        final Activity mActivity = this;

        @SuppressLint("StaticFieldLeak") // <-- Just to suppress warning
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<BookingListViewItem> results = refreshBookingListView();

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
                    ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
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
            for (WakeUp wakeUp : WakeUps){
                BookingListViewItem booking = new BookingListViewItem();
                Space space = mSpaceTable.where().field("WakeUpID").eq(wakeUp.getId()).execute().get().get(0);
                booking.setTime(wakeUp.getTime());
                booking.setComment(wakeUp.getComment());
                booking.setColumn(space.getColumn());
                booking.setRow(space.getRow());
                booking.setNickName(space.getUserName());
                booking.setHallName(space.getHallName());
                booking.setPoke(wakeUp.getPokeCounter());

                merge.add(booking);
            }
        }
        return merge;
    }

    private List<Space> refreshSpaceFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
            return mSpaceTable.execute().get();
    }

    private List<Hall> refreshHallFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return mHallTable.execute().get();
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
        mBookingAdapter = new BookingListAdapter(this, R.layout.item);
        mSpaceTable = mClient.getTable(Space.class);
        mHallTable = mClient.getTable(Hall.class);
        mWakeUpTable = mClient.getTable(WakeUp.class);
    }
}