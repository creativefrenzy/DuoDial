package com.privatepe.app.adapter.metend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.privatepe.app.Inbox.FirebaseUserModel;
import com.privatepe.app.R;
import com.privatepe.app.activity.ViewProfileMet;
import com.privatepe.app.fragments.metend.FollowingFragment;
import com.privatepe.app.response.UserListResponse;
import com.privatepe.app.response.metend.AddRemoveFavResponse;
import com.privatepe.app.response.metend.FollowingDatum;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.PaginationAdapterCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowUserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ApiResponseInterface {

    private final List<FollowingDatum> datumList;

    Context context;
    private DatabaseReference chatRef;
    private PaginationAdapterCallback mCallback;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;
    FollowingFragment fragment;
    OnClickedOpenPopupListener clickedOpenPopupListener;
    boolean isClicked = false;

    public FollowUserListAdapter(Context context, PaginationAdapterCallback mCallback, FollowingFragment followingFragment) {

        this.context = context;
        this.mCallback = mCallback;
        this.datumList = new ArrayList<>();
        this.fragment = followingFragment;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View v1 = null;
                v1 = inflater.inflate(R.layout.follow_user_list_item, parent, false);
                viewHolder = new MyViewHolder(v1);
                break;

            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder hld, @SuppressLint("RecyclerView") int position) {

        switch (getItemViewType(position)) {

            case ITEM:
                try {
                    final MyViewHolder holder = (MyViewHolder) hld;

                    FollowingDatum employee = datumList.get(position);
                    holder.tvUserName.setText(employee.getFollowing_data().getName());
                    holder.tvUserLevel.setText(employee.getFollowing_data().getLevel()+"");


                    if (!TextUtils.isEmpty(employee.following_data.profile_images.get(0).image_name)) {
                        Picasso.get().load(employee.following_data.profile_images.get(0).image_name).placeholder(R.drawable.default_profile).into(holder.icon);
                    }

                    holder.tempId.setText("" + employee.getFollowing_data().getProfile_id());

                    try {
                        DatabaseReference userDBRef;
                        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
                        userDBRef = mFirebaseInstance.getReference("Users/" + employee.getFollowing_data().getProfile_id());

                        userDBRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {

                                    if (dataSnapshot.exists()) {

                                        FirebaseUserModel userModel = dataSnapshot.getValue(FirebaseUserModel.class);
                                        String status = userModel.getStatus();
                                        if (userModel.getUid().equals(holder.tempId.getText().toString())) {

                                            Log.e("FirebaseRealTimeDB11", "status=" + status + " UserName " + employee.getFollowing_data().getName());
//                            firebaseFCMToken = userModel.getFcmToken();
                                            if (status.equalsIgnoreCase("Online") || status.equalsIgnoreCase("Live")) {
//                              Log.e("userFCMOnline", userModel.getFcmToken());
                                                Log.e("checkOnineStatusLog", "result = Online");
                                                holder.status_dot.setImageResource(R.drawable.status_online_symbol);
                                            } else if (status.equalsIgnoreCase("Busy")) {
                                                holder.status_dot.setImageResource(R.drawable.status_busy_symbol);
                                                Log.e("busyyyy", "onDataChange: " + "busyyy  " + " UserName " + employee.getFollowing_data().getName());
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

                    holder.parentRL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ViewProfileMet.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("id", employee.getFollowing_data().getId());
                            bundle.putSerializable("profileId", employee.getFollowing_data().getProfile_id() + "");
                            bundle.putSerializable("level", employee.getFollowing_data().getLevel());
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });
                    holder.tvFollowing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //clickedOpenPopupListener.openPopup(employee.getFollowing_data().getId()+"");
                            /*datumList.remove(position);
                            notifyDataSetChanged();*/

                            showBottomSheetDialog(employee.getFollowing_data().getId()+"", holder);

                        }
                    });

                } catch (Exception e) {
                    Log.e("HomeUserAdapter", "onBindViewHolder: Exception " + e.getMessage());
                }
            case LOADING:
                //   LoadingVH loadingVH = (LoadingVH) hld;

                if (retryPageLoad) {
                    //   loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    //  loadingVH.mProgressBar.setVisibility(View.GONE);
                    // homeFragment.hideProgress();
                          /*    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    "Unknown Error");*/

                } else {
                    //  loadingVH.mErrorLayout.setVisibility(View.GONE);
                    //    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                    //   homeFragment.showProgress();
                }
                break;

        }

    }


    @Override
    public int getItemCount() {
        return datumList == null ? 0 : datumList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == datumList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.FOLLOWING_HOST) {
            AddRemoveFavResponse addRemoveFavResponse = (AddRemoveFavResponse) response;
            if(addRemoveFavResponse.isSuccess()) {
                customErrorToast(addRemoveFavResponse.getResult());
            }
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout parentRL;
        TextView tvUserName;
        TextView tempId;
        TextView tvFollowing,tvUserLevel;
        CircleImageView icon, status_dot;


        public MyViewHolder(View itemView) {
            super(itemView);

            parentRL = itemView.findViewById(R.id.parentRL);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tempId = (TextView) itemView.findViewById(R.id.tempId);
            tvUserLevel = (TextView) itemView.findViewById(R.id.tvUserLevel);
            tvFollowing = (TextView) itemView.findViewById(R.id.tvFollowing);
            icon = itemView.findViewById(R.id.icon);
            status_dot = itemView.findViewById(R.id.status_dot);


        }
    }

    private void showBottomSheetDialog(String userId, MyViewHolder holder) {
        //com.zego.ve.Log.e("openPopup==",userId+"===showBottomSheetDialog=======>);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_edit_profile);

        Button cancel = bottomSheetDialog.findViewById(R.id.cancel);
        TextView delete = bottomSheetDialog.findViewById(R.id.delete);
        if (!isClicked){
            delete.setText("UnFollow");
        }else{
            delete.setText("Follow");
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiManager apiManager = new ApiManager(context, FollowUserListAdapter.this );
                apiManager.followingHost(userId);
                bottomSheetDialog.dismiss();

                if (!isClicked){
                    holder.tvFollowing.setText("Follow");
                    holder.tvFollowing.setTextColor(context.getResources().getColor(R.color.white_color));
                    holder.tvFollowing.setBackgroundResource(R.drawable.rounded_fans_blue);

                    isClicked = true;
                    /*if(datumList.size()==1) {
                        datumList.remove(position);
                        notifyDataSetChanged();
                    }*/
                }else{
                    holder.tvFollowing.setText("Following");
                    holder.tvFollowing.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.tvFollowing.setBackgroundResource(R.drawable.rounded_fans);
                    isClicked = false;
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    private LinearLayout toast;
//    LayoutInflater inflater = LayoutInflater.from(context);
    private void customErrorToast(String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View layout = li.inflate(R.layout.unable_to_call_lay, (ViewGroup) toast);
        TextView textView = layout.findViewById(R.id.custom_toast_message);
        textView.setText(msg);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 30);
        toast.setView(layout);
        toast.show();
    }
    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        //  private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            //    mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:
                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
            }
        }

    }

    public void add(FollowingDatum results) {
        datumList.add(results);
        notifyItemInserted(datumList.size() - 1);
    }

    public void addAll(List<FollowingDatum> moveResults) {
        for (FollowingDatum result : moveResults) {
            add(result);
        }
    }

    public void remove(UserListResponse.Result r) {
        int position = datumList.indexOf(r);
        if (position > -1) {
            datumList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAll() {
        if (datumList != null && datumList.size() > 0) {
            datumList.clear();
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new FollowingDatum());
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(datumList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = datumList.size() - 1;
        FollowingDatum result = getItem(position);

        if (result != null) {
            datumList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public FollowingDatum getItem(int position) {
        return datumList.get(position);
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

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        // chatRef.removeEventListener(valueEventListener);
        super.onViewDetachedFromWindow(holder);
    }

    public interface OnClickedOpenPopupListener{
        public void openPopup(String hostId);
    }
}




