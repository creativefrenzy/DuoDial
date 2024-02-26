package com.privatepe.app.adapter.metend;


import static com.privatepe.app.utils.Constant.GET_VIDEO_STATUS_LIST;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.privatepe.app.R;
import com.privatepe.app.Swipe.OnSwipeTouchListener;
import com.privatepe.app.Swipe.TouchListener;
import com.privatepe.app.activity.ViewProfileMet;
import com.privatepe.app.fragments.metend.HomeFragmentMet;
import com.privatepe.app.fragments.metend.NearbyFragmentMet;
import com.privatepe.app.response.NewVideoStatus.NewVideoStatusResponse;
import com.privatepe.app.response.NewVideoStatus.NewVideoStatusResult;
import com.privatepe.app.response.metend.AdapterRes.UserListResponseMet;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.status_videos.ActivityStatus;
import com.privatepe.app.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class HomeUserAdapterMet extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ApiResponseInterface {

    Context context;
    List<UserListResponseMet.Data> list;
    String type;
    HomeFragmentMet homeFragment;
    NearbyFragmentMet nearbyFragment;
    // NearByListFragment nearByListFragment;
    //SearchFragment searchFragment;

    private PaginationAdapterCallback mCallback;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    DatabaseReference chatRef;
    private ValueEventListener valueEventListener;


    private ApiManager apiManager;


    private int CURRENT_POS = -1;

    public HomeUserAdapterMet(Context context, PaginationAdapterCallback mCallback, String type, HomeFragmentMet homeFragment) {
        this.context = context;
        this.mCallback = mCallback;
        this.list = new ArrayList<>();
        this.type = type;
        this.homeFragment = homeFragment;
        apiManager = new ApiManager(context, this);
    }

    public HomeUserAdapterMet(Context context, PaginationAdapterCallback mCallback, String type, NearbyFragmentMet nearbyFragment) {
        this.context = context;
        this.mCallback = mCallback;
        this.list = new ArrayList<>();
        this.type = type;
        this.nearbyFragment = nearbyFragment;
        apiManager = new ApiManager(context, this);
    }

   /*      public HomeUserAdapter(Context context, PaginationAdapterCallback mCallback, String type, NearByListFragment nearByListFragment) {
        this.context = context;
        this.mCallback = mCallback;
        this.list = new ArrayList<>();
        this.type = type;
        this.nearByListFragment = nearByListFragment;
}*/

   /* public HomeUserAdapterMet(Context context, PaginationAdapterCallback mCallback, String type, SearchFragment homeFragment) {
        this.context = context;
        this.mCallback = mCallback;
        this.list = new ArrayList<>();
        this.type = type;
        this.searchFragment = homeFragment;
        apiManager = new ApiManager(context, this);
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View v1 = null;
                if (type.equals("dashboard")) {
                    v1 = inflater.inflate(R.layout.adapter_user_gridview_met, parent, false);
                } else if (type.equals("search")) {
                    v1 = inflater.inflate(R.layout.adapter_search, parent, false);
                } else if (type.equals("nearlist")) {
                    v1 = inflater.inflate(R.layout.nearby_item, parent, false);
                }
                viewHolder = new myViewHolder(v1);
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
    public void onBindViewHolder(RecyclerView.ViewHolder hld, @SuppressLint("RecyclerView") int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                try {
                    final myViewHolder holder = (myViewHolder) hld;

                    if (type.equals("nearlist")) {
                      /*  if (!list.get(position).getProfile_images().get(0).getImage_name().equals("")) {
                            if (type.equals("nearlist")) {
                                if (list.get(position).getProfile_images().size() > 0) {
                                    Glide.with(context).load(list.get(position).getProfile_images().get(0).getImage_name())
                                            .apply(new RequestOptions().placeholder(R.drawable.default_profile).error
                                                    (R.drawable.default_profile).circleCrop()).into(holder.user_image);
                                } else {
                                    Glide.with(context).load(R.drawable.default_profile).apply(new RequestOptions()).into(holder.user_image);
                                }

                            }
                        }*/


                        if (!list.get(position).getProfile_image().equals("")) {
                           /* Glide.with(context).load(list.get(position).getProfile_image())
                                    .apply(new RequestOptions().placeholder(R.drawable.default_profile).error
                                            (R.drawable.female_placeholder).circleCrop()).into(holder.user_image);*/

                            Glide.with(context).load(list.get(position).getProfile_image())
                                    .apply(new RequestOptions().placeholder(R.drawable.default_profile).error
                                            (R.drawable.default_profile).circleCrop()).into(holder.user_image);

                        } else {
                            Glide.with(context).load(R.drawable.default_profile).apply(new RequestOptions()).into(holder.user_image);
                        }

                        holder.user_name.setText(list.get(position).getName());
                        holder.about_user.setText(list.get(position).getAbout_user());
                        holder.distance.setText(list.get(position).getDistance() + "km");

                  /*
                        holder.img_video_call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("CallProcess", "call button Clicked");

                                //   new AppLifecycle().InitiateCall(String.valueOf(list.get(position).getProfile_id()), "Video Call", "");

                                try {
                                    nearByListFragment.startVideoCall(String.valueOf(list.get(position).getProfile_id()),
                                            String.valueOf(list.get(position).getCall_rate()),
                                            list.get(position).getId(),
                                            list.get(position).getName(),
                                            list.get(position).getProfile_images().get(0).getImage_name());
                                } catch (Exception e) {
                                    nearByListFragment.startVideoCall(String.valueOf(list.get(position).getProfile_id()),
                                            String.valueOf(list.get(position).getCall_rate()),
                                            list.get(position).getId(),
                                            list.get(position).getName(),
                                            list.get(position).getProfile_images().get(0).getImage_name());
                                }

                            }
                        });
                        */


                    } else {
                        /*if (!list.get(position).getProfile_image().equals("")) {*/
                           /* if (type.equals("search")) {
                                if (list.get(position).getProfile_images().size() > 0) {

                                } else {
                                    Glide.with(context).load(R.drawable.default_profile).apply(new RequestOptions()).into(holder.user_image);
                                }

                            } else {
                                if (list.get(position).getProfile_images().size() > 0) {
                                    Glide.with(context).load(list.get(position).getProfile_images().get(0).getImage_name())
                                            .apply(new RequestOptions().placeholder(R.drawable.female_placeholder).
                                                    error(R.drawable.female_placeholder)).into(holder.user_image);
                                } else {
                                    Glide.with(context).load(R.drawable.female_placeholder).apply(new RequestOptions()).into(holder.user_image);
                                }
                            }*/


                        if (type.equals("search")) {
                            if (!list.get(position).getProfile_image().equals("")) {

                                Glide.with(context).load(list.get(position).getProfile_image())
                                        .apply(new RequestOptions().placeholder(R.drawable.default_profile).error
                                                (R.drawable.default_profile).circleCrop()).into(holder.user_image);
                            } else {

                                Glide.with(context).load(R.drawable.default_profile).apply(new RequestOptions()).into(holder.user_image);
                            }

                        } else {
                            if (list.get(position).getProfile_image() != null && !list.get(position).getProfile_image().equals("")) {
                                Glide.with(context).load(list.get(position).getProfile_image())
                                        .apply(new RequestOptions().placeholder(R.drawable.female_placeholder).
                                                error(R.drawable.female_placeholder)).into(holder.user_image);
                            } else {
                                Glide.with(context).load(R.drawable.female_placeholder).apply(new RequestOptions()).into(holder.user_image);

                            }

                        }

                        Log.e("NewUserlistResp", "onBindViewHolder: " + list.get(position).getCall_rate());

                        String callRateString = "\u20B9" + list.get(position).getCall_price() + "/min";

                        holder.callrate_text.setText(callRateString);
/*
                        } else {
                            Glide.with(context).load(R.drawable.female_placeholder).apply(new RequestOptions()).into(holder.user_image);
                        }*/
                        //holder.total_flash.setText(String.valueOf(list.get(position).getFavorite_count()));
                        holder.total_flash.setText(String.valueOf(list.get(position).getTotal_coins()));
                        Log.e("ToatalCoin", "TotalCoin " + list.get(position).getTotal_coins());


                        String tempUserName = list.get(position).getName();

                        if (tempUserName.length() > 11) {
                            String userName = tempUserName.substring(0, 10) + "...";
                            holder.user_name.setText(userName);
                        } else {
                            holder.user_name.setText(tempUserName);
                        }


                        holder.id.setText("" + list.get(position).getProfile_id());

                        Log.e("HomeUserAdapter", "onBindViewHolder: " + list.get(position).getName());


                        holder.about_user.setText(list.get(position).getAbout_user());
                        holder.countryDisplay.setText(list.get(position).getCity());

                       /* if (list.get(position).getProfileVideo().size() > 0) {
                            holder.realVideoIV.setVisibility(View.VISIBLE);
                        } else {
                            holder.realVideoIV.setVisibility(View.INVISIBLE);
                        }*/


                        if (list.get(position).getProfile_video() == 1) {
                            //holder.realVideoIV.setVisibility(View.VISIBLE);
                        } else if (list.get(position).getProfile_video() == 0) {
                            //holder.realVideoIV.setVisibility(View.INVISIBLE);
                        }

                        addfblistener(position, holder);

                        /*

                        if (list.get(position).getIs_busy() == 0) {
                            if (list.get(position).getIs_online() == 1) {
                                holder.is_online.setText("Online");
                                holder.is_online.setBackgroundResource(R.drawable.rounded_corner_tranparent_black);
                                // holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_green, 0, 0, 0);
                            } else {
                                holder.is_online.setText("Offline");
                                holder.is_online.setBackgroundResource(R.drawable.viewprofile_offline_background);
                                //holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_grey, 0, 0, 0);
                            }
                        } else {
                            holder.is_online.setText("Busy");
                            holder.is_online.setBackgroundResource(R.drawable.viewprofile_busybackground);
                            //holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_orange, 0, 0, 0);
                        }
*/

                        holder.img_video_call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Log.e("CallProcess", " HomeUserAdapter call button Clicked");

                                //new AppLifecycle().InitiateCall(String.valueOf(list.get(position).getProfile_id()), "Video Call", "");

                                try {
                                    Log.e("CallProcess1", " HomeUserAdapter call button Clicked try");
                                    Log.e("STARTVIDEOCALL_NEARBY", "startVideoCall: homefragment123 " + list.get(position).getProfile_id() +" "+list.get(position).getId());

                                    homeFragment.startVideoCall(String.valueOf(list.get(position).getProfile_id()),
                                            String.valueOf(list.get(position).getCall_price()),
                                            list.get(position).getId(),
                                            list.get(position).getName(),
                                            list.get(position).getProfile_image());
                                } catch (Exception e) {
                                    Log.e("CallProcess1", " HomeUserAdapter call button Clicked catch");
                                    nearbyFragment.startVideoCall(String.valueOf(list.get(position).getProfile_id()),
                                            String.valueOf(list.get(position).getCall_price()),
                                            list.get(position).getId(),
                                            list.get(position).getName(),
                                            list.get(position).getProfile_image());
                                }

                                holder.img_video_call.setEnabled(false);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.img_video_call.setEnabled(true);
                                    }
                                }, 2000);

                            }
                        });
                    }
                    holder.user_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                        }
                    });


                    holder.user_image.setOnTouchListener(new OnSwipeTouchListener(context, new TouchListener() {
                        @Override
                        public void onSingleTap() {
                            Log.e("TAG", ">> Single tap");
                            try {

                                // Log.e("onSingleTap11", "onSingleTap: videolist size " + (list.get(position).getProfileVideo().size()));

                                Log.e("activitysss", "onSingleTap: user id  adapter" + list.get(position).getId());


                                if (list.get(position).getProfile_video() == 1) {
                                    Log.e("HomeUserAdapter", "onSingleTap: have video status");
                                    CURRENT_POS = position;
                                    //apiManager.getStatusVideosList(String.valueOf(list.get(position).getId()));
                                    Log.e("HomeUserAdapter", "onSingleTap: dont have video status");
                                    Intent intent = new Intent(context, ViewProfileMet.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("id", list.get(position).getId());
                                    bundle.putSerializable("profileId", list.get(position).getProfile_id());
                                    bundle.putSerializable("level", list.get(position).getLevel());
                                    Log.e("VIEW_PROFILE_TEST", "onSingleTap: id  " + list.get(position).getId() + " profileId  " + list.get(position).getProfile_id() + " level  " + list.get(position).getLevel());
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);

                                } else if (list.get(position).getProfile_video() == 0) {
                                    Log.e("HomeUserAdapter", "onSingleTap: dont have video status");
                                    Intent intent = new Intent(context, ViewProfileMet.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("id", list.get(position).getId());
                                    bundle.putSerializable("profileId", list.get(position).getProfile_id());
                                    bundle.putSerializable("level", list.get(position).getLevel());
                                    Log.e("VIEW_PROFILE_TEST", "onSingleTap: id  " + list.get(position).getId() + " profileId  " + list.get(position).getProfile_id() + " level  " + list.get(position).getLevel());
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);

                                }


                            /*    if (list.get(position).getProfileVideo().size() > 0) {

                                    Intent intent = new Intent(context, ActivityStatus.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("name", list.get(position).getName());
                                    intent.putExtra("id", String.valueOf(list.get(position).getId()));
                                    intent.putExtra("profileId", String.valueOf(list.get(position).getProfile_id()));
                                    intent.putExtra("level", String.valueOf(list.get(position).getLevel()));
                                    intent.putExtra("location", String.valueOf(list.get(position).getCity()));
                                    intent.putExtra("callrate", list.get(position).getCall_rate());
                                    intent.putExtra("videonum", "");

                                    intent.putExtra("profile_pic", list.get(position).getProfile_image());

                                *//*    if (list.get(position).getProfile_images().size() > 0) {
                                        intent.putExtra("profile_pic", list.get(position).getProfile_images().get(0).getImage_name());
                                    } else {
                                        intent.putExtra("profile_pic", "");
                                    }*//*

                                    try {
                                        String[] dob = list.get(position).getDob().split("-");
                                        int date = Integer.parseInt(dob[0]);
                                        int month = Integer.parseInt(dob[1]);
                                        int year = Integer.parseInt(dob[2]);
                                        intent.putExtra("age", String.valueOf(getAge(year, month, date)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    ArrayList<String> list2 = getVideolinksList(list.get(position));
                                    Log.e("onSingleTap11", "onSingleTap: " + "list size  " + list2);
                                    intent.putStringArrayListExtra("resoureList", list2);
                                    context.startActivity(intent);

                                    Log.e("VIEW_PROFILE_TEST", "onSingleTap: id  " + list.get(position).getId() + " profileId  " + list.get(position).getProfile_id() + " level  " + list.get(position).getLevel());

                                } else {


                                    Intent intent = new Intent(context, ViewProfile.class);
                                    Bundle bundle = new Bundle();
                                    //hide intent sending data homefragment to ViewprofileActivity...
                                    //bundle.putSerializable("user_data", list.get(position));
                                    bundle.putSerializable("id", list.get(position).getId());
                                    bundle.putSerializable("profileId", list.get(position).getProfile_id());
                                    bundle.putSerializable("level", list.get(position).getLevel());


                                    Log.e("VIEW_PROFILE_TEST", "onSingleTap: id  " + list.get(position).getId() + " profileId  " + list.get(position).getProfile_id() + " level  " + list.get(position).getLevel());

                                    intent.putExtras(bundle);

                                    context.startActivity(intent);
                                }
*/

                                // intent.putStringArrayListExtra("resoureList", (ArrayList<String>) list.get(position).getVideoLinkLists());


                            } catch (Exception e) {
                                Log.e("onSingleTap11", "onSingleTap: Exception " + e.getMessage());
                            }
                        }

                        @Override
                        public void onDoubleTap() {
                            Log.e("TAG", ">> Double tap");
                        }

                        @Override
                        public void onLongPress() {
                            Log.e("TAG", ">> Long press");
                        }

                        @Override
                        public void onSwipeLeft() {
                            Log.e("TAG", ">> Swipe left");
                        }

                        @Override
                        public void onSwipeRight() {
                            Log.e("TAG", ">> Swipe right");

                        }
                    }));

                    try {
                        String[] dob = list.get(position).getDob().split("-");
                        int date = Integer.parseInt(dob[0]);
                        int month = Integer.parseInt(dob[1]);
                        int year = Integer.parseInt(dob[2]);
                        holder.user_age.setText(getAge(year, month, date));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

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

    private ArrayList<String> getVideolinksList(UserListResponseMet.Data data) {

        ArrayList<String> videolist = new ArrayList<>();
        videolist.clear();

      /*  for (int i = 0; i < data.getProfileVideo().size(); i++) {
            videolist.add(data.getProfileVideo().get(i).getVideoUrl());
        }*/
        return videolist;
    }

    private void fbstatuslistener(String profileId, myViewHolder holder) {

        //chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(String.valueOf(672206762));

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> map = null;
                if (snapshot.exists()) {
                    map = (Map<String, Object>) snapshot.getValue();

                    // Log.e("HomeUserAdapterFB44", "onDataChange: "+map.toString() );


                                    Log.e("HomeUserAdapterFB", "onDataChange: status " + map.get("status") + "  name " + map.get("name"));

                                    Log.e("HomeUserAdapterFB11", "onDataChange: uid " + map.get("uid").toString() /*+ "   getProfile_id   " +profileId*/);


                    if (map.get("uid").toString().equals(holder.id.getText().toString())) {

                        if (map.get("status").toString().equalsIgnoreCase("Online") || map.get("status").toString().equalsIgnoreCase("lLive")) {

                            holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_green, 0, 0, 0);
                            holder.is_online.setTextColor(context.getColor(R.color.white));
                            holder.is_online.setText("Online");
                            holder.is_online.setPadding(8, 4, 18, 4);
                            holder.is_online.setBackgroundResource(R.drawable.rounded_corner_tranparent_black);

                        } else if (map.get("status").toString().equalsIgnoreCase("Live")) {

                            holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_green, 0, 0, 0);
                            holder.is_online.setTextColor(context.getColor(R.color.white));
                            holder.is_online.setText("Online");
                            holder.is_online.setPadding(8, 4, 18, 4);
                            holder.is_online.setBackgroundResource(R.drawable.viewprofile_busybackground);

                        }

                        else if (map.get("status").toString().equalsIgnoreCase("Busy")) {

                            holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_green, 0, 0, 0);
                            holder.is_online.setTextColor(context.getColor(R.color.white));
                            holder.is_online.setText("Busy");
                            holder.is_online.setPadding(8, 4, 18, 4);
                            holder.is_online.setBackgroundResource(R.drawable.viewprofile_busybackground);

                        } else if (map.get("status").toString().equalsIgnoreCase("Offline")) {

                            holder.is_online.setText("Offline");
                            holder.is_online.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            holder.is_online.setTextColor(context.getColor(R.color.white));
                            holder.is_online.setPadding(18, 4, 18, 4);
                            holder.is_online.setBackgroundResource(R.drawable.viewprofile_offline_background);

                        }

                    }


                    /* if (map.get("uid").toString().equals(String.valueOf(list.get(position).getProfile_id()))) { }*/

                } else {
                    Log.e("HomeUserAdapterFB", "onDataChange: " + "data not exist");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


    }
    private HashSet<Integer> positionSet=new HashSet<>();
    private HashSet<Integer> profileListening=new HashSet<>();
    public void addfblistener(int pos, myViewHolder holder){
      //  Log.e("checkthepositlis1","PosiSet "+pos);

        if(!positionSet.contains(pos)/* && currentScrollPos==pos*/){
            positionSet.add(pos);
            chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(String.valueOf(list.get(pos).getProfile_id()));
            fbstatuslistener(String.valueOf(list.get(pos).getProfile_id()),holder);
            chatRef.addValueEventListener(valueEventListener);
            Log.e("checkthepositlis","Add "+pos);
            if(pos-11>=0) {
                positionSet.remove(pos - 11);
                removefblistener(pos - 11);
            }
            if(pos+11<=getItemCount()) {
                positionSet.remove(pos + 11);
                removefblistener(pos + 11);
            }
        }else {



        }


    }
    public void currentScrollPos(int pos){
       if(currentScrollPos!=pos) {
           this.currentScrollPos = pos;
         //  Log.e("checkthepositlis1", "current " + pos);
       }


    }
    private int currentScrollPos=0;
    public void removefblistener(int pos){

                chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(String.valueOf(list.get(pos).getProfile_id()));
                //fbstatuslistener(profileId,holder);
                chatRef.removeEventListener(valueEventListener);
                Log.e("checkthepositlis","Remove "+pos);




    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month - 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        int ageInt = age;
        return Integer.toString(ageInt);
    }


    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image, img_video_call, realVideoIV;
        TextView total_flash, user_name, user_age, about_user, is_online, countryDisplay, distance;
        RelativeLayout container;
        TextView id, callrate_text;

        public myViewHolder(View itemView) {
            super(itemView);
            distance = itemView.findViewById(R.id.distance);
            container = itemView.findViewById(R.id.container);
            total_flash = itemView.findViewById(R.id.total_flash);
            user_age = itemView.findViewById(R.id.user_age);
            user_image = itemView.findViewById(R.id.user_image);
            img_video_call = itemView.findViewById(R.id.img_video_call);
            user_name = itemView.findViewById(R.id.user_name);
            about_user = itemView.findViewById(R.id.about_user);
            is_online = itemView.findViewById(R.id.is_online);
            countryDisplay = itemView.findViewById(R.id.countryDisplay);
            realVideoIV = itemView.findViewById(R.id.realvideoIV);
            callrate_text = itemView.findViewById(R.id.callrate_text);
            id = itemView.findViewById(R.id.textid);
        }
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

           /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(UserListResponseMet.Data results) {
        list.add(results);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<UserListResponseMet.Data> moveResults) {
        for (UserListResponseMet.Data result : moveResults) {
            add(result);
        }
    }

    public void updateItem(int position, UserListResponseMet.Data data) {
        list.set(position, data);
        notifyItemChanged(position);
    }

    public void remove(UserListResponseMet.Result r) {
        int position = list.indexOf(r);
        if (position > -1) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAll() {
        if (list != null && list.size() > 0) {
            list.clear();
        }
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new UserListResponseMet.Data());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = list.size() - 1;
        UserListResponseMet.Data result = getItem(position);

        if (result != null) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(list.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public UserListResponseMet.Data getItem(int position) {
        return list.get(position);
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        // chatRef.removeEventListener(valueEventListener);
        super.onViewDetachedFromWindow(holder);
    }


    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == GET_VIDEO_STATUS_LIST) {

            NewVideoStatusResponse rsp = (NewVideoStatusResponse) response;
            List<NewVideoStatusResult> resultlist = rsp.getResult();

            int position = CURRENT_POS;

            //  Log.e("GET_VIDEO_STATUS_LIST1", "isSuccess: " + new Gson().toJson(rsp) + " CurrentPos " + CURRENT_POS);

            Log.e("GET_VIDEO_STATUS_LIST1", "isSuccess: result: " + new Gson().toJson(resultlist) + " \n ");
            Log.e("GET_VIDEO_STATUS_LIST1", "isSuccess: result- " + getVideoStatuslinksList(resultlist) + " \n ");

            if (position != -1) {
                Log.e("GET_VIDEO_STATUS_LIST1", "isSuccess: position " + position + "\n");

                try {
                    Log.e("GET_VIDEO_STATUS_LIST1", "isSuccess: try ");

                    Intent intent = new Intent(context, ActivityStatus.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("name", list.get(position).getName());
                    intent.putExtra("id", String.valueOf(list.get(position).getId()));
                    intent.putExtra("profileId", String.valueOf(list.get(position).getProfile_id()));
                    intent.putExtra("level", String.valueOf(list.get(position).getLevel()));
                    intent.putExtra("location", String.valueOf(list.get(position).getCity()));
                    intent.putExtra("callrate", list.get(position).getCall_price());
                    intent.putExtra("videonum", "");
                    intent.putExtra("profile_pic", list.get(position).getProfile_image());

                    try {
                        String[] dob = list.get(position).getDob().split("-");
                        int date = Integer.parseInt(dob[0]);
                        int month = Integer.parseInt(dob[1]);
                        int year = Integer.parseInt(dob[2]);
                        intent.putExtra("age", String.valueOf(getAge(year, month, date)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ArrayList<String> videoStatusList = getVideoStatuslinksList(resultlist);

                    //  ArrayList<String> videoStatusList=getVideosListNew(rsp);

                    intent.putStringArrayListExtra("resoureList", videoStatusList);
                    intent.putStringArrayListExtra("thumbnailList", getThumbnailList(resultlist));
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.e("GET_VIDEO_STATUS_LIST1", "isSuccess: catch Exception  " + e.getMessage());

                }

            }

        }

    }


    private ArrayList<String> getVideoStatuslinksList(List<NewVideoStatusResult> data) {

        ArrayList<String> videolist = new ArrayList<>();
        videolist.clear();
        for (int i = 0; i < data.size(); i++) {
            videolist.add(data.get(i).getVideo_name());
        }

        return videolist;
    }

    private ArrayList<String> getThumbnailList(List<NewVideoStatusResult> data) {
        ArrayList<String> thumbnailList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            thumbnailList.add(data.get(i).getVideo_thumbnail());
        }
        return thumbnailList;
    }


/*
    private ArrayList<String> getVideosListNew(NewVideoStatusResponse data)
    {
        ArrayList<String> videolist = new ArrayList<>();
        videolist.clear();
        for (int i = 0; i < videolist.size(); i++) {
            videolist.add(data.getResult().get(i).getVideo_name());
        }
        return videolist;
    }*/

}