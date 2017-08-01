package com.example.majed.virtualwaitingroom.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.majed.virtualwaitingroom.R;
import com.example.majed.virtualwaitingroom.Session.SessionManager;
import com.example.majed.virtualwaitingroom.model.Doctor;
import com.example.majed.virtualwaitingroom.model.OnlineConversation;
import com.example.majed.virtualwaitingroom.rest.ApiClient;
import com.example.majed.virtualwaitingroom.rest.ApiInterface;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by majed on 7/25/2017.
 */

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> {

    private List<Doctor> doctors;
    private int rawLayout;
    private Context context;
    SessionManager session;


    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        LinearLayout doctorsLayout;
        TextView fullName;
        TextView status;
        Button callNow;
        TextView inCall;

        public DoctorViewHolder(View v) {
            super(v);
            doctorsLayout= (LinearLayout) v.findViewById(R.id.doctors_layout);
            fullName =(TextView) v.findViewById(R.id.fullName);
            status= (TextView)v.findViewById(R.id.status);
            callNow= v.findViewById(R.id.callNow);
            inCall=v.findViewById(R.id.InCall);

        }
    }

    public  DoctorsAdapter(List<Doctor> doctors, int rawLayout, Context context){
        this.doctors=doctors;
        this.rawLayout=rawLayout;
        this.context=context;

    }
    @Override
    public DoctorsAdapter.DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(rawLayout,parent,false);
        return new DoctorViewHolder(view);
    }



    @Override
    public void onBindViewHolder(DoctorViewHolder holder, final int position) {

        holder.fullName.setText(doctors.get(position).getFullName());
        holder.status.setText(doctors.get(position).getOnlineStatusStr());
        final  String contactId = doctors.get(position).getContactId();
        final String calleeName = doctors.get(position).getFullName();

        String webConference="http://myvirtualdoc.herokuapp.com";

        session = new SessionManager(context);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        String[] calleeN = calleeName.split("\\s");
        String calleeD = calleeN[calleeN.length-1];

        // name
     final String uName = user.get(SessionManager.KEY_NAME);

        String[] callerN = uName.split("\\s");
        String callerP = callerN[calleeN.length-1];

        // contact id
      final String ucontactId = user.get(SessionManager.KEY_CONTACTID);

        String mixedName = calleeD + "-" + callerP + "-" + System.currentTimeMillis() % 1000;

      final   String myUrl = String.format("%s/?did=%s&pid=%s&mixedName=%s", webConference, contactId, ucontactId, mixedName);
      final   String nUrl = String.format("%s/?mixedName=%s&did=%s&isMessage=%s", webConference, mixedName, contactId, true);



       // http://myvirtualdoc.herokuapp.com/?did=85a86ada-cae7-e611-a207-00155d0a5906&pid=85a86ada-cae7-e611-a207-00155d0a5906&mixedName=dr-watson

        holder.callNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                OnlineConversation conversation = new OnlineConversation(0,ucontactId,contactId,uName,calleeName,nUrl,"",false);

                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<Boolean> call = apiService.SaveUpdatedStatus(conversation);

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse("http://www.stackoverflow.com"));
//                        context.startActivity(intent);

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myUrl));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(browserIntent);
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });

                //notifyDataSetChanged();

            }
        });

        if (doctors.get(position).getOnlineStatus().equals("907660002")){
            holder.callNow.setVisibility(View.GONE);
        }else{
            holder.inCall.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {

        return doctors.size();
    }


}
