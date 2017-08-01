package com.example.majed.virtualwaitingroom.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.FontsContract;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.majed.virtualwaitingroom.R;
import com.example.majed.virtualwaitingroom.Session.SessionManager;
import com.example.majed.virtualwaitingroom.activity.NotificationActivity;
import com.example.majed.virtualwaitingroom.model.OnlineStatus;
import com.example.majed.virtualwaitingroom.rest.ApiClient;
import com.example.majed.virtualwaitingroom.rest.ApiInterface;

import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuFragment extends Fragment {

    private SessionManager session;
    private Menu menu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Session class instance
        session = new SessionManager(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.activity_main_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        String Role = session.getUserDetails().get(SessionManager.KEY_ROLE);
        if (!Role.equals("Doctor")){

            menu.findItem(R.id.notificationBox).setVisible(false);
        }
        updateMenuTitles();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int status= Integer.parseInt(session.getUserDetails().get(SessionManager.KEY_STATUS));
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.notificationBox:
                Intent intent = new Intent(getActivity().getApplicationContext(),NotificationActivity.class);
                startActivity(intent);
              //  setContentView(R.layout.activity_notification);
                return true;
            case R.id.online:
                styleMenuOnclick(item,907660001,checkStatus(status));
                return true;
            case R.id.offline:
                styleMenuOnclick(item,907660000,checkStatus(status));
                return true;
            case R.id.away:
                styleMenuOnclick(item,907660004,checkStatus(status));
                return true;
            case R.id.noDisturb:
                styleMenuOnclick(item,907660003,checkStatus(status));
                return true;
            case R.id.logOut:
                session.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateMenuTitles() {
        MenuItem menuItem = menu.findItem(R.id.UserName);
        String fullName= session.getUserDetails().get(SessionManager.KEY_NAME);
        menuItem.setTitle(fullName);
        MenuItem statusColor = null;

        int status= Integer.parseInt(session.getUserDetails().get(SessionManager.KEY_STATUS));

        statusColor = checkStatus(status);

        styleMenuOnclick(statusColor,status,statusColor);
    }

    private void styleMenuOnclick(MenuItem item, int status, MenuItem prevItem){

        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.GRAY);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.4f);

        //remove style
        SpannableString prevItems = new SpannableString(prevItem.getTitle().toString());
        prevItems.removeSpan(styleSpan);
        prevItems.removeSpan(foregroundColorSpan);
        prevItems.removeSpan(relativeSizeSpan);
        prevItem.setTitle(prevItems);

        // add style
        SpannableString currentItem = new SpannableString(item.getTitle().toString());
        currentItem.setSpan(foregroundColorSpan , 0, currentItem.length(), 0);
        currentItem.setSpan(relativeSizeSpan , 0, currentItem.length(), 0);
        item.setTitle(currentItem);

        UpdateOnlineStatus(status);
    }

    private void UpdateOnlineStatus(int status){

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        String contactId = session.getUserDetails().get(SessionManager.KEY_CONTACTID);
        final OnlineStatus onlineStatus = new OnlineStatus(contactId,status);

        Call<OnlineStatus> call= apiService.UpdateOnlineStatus(onlineStatus);

        call.enqueue(new Callback<OnlineStatus>() {
            @Override
            public void onResponse(Call<OnlineStatus> call, Response<OnlineStatus> response) {


                session.setUpdatedOnlineStatus(onlineStatus.getOnlineStatus());
               // Toast.makeText(getActivity().getApplicationContext(),"Updated" + session.getUserDetails().get(SessionManager.KEY_STATUS),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<OnlineStatus> call, Throwable t) {

            }
        });

    }

    private MenuItem checkStatus(int status){

        MenuItem statusColor = null;

        if (status==907660001){
            statusColor = menu.findItem(R.id.online);
        }else if(status==907660000){
            statusColor = menu.findItem(R.id.offline);
        }else if(status==907660004){
            statusColor = menu.findItem(R.id.away);
        }else if(status==907660003){
            statusColor = menu.findItem(R.id.noDisturb);
        }else
        {

        }

        return statusColor;
    }

}
