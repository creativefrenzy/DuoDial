package com.klive.app.Inbox;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.klive.app.Interface.ItemClick;
import com.klive.app.R;
import com.klive.app.model.VideoStatus.CircularStatusView;

import java.util.EventListener;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListAdapterHolder> {

    List<UserModal> arrayList;
    Context context;
    ItemClick itemClick;
    DatabaseReference chatRef;

    public UserListAdapter(Context context, List<UserModal> arrayList, ItemClick itemClick) {
        this.arrayList = arrayList;
        this.context = context;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public UserListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new UserListAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListAdapterHolder holder, final int position) {


        Log.e("recievemsggg112", "onBindViewHolder111: " );
        UserModal model = arrayList.get(position);
        Glide.with(context).load(model.getPeer_icon()).placeholder(R.drawable.default_profile).into(holder.icon);
        holder.name.setText(model.getPeer_name());

        Log.e("recievemsggg", "onBindViewHolder: model  1 "+new Gson().toJson(model));

        Log.e("recievemsggg11", "onBindViewHolder: listsize "+arrayList.size() );

        if(model.getChatType().equals("TEXT"))
        {
            holder.last_msg.setText(model.getPeer_last_msg());
        }
        else if(model.getChatType().equals("GIFT")) {
            holder.last_msg.setText("gift");

        }
      Log.e("recievemsggg", "onBindViewHolder: model  2  "+new Gson().toJson(model));
        holder.time.setText(model.getLast_time());

       checkOnlineOfflineStatus(model.getPeer_id(),holder.status_dot);

      Log.e("recievemsggg", "onBindViewHolder: 3  "+String.valueOf(model.getCount()));

        if(model.getCount()>0){
            holder.unread.setVisibility(View.VISIBLE);
            holder.unread.setText(String.valueOf(model.getCount()));
            Log.e("recievemsggg", "onBindViewHolder: 4  "+String.valueOf(model.getCount()));
        }
        else{
            holder.unread.setVisibility(View.INVISIBLE);
        }

        Log.e("recievemsggg", "onBindViewHolder: 5  "+String.valueOf(model.getCount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String peer_id = model.getPeer_id();
                itemClick.onClick(peer_id, model.getPeer_name(), model.getPeer_icon());
            }
        });

       // Log.e("recievemsggg", "onBindViewHolder: visibility "+ holder.unread.getVisibility());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class UserListAdapterHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name, last_msg, time, unread;
        CircleImageView status_dot;

        public UserListAdapterHolder(View view) {
            super(view);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            last_msg = itemView.findViewById(R.id.msg);
            time = itemView.findViewById(R.id.date);
            unread = itemView.findViewById(R.id.unread);
            status_dot = itemView.findViewById(R.id.status_dot);
        }
    }




    private void checkOnlineOfflineStatus(String uid, CircleImageView status_dot) {

      //  chatRef= FirebaseDatabase.getInstance().getReference().child("Users").child("672206762");
        chatRef= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> map = null;

                if(snapshot.exists())
                {
                    map = (Map<String, Object>) snapshot.getValue();


                    if(map.get("status").equals("Offline"))
                    {
                        status_dot.setImageResource(R.drawable.status_offline_symbol);
                    }
                    if(map.get("status").equals("Online"))
                    {
                        status_dot.setImageResource(R.drawable.status_symbol);
                    }

                }
                else {
                    status_dot.setImageResource(R.drawable.status_offline_symbol);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}
