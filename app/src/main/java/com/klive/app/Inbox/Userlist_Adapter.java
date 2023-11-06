package com.klive.app.Inbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import com.klive.app.R;
import com.klive.app.utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Userlist_Adapter extends RecyclerView.Adapter<Userlist_Adapter.MyViewHolder> {

    private final List<UserInfo> employeeList;
    protected final LayoutInflater inflater;
    Context context;
    int listLayoutRes;
    View itemViewforCLick;
    private DatabaseReference chatRef;

    public Userlist_Adapter(Context context, int listLayoutRes, List<UserInfo> employeeList) {
        //super(context, listLayoutRes, employeeList);
        inflater = ((Activity) context).getLayoutInflater();
        this.context = context;
        this.listLayoutRes = listLayoutRes;
        this.employeeList = employeeList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.user_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        setupView(holder, position);

    }

    @Override
    public int getItemCount() {

        return employeeList.size();
    }


    private void setupView(MyViewHolder holder, int position) {

        Log.e("employeelistsize", "setupView: employeeList.size() " + employeeList.size());

        final UserInfo employee = employeeList.get(position);
        holder.chat_name_txt.setText(employee.getUser_name());
        holder.chat_message.setText(employee.getMessage());
        holder.chat_date_txt.setText(getDateTimeByTimestamp(employee.getTime()));

        if (employee.getMsg_type().equals("gift")) {
            holder.chat_message.setText(employee.getMsg_type());
        } else if (employee.getMsg_type().equals("text")) {
            holder.chat_message.setText(employee.getMessage());
        } else if (employee.getMsg_type().equals("ss")) {
            holder.chat_message.setText("Screen Shot");
        }
        if (!TextUtils.isEmpty(employee.getUser_photo())) {
            Picasso.get().load(employee.getUser_photo()).placeholder(R.drawable.default_profile).into(holder.icon);
        }
        String unreadMsgCount = employee.getUnread_msg_count();
        int unreadCount = 0;
        if (!TextUtils.isEmpty(unreadMsgCount)) {
            unreadCount = Integer.parseInt(unreadMsgCount);
        }
        if (unreadCount > 0) {
            holder.chat_notifs_txt.setText("" + unreadCount);
            holder.chat_notifs_txt.setVisibility(View.VISIBLE);
            holder.chat_message.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.chat_notifs_txt.setVisibility(View.GONE);
            holder.chat_message.setTextColor(ContextCompat.getColor(context, R.color.grey));
        }
        holder.parentRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatActivity(employee);
            }
        });


        holder.tempId.setText("" + employee.getUser_id());


        try {
            DatabaseReference userDBRef;
            FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
            userDBRef = mFirebaseInstance.getReference("Users/" + employee.getUser_id());

            userDBRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Log.e("FirebaseRealTimeDB user", dataSnapshot.exists() + " UserName " + employee.getUser_name());
                        if (dataSnapshot.exists()) {

                            FirebaseUserModel userModel = dataSnapshot.getValue(FirebaseUserModel.class);
                            String status = userModel.getStatus();

                            if (userModel.getUid().equals(holder.tempId.getText().toString())) {

                                Log.e("FirebaseRealTimeDB22", "status=" + status + " UserName " + employee.getUser_name());
//                            firebaseFCMToken = userModel.getFcmToken();
                                if (status.equalsIgnoreCase("Online") || status.equalsIgnoreCase("Live")) {
//                              Log.e("userFCMOnline", userModel.getFcmToken());
                                    Log.e("checkOnineStatusLog", "result = Online");
                                    holder.status_dot.setImageResource(R.drawable.status_online_symbol);
                                } else if (status.equalsIgnoreCase("Busy")) {
                                    holder.status_dot.setImageResource(R.drawable.status_busy_symbol);
                                    Log.e("busyyyy", "onDataChange: " + "busyyy  " + " UserName " + employee.getUser_name());
                                } else if (status.equalsIgnoreCase("Offline")) {
                                    //   Log.e("userFCMOffline", userModel.getFcmToken());
                                    Log.e("checkOnineStatusLog", "result = Offline");
                                    holder.status_dot.setImageResource(R.drawable.status_offline_symbol);
                                }
                            }

                            // userDBRef.removeEventListener(this);

                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseRealTimeDB Fail", databaseError + "");
                }
            });
        } catch (Exception ex) {
            //
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout parentRL;
        TextView chat_name_txt;
        TextView chat_message;
        TextView chat_date_txt;
        CircleImageView icon, status_dot;
        TextView chat_notifs_txt;
        TextView tempId;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemViewforCLick = itemView;
            parentRL = itemView.findViewById(R.id.parentRL);
            chat_name_txt = (TextView) itemView.findViewById(R.id.chat_name_txt);
            chat_message = (TextView) itemView.findViewById(R.id.chat_message);
            chat_date_txt = (TextView) itemView.findViewById(R.id.chat_date_txt);
            icon = itemView.findViewById(R.id.icon);
            status_dot = itemView.findViewById(R.id.status_dot);
            chat_notifs_txt = itemView.findViewById(R.id.chat_notifs_txt);
            tempId = itemView.findViewById(R.id.tempId);


        }
    }

    public String getDateTimeByTimestamp(String dateInMilliseconds) {
        String dateFormat = "hh:mm a";
        try {
            return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private void openChatActivity(UserInfo contactInfo) {

        Log.e("VIEW_PROFILE_TEST", "openChatActivity: " + new Gson().toJson(contactInfo));

        int unreadCount = 0;
        if (!TextUtils.isEmpty(contactInfo.getUnread_msg_count())) {
            unreadCount = Integer.parseInt(contactInfo.getUnread_msg_count());
        }
        if (new SessionManager(context).getGender().equals("male")) {
            Intent intent = new Intent(context, InboxDetails.class);
            intent.putExtra("mode", true);
            intent.putExtra("channelName", "zeeplive662730982537574");
            intent.putExtra("chatProfileId", contactInfo.getUser_id());
            intent.putExtra("contactId", contactInfo.getId());
            intent.putExtra("profileName", contactInfo.getUser_name());
            intent.putExtra("usercount", 0);
            intent.putExtra("unreadMsgCount", unreadCount);
            intent.putExtra("user_image", contactInfo.getUser_photo());
            context.startActivity(intent);
        } else {
       /*
            Bundle bundle = new Bundle();
            bundle.putString("channelName", "zeeplive662730982537574");
            bundle.putString("chatProfileId", contactInfo.getUser_id());
            bundle.putString("contactId", contactInfo.getId());
            bundle.putString("profileName", contactInfo.getUser_name());
            bundle.putBoolean("mode", true);
            bundle.putInt("usercount", 0);
            bundle.putInt("unreadMsgCount", unreadCount);

            InboxDetailsFragment inboxDetailsFragment = new InboxDetailsFragment();
            AppCompatActivity activity = (AppCompatActivity) itemViewforCLick.getContext();
            Fragment fr = inboxDetailsFragment;
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction().addToBackStack(null);
            fr.setArguments(bundle);
            ft.add(R.id.fragment_view, fr);
            ((MainActivity) context).active = inboxDetailsFragment;
            ((MainActivity) context).hideMenu();
            ft.commit();

          */
            Intent intent = new Intent(context, InboxDetails.class);
            intent.putExtra("mode", true);
            intent.putExtra("channelName", "zeeplive662730982537574");
            intent.putExtra("chatProfileId", contactInfo.getUser_id());
            intent.putExtra("contactId", contactInfo.getId());
            intent.putExtra("profileName", contactInfo.getUser_name());
            intent.putExtra("usercount", 0);
            intent.putExtra("unreadMsgCount", unreadCount);
            intent.putExtra("user_image", contactInfo.getUser_photo());
            context.startActivity(intent);
        }
        //    Log.e("fromListAdapter", " m here");

       /* ((MainActivity) context).loadCHatForUser("zeeplive662730982537574", contactInfo.getUser_id(), contactInfo.getId(),
                contactInfo.getUser_name(), unreadCount, contactInfo.getUser_photo());*/

     /*   InboxDetailsFragment fragment = new InboxDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("mode", true);
        bundle.putString("channelName", "zeeplive662730982537574");
        bundle.putString("chatProfileId", contactInfo.getUser_id());
        bundle.putString("contactId", contactInfo.getId());
        bundle.putString("profileName", contactInfo.getUser_name());
        bundle.putInt("usercount", 0);
        bundle.putInt("unreadMsgCount", unreadCount);
        bundle.putString("user_image", contactInfo.getUser_photo());

        // bundle.putString("image", fileName);
        fragment.setArguments(bundle);//Here pass your data*/


    }

    private void checkOnlineOfflineStatus(String uid, CircleImageView status_dot) {
        chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> map = null;

                if (snapshot.exists()) {
                    map = (Map<String, Object>) snapshot.getValue();


                    if (map.get("status").equals("Offline")) {
                        status_dot.setImageResource(R.drawable.status_offline_symbol);
                    }

                    if (map.get("status").equals("Online")) {
                        status_dot.setImageResource(R.drawable.status_online_symbol);
                    }

                } else {
                    status_dot.setImageResource(R.drawable.status_offline_symbol);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}




