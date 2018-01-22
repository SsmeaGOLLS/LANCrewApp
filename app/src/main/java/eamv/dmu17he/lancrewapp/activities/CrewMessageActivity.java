package eamv.dmu17he.lancrewapp.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.GlobalUserSingleton;
import eamv.dmu17he.lancrewapp.helper.MessageAdapter;
import eamv.dmu17he.lancrewapp.model.Message;
import eamv.dmu17he.lancrewapp.model.User;

public class CrewMessageActivity extends Activity {

    private AzureServiceAdapter azureService;
    private MobileServiceTable<Message> messagesTable;
    private MobileServiceTable<User> usersTable;
    private EditText editText;
    private static List<User> users = new ArrayList<User>();

    static Handler messageHandler;
    static Thread thread;

    /**
     * Adapter to sync the items list with the view
     */
    private MessageAdapter mAdapter;

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

    public String getNickName(String userid)
    {
        for(User u : users)
            if (u.getId().equals(userid))
                return u.getUsername();
        return null;
    }

    public void updateUsers()
    {
        try {
            users = usersTable.execute().get();
        } catch(Exception e){users = new ArrayList<User>();}
    }

    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

                    @SuppressLint("StaticFieldLeak")
                    AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected Void doInBackground(Void... params) {

                            try {
                                updateUsers();
                                final List<Message> results = refreshItemsFromMobileServiceTable();
                                List<Message> test = messagesTable.execute().get();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.clear();
                                        for (Message item : results) {
                                            mAdapter.add(item);
                                        }
                                        messageHandler.sendEmptyMessage(0);
                                    }
                                });
                } catch (final Exception e){}
                return null;
            }
        };
        task.execute();
    }

    private List<Message> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return messagesTable.where().field("crewId").eq(GlobalUserSingleton.getGlobals(this).theCurrentUser.getCrew()).execute().get();
    }

    @SuppressLint("HandlerLeak")
    private void startMessenger() {
        editText = (EditText) findViewById(R.id.editText);
        azureService = AzureServiceAdapter.getInstance();
        azureService.updateClient(this,this,null);
        messagesTable = azureService.getClient().getTable(Message.class);
        usersTable = azureService.getClient().getTable(User.class);

        mAdapter = new MessageAdapter(this, R.layout.row_list_message);
        ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
        listViewToDo.setAdapter(mAdapter);

        messageHandler = new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {
                messageHandler.post(thread);
            }
        };
        thread = new Thread()
        {
            public void run()
            {
                refreshItemsFromTable();
            }
        };
        messageHandler.postDelayed(thread,0);
    }

    public void sendMessage(View view)
    {
        Message m = new Message();
        m.setText(editText.getText()+"");
        m.setCrewId(GlobalUserSingleton.getGlobals(this).theCurrentUser.getCrew());
        m.setMemberId(GlobalUserSingleton.getGlobals(this).theCurrentUser.getId());
        messagesTable.insert(m);
        editText.setText("");
    }
}