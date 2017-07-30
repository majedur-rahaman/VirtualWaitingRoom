package com.example.majed.virtualwaitingroom.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.majed.virtualwaitingroom.R;
import com.example.majed.virtualwaitingroom.Session.SessionManager;
import com.example.majed.virtualwaitingroom.adapter.MessageAdapter;
import com.example.majed.virtualwaitingroom.model.OnlineConversation;
import com.example.majed.virtualwaitingroom.rest.ApiClient;
import com.example.majed.virtualwaitingroom.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        // Session class instance
        session = new SessionManager(getApplicationContext());

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();


        LoadNotification();
    }

    private void LoadNotification(){



        final RecyclerView recyclerView= (RecyclerView) findViewById(R.id.messages_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        HashMap<String,String> user=session.getUserDetails();
        final String dContactId = user.get(SessionManager.KEY_CONTACTID);


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<OnlineConversation>> call= apiService.getNotifications(dContactId);

        call.enqueue(new Callback<List<OnlineConversation>>() {
            @Override
            public void onResponse(Call<List<OnlineConversation>> call, Response<List<OnlineConversation>> response) {

                List<OnlineConversation> notifications=response.body();

                if (notifications==null){
                    Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                }
                else
                {
                    recyclerView.setAdapter(new MessageAdapter(notifications, R.layout.list_item_message, getApplicationContext()));
                }
            }

            @Override
            public void onFailure(Call<List<OnlineConversation>> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Loading failed!",Toast.LENGTH_LONG).show();
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
//                Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
//                startActivity(intent);
//                setContentView(R.layout.activity_notification);
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
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        setContentView(R.layout.activity_main);
    }

}
