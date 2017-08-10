package mmh.app.majed.virtualwaitingroom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import mmh.app.majed.virtualwaitingroom.R;
import mmh.app.majed.virtualwaitingroom.Session.SessionManager;
import mmh.app.majed.virtualwaitingroom.ViewModel.UserInformation;
import mmh.app.majed.virtualwaitingroom.model.SignIn;
import mmh.app.majed.virtualwaitingroom.rest.ApiClient;
import mmh.app.majed.virtualwaitingroom.rest.ApiInterface;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {



    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;

    private static final String TAG = "LoginActivity";

    private ProgressBar spinner;

    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _regisButton;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);



        session = new SessionManager(getApplicationContext());
        session.checkLoginSession();
        _emailText= (EditText) findViewById(R.id.input_email);
        _passwordText= (EditText) findViewById(R.id.input_password);
        _loginButton= (Button) findViewById(R.id.btn_login);
        _regisButton = findViewById(R.id.link_signup);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // spinner.setVisibility(View.VISIBLE);
                new MyTask().execute();
                login();

            }
        });

        _regisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(),RegistrationActivity.class);
                startActivity(intent);
                finish();
            }});


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
                   // spinner.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Your username or password is incorrect, please try again with correct one.", Toast.LENGTH_LONG).show();

                }
                else
                {

                    session.createLoginSession(userInformation.FullName,userInformation.OnlineStatus,userInformation.ContactId,userInformation.Role);
                    UnReadNotification();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                   // spinner.setVisibility(View.GONE);

                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserInformation> call, Throwable t) {
               // spinner.setVisibility(View.GONE);
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

    private void UnReadNotification(){

        // get Unread notification count

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Integer> call= apiService.UnReadNotificationCount(session.getUserDetails().get(SessionManager.KEY_CONTACTID).toString());

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                int count =response.body();
                session.NotificationCount(count);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }



    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _loginButton.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            _loginButton.setEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 5; i++) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
             //   e.printStackTrace();
            }
            return null;
        }
    }


}
