package mmh.app.majed.virtualwaitingroom.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import mmh.app.majed.virtualwaitingroom.R;
import mmh.app.majed.virtualwaitingroom.Session.SessionManager;
import mmh.app.majed.virtualwaitingroom.adapter.MessageAdapter;
import mmh.app.majed.virtualwaitingroom.model.OnlineConversation;
import mmh.app.majed.virtualwaitingroom.rest.ApiClient;
import mmh.app.majed.virtualwaitingroom.rest.ApiInterface;

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

        //Swipe Refresh
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                LoadNotification();

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

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
                    recyclerView.setAdapter(new MessageAdapter(notifications, R.layout.list_item_message, NotificationActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<OnlineConversation>> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Loading failed!",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent  i = new Intent(NotificationActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

}
