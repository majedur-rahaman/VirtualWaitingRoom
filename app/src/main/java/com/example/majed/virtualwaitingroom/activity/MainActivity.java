package com.example.majed.virtualwaitingroom.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.majed.virtualwaitingroom.R;
import com.example.majed.virtualwaitingroom.Session.SessionManager;
import com.example.majed.virtualwaitingroom.adapter.DoctorsAdapter;
import com.example.majed.virtualwaitingroom.adapter.MessageAdapter;
import com.example.majed.virtualwaitingroom.model.Doctor;
import com.example.majed.virtualwaitingroom.model.OnlineConversation;
import com.example.majed.virtualwaitingroom.rest.ApiClient;
import com.example.majed.virtualwaitingroom.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        //Load All doctors list.
        LoadDoctorList();
    }

    private void LoadDoctorList(){


        final RecyclerView recyclerView= (RecyclerView) findViewById(R.id.doctors_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Doctor>> call= apiService.getDoctorList();

        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {

                List<Doctor> doctors=response.body();

                if (doctors==null){
                    Toast.makeText(getApplicationContext(),"Incorrect Username/Password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    recyclerView.setAdapter(new DoctorsAdapter(doctors, R.layout.list_item_doctor, getApplicationContext()));
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Login Failed!",Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.notificationBox:
                Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
                startActivity(intent);
                setContentView(R.layout.activity_notification);
                return true;
            case R.id.onlineStatus:
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
//                }
                return true;
            case R.id.logOut:
                session.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
