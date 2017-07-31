package com.example.majed.virtualwaitingroom.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.FontsContract;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import com.example.majed.virtualwaitingroom.R;
import com.example.majed.virtualwaitingroom.Session.SessionManager;
import com.example.majed.virtualwaitingroom.activity.NotificationActivity;


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
        updateMenuTitles();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.notificationBox:
                Intent intent = new Intent(getActivity().getApplicationContext(),NotificationActivity.class);
                startActivity(intent);
              //  setContentView(R.layout.activity_notification);
                return true;
            case R.id.onlineStatus:

                return true;
            case R.id.offline:

                return true;
            case R.id.away:

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


        MenuItem statusColor = menu.findItem(R.id.online);
        styleMenuOnclick(statusColor);
    }

    private void styleMenuOnclick(MenuItem item){
        SpannableString ol = new SpannableString(item.getTitle().toString());
        ol.setSpan(new ForegroundColorSpan(Color.GRAY), 0, ol.length(), 0);
        ol.setSpan(new RelativeSizeSpan(1.4f),0,ol.length(),0);
        //ol.setSpan(new BackgroundColorSpan(Color.parseColor("#E9EBEE")),0,ol.length(),0);
        item.setTitle(ol);
    }

}
