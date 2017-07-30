package com.example.majed.virtualwaitingroom.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.majed.virtualwaitingroom.R;
import com.example.majed.virtualwaitingroom.Session.SessionManager;
import com.example.majed.virtualwaitingroom.ViewModel.UserInformation;
import com.example.majed.virtualwaitingroom.model.SignIn;
import com.example.majed.virtualwaitingroom.rest.ApiClient;
import com.example.majed.virtualwaitingroom.rest.ApiInterface;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";


    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        session = new SessionManager(getApplicationContext());
        session.checkLoginSession();

        _emailText= (EditText) findViewById(R.id.input_email);
        _passwordText= (EditText) findViewById(R.id.input_password);
        _loginButton= (Button) findViewById(R.id.btn_login);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();

            }
        });
    }
    public void login() {
        Log.d(TAG, "Login");

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        SignIn signIn = new SignIn();
        signIn.setEmail(email);
        signIn.setPassword(password);

        // TODO: Implement your own authentication logic here.

        if (email!=null && password!=null)
        {
            onLoginSuccess(signIn);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please provide username and password.", Toast.LENGTH_LONG).show();
        }


    }


    public void onLoginSuccess(SignIn signIn) {
        _loginButton.setEnabled(true);



        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<UserInformation> call = apiService.getSignin(signIn);

        call.enqueue(new Callback<UserInformation>() {
            @Override
            public void onResponse(Call<UserInformation> call, Response<UserInformation> response) {

                UserInformation userInformation =  response.body();

                if (userInformation==null){
                    Toast.makeText(getApplicationContext(), "Your username or password is incorrect, please try again with correct one.", Toast.LENGTH_LONG).show();

                }
                else
                {

                    session.createLoginSession(userInformation.FullName,userInformation.OnlineStatus,userInformation.ContactId);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    setContentView(R.layout.activity_main);

                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserInformation> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something going wrong!", Toast.LENGTH_LONG).show();
            }
        });
      //  finish();
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }


}
