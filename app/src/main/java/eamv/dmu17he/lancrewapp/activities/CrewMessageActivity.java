package eamv.dmu17he.lancrewapp.activities;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.MessageAdapter;
import eamv.dmu17he.lancrewapp.helper.MessageController;
import eamv.dmu17he.lancrewapp.model.Message;

public class CrewMessageActivity extends Activity {

    private AzureServiceAdapter azureService;

    // Bruges kun til at opbevare crewid og memberid
    // Skal erstattes med CurrentUser klasse
    private MobileServiceTable<Message> mToDoTable;

    static Handler h;
    static Thread t;

    /**
     * Adapter to sync the items list with the view
     */
    private MessageAdapter mAdapter;

    private MessageController messageController;

    /**
     * Initializes the activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

            // Load the items from the Mobile Service
            startMessenger();

    }

    public MessageController getMessageController()
    {
        return messageController;
    }

    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<Message> results = refreshItemsFromMobileServiceTable();
                    List<Message> test = mToDoTable.execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();
                            for (Message item : results) {
                                mAdapter.add(item);
                            }
                        }
                    });
                } catch (final Exception e){}
                return null;
            }
        };

        runAsyncTask(task);
    }

    private List<Message> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return mToDoTable.execute().get();
    }

    private void startMessenger()
    {
        messageController = new MessageController();

        AzureServiceAdapter.Initialize();
        azureService = AzureServiceAdapter.getInstance();
        azureService.updateClient(this,this,null);
        mToDoTable = azureService.getClient().getTable(Message.class);

        mAdapter = new MessageAdapter(this, R.layout.row_list_message);
        ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
        listViewToDo.setAdapter(mAdapter);

        h= new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {
                refreshItemsFromTable();
                h.postDelayed(t,1000);
            }
        };
        t = new Thread()
        {
            public void run()
            {
                h.sendEmptyMessage(0);
            }
        };
        h.postDelayed(t,500);
    }

    public void sendMessage(View view)
    {
        EditText editText = (EditText) findViewById(R.id.editText);

        Message m = new Message();
        m.setText(editText.getText()+"");
        m.setCrewId(messageController.getCrewId());
        m.setMemberId(messageController.getMemberId());
        mToDoTable.insert(m);
        editText.setText("");
    }

    /**
     * Run an ASync task on the corresponding executor
     * @param task
     * @return
     */
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }
}