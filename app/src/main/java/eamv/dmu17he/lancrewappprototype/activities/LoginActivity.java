package eamv.dmu17he.lancrewappprototype.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import eamv.dmu17he.lancrewappprototype.R;
import eamv.dmu17he.lancrewappprototype.helper.InputValidation;
import eamv.dmu17he.lancrewappprototype.model.User;
import eamv.dmu17he.lancrewappprototype.sql.sqLiteDatabase;
//import eamv.dmu17he.lancrewappprototype.sql.DBHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();
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

    private void initObjects(){
        inputValidation = new InputValidation(activity);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.appCompatButtonLogin:
                verifyFromDAO();
                break;

            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void verifyFromDAO() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUsername, textInputLayoutUsername, getString(R.string.error_message_username))) {
            return;
        }
        if (!inputValidation.isInputEditTextValidUsername(textInputEditTextUsername, textInputLayoutUsername, getString(R.string.error_message_username), this)) {
            Log.d("Jeg klarede den ikke", "fix mig tak");
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_username))) {
            return;
        }
        sqLiteDatabase db = sqLiteDatabase.getDatabase(this);
        String username = textInputEditTextUsername.getText().toString();
        String password = textInputEditTextPassword.getText().toString();
        String validUser = db.uDAO().findUserFromName(textInputEditTextUsername.getText().toString()).getUsername();


        if (!(validUser==null) && validUser.equals(textInputEditTextUsername.getText().toString())){
            Intent accountsIntent = new Intent(activity, UsersActivity.class);
            accountsIntent.putExtra("USERNAME", textInputEditTextUsername.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);

        }
        else {
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_username_password), Snackbar.LENGTH_LONG).show();
        }
        }



    private void emptyInputEditText(){
        textInputEditTextUsername.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
