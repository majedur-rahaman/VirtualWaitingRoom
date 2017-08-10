package mmh.app.majed.virtualwaitingroom.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import mmh.app.majed.virtualwaitingroom.R;
import mmh.app.majed.virtualwaitingroom.Session.SessionManager;
import mmh.app.majed.virtualwaitingroom.model.OnlineCallStatus;
import mmh.app.majed.virtualwaitingroom.model.OnlineConversation;
import mmh.app.majed.virtualwaitingroom.rest.ApiClient;
import mmh.app.majed.virtualwaitingroom.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mmh.app.majed.virtualwaitingroom.helper.BadgeChange.setBadgeCount;

/**
 * Created by majed on 7/27/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<OnlineConversation> notifications;
    private int rawLayout;
    private Context context;
    SessionManager session;
    final  String messageUnRead=" ,want's to connect with video conversation,call now.";
    final  String messageRead=" ,has connect with video conversation.";

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        LinearLayout messageLayout;
        TextView dFullName;
        TextView dStatus;
        Button dCallNow;
        TextView message;


        public MessageViewHolder(View v) {
            super(v);
            messageLayout=v.findViewById(R.id.messages_layout);
            dFullName=v.findViewById(R.id.dFullName);
         //   dStatus=v.findViewById(R.id.dStatus);
            message=v.findViewById(R.id.dMessage);
            dCallNow=v.findViewById(R.id.dCallNow);

        }
    }
    public MessageAdapter(List<OnlineConversation> notifications, int rawLayout,Context context){
        this.notifications=notifications;
        this.rawLayout=rawLayout;
        this.context=context;

    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(rawLayout,parent,false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MessageAdapter.MessageViewHolder holder, final int position) {
        holder.dFullName.setText(notifications.get(position).getCallerName());

        if (notifications.get(position).isRead()==true) {
            holder.message.setText(notifications.get(position).getCallerName() + messageRead);
            holder.dCallNow.setVisibility(View.GONE);
        }else{

            holder.message.setText(notifications.get(position).getCallerName() + messageUnRead);
        }

        holder.dCallNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OnlineCallStatus callStatus = new OnlineCallStatus(notifications.get(position).getId(),notifications.get(position).getCalleeId(),true);
                ApiInterface apiService= ApiClient.getClient().create(ApiInterface.class);

                Call<Boolean> call = apiService.UpdateCallStatus(callStatus);

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        holder.dCallNow.setVisibility(View.GONE);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notifications.get(position).getUrl()));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(browserIntent);
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });

//                MenuItem itemCart=menu.findItem(R.id.notificationBox);
//                LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
//                setBadgeCount(context.getApplicationContext(), icon, "9");

                ((Activity)context).invalidateOptionsMenu();

            }
        });


    }

    @Override
    public int getItemCount() {

        return notifications.size();
    }


}
