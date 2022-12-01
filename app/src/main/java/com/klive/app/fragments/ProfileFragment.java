package com.klive.app.fragments;


import static android.app.Activity.RESULT_OK;

import static com.klive.app.main.Home.cardView;
import static com.klive.app.main.Home.unread;
import static com.klive.app.utils.SessionManager.PROFILE_ID;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.klive.app.Firestatus.FireBaseStatusManage;
import com.klive.app.OnDataReceiveCallback;
import com.klive.app.activity.AnchorPolicyActivity;
import com.klive.app.R;
import com.klive.app.activity.EditActivity;
import com.klive.app.activity.HostIncomeReportActivity;
import com.klive.app.activity.IncomeReportActivity;
import com.klive.app.activity.LevelActivity;
import com.klive.app.activity.RecordStatusActivity;
import com.klive.app.activity.SettingActivity;
import com.klive.app.activity.TradeAccountActivity;
import com.klive.app.adapter.HomeUserAdapter;
import com.klive.app.adapter.ProfileVideoAdapter;
import com.klive.app.adapter.ViewLevelAdapter;
import com.klive.app.dialogs.priceDialog;
import com.klive.app.dialogs_agency.PaymentMethod;
import com.klive.app.fudetector.ui.FUBeautyActivity;
import com.klive.app.fudetector.ui.VideoFilterMainUI;
import com.klive.app.main.Home;
import com.klive.app.model.PriceList.PriceDataModel;
import com.klive.app.model.PriceList.priceupdateModel;
import com.klive.app.model.Profile;
import com.klive.app.model.ProfileDetailsResponse;
import com.klive.app.model.UserListResponse;
import com.klive.app.model.level.Level;
import com.klive.app.model.level.LevelDataResponce;
import com.klive.app.recycler.ProfileAdapter;
import com.klive.app.response.trading_response.TradingAccountResponse;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.sqlite.Chat;
import com.klive.app.sqlite.ChatDB;
import com.klive.app.utils.Constant;
import com.klive.app.utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment implements ApiResponseInterface {
    HashMap<String, String> user;
    ImageView setting, edit, copy;
    LinearLayout bonus, center, policy, income, price, toast, editBottom, anchor_level, addAccount, settlement, tradeAccount;  //record_status
    RelativeLayout addImage, addImageRecycler;
    CircleImageView viewImage, user_profile_photo;
    TextView uid, user_profile_name, level;
    TextView PriceText;
    CircleImageView StatusDot;
    TextView Status;

    public static RecyclerView recyclerView;
    public static RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter adapter;

    private RecyclerView.LayoutManager videoLayoutManager;
    private RecyclerView rvStatusVideo;
    private ProfileVideoAdapter profileVideoAdapter;

    private List<UserListResponse.UserPics> imgList;
    private DatabaseReference chatRef;
    private int UserId;
    private String onlineofflineStatuss = "";
    private String OnlineOffileStatus;

    private TextView diamondText;
    private HomeUserAdapter levelArrayList;

    public ProfileFragment() {
        // require a empty public constructor
    }

    int SELECT_PICTURE = 200;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_REQUEST_CODE_VIDEO = 2;
    private SessionManager sessionManager;

    ArrayList<PriceDataModel> priceArrayList = new ArrayList<>();
    private int SelectedChatPrice = 0, SelectedLevel = 0;
    SharedPreferences sharedPreferences;
    LinearLayout onlineOffline;
    LinearLayout TradeAccountLay;
    ArrayList<UserListResponse.ProfileVideo> profileVideoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  ((Home)getActivity()).hideStatusBar(getActivity().getWindow(), true);
        View v = inflater.inflate(R.layout.profile_fragment, container, false);
        //getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        // getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        cardView.setVisibility(View.VISIBLE);
        imgList = new ArrayList<>();
        // mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        new ApiManager(getContext(), this).getProfileDetails();

        Log.e("CreatedFragment", "onCreateView: " + "ProfileFragment");

        /* new ApiManager(getContext(), ProfileFragment.this).getLevelData();*/

        sharedPreferences = getActivity().getSharedPreferences("VideoApp", Context.MODE_PRIVATE);

        PriceText = v.findViewById(R.id.pricettxt);

        StatusDot = v.findViewById(R.id.status_dot);

        Status = v.findViewById(R.id.status);

        onlineOffline = v.findViewById(R.id.onlineOffline);

        TradeAccountLay = v.findViewById(R.id.trade_account);

        //  priceArrayList.clear();
        priceArrayList = ((Home) getActivity()).priceDataModelArrayList;

        if (sharedPreferences.contains("SelectedPrice")) {
            SelectedChatPrice = sharedPreferences.getInt("SelectedPrice", 0);
        } else {
            SelectedChatPrice = 60;
        }

        PriceText.setText("Price: " + SelectedChatPrice + "/min");
        sessionManager = new SessionManager(getContext());
        user = new SessionManager(getContext()).getUserDetails();

        init(v);

        Log.e("userid", new SessionManager(getContext()).getUserId());

        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                SettingFragment fragment2 = new SettingFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, fragment2);
                fragmentTransaction.commit();
                */


                startActivity(new Intent(getActivity(), SettingActivity.class));


                //   new ApiManager(getContext(), ProfileFragment.this).sendChatNotification(new SessionManager(getContext()).getFcmToken(),"System","hello welcome to klive hello welcome to klive hello welcome to klive hello welcome to klive hello welcome to klive","System","","text");

                // startActivity(new Intent(getActivity(), VideoFilterMainUI.class));
                // startActivity(new Intent(getActivity(), VideoRecorderSample.class));
                //cardView.setVisibility(View.GONE);
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("id", uid.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditActivity.class));
            }
        });
        editBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditActivity.class));
            }
        });

        bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BonusFragment fragment2 = new BonusFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, fragment2);
                fragmentTransaction.commit();
                cardView.setVisibility(View.GONE);
            }
        });

        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*CenterFragment fragment2 = new CenterFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, fragment2);
                fragmentTransaction.commit();*/
                Log.e("ProfileFragment", "I am here");
                startActivity(new Intent(getContext(), HostIncomeReportActivity.class));

            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* AnchorPolicyFragment fragment2 = new AnchorPolicyFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, fragment2);
                fragmentTransaction.commit();
                cardView.setVisibility(View.GONE);*/

                startActivity(new Intent(getContext(), AnchorPolicyActivity.class));

            }
        });

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*IncomeReportActivity fragment2 = new IncomeReportActivity();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, fragment2);
                fragmentTransaction.commit();*/
                startActivity(new Intent(getContext(), IncomeReportActivity.class));
                cardView.setVisibility(View.GONE);
            }
        });

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  getPriceDataFromApi();
                // Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();

                priceDialog dialog = null;


                if (SelectedChatPrice != 0) {

                    //  Toast.makeText(getContext(),""+getSelectedPosition(priceDataModelArrayList,SelectedChatPrice),Toast.LENGTH_SHORT).show();

                    dialog = new priceDialog(getActivity(), priceArrayList, SelectedChatPrice, SelectedLevel, getSelectedPosition(priceArrayList, SelectedChatPrice), new priceDialog.SelectedPriceCallback() {
                        @Override
                        public void GetSelectedPrice(String price, priceDialog pDialog) {
                            //  Toast.makeText(getContext(),""+price,Toast.LENGTH_LONG).show();

                            new ApiManager(getContext(), ProfileFragment.this).updateCallPrice(new priceupdateModel(price));

                            SelectedChatPrice = Integer.parseInt(price.trim());

                            sharedPreferences.edit().putInt("SelectedPrice", SelectedChatPrice).apply();
                            PriceText.setText("Price: " + SelectedChatPrice + "/min");

                            //  new ApiManager(getContext(), ProfileFragment.this).getCallPriceList();
                            customToastForPrice();
                            pDialog.dismiss();

                        }
                    });
                }

            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //customToast();
                //imageChooser();
                //openGallery();
                //dispatchTakePictureIntent();
                //dispatchTakeVideoIntent();
                Log.e("List size===", profileVideoList.size() + "");
                if (profileVideoList.size() == 4) {
                    Toast.makeText(getActivity(), "You can not upload more then four videos.", Toast.LENGTH_SHORT).show();
                } else
                    startActivity(new Intent(getContext(), RecordStatusActivity.class));
            }
        });

        addImageRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        settlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PaymentMethod(getContext());
            }
        });

        anchor_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LevelActivity.class));
            }
        });

        /*record_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        return v;
    }

    private void storeStatus(String userId) {
        //chatRef = FirebaseDatabase.getInstance().getReference().child("Users");
        String uid = String.valueOf(userId);
        String name = sessionManager.getUserName();
        String fcmToken = sessionManager.getFcmToken();

        if (Status.getText().toString().equals("Online")) {
            new FireBaseStatusManage(getContext(), sessionManager.getUserId(), sessionManager.getUserName(),
                    "", "", "Offline");

            Status.setText("Offline");
            Status.setTextColor(Color.parseColor("#FFFFFF"));
            StatusDot.setImageResource(R.drawable.status_offline_symbol);

        } else {
            new FireBaseStatusManage(getContext(), sessionManager.getUserId(), sessionManager.getUserName(),
                    "", "", "Online");

            Status.setText("Online");
            Status.setTextColor(Color.parseColor("#FFFFFF"));
            StatusDot.setImageResource(R.drawable.status_symbol);
        }

       /* chatRef.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                Map<String, Object> map = null;

                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    if (snapshot.exists()) {
                        map = (Map<String, Object>) snapshot.getValue();
                        HashMap<String, String> details = new HashMap<>();
                        details.put("uid", uid);
                        details.put("name", name);
                        details.put("fcmToken", fcmToken);


                        if (map.get("status").toString().equals("Online")) {
                            details.put("status", "Offline");
                            chatRef.child(uid).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // Toast.makeText(getContext(), "" + onlineOfflineStatus, Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {

                            details.put("status", "Online");

                            chatRef.child(uid).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    //   Toast.makeText(getContext(), "" + onlineOfflineStatus, Toast.LENGTH_SHORT).show();

                                }
                            });

                        }

                        //   Toast.makeText(getContext(),"rrrr "+map.get("status").toString(),Toast.LENGTH_SHORT).show();

                    }


                }


            }
        });*/


    }


    private void getOnlineOfflineStatus(String userId, int type) {


        String uid = String.valueOf(userId);
        chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> map = null;

                if (snapshot.exists()) {
                    map = (Map<String, Object>) snapshot.getValue();
                    // Toast.makeText(getApplicationContext(),""+map.get("status"),Toast.LENGTH_LONG).show();

                    if (map.get("status").equals("Offline")) {
                        // Toast.makeText(getContext(),""+map.get("status").toString(),Toast.LENGTH_SHORT).show();
                        Status.setText(map.get("status").toString());
                        //    Status.setTextColor(getContext().getResources().getColor(R.color.white));
                        Status.setTextColor(Color.parseColor("#FFFFFF"));

                        StatusDot.setImageResource(R.drawable.status_offline_symbol);

                    }

                    if (map.get("status").equals("Online")) {
                        //  Toast.makeText(getContext(),""+map.get("status").toString(),Toast.LENGTH_SHORT).show();
                        Status.setText(map.get("status").toString());
                        // Status.setTextColor(getContext().getResources().getColor(R.color.white));
                        Status.setTextColor(Color.parseColor("#FFFFFF"));
                        StatusDot.setImageResource(R.drawable.status_symbol);
                    }


                } else {
                    //  Toast.makeText(getContext(),"not exist",Toast.LENGTH_SHORT).show();
                    // Status.setText("Offline");
                    // Status.setTextColor(getResources().getColor(R.color.white));
                    // status[0] =map.get("status").toString();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void customToastForPrice() {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.priceupdated, (ViewGroup) toast);
        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(layout);
        toast.show();
    }


    private int getSelectedPosition(ArrayList<PriceDataModel> priceDataModelArrayList, int selectedChatPrice) {

        int SelectedPosition = 0;

        for (int i = 0; i < priceDataModelArrayList.size(); i++) {
            if (priceDataModelArrayList.get(i).getAmount().equals(String.valueOf(selectedChatPrice))) {
                SelectedPosition = i;
            }


        }

        return SelectedPosition;
    }

    private void init(View v) {
        setting = v.findViewById(R.id.setting);
        edit = v.findViewById(R.id.edit);
        policy = v.findViewById(R.id.policy);
        bonus = v.findViewById(R.id.bonus2);
        center = v.findViewById(R.id.center);
        income = v.findViewById(R.id.income);
        price = v.findViewById(R.id.price);
        price.setVisibility(View.GONE);
        price.setEnabled(false);
        toast = v.findViewById(R.id.custom_toast_layout);
        copy = v.findViewById(R.id.copy);
        editBottom = v.findViewById(R.id.editBottom);
        uid = v.findViewById(R.id.tid);
        user_profile_name = v.findViewById(R.id.user_profile_name);
        level = v.findViewById(R.id.level);
        addImage = v.findViewById(R.id.addImage);
        viewImage = v.findViewById(R.id.viewImage);
        addImageRecycler = v.findViewById(R.id.addImageRecycler);
        user_profile_photo = v.findViewById(R.id.user_profile_photo);
        anchor_level = v.findViewById(R.id.anchor_level);
//        record_status = v.findViewById(R.id.record_status);
        addAccount = v.findViewById(R.id.add_account);
        settlement = v.findViewById(R.id.settlement);

        tradeAccount = v.findViewById(R.id.trade_account);

        diamondText = v.findViewById(R.id.dimaond_text);

        recyclerView = v.findViewById(R.id.images);
        rvStatusVideo = v.findViewById(R.id.rvStatusVideo);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        videoLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvStatusVideo.setLayoutManager(videoLayoutManager);

        profileVideoAdapter = new ProfileVideoAdapter(getContext(), profileVideoList);
        rvStatusVideo.setAdapter(profileVideoAdapter);


        //imgList.add(new Profile(1,  R.drawable.img_file, "yes"));
        adapter = new ProfileAdapter(getContext(), imgList);
        recyclerView.setAdapter(adapter);

        Glide.with(getActivity())
                .load(sessionManager.getUserProfilepic())
                .placeholder(R.drawable.default_profile)
                .into(user_profile_photo);
        Glide.with(getActivity())
                .load(sessionManager.getUserProfilepic())
                .placeholder(R.drawable.default_profile)
                .into(viewImage);

        user_profile_name.setText(sessionManager.getName() + ", " + sessionManager.getUserAge());
        uid.setText(user.get(PROFILE_ID));
        level.setText("Lv " + sessionManager.getUserLevel());


        //getOnlineOfflineStatus(sessionManager.getUserId(), 0);


        tradeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TradePointString = diamondText.getText().toString().replaceAll(",", "");

                Log.e("ProfileFragment", "onClick: TradingPoints " + TradePointString);

                if (!TradePointString.equals("")) {
                    startActivity(new Intent(getActivity(), TradeAccountActivity.class).putExtra("tradePoints", TradePointString));
                }

            }
        });


        onlineOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeStatus(sessionManager.getUserId());
            }
        });

        /* user_profile_name.setText(sessionManager.getName()+", "+sessionManager.getUserAge());*/
    }


    private String getFormatedAmount(int amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }


    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

       /* if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }*/

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private void openGallery() {

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        startActivityForResult(gallery, SELECT_PICTURE);

    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onResume() {
        super.onResume();
        //  ChatDB db = new ChatDB(getContext());
        //  List<Chat> peers = db.getAllPeer();
        //  if (peers.size() > 0) {
        //      int countAll = 0;
        //   //   for (Chat cn : peers) {
        //          countAll = +countAll + db.getAllChatUnreadCount(cn.get_id());
        //          if (countAll > 0) {
        //              unread.setVisibility(View.VISIBLE);
        //              unread.setText(String.valueOf(countAll));
        //          } else {
        //              unread.setVisibility(View.INVISIBLE);
        //          }
        //    //  }
        //  }

        user_profile_name.setText(sessionManager.getName() + ", " + sessionManager.getUserAge());
        new ApiManager(getContext(), this).getProfileDetails();
        new ApiManager(getContext(), this).getTradingAccount();


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    viewImage.setImageURI(selectedImageUri);
                    //String path = getImageUri(getContext(), imageBitmap).toString();
                   /* imgList.add(new Profile(2, selectedImageUri.toString(), "yes")); naval
                    adapter.notifyDataSetChanged();*/
                }
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            viewImage.setImageBitmap(imageBitmap);
            /*getImageUri(getContext(), imageBitmap);

            try {
                if(  Uri.parse(getImageUri(getContext(), imageBitmap).toString())!=null   ){
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver() , Uri.parse(getImageUri(getContext(), imageBitmap).toString()));
                }
            }
            catch (Exception e) {
                //handle exception
            }
            String path = getImageUri(getContext(), imageBitmap).toString();
            imgList.add(new Profile(2,  path, "yes"));
            adapter.notifyDataSetChanged();
           */
        }

        if (requestCode == CAMERA_REQUEST_CODE_VIDEO && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            //videoView.setVideoURI(videoUri);
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, CAMERA_REQUEST_CODE_VIDEO);
        }
    }

    private void sendVideo() {
        String vdoPath = Environment.getExternalStorageDirectory() + "/com.klive.app/myvideo.mp4";
        Uri vdoUri = Uri.parse(vdoPath);
        try {
            File vdo = new File(vdoPath);
            if (vdo.exists()) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), vdo);
                MultipartBody.Part file = MultipartBody.Part.createFormData("profile_video", vdo.getName(), requestBody);
                new ApiManager(getContext(), this).sendVideo(file);
            }

            //   new ApiManager(getContext(), this).sendVideo(vdoToUpload);
        } catch (Exception e) {
            Log.e("errorVdoFRG", e.getMessage());
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, simpleDateFormat.format(date), null);
        return Uri.parse(path);
    }

    private void customToast() {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.custom_toast, (ViewGroup) toast);
        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    public void showDialog() {
        new PaymentMethod(getContext());


    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.PROFILE_DETAILS) {
            try {
                ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;

                //String level = String.valueOf(rsp.getSuccess().getLevel());
                String userDob = rsp.getSuccess().getDob();
                String[] age = userDob.split("-");

                // Toast.makeText(getContext(),"uid "+UserId,Toast.LENGTH_SHORT).show();

                SessionManager sessionManager = new SessionManager(getContext());
                String img = "";
                if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                    img = rsp.getSuccess().getProfile_images().get(0).getImage_name();
                }
                new SessionManager(getContext()).setUserAge(getAge(Integer.parseInt(age[2]), Integer.parseInt(age[0]), Integer.parseInt(age[1])));
                new SessionManager(getContext()).setUserLevel(String.valueOf(rsp.getSuccess().getLevel()));
                new SessionManager(getContext()).setBio(String.valueOf(rsp.getSuccess().getAbout_user()));
                new SessionManager(getContext()).setUserAddress(String.valueOf(rsp.getSuccess().getCity()));
                new SessionManager(getContext()).setUserDob(String.valueOf(rsp.getSuccess().getDob()));
                new SessionManager(getContext()).setLanguage(String.valueOf(rsp.getSuccess().getUser_languages().get(0).getLanguage_name()));
                new SessionManager(getContext()).setUserProfilepic(img);

                Log.e("ProfileFragment", "isSuccess: level " + String.valueOf(rsp.getSuccess().getLevel()));

                Glide.with(getActivity())
                        .load(img)
                        .placeholder(R.drawable.default_profile)
                        .into(user_profile_photo);
                Glide.with(getActivity())
                        .load(img)
                        .placeholder(R.drawable.default_profile)
                        .into(viewImage);

                level.setText("Lv " + sessionManager.getUserLevel());
                user_profile_name.setText(sessionManager.getName() + ", " + sessionManager.getUserAge());
                profileVideoList.clear();
                Log.e("status video List==", rsp.getSuccess().getProfileVideo().size() + "");
                profileVideoList.addAll(rsp.getSuccess().getProfileVideo());
                profileVideoAdapter.notifyDataSetChanged();

                imgList.clear();
                imgList.addAll(rsp.getSuccess().getProfile_images());
                adapter.notifyDataSetChanged();

            } catch (Exception e) {

            }
        }


        if (ServiceCode == Constant.CALL_PRICE_LIST) {
            //  priceDataModelArrayList.clear();
            //  PriceListResponse rsp = (PriceListResponse) response;
            //  priceDataModelArrayList.addAll(rsp.getResult());
            //  SelectedChatPrice=rsp.getCall_rate();
            //  SelectedLevel=rsp.getLevel();
            //  PriceText.setText("Price: "+SelectedChatPrice+"/min");
            //  Log.i("size_price",""+priceDataModelArrayList.size());
            //   Toast.makeText(getContext(),"size_price    "+priceDataModelArrayList.get(0).getAmount(),Toast.LENGTH_SHORT).show();
        }
        if (ServiceCode == Constant.GET_TRADING_ACCOUNT) {
            TradingAccountResponse rsp = (TradingAccountResponse) response;
            if (rsp.getResult().getIsTradeAccount() == 1) {
                TradeAccountLay.setVisibility(View.VISIBLE);
                diamondText.setText(getFormatedAmount(rsp.getResult().getTotalPonts()));
            } else if (rsp.getResult().getIsTradeAccount() == 0) {
                TradeAccountLay.setVisibility(View.GONE);
                diamondText.setText(getFormatedAmount(rsp.getResult().getTotalPonts()));
            }

        }

    /*    if (ServiceCode == Constant.GET_LEVEL_DATA) {
            LevelDataResponce rsp = (LevelDataResponce) response;

            Log.e("ProfileFragment", "isSuccess:  userLivelDATA "+rsp.getResult().getLevel() );

            levelArrayList.addAll(rsp.getResult().getLevel());


        }
        */

    }


}