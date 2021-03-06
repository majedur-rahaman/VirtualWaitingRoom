package mmh.app.majed.virtualwaitingroom.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import mmh.app.majed.virtualwaitingroom.R;
import mmh.app.majed.virtualwaitingroom.Session.SessionManager;
import mmh.app.majed.virtualwaitingroom.adapter.DoctorsAdapter;
import mmh.app.majed.virtualwaitingroom.model.Doctor;
import mmh.app.majed.virtualwaitingroom.rest.ApiClient;
import mmh.app.majed.virtualwaitingroom.rest.ApiInterface;

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

       //Swipe Refresh
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                LoadDoctorList();

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });



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

              //  Toast.makeText(getApplicationContext(),"Login Failed!",Toast.LENGTH_LONG).show();
            }
        });


    }


}
