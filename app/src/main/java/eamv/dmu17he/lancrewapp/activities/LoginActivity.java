package eamv.dmu17he.lancrewapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.GlobalUserSingleton;
import eamv.dmu17he.lancrewapp.helper.ToDialogError;
import eamv.dmu17he.lancrewapp.model.User;

import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;

    private MobileServiceClient mClient;
    private MobileServiceTable<User> mTable;
    private ProgressBar mProgressBar;
    private AzureServiceAdapter mAzureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AzureServiceAdapter.Initialize();
        ToDialogError.initToDialogError();
        getSupportActionBar().hide();
        initViews();
        initListeners();
        initMobileService();
        GlobalUserSingleton.getGlobals(this).loadGlobalInfoFromFile(this);
    }
    private void initViews(){
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutUsername = (TextInputLayout) findViewById(R.id.textInputLayoutUsername);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextUsername = (TextInputEditText) findViewById(R.id.textInputEditTextUsername);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
    }

    private void initListeners(){
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                EditText userName =(EditText)findViewById(R.id.textInputEditTextUsername);
                EditText password =(EditText)findViewById(R.id.textInputEditTextPassword);

                new checkUser().execute(new String[]{userName.getText().toString(),password.getText().toString()});
                break;

            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }

    }

    private class checkUser extends AsyncTask<String, Void, User>
    {
        @Override
        protected User doInBackground(String... params)
        {
            try
            {
                List<User> temp=mTable.where().field("userName").
                        eq(textInputEditTextUsername.getText().toString()).execute().get();

                if(temp.size()!=0)
                {
                    //check password
                    if(temp.get(0).getPassword().equals(textInputEditTextPassword.getText().toString()))
                    {
                        return temp.get(0);
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {

                }
            }
            catch (InterruptedException | ExecutionException e){}



            return null;
        }

        @Override
        protected void onPostExecute(User foundUser)
        {
            if(foundUser!=null)
            {
                loggedIn(foundUser);
            }
            else
            {
                failedLogin();
            }
        }
    }

    public void loggedIn(User theUser)
    {
        //set global with the current user
        GlobalUserSingleton.getGlobals(this).theCurrentUser=theUser;

        Intent intent=new Intent(this, MenuActivity.class);
        startActivity(intent);
        //go to menuScreen
    }

    public void failedLogin()
    {
        Toast toast = Toast.makeText(this, "Failed Login, check username and password", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void initMobileService() {
        mAzureAdapter = AzureServiceAdapter.getInstance();
        mAzureAdapter.updateClient(this, this, mProgressBar);
        mClient = mAzureAdapter.getClient();
        mTable = mClient.getTable(User.class);
    }
}