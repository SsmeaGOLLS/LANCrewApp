package eamv.dmu17he.lancrewapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.helper.AzureServiceAdapter;
import eamv.dmu17he.lancrewapp.helper.ToDialogError;
import eamv.dmu17he.lancrewapp.model.User;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputLayout textInputLayoutPhoneNumber;
    private TextInputLayout textInputLayoutAge;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEdittextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private TextInputEditText textInputEditTextPhoneNumber;
    private TextInputEditText textInputEditTextAge;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private Spinner dropdown;

    private MobileServiceClient mClient;
    private MobileServiceTable<User> mTable;
    private ProgressBar mProgressBar;
    private AzureServiceAdapter mAzureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        initViews();
        initMobileService();
        initListeners();
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutUsername = (TextInputLayout) findViewById(R.id.textInputLayoutUsername);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputConfirmPassword);
        textInputLayoutPhoneNumber = (TextInputLayout) findViewById(R.id.textInputLayoutPhoneNumber);
        textInputLayoutAge = (TextInputLayout) findViewById(R.id.textInputLayoutAge);


        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextUsername = (TextInputEditText) findViewById(R.id.textInputEditTextUsername);
        textInputEdittextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        textInputEditTextPhoneNumber = (TextInputEditText) findViewById(R.id.textInputEditTextPhoneNumber);
        textInputEditTextAge = (TextInputEditText) findViewById(R.id.textInputEditTextAge);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompactTextViewLoginLink);

        mProgressBar = (ProgressBar) findViewById(R.id.registerProgressBar);
        mProgressBar.setVisibility(ProgressBar.GONE);


        dropdown = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[]{"Crewcare", "Køkken", "Support", "IT", "Security"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    private void initListeners(){
        appCompatButtonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        isUsernameTaken();
    }

    public void goToLoginActivity(View view) {
        Intent intent = new Intent((getApplicationContext()), LoginActivity.class);
        startActivity(intent);
    }

    public void isUsernameTaken(){
        final Activity mActivity = this;
        if (!textInputEditTextUsername.getText().toString().isEmpty()) {
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        final List<User> mUser = mTable.where().field("username").eq(textInputEditTextUsername.getText().toString()).execute().get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mUser.size() > 0) {
                                    ToDialogError.getInstance().createAndShowDialog(getString(R.string.error_message_username), "Username is already in use", mActivity);

                                } else
                                {
                                    postDataToAzure();
                                }
                            }
                        });
                    } catch (Exception e) {
                        ToDialogError.getInstance().createAndShowDialogFromTask(e, "Error", mActivity);
                    }
                    return null;
                }


            };
            task.execute();

        } else {
            ToDialogError.getInstance().createAndShowDialog(getString(R.string.error_message_username), "Username is empty", this);
        }
    }

    private void postDataToAzure(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final User user = new User();

                    user.setIsAdmin(false);
                    user.setName(textInputEditTextName.getText().toString());
                    user.setNickName(textInputEditTextUsername.getText().toString());
                    user.setPhoneNumber(Integer.parseInt(textInputEditTextPhoneNumber.getText().toString()));
                    user.setCrew(dropdown.getSelectedItem().toString());
                    user.setAge(Integer.parseInt(textInputEditTextAge.getText().toString()));
                    user.setUsername(textInputEditTextUsername.getText().toString());
                    user.setNickName(textInputEditTextUsername.getText().toString());
                    user.setPassword(textInputEdittextPassword.getText().toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTable.insert(user);
                            // Snack Bar to show success message that record saved successfully
                            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
                            emptyInputEditText();

                            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentLogin);

                        }
                    });
                } catch (Exception e) {
                }
                return null;
            }
        };
        task.execute();

    }

    private void emptyInputEditText(){
        textInputEditTextName.setText(null);
        textInputEditTextUsername.setText(null);
        textInputEdittextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }

    private void initMobileService() {
        mAzureAdapter = AzureServiceAdapter.getInstance();
        mAzureAdapter.updateClient(this, this, mProgressBar);
        mClient = mAzureAdapter.getClient();
        mTable = mClient.getTable(User.class);
    }

}